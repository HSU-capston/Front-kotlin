package com.example.capston_spotyup.Analyze.Domain

import com.kizitonwose.calendarview.model.CalendarDay
import java.time.Month
import java.time.YearMonth

data class MyCustomMonth( // ← 이름을 CalendarMonth가 아닌 다른 이름으로
    val yearMonth: YearMonth,
    val weekDays: List<List<CalendarDay>>,
    val year: Int,
    val month: Month,
    val numberOfWeekInMonth: Int
)
