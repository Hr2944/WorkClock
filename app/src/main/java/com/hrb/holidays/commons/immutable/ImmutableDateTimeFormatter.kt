package com.hrb.holidays.commons.immutable

import android.os.Parcel
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parceler
import java.text.Format
import java.text.ParsePosition
import java.time.DateTimeException
import java.time.Period
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.Chronology
import java.time.format.*
import java.time.temporal.*
import java.util.*

// TODO: Verify this works properly
object DateTimeFormatterParceler : Parceler<DateTimeFormatter> {
    private const val separator = "-::-"

    override fun create(parcel: Parcel): DateTimeFormatter {
        val str = parcel.readString()
        val pattern = str?.substringBefore(separator)
        val loc = str?.substringAfter(separator)
        return DateTimeFormatter.ofPattern(
            pattern,
            Locale.getAvailableLocales().find { it.displayName == loc } ?: Locale.getDefault()
        )
    }

    override fun DateTimeFormatter.write(parcel: Parcel, flags: Int) {
        val str = this.toString() + separator + this.locale.displayName
        parcel.writeString(str)
    }
}


@Immutable
//@Parcelize
//@TypeParceler<DateTimeFormatter, DateTimeFormatterParceler>()
class ImmutableDateTimeFormatter(private val _formatter: DateTimeFormatter) {

    //-----------------------------------------------------------------------
    /**
     * Gets the locale to be used during formatting.
     *
     *
     * This is used to lookup any part of the formatter needing specific
     * localization, such as the text or localized pattern.
     *
     * @return the locale of this formatter, not null
     */
    fun getLocale(): Locale {
        return _formatter.locale
    }

