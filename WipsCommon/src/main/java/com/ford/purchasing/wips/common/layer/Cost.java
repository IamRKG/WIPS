package com.ford.purchasing.wips.common.layer;

public class Cost {

    private final String amount;
    private final String currency;
    private final String sign;

    public Cost(final String amount, final String currency, final String sign) {
        this.amount = amount;
        this.currency = currency;
        this.sign = sign;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getSign() {
        return this.sign;
    }

}