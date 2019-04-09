//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

/**
 * TODO - Place class description here
 */
public class TextMessageOptinRequest extends WipsBaseRequest {

    private String selectedOption;

    /**
     * @return Returns the selectedOption.
     */
    public String getSelectedOption() {
        return this.selectedOption;
    }

    /**
     * @param selectedOption The selectedOption to set.
     */
    public void setSelectedOption(final String selectedOption) {

        this.selectedOption = selectedOption;
    }

}
