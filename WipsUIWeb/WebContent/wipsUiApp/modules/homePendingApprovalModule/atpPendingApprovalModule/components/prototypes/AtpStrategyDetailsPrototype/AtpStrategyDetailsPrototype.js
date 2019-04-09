'use strict';

angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.factory('AtpStrategyDetailsPrototype',function(){
		
		function AtpStrategyDetailsPrototype(AtpStrategyDetailsObject) {
			this.atpNumber = '';
			this.atpType = '';
            this.buyer = '';
            this.enggLevel = '';
            this.errorFlag = '';
            this.part = '';
            this.plantDetailsBean = [];
            this.status = '';
            this.strategy = '';
            this.trendRate = '';
            this.wipsCommonBean = '';
 
   
            if(AtpStrategyDetailsObject) {
                angular.extend(this, AtpStrategyDetailsObject);
            }
		};
		
		AtpStrategyDetailsPrototype.prototype = {
			createFrom: function(AtpStrategyDetailsObject) {
				if(AtpStrategyDetailsObject) {
					angular.extend(this, AtpStrategyDetailsObject);				
				}			
			}
		};
		
		return AtpStrategyDetailsPrototype;
	});