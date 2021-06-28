import notion.api.v1.NotionClient;
import okhttp3.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.fusesource.jansi.Ansi.ansi;

public class MochiService {
    private final static OkHttpClient defaultClient = new OkHttpClient.Builder().build();
    private final static OkHttpClient client = defaultClient.newBuilder()
            .readTimeout(3, TimeUnit.DAYS)
            .connectTimeout(3, TimeUnit.DAYS)
            .readTimeout(3, TimeUnit.DAYS)
            .retryOnConnectionFailure(true)
            .connectionPool(new ConnectionPool(0, 1, TimeUnit.SECONDS))
            .build();

    public static SessionInfo login() {
        String email = UserInfo.askForMochiEmail();
        String password = UserInfo.askForMochiPassword();
        RequestBody body = RequestBody.create(
                "{ \"session\": { \"email\": \"" + email + "\", \"password\": \"" + password + "\" } }",
                null
        );

        Request request = new Request.Builder()
                .url("https://api.mochi.cards/login")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {

            String encryptedUserId = response.headers().toMultimap()
                    .get("set-cookie").get(0)
                    .split("=")[1]
                    .split(";")[0];


            String rememberToken = response.headers().toMultimap()
                    .get("set-cookie").get(1)
                    .split("=")[1]
                    .split(";")[0];

            SessionInfo sessionInfo = new SessionInfo();
            sessionInfo.setRemember_token(rememberToken);
            sessionInfo.setId(encryptedUserId);
            return sessionInfo;
        } catch (Exception e) {
            System.out.println(ansi().render(AnsiColors.ANSI_RED + "Incorrect email or password." + AnsiColors.ANSI_RESET));
            login();
        }
        return null;
    }

    public static void createCards(NotionClient notionClient) {
        HashMap<String, String> dictionary = Dictionary.createDictionary(notionClient);
        SessionInfo sessionInfo = login();
        String deckId = UserInfo.askForDeckID();
        ArrayList<String> cardsContent = new ArrayList<>();

        for (Map.Entry<String, String> entry :
                dictionary.entrySet()) {
            String front = entry.getKey();
            String back = entry.getValue();
            cardsContent.add(front + "\\n" + "---\\n" + back);
        }

        for (String content : cardsContent) {
            System.out.println(ansi().render(AnsiColors.ANSI_GREEN + "Creating card..:" + AnsiColors.ANSI_RESET));
            System.out.println(ansi().render(content));
            Response response = sendACard(content, deckId, sessionInfo.getId(), sessionInfo.getRemember_token());
            response.close();
        }
        System.out.println(ansi().render(AnsiColors.ANSI_GREEN + "Ready!!!" + AnsiColors.ANSI_RESET));
    }


    public static Response sendACard(String content, String deckId, String userID, String rememberToken) {
        RequestBody body = RequestBody.create("{ \"card\": { \"content\": \"" + content + "\", \"deck-id\": \"" + deckId + "\" } }", null);
        Request request = new Request.Builder()
                .url("https://api.mochi.cards/v1/cards")
                .addHeader("Cookie", "user_id=" + userID + "; remember_token=" + rememberToken)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            response.body().close();
            if (response.code() == 400) {
                System.out.println(ansi().render(AnsiColors.ANSI_RED + "Incorrect deck ID!" + AnsiColors.ANSI_RESET));
                return null;
            }
            return response;
        } catch (Exception e) {
            System.out.println(ansi().render(AnsiColors.ANSI_RED + "Mochi does not response." + AnsiColors.ANSI_RESET));
        }
        return null;
    }
}

