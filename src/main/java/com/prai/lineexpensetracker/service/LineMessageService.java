package com.prai.lineexpensetracker.service;

import org.springframework.stereotype.Service;

@Service
public class LineMessageService {

    public void  replyMessage(String replyToken, String message) {
        System.out.println("Reply token: " + replyToken);
        System.out.println("Reply message:");
        System.out.println(message);
    }

    public void pushMessage(String lineUserId, String message) {
        System.out.println("Push to: " + lineUserId);
        System.out.println("Push message:");
        System.out.println(message);
    }
}
