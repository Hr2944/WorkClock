package com.hrb.holidays.business.interactors.office

import java.time.Duration
import java.time.LocalDateTime

interface IRangeOfficeTimeCalculator {
    fun calculateOfficeTimeInDatesRange(from: LocalDateTime, to: LocalDateTime): Duration
    fun calculateOfficeTimeProgressInDatesRange(
        from: LocalDateTime,
        to: LocalDateTime,
        atTime: LocalDateTime
    ): Float
}
