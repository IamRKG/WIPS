//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp;

import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.domain.atp.beans.G53xTransactionOutput;

@SuppressWarnings("javadoc")
public class AtpRecapResponse extends WipsBaseResponse {
    private G53xTransactionOutput g53xTransactionOutput;
    private boolean attachmentsErrorFlag;
    private String attachmentsErrorMessage;

    public G53xTransactionOutput getG53xTransactionOutput() {
        return this.g53xTransactionOutput;
    }

    public void setG53xTransactionOutput(final G53xTransactionOutput g53xTransactionOutput) {
        this.g53xTransactionOutput = g53xTransactionOutput;
    }

    public boolean isAttachmentsErrorFlag() {
        return this.attachmentsErrorFlag;
    }

    public void setAttachmentsErrorFlag(final boolean attachmentsErrorFlag) {
        this.attachmentsErrorFlag = attachmentsErrorFlag;
    }

    public String getAttachmentsErrorMessage() {
        return this.attachmentsErrorMessage;
    }

    public void setAttachmentsErrorMessage(final String attachmentsErrorMessage) {
        this.attachmentsErrorMessage = attachmentsErrorMessage;
    }

}
