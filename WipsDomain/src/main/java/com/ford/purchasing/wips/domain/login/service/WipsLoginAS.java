//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.login.service;

import javax.inject.Inject;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.Level;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.layer.JobDetail;
import com.ford.purchasing.wips.common.layer.LoginRequest;
import com.ford.purchasing.wips.common.layer.PendingApprovalResponse;
import com.ford.purchasing.wips.common.layer.UserProfile;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.dao.db2.JobDetailDAO;
import com.ford.purchasing.wips.domain.layer.WipsBaseAS;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.layer.exception.WipsImsInterfaceException;

/**
 * Process the User Login request
 */
public class WipsLoginAS extends WipsBaseAS {

    private static final String CLASS_NAME = WipsLoginAS.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    private static final String INVALID_RACF_CREDENTIALS = "Invalid RACF credentials";
    private static final String ERROR_INVALID_RACF_CREDENTIALS = "Error. Invalid RACF credentials / RACF ID IS NOT AUTHORIZED FOR WIPS";

    @Inject
    private JobDetailDAO jobDetail;

    /**
     * Process the User Login request to validate the login credentials with the
     * Mainframe system.
     */
    public PendingApprovalResponse processUserLoginRequest(final LoginRequest loginRequest) {
        final String methodName = "processUserLoginRequest";
        log.entering(CLASS_NAME, methodName);
        PendingApprovalResponse pendingApproval = new PendingApprovalResponse();
        try {
            this.imsConversation = startImsConversation();
            final EppsTransactionInput wipsImsEppsInput = populateWipsEppsInput(loginRequest);
            final M00mTransactionOutput m00mTransactionOutput = (M00mTransactionOutput) this.eppsTransaction
                    .executeWipsImsTransaction(wipsImsEppsInput, this.imsConversation);
            final UserProfile userProfile = this.jobDetail.retrieveUserJobCodesProfile(loginRequest.getRacfId());
            pendingApproval = populateLoginResponse(m00mTransactionOutput);
            pendingApproval.setUserProfile(userProfile);
        } catch (final WipsImsInterfaceException wipsImsInterfaceException) {
            if (WipsConstant.SECURITY_FAILURE.equals(wipsImsInterfaceException.getMessage())) {
                log.logp(Level.SEVERE, CLASS_NAME, methodName, ERROR_INVALID_RACF_CREDENTIALS);
                log.throwing(CLASS_NAME, methodName, wipsImsInterfaceException);
                pendingApproval.populateErrorDetails(INVALID_RACF_CREDENTIALS);
            } else {
                log.throwing(CLASS_NAME, methodName, wipsImsInterfaceException);
                pendingApproval.populateErrorDetails(getErrorMessageWithLogReferenceCode(wipsImsInterfaceException));
            }
        } catch (final WipsImsTransactionException wipsImsTransactionException) {
            log.throwing(CLASS_NAME, methodName, wipsImsTransactionException);
            pendingApproval.populateErrorDetails(wipsImsTransactionException.getErrorMessage());
        } catch (final WipsDb2DatabaseException wipsDb2DatabaseException) {
            log.throwing(CLASS_NAME, methodName, wipsDb2DatabaseException);
            pendingApproval.populateErrorDetails(getErrorMessageWithLogReferenceCode(wipsDb2DatabaseException));
        } finally {
            endImsConversation();
        }
        log.exiting(CLASS_NAME, methodName);
        return pendingApproval;
    }

    /*
     * public PendingApprovalResponse processUserLoginRequestNew(final
     * LoginRequest loginRequest) { PendingApprovalResponse response = new
     * PendingApprovalResponse(); try { final M00mTransactionOutput
     * m00mTransactionOutput = this.ims.login("AAIMEPPS",
     * loginRequest.getRacfId(), loginRequest.getRacfPassword(), new
     * ConsumerThrowsException<DataArea, ConnectorException>() {
     * 
     * @Override public void accept(DataArea dataArea) throws ConnectorException
     * { dataArea.put("TP-INPUT-FIELDS.TPI-IOINDIC", WipsConstant.InputO);
     * dataArea.put("TP-INPUT-FIELDS.TPI-PFKEY", WipsConstant.PFKEY0);
     * dataArea.put("TP-INPUT-FIELDS.TPI-RACF-ID", loginRequest.getRacfId()); }
     * 
     * }, new BiFunctionThrowsException<DataArea, String, M00mTransactionOutput,
     * Exception>() {
     * 
     * @Override public M00mTransactionOutput apply(DataArea outputDataArea,
     * String rawOutput) throws Exception { final String transactionName =
     * TransactionOutputUtil.getString(outputDataArea,
     * "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM"); if
     * (!WipsTransactionConstant.M00M_TRANSACTION_NAME.equals(transactionName))
     * { throw new WipsImsTransactionException("", rawOutput.substring(38,
     * 113)); } return new
     * WipsTransactionHelper().populateM00mOutput(outputDataArea); } }); final
     * UserProfile userProfile =
     * this.jobDetail.retrieveUserJobCodesProfile(loginRequest.getRacfId());
     * response = populateLoginResponse(m00mTransactionOutput);
     * response.setUserProfile(userProfile); } catch (final
     * WipsImsInterfaceException wipsImsInterfaceException) { if
     * (WipsConstant.SECURITY_FAILURE.equals(wipsImsInterfaceException.
     * getMessage())) { response.populateErrorDetails(INVALID_RACF_CREDENTIALS);
     * } else {
     * response.populateErrorDetails(getErrorMessageWithLogReferenceCode(
     * wipsImsInterfaceException)); } } catch (final WipsImsTransactionException
     * wipsImsTransactionException) { //TODO log exceptions
     * response.populateErrorDetails(wipsImsTransactionException.getErrorMessage
     * ()); } catch (final WipsDb2DatabaseException wipsDb2DatabaseException) {
     * response.populateErrorDetails(getErrorMessageWithLogReferenceCode(
     * wipsDb2DatabaseException)); } catch (Exception e) {
     * response.populateErrorDetails(e.getMessage()); } return response; }
     */

    private PendingApprovalResponse populateLoginResponse(final M00mTransactionOutput m00mTransactionOutput) {
        final PendingApprovalResponse pendingApprovalResponse = new PendingApprovalResponse();
        pendingApprovalResponse.setValidUser(true);
        pendingApprovalResponse.setPendingApprovals(m00mTransactionOutput.getPendingApprovalList());
        final JobDetail userJobDetail = new JobDetail(m00mTransactionOutput.getUserJobCode(),
                m00mTransactionOutput.getUserJobName());
        pendingApprovalResponse.setJobDetail(userJobDetail);
        return pendingApprovalResponse;
    }

    private EppsTransactionInput populateWipsEppsInput(final LoginRequest loginRequest) {
        final EppsTransactionInput wipsEppsInput = new EppsTransactionInput();
        wipsEppsInput.setTpiIoindic(WipsConstant.InputO);
        wipsEppsInput.setTpiPfKey(WipsConstant.PFKEY0);
        wipsEppsInput.setUserRacfId(loginRequest.getRacfId());
        wipsEppsInput.setUserLoginPassword(loginRequest.getRacfPassword());
        wipsEppsInput.setImsAction(loginRequest.getImsAction());
        return wipsEppsInput;
    }

}
