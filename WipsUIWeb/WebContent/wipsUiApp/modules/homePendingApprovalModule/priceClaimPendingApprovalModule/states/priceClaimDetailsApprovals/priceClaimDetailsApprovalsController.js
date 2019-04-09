'use strict';

angular.module('WipsUiApp.priceClaimPendingApprovalModule')
    .controller('PriceClaimDetailsApprovalsController', ['$scope', '$state', 'User', '$stateParams', 'resolvedPriceClaimApproval','priceClaimDetailsApprovalServices',  'confirmModalService','atpApproveModalService' ,'WcAlertConsoleService', '_',
        function ($scope, $state, User, $stateParams, resolvedPriceClaimApproval,priceClaimDetailsApprovalServices, confirmModalService, atpApproveModalService, WcAlertConsoleService, _) {
    	
    	
	    	this.lumpsumBack = function () {
	            this.param = {
	                ltermToken :User.userInformation.ltermToken,
	                categoryCode : User.currentCategoryCode
	            };
                var approvalState = $state.get('price-claim-pending-approval');
                approvalState.data.clickedBackButtonCallNewData = "";
	            //$state.get('price-claim-pending-approval').data.clickedBackButtonCallNewData = "";
	            $state.go('price-claim-pending-approval', this.param);
	        };

            this.selectedClaimYear = 'All'
            var reloadParam = {}

            this.reloadParam = function () {
                reloadParam.ltermToken = this.param().ltermToken;
                reloadParam.priceClaimNumber = this.param().priceClaimNumber;
                reloadParam.supplier = this.pcsData.pcsSummaryInformation.supplier;
                reloadParam.selectedPM = this.pmValue();
                reloadParam.selectedYear = this.claimYearValue();
                return reloadParam;
                
            }

            var all = 'All';
            var asterisk = '*';

            this.pmValue = function () {
                if(this.selectedPMValue  != all){
                    return this.selectedPMValue.split(" - ",1).toString();;
                }else{
                      return asterisk+this.selectedPMValue
                }
            }

            this.claimYearValue = function () {
                if(this.selectedClaimYear != all){
                    return this.selectedClaimYear;
                }else{
                    return asterisk+this.selectedClaimYear;
                }
            }

            this.reloadFinancialImpactInformation = function () {
                priceClaimDetailsApprovalServices.getReloadFinancialImpactInformation(this.reloadParam()).then(angular.bind(this,function (response) {
                    this.pcsDataFinance = response.financialImpactInformation;
                    return response;
                }));
            }


            this.pcsData = resolvedPriceClaimApproval;
            this.pcsDataFinance=resolvedPriceClaimApproval.financialImpactInformation;
            this.pcsDetails = resolvedPriceClaimApproval.pcsSummaryInformation;
            this.priceClaimNumber=$stateParams.priceClaimNumber;
            this.jobDetail = User.currentjobDetail;
            this.priceClaimRemarks= this.pcsData.documentRemarks;
            var priceClaimListName =  _.findWhere(this.pcsData.priceClaimRemarks, {jobCode: User.currentjobDetail.jobCode});
            this.selectedApprovalDetailsNotesInitialResponse = priceClaimListName.remarks;
            this.selectedApprovalDetailsNotes = priceClaimListName.remarks;
            this.selectedPMValue = resolvedPriceClaimApproval.financialImpactInformation.purchaseManager[0];
            var param = {};

            this.param = function () {
                param.ltermToken = User.userInformation.ltermToken;
                param.remarks = this.selectedApprovalDetailsNotes;
                param.priceClaimNumber = this.priceClaimNumber;
                return param;
            }
            this.updateCurrentJobCodeRemarks = function (claimApprovalDetails) {
            	 var priceClaimListName =  _.findWhere(claimApprovalDetails, {jobCode: User.currentjobDetail.jobCode});
                 this.selectedApprovalDetailsNotesInitialResponse = priceClaimListName.remarks;
                 this.selectedApprovalDetailsNotes = priceClaimListName.remarks;
                return this.selectedApprovalDetailsNotes;
            }
        	this.getSaveDetails = function (actionParam) {
        		this.param();
                    return priceClaimDetailsApprovalServices.getSavedDetails(this.param()).then(angular.bind(this, function (saveResponse) {
                        if (!saveResponse.errorFlag) {
                        	 if(saveResponse.priceClaimRemarks!=undefined){
                                 this.updateCurrentJobCodeRemarks(saveResponse.priceClaimRemarks);
                             }
                            WcAlertConsoleService.addMessage({
                                /*TODO:Remove hard code value in message*/
                                message: 'Saved!',
                                type: 'success',
                                multiple: false
                            });
                        } else {
                            WcAlertConsoleService.addMessage({
                                message: saveResponse.errorMessage,
                                type: 'success',
                                multiple: false
                            });
                        }
                        ;
                        return saveResponse;
                    }));
            };

            this.getApproveAndRejectDetails = function (actionParam) {
            	this.isCheckRemarksEdit();
        		this.param();
                     return confirmModalService.openConfirmModal(actionParam).then(angular.bind(this, function () {
                         this.param().actionTaken = actionParam;
                         this.param().remarksEditFlag = this.isRemarksEdit;
                         return priceClaimDetailsApprovalServices.approveAndRejectDetails(this.param()).then(angular.bind(this, function (saveAndApproveRejectResponse) {
                             if (saveAndApproveRejectResponse.approvalWarningFlag == true) {
                                 return atpApproveModalService.openATPApproveModal(saveAndApproveRejectResponse).then(angular.bind(this, function (approveOrRejectAction) {
                                     param.actionTaken = approveOrRejectAction;
                                     param.ltermToken = User.userInformation.ltermToken;
                                     return priceClaimDetailsApprovalServices.approveAndRejectDetails(param).then(angular.bind(this, function (approveOrRejectActionTakenResponse) {
                                         this.navigateToPriceClaimApprovalReject(approveOrRejectActionTakenResponse);
                                     }));
                                 }));
                             }
                             else {
                                 this.navigateToPriceClaimApprovalReject(saveAndApproveRejectResponse);
                             }
                         }));
                     }));
             };
             this.isCheckRemarksEdit = function () {
                 if (this.selectedApprovalDetailsNotes == this.selectedApprovalDetailsNotesInitialResponse) {
                     this.selectedApprovalDetailsNotesParam = [];
                     this.isRemarksEdit = false;
                 }
                 else if(this.selectedApprovalDetailsNotes != this.selectedApprovalDetailsNotesSavedResponse || this.selectedApprovalDetailsNotesSavedResponse!=this.selectedApprovalDetailsNotesInitialResponse) {
                     this.selectedApprovalDetailsNotesParam = this.selectedApprovalDetailsNotes;
                     this.isRemarksEdit = true;
                 }
             };
             this.navigateToPriceClaimApprovalReject = function (approveRejectResponse) {
                 $state.get('price-claim-pending-approval').data.clickedBackButtonCallNewData = approveRejectResponse;
                 $state.go('price-claim-pending-approval');
             }

            /*Accordion open/close for textarea validation*/

            this.isInValidOpenAccordion = function() {
                this.isRemarksOpen = [];
                angular.forEach(this.pcsData.priceClaimRemarks, angular.bind(this, function (jobCodeList, index) {
                    if(jobCodeList.jobCode == User.currentjobDetail.jobCode){
                        this.isRemarksOpen[index] = true;
                    }else{
                        this.isRemarksOpen[index] = false;
                    }

                }));
            }

            this.isInValidOpenAccordion();

            this.isOpen = [];
            this.isOpen[0] = true;
        }]);
