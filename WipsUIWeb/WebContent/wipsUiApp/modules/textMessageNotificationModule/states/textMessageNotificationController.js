'use strict';

angular.module('WipsUiApp.textMessageNotificationModule')
	.controller('textMessageNotificationController', ['$scope','textMessageNotificationServices','User','resolvedTextMessageNotificationData',	
		function($scope,textMessageNotificationServices,User,resolvedTextMessageNotificationData){
		this.textMessageInputs=resolvedTextMessageNotificationData;
		this.notification=function(getValue){
			if(getValue=='Yes'){
				
			this.notValue='Y';
			}
			else{
				
				this.notValue='N';	
			}
			 this.param = {
					 selectedOption :this.notValue,
					 ltermToken:User.userInformation.ltermToken
	                    
	                };
			 return textMessageNotificationServices.getTextMessageSelectedResponse(this.param).then(angular.bind(this, function (saveResponse) {
				 
				 
			 }));
		}
		this.checkValueYes=function(){
			var buttonValue='active';
			if(this.textMessageInputs.optedForSms=="Y"){
				
				return buttonValue;
			}
			else{
				
				return null;
			}
		}
		this.notificationSymNo=function(){
			
			var addTick="addTick";
			if(this.textMessageInputs.optedForSms=="Y"){
				return addTick
				
			}
			else{
				
				return null;
			}
		}
this.notificationSymYes=function(){
			
			var addTick="addTick";
			if(this.textMessageInputs.optedForSms=="N"){
				return addTick
				
			}
			else{
				
				return null;
			}
		}
		this.checkValueNo=function(){
			var buttonValue='active';
			if(this.textMessageInputs.optedForSms=="N"){
				
				return buttonValue;
			}
			else{
				
				return null;
			}
		}
		
	}]);
