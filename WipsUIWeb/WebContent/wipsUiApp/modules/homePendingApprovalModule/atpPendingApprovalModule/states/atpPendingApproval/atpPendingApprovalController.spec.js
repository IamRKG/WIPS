'use strict';
fdescribe('WipsUiApp.ATPPendingApprovalModule ATPPendingApprovalController:', function () {

    //Dependencies
    var scope, $rootScope, $controller, $state, WcDataTableService, WcAlertConsoleService,$templateCache,$httpBackend,$q;
    var User,SwitchJobFactory;

    // Controllers
    function ATPPendingApprovalController(resolvedATPApprovalMock) {
        var controller = $controller('ATPPendingApprovalController as atpPendingApprovalController', {
            $scope : scope,
            resolvedATPApproval: resolvedATPApprovalMock
        });
        return controller;
    };

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
    var resolvedATPApprovalMock = {
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
        "approveOrRejectMessage":"test"
    }



    beforeEach(function () {
        module('WipsUiApp.ATPPendingApprovalModule');

        inject(function ($injector) {
            $rootScope = $injector.get('$rootScope');
            $controller = $injector.get('$controller');
            $state = $injector.get('$state');
            WcDataTableService = $injector.get('WcDataTableService');
            WcAlertConsoleService = $injector.get('WcAlertConsoleService');
            $templateCache = $injector.get('$templateCache');
            $httpBackend = $injector.get('$httpBackend');
            $q = $injector.get('$q');
            User = $injector.get('User');
            SwitchJobFactory = $injector.get('SwitchJobFactory');
        });


        scope = $rootScope.$new();

        $state.current.data = {};
        $state.current.data.clickedBackButtonCallNewData = resolvedATPApprovalMock;
        User.currentjobDetail = testUser.jobDetail;


    });

    iit('should be registered', function () {
            expect(ATPPendingApprovalController).toBeDefined();
    });

    it('should load ATP pending list resolve data when errorFlag is true or clickedBackButtonCallNewData is empty',function(){
        $state.current.data.clickedBackButtonCallNewData = '';

        ATPPendingApprovalController(resolvedATPApprovalMock);

        expect(scope.atpPendingApprovalController.atpPendingApprovalTable).toEqual(resolvedATPApprovalMock.atp);
        expect(scope.atpPendingApprovalController.jobDetail).toEqual(testUser.jobDetail);

    });

    it('should take given result with errorFlag false and actionAlreadyTakenFlag true by state.go().data.clickedBackButtonCallNewData and create the appropriate list messages with the WcAlertConsoleService',function(){


        $state.current.data.clickedBackButtonCallNewData.errorFlag = false;
        $state.current.data.clickedBackButtonCallNewData.actionAlreadyTakenFlag = true;

        spyOn(WcAlertConsoleService, 'addMessage');

        ATPPendingApprovalController();

        expect(scope.atpPendingApprovalController.atpPendingApprovalTable).toEqual($state.current.data.clickedBackButtonCallNewData.atp);
        expect(scope.atpPendingApprovalController.jobDetail).toEqual(testUser.jobDetail);
        expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'danger', multiple: false});

    });

    it('should take given result with errorFlag false and actionAlreadyTakenFlag false by state.go().data.clickedBackButtonCallNewData and create the appropriate list messages with the WcAlertConsoleService',function(){

        $state.current.data.clickedBackButtonCallNewData.errorFlag = false;
        $state.current.data.clickedBackButtonCallNewData.actionAlreadyTakenFlag = false;

        spyOn(WcAlertConsoleService, 'addMessage');

        ATPPendingApprovalController();

        expect(scope.atpPendingApprovalController.atpPendingApprovalTable).toEqual($state.current.data.clickedBackButtonCallNewData.atp);
        expect(scope.atpPendingApprovalController.jobDetail).toEqual(testUser.jobDetail);
        expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'success', multiple: false});

    });

    it('should not navigate to state if error flag true and create the appropriate list messages with the WcAlertConsoleService',function(){

        $state.current.data.clickedBackButtonCallNewData.errorFlag = true;

        ATPPendingApprovalController();

        expect(scope.atpPendingApprovalController.atpPendingApprovalTable).toBeUndefined();

      });

/*TODO Need to review test below*/
    describe('atpRecap():', function (){

        it('should call state.go to navigate to the lump-sum-details-approvals state for the given data', function() {
            var atpRecapNumber = {"entityNumber":"XM6BM3"}
            spyOn(WcDataTableService, 'getClickedRowData').and.returnValue($q.when(atpRecapNumber));

            spyOn($state, 'go');

            ATPPendingApprovalController();

            scope.atpPendingApprovalController.atpRecap();

            User.userInformation.ltermToken = testUser.ltermToken;

            $rootScope.$apply();

            expect(WcDataTableService.getClickedRowData).toHaveBeenCalled();
            expect($state.go).toHaveBeenCalledWith('atp-recap', {ltermToken:testUser.ltermToken,atpNumber:resolvedATPApprovalMock.atp[0].entityNumber});

        });

    });

});