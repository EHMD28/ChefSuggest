package io.github.ehmd28.chefsuggest

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

fun getDisplayNameFromFileName(fileName: String): String {
    val displayName = try {
        val dateTime = LocalDateTime.parse(fileName)
        dateTime.format(DateTimeConstants.displayFormat)
    } catch (_: IllegalArgumentException) {
        "ERROR: Expected date. Received: '${fileName}'"
    }
    return displayName
}
