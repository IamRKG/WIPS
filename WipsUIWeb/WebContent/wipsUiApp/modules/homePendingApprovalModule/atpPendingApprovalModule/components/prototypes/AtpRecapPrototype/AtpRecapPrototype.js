'use strict';

angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.factory('AtpRecapPrototype',function(){
		
		function AtpRecapPrototype(AtpRecapObject) {
			this.errorFlag = '',
			this.g53xTransactionOutput = {},
			this.jobDetail = {},
			this.userSessionId = ''
   
            if(AtpRecapObject) {
                angular.extend(this, AtpRecapObject);
            }
		};
		
		AtpRecapPrototype.prototype = {
			createFrom: function(AtpRecapObject) {
				if(AtpRecapObject) {
					angular.extend(this, AtpRecapObject);				
				}			
			}
		};
		
		return AtpRecapPrototype;
	});