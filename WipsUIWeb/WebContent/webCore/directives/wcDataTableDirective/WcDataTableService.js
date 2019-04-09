/**
 * @ngdoc service
 * @name WebCoreModule.service:WcDatatableService
 * @requires $q
 * @description
 * Helper service for the wcDataTable directive. This service allows the user to set which row is selected or retrieve data
 * from the selected row.
 */
'use strict';
angular.module('WebCoreModule')
	.service('WcDataTableService', ['$q', function($q) {

		var clickedRowData = {};

		this.setClickedRowData = function(data) {
			clickedRowData = data;
		};

		this.getClickedRowData = function() {
			return $q.when(clickedRowData);
		};

	}]);