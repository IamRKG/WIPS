'use strict';

angular.module('WipsUiApp.ComponentsModule')
.directive('wipsReadUnReadFlag',['$timeout','LumpSumPendingApprovalServices',function($timeout,LumpSumPendingApprovalServices){
    return {
        restrict: 'A',
        link: function (scope, element ) {
            if(scope.lumpSumPendingApprovalController){
                var pendingList = scope.lumpSumPendingApprovalController.lumpSumPendingApprovalTable
            }else if(scope.atpPendingApprovalController){
                var pendingList = scope.atpPendingApprovalController.atpPendingApprovalTable
            }else{
                var pendingList = scope.priceClaimPendingApprovalController.priceClaimPendingApprovalTable
            }
            $timeout(function(){
                for(var i=0; i<pendingList.length; i++){
                    var  readOrUnreadFlag = pendingList[i].readOrUnreadFlag;
                   if(readOrUnreadFlag == "0"){
                       element.find("tbody tr").eq(i).find("td").addClass('read-flag');
                   }else {
                       element.find("tbody tr").eq(i).find("td").addClass('un-read-flag');
                   }
                }
            })
        }
     };

}]);