package com.homebudget.repository;

import java.util.HashMap;
import java.util.Map;

public class CalendarRepository {

    public static Map<Integer, String> month() {
        Map<Integer, String> month = new HashMap<>();
        month.put(1, "Styczeń");
        month.put(2, "Luty");
        month.put(3, "Marzec");
        month.put(4, "Kwiecień");
        month.put(5, "Maj");
        month.put(6, "Czerwiec");
        month.put(7, "Lipiec");
        month.put(8, "Sierpień");
        month.put(9, "Wrzesień");
        month.put(10, "Październik");
        month.put(11, "Listopad");
        month.put(12, "Grudzień");
        return month;
    }
}
