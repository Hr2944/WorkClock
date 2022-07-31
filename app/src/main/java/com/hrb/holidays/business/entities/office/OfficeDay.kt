package com.hrb.holidays.business.entities.office

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime

class OfficeDay(
    val dayOfWeek: DayOfWeek,
    val startAt: LocalTime,
    val endAt: LocalTime
) {
    fun officeTime(): Duration {
        return Duration.between(this.startAt, this.endAt)
    }

    fun remainingTime(date: LocalTime): Duration {
        return if ((date > this.startAt) and (date < this.endAt)) {
            Duration.between(date, this.endAt)
        } else {
            Duration.ZERO
        }
    }
}
