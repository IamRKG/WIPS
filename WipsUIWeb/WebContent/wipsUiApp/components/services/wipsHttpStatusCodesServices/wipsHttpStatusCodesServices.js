'use strict';
angular.module('WipsUiApp.ComponentsModule')
    .service('WipsHttpStatusCodesServices', ['$q','WcAlertConsoleService', '$translate','$state','User','WipsHttpStatusCodesConstant',function($q,WcAlertConsoleService, $translate, $state,User,WipsHttpStatusCodesConstant) {
        this.getWipsHttpStatusCodes = function(failure) {

            if (failure.status == WipsHttpStatusCodesConstant.unauthorized401) {
                $state.go('login.enter-login-information');
                User.getClearUserInformation();
                WcAlertConsoleService.addMessage({
                    message: $translate.instant('sessionTimeOut.message'),
                    type: 'warning',
                    multiple: false
                });

            }else if (failure.status == WipsHttpStatusCodesConstant.internalServerError500) {
                WcAlertConsoleService.addMessage({
                    message: $translate.instant('application.errors.internalServerError'),
                    type: 'danger',
                    multiple: false
                });
            }
            else {
                WcAlertConsoleService.addMessage({
                    message: $translate.instant(failure.data.errorMessage),
                    type: 'danger',
                    multiple: false
                });
            }
            return $q.reject(failure);
        }

     }]);
