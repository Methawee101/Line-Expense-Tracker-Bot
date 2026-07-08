package com.prai.lineexpensetracker.dto.line;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class lineEvent {
    private String type;
    private lineSource source;
    private lineMessage message;
    private String replyToken;
}
