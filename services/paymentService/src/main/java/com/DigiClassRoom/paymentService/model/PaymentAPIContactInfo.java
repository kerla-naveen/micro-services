package com.DigiClassRoom.paymentService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "payment")
@Getter
@Setter
public class PaymentAPIContactInfo {
    private String message;
    private Map<String,String> contactDetails;
    private List<String> onCallSupport;
}
