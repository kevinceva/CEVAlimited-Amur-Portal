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
;
</style>

<script type="text/javascript">

function bioRegister(){
	$("#form1")[0].action='<%=request.getContextPath()%>/AgencyBanking/bioReg.action';  
	$("#form1").submit();
	return true;
}

function loadData(){
	
	<%	
	System.out.println("session:::"+session);

		request.setAttribute("bio_mid", "BPWU"+new Random().nextInt(9999));
	%>
}

</script>

</head>
<body onload="loadData()">
	<form name="form1" id="form1" method="post" action="BioActivities.jsp" autocomplete="off">
		<div id="content" class="span10">

			<div>
				<ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider">
							&gt;&gt; </span></li>
					<li><a href="#">Bio Registration</a></li>
				</ul>
			</div>


<div class="row-fluid sortable">
			<div class="box span12">
			<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>Profile Details
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
				</div>
			</div>

			<div class="box-content" id=" ">
			 <fieldset>
			 <table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
				<tr  class="even">
					 <td width="20%"><label for="Address Line1"><strong>Profile Id<font color="red">*</font></strong></label></td>
					 <td><input name="profileId" type="text"  id="profileId" class="field" maxlength='30' value=""></td>
				</tr>
				<tr>
					 <td width="20%"><label for="Profile Name"><strong>Profile Name<font color="red">*</font></strong></label></td>
					 <td><input name="profileName" id="profileName" class="field" type="text"  maxlength='30' value="" /></td>
				</tr>
				<tr  class="even">
					 <td width="20%"><label for="Address Line1"><strong>National Id<font color="red">*</font></strong></label></td>
					 <td><input name="nationalId" type="text"  id="nationalId" class="field" maxlength='30' value=""></td>
				</tr>
			</table>
			</fieldset>
			</div>
			</div>
			</div>
			
			<div class="box-content" id="top-layer-anchor">
				 	<div id="bioval" >
						<%
								ResourceBundle resource = ResourceBundle.getBundle("BioConfiguration");
								String sPath=resource.getString("ipport");
								System.out.println(sPath);
						
						%>
						<jsp:plugin type="applet" code="FM220SDKGUIRegisterMultipleIdsDemo.class" codebase="test1" archive="FM220SDK.jar" width = "500" height = "400">
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
					</div>
			</div>
			<input class="btn btn-success" type="submit" name="submit" id="submit" value="Next" />
		</div>
	</form>
</body>
</html>

