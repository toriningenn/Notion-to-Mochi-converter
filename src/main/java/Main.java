import notion.api.v1.NotionClient;
import notion.api.v1.exception.NotionAPIError;
import notion.api.v1.http.OkHttp4Client;
import notion.api.v1.logging.Slf4jLogger;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.ansi;


public class Main {
    public static void main(String[] args) {
            AnsiConsole.systemInstall();
            try {
                NotionClient notionClient = new NotionClient(UserInfo.askToken(),
                        new OkHttp4Client(),
                        new Slf4jLogger());
                MochiService.createCards(notionClient);
            } catch (NotionAPIError e) {
                System.out.println(ansi().render(AnsiColors.ANSI_RED + e.getMessage() + AnsiColors.ANSI_RESET));
                main(args);
            }
    }
}
