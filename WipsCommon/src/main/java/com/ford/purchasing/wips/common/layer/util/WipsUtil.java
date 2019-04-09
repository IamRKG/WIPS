//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer.util;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.Level;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.atp.ApproveWarningHandler;
import com.ford.purchasing.wips.common.atp.S01i0TransactionOutput;
import com.ford.purchasing.wips.common.layer.WipsConstant;

/**
 * This class contains the utility function for WIPS application.
 */
public class WipsUtil {

    private static final String SQL_PLACE_HOLDER_1 = "#@$%!";
    private static final String SQL_PLACE_HOLDER_2 = "!%$@#";
    private static final String CLASS_NAME = WipsUtil.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    private WipsUtil() {
    }

    /**
     * This method returns the Formatted current date for the given pattern.
     */
    public static String getFormattedCurrentDate(final String pattern) {
        final Date date = new Date();
        final DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * This method returns the Default Formatted current date in the pattern
     * yyyyMMdd.
     */
    public static String getDefaultFormattedCurrentDate() {
        return getFormattedCurrentDate(WipsConstant.YYYY_MM_DD);
    }

    /**
     * Return the formatted Date string object.
     */
    public static String convertDateStringToFormattedDateString(final String dateInput) {
        if (dateInput != null && !"".equals(dateInput)) {
            final String formattedDate = dateInput.substring(2, 4) + "/" + dateInput.substring(4, 6) + "/"
                    + dateInput.substring(0, 2);
            final SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yy");
            final SimpleDateFormat format2 = new SimpleDateFormat("MMM-dd-yyyy");
            Date dateObject = null;
            try {
                dateObject = format1.parse(formattedDate);
            } catch (final ParseException exception) {
                log.log(Level.WARNING, "dateFormatter", exception.getMessage());
            }
            return format2.format(dateObject);
        }
        return "";
    }

    /**
     * Returns key value Map.
     */
    public static Map<String, List<String>> getMapFromList(final List<Map<String, List<String>>> list) {

        final Map<String, List<String>> tempMap = new HashMap<String, List<String>>();

        for (final Map<String, List<String>> i : list) {
            for (final Map.Entry<String, List<String>> entry : i.entrySet()) {
                tempMap.put(entry.getKey(), entry.getValue());
            }
        }
        return tempMap;
    }

    public static boolean isExceptionContainingMessageCode(final String errorMessage, final List<String> list) {
        for (final String messageCode : list) {
            if (errorMessage.contains(messageCode))
                return true;
        }
        return false;
    }

    /**
     * Populates the place holders dynamically in the SQL Query based on the
     * size.
     */
    public static String populatePlaceholdersInSql(final int count, final String sqlQuery) {
        return sqlQuery.replace(SQL_PLACE_HOLDER_1, sqlPlaceHolder(count));
    }

    public static String encrpyt(final String lterm) {
        final String methodName = "encrpyt";
        String encodedLterm = "";
        try {
            encodedLterm = Base64.encodeBase64URLSafeString(CryptUtil.encrypt(lterm).getBytes("UTF-8"));
        } catch (final UnsupportedEncodingException exception) {
            log.throwing(CLASS_NAME, methodName, exception);
            encodedLterm = lterm;
        }
        return encodedLterm;
    }

    public static String decrpyt(final String ltermToken) {
        final String methodName = "decrpyt";
        String lterm = null;
        try {
            lterm = CryptUtil.decrypt(new String(Base64.decodeBase64(ltermToken.getBytes("UTF-8"))));
        } catch (final UnsupportedEncodingException exception) {
            log.throwing(CLASS_NAME, methodName, exception);
            lterm = ltermToken;
        }
        return lterm;
    }

    public static ApproveWarningHandler populateWarningMessages(final S01i0TransactionOutput output) {
        final String errorMessage = output.getErrorMessage();
        final String actionTaken = output.getActionTaken();
        return populateWarningMessages(errorMessage, actionTaken);
    }

    public static ApproveWarningHandler populateWarningMessages(final String errorMessage, final String actionTaken) {
        final ApproveWarningHandler handler = new ApproveWarningHandler();
        handler.setErrorMessage(errorMessage);
        handler.setMessageCode(errorMessage.trim().substring(0, 8));
        handler.setActionTaken(actionTaken);
        return handler;
    }

    public static String populatePlaceholdersInSql(final int firstHolder, final int secondHolder,
            final String sqlQuery) {
        final String intermediateSql = sqlQuery.replace(SQL_PLACE_HOLDER_2, sqlPlaceHolder(firstHolder).toString());
        return intermediateSql.replace(SQL_PLACE_HOLDER_1, sqlPlaceHolder(secondHolder).toString());
    }

    private static StringBuilder sqlPlaceHolder(final int secondHolder) {
        final StringBuilder inPlaceHolders = new StringBuilder();
        for (int i = 0; i < secondHolder; i++) {
            if (i != (secondHolder - 1)) {
                inPlaceHolders.append("?,");
            } else {
                inPlaceHolders.append("?");
            }
        }
        return inPlaceHolders;
    }

}
