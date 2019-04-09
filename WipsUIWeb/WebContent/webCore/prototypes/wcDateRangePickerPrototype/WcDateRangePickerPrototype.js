/**
 * @ngdoc service
 * @name WebCoreModule.service:WcDateRangePickerPrototype
 * @requires $rootScope
 * @description
 * The WcDateRangePickerPrototype provides a way to create paired date picker inputs for range selection. The pickers are
 * aware of the dates picked in either picker and disallow selections that are then out of range.
 */
'use strict';
angular.module('WebCoreModule')
	.factory('WcDateRangePickerPrototype', ['$rootScope', function($rootScope) {
		function WcDateRangePickerPrototype(config) {
			var startDate, endDate, minDate, maxDate, startDateFieldName, endDateFieldName, form;

			if(config) {
				startDate = config.startDate ? config.startDate : null;
				endDate = config.endDate ? config.endDate : null;
				minDate = config.minDate ? config.minDate : null;
				maxDate = config.maxDate ? config.maxDate : null;
				startDateFieldName = config.startDateFieldName ? config.startDateFieldName : null;
				endDateFieldName = config.endDateFieldName ? config.endDateFieldName : null;
				form = config.form ? config.form : null;
			}

			this.isStartDateOpen = false;
			this.isEndDateOpen = false;

			this.load(startDate, endDate, minDate, maxDate);

			$rootScope.$watch(angular.bind(this, function() {
				return this.startDate;
			}), angular.bind(this, function(newValue) {
				this.minEndDate = newValue ? newValue : this.minDate;
			}));

			$rootScope.$watch(angular.bind(this, function() {
				return this.endDate;
			}), angular.bind(this, function(newValue) {
				this.maxStartDate = newValue ? newValue : this.maxDate;
			}));

			if(form && startDateFieldName && endDateFieldName) {
				$rootScope.$watch(function(){
					var dateDisabledArray = form.$error["date-disabled"];
					if(dateDisabledArray) {
						return dateDisabledArray.length;
					}
					return 0;
				}, angular.bind(this, function(newVal) {
					var dateDisabledArray = form.$error["date-disabled"];
					if(newVal > 0) {
						for(var i=0; i<dateDisabledArray.length; i++) {
							//this only operates on two date pickers now, so no need to look further in the createBookingForm.
							//we'd need to augment this if we were using more datepickers
							if (dateDisabledArray[i].$viewValue == form[startDateFieldName].$viewValue) {
								form[startDateFieldName].$setValidity('dateRange', !(dateDisabledArray[i].$error["date-disabled"]));
							}
							else {
								form[endDateFieldName].$setValidity('dateRange', !(dateDisabledArray[i].$error["date-disabled"]));
							}
						}
					} else {
						form[startDateFieldName].$setValidity('dateRange', true);
						form[endDateFieldName].$setValidity('dateRange', true);
					}
				}));
			}
			else {
				console.log('The DateRangePicker cannot support validation indicators without having been provided the formObj and the field names.');
			}
		};

		WcDateRangePickerPrototype.prototype.load = function(startDate, endDate, minDate, maxDate) {
			this.startDate = startDate || null; //The day the start datepicker defaults to
			this.endDate = endDate || null; //The day the end datepicker defaults to

			this.minDate = minDate || null; //The minimum day the start datepicker can have
			this.maxDate = maxDate || null; //The maximum day the end datepicker can have

			this.minStartDate = this.minDate;
			this.maxStartDate = this.endDate ? this.endDate : this.maxDate; //TODO: verify endDate <= maxDate

			this.minEndDate = this.startDate ? this.startDate : this.minDate; //TODO: verify startDate >= minDate
			this.maxEndDate = this.maxDate;
		};

		WcDateRangePickerPrototype.prototype.openStartDate = function($event) {
			$event.preventDefault();
			$event.stopPropagation();

			if (this.isStartDateOpen === true) {
				this.isStartDateOpen = false;
			} else {
				this.isStartDateOpen = true;
				this.isEndDateOpen = false;
			}
		};

		WcDateRangePickerPrototype.prototype.openEndDate = function($event) {
			$event.preventDefault();
			$event.stopPropagation();

			if (this.isEndDateOpen === true) {
				this.isEndDateOpen = false;
			} else {
				this.isEndDateOpen = true;
				this.isStartDateOpen = false;
			}
		};

		return WcDateRangePickerPrototype;
	}]);