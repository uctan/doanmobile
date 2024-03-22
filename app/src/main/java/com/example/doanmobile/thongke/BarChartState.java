package com.example.doanmobile.thongke;

import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

public class BarChartState implements ChartState {
    private BarChart barChart;

    public BarChartState(BarChart barChart) {
        this.barChart = barChart;
    }

    @Override
    public void showChart() {
        barChart.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideChart() {
        barChart.setVisibility(View.GONE);
    }
}


