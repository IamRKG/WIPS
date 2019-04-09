package com.ford.purchasing.wips.domain.atp.connector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsConversation;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionOutput;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;
import com.ford.purchasing.wips.domain.layer.PendingApprItemsHelper;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;
import com.ford.purchasing.wips.domain.layer.exception.WipsImsInterfaceException;

public class Q01xTransaction extends WipsTransactionConnector {

    private static final String Q01X_TRANSACTION = "AAIMQ01X";
    private static final String CLASS_NAME = Q01xTransaction.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private Category expectedCategory;
    private String currentPageNo;

    @Override
    public void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) throws ConnectorException {
        final Q01xTransactionInput q01xInputBean = (Q01xTransactionInput)wipsImsInput;
        inputDataArea.put("TPI-IOINDIC", q01xInputBean.getTpiIoindic());
        inputDataArea.put("TPI-PFKEY", q01xInputBean.getTpiPfKey());
        inputDataArea.put("TPI-PAGENO", this.currentPageNo);
        inputDataArea.put("TPI-SELNO", q01xInputBean.getTpiSelNo());
        inputDataArea.put("TPI-SELPRE", q01xInputBean.getTpiSelPre());
        inputDataArea.put("TPI-SELBAS", q01xInputBean.getTpiSelBas());
        inputDataArea.put("TPI-SELSUF", q01xInputBean.getTpiSelSuf());
        inputDataArea.put("TPI-SELSUP", q01xInputBean.getTpiSelUp());
        inputDataArea.put("TPI-SELCAUS", q01xInputBean.getTpiSelCaus());
        inputDataArea.put("TPI-SELAGE", q01xInputBean.getTpiSelAge());
        inputDataArea.put("TPI-SELRESP", q01xInputBean.getTpiSelResp());
        inputDataArea.put("TPI-SELPROG", q01xInputBean.getTpiSelProg());
        inputDataArea.put("TPI-SELNUM", q01xInputBean.getTpiSelNum());
        inputDataArea.put("TPI-SELREAD", q01xInputBean.getTpiRead());
        inputDataArea.put("TPI-SELCAT", q01xInputBean.getTpiCat());
        inputDataArea.put("TPI-SELENG", q01xInputBean.getTpiEng());
        final List<DataArea> ar730InputDocIndList =
                TransactionOutputUtil.getArray(inputDataArea, "TPI-BUFFER-SEGLOOP");
        DataArea parameterList;
        for (final Iterator<DataArea> i = ar730InputDocIndList.iterator(); i.hasNext();) {
            parameterList = i.next();
            parameterList.put("TPI-SELECT", q01xInputBean.getTpiSelect());
        }
        this.expectedCategory = Category.getCategory(q01xInputBean.getTpiSelNo());
    }

    @Override
    public WipsImsOutput executeWipsImsTransaction(final WipsImsInput wipsImsInput,
            final ImsConversation imsConversation) throws WipsImsInterfaceException,
            WipsImsTransactionException {
        Q01xTransactionOutput q01xTransactionOutputBean = null;
        final List<WipsPendingApprovalOutput> pendingApprItemsList =
                new ArrayList<WipsPendingApprovalOutput>();
        this.currentPageNo = wipsImsInput.getTpiPageNo();
        do {
            q01xTransactionOutputBean =
                    (Q01xTransactionOutput)super.executeWipsImsTransaction(wipsImsInput,
                            imsConversation);
            pendingApprItemsList
                    .addAll(q01xTransactionOutputBean.getPendingApprItemsList());
        } while (q01xTransactionOutputBean.getContainMorePages()
                .contains(WipsTransactionConstant.MORE));
        q01xTransactionOutputBean.setPendingApprItemsList(pendingApprItemsList);
        return q01xTransactionOutputBean;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        Q01xTransactionOutput q01xTransactionOutput = null;
        final String rawOutputQ01X = imsOperation.getRawOutput();
        log.info("Q01X Output" + imsOperation.getRawOutput());
        final DataArea outputDataArea = (DataArea)imsOperation.getOutputRecord();
        final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PGMNAME");
        if (WipsTransactionConstant.Q01X_TRANSACTION_NAME.contains(transactionName)) {
            if (!rawOutputQ01X.substring(62, 113).contains("MSG-3204")) {
                q01xTransactionOutput = new Q01xTransactionOutput();
                q01xTransactionOutput.setErrorFlag(false);
                final PendingApprItemsHelper helper = new PendingApprItemsHelper();
                q01xTransactionOutput.setPendingApprItemsList(helper
                        .populatePendingApprItems(outputDataArea, this.expectedCategory));
                this.currentPageNo = TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-PAGENO");
                q01xTransactionOutput.setPageNo(this.currentPageNo);
                q01xTransactionOutput.setContainMorePages(TransactionOutputUtil
                        .getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE"));
            } else {
                throw new WipsImsTransactionException(getImsTransactionName(),
                        rawOutputQ01X.substring(62, 113));
            }
        } else {
            throw new WipsImsTransactionException(getImsTransactionName(),
                    rawOutputQ01X.substring(108, 184));
        }
        return q01xTransactionOutput;
    }

    @Override
    protected String getImsTransactionName() {
        return Q01X_TRANSACTION;
    }
}
