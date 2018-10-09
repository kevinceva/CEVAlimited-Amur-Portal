

$(function(){
	$( "#startdate" ).datepicker({
		dateFormat: 'dd-mm-yy',
		maxDate: '0'
	});
	$( "#enddate" ).datepicker({
		dateFormat: 'dd-mm-yy',
		maxDate: '0'
	});
	$('.startDate').datepicker({
	    format: 'dd-mm-yy',
	    maxDate: '0'
	});
	$('.endDate').datepicker({
	     format: 'dd-mm-yy',
	     maxDate: '0'
	});
});