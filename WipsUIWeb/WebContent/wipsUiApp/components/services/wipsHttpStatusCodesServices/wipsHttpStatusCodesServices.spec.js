describe('WipsUiApp.ComponentsModule WipsHttpStatusCodesServices:',function(){

    var $q,WcAlertConsoleService, $translate,$state;
    var WipsHttpStatusCodesServices,User,WipsHttpStatusCodesConstant

    /*Test Data*/

    var testErrorData = {
        status:'',
        data:{
            "errorMessage":'test',
        }
    }

    beforeEach(function() {
        // Module & Providers
        module('WipsUiApp.ComponentsModule');

        inject(function($injector) {
            WipsHttpStatusCodesServices = $injector.get('WipsHttpStatusCodesServices');
            WcAlertConsoleService = $injector.get('WcAlertConsoleService');
            User = $injector.get('User');
            $q = $injector.get('$q');
            $state = $injector.get('$state');
            $rootScope = $injector.get('$rootScope');
            $httpBackend = $injector.get('$httpBackend');
            $translate = $injector.get('$translate');
            WipsHttpStatusCodesConstant = $injector.get('WipsHttpStatusCodesConstant');
        });

    });

    it('define a WipsHttpStatusCodesServices', function() {
        expect(WipsHttpStatusCodesServices).toBeDefined();
    });

    describe('getWipsHttpStatusCodes();',function(){

        it('should take given object and create a warning message if request has not been applied because it session is not valid',function(){

            spyOn($state, 'go');

            spyOn(User, 'getClearUserInformation');

            spyOn(WcAlertConsoleService, 'addMessage');

            spyOn($translate, 'instant').and.callFake(function() {
                return 'test';
            });
            testErrorData.status = WipsHttpStatusCodesConstant.unauthorized401;
            WipsHttpStatusCodesServices.getWipsHttpStatusCodes(testErrorData);



            $rootScope.$apply();


            expect(WipsHttpStatusCodesConstant.unauthorized401).toEqual('401');
            expect($state.go).toHaveBeenCalledWith('login.enter-login-information');
            expect(User.getClearUserInformation).toHaveBeenCalled();
            expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'warning', multiple: false});
        });

        it('should take given object and create a danger message the server encountered an unexpected condition that prevented it from fulfilling the request.',function(){

           spyOn(WcAlertConsoleService, 'addMessage');

            spyOn($translate, 'instant').and.callFake(function() {
                return 'test';
            });
            testErrorData.status = WipsHttpStatusCodesConstant.internalServerError500;
            WipsHttpStatusCodesServices.getWipsHttpStatusCodes(testErrorData);

            $rootScope.$apply();

            expect(WipsHttpStatusCodesConstant.internalServerError500).toEqual('500');
            expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'danger', multiple: false});
        });

        it('should take given object and create a danger message the server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, or deceptive request routing).',function(){
            spyOn(WcAlertConsoleService, 'addMessage');

            spyOn($translate, 'instant').and.callFake(function() {
                return 'test';
            });

            testErrorData.status = WipsHttpStatusCodesConstant.badRequest400;
            WipsHttpStatusCodesServices.getWipsHttpStatusCodes(testErrorData);

            $rootScope.$apply();

            expect(WipsHttpStatusCodesConstant.badRequest400).toEqual('400');
            expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'danger', multiple: false});
        });

    });

});