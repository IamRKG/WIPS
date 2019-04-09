'use strict';

angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.factory('AtpRemarksPrototype',function(){
		
		function AtpRemarksPrototype(AtpRemarksObject) {
			this.atpPartNumberVisited = '';
			this.buyerCode = '';
			this.engineeringLevel = '';
			this.partNumber = '';
			this.atpRemarksList = [];
			this.wipsCommonBean = {};
	
            if(AtpRemarksObject) {
                angular.extend(this, AtpRemarksObject);
            }
		};
		
		AtpRemarksPrototype.prototype = {
			createFrom: function(AtpRemarksObject) {
				if(AtpRemarksObject) {
					angular.extend(this, AtpRemarksObject);				
				}			
			}
		};
		
		return AtpRemarksPrototype;
	});