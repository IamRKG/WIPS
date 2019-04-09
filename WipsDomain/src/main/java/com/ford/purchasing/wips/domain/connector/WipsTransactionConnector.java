//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.connector;

import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ConnectorFactory;
import com.ford.it.connector.ims.ImsConversation;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.exception.FordExceptionAttributes;
import com.ford.it.properties.PropertyManager;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.domain.layer.exception.WipsImsInterfaceException;

/**
 * This class acts as a base class for all the WIPS Mobile Application IMS Transaction
 * connector class
 */
public abstract class WipsTransactionConnector {

    protected abstract void populateImsTransactionInput(WipsImsInput wipsImsInput,
            DataArea inputDataArea) throws ConnectorException;

    public WipsImsOutput executeWipsImsTransaction(final WipsImsInput wipsImsInput,
            final ImsConversation imsConversation)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final String methodName = "executeWipsImsTransaction";
        WipsImsOutput wipsImsOutput = null;
        try {
            final ImsOperation imsOperation = createImsOperation(getImsTransactionName());
            imsOperation.setConversation(imsConversation);
            imsOperation.overrideCredentials(getWipsProxyId(),
                    getWipsProxyPassword());
            imsOperation.overrideLtermName(wipsImsInput.getLterm());
            final DataArea inputDataArea = (DataArea)imsOperation.getInputRecord();
            populateImsTransactionInput(wipsImsInput, inputDataArea);
            imsOperation.execute();
            wipsImsOutput = populateImsTransactionOutput(imsOperation);
        } catch (final ConnectorException connectorException) {
            throw wipsImsInterfaceException(methodName, connectorException);
        } catch (final ResourceException resourceException) {
            throw wipsImsInterfaceException(methodName, resourceException);
        }
        return wipsImsOutput;
    }

    protected abstract String getImsTransactionName();

    protected abstract WipsImsOutput populateImsTransactionOutput(ImsOperation imsoperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException;

    protected String getWipsProxyId() {
        return PropertyManager.getInstance().getString(WipsConstant.WIPS_PROXY_USERNAME);
    }

    protected String getWipsProxyPassword() {
        return PropertyManager.getInstance().getString(WipsConstant.WIPS_PROXY_PWD);
    }

    public ImsOperation createImsOperation(final String imsTransaction)
            throws ConnectorException {
        return (ImsOperation)ConnectorFactory.getInstance()
                .getOperation(imsTransaction);
    }

    protected WipsImsInterfaceException wipsImsInterfaceException(final String methodName,
            final Exception exception) {
        return new WipsImsInterfaceException(new FordExceptionAttributes.Builder(
                this.getClass().getName(), methodName).build(), exception.getMessage(),
                exception);
    }

}
