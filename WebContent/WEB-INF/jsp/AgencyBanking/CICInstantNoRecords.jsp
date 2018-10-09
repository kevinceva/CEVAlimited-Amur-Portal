<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%> 
<%@taglib uri="/struts-tags" prefix="s"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>

<%
	String ctxstr = request.getContextPath();
%>
<%
	String appName = session.getAttribute(
			CevaCommonConstants.ACCESS_APPL_NAME).toString();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Amur</title>
   
</head>
<body>

<form name="form1" id="form1" method="post"
					action="${pageContext.request.contextPath}/<%=appName %>/cicreportsact.action">
					  
	<div id="content" class="span10">
				<div>
					<ul class="breadcrumb">
						<li><a href="home.action">Home</a> <span class="divider">&gt;&gt;</span>
						</li>
						<li><a href="#">Reports List</a></li>

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
								<i class="icon-edit"></i>List Of Reports
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i
										class="icon-cog"></i></a> <a href="#"
										class="btn btn-minimize btn-round"><i
										class="icon-chevron-up"></i></a>

								</div>
							</div>

							<div class="box-content">

								<table width="100%" border="0" cellpadding="5" cellspacing="1"
									id="table table-striped table-bordered bootstrap-datatable">
									<tr>
										<td width="60%"><strong>Report Name </strong></td>
										<td width="29%"><strong>Report Date</strong></td>

										<td width="3%" align="center"><strong>PDF</strong></td>
										<td width="3%" align="center"><strong>XLS</strong></td>
										<td width="3%" align="center"><strong>CSV</strong></td>
										<td width="3%" align="center"><strong>HTML</strong></td>

									</tr>
									<tr>
										<td><strong></strong></td>
										<td><strong></strong>No Records Found</td>
										<td><strong></strong></td>
										<td><strong></strong></td>
										<td><strong></strong></td>
										<td><strong></strong></td>
									</tr>
								</table>

								<div class="form-actions">
									<input type="submit" class="btn btn-primary" type="text"
										name="next" id="next" value="Next" width="100"></input> <input
										type="button" class="btn" type="text" name="save" id="save"
										value="Cancel" width="100"></input>
								</div>
							</div>
						</div>
			</div>

	</div> 
</form>
</body>
</html>

