//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import com.ford.purchasing.wips.common.layer.WipsBaseResponse;

public class AtpStrategyResponse extends WipsBaseResponse {

    private StrategyOutput strategyOutput;

    public StrategyOutput getStrategyOutput() {
        return this.strategyOutput;
    }

    public void setStrategyOutput(final StrategyOutput strategyOutput) {
        this.strategyOutput = strategyOutput;
    }

}
