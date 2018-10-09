
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Create Entity</title> 
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
 
<script type="text/javascript">

var mydata;
var json;

$(document).ready(function () {


	mydata ='${responseJSON.BANK_LIST}';
 	json = jQuery.parseJSON(mydata);
	//alert(json);
	$.each(json, function(i, v) {
	    // create option with value as index - makes it easier to access on change
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);
	    // append the options to job selectbox
	    var data=$('#bankcode').append(options);

	}); 


    $('#save').live('click', function () {
    	//alert("inside action!!!!");

    		$("#form1").validate(depositrules);
    		if($("#form1").valid()){
    	    $("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/CashDepositSecurity.action';
			$("#form1").submit();
			return true;
    	}else{
			return false;
    	} 

    });


    $('#cancel').live('click', function () { 
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/redirectlogin.action';
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

<script type="text/javascript">
	var intermediaryRules = {
				rules : {
					bankcode : { required : true },
					bankname : { required : true } ,
					bankIp : { required : true } ,
					bankport : { required : true } ,
					c2 : { required : true } ,
					c3 : { required : true } ,
					c1 : { required : true } ,
					cloudmode : { required : true } ,
					acquirerId : { required : true} ,
					zpk: { required : true}
				},
				messages : {
					bankcode : {
								required : "Please enter Bank Code."
							  },
					bankname : {
									required : "Please enter Bank Name."
								},
					bankIp : {
									required : "Please enter Bank IP."
								},
					bankport : {
									required : "Please enter Bank Port."
								},
					c2 : {
									required : "Please enter C2 value."
								},
					c3 : {
									required : "Please enter C3 value."
						},
					c1 : {
									required : "Please enter C1 value."
						},
					cloudmode : {
									required : "Please select cloud mode."
								},
					acquirerId : {
									required : "Please enter Acquirer Id."
								} ,
					zpk : {
									required : "Please enter zpk."
								}
				}
			};

	var val = 1;
	var rowindex = 0;
	var colindex = 0;

	function bankCreationAction() { 
		
		$("#form1").validate(intermediaryRules);
 
		if($("#form1").valid()) { 

			$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/CreateSwitchBankAction.action';
			$("#form1").submit();
			return true; 
		} else {
			return false;
		}

	}


	 
  
function changevalue() {
 	 var index = $("#bankcode option:selected").text();
     $("#bankname").val(index.split("-")[1]);
}
 
function cancel() { 
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/redirectlogin.action';
	$("#form1").submit();
	return true;
}
</script>

</head>
<body> 
<form name="form1" id="form1" method="post"> 
	
		
			<div id="content" class="span10"> 
			    <div>
					<ul class="breadcrumb">
					  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#">Bank Creation</a> </li>

					</ul>
				</div>

				<table height="3">
					 <tr>
						<td colspan="3">
							<div class="messages" id="messages"><s:actionmessage /></div>
							<div class="errors" id="errors"><s:actionerror /></div>
						</td>
					</tr>
				 </table>

		   <div class="row-fluid sortable"> 
				<div class="box span12"> 
					<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Bank Information
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

						</div>
					</div>
				<div class="box-content">
					<fieldset> 
						<table width="950" border="0" cellpadding="5" cellspacing="1"
									class="table table-striped table-bordered bootstrap-datatable "> 
							<tr class="even">
								<td width="20%"><strong><label for="Bank Code">Bank Code<font color="red">*</font></label></strong></td>
								<td width="30%">
									<select id="bankcode" name="bankcode" onchange="changevalue()" data-placeholder="Choose BankCode..." 
											class="chosen-select" style="width: 280px;" required=true>
										<option value="">Select</option> 
									</select>
								</td>
								<td width="20%"><strong><label for="Bank Name">Bank Name<font color="red">*</font></label></strong></td>
								<td width="30%"><input name="bankname" type="text" id="bankname" class="field" maxlength="50" required='true'  readonly></td>
							</tr>
							<tr class="even">
								<td><strong><label for="Bank Ip">Bank Ip<font color="red">*</font></label></strong></td>
								<td><input name="bankIp" type="text" id="bankIp" class="field" maxlength="50" required='true' ></td>
								<td ><strong><label for="Bank Port">Bank Port<font color="red">*</font></label></strong></td>
								<td ><input name="bankport" id="bankport" class="field" type="text"  maxlength="50" required='true' ></td>
							</tr>
							<tr class="even">
								<td ><strong><label for="Acquirer Id">Acquirer Id<font color="red">*</font></label></strong></td>
								<td ><input name="acquirerId" type="text" id="acquirerId" class="field"  maxlength="50" required='true'/></td>
								<td ><strong><label for="BIN">BIN</label></strong>	</td>
								<td ><input name="bin" type="text" class="field" id="bin" /></td> 
							</tr> 
						</table> 
					</fieldset>  
				</div>
			</div>
		</div> 
  <div class="row-fluid sortable"> 
	<div class="box span12"> 
			<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>Key Information
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
				</div>
			</div>
		<div class="box-content">
				<fieldset> 
					<table width="950" border="0" cellpadding="5" cellspacing="1"
							class="table table-striped table-bordered bootstrap-datatable "> 
						<tr class="even"> 
							<td width="20%"><strong><label for="C1">C1<font color="red">*</font></label></strong></td>
							<td width="30%"><input name="c1" type="text"  id="c1" class="field" maxlength="50" required='true' > </td>
							<td width="20%"><strong><label for="ZPK">ZPK<font color="red">*</font></label></strong></td>
							<td width="30%"><input name="zpk" type="text" id="zpk" class="field"  maxlength="50" required='true'/></td>

						</tr>

						<tr class="even">
							 <td ><strong><label for="c2">c2<font color="red">*</font> </label></strong></td>
							<td ><input name="c2" type="text" class="field" id="c2"   maxlength="50" required='true'/></td>
							<td ><strong><label for="KCV">KCV</strong></td>
							<td ><input name="kcv" id="kcv" class="field" type="text"   /></td>

							<input name="type" id="type" class="field" type="hidden"   value="INSERT"/>
						</tr>
						<tr class="odd"> 
							<td><strong><label for="C3">C3<font color="red">*</font></label></strong></td>
							<td ><input name="c3" type="text" class="field" id="c3"  maxlength="50" required='true' /></td>
							<td></td>
							<td></td>

						</tr>

					</table>
				</fieldset>  
			</div>
			</div>
			</div>
	<div class="form-actions"> 
		<input type="button" class="btn btn-success" type="text"  name="btn-submit" id="btn-submit" value="Submit" width="100" onClick="bankCreationAction()"></input>
		&nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Cancel" width="100" onClick="cancel()"></input>	        
	</div>
</div> 
</form> 
</body>
</html>