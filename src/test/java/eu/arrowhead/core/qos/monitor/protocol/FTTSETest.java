/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.protocol;

import com.google.gson.Gson;
import eu.arrowhead.common.exception.MissingParameterException;
import eu.arrowhead.common.exception.NoMonitorParametersException;
import eu.arrowhead.common.model.ArrowheadSystem;
import eu.arrowhead.common.model.messages.AddMonitorLog;
import eu.arrowhead.common.model.messages.AddMonitorRule;
import eu.arrowhead.common.model.messages.EventMessage;
import eu.arrowhead.core.qos.monitor.QoSMonitorService;
import eu.arrowhead.core.qos.monitor.database.MonitorLog;
import eu.arrowhead.core.qos.monitor.database.MonitorRule;
import eu.arrowhead.core.qos.monitor.event.SLAVerificationParameter;
import eu.arrowhead.core.qos.monitor.event.SLAVerificationResponse;
import eu.arrowhead.core.qos.monitor.event.model.Event;
import eu.arrowhead.core.qos.monitor.event.model.Metadata;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author ID0084D
 */
public class FTTSETest {

    private static String PROTOCOL;
    private static ArrowheadSystem PROVIDER;
    private static ArrowheadSystem CONSUMER;
    private static FTTSE instance;
    private static boolean lastGraphState;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public FTTSETest() {
    }

    @BeforeClass
    public static void setUp() {
        lastGraphState = QoSMonitorService.SHOW_GRAPHS;
        QoSMonitorService.SHOW_GRAPHS = false;
        instance = new FTTSE();
        PROTOCOL = "FTTSE";
        PROVIDER = new ArrowheadSystem("providerGroup", "providerName", "localhost", "8080", "authInfo");
        CONSUMER = new ArrowheadSystem("consumerGroup", "consumerName", "localhost", "8080", "authInfo");
    }

    @AfterClass
    public static void tearDown() {
        QoSMonitorService.SHOW_GRAPHS = lastGraphState;
    }

    /**
     * Tests if an exception is thrown when there is no stream_id in the
     * parameters
     */
    @Test
    public void testFilterRuleMessageNoStreamID() {
        System.out.println("filterRuleMessageNoStreamID");

        //ARRANGE
        thrown.expect(MissingParameterException.class);
        thrown.expectMessage("Missing stream_id in FTTSE rule!");

        AddMonitorRule message = createAddMonitorRuleMessage();
        Map<String, String> ruleParameters = new HashMap<>();
        ruleParameters.put("bandwidth", "200");
        message.setParameters(ruleParameters);

        //ACT
        instance.filterRuleMessage(message);
    }

    /**
     * Tests if an exception is thrown when there is no NLogs value in the
     * parameters when soft real time monitoring is enabled
     */
    @Test
    public void testFilterRuleMessageNoNLogsValueIfSoftRealTimeEnabled() {
        System.out.println("filterRuleMessageNoNLogsValueIfSoftRealTimeEnabled");

        //ARRANGE
        thrown.expect(MissingParameterException.class);
        thrown.expectMessage("Missing NLogs in FTTSE rule!");

        AddMonitorRule message = createAddMonitorRuleMessage();
        Map<String, String> ruleParameters = new HashMap<>();
        ruleParameters.put("stream_id", "1");
        ruleParameters.put("bandwidth", "200");
        message.setParameters(ruleParameters);
        message.setSoftRealTime(true);

        //ACT
        instance.filterRuleMessage(message);
    }

    /**
     * Tests if an exception is thrown when there are no consistent monitor
     * parameters
     */
    @Test
    public void testFilterRuleMessageNoMonitorParameters() {
        System.out.println("filterRuleMessageNoConsistentMonitorParameters");

        //ARRANGE
        thrown.expect(NoMonitorParametersException.class);
        thrown.expectMessage("No monitor parameters were found!");

        AddMonitorRule message = createAddMonitorRuleMessage();
        Map<String, String> ruleParameters = new HashMap<>();
        ruleParameters.put("stream_id", "1");
        ruleParameters.put("abc", "150");
        message.setParameters(ruleParameters);

        //ACT
        instance.filterRuleMessage(message);
    }

    /**
     * Tests of filterRuleMessage method
     */
    @Test
    public void testFilterRuleMessage() {
        System.out.println("filterRuleMessage");

        //ARRANGE
        AddMonitorRule message = createAddMonitorRuleMessage();
        Map<String, String> addRuleParameters = new HashMap<>();
        addRuleParameters.put("stream_id", "1");
        addRuleParameters.put("bandwidth", "200");
        addRuleParameters.put("delay", "200");
        message.setSoftRealTime(true);
        addRuleParameters.put("NLogs", "10");
        message.setParameters(addRuleParameters);

        MonitorRule expResult = createMonitorRule("1", "200", "200");
        expResult.setSoftRealTime(true);
        expResult.getParameters().put("NLogs", "10");

        //ACT
        MonitorRule result = instance.filterRuleMessage(message);

        //ASSERT
        assertEquals(expResult, result);
    }

    /**
     * Tests if an exception is thrown when there are no consistent monitor
     * parameters
     */
    @Test
    public void testFilterLogMessageNoConsistentMonitorParameters() {
        System.out.println("filterLogMessageNoConsistentMonitorParameters");

        //ARRANGE
        thrown.expect(NoMonitorParametersException.class);
        thrown.expectMessage("No monitor parameters were found!");

        AddMonitorLog message = createAddMonitorLogMessage();
        Map<String, String> logParameters = new HashMap<>();
        logParameters.put("abc", "150");
        message.setParameters(logParameters);

        //ACT
        instance.filterLogMessage(message);
    }

    /**
     * Test of filterLogMessage method.
     */
    public void testFilterLogMessage() {
        System.out.println("filterLogMessage");

        //ARRANGE
        AddMonitorLog message = createAddMonitorLogMessage();
        Map<String, String> addLogParameters = new HashMap<>();
        addLogParameters.put("bandwidth", "250");
        addLogParameters.put("delay", "101");
        message.setParameters(addLogParameters);

        MonitorLog expResult = createMonitorLog("200", "200");

        //ACT
        MonitorLog result = instance.filterLogMessage(message);

        //ASSERT
        assertEquals(expResult, result);
    }

    /**
     * Tests if an exception is thrown when there is no stream_id in the
     * parameters
     */
    public void testCreateEventNoStreamID() {
        System.out.println("createEventNoStreamID");

        //ARRANGE
        thrown.expect(MissingParameterException.class);
        thrown.expectMessage("Missing stream_id in FTTSE event!");
        EventMessage message = createEventMessage();

        //ACT
        instance.createEvent(message);
    }

    /**
     * Test of createEvent method.
     */
    public void testCreateEvent() {
        System.out.println("createEvent");

        //ARRANGE
        EventMessage message = createEventMessage();
        Map<String, String> messageParameters = new HashMap<>();
        messageParameters.put("stream_id", "1");

        Event expResult = createEvent();
        Map<String, String> eventParameters = new HashMap<>();
        eventParameters.put("stream_id", "1");
        expResult.setPayload(new Gson().toJson(eventParameters));

        //ACT
        Event result = instance.createEvent(message);

        //ASSERT
        assertEquals(expResult, result);
    }

    /**
     * Test of verifyQoS method in real time, meeting QoS requirements.
     */
    public void testVerifyQoSNotMetRealTime() {
        System.out.println("verifyQoSNotMet");

        MonitorRule rule = createMonitorRule("1", "200", "40");
        Map<String, String> ruleParameters = new HashMap<>();
        rule.setParameters(ruleParameters);

        MonitorLog log = createMonitorLog("120", "60");

        SLAVerificationResponse expResult = new SLAVerificationResponse();
        List<SLAVerificationParameter> verificationParameters = new ArrayList();
        verificationParameters.add(new SLAVerificationParameter("bandwidth", 200.0, 120.0));
        verificationParameters.add(new SLAVerificationParameter("delay", 40.0, 60.0));
        expResult.setParameters(verificationParameters);

        SLAVerificationResponse result = instance.verifyQoS(rule, log);

        assertEquals(expResult, result);
    }

    /**
     * Test of verifyQoS method in real time, meeting QoS requirements.
     */
    public void testVerifyQoSMetRealTime() {
        System.out.println("verifyQoSNotMet");

        MonitorRule rule = createMonitorRule("1", "200", "40");

        MonitorLog log = createMonitorLog("120", "60");

        SLAVerificationResponse expResult = new SLAVerificationResponse();
        List<SLAVerificationParameter> verificationParameters = new ArrayList();
        verificationParameters.add(new SLAVerificationParameter("bandwidth", 200.0, 240.0));
        verificationParameters.add(new SLAVerificationParameter("delay", 40.0, 30.0));
        expResult.setParameters(verificationParameters);

        SLAVerificationResponse result = instance.verifyQoS(rule, log);

        assertEquals(expResult, result);
    }

