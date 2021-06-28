import notion.api.v1.NotionClient;
import notion.api.v1.model.databases.Database;
import notion.api.v1.model.databases.QueryResults;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageProperty;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.fusesource.jansi.Ansi.ansi;


public class Dictionary {

    public static String getText(String columnName, Map<String, PageProperty> propertyMap) throws Exception {
        try {
            if (propertyMap.containsKey(columnName)) {
                return switch (Objects.requireNonNull(propertyMap.get(columnName).getType())) {
                    case RichText -> propertyMap.get(columnName).getRichText().get(0).getPlainText();
                    case Title -> propertyMap.get(columnName).getTitle().get(0).getPlainText();
                    default -> null;
                };
            } else {
                throw new Exception("Property not found!");
            }
        } catch (IndexOutOfBoundsException e) {
            return " ";
        }
    }

    public static HashMap<String, String> createDictionary(NotionClient notionClient)  {
        HashMap<String, String> dictionary = new HashMap<String, String>();
        List<Database> databasesList = notionClient.listDatabases().getResults();
        Database database = UserInfo.askDatabaseIDReturnDB(databasesList);

        assert database != null;
        String frontColumn = UserInfo.askFirstColumnName();
        String backColumn = UserInfo.askSecondColumnName();

        QueryResults resultQuery = notionClient.queryDatabase(database.getId(), null, null, null, 100);
        Page[] results = resultQuery.getResults().toArray(Page[]::new);
        Page[] pages = results;

        while (resultQuery.getHasMore()) {
            resultQuery = notionClient.queryDatabase(database.getId(), null, null, resultQuery.getNextCursor(), 100);
            results = resultQuery.getResults().toArray(Page[]::new);
            pages = ArrayUtils.addAll(pages, results);
        }
        try{
        for (Page page : pages) {
            Map<String, PageProperty> propertyMap = page.getProperties();
            String frontText = getText(frontColumn, propertyMap);
            String backText = getText(backColumn, propertyMap);
            dictionary.put(frontText, backText);
        } } catch (Exception e) {
            System.out.println(ansi().render(AnsiColors.ANSI_RED + e.getMessage() + AnsiColors.ANSI_RESET));
            createDictionary(notionClient);
        }
        return dictionary;
    }
}
