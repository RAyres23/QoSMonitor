/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.event;

import com.google.gson.Gson;
import eu.arrowhead.core.qos.monitor.database.MongoDatabaseManager;
import eu.arrowhead.core.qos.monitor.database.MonitorLog;
import eu.arrowhead.core.qos.monitor.database.MonitorRule;
import eu.arrowhead.core.qos.monitor.event.model.Event;
import eu.arrowhead.core.qos.monitor.event.model.Metadata;
import eu.arrowhead.core.qos.monitor.type.Monitor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Renato
 */
public class SLAVerification implements Runnable {

    private final Monitor monitor;
    private final MonitorRule rule;
    private final MonitorLog log;
    private static final Logger LOG = Logger.getLogger(SLAVerification.class.getName());

    public SLAVerification(Monitor monitor, MonitorRule rule, MonitorLog log) {
        this.monitor = monitor;
        this.rule = rule;
        this.log = log;
    }

    @Override
    public void run() {
        SLAVerificationResponse SLAresponse;
        if (rule.isSoftRealTime()) {
            //FIXME soft real time. Where does this N come from?
            MonitorLog[] logs = MongoDatabaseManager.getInstance().getLastNLogs(rule);
            SLAresponse = monitor.verifyQoS(rule, logs);
        } else {
            SLAresponse = monitor.verifyQoS(rule, log);
        }

        if (SLAresponse.isSLABroken()) {
            EventProducer eventProducer = new EventProducer(createEvent(SLAresponse.getParameters()));
            eventProducer.publishEvent();

            //Only for test purposes
            for (SLAVerificationParameter parameter : SLAresponse.getParameters()) {
                LOG.log(Level.INFO, "Parameter: {0}" + "\n\t" + "Requested Value: {1}"
                        + "\n\t" + "Logged Value: {2}", new Object[]{parameter.getName(), parameter.getRequestedValue(), parameter.getLoggedValue()});
            }
            LOG.log(Level.WARNING, "SLA was broken");
        } else {
            LOG.log(Level.INFO, "SLA was met");
        }
    }

    /**
     * Creates an EventType instance with the information from the given
     * parameter.
     *
     * @param parameters instance containing information to create an EventType
     * @return the EventType instance
     */
    private Event createEvent(List<SLAVerificationParameter> parameters) {
        Event event = new Event();
        Metadata meta = new Metadata();
        meta.setSeverity(1);
        event.setDescription(meta);
        event.setFrom(EventProducer.getProducer());
        event.setType("event");

        event.setPayload(new Gson().toJson(parameters));

        return event;
    }

}
