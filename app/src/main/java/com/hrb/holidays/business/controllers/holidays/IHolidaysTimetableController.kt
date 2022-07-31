package com.hrb.holidays.business.controllers.holidays

import com.hrb.holidays.business.entities.holidays.HolidayPeriod
import com.hrb.holidays.business.entities.holidays.HolidaysTimetable

interface IHolidaysTimetableController {
    fun getTimetable(): HolidaysTimetable
    suspend fun deleteHoliday(holiday: HolidayPeriod): HolidaysTimetable
    suspend fun addHoliday(holiday: HolidayPeriod): HolidaysTimetable
}
