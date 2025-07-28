package com.example.sicuan.chart

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.sicuan.R
import com.github.mikephil.charting.data.*

object BarChartUtil {
    fun createSampleEntries(): List<BarEntry> {
        return listOf(
            BarEntry(0f, 40f),
            BarEntry(1f, 15f),
            BarEntry(2f, 20f),
            BarEntry(3f, 35f)
        )
    }

    fun createDataSet(entries: List<BarEntry>, context: Context): BarDataSet {
        val dataSet = BarDataSet(entries, "Penjualan")
        dataSet.color = ContextCompat.getColor(context, R.color.bar)
        return dataSet
    }

    fun createBarData(dataSet: BarDataSet): BarData {
        return BarData(dataSet).apply {
            barWidth = 0.9f
        }
    }
}