package com.example.smsSpringTest.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author : 신기훈
 * date : 2025-03-06
 * comment : 네이버 클로바 챗봇 service
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ClovaChatbotService {

//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
    private static final String secretKey = "";
    private static final String apiUrl = "";

//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper;

//    public ClovaChatbotService() {
//        this.restTemplate = new RestTemplate();
//        this.objectMapper = new ObjectMapper();
//    }

    // 챗봇 오픈시 오픈 메세지
    public String openMessage() throws Exception {

        URL url = new URL(apiUrl);
        String chatMessage = "";
        String message = getReqMessage(chatMessage, "open");
        String encodeBase64String = makeSignature(message, secretKey);

        // api 서버 접속 (서버 -> 서버 통신)
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json;UTF-8");
        con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(message.getBytes("UTF-8"));
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();

        log.info("responseCode = " + responseCode);

        BufferedReader br;

        if(responseCode == 200) {
            // 정상 호출
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            con.getInputStream(), "UTF-8"));
            String decodedString;
            String jsonString = "";
            while ((decodedString = in.readLine()) != null) {
                jsonString = decodedString;
            }
            log.info("Response JSON: " + jsonString);
            // 받아온 값을 세팅하는 부분
            JsonParser jsonParser = new JsonParser();
            try {
                JsonObject json = (JsonObject) jsonParser.parse(jsonString);
                JsonArray bubblesArray = (JsonArray) json.get("bubbles");
                JsonObject bubbles = (JsonObject) bubblesArray.get(0);
                JsonObject data = (JsonObject) bubbles.get("data");
                String description = "";
                description = data.get("description").getAsString();
                chatMessage = description;
                log.info("최종 메세지 = " + chatMessage);
            } catch (Exception e) {
                log.info("error : " + e.getMessage(), e);
//                e.printStackTrace();
            }

            in.close();
        } else {  // 에러 발생
            chatMessage = con.getResponseMessage();
        }

        return chatMessage;
    }

    // 챗봇에 메세지 전송
    public String sendMessage(String chatMessage) throws Exception {

        URL url = new URL(apiUrl);

        String message = getReqMessage(chatMessage, "send");
        String encodeBase64String = makeSignature(message, secretKey);

        // api 서버 접속 (서버 -> 서버 통신)
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json;UTF-8");
        con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(message.getBytes("UTF-8"));
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();

        log.info("responseCode = " + responseCode);

        BufferedReader br;

        if(responseCode == 200) {
            // 정상 호출
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            con.getInputStream(), "UTF-8"));
            String decodedString;
            String jsonString = "";
            while ((decodedString = in.readLine()) != null) {
                jsonString = decodedString;
            }
            log.info("Response JSON: " + jsonString);
            // 받아온 값을 세팅하는 부분
            JsonParser jsonParser = new JsonParser();
            try {
                JsonObject json = (JsonObject) jsonParser.parse(jsonString);
                JsonArray bubblesArray = (JsonArray) json.get("bubbles");
                JsonObject bubbles = (JsonObject) bubblesArray.get(0);
                JsonObject data = (JsonObject) bubbles.get("data");
                String description = "";

                if (data.has("cover")) {
                    // 만약 폼 답변일때
                    JsonObject cover = data.getAsJsonObject("cover");
                    if (cover.has("data")) {
                        JsonObject coverData = cover.getAsJsonObject("data");
                        if (coverData.has("description")) {
                            description = coverData.get("description").getAsString();
                        }
                    }
                } else {
                    // 일반 답변
                    description = data.get("description").getAsString();
                }
                chatMessage = description;
            log.info("최종 메세지 = " + chatMessage);

                // Title 리스트 가져오기 (객관식 폼 답변 때)
                List<String> titles = new ArrayList<>();
                if (data.has("contentTable")) {
                    JsonArray contentTable = data.getAsJsonArray("contentTable");
                    for (int i = 0; i < contentTable.size(); i++) {
                        JsonArray row = contentTable.get(i).getAsJsonArray();
                        for (int j = 0; j < row.size(); j++) {
                            JsonObject cell = row.get(j).getAsJsonObject();
                            if (cell.has("data")) {
                                JsonObject cellData = cell.getAsJsonObject("data");
                                if (cellData.has("title")) {
                                    titles.add(cellData.get("title").getAsString());
                                }
                            }
                        }
                    }
                }

                log.info("title = " + titles);

            } catch (Exception e) {
                log.info("error : " + e.getMessage(), e);
//                e.printStackTrace();
            }

            in.close();
        } else {  // 에러 발생
            chatMessage = con.getResponseMessage();
        }

        return chatMessage;
    }


    //보낼 메세지를 네이버에서 제공해준 암호화로 변경해주는 메소드
    public static String makeSignature(String message, String secretKey) {

        String encodeBase64String = "";

        try {
            byte[] secrete_key_bytes = secretKey.getBytes("UTF-8");

            SecretKeySpec signingKey = new SecretKeySpec(secrete_key_bytes, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            encodeBase64String = Base64.encodeBase64String(rawHmac);

            return encodeBase64String;

        } catch (Exception e){
            System.out.println(e);
        }

        return encodeBase64String;

    }

    //보낼 메세지를 네이버 챗봇에 포맷으로 변경해주는 메소드
    public static String getReqMessage(String voiceMessage, String type) {

        String requestBody = "";

        try {

            JsonObject obj = new JsonObject();

            long timestamp = new Date().getTime();

            System.out.println("##"+timestamp);

            obj.addProperty("version", "v2");
            obj.addProperty("userId", "U47b00b58c90f8e47428af8b7bddc1231heo2");
            obj.addProperty("timestamp", timestamp);

            JsonObject bubbles_obj = new JsonObject();

            bubbles_obj.addProperty("type", "text");

            JsonObject data_obj = new JsonObject();
            data_obj.addProperty("description", voiceMessage);

            bubbles_obj.addProperty("type", "text");
            bubbles_obj.add("data", data_obj);

            JsonArray bubbles_array = new JsonArray();
            bubbles_array.add(bubbles_obj);

            obj.add("bubbles", bubbles_array);
            obj.addProperty("event", type);

            requestBody = obj.toString();

        } catch (Exception e){
            System.out.println("## Exception : " + e);
        }

        return requestBody;

    }

}
