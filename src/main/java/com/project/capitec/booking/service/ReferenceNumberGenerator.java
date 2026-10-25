package com.project.capitec.booking.service;

import org.springframework.stereotype.Component;

import java.util.Random;

import static com.project.capitec.booking.utils.DateUtils.getCurrentDate;

@Component
public class ReferenceNumberGenerator {

    private static final String PREFIX = "REF";
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final Random random = new Random();

    public String generate() {
        String randomSuffix = generateRandomString();
        return String.format("%s-%s-%s", PREFIX, getCurrentDate(), randomSuffix);
    }

    private String generateRandomString() {
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(CHAR_SET.length());
            sb.append(CHAR_SET.charAt(index));
        }
        return sb.toString();
    }
}
