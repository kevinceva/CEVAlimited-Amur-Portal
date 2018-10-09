
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 
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
 
<s:set value="responseJSON" var="respData" />
<script  type="text/javascript"> 

var depositrules= {
		rules : { 
			payeername : { 	required : true },
			mobile : { required : true, number: true  },
			amount : { 	required : true, number : true },
			paymentmode : { required : true }
		},
		messages : { 
			payeername : { required : "Please Enter Senders Name." },
			mobile : { required : "Please Enter Mobile Number." },
			amount : { required : "Please Enter Amount." },
			paymentmode : { required : "Please choose Mode Of Payment." } 
		}
	};

var mydata;
var json;

$(function () {
	
	var orgType='<s:property value="#respData.organizationType" />';
	if(orgType == "Large"){
		$("#paymentmode").val("WEB");
		$("#paymentmode").prop("readonly","readonly");
	}
	
    $('#Submit').live('click', function () {
 
    		$("#form1").validate(depositrules);
    		if($("#form1").valid()){
    			
				$('#save').attr('disabled','disabled');
    			$('#cash').val($('#paymentmode').val());
    			
	    	    $("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/orgCashDepositSecurity.action';
				$("#form1").submit();
			return true;
    	}else{
			$('#save').removeAttr('disabled');
			return false;
    	} 

    });
	
	
    $('#cancel').live('click', function () { 
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/viewOrganization.action?pid=130';
		$("#form1").submit();
		return true; 
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
 
</script>
</head>
<body >
<form name="form1" id="form1" method="post" action=""> 
	<div id="content" class="span10">  
		<div>
			<ul class="breadcrumb">
				<li> <a href="home.action">Home</a> <span class="divider">&gt;&gt;</span> </li>
				<li> <a href="viewOrganization.action?pid=130">E-Wallet</a> <span class="divider">&gt;&gt;</span> </li>
				<li><a href="#">Account Credit</a></li>

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
							<i class="icon-edit"></i>Account Credit Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i
								class="icon-cog"></i></a> <a href="#"
								class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a> 
						</div>
					</div> 
				<div class="box-content">
					<fieldset>  
						<table width="950"  border="0" cellpadding="5" cellspacing="1" 
							class="table table-striped table-bordered bootstrap-datatable " >
							<tr class="even">
								<td width="20%"><label for="Service"><strong>Organization Id</strong></label></td>
								<td width="50%" colspan="3">
									<s:property value="#respData.orgId" />  <input name="orgId" id="orgId" class="field" type="hidden"   value="<s:property value="#respData.orgId" />"/> 
 								</td> 
							</tr> 
							<tr class="even">
								<td><label for="Service"><strong>Organization Name</strong></label></td>
								<td colspan="3">
									<s:property value="#respData.orgName" />  <input name="orgName" id="orgName" class="field" type="hidden"   value="<s:property value="#respData.orgName" />"/>
								</td> 
							</tr> 
							<tr class="even">
								<td><label for="Service"><strong>Organization Type</strong></label></td>
								<td colspan="3">
									<s:property value="#respData.organizationType" />  <input name="organizationType" id="organizationType" class="field" type="hidden"   value="<s:property value="#respData.organizationType" />"/>
								</td> 
							</tr> 
							<tr class="even">
								<td><label for="Service"><strong>Sender's Name<font color="red">*</font></strong></label></td>
								<td colspan="3">
									<input name="payeername" id="payeername" class="field" type="text" required="true"  maxlength="50" autocomplete="off" value='<s:property value="#respData.payeername" />' />
								</td> 
							</tr> 
							<tr  class="odd">
								<td><label for="Amount"><strong>Amount<font color="red">*</font></strong></label></td>
								<td colspan="3"><input name="amount" id="amount" class="field" type="text" required="true"  maxlength="50" autocomplete="off" value='<s:property value="#respData.amount" />'/></td>
							</tr> 
							<tr  class="even">	
								<td><label for="Mobile"><strong>Mobile No<font color="red">*</font></strong></label></td>
								<td colspan="3"><input name="mobile" type="text" class="field" id="mobile" required="true"  maxlength="50" autocomplete="off" value='<s:property value="#respData.mobile" />'/></td>
							</tr>
							<tr class="even"> 
								<td><label for="Mode Of Deposit"><strong>Mode Of Deposit<font color="red">*</font></strong></label></td>
								<td colspan="3">
									 
									<s:select cssClass="chosen-select" 
										headerKey="" 
										headerValue="Select"
										list="#{'WEB':'WEB','POS':'POS','MPESA':'MPESA'}"
										name="paymentmode" 
										value="#respData.paymentmode" 
										id="paymentmode" 
										requiredLabel="true" 
										theme="simple"
										data-placeholder="Choose Mode Of Deposit..." 
 									 />   &nbsp; <label id="paymentmode-id" class="errors" ></label>
								</td> 
							</tr> 
						</table> 
					</fieldset> 
				</div>  
				
			</div>
				</div>
			  <div class="form-actions">
				<input type="button" class="btn btn-primary" type="text"  name="Submit" id="Submit" value="Submit" width="100" ></input> &nbsp;
				<input type="button" class="btn" type="text"  name="cancel" id="cancel" value="Back" width="100" ></input>
			  </div>
	</div> 
</form>
 </body> 
</html>