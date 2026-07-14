package com.prai.lineexpensetracker.scheduler;

import com.prai.lineexpensetracker.dto.response.monthlySummaryResponse;
import com.prai.lineexpensetracker.entity.User;
import com.prai.lineexpensetracker.enums.UserStatus;
import com.prai.lineexpensetracker.repository.UserRepository;
import com.prai.lineexpensetracker.service.LineMessageService;
import com.prai.lineexpensetracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
public class monthlySummaryScheduler {
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final LineMessageService lineMessageService;

//    ทุกวันที่ 1
    @Scheduled(cron = "0 0 9 1 * *")
//    @Scheduled(cron = "*/30 * * * * *")
    public  void sendMonthlySummary() {
        YearMonth previousMonth = YearMonth.now().minusMonths(1);

//   test     YearMonth previousMonth = YearMonth.now();
        List<User> activeUsers = userRepository.findByStatus(UserStatus.ACTIVE);

        for(User user : activeUsers) {
            monthlySummaryResponse summary = transactionService.getMonthlySummary(user.getLineUserId(), previousMonth);

            String message = formatSummaryMessage(summary);

            // รอเชื่อม line
            lineMessageService.pushMessage(user.getLineUserId(),message);
        }
    }
    private String formatSummaryMessage(monthlySummaryResponse summary) {
        return String.format(
                """
                สรุปรายรับรายจ่ายประจำเดือน %s
                
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
}
