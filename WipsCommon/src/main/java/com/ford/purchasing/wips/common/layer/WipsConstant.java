// ******************************************************************************
// * Copyright (c) 2015 Ford Motor Company. All Rights Reserved.
// * Original author: KJAGADE4
// *
// * $Workfile:   WipsConstant.java  $
// * $Revision:   1.1  $
// * $Author:   kjagade4 $
// * $Date:   Nov 11 2015 23:43:56  $
// *
// ******************************************************************************
package com.ford.purchasing.wips.common.layer;

import java.math.BigInteger;

import com.ford.purchasing.wips.common.layer.util.StringUtil;

/**
 * This class contains constants used by WIPS Application.
 */
@SuppressWarnings("javadoc")
public class WipsConstant {

    public static final String YES = "Yes";

    public static final String DEFAULT_REQUESTED_AMENDMENT = "00";

    public static final String INITIAL_AMENDMENT = "01";

    public static final String LUMPSUM_ENTITY_CODE = "102";

    public static final String ATP_ENTITY_CODE = "25";

    public static final String PRICE_CLAIMS_ENTITY_CODE = "3";

    public static final String ZERO_COUNT = "0";

    public static final String DEFAULT_ATP_VERSION = "0";

    public static final String WIPS_DATABASE = "WIPSDatabase";

    public static final String WSL_PERSISTENT_COOKIES_CONFIG = "WSLPersistentCookiesConfig";

    public static final String WSC_CONFIG = "WscConfig";

    public static final String WIPS_IMS_CONNECTOR_CONFIG = "ConnectorConfig";

    public static final String SECURITY_FAILURE = "Security failure. RACF call failed";

    public static final BigInteger WIPS_APPLICATION_ID = BigInteger.valueOf(858);

    public static final String WIPS_ENTITY_NAME = "WIPS_ATTACHMENT";

    public static final String WIPS_LOGIN_ACTION = "Login";

    public static final String InputO = "O";

    public static final short PFKEY0 = 0;

    public static final String END_CONVERSATION_ERROR =
            "[SYNC_END_CONVERSATION] is not allowed for the current state [SEND_STATE]";

    public static final String WIPS_MOBILE = "WIPSMobile";

    public static final String WIPS_PROXY_USERNAME = "WIPSMobile.proxyConnection.userName";

    public static final String WIPS_PROXY_PWD = "WIPSMobile.proxyConnection.password";

    public static final String WIPS_SESSION_TIMEOUT =
            "WIPSMobile.SessionMaintenance.SessionTimeOut";

    public static final String WIPS_DB2_JNDI_NAME = "WIPSDatabase.DB2.Credentials.JNDIName";

    public static final String BLANK_SPACE_3 = StringUtil.createBlankSpaces(3);

    public static final String PAGENUMBERONE = "01";

    public static final String InputI = "I";

    public static final short PFKEY8 = 8;

    public static final short PFKEY7 = 7;

    public static final String SELCAT = "AWTG RECAP__";

    public static final String SelectLumpSum = "LS APPROVAL_";

    public static final short PFKEY13 = 13;

    public static final short PFKEY18 = 18;

    public static final short PFKEY17 = 17;

    public static final short PFKEY20 = 20;

    public static final short PFKEY23 = 23;

    public static final short PFKEY9 = 9;

    public static final String BLANK_SPACE_1 = StringUtil.createBlankSpaces(1);

    public static final String BLANK_SPACE_56 = StringUtil.createBlankSpaces(56);

    public static final String BLANK_SPACE_2 = StringUtil.createBlankSpaces(2);

    public static final String BLANK_SPACE_5 = StringUtil.createBlankSpaces(5);

    public static final String BLANK_SPACE_6 = StringUtil.createBlankSpaces(6);

    public static final String BLANK_SPACE_8 = StringUtil.createBlankSpaces(8);

    public static final String BLANK_SPACE_4 = StringUtil.createBlankSpaces(4);

    public static final String BLANK_SPACE_14 = StringUtil.createBlankSpaces(14);

    public static final short PFKEY14 = 14;

    public static final short PFKEY21 = 21;

    public static final short PFKEY00 = 0;

    public static final String CONFIRMLS_PAYMENT_DESC = "confirmLSPaymentDesc";

    public static final short CR5X_RAWOUTPUT_LENGTH = 1298;

    public static final String MORE_PAGES = "***MORE";

    public static final short PFKEY16 = 16;
    public static final String SAVE_REMARKS = "SaveRemarks";
    public static final String SAVE_LS_REMARKS = "SaveLSRemarks";
    public static final String CLEAR_REMARKS = "ClearRemarks";

