//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.business.layer;

import com.ford.purchasing.wips.outbound.attachments.WipsConsumerAS;

@SuppressWarnings("javadoc")
public abstract class WipsConsumerBaseBF extends WipsBaseBF {

    private WipsConsumerAS wipsConsumerAS;

    public void setWipsConsumerAS(final WipsConsumerAS wipsConsumerAS) {
        this.wipsConsumerAS = wipsConsumerAS;
    }

    protected WipsConsumerAS getWipsConsumerAS() {
        if (this.wipsConsumerAS == null) {
            this.wipsConsumerAS = new WipsConsumerAS();
        }
        return this.wipsConsumerAS;
    }

}
