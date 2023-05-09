package com.homebudget.utils;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomCalendar {

    public LocalDate date;
    private int year;
    private int month;
    private int months;

    public CustomCalendar(int year, int month, LocalDate date, int months) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.months = months;
    }

    public int getLastMondayInPreviousMonth() {
        int lastMonday = -1;
        LocalDate previousDate = this.date.plusMonths(months - 1);
        int month = previousDate.getMonth().getValue();

        for (int i = 1; i <= getDaysInPreviousMonth(); i++) {
            LocalDate date = LocalDate.of(previousDate.getYear(), month, i);
            if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
                lastMonday = date.getDayOfMonth();
            }
        }
        return lastMonday;
    }

    public int getDaysInPreviousMonth() {
        return date.plusMonths(months - 1).lengthOfMonth();
    }

    public int getDaysInCurrentMonth() {
        return date.plusMonths(months).lengthOfMonth();
    }

    public List<Integer> getAllMondaysInMonth() {
        List<Integer> mondays = new ArrayList<>();
        LocalDate currentMonth = LocalDate.of(year, month, 1);
        for (int i = 1; i <= currentMonth.lengthOfMonth(); i++) {
            LocalDate date = LocalDate.of(year, month, i);
            if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
                mondays.add(i);
            }
        }
        return mondays;
    }

    public boolean isMonday(int day) {
        for (int monday : getAllMondaysInMonth()) {
            if (day == monday) {
                return true;
            }
        }
        return false;
    }
}
