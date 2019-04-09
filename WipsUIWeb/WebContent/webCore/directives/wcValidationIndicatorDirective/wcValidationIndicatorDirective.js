/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcValidationIndicator
 * @restrict A
 * @priority 1
 * @param {string=} wcValidationIndicator string defining the field identifier text that should be used in the validation message
 * @description
 * The wcValidationIndicator directive utilizes Angular's isValid and isRequired checks to display messages according to the error
 * state at the field level. The directive supports all form input types that are currently a part of the FRF.
 */
'use strict';
angular.module('WebCoreModule')
	.directive('wcValidationIndicator', function() {
		return {
			restrict: 'A',
			priority: 1,
			controller: ['$timeout', '$translate', function($timeout, $translate) {
				this.timeout = $timeout;
				this.translate = $translate;
			}],
			link: {
				pre: function(scope, elem, attr, ctrl) {
					if(!attr.wcValidationIndicator) {
						ctrl.fieldName = 'Value';
					}
					else {
						ctrl.fieldName = attr.wcValidationIndicator;
					}
					if(!attr.invalidText) {
						ctrl.invalidText = ctrl.translate.instant('application.errors.isInvalidError');
					}
					else {
						ctrl.invalidText = attr.invalidText;
					}
					if(!attr.requiredText) {
						ctrl.requiredText = ctrl.translate.instant('application.errors.isRequiredError');
					}
					else {
						ctrl.requiredText = attr.requiredText;
					}
				},
				post: function(scope, elem, attr, ctrl) {
					//wrap in timeout of zero seconds to force a digest. This ensures nested directives finish before the indicator directive runs
					ctrl.timeout(function() {
						//fieldset is caps here thanks to the browser defining it that way
						if(!elem[0].form || elem[0].tagName == 'FIELDSET') {
							elem = $(elem).find('input');
						}
						var formName = elem[0].form.name;
						var ngFormObj;
						//modals use ctrl.form.formName instead of ctrl.formName
						//handle it here
						if(formName.indexOf(".") > -1) {
							formName = formName.split('.');
							ngFormObj = scope.$parent[formName[0]][formName[1]];
						}
						else {
							//break out of isolate scope and then walk the tree for the form obj
							ngFormObj = scope.$parent[formName];
						}

						var elemName = elem[0].name;
						//sometimes the reset element will not carry the name attr (I'm looking at you, ui-select).
						//in this case, instead grab it off of the attr object that was passed in
						if(!elemName) {
							elemName = attr.name;
						}
						var elemCtrl;
						//need to select the controller in a different way if we are within a modal
						if(angular.isArray(formName)) {
							//take the controller's name off of the ng-model.
							//todo - if there's a better way to do this, DO IT!!
							var ctrlName = elem[0].attributes["ng-model"].nodeValue.split('.')[0];
							elemCtrl = scope.$parent[ctrlName];
						}
						else {
							elemCtrl = elem.controller();
						}
						var formGroup = $(elem).closest('.form-group')[0];
						//datatables will not be in a formgroup. add one and refer to it
						if(!formGroup) {
							formGroup = $('<div class="form-group"></div>').insertBefore('#' + elem.context.attributes.id.value);
						}

						//build error messages
						var invalidMessage = ctrl.invalidText + ' ' + ctrl.fieldName + '.';
						var requiredMessage = ctrl.fieldName + ' ' + ctrl.requiredText;

						//handy regex from http://stackoverflow.com/questions/8955533/javascript-jquery-split-camelcase-string-and-add-hyphen-rather-than-space
						var elemNameSnakeCase = elemName.replace(/([a-z])([A-Z])/g, '$1-$2').toLowerCase();
						var invalidId = elemNameSnakeCase + '-invalid';
						var requiredId = elemNameSnakeCase + '-required';
						var invalidMessageDOM = $('<span id="' + invalidId + '" class="label label-danger"><span class="glyphicon glyphicon-remove-sign"></span><span class="label-text">' + invalidMessage + '</span></span>');
						var requiredMessageDOM = $('<span id="' + requiredId + '" class="label label-danger"><span class="glyphicon glyphicon-remove-sign"></span><span class="label-text">' + requiredMessage + '</span></span>');

						var watchListener = function() {
							//when the validation error occurs, insert the error messages and mark the container
							if(!ngFormObj[elemName].$valid && elemCtrl.isFormSubmitted) {
								if(ngFormObj[elemName].$error.required === true) {
									$(formGroup).find('#' + invalidId).remove();
									$(formGroup).append(requiredMessageDOM);
								} else {
									$(formGroup).find('#' + requiredId).remove();
									$(formGroup).append(invalidMessageDOM);
								}
								$(formGroup).addClass('has-error');
							} else {
								$(formGroup).removeClass('has-error');
								$(formGroup).find('#' + invalidId + ', #' + requiredId).remove();
							}
						};

						scope.$watch(function() {
							return ngFormObj[elemName].$valid;
						}, function() {
							watchListener();
						});

						scope.$watch(function() {
							return elemCtrl.isFormSubmitted;
						}, function() {
							watchListener();
						});

						scope.$watch(function() {
							return ngFormObj[elemName].$error;
						}, function() {
							watchListener();
						}, true);
					}, 0);
				}
			}
		};
	})
;
