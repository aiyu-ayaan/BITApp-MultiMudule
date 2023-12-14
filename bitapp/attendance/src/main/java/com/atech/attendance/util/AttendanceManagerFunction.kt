/*
 * BIT Lalpur App
 *
 * Created by Ayaan on 9/1/21, 1:09 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 9/1/21, 11:14 AM
 */



package com.atech.attendance.util

import androidx.compose.ui.graphics.toArgb
import com.atech.core.data_source.room.attendance.AttendanceModel
import com.atech.core.data_source.room.attendance.IsPresent
import com.atech.core.use_case.SyllabusUIModel
import com.atech.core.utils.convertLongToTime
import com.atech.theme.SwipeGreen
import com.atech.theme.SwipeRed
import com.github.sundeepk.compactcalendarview.domain.Event
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun findPercentage(present: Float, total: Float, action: (Float, Float) -> Float) =
    action(present, total)

fun setResources(percentage: Int, action: (Int) -> Unit) = action(percentage)

fun calculatedDays(present: Int, total: Int, action: (Float, Float) -> Float) =
    action(present.toFloat(), total.toFloat())


fun Long.getRelativeDateForAttendance(): String {
    val currentTime = System.currentTimeMillis()
    val difference = currentTime - this
    val seconds = difference / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val months = days / 30

    return when {
        seconds < 60 -> {
            " Just now"
        }

        minutes < 60 -> {
            " ${minutes.toInt()} minute${if (minutes.toInt() > 1) "s" else ""} ago"
        }

        hours < 24 -> {
            " ${hours.toInt()} hour${if (hours.toInt() > 1) "s" else ""} ago"
        }

        days == 1L -> {
            " Yesterday"
        }

        days < 30 -> {
            " ${this.convertLongToTime("dd MMM")}"
        }

        months < 12 -> {
            " ${this.convertLongToTime("dd MMM")}"
        }

        else -> {
            " ${this.convertLongToTime("dd MMM yyyy")}"
        }
    }
}


fun AttendanceModel.getEventList(): List<Event> {
    val events = mutableListOf<Event>()
    this.days.totalDays.asReversed().forEach {
        it.totalClasses?.let { totalClasses ->
            var v = totalClasses
            while (v-- != 0) {
                events.add(
                    Event(
                        if (it.isPresent) SwipeGreen.toArgb() else SwipeRed.toArgb(), it.day
                    )
                )
            }
        }
    }
    return events
}

fun AttendanceModel.getCurrentMonthList(data: Date): List<IsPresent> = this.run {
    val list = arrayListOf<IsPresent>()
    val sf = SimpleDateFormat("MMMM/yyyy", Locale.getDefault())
    val compare = sf.format(data)
    this.days.totalDays.getOnly50Data().forEach {
        if (compare.equals(sf.format(it.day))) {
            list.add(it)
        }
    }
    list.toList()
}

private fun ArrayList<IsPresent>.getOnly50Data(): ArrayList<IsPresent> {
    val list = arrayListOf<IsPresent>()
    this.asReversed().forEach { isPresent ->
        if (list.size < 50) {
            list.add(isPresent)
        }
    }
    return list
}
