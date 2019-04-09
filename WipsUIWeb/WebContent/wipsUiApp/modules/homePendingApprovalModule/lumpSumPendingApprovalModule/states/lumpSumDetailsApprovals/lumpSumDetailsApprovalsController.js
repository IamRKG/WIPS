'use strict';

angular.module('WipsUiApp.LumpSumPendingApprovalModule')
    .controller('LumpSumDetailsApprovalsController', ['$scope', '$state', 'User', '$stateParams', 'resolvedLumpSumApproval', 'LumpSumDetailsApprovalsPrototype', 'WcAlertConsoleService', 'SessionServices', '_', '$window', 'LumpSumDetailsApprovalServices', 'ViewAttachmentDetailsServices', 'confirmModalService', 'atpApproveModalService', 'ATPApproveServices',
        function ($scope, $state, User, $stateParams, resolvedLumpSumApproval, LumpSumDetailsApprovalsPrototype, WcAlertConsoleService, SessionServices, _, $window, LumpSumDetailsApprovalServices, ViewAttachmentDetailsServices, confirmModalService, atpApproveModalService, ATPApproveServices) {

            /*TODO:Remove if anything not used*/
            this.lumpSumReadOnlyFlag = resolvedLumpSumApproval.lumpSumReadOnlyFlag;
            this.additionalLumpSumInformation = new LumpSumDetailsApprovalsPrototype(resolvedLumpSumApproval.additionalLumpSumInformation);
            this.jobDetail = User.currentjobDetail;
            this.lumpSumInformation = new LumpSumDetailsApprovalsPrototype(resolvedLumpSumApproval.lumpSumInformation);
            this.lumpSumRemarks = new LumpSumDetailsApprovalsPrototype(resolvedLumpSumApproval.lumpSumRemarks);
            this.availableAmendments = resolvedLumpSumApproval.availableAmendments;
            this.securityInformation = new LumpSumDetailsApprovalsPrototype(resolvedLumpSumApproval.securityInformation);
            this.attachmentsErrorFlag = resolvedLumpSumApproval.attachmentsErrorFlag;
            this.workTaskNoValue = this.additionalLumpSumInformation.workTaskNumber;
            this.selectedClassificationCode = this.additionalLumpSumInformation.selectedClassification;
            this.selectedGSAAudit = this.lumpSumInformation.selectedGsaAudit;
            this.lonTermCostVal = this.additionalLumpSumInformation.longTermCost;
            this.lonTermCostCur = this.additionalLumpSumInformation.longTermCurrency;
            this.shortTermCostVal = this.additionalLumpSumInformation.shortTermCost;
            this.shortTermCostCur = this.additionalLumpSumInformation.shortTermCurrency;
            this.longTermCost = this.lonTermCostVal;
            this.shortTermCost = this.shortTermCostVal;
            this.totalAmount = this.lumpSumInformation.totalAmount;
            this.lumpsumNumber = this.lumpSumInformation.lumpsumNumber;
            this.getGsaAmount = function (gsaAmount){
            	if(gsaAmount == undefined || gsaAmount == '' || gsaAmount == '') {
            		return ;
            	} else {
            		return this.toggled?(this.currencyConversion(gsaAmount.amount)+gsaAmount.sign):(gsaAmount.amount+gsaAmount.sign);
            	}
            };
            this.gsaAmount = this.getGsaAmount(this.lumpSumInformation.gsaAmount);
            this.prePayment=this.additionalLumpSumInformation.prePayment;
            /*TODO: Test and remove "isConfirm" if not required*/
            this.isConfirm = {
                defaultTrue: true
            };
            if (this.additionalLumpSumInformation.hasPaymentConfirmed == true) {
                this.confirmPayment = true;
            }
            else {
                this.confirmPayment = false;
                this.confirmLSFlagError = true;
            }

            //TODO : Remove this once test all conditions
            if (this.attachmentsErrorFlag) {
                this.attachmentsErrorMessage = resolvedLumpSumApproval.attachmentsErrorMessage;
            }
            var lumpSumRemarksListName = _.findWhere(this.lumpSumRemarks.approvalDetails, {jobCode: User.currentjobDetail.jobCode});
            this.selectedApprovalDetailsNotesInitialResponse = lumpSumRemarksListName.remarks;
            this.selectedApprovalDetailsNotes = lumpSumRemarksListName.remarks;

            this.updateCurrentJobCodeRemarks = function (lumpSumRemarksApprovalDetails) {
                var lumpSumRemarksListName = _.findWhere(lumpSumRemarksApprovalDetails, {jobCode: User.currentjobDetail.jobCode});
                this.selectedApprovalDetailsNotesSavedResponse = lumpSumRemarksListName.remarks;
                this.selectedApprovalDetailsNotes = lumpSumRemarksListName.remarks;
                return this.selectedApprovalDetailsNotes;
            }
            this.getLumpSumDetailsConfirmApproval = function () {
                this.isConfirm.defaultTrue = false;
                return LumpSumDetailsApprovalServices.getConfirm(this.param()).then(angular.bind(this, function (errorResponse) {
                    this.confirmPayment = "true";
                    var confirmLSFlagError = errorResponse.errorFlag;
                    this.confirmLSFlagError = confirmLSFlagError;
                    if (this.confirmLSFlagError == true) {
                        WcAlertConsoleService.addMessage({
                            /*TODO:Remove hard code value in message*/
                            message: 'Unable to process your request currently. Please contact system administrator',
                            type: 'danger',
                            multiple: false
                        });
                    }
                    else {
                        this.enableButton();

                    }
                }));
            };


            this.isGroup1Save = false;
            this.isGroup2Save = false;
            this.isGroup3Save = false;
            this.gsaAmountSave = function () {
                this.gsaAmountParam = this.gsaAmount;
                this.isGroup1Save = true;
            }
            this.gsaAuditSave = function () {
                this.selectedGSAAuditParam = this.selectedGSAAudit;
                this.isGroup1Save = true;
            }
            this.shortTermCostSave = function () {
                this.shortTermCostParam = this.shortTermCost;
                this.isGroup2Save = true;
            }
            this.longTermCostSave = function () {
                this.longTermCostParam = this.longTermCost;
                this.isGroup2Save = true;
            }
            this.workTaskSave = function () {
                this.workTaskNoValueParam = this.workTaskNoValue;
                this.isGroup2Save = true;
            }
            this.classificationSave = function () {
                this.selectedClassificationCodeParam = this.selectedClassificationCode;
                this.isGroup2Save = true;
            }

            this.getSaveDetails = function (actionParam) {
                this.validateWorkTaskNo();
                this.gsaAmountPattern();
                this.validateRemarks(actionParam);
                this.param();
                if ($scope.lumpSumDetailsApprovals.$valid) {
                    this.isCheckRemarksEdit();
                    this.toggled = false;
                    return LumpSumDetailsApprovalServices.getSavedDetails(this.param()).then(angular.bind(this, function (saveResponse) {
                        if (!saveResponse.errorFlag) {
                            this.lonTermCostVal = saveResponse.additionalLumpSumInformation.longTermCost;
                            this.shortTermCostVal = saveResponse.additionalLumpSumInformation.shortTermCost;
                            this.longTermCost = saveResponse.additionalLumpSumInformation.longTermCost;
                            this.shortTermCost = saveResponse.additionalLumpSumInformation.shortTermCost;
                            this.gsaAmount = this.getGsaAmount(saveResponse.lumpSumInformation.gsaAmount);
                            this.workTaskNoValue = saveResponse.additionalLumpSumInformation.workTaskNumber;
                            this.prePayment = saveResponse.additionalLumpSumInformation.prePayment;
                            this.lumpSumInformation.gsaAmount.amount=saveResponse.lumpSumInformation.gsaAmount.amount;
                            if(saveResponse.lumpSumRemarks!=undefined){
                                this.updateCurrentJobCodeRemarks(saveResponse.lumpSumRemarks.approvalDetails);
                            }
                            this.securityInformation = saveResponse.securityInformation;
                            this.selectedGSAAudit = saveResponse.lumpSumInformation.selectedGsaAudit;
                            WcAlertConsoleService.addMessage({
                                /*TODO:Remove hard code value in message*/
                                message: 'Saved!',
                                type: 'success',
                                multiple: false
                            });
                        } else {
                            this.lonTermCostVal = this.additionalLumpSumInformation.longTermCost;
                            this.shortTermCostVal = this.additionalLumpSumInformation.shortTermCost;
                            this.longTermCost = this.additionalLumpSumInformation.longTermCost;
                            this.shortTermCost = this.additionalLumpSumInformation.shortTermCost;
                            this.gsaAmount = this.getGsaAmount(this.lumpSumInformation.gsaAmount);
                            this.workTaskNoValue = this.additionalLumpSumInformation.workTaskNumber;
                            this.prePayment = this.additionalLumpSumInformation.prePayment;
                            this.selectedClassificationCode = this.additionalLumpSumInformation.selectedClassification;
                            this.selectedGSAAudit = this.lumpSumInformation.selectedGsaAudit;
                            this.isGroup1Save = false;
                            this.isGroup2Save = false;
                            this.isGroup3Save = false;
                            this.gsaAmountParam = null;
                            this.selectedGSAAuditParam = null;
                            this.shortTermCostParam= null;
                            this.longTermCostParam= null;
                            this.workTaskNoValueParam= null;
                            this.selectedClassificationCodeParam= null;
                            WcAlertConsoleService.addMessage({
                                message: saveResponse.errorMessage,
                                type: 'success',
                                multiple: false
                            });
                        }
                        return saveResponse;
                    }));

                } else {
                    this.isFormSubmitted = true;
                }

            };
            this.saveAndApproveReject = function (actionParam) {
                this.validateWorkTaskNo();
                this.gsaAmountPattern();
                this.validateRemarks(actionParam);
                if ($scope.lumpSumDetailsApprovals.$valid) {
                    this.isCheckRemarksEdit();
                    this.toggled = false;
                    return confirmModalService.openConfirmModal(actionParam).then(angular.bind(this, function () {
                        this.param().actionTaken = actionParam;
                        return LumpSumDetailsApprovalServices.saveAndApproveReject(this.param()).then(angular.bind(this, function (saveAndApproveRejectResponse) {
                            if (saveAndApproveRejectResponse.approvalWarningFlag == true) {
                                return atpApproveModalService.openATPApproveModal(saveAndApproveRejectResponse).then(angular.bind(this, function (approveOrRejectAction) {
                                    param.actionTaken = approveOrRejectAction;
                                    param.ltermToken = User.userInformation.ltermToken;

                                    return LumpSumDetailsApprovalServices.saveAndApproveReject(param).then(angular.bind(this, function (approveOrRejectActionTakenResponse) {
                                        this.navigateToLumpsumApprovalReject(approveOrRejectActionTakenResponse);

                                    }));

                                }));

                            }
                            else {

                                this.navigateToLumpsumApprovalReject(saveAndApproveRejectResponse);
                            }

                        }));
                    }));
                } else {
                    this.isFormSubmitted = true;
                }

            };

            this.isCheckRemarksEdit = function () {
                if (this.selectedApprovalDetailsNotes == this.selectedApprovalDetailsNotesInitialResponse) {
                    this.selectedApprovalDetailsNotesParam = [];
                    this.isGroup3Save = false;
                }
                else if(this.selectedApprovalDetailsNotes != this.selectedApprovalDetailsNotesSavedResponse || this.selectedApprovalDetailsNotesSavedResponse!=this.selectedApprovalDetailsNotesInitialResponse) {

                    this.selectedApprovalDetailsNotesParam = this.selectedApprovalDetailsNotes;
                    this.isGroup3Save = true;
                }
            };

            var param = {};

            this.param = function () {
                param.ltermToken = User.userInformation.ltermToken;
                param.selectedGsaAudit = this.selectedGSAAuditParam;
                param.gsaAmount = this.gsaAmountParam;
                param.selectedClassification = this.selectedClassificationCodeParam;
                param.workTaskNumber = this.workTaskNoValueParam;
                param.shortTermCost = this.shortTermCostParam;
                param.longTermCost = this.longTermCostParam;
                param.userRemarks = this.selectedApprovalDetailsNotesParam;
                param.group1Save = this.isGroup1Save;
                param.group2Save = this.isGroup2Save;
                param.group3Save = this.isGroup3Save;
                param.lumpSumNumber = this.lumpsumNumber;
                param.totalAmount = this.totalAmount.amount;
                return param;
            }

            this.readOnlyFunc = function () {
                this.param = {
                    ltermToken :this.param().ltermToken,
                    lumpSumNumber :this.param().lumpSumNumber,
                    defaultAmendment : this.lumpSumInformation.currentAmendment
                };
                $state.go('lump-sum-details-approvals', this.param);
            };
            angular.forEach(this.additionalLumpSumInformation.classification, function (classification, index) {
                classification.codeAnddescription = classification.code + " " + classification.description;
            });

            angular.forEach(this.lumpSumInformation.gsaAudit, function (classification, index) {
                classification.codeAnddescription = classification.code + '-' + classification.description;
            });

            /*TODO:Remove if anything not used*/
            this.models = this.additionalLumpSumInformation.models.join(', ');

            this.lumpsumBack = function () {
                this.param = {
                    ltermToken :this.param().ltermToken,
                    categoryCode : User.currentCategoryCode
                };

                $state.get('lump-sum-pending-approval').data.clickedBackButtonCallNewData = "";
                $state.go('lump-sum-pending-approval', this.param);

            };

            this.viewAttachmentDetails = function (attachmentDetails) {
                this.attachmentId = _.findWhere(this.lumpSumInformation.attachmentDetails, {id: attachmentDetails.id})
                /*TODO:Move parm object in to this.param() function*/
                this.paramAttachment = {
                    attachmentId: this.attachmentId.id,
                    ltermToken: User.userInformation.ltermToken
                };

                ViewAttachmentDetailsServices.getUserSession(this.paramAttachment.ltermToken).then(angular.bind(this, function (response) {
                    var urlEndCodeAttachmentId = encodeURIComponent(this.paramAttachment.attachmentId);
                    var urlEndCodeLterm = encodeURIComponent(this.paramAttachment.ltermToken);
                    this.fileUrl = '/WipsWeb/Attachment/ViewAttachment?lterm=' + urlEndCodeLterm + '&attachmentId=' + urlEndCodeAttachmentId;
                    $window.open(this.fileUrl);
                    return response;
                }));


            };

            this.navigateToLumpsumApprovalReject = function (approveRejectResponse) {
                $state.get('lump-sum-pending-approval').data.clickedBackButtonCallNewData = approveRejectResponse;
                $state.go('lump-sum-pending-approval');
            }

            this.notRequiredWorkTaskNo = function () {
                $scope.lumpSumDetailsApprovals.workTaskNo.$setValidity('required', true);
            }


            this.validateWorkTaskNo = function () {
                if (this.workTaskNoValue == '' || this.workTaskNoValue == undefined) {
                    this.notRequiredWorkTaskNo();
                } else {
                    if (this.workTaskNoValue.charAt(0) == '?') {
                        $scope.lumpSumDetailsApprovals.workTaskNo.$setValidity('required', false);
                    } else {
                        this.notRequiredWorkTaskNo();
                    }
                }
            };
            this.validateRemarks = function (actionParam) {
                if ( actionParam === "Reject") {
                    if (this.selectedApprovalDetailsNotes.toString().toUpperCase() === this.selectedApprovalDetailsNotesInitialResponse.toString().toUpperCase() || this.selectedApprovalDetailsNotes == '') {
                        $scope.lumpSumDetailsApprovals.lumpSumVersion.$setValidity('required', false);
                        this.isInValidOpenAccordion();
                    }
                    else{

                        $scope.lumpSumDetailsApprovals.lumpSumVersion.$setValidity('required', true);
                    }

                }
                else {
                    $scope.lumpSumDetailsApprovals.lumpSumVersion.$setValidity('required', true);
                }
            };
            /*TODO: Need to check usage of the dom function*/
            this.enableButton = function () {
                angular.element(document.getElementById('confirm'))[0].disabled = true;

            };
            /*TODO Move all patterns in to controller*/
            this.gsaAmountPattern = function(){
                if(this.securityInformation.globalSupplierAuditEditable == true){
                    return /^(?=.*[1-9])\d{0,11}(?:\.\d{1,2})?[+-]{0,1}\s*$/

                }else{
                    $scope.lumpSumDetailsApprovals.gsaAmount.$setValidity('pattern', true);
                    return /^(?=.*[1-9])\d{0,11}(?:\.\d{1,2})?[+-]{0,1}\s*$/

                };
            };

            /*Accordion open/close for textarea validation*/

            this.isInValidOpenAccordion = function() {
                this.isRemarksOpen = [];
                angular.forEach(this.lumpSumRemarks.approvalDetails, angular.bind(this, function (jobCodeList, index) {
                    if(jobCodeList.jobCode == User.currentjobDetail.jobCode){
                        this.isRemarksOpen[index] = true;
                    }else{
                        this.isRemarksOpen[index] = false;
                    }

                }));
            }

            this.isInValidOpenAccordion();

			this.currencyToggle = function(currency){
                this.exchangeRate = this.lumpSumInformation.exchangeRate;
                 if(currency != 'USD'){
                 	this.toggled = false;
                 	this.gsaAmount = this.gsaAmountValue==undefined ? this.lumpSumInformation.gsaAmount.amount+this.lumpSumInformation.gsaAmount.sign:this.gsaAmountValue; 
                 }else{
                 	this.currencyUSD = 'USD';
                 	if(!this.toggled){
                 		this.gsaAmountValue = this.gsaAmount; 
                 		this.gsaAmount = this.currencyConversion(this.gsaAmountValue)+this.lumpSumInformation.gsaAmount.sign;
                 	}
                    this.toggled = true;
                 }
             };

             
            this.convertAmount = function (object){
            	if(object == undefined || object == '' || object.amount == '') {
            		return ;
            	} else {
            		return this.currencyConversion(object.amount) +' '+ object.sign +' '+ this.currencyUSD;
            	}
            };
            
            this.currencyConversion = function (amount){
            	var convertedAmount = parseFloat(amount) / this.exchangeRate;
            	return convertedAmount.toString().match(/^\d+(?:\.\d{0,2})?/);
            };
            
        }]);
