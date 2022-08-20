package com.hrb.holidays.ui.components.textfields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.text.isDigitsOnly
import com.hrb.holidays.commons.ImmutableLocalDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Composable
fun DateTextField(
    initialValue: ImmutableLocalDate? = null,
    onValueChange: (LocalDate?) -> Unit,
    label: @Composable (() -> Unit)? = null,
    dateTimeFormatterPattern: String = "MM dd yyyy",
    delimiter: Char = '/',
    isError: Boolean = false,
    onError: () -> Unit
) {
    val dateTimeFormatter = remember(dateTimeFormatterPattern) {
        DateTimeFormatter.ofPattern(dateTimeFormatterPattern)
    }
    val placeholderText = remember(dateTimeFormatterPattern) {
        dateTimeFormatterPattern.replace(' ', delimiter).lowercase()
    }
    val sortedPatternSpaceIndexes: List<Int> = remember(dateTimeFormatterPattern) {
        dateTimeFormatterPattern.mapIndexedNotNull { i, c ->
            return@mapIndexedNotNull if (c == ' ') i
            else null
        }
    }
    val maxTextSize = remember(dateTimeFormatterPattern) {
        dateTimeFormatterPattern.replace(" ", "").length
    }
    var valueText by remember {
        mutableStateOf(
            if (initialValue == null) {
                ""
            } else {
                val date = initialValue().format(dateTimeFormatter)
                val builder = StringBuilder()
                date.forEach {
                    builder.append(
                        if (it.isDigit()) it
                        else delimiter
                    )
                }
                builder.toString()
            }
        )
    }

    TextField(
        value = valueText,
        onValueChange = {
            if (it.length <= maxTextSize && it.isDigitsOnly()) {
                valueText = it
                if (it == "") {
                    onValueChange(null)
                } else {
                    try {
                        onValueChange(
                            LocalDate.parse(
                                it.toDateRepresentation(
                                    sortedPatternSpaceIndexes = sortedPatternSpaceIndexes,
                                    replaceChar = ' '
                                ),
                                dateTimeFormatter
                            )
                        )
                    } catch (e: DateTimeParseException) {
                        onError()
                    }
                }
            }
        },
        isError = isError,
        singleLine = true,
        label = label,
        visualTransformation = DateVisualTransformation(sortedPatternSpaceIndexes, delimiter),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = {
            Text(text = placeholderText)
        }
    )
}

private fun CharSequence.toDateRepresentation(
    sortedPatternSpaceIndexes: List<Int>,
    replaceChar: Char
): CharSequence {
    val builder = StringBuilder(this)
    sortedPatternSpaceIndexes.forEach { i ->
        if (i <= builder.length) {
            builder.insert(i, replaceChar)
        }
    }
    return builder
}

private class DateVisualTransformation(
    patternSpaceIndexes: List<Int>,
    private val replaceChar: Char = '/'
) : VisualTransformation {

    private val sortedPatternSpaceIndexes = patternSpaceIndexes.sorted()

    private class DateOffset(
        private val newText: String,
        private val replaceChar: Char,
        sortedPatternSpaceIndexes: List<Int>
    ) : OffsetMapping {

        private val sortedPatternSpaceIndexes = sortedPatternSpaceIndexes.mapIndexed { index, i ->
            if (index == 0) i else i - 1
        }

        override fun originalToTransformed(offset: Int): Int {
            var nbIndexBeforeOffset = 0
            sortedPatternSpaceIndexes.forEach {
                if (it <= offset) nbIndexBeforeOffset++
            }
            return offset + nbIndexBeforeOffset
        }

        override fun transformedToOriginal(offset: Int): Int {
            return offset - newText.substring(0, offset).count { it == replaceChar }
        }
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val newText = text.toDateRepresentation(
            sortedPatternSpaceIndexes = sortedPatternSpaceIndexes,
            replaceChar = replaceChar
        ).toString()

        return TransformedText(
            text = AnnotatedString(newText),
            offsetMapping = DateOffset(
                newText = newText,
                replaceChar = replaceChar,
                sortedPatternSpaceIndexes = sortedPatternSpaceIndexes
            )
        )
    }
}
