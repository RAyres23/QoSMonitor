package eu.arrowhead.core.qos.monitor;

import eu.arrowhead.common.model.messages.AddMonitorLog;
import eu.arrowhead.common.model.messages.AddMonitorRule;
import eu.arrowhead.common.model.messages.EventMessage;
import eu.arrowhead.common.model.messages.RemoveMonitorRule;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "monitor" path).
 *
 * @author Renato Ayres
 */
@Path("Monitor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QoSMonitorResource {

    private final QoSMonitorService monitor = new QoSMonitorService();
    private static final Logger LOG = Logger.getLogger(QoSMonitorResource.class.getName());

    /**
     * Method handling HTTP GET requests in /online path. The returned object
     * will be sent to the client as "text/plain" media type.
     *
     * Used for testing purposes only.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("/online")
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Online.";
    }

    /**
     * Method handling HTTP GET requests in /online path. The returned object
     * will be sent to the client as "application/json" media type.
     *
     * Used to load all configurations for the QoSMonitor service. Only needed
     * for testing purposes.
     *
     * @return HTTP OK STATUS
     */
    @GET
    @Path("/reload")
    public Response startService() {
        monitor.startService();
        return Response.ok().build();
    }

    /**
     * Method handling HTTP POST request in /qosrule path. The returned object
     * will be sent to the client as "application/json" media type.
     *
     * Used to add new monitor rules to the QoSMonitorService. This rule will be
     * used against AddMonitorLog for SLA verification.
     *
     * @param message message with all the information regarding the monitor
     * rule being added
     * @return Response status and information of the process of adding a
     * monitor rule
     */
    @POST
    @Path("/QoSRule")
    public Response addRule(AddMonitorRule message) {

        try {
            monitor.addRule(message);
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ex.getMessage())
                    .build();
        }

        return Response.ok("OK").build();
    }

    /**
     * Method handling HTTP DELETE request in /qosrule path. The return object
     * will be sent to the client as "application/json" media type.
     *
     * Used to remove monitor rules from the QoSMonitorService.
     *
     * @param message message with all the information regarding the monitor
     * rule being removed
     * @return Response status and information of the process of removing a
     * monitor rule
     */
    @DELETE
    @Path("/QoSRule")
    public Response removeRule(RemoveMonitorRule message) {
        monitor.removeRule(message);
        return Response.ok("OK").build();
    }

    /**
     * Method handling HTTP POST request in /qoslog path. The return object will
     * be sent to the client as "application/json" media type.
     *
     * @param message message with all the information regarding the monitor log
     * being removed
     * @return Response status and information of the process of adding a
     * monitor log
     */
    @POST
    @Path("/QoSLog")
    public Response addLog(AddMonitorLog message) {
        try {
            monitor.addLog(message);
        } catch (InstantiationException | IllegalAccessException ex) {
            // FIXME
            LOG.log(Level.WARNING, ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ex.getMessage())
                    .build();
        }
        // FIXME
        return Response.ok("OK").build();
    }

    @POST
    @Path("/QoSEvent")
    public Response sendEvent(EventMessage error) {
        try {
            monitor.sendEvent(error);
        } catch (InstantiationException | IllegalAccessException ex) {
            // FIXME
            LOG.log(Level.WARNING, ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ex.getMessage())
                    .build();
        }
        return Response.ok("OK").build();
    }
}
