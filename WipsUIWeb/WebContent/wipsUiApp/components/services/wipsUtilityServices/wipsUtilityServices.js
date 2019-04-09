'use strict';

angular.module('WipsUiApp.ComponentsModule').
	service('WipsUtilityServices',['$state','User',function($state,User){
	
	this.redirectiveListPage = function(userInfo){
		if(User.userInformation.pendingApprovals.length == 0){
			this.ltermToken = User.userInformation.ltermToken;
			if(!userInfo.pendingApprovals.length==0){
			this.categoryCode = userInfo.pendingApprovals[0].categoryCode;
			}
		}else{
			this.ltermToken = User.userInformation.ltermToken;
			this.categoryCode = userInfo.pendingApprovals[0].categoryCode;
		}
		if(userInfo.pendingApprovals.length==1){
			
			User.setCategoryCode(userInfo.pendingApprovals[0].categoryCode);
			var param = {
					categoryCode: this.categoryCode,
					ltermToken:this.ltermToken
			};
				if(userInfo.pendingApprovals[0].categoryCode == '3'){
						$state.go('price-claim-pending-approval',param,{reload: true});
				}else if(userInfo.pendingApprovals[0].categoryCode == '25'){
						$state.go('atp-pending-approval',param,{reload: true});
				}else if(userInfo.pendingApprovals[0].categoryCode == '102'){
						$state.go('lump-sum-pending-approval',param,{reload: true});
					}
				}else{
					$state.go('home-atp-pending-approval');
				}
	}
	
	}]);