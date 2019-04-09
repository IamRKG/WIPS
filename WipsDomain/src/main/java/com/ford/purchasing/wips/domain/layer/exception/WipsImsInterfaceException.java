//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.layer.exception;

import com.ford.it.exception.FordExceptionAttributes;
import com.ford.it.exception.FordSelfLoggingException;

/**
 * Custom exception class for IMS Interface related runtime exceptions.
 */
public class WipsImsInterfaceException extends FordSelfLoggingException {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct the new exception with the specified detail message and
     * immutable attributes.
     *
     * @param fordExceptionAttributes
     * @param message
     */
    public WipsImsInterfaceException(
            final FordExceptionAttributes fordExceptionAttributes,
            final String message) {
        super(fordExceptionAttributes, message);
    }

    /**
     * Construct a WipsImsInterfaceException instance
     *
     * @param fordExceptionAttributes
     * @param message
     * @param cause
     */
    public WipsImsInterfaceException(final FordExceptionAttributes fordExceptionAttributes,
            final String message, final Throwable cause) {
        super(fordExceptionAttributes, message, cause);
    }

}
