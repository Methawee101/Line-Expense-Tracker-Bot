package com.prai.lineexpensetracker.controller;

import com.prai.lineexpensetracker.dto.line.lineEvent;
import com.prai.lineexpensetracker.dto.line.lineWebhookRequest;
import com.prai.lineexpensetracker.dto.request.transactionRequest;
import com.prai.lineexpensetracker.enums.TypeTransaction;
import com.prai.lineexpensetracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/webhook/line")
@RequiredArgsConstructor
public class LineWebhookController {
    private final TransactionService transactionService;

    @PostMapping
    public void handleWebhook(@RequestBody lineWebhookRequest request){
        if(request.getEvents() == null){
            return;
        }

        for (lineEvent event: request.getEvents()){
            if(!"message".equals(event.getType())){
                continue;
                //ข้าม
            }

            if (event.getMessage() == null || !"text".equals(event.getMessage().getType())){
                continue;
            }

            String lineUserId = event.getSource().getUserId();
            String text = event.getMessage().getText();

            transactionRequest transactionRequest = parseTextToRequest(lineUserId,text);

            transactionService.createTransaction(transactionRequest);

        }

    }

    private  transactionRequest parseTextToRequest(String lineUserId, String text) {
        String[] parts = text.trim().split("\\s+");

        if(parts.length < 3){
            throw  new IllegalArgumentException("Invalid message format");
        }

        String typeText = parts[0];
        String amountText = parts[parts.length -1];

        StringBuilder titleBuilder = new StringBuilder();
        for (int i = 1; i< parts.length - 1; i++){
            if(i>1){
                titleBuilder.append(" ");
            }
            titleBuilder.append(parts[i]);
        }

        TypeTransaction type = switch (typeText){
            case "รายรับ" -> TypeTransaction.INCOME;
            case "รายจ่าย" -> TypeTransaction.EXPENSE;
            default -> throw  new IllegalArgumentException("Invalid transaction type");
        };

        transactionRequest request = new transactionRequest();
        request.setLineUserId(lineUserId);
        request.setDisplayName(null);
        request.setTitle(titleBuilder.toString());
        request.setAmount(new BigDecimal(amountText));
        request.setType(type);

        return request;
    }
}
