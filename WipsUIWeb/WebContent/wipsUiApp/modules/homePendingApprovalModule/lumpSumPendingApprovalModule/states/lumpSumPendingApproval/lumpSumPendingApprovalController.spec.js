'use strict';
describe('WipsUiApp.LumpSumPendingApprovalModule LumpSumPendingApprovalController:', function () {

    //Dependencies
    var scope, $rootScope, $controller, $state, WcDataTableService, WcAlertConsoleService,$templateCache,$httpBackend,$q;
    var User;

    // Controllers
    function LumpSumPendingApprovalController(resolvedLumpSumApprovalListtMock) {
        var controller = $controller('LumpSumPendingApprovalController as lumpSumPendingApprovalController', {
            $scope : scope,
            resolvedLumpSumApprovalList: resolvedLumpSumApprovalListtMock
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
    var resolvedLumpSumApprovalListtMock = {
        "categoryCode":"102",
        "atp":[],
        "lumpSum":[
            {"cause":"NTE","supplierCode":"CF33A - ROBERT BOSCH GM","entityNumber":"EB4916","buyerCode":"GT3K - VILANI, THIAGO","readOrUnreadFlag":"0","subsequentProgram":"LS APPROVAL"},
            {"cause":"SETTLE","supplierCode":"CF33A - ROBERT BOSCH GM","entityNumber":"EB4895","buyerCode":"GT3K - VILANI, THIAGO","readOrUnreadFlag":"0","subsequentProgram":"LS APPROVAL"},
            {"cause":"NTE","supplierCode":"MANY","entityNumber":"EB5373","buyerCode":"GT3K - VILANI, THIAGO","readOrUnreadFlag":"","subsequentProgram":"LS APPROVAL"}
        ],"status":"OK",
        "userRacfId":"u#ifo3",
        "errorFlag":false,
        "actionAlreadyTakenFlag":false,
        "approveOrRejectMessage":"test"
    };



    beforeEach(function () {
        module('WipsUiApp.LumpSumPendingApprovalModule');

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
        });


        scope = $rootScope.$new();

        $state.current.data = {};
        $state.current.data.clickedBackButtonCallNewData = resolvedLumpSumApprovalListtMock;
        User.currentjobDetail = testUser.jobDetail;


    });

    it('should be registered', function () {
            expect(LumpSumPendingApprovalController).toBeDefined();
    });

    it('should load LS pending list resolve data when errorFlag is true or clickedBackButtonCallNewData is empty',function(){
        $state.current.data.clickedBackButtonCallNewData = '';

        LumpSumPendingApprovalController(resolvedLumpSumApprovalListtMock);

        expect(scope.lumpSumPendingApprovalController.lumpSumPendingApprovalTable).toEqual(resolvedLumpSumApprovalListtMock.lumpSum);
        expect(scope.lumpSumPendingApprovalController.jobDetail).toEqual(testUser.jobDetail);

    });

    it('should take given result with errorFlag false and actionAlreadyTakenFlag true by state.go().data.clickedBackButtonCallNewData and create the appropriate list messages with the WcAlertConsoleService',function(){


        $state.current.data.clickedBackButtonCallNewData.errorFlag = false;
        $state.current.data.clickedBackButtonCallNewData.actionAlreadyTakenFlag = true;

        spyOn(WcAlertConsoleService, 'addMessage');

        LumpSumPendingApprovalController();

        expect(scope.lumpSumPendingApprovalController.lumpSumPendingApprovalTable).toEqual($state.current.data.clickedBackButtonCallNewData.lumpSum);
        expect(scope.lumpSumPendingApprovalController.jobDetail).toEqual(testUser.jobDetail);
        expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'danger', multiple: false});

    });

    it('should take given result with errorFlag false and actionAlreadyTakenFlag false by state.go().data.clickedBackButtonCallNewData and create the appropriate list messages with the WcAlertConsoleService',function(){

        $state.current.data.clickedBackButtonCallNewData.errorFlag = false;
        $state.current.data.clickedBackButtonCallNewData.actionAlreadyTakenFlag = false;

        spyOn(WcAlertConsoleService, 'addMessage');

        LumpSumPendingApprovalController();

        expect(scope.lumpSumPendingApprovalController.lumpSumPendingApprovalTable).toEqual($state.current.data.clickedBackButtonCallNewData.lumpSum);
        expect(scope.lumpSumPendingApprovalController.jobDetail).toEqual(testUser.jobDetail);
        expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'success', multiple: false});

    });

    it('should not navigate to state if error flag true and create the appropriate list messages with the WcAlertConsoleService',function(){

        $state.current.data.clickedBackButtonCallNewData.errorFlag = true;

        LumpSumPendingApprovalController();

        expect(scope.lumpSumPendingApprovalController.lumpSumPendingApprovalTable).toBeUndefined();

      });

    describe('lumpSumDetailsApprovals():', function (){

        it('should call state.go to navigate to the lump-sum-details-approvals state for the given data', function() {
            var lumpsumNumber = {"entityNumber":"EB4916"}
            spyOn(WcDataTableService, 'getClickedRowData').and.returnValue($q.when(lumpsumNumber));

            spyOn($state, 'go');

            LumpSumPendingApprovalController();

            scope.lumpSumPendingApprovalController.lumpSumDetailsApprovals();

            User.userInformation.ltermToken = testUser.ltermToken;

            $rootScope.$apply();

            expect(WcDataTableService.getClickedRowData).toHaveBeenCalled();
            expect($state.go).toHaveBeenCalledWith('lump-sum-details-approvals', {ltermToken:testUser.ltermToken,lumpsumNumber:resolvedLumpSumApprovalListtMock.lumpSum[0].entityNumber,defaultAmendment:'00'});

        });

    });

});