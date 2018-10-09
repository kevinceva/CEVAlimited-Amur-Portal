<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Banking</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<% String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<%@taglib uri="/struts-tags" prefix="s"%> 

<style type="text/css">
label.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
.errmsg {
color: red;
}
.errors {
color: red;
}
</style>

<s:set value="responseJSON" var="respData"/>
 
<script type="text/javascript" >
var userLinkData ='${USER_LINKS}';
var jsonLinks = jQuery.parseJSON(userLinkData);
var linkIndex = new Array();
var linkName = new Array();
var linkStatus = new Array();



var listDate = "settlementdate,tosettlementdate".split(",");
var datepickerOptions = {
			showTime: false,
			showHour: false,
			showMinute: false,
  		dateFormat:'dd/mm/yy',
  		alwaysSetTime: false,
		changeMonth: true,
		changeYear: true
};

var settlementrules = {
		rules : { 
			settlementdate : { required : true , date : true },
			tosettlementdate : { required : true , date : true }
		},		
		messages : { 
			settlementdate : { 
						required : "Please Select Settlement From Date.",
						date : "The Selected Date Is Not Valid."
					} ,
			tosettlementdate : { 
						required : "Please Select Settlement To Date.",
						date : "The Selected Date Is Not Valid."
					} 
		} 
	}; 
$(document).ready(function () {
	
	$.each(jsonLinks, function(index, v) {
		linkIndex[index] = index;
		linkName[index] = v.name;
		linkStatus[index] = v.status;
	});


	$(listDate).each(function(i,val){
 		$('#'+val).datepicker(datepickerOptions);
	});  
	
	$.validator.addMethod("date", function (value, element) {  
		if ( value.match(/^\d\d?\/\d\d?\/\d\d\d\d$/)   ) {
			return true;
		} else {
			return false;
		}
	});		
	
	$.validator.addMethod("regex", function(value, element, regexpr) {          
		 return regexpr.test(value);
	   }, "");   
	 
	$('#btn-submit').live('click', function () {
		$("#error_dlno").text('');	 
		$("#form1").validate(settlementrules);
		
		if($("#form1").valid()) {  
			$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/getSettlementData.action';
			$("#form1").submit();
			return true;
		}
		
	});
	
	$('#add-settlement-bank').live('click', function () { 
		$("form").validate().cancelSubmit = true; 
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/addSettlementBank.action';
		$("#form1").submit();  
		
	});
	
	$('#settlement-dashboard').live('click', function () {
		$("form").validate().cancelSubmit = true; 
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/viewSettlementBanks.action';
		$("#form1").submit();  
		
	});
	
	// Search Links For Top Layer
	 $('#top-layer-anchor').find('a').each(function(index) {
		var anchor = $(this);   
		var flagToDo = false;
		 
		$.each(linkIndex, function(indexLink, v) {	  
			if(linkName[indexLink] == anchor.attr('id'))  {
				flagToDo = true;
			} 
		});
		
		if(!flagToDo) {
			anchor.attr("disabled","disabled");
		} else {
			anchor.removeAttr("disabled");
		} 
	});
	
	//Search For The Links That Are Assigned To Table Level
	 $('table > tbody').find('a').each(function(index) {
		var anchor = $(this);   
		var flagToDo = false;
		 
		$.each(linkIndex, function(indexLink, v) {	 
			if(linkName[indexLink] == anchor.attr('id'))  {
				flagToDo = true;
			} 
		}); 
		if(!flagToDo) {
			anchor.attr("disabled","disabled");
		} else {
			anchor.removeAttr("disabled");
		} 
		 
	}); 
	
	
});  
</script>  
</head> 
<body>
<form name="form1" id="form1" method="post" >	
	<div id="content" class="span10"> 
	  <div>
		 <ul class="breadcrumb">
			<li><a href="#">Home </a> <span class="divider"> &gt;&gt; </span></li>
			<li><a href="#">Settlement Data</a></li>
 		</ul>
	</div>			
	<table height="3">
		<tr>
			<td colspan="3">
				<div class="messages" id="messages">
					<s:actionmessage />
				</div>
				<div class="errors" id="errors">
					<s:actionerror />
				</div>
			</td>
		</tr>
	 </table>
     <div class="box-content" id="top-layer-anchor">
			<span>
				<a href="#" class="btn btn-info" id="add-settlement-bank" title='Add Bank For Settlement' data-rel='popover'  data-content='Add a new bank to process the settlement.'>
				<i class="icon icon-web icon-white"></i>&nbsp;Add Bank </a> &nbsp;							
			</span>
			<span>
				<a href="#" class="btn btn-warning" id="settlement-dashboard" title='Settlement Dashboard' data-rel='popover'  data-content="Viewing the list of registered settlement bank's.">
				<i class="icon icon-briefcase icon-white"></i>&nbsp;Settlement Dashboard</a> &nbsp; 
			</span> 			 
		</div> 			 
    <div class="row-fluid sortable"><!--/span--> 
		<div class="box span12" id="groupInfo">
                  <div class="box-header well" data-original-title>Settlement Information
					<div class="box-icon"> <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					 <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> </div>
				</div>
           		<div class="box-content">
				<fieldset> 
					<table width="950" border="0" cellpadding="5" cellspacing="1" 
							class="table table-striped table-bordered bootstrap-datatable " id="user-details">  
						<tr class="odd">
							<td width="10%"><label for="Bank Id"><strong>Bank Id</strong></label></td>
							<td width="50%" colspan=3>
								 <s:select cssClass="chosen-select" 
									headerKey="" 
									headerValue="Select"
									list="#respData.bank_data"  
									name="bankid" 
									id="bankid" 
									theme="simple"
									data-placeholder="Choose Bank..."
									style="width: 420px;"   
									 />  
						</tr>
						<tr class="odd">
							<td><label for="Settlement From Date"><strong>Settlement From Date<font color="red">*</font></strong></label></td>
		                  	<td > <input name="settlementdate" id="settlementdate" type="text"  class="field" required="true"  
		                  			 maxlength="12"  style="width: 210px;"   />  </td>
		                  	<td><label for="Settlement To Date"><strong>Settlement To Date<font color="red">*</font></strong></label></td>
		                  	<td > <input name="tosettlementdate" id="tosettlementdate" type="text"  class="field" required="true"  
		                  			 maxlength="12"  style="width: 210px;"   />  </td>
						</tr> 
					</table>
				</fieldset> 
			</div>	 
		</div>
	</div> 
	<div class="form-actions"> 
		<input type="button" class="btn btn-success" type="text"  name="btn-submit" id="btn-submit" value="Submit" width="100" />
		&nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Cancel" width="100" />
		<span id ="error_dlno" class="errors"></span> 
	</div>  
</div>  
</form>
<script type="text/javascript">
$(function(){ 
	var config = {
      '.chosen-select'           : {},
      '.chosen-select-deselect'  : {allow_single_deselect:true},
      '.chosen-select-no-single' : {disable_search_threshold:10},
      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
      '.chosen-select-width'     : {width:"95%"}
    }
    for (var selector in config) {
      $(selector).chosen(config[selector]);
    }
}); 
</script>
</body>
</html>
