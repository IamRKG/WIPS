'use strict';
var LoginPage= require('../../../../loginModule/states/login/enterLoginInformation/enterLoginInformation.page.e2e.js');
var HomePendingApprovalPage = require('../../../states/homePendingApproval/HomePendingApproval.page.e2e.js');
var ATPPendingApprovalPage = require('../atpPendingApproval/ATPPendingApproval.page.e2e.js');
var ATPRecapPage = require('../atpRecap/ATPRecap.page.e2e.js');
var ATPStrategyDetailsPage = require('./ATPStrategyDetails.page.e2e.js');

describe('ATP Strategy Details Page',function(){

    var loginPage = new LoginPage();
    var homePendingApprovalPage = new HomePendingApprovalPage();
    var atpPendingApprovalPage = new ATPPendingApprovalPage();
    var atpRecapPage = new ATPRecapPage();
    var atpStrategyDetailsPage = new ATPStrategyDetailsPage();

    beforeEach(function() {
        loginPage.load();
        loginPage.navigateToHome();
        homePendingApprovalPage.navigateToATPListPage();
        atpPendingApprovalPage.navigateToATPRecapPage();
        atpRecapPage.navigateToStrategyDetailsPage();
    });

    it('Should display the current page',function(){
        expect(atpStrategyDetailsPage.isCurrentPage()).toBeTruthy();
    });

    it('Should display work flow job code',function(){
        expect(atpStrategyDetailsPage.visibleJobCode()).toBeTruthy();
    });

    it('Should display plant name by location',function(){
        expect(atpStrategyDetailsPage.plantNameBylocation()).toContain('01-ATLANTA');
    });

    it('Should have a national company for every plant in the accordion',function(){
        var numberOfPlant = atpStrategyDetailsPage.numberOfPlant()
        var numberOfNationalCompany = atpStrategyDetailsPage.numberOfNationalCompany()
        expect(numberOfNationalCompany).toEqual(numberOfPlant);
    });

    it('Should have strategy suppliers table inside accordion panel',function(){
        expect(atpStrategyDetailsPage.isStrategySuppliersTable()).toBeTruthy();
    });

    it('should display correct columns', function() {
        var customerColumnHeaders = ['Supplier', 'Sourcing %','Supplier', 'Sourcing %','Supplier', 'Sourcing %','Supplier', 'Sourcing %'];
        //use map implementation for array of promise handling as talked about here:
        //http://stackoverflow.com/questions/21736191/protractor-how-to-get-the-result-of-an-array-of-promises-into-another-array
        atpStrategyDetailsPage.columnHeadersText().then(function(result){
            for(var i=0; i<result.length; i++){
                expect(result[i]).toEqual(customerColumnHeaders[i]);
            }
        });

    });

});