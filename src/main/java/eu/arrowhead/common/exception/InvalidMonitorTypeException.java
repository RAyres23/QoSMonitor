/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.common.exception;

/**
 *
 * @author Renato Ayres
 */
public class InvalidMonitorTypeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -4358132536096296490L;

    public InvalidMonitorTypeException(String message) {
        super(message);
    }

}
