package com.example.capston_spotyup.Analyze.Domain

import android.content.Context
import android.widget.TextView
import com.example.capston_spotyup.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF


class CustomMarkerView(
    context: Context,
    layoutResource: Int
) : MarkerView(context, layoutResource) {

    private val tvMarkerValue: TextView = findViewById(R.id.tvMarkerValue)

    // Entry 값이 변경될 때마다 호출
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        tvMarkerValue.text = "${e?.y?.toInt()}점"
        super.refreshContent(e, highlight)
    }

    // 말풍선 위치 설정
    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}