    /**
     * Returns a copy of this formatter with a new locale.
     *
     *
     * This is used to lookup any part of the formatter needing specific
     * localization, such as the text or localized pattern.
     *
     *
     * The locale is stored as passed in, without further processing.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param locale  the new locale, not null
     * @return a formatter based on this formatter with the requested locale, not null
     */
    fun withLocale(locale: Locale): ImmutableDateTimeFormatter {
        val newFormatter = _formatter.withLocale(locale)
        return if (newFormatter == _formatter) this else ImmutableDateTimeFormatter(newFormatter)
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the DecimalStyle to be used during formatting.
     *
     * @return the locale of this formatter, not null
     */
    fun getDecimalStyle(): DecimalStyle? {
        return _formatter.decimalStyle
    }

    /**
     * Returns a copy of this formatter with a new DecimalStyle.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param decimalStyle  the new DecimalStyle, not null
     * @return a formatter based on this formatter with the requested DecimalStyle, not null
     */
    fun withDecimalStyle(decimalStyle: DecimalStyle): ImmutableDateTimeFormatter {
        return if (decimalStyle == _formatter.decimalStyle) this
        else ImmutableDateTimeFormatter(_formatter.withDecimalStyle(decimalStyle))
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the overriding chronology to be used during formatting.
     *
     *
     * This returns the override chronology, used to convert dates.
     * By default, a formatter has no override chronology, returning null.
     * See [.withChronology] for more details on overriding.
     *
     * @return the override chronology of this formatter, null if no override
     */
    fun getChronology(): Chronology? {
        return _formatter.chronology
    }

    /**
     * Returns a copy of this formatter with a new override chronology.
     *
     *
     * This returns a formatter with similar state to this formatter but
     * with the override chronology set.
     * By default, a formatter has no override chronology, returning null.
     *
     *
     * If an override is added, then any date that is formatted or parsed will be affected.
     *
     *
     * When formatting, if the temporal object contains a date, then it will
     * be converted to a date in the override chronology.
     * Whether the temporal contains a date is determined by querying the
     * [EPOCH_DAY][ChronoField.EPOCH_DAY] field.
     * Any time or zone will be retained unaltered unless overridden.
     *
     *
     * If the temporal object does not contain a date, but does contain one
     * or more `ChronoField` date fields, then a `DateTimeException`
     * is thrown. In all other cases, the override chronology is added to the temporal,
     * replacing any previous chronology, but without changing the date/time.
     *
     *
     * When parsing, there are two distinct cases to consider.
     * If a chronology has been parsed directly from the text, perhaps because
     * [DateTimeFormatterBuilder.appendChronologyId] was used, then
     * this override chronology has no effect.
     * If no zone has been parsed, then this override chronology will be used
     * to interpret the `ChronoField` values into a date according to the
     * date resolving rules of the chronology.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param chrono  the new chronology, null if no override
     * @return a formatter based on this formatter with the requested override chronology, not null
     */
    fun withChronology(chrono: Chronology?): ImmutableDateTimeFormatter {
        return if (chrono == _formatter.chronology) this
        else ImmutableDateTimeFormatter(_formatter.withChronology(chrono))
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the overriding zone to be used during formatting.
     *
     *
     * This returns the override zone, used to convert instants.
     * By default, a formatter has no override zone, returning null.
     * See [.withZone] for more details on overriding.
     *
     * @return the override zone of this formatter, null if no override
     */
    fun getZone(): ZoneId? {
        return _formatter.zone
    }

    /**
     * Returns a copy of this formatter with a new override zone.
     *
     *
     * This returns a formatter with similar state to this formatter but
     * with the override zone set.
     * By default, a formatter has no override zone, returning null.
     *
     *
     * If an override is added, then any instant that is formatted or parsed will be affected.
     *
     *
     * When formatting, if the temporal object contains an instant, then it will
     * be converted to a zoned date-time using the override zone.
     * Whether the temporal is an instant is determined by querying the
     * [INSTANT_SECONDS][ChronoField.INSTANT_SECONDS] field.
     * If the input has a chronology then it will be retained unless overridden.
     * If the input does not have a chronology, such as `Instant`, then
     * the ISO chronology will be used.
     *
     *
     * If the temporal object does not contain an instant, but does contain
     * an offset then an additional check is made. If the normalized override
     * zone is an offset that differs from the offset of the temporal, then
     * a `DateTimeException` is thrown. In all other cases, the override
     * zone is added to the temporal, replacing any previous zone, but without
     * changing the date/time.
     *
     *
     * When parsing, there are two distinct cases to consider.
     * If a zone has been parsed directly from the text, perhaps because
     * [DateTimeFormatterBuilder.appendZoneId] was used, then
     * this override zone has no effect.
     * If no zone has been parsed, then this override zone will be included in
     * the result of the parse where it can be used to build instants and date-times.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param zone  the new override zone, null if no override
     * @return a formatter based on this formatter with the requested override zone, not null
     */
    fun withZone(zone: ZoneId?): ImmutableDateTimeFormatter {
        return if (zone == _formatter.zone) this
        else ImmutableDateTimeFormatter(_formatter.withZone(zone))
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the resolver style to use during parsing.
     *
     *
     * This returns the resolver style, used during the second phase of parsing
     * when fields are resolved into dates and times.
     * By default, a formatter has the [SMART][ResolverStyle.SMART] resolver style.
     * See [.withResolverStyle] for more details.
     *
     * @return the resolver style of this formatter, not null
     */
    fun getResolverStyle(): ResolverStyle {
        return _formatter.resolverStyle
    }

    /**
     * Returns a copy of this formatter with a new resolver style.
     *
     *
     * This returns a formatter with similar state to this formatter but
     * with the resolver style set. By default, a formatter has the
     * [SMART][ResolverStyle.SMART] resolver style.
     *
     *
     * Changing the resolver style only has an effect during parsing.
     * Parsing a text string occurs in two phases.
     * Phase 1 is a basic text parse according to the fields added to the builder.
     * Phase 2 resolves the parsed field-value pairs into date and/or time objects.
     * The resolver style is used to control how phase 2, resolving, happens.
     * See `ResolverStyle` for more information on the options available.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param resolverStyle  the new resolver style, not null
     * @return a formatter based on this formatter with the requested resolver style, not null
     */
    fun withResolverStyle(resolverStyle: ResolverStyle): ImmutableDateTimeFormatter {
        return if (resolverStyle == _formatter.resolverStyle) this
        else ImmutableDateTimeFormatter(_formatter.withResolverStyle(resolverStyle))
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the resolver fields to use during parsing.
     *
     *
     * This returns the resolver fields, used during the second phase of parsing
     * when fields are resolved into dates and times.
     * By default, a formatter has no resolver fields, and thus returns null.
     * See [.withResolverFields] for more details.
     *
     * @return the immutable set of resolver fields of this formatter, null if no fields
     */
    fun getResolverFields(): Set<TemporalField>? {
        return _formatter.resolverFields
    }

    /**
     * Returns a copy of this formatter with a new set of resolver fields.
     *
     *
     * This returns a formatter with similar state to this formatter but with
     * the resolver fields set. By default, a formatter has no resolver fields.
     *
     *
     * Changing the resolver fields only has an effect during parsing.
     * Parsing a text string occurs in two phases.
     * Phase 1 is a basic text parse according to the fields added to the builder.
     * Phase 2 resolves the parsed field-value pairs into date and/or time objects.
     * The resolver fields are used to filter the field-value pairs between phase 1 and 2.
     *
     *
     * This can be used to select between two or more ways that a date or time might
     * be resolved. For example, if the formatter consists of year, month, day-of-month
     * and day-of-year, then there are two ways to resolve a date.
     * Calling this method with the arguments [YEAR][ChronoField.YEAR] and
     * [DAY_OF_YEAR][ChronoField.DAY_OF_YEAR] will ensure that the date is
     * resolved using the year and day-of-year, effectively meaning that the month
     * and day-of-month are ignored during the resolving phase.
     *
     *
     * In a similar manner, this method can be used to ignore secondary fields that
     * would otherwise be cross-checked. For example, if the formatter consists of year,
     * month, day-of-month and day-of-week, then there is only one way to resolve a
     * date, but the parsed value for day-of-week will be cross-checked against the
     * resolved date. Calling this method with the arguments [YEAR][ChronoField.YEAR],
     * [MONTH_OF_YEAR][ChronoField.MONTH_OF_YEAR] and
     * [DAY_OF_MONTH][ChronoField.DAY_OF_MONTH] will ensure that the date is
     * resolved correctly, but without any cross-check for the day-of-week.
     *
     *
     * In implementation terms, this method behaves as follows. The result of the
     * parsing phase can be considered to be a map of field to value. The behavior
     * of this method is to cause that map to be filtered between phase 1 and 2,
     * removing all fields other than those specified as arguments to this method.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param resolverFields  the new set of resolver fields, null if no fields
     * @return a formatter based on this formatter with the requested resolver style, not null
     */
    fun withResolverFields(resolverFields: Set<TemporalField>): ImmutableDateTimeFormatter {
        return ImmutableDateTimeFormatter(_formatter.withResolverFields(resolverFields))
    }

    //-----------------------------------------------------------------------
    /**
     * Formats a date-time object using this formatter.
     *
     *
     * This formats the date-time to a String using the rules of the formatter.
     *
     * @param temporal  the temporal object to format, not null
     * @return the formatted string, not null
     * @throws DateTimeException if an error occurs during formatting
     */
    fun format(temporal: TemporalAccessor): String {
        return _formatter.format(temporal)
    }

    //-----------------------------------------------------------------------
    /**
     * Formats a date-time object to an `Appendable` using this formatter.
     *
     *
     * This outputs the formatted date-time to the specified destination.
     * [Appendable] is a general purpose interface that is implemented by all
     * key character output classes including `StringBuffer`, `StringBuilder`,
     * `PrintStream` and `Writer`.
     *
     *
     * Although `Appendable` methods throw an `IOException`, this method does not.
     * Instead, any `IOException` is wrapped in a runtime exception.
     *
     * @param temporal  the temporal object to format, not null
     * @param appendable  the appendable to format to, not null
     * @throws DateTimeException if an error occurs during formatting
     */
    fun formatTo(temporal: TemporalAccessor?, appendable: Appendable) {
        return _formatter.formatTo(temporal, appendable)
    }

    //-----------------------------------------------------------------------
    /**
     * Fully parses the text producing a temporal object.
     *
     *
     * This parses the entire text producing a temporal object.
     * It is typically more useful to use [.parse].
     * The result of this method is `TemporalAccessor` which has been resolved,
     * applying basic validation checks to help ensure a valid date-time.
     *
     *
     * If the parse completes without reading the entire length of the text,
     * or a problem occurs during parsing or merging, then an exception is thrown.
     *
     * @param text  the text to parse, not null
     * @return the parsed temporal object, not null
     * @throws DateTimeParseException if unable to parse the requested result
     */
    fun parse(text: CharSequence): TemporalAccessor {
        return _formatter.parse(text)
    }

    /**
     * Parses the text using this formatter, providing control over the text position.
     *
     *
     * This parses the text without requiring the parse to start from the beginning
     * of the string or finish at the end.
     * The result of this method is `TemporalAccessor` which has been resolved,
     * applying basic validation checks to help ensure a valid date-time.
     *
     *
     * The text will be parsed from the specified start `ParsePosition`.
     * The entire length of the text does not have to be parsed, the `ParsePosition`
     * will be updated with the index at the end of parsing.
     *
     *
     * The operation of this method is slightly different to similar methods using
     * `ParsePosition` on `java.text.Format`. That class will return
     * errors using the error index on the `ParsePosition`. By contrast, this
     * method will throw a [DateTimeParseException] if an error occurs, with
     * the exception containing the error index.
     * This change in behavior is necessary due to the increased complexity of
     * parsing and resolving dates/times in this API.
     *
     *
     * If the formatter parses the same field more than once with different values,
     * the result will be an error.
     *
     * @param text  the text to parse, not null
     * @param position  the position to parse from, updated with length parsed
     * and the index of any error, not null
     * @return the parsed temporal object, not null
     * @throws DateTimeParseException if unable to parse the requested result
     * @throws IndexOutOfBoundsException if the position is invalid
     */
    fun parse(text: CharSequence, position: ParsePosition): TemporalAccessor {
        return _formatter.parse(text, position)
    }

    //-----------------------------------------------------------------------
    /**
     * Fully parses the text producing an object of the specified type.
     *
     *
     * Most applications should use this method for parsing.
     * It parses the entire text to produce the required date-time.
     * The query is typically a method reference to a `from(TemporalAccessor)` method.
     * For example:
     * <pre>
     * LocalDateTime dt = parser.parse(str, LocalDateTime::from);
    </pre> *
     * If the parse completes without reading the entire length of the text,
     * or a problem occurs during parsing or merging, then an exception is thrown.
     *
     * @param <T> the type of the parsed date-time
     * @param text  the text to parse, not null
     * @param query  the query defining the type to parse to, not null
     * @return the parsed date-time, not null
     * @throws DateTimeParseException if unable to parse the requested result
    </T> */
    fun <T> parse(text: CharSequence, query: TemporalQuery<T>): T {
        return _formatter.parse(text, query)
    }

    /**
     * Fully parses the text producing an object of one of the specified types.
     *
     *
     * This parse method is convenient for use when the parser can handle optional elements.
     * For example, a pattern of 'uuuu-MM-dd HH.mm[ VV]' can be fully parsed to a `ZonedDateTime`,
     * or partially parsed to a `LocalDateTime`.
     * The queries must be specified in order, starting from the best matching full-parse option
     * and ending with the worst matching minimal parse option.
     * The query is typically a method reference to a `from(TemporalAccessor)` method.
     *
     *
     * The result is associated with the first type that successfully parses.
     * Normally, applications will use `instanceof` to check the result.
     * For example:
     * <pre>
     * TemporalAccessor dt = parser.parseBest(str, ZonedDateTime::from, LocalDateTime::from);
     * if (dt instanceof ZonedDateTime) {
     * ...
     * } else {
     * ...
     * }
    </pre> *
     * If the parse completes without reading the entire length of the text,
     * or a problem occurs during parsing or merging, then an exception is thrown.
     *
     * @param text  the text to parse, not null
     * @param queries  the queries defining the types to attempt to parse to,
     * must implement `TemporalAccessor`, not null
     * @return the parsed date-time, not null
     * @throws IllegalArgumentException if less than 2 types are specified
     * @throws DateTimeParseException if unable to parse the requested result
     */
    fun parseBest(text: CharSequence, vararg queries: TemporalQuery<*>?): TemporalAccessor {
        return _formatter.parseBest(text, *queries)
    }

    /**
     * Parses the text using this formatter, without resolving the result, intended
     * for advanced use cases.
     *
     *
     * Parsing is implemented as a two-phase operation.
     * First, the text is parsed using the layout defined by the formatter, producing
     * a `Map` of field to value, a `ZoneId` and a `Chronology`.
     * Second, the parsed data is *resolved*, by validating, combining and
     * simplifying the various fields into more useful ones.
     * This method performs the parsing stage but not the resolving stage.
     *
     *
     * The result of this method is `TemporalAccessor` which represents the
     * data as seen in the input. Values are not validated, thus parsing a date string
     * of '2012-00-65' would result in a temporal with three fields - year of '2012',
     * month of '0' and day-of-month of '65'.
     *
     *
     * The text will be parsed from the specified start `ParsePosition`.
     * The entire length of the text does not have to be parsed, the `ParsePosition`
     * will be updated with the index at the end of parsing.
     *
     *
     * Errors are returned using the error index field of the `ParsePosition`
     * instead of `DateTimeParseException`.
     * The returned error index will be set to an index indicative of the error.
     * Callers must check for errors before using the result.
     *
     *
     * If the formatter parses the same field more than once with different values,
     * the result will be an error.
     *
     *
     * This method is intended for advanced use cases that need access to the
     * internal state during parsing. Typical application code should use
     * [.parse] or the parse method on the target type.
     *
     * @param text  the text to parse, not null
     * @param position  the position to parse from, updated with length parsed
     * and the index of any error, not null
     * @return the parsed text, null if the parse results in an error
     * @throws DateTimeException if some problem occurs during parsing
     * @throws IndexOutOfBoundsException if the position is invalid
     */
    fun parseUnresolved(text: CharSequence, position: ParsePosition): TemporalAccessor? {
        return _formatter.parseUnresolved(text, position)
    }

    /**
     * Returns this formatter as a `java.text.Format` instance.
     *
     *
     * The returned [Format] instance will format any [TemporalAccessor]
     * and parses to a resolved [TemporalAccessor].
     *
     *
     * Exceptions will follow the definitions of `Format`, see those methods
     * for details about `IllegalArgumentException` during formatting and
     * `ParseException` or null during parsing.
     * The format does not support attributing of the returned format string.
     *
     * @return this formatter as a classic format instance, not null
     */
    fun toFormat(): Format {
        return _formatter.toFormat()
    }

    fun toDateTimeFormatter(): DateTimeFormatter {
        return _formatter
    }

    /**
     * Returns this formatter as a `java.text.Format` instance that will
     * parse using the specified query.
     *
     *
     * The returned [Format] instance will format any [TemporalAccessor]
     * and parses to the type specified.
     * The type must be one that is supported by [.parse].
     *
     *
     * Exceptions will follow the definitions of `Format`, see those methods
     * for details about `IllegalArgumentException` during formatting and
     * `ParseException` or null during parsing.
     * The format does not support attributing of the returned format string.
     *
     * @param parseQuery  the query defining the type to parse to, not null
     * @return this formatter as a classic format instance, not null
     */
    fun toFormat(parseQuery: TemporalQuery<*>?): Format {
        return _formatter.toFormat(parseQuery)
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a description of the underlying formatters.
     *
     * @return a description of this formatter, not null
     */
    override fun toString(): String {
        return _formatter.toString()
    }

    companion object {
        //-----------------------------------------------------------------------
        /**
         * Creates a formatter using the specified pattern.
         *
         *
         * This method will create a formatter based on a simple
         * [pattern of letters and symbols](#patterns)
         * as described in the class documentation.
         * For example, `d MMM uuuu` will format 2011-12-03 as '3 Dec 2011'.
         *
         *
         * The formatter will use the [default FORMAT locale][Locale.getDefault].
         * This can be changed using [DateTimeFormatter.withLocale] on the returned formatter.
         * Alternatively use the [.ofPattern] variant of this method.
         *
         *
         * The returned formatter has no override chronology or zone.
         * It uses [SMART][ResolverStyle.SMART] resolver style.
         *
         * @param pattern  the pattern to use, not null
         * @return the formatter based on the pattern, not null
         * @throws IllegalArgumentException if the pattern is invalid
         * @see DateTimeFormatterBuilder.appendPattern
         */
        fun ofPattern(pattern: String): ImmutableDateTimeFormatter {
            return ImmutableDateTimeFormatter(DateTimeFormatter.ofPattern(pattern))
        }

        /**
         * Creates a formatter using the specified pattern and locale.
         *
         *
         * This method will create a formatter based on a simple
         * [pattern of letters and symbols](#patterns)
         * as described in the class documentation.
         * For example, `d MMM uuuu` will format 2011-12-03 as '3 Dec 2011'.
         *
         *
         * The formatter will use the specified locale.
         * This can be changed using [DateTimeFormatter.withLocale] on the returned formatter.
         *
         *
         * The returned formatter has no override chronology or zone.
         * It uses [SMART][ResolverStyle.SMART] resolver style.
         *
         * @param pattern  the pattern to use, not null
         * @param locale  the locale to use, not null
         * @return the formatter based on the pattern, not null
         * @throws IllegalArgumentException if the pattern is invalid
         * @see DateTimeFormatterBuilder.appendPattern
         */
        fun ofPattern(pattern: String, locale: Locale): ImmutableDateTimeFormatter {
            return ImmutableDateTimeFormatter(DateTimeFormatter.ofPattern(pattern, locale))
        }

        //-----------------------------------------------------------------------
        /**
         * Returns a locale specific date format for the ISO chronology.
         *
         *
         * This returns a formatter that will format or parse a date.
         * The exact format pattern used varies by locale.
         *
         *
         * The locale is determined from the formatter. The formatter returned directly by
         * this method will use the [default FORMAT locale][Locale.getDefault].
         * The locale can be controlled using [withLocale(Locale)][DateTimeFormatter.withLocale]
         * on the result of this method.
         *
         *
         * Note that the localized pattern is looked up lazily.
         * This `DateTimeFormatter` holds the style required and the locale,
         * looking up the pattern required on demand.
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [SMART][ResolverStyle.SMART] resolver style.
         *
         * @param dateStyle  the formatter style to obtain, not null
         * @return the date formatter, not null
         */
        fun ofLocalizedDate(dateStyle: FormatStyle): ImmutableDateTimeFormatter {
            return ImmutableDateTimeFormatter(DateTimeFormatter.ofLocalizedDate(dateStyle))
        }

        /**
         * Returns a locale specific time format for the ISO chronology.
         *
         *
         * This returns a formatter that will format or parse a time.
         * The exact format pattern used varies by locale.
         *
         *
         * The locale is determined from the formatter. The formatter returned directly by
         * this method will use the [default FORMAT locale][Locale.getDefault].
         * The locale can be controlled using [withLocale(Locale)][DateTimeFormatter.withLocale]
         * on the result of this method.
         *
         *
         * Note that the localized pattern is looked up lazily.
         * This `DateTimeFormatter` holds the style required and the locale,
         * looking up the pattern required on demand.
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [SMART][ResolverStyle.SMART] resolver style.
         * The `FULL` and `LONG` styles typically require a time-zone.
         * When formatting using these styles, a `ZoneId` must be available,
         * either by using `ZonedDateTime` or [DateTimeFormatter.withZone].
         *
         * @param timeStyle  the formatter style to obtain, not null
         * @return the time formatter, not null
         */
        fun ofLocalizedTime(timeStyle: FormatStyle): ImmutableDateTimeFormatter {
            return ImmutableDateTimeFormatter(DateTimeFormatter.ofLocalizedTime(timeStyle))
        }

        /**
         * Returns a locale specific date-time formatter for the ISO chronology.
         *
         *
         * This returns a formatter that will format or parse a date-time.
         * The exact format pattern used varies by locale.
         *
         *
         * The locale is determined from the formatter. The formatter returned directly by
         * this method will use the [default FORMAT locale][Locale.getDefault].
         * The locale can be controlled using [withLocale(Locale)][DateTimeFormatter.withLocale]
         * on the result of this method.
         *
         *
         * Note that the localized pattern is looked up lazily.
         * This `DateTimeFormatter` holds the style required and the locale,
         * looking up the pattern required on demand.
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [SMART][ResolverStyle.SMART] resolver style.
         * The `FULL` and `LONG` styles typically require a time-zone.
         * When formatting using these styles, a `ZoneId` must be available,
         * either by using `ZonedDateTime` or [DateTimeFormatter.withZone].
         *
         * @param dateTimeStyle  the formatter style to obtain, not null
         * @return the date-time formatter, not null
         */
        fun ofLocalizedDateTime(dateTimeStyle: FormatStyle): ImmutableDateTimeFormatter {
            return ImmutableDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(dateTimeStyle))
        }

        /**
         * Returns a locale specific date and time format for the ISO chronology.
         *
         *
         * This returns a formatter that will format or parse a date-time.
         * The exact format pattern used varies by locale.
         *
         *
         * The locale is determined from the formatter. The formatter returned directly by
         * this method will use the [default FORMAT locale][Locale.getDefault].
         * The locale can be controlled using [withLocale(Locale)][DateTimeFormatter.withLocale]
         * on the result of this method.
         *
         *
         * Note that the localized pattern is looked up lazily.
         * This `DateTimeFormatter` holds the style required and the locale,
         * looking up the pattern required on demand.
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [SMART][ResolverStyle.SMART] resolver style.
         * The `FULL` and `LONG` styles typically require a time-zone.
         * When formatting using these styles, a `ZoneId` must be available,
         * either by using `ZonedDateTime` or [DateTimeFormatter.withZone].
         *
         * @param dateStyle  the date formatter style to obtain, not null
         * @param timeStyle  the time formatter style to obtain, not null
         * @return the date, time or date-time formatter, not null
         */
        fun ofLocalizedDateTime(
            dateStyle: FormatStyle,
            timeStyle: FormatStyle
        ): ImmutableDateTimeFormatter {
            return ImmutableDateTimeFormatter(
                DateTimeFormatter.ofLocalizedDateTime(
                    dateStyle,
                    timeStyle
                )
            )
        }

        //-----------------------------------------------------------------------
        /**
         * The ISO date formatter that formats or parses a date without an
         * offset, such as '2011-12-03'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 extended local date format.
         * The format consists of:
         *
         *  * Four digits or more for the [year][ChronoField.YEAR].
         * Years in the range 0000 to 9999 will be pre-padded by zero to ensure four digits.
         * Years outside that range will have a prefixed positive or negative symbol.
         *  * A dash
         *  * Two digits for the [month-of-year][ChronoField.MONTH_OF_YEAR].
         * This is pre-padded by zero to ensure two digits.
         *  * A dash
         *  * Two digits for the [day-of-month][ChronoField.DAY_OF_MONTH].
         * This is pre-padded by zero to ensure two digits.
         *
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        val ISO_LOCAL_DATE: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

        //-----------------------------------------------------------------------
        /**
         * The ISO date formatter that formats or parses a date with an
         * offset, such as '2011-12-03+01:00'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 extended offset date format.
         * The format consists of:
         *
         *  * The [.ISO_LOCAL_DATE]
         *  * The [offset ID][ZoneOffset.getId]. If the offset has seconds then
         * they will be handled even though this is not part of the ISO-8601 standard.
         * Parsing is case insensitive.
         *
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        val ISO_OFFSET_DATE: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE

        //-----------------------------------------------------------------------
        /**
         * The ISO date formatter that formats or parses a date with the
         * offset if available, such as '2011-12-03' or '2011-12-03+01:00'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 extended date format.
         * The format consists of:
         *
         *  * The [.ISO_LOCAL_DATE]
         *  * If the offset is not available then the format is complete.
         *  * The [offset ID][ZoneOffset.getId]. If the offset has seconds then
         * they will be handled even though this is not part of the ISO-8601 standard.
         * Parsing is case insensitive.
         *
         *
         *
         * As this formatter has an optional element, it may be necessary to parse using
         * [DateTimeFormatter.parseBest].
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        val ISO_DATE: DateTimeFormatter = DateTimeFormatter.ISO_DATE

        //-----------------------------------------------------------------------
        /**
         * The ISO time formatter that formats or parses a time without an
         * offset, such as '10:15' or '10:15:30'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 extended local time format.
         * The format consists of:
         *
         *  * Two digits for the [hour-of-day][ChronoField.HOUR_OF_DAY].
         * This is pre-padded by zero to ensure two digits.
         *  * A colon
         *  * Two digits for the [minute-of-hour][ChronoField.MINUTE_OF_HOUR].
         * This is pre-padded by zero to ensure two digits.
         *  * If the second-of-minute is not available then the format is complete.
         *  * A colon
         *  * Two digits for the [second-of-minute][ChronoField.SECOND_OF_MINUTE].
         * This is pre-padded by zero to ensure two digits.
         *  * If the nano-of-second is zero or not available then the format is complete.
         *  * A decimal point
         *  * One to nine digits for the [nano-of-second][ChronoField.NANO_OF_SECOND].
         * As many digits will be output as required.
         *
         *
         *
         * The returned formatter has no override chronology or zone.
         * It uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        val ISO_LOCAL_TIME: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

        //-----------------------------------------------------------------------
        /**
         * The ISO time formatter that formats or parses a time with an
         * offset, such as '10:15+01:00' or '10:15:30+01:00'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 extended offset time format.
         * The format consists of:
         *
         *  * The [.ISO_LOCAL_TIME]
         *  * The [offset ID][ZoneOffset.getId]. If the offset has seconds then
         * they will be handled even though this is not part of the ISO-8601 standard.
         * Parsing is case insensitive.
         *
         *
         *
         * The returned formatter has no override chronology or zone.
         * It uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        val ISO_OFFSET_TIME: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_TIME

        //-----------------------------------------------------------------------
        /**
         * The ISO time formatter that formats or parses a time, with the
         * offset if available, such as '10:15', '10:15:30' or '10:15:30+01:00'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 extended offset time format.
         * The format consists of:
         *
         *  * The [.ISO_LOCAL_TIME]
         *  * If the offset is not available then the format is complete.
         *  * The [offset ID][ZoneOffset.getId]. If the offset has seconds then
         * they will be handled even though this is not part of the ISO-8601 standard.
         * Parsing is case insensitive.
         *
         *
         *
         * As this formatter has an optional element, it may be necessary to parse using
         * [DateTimeFormatter.parseBest].
         *
         *
         * The returned formatter has no override chronology or zone.
         * It uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        var ISO_TIME: DateTimeFormatter = DateTimeFormatter.ISO_TIME

        //-----------------------------------------------------------------------
        /**
         * The ISO date-time formatter that formats or parses a date-time without
         * an offset, such as '2011-12-03T10:15:30'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 extended offset date-time format.
         * The format consists of:
         *
         *  * The [.ISO_LOCAL_DATE]
         *  * The letter 'T'. Parsing is case insensitive.
         *  * The [.ISO_LOCAL_TIME]
         *
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        var ISO_LOCAL_DATE_TIME: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        //-----------------------------------------------------------------------
        /**
         * The ISO date-time formatter that formats or parses a date-time with an
         * offset, such as '2011-12-03T10:15:30+01:00'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 extended offset date-time format.
         * The format consists of:
         *
         *  * The [.ISO_LOCAL_DATE_TIME]
         *  * The [offset ID][ZoneOffset.getId]. If the offset has seconds then
         * they will be handled even though this is not part of the ISO-8601 standard.
         * The offset parsing is lenient, which allows the minutes and seconds to be optional.
         * Parsing is case insensitive.
         *
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        var ISO_OFFSET_DATE_TIME: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        //-----------------------------------------------------------------------
        /**
         * The ISO-like date-time formatter that formats or parses a date-time with
         * offset and zone, such as '2011-12-03T10:15:30+01:00[Europe/Paris]'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * a format that extends the ISO-8601 extended offset date-time format
         * to add the time-zone.
         * The section in square brackets is not part of the ISO-8601 standard.
         * The format consists of:
         *
         *  * The [.ISO_OFFSET_DATE_TIME]
         *  * If the zone ID is not available or is a `ZoneOffset` then the format is complete.
         *  * An open square bracket '['.
         *  * The [zone ID][ZoneId.getId]. This is not part of the ISO-8601 standard.
         * Parsing is case sensitive.
         *  * A close square bracket ']'.
         *
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        var ISO_ZONED_DATE_TIME: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

        //-----------------------------------------------------------------------
        /**
         * The ISO-like date-time formatter that formats or parses a date-time with
         * the offset and zone if available, such as '2011-12-03T10:15:30',
         * '2011-12-03T10:15:30+01:00' or '2011-12-03T10:15:30+01:00[Europe/Paris]'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 extended local or offset date-time format, as well as the
         * extended non-ISO form specifying the time-zone.
         * The format consists of:
         *
         *  * The [.ISO_LOCAL_DATE_TIME]
         *  * If the offset is not available to format or parse then the format is complete.
         *  * The [offset ID][ZoneOffset.getId]. If the offset has seconds then
         * they will be handled even though this is not part of the ISO-8601 standard.
         *  * If the zone ID is not available or is a `ZoneOffset` then the format is complete.
         *  * An open square bracket '['.
         *  * The [zone ID][ZoneId.getId]. This is not part of the ISO-8601 standard.
         * Parsing is case sensitive.
         *  * A close square bracket ']'.
         *
         *
         *
         * As this formatter has an optional element, it may be necessary to parse using
         * [DateTimeFormatter.parseBest].
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        var ISO_DATE_TIME: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

        //-----------------------------------------------------------------------
        /**
         * The ISO date formatter that formats or parses the ordinal date
         * without an offset, such as '2012-337'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 extended ordinal date format.
         * The format consists of:
         *
         *  * Four digits or more for the [year][ChronoField.YEAR].
         * Years in the range 0000 to 9999 will be pre-padded by zero to ensure four digits.
         * Years outside that range will have a prefixed positive or negative symbol.
         *  * A dash
         *  * Three digits for the [day-of-year][ChronoField.DAY_OF_YEAR].
         * This is pre-padded by zero to ensure three digits.
         *  * If the offset is not available to format or parse then the format is complete.
         *  * The [offset ID][ZoneOffset.getId]. If the offset has seconds then
         * they will be handled even though this is not part of the ISO-8601 standard.
         * Parsing is case insensitive.
         *
         *
         *
         * As this formatter has an optional element, it may be necessary to parse using
         * [DateTimeFormatter.parseBest].
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        var ISO_ORDINAL_DATE: DateTimeFormatter = DateTimeFormatter.ISO_ORDINAL_DATE

        //-----------------------------------------------------------------------
        /**
         * The ISO date formatter that formats or parses the week-based date
         * without an offset, such as '2012-W48-6'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 extended week-based date format.
         * The format consists of:
         *
         *  * Four digits or more for the [week-based-year][IsoFields.WEEK_BASED_YEAR].
         * Years in the range 0000 to 9999 will be pre-padded by zero to ensure four digits.
         * Years outside that range will have a prefixed positive or negative symbol.
         *  * A dash
         *  * The letter 'W'. Parsing is case insensitive.
         *  * Two digits for the [week-of-week-based-year][IsoFields.WEEK_OF_WEEK_BASED_YEAR].
         * This is pre-padded by zero to ensure three digits.
         *  * A dash
         *  * One digit for the [day-of-week][ChronoField.DAY_OF_WEEK].
         * The value run from Monday (1) to Sunday (7).
         *  * If the offset is not available to format or parse then the format is complete.
         *  * The [offset ID][ZoneOffset.getId]. If the offset has seconds then
         * they will be handled even though this is not part of the ISO-8601 standard.
         * Parsing is case insensitive.
         *
         *
         *
         * As this formatter has an optional element, it may be necessary to parse using
         * [DateTimeFormatter.parseBest].
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        var ISO_WEEK_DATE: DateTimeFormatter = DateTimeFormatter.ISO_WEEK_DATE

        //-----------------------------------------------------------------------
        /**
         * The ISO instant formatter that formats or parses an instant in UTC,
         * such as '2011-12-03T10:15:30Z'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 instant format.
         * When formatting, the second-of-minute is always output.
         * The nano-of-second outputs zero, three, six or nine digits as necessary.
         * When parsing, time to at least the seconds field is required.
         * Fractional seconds from zero to nine are parsed.
         * The localized decimal style is not used.
         *
         *
         * This is a special case formatter intended to allow a human readable form
         * of an [java.time.Instant]. The `Instant` class is designed to
         * only represent a point in time and internally stores a value in nanoseconds
         * from a fixed epoch of 1970-01-01Z. As such, an `Instant` cannot be
         * formatted as a date or time without providing some form of time-zone.
         * This formatter allows the `Instant` to be formatted, by providing
         * a suitable conversion using `ZoneOffset.UTC`.
         *
         *
         * The format consists of:
         *
         *  * The [.ISO_OFFSET_DATE_TIME] where the instant is converted from
         * [ChronoField.INSTANT_SECONDS] and [ChronoField.NANO_OF_SECOND]
         * using the `UTC` offset. Parsing is case insensitive.
         *
         *
         *
         * The returned formatter has no override chronology or zone.
         * It uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        var ISO_INSTANT: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

        //-----------------------------------------------------------------------
        /**
         * The ISO date formatter that formats or parses a date without an
         * offset, such as '20111203'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * the ISO-8601 basic local date format.
         * The format consists of:
         *
         *  * Four digits for the [year][ChronoField.YEAR].
         * Only years in the range 0000 to 9999 are supported.
         *  * Two digits for the [month-of-year][ChronoField.MONTH_OF_YEAR].
         * This is pre-padded by zero to ensure two digits.
         *  * Two digits for the [day-of-month][ChronoField.DAY_OF_MONTH].
         * This is pre-padded by zero to ensure two digits.
         *  * If the offset is not available to format or parse then the format is complete.
         *  * The [offset ID][ZoneOffset.getId] without colons. If the offset has
         * seconds then they will be handled even though this is not part of the ISO-8601 standard.
         * The offset parsing is lenient, which allows the minutes and seconds to be optional.
         * Parsing is case insensitive.
         *
         *
         *
         * As this formatter has an optional element, it may be necessary to parse using
         * [DateTimeFormatter.parseBest].
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [STRICT][ResolverStyle.STRICT] resolver style.
         */
        var BASIC_ISO_DATE: DateTimeFormatter = DateTimeFormatter.BASIC_ISO_DATE

        //-----------------------------------------------------------------------
        /**
         * The RFC-1123 date-time formatter, such as 'Tue, 3 Jun 2008 11:05:30 GMT'.
         *
         *
         * This returns an immutable formatter capable of formatting and parsing
         * most of the RFC-1123 format.
         * RFC-1123 updates RFC-822 changing the year from two digits to four.
         * This implementation requires a four digit year.
         * This implementation also does not handle North American or military zone
         * names, only 'GMT' and offset amounts.
         *
         *
         * The format consists of:
         *
         *  * If the day-of-week is not available to format or parse then jump to day-of-month.
         *  * Three letter [day-of-week][ChronoField.DAY_OF_WEEK] in English.
         *  * A comma
         *  * A space
         *  * One or two digits for the [day-of-month][ChronoField.DAY_OF_MONTH].
         *  * A space
         *  * Three letter [month-of-year][ChronoField.MONTH_OF_YEAR] in English.
         *  * A space
         *  * Four digits for the [year][ChronoField.YEAR].
         * Only years in the range 0000 to 9999 are supported.
         *  * A space
         *  * Two digits for the [hour-of-day][ChronoField.HOUR_OF_DAY].
         * This is pre-padded by zero to ensure two digits.
         *  * A colon
         *  * Two digits for the [minute-of-hour][ChronoField.MINUTE_OF_HOUR].
         * This is pre-padded by zero to ensure two digits.
         *  * If the second-of-minute is not available then jump to the next space.
         *  * A colon
         *  * Two digits for the [second-of-minute][ChronoField.SECOND_OF_MINUTE].
         * This is pre-padded by zero to ensure two digits.
         *  * A space
         *  * The [offset ID][ZoneOffset.getId] without colons or seconds.
         * An offset of zero uses "GMT". North American zone names and military zone names are not handled.
         *
         *
         *
         * Parsing is case insensitive.
         *
         *
         * The returned formatter has a chronology of ISO set to ensure dates in
         * other calendar systems are correctly converted.
         * It has no override zone and uses the [SMART][ResolverStyle.SMART] resolver style.
         */
        var RFC_1123_DATE_TIME: DateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME

        //-----------------------------------------------------------------------
        /**
         * A query that provides access to the excess days that were parsed.
         *
         *
         * This returns a singleton [query][TemporalQuery] that provides
         * access to additional information from the parse. The query always returns
         * a non-null period, with a zero period returned instead of null.
         *
         *
         * There are two situations where this query may return a non-zero period.
         *
         *  * If the `ResolverStyle` is `LENIENT` and a time is parsed
         * without a date, then the complete result of the parse consists of a
         * `LocalTime` and an excess `Period` in days.
         *
         *  * If the `ResolverStyle` is `SMART` and a time is parsed
         * without a date where the time is 24:00:00, then the complete result of
         * the parse consists of a `LocalTime` of 00:00:00 and an excess
         * `Period` of one day.
         *
         *
         *
         * In both cases, if a complete `ChronoLocalDateTime` or `Instant`
         * is parsed, then the excess days are added to the date part.
         * As a result, this query will return a zero period.
         *
         *
         * The `SMART` behaviour handles the common "end of day" 24:00 value.
         * Processing in `LENIENT` mode also produces the same result:
         * <pre>
         * Text to parse        Parsed object                         Excess days
         * "2012-12-03T00:00"   LocalDateTime.of(2012, 12, 3, 0, 0)   ZERO
         * "2012-12-03T24:00"   LocalDateTime.of(2012, 12, 4, 0, 0)   ZERO
         * "00:00"              LocalTime.of(0, 0)                    ZERO
         * "24:00"              LocalTime.of(0, 0)                    Period.ofDays(1)
        </pre> *
         * The query can be used as follows:
         * <pre>
         * TemporalAccessor parsed = formatter.parse(str);
         * LocalTime time = parsed.query(LocalTime::from);
         * Period extraDays = parsed.query(DateTimeFormatter.parsedExcessDays());
        </pre> *
         * @return a query that provides access to the excess days that were parsed
         */
        fun parsedExcessDays(): TemporalQuery<Period> {
            return DateTimeFormatter.parsedExcessDays()
        }

        /**
         * A query that provides access to whether a leap-second was parsed.
         *
         *
         * This returns a singleton [query][TemporalQuery] that provides
         * access to additional information from the parse. The query always returns
         * a non-null boolean, true if parsing saw a leap-second, false if not.
         *
         *
         * Instant parsing handles the special "leap second" time of '23:59:60'.
         * Leap seconds occur at '23:59:60' in the UTC time-zone, but at other
         * local times in different time-zones. To avoid this potential ambiguity,
         * the handling of leap-seconds is limited to
         * [DateTimeFormatterBuilder.appendInstant], as that method
         * always parses the instant with the UTC zone offset.
         *
         *
         * If the time '23:59:60' is received, then a simple conversion is applied,
         * replacing the second-of-minute of 60 with 59. This query can be used
         * on the parse result to determine if the leap-second adjustment was made.
         * The query will return `true` if it did adjust to remove the
         * leap-second, and `false` if not. Note that applying a leap-second
         * smoothing mechanism, such as UTC-SLS, is the responsibility of the
         * application, as follows:
         * <pre>
         * TemporalAccessor parsed = formatter.parse(str);
         * Instant instant = parsed.query(Instant::from);
         * if (parsed.query(DateTimeFormatter.parsedLeapSecond())) {
         * // validate leap-second is correct and apply correct smoothing
         * }
        </pre> *
         * @return a query that provides access to whether a leap-second was parsed
         */
        fun parsedLeapSecond(): TemporalQuery<Boolean> {
            return DateTimeFormatter.parsedLeapSecond()
        }
    }
}
