<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Random"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>

<script type="text/javascript">

	var list = "dob".split(",");
	var datepickerOptions = {
				showTime: false,
				showHour: false,
				showMinute: false,
	  		dateFormat:'dd/mm/yy',
	  		alwaysSetTime: false,
			changeMonth: true,
			changeYear: true
	};
	
	var createProfileRules = {
			   rules : {
				profileId : { required : true },
				firstName : { required : true },
				lastName : { required : true },
				dob : { required : true },
				nationalId : { required : true },
				mobileNo : { required : true },
				emailId : { required : true }
			   },  
			   messages : {
				profileId : { 
			       required : "Profile Id Missing"
			        },
			    firstName : { 
			       required : "Please enter First Name"
			        },
			    lastName : { 
			       required : "Please enter Last Name"
			        },
				dob : { 
			       required : "Please enter Date of Birth"
			        },
			    nationalId : { 
			       required : "Please enter National Id"
			        },
			    mobileNo : { 
			       required : "Please enter Mobile No"
			        },
			    emailId : { 
			       required : "Please enter Email Id"
			        }
			   } 
			 };

	$(function(){
		
		$(list).each(function(i,val){
			$('#'+val).datepicker(datepickerOptions);
		});
		
		var data='${responseJSON.LOCATION_LIST}';
		json = jQuery.parseJSON(data);
		
		$.each(json, function(i, v) {
			var options = $('<option/>', {value: v.key, text: v.val}).attr('data-id',i);  
			$('#location').append(options);
		});
		
		
		
		$("#submit").click(function(){
			$("#form1").validate(createProfileRules);
			if($("#form1").valid()){
				$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioRegSubmit.action";
				$("#form1").submit();
				return true;
			}else{
				return false;
			}
		});
		
		$("#back").click(function(){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioAct.action";
			$("#form1").submit();
		});
		
	});

</script>

</head>
<body >
	<form name="form1" id="form1" method="post"  >
		
		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="home.action">Bio Activities</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Bio Registration</a></li>
				</ul>
			</div>
			
			<div>
					<ul class="breadcrumb">
						<li><a href="#" style="font-size: 15px;font-variant: inherit;">Bio Registration</a></li>
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
	
				<div class="row-fluid sortable" >
						<div class="box span12" >
						<div class="box-header well" data-original-title>
								<i class="icon-edit"></i>Profile Details
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
							</div>
						</div>
			
						<div class="box-content">
						 <fieldset>
						 <table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
							<tr  class="even">
								 <td width="20%"><label for="Address Line1"><strong>Profile Id<font color="red">*</font></strong></label></td>
								 <td><input name="profileId" type="text"  id="profileId" class="field" maxlength='30' value="<%= new Random().nextInt(99999) %>" readonly></td>
							</tr>
							
							<tr class="odd">
								 <td width="20%"><label for="Profile Name"><strong>First Name<font color="red">*</font></strong></label></td>
								 <td><input name="firstName" id="firstName" class="field" type="text"  maxlength='30' value="" /></td>
							</tr>
							<tr class="even">
								 <td width="20%"><label for="Profile Name"><strong>Last Name<font color="red">*</font></strong></label></td>
								 <td><input name="lastName" id="lastName" class="field" type="text"  maxlength='30' value="" /></td>
							</tr>
							<tr  class="even">
								 <td width="20%"><label for="Address Line1"><strong>Date Of Birth<font color="red">*</font></strong></label></td>
								 <td><input name="dob" type="text"  id="dob" class="field" maxlength='30' value=""></td>
							</tr>
							<tr  class="odd">
								 <td width="20%"><label for="Address Line1"><strong>National Id<font color="red">*</font></strong></label></td>
								 <td><input name="nationalId" type="text"  id="nationalId" class="field" maxlength='30' value=""></td>
							</tr>
							<tr  class="even">
								 <td width="20%"><label for="Address Line1"><strong>Mobile No<font color="red">*</font></strong></label></td>
								 <td><input name="mobileNo" type="text"  id="mobileNo" class="field" maxlength='30' value=""></td>
							</tr>
							<tr  class="even">
								 <td width="20%"><label for="Address Line1"><strong>Email<font color="red">*</font></strong></label></td>
								 <td><input name="emailId" type="text"  id="emailId" class="field" maxlength='30' value=""></td>
							</tr>
							<tr>
								<td width="20%"><label for="Address Line1"><strong>Location<font color="red">*</font></strong></label></td>
								 <td>
								 	<select id="location" name="location" data-placeholder="Choose Location..." class="chosen-select" style="width: 220px;" >
										<option value="">Select</option>
									</select>
								 
								 </td>
							</tr>
							
							<tr class="even">
								<td colspan="2">
									<input class="btn btn-danger" type="submit" name="back" id="back" value="Back" /> &nbsp;&nbsp;&nbsp;
									<input class="btn btn-success" type="submit" name="submit" id="submit" value="Submit" />
								</td>
							</tr>
						</table>
						</fieldset>
						</div>
						</div>
				</div>
				
			</div>
	</form>
</body>
</html>
