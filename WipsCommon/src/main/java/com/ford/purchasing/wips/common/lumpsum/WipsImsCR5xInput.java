package com.ford.purchasing.wips.common.lumpsum;

import java.util.ArrayList;
import java.util.List;

import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.layer.WipsImsInput;

public class WipsImsCR5xInput extends WipsImsInput {

    private List<String> lumpsumRemarks = new ArrayList<String>();
    private List<DataArea> bufferedSegLoop;
    private String actionTaken;
    private List<String> remarksText;

    public List<String> getRemarksText() {
        return this.remarksText;
    }

    public void setRemarksText(final List<String> remarksText) {
        this.remarksText = remarksText;
    }

    public String getActionTaken() {
        return this.actionTaken;
    }

    public void setActionTaken(final String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public List<String> getLumpsumRemarks() {
        return this.lumpsumRemarks;
    }

    public void setLumpsumRemarks(final List<String> lumpsumRemarks) {
        this.lumpsumRemarks = lumpsumRemarks;
    }

    public List<DataArea> getBufferedSegLoop() {
        return this.bufferedSegLoop;
    }

    public void setBufferedSegLoop(final List<DataArea> bufferedSegLoop) {
        this.bufferedSegLoop = bufferedSegLoop;
    }

}
