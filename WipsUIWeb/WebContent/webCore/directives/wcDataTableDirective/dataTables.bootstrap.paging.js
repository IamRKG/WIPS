/* Set the defaults for DataTables initialisation */
$.extend(true, $.fn.dataTable.defaults, {
	"sDom": "<'row paginator'<'col-xs-6'l><'col-xs-6'f>r>t<'row paginator'<'col-xs-6'i><'col-xs-6'p>>",
	"sPaginationType": "bootstrap_full_numbers",
	"oLanguage": {
		"sLengthMenu": "Results per page: _MENU_",
		"sInfo": "Showing Results: _START_ - _END_ of _TOTAL_"
	}
});


/* Default class modification */
$.extend($.fn.dataTableExt.oStdClasses, {
	sWrapper: "dataTables_wrapper form-inline"
});

/* Bootstrap style pagination control */
$.extend($.fn.dataTableExt.oPagination, {
	"bootstrap_two_button": {
		"fnInit": function(oSettings, nPaging, fnCallbackDraw) {
			var oLang = oSettings.oLanguage.oPaginate;
			var oClasses = oSettings.oClasses;
			var disabledClass = 'disabled ' + oClasses.sPageButtonStaticDisabled;

			var sAppend =
				'<ul class="pagination">' +
				'<li class="previous ' + disabledClass + '"><a href="javascript:;" class="' + oClasses.sPageButton + " " + oClasses.sPagePrevious + '" tabindex="' + oSettings.iTabIndex + '" role="button"><span class="' + oClasses.sPagePreviousText + '">' + oLang.sPrevious + '</span></a></li>' +
				'<li class="next ' + disabledClass + '"><a href="javascript:;" class="' + oClasses.sPageButton + " " + oClasses.sPageNext + '" tabindex="' + oSettings.iTabIndex + '" role="button"><span class="' + oClasses.sPageNextText + '">' + oLang.sNext + '</span></a></li>' +
				'</ul>';
			$(nPaging).append(sAppend);

			var els = $('a', nPaging);
			var nPrevious = els[0],
				nNext = els[1];

			/* ID the first elements only */
			if (!oSettings.aanFeatures.p) {
				nPaging.id = oSettings.sTableId + '_paginate';
				nPrevious.id = oSettings.sTableId + '_previous';
				nNext.id = oSettings.sTableId + '_next';

				nPrevious.setAttribute('aria-controls', oSettings.sTableId);
				nNext.setAttribute('aria-controls', oSettings.sTableId);
			}

			var fnClickHandler = function(e) {
				e.preventDefault();
				if (oSettings.oApi._fnPageChange(oSettings, e.data.action)) {
					fnCallbackDraw(oSettings);
				}
			};

			oSettings.oApi._fnBindAction(nPrevious, {
				action: "previous"
			}, fnClickHandler);
			oSettings.oApi._fnBindAction(nNext, {
				action: "next"
			}, fnClickHandler);
		},

		"fnUpdate": function(oSettings, fnCallbackDraw) {
			if (!oSettings.aanFeatures.p) {
				return;
			}

			var iPages = Math.ceil((oSettings.fnRecordsDisplay()) / oSettings._iDisplayLength);
			var iCurrentPage = Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength) + 1;
			var oClasses = oSettings.oClasses;
			var disabledClass = 'disabled ' + oClasses.sPageButtonStaticDisabled;

			/* Loop over each instance of the pager */
			var an = oSettings.aanFeatures.p;
			for (var i = 0; i < an.length; i++) {
				/* Update the permanent button's classes */
				if (iCurrentPage == 1) {
					$('li:eq(0)', an[i]).addClass(disabledClass);
				} else {
					$('li:eq(0)', an[i]).removeClass(disabledClass);
				}
				if (iPages === 0 || iCurrentPage === iPages || oSettings._iDisplayLength === -1) {
					$('li:eq(-1)', an[i]).addClass(disabledClass);
				} else {
					$('li:eq(-1)', an[i]).removeClass(disabledClass);
				}
			}
		}
	},
	"bootstrap_normal": {
		"fnInit": function(oSettings, nPaging, fnCallbackDraw) {
			var oLang = oSettings.oLanguage.oPaginate;
			var oClasses = oSettings.oClasses;
			var disabledClass = 'disabled ' + oClasses.sPageButtonStaticDisabled;

			var sAppend =
				'<ul class="pagination">' +
				'<li class="previous ' + disabledClass + '"><a href="javascript:;" class="' + oClasses.sPageButton + " " + oClasses.sPagePrevious + '" tabindex="' + oSettings.iTabIndex + '" role="button"><span class="' + oClasses.sPagePreviousText + '">' + oLang.sPrevious + '</span></a></li>' +
				'<li class="next ' + disabledClass + '"><a href="javascript:;" class="' + oClasses.sPageButton + " " + oClasses.sPageNext + '" tabindex="' + oSettings.iTabIndex + '" role="button"><span class="' + oClasses.sPageNextText + '">' + oLang.sNext + '</span></a></li>' +
				'</ul>';
			$(nPaging).append(sAppend);

			var els = $('a', nPaging);
			var nPrevious = els[0],
				nNext = els[1];

			/* ID the first elements only */
			if (!oSettings.aanFeatures.p) {
				nPaging.id = oSettings.sTableId + '_paginate';
				nPrevious.id = oSettings.sTableId + '_previous';
				nNext.id = oSettings.sTableId + '_next';

				nPrevious.setAttribute('aria-controls', oSettings.sTableId);
				nNext.setAttribute('aria-controls', oSettings.sTableId);
			}

			var fnClickHandler = function(e) {
				e.preventDefault();
				if (oSettings.oApi._fnPageChange(oSettings, e.data.action)) {
					fnCallbackDraw(oSettings);
				}
			};

			oSettings.oApi._fnBindAction(nPrevious, {
				action: "previous"
			}, fnClickHandler);
			oSettings.oApi._fnBindAction(nNext, {
				action: "next"
			}, fnClickHandler);
		},

		"fnUpdate": function(oSettings, fnCallbackDraw) {
			if (!oSettings.aanFeatures.p) {
				return;
			}

			var iPageCount = $.fn.dataTableExt.oPagination.iFullNumbersShowPages;
			var iPageCountHalf = Math.floor(iPageCount / 2);
			var iPages = Math.ceil((oSettings.fnRecordsDisplay()) / oSettings._iDisplayLength);
			var iCurrentPage = Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength) + 1;
			var iStartButton, iEndButton;
			var fnBind = function(j) {
				oSettings.oApi._fnBindAction(this, {
					"page": j + iStartButton - 1
				}, function(e) {
					/* Use the information in the element to jump to the required page */
					oSettings.oApi._fnPageChange(oSettings, e.data.page);
					fnCallbackDraw(oSettings);
					e.preventDefault();
				});
			};

			/* Pages calculation */
			if (oSettings._iDisplayLength === -1) {
				iStartButton = 1;
				iEndButton = 1;
				iCurrentPage = 1;
			} else if (iPages < iPageCount) {
				iStartButton = 1;
				iEndButton = iPages;
			} else if (iCurrentPage <= iPageCountHalf) {
				iStartButton = 1;
				iEndButton = iPageCount;
			} else if (iCurrentPage >= (iPages - iPageCountHalf)) {
				iStartButton = iPages - iPageCount + 1;
				iEndButton = iPages;
			} else {
				iStartButton = iCurrentPage - Math.ceil(iPageCount / 2) + 1;
				iEndButton = iStartButton + iPageCount - 1;
			}

			/* Build the dynamic list */
			var oClasses = oSettings.oClasses;
			var activeClass = 'active ' + oClasses.sPageButtonActive;
			var disabledClass = 'disabled ' + oClasses.sPageButtonStaticDisabled;

			var sList = "";
			for (var i = iStartButton; i <= iEndButton; i++) {
				sList += (iCurrentPage !== i) ?
					'<li><a href="javascript:;" class="' + oClasses.sPageButton + '" tabindex="' + oSettings.iTabIndex + '" role="button">' + oSettings.fnFormatNumber(i) + '</a></li>' :
					'<li class="' + activeClass + '"><a href="javascript:;" class="' + oClasses.sPageButton + '" tabindex="' + oSettings.iTabIndex + '" role="button">' + oSettings.fnFormatNumber(i) + '</a></li>';
			}

			/* Loop over each instance of the pager */
			var an = oSettings.aanFeatures.p;
			for (var i = 0; i < an.length; i++) {
				if (!an[i].hasChildNodes()) {
					continue;
				}

				/* Update the permanent button's classes */
				if (iCurrentPage == 1) {
					$('li:eq(0)', an[i]).addClass(disabledClass);
				} else {
					$('li:eq(0)', an[i]).removeClass(disabledClass);
				}
				if (iPages === 0 || iCurrentPage === iPages || oSettings._iDisplayLength === -1) {
					$('li:eq(-1)', an[i]).addClass(disabledClass);
				} else {
					$('li:eq(-1)', an[i]).removeClass(disabledClass);
				}

				/* Build up the dynamic list first - html and listeners */
				$('li:gt(0)', an[i]).filter(':not(li:eq(-1))').remove();
				$(sList)
					.clone()
					.insertBefore($('li:eq(-1)', an[i]))
					.each(fnBind);
			}
		}
	},
	"bootstrap_full_numbers": {
		"fnInit": function(oSettings, nPaging, fnCallbackDraw) {
			var oLang = oSettings.oLanguage.oPaginate;
			var oClasses = oSettings.oClasses;
			var disabledClass = 'disabled ' + oClasses.sPageButtonStaticDisabled;
			var sPageButtonText = oLang.sShowPagingText ? '' : 'text-hide';
			var sPageButton = oLang.sShowPagingText ? oClasses.sPageButton : oClasses.sPageButton + ' no-text';

			var sAppend =
				'<ul class="pagination">' +
				'<li class="first ' + disabledClass + '"><a href="javascript:;" class="' + sPageButton + " " + oClasses.sPageFirst + '" tabindex="' + oSettings.iTabIndex + '" role="button"><span class="' + sPageButtonText + '">' + oLang.sFirst + '</span></a></li>' +
				'<li class="previous ' + disabledClass + '"><a href="javascript:;" class="' + sPageButton + " " + oClasses.sPagePrevious + '" tabindex="' + oSettings.iTabIndex + '" role="button"><span class="' + sPageButtonText + '">' + oLang.sPrevious + '</span></a></li>' +
				'<li class="next ' + disabledClass + '"><a href="javascript:;" class="' + sPageButton + " " + oClasses.sPageNext + '" tabindex="' + oSettings.iTabIndex + '" role="button"><span class="' + sPageButtonText + '">' + oLang.sNext + '</span></a></li>' +
				'<li class="last ' + disabledClass + '"><a href="javascript:;" class="' + sPageButton + " " + oClasses.sPageLast + '" tabindex="' + oSettings.iTabIndex + '" role="button"><span class="' + sPageButtonText + '">' + oLang.sLast + '</span></a></li>' +
				'</ul>';
			$(nPaging).append(sAppend);

			var els = $('a', nPaging);
			var nFirst = els[0],
				nPrevious = els[1],
				nNext = els[2],
				nLast = els[3];

			/* ID the first elements only */
			if (!oSettings.aanFeatures.p) {
				nPaging.id = oSettings.sTableId + '_paginate';
				nFirst.id = oSettings.sTableId + '_first';
				nPrevious.id = oSettings.sTableId + '_previous';
				nNext.id = oSettings.sTableId + '_next';
				nLast.id = oSettings.sTableId + '_last';

				nFirst.setAttribute('aria-controls', oSettings.sTableId);
				nPrevious.setAttribute('aria-controls', oSettings.sTableId);
				nNext.setAttribute('aria-controls', oSettings.sTableId);
				nLast.setAttribute('aria-controls', oSettings.sTableId);
			}

			var fnClickHandler = function(e) {
				e.preventDefault();
				if (oSettings.oApi._fnPageChange(oSettings, e.data.action)) {
					fnCallbackDraw(oSettings);
				}
			};

			oSettings.oApi._fnBindAction(nFirst, {
				action: "first"
			}, fnClickHandler);
			oSettings.oApi._fnBindAction(nPrevious, {
				action: "previous"
			}, fnClickHandler);
			oSettings.oApi._fnBindAction(nNext, {
				action: "next"
			}, fnClickHandler);
			oSettings.oApi._fnBindAction(nLast, {
				action: "last"
			}, fnClickHandler);
		},

		"fnUpdate": function(oSettings, fnCallbackDraw) {
			if (!oSettings.aanFeatures.p) {
				return;
			}

			var iPageCount = $.fn.dataTableExt.oPagination.iFullNumbersShowPages;
			var iPageCountHalf = Math.floor(iPageCount / 2);
			var iPages = Math.ceil((oSettings.fnRecordsDisplay()) / oSettings._iDisplayLength);
			var iCurrentPage = Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength) + 1;
			var iStartButton, iEndButton;
			var fnBind = function(j) {
				oSettings.oApi._fnBindAction(this, {
					"page": j + iStartButton - 1
				}, function(e) {
					/* Use the information in the element to jump to the required page */
					oSettings.oApi._fnPageChange(oSettings, e.data.page);
					fnCallbackDraw(oSettings);
					e.preventDefault();
				});
			};

			/* Pages calculation */
			if (oSettings._iDisplayLength === -1) {
				iStartButton = 1;
				iEndButton = 1;
				iCurrentPage = 1;
			} else if (iPages < iPageCount) {
				iStartButton = 1;
				iEndButton = iPages;
			} else if (iCurrentPage <= iPageCountHalf) {
				iStartButton = 1;
				iEndButton = iPageCount;
			} else if (iCurrentPage >= (iPages - iPageCountHalf)) {
				iStartButton = iPages - iPageCount + 1;
				iEndButton = iPages;
			} else {
				iStartButton = iCurrentPage - Math.ceil(iPageCount / 2) + 1;
				iEndButton = iStartButton + iPageCount - 1;
			}

			/* Build the dynamic list */
			var oClasses = oSettings.oClasses;
			var activeClass = 'active ' + oClasses.sPageButtonActive;
			var disabledClass = 'disabled ' + oClasses.sPageButtonStaticDisabled;

			var sList = "";
			for (var i = iStartButton; i <= iEndButton; i++) {
				sList += (iCurrentPage !== i) ?
					'<li><a href="javascript:;" class="' + oClasses.sPageButton + '" tabindex="' + oSettings.iTabIndex + '" role="button">' + oSettings.fnFormatNumber(i) + '</a></li>' :
					'<li class="' + activeClass + '"><a href="javascript:;" class="' + oClasses.sPageButton + '" tabindex="' + oSettings.iTabIndex + '" role="button">' + oSettings.fnFormatNumber(i) + '</a></li>';
			}

			/* Loop over each instance of the pager */
			var an = oSettings.aanFeatures.p;
			for (var i = 0; i < an.length; i++) {
				if (!an[i].hasChildNodes()) {
					continue;
				}

				/* Update the permanent button's classes */
				if (iCurrentPage == 1) {
					$('li:eq(0)', an[i]).addClass(disabledClass);
					$('li:eq(1)', an[i]).addClass(disabledClass);
				} else {
					$('li:eq(0)', an[i]).removeClass(disabledClass);
					$('li:eq(1)', an[i]).removeClass(disabledClass);
				}
				if (iPages === 0 || iCurrentPage === iPages || oSettings._iDisplayLength === -1) {
					$('li:eq(-1)', an[i]).addClass(disabledClass);
					$('li:eq(-2)', an[i]).addClass(disabledClass);
				} else {
					$('li:eq(-1)', an[i]).removeClass(disabledClass);
					$('li:eq(-2)', an[i]).removeClass(disabledClass);
				}

				/* Build up the dynamic list first - html and listeners */
				$('li:gt(1)', an[i]).filter(':not(li:eq(-2))').filter(':not(li:eq(-1))').remove();
				$(sList)
					.clone()
					.insertBefore($('li:eq(-2)', an[i]))
					.each(fnBind);
			}
		}
	}
});


/*
 * TableTools Bootstrap compatibility
 * Required TableTools 2.1+
 */
if ($.fn.DataTable.TableTools) {
	// Set the classes that TableTools uses to something suitable for Bootstrap
	$.extend(true, $.fn.DataTable.TableTools.classes, {
		"container": "DTTT btn-group",
		"buttons": {
			"normal": "btn btn-default",
			"disabled": "disabled"
		},
		"collection": {
			"container": "DTTT_dropdown dropdown-menu",
			"buttons": {
				"normal": "",
				"disabled": "disabled"
			}
		},
		"print": {
			"info": "DTTT_print_info"
		},
		"select": {
			"row": "active"
		}
	});

	// Have the collection use a bootstrap compatible dropdown
	$.extend(true, $.fn.DataTable.TableTools.DEFAULTS.oTags, {
		"collection": {
			"container": "ul",
			"button": "li",
			"liner": "a"
		}
	});
}