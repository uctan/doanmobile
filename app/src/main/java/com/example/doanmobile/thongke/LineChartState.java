package com.example.doanmobile.thongke;

import android.view.View;

import com.github.mikephil.charting.charts.LineChart;

public class LineChartState implements ChartState {
    private LineChart lineChart;

    public LineChartState(LineChart lineChart) {
        this.lineChart = lineChart;
    }

    @Override
    public void showChart() {
        lineChart.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideChart() {
        lineChart.setVisibility(View.GONE);
    }
}
