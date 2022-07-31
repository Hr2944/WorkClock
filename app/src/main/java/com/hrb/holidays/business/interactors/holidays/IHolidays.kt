package com.hrb.holidays.business.interactors.holidays

import com.hrb.holidays.business.entities.holidays.HolidayPeriod
import java.time.LocalDate

interface IHolidays {
    fun nextAfter(date: LocalDate): HolidayPeriod?
    fun previousBefore(date: LocalDate): HolidayPeriod?
    fun at(date: LocalDate): HolidayPeriod?
}
