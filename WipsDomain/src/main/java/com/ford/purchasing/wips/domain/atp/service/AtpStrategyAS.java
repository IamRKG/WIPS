//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.service;

import javax.inject.Inject;

import com.ford.purchasing.wips.common.atp.AtpRequest;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.domain.connector.Ims;

public class AtpStrategyAS {

    private final Ims ims;

    @Inject
    private RetrieveAtpStrategy retrieveAtpStrategy;

    @Inject
    public AtpStrategyAS(final Ims ims) {
        this.ims = ims;
    }

    public WipsBaseResponse retrieveStrategy(final AtpRequest atpRequest) {
        this.retrieveAtpStrategy.setAtpRequest(atpRequest);
        return this.ims.converse(atpRequest.getLterm(), this.retrieveAtpStrategy);
    }

}
