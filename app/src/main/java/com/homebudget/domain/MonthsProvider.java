package com.homebudget.domain;

import com.homebudget.repository.CalendarRepository;

public class MonthsProvider {

    public String getMonth(int month) {
        return CalendarRepository.month().get(month);
    }
}
