<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Amur</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Create Entity</title> 
<!-- jQuery -->  
  
<!--  -->
   
<link href="<%=ctxstr%>/css/bootstrap-responsive.css" rel="stylesheet"/>   
<link rel="stylesheet" type="text/css" media="screen" href="<%=ctxstr%>/css/jquery-ui-1.8.21.custom.css" />
  
 
<link href="<%=ctxstr%>/css/agency-app.css" rel="stylesheet"/>

<%

String  info = session.getAttribute("print_details").toString();
String infoPrint[] = info.split("\\~");
 
%>

 
   
<script type="text/javascript">   
 
$(function(){  
	 window.print();
});
 
</script>
</head>
 <body>
  <form name="form1" id="form1" method="post" >  
	 <div id="content" class="span10"> 
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
			<div id="acctDetails"> 
				<table width="950" border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable ">
					<tr class="even">
						<center><%=infoPrint[2] %> Service Payment Receipt</center>
					</tr>
					<tr class="even">
						<center>==============================</center>
					</tr>
				</table>
			</div>  
			
			<div id="acctDetails"> 
			<table width="100%" border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable ">
				<tr class="even">
					<td width="30%">&nbsp;</td><td width="20%">Location</td>  <td width="50%"> :&nbsp;&nbsp;&nbsp;<%=infoPrint[10] %> </td>
				</tr>
				<tr class="even">
					<td >&nbsp;</td><td >Transaction Date Time</td><td >:&nbsp;&nbsp;&nbsp;<%=infoPrint[0] %></td>
				</tr>
				<tr class="even">
					<td>&nbsp;</td><td >Agent Name</td><td >:&nbsp;&nbsp;&nbsp;<%=infoPrint[2] %></td>
				</tr>
				<tr class="even">
					<td  >&nbsp;</td><td >Dl Number</td><td >:&nbsp;&nbsp;&nbsp;<%=infoPrint[5] %></td>
				</tr>
				<tr class="even">
					<td  >&nbsp;</td><td >Amount</td>  <td >:&nbsp;&nbsp;&nbsp;<%=infoPrint[4] %></td>
				</tr>
				<tr class="even">
					<td  >&nbsp;</td><td >Transaction Reference </td>  <td >:&nbsp;&nbsp;&nbsp; <%=infoPrint[1] %> </td>
				</tr>
				<tr class="even">
					<td  >&nbsp;</td><td >Customer Name</td><td >:&nbsp;&nbsp;&nbsp;<%=infoPrint[6] %></td>
				</tr>
				<tr class="even"> 
					<td  >&nbsp;</td><td >Service</td>  <td >:&nbsp;&nbsp;&nbsp;<%=infoPrint[3] %></td>
				</tr>
				<tr class="even">
					<td >&nbsp;</td><td >Payment Mode<td> <td >:&nbsp;&nbsp;&nbsp;<%=infoPrint[8] %></td>
				</tr>
		</table>
		</div> 
</div><!--/#content.span10--> 
</form>
</body> 
</html>
