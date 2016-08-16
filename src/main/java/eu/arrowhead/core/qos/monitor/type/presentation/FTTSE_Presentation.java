/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.type.presentation;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart.Series;

/**
 *
 * @author ID0084D
 */
public class FTTSE_Presentation extends Presentation {

    private Series series;
    
    private enum Key {

        BANDWIDTH("bandwidth"), DELAY("delay");

        private final String name;

        private Key(String name) {
            this.name = name;
        }
    }

    public FTTSE_Presentation(String queueKey, PresentationData data) {
        super(queueKey, data);
    }

    @Override
    protected void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void addDataToSeries() {
        for (int i = 0; i < 20; i++) { //-- add 20 numbers to the plot+
            if (data.getLogs().isEmpty()) {
                break;
            }
            series.getData().add(new AreaChart.Data(xSeriesData++, data.remove()));
        }
        // remove points to keep us at no more than MAX_DATA_POINTS
        if (series.getData().size() > MAX_DATA_POINTS) {
            series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
        }
        // update 
        xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData - 1);
    }

}
