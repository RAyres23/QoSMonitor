/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.type.presentation;

import com.sun.javafx.collections.ObservableListWrapper;
import eu.arrowhead.core.qos.monitor.database.MonitorLog;
import eu.arrowhead.core.qos.monitor.type.FTTSE;
import eu.arrowhead.core.qos.monitor.type.presentation.model.PresentationData;
import eu.arrowhead.core.qos.monitor.type.presentation.model.PresentationEvent;
import eu.arrowhead.core.qos.monitor.type.presentation.model.SceneNode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author ID0084D
 */
public class FTTSE_Presentation extends Presentation {

    private final JFXPanel contentPane;
    private Map<NodeKey, SceneNode> nodes = new HashMap<>();
    private static final int MAX_DATA_POINTS = 50;
    private static final int MAX_TABLE_POINTS = 30;
    private final ObservableList<PresentationEvent> events;

    private enum NodeKey {

        BANDWIDTH("bandwidth"), DELAY("delay");

        private final String name;

        private NodeKey(String name) {
            this.name = name;
        }
    }

    public FTTSE_Presentation(String queueKey, PresentationData data) {
        super("FTTSE Communication - " + queueKey, queueKey, data);
        nodes = new HashMap<>();
        contentPane = new JFXPanel();
        events = new ObservableListWrapper(new ArrayList());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("FXML\\FTTSEDocument.fxml"));

        // building the scene graph must be done on the javafx thread
        Platform.runLater(() -> {
            try {
                contentPane.setScene(init(fxmlLoader));
            } catch (IOException ex) {
                Logger.getLogger(FTTSE_Presentation.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            prepareTimeline();
        });
    }

    @Override
    public void build() {

        setLayout(new BorderLayout());

        JScrollPane root = new JScrollPane(contentPane);
        root.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        root.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

//        root.add(contentPane);
//        add(root, BorderLayout.CENTER);
        add(root, BorderLayout.CENTER);
        setExtendedState(MAXIMIZED_BOTH);
//        setUndecorated(true);
        setResizable(true);
        setMinimumSize(new Dimension(1120, 540));
//        setLocationRelativeTo(null);
        pack();

        setVisible(true);
        requestFocus();
    }

    private Scene init(FXMLLoader fxmlLoader) throws IOException {

        Parent root = fxmlLoader.load();

        ScrollPane charts = (ScrollPane) root.getChildrenUnmodifiable().filtered((Node t) -> t.getId().equals("charts")).get(0);

        HBox box = new HBox(8);

        charts.setContent(box);
//        box.prefHeightProperty().bind(charts.prefViewportHeightProperty());
//        box.prefWidthProperty().bind(charts.prefViewportWidthProperty());

        charts.prefWidthProperty().bind(box.prefWidthProperty());

        NodeKey[] keys = NodeKey.values();

        for (NodeKey key : keys) {
            SceneNode node = new SceneNode(SceneType.AREACHART, key.name);
            nodes.put(key, node);
            box.getChildren().add(node.getChart());
            node.getChart().prefHeightProperty().bind(box.prefHeightProperty());
            HBox.setHgrow(node.getChart(), Priority.ALWAYS);
        }

        VBox vbox = (VBox) root.getChildrenUnmodifiable().filtered((Node t) -> t.getId().equals("vbox")).get(0);

        TableView<PresentationEvent> table = (TableView) vbox.getChildren().filtered((Node t) -> t.getId().equals("events")).get(0);
        table.prefHeightProperty().bind(vbox.prefHeightProperty());

        table.setEditable(false);

        TableColumn source = new TableColumn("Source");
        source.setCellValueFactory(new PropertyValueFactory<PresentationEvent, String>("from"));
        source.setMinWidth(50);

        TableColumn type = new TableColumn("Type");
        type.setCellValueFactory(new PropertyValueFactory<PresentationEvent, String>("type"));
        type.setMinWidth(50);

        TableColumn description = new TableColumn("Description");
        description.setCellValueFactory(new PropertyValueFactory<PresentationEvent, String>("payload"));

        TableColumn severity = new TableColumn("Severity");
        severity.setCellValueFactory(new PropertyValueFactory<PresentationEvent, Integer>("severity"));
        severity.setMinWidth(10);

        table.setItems(events);
        table.getColumns().addAll(source, type, description, severity);

        return new Scene(root);
    }

    @Override
    protected void addDataToSeries() {

        boolean dataChanged = false;

        for (int i = 0; i < 10; i++) { //--add 10 events
            if (data.getEvents().isEmpty()) {
                break;
            }
            events.add(data.getEvents().remove());
        }

        if (events.size() > MAX_TABLE_POINTS) {
            events.remove(0, events.size() - MAX_TABLE_POINTS);
        }

        NodeKey[] keys = NodeKey.values();
        for (int i = 0; i < 10; i++) { //-- add 10 numbers to the plot+
            if (data.getLogs().isEmpty()) {
                break;
            }

            MonitorLog log = data.getLogs().remove();

            for (NodeKey key : keys) {
                Double value = Double.valueOf(log.getParameters().get(key.name));
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

        for (NodeKey key : keys) {
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
        FTTSE.getData().remove(queueKey);
    }

}
