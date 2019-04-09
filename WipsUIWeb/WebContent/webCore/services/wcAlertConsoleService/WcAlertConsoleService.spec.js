'use strict';

describe('WebCoreModule WcAlertConsoleService:', function() {

	var WcAlertConsoleService;

	beforeEach(function() {
		module('WebCoreModule');

		inject(function($injector) {
			WcAlertConsoleService = $injector.get('WcAlertConsoleService');
		});

		WcAlertConsoleService.removeMessages(true);
	});

	it('should be defined', function() {
		expect(WcAlertConsoleService).toBeDefined();
	});

	describe('addMessage():', function() {

		it('should add a message to the message array with the given properties', function() {
			expect(WcAlertConsoleService.getMessages()).toEqual([]);

			var expectedMessage = angular.toJson({message: 'test message', type: 'danger', visible: false, multiple: true, persist: true, timeout: 7});

			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger', visible: false});
			WcAlertConsoleService.addMessage({message: 'test message 2', type: 'danger', visible: false});

			var actualMessage = WcAlertConsoleService.getMessages()[0];
			delete actualMessage.uniqueishID;
			actualMessage = angular.toJson(actualMessage);

			expect(actualMessage).toBe(expectedMessage);
		});

		it('should not add a duplicate message if multiple is false', function() {
			expect(WcAlertConsoleService.getMessages()).toEqual([]);
			spyOn(WcAlertConsoleService, 'isMessageDuplicate').and.callThrough();

			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger', multiple: false});
			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger', multiple: false});

			expect(WcAlertConsoleService.isMessageDuplicate).toHaveBeenCalledWith('test message');
			expect(WcAlertConsoleService.getMessages().length).toBe(1);
		});

		it('should set defaults if not specified in message', function() {
			expect(WcAlertConsoleService.getMessages()).toEqual([]);

			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger'});

			var actualMessage = WcAlertConsoleService.getMessages()[0];
			delete actualMessage.uniqueishID;
			actualMessage = angular.toJson(actualMessage);

			var expectedMessage = angular.toJson({message: 'test message', type: 'danger', visible: true, multiple: true, persist: true, timeout: 7});

			expect(actualMessage).toBe(expectedMessage);
		});

	});

	describe('removeMessage():', function() {

		it('should remove all messages that matches the given uniqueishID', function() {

			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger'});
			var uniqID = WcAlertConsoleService.getMessages()[WcAlertConsoleService.getMessages().length-1].uniqueishID;
			WcAlertConsoleService.addMessage({message: 'test message', type: 'success'});

			WcAlertConsoleService.removeMessage(uniqID);

			expect(WcAlertConsoleService.getMessages().length).toBe(1);
		});

	});

	describe('removeMessages():', function() {

		it('should remove all non persistent messages', function() {
			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger'}); // danger is persistent
			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger', persist: true});
			WcAlertConsoleService.addMessage({message: 'test message', type: 'success'});

			WcAlertConsoleService.removeMessages();

			expect(WcAlertConsoleService.getMessages().length).toBe(2);
		});

		it('should remove all messages if includingPersistant is true', function() {
			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger'});
			WcAlertConsoleService.addMessage({message: 'test message', type: 'success', persist: true});
			WcAlertConsoleService.addMessage({message: 'test message', type: 'success'});

			WcAlertConsoleService.removeMessages(true);

			expect(WcAlertConsoleService.getMessages().length).toBe(0);
		});

	});

	describe('getMessages():', function() {

		it('should return all messages', function() {
			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger'});
			WcAlertConsoleService.addMessage({message: 'test message6', type: 'success', persist: true});
			WcAlertConsoleService.addMessage({message: 'test message5', type: 'danger'});
			WcAlertConsoleService.addMessage({message: 'test message9', type: 'success', persist: true});
			WcAlertConsoleService.addMessage({message: 'test message2', type: 'danger'});

			var actualMessages = WcAlertConsoleService.getMessages();

			expect(actualMessages.length).toBe(5);
		});

		it('should not return invisible messages if onlyVisible is true', function() {
			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger'});
			WcAlertConsoleService.addMessage({message: 'test message', type: 'success', persist: true});
			WcAlertConsoleService.addMessage({message: 'test message', type: 'success', visible: false});

			var actualMessages = WcAlertConsoleService.getMessages(true);

			expect(actualMessages.length).toBe(2);
		});

	});

	describe('getMessagesByType():', function() {

		it('should return all messages belonging to a specified type', function() {
			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger'});
			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger'});
			WcAlertConsoleService.addMessage({message: 'test message', type: 'success', persist: true});
			WcAlertConsoleService.addMessage({message: 'test message', type: 'success', persist: true});
			WcAlertConsoleService.addMessage({message: 'test message', type: 'warning'});

			var actualReturn = WcAlertConsoleService.getMessagesByType('success');

			expect(actualReturn.length).toBe(2);
		});

		it('should return nothing if no matches are found', function() {
			WcAlertConsoleService.addMessage({message: 'test message', type: 'success', persist: true});
			WcAlertConsoleService.addMessage({message: 'test message', type: 'warning'});

			var actualReturn = WcAlertConsoleService.getMessagesByType();

			expect(actualReturn.length).toBe(0);

			var actualReturn2 = WcAlertConsoleService.getMessagesByType('danger');

			expect(actualReturn2.length).toBe(0);
		});

	});

	describe('setup():', function() {

		it('should overwrite defaults for message behavior with given values', function() {
			expect(WcAlertConsoleService.getMessages()).toEqual([]);

			var actualReturn = WcAlertConsoleService.configureDefaults({visible: false, multiple: false, persist: true});

			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger'});

			var actualMessage = WcAlertConsoleService.getMessages()[0];

			expect(actualMessage.visible).toBe(false);
			expect(actualMessage.multiple).toBe(false);
			expect(actualMessage.persist).toBe(true);
		});

		it('should only overwrite defaults for message behavior for the specified defaults', function() {
			expect(WcAlertConsoleService.getMessages()).toEqual([]);

			var actualReturn = WcAlertConsoleService.configureDefaults({visible: undefined, multiple: null, persist: false});

			WcAlertConsoleService.addMessage({message: 'test message', type: 'danger'});

			var actualMessage = WcAlertConsoleService.getMessages()[0];

			expect(actualMessage.visible).toBe(true);
			expect(actualMessage.multiple).toBe(true);
			expect(actualMessage.persist).toBe(true);
		});


	});


});