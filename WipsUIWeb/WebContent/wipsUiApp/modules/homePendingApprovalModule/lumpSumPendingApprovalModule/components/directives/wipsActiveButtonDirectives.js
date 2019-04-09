'use strict';

angular.module('WipsUiApp.LumpSumPendingApproval.ComponentsModule')
    .directive('wipsActiveButton',function(){
        return{
            link: function(scope, elem, attrs, ctrl) {
                elem.bind('click', function () {
                	if(elem[0].id == 'localToggleButton' ||  elem[0].id == 'usdToggleButton' || elem[0].id == 'yesButton' || elem[0].id == 'noButton' ){
	                    $(this).addClass('active');
	                    $(this).siblings().removeClass('active');
	                    if(elem[0].id=='yesButton'){
	                    	
	                    	$('#yesButton span').removeClass("addTick");
	                    	$('#noButton .glyphicon').addClass("addTick");
	                    	}
	                    else if(elem[0].id=='noButton'){
	                    	
	                    	 $('#noButton span').removeClass("addTick");
	                    	 $('#yesButton span').addClass("addTick");
	                    }
	                    
	                   
	               	}
                	else{
	               		elem.parent().parent().parent().find('#usdToggleButton').removeClass('active');
	                    elem.parent().parent().parent().find('#localToggleButton').addClass('active');
	                    elem.parent().parent().parent().find('#yesButton').removeClass('active');
	                    elem.parent().parent().parent().find('#noButton').addClass('active');
	               	}
                });
            }
        }
    });