package eu.arrowhead.core.qos.monitor.protocol;

import eu.arrowhead.common.exception.InvalidParameterException;
import eu.arrowhead.common.exception.MissingParameterException;
import eu.arrowhead.common.exception.NoMonitorParametersException;
import eu.arrowhead.common.model.ArrowheadSystem;
import eu.arrowhead.common.model.messages.AddMonitorLog;
import eu.arrowhead.common.model.messages.AddMonitorRule;
import eu.arrowhead.common.model.messages.EventMessage;
import eu.arrowhead.core.qos.monitor.QoSMonitorService;
import eu.arrowhead.core.qos.monitor.database.FilterParameter;
import eu.arrowhead.core.qos.monitor.database.MongoDatabaseManager;
import eu.arrowhead.core.qos.monitor.database.MonitorLog;
import eu.arrowhead.core.qos.monitor.database.MonitorRule;
import eu.arrowhead.core.qos.monitor.event.EventUtil;
import eu.arrowhead.core.qos.monitor.event.SLAVerificationParameter;
import eu.arrowhead.core.qos.monitor.event.SLAVerificationResponse;
import eu.arrowhead.core.qos.monitor.event.model.Event;
import eu.arrowhead.core.qos.monitor.protocol.presentation.FTTSE_Presentation;
import eu.arrowhead.core.qos.monitor.protocol.presentation.model.PresentationData;
import eu.arrowhead.core.qos.monitor.protocol.presentation.model.PresentationEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class FTTSE implements IProtocol {

    public static final Logger LOG = Logger.getLogger(FTTSE.class.getName());
    private static final Map<String, PresentationData> DATA = new ConcurrentHashMap();
//    private static final Map<String, PresentationData> DATA = new HashMap();

    private enum Monitor {

        BANDWIDTH("bandwidth"), DELAY("delay");

        private final String name;

        private Monitor(String name) {
            this.name = name;
        }
    }

    private enum Key {

        STREAMID("stream_id");

        private final String name;

        private Key(String name) {
            this.name = name;
        }
    }

    public FTTSE() {
    }

    public static Map<String, PresentationData> getData() {
        return DATA;
    }

    @Override
    public MonitorRule filterRuleMessage(AddMonitorRule message) {
        ArrowheadSystem provider = message.getProvider();
        ArrowheadSystem consumer = message.getConsumer();

        Map<String, String> parameters = filterParameters(message.getParameters());

        if (parameters.isEmpty()) {
            throw new NoMonitorParametersException("No monitor parameters were found!");
        }

        String streamIDName = Key.STREAMID.name;
        String streamID = message.getParameters().get(streamIDName);

        if (streamID == null) {
            throw new MissingParameterException("Missing " + streamIDName + " in FTTSE rule!");
        }

        try {
            Integer.valueOf(message.getParameters().get(streamIDName));
            parameters.put(streamIDName, streamID);
        } catch (NumberFormatException ex) {
            throw new InvalidParameterException("Value of parameter "
                    + streamIDName + " is not parsable. Please make sure "
                    + "that no invalid characters are present");
        }

        if (message.isSoftRealTime()) {
            String nLogs = message.getParameters().get(NLOGS);

            if (nLogs == null) {
                throw new MissingParameterException("Missing " + NLOGS + " in FTTSE rule!");
            }

            try {
                Integer.valueOf(message.getParameters().get(NLOGS));
                parameters.put(NLOGS, nLogs);
            } catch (NumberFormatException ex) {
                throw new InvalidParameterException("Value of parameter "
                        + NLOGS + " is not parsable. Please make sure "
                        + "that no invalid characters are present");
            }
        }

        return new MonitorRule(message.getProtocol(),
                provider.getSystemName(), provider.getSystemGroup(),
                consumer.getSystemName(), consumer.getSystemGroup(),
                parameters, message.isSoftRealTime());
    }

    @Override
    public MonitorLog filterLogMessage(AddMonitorLog message) {
        MonitorLog log = new MonitorLog();
        log.setProtocol(message.getProtocol());
        log.setTimestamp(message.getTimestamp());

        Map<String, String> parameters = filterParameters(message.getParameters());

        if (parameters.isEmpty()) {
            throw new NoMonitorParametersException("No monitor parameters were found!");
        }

        log.setParameters(parameters);

        if (QoSMonitorService.SHOW_GRAPHS) {
            String queueKey = (message.getProvider().getSystemGroup() + message.getProvider().getSystemName() + message.getConsumer().getSystemGroup() + message.getConsumer().getSystemName());
            if (!(DATA.containsKey(queueKey))) {
                PresentationData data = new PresentationData();
                DATA.put(queueKey, data);
                data.getLogs().add(log);
                Runnable r = () -> {
                    new FTTSE_Presentation(queueKey, data).build();
                };
                new Thread(r).start();
            } else {
                DATA.get(queueKey).getLogs().add(log);
            }
        }

        return log;
    }

    @Override
    public void addEventToPresentationQueue(String queueKey, PresentationEvent event) {
        DATA.get(queueKey).getEvents().add(event);
    }

    @Override
    public Event createEvent(EventMessage message) {

        String stream = message.getParameters().get(Key.STREAMID.name);
        if (stream == null) {
            throw new MissingParameterException("Missing " + stream + " in FTTSE event!");
        }

        List<MonitorRule> rules = MongoDatabaseManager.getInstance().findRuleByParameters(new FilterParameter(Key.STREAMID.name, stream));

        if (QoSMonitorService.SHOW_GRAPHS) {
            rules.stream().map((rule) -> (rule.getProviderSystemGroup() + rule.getProviderSystemName() + rule.getConsumerSystemGroup() + rule.getConsumerSystemName())).forEach((queueKey) -> {
                if (!(DATA.containsKey(queueKey))) {
                    PresentationData data = new PresentationData();
                    DATA.put(queueKey, data);
                    data.getEvents().add(EventUtil.createPresentationEvent(message));
                    Runnable r = () -> {
                        new FTTSE_Presentation(queueKey, data).build();
                    };
                    new Thread(r).start();
                } else {
                    DATA.get(queueKey).getEvents().add(EventUtil.createPresentationEvent(message));
                }
            });
        }
        return EventUtil.createEvent(message);
    }

    @Override
    public SLAVerificationResponse verifyQoS(MonitorRule rule, MonitorLog... logs) {
        int nLogs = logs.length;
        if (nLogs == 0) {
            //FIXME Exception
            return null;
        }

        if (nLogs > 1) {
            return doSoftRealTime(rule.getParameters(), Arrays.asList(logs));
        }

        return doRealTime(rule.getParameters(), logs[0].getParameters());
    }

    private Map<String, String> filterParameters(Map<String, String> params) {
        Map<String, String> parameters = new HashMap<>();

        Monitor[] keys = Monitor.values();

        for (Monitor key : keys) {
            String name = key.name;
            String param = params.get(name);
            if (param != null) {
                try {
                    Double.valueOf(param);
                    parameters.put(name, param);
                } catch (NumberFormatException ex) {
                    throw new InvalidParameterException("Value of parameter "
                            + name + " is not parsable. Please make sure "
                            + "that no invalid characters are present");
                }
            }
        }
        return parameters;
    }

    private SLAVerificationResponse doRealTime(Map<String, String> rule, Map<String, String> log) {
        SLAVerificationResponse response = new SLAVerificationResponse();

        System.out.println("On the SLAVerificationResponse " + log);

        Monitor[] keys = Monitor.values();

        for (Monitor key : keys) {
            //FIXME check if value is in the map. test
            String tempRequested = rule.get(key.name);
            if (tempRequested == null) {
                continue;
            }
            Double requestedValue = Double.valueOf(tempRequested);

            String tempLogged = log.get(key.name);
            if (tempLogged == null) {
                continue;
            }
            Double loggedValue = Double.valueOf(tempLogged);

            switch (key) {
                case BANDWIDTH:
                    if (loggedValue > (requestedValue * 1.1)) {
                        response.addParameter(new SLAVerificationParameter(key.name, requestedValue, loggedValue));
                    }
                    break;
                case DELAY:
                    if (loggedValue > (requestedValue) + 0.15) {
                        response.addParameter(new SLAVerificationParameter(key.name, requestedValue, loggedValue));
                    }
                    break;
                default:
                    break;
            }
        }

        return response;
    }

    private SLAVerificationResponse doSoftRealTime(Map<String, String> rule, List<MonitorLog> logs) {
        SLAVerificationResponse response = new SLAVerificationResponse();

        Double bandwidthMean = 0.0;
        Double delayMean = 0.0;
        Double nLogs = Double.valueOf(logs.size());

        for (MonitorLog log : logs) {
            Map<String, String> temp = log.getParameters();
            bandwidthMean += Double.valueOf(temp.get(Monitor.BANDWIDTH.name));
            delayMean += Double.valueOf(temp.get(Monitor.DELAY.name));
        }

        bandwidthMean /= nLogs;
        delayMean /= nLogs;

        Monitor[] keys = Monitor.values();

        for (Monitor key : keys) {
            Double requestedValue = Double.valueOf(rule.get(key.name));
            switch (key) {
                case BANDWIDTH:
                    if (bandwidthMean > requestedValue) {
                        response.addParameter(new SLAVerificationParameter(key.name, requestedValue, bandwidthMean));
                    }
                    break;
                case DELAY:
                    if (delayMean > requestedValue) {
                        response.addParameter(new SLAVerificationParameter(key.name, requestedValue, delayMean));
                    }
                    break;
                default:
                    break;
            }
        }

        return response;
    }

}
