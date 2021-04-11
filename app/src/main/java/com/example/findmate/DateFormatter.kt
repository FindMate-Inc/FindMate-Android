package com.example.findmate

import android.content.Context
import android.util.Log
import java.time.*
import java.time.format.DateTimeFormatter

fun getUtcOffsetDateTime(epochMilli: Long): OffsetDateTime {
    return Instant.ofEpochMilli(epochMilli).atOffset(ZoneOffset.UTC)
}

fun OffsetDateTime.asRelativeTime(context: Context): String {
    val currentZoneId = ZoneId.systemDefault()

    val zonnedTargetTime = atZoneSameInstant(currentZoneId)
    val zonnedCurrentTime =
        OffsetDateTime.now(toZonedDateTime().zone).atZoneSameInstant(currentZoneId)
    val diffPeriod = Period.between(zonnedTargetTime.toLocalDate(), zonnedCurrentTime.toLocalDate())
    val diffDuration = Duration.between(zonnedTargetTime.toLocalTime(), zonnedCurrentTime.toLocalTime())

    Log.d("TestPish", "diff period ${diffPeriod} ${diffDuration}")
    val resources = context.resources
    return when {
        diffPeriod.years > 0 || diffPeriod.months > 0 || diffPeriod.days > 7 -> zonnedTargetTime.format(
            DateTimeFormatter.ofPattern(resources.getString(R.string.date_formatter_date))
        )
        diffPeriod.days == 7 -> resources.getQuantityString(
            R.plurals.date_formatter_past_weeks,
            1,
            1
        )
        diffPeriod.days > 0 -> {
            val days = diffPeriod.days
            resources.getQuantityString(R.plurals.date_formatter_past_days, days, days)
        }
        diffDuration.toMinutes() >= 60 -> {
            val hours = diffDuration.toHours().toInt()
            resources.getQuantityString(R.plurals.date_formatter_past_hours, hours, hours)
        }
        diffDuration.toMinutes() > 0 -> {
            val minutes = diffDuration.toMinutes().toInt()
            resources.getQuantityString(R.plurals.date_formatter_past_minutes, minutes, minutes)
        }
        diffDuration.toMinutes() == 0L -> resources.getString(R.string.date_formatter_now)
        else -> zonnedTargetTime.format(DateTimeFormatter.ofPattern(resources.getString(R.string.date_formatter_date)))
    }
}