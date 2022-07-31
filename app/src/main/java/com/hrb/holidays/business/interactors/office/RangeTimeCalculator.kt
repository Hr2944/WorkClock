package com.hrb.holidays.business.interactors.office

import java.time.Duration
import java.time.LocalDateTime

/**
 * Calculates time between two given date
 */
class RangeTimeCalculator : IRangeTimeCalculator {

    override fun calculate(from: LocalDateTime, to: LocalDateTime): Duration {
        return Duration.between(from, to)
    }
}
