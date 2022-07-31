package com.hrb.holidays.business.interactors.office

import com.hrb.holidays.app.databases.office.IOfficeWeekGateway
import com.hrb.holidays.business.entities.office.OfficeDay
import com.hrb.holidays.business.entities.office.OfficeWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime


class RangeOfficeTimeCalculator(
    private val officeWeekGateway: IOfficeWeekGateway
) : IRangeOfficeTimeCalculator {

    override fun calculateOfficeTimeInDatesRange(from: LocalDateTime, to: LocalDateTime): Duration {
        var remainingTime = Duration.ZERO
        var currentEvaluatedDay = from
        if (isOfficeDayStarted(currentEvaluatedDay)) {
            remainingTime += getRemainingOfficeTimeForDay(currentEvaluatedDay)
            currentEvaluatedDay = currentEvaluatedDay.plusDays(1).with(LocalTime.MIDNIGHT)
        }
        while (currentEvaluatedDay.isBefore(to) or currentEvaluatedDay.isEqual(to)) {
            remainingTime += getOfficeTimeForDay(currentEvaluatedDay)
            currentEvaluatedDay = currentEvaluatedDay.plusDays(1)
        }
        return remainingTime
    }

    override fun calculateOfficeTimeProgressInDatesRange(
        from: LocalDateTime,
        to: LocalDateTime,
        atTime: LocalDateTime
    ): Float {
        if (atTime.isBefore(from) or atTime.isAfter(to)) {
            throw IllegalArgumentException(
                "atTime ($atTime) must be between from ($from) and to ($to)"
            )
        }
        val totalTimeInRange = calculateOfficeTimeInDatesRange(from, to)
        val currentRemainingTime = calculateOfficeTimeInDatesRange(atTime, to)
        return 1 - (
                currentRemainingTime.toMillis().toFloat() /
                        totalTimeInRange.toMillis().toFloat()
                )
    }

    private fun getOfficeDay(day: LocalDateTime, officeWeek: OfficeWeek): OfficeDay {
        for (weekDay in officeWeek.week) {
            if (weekDay.dayOfWeek == day.dayOfWeek) {
                return weekDay
            }
        }
        throw RuntimeException("$day not recognised as a week day")
    }

    private fun getOfficeTimeForDay(day: LocalDateTime): Duration {
        return getOfficeDay(day, officeWeekGateway.fetch()).officeTime()
    }

    private fun getRemainingOfficeTimeForDay(day: LocalDateTime): Duration {
        val officeDay = getOfficeDay(day, officeWeekGateway.fetch())
        return Duration.between(day.toLocalTime(), officeDay.endAt)
    }

    private fun isOfficeDayStarted(day: LocalDateTime): Boolean {
        val officeDay = getOfficeDay(day, officeWeekGateway.fetch())
        return day.toLocalTime().isBetween(officeDay.startAt, officeDay.endAt)
    }

    private fun LocalTime.isBetween(from: LocalTime, to: LocalTime): Boolean {
        return this.isAfter(from) && this.isBefore(to)
    }
}
