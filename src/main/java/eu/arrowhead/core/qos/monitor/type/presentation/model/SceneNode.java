/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.type.presentation.model;

import eu.arrowhead.core.qos.monitor.type.presentation.Presentation;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 *
 * @author ID0084D
 */
public class SceneNode {

    private XYChart chart;
    private NumberAxis xAxis;
    private XYChart.Series series;
    private int xSeriesData;

    public SceneNode(Presentation.SceneType type, String title) {

        xAxis = new NumberAxis();
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);

        switch (type) {
            case AREACHART:
                chart = new AreaChart<Number, Number>(xAxis, yAxis) {
                    // Override to remove symbols on each data point
                    @Override
                    protected void dataItemAdded(XYChart.Series<Number, Number> series, int itemIndex, XYChart.Data<Number, Number> item) {
                    }
                };
                break;
            case LINECHART:
                chart = new LineChart<Number, Number>(xAxis, yAxis) {
                    // Override to remove symbols on each data point
                    @Override
                    protected void dataItemAdded(XYChart.Series<Number, Number> series, int itemIndex, XYChart.Data<Number, Number> item) {
                    }
                };
                break;
            default:
                throw new IllegalArgumentException("Chart type unavailable");
        }

        chart.setAnimated(false);
        chart.setId(title);

        // -- Chart Series
        series = new XYChart.Series();
        series.setName(title);
        chart.getData().add(series);

        xSeriesData = 0;
    }

    public XYChart getChart() {
        return chart;
    }

    public NumberAxis getXAxis() {
        return xAxis;
    }

    public XYChart.Series getSeries() {
        return series;
    }

    public int getXSeriesData() {
        return xSeriesData;
    }

    public int getXSeriesDataAndIncrement() {
        return xSeriesData++;
    }

}
