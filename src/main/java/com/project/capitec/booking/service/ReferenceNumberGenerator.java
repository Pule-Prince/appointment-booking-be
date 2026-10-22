package com.project.capitec.booking.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class ReferenceNumberGenerator {

    private static final String PREFIX = "REF";
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final Random random = new Random();

    public String generate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = LocalDateTime.now().format(formatter);
        String randomSuffix = generateRandomString(4);
        return String.format("%s-%s-%s", PREFIX, dateString, randomSuffix);
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHAR_SET.length());
            sb.append(CHAR_SET.charAt(index));
        }
        return sb.toString();
    }
}
