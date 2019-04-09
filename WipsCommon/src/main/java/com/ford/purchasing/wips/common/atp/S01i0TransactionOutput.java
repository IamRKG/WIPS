//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import java.util.ArrayList;
import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;

@SuppressWarnings("javadoc")
public class S01i0TransactionOutput extends WipsImsOutput {
    private List<String> confirmMessage;
    private String actionTaken;

    public String getActionTaken() {
        return this.actionTaken;
    }

    public void setActionTaken(final String actionTaken) {

        this.actionTaken = actionTaken;
    }

    public List<String> getConfirmMessage() {
        return this.confirmMessage;
    }

    public void setConfirmMessage(final List<String> confirmMessage) {

        this.confirmMessage = confirmMessage;
    }

    public S01i0TransactionOutput from(final String rawOutput) throws ConnectorException {
        final S01i0TransactionOutput output = new S01i0TransactionOutput();
        if (rawOutput.length() != 121) {
            output.setErrorFlag(true);
            final String errorMessage = rawOutput.substring(62, 141);
            output.setErrorMessage(errorMessage);
        }
        return output;
    }

    public BiFunctionThrowsException<DataArea, String, S01i0TransactionOutput, Exception> getS01i0Output() {
        return new BiFunctionThrowsException<DataArea, String, S01i0TransactionOutput, Exception>() {

            @Override
            public S01i0TransactionOutput apply(final DataArea outputDataArea, final String rawOutput)
                    throws Exception {
                return from(rawOutput);
            }

        };
    }

    public BiFunctionThrowsException<DataArea, String, S01i0TransactionOutput, Exception> getS01i0ApproveOrRejectOutput(
        final AtpApproveOrRejectResponse atpApproveOrRejectResponse, final List<ApproveWarningHandler> warningMessages) {
        return new BiFunctionThrowsException<DataArea, String, S01i0TransactionOutput, Exception>() {

            @Override
            public S01i0TransactionOutput apply(final DataArea outputDataArea, final String rawOutput)
                    throws Exception {
                final S01i0TransactionOutput transactionOutput = new S01i0TransactionOutput();
                if (rawOutput.length() != 121) {
                    final List<String> message = new ArrayList<String>();
                    final String errorMessage = rawOutput.substring(94, 170);
                    final ApproveWarningHandler handler = new ApproveWarningHandler();
                    message.add(errorMessage);
                    transactionOutput.setConfirmMessage(message);
                    transactionOutput.setErrorMessage(errorMessage);
                    transactionOutput.setActionTaken(actionTaken(errorMessage));
                    handler.setErrorMessage(errorMessage);
                    handler.setMessageCode(errorMessage.trim().substring(0, 8));
                    handler.setActionTaken(actionTaken(errorMessage));
                    warningMessages.add(handler);
                    atpApproveOrRejectResponse.setWarningMessagesList(warningMessages);
                } else {
                    transactionOutput.setErrorFlag(false);
                }
                return transactionOutput;
            }
        };
    }

    protected String actionTaken(final String errorMessage) {
        String actionTakenConfirmReject = null;
        if (confirmAction(errorMessage)) {
            actionTakenConfirmReject = WipsConstant.CONFIRM;
        } else if (rejectAction(errorMessage)) {
            actionTakenConfirmReject = WipsConstant.REJECT_ATP;
        }
        return actionTakenConfirmReject;
    }

    private boolean rejectAction(final String errorMessage) {
        return errorMessage.contains("MSG-4499") || errorMessage.contains("MSG-4628");
    }

    private boolean confirmAction(final String errorMessage) {
        return errorMessage.contains("MSG-4276") || errorMessage.contains("MSG-4236")
                || errorMessage.contains("MSG-4524");
    }

}
