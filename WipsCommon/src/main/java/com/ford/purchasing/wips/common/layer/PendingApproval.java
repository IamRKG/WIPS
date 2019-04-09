//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

public class PendingApproval {

    private String totalCount;

    private Category category;

    public String getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(final String totalCount) {
        this.totalCount = totalCount;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public String getCategoryCode() {
        return this.category.getCategoryCode();
    }

    public String getCategoryName() {
        return this.category.getCategoryName();
    }
}
