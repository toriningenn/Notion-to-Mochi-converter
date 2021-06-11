
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import notion.api.v1.NotionClient;
import notion.api.v1.http.OkHttp4Client;
import notion.api.v1.logging.Slf4jLogger;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

      NotionClient notionClient = new NotionClient(UserInfo.askToken(),
             new OkHttp4Client(),
               new Slf4jLogger());

       MochiService.createCards(notionClient);

    }
}
