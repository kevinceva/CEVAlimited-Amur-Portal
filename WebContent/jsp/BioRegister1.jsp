<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<link href="../css/chosen.min.css" 	rel="stylesheet" type="text/css" media="screen"  /> 
<link href="../css/agency-app.min.css"	rel="stylesheet" type="text/css" />
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Random"%>
<style type="text/css">
.errors {
	font-weight: bold;
	color: red;
	padding: 2px 8px;
	margin-top: 2px;
}

input#abbreviation {
	text-transform: uppercase
}

.page {
	background: #004c89;
	width: 100%;
	height: 98%;
}

.header {
	width: 100%;
	height: 84px;
	background: linear-gradient(#CB0808 0%, #9D0606 100%);
	background: -moz-linear-gradient(#CB0808 0%, #9D0606 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #CB0808),
		color-stop(100%, #9D0606));
	background: -webkit-linear-gradient(#CB0808 0%, #9D0606 100%);
	border-bottom: 3px solid #052e56 !important
}
.explorer{
margin-top: 32px;
}
.link{
height: 40px !important;padding-top: 30px !important;
}
.odd{
background-color: rgb(232, 232, 232);
}
</style>

</head>
<body style="background: #004c89;">
	<form name="form1" id="form1" method="post" action="BioRegisterSuccess.jsp" autocomplete="off">
	 <div class="page">	
		<div class="header">
			<img alt="" src="../images/logo.png"  style="width: 25%; margin-top: 5px;">
		</div>
		<div class="explorer" style=" background-color: #FFFFFF !important; margin-left: 196px; border-radius: 2px;">
			<div id="content" class="span10" style=" background-color:#FFFFFF !important;">
	
				<div>
					<ul class="breadcrumb">
						<li><a href="#" style="font-size: 15px;font-variant: inherit;">Bio Registration</a></li>
					</ul>
				</div>
	
	
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
							<tr class="odd">
								<td colspan="2">
									<%
											ResourceBundle resource = ResourceBundle.getBundle("BioConfiguration");
											String sPath=resource.getString("ipport");
											System.out.println(sPath);
									
									%>
									<jsp:plugin type="applet" code="FM220SDKGUIRegisterMultipleIdsDemo.class" codebase="/CevaBase/jsp/test1" archive="FM220SDK.jar" width = "500" height = "400">
									<jsp:params>
											    <jsp:param name="msg" value="<%= sPath %>" />
											    <jsp:param name="memberId" value='<%= request.getAttribute("bio_mid") %>' />
											    <jsp:param name="makerId" value='RAVI' />
											    <jsp:param name="JSESSIONID" value="<%= request.getSession(false).getId() %>" />
									</jsp:params>
										<jsp:fallback>
											<p>Unable to load applet</p>
										</jsp:fallback>
									</jsp:plugin>
									
								</td>
							</tr>
							<tr class="even">
								<td colspan="2">
									<input class="btn btn-success" type="submit" name="submit" id="submit" value="Submit" />
								</td>
							</tr>
						</table>
						</fieldset>
						</div>
						</div>
				</div>
				
			</div>
		</div>
	</div>	
	</form>
</body>
</html>
