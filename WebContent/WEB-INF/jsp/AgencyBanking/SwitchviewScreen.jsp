
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
label.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
</style>
<!-- add row validation -->

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


$(function() {
	$(listDate).each(function(i,val){
 		$('#'+val).datepicker(datepickerOptions);
	});

	var entCount = 0;
	var mydata ='${responseJSON.LOCATION_LIST}';
 	var json = jQuery.parseJSON(mydata);
 	$.each(json, function(i, v) {
	    // create option with value as index - makes it easier to access on change
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);
	    // append the options to job selectbox
	    $('#officeLocation').append(options);
	}); 
 
	$(".chosen-select").chosen({no_results_text: "Oops, nothing found!"});
	$('#enableUserId').removeAttr('style');
});
</script>

<script type="text/javascript">

	var val = 1;
	var rowindex = 0;
	var colindex = 0;

	function bankCreationAction() {

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

		$("#form1").validate(intermediaryRules);


		if($("#form1").valid()) { 
			$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/CreateSwitchBankAction.action';
			$("#form1").submit();
			return true; 
		} else {
			return false;
		}

	}


	$(function() {
		$('#userId').live('keyup',function(){
			 var id = $(this).attr('id');
			 var v_val = $(this).val();
			 $("#"+id).val(v_val.toUpperCase());
		});

		// The below event is to delete the entire row on selecting the delete button
		$('#delete').live('click',function() {
			$(this).parent().parent().remove();
			if($('#myTable > tbody  > tr').length == 0) { rowindex = 0; colindex=0; }
		});



		$('#btn-cancel').live('click', function () {
			$("form").validate().cancelSubmit = true;
			var url="${pageContext.request.contextPath}/<%=appName %>/userGrpCreation.action?pid=2";
			$("#form1")[0].action=url;
			$("#form1").submit();

		});

		$('#btn-submit').live('click', function () {
				var url="${pageContext.request.contextPath}/<%=appName %>/insertIctAdminDetails.action";
				$("#form1")[0].action=url;
				$("#form1").submit();

		});



		$('#delete').live('click',function() {
			$(this).parent().parent().remove();
			if($('#myTable > tbody  > tr').length == 0) { rowindex = 0; colindex=0; }
		});
	});

	var tempUserId= "";

	$("#enableUserId").live( "click", function(){

		 var checkStatus = $(this).attr('checked');
		 tempUserId = $('#userId').val();
		 if(checkStatus == false) {
			 $('#userId').attr('disabled','disabled');
			 $('#userId').val('');

		 } else {
			 $('#userId').removeAttr('disabled');
			 var employeeNo = $('#employeeNo').val() == '' ? ' ' : $('#employeeNo').val();
			 var firstName = $('#firstName').val() == '' ? ' ' : $('#firstName').val();

		 }

	});

	function Cancel() {
 		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/switchBankModify.action';
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
		  <li> <a href="#">SwitchViewScreen</a></li>

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
							<td ><strong><label for="Bank Code">Bank Code<font color="red">*</font></label></strong></td>
							<td><input name="bankcode" type="text"  id="bankcode" class="field"  maxlength="50" required='true' value="${responseJSON.bankcode}" readonly/></td>
							<td ><strong><label for="Bank Name">Bank Name<font color="red">*</font></label></strong></td>
							<td><input name="bankname" type="text" id="bankname" class="field" maxlength="50" required='true' value="${responseJSON.bankname}" readonly/></td>
						</tr>

						<tr class="odd">
							<td><strong><label for="Bank Ip">Bank Ip<font color="red">*</font></label></strong></td>
							<td><input name="bankIp" type="text" id="bankIp" class="field" maxlength="50" required='true' value="${responseJSON.bankIp}" readonly/></td>
							<td ><strong><label for="Bank Port">Bank Port<font color="red">*</font></label></strong></td>
							<td ><input name="bankport" id="bankport" class="field" type="text"  maxlength="50" required='true' value="${responseJSON.bankport}" readonly/></td>
						</tr>
						<tr class="even">
							<td ><strong><label for="Acquirer Id">Acquirer Id<font color="red">*</font></label></strong></td>
							<td ><input name="acquirerId" type="text" id="acquirerId" class="field"  maxlength="50" required='true' value="${responseJSON.acquirerId}" readonly/></td>
							<td ><strong><label for="BIN">BIN</label></strong>	</td>
							<td ><input name="bin" type="text" class="field" id="bin" value="${responseJSON.bin}" readonly/></td> 
						</tr> 
					</table> 
				</fieldset> 
		</div>
		</div>
	</div>

  <div class="row-fluid sortable"> 
	<div class="box span12"> 
			<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>Products Information
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
						<td ><strong><label for="C1">C1<font color="red">*</font></label></strong></td>
						<td><input name="c1" type="text"  id="c1" class="field" maxlength="50" required='true' value="${responseJSON.c1}" readonly/>  </td>
						<td ><strong><label for="ZPK">ZPK<font color="red">*</font></label></strong></td>
						<td><input name="zpk" type="text" id="zpk" class="field"  maxlength="50" required='true' value="${responseJSON.zpk}" readonly/></td>

					</tr>

					<tr class="even">
						 <td ><strong><label for="c2">c2<font color="red">*</font> </label></strong></td>
						<td ><input name="c2" type="text" class="field" id="c2"   maxlength="50" required='true' value="${responseJSON.c2}" readonly/></td>
						<td ><strong><label for="KCV">KCV</strong></td>
						<td ><input name="kcv" id="kcv" class="field" type="text" value="${responseJSON.kcv}" readonly/> </td>

						<input name="type" id="type" class="field" type="hidden"   value="INSERT"/>
					</tr>
					<tr class="odd">

						<td><strong><label for="C3">C3<font color="red">*</font></label></strong></td>
						<td ><input name="c3" type="text" class="field" id="c3"  maxlength="50" required='true' value="${responseJSON.c3}" readonly/></td>
						<td></td>
						<td></td>

					</tr> 
				</table>
			</fieldset> 
		</div>
		</div>
	</div>
	<div class="form-actions"> 
		<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Cancel" width="100" onclick="Cancel()" ></input>
	</div>

		</div> 
	</form> 
</body>
</html>