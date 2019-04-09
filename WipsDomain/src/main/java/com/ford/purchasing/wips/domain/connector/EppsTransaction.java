package com.ford.purchasing.wips.domain.connector;

import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsConversation;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;
import com.ford.purchasing.wips.domain.layer.exception.WipsImsInterfaceException;

/**
 * This connector class makes an IMS invocation to communicate with WIPS Mainframe system to
 * authenticate RACF ID & Password credentials of the User who tries to login.
 */
public class EppsTransaction extends WipsTransactionConnector {

    private static final String EPPS_TRANSACTION = "AAIMEPPS";
    private static final String CLASS_NAME = EppsTransaction.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) {
        final EppsTransactionInput eppsInput = (EppsTransactionInput)wipsImsInput;
        inputDataArea.put("TP-INPUT-FIELDS.TPI-IOINDIC", eppsInput.getTpiIoindic());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PFKEY", eppsInput.getTpiPfKey());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-RACF-ID", eppsInput.getUserRacfId());
    }

    @Override
    public WipsImsOutput executeWipsImsTransaction(final WipsImsInput wipsImsInput,
            final ImsConversation imsConversation)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final String methodName = "executeWipsImsTransaction";
        final EppsTransactionInput wipsImsEppsInput = (EppsTransactionInput)wipsImsInput;
        M00mTransactionOutput m00mTransactionOutputBean = null;
        try {
            final ImsOperation imsOperation = createImsOperation(getImsTransactionName());
            imsOperation.setConversation(imsConversation);
            if (WipsConstant.WIPS_LOGIN_ACTION
                    .equalsIgnoreCase(wipsImsEppsInput.getImsAction())) {
                imsOperation.overrideCredentials(wipsImsEppsInput.getUserRacfId(),
                        wipsImsEppsInput.getUserLoginPassword());
            } else {
                imsOperation.overrideCredentials(getWipsProxyId(), getWipsProxyPassword());
                imsOperation.overrideLtermName(wipsImsEppsInput.getLterm());
            }
            final DataArea inputDataArea = (DataArea)imsOperation.getInputRecord();
            populateImsTransactionInput(wipsImsEppsInput, inputDataArea);
            imsOperation.execute();
            m00mTransactionOutputBean =
                    (M00mTransactionOutput)populateImsTransactionOutput(imsOperation);
        } catch (final ConnectorException connectorException) {
            throw wipsImsInterfaceException(methodName, connectorException);
        } catch (final ResourceException resourceException) {
            throw wipsImsInterfaceException(methodName, resourceException);
        }
        return m00mTransactionOutputBean;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        final String rawOutputEPPS = imsOperation.getRawOutput();
        log.info("Epps output" + rawOutputEPPS);
        final DataArea outputDataArea = (DataArea)imsOperation.getOutputRecord();
        final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM");
        if (!WipsTransactionConstant.M00M_TRANSACTION_NAME.equals(transactionName)) {
            throw new WipsImsTransactionException(
                    EPPS_TRANSACTION, rawOutputEPPS.substring(38, 113));
        }
        return new WipsTransactionHelper().populateM00mOutput(outputDataArea);
    }

    @Override
    protected String getImsTransactionName() {
        return EPPS_TRANSACTION;
    }
}
