package com.example.company.maina;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class time {
    public static String gettime(){
        DateTimeFormatter format = DateTimeFormatter.RFC_1123_DATE_TIME;
        ZoneId zone = ZoneId.of("Europe/Moscow");
        Instant instant = Instant.now();
        ZonedDateTime zdt = instant.atZone(zone);
        String out = zdt.format(format);
        return out;
    }
}
