/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.type.presentation;

import java.awt.Dimension;
import javafx.embed.swing.JFXPanel;
import javax.swing.JFrame;

/**
 *
 * @author ID0084D
 */
public abstract class Presentation extends JFrame {

    protected final String queueKey;
    protected final PresentationData data;

    public Presentation(String queueKey, PresentationData data) {
        super("Monitor of communication: " + queueKey);
        this.queueKey = queueKey;
        this.data = data;

        final JFXPanel contentPane = new JFXPanel();
        contentPane.setPreferredSize(new Dimension(960, 540));
    }

    protected abstract void init();
    
    protected abstract void addDataToSeries();

}
