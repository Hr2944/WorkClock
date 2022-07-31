package com.hrb.holidays.business.entities.holidays

import java.time.LocalDate

data class HolidayPeriod(
    val name: String,
    val fromDate: LocalDate,
    val toDate: LocalDate
)
