//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.layer.exception;

import com.ford.it.exception.FordExceptionAttributes;
import com.ford.it.exception.FordSelfLoggingException;

/**
 * Custom exception class for DB2 database interface related runtime exceptions.
 */
public class WipsDb2DatabaseException extends FordSelfLoggingException {

    /**
     * UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct the new exception with the specified detail message and
     * immutable attributes.
     *
     * @param fordExceptionAttributes
     * @param message
     */
    public WipsDb2DatabaseException(
            final FordExceptionAttributes fordExceptionAttributes,
            final String message) {
        super(fordExceptionAttributes, message);
    }

    /**
     * Construct the new exception with the specified detail message, immutable
     * attributes and cause.
     *
     * @param fordExceptionAttributes Instance of immutable attributes
     * @param message Detail Logging message
     * @param cause Nested root cause exception
     */
    public WipsDb2DatabaseException(
            final FordExceptionAttributes fordExceptionAttributes,
            final String message, final Throwable cause) {
        super(fordExceptionAttributes, message, cause);
    }

}
