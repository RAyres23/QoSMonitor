/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.type.presentation;

import eu.arrowhead.core.qos.monitor.type.presentation.model.PresentationData;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javax.swing.JFrame;

/**
 *
 * @author ID0084D
 */
public abstract class Presentation extends JFrame {

    protected final int MAX_DATA_POINTS = 50;
    protected final String queueKey;
    protected final PresentationData data;

    protected enum SceneType {
        AREACHART, LINECHART;
    }

    public Presentation(String title, String queueKey, PresentationData data) {
        super(title);
        this.queueKey = queueKey;
        this.data = data;

        final JFXPanel contentPane = new JFXPanel();
        contentPane.setPreferredSize(new Dimension(960, 540));

        // building the scene graph must be done on the javafx thread
        Platform.runLater(() -> {
            contentPane.setScene(init());
            prepareTimeline();
        });

        this.setContentPane(contentPane);

        pack();
        setVisible(true);
        setResizable(true);

        setLocationRelativeTo(null);
    }

    protected abstract Scene init();

    // -- Timeline gets called in the JavaFX Main thread
    protected abstract void prepareTimeline();

    protected abstract void addDataToSeries();

    protected abstract void closeWindow();

    protected final class SceneNode {

        private final XYChart chart;
        private final NumberAxis xAxis;
        private final Series series;
        private int xSeriesData;

        public SceneNode(SceneType type, String title) {

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
                        protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
                        }
                    };
                    break;
                case LINECHART:
                    chart = new LineChart<Number, Number>(xAxis, yAxis) {
                        // Override to remove symbols on each data point
                        @Override
                        protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
                        }
                    };
                    break;
                default:
                    throw new IllegalArgumentException("Chart type unavailable");
            }

            chart.setAnimated(false);
            chart.setId(title);

            // -- Chart Series
            series = new Series();
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

        public Series getSeries() {
            return series;
        }

        public int getXSeriesData() {
            return xSeriesData;
        }

        public int getXSeriesDataAndIncrement() {
            return xSeriesData++;
        }

    }
}
