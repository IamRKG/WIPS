package com.ford.purchasing.wips.domain.lumpsum.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.atp.ApproveWarningHandler;
import com.ford.purchasing.wips.common.atp.S01i0TransactionOutput;
import com.ford.purchasing.wips.common.layer.ApprovalDetail;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.common.lumpsum.C32uTransactionOutput;
import com.ford.purchasing.wips.common.lumpsum.LumpSumApproveOrRejectResponse;
import com.ford.purchasing.wips.common.lumpsum.LumpSumRequest;
import com.ford.purchasing.wips.common.lumpsum.WipsImsC32uInput;
import com.ford.purchasing.wips.common.lumpsum.WipsImsC33uInput;
import com.ford.purchasing.wips.common.lumpsum.WipsImsCR5xInput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionInput;
import com.ford.purchasing.wips.domain.atp.connector.M00mQ01xTransaction;
import com.ford.purchasing.wips.domain.atp.connector.Q01xTransaction;
import com.ford.purchasing.wips.domain.dao.db2.LsApproversDAO;
import com.ford.purchasing.wips.domain.dao.db2.LsClassificationDAO;
import com.ford.purchasing.wips.domain.dao.db2.LsGsaAuditDAO;
import com.ford.purchasing.wips.domain.dao.db2.LsSuppliersDAO;
import com.ford.purchasing.wips.domain.layer.WipsBaseAS;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.layer.exception.WipsImsInterfaceException;
import com.ford.purchasing.wips.domain.lumpsum.beans.C33uTransactionOutput;
import com.ford.purchasing.wips.domain.lumpsum.beans.CR5xTransactionOutput;
import com.ford.purchasing.wips.domain.lumpsum.beans.LumpSumResponse;
import com.ford.purchasing.wips.domain.lumpsum.beans.LumpSumSecurity;
import com.ford.purchasing.wips.domain.lumpsum.connector.C32uC33uTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.C32uC34uTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.C32uQ01xTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.C32uS01iOTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.C32uTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.C33uC32uTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.C33uCR5xTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.C33uTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.C34uC32uTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.CR5xC33uTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.CR5xTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.Q01xC32uTransaction;
import com.ford.purchasing.wips.domain.lumpsum.connector.S01OC32UTransaction;

public class LumpSumDetailAS extends WipsBaseAS {

    private static final String CLASS_NAME = LumpSumDetailAS.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private CR5xTransactionOutput cR5xTransactionOutput = null;
    private int lineNumber = 0;
    private String jobCode;
    private String enterUser;
    private boolean checkUserWithPFKey13 = false;

    @Inject
    private M00mQ01xTransaction m00mQ01xTransaction;

    @Inject
    private Q01xTransaction q01xTransaction;

    @Inject
    private Q01xC32uTransaction q01xC32uTransaction;

    @Inject
    private C32uTransaction c32uTransaction;

    @Inject
    private C32uC33uTransaction c32uC33uTransaction;

    @Inject
    private C33uCR5xTransaction c33uCR5xTransaction;

    @Inject
    private CR5xTransaction cR5xTransaction;

    @Inject
    private C33uTransaction c33uTransaction;

    @Inject
    private LsGsaAuditDAO lumpSumGsaAudit;

    @Inject
    private LsClassificationDAO lumpSumClassification;

    @Inject
    private LsSuppliersDAO suppliers;

    @Inject
    private LsApproversDAO approvers;

    @Inject
    private C32uS01iOTransaction c32uS01i0Transaction;

    @Inject
    private S01OC32UTransaction s01oc32uTransaction;

    @Inject
    private CR5xC33uTransaction cR5xC33uTransaction;

    @Inject
    private C33uC32uTransaction c33uC32uTransaction;

    @Inject
    private C32uQ01xTransaction c32uQ01xTransaction;

    @Inject
    private C32uC34uTransaction c32uC34uTransaction;

    @Inject
    private C34uC32uTransaction c34uC32uTransaction;

    @Inject
    private LumpSumSecurityService securityService;

