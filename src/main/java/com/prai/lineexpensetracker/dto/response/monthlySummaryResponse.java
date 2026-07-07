package com.prai.lineexpensetracker.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Getter
@Builder
public class monthlySummaryResponse {

    private  String lineUserId;

    private YearMonth month;

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal balance;

}
