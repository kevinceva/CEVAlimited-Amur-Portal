
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%> 
 
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 
<style type="text/css">
.messages {
  font-weight: bold;
  color: green;
  padding: 2px 8px;
  margin-top: 2px;
}

.errors{
  font-weight: bold;
  color: red;
  padding: 2px 8px;
  margin-top: 2px;
}
label.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
.errmsg {
color: red;
}
</style>  
 <s:set value="responseJSON" var="respData"/>
<script type="text/javascript">  
var finalData="";
var listDate = "expiryDate".split(",");
var datepickerOptions = {
			showTime: false,
			showHour: false,
			showMinute: false,
  		dateFormat:'dd/mm/yy',
  		alwaysSetTime: false,
		changeMonth: true,
		changeYear: true
};

var toDisp = '${type}';
var validationRules = {
			rules : { 
				CV0003 : { required : true } ,
				CV0004 : { required : true } ,
				CV0009 : { required : true } ,
				officeLocation : { required : true } ,
				CV0011 : { required : true } ,
				CV0012 : { required : true , email : true}
			},		
			messages : { 
				CV0003 : { 
								required : "Please Enter First Name."
							},
				CV0004 : { 
								required : "Please Enter Last Name."
							},
				CV0009 : { 
								required : "Please Choose User Designation."
							},
				officeLocation : { 
								required : "Please Choose Office Location."
							},
				CV0011 : { 
								required : "Please Select Expiry Date."
							},
				CV0012 : { 
								required : "Please Enter email address.",
								email : "Please Enter a valid email address."
						}
			} 
		}; 
		
$(function() {
	$(listDate).each(function(i,val){
 		$('#'+val).datepicker(datepickerOptions);
	});  
	
	var entCount = 0; 
	var mydata ='${responseJSON.LOCATION_LIST}';
 	var json = jQuery.parseJSON(mydata);
 	$.each(json, function(i, v) {
	    // create option with value as index - makes it easier to access on change
	    var options = $('<option/>', {value: v.val, text: v.key+"-"+v.val}).attr('data-id',i);
	    // append the options to job selectbox
	    $('#officeLocation').append(options);
	});  

 
	//$(".chosen-select").chosen({no_results_text: "Oops, nothing found!"}); 
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
  
	$('#enableUserId').removeAttr('style');  
	
	var userLevel = '${responseJSON.CV0009}'; 
	// To Iterate the Select box ,and setting the in-putted as selected 
	$('#adminType').find('option').each(function( i, opt ) { 
		if( opt.value === userLevel ) {
			$(opt).attr('selected', 'selected');
			$('#adminType').trigger("liszt:updated");
		}
	});
	var userLocation = '${responseJSON.CV0010}';
	 
	// To Iterate the Select box ,and setting the in-putted as selected 
	$('#officeLocation').find('option').each(function( i, opt ) { 
		if( opt.value === userLocation ) {
			$(opt).attr('selected', 'selected');
			$('#officeLocation').trigger("liszt:updated");
		}
	});
	
	$("#telephoneRes,#telephoneOff,#mobile,#fax").keypress(function (e) {
		 //if the letter is not digit then display error and don't type anything
		 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
			//display error message
			$("#"+$(this).attr('id')+"_err").html("Digits Only").show().fadeOut("slow");
				   return false;
			}
	   });
 
	 
	var actionLink = "";
	
	 
	var userStatus = '${responseJSON.CV0013}';
	var text = "";
	
	if( userStatus == 'Active')
		text = "<a href='#' class='label label-success'   >"+userStatus+"</a>";
	else if( userStatus == 'De-Active')
		text = "<a href='#'  class='label label-warning'   >"+userStatus+"</a>";
	else if( userStatus == 'InActive')
		text = "<a href='#'  class='label label-info'  >"+userStatus+"</a>";
	else if( userStatus == 'Un-Authorize')
		text = "<a href='#'  class='label label-primary'   >"+userStatus+"</a>";
	
	$('#spn-user-status').append(text); 
	
	$('#btn-submit').live('click',function() { 
		$("#form1").validate(validationRules);
		if($("#form1").valid()) { 
			$('#CV0014').val($('#officeLocation').val());
			$('#CV0010').val($('#officeLocation option:selected').text());
		
			//$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/userModifyConfirm.action";
			//$("#form1").submit();	
		} else {
			return false;
		}
	});  
	
	$('#btn-cancel').live('click',function() { 
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/userGrpCreation.action?pid=7";
		$("#form1").submit();	 
	});
});
		//--> 
 </SCRIPT>
    
  
</head>

<body>
	<form name="form1" id="form1" method="post">
	<!-- topbar ends --> 
		
			<div id="content" class="span10">   
		    <div> 
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="userGrpCreation.action?pid=7">User Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#"> ${type} User </a></li>
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
	<div class="row-fluid sortable"> 
		<div class="box span12">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>User Details 
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>
						
		<div class="box-content">
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
						<tr class="odd" > 
							<td width="25%" ><strong><label for="User Id"> User Id</label></strong></td>
							<td width="25%" >${userId}  <input type="hidden" name="CV0001"  id="userId" value="${responseJSON.CV0001}" /> </td> 
							<td width="25%" ><strong><label for="Employee No"> Employee No</label></strong></td>
							<td width="25%" >${responseJSON.CV0002} <input type="hidden" name="CV0002"  id="empId" value="${responseJSON.CV0002}" /></td>
						</tr>
						<tr class="odd">
 							<td><strong><label for="First Name"> First Name<font color="red">*</font></label></strong></td>
							<td><input name="CV0003" id="firstName" value="${responseJSON.CV0003}"  type="text" class="field" required=true  maxlength="50" ></td>
 							<td><strong><label for="Last Name"> Last Name<font color="red">*</font></label></strong></td>
							<td><input name="CV0004" type="text" value="${responseJSON.CV0004}" class="field" id="lastName"  required=true maxlength="50" /></td>
						</tr> 
						<tr class="odd">
 							<td><strong><label for="Telephone Res"> Telephone(Res)</label></strong></td>
							<td><input name="CV0005" type="text" value="${responseJSON.CV0005}" class="field" id="telephoneRes"  maxlength="30" size="30" /></td>
 					        <td><strong><label for="Telephone Off"> Telephone(Off)</label></strong></td>
					        <td><input name="CV0006" type="text" value="${responseJSON.CV0006}" class="field" id="telephoneOff"  maxlength="30" size="30" /></td>
				       </tr> 
						<tr class="odd">
							<td><strong><label for="Mobile"> Mobile</label></strong></td>
							<td><input name="CV0007" type="text" value="${responseJSON.CV0007}" class="field" id="mobile"  maxlength="30" size="30" /></td>
							<td><strong><label for="Fax"> Fax</label></strong></td>
							<td><input name="CV0008" type="text" value="${responseJSON.CV0008}" class="field" id="fax"  maxlength="30" size="30" /></td>
						 </tr>
						<tr class="odd">
						  <td><strong><label for="User Designation"> User Designation<font color="red">*</font></label></strong></td>
						  <td>
							<select id="adminType" name="CV0009" data-placeholder="Choose a Designation..." 
											class="chosen-select-no-results" style="width: 220px; display: none;">
									<option value="">Select</option>
									<option value="Region">Region</option>
									<option value="Head Office">Head Office</option>
 									<option value="Branch Office">Supervisor</option>
									<option value="Branch Manager">Branch Manager</option>
									<option value="User">User</option>
							</select>
						 </td>
						 <td><strong><label for="Office Location"> Office Location<font color="red">*</font></label></strong></td>
						  <td>
							<select id="officeLocation" name="officeLocation" data-placeholder="Choose office location..." 
											class="chosen-select-no-results" style="width: 220px; " required=true  >
								<option value="">Select</option>  
							</select> 
							<%-- <s:select cssClass="chosen-select" 
						       headerKey="" 
						       headerValue="Select"
						       list="#respData.LOCATION_LIST"
						       name="officeLocation" 
						        id="officeLocation" 
						       requiredLabel="true" 
						       theme="simple"
						       data-placeholder="Choose office location..." 
					         /> --%>
						   </td>
						</tr> 
						<tr class="odd">
							<td><strong><label for="Expiry Date">Expiry Date<font color="red">*</font></label></strong></td>
							<td>
								<input name="CV0011" type="text" value="${responseJSON.CV0011}" class="field" id="expiryDate" required=true  maxlength="20" size="20" />
							</td>
							<td><strong><label for="E-Mail"> E-Mail<font color="red">*</font></label></strong></td>
							<td>
								<input name="CV0012" type="text" value="${responseJSON.CV0012}" class="field" id="email" required=true  maxlength="50" />
							</td>
						</tr>  
						<tr > 
							<td><strong><label for="User Status">User Status</label></strong></td>
							<td>
								<span id="spn-user-status"></span> <input type="hidden" name="CV0013"  id="user_status" value="${responseJSON.CV0013}" />
							</td> 
							<td> &nbsp;</td>
							<td>
								&nbsp;
							</td>
						</tr>
				</table>
				<input type="hidden" name="type"  id="type" value="Modify" />
				<input type="hidden" name="CV0014"  id="CV0014" />
				<input type="hidden" name="CV0010"  id="CV0010" />
			</fieldset> 
			
		</div>
		</div>
		</div> 
		<div class="form-actions">
		   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Submit" ></input>
		   &nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Cancel"  ></input>
		</div>  
              						 
</div><!--/#content.span10-->

 </form>
</body>
</html>

