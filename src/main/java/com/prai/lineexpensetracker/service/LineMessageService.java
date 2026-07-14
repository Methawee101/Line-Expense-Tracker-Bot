package com.prai.lineexpensetracker.service;

import com.prai.lineexpensetracker.config.LineProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LineMessageService {

    private final LineProperties lineProperties;

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://api.line.me")
            .build();

    public void  replyMessage(String replyToken, String message) {
        try{
            System.out.println("=== SEND LINE REPLY ===");
            System.out.println("replyToken = " + replyToken);
            System.out.println("token empty? = " +
                    (lineProperties.getChannelAccessToken() == null
                    || lineProperties.getChannelAccessToken().isBlank()));

            Map<String,Object> body = Map.of(
                    "replyToken",replyToken,
                    "message", List.of(
                            Map.of(
                                    "type","text",
                                    "text",message
                            )
                    )
            );
            restClient.post()
                    .uri("/v2/bot/message/reply")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + lineProperties.getChannelAccessToken())
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

            System.out.println("=== Line reply success ===");
        }catch (Exception e){
            System.out.println("=== Line Reply faield ===");
            e.printStackTrace();
        }
    }

    public void pushMessage(String lineUserId, String message) {
       try {
           Map<String, Object> body = Map.of(
                   "to", lineUserId,
                   "message", List.of(
                           Map.of(
                                   "type","text",
                                   "text",message
                           )
                   )
           );

           restClient.post()
                   .uri("/v2/bot/message/push")
                   .contentType(MediaType.APPLICATION_JSON)
                   .header("Authorization", "Bearer " + lineProperties.getChannelAccessToken())
                   .body(body)
                   .retrieve()
                   .toBodilessEntity();

           System.out.println("=== Line push success ===");
       } catch (Exception e){
           System.out.println("=== line push failed ===");
           e.printStackTrace();
       }
    }
}
