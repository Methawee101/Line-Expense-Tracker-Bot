package com.prai.lineexpensetracker.dto.request;

import com.prai.lineexpensetracker.enums.TypeTransaction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class transactionRequest {
    @NotBlank(message = "lineUserId is required")
    private String lineUserId;

    private String displayName;

    @NotNull(message = "type is required")
    private TypeTransaction type;

    @NotBlank(message = "title is required")
    private String title;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;


}
