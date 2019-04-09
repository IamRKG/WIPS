'use strict';
angular.module('WipsUiApp.ComponentsModule')
    .constant('WipsHttpStatusCodesConstant', {
        "accepted202":"202",
        "badRequest400":"400",
        "conflict409":"409",
        "unauthorized401":"401",
        "internalServerError500":"500",
        "statusTextAccepted":"ACCEPTED"
    });