/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.protocol.presentation;

import eu.arrowhead.core.qos.monitor.protocol.presentation.model.PresentationData;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafx.animation.AnimationTimer;
import javax.swing.JFrame;

/**
 *
 * @author ID0084D
 */
public abstract class Presentation extends JFrame {

    protected final String queueKey;
    protected final PresentationData data;

    public Presentation(String title, String queueKey, PresentationData data) {
        super(title);
        this.queueKey = queueKey;
        this.data = data;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow();
            }
        });
    }

    public abstract void build();

    // -- Timeline gets called in the JavaFX Main thread
    protected void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    protected abstract void addDataToSeries();

    protected abstract void closeWindow();
}
