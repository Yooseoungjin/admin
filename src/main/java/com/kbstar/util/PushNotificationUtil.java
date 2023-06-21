package com.kbstar.util;


import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class PushNotificationUtil {

    // Modification Field ---------------------------------------
    private static final String PROJECT_ID = "kbstar2023-60755";
    public static final String firebaseConfig = "fcm_admin.json";
    // Modification Field ---------------------------------------

    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };
    public static final String MESSAGE_KEY = "message";




    private static HttpURLConnection getConnection() throws IOException {
        // [START use_access_token]
        URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;
        // [END use_access_token]
    }

    private static String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfig).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    private static String inputstreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    public static void sendMessage(JsonObject fcmMessage) throws IOException {
        HttpURLConnection connection = getConnection();
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(fcmMessage.toString());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            String response = inputstreamToString(connection.getInputStream());
            System.out.println("Message sent to Firebase for delivery, response:");
            System.out.println(response);
        } else {
            System.out.println("Unable to send message to Firebase:");
            String response = inputstreamToString(connection.getErrorStream());
            System.out.println(response);
        }
    }

    private static JsonObject buildNotificationMessage(String title, String body, String next, String imgUrl) {
        JsonObject jNotification = new JsonObject();
        jNotification.addProperty("title", title);
        jNotification.addProperty("body", body);
        jNotification.addProperty("image", imgUrl);


        JsonObject jMessage = new JsonObject();
        jMessage.add("notification", jNotification);


        JsonObject jData = new JsonObject();
        jData.addProperty("key1", next);
        jMessage.add("data", jData);

        /*
            firebase
            1. topic
            2. token
            3. condition -> multiple topic
         */

        //kb라는 app들에게(=topic을 구독하는 app들에게) 다 보내라
        //안드로이드스튜디오 프로젝트에서 MainActivity.java에서 오른쪽 코드가 토픽에ㅔ 대한 구독을 설정하는 것이다
        //>>> FirebaseMessaging.getInstance().subscribeToTopic("kb");

//        jMessage.addProperty("topic", "cherry");
        jMessage.addProperty("topic", "kb");
//        jMessage.addProperty("topic", "CH");

        JsonObject jFcm = new JsonObject();
        jFcm.add(MESSAGE_KEY, jMessage);

        return jFcm;
    }
    private static JsonObject buildNotificationTargetMessage(String title, String body, String next, String token) {
        JsonObject jNotification = new JsonObject();
        jNotification.addProperty("title", title);
        jNotification.addProperty("body", body);
        jNotification.addProperty("image", "");


        JsonObject jMessage = new JsonObject();
        jMessage.add("notification", jNotification);


        JsonObject jData = new JsonObject();
        jData.addProperty("key1", next);
        jMessage.add("data", jData);

        /*
            firebase
            1. topic
            2. token
            3. condition -> multiple topic
         */
        //jMessage.addProperty("topic", "kbs");
        // 토큰에 해당하는 놈에게만 보내라
        jMessage.addProperty("token",token);

        JsonObject jFcm = new JsonObject();
        jFcm.add(MESSAGE_KEY, jMessage);

        return jFcm;
    }
    // KB라고 되어져 있는 어플에 모두 알람 보낸다 또한 imgUrl으로 이미지도 보낸다
    public static void sendCommonMessage(String title, String body, String next, String imgUrl) throws IOException {
        JsonObject notificationMessage = buildNotificationMessage(title, body, next, imgUrl);
        sendMessage(notificationMessage);
    }
    //token에 지정된 사람에게 메세지가 간다
    public static void sendTargetMessage(String title, String body, String next, String token) throws IOException {
        JsonObject notificationMessage = buildNotificationTargetMessage(title, body, next, token);
        sendMessage(notificationMessage);
    }
}