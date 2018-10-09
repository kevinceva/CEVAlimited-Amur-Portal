
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
var listDate = "dob".split(",");
var glistDate = "bdob".split(",");
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
				gender : { required : true } ,
				dob : { required : true } ,
				idno : { required : true } ,				
				pcode : { required : true } ,
				email : { required : true , email : true}
			},		
			messages : { 
				gender : { 
								required : "Please Enter Gender."
							},
				dob : { 
								required : "Please Select Date Of Birth."
							},
				idno : { 
								required : "Please Enter ID Number."
							},
				pcode : { 
								required : "Please Enter Postal Code."
							},
				email : { 
								required : "Please Enter email address.",
								email : "Please Enter a valid email address."
						}
			} 
		}; 
		
$(function() {
	 $(listDate).each(function(i,val){
 		$('#'+val).datepicker(datepickerOptions);
	}); 
	 
	 $(glistDate).each(function(i,val){
	 		$('#'+val).datepicker(datepickerOptions);
		}); 
	 /*
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
	
	$('#spn-user-status').append(text);  */
	
 	 var sttd = "${responseJSON.STATUS}";
 	if( sttd == 'Active') {
		sttd = "<a href='#' class='label label-success' >Active</a>";
	    } else {
	    	sttd = "<a href='#' class='label label-warning' >"+sttd+"</a>";
	    }
	
	$("#statusdisp").append(sttd);
	
	 var bsttd = "${responseJSON.BSTATUS}";

		if( bsttd == 'BS') {
			bsttd = "<a href='#' class='label label-success' >Active</a>";
		}
		else if( bsttd == 'BV') {
			bsttd = "<a href='#' class='label label-warning' >Beneficiary ID Verified</a>";
		}
		else if( bsttd == 'BC') {
			bsttd = "<a href='#' class='label label-warning' >Beneficiary ID Received</a>";
		}
		else if( bsttd == 'BR') {
			bsttd = "<a href='#' class='label label-warning' >Beneficiary Mobile No Submitted</a>";
		}
		else if( bsttd == 'BF') {
			bsttd = "<a href='#' class='label label-warning' >Beneficiary Verification Failed</a>";
			}
		else {
			bsttd = "<a href='#' class='label label-warning' >Beneficiary Verification In Progress</a>";
		}
		
		$("#bstatusdisp").append(bsttd);	
		
		 var vbid = "${responseJSON.BEN_ID}";
		 if (vbid.length == 0)
		{
			 $('#ben-grid').hide();
		}	 
		 else
		{	 
			 $('#ben-grid').show();	
		}
	
			$('#btn-submit').live('click',function() { 
				$("#form1").validate(validationRules);
				if($("#form1").valid()) {		
					$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/clientModifyConfirm.action";
					$("#form1").submit();	
				} else {
					return false;
				}
		
			});  
			
			$('#btn-cancel').live('click',function() { 
				$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/clientinfo.action?pid=141";
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
				  <li> <a href="clientinfo.action?pid=141">Client Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#"> ${type} Client </a></li>
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
			 <i class="icon-edit"></i>Client Details 
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
							<td width="25%" ><strong><label for="Client ID"> Client Id</label></strong></td>
							<td width="25%" >${responseJSON.CLIENT_ID}  <input type="hidden" name="cid"  id="cid" value="${responseJSON.CLIENT_ID}" /> </td> 
							<td width="25%" ><strong><label for="Client Name"> Client Name</label></strong></td>
							<td width="25%" >${responseJSON.CLIENT_NAME} <input type="hidden" name="cname"  id="cname" value="${responseJSON.CLIENT_NAME}" /></td>
						</tr>
						<tr class="odd">
 							<td><strong><label for="Gender"> Gender<font color="red">*</font></label></strong></td>
							<td><input name="gender" id="gender" value="${responseJSON.GENDER}"  type="text" class="field" required=true  maxlength="50" ></td>
							<td><strong><label for="Date Of Birth"> Date Of Birth<font color="red">*</font></label></strong></td>
							<td><input name="dob" type="text" value="${responseJSON.DOB}" class="field" id="dob"  required=true maxlength="50" /></td>
						</tr> 
						<tr class="odd">
 							<td><strong><label for="ID Type"> ID Type</label></strong></td>
 							<td width="25%" >${responseJSON.ID_TYPE}  <input type="hidden" name="idtype"  id="idtype" value="${responseJSON.ID_TYPE}" /> </td>
 					        <td><strong><label for="ID Number"> ID Number<font color="red">*</font></label></strong></td>
					        <td><input name="idno" type="text" value="${responseJSON.ID_NUMBER}" class="field" id="idno"  maxlength="30" size="30" /></td>
				       </tr> 
						<tr class="odd">
							<td><strong><label for="Mobile"> Mobile</label></strong></td>
							<td width="25%" >${responseJSON.MOB}  <input type="hidden" name="mob"  id="mob" value="${responseJSON.MOB}" /> </td> 
							<td><strong><label for="Address"> Address</label></strong></td>
							<td><input name="addr" type="text" value="${responseJSON.ADDR}" class="field" id="addr"  maxlength="30" size="30" /></td>
						 </tr>
	
						<tr class="odd">
							<td><strong><label for="Postal Code">Postal Code<font color="red">*</font></label></strong></td>
							<td>
								<input name="pcode" type="text" value="${responseJSON.PCODE}" class="field" id="pcode" required=true  maxlength="20" size="20" />
							</td>
							<td><strong><label for="E-Mail"> E-Mail<font color="red">*</font></label></strong></td>
							<td>
								<input name="email" type="text" value="${responseJSON.EMAIL}" class="field" id="email" required=true  maxlength="50" />
							</td>
						</tr>  
						<tr class="odd">
							<td><strong><label for="Created Date">Created Date</label></strong></td>
							<td width="25%" >${responseJSON.MKRDT}  <input type="hidden" name="cdate"  id="cdate" value="${responseJSON.MKRDT}" /> </td> 
							<td><strong><label for="Status">Status</label></strong></td>
							<td width="25%" > <span id="statusdisp"></span> </td> 
						</tr> 						

				</table>
				<input type="hidden" name="type"  id="type" value="Modify" />
				<input type="hidden" name="CV0014"  id="CV0014" />
				<input type="hidden" name="CV0010"  id="CV0010" />
				<input type="hidden" name="status"  id="status" value="${responseJSON.STATUS}" />
				<input type="hidden" name="bstatus"  id="bstatus" value="${responseJSON.BSTATUS}" />
			</fieldset> 
			
		</div>
		</div>
		</div> 
		
	<div id="ben-grid" class="row-fluid sortable"> 
		<div class="box span13">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Beneficiary Details 
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
							<td width="25%" ><strong><label for="Beneficiary ID"> Beneficiary Id</label></strong></td>
							<td width="25%" >${responseJSON.BEN_ID}  <input type="hidden" name="bid"  id="bid" value="${responseJSON.BEN_ID}" /> </td> 
							<td width="25%" ><strong><label for="Beneficiary Name"> Beneficiary Name</label></strong></td>
							<td width="25%" >${responseJSON.BEN_NAME} <input type="hidden" name="bname"  id="bname" value="${responseJSON.BEN_NAME}" /></td>
						</tr>
						<tr class="odd">
 							<td><strong><label for="Gender"> Gender<font color="red">*</font></label></strong></td>
							<td><input name="bgender" id="bgender" value="${responseJSON.BGENDER}"  type="text" class="field" required=true  maxlength="50" ></td>						
							<td><strong><label for="Date Of Birth"> Date Of Birth<font color="red">*</font></label></strong></td>
							<td><input name="bdob" type="text" value="${responseJSON.BDOB}" class="field" id="bdob"  required=true maxlength="50" /></td>
						</tr> 
						<tr class="odd">
 					        <td><strong><label for="ID Number"> ID Number<font color="red">*</font></label></strong></td>
					        <td><input name="bidno" type="text" value="${responseJSON.BID_NUMBER}" class="field" id="bidno"  maxlength="30" size="30" /></td>
 					        <td><strong><label for="Mobile"> Mobile</label></strong></td>
							<td width="25%" >${responseJSON.BMOB}  <input type="hidden" name="bmob"  id="bmob" value="${responseJSON.BMOB}" /> </td> 
				       </tr> 
<%-- 						<tr class="odd">
							<td><strong><label for="Validation Count">Validation Count</label></strong></td>
							<td width="25%" >${responseJSON.VAL_CNT}  <input type="hidden" name="vcnt"  id="vcnt" value="${responseJSON.VAL_CNT}" /> </td>
							<td><strong><label for="Photo"> Photo<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.PHOTO}  <input type="hidden" name="photo"  id="photo" value="${responseJSON.PHOTO}" /> </td>
						</tr>  --%> 
						<tr class="odd">
							<td><strong><label for="Created Date">Created Date</label></strong></td>
							<td width="25%" >${responseJSON.BMKRDT}  <input type="hidden" name="bcdate"  id="bcdate" value="${responseJSON.BMKRDT}" /> </td> 
							<td><strong><label for="BStatus">Status</label></strong></td>
							<td width="25%" > <span id="bstatusdisp"></span> </td>
						</tr> 						

				</table>
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

