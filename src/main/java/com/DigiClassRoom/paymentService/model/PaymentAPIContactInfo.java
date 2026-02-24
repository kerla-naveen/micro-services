package com.DigiClassRoom.paymentService.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "payment")
public record PaymentAPIContactInfo(String message, Map<String,String> contactDetails, List<String> onCallSupport) {
}
