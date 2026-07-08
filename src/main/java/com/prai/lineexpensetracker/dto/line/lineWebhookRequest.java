package com.prai.lineexpensetracker.dto.line;

import lombok.Getter;
import lombok.Setter;

import javax.sound.sampled.LineEvent;
import java.util.List;

@Getter
@Setter
public class lineWebhookRequest {
    private List<lineEvent> events;
}
