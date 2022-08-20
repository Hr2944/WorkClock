package com.hrb.holidays.commons

import androidx.compose.runtime.Immutable
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Immutable
open class ImmutableObject<out T>(private val _value: T) {
    operator fun invoke(): T = _value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableObject<*>) return false

        if (_value != other._value) return false

        return true
    }

    override fun hashCode(): Int {
        return _value?.hashCode() ?: 0
    }

    override fun toString(): String {
        return _value.toString()
    }
}

class ImmutableYearMonth(month: YearMonth) : ImmutableObject<YearMonth>(month)

class ImmutableLocalDate(date: LocalDate) : ImmutableObject<LocalDate>(date)

class ImmutableDateTimeFormatter(
    formatter: DateTimeFormatter
) : ImmutableObject<DateTimeFormatter>(formatter)
