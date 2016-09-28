/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.event;

import eu.arrowhead.core.qos.monitor.QoSMonitorService;
import eu.arrowhead.core.qos.monitor.database.MongoDatabaseManager;
import eu.arrowhead.core.qos.monitor.database.MonitorLog;
import eu.arrowhead.core.qos.monitor.database.MonitorRule;
import eu.arrowhead.core.qos.monitor.protocol.IProtocol;
import eu.arrowhead.core.qos.monitor.protocol.presentation.model.PresentationEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            MonitorLog[] logs = MongoDatabaseManager.getInstance().getLastNLogs(rule);
            if (logs == null) {
                return;
            }
            SLAresponse = monitor.verifyQoS(rule, logs);
        } else {
            SLAresponse = monitor.verifyQoS(rule, log);
        }

        if (SLAresponse.isSLABroken()) {
//            EventProducer eventProducer = new EventProducer(EventUtil.createEvent(SLAresponse.getParameters()));
//            eventProducer.publishEvent();

            String queueKey = rule.getProviderSystemGroup() + rule.getProviderSystemName() + rule.getConsumerSystemGroup() + rule.getConsumerSystemName();
//            monitor.addEventToPresentationQueue(queueKey, eventProducer.getEvent());
            //FIXME used when not using event handler. testing
            if (QoSMonitorService.SHOW_GRAPHS) {
                monitor.addEventToPresentationQueue(queueKey, new PresentationEvent(EventUtil.createEvent(SLAresponse.getParameters())));
            }

            //Only for test purposes
            SLAresponse.getParameters().stream().forEach((parameter) -> {
                LOG.log(Level.INFO, "Parameter: {0}" + "\n\t" + "Requested Value: {1}"
                        + "\n\t" + "Logged Value: {2}", new Object[]{parameter.getName(), parameter.getRequestedValue(), parameter.getLoggedValue()});
            });
            LOG.log(Level.WARNING, "SLA was broken");
        } else {
            LOG.log(Level.INFO, "SLA was met");
        }
    }
}
