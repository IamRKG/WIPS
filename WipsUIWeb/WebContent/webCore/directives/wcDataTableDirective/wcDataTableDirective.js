/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcDataTable
 * @restrict A
 * @requires ngModel
 * @scope
 * @param {object} wcDataTable Options for overriding the table's default configuration
 * @param {string=} headerTemplate Optional string pointing to a file containing HTML defining the table's headers
 * @param {array} columns array of objects defining how the columns relate to the dataset and how they should be rendered
 * @param {array=} columnDefs array of objects defining how the columns should be hidden or sorted by default
 * @param {object=} responsiveColumnDefs array of objects defining how the columns should respond when the viewport with changes
 * @param {array} data the array of data to be displayed in the table
 * @param {number=} checkBoxCol the index of the column to be used to house the table's checkboxes
 * @param {number=} radioButtonCol the index of the column to be used to house the table's radio buttons
 * @description
 * The wcDataTable directive wraps the functionality of the jQuery DataTables component into an Angular context construct.
 * Using the various parameters, the screen designer can implement a table with various features offered by the jQuery component.
 */
'use strict';
angular.module('WebCoreModule')
	.directive('wcDataTable', ['$compile', '$window', '_',
		function($compile, $window, _) {
			return {
				restrict: 'A',
				require: ['wcDataTable', '?ngModel'],
				scope: {
					headerTemplate: '@headerTemplate',
					overrideOptions: '@wcDataTable',
					columns: '=columns',
					columnDefs: '@columnDefs',
					responsiveColumnDefs: '@responsiveColumnDefs',
					data: '=data',
					checkBoxCol: '@checkBoxCol',
					radioButtonCol: '@radioButtonCol',
					selectedValue: '@selectedValue',
					ngModel: '=ngModel',
					name: '@name'
					//          showPaginationButtonText: '@showPaginationButtonText'
				},
				transclude: true,
				replace: true,
				template: '<div ng-transclude></div>',
				controller: ['$compile', '$scope', '$element', '$attrs', 'WcDataTableService',
					function($compile, $scope, $element, $attrs, WcDataTableService) {

						this.defaultOptions = {
							bAutoWidth: false,
							bStateSave: false,
							bPaginate: true,
							sPaginationType: 'bootstrap_full_numbers',
							sDom: '<"row paginator paginator-top"<"col-xs-3"l><"col-xs-4 text-right"i><"col-xs-5 text-right"p>>t<"row paginator paginator-bottom"<"col-xs-3"l><"col-xs-4 text-right"i><"col-xs-5 text-right"p>>>',
							bLengthChange: true,
							bFilter: false,
							bDestroy: true,
							fnCreatedRow: function(row, data, dataIndex) {
								var theRow = $(row)[0];
								var theCells = $(theRow).find('td');
								var cellWithLinks = $($(theCells)[0]);

								// updated clickedRowData
								$(theRow).find('a').on('click', angular.bind(this, function() {
									// use index to find value in original data set because mRender could have overwritten the local copy
									WcDataTableService.setClickedRowData($scope.data[dataIndex]);
								}));

								//we only compile the first cell in each row, as this currently is the only location for angular controls
								$compile(cellWithLinks.contents())($scope.$parent);
							},
							aaSorting: [
								[2, 'asc']
							]
						};

						this.options = {};
						this.useResponsiveColumns = false;
						this.currentBreakpoint = -1;
						this.previousBreakPoint = -1;
						this.toggleCheckAll = $scope.checkBoxCol == parseInt($scope.checkBoxCol);

						this.checkAuthorization = function() {
						};

						this.getToggleCheckAllExists = function() {
							return this.toggleCheckAll;
						};

						this.initialize = function(ngModelController) {
							//first thing - redirect the alert warnings to the console
							//credit to stack overflow poster 'orad': http://stackoverflow.com/questions/11941876/correctly-suppressing-warnings-in-datatables
							$window.alert = (function() {
								var nativeAlert = $window.alert;
								return function(message) {
									$window.alert = nativeAlert;
									message.indexOf("DataTables warning") === 0 ?
										console.warn(message) :
										nativeAlert(message);
								};
							})();

							this.table = $element.find('table');

							// apply DataTable options, use defaults if none specified by user
							var options = {};
							if($scope.overrideOptions) {
								options = $scope.$eval($scope.overrideOptions);
							}

							//column configuration
							if($scope.columns) {
								options.aoColumns = angular.copy($scope.columns);
							}

							this.showcheckBoxColumn = $scope.checkBoxCol == parseInt($scope.checkBoxCol);
							this.showRadioColumn = $scope.radioButtonCol == parseInt($scope.radioButtonCol);
							this.inValidCombination = this.showcheckBoxColumn && this.showRadioColumn;
							this.hasInputField = this.showcheckBoxColumn || this.showRadioColumn;

							if(this.inValidCombination) {
								var errorMessage = 'YOU CANNOT HAVE A DATATABLE WITH BOTH A CHECKBOX COLUMN AND A RADIOBUTTON COLUMN - IT MESSES WITH THE DATA';
								console.error(errorMessage);
								$element.prepend('<div class="alert alert-danger"><td colspan=""><p>' + errorMessage + '</p></div>');
								return false;
							}

							// Build required text for input attributes
							var requiredString = $attrs.required ? 'required' : '';
							var inputName = $scope.name ? 'name="' + $scope.name + '"' : '';

							// Error msg if has input field without ng-model
							if(this.hasInputField && !$attrs.ngModel) {
								var errorMessage = 'Must define the "ng-model" attribute IF a checkbox or radio column index is defined.';
								console.error(errorMessage);
								$element.prepend('<div class="alert alert-danger"><td colspan=""><p>' + errorMessage + '</p></div>');
							}
							// Error msg if has input field without name
							if(this.hasInputField && !inputName) {
								var errorMessage = 'Must define the "name" attribute IF a checkbox or radio column index is defined.';
								console.error(errorMessage);
								$element.prepend('<div class="alert alert-danger"><td colspan=""><p>' + errorMessage + '</p></div>');
							}

							// Error msg if has input field without the selected value (primary key)
							if(this.hasInputField && !$scope.selectedValue) {
								var errorMessage = 'Must define the "selected-value" attribute IF a checkbox or radio column index is defined.';
								console.error(errorMessage);
								$element.prepend('<div class="alert alert-danger"><td colspan=""><p>' + errorMessage + '</p></div>');
							}


							// Error msg if required without input field
							if(!this.hasInputField && requiredString) {
								var errorMessage = 'Must define a checkbox or radio column index IF the HTML5 required attribute is set';
								console.error(errorMessage);
								$element.prepend('<div class="alert alert-danger"><td colspan=""><p>' + errorMessage + '</p></div>');
							}

							//CheckBox column
							if(this.showcheckBoxColumn) { //so that check boxes show even if chosen column is index 0
								var checkBoxCol = {
									sTitle: '<input type="checkbox" class="toggle-check-all"/>',
									sClass: 'text-center',
									mData: $scope.selectedValue,
									mRender: function(data, type, full) {

										if(angular.toJson(data) == angular.toJson(full[$scope.selectedValue]) && (new String(full[$scope.selectedValue]).indexOf('<label') == -1)) {
											// will overwrite row's data value
											return '<label class="sr-only" for="checkbox_' + full[$scope.selectedValue] + '">Select ' + full[$scope.selectedValue] + '</label><input type="checkbox" id="checkbox_' + full[$scope.selectedValue] + '" ' + inputName + ' ' + requiredString + ' class="checkbox table-check-box" value="' + full[$scope.selectedValue] + '"/>';
										} else {
											return data;
										}


									},
									bSortable: false
								};

								if(!(angular.toJson(options.aoColumns[$scope.checkBoxCol]) == angular.toJson(checkBoxCol))) {
									//check if checkbox column is already here, so we don't add it multiple times
									options.aoColumns.splice($scope.checkBoxCol, 0, checkBoxCol);
								}

								$(this.table).addClass('table-toggle-check-all');
							}

							//RadioButton column
							if(this.showRadioColumn) { //so that radio buttons show even if chosen column is index 0
								var radioButtonCol = {
									sTitle: '',
									sClass: 'text-center',
									mData: $scope.selectedValue,
									mRender: function(data) {
										if(data) {
											return '<label class="sr-only" for="radio_' + data + '">Select ' + data + '</label><input type="radio" id="radio_' + data + '" ' + inputName + ' ' + requiredString + ' class="radio table-radio-button" value="' + data + '"/>';
										} else {
											return '';
										}
									},
									bSortable: false
								};
								if(!(angular.toJson(options.aoColumns[$scope.radioButtonCol]) == angular.toJson(radioButtonCol))) {
									//check if radioButton column is already here, so we don't add it multiple times
									options.aoColumns.splice($scope.radioButtonCol, 0, radioButtonCol);
								}
							}

							//column configuration
							if($scope.columnDefs) {
								options.aoColumnDefs = $scope.$eval($scope.columnDefs);
							}

							if($scope.responsiveColumnDefs) {
								this.responsiveColumnDefs = $scope.$eval($scope.responsiveColumnDefs);
								if(this.responsiveColumnDefs.initial === undefined) {
									throw 'DataTables Error: No initial state defined.';
								}

								this.useResponsiveColumns = true;
								this.maxBreakpoint = _.max(this.responsiveColumnDefs.breakpoints);
							}

							// pass options to dataTable
							this.defineTable(options);

							if($scope.data) {
								this.dataTable.fnAddData($scope.data);

								// on data change, redo directive with original DOM
								$scope.$watch(function() {
									return $scope.data;
								}, angular.bind(this, function(oldVal, newVal) {
									if(oldVal != newVal) {
										this.defineTable(this.options);
										this.dataTable.fnClearTable();
										this.dataTable.fnAddData($scope.data);
										this.createInputColumn(ngModelController);
									}
								}));
							}


							this.createInputColumn = function(ngModelController) {

								var jqTable = $(this.table);

								if(this.showRadioColumn) {
									var rows = this.table.fnGetNodes();

									//check if there is a radio button already selected
									if($scope.$parent.$eval($attrs.ngModel)) {
										$(rows).find('input[id=radio_' + $scope.$parent.$eval($attrs.ngModel) + ']').prop('checked', true);
										$(rows).find('input[id=radio_' + $scope.$parent.$eval($attrs.ngModel) + ']').parent().parent().addClass('selected');
									}

									angular.element($(rows).find('.table-radio-button')).on('change', function(event) {
										$.each(rows, function() {
											$(this).removeClass('selected');
											$(this).find('input[type="radio"]').prop('checked', false);
										});
										$(this).parent().parent().addClass('selected');
										$(this).prop('checked', true);
										ngModelController.$setViewValue($(this).val());
										$scope.$apply();
									});
								}

								if(this.showcheckBoxColumn) {


									$(this.table).addClass('table-toggle-check-all');

									var oldView = [];
									var newView = [];
									var latestChecked;

									//this.CheckBoxAdjust = function() {
									var rows = this.table.fnGetNodes();


									//check if there are check boxes already selected
									if($scope.$parent.$eval($attrs.ngModel)) {
										angular.forEach($scope.$parent.$eval($attrs.ngModel), function(item) {
											$(rows).find('input[id=checkbox_' + item + ']').prop('checked', true);
											$(rows).find('input[id=checkbox_' + item + ']').parent().parent().addClass('selected');
										});
									}

									angular.element($(rows).find('.table-check-box')).on('change', function(event) {
										if(!ngModelController.$viewValue) { //first time you check a box
											$(this).parent().parent().addClass('selected');
											ngModelController.$setViewValue([$(this).val()]);
										} else if(this.checked) { //checking a box
											$(this).parent().parent().addClass('selected');
											oldView = ngModelController.$viewValue;
											latestChecked = [$(this).val()];
											newView = _.uniq(oldView.concat(latestChecked));
											ngModelController.$setViewValue(newView);
										} else { //un-checking a box
											$(this).parent().parent().removeClass('selected');
											newView = _.without(ngModelController.$viewValue, $(this).val());
											ngModelController.$setViewValue(newView);
										}
										;
										/* -----Start Toggle Check All Functionality -------*/
										var numCheckBoxes = jqTable.find('.table-check-box').length;
										var numSelectedRows = jqTable.find('.selected').length;
										if(numCheckBoxes == numSelectedRows) {
											jqTable.find('thead th input.toggle-check-all').prop('checked', true);
										} else {
											jqTable.find('thead th input.toggle-check-all').prop('checked', false);
										}

										if($scope.$root.$$phase != '$apply' && $scope.$root.$$phase != '$digest') {
											//avoid $scope.apply is already in progress error
											$scope.$apply();
										}
									});

									angular.element(jqTable.find('thead th input.toggle-check-all')).on('change', function(event) {
										if(this.checked) { //checking the box
											$.each(jqTable.find('.table-check-box'), function() {
												if($(this).prop('checked') == false) {
													$(this).click();
												}
											});
										} else { //un-checking the box
											$.each(jqTable.find('.table-check-box'), function() {
												if($(this).prop('checked')) {
													$(this).click();
												}
											});
										}
										;
									});

									//check or uncheck the toggle check all box on pagination and resize
									this.table.bind('draw', function() {
										var numCheckBoxes = jqTable.find('.table-check-box').length;
										var numSelectedRows = jqTable.find('.selected').length;
										if(numCheckBoxes == numSelectedRows) {
											jqTable.find('thead th input.toggle-check-all').prop('checked', true);
										} else {
											jqTable.find('thead th input.toggle-check-all').prop('checked', false);
										}
									});
									/* -----End Toggle Check All Functionality -------*/
									//};

									//this.CheckBoxAdjust();
								}
							};


							return true;
						};

						this.defineTable = function(options) {

							options = _.defaults(options, this.defaultOptions);
							angular.extend(this.options, options);

							if(this.dataTable) {
								this.dataTable.fnDestroy();
							}
							this.dataTable = this.table.dataTable(this.options);
						};


						this.adjustColumns = function() {
							var settings = this.dataTable.fnSettings();
							var options = {};
							var columnsToHide = null;
							var pagingTextClass = '';

							if($($window).width() > this.maxBreakpoint) {
								this.previousBreakPoint = this.currentBreakPoint;
								this.currentBreakPoint = -1;


								columnsToHide = this.responsiveColumnDefs.initial.columns;
								options = {
									"bPaginate": this.responsiveColumnDefs.initial.paging,
									"sDom": this.responsiveColumnDefs.initial.dom,
									"sPaginationType": this.responsiveColumnDefs.initial.pagination,
									"oLanguage": {
										"oPaginate": {
											"sShowPagingText": this.responsiveColumnDefs.initial.showPagingText
										}
									}
								};
							}

							for(var i = 0; i <= this.responsiveColumnDefs.breakpoints.length; i++) {
								if($($window).width() < this.responsiveColumnDefs.breakpoints[i]) {
									this.previousBreakPoint = this.currentBreakPoint;
									this.currentBreakPoint = this.responsiveColumnDefs.breakpoints[i];

									columnsToHide = this.responsiveColumnDefs.columns[i];
									options = {
										"bPaginate": this.responsiveColumnDefs.paging[i],
										"sDom": this.responsiveColumnDefs.dom[i],
										"sPaginationType": this.responsiveColumnDefs.pagination[i],
										"oLanguage": {
											"oPaginate": {
												"sShowPagingText": this.responsiveColumnDefs.showPagingText[i]
											}
										}
									};

									break;
								}
							}
							;

							if(this.currentBreakPoint !== this.previousBreakPoint) {
								var paginationOptions = angular.merge(this.options, options);
								this.defineTable(paginationOptions);

								for(var i = 0; i < settings.aoColumns.length; i++) {
									if(_.contains(columnsToHide, i)) {
										this.dataTable.fnSetColumnVis(i, false);
									} else if(!settings.aoColumns[i].bVisible) {
										this.dataTable.fnSetColumnVis(i, true);
									}
								}

								$element.find('.paginator select').addClass('form-control input-sm');
							}
						};
					}
				],
				compile: function(iElement, iAttrs) {
					var TEMPLATE_REGEXP = /<.+>/;

					return {
						pre: function($scope, iElement, iAttrs, controllers) {

							if($scope.headerTemplate) {
								var headerTemplate = $scope.headerTemplate;

								if(headerTemplate && !TEMPLATE_REGEXP.test(headerTemplate)) {
									headerTemplate = $.ajax({
										type: "GET",
										url: headerTemplate,
										async: false
									}).responseText;
								}

								headerTemplate = angular.element(headerTemplate);
								$compile(headerTemplate)($scope);

								var table = iElement.find('table');
								var thead = table.find('thead');
								if(thead.exists()) {
									thead.replaceWith(headerTemplate);
								} else {
									table.append(headerTemplate);
								}
							}
						},
						post: function($scope, iElement, iAttrs, controllers) {
							var datatableController = controllers[0];
							var ngModelController = controllers[1];

							if(datatableController.initialize(ngModelController)) {
								if(datatableController.useResponsiveColumns) {
									datatableController.adjustColumns();

									angular.element($window).bind('resize', function() {
										datatableController.adjustColumns();
										datatableController.createInputColumn(ngModelController);
										$scope.$apply();
									});
								}

								datatableController.createInputColumn(ngModelController);

							}

						}
					};
				}
			};
		}
	]);
