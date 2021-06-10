import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import notion.api.v1.NotionClient;
import notion.api.v1.model.databases.Database;
import notion.api.v1.model.databases.QueryResults;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageProperty;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {
    //здесь будет генериться словарь
    static NotionClient notionClient;

    public Dictionary(NotionClient notionClient) {
        this.notionClient = notionClient;
    }

    public HashMap<String, String> createDictionary() throws IOException {
        HashMap<String, String> dictionary = new HashMap<String, String>();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        List<Database> databasesList = Dictionary.notionClient.listDatabases().getResults();
        Database database = UserInfo.askDatabaseIDIfNeededReturnDB(databasesList);

        assert database != null;
        String frontColumn = UserInfo.askFirstColumnName();
        String backColumn = UserInfo.askSecondColumnName();

        QueryResults resultQuery = Dictionary.notionClient.queryDatabase(database.getId(), null, null, null, 100);
        Page[] results = resultQuery.getResults().toArray(Page[]::new);
        Page[] pages = results;

        while (resultQuery.getHasMore()) {
            resultQuery = Dictionary.notionClient.queryDatabase(database.getId(), null, null, resultQuery.getNextCursor(), 100);
            results = resultQuery.getResults().toArray(Page[]::new);
            pages = ArrayUtils.addAll(pages, results);
        }
        for (Page page : pages) {
            Map<String, PageProperty> propertyMap = page.getProperties();
            String front = null;
            String back = null;
            //вынести в отдельный метод!
           switch (propertyMap.get(frontColumn).getType()) {
               case RichText:
               front= propertyMap.get(frontColumn).getRichText().get(0).getPlainText();
               break;
               case Title:
               front = propertyMap.get(frontColumn).getTitle().get(0).getPlainText();
               break;
           }
            switch (propertyMap.get(backColumn).getType()) {
                case RichText:
                  back =  propertyMap.get(backColumn).getRichText().get(0).getPlainText();
                  break;
                case Title:
                  back =  propertyMap.get(backColumn).getTitle().get(0).getPlainText();
                  break;
            }
            dictionary.put(front,back);
        }
        return dictionary;
    }
}