    public LumpSumResponse retrieveLumpSumDetail(final LumpSumRequest lumpSumRequest) {
        final String methodName = "retrieveLumpSumDetail";
        final LumpSumResponse lumpSumResponse = new LumpSumResponse();
        Map<String, List<String>> lumpSumRemarksMap = null;
        try {
            this.imsConversation = startImsConversation();
            final C32uTransactionOutput latestLumpSumVersion =
                    retrieveLatestAmendmentDetails(lumpSumRequest);
            final C32uTransactionOutput c32uTransactionOutput =
                    retrieveDetailsForPreviousAmendment(lumpSumRequest,
                            latestLumpSumVersion);
            c32uTransactionOutput.setGsaAudit(this.lumpSumGsaAudit.retrieveLumpsumGsaAudit());
            c32uTransactionOutput.setSupplierInformation(this.suppliers.retrieveSuppliers(
                    c32uTransactionOutput.getLumpsumNumber(),
                    c32uTransactionOutput.getCurrentAmendment()));

            final C33uTransactionOutput c33uTransactionOutput =
                    generateAdditionalLumpSumInformation(
                            c32uTransactionOutput, lumpSumRequest);
            c33uTransactionOutput.setClassification(
                    this.lumpSumClassification.retrieveLumpSumClassifications(
                            c32uTransactionOutput.getLumpsumNumber(),
                            c32uTransactionOutput.getCurrentAmendment()));

            // TO get the Lump Sum Remarks
            lumpSumRemarksMap = getLumpSumRemarks(lumpSumRequest,
                    c33uTransactionOutput.getWipsImsC33uInput(),
                    WipsConstant.RETRIEVE_REMARKS);

            this.cR5xTransactionOutput.setApprovalDetails(this.approvers.retrieveApprovers(
                    c32uTransactionOutput.getLumpsumNumber(),
                    c32uTransactionOutput.getCurrentAmendment(),
                    lumpSumRemarksMap, lumpSumRequest.getCurrentJobCode()));
            c32uTransactionOutput.setWipsImsC32uInput(null);
            lumpSumResponse.setLumpSumInformation(c32uTransactionOutput);
            lumpSumResponse.setAdditionalLumpSumInformation(c33uTransactionOutput);
            this.cR5xTransactionOutput.setWipsImsCR5xInput(null);
            lumpSumResponse.setLumpSumRemarks(this.cR5xTransactionOutput);
            lumpSumResponse.setUserRacfId(lumpSumRequest.getUserRacfId());
            lumpSumResponse.setAvailableAmendments(
                    this.getAmendmentsForLumpSum(
                            lumpSumResponse.getLumpSumInformation().getLatestAmendment()));
            lumpSumResponse
                    .setLumpSumReadOnlyFlag(c32uTransactionOutput.isLumpSumReadOnlyFlag());
            lumpSumResponse.setSecurityInformation(
                    getSecurityInformation(lumpSumRequest, lumpSumResponse));
        } catch (final WipsImsInterfaceException wipsImsInterfaceException) {
            log.throwing(CLASS_NAME, methodName, wipsImsInterfaceException);
            lumpSumResponse.populateErrorDetails(
                    getErrorMessageWithLogReferenceCode(wipsImsInterfaceException));
        } catch (final WipsDb2DatabaseException wipsDb2DatabaseException) {
            log.throwing(CLASS_NAME, methodName, wipsDb2DatabaseException);
            lumpSumResponse.populateErrorDetails(
                    getErrorMessageWithLogReferenceCode(wipsDb2DatabaseException));
        } catch (final WipsImsTransactionException exception) {
            log.throwing(CLASS_NAME, methodName, exception);
            lumpSumResponse.populateErrorDetails(exception.getErrorMessage());
        } finally {
            endImsConversation();
        }

        return lumpSumResponse;
    }

    public LumpSumResponse saveLumpSumDetails(final LumpSumRequest lumpSumRequest) {
        final String methodName = "saveLumpSumDetails";
        final LumpSumResponse lumpSumResponse = new LumpSumResponse();
        C32uTransactionOutput c32uTransactionOutput = null;
        C33uTransactionOutput c33uTransactionOutput = null;
        try {
            this.imsConversation = startImsConversation();
            c32uTransactionOutput = retrieveLatestAmendmentDetails(lumpSumRequest);

            if (lumpSumRequest.isGroup1Save()) {
                c32uTransactionOutput =
                        saveGsaAuditDetails(lumpSumRequest, c32uTransactionOutput);
            }
            c33uTransactionOutput = generateAdditionalLumpSumInformation(
                    c32uTransactionOutput, lumpSumRequest);
            if (lumpSumRequest.isGroup2Save()) {
                c33uTransactionOutput = saveAdditionalLumpsumInformation(lumpSumRequest,
                        c33uTransactionOutput);
            }
            if (lumpSumRequest.isGroup3Save()) {
                saveLumpsumRemarks(lumpSumRequest, c33uTransactionOutput);
                this.cR5xTransactionOutput.setRemarksSuccessfullySaved(true);
                this.cR5xTransactionOutput.setWipsImsCR5xInput(null);
            }
            lumpSumResponse.setLumpSumDetailSavedFlag(true);
            lumpSumResponse.setLumpSumInformation(c32uTransactionOutput);
            lumpSumResponse.setAdditionalLumpSumInformation(c33uTransactionOutput);
            lumpSumResponse.setLumpSumRemarks(this.cR5xTransactionOutput);
            lumpSumResponse.setSecurityInformation(
                    getSecurityInformation(lumpSumRequest, lumpSumResponse));
        } catch (final WipsImsInterfaceException wipsImsInterfaceException) {
            log.throwing(CLASS_NAME, methodName, wipsImsInterfaceException);
            lumpSumResponse.populateErrorDetails(
                    getErrorMessageWithLogReferenceCode(wipsImsInterfaceException));
        } catch (final WipsImsTransactionException exception) {
            log.throwing(CLASS_NAME, methodName, exception);
            checkAndExitToM00m(lumpSumRequest, c32uTransactionOutput, c33uTransactionOutput,
                    exception);
            lumpSumResponse.populateErrorDetails(exception.getErrorMessage());

        } finally {
            endImsConversation();
        }
        return lumpSumResponse;
    }

    private LumpSumSecurity getSecurityInformation(final LumpSumRequest lumpSumRequest,
            final LumpSumResponse lumpSumResponse) {
        return this.securityService.getLumpSumSecurity(lumpSumRequest.getJobTitle(),
                lumpSumResponse);
    }

