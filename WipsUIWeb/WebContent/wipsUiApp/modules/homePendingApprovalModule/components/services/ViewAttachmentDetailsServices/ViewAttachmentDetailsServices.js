'use strict';
angular.module('WipsUiApp.HomePendingApproval.ComponentsModule')
.service('ViewAttachmentDetailsServices',['WcHttpEndpointPrototype','SessionServices','WcAlertConsoleService','$q', '$translate', '$state','User','$window','WcHttpRequestService','$http','$location','WipsHttpStatusCodesConstant','WipsHttpStatusCodesServices',function(WcHttpEndpointPrototype,SessionServices,WcAlertConsoleService,$q,$translate,$state,User,$window, WcHttpRequestService,$http,$location,WipsHttpStatusCodesConstant,WipsHttpStatusCodesServices){

    this.viewAttachmentDetails = new WcHttpEndpointPrototype('Attachment/ViewAttachment');
    
    this.userSession = new WcHttpEndpointPrototype('UserSession');

    this.downloadFile = function(response){
        var file = new Blob([response],{type: response.contentType});
        this.url =  ($window.URL) ? $window.URL.createObjectURL(file) : $window.webkitURL.createObjectURL(file);
        $window.open(this.url);
        $window.URL.revokeObjectURL(this.url);
    };


    this.getviewAttachmentDetails = function(attachmentId){
        var urlEndCode = encodeURIComponent(attachmentId);
        this.httpHeaderresponse =  WcHttpRequestService.addResponseInterceptor(function(httpHeaderresponse){
            if(!httpHeaderresponse.headers()['content-type'] || !httpHeaderresponse.headers()['content-disposition']) {
                return undefined;
            }else{
                httpHeaderresponse.data.contentType = httpHeaderresponse.headers()['content-type'];
                httpHeaderresponse.data.contentDisposition = httpHeaderresponse.headers()['content-disposition'];
         }
            return httpHeaderresponse;
        });
        return this.viewAttachmentDetails.subRoute(urlEndCode).get({ responseType : 'arraybuffer'}).then(angular.bind(this,function(response){
            if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
                this.downloadFile(response);
                return response;
            }else{
                WcAlertConsoleService.addMessage({
                    message: $translate.instant(response.errorMessage),
                    type: 'danger',
                    multiple: false
                });
                return $q.reject(response);
            }
        }),function(failure){
            if(failure.status == WipsHttpStatusCodesConstant.conflict409){
                var param = {
                    categoryCode: User.currentCategoryCode,
                    ltermToken:User.userInformation.ltermToken
                };
                $state.go('lump-sum-pending-approval',param,{reload: true});
                WcAlertConsoleService.addMessage({
                    message:$translate.instant(failure.data.errorMessage),
                    type: 'danger',
                    multiple: false
                });
            }else{
                WipsHttpStatusCodesServices.getWipsHttpStatusCodes(failure)
            }
            return $q.reject(failure);
        });

    }
    
    
    this.getUserSession = function(ltermToken){
        var urlEndCode = encodeURIComponent(ltermToken);
        return this.userSession.subRoute(urlEndCode).get().then(angular.bind(this,function(response){
            if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
                return response;
            }else{
                WcAlertConsoleService.addMessage({
                    message: $translate.instant(response.errorMessage),
                    type: 'danger',
                    multiple: false
                });
                return $q.reject(response);
            }
        }),function(failure){
            if(failure.status == WipsHttpStatusCodesConstant.conflict409){
                var param = {
                    categoryCode: User.currentCategoryCode,
                    ltermToken:User.userInformation.ltermToken
                };
                $state.go('lump-sum-pending-approval',param,{reload: true});
                WcAlertConsoleService.addMessage({
                    message:$translate.instant(failure.data.errorMessage),
                    type: 'danger',
                    multiple: false
                });
            }else{
                WipsHttpStatusCodesServices.getWipsHttpStatusCodes(failure)
            }
            return $q.reject(failure);
        });

    }


}]);
