package com.hrb.holidays.business.controllers.holidays

import com.hrb.holidays.business.entities.holidays.HolidayPeriod
import com.hrb.holidays.business.entities.holidays.HolidaysTimetable
import com.hrb.holidays.app.databases.holidays.IHolidaysTimetableGateway

class HolidaysTimetableController(
    private val holidaysGateway: IHolidaysTimetableGateway
) : IHolidaysTimetableController {
    override fun getTimetable(): HolidaysTimetable {
        return holidaysGateway.fetch()
    }

    override suspend fun deleteHoliday(holiday: HolidayPeriod): HolidaysTimetable {
        val currentHolidays = holidaysGateway.fetch()
        if (holiday in currentHolidays.timetable) {
            holidaysGateway.update(
                currentHolidays.copy(timetable = (currentHolidays.timetable - holiday) as MutableSet<HolidayPeriod>)
            )
        }
        return holidaysGateway.fetch()
    }

    override suspend fun addHoliday(holiday: HolidayPeriod): HolidaysTimetable {
        val currentHolidays = getTimetable()
        if (holiday !in currentHolidays.timetable) {
            holidaysGateway.update(
                currentHolidays.copy(
                    timetable = (currentHolidays.timetable + holiday) as MutableSet<HolidayPeriod>
                )
            )
        }
        return holidaysGateway.fetch()
    }
}
