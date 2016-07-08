package eu.arrowhead.qos.monitor.type;

import eu.arrowhead.common.model.messages.QoSMonitorAddRule;
import eu.arrowhead.common.model.messages.QoSMonitorLog;
import eu.arrowhead.qos.monitor.event.SLAVerificationResponse;
import eu.arrowhead.qos.monitor.database.MonitorLog;
import eu.arrowhead.qos.monitor.database.MonitorRule;

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
     * Verifies if the SLA is being respected regarding the existing rule and
     * the received monitor log. The logs parameter
     *
     * @param logs the monitor log
     * @param rule the monitor rule
     * @return
     */
    public SLAVerificationResponse verifyQoS(MonitorRule rule, MonitorLog... logs);

}