    public static final String RETRIEVE_REMARKS = "RetrieveRemarks";

    public static final String SAVE_REMARKS_NEXT_PAGE = "SaveRemarksNextPage";

    public static final String CLIENT_VALIDATION_INVALID_DATA =
            "Client.Validation.InvalidData";

    public static final String CLIENT_VALIDATION_DOCUMENT_NOT_FOUND =
            "Client.Validation.DocumentNotFound";

    public static final String LUMPSUM_NUMBER_AND_VERSION_REQUIRED =
            "Both LumpSum Document Number and amendment version are required";

    public static final String DOCUMENT_CURRENTLY_UNAVAILABLE_IN_WEB_QUOTE =
            "This LumpSum is currently unavailable in WebQuote.";

    public static final String UNABLE_TO_RETRIEVE_ATTACHMENTS =
            "Unable to retrieve available attachments from WebQuote";

    public static final String ATP_NUMBER_REQUIRED =
        "ATP Number is required";

    public static final String UNLINKED_SETTLEMENT_LS = "S";

    public static final String FORECAST_LS = "F";

    public static final String NTE_LS = "N";

    public static final String UNLINKED_RESERVE_LS = "R";

    public static final String RESERVE_NTE_LS = "R_N";

    public static final String SETTLEMENT_NTE_LS = "S_N";

    public static final String SETTLEMENT_RESERVE_LS = "S_R";

    public static final String SETTLEMENT_RESERVE_NTE_LS = "S_B";

    public static final String PREV_NTE_BAL = "Prev NTE Bal  :";

    public static final String NEW_NTE_BAL = "New  NTE Bal  :";

    public static final String PREV_RSV_BAL = "Prev Rsv Bal  :";

    public static final String NEW_RSV_BAL = "New  Rsv Bal  :";

    public static final String FINANCE_ANALYST_CODE = "FP";

    public static final String FINANCE_LL6_CODE = "F6";

    public static final String GLOBAL_SUPPLIER_AUDIT_CODE = "GS";

    public static final String PAYMENT_TYPE_PREPAID = "Y";

    public static final String PAYMENT_TYPE_NON_PREPAID = "N";

    public static final short PFKEY1 = 1;

    public static final String REJECT = "Reject";

    public static final String APPROVE = "Approve";

    public static final String CONFIRM = "Confirm";
    public static final short PFKEY3 = 3;
    public static final String REJECT_ATP = "Reject ATP";
    private static final String[] C32U_MESSAGES =
            {"MSG-1411", "MSG-2659", "MSG-5475", "MSG-5483", "MSG-5684"};

    private static final String[] C33U_MESSAGES =
            {"MSG-1411", "MSG-2659", "MSG-5521", "MSG-5523", "MSG-5575", "MSG-5581"};
    private static final String[] CDS_LOOKUP_ATTRIBUTES = {"fordPagerPin"};
    public static final short PFKEY11 = 11;

    public static final String YYYY_MM_DD = "yyyyMMdd";

    private static final String[] NO_WORKLIST_FOUND_CODE = {"MSG-3204", "MSG-2968"};

    public static final CharSequence LUMPSUM_CURRENTLY_IN_USE = "MSG-2659";

    public static final CharSequence LUMPSUM_ERROR_1283 = "MSG-1283";

    public static final String MANY = "MANY";

    public static final short PFKEY19 = 19;

    public static final String STRING_HYPEN = "-";

    public static final CharSequence MORE = "MORE";

    public static final short PFKEY15 = 15;

    public static final short PFKEY22 = 22;

    public static final CharSequence CLAIM_CURRENTLY_IN_USE = "MSG-1370";

    public static final CharSequence MORE_THAN_18_MONTHS = "MSG-2676";

    public static final CharSequence NO_PRICE_CLAIMS = "MSG-3544";

    public static final String SAVE = "Save";

    public static final Object BLANK_SPACE_13 = StringUtil.createBlankSpaces(13);;

    public static final String LDAP_SEARCH_PREFIX = "(uid=";

    public static final String CLOSING_BRACKET = ")";

    public static final String STAR_ALL = "*All";

    public static final String ALL = "All";

    public static final String STAR_ALL_UNDERSCORE = "*All_";

    public static String[] getNoWorklistFoundCode() {
        return NO_WORKLIST_FOUND_CODE;
    }

    public static String[] getC32uErrorMessages() {
        return C32U_MESSAGES;
    }

    public static String[] getC33uErrorMessages() {
        return C33U_MESSAGES;
    }

    public static String[] getCdsLookupAttributes() {
        return CDS_LOOKUP_ATTRIBUTES;
    }

    private WipsConstant() {
    }
}
