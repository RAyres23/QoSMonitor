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
public class MissingParameterException extends RuntimeException {

    
    /**
	 * 
	 */
	private static final long serialVersionUID = 935117653910561573L;

	public MissingParameterException(String message) {
        super(message);
    }

}
