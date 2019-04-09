'use strict';

angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.factory('AtpRemarksPrototype',function(){
		
		function AtpRemarksPrototype(AtpRemarksObject) {
			this.atpNumber = '';
            this.jobCode = '';
            this.jobOwner = '';
            this.strategy = '';
            this.Part = '';
            this.effectiveDate = '';
            this.reasonCode = '';
            this.autoPO = '';
            this.supplierList = [];
            this.approversList = [];
   
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