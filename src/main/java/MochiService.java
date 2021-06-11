import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import notion.api.v1.NotionClient;
import notion.api.v1.model.users.User;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MochiService {
    private static final OkHttpClient client = new OkHttpClient();

    public static SessionCookie login() {
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
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.headers().get("Set-Cookie"));
           // CookieJar cj = new CookieJar();
           // SessionCookie sessionCookie =
            return mapper.readValue(response.body().string(), SessionCookie.class);
           // sessionCookie.setRemember_token();
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
        ow.enable(SerializationFeature.WRAP_ROOT_VALUE);
        for (Map.Entry<String, String> entry :
                dictionary.entrySet()) {
            String front = entry.getKey();
            String back = entry.getValue();
            cardsContent.add(front+"\\---\\"+back);
        }

        for (String content : cardsContent) {
            Card card = new Card(content, deckId);
            RequestBody body = RequestBody.create(ow.writeValueAsString(card), null);
            assert sessionCookie != null;
            Request request = new Request.Builder()
                    .url("https://api.mochi.cards/v1/cards")
                    .post(body)
                    .addHeader("Cookie","user_id="+sessionCookie.getId()+"; remember_token=2i9JXe9EF-1cZ7yEsH6-2A")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(ow.writeValueAsString(response.body()));
        }
    }
}

