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
<%String appName = session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/css/chosen.min.css" />
<link rel="stylesheet" type="text/css" media="screen" href='${pageContext.request.contextPath}/css/jquery.cleditor.min.css' />  

<script type="text/javascript" > 
  
var val = 1;
var rowindex = 0;
var colindex = 0;
var bankAcctFinalData="";
var binstatus = "";
var v_message = "";

$(document).ready(function() {  
 
	var mydata ='${responseJSON.USER_GROUPS}';
	var json = jQuery.parseJSON(mydata);
	$.each(json, function(i, v) {
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
	    $('#s_group_select').append(options);
	});
	
	mydata ='${responseJSON.MERCHANT_GROUPS}';
	
	json = jQuery.parseJSON(mydata);
	
	$.each(json, function(i, v) {
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);  
		$('#s_merchant_select').append(options);
	});
	
	mydata ='${responseJSON.USER_INFO}';
	
	json = jQuery.parseJSON(mydata);
	
	$.each(json, function(i, v) {
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);  
		$('#s_user_select').append(options);
	}); 
	  
	 var announceRules = {
			   rules : {
					messageText : { required : true} 
			   },  
			   messages : {
				messageText : { 
				   required : "Please enter Message."
					} 
			   } 
			 };
		 
			
	$('#btn-submit').live('click', function () { 
		if(selectedRadVal=='group_val'){
			var groupid=$("#s_group_select").val();
			$("#typeOfDataVal").val(groupid);
			$("#typeOfData").val("G");
		}
		else if(selectedRadVal=='merchant_val'){
			var merchantId=$("#s_merchant_select").val();
			$("#typeOfDataVal").val(merchantId);
			$("#typeOfData").val("M");
		}
		else if(selectedRadVal=='user_val'){
			var userid=$("#s_user_select").val();
			$("#typeOfDataVal").val(userid);
			$("#typeOfData").val("U");
		}else{
			var groupid=$("#s_group_select").val();
			$("#typeOfDataVal").val(groupid);
			$("#typeOfData").val("G");
		}		
		$("#form1").validate(announceRules);
		 if($("#form1").valid()) { 
			var url="${pageContext.request.contextPath}/<%=appName %>/announcementsubmit.action"; 
			$("#form1")[0].action=url;
			$("#form1").submit(); 
		} else { 
			return false;
		}
	});  
	
	var selectedRadVal="";
	$("input[type=radio]").live("click",function(){  
		var radId = "";
		radId =$(this).attr("id"); 
		selectedRadVal=$(this).attr("value"); 
		$('input[type=radio]').each(function(){
 			if($(this).attr("id") != radId) {
				$("#"+$(this).attr("id")+"_select").prop('disabled', 'disabled');
				$("#"+$(this).attr("id")+"_select option").each(function(){
					if($(this).attr('value') == "") {
						$(this).attr('selected',true);
					}					
				});
			} else {
				$("#"+radId+"_select").attr('disabled', false);
			}
		});
		 
	}); 
	
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
 
 function getAnnouneManagementScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/announceMgmtAct.action';
	$("#form1").submit();
	return true;
}
</script>
</head> 
<body>
<form name="form1" id="form1" method="post" action="">	 
<div id="content" class="span10">  
	<div>
		<ul class="breadcrumb">
		  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
		  <li> <a href="#">Announcement</a>  </li>
		</ul>
	</div>
	<div class="row-fluid sortable"><!--/span-->
		<div class="box span12"> 
			<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>Information
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

				</div>
			</div>  
			<div class="box-content"  id="userDetails">
				 <fieldset>
					 <table width="950" border="0" cellpadding="5" cellspacing="1"  
							class="table table-striped table-bordered bootstrap-datatable">
						<tr class="even" id="tr_group">
							<td width="15%"><strong><label for="Select Group">Select Group<font color="red">*</font></label></strong></td>
							<td width="2%">  <input type="radio" name="announce" id="s_group" style="opacity: 10;" value="group_val" /> 
							</td>
							<td colspan=2>  
									<select id="s_group_select" name="s_group_select"   required='true' data-placeholder="Choose location..." 
										class="chosen-select" style="width: 220px;" >
											<option value="">Select</option>
									</select> 
							</td>
						</tr> 
						<tr class="even" id="tr_merchant">
							<td><strong><label for="Select Merchant">Select Merchant<font color="red">*</font></label></strong></td>									 
							<td><input type="radio" name="announce" id="s_merchant" value="merchant_val"   style="opacity: 10;" /> </td>
							<td>
								<select id="s_merchant_select" name="s_merchant_select"   required='true' data-placeholder="Choose location..." 
										class="chosen-select" style="width: 220px;" disabled>
									<option value="">Select</option>
								</select>
							</td>
						</tr>
						<tr class="even" id="tr_user">
							<td> <strong><label for="Select User">Select User<font color="red">*</font></label></strong></td>
							<td> <input type="radio" name="announce" id="s_user" value="user_val" style="opacity: 10;" /> </td>
							<td>
								<select id="s_user_select" name="s_user_select"   required='true' data-placeholder="Choose location..." 
										class="chosen-select" style="width: 220px;" disabled>
									<option value="">Select</option>
								</select>
							</td>
						</tr>
						<tr class="even">
							<td> <strong><label for="Select User">Message<font color="red">*</font></label></strong></td> 
							<td>&nbsp;</td>
							<td colspan=2>
								 <textarea class="cleditor" id="messageText" name="messageText"></textarea>
							</td>
						</tr> 
						<input type="hidden" name="typeOfDataVal" id="typeOfDataVal" value=""/>
						<input type="hidden" name="typeOfData" id="typeOfData" value="" />
					</table>
				</fieldset> 
			</div>
		</div>
	</div> 
	<div class="form-action">
		<input type="button" name="btn-back" id="btn-back" class="btn btn-danger"  value="Back" onClick="getAnnouneManagementScreen()"/> &nbsp;&nbsp;
		<input type="button" name="btn-submit" id="btn-submit" class="btn btn-success" value="Submit" />
	</div>	 
</div> 
 
</form>
</body>
</html>
