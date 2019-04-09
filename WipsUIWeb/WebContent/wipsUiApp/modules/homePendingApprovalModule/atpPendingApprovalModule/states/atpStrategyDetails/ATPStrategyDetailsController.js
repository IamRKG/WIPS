'use strict';

angular.module('WipsUiApp.ATPPendingApprovalModule')
	.controller('ATPStrategyDetailsController', ['$scope','resolvedATPStrategyDetails','AtpStrategyDetailsPrototype','$state','User',
		function($scope,resolvedATPStrategyDetails,AtpStrategyDetailsPrototype,$state,User) {
		this.atpStrategyDetails = new AtpStrategyDetailsPrototype(resolvedATPStrategyDetails.strategyOutput);
		this.jobDetail = User.currentjobDetail;
		this.getPrevious = function(){
			var param = {
					ltermToken:User.userInformation.ltermToken,
					atpNumber: this.atpStrategyDetails.atpNumber
			};
			$state.go('atp-recap',param);
		};	
	}]);
