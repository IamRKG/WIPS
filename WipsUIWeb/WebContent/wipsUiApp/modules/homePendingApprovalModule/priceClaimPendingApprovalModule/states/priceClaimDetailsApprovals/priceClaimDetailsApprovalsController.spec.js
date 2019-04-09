'use strict';

describe('WipsUiApp.priceClaimPendingApprovalModule PriceClaimDetailsApprovalsController:',function(){

    //Dependencies
    var scope, $rootScope, $controller, $state, $stateParams,_
    var User,confirmModalService

    //Test Data

    var resolvedPriceClaimApprovalMock =  {
        "pcsSummaryInformation":{"linkedPcNumber":"EB4742","supplier":"EPNUA - COOPER STANDARD","buyer":"CH9R - ZHAO, XIANPENG","causalFactor":"N - NEGOTIATION","effectiveDate":"Jan-01-2016","exchangeRate":"CNY 6.57764","volumes":"DEFAULT","pcsRemark":"NO","intPrc":"NO","errorFlag":false},
        "status":"OK",
        "errorFlag":false
    }

    // Controllers
    function PriceClaimDetailsApprovalsController(resolvedPriceClaimApprovalMock) {
        var controller = $controller('PriceClaimDetailsApprovalsController as priceClaimDetailsApprovalsController', {
            $scope : scope,
            resolvedPriceClaimApproval:resolvedPriceClaimApprovalMock
        });
        return controller;
    };

    beforeEach(function(){
        module('WipsUiApp.priceClaimPendingApprovalModule');
        module('WipsUiApp.ATPPendingApproval.ComponentsModule');

        inject(function ($injector) {
            $rootScope = $injector.get('$rootScope');
            $controller = $injector.get('$controller');
            $state = $injector.get('$state');
            $stateParams = $injector.get('$stateParams');
            _ = $injector.get('_');
            User = $injector.get('User');
        });
        scope = $rootScope.$new();

    });

    it('should be registered', function () {
        expect(PriceClaimDetailsApprovalsController).toBeDefined();
    });

    xit('should taken given result and map appropirate object ',function(){

        PriceClaimDetailsApprovalsController(resolvedPriceClaimApprovalMock);
        expect(scope.priceClaimDetailsApprovalsController.pcsData).toEqual(resolvedPriceClaimApprovalMock);
    });

    describe('lumpsumBack():',function(){

        it('Should navigate to PCS worklist page',function () {

            var data = {
                clickedBackButtonCallNewData:""
            }

            spyOn($state,'get');
            PriceClaimDetailsApprovalsController();
            scope.priceClaimDetailsApprovalsController.lumpsumBack();
            $rootScope.$apply();
            console.log($state);
            expect($state.get).toHaveBeenCalledWith('price-claim-pending-approval');
        })

    });

});