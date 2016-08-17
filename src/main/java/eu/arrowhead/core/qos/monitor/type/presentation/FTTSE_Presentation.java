/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.type.presentation;

import eu.arrowhead.core.qos.monitor.database.MonitorLog;
import eu.arrowhead.core.qos.monitor.type.FTTSE;
import eu.arrowhead.core.qos.monitor.type.presentation.model.PresentationData;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author ID0084D
 */
public class FTTSE_Presentation extends Presentation {

    private final Map<Key, SceneNode> nodes = new HashMap<>();

    private enum Key {

        BANDWIDTH("bandwidth"), DELAY("delay");

        private final String name;

        private Key(String name) {
            this.name = name;
        }
    }

    public FTTSE_Presentation(String queueKey, PresentationData data) {
        super("FTTSE Communication - " + queueKey, queueKey, data);
    }

    @Override
    protected Scene init() {
        FlowPane root = new FlowPane();

        Key[] keys = Key.values();
        for (Key key : keys) {
            SceneNode node = new SceneNode(SceneType.AREACHART, key.name);
            nodes.put(key, node);
            root.getChildren().add(node.getChart());
        }

        return new Scene(root);
    }

    // -- Timeline gets called in the JavaFX Main thread
    @Override
    protected void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    @Override
    protected void addDataToSeries() {

        boolean dataChanged = false;

        Key[] keys = Key.values();
        for (int i = 0; i < 10; i++) { //-- add 20 numbers to the plot+
            if (data.getLogs().isEmpty()) {
                break;
            }

            MonitorLog log = data.getLogs().remove();

            for (Key key : keys) {
                Double value = log.getParameters().get(key.name);
                if (value == null) {
                    continue;
                }
                SceneNode node = nodes.get(key);
                node.getSeries().getData().add(new Data(node.getXSeriesDataAndIncrement(), value));
                dataChanged = true;
            }
        }

        if (!dataChanged) {
            return;
        }

        for (Key key : keys) {
            SceneNode node = nodes.get(key);
            ObservableList nodeSeriesData = node.getSeries().getData();
            NumberAxis xAxis = node.getXAxis();

            // remove points to keep us at no more than MAX_DATA_POINTS
            if (nodeSeriesData.size() > MAX_DATA_POINTS) {
                nodeSeriesData.remove(0, nodeSeriesData.size() - MAX_DATA_POINTS);
            }

            int xSeriesData = node.getXSeriesData();

            // update 
            xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
            xAxis.setUpperBound(xSeriesData - 1);
        }

    }

    @Override
    protected void closeWindow() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                FTTSE.getData().remove(queueKey);
            }
        });
    }

}
