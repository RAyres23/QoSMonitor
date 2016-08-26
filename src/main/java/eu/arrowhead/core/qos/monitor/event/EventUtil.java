/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.event;

import com.google.gson.Gson;
import eu.arrowhead.common.model.messages.ServiceError;
import eu.arrowhead.core.qos.monitor.event.model.Event;
import eu.arrowhead.core.qos.monitor.event.model.Metadata;
import eu.arrowhead.core.qos.monitor.type.presentation.model.PresentationEvent;

/**
 *
 * @author ID0084D
 */
public final class EventUtil {

    /**
     * Creates a new Event from given ServiceError parameter
     *
     * @param error
     * @return
     */
    public static final Event createEvent(ServiceError error) {
        Event event = new Event();
        Metadata meta = new Metadata();
        meta.setSeverity(1);
        event.setDescription(meta);
        event.setFrom(EventProducer.getProducer());
        event.setType("event");

        event.setPayload(new Gson().toJson(error.getErrorMessage()));

        return event;
    }

    /**
     * Creates a new PresentationEvent from given ServiceError parameter
     *
     * @param error
     * @return
     */
    public static final PresentationEvent createPresentationEvent(ServiceError error) {
        String from = error.getSystem().getSystemGroup() + error.getSystem().getSystemName();

        PresentationEvent event = new PresentationEvent(from, "error", 1, error.getErrorMessage());

        return event;
    }

}
