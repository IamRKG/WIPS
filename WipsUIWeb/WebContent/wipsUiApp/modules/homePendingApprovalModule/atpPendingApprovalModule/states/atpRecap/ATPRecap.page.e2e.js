'use strict';

var ATPRecapPage = function(){
	
};

ATPRecapPage.prototype = Object.create({},{
	
	isCurrentPage:{
		value: function(){
			return element(by.tagName('h1')).getText().then(function(text){
				return text == 'ATP Recap - XMS35T';
			});
		}
	},
	isStrategyDetailsButton:{
		value:function(){
			return element(by.name('strategyDetail'));
		}
	},
	navigateToStrategyDetailsPage:{
		value:function(){
			return element(by.name('strategyDetail')).click();
		}
	}
});

module.exports = ATPRecapPage;