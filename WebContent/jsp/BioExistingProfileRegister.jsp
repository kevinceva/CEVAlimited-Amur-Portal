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
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>

<script src="${pageContext.request.contextPath}/pagenationjs/jquery-1.12.2.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/dataTables.colVis.js"></script>

<script type="text/javascript">

	
	$(function(){
		
		
		
		$("#back").click(function(){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioAct.action";
			$("#form1").submit();
		});
		
		
	});

	
</script>

</head>
<body >
	<form name="form1" id="form1" method="post" action="" autocomplete="off">
		
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
							
							<tr class="odd">
								<td colspan="2">
									<%
											ResourceBundle resource = ResourceBundle.getBundle("BioConfiguration");
											String sPath=resource.getString("ipport");
											System.out.println(sPath);
									
									%>
									<jsp:plugin type="applet" code="FM220SDKRegister.class" codebase="/CevaBase/jsp/test1" archive="FM220SDK.jar" width = "500" height = "400">
									<jsp:params>
											    <jsp:param name="msg" value="<%= sPath %>" />
											    <jsp:param name="memberId" value='<%= session.getAttribute("BIO_MID") %>' />
											    <jsp:param name="makerId" value='<%=(String)session.getAttribute(CevaCommonConstants.MAKER_ID) %>' />
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
									<input class="btn btn-danger" type="submit" name="back" id="back" value="Next" /> &nbsp;&nbsp;&nbsp;
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
