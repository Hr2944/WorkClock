package com.hrb.holidays.business.interactors.holidays

import com.hrb.holidays.app.databases.holidays.IHolidaysTimetableGateway
import com.hrb.holidays.business.entities.holidays.HolidayPeriod
import java.time.LocalDate

class Holidays(private val holidaysGateway: IHolidaysTimetableGateway) : IHolidays {

    override fun nextAfter(date: LocalDate): HolidayPeriod? {
        val sortedHolidays = sortHolidays(holidaysGateway.fetch().timetable)
        return sortedHolidays.firstOrNull { it.fromDate.isAfter(date) }
    }

    override fun previousBefore(date: LocalDate): HolidayPeriod? {
        val reverseSortedHolidays = sortHolidays(holidaysGateway.fetch().timetable).reversedArray()
        return reverseSortedHolidays.firstOrNull { it.toDate.isBefore(date) }
    }

    override fun at(date: LocalDate): HolidayPeriod? {
        for (holidays in holidaysGateway.fetch().timetable) {
            if (date.isAfter(holidays.fromDate) && date.isBefore(holidays.toDate)) {
                return holidays
            }
        }
        return null
    }

    private fun sortHolidays(holidays: Set<HolidayPeriod>): Array<HolidayPeriod> {
        return holidays.sortedBy { it.fromDate }
            .toTypedArray()
    }
}