    /**
     * Test of verifyQoS method in soft real time, meeting QoS requirements.
     */
    public void testVerifyQoSMetSoftRealTime() {
        System.out.println("verifyQoSNotMet");

        MonitorRule rule = createMonitorRule("1", "200", "40");
        rule.setSoftRealTime(true);

        MonitorLog log = createMonitorLog("120", "60");

        SLAVerificationResponse expResult = new SLAVerificationResponse();
        List<SLAVerificationParameter> verificationParameters = new ArrayList();
        verificationParameters.add(new SLAVerificationParameter("bandwidth", 200.0, 240.0));
        verificationParameters.add(new SLAVerificationParameter("delay", 40.0, 30.0));
        expResult.setParameters(verificationParameters);

        SLAVerificationResponse result = instance.verifyQoS(rule, log);

        assertEquals(expResult, result);
    }

    /**
     * Test of verifyQoS method in soft real time, not meeting QoS requirements.
     */
    public void testVerifyQoSNotMetSoftRealTime() {
        System.out.println("verifyQoSNotMetSoftRealTime");

        MonitorRule rule = createMonitorRule("1", "200", "40");
        rule.setSoftRealTime(true);
        rule.getParameters().put("NLogs", "20");

        MonitorLog[] logs = create20Logs();

        SLAVerificationResponse expResult = new SLAVerificationResponse();
        List<SLAVerificationParameter> verificationParameters = new ArrayList();
        verificationParameters.add(new SLAVerificationParameter("bandwidth", 200.0, 310.9));
        verificationParameters.add(new SLAVerificationParameter("delay", 40.0, 47.85));
        expResult.setParameters(verificationParameters);

        SLAVerificationResponse result = instance.verifyQoS(rule, logs);

        assertEquals(expResult, result);
    }

    private MonitorLog[] create20Logs() {
        return new MonitorLog[]{
            createMonitorLog("112", "58"),
            createMonitorLog("162", "70"),
            createMonitorLog("158", "65"),
            createMonitorLog("164", "45"),
            createMonitorLog("100", "20"),
            createMonitorLog("780", "100"),
            createMonitorLog("12", "90"),
            createMonitorLog("1200", "10"),
            createMonitorLog("192", "48"),
            createMonitorLog("241", "40"),
            createMonitorLog("243", "39"),
            createMonitorLog("351", "20"),
            createMonitorLog("121", "17"),
            createMonitorLog("801", "45"),
            createMonitorLog("709", "38"),
            createMonitorLog("125", "85"),
            createMonitorLog("251", "21"),
            createMonitorLog("199", "37"),
            createMonitorLog("177", "49"),
            createMonitorLog("120", "60")
        };
    }

    private AddMonitorRule createAddMonitorRuleMessage() {
        AddMonitorRule message = new AddMonitorRule();
        message.setProtocol(PROTOCOL);
        message.setProvider(PROVIDER);
        message.setConsumer(CONSUMER);
        message.setSoftRealTime(false);
        return message;
    }

    private AddMonitorLog createAddMonitorLogMessage() {
        AddMonitorLog message = new AddMonitorLog();
        message.setProtocol(PROTOCOL);
        message.setProvider(PROVIDER);
        message.setConsumer(CONSUMER);
        message.setTimestamp(1475079675L);
        return message;
    }

    private MonitorRule createMonitorRule(String stream_id, String bandwidth, String delay) {
        MonitorRule rule = new MonitorRule();
        rule.setProviderSystemGroup("providerGroup");
        rule.setProviderSystemName("providerName");
        rule.setConsumerSystemGroup("consumerGroup");
        rule.setConsumerSystemName("consumerName");
        rule.setProtocol("FTTSE");
        rule.setSoftRealTime(false);
        Map<String, String> monitorRuleParameters = new HashMap<>();
        monitorRuleParameters.put("stream_id", stream_id);
        monitorRuleParameters.put("bandwidth", bandwidth);
        monitorRuleParameters.put("delay", delay);
        rule.setParameters(monitorRuleParameters);
        return rule;
    }

    private MonitorLog createMonitorLog(String bandwidth, String delay) {
        MonitorLog log = new MonitorLog();
        log.setProtocol(PROTOCOL);
        log.setTimestamp(1475079675L);
        Map<String, String> monitorLogParameters = new HashMap<>();
        monitorLogParameters.put("bandwidth", bandwidth);
        monitorLogParameters.put("delay", delay);
        log.setParameters(monitorLogParameters);
        return log;
    }

    private EventMessage createEventMessage() {
        EventMessage message = new EventMessage();
        message.setProtocol(PROTOCOL);
        message.setSystem(PROVIDER);
        message.setErrorMessage("Test error message");
        return message;
    }

    private Event createEvent() {
        Event event = new Event();
        Metadata meta = new Metadata();
        meta.setSeverity(1);
        event.setType("event");
        event.setFrom("qosmonitorsystem");
        event.setDescription(meta);
        return event;
    }
}
