package com.project.capitec.booking.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String formatDateTime(LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String date = startTime.format(dateFormatter);
        String startTimeStr = startTime.format(timeFormatter);
        String endTimeStr = endTime.format(timeFormatter);

        return String.format("%s at %s to %s", date, startTimeStr, endTimeStr);
    }

    public static String getCurrentDate() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDateTime.now().format(dateFormatter);
    }
}
