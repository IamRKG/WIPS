'use strict';

var ATPStrategyDetailsPage = function(){
	
};

ATPStrategyDetailsPage.prototype = Object.create({},{
	
	isCurrentPage:{
		value: function(){
			return element(by.tagName('h1')).getText().then(function(text){
				return text == 'ATP Strategy Details - XMS35T';
			});
		}
	},

	visibleJobCode:{
		value:function(){
			return element(by.binding('atpStrategyDetailsController.jobDetail.jobCode')).getText().then(function(text){
				return text == 'GN7A';
			});
		}
	},

	plantNameBylocation:{
		value:function(){
			 return element.all(by.repeater('plantDetails in atpStrategyDetailsController.atpStrategyDetails.plantDetails')).get(0).getText();
		}
	},
	numberOfPlant:{
		value:function(){
			return element.all(by.repeater('plantDetails in atpStrategyDetailsController.atpStrategyDetails.plantDetails')).count();
		}
	},
	numberOfNationalCompany:{
		value:function(){
			return element.all(by.repeater('plantDetails in atpStrategyDetailsController.atpStrategyDetails.plantDetails')).count();
		}

	},
	isStrategySuppliersTable:{
		value:function(){
			return element(by.id('strategySuppliersTable')).isPresent();/*TODO:Use element.all for test all table*/
		}
	},
	columnHeadersText: {
		value: function() {
			//use map implementation for array of promise handling as talked about here:
			//http://stackoverflow.com/questions/21736191/protractor-how-to-get-the-result-of-an-array-of-promises-into-another-array
			return element.all(by.css('#strategySuppliersTable:first-child thead tr th')).map(function(elm){
				return elm.getText();
			});
		}
	}



});

module.exports = ATPStrategyDetailsPage;