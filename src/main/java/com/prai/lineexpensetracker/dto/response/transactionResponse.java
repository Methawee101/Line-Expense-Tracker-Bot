package com.prai.lineexpensetracker.dto.response;

import com.prai.lineexpensetracker.enums.TypeTransaction;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class transactionResponse {
    private Long id;
    private Long userId;
    private String lineUserId;
    private String DisplayName;
    private TypeTransaction type;
    private String title;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private LocalDate createdAt;

}
