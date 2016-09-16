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
import eu.arrowhead.core.qos.monitor.protocol.presentation.model.PresentationEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import eu.arrowhead.core.qos.monitor.protocol.IProtocol;

/**
 *
 * @author Renato
 */
public class SLAVerification implements Runnable {

    private final IProtocol monitor;
    private final MonitorRule rule;
    private final MonitorLog log;
    private static final Logger LOG = Logger.getLogger(SLAVerification.class.getName());

    public SLAVerification(IProtocol monitor, MonitorRule rule, MonitorLog log) {
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
//            EventProducer eventProducer = new EventProducer(createEvent(SLAresponse.getParameters()));
//            eventProducer.publishEvent();

            String queueKey = rule.getProviderSystemGroup() + rule.getProviderSystemName() + rule.getConsumerSystemGroup() + rule.getConsumerSystemName();
//            monitor.addEventToPresentationQueue(queueKey, eventProducer.getEvent());
            //FIXME used when not using event handler. testing
            monitor.addEventToPresentationQueue(queueKey, new PresentationEvent(createEvent(SLAresponse.getParameters())));

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
     * Creates an Event instance with the information from the given
     * parameter.
     *
     * @param parameters instance containing information to create an Event
     * @return the Event instance
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
