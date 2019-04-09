//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

import java.util.HashMap;
import java.util.Map;

public enum Category {

    ATP(WipsConstant.ATP_ENTITY_CODE, "ATPs"),
    LUMPSUM(WipsConstant.LUMPSUM_ENTITY_CODE, "Lumpsums"),
    PRICE_CLAIMS(WipsConstant.PRICE_CLAIMS_ENTITY_CODE, "Price Claims");

    private static Map<String, Category> categoryLookup = createLookup();
    private String categoryName;
    private String categoryCode;

    Category(final String categoryCode, final String categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }

    private static Map<String, Category> createLookup() {
        final Map<String, Category> map = new HashMap<String, Category>();
        map.put(WipsConstant.ATP_ENTITY_CODE, ATP);
        map.put(WipsConstant.LUMPSUM_ENTITY_CODE, LUMPSUM);
        map.put(WipsConstant.PRICE_CLAIMS_ENTITY_CODE, PRICE_CLAIMS);
        return map;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public String getCategoryCode() {
        return this.categoryCode;
    }

    public static Category getCategory(final String approvalCode) {
        return categoryLookup.get(approvalCode);
    }
}
