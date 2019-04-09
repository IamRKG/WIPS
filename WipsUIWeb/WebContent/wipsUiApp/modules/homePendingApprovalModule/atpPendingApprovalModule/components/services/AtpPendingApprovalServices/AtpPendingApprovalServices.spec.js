'use strict';

describe('WipsUiApp.ATPPendingApproval.ComponentsModule ATPPendingApprovalServices:',function(){
    //Dependencies
    var ATPPendingApprovalServices, WcHttpEndpointPrototype, WcAlertConsoleService, User,$q, $state, $rootScope, $translate,WipsHttpStatusCodesServices,WipsHttpStatusCodesConstant

    //Test Data

    var testUser = {
        "validUser":true,
        "pendingApprovals":[
            {"totalCount":"3","categoryCode":"102","categoryName":"Lumpsums"}
        ],
        "delegatedJobs":[
            {"jobCode":"177A","jobName":"MASON, JAMIE"},
            {"jobCode":"GT0C","jobName":"LAMBA, SANJAY"},
            {"jobCode":"021A","jobName":"****021A"},
            {"jobCode":"251A","jobName":"OTEYZA, RENATO"},
            {"jobCode":"GT3K","jobName":"VILANI, THIAGO"},
            {"jobCode":"GC5A","jobName":"MOLNAR, DAVID"},
            {"jobCode":"GT0A","jobName":"MOLINA, ROXANA"},
            {"jobCode":"GK6A","jobName":"FLACK, THOMAS"}
        ],
        "jobDetail":{"jobCode":"GT7K","jobName":"GOKUL"},
        "status":"OK",
        "errorFlag":false,
        "ltermToken":"dzd2T0IxRzdpY3M9"
    };

    var testAtpPendingList = {
        "categoryCode":"25",
        "atp":[{"partNumber":"KK1P-7P155-BA1","entityNumber":"XM6BM3","buyerCode":"CH83 - ZHENG, YANGLEI","readOrUnreadFlag":"0","subsequentProgram":"G53X"},
            {"partNumber":"KK1P-7P155-AA1","entityNumber":"XM6BK2","buyerCode":"CH83 - ZHENG, YANGLEI","readOrUnreadFlag":"0","subsequentProgram":"G53X"},
            {"partNumber":"J1LP-3B436-BA01","entityNumber":"XNAKBJ","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"0","subsequentProgram":"G53X"},
            {"partNumber":"J1LP-3B437-BA01","entityNumber":"XNAKBK","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"0","subsequentProgram":"G53X"},
            {"partNumber":"J1LP-3B436-CA01","entityNumber":"XNA36J","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"0","subsequentProgram":"G53X"},
            {"partNumber":"J1LP-3B436-DA01","entityNumber":"XNA375","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"","subsequentProgram":"G53X"},
            {"partNumber":"J1LP-3B437-DA01","entityNumber":"XNA376","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"","subsequentProgram":"G53X"},
            {"partNumber":"J1LP-3B437-CA01","entityNumber":"XNA36N","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"0","subsequentProgram":"G53X"},
            {"partNumber":"ES73-6C646-GF","entityNumber":"XMZ9JZ","buyerCode":"CH41 - ZHOU, SILIANG","readOrUnreadFlag":"0","subsequentProgram":"G53X"},
            {"partNumber":"FD8P-7F465-BB","entityNumber":"XNCD3U","buyerCode":"CH83 - ZHENG, YANGLEI","readOrUnreadFlag":"0","subsequentProgram":"G53X"},
            {"partNumber":"HG93-3B437-CB","entityNumber":"XM1UBV","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"","subsequentProgram":"G53X"},
            {"partNumber":"HG98-3B436-BC","entityNumber":"XMSY8Z","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"","subsequentProgram":"G53X"},
            {"partNumber":"HG93-3B436-FB","entityNumber":"XM1UBU","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"","subsequentProgram":"G53X"},
            {"partNumber":"HG98-3B436-BD","entityNumber":"XM1UBW","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"","subsequentProgram":"G53X"},
            {"partNumber":"HG98-3B437-BC","entityNumber":"XM1UBX","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"","subsequentProgram":"G53X"},
            {"partNumber":"GN1G-6A266-AA","entityNumber":"XNCKIN","buyerCode":"CH43 - OU, YANG","readOrUnreadFlag":"","subsequentProgram":"G53X"},
            {"partNumber":"GN1G-6A268-AA","entityNumber":"XNCLAN","buyerCode":"CH43 - OU, YANG","readOrUnreadFlag":"","subsequentProgram":"G53X"},
            {"partNumber":"DV61-4K138-BC","entityNumber":"XNCNNQ","buyerCode":"CH8H - WANG, JIA","readOrUnreadFlag":"","subsequentProgram":"G53X"}],
        "lumpSum":[],
        "priceClaims":[],
        "status":"OK",
        "userRacfId":"w#e0na",
        "errorFlag":false,
        "actionAlreadyTakenFlag":false,
        "approveOrRejectMessage":"test",

        data:{
            "errorMessage":'test',
        }
    }




    beforeEach(function() {
        // Module & Providers
        module('WipsUiApp.ATPPendingApproval.ComponentsModule');

        inject(function($injector) {
            ATPPendingApprovalServices = $injector.get('ATPPendingApprovalServices');
            WcHttpEndpointPrototype = $injector.get('WcHttpEndpointPrototype');
            WcAlertConsoleService = $injector.get('WcAlertConsoleService');
            User = $injector.get('User');
            WipsHttpStatusCodesServices = $injector.get('WipsHttpStatusCodesServices');
            $q = $injector.get('$q');
            $state = $injector.get('$state');
            $rootScope = $injector.get('$rootScope');
            $translate = $injector.get('$translate');
            WipsHttpStatusCodesServices = $injector.get('WipsHttpStatusCodesServices');
            WipsHttpStatusCodesConstant = $injector.get('WipsHttpStatusCodesConstant');


        });

     });

    it('define a ATPPendingApprovalServices', function() {
        expect(ATPPendingApprovalServices).toBeDefined();
    });

    it('establishes a endpoint for PendingApprovals', function() {
        expect(ATPPendingApprovalServices.pendingApprovalEndpoint.route).toEqual('PendingApprovals');
    });

    describe('getATPPendingApproval():', function() {

        it('should take given object and create list if the request has been success',function(){

            spyOn(ATPPendingApprovalServices.pendingApprovalEndpoint,'subRoute').and.callFake(function(){
                return {get: function(){return $q.when(testAtpPendingList);}};
            });

            var actualResponse={};
            ATPPendingApprovalServices.getATPPendingApproval('dzd2T0IxRzdpY3M9', '25').then(function(response) {
                actualResponse = response;
            }, function() {
                console.error('This should not happen');
            });

            testAtpPendingList.status = '200';

            $rootScope.$apply();

            expect(ATPPendingApprovalServices.pendingApprovalEndpoint.subRoute).toHaveBeenCalledWith('dzd2T0IxRzdpY3M9/25');
        });

        it('should take given object and create a danger alert if the request has not been completed',function(){

            spyOn(ATPPendingApprovalServices.pendingApprovalEndpoint,'subRoute').and.callFake(function(){
                return {get: function(){return $q.when(testAtpPendingList);}};
            });

            var actualResponse={};
            ATPPendingApprovalServices.getATPPendingApproval('dzd2T0IxRzdpY3M9', '25').then(function(response) {
                actualResponse = response;
            }, function() {
                console.error('This should not happen');
            });


            spyOn($translate, 'instant').and.callFake(function() {
                return 'test';
            });

            testAtpPendingList.status = WipsHttpStatusCodesConstant.statusTextAccepted;

            spyOn(WcAlertConsoleService, 'addMessage');

            $rootScope.$apply();
            expect(WipsHttpStatusCodesConstant.statusTextAccepted).toEqual('Accepted');
            expect(ATPPendingApprovalServices.pendingApprovalEndpoint.subRoute).toHaveBeenCalledWith('dzd2T0IxRzdpY3M9/25');
            expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'danger', multiple: false});
        });


        it('should take given object and create a danger message the request could not be completed due to a conflict with the current state of the target resource. This code is used in situations where the user might be able to resolve the conflict(navigate to List page) and submit new the request.',function(){

            spyOn(ATPPendingApprovalServices.pendingApprovalEndpoint,'subRoute').and.callFake(function(){
                return {get: function(){return $q.reject(testAtpPendingList);}};
            });

            var errorMessage={};
            ATPPendingApprovalServices.getATPPendingApproval('dzd2T0IxRzdpY3M9', '25').then(function(response) {
                console.error('This should not happen');
            }, function(err) {
                errorMessage = err;
            });

            spyOn($state, 'go');

            spyOn(WcAlertConsoleService, 'addMessage');

            spyOn($translate, 'instant').and.callFake(function() {
                return 'test';
            });

            testAtpPendingList.status = WipsHttpStatusCodesConstant.conflict409;
            User.currentCategoryCode = testAtpPendingList.categoryCode;
            User.userInformation.ltermToken = testUser.ltermToken;

            $rootScope.$apply();
            expect(WipsHttpStatusCodesConstant.conflict409).toEqual('409');
            expect(ATPPendingApprovalServices.pendingApprovalEndpoint.subRoute).toHaveBeenCalledWith('dzd2T0IxRzdpY3M9/25');
            expect($state.go).toHaveBeenCalledWith('atp-pending-approval',{categoryCode:testAtpPendingList.categoryCode,ltermToken:testUser.ltermToken},{reload: true});
            expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'danger', multiple: false});
        });



        it('should take given object and the server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, or deceptive request routing).',function(){
            spyOn(ATPPendingApprovalServices.pendingApprovalEndpoint,'subRoute').and.callFake(function(){
                return {get: function(){return $q.reject(testAtpPendingList);}};
            });

            var errorMessage={};
            ATPPendingApprovalServices.getATPPendingApproval('dzd2T0IxRzdpY3M9', '25').then(function(response) {
                console.error('This should not happen');
            }, function(err) {
                errorMessage = err;
            });

            spyOn(WipsHttpStatusCodesServices, 'getWipsHttpStatusCodes');

            testAtpPendingList.status = WipsHttpStatusCodesConstant.badRequest400;

            WipsHttpStatusCodesServices.getWipsHttpStatusCodes();

            $rootScope.$apply();

            expect(WipsHttpStatusCodesConstant.badRequest400).toEqual('400');
            expect(ATPPendingApprovalServices.pendingApprovalEndpoint.subRoute).toHaveBeenCalledWith('dzd2T0IxRzdpY3M9/25');
            expect(WipsHttpStatusCodesServices.getWipsHttpStatusCodes).toHaveBeenCalled();

        });

    });

})