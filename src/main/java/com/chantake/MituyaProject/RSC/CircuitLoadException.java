/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.MituyaProject.RSC;

/**
 *
 * @author fumitti
 */
public class CircuitLoadException extends Exception {

    public CircuitLoadException() {
    }

    public CircuitLoadException(String message) {
        super(message);
    }

    public CircuitLoadException(Throwable cause) {
        super(cause);
    }

    public CircuitLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}
