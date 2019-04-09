'use strict';

angular.module('WipsUiApp.LumpSumPendingApproval.ComponentsModule')
	.factory('LumpSumDetailsApprovalsPrototype',function(){
		
		function LumpSumDetailsApprovalsPrototype(AtpRecapObject) {
			this.additionalLumpSumInformation = {},
			this.errorFlag = '',
			this.jobDetail = {},
			this.lumpSumInformation = {},
			this.lumpSumRemarks = {},
			this.userRacfId = '',
			this.userSessionId = ''
   
            if(AtpRecapObject) {
                angular.extend(this, AtpRecapObject);
            }
		};

		LumpSumDetailsApprovalsPrototype.prototype = {
			createFrom: function(AtpRecapObject) {
				if(AtpRecapObject) {
					angular.extend(this, AtpRecapObject);				
				}			
			}
		};
		
		return LumpSumDetailsApprovalsPrototype;
	});