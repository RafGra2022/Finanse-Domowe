package com.homebudget.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class SystemTimestamp {

    public static String getTimestamp() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), TimeZone
                .getDefault().toZoneId()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
