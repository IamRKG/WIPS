'use strict';

angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.factory('SessionServices',function(){
		var session = {
				sessionId: 'catch',
				clikedBackButton : false,
				isMenuCliked:false
			};
			return session;
	});
