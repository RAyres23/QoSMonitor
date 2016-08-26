package eu.arrowhead.core.qos.monitor.type;

import eu.arrowhead.common.model.messages.QoSMonitorAddRule;
import eu.arrowhead.common.model.messages.QoSMonitorLog;
import eu.arrowhead.common.model.messages.ServiceError;
import eu.arrowhead.core.qos.monitor.database.MonitorLog;
import eu.arrowhead.core.qos.monitor.database.MonitorRule;
import eu.arrowhead.core.qos.monitor.event.SLAVerificationResponse;
import eu.arrowhead.core.qos.monitor.event.model.Event;
import eu.arrowhead.core.qos.monitor.type.presentation.model.PresentationEvent;

public interface Monitor {

    /**
     * Filters a QoSMonitorAddRule message into a MonitorRule.
     *
     * @param message message to filter
     * @return instance of created MonitorRule
     */
    public MonitorRule filterRuleMessage(QoSMonitorAddRule message);

    /**
     * Filters a QoSMonitorLog message into a MonitorLog
     *
     * @param message message to filter
     * @return instance of created MonitorLog
     */
    public MonitorLog filterLogMessage(QoSMonitorLog message);

    /**
     * Adds a new Event to the queue of events.
     *
     * @param queueKey source of the Event
     * @param event new Event to add
     */
    public void addEventToPresentationQueue(String queueKey, PresentationEvent event);

    /**
     * Creates an Event to the EventHandler and shows to the user if JavaFX
     * enabled.
     *
     * @param error ServiceError message
     * @return
     */
    public Event addServiceError(ServiceError error);

    /**
     * Verifies if the SLA is being respected regarding the existing rule and
     * the received monitor log. The logs parameter
     *
     * @param logs the monitor log
     * @param rule the monitor rule
     * @return
     */
    public SLAVerificationResponse verifyQoS(MonitorRule rule, MonitorLog... logs);

}
