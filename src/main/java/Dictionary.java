import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import notion.api.v1.NotionClient;
import notion.api.v1.endpoint.DatabasesSupport;
import notion.api.v1.model.databases.Database;
import notion.api.v1.model.databases.QueryResults;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageProperty;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Dictionary {

    public static String getText(String columnName, Map<String, PageProperty> propertyMap) {
        return switch (Objects.requireNonNull(propertyMap.get(columnName).getType())) {
            case RichText -> propertyMap.get(columnName).getRichText().get(0).getPlainText();
            case Title -> propertyMap.get(columnName).getTitle().get(0).getPlainText();
            default -> null;
        };
    }

    public static HashMap<String, String> createDictionary(NotionClient notionClient) throws IOException {
        HashMap<String, String> dictionary = new HashMap<String, String>();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        List<Database> databasesList = notionClient.listDatabases().getResults();
        Database database = UserInfo.askDatabaseIDIfNeededReturnDB(databasesList);

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
        for (Page page : pages) {
            Map<String, PageProperty> propertyMap = page.getProperties();
            String frontText = getText(frontColumn, propertyMap);
            String backText = getText(backColumn, propertyMap);
            //вынести в отдельный метод!
            dictionary.put(frontText,backText);
        }
        return dictionary;
    }

}