    private void checkAndExitToM00m(final LumpSumRequest lumpSumRequest,
            final C32uTransactionOutput c32uTransactionOutput,
            final C33uTransactionOutput c33uTransactionOutput,
            final WipsImsTransactionException exception) {
        if (exception.getTransactionCode().contains("C32U")
            && (WipsUtil.isExceptionContainingMessageCode(exception.getErrorMessage(),
                    Arrays.asList(WipsConstant.getC32uErrorMessages()))))
            moveToQ01xTransactionFromC32u(c32uTransactionOutput, lumpSumRequest);
        if (exception.getTransactionCode().contains("C33U")
            && (WipsUtil.isExceptionContainingMessageCode(exception.getErrorMessage(),
                    Arrays.asList(WipsConstant.getC33uErrorMessages())))) {
            moveToC32uTransactionFromC33u(c33uTransactionOutput, lumpSumRequest);
            moveToQ01xTransactionFromC32u(c32uTransactionOutput, lumpSumRequest);

        }
    }

    private void moveToC32uTransactionFromC33u(
            final C33uTransactionOutput c33uTransactionOutput,
            final LumpSumRequest lumpSumRequest) {
        final String methodName = "moveToM00mTransactionFromC33u";
        final WipsImsC33uInput wipsImsC33uInput = c33uTransactionOutput.getWipsImsC33uInput();
        wipsImsC33uInput.setTpiPfKey(WipsConstant.PFKEY11);
        wipsImsC33uInput.setLterm(lumpSumRequest.getLterm());
        try {
            this.c33uC32uTransaction.executeWipsImsTransaction(wipsImsC33uInput,
                    this.imsConversation);
        } catch (final WipsImsInterfaceException exception) {
            log.throwing(CLASS_NAME, methodName, exception);
        } catch (final WipsImsTransactionException exception) {
            log.throwing(CLASS_NAME, methodName, exception);
        }
    }

