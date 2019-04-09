'use strict';
describe('WipsUiApp.priceClaimPendingApprovalModule PriceClaimPendingApprovalController:', function () {

    //Dependencies
    var scope, $rootScope, $controller, $state, WcDataTableService, WcAlertConsoleService,$templateCache,$httpBackend,$q;
    var User;

    // Controllers
    function PriceClaimPendingApprovalController(resolvedPriceClaimApprovalListMock) {
        var controller = $controller('PriceClaimPendingApprovalController as priceClaimPendingApprovalController', {
            $scope : scope,
            resolvedPriceClaimApprovalList: resolvedPriceClaimApprovalListMock
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
    var resolvedPriceClaimApprovalListMock = {
        "categoryCode":"25",
        "atp":[],
        "lumpSum":[],
        "priceClaims":[
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"EPNUA - COOPER STANDARD","supplierName":"COOPER STA","entityNumber":"EB4742","readOrUnreadFlag":"9"},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"EPNUA - COOPER STANDARD","supplierName":"COOPER STA","entityNumber":"EB4741","readOrUnreadFlag":"9"},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"EPNUA - COOPER STANDARD","supplierName":"COOPER STA","entityNumber":"EB4738","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"EPNUA - COOPER STANDARD","supplierName":"COOPER STA","entityNumber":"EB4737","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"EPNUA - COOPER STANDARD","supplierName":"COOPER STA","entityNumber":"EB4736","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"EPNUA - COOPER STANDARD","supplierName":"COOPER STA","entityNumber":"EB4735","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"EPNUA - COOPER STANDARD","supplierName":"COOPER STA","entityNumber":"EB4734","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"EPNUA - COOPER STANDARD","supplierName":"COOPER STA","entityNumber":"EB4733","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"EPNUA - COOPER STANDARD","supplierName":"COOPER STA","entityNumber":"EB4732","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"EC0ZA - COOPER STANDARD","supplierName":"COOPER STA","entityNumber":"EB4730","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"DZ4JA - PORITE YANGZHOU","supplierName":"PORITE YAN","entityNumber":"EB4729","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"FV3YA - WUHAN MECAPLAST","supplierName":"WUHAN MECA","entityNumber":"EB4725","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"DZ4JA - PORITE YANGZHOU","supplierName":"PORITE YAN","entityNumber":"EB4722","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016YOY","supplier":"DZ4JA - PORITE YANGZHOU","supplierName":"PORITE YAN","entityNumber":"EB4719","readOrUnreadFlag":""},
            {"effectiveDate":"Jan-01-2016","claimTitle":"2016 YOY","supplier":"GNWCA - BJ FILTRAN","supplierName":"BJ FILTRAN","entityNumber":"EB4584","readOrUnreadFlag":""}
        ],
        "status":"OK",
        "userRacfId":"w#e0na",
        "errorFlag":false,
        "actionAlreadyTakenFlag":false,
        "approveOrRejectMessage":"test"
    }


    beforeEach(function () {
        module('WipsUiApp.priceClaimPendingApprovalModule');

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
        $state.current.data.clickedBackButtonCallNewData = resolvedPriceClaimApprovalListMock;
        User.currentjobDetail = testUser.jobDetail;


    });

    it('should be registered', function () {
            expect(PriceClaimPendingApprovalController).toBeDefined();
    });

    it('should load ATP pending list resolve data when errorFlag is true or clickedBackButtonCallNewData is empty',function(){
        $state.current.data.clickedBackButtonCallNewData = '';

        PriceClaimPendingApprovalController(resolvedPriceClaimApprovalListMock);

        expect(scope.priceClaimPendingApprovalController.priceClaimPendingApprovalTable).toEqual(resolvedPriceClaimApprovalListMock.priceClaims);
        expect(scope.priceClaimPendingApprovalController.jobDetail).toEqual(testUser.jobDetail);

    });

    it('should take given result with errorFlag false and actionAlreadyTakenFlag true by state.go().data.clickedBackButtonCallNewData and create the appropriate list messages with the WcAlertConsoleService',function(){


        $state.current.data.clickedBackButtonCallNewData.errorFlag = false;
        $state.current.data.clickedBackButtonCallNewData.actionAlreadyTakenFlag = true;

        spyOn(WcAlertConsoleService, 'addMessage');

        PriceClaimPendingApprovalController();

        expect(scope.priceClaimPendingApprovalController.priceClaimPendingApprovalTable).toEqual($state.current.data.clickedBackButtonCallNewData.priceClaims);
        expect(scope.priceClaimPendingApprovalController.jobDetail).toEqual(testUser.jobDetail);
        expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'danger', multiple: false});

    });

    it('should take given result with errorFlag false and actionAlreadyTakenFlag false by state.go().data.clickedBackButtonCallNewData and create the appropriate list messages with the WcAlertConsoleService',function(){

        $state.current.data.clickedBackButtonCallNewData.errorFlag = false;
        $state.current.data.clickedBackButtonCallNewData.actionAlreadyTakenFlag = false;

        spyOn(WcAlertConsoleService, 'addMessage');

        PriceClaimPendingApprovalController();

        expect(scope.priceClaimPendingApprovalController.priceClaimPendingApprovalTable).toEqual($state.current.data.clickedBackButtonCallNewData.priceClaims);
        expect(scope.priceClaimPendingApprovalController.jobDetail).toEqual(testUser.jobDetail);
        expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'success', multiple: false});

    });

    it('should not navigate to state if error flag true and create the appropriate list messages with the WcAlertConsoleService',function(){

        $state.current.data.clickedBackButtonCallNewData.errorFlag = true;

        PriceClaimPendingApprovalController();

        expect(scope.priceClaimPendingApprovalController.priceClaimPendingApprovalTable).toBeUndefined();

      });

/*TODO Need to review test below*/
    describe('priceClaimDetailsApprovals():', function (){

        it('should call state.go to navigate to the lump-sum-details-approvals state for the given data', function() {
            var priceClaimNumber = {"entityNumber":"EB4742","supplier":"EPNUA - COOPER STANDARD"}
            spyOn(WcDataTableService, 'getClickedRowData').and.returnValue($q.when(priceClaimNumber));

            spyOn($state, 'go');

            PriceClaimPendingApprovalController();

            scope.priceClaimPendingApprovalController.priceClaimDetailsApprovals();

            User.userInformation.ltermToken = testUser.ltermToken;

            $rootScope.$apply();

            expect(WcDataTableService.getClickedRowData).toHaveBeenCalled();
            expect($state.go).toHaveBeenCalledWith('price-claim-details-approvals', {ltermToken:testUser.ltermToken,priceClaimNumber:resolvedPriceClaimApprovalListMock.priceClaims[0].entityNumber,supplier:resolvedPriceClaimApprovalListMock.priceClaims[0].supplier});

        });

    });

});