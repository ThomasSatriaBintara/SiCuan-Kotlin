package com.example.sicuan.chart

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.sicuan.R
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

object BarChartUtil {
    fun createDataSet(entries: List<BarEntry>, context: Context): BarDataSet {
        return BarDataSet(entries, "Penjualan").apply {
            color = ContextCompat.getColor(context, R.color.bar)
        }
    }

    fun createBarData(dataSet: BarDataSet): BarData {
        return BarData(dataSet).apply {
            barWidth = 0.9f
        }
    }
}