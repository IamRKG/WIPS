// ******************************************************************************
// * Copyright (c) 2010 Ford Motor Company. All Rights Reserved.
// * Original author: Ford Motor Company J2EE Center of Excellence
// *
// * $Workfile:   WipsBaseRuntimeException.java  $
// * $Revision:   1.1  $
// * $Author:   mtoll2  $
// * $Date:   Jun 09 2010 12:03:34  $
// *
// ******************************************************************************

package com.ford.purchasing.wips.common.layer.exception;

import com.ford.it.exception.FordExceptionAttributes;
import com.ford.it.exception.FordSelfLoggingRuntimeException;

/**
 * This base class supports runtime exceptions.
 * 
 * @since v. 1.0
 */
public abstract class WipsBaseRuntimeException
        extends FordSelfLoggingRuntimeException {

    /** UID */
    private static final long serialVersionUID = 1L;

    /**
     * Construct the new exception with the specified detail message and
     * immutable attributes.
     * 
     * @param fordExceptionAttributes Instance of immutable attributes
     * @param message Detail Logging message
     */
    public WipsBaseRuntimeException(
            final FordExceptionAttributes fordExceptionAttributes,
            final String message) {

        super(fordExceptionAttributes, message);
    }

    /**
     * 
     * Construct the new exception with the specified detail message, immutable
     * attributes and cause.
     * 
     * @param fordExceptionAttributes Instance of immutable attributes
     * @param message Detail Logging message
     * @param cause Nested root cause exception
     */
    public WipsBaseRuntimeException(
            final FordExceptionAttributes fordExceptionAttributes,
            final String message, final Throwable cause) {

        super(fordExceptionAttributes, message, cause);
    }
}
