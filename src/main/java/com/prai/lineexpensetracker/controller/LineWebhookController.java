package com.prai.lineexpensetracker.controller;

import com.prai.lineexpensetracker.dto.line.lineEvent;
import com.prai.lineexpensetracker.dto.line.lineWebhookRequest;
import com.prai.lineexpensetracker.dto.request.transactionRequest;
import com.prai.lineexpensetracker.dto.response.monthlySummaryResponse;
import com.prai.lineexpensetracker.dto.response.transactionResponse;
import com.prai.lineexpensetracker.enums.TypeTransaction;
import com.prai.lineexpensetracker.service.LineMessageService;
import com.prai.lineexpensetracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/webhook/line")
@RequiredArgsConstructor
public class LineWebhookController {
    private final TransactionService transactionService;
    private final LineMessageService lineMessageService;

    @GetMapping("/")
    public String home() {
        return "Line Expense Tracker Bot is running";
    }

    @PostMapping
    public List<String> handleWebhook(@RequestBody lineWebhookRequest request){
        List<String> responses = new ArrayList<>();
        if(request.getEvents() == null){
            return responses;
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

            String responseMessage = handleTextMessage(lineUserId, text);
            lineMessageService.replyMessage(event.getReplyToken(),responseMessage);
            responses.add(responseMessage);
//            transactionRequest transactionRequest = parseTextToRequest(lineUserId,text);


//            transactionService.createTransaction(transactionRequest);

        }
        return responses;

    }

    private String handleTextMessage(String lineUserId, String text) {
        String trimText = text.trim();

        if(trimText.startsWith("รายรับ") || trimText.startsWith("รายจ่าย")){
            return handleCreateTransaction(lineUserId,trimText);
        }

        if(trimText.equals("สรุปเดือนนี้")){
            return handleMonthSummary(lineUserId);
        }
        if (trimText.startsWith("สรุปเดือน")){
            return handleSpecificMonthSummary(lineUserId,text);
        }

        return """
                รูปแบบข้อความไม่ถูกต้อง
                
                ตัวอย่าง:
                รายรับ เงินเดือน 15000
                รายจ่าย กาแฟ 50
                
                สรุปเดือนนี้
                สรุปเดือน 2026-07
                """;


    }

//    รายรับรายจ่าย
    private String handleCreateTransaction(String lineUserId, String text) {
        transactionRequest request = parseTextToRequest(lineUserId, text);

        transactionResponse response = transactionService.createTransaction(request);

        String typeText = response.getType() == TypeTransaction.INCOME
                ? "รายรับ"
                : "รายจ่าย";

        return String.format(
                "บันทึกเรียบร้อย ✅%n%s: %s%nจำนวน: %s บาท",
                typeText,
                response.getTitle(),
                response.getAmount()
        );
    }

//    กรณีสรุปรายเดือนนี้
    private String handleMonthSummary(String lineUserID) {
        YearMonth currentMonth = YearMonth.now();

        monthlySummaryResponse summary = transactionService.getMonthlySummary(lineUserID,currentMonth);

        return formatSummaryMessage(summary);

    }

//    สรุประบุเดือน
    private String handleSpecificMonthSummary(String lineUserId, String text){
        String[] parts = text.trim().split("\\s+");

        if (parts.length < 2 ){
            return "กรุณาระบุเดือนเช่น สรุปเดือน 2026-07";
        }

        try{
            YearMonth month = YearMonth.parse(parts[1]);
            monthlySummaryResponse summary = transactionService.getMonthlySummary(lineUserId,month);
            return formatSummaryMessage(summary);
        }catch (Exception e){
            return "รูปแบบเดือนไม่ถูกต้อง";
        }
    }

    private String formatSummaryMessage(monthlySummaryResponse summary) {
        return String.format(
                """
                สรุปรายจ่าย เดือน %s
                
                รายรับรวม: %s บาท
                รายจ่ายรวม: %s บาท
                คงเหลือ: %s บาท
                        """,
                summary.getMonth(),
                summary.getTotalIncome(),
                summary.getTotalExpense(),
                summary.getBalance()
        );
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
