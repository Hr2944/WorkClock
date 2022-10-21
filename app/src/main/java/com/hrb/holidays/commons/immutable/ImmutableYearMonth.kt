package com.hrb.holidays.commons.immutable

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.*

@Immutable
@Parcelize
class ImmutableYearMonth(private val _yearMonth: YearMonth) : Temporal, TemporalAdjuster,
    Comparable<ImmutableYearMonth>, Parcelable {

    //-----------------------------------------------------------------------
    /**
     * Checks if the specified field is supported.
     *
     *
     * This checks if this year-month can be queried for the specified field.
     * If false, then calling the [range][.range],
     * [get][.get] and [.with]
     * methods will throw an exception.
     *
     *
     * If the field is a [ChronoField] then the query is implemented here.
     * The supported fields are:
     *
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
     * @return true if the field is supported on this year-month, false if not
     */
    override fun isSupported(field: TemporalField?): Boolean {
        return _yearMonth.isSupported(field)
    }

    /**
     * Checks if the specified unit is supported.
     *
     *
     * This checks if the specified unit can be added to, or subtracted from, this year-month.
     * If false, then calling the [.plus] and
     * [minus][.minus] methods will throw an exception.
     *
     *
     * If the unit is a [ChronoUnit] then the query is implemented here.
     * The supported units are:
     *
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
        return _yearMonth.isSupported(unit)
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the range of valid values for the specified field.
     *
     *
     * The range object expresses the minimum and maximum valid values for a field.
     * This year-month is used to enhance the accuracy of the returned range.
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
        return _yearMonth.range(field)
    }

    /**
     * Gets the value of the specified field from this year-month as an `int`.
     *
     *
     * This queries this year-month for the value of the specified field.
     * The returned value will always be within the valid range of values for the field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     *
     *
     * If the field is a [ChronoField] then the query is implemented here.
     * The [supported fields][.isSupported] will return valid
     * values based on this year-month, except `PROLEPTIC_MONTH` which is too
     * large to fit in an `int` and throw a `DateTimeException`.
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
    override fun get(field: TemporalField): Int {
        return _yearMonth.get(field)
    }

    /**
     * Gets the value of the specified field from this year-month as a `long`.
     *
     *
     * This queries this year-month for the value of the specified field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     *
     *
     * If the field is a [ChronoField] then the query is implemented here.
     * The [supported fields][.isSupported] will return valid
     * values based on this year-month.
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
        return _yearMonth.getLong(field)
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the year field.
     *
     *
     * This method returns the primitive `int` value for the year.
     *
     *
     * The year returned by this method is proleptic as per `get(YEAR)`.
     *
     * @return the year, from MIN_YEAR to MAX_YEAR
     */
    fun getYear(): Int {
        return _yearMonth.year
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
        return _yearMonth.monthValue
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
        return _yearMonth.month
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
    fun isLeapYear(): Boolean {
        return _yearMonth.isLeapYear
    }

    /**
     * Checks if the day-of-month is valid for this year-month.
     *
     *
     * This method checks whether this year and month and the input day form
     * a valid date.
     *
     * @param dayOfMonth  the day-of-month to validate, from 1 to 31, invalid value returns false
     * @return true if the day is valid for this year-month
     */
    fun isValidDay(dayOfMonth: Int): Boolean {
        return _yearMonth.isValidDay(dayOfMonth)
    }

    /**
     * Returns the length of the month, taking account of the year.
     *
     *
     * This returns the length of the month in days.
     * For example, a date in January would return 31.
     *
     * @return the length of the month in days, from 28 to 31
     */
    fun lengthOfMonth(): Int {
        return _yearMonth.lengthOfMonth()
    }

    /**
     * Returns the length of the year.
     *
     *
     * This returns the length of the year in days, either 365 or 366.
     *
     * @return 366 if the year is leap, 365 otherwise
     */
    fun lengthOfYear(): Int {
        return _yearMonth.lengthOfYear()
    }

    //-----------------------------------------------------------------------
    /**
     * Returns an adjusted copy of this year-month.
     *
     *
     * This returns a `YearMonth`, based on this one, with the year-month adjusted.
     * The adjustment takes place using the specified adjuster strategy object.
     * Read the documentation of the adjuster to understand what adjustment will be made.
     *
     *
     * A simple adjuster might simply set the one of the fields, such as the year field.
     * A more complex adjuster might set the year-month to the next month that
     * Halley's comet will pass the Earth.
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
     * @return a `YearMonth` based on `this` with the adjustment made, not null
     * @throws DateTimeException if the adjustment cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun with(adjuster: TemporalAdjuster): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.with(adjuster))
    }

    /**
     * Returns a copy of this year-month with the specified field set to a new value.
     *
     *
     * This returns a `YearMonth`, based on this one, with the value
     * for the specified field changed.
     * This can be used to change any supported field, such as the year or month.
     * If it is not possible to set the value, because the field is not supported or for
     * some other reason, an exception is thrown.
     *
     *
     * If the field is a [ChronoField] then the adjustment is implemented here.
     * The supported fields behave as follows:
     *
     *  * `MONTH_OF_YEAR` -
     * Returns a `YearMonth` with the specified month-of-year.
     * The year will be unchanged.
     *  * `PROLEPTIC_MONTH` -
     * Returns a `YearMonth` with the specified proleptic-month.
     * This completely replaces the year and month of this object.
     *  * `YEAR_OF_ERA` -
     * Returns a `YearMonth` with the specified year-of-era
     * The month and era will be unchanged.
     *  * `YEAR` -
     * Returns a `YearMonth` with the specified year.
     * The month will be unchanged.
     *  * `ERA` -
     * Returns a `YearMonth` with the specified era.
     * The month and year-of-era will be unchanged.
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
     * @return a `YearMonth` based on `this` with the specified field set, not null
     * @throws DateTimeException if the field cannot be set
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun with(field: TemporalField, newValue: Long): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.with(field, newValue))
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this `YearMonth` with the year altered.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param year  the year to set in the returned year-month, from MIN_YEAR to MAX_YEAR
     * @return a `YearMonth` based on this year-month with the requested year, not null
     * @throws DateTimeException if the year value is invalid
     */
    fun withYear(year: Int): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.withYear(year))
    }

    /**
     * Returns a copy of this `YearMonth` with the month-of-year altered.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param month  the month-of-year to set in the returned year-month, from 1 (January) to 12 (December)
     * @return a `YearMonth` based on this year-month with the requested month, not null
     * @throws DateTimeException if the month-of-year value is invalid
     */
    fun withMonth(month: Int): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.withMonth(month))
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this year-month with the specified amount added.
     *
     *
     * This returns a `YearMonth`, based on this one, with the specified amount added.
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
     * @return a `YearMonth` based on this year-month with the addition made, not null
     * @throws DateTimeException if the addition cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun plus(amountToAdd: TemporalAmount): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.plus(amountToAdd))
    }

    /**
     * Returns a copy of this year-month with the specified amount added.
     *
     *
     * This returns a `YearMonth`, based on this one, with the amount
     * in terms of the unit added. If it is not possible to add the amount, because the
     * unit is not supported or for some other reason, an exception is thrown.
     *
     *
     * If the field is a [ChronoUnit] then the addition is implemented here.
     * The supported fields behave as follows:
     *
     *  * `MONTHS` -
     * Returns a `YearMonth` with the specified number of months added.
     * This is equivalent to [.plusMonths].
     *  * `YEARS` -
     * Returns a `YearMonth` with the specified number of years added.
     * This is equivalent to [.plusYears].
     *  * `DECADES` -
     * Returns a `YearMonth` with the specified number of decades added.
     * This is equivalent to calling [.plusYears] with the amount
     * multiplied by 10.
     *  * `CENTURIES` -
     * Returns a `YearMonth` with the specified number of centuries added.
     * This is equivalent to calling [.plusYears] with the amount
     * multiplied by 100.
     *  * `MILLENNIA` -
     * Returns a `YearMonth` with the specified number of millennia added.
     * This is equivalent to calling [.plusYears] with the amount
     * multiplied by 1,000.
     *  * `ERAS` -
     * Returns a `YearMonth` with the specified number of eras added.
     * Only two eras are supported so the amount must be one, zero or minus one.
     * If the amount is non-zero then the year is changed such that the year-of-era
     * is unchanged.
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
     * @return a `YearMonth` based on this year-month with the specified amount added, not null
     * @throws DateTimeException if the addition cannot be made
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun plus(amountToAdd: Long, unit: TemporalUnit): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.plus(amountToAdd, unit))
    }

    /**
     * Returns a copy of this `YearMonth` with the specified number of years added.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param yearsToAdd  the years to add, may be negative
     * @return a `YearMonth` based on this year-month with the years added, not null
     * @throws DateTimeException if the result exceeds the supported range
     */
    fun plusYears(yearsToAdd: Long): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.plusYears(yearsToAdd))
    }

    /**
     * Returns a copy of this `YearMonth` with the specified number of months added.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthsToAdd  the months to add, may be negative
     * @return a `YearMonth` based on this year-month with the months added, not null
     * @throws DateTimeException if the result exceeds the supported range
     */
    fun plusMonths(monthsToAdd: Long): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.plusMonths(monthsToAdd))
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this year-month with the specified amount subtracted.
     *
     *
     * This returns a `YearMonth`, based on this one, with the specified amount subtracted.
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
     * @return a `YearMonth` based on this year-month with the subtraction made, not null
     * @throws DateTimeException if the subtraction cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun minus(amountToSubtract: TemporalAmount): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.minus(amountToSubtract))
    }

    /**
     * Returns a copy of this year-month with the specified amount subtracted.
     *
     *
     * This returns a `YearMonth`, based on this one, with the amount
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
     * @return a `YearMonth` based on this year-month with the specified amount subtracted, not null
     * @throws DateTimeException if the subtraction cannot be made
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun minus(amountToSubtract: Long, unit: TemporalUnit): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.minus(amountToSubtract, unit))
    }

    /**
     * Returns a copy of this `YearMonth` with the specified number of years subtracted.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param yearsToSubtract  the years to subtract, may be negative
     * @return a `YearMonth` based on this year-month with the years subtracted, not null
     * @throws DateTimeException if the result exceeds the supported range
     */
    fun minusYears(yearsToSubtract: Long): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.minusYears(yearsToSubtract))
    }

    /**
     * Returns a copy of this `YearMonth` with the specified number of months subtracted.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthsToSubtract  the months to subtract, may be negative
     * @return a `YearMonth` based on this year-month with the months subtracted, not null
     * @throws DateTimeException if the result exceeds the supported range
     */
    fun minusMonths(monthsToSubtract: Long): ImmutableYearMonth {
        return ImmutableYearMonth(_yearMonth.minusMonths(monthsToSubtract))
    }

    //-----------------------------------------------------------------------
    /**
     * Queries this year-month using the specified query.
     *
     *
     * This queries this year-month using the specified query strategy object.
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
        return _yearMonth.query(query)
    }

    /**
     * Adjusts the specified temporal object to have this year-month.
     *
     *
     * This returns a temporal object of the same observable type as the input
     * with the year and month changed to be the same as this.
     *
     *
     * The adjustment is equivalent to using [Temporal.with]
     * passing [ChronoField.PROLEPTIC_MONTH] as the field.
     * If the specified temporal object does not use the ISO calendar system then
     * a `DateTimeException` is thrown.
     *
     *
     * In most cases, it is clearer to reverse the calling pattern by using
     * [Temporal.with]:
     * <pre>
     * // these two lines are equivalent, but the second approach is recommended
     * temporal = thisYearMonth.adjustInto(temporal);
     * temporal = temporal.with(thisYearMonth);
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
        return _yearMonth.adjustInto(temporal)
    }

    /**
     * Calculates the amount of time until another year-month in terms of the specified unit.
     *
     *
     * This calculates the amount of time between two `YearMonth`
     * objects in terms of a single `TemporalUnit`.
     * The start and end points are `this` and the specified year-month.
     * The result will be negative if the end is before the start.
     * The `Temporal` passed to this method is converted to a
     * `YearMonth` using [.from].
     * For example, the amount in years between two year-months can be calculated
     * using `startYearMonth.until(endYearMonth, YEARS)`.
     *
     *
     * The calculation returns a whole number, representing the number of
     * complete units between the two year-months.
     * For example, the amount in decades between 2012-06 and 2032-05
     * will only be one decade as it is one month short of two decades.
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
     * The units `MONTHS`, `YEARS`, `DECADES`,
     * `CENTURIES`, `MILLENNIA` and `ERAS` are supported.
     * Other `ChronoUnit` values will throw an exception.
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
     * @param endExclusive  the end date, exclusive, which is converted to a `YearMonth`, not null
     * @param unit  the unit to measure the amount in, not null
     * @return the amount of time between this year-month and the end year-month
     * @throws DateTimeException if the amount cannot be calculated, or the end
     * temporal cannot be converted to a `YearMonth`
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    override fun until(endExclusive: Temporal, unit: TemporalUnit): Long {
        return _yearMonth.until(endExclusive, unit)
    }

    /**
     * Formats this year-month using the specified formatter.
     *
     *
     * This year-month will be passed to the formatter to produce a string.
     *
     * @param formatter  the formatter to use, not null
     * @return the formatted year-month string, not null
     * @throws DateTimeException if an error occurs during printing
     */
    fun format(formatter: DateTimeFormatter): String {
        return _yearMonth.format(formatter)
    }

    //-----------------------------------------------------------------------
    /**
     * Combines this year-month with a day-of-month to create a `LocalDate`.
     *
     *
     * This returns a `LocalDate` formed from this year-month and the specified day-of-month.
     *
     *
     * The day-of-month value must be valid for the year-month.
     *
     *
     * This method can be used as part of a chain to produce a date:
     * <pre>
     * LocalDate date = year.atMonth(month).atDay(day);
    </pre> *
     *
     * @param dayOfMonth  the day-of-month to use, from 1 to 31
     * @return the date formed from this year-month and the specified day, not null
     * @throws DateTimeException if the day is invalid for the year-month
     * @see .isValidDay
     */
    fun atDay(dayOfMonth: Int): LocalDate {
        return _yearMonth.atDay(dayOfMonth)
    }

    /**
     * Returns a `LocalDate` at the end of the month.
     *
     *
     * This returns a `LocalDate` based on this year-month.
     * The day-of-month is set to the last valid day of the month, taking
     * into account leap years.
     *
     *
     * This method can be used as part of a chain to produce a date:
     * <pre>
     * LocalDate date = year.atMonth(month).atEndOfMonth();
    </pre> *
     *
     * @return the last valid date of this year-month, not null
     */
    fun atEndOfMonth(): LocalDate {
        return _yearMonth.atEndOfMonth()
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this year-month to another year-month.
     *
     *
     * The comparison is based first on the value of the year, then on the value of the month.
     * It is "consistent with equals", as defined by [Comparable].
     *
     * @param other  the other year-month to compare to, not null
     * @return the comparator value, negative if less, positive if greater
     */
    operator fun compareTo(other: YearMonth): Int {
        return _yearMonth.compareTo(other)
    }

    override operator fun compareTo(other: ImmutableYearMonth): Int {
        return compareTo(_yearMonth)
    }

    /**
     * Checks if this year-month is after the specified year-month.
     *
     * @param other  the other year-month to compare to, not null
     * @return true if this is after the specified year-month
     */
    fun isAfter(other: YearMonth): Boolean {
        return _yearMonth.isAfter(other)
    }

    fun isAfter(other: ImmutableYearMonth): Boolean {
        return isAfter(other._yearMonth)
    }

    /**
     * Checks if this year-month is before the specified year-month.
     *
     * @param other  the other year-month to compare to, not null
     * @return true if this point is before the specified year-month
     */
    fun isBefore(other: YearMonth): Boolean {
        return _yearMonth.isBefore(other)
    }

    fun isBefore(other: ImmutableYearMonth): Boolean {
        return isBefore(other._yearMonth)
    }

    //-----------------------------------------------------------------------
    /**
     * Outputs this year-month as a `String`, such as `2007-12`.
     *
     *
     * The output will be in the format `uuuu-MM`:
     *
     * @return a string representation of this year-month, not null
     */
    override fun toString(): String {
        return _yearMonth.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImmutableYearMonth

        if (_yearMonth != other._yearMonth) return false

        return true
    }

    override fun hashCode(): Int {
        return _yearMonth.hashCode()
    }

    companion object {
        /**
         * Obtains the current year-month from the system clock in the default time-zone.
         *
         *
         * This will query the [system clock][Clock.systemDefaultZone] in the default
         * time-zone to obtain the current year-month.
         *
         *
         * Using this method will prevent the ability to use an alternate clock for testing
         * because the clock is hard-coded.
         *
         * @return the current year-month using the system clock and default time-zone, not null
         */
        fun now(): ImmutableYearMonth {
            return ImmutableYearMonth(YearMonth.now())
        }

        /**
         * Obtains the current year-month from the system clock in the specified time-zone.
         *
         *
         * This will query the [system clock][Clock.system] to obtain the current year-month.
         * Specifying the time-zone avoids dependence on the default time-zone.
         *
         *
         * Using this method will prevent the ability to use an alternate clock for testing
         * because the clock is hard-coded.
         *
         * @param zone  the zone ID to use, not null
         * @return the current year-month using the system clock, not null
         */
        fun now(zone: ZoneId): ImmutableYearMonth {
            return ImmutableYearMonth(YearMonth.now(zone))
        }

        /**
         * Obtains the current year-month from the specified clock.
         *
         *
         * This will query the specified clock to obtain the current year-month.
         * Using this method allows the use of an alternate clock for testing.
         * The alternate clock may be introduced using [dependency injection][Clock].
         *
         * @param clock  the clock to use, not null
         * @return the current year-month, not null
         */
        private fun now(clock: Clock): ImmutableYearMonth {
            return ImmutableYearMonth(YearMonth.now(clock))
        }

        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `YearMonth` from a year and month.
         *
         * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
         * @param month  the month-of-year to represent, not null
         * @return the year-month, not null
         * @throws DateTimeException if the year value is invalid
         */
        fun of(year: Int, month: Month): ImmutableYearMonth {
            return ImmutableYearMonth(YearMonth.of(year, month))
        }

        /**
         * Obtains an instance of `YearMonth` from a year and month.
         *
         * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
         * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
         * @return the year-month, not null
         * @throws DateTimeException if either field value is invalid
         */
        fun of(year: Int, month: Int): ImmutableYearMonth {
            return ImmutableYearMonth(YearMonth.of(year, month))
        }

        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `YearMonth` from a temporal object.
         *
         *
         * This obtains a year-month based on the specified temporal.
         * A `TemporalAccessor` represents an arbitrary set of date and time information,
         * which this factory converts to an instance of `YearMonth`.
         *
         *
         * The conversion extracts the [YEAR][ChronoField.YEAR] and
         * [MONTH_OF_YEAR][ChronoField.MONTH_OF_YEAR] fields.
         * The extraction is only permitted if the temporal object has an ISO
         * chronology, or can be converted to a `LocalDate`.
         *
         *
         * This method matches the signature of the functional interface [TemporalQuery]
         * allowing it to be used as a query via method reference, `YearMonth::from`.
         *
         * @param temporal  the temporal object to convert, not null
         * @return the year-month, not null
         * @throws DateTimeException if unable to convert to a `YearMonth`
         */
        fun from(temporal: TemporalAccessor): ImmutableYearMonth {
            return ImmutableYearMonth(YearMonth.from(temporal))
        }

        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `YearMonth` from a text string such as `2007-12`.
         *
         *
         * The string must represent a valid year-month.
         * The format must be `uuuu-MM`.
         * Years outside the range 0000 to 9999 must be prefixed by the plus or minus symbol.
         *
         * @param text  the text to parse such as "2007-12", not null
         * @return the parsed year-month, not null
         * @throws DateTimeParseException if the text cannot be parsed
         */
        fun parse(text: CharSequence): ImmutableYearMonth {
            return ImmutableYearMonth(YearMonth.parse(text))
        }

        /**
         * Obtains an instance of `YearMonth` from a text string using a specific formatter.
         *
         *
         * The text is parsed using the formatter, returning a year-month.
         *
         * @param text  the text to parse, not null
         * @param formatter  the formatter to use, not null
         * @return the parsed year-month, not null
         * @throws DateTimeParseException if the text cannot be parsed
         */
        fun parse(text: CharSequence, formatter: DateTimeFormatter): ImmutableYearMonth {
            return ImmutableYearMonth(YearMonth.parse(text, formatter))
        }
    }
}
