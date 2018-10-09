
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
	
   
<script type="text/javascript" >
var list = "fromDate,toDate".split(",");
var datepickerOptions = {
			showTime: false,
			showHour: false,
			showMinute: false,
  		dateFormat:'dd/mm/yy',
  		alwaysSetTime: false,
		changeMonth: true,
		changeYear: true
};

$(document).ready(function() { 

	var reqdata = '${responseJSON}';
	console.log("reqdataaaaaaaaaaaaaa:"+reqdata);
	$(list).each(function(i,val){
		$('#'+val).datepicker(datepickerOptions);
	});
	
	
	$('#btn-back').live('click',function() { 
		var srhval = '${responseJSON.srchval}';
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcentersearch.action?";
		$("#form1").submit();					
	}); 
	
	$('#btn-submit').live('click',function() { 
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcenterpurchasedet.action?";
		$("#form1").submit();					
	}); 
	
	
});
 
	
</script>
 
</head>

<body>
 <form name="form1" id="form1">
	<div id="content" class="span10">
            			<!-- content starts -->
		<div>
				<ul class="breadcrumb">
				  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="#">Call Center</a> <span class="divider"> &gt;&gt; </span></li>
				  <li><a href="#">Statements</a></li>
				</ul>
		</div>	
		
				 	<div class="row-fluid sortable">
           
	<div id="date-grid"  class="box span12" >
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Date Range 
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>
						
		<div  class="box-content" >
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
						<tr id="tr-fromdate" >
							<td >&nbsp;</td>
							<td > <label for="From Date"><strong>From Date<font color="red">*</font></strong></label></strong></td>
							<td class="fromDateTd"><strong><input name="name" type="text"  id="fromDate" name="fromDate"  /></strong></td>
						</tr>
						<tr  id="tr-todate" >
							<td >&nbsp;</td>
							<td > <label for="To Date"><strong>To Date<font color="red">*</font></strong></label></strong></td>
							<td class="toDateTd"><strong> <input name="name" type="text"  id="toDate" name="toDate"  /></strong></td>
						</tr> 
									
				</table>
			</fieldset> 
			
              
		</div>
		
		
		</div>
		</div> 
		
		<div class="form-actions">
			 <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Submit" ></input>
			 <input type="button" class="btn btn-primary" type="text"  name="btn-back" id="btn-back" value="Back" ></input>
			<input type="hidden" name="srchval"  id="srchval" value="${responseJSON.srchval}" />
			<input type="hidden" name="cid"  id="cid" value="${responseJSON.cid}" />
		</div> 

	</div>	
</form>
</body>
</html>
