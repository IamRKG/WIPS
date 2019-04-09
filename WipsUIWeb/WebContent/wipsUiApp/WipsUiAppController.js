'use strict';
angular.module('WipsUiAppModule').
	controller('WipsUiAppController', ['$state','$rootScope','User','SessionServices','logOutServices',
		function($state,$rootScope,User,SessionServices,logOutServices) {
		
		this.userInformation = User.userInformation;
		
		this.isMenuVislbe = $state.current.name;
		/*TODO: "atp-pending-approval" state name need to move view(HTML) when latest default data web web service ready*/
		this.atpPendingApproval = function (){
			SessionServices.isMenuCliked = true;

			$state.go('home-atp-pending-approval');
			$state.get('atp-pending-approval').data.clickedBackButtonCallNewData = "";
			$state.get('lump-sum-pending-approval').data.clickedBackButtonCallNewData = "";
			$state.get('price-claim-pending-approval').data.clickedBackButtonCallNewData = "";
			
		};
		this.smsPage=function(){
			$state.go('home-sms');
		};
		this.textMessageNotificationPage=function(){
			$state.go('home-textMessageNotification');
			
		}
		this.logOut = function() {
			var param = {
					ltermToken:User.userInformation.ltermToken
			};
			$state.get('atp-pending-approval').data.clickedBackButtonCallNewData = "";
			$state.get('lump-sum-pending-approval').data.clickedBackButtonCallNewData = "";
			User.getClearUserInformation();
			return logOutServices.getLogOut(param.ltermToken);
		};
			
		this.headerTemplateURL = 'wipsUiApp/wipsUiAppHeaderTemplate.html';
		
		$rootScope.$on('$stateChangeError', function (event, toState, toParams, fromState, fromParams, error) {
	            console.log('ERROR ($stateChangeError): ', error);
	     });

        $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
        	
        });
	}]);
