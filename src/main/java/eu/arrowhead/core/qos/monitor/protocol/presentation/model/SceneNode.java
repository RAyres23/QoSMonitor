/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.protocol.presentation.model;

import com.sun.javafx.charts.Legend;
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Font;

/**
 *
 * @author ID0084D
 */
public class SceneNode {

    private final XYChart chart;
    private final NumberAxis xAxis;
    private final XYChart.Series series;
    private int xSeriesData;
    private final int fontSize = 17;

    public SceneNode(String name, String unit) {

        xAxis = new NumberAxis();
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis(0, Long.MAX_VALUE, 1);
        yAxis.setAutoRanging(true);

        chart = new AreaChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override
            protected void dataItemAdded(XYChart.Series<Number, Number> series, int itemIndex, XYChart.Data<Number, Number> item) {
            }
        };

        chart.setAnimated(false);
        chart.setTitle(unit);
        chart.setTitleSide(Side.LEFT);
        chart.setMinWidth(720);

        Legend legend = (Legend) chart.lookup(".chart-legend");
        legend.setStyle("-fx-font-size: " + fontSize + "px;");

        xAxis.tickLabelFontProperty().set(Font.font(fontSize));
        yAxis.tickLabelFontProperty().set(Font.font(fontSize));

        // -- Chart Series
        series = new XYChart.Series();
        series.setName(name);
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
