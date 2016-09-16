/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.protocol.presentation;

import eu.arrowhead.core.qos.monitor.event.model.Event;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ID0084D
 */
public class FTTSEDocumentController implements Initializable {

    @FXML
    private ScrollPane charts;
    @FXML
    private TableView<Event> events;
    @FXML
    private VBox vbox;
    @FXML
    private Label labelEvents;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO no need to do anything for now
    }

    public ScrollPane getCharts() {
        return charts;
    }

    public TableView<Event> getTable() {
        return events;
    }

}
