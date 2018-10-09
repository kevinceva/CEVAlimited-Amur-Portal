<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%
	String ctxstr = request.getContextPath();
%>
<% String appName= null;
	try{ 
		appName = (session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString() == null ? "AgencyBanking" :  session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString());
	
	}catch(Exception ee){
		response.sendRedirect(ctxstr+"/cevaappl.action");
	}
  
%>
<title>Amur | SESSION OUT</title>

<link
	href="<%=ctxstr%>/css/bootstrap-responsive.min.css"
	rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen"
	href="<%=ctxstr%>/css/jquery-ui-1.8.21.custom.min.css" />
<link href="<%=ctxstr%>/css/agency-app.min.css"
	rel="stylesheet" />


<script type="text/javascript"> 
window.setTimeout( redirectLogin, 3000);

function redirectLogin() {
	document.form1.action = "<%=ctxstr%>/logout.action";
	document.form1.submit();
}
</script>
</head>
<body>
	<form name="form1" id="form1" method="post"> 

		<div id="content" class="span10">
			 
			<div class="row-fluid sortable"> 
				<div class="box span12"> 
					<div class="box-content">
						<table width="100%" border="0" cellpadding="5" cellspacing="1"
							class="table" border=1>
							<tr>
								<td width="20%">&nbsp;</td>
								<td width="10%">&nbsp;</td>
								<td width="60%"><img
									src="${pageContext.request.contextPath}/images/ajax-loaders/ajax-loader-5.gif" />
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td >
									<h4>
										<font color=red>Session Got Expired.</font>
									</h4>
								</td> 
							</tr>
							<tr>
								<td>&nbsp;</td> 
								<td>&nbsp;</td>
								<td>
									<h4>Please Wait.. Redirecting To Login Page.</h4>
								</td>
							</tr>
							<tr>

							</tr>
						</table>

						<table width="950" border="0" cellpadding="0" cellspacing="0"
							class="head1">
							<tr>
								<td>&nbsp;</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</form>
</body>
</html>