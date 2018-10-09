<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String mstr = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Home Page</title>
<%String ctxstr = request.getContextPath(); %>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style></head>
<body>


<table width="98%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="<%=ctxstr %>/jsp/images/spacer.gif" width="1" height="10"></td>
  </tr>
</table>
<table width="95%" height="20" border="0" cellpadding="0" cellspacing="0" class="head2">
	<tr>
		<td align="center"><strong>Welcome to <%=session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME) %> </strong></td>
	</tr>
</table>
</body>
</html>