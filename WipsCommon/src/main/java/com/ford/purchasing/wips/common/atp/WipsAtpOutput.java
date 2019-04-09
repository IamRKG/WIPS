package com.ford.purchasing.wips.common.atp;

import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;

@SuppressWarnings("javadoc")
public class WipsAtpOutput extends WipsPendingApprovalOutput {

    private String partNumber;

    public String getPartNumber() {
        return this.partNumber;
    }

    public void setPartNumber(final String partNumber) {
        this.partNumber = partNumber;
    }

}
