package com.hrb.holidays.commons

import java.util.*


fun String.capitalizeFirstChar(locale: Locale = Locale.getDefault()): String =
    this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(locale)
        else it.toString()
    }
