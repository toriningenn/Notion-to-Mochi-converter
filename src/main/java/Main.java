
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import notion.api.v1.NotionClient;
import notion.api.v1.http.OkHttp4Client;
import notion.api.v1.logging.Slf4jLogger;
import notion.api.v1.model.databases.Database;
import notion.api.v1.model.databases.DatabaseProperty;
import notion.api.v1.model.databases.QueryResults;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        NotionClient notionClient = new NotionClient(UserInfo.askToken(),
                new OkHttp4Client(),
                new Slf4jLogger());
        List<Database> databasesList = notionClient.listDatabases().getResults();
        Database database = UserInfo.askDatabaseIDIfNeededReturnDB(databasesList);

        // System.out.print("First column title(front of the card):");
        //   String firstColumnTitle = sc.nextLine();
        //  System.out.print("Second column title(back of the card):");
        //  String secondColumnTitle = sc.nextLine();
//возможно нам нужны будут из id
        assert database != null;
        // DatabaseProperty firstProperty = database.getProperties().get(firstColumnTitle);
        // DatabaseProperty secondProperty = database.getProperties().get(secondColumnTitle);

        Object[] words = new Object[0];
        HashMap<String, String> dictionary = new HashMap<String, String>();
        //потом сделать одним методом, который будет возвращать булиан в зависимости от resultQuery.getHasMore()
        QueryResults resultQuery = notionClient.queryDatabase(id, null, null, null, 100);
        Object[] results = resultQuery.getResults().toArray();
        words = ArrayUtils.addAll(words, results);

        while (resultQuery.getHasMore()) {
            resultQuery = notionClient.queryDatabase(id, null, null, resultQuery.getNextCursor(), 100);
            results = resultQuery.getResults().toArray();
            words = ArrayUtils.addAll(words, results);
            for (Object word : words) {

                dictionary.put();
            }
        }
        System.out.println(ow.writeValueAsString(words));
    }
}
