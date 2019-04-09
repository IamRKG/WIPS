/**
 * @ngdoc service
 * @name WebCoreModule.service:WcAlertConsoleService
 * @requires $timeout
 * @requires _
 * @requires $interval
 * @description
 * The WcAlertConsoleService manages the array of app-wide alert messages that are made visible to the user. The service exposes
 * a configureDefaults function and an addMessage function. Messages added to the service are then displayed by the wcAlertConsole directive.
 */
'use strict';
angular.module('WebCoreModule')
	.service('WcAlertConsoleService', ['$timeout', '_', '$interval', function($timeout, _, $interval) {

		// allows changing of defaults
		this.configureDefaults = function(config) {

			if(config) {
				if(config.visible !== undefined && (config.visible == true || config.visible == false)) {
					visibleDefault = config.visible;
				}
				if(config.multiple !== undefined && (config.multiple == true || config.multiple == false)) {
					multipleDefault = config.multiple;
				}
				if(config.persist !== undefined && (config.persist == true || config.persist == false)) {
					persistDefault = config.persist;
				}
				if(config.timeout !== undefined && (config.parseInt(timeout) != 'NaN')) {
					timeoutDefault =config. timeout;
				}
				if(config.errorOnStateChange && (config.errorOnStateChange == true || config.errorOnStateChange == false)) {
					removeErrorOnStateChange = config.errorOnStateChange;
				}
			}
		};

		var visibleDefault = true;
		var multipleDefault = true;
		var persistDefault = false;
		var timeoutDefault = 7;
		var fadeOutAnimationLengthMS = 350;
		var removeErrorOnStateChange = true;

		this.getSettings = function() {
			var settings = {
				visible: visibleDefault,
				multiple: multipleDefault,
				persist: persistDefault,
				timeout: timeoutDefault,
				fadeOutAnimationLengthMS: fadeOutAnimationLengthMS,
				removeErrorOnStateChange: removeErrorOnStateChange
			};
			return settings;
		};

		var messages = [];

		this.isMessageDuplicate = function(msg) {

			var isDuplicate = false;

			for(var i = 0; i < messages.length; i++) {
				if(messages[i].message === msg) {
					isDuplicate = true;
				}
			}

			return isDuplicate;

		};
		// adds a message (compatible with current format)
		this.addMessage = function(messageInstance) {

			if(messageInstance) {
				if(messageInstance.visible == undefined) {
					messageInstance.visible = visibleDefault;
				}
				if(messageInstance.multiple == undefined) {
					messageInstance.multiple = multipleDefault;
				}
				if(messageInstance.persist == undefined) {
					messageInstance.persist = persistDefault;
				}
				if(messageInstance.timeout == undefined) {
					messageInstance.timeout = timeoutDefault;
				}
			};

			// create identifier for message
			var uniqueishID = _.uniqueId('alert_');
			messageInstance.uniqueishID = uniqueishID;

			// persist danger messages by default
			if(messageInstance.type === 'danger') {
				messageInstance.persist = true;
			}

			// prevent duplicate message if multiple=false
			if(messageInstance.multiple == false) {
				if(!this.isMessageDuplicate(messageInstance.message)) {
					messages.push(messageInstance);
				}
			} else {
				messages.push(messageInstance);
			}

			if(messageInstance.persist === false) {
				$interval(function() {
					for(var f = 0; f < messages.length; f++) {
						if(messages[f].uniqueishID === messageInstance.uniqueishID) {
							messages[f].fadeOut = true;
						}
					}
				}, messageInstance.timeout * 1000 - fadeOutAnimationLengthMS,1);

				$interval(angular.bind(this, function() {
					this.removeMessage(messageInstance.uniqueishID);
				}), messageInstance.timeout * 1000,1);

				$interval(angular.bind(this, function() {
					this.updateTimeout(messageInstance.uniqueishID);
				}), 1000, messageInstance.timeout);
			}
		};

		this.updateTimeout = function(uniqueishID) {
			for(var i = 0; i < messages.length; i++) {
				if(messages[i].uniqueishID === uniqueishID) {
					messages[i].timeout --;
				}
			}
		};

		// removes a specific message
		this.removeMessage = function(uniqueishID, manualFade) {

			for(var i = 0; i < messages.length; i++) {
				if(messages[i].uniqueishID === uniqueishID) {

					if(manualFade) {
						messages[i].fadeOut = true;
						$timeout(angular.bind(this, function() {
							this.removeMessage(uniqueishID);
						}), fadeOutAnimationLengthMS);
						return;
					} else {
						messages.splice(i, 1);
						return;
					}

				}
			}

		};

		// removes all messages (includingPersistent - set to true to remove persistent msg)
		this.removeMessages = function(includingPersistent) {

			for(var i = 0; i < messages.length; i++) {
				if(messages[i].persist === false || includingPersistent === true) {
					messages.splice(i, 1);
					// reset index for accurate loop
					i = i - 1;
				}
			}

		};

		this.removeErrorMessages = function() {
			for(var i = 0; i < messages.length; i++) {
				if(messages[i].type === 'danger') {
					messages.splice(i, 1);
					// reset index for accurate loop
					i = i - 1;
				}
			}
		};

		// returns all messages (onlyVisible to only get visible messages)
		this.getMessages = function(onlyVisible) {
			var messagesToReturn = [];

			for(var i = 0; i < messages.length; i++) {
				if((onlyVisible === true && messages[i].visible === true) || onlyVisible === undefined) {
					messagesToReturn.push(messages[i]);
				}
			}

			return messagesToReturn;
		};

		// returns messages that match specific type
		this.getMessagesByType = function(typeToMatch) {

			var messagesToReturn = [];

			for(var i = 0; i < messages.length; i++) {
				if(messages[i].type === typeToMatch) {
					messagesToReturn.push(messages[i]);
				}
			}

			return messagesToReturn;
		};


	}]);