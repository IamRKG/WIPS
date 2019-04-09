'use strict';

angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.factory('PreviousListServices',function(){
		var altJobCode = {
				session: 'catch',
			    atpListForApproval:[],
			    atpPendingApproval:'',
			  };
			 
			return altJobCode;
	});