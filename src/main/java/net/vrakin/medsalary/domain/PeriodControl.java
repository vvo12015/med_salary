package net.vrakin.medsalary.domain;

import java.time.LocalDate;

public interface PeriodControl {
    LocalDate getPeriod();
    void setPeriod(LocalDate period);
}
