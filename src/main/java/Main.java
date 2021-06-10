
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

       // NotionClient notionClient = new NotionClient(UserInfo.askToken(),
        //        new OkHttp4Client(),
         //       new Slf4jLogger());

        //    Dictionary dictionary = new Dictionary(notionClient);
        //     System.out.println(ow.writeValueAsString(dictionary.createDictionary()));
        System.out.println(ow.writeValueAsString(MochiRequests.login()));
    }
}
