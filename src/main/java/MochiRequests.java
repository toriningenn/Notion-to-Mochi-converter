import okhttp3.*;

import java.io.IOException;

public class MochiRequests {
    private static OkHttpClient client = new OkHttpClient();

//.addHeader("Content-Type", "application/json") нужен ли?

    public static String login() {
        String email = UserInfo.askForMochiEmail();
        String password = UserInfo.askForMochiPassword();
        RequestBody body = RequestBody.create("{ \"session\": { \"email\": \"" + email + "\", \"password\": \"" + password + "\" } }",null);
        Request request = new Request.Builder()
                .url("https://api.mochi.cards/login")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}

