import com.fasterxml.jackson.databind.ObjectMapper;
import notion.api.v1.NotionClient;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MochiService {
    private final static OkHttpClient defaultClient = new OkHttpClient.Builder().build();
    private final static OkHttpClient client = defaultClient.newBuilder()
            .readTimeout(15, TimeUnit.DAYS)
            .connectTimeout(15, TimeUnit.DAYS)
            .readTimeout(15,TimeUnit.DAYS)
            .connectionPool(new ConnectionPool(0, 1, TimeUnit.SECONDS))
            .build();
//.protocols(List.of(Protocol.HTTP_1_1))
    public static SessionCookie login() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
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
        ObjectMapper ow = new ObjectMapper();

        try (Response response = client.newCall(request).execute()) {

            String encryptedUserId = response.headers().toMultimap()
                    .get("set-cookie").get(0)
                    .split("=")[1]
                    .split(";")[0];


            String rememberToken = response.headers().toMultimap()
                    .get("set-cookie").get(1)
                    .split("=")[1]
                    .split(";")[0];

            SessionCookie sessionCookie = mapper.readValue(response.body().string(), SessionCookie.class);
            sessionCookie.setRemember_token(rememberToken);
            sessionCookie.setId(encryptedUserId);
            return sessionCookie;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createCards(NotionClient notionClient) throws IOException {
        HashMap<String, String> dictionary = Dictionary.createDictionary(notionClient);
        SessionCookie sessionCookie = login();
        String deckId = UserInfo.askForDeckID();
        ArrayList<String> cardsContent = new ArrayList<>();
        ObjectMapper ow = new ObjectMapper();
        System.out.println(ow.writeValueAsString(dictionary.entrySet()));

        for (Map.Entry<String, String> entry :
                dictionary.entrySet()) {
            String front = entry.getKey();
            String back = entry.getValue();
            cardsContent.add(front + " --- " + back);
        }

            for (String content : cardsContent) {
                System.out.println(content);
                try {
                    sendACard(content, deckId, sessionCookie.getId(), sessionCookie.getRemember_token());
                    //Thread.sleep(3000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        System.out.println("Ready!!!");
        }



    public static void sendACard(String content, String deckId, String userID, String rememberToken) throws IOException {
        RequestBody body = RequestBody.create("{ \"card\": { \"content\": \"" + content + "\", \"deck-id\": \"" + deckId + "\" } }", null);
        Request request = new Request.Builder()
                .url("https://api.mochi.cards/v1/cards")
                .addHeader("Cookie", "user_id=" + userID + "; remember_token=" + rememberToken)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

