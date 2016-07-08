/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 * @author Renato Ayres
 */
public class MonitorRuleNotFoundExceptionMapper implements ExceptionMapper<MonitorRuleNotFoundException> {

    @Override
    public Response toResponse(MonitorRuleNotFoundException ex) {
        // FIXME Fix documentation
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 400, "No documentation yet.");
        return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
    }

}
