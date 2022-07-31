package com.hrb.holidays.business.controllers.office

import com.hrb.holidays.business.entities.holidays.HolidayPeriod
import com.hrb.holidays.business.interactors.holidays.IHolidays
import com.hrb.holidays.business.interactors.office.IRangeOfficeTimeCalculator
import com.hrb.holidays.app.presenters.office.ProgressInDatesRange
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

class OfficeTimeBeforeHolidaysController(
    private val officeCalculator: IRangeOfficeTimeCalculator,
    private val holidays: IHolidays,
) : IOfficeTimeBeforeHolidaysController {
    override fun getTimeBeforeNextHolidays(): Duration? {
        val now = LocalDateTime.now()
        val from = getStartingDate(now)
        val to = getHolidaysAfter(now.toLocalDate()) ?: return null
        return this.officeCalculator.calculateOfficeTimeInDatesRange(
            from = from,
            to = to
        )
    }

    override fun getTimeProgressBeforeNextHolidays(): ProgressInDatesRange? {
        val now = LocalDateTime.now()
        val from = getHolidaysBefore(now.toLocalDate())
        val to = getHolidaysAfter(now.toLocalDate())
        if (from == null || to == null) {
            return null
        }
        return ProgressInDatesRange(
            progress = officeCalculator.calculateOfficeTimeProgressInDatesRange(from, to, now),
            fromDate = from.toLocalDate(),
            toDate = to.toLocalDate(),
            atTime = now
        )
    }

    private fun getStartingDate(date: LocalDateTime): LocalDateTime {
        val potentialCurrentHolidays = holidays.at(date.toLocalDate())
        return if (potentialCurrentHolidays is HolidayPeriod) {
            potentialCurrentHolidays.toDate.atStartOfDay()
        } else {
            date
        }
    }

    private fun getHolidaysAfter(date: LocalDate): LocalDateTime? {
        return this.holidays.nextAfter(date)?.fromDate?.atStartOfDay()
    }

    private fun getHolidaysBefore(date: LocalDate): LocalDateTime? {
        return this.holidays.previousBefore(date)?.toDate?.atStartOfDay()
    }
}