    private void saveLumpsumRemarks(final LumpSumRequest lumpSumRequest,
            final C33uTransactionOutput c33uTransactionOutput)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final List<ApprovalDetail> savedUserRemarks = new ArrayList<ApprovalDetail>();
        c33uTransactionOutput.getWipsImsC33uInput().setTpiPfKey(WipsConstant.PFKEY9);
        getLumpSumRemarks(lumpSumRequest, c33uTransactionOutput.getWipsImsC33uInput(),
                WipsConstant.SAVE_LS_REMARKS);
        if (this.checkUserWithPFKey13) {
            getLumpSumRemarks(lumpSumRequest, c33uTransactionOutput.getWipsImsC33uInput(),
                    WipsConstant.SAVE_LS_REMARKS);
        }
        final String userRemarks =
                StringUtil.buildUserRemarks(lumpSumRequest.getUserRemarks());
        clearExistingUserRemarks(this.cR5xTransactionOutput.getWipsImsCR5xInput());
        final List<String> tempRemarksList = StringUtil.splitString(userRemarks);
        final ApprovalDetail approvalDetail = saveLumpsumRemarksEntered(
                this.cR5xTransactionOutput.getWipsImsCR5xInput(), lumpSumRequest,
                tempRemarksList);
        savedUserRemarks.add(approvalDetail);
        this.cR5xTransactionOutput.setApprovalDetails(savedUserRemarks);
    }

    private void moveToQ01xTransactionFromC32u(
            final C32uTransactionOutput c32uTransactionOutput,
            final LumpSumRequest lumpSumRequest) {
        final String methodName = "moveToM00mTransactionFromC32u";
        final WipsImsC32uInput wipsImsC32uInput = c32uTransactionOutput.getWipsImsC32uInput();
        wipsImsC32uInput.setTpiPfKey(WipsConstant.PFKEY11);
        wipsImsC32uInput.setLterm(lumpSumRequest.getLterm());
        try {
            this.c32uQ01xTransaction.executeWipsImsTransaction(wipsImsC32uInput,
                    this.imsConversation);
        } catch (final WipsImsInterfaceException exception) {
            log.throwing(CLASS_NAME, methodName, exception);
        } catch (final WipsImsTransactionException exception) {
            log.throwing(CLASS_NAME, methodName, exception);
        }
    }

    private void clearExistingUserRemarks(final WipsImsCR5xInput wipsCR5xInput)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        CR5xTransactionOutput output = new CR5xTransactionOutput();
        WipsImsCR5xInput cR5xInput = wipsCR5xInput;
        do {
            cR5xInput.setTpiIoindic(WipsConstant.InputI);
            cR5xInput.setTpiPfKey(WipsConstant.PFKEY00);

            final List<String> remarks = new ArrayList<String>();

            cR5xInput.setRemarksText(remarks);
            cR5xInput.setActionTaken(WipsConstant.CLEAR_REMARKS);

            output = (CR5xTransactionOutput)this.cR5xTransaction.executeWipsImsTransaction(
                    cR5xInput,
                    this.imsConversation);

            cR5xInput = output.getWipsImsCR5xInput();
        } while (output.getErrorMessage().contains("MSG-1594"));
    }

    private ApprovalDetail saveLumpsumRemarksEntered(final WipsImsCR5xInput cR5xInput,
            final LumpSumRequest lumpSumRequest, final List<String> tempRemarksList)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        CR5xTransactionOutput output = new CR5xTransactionOutput();
        final List<String> remarksFromIms = new ArrayList<String>();
        String outputJobCode;
        final ApprovalDetail apprDetail = new ApprovalDetail();
        do {
            if (this.lineNumber != 0) {
                this.cR5xTransactionOutput = output;
                getLumpSumRemarks(lumpSumRequest, new WipsImsC33uInput(),
                        WipsConstant.SAVE_REMARKS_NEXT_PAGE);
            }
            cR5xInput.setActionTaken(WipsConstant.SAVE_REMARKS);
            cR5xInput.setTpiPfKey(WipsConstant.PFKEY0);
            putRemarksInDataArea(cR5xInput, tempRemarksList);
            output = (CR5xTransactionOutput)this.cR5xTransaction.executeWipsImsTransaction(
                    cR5xInput,
                    this.imsConversation);
            outputJobCode = output.getApprovalDetails().get(0).getJobCode();
        } while (this.lineNumber < tempRemarksList.size()
                 && !output.getErrorMessage().contains("MSG-0046"));
        remarksFromIms.addAll(output.getApprovalDetails().get(0).getRemarks());
        apprDetail.setRemarks(remarksFromIms);
        apprDetail.setJobCode(outputJobCode);
        return apprDetail;
    }

    private void putRemarksInDataArea(final WipsImsCR5xInput cR5xInput,
            final List<String> tempRemarksList) {
        final List<DataArea> outputLoop = cR5xInput.getBufferedSegLoop();
        for (final Iterator<DataArea> i = outputLoop.iterator(); i.hasNext();) {
            final DataArea inputDataArea = i.next();
            if (this.lineNumber < tempRemarksList.size()) {
                log.info("String length"
                         + tempRemarksList.get(this.lineNumber).toString().length() + " "
                         + tempRemarksList.get(this.lineNumber).toString());
                inputDataArea.put("TPO-APPRMKS",
                        tempRemarksList.get(this.lineNumber).toString());
            } else {
                inputDataArea.put("TPO-APPRMKS", StringUtil.createBlankSpaces(72));
            }
            this.lineNumber++;
        }
    }

    public LumpSumApproveOrRejectResponse saveAndApproveOrRejectLumpSumDetails(
            final LumpSumRequest lumpSumRequest) {

        final String methodName = "getRemarks";
        log.entering(CLASS_NAME, methodName);
        LumpSumApproveOrRejectResponse lsApproveOrRejectResponse =
                new LumpSumApproveOrRejectResponse();
        final LumpSumResponse lumpSumResponse = new LumpSumResponse();
        C32uTransactionOutput c32uTransactionOutput = null;
        C33uTransactionOutput c33uTransactionOutput = null;
        try {
            this.imsConversation = startImsConversation();

            c32uTransactionOutput = retrieveLatestAmendmentDetails(lumpSumRequest);
            if (lumpSumRequest.isGroup1Save()) {
                saveGsaAuditDetails(lumpSumRequest, c32uTransactionOutput);
            }

            c33uTransactionOutput = generateAdditionalLumpSumInformation(
                    c32uTransactionOutput, lumpSumRequest);
            if (lumpSumRequest.isGroup2Save()) {
                saveAdditionalLumpsumInformation(lumpSumRequest, c33uTransactionOutput);
            }
            if (lumpSumRequest.isGroup3Save()) {
                saveLumpsumRemarks(lumpSumRequest, c33uTransactionOutput);
                executeCR5xC33uTranasction(lumpSumRequest);
            }
            final C32uTransactionOutput output =
                    executeC33uC32uTransaction(lumpSumRequest, c33uTransactionOutput);
            lumpSumResponse.setLumpSumDetailSavedFlag(true);
            if (output.hasMoreSuppliers()) {
                executeC34Transaction(lumpSumRequest, c32uTransactionOutput);
                executeC34uC32uTransaction(lumpSumRequest);
            }
            lsApproveOrRejectResponse =
                    this.approveOrRejectLumpSum(lumpSumRequest, c32uTransactionOutput);

        } catch (final WipsImsInterfaceException wipsImsInterfaceException) {
            log.throwing(CLASS_NAME, methodName, wipsImsInterfaceException);
            lsApproveOrRejectResponse
                    .populateErrorDetails(
                            getErrorMessageWithLogReferenceCode(wipsImsInterfaceException));
        } catch (final WipsImsTransactionException exception) {
            log.throwing(CLASS_NAME, methodName, exception);
            checkAndExitToM00m(lumpSumRequest, c32uTransactionOutput, c33uTransactionOutput,
                    exception);
            lsApproveOrRejectResponse.populateErrorDetails(exception.getErrorMessage());
        } finally {
            endImsConversation();
        }
        log.exiting(CLASS_NAME, methodName);
        return lsApproveOrRejectResponse;
    }

    private void executeC34uC32uTransaction(final LumpSumRequest lumpSumRequest)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final WipsImsInput wipsImsInput = new WipsImsInput();
        wipsImsInput.setLterm(lumpSumRequest.getLterm());
        wipsImsInput.setTpiPfKey(WipsConstant.PFKEY3);
        wipsImsInput.setTpiIoindic(WipsConstant.InputI);

        this.c34uC32uTransaction.executeWipsImsTransaction(wipsImsInput,
                this.imsConversation);

    }

    private void executeC34Transaction(final LumpSumRequest lumpSumRequest,
            final C32uTransactionOutput c32uTransactionOutput)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final WipsImsC32uInput wipsImsC32uInput = c32uTransactionOutput.getWipsImsC32uInput();
        wipsImsC32uInput.setLterm(lumpSumRequest.getLterm());
        wipsImsC32uInput.setTpiPfKey(WipsConstant.PFKEY19);

        this.c32uC34uTransaction.executeWipsImsTransaction(wipsImsC32uInput,
                this.imsConversation);
    }

    private C33uTransactionOutput executeCR5xC33uTranasction(
            final LumpSumRequest lumpSumRequest)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final WipsImsCR5xInput input = createCR5xInput(lumpSumRequest);
        return (C33uTransactionOutput)this.cR5xC33uTransaction
                .executeWipsImsTransaction(input, this.imsConversation);
    }

    private WipsImsCR5xInput createCR5xInput(final LumpSumRequest lumpSumRequest) {
        final WipsImsCR5xInput cR5xInput = this.cR5xTransactionOutput.getWipsImsCR5xInput();// new
        cR5xInput.setLterm(lumpSumRequest.getLterm());
        cR5xInput.setTpiPfKey(WipsConstant.PFKEY3);
        return cR5xInput;
    }

    private C32uTransactionOutput executeC33uC32uTransaction(
            final LumpSumRequest lumpSumRequest,
            final C33uTransactionOutput c33uTransactionOutput)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final WipsImsC33uInput input = createC33uInputForApprove(lumpSumRequest,
                c33uTransactionOutput.getWipsImsC33uInput());
        return (C32uTransactionOutput)this.c33uC32uTransaction
                .executeWipsImsTransaction(input, this.imsConversation);
    }

    private WipsImsC33uInput createC33uInputForApprove(final LumpSumRequest lumpSumRequest,
            final WipsImsC33uInput wipsImsC33uInput) {
        final WipsImsC33uInput c33uInput = wipsImsC33uInput;
        c33uInput.setLterm(lumpSumRequest.getLterm());
        c33uInput.setTpiPfKey(WipsConstant.PFKEY3);
        return c33uInput;
    }

    private C33uTransactionOutput saveAdditionalLumpsumInformation(
            final LumpSumRequest lumpSumRequest,
            final C33uTransactionOutput c33uTransactionOutput2)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final WipsImsC33uInput wipsImsC33uInput = createC33uInput(lumpSumRequest,
                c33uTransactionOutput2.getWipsImsC33uInput());
        return (C33uTransactionOutput)this.c33uTransaction.executeWipsImsTransaction(
                wipsImsC33uInput,
                this.imsConversation);
    }

    private WipsImsC33uInput createC33uInput(final LumpSumRequest lumpSumRequest,
            final WipsImsC33uInput wipsImsC33uInput) {
        if (lumpSumRequest.getSelectedClassification() != null)
            wipsImsC33uInput.setClassification(lumpSumRequest.getSelectedClassification());
        if (lumpSumRequest.getWorkTaskNumber() != null)
            wipsImsC33uInput.setWorkTaskNumber(lumpSumRequest.getWorkTaskNumber());
        if (lumpSumRequest.getShortTermCost() != null) {
            wipsImsC33uInput.setShortTermSign(
                    StringUtil.retriveCostSign(lumpSumRequest.getShortTermCost()));
            wipsImsC33uInput.setShortTermCost(
                    StringUtil.retrieveCost(lumpSumRequest.getShortTermCost()));
        }
        if (lumpSumRequest.getLongTermCost() != null) {
            wipsImsC33uInput.setLongTermSign(
                    StringUtil.retriveCostSign(lumpSumRequest.getLongTermCost()));
            wipsImsC33uInput.setLongTermCost(
                    StringUtil.retrieveCost(lumpSumRequest.getLongTermCost()));
        }
        wipsImsC33uInput.setTpiPfKey(WipsConstant.PFKEY0);
        wipsImsC33uInput.setTpiIoindic(WipsConstant.InputI);
        wipsImsC33uInput.setLterm(lumpSumRequest.getLterm());
        return wipsImsC33uInput;
    }

    private C32uTransactionOutput saveGsaAuditDetails(final LumpSumRequest lumpSumRequest,
            final C32uTransactionOutput c32uTransactionOutput)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final WipsImsC32uInput input =
                createC32uInputForSaveLumpsum(lumpSumRequest, c32uTransactionOutput);
        return (C32uTransactionOutput)this.c32uTransaction.executeWipsImsTransaction(input,
                this.imsConversation);
    }

    private WipsImsC32uInput createC32uInputForSaveLumpsum(
            final LumpSumRequest lumpSumRequest,
            final C32uTransactionOutput c32uTransactionOutput) {
        final WipsImsC32uInput input = c32uTransactionOutput.getWipsImsC32uInput();
        input.setTpiPfKey(WipsConstant.PFKEY0);
        if (lumpSumRequest.getGsaAmount() != null) {
            input.setGsaCosts(StringUtil.retriveCostSign(lumpSumRequest.getGsaAmount()));
            input.setGsaCost(StringUtil.retrieveCost(lumpSumRequest.getGsaAmount()));
        }
        if (lumpSumRequest.getSelectedGsaAudit() != null)
            input.setGsaAudit(lumpSumRequest.getSelectedGsaAudit());
        input.setLterm(lumpSumRequest.getLterm());
        return input;
    }

    private C32uTransactionOutput retrieveLatestAmendmentDetails(
            final LumpSumRequest lumpSumRequest)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final M00mTransactionOutput m00mTransactionOutput =
                executeEppsCallForAuthentication(lumpSumRequest);
        if (!m00mTransactionOutput.getUserJobCode()
                .equals(lumpSumRequest.getCurrentJobCode())) {
            executeM00mCallForswitchDelegateJob(lumpSumRequest);
        }
        final WipsImsInput wipsImsM00mInput = populateM00mInput(lumpSumRequest);
        wipsImsM00mInput.setTpiOption(WipsConstant.LUMPSUM_ENTITY_CODE);
        this.m00mQ01xTransaction.executeWipsImsTransaction(wipsImsM00mInput,
                this.imsConversation);
        this.q01xTransaction.executeWipsImsTransaction(
                new Q01xTransactionInput().populateWipsQ01XInput(lumpSumRequest),
                this.imsConversation);
        return retrieveLatestAmendment(lumpSumRequest);
    }

    protected C32uTransactionOutput retrieveDetailsForPreviousAmendment(
            final LumpSumRequest lumpSumRequest,
            final C32uTransactionOutput latestLumpSumVersion)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final String requestedAmendment = lumpSumRequest.getRequestedAmendmentVersion();
        final String latestAmendment = latestLumpSumVersion.getCurrentAmendment();
        C32uTransactionOutput previouslumpSumVersion = latestLumpSumVersion;
        if (isPreviousAmendmentRequested(requestedAmendment,
                latestLumpSumVersion.getCurrentAmendment())) {
            previouslumpSumVersion =
                    retrieveRequestedAmendment(requestedAmendment, previouslumpSumVersion);
            previouslumpSumVersion.setLatestAmendment(latestAmendment);
        }
        return previouslumpSumVersion;
    }

    private C32uTransactionOutput retrieveRequestedAmendment(
            final String requestedAmendmentNumber,
            final C32uTransactionOutput lumpSumDetails)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        String currentAmendmentNumber = lumpSumDetails.getCurrentAmendment();
        C32uTransactionOutput requestedAmendment = null;
        while (isRequestedAmendment(requestedAmendmentNumber, currentAmendmentNumber)) {
            requestedAmendment = retrievePreviousAmendment(lumpSumDetails);
            currentAmendmentNumber = requestedAmendment.getCurrentAmendment();
        }
        return requestedAmendment;
    }

    private C32uTransactionOutput retrievePreviousAmendment(
            final C32uTransactionOutput lumpSumDetails)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final WipsImsC32uInput wipsImsC32uInput = lumpSumDetails.getWipsImsC32uInput();
        wipsImsC32uInput.setTpiPfKey(WipsConstant.PFKEY18);
        final C32uTransactionOutput previousAmendment =
                (C32uTransactionOutput)this.c32uTransaction
                        .executeWipsImsTransaction(wipsImsC32uInput, this.imsConversation);
        previousAmendment.setLumpSumReadOnlyFlag(true);
        return previousAmendment;
    }

    private boolean isRequestedAmendment(final String requestedAmendmentVersion,
            final String currentAmendment) {
        return !requestedAmendmentVersion.equals(currentAmendment)
               && !WipsConstant.INITIAL_AMENDMENT.equals(currentAmendment);
    }

    private boolean isPreviousAmendmentRequested(final String requestedAmendmentVersion,
            final String currentAmendment) {
        return !WipsConstant.DEFAULT_REQUESTED_AMENDMENT.equals(requestedAmendmentVersion)
               && isRequestedAmendment(requestedAmendmentVersion, currentAmendment);
    }

    private C32uTransactionOutput retrieveLatestAmendment(final LumpSumRequest lumpSumRequest)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final C32uTransactionOutput c32uTransactionOutput =
                (C32uTransactionOutput)this.q01xC32uTransaction
                        .executeWipsImsTransaction(new Q01xTransactionInput()
                                .populateWipsQ01XToC32UInput(lumpSumRequest),
                                this.imsConversation);
        c32uTransactionOutput.setLatestAmendment(c32uTransactionOutput.getCurrentAmendment());
        return c32uTransactionOutput;
    }

    private C33uTransactionOutput generateAdditionalLumpSumInformation(
            final C32uTransactionOutput c32uTransactionOutput,
            final LumpSumRequest lumpSumRequest)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final WipsImsC32uInput wipsImsC32uInput = c32uTransactionOutput.getWipsImsC32uInput();
        wipsImsC32uInput.setTpiPfKey(WipsConstant.PFKEY23);
        wipsImsC32uInput.setLterm(lumpSumRequest.getLterm());
        return (C33uTransactionOutput)this.c32uC33uTransaction.executeWipsImsTransaction(
                wipsImsC32uInput,
                this.imsConversation);
    }

    public WipsBaseResponse confirmPaymentDescription(final LumpSumRequest lumpSumRequest) {
        final String methodName = "confirmLSPaymentDescription";
        final WipsBaseResponse wipsBaseResponse = new WipsBaseResponse();

        try {
            this.imsConversation = startImsConversation();
            final C32uTransactionOutput latestLumpSumVersion =
                    retrieveLatestAmendmentDetails(lumpSumRequest);
            final C33uTransactionOutput c33uTransactionOutput =
                    generateAdditionalLumpSumInformation(
                            latestLumpSumVersion, lumpSumRequest);

            // process lump sum confirm selection for payment description
            confirmPaymentDescription(c33uTransactionOutput,
                    WipsConstant.CONFIRMLS_PAYMENT_DESC);

        } catch (final WipsImsInterfaceException wipsImsInterfaceException) {
            log.throwing(CLASS_NAME, methodName, wipsImsInterfaceException);
            wipsBaseResponse.populateErrorDetails(
                    getErrorMessageWithLogReferenceCode(wipsImsInterfaceException));
        } catch (final WipsImsTransactionException exception) {
            log.throwing(CLASS_NAME, methodName, exception);
            wipsBaseResponse.populateErrorDetails(exception.getErrorMessage());
        } finally {
            endImsConversation();
        }
        return wipsBaseResponse;
    }

    private C33uTransactionOutput confirmPaymentDescription(
            final C33uTransactionOutput c33uTransactionOutput,
            final String confirmLSPayment)
            throws WipsImsInterfaceException, WipsImsTransactionException {

        final WipsImsC33uInput wipsImsC33uInput = c33uTransactionOutput.getWipsImsC33uInput();
        wipsImsC33uInput.setTpiPfKey(WipsConstant.PFKEY21);
        wipsImsC33uInput.setActionTaken(confirmLSPayment);
        return (C33uTransactionOutput)this.c33uTransaction.executeWipsImsTransaction(
                wipsImsC33uInput,
                this.imsConversation);
    }

    private Map<String, List<String>> getLumpSumRemarks(final LumpSumRequest lumpSumRequest,
            final WipsImsC33uInput wipsImsC33uInput, final String actionTakenForRemarks)
            throws WipsImsInterfaceException, WipsImsTransactionException {

        String actionTaken = actionTakenForRemarks;

        final List<Map<String, List<String>>> lumpSumAllUserRemarks =
                new ArrayList<Map<String, List<String>>>();
        final Map<String, List<String>> remarksMap = new HashMap<String, List<String>>();
        List<String> remarks = new ArrayList<String>();
        boolean checkMoreFlag = false;

        wipsImsC33uInput.setLterm(lumpSumRequest.getLterm());

        // executes first remarks screen from CF33 with F9 key
        if (!WipsConstant.SAVE_REMARKS_NEXT_PAGE.equals(actionTaken)
            && !this.checkUserWithPFKey13) {
            this.cR5xTransactionOutput = (CR5xTransactionOutput)this.c33uCR5xTransaction
                    .executeWipsImsTransaction(wipsImsC33uInput, this.imsConversation);

            remarks = this.cR5xTransactionOutput.getApprovalDetails().get(0).getRemarks();
            this.enterUser =
                    this.cR5xTransactionOutput.getApprovalDetails().get(0).getEnterUser();
            this.jobCode =
                    this.cR5xTransactionOutput.getApprovalDetails().get(0).getJobCode();
            checkMoreFlag = this.cR5xTransactionOutput.isCheckMorePagesFlag();

            // system remarks check, if NO more pages put c33uToCR5x remarks
            // in map and check for next user remarks
            if (checkMoreFlag && !(WipsConstant.SAVE_REMARKS.equals(actionTaken)
                                   || WipsConstant.SAVE_LS_REMARKS.equals(actionTaken))) {
                remarksMap.put(this.enterUser, remarks);
                remarks = new ArrayList<String>();
            }
        }

        if (this.enterUser.equals(this.jobCode)
            && (actionTaken.equals(WipsConstant.SAVE_REMARKS)
                || WipsConstant.SAVE_LS_REMARKS.equals(actionTaken))) {
            // do nothing
        } else {
            // executes MORE/Approvers remarks from CR5x with F8 and F14 keys
            do {
                final WipsImsCR5xInput wipsImsCR5xInput =
                        this.cR5xTransactionOutput.getWipsImsCR5xInput();

                wipsImsCR5xInput.setLterm(lumpSumRequest.getLterm());
                wipsImsCR5xInput.setActionTaken(actionTaken);

                if (this.checkUserWithPFKey13) {
                    wipsImsCR5xInput.setTpiPfKey(WipsConstant.PFKEY13);
                }

                if (WipsConstant.SAVE_REMARKS_NEXT_PAGE.equals(actionTaken)) {
                    wipsImsCR5xInput.setTpiPfKey(WipsConstant.PFKEY8);
                    wipsImsCR5xInput.setActionTaken(WipsConstant.SAVE_REMARKS_NEXT_PAGE);
                    actionTaken = WipsConstant.SAVE_REMARKS;
                }

                this.cR5xTransactionOutput = (CR5xTransactionOutput)this.cR5xTransaction
                        .executeWipsImsTransaction(wipsImsCR5xInput, this.imsConversation);

                if (!this.cR5xTransactionOutput.getApprovalDetails().isEmpty()
                    || (WipsConstant.SAVE_REMARKS.equals(actionTaken)
                        || WipsConstant.SAVE_LS_REMARKS.equals(actionTaken))) {
                    if (!(WipsConstant.SAVE_REMARKS.equals(actionTaken)
                          || WipsConstant.SAVE_LS_REMARKS.equals(actionTaken))) {
                        remarks.addAll(this.cR5xTransactionOutput.getApprovalDetails()
                                .get(0)
                                .getRemarks());
                    }
                    if (this.cR5xTransactionOutput.getErrorMessage() != null
                        && !this.cR5xTransactionOutput
                                .getErrorMessage()
                                .contains(WipsConstant.LUMPSUM_ERROR_1283)) {
                        this.enterUser = this.cR5xTransactionOutput.getApprovalDetails()
                                .get(0)
                                .getEnterUser();
                        this.jobCode = this.cR5xTransactionOutput.getApprovalDetails()
                                .get(0)
                                .getJobCode();
                    }
                }
                checkMoreFlag = this.cR5xTransactionOutput.isCheckMorePagesFlag();
                if (checkMoreFlag && !WipsConstant.SAVE_REMARKS.equals(actionTaken)) {
                    remarksMap.put(this.enterUser, remarks);
                    remarks = new ArrayList<String>();
                }
                if (this.enterUser.equals(this.jobCode)
                    && (actionTaken.equals(WipsConstant.SAVE_REMARKS)
                        || actionTaken.equals(WipsConstant.SAVE_LS_REMARKS)
                        || actionTaken.equals(WipsConstant.SAVE_REMARKS_NEXT_PAGE)))
                    break;
                if (this.cR5xTransactionOutput.getErrorMessage()
                        .contains(WipsConstant.LUMPSUM_ERROR_1283)
                    && !(this.enterUser.equals(this.jobCode))
                    && (actionTaken.equals(WipsConstant.SAVE_LS_REMARKS))) {
                    this.checkUserWithPFKey13 = true;
                    break;
                }
            } while (null != this.cR5xTransactionOutput.getErrorMessage()
                     && !this.cR5xTransactionOutput.getErrorMessage()
                             .contains(WipsConstant.LUMPSUM_ERROR_1283));
        }
        lumpSumAllUserRemarks.add(remarksMap);

        return WipsUtil.getMapFromList(lumpSumAllUserRemarks);
    }

    List<String> getAmendmentsForLumpSum(final String latestAmendment) {
        final List<String> amendments = new ArrayList<String>();
        for (int i = Integer.parseInt(latestAmendment); i > 0; i--) {
            amendments.add(String.format("%02d", i));
        }
        return amendments;
    }

    protected LumpSumApproveOrRejectResponse approveOrRejectLumpSum(
            final LumpSumRequest lumpSumRequest,
            final C32uTransactionOutput latestAmendment)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final LumpSumApproveOrRejectResponse lumpSumApproveOrRejectResponse =
                new LumpSumApproveOrRejectResponse();
        final String lterm = lumpSumRequest.getLterm();
        final String actionTaken = lumpSumRequest.getActionTaken();
        final short pfKey = pfKey(actionTaken);
        final S01i0TransactionOutput s01i0TransactionOutput =
                executeS01i0C32uCall(latestAmendment, lterm, pfKey);
        final String errorMessage = s01i0TransactionOutput.getErrorMessage();
        if (errorMessage != null) {
            if (!WipsConstant.CONFIRM.equals(actionTaken)) {
                moveToQ01xTransactionFromC32u(latestAmendment, lumpSumRequest);
                return approveOrRejectWarningResponse(actionTaken, errorMessage);
            }
            executeS01i0C32uCall(latestAmendment, lterm, pfKey);
        }
        final C32uTransactionOutput approveOutput = executeS01OToC32uTransaction(lterm);
        if (!WipsConstant.REJECT.equals(actionTaken)) {
            moveToQ01xTransactionFromC32u(latestAmendment, lumpSumRequest);
        }

        lumpSumApproveOrRejectResponse
                .setApproveOrRejectMessage(approveOutput.getConfirmationMessage());
        return lumpSumApproveOrRejectResponse;
    }

    private S01i0TransactionOutput executeS01i0C32uCall(
            final C32uTransactionOutput latestAmendment, final String lterm,
            final short pfKey) throws WipsImsInterfaceException, WipsImsTransactionException {
        final WipsImsC32uInput wipsImsC32uInput = latestAmendment.getWipsImsC32uInput();
        wipsImsC32uInput.setLterm(lterm);
        wipsImsC32uInput.setTpiPfKey(pfKey);
        return (S01i0TransactionOutput)this.c32uS01i0Transaction.executeWipsImsTransaction(
                wipsImsC32uInput,
                this.imsConversation);
    }

    private LumpSumApproveOrRejectResponse approveOrRejectWarningResponse(
            final String actionTaken,
            final String errorMessage) {
        LumpSumApproveOrRejectResponse lumpSumApproveOrRejectResponse;
        lumpSumApproveOrRejectResponse = new LumpSumApproveOrRejectResponse();
        if (errorMessage.contains("MSG-5875")) {
            lumpSumApproveOrRejectResponse.populateErrorDetails(errorMessage);
        } else {
            final List<ApproveWarningHandler> warningMessages =
                    createWarningMessages(actionTaken, errorMessage);
            lumpSumApproveOrRejectResponse.setWarningMessagesList(warningMessages);
            lumpSumApproveOrRejectResponse.setApprovalWarningFlag(true);
        }
        return lumpSumApproveOrRejectResponse;
    }

    private List<ApproveWarningHandler> createWarningMessages(final String actionTaken,
            final String errorMessage) {
        final List<ApproveWarningHandler> warningMessages =
                new ArrayList<ApproveWarningHandler>();
        final ApproveWarningHandler handler = new ApproveWarningHandler();
        handler.setErrorMessage(errorMessage);
        handler.setMessageCode(errorMessage.trim().substring(0, 8));
        handler.setActionTaken(actionTaken);
        warningMessages.add(handler);
        return warningMessages;
    }

    private short pfKey(final String action) {
        short pfkey = WipsConstant.PFKEY0;
        if (WipsConstant.APPROVE.equals(action) || WipsConstant.CONFIRM.equals(action)) {
            pfkey = WipsConstant.PFKEY13;
        } else {
            pfkey = WipsConstant.PFKEY17;
        }
        return pfkey;
    }

    private C32uTransactionOutput executeS01OToC32uTransaction(final String lterm)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        final WipsImsInput input = createS01OInput(lterm);
        return (C32uTransactionOutput)this.s01oc32uTransaction
                .executeWipsImsTransaction(input, this.imsConversation);
    }

    private WipsImsInput createS01OInput(final String lterm) {
        final WipsImsInput input = new WipsImsInput();
        input.setLterm(lterm);
        input.setTpiPfKey(WipsConstant.PFKEY13);
        return input;
    }

}
