'use strict';

var priceClaimDetailPage = function(){
	
};

priceClaimDetailPage.prototype = Object.create({},{
	
	isCurrentPage:{
		value: function(){
			return element(by.tagName('h1')).getText().then(function(text){
				return text == 'Price Claim - XMS35T';
			});
		}
	}});