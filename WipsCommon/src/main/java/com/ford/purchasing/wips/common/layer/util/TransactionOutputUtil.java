//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.Array;
import com.ford.it.connector.record.DataArea;
import com.ford.it.connector.record.Parameter;

public class TransactionOutputUtil {

    private TransactionOutputUtil() {

    }

    public static final String getString(final DataArea dataArea, final String fieldName) throws ConnectorException {
        return StringUtils.trim(getStringWithoutTrimming(dataArea, fieldName));
    }

    public static final List<DataArea> getArray(final DataArea dataArea, final String fieldName)
            throws ConnectorException {
        return ((Array) getField(dataArea, fieldName)).getDataAreas();
    }

    public static String getStringWithoutTrimming(final DataArea dataArea, final String fieldName)
            throws ConnectorException {
        return ((Parameter) getField(dataArea, fieldName)).getValueString();
    }

    private static Object getField(final DataArea dataArea, final String fieldName) {
        return dataArea.get(fieldName);
    }

    public static String getStringWithUnderScoreTrimmed(final DataArea dataArea, final String fieldName)
            throws ConnectorException {
        String fieldValue = getString(dataArea, fieldName);
        if (fieldValue != null) {
            fieldValue = fieldValue.replaceAll("_", "");
        }
        return fieldValue;
    }

    public static List<DataArea> getSegmentLoop(final DataArea outputDataArea) throws ConnectorException {
        return TransactionOutputUtil.getArray(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-BUFFER-SEGLOOP");
    }

    public static List<String> getListOfValues(final DataArea outputDataArea, final String descriptionFieldName)
            throws ConnectorException {
        final List<DataArea> outputDetailList = getSegmentLoop(outputDataArea);
        final List<String> fullDescription = new ArrayList<String>();
        for (final Iterator<DataArea> i = outputDetailList.iterator(); i.hasNext();) {
            final DataArea dataAreaForRemarks = i.next();
            String singleLineDesc = getStringWithoutTrimming(dataAreaForRemarks, descriptionFieldName);
            if (singleLineDesc != null) {
                singleLineDesc = singleLineDesc.replace(" ", "\u00A0");
                singleLineDesc = singleLineDesc.replace("_", "");
                if (!"".equalsIgnoreCase(singleLineDesc)) {
                    fullDescription.add(singleLineDesc);
                }
            }
        }
        return fullDescription;
    }

    public static List<String> getListOfValuesWithoutSpaces(final DataArea outputDataArea,
            final String descriptionFieldName) throws ConnectorException {
        final List<DataArea> outputDetailList = getSegmentLoop(outputDataArea);
        final List<String> fullDescription = new ArrayList<String>();
        for (final Iterator<DataArea> i = outputDetailList.iterator(); i.hasNext();) {
            final DataArea dataAreaForRemarks = i.next();
            String singleLineDesc = getStringWithoutTrimming(dataAreaForRemarks, descriptionFieldName);
            if (singleLineDesc != null) {
                singleLineDesc = singleLineDesc.replace("_", "");
                if (!"".equalsIgnoreCase(singleLineDesc) && (singleLineDesc.trim().length() > 0)) {
                    fullDescription.add(singleLineDesc);
                }
            }
        }
        return fullDescription;
    }
}
