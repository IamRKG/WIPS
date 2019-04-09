//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("javadoc")
public enum LumpSumType {

    UNLINKED_SETTLEMENT(WipsConstant.UNLINKED_SETTLEMENT_LS), FORECAST(WipsConstant.FORECAST_LS), UNLINKED_RESERVE(
            WipsConstant.UNLINKED_RESERVE_LS), NTE(WipsConstant.NTE_LS), RESERVE_NTE(
                    WipsConstant.RESERVE_NTE_LS), SETTLEMENT_NTE(WipsConstant.SETTLEMENT_NTE_LS), SETTLEMENT_RESERVE(
                            WipsConstant.SETTLEMENT_RESERVE_LS), SETTLEMENT_RESERVE_NTE(
                                    WipsConstant.SETTLEMENT_RESERVE_NTE_LS);

    private static Map<String, LumpSumType> lsTypeLookup = createLookup();
    private String typeCode;

    private LumpSumType(final String typeCode) {
        this.typeCode = typeCode;
    }

    public String getLumpSumTypeCode() {
        return this.typeCode;
    }

    private static Map<String, LumpSumType> createLookup() {
        final Map<String, LumpSumType> lookUpMap = new HashMap<String, LumpSumType>();
        lookUpMap.put(WipsConstant.UNLINKED_SETTLEMENT_LS, UNLINKED_SETTLEMENT);
        lookUpMap.put(WipsConstant.FORECAST_LS, FORECAST);
        lookUpMap.put(WipsConstant.UNLINKED_RESERVE_LS, UNLINKED_RESERVE);
        lookUpMap.put(WipsConstant.NTE_LS, NTE);
        lookUpMap.put(WipsConstant.RESERVE_NTE_LS, RESERVE_NTE);
        lookUpMap.put(WipsConstant.SETTLEMENT_NTE_LS, SETTLEMENT_NTE);
        lookUpMap.put(WipsConstant.SETTLEMENT_RESERVE_LS, SETTLEMENT_RESERVE);
        lookUpMap.put(WipsConstant.SETTLEMENT_RESERVE_NTE_LS, SETTLEMENT_RESERVE_NTE);
        return lookUpMap;
    }

    public static LumpSumType getLumpSumType(final String lumpSumType, final String linkedType) {
        String lookUpKey = lumpSumType;
        if (linkedType != null) {
            lookUpKey = lumpSumType + "_" + linkedType;
        }
        return lsTypeLookup.get(lookUpKey);
    }

    public static LumpSumType getLumpSumType(final String lumpsumType) {
        return getLumpSumType(lumpsumType, null);
    }
}
