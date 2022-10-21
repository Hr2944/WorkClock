package com.hrb.holidays.commons.immutable

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import java.time.*
import java.time.chrono.ChronoLocalDate
import java.time.chrono.Era
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.*

@Immutable
@Parcelize
class ImmutableLocalDate(private val _localDate: LocalDate) : Parcelable, Temporal,
    TemporalAdjuster, ChronoLocalDate {
    /**
     * Checks if the specified field is supported.
     *
     *
     * This checks if this date can be queried for the specified field.
     * If false, then calling the [range][.range],
     * [get][.get] and [.with]
     * methods will throw an exception.
     *
     *
     * If the field is a [ChronoField] then the query is implemented here.
     * The supported fields are:
     *
     *  * `DAY_OF_WEEK`
     *  * `ALIGNED_DAY_OF_WEEK_IN_MONTH`
     *  * `ALIGNED_DAY_OF_WEEK_IN_YEAR`
     *  * `DAY_OF_MONTH`
     *  * `DAY_OF_YEAR`
     *  * `EPOCH_DAY`
     *  * `ALIGNED_WEEK_OF_MONTH`
     *  * `ALIGNED_WEEK_OF_YEAR`
     *  * `MONTH_OF_YEAR`
     *  * `PROLEPTIC_MONTH`
     *  * `YEAR_OF_ERA`
     *  * `YEAR`
     *  * `ERA`
     *
     * All other `ChronoField` instances will return false.
     *
     *
     * If the field is not a `ChronoField`, then the result of this method
     * is obtained by invoking `TemporalField.isSupportedBy(TemporalAccessor)`
     * passing `this` as the argument.
     * Whether the field is supported is determined by the field.
     *
     * @param field  the field to check, null returns false
     * @return true if the field is supported on this date, false if not
     */
    override fun isSupported(field: TemporalField?): Boolean {
        return _localDate.isSupported(field)
    }

    /**
     * Checks if the specified unit is supported.
     *
     *
     * This checks if the specified unit can be added to, or subtracted from, this date.
     * If false, then calling the [.plus] and
     * [minus][.minus] methods will throw an exception.
     *
     *
     * If the unit is a [ChronoUnit] then the query is implemented here.
     * The supported units are:
     *
     *  * `DAYS`
     *  * `WEEKS`
     *  * `MONTHS`
     *  * `YEARS`
     *  * `DECADES`
     *  * `CENTURIES`
     *  * `MILLENNIA`
     *  * `ERAS`
     *
     * All other `ChronoUnit` instances will return false.
     *
     *
     * If the unit is not a `ChronoUnit`, then the result of this method
     * is obtained by invoking `TemporalUnit.isSupportedBy(Temporal)`
     * passing `this` as the argument.
     * Whether the unit is supported is determined by the unit.
     *
     * @param unit  the unit to check, null returns false
     * @return true if the unit can be added/subtracted, false if not
     */
    override fun isSupported(unit: TemporalUnit?): Boolean {
        return _localDate.isSupported(unit)
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the range of valid values for the specified field.
     *
     *
     * The range object expresses the minimum and maximum valid values for a field.
     * This date is used to enhance the accuracy of the returned range.
     * If it is not possible to return the range, because the field is not supported
     * or for some other reason, an exception is thrown.
     *
     *
     * If the field is a [ChronoField] then the query is implemented here.
     * The [supported fields][.isSupported] will return
     * appropriate range instances.
     * All other `ChronoField` instances will throw an `UnsupportedTemporalTypeException`.
     *
     *
     * If the field is not a `ChronoField`, then the result of this method
     * is obtained by invoking `TemporalField.rangeRefinedBy(TemporalAccessor)`
     * passing `this` as the argument.
     * Whether the range can be obtained is determined by the field.
     *
     * @param field  the field to query the range for, not null
     * @return the range of valid values for the field, not null
     * @throws DateTimeException if the range for the field cannot be obtained
     * @throws UnsupportedTemporalTypeException if the field is not supported
     */
    override fun range(field: TemporalField): ValueRange {
        return _localDate.range(field)
    }

    /**
     * Gets the value of the specified field from this date as an `int`.
     *
     *
     * This queries this date for the value of the specified field.
     * The returned value will always be within the valid range of values for the field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     *
     *
     * If the field is a [ChronoField] then the query is implemented here.
     * The [supported fields][.isSupported] will return valid
     * values based on this date, except `EPOCH_DAY` and `PROLEPTIC_MONTH`
     * which are too large to fit in an `int` and throw an `UnsupportedTemporalTypeException`.
     * All other `ChronoField` instances will throw an `UnsupportedTemporalTypeException`.
     *
     *
     * If the field is not a `ChronoField`, then the result of this method
     * is obtained by invoking `TemporalField.getFrom(TemporalAccessor)`
     * passing `this` as the argument. Whether the value can be obtained,
     * and what the value represents, is determined by the field.
     *
     * @param field  the field to get, not null
     * @return the value for the field
     * @throws DateTimeException if a value for the field cannot be obtained or
     * the value is outside the range of valid values for the field
     * @throws UnsupportedTemporalTypeException if the field is not supported or
     * the range of values exceeds an `int`
     * @throws ArithmeticException if numeric overflow occurs
     */
    // override for Javadoc and performance
    override fun get(field: TemporalField): Int {
        return _localDate.get(field)
    }

    /**
     * Gets the value of the specified field from this date as a `long`.
     *
     *
     * This queries this date for the value of the specified field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     *
     *
     * If the field is a [ChronoField] then the query is implemented here.
     * The [supported fields][.isSupported] will return valid
     * values based on this date.
     * All other `ChronoField` instances will throw an `UnsupportedTemporalTypeException`.
     *
     *
     * If the field is not a `ChronoField`, then the result of this method
     * is obtained by invoking `TemporalField.getFrom(TemporalAccessor)`
     * passing `this` as the argument. Whether the value can be obtained,
     * and what the value represents, is determined by the field.
     *
     * @param field  the field to get, not null
     * @return the value for the field
     * @throws DateTimeException if a value for the field cannot be obtained
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun getLong(field: TemporalField): Long {
        return _localDate.getLong(field)
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the chronology of this date, which is the ISO calendar system.
     *
     *
     * The `Chronology` represents the calendar system in use.
     * The ISO-8601 calendar system is the modern civil calendar system used today
     * in most of the world. It is equivalent to the proleptic Gregorian calendar
     * system, in which today's rules for leap years are applied for all time.
     *
     * @return the ISO chronology, not null
     */
    override fun getChronology(): IsoChronology {
        return _localDate.chronology
    }

    /**
     * Gets the era applicable at this date.
     *
     *
     * The official ISO-8601 standard does not define eras, however `IsoChronology` does.
     * It defines two eras, 'CE' from year one onwards and 'BCE' from year zero backwards.
     * Since dates before the Julian-Gregorian cutover are not in line with history,
     * the cutover between 'BCE' and 'CE' is also not aligned with the commonly used
     * eras, often referred to using 'BC' and 'AD'.
     *
     *
     * Users of this class should typically ignore this method as it exists primarily
     * to fulfill the [ChronoLocalDate] contract where it is necessary to support
     * the Japanese calendar system.
     *
     * @return the IsoEra applicable at this date, not null
     */
    override fun getEra(): Era {
        return _localDate.era
    }

    /**
     * Gets the year field.
     *
     *
     * This method returns the primitive `int` value for the year.
     *
     *
     * The year returned by this method is proleptic as per `get(YEAR)`.
     * To obtain the year-of-era, use `get(YEAR_OF_ERA)`.
     *
     * @return the year, from MIN_YEAR to MAX_YEAR
     */
    fun getYear(): Int {
        return _localDate.year
    }

    /**
     * Gets the month-of-year field from 1 to 12.
     *
     *
     * This method returns the month as an `int` from 1 to 12.
     * Application code is frequently clearer if the enum [Month]
     * is used by calling [.getMonth].
     *
     * @return the month-of-year, from 1 to 12
     * @see .getMonth
     */
    fun getMonthValue(): Int {
        return _localDate.monthValue
    }

    /**
     * Gets the month-of-year field using the `Month` enum.
     *
     *
     * This method returns the enum [Month] for the month.
     * This avoids confusion as to what `int` values mean.
     * If you need access to the primitive `int` value then the enum
     * provides the [int value][Month.getValue].
     *
     * @return the month-of-year, not null
     * @see .getMonthValue
     */
    fun getMonth(): Month {
        return _localDate.month
    }

    /**
     * Gets the day-of-month field.
     *
     *
     * This method returns the primitive `int` value for the day-of-month.
     *
     * @return the day-of-month, from 1 to 31
     */
    fun getDayOfMonth(): Int {
        return _localDate.dayOfMonth
    }

    /**
     * Gets the day-of-year field.
     *
     *
     * This method returns the primitive `int` value for the day-of-year.
     *
     * @return the day-of-year, from 1 to 365, or 366 in a leap year
     */
    fun getDayOfYear(): Int {
        return _localDate.dayOfYear
    }

    /**
     * Gets the day-of-week field, which is an enum `DayOfWeek`.
     *
     *
     * This method returns the enum [DayOfWeek] for the day-of-week.
     * This avoids confusion as to what `int` values mean.
     * If you need access to the primitive `int` value then the enum
     * provides the [int value][DayOfWeek.getValue].
     *
     *
     * Additional information can be obtained from the `DayOfWeek`.
     * This includes textual names of the values.
     *
     * @return the day-of-week, not null
     */
    fun getDayOfWeek(): DayOfWeek {
        return _localDate.dayOfWeek
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the year is a leap year, according to the ISO proleptic
     * calendar system rules.
     *
     *
     * This method applies the current rules for leap years across the whole time-line.
     * In general, a year is a leap year if it is divisible by four without
     * remainder. However, years divisible by 100, are not leap years, with
     * the exception of years divisible by 400 which are.
     *
     *
     * For example, 1904 is a leap year it is divisible by 4.
     * 1900 was not a leap year as it is divisible by 100, however 2000 was a
     * leap year as it is divisible by 400.
     *
     *
     * The calculation is proleptic - applying the same rules into the far future and far past.
     * This is historically inaccurate, but is correct for the ISO-8601 standard.
     *
     * @return true if the year is leap, false otherwise
     */
    override fun isLeapYear(): Boolean {
        return _localDate.isLeapYear
    }

    /**
     * Returns the length of the month represented by this date.
     *
     *
     * This returns the length of the month in days.
     * For example, a date in January would return 31.
     *
     * @return the length of the month in days
     */
    override fun lengthOfMonth(): Int {
        return _localDate.lengthOfMonth()
    }

    /**
     * Returns the length of the year represented by this date.
     *
     *
     * This returns the length of the year in days, either 365 or 366.
     *
     * @return 366 if the year is leap, 365 otherwise
     */
    override fun lengthOfYear(): Int {
        return _localDate.lengthOfYear()
    }

    //-----------------------------------------------------------------------
    /**
     * Returns an adjusted copy of this date.
     *
     *
     * This returns a `LocalDate`, based on this one, with the date adjusted.
     * The adjustment takes place using the specified adjuster strategy object.
     * Read the documentation of the adjuster to understand what adjustment will be made.
     *
     *
     * A simple adjuster might simply set the one of the fields, such as the year field.
     * A more complex adjuster might set the date to the last day of the month.
     *
     *
     * A selection of common adjustments is provided in
     * [TemporalAdjusters][java.time.temporal.TemporalAdjusters].
     * These include finding the "last day of the month" and "next Wednesday".
     * Key date-time classes also implement the `TemporalAdjuster` interface,
     * such as [Month] and [MonthDay][java.time.MonthDay].
     * The adjuster is responsible for handling special cases, such as the varying
     * lengths of month and leap years.
     *
     *
     * For example this code returns a date on the last day of July:
     * <pre>
     * import static java.time.Month.*;
     * import static java.time.temporal.TemporalAdjusters.*;
     *
     * result = localDate.with(JULY).with(lastDayOfMonth());
    </pre> *
     *
     *
     * The result of this method is obtained by invoking the
     * [TemporalAdjuster.adjustInto] method on the
     * specified adjuster passing `this` as the argument.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param adjuster the adjuster to use, not null
     * @return a `LocalDate` based on `this` with the adjustment made, not null
     * @throws DateTimeException if the adjustment cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun with(adjuster: TemporalAdjuster): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.with(adjuster))
    }

    /**
     * Returns a copy of this date with the specified field set to a new value.
     *
     *
     * This returns a `LocalDate`, based on this one, with the value
     * for the specified field changed.
     * This can be used to change any supported field, such as the year, month or day-of-month.
     * If it is not possible to set the value, because the field is not supported or for
     * some other reason, an exception is thrown.
     *
     *
     * In some cases, changing the specified field can cause the resulting date to become invalid,
     * such as changing the month from 31st January to February would make the day-of-month invalid.
     * In cases like this, the field is responsible for resolving the date. Typically it will choose
     * the previous valid date, which would be the last valid day of February in this example.
     *
     *
     * If the field is a [ChronoField] then the adjustment is implemented here.
     * The supported fields behave as follows:
     *
     *  * `DAY_OF_WEEK` -
     * Returns a `LocalDate` with the specified day-of-week.
     * The date is adjusted up to 6 days forward or backward within the boundary
     * of a Monday to Sunday week.
     *  * `ALIGNED_DAY_OF_WEEK_IN_MONTH` -
     * Returns a `LocalDate` with the specified aligned-day-of-week.
     * The date is adjusted to the specified month-based aligned-day-of-week.
     * Aligned weeks are counted such that the first week of a given month starts
     * on the first day of that month.
     * This may cause the date to be moved up to 6 days into the following month.
     *  * `ALIGNED_DAY_OF_WEEK_IN_YEAR` -
     * Returns a `LocalDate` with the specified aligned-day-of-week.
     * The date is adjusted to the specified year-based aligned-day-of-week.
     * Aligned weeks are counted such that the first week of a given year starts
     * on the first day of that year.
     * This may cause the date to be moved up to 6 days into the following year.
     *  * `DAY_OF_MONTH` -
     * Returns a `LocalDate` with the specified day-of-month.
     * The month and year will be unchanged. If the day-of-month is invalid for the
     * year and month, then a `DateTimeException` is thrown.
     *  * `DAY_OF_YEAR` -
     * Returns a `LocalDate` with the specified day-of-year.
     * The year will be unchanged. If the day-of-year is invalid for the
     * year, then a `DateTimeException` is thrown.
     *  * `EPOCH_DAY` -
     * Returns a `LocalDate` with the specified epoch-day.
     * This completely replaces the date and is equivalent to [.ofEpochDay].
     *  * `ALIGNED_WEEK_OF_MONTH` -
     * Returns a `LocalDate` with the specified aligned-week-of-month.
     * Aligned weeks are counted such that the first week of a given month starts
     * on the first day of that month.
     * This adjustment moves the date in whole week chunks to match the specified week.
     * The result will have the same day-of-week as this date.
     * This may cause the date to be moved into the following month.
     *  * `ALIGNED_WEEK_OF_YEAR` -
     * Returns a `LocalDate` with the specified aligned-week-of-year.
     * Aligned weeks are counted such that the first week of a given year starts
     * on the first day of that year.
     * This adjustment moves the date in whole week chunks to match the specified week.
     * The result will have the same day-of-week as this date.
     * This may cause the date to be moved into the following year.
     *  * `MONTH_OF_YEAR` -
     * Returns a `LocalDate` with the specified month-of-year.
     * The year will be unchanged. The day-of-month will also be unchanged,
     * unless it would be invalid for the new month and year. In that case, the
     * day-of-month is adjusted to the maximum valid value for the new month and year.
     *  * `PROLEPTIC_MONTH` -
     * Returns a `LocalDate` with the specified proleptic-month.
     * The day-of-month will be unchanged, unless it would be invalid for the new month
     * and year. In that case, the day-of-month is adjusted to the maximum valid value
     * for the new month and year.
     *  * `YEAR_OF_ERA` -
     * Returns a `LocalDate` with the specified year-of-era.
     * The era and month will be unchanged. The day-of-month will also be unchanged,
     * unless it would be invalid for the new month and year. In that case, the
     * day-of-month is adjusted to the maximum valid value for the new month and year.
     *  * `YEAR` -
     * Returns a `LocalDate` with the specified year.
     * The month will be unchanged. The day-of-month will also be unchanged,
     * unless it would be invalid for the new month and year. In that case, the
     * day-of-month is adjusted to the maximum valid value for the new month and year.
     *  * `ERA` -
     * Returns a `LocalDate` with the specified era.
     * The year-of-era and month will be unchanged. The day-of-month will also be unchanged,
     * unless it would be invalid for the new month and year. In that case, the
     * day-of-month is adjusted to the maximum valid value for the new month and year.
     *
     *
     *
     * In all cases, if the new value is outside the valid range of values for the field
     * then a `DateTimeException` will be thrown.
     *
     *
     * All other `ChronoField` instances will throw an `UnsupportedTemporalTypeException`.
     *
     *
     * If the field is not a `ChronoField`, then the result of this method
     * is obtained by invoking `TemporalField.adjustInto(Temporal, long)`
     * passing `this` as the argument. In this case, the field determines
     * whether and how to adjust the instant.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param field  the field to set in the result, not null
     * @param newValue  the new value of the field in the result
     * @return a `LocalDate` based on `this` with the specified field set, not null
     * @throws DateTimeException if the field cannot be set
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun with(field: TemporalField, newValue: Long): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.with(field, newValue))
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this `LocalDate` with the year altered.
     *
     *
     * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param year  the year to set in the result, from MIN_YEAR to MAX_YEAR
     * @return a `LocalDate` based on this date with the requested year, not null
     * @throws DateTimeException if the year value is invalid
     */
    fun withYear(year: Int): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.withYear(year))
    }

    /**
     * Returns a copy of this `LocalDate` with the month-of-year altered.
     *
     *
     * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param month  the month-of-year to set in the result, from 1 (January) to 12 (December)
     * @return a `LocalDate` based on this date with the requested month, not null
     * @throws DateTimeException if the month-of-year value is invalid
     */
    fun withMonth(month: Int): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.withMonth(month))
    }

    /**
     * Returns a copy of this `LocalDate` with the day-of-month altered.
     *
     *
     * If the resulting date is invalid, an exception is thrown.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfMonth  the day-of-month to set in the result, from 1 to 28-31
     * @return a `LocalDate` based on this date with the requested day, not null
     * @throws DateTimeException if the day-of-month value is invalid,
     * or if the day-of-month is invalid for the month-year
     */
    fun withDayOfMonth(dayOfMonth: Int): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.withDayOfMonth(dayOfMonth))
    }

    /**
     * Returns a copy of this `LocalDate` with the day-of-year altered.
     *
     *
     * If the resulting date is invalid, an exception is thrown.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfYear  the day-of-year to set in the result, from 1 to 365-366
     * @return a `LocalDate` based on this date with the requested day, not null
     * @throws DateTimeException if the day-of-year value is invalid,
     * or if the day-of-year is invalid for the year
     */
    fun withDayOfYear(dayOfYear: Int): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.withDayOfYear(dayOfYear))
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this date with the specified amount added.
     *
     *
     * This returns a `LocalDate`, based on this one, with the specified amount added.
     * The amount is typically [Period] but may be any other type implementing
     * the [TemporalAmount] interface.
     *
     *
     * The calculation is delegated to the amount object by calling
     * [TemporalAmount.addTo]. The amount implementation is free
     * to implement the addition in any way it wishes, however it typically
     * calls back to [.plus]. Consult the documentation
     * of the amount implementation to determine if it can be successfully added.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param amountToAdd  the amount to add, not null
     * @return a `LocalDate` based on this date with the addition made, not null
     * @throws DateTimeException if the addition cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun plus(amountToAdd: TemporalAmount): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.plus(amountToAdd))
    }

    /**
     * Returns a copy of this date with the specified amount added.
     *
     *
     * This returns a `LocalDate`, based on this one, with the amount
     * in terms of the unit added. If it is not possible to add the amount, because the
     * unit is not supported or for some other reason, an exception is thrown.
     *
     *
     * In some cases, adding the amount can cause the resulting date to become invalid.
     * For example, adding one month to 31st January would result in 31st February.
     * In cases like this, the unit is responsible for resolving the date.
     * Typically it will choose the previous valid date, which would be the last valid
     * day of February in this example.
     *
     *
     * If the field is a [ChronoUnit] then the addition is implemented here.
     * The supported fields behave as follows:
     *
     *  * `DAYS` -
     * Returns a `LocalDate` with the specified number of days added.
     * This is equivalent to [.plusDays].
     *  * `WEEKS` -
     * Returns a `LocalDate` with the specified number of weeks added.
     * This is equivalent to [.plusWeeks] and uses a 7 day week.
     *  * `MONTHS` -
     * Returns a `LocalDate` with the specified number of months added.
     * This is equivalent to [.plusMonths].
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     *  * `YEARS` -
     * Returns a `LocalDate` with the specified number of years added.
     * This is equivalent to [.plusYears].
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     *  * `DECADES` -
     * Returns a `LocalDate` with the specified number of decades added.
     * This is equivalent to calling [.plusYears] with the amount
     * multiplied by 10.
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     *  * `CENTURIES` -
     * Returns a `LocalDate` with the specified number of centuries added.
     * This is equivalent to calling [.plusYears] with the amount
     * multiplied by 100.
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     *  * `MILLENNIA` -
     * Returns a `LocalDate` with the specified number of millennia added.
     * This is equivalent to calling [.plusYears] with the amount
     * multiplied by 1,000.
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     *  * `ERAS` -
     * Returns a `LocalDate` with the specified number of eras added.
     * Only two eras are supported so the amount must be one, zero or minus one.
     * If the amount is non-zero then the year is changed such that the year-of-era
     * is unchanged.
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     *
     *
     *
     * All other `ChronoUnit` instances will throw an `UnsupportedTemporalTypeException`.
     *
     *
     * If the field is not a `ChronoUnit`, then the result of this method
     * is obtained by invoking `TemporalUnit.addTo(Temporal, long)`
     * passing `this` as the argument. In this case, the unit determines
     * whether and how to perform the addition.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param amountToAdd  the amount of the unit to add to the result, may be negative
     * @param unit  the unit of the amount to add, not null
     * @return a `LocalDate` based on this date with the specified amount added, not null
     * @throws DateTimeException if the addition cannot be made
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun plus(amountToAdd: Long, unit: TemporalUnit): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.plus(amountToAdd, unit))
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this `LocalDate` with the specified number of years added.
     *
     *
     * This method adds the specified amount to the years field in three steps:
     *
     *  1. Add the input years to the year field
     *  1. Check if the resulting date would be invalid
     *  1. Adjust the day-of-month to the last valid day if necessary
     *
     *
     *
     * For example, 2008-02-29 (leap year) plus one year would result in the
     * invalid date 2009-02-29 (standard year). Instead of returning an invalid
     * result, the last valid day of the month, 2009-02-28, is selected instead.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param yearsToAdd  the years to add, may be negative
     * @return a `LocalDate` based on this date with the years added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun plusYears(yearsToAdd: Long): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.plusYears(yearsToAdd))
    }

    /**
     * Returns a copy of this `LocalDate` with the specified number of months added.
     *
     *
     * This method adds the specified amount to the months field in three steps:
     *
     *  1. Add the input months to the month-of-year field
     *  1. Check if the resulting date would be invalid
     *  1. Adjust the day-of-month to the last valid day if necessary
     *
     *
     *
     * For example, 2007-03-31 plus one month would result in the invalid date
     * 2007-04-31. Instead of returning an invalid result, the last valid day
     * of the month, 2007-04-30, is selected instead.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthsToAdd  the months to add, may be negative
     * @return a `LocalDate` based on this date with the months added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun plusMonths(monthsToAdd: Long): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.plusMonths(monthsToAdd))
    }

    /**
     * Returns a copy of this `LocalDate` with the specified number of weeks added.
     *
     *
     * This method adds the specified amount in weeks to the days field incrementing
     * the month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     *
     *
     * For example, 2008-12-31 plus one week would result in 2009-01-07.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeksToAdd  the weeks to add, may be negative
     * @return a `LocalDate` based on this date with the weeks added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun plusWeeks(weeksToAdd: Long): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.plusWeeks(weeksToAdd))
    }

    /**
     * Returns a copy of this `LocalDate` with the specified number of days added.
     *
     *
     * This method adds the specified amount to the days field incrementing the
     * month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     *
     *
     * For example, 2008-12-31 plus one day would result in 2009-01-01.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param daysToAdd  the days to add, may be negative
     * @return a `LocalDate` based on this date with the days added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun plusDays(daysToAdd: Long): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.plusDays(daysToAdd))
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this date with the specified amount subtracted.
     *
     *
     * This returns a `LocalDate`, based on this one, with the specified amount subtracted.
     * The amount is typically [Period] but may be any other type implementing
     * the [TemporalAmount] interface.
     *
     *
     * The calculation is delegated to the amount object by calling
     * [TemporalAmount.subtractFrom]. The amount implementation is free
     * to implement the subtraction in any way it wishes, however it typically
     * calls back to [.minus]. Consult the documentation
     * of the amount implementation to determine if it can be successfully subtracted.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param amountToSubtract  the amount to subtract, not null
     * @return a `LocalDate` based on this date with the subtraction made, not null
     * @throws DateTimeException if the subtraction cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun minus(amountToSubtract: TemporalAmount): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.minus(amountToSubtract))
    }

    /**
     * Returns a copy of this date with the specified amount subtracted.
     *
     *
     * This returns a `LocalDate`, based on this one, with the amount
     * in terms of the unit subtracted. If it is not possible to subtract the amount,
     * because the unit is not supported or for some other reason, an exception is thrown.
     *
     *
     * This method is equivalent to [.plus] with the amount negated.
     * See that method for a full description of how addition, and thus subtraction, works.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param amountToSubtract  the amount of the unit to subtract from the result, may be negative
     * @param unit  the unit of the amount to subtract, not null
     * @return a `LocalDate` based on this date with the specified amount subtracted, not null
     * @throws DateTimeException if the subtraction cannot be made
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun minus(amountToSubtract: Long, unit: TemporalUnit): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.minus(amountToSubtract, unit))
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this `LocalDate` with the specified number of years subtracted.
     *
     *
     * This method subtracts the specified amount from the years field in three steps:
     *
     *  1. Subtract the input years from the year field
     *  1. Check if the resulting date would be invalid
     *  1. Adjust the day-of-month to the last valid day if necessary
     *
     *
     *
     * For example, 2008-02-29 (leap year) minus one year would result in the
     * invalid date 2007-02-29 (standard year). Instead of returning an invalid
     * result, the last valid day of the month, 2007-02-28, is selected instead.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param yearsToSubtract  the years to subtract, may be negative
     * @return a `LocalDate` based on this date with the years subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun minusYears(yearsToSubtract: Long): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.minusYears(yearsToSubtract))
    }

    /**
     * Returns a copy of this `LocalDate` with the specified number of months subtracted.
     *
     *
     * This method subtracts the specified amount from the months field in three steps:
     *
     *  1. Subtract the input months from the month-of-year field
     *  1. Check if the resulting date would be invalid
     *  1. Adjust the day-of-month to the last valid day if necessary
     *
     *
     *
     * For example, 2007-03-31 minus one month would result in the invalid date
     * 2007-02-31. Instead of returning an invalid result, the last valid day
     * of the month, 2007-02-28, is selected instead.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthsToSubtract  the months to subtract, may be negative
     * @return a `LocalDate` based on this date with the months subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun minusMonths(monthsToSubtract: Long): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.minusMonths(monthsToSubtract))
    }

    /**
     * Returns a copy of this `LocalDate` with the specified number of weeks subtracted.
     *
     *
     * This method subtracts the specified amount in weeks from the days field decrementing
     * the month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     *
     *
     * For example, 2009-01-07 minus one week would result in 2008-12-31.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeksToSubtract  the weeks to subtract, may be negative
     * @return a `LocalDate` based on this date with the weeks subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun minusWeeks(weeksToSubtract: Long): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.minusWeeks(weeksToSubtract))
    }

    /**
     * Returns a copy of this `LocalDate` with the specified number of days subtracted.
     *
     *
     * This method subtracts the specified amount from the days field decrementing the
     * month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     *
     *
     * For example, 2009-01-01 minus one day would result in 2008-12-31.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param daysToSubtract  the days to subtract, may be negative
     * @return a `LocalDate` based on this date with the days subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun minusDays(daysToSubtract: Long): ImmutableLocalDate {
        return ImmutableLocalDate(_localDate.minusDays(daysToSubtract))
    }

    //-----------------------------------------------------------------------
    /**
     * Queries this date using the specified query.
     *
     *
     * This queries this date using the specified query strategy object.
     * The `TemporalQuery` object defines the logic to be used to
     * obtain the result. Read the documentation of the query to understand
     * what the result of this method will be.
     *
     *
     * The result of this method is obtained by invoking the
     * [TemporalQuery.queryFrom] method on the
     * specified query passing `this` as the argument.
     *
     * @param <R> the type of the result
     * @param query  the query to invoke, not null
     * @return the query result, null may be returned (defined by the query)
     * @throws DateTimeException if unable to query (defined by the query)
     * @throws ArithmeticException if numeric overflow occurs (defined by the query)
    </R> */
    override fun <R> query(query: TemporalQuery<R>): R {
        return _localDate.query(query)
    }

    /**
     * Adjusts the specified temporal object to have the same date as this object.
     *
     *
     * This returns a temporal object of the same observable type as the input
     * with the date changed to be the same as this.
     *
     *
     * The adjustment is equivalent to using [Temporal.with]
     * passing [ChronoField.EPOCH_DAY] as the field.
     *
     *
     * In most cases, it is clearer to reverse the calling pattern by using
     * [Temporal.with]:
     * <pre>
     * // these two lines are equivalent, but the second approach is recommended
     * temporal = thisLocalDate.adjustInto(temporal);
     * temporal = temporal.with(thisLocalDate);
    </pre> *
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param temporal  the target object to be adjusted, not null
     * @return the adjusted object, not null
     * @throws DateTimeException if unable to make the adjustment
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun adjustInto(temporal: Temporal): Temporal {
        return _localDate.adjustInto(temporal)
    }

    /**
     * Calculates the amount of time until another date in terms of the specified unit.
     *
     *
     * This calculates the amount of time between two `LocalDate`
     * objects in terms of a single `TemporalUnit`.
     * The start and end points are `this` and the specified date.
     * The result will be negative if the end is before the start.
     * The `Temporal` passed to this method is converted to a
     * `LocalDate` using [.from].
     * For example, the amount in days between two dates can be calculated
     * using `startDate.until(endDate, DAYS)`.
     *
     *
     * The calculation returns a whole number, representing the number of
     * complete units between the two dates.
     * For example, the amount in months between 2012-06-15 and 2012-08-14
     * will only be one month as it is one day short of two months.
     *
     *
     * There are two equivalent ways of using this method.
     * The first is to invoke this method.
     * The second is to use [TemporalUnit.between]:
     * <pre>
     * // these two lines are equivalent
     * amount = start.until(end, MONTHS);
     * amount = MONTHS.between(start, end);
    </pre> *
     * The choice should be made based on which makes the code more readable.
     *
     *
     * The calculation is implemented in this method for [ChronoUnit].
     * The units `DAYS`, `WEEKS`, `MONTHS`, `YEARS`,
     * `DECADES`, `CENTURIES`, `MILLENNIA` and `ERAS`
     * are supported. Other `ChronoUnit` values will throw an exception.
     *
     *
     * If the unit is not a `ChronoUnit`, then the result of this method
     * is obtained by invoking `TemporalUnit.between(Temporal, Temporal)`
     * passing `this` as the first argument and the converted input temporal
     * as the second argument.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param endExclusive  the end date, exclusive, which is converted to a `LocalDate`, not null
     * @param unit  the unit to measure the amount in, not null
     * @return the amount of time between this date and the end date
     * @throws DateTimeException if the amount cannot be calculated, or the end
     * temporal cannot be converted to a `LocalDate`
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun until(endExclusive: Temporal, unit: TemporalUnit): Long {
        return _localDate.until(endExclusive, unit)
    }

    /**
     * Calculates the period between this date and another date as a `Period`.
     *
     *
     * This calculates the period between two dates in terms of years, months and days.
     * The start and end points are `this` and the specified date.
     * The result will be negative if the end is before the start.
     * The negative sign will be the same in each of year, month and day.
     *
     *
     * The calculation is performed using the ISO calendar system.
     * If necessary, the input date will be converted to ISO.
     *
     *
     * The start date is included, but the end date is not.
     * The period is calculated by removing complete months, then calculating
     * the remaining number of days, adjusting to ensure that both have the same sign.
     * The number of months is then normalized into years and months based on a 12 month year.
     * A month is considered to be complete if the end day-of-month is greater
     * than or equal to the start day-of-month.
     * For example, from `2010-01-15` to `2011-03-18` is "1 year, 2 months and 3 days".
     *
     *
     * There are two equivalent ways of using this method.
     * The first is to invoke this method.
     * The second is to use [Period.between]:
     * <pre>
     * // these two lines are equivalent
     * period = start.until(end);
     * period = Period.between(start, end);
    </pre> *
     * The choice should be made based on which makes the code more readable.
     *
     * @param endDateExclusive  the end date, exclusive, which may be in any chronology, not null
     * @return the period between this date and the end date, not null
     */
    override fun until(endDateExclusive: ChronoLocalDate): Period {
        return _localDate.until(endDateExclusive)
    }

    /**
     * Formats this date using the specified formatter.
     *
     *
     * This date will be passed to the formatter to produce a string.
     *
     * @param formatter  the formatter to use, not null
     * @return the formatted date string, not null
     * @throws DateTimeException if an error occurs during printing
     */
    override fun format(formatter: DateTimeFormatter): String {
        return _localDate.format(formatter)
    }

    //-----------------------------------------------------------------------
    /**
     * Combines this date with a time to create a `LocalDateTime`.
     *
     *
     * This returns a `LocalDateTime` formed from this date at the specified time.
     * All possible combinations of date and time are valid.
     *
     * @param time  the time to combine with, not null
     * @return the local date-time formed from this date and the specified time, not null
     */
    override fun atTime(time: LocalTime): LocalDateTime {
        return _localDate.atTime(time)
    }

    /**
     * Combines this date with a time to create a `LocalDateTime`.
     *
     *
     * This returns a `LocalDateTime` formed from this date at the
     * specified hour and minute.
     * The seconds and nanosecond fields will be set to zero.
     * The individual time fields must be within their valid range.
     * All possible combinations of date and time are valid.
     *
     * @param hour  the hour-of-day to use, from 0 to 23
     * @param minute  the minute-of-hour to use, from 0 to 59
     * @return the local date-time formed from this date and the specified time, not null
     * @throws DateTimeException if the value of any field is out of range
     */
    fun atTime(hour: Int, minute: Int): LocalDateTime {
        return _localDate.atTime(hour, minute)
    }

    /**
     * Combines this date with a time to create a `LocalDateTime`.
     *
     *
     * This returns a `LocalDateTime` formed from this date at the
     * specified hour, minute and second.
     * The nanosecond field will be set to zero.
     * The individual time fields must be within their valid range.
     * All possible combinations of date and time are valid.
     *
     * @param hour  the hour-of-day to use, from 0 to 23
     * @param minute  the minute-of-hour to use, from 0 to 59
     * @param second  the second-of-minute to represent, from 0 to 59
     * @return the local date-time formed from this date and the specified time, not null
     * @throws DateTimeException if the value of any field is out of range
     */
    fun atTime(hour: Int, minute: Int, second: Int): LocalDateTime {
        return _localDate.atTime(hour, minute, second)
    }

    /**
     * Combines this date with a time to create a `LocalDateTime`.
     *
     *
     * This returns a `LocalDateTime` formed from this date at the
     * specified hour, minute, second and nanosecond.
     * The individual time fields must be within their valid range.
     * All possible combinations of date and time are valid.
     *
     * @param hour  the hour-of-day to use, from 0 to 23
     * @param minute  the minute-of-hour to use, from 0 to 59
     * @param second  the second-of-minute to represent, from 0 to 59
     * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
     * @return the local date-time formed from this date and the specified time, not null
     * @throws DateTimeException if the value of any field is out of range
     */
    fun atTime(hour: Int, minute: Int, second: Int, nanoOfSecond: Int): LocalDateTime {
        return _localDate.atTime(hour, minute, second, nanoOfSecond)
    }

    /**
     * Combines this date with an offset time to create an `OffsetDateTime`.
     *
     *
     * This returns an `OffsetDateTime` formed from this date at the specified time.
     * All possible combinations of date and time are valid.
     *
     * @param time  the time to combine with, not null
     * @return the offset date-time formed from this date and the specified time, not null
     */
    fun atTime(time: OffsetTime): OffsetDateTime {
        return _localDate.atTime(time)
    }

    /**
     * Combines this date with the time of midnight to create a `LocalDateTime`
     * at the start of this date.
     *
     *
     * This returns a `LocalDateTime` formed from this date at the time of
     * midnight, 00:00, at the start of this date.
     *
     * @return the local date-time of midnight at the start of this date, not null
     */
    fun atStartOfDay(): LocalDateTime {
        return _localDate.atStartOfDay()
    }

    /**
     * Returns a zoned date-time from this date at the earliest valid time according
     * to the rules in the time-zone.
     *
     *
     * Time-zone rules, such as daylight savings, mean that not every local date-time
     * is valid for the specified zone, thus the local date-time may not be midnight.
     *
     *
     * In most cases, there is only one valid offset for a local date-time.
     * In the case of an overlap, there are two valid offsets, and the earlier one is used,
     * corresponding to the first occurrence of midnight on the date.
     * In the case of a gap, the zoned date-time will represent the instant just after the gap.
     *
     *
     * If the zone ID is a [ZoneOffset], then the result always has a time of midnight.
     *
     *
     * To convert to a specific time in a given time-zone call [.atTime]
     * followed by [LocalDateTime.atZone].
     *
     * @param zone  the zone ID to use, not null
     * @return the zoned date-time formed from this date and the earliest valid time for the zone, not null
     */
    fun atStartOfDay(zone: ZoneId): ZonedDateTime {
        return _localDate.atStartOfDay(zone)
    }

    //-----------------------------------------------------------------------
    override fun toEpochDay(): Long {
        return _localDate.toEpochDay()
    }

    fun toLocalDate(): LocalDate {
        return LocalDate.of(_localDate.year, _localDate.month, _localDate.dayOfMonth)
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this date to another date.
     *
     *
     * The comparison is primarily based on the date, from earliest to latest.
     * It is "consistent with equals", as defined by [Comparable].
     *
     *
     * If all the dates being compared are instances of `LocalDate`,
     * then the comparison will be entirely based on the date.
     * If some dates being compared are in different chronologies, then the
     * chronology is also considered, see [java.time.chrono.ChronoLocalDate.compareTo].
     *
     * @param other  the other date to compare to, not null
     * @return the comparator value, negative if less, positive if greater
     */
    override fun compareTo(other: ChronoLocalDate): Int {
        return _localDate.compareTo(other)
    }

    /**
     * Checks if this date is after the specified date.
     *
     *
     * This checks to see if this date represents a point on the
     * local time-line after the other date.
     * <pre>
     * LocalDate a = LocalDate.of(2012, 6, 30);
     * LocalDate b = LocalDate.of(2012, 7, 1);
     * a.isAfter(b) == false
     * a.isAfter(a) == false
     * b.isAfter(a) == true
    </pre> *
     *
     *
     * This method only considers the position of the two dates on the local time-line.
     * It does not take into account the chronology, or calendar system.
     * This is different from the comparison in [.compareTo],
     * but is the same approach as [ChronoLocalDate.timeLineOrder].
     *
     * @param other  the other date to compare to, not null
     * @return true if this date is after the specified date
     */
    // override for Javadoc and performance
    override fun isAfter(other: ChronoLocalDate): Boolean {
        return _localDate.isAfter(other)
    }

    /**
     * Checks if this date is before the specified date.
     *
     *
     * This checks to see if this date represents a point on the
     * local time-line before the other date.
     * <pre>
     * LocalDate a = LocalDate.of(2012, 6, 30);
     * LocalDate b = LocalDate.of(2012, 7, 1);
     * a.isBefore(b) == true
     * a.isBefore(a) == false
     * b.isBefore(a) == false
    </pre> *
     *
     *
     * This method only considers the position of the two dates on the local time-line.
     * It does not take into account the chronology, or calendar system.
     * This is different from the comparison in [.compareTo],
     * but is the same approach as [ChronoLocalDate.timeLineOrder].
     *
     * @param other  the other date to compare to, not null
     * @return true if this date is before the specified date
     */
    override fun isBefore(other: ChronoLocalDate): Boolean {
        return _localDate.isBefore(other)
    }

    /**
     * Checks if this date is equal to the specified date.
     *
     *
     * This checks to see if this date represents the same point on the
     * local time-line as the other date.
     * <pre>
     * LocalDate a = LocalDate.of(2012, 6, 30);
     * LocalDate b = LocalDate.of(2012, 7, 1);
     * a.isEqual(b) == false
     * a.isEqual(a) == true
     * b.isEqual(a) == false
    </pre> *
     *
     *
     * This method only considers the position of the two dates on the local time-line.
     * It does not take into account the chronology, or calendar system.
     * This is different from the comparison in [.compareTo]
     * but is the same approach as [ChronoLocalDate.timeLineOrder].
     *
     * @param other  the other date to compare to, not null
     * @return true if this date is equal to the specified date
     */
    override fun isEqual(other: ChronoLocalDate): Boolean {
        return _localDate.isEqual(other)
    }

    //-----------------------------------------------------------------------
    /**
     * Outputs this date as a `String`, such as `2007-12-03`.
     *
     *
     * The output will be in the ISO-8601 format `uuuu-MM-dd`.
     *
     * @return a string representation of this date, not null
     */
    override fun toString(): String {
        return _localDate.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImmutableLocalDate

        if (_localDate != other._localDate) return false

        return true
    }

    override fun hashCode(): Int {
        return _localDate.hashCode()
    }

    companion object {
        /**
         * The minimum supported {@code LocalDate}, '-999999999-01-01'.
         * This could be used by an application as a "far past" date.
         */
        val MIN = LocalDate.MIN

        /**
         * The maximum supported `LocalDate`, '+999999999-12-31'.
         * This could be used by an application as a "far future" date.
         */
        val MAX = LocalDate.MAX

        //-----------------------------------------------------------------------
        /**
         * Obtains the current date from the system clock in the default time-zone.
         *
         *
         * This will query the [system clock][Clock.systemDefaultZone] in the default
         * time-zone to obtain the current date.
         *
         *
         * Using this method will prevent the ability to use an alternate clock for testing
         * because the clock is hard-coded.
         *
         * @return the current date using the system clock and default time-zone, not null
         */
        fun now(): ImmutableLocalDate {
            return ImmutableLocalDate(LocalDate.now())
        }

        /**
         * Obtains the current date from the system clock in the specified time-zone.
         *
         *
         * This will query the [system clock][Clock.system] to obtain the current date.
         * Specifying the time-zone avoids dependence on the default time-zone.
         *
         *
         * Using this method will prevent the ability to use an alternate clock for testing
         * because the clock is hard-coded.
         *
         * @param zone  the zone ID to use, not null
         * @return the current date using the system clock, not null
         */
        fun now(zone: ZoneId): ImmutableLocalDate {
            return ImmutableLocalDate(LocalDate.now(zone))
        }

        /**
         * Obtains the current date from the specified clock.
         *
         *
         * This will query the specified clock to obtain the current date - today.
         * Using this method allows the use of an alternate clock for testing.
         * The alternate clock may be introduced using [dependency injection][Clock].
         *
         * @param clock  the clock to use, not null
         * @return the current date, not null
         */
        fun now(clock: Clock): ImmutableLocalDate {
            return ImmutableLocalDate(LocalDate.now(clock))
        }

        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `LocalDate` from a year, month and day.
         *
         *
         * This returns a `LocalDate` with the specified year, month and day-of-month.
         * The day must be valid for the year and month, otherwise an exception will be thrown.
         *
         * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
         * @param month  the month-of-year to represent, not null
         * @param dayOfMonth  the day-of-month to represent, from 1 to 31
         * @return the local date, not null
         * @throws DateTimeException if the value of any field is out of range,
         * or if the day-of-month is invalid for the month-year
         */
        fun of(year: Int, month: Month, dayOfMonth: Int): ImmutableLocalDate {
            return ImmutableLocalDate(LocalDate.of(year, month, dayOfMonth))
        }

        /**
         * Obtains an instance of `LocalDate` from a year, month and day.
         *
         *
         * This returns a `LocalDate` with the specified year, month and day-of-month.
         * The day must be valid for the year and month, otherwise an exception will be thrown.
         *
         * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
         * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
         * @param dayOfMonth  the day-of-month to represent, from 1 to 31
         * @return the local date, not null
         * @throws DateTimeException if the value of any field is out of range,
         * or if the day-of-month is invalid for the month-year
         */
        fun of(year: Int, month: Int, dayOfMonth: Int): ImmutableLocalDate {
            return ImmutableLocalDate(LocalDate.of(year, month, dayOfMonth))
        }

        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `LocalDate` from a year and day-of-year.
         *
         *
         * This returns a `LocalDate` with the specified year and day-of-year.
         * The day-of-year must be valid for the year, otherwise an exception will be thrown.
         *
         * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
         * @param dayOfYear  the day-of-year to represent, from 1 to 366
         * @return the local date, not null
         * @throws DateTimeException if the value of any field is out of range,
         * or if the day-of-year is invalid for the year
         */
        fun ofYearDay(year: Int, dayOfYear: Int): ImmutableLocalDate {
            return ImmutableLocalDate(LocalDate.ofYearDay(year, dayOfYear))
        }

        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `LocalDate` from the epoch day count.
         *
         *
         * This returns a `LocalDate` with the specified epoch-day.
         * The [EPOCH_DAY][ChronoField.EPOCH_DAY] is a simple incrementing count
         * of days where day 0 is 1970-01-01. Negative numbers represent earlier days.
         *
         * @param epochDay  the Epoch Day to convert, based on the epoch 1970-01-01
         * @return the local date, not null
         * @throws DateTimeException if the epoch day exceeds the supported date range
         */
        fun ofEpochDay(epochDay: Long): ImmutableLocalDate {
            return ImmutableLocalDate(LocalDate.ofEpochDay(epochDay))
        }

        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `LocalDate` from a temporal object.
         *
         *
         * This obtains a local date based on the specified temporal.
         * A `TemporalAccessor` represents an arbitrary set of date and time information,
         * which this factory converts to an instance of `LocalDate`.
         *
         *
         * The conversion uses the [TemporalQueries.localDate] query, which relies
         * on extracting the [EPOCH_DAY][ChronoField.EPOCH_DAY] field.
         *
         *
         * This method matches the signature of the functional interface [TemporalQuery]
         * allowing it to be used as a query via method reference, `LocalDate::from`.
         *
         * @param temporal  the temporal object to convert, not null
         * @return the local date, not null
         * @throws DateTimeException if unable to convert to a `LocalDate`
         */
        fun from(temporal: TemporalAccessor): ImmutableLocalDate {
            return ImmutableLocalDate(LocalDate.from(temporal))
        }

        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `LocalDate` from a text string such as `2007-12-03`.
         *
         *
         * The string must represent a valid date and is parsed using
         * [java.time.format.DateTimeFormatter.ISO_LOCAL_DATE].
         *
         * @param text  the text to parse such as "2007-12-03", not null
         * @return the parsed local date, not null
         * @throws DateTimeParseException if the text cannot be parsed
         */
        fun parse(text: CharSequence): ImmutableLocalDate {
            return ImmutableLocalDate(LocalDate.parse(text))
        }

        /**
         * Obtains an instance of `LocalDate` from a text string using a specific formatter.
         *
         *
         * The text is parsed using the formatter, returning a date.
         *
         * @param text  the text to parse, not null
         * @param formatter  the formatter to use, not null
         * @return the parsed local date, not null
         * @throws DateTimeParseException if the text cannot be parsed
         */
        fun parse(text: CharSequence, formatter: DateTimeFormatter): ImmutableLocalDate {
            return ImmutableLocalDate(LocalDate.parse(text, formatter))
        }
    }
}
