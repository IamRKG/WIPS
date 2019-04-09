'use strict';
describe('WipsUiApp.LumpSumPendingApprovalModule:', function () {

    //Module
    var WipsUiAppLumpSumPendingApprovalModule;

    //Dependencies
    var $httpBackend,$q,$rootScope,$state,$templateCache,LumpSumPendingApprovalServices,LumpSumDetailsApprovalServices;

    var LumpSumPendingApprovalMock = [
        {
            "validUser":true,
            "pendingApprovals":[],
            "delegatedJobs":[{"jobCode":"938A","jobName":"DAVIDSON, MIKE"},{"jobCode":"251A","jobName":"OTEYZA, RENATO"},{"jobCode":"M808","jobName":"J TIMOTHY"},{"jobCode":"096A","jobName":"FAHNDRICH, C."},{"jobCode":"GC0H","jobName":"CARTER, ANN"},{"jobCode":"GACA","jobName":"A FUHRMANN"},{"jobCode":"GA5T","jobName":"P GIJSEN"},{"jobCode":"GA2A","jobName":"S ZILLIG"},{"jobCode":"117A","jobName":"MAYORAS, YUNCHIN"}],
            "jobDetail":{"jobCode":"986A","jobName":"ARAVIND"},
            "status":"OK",
            "errorFlag":false,
            "ltermToken":"d2J2Q1VWRHBpY3M9"
        }
    ]


    beforeEach(function () {
        module('WipsUiApp.LumpSumPendingApprovalModule',function($stateProvider){
            $stateProvider
                .state('mock-state', {
                    url: '/mock-state',
                    template: '<div />'
                })
                .state('wips-ui-app', {
                    url: '',
                    'abstract': true
                })
        });

        inject(function($injector) {
            $httpBackend = $injector.get('$httpBackend');
            $q = $injector.get('$q');
            $rootScope = $injector.get('$rootScope');
            $state = $injector.get('$state');
            $templateCache = $injector.get('$templateCache');
            LumpSumPendingApprovalServices = $injector.get('LumpSumPendingApprovalServices');
            LumpSumDetailsApprovalServices = $injector.get('LumpSumDetailsApprovalServices');
        });
    })


    WipsUiAppLumpSumPendingApprovalModule = angular.module('WipsUiApp.LumpSumPendingApprovalModule');

    it('should ensure WipsUiApp.LumpSumPendingApprovalModule module was registered', function () {
        expect(WipsUiAppLumpSumPendingApprovalModule).toBeDefined();
    });

    describe('State [Lump Sum Pending Approval]:', function () {
        beforeEach(function () {
            // We need add the template entry into the templateCache if we ever specify a templateUrl
            $templateCache.put('wipsUiApp/modules/homePendingApprovalModule/lumpSumPendingApprovalModule/states/lumpSumPendingApproval/LumpSumPendingApprovalTemplate.html', '');
            spyOn(LumpSumPendingApprovalServices, 'getLumpSumPendingApproval').and.callFake(function () {
                return $q.when(LumpSumPendingApprovalMock);
            });
        });
        it('should ensure a state named lump-sum-pending-approval is registered', function () {
            var state = $state.get('lump-sum-pending-approval');
            expect(state).not.toBeNull();
        });

        it('should define a URL', function() {
            expect($state.href('lump-sum-pending-approval')).toEqual('#/lump-sum-pending-approval//');
        });

        it('should populate Lump Sum Pending Approval via resolve', inject(function($injector) {
            var state = $state.get('lump-sum-pending-approval');

            $state.go(state);
            $rootScope.$digest();
            expect($state.current).toBe(state);

            expect(LumpSumPendingApprovalServices.getLumpSumPendingApproval).toHaveBeenCalled();
        }));
    });

    describe('State [Lump Sum Details Approvals]:',function(){
        beforeEach(function () {
            // We need add the template entry into the templateCache if we ever specify a templateUrl
            $templateCache.put('wipsUiApp/modules/homePendingApprovalModule/lumpSumPendingApprovalModule/states/lumpSumDetailsApprovals/LumpSumDetailsApprovalsTemplate.html', '');
            spyOn(LumpSumDetailsApprovalServices, 'getLumpSumDetailsApproval').and.callFake(function () {
                return $q.when(LumpSumPendingApprovalMock);
            });
        });

        it('should ensure a state named lump-sum-pending-approval is registered', function () {
            var state = $state.get('lump-sum-details-approvals');
            expect(state).not.toBeNull();
        });

        it('should define a URL', function() {
            expect($state.href('lump-sum-details-approvals')).toEqual('#/lump-sum-details-approvals///');
        });

        it('should populate Lump Sum Details Approvals via resolve', inject(function($injector) {
            var state = $state.get('lump-sum-details-approvals');

            $state.go(state);
            $rootScope.$digest();
            expect($state.current).toBe(state);

            expect(LumpSumDetailsApprovalServices.getLumpSumDetailsApproval).toHaveBeenCalled();
        }));

    });

});
