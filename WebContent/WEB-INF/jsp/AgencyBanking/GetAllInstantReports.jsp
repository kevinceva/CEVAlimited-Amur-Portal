<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%@ page language="java" import="java.util.*" %>
<%@ page language="java" import="com.ceva.util.GenUtils" %>
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>

<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>

<%
    String querymode		= request.getParameter("querymode");
	String JRPTCODE			= request.getParameter("JRPTCODE");
	String mode				= request.getParameter("mode");
	String str				=request.getParameter("QryKey");
	String param			=request.getParameter("eparam");
	String dateCheck		=request.getParameter("dateCheck");
	String reportName		=request.getParameter("reportName");
	String subQueryData		= "";
	String QryKey	= "";
	String Query = "";
	String extraFields		= "";
	
	//param=param.replaceAll("\\$","##");
 
 	/*Query from Front End or html Page*/
	Query = request.getParameter("Query");
	if(Query == null || Query.length()==0)
			Query	= (String)request.getAttribute("Query");
	//System.out.println("[Query	] :"+Query	);
	/* Query from Property Files and Condition form FrontEnd or html page */
	if(Query==null || Query.length()==0)
	{
		 QryKey	= request.getParameter("QryKey");
		
		if(QryKey == null || QryKey.length()==0)
				Query	= (String)request.getAttribute("QryKey");

		//Query Fields Names from reportQueryFields.properties
		String queryFields = GenUtils.getKeyValue("reportQueryFields",QryKey);
		String buildQuery = "SELECT " + queryFields;

		//Extra Query Fields Names from Front End
		extraFields		= request.getParameter("extrafields");
		if(extraFields == null || extraFields.length()==0)
			extraFields	= (String)request.getAttribute("extrafields");

		if(extraFields!=null
			&& extraFields.length()!=0)
		{
			buildQuery = buildQuery + ", " + extraFields;
		}

		//Query Tables Names from reportQueryTables.properties
		String queryTables = GenUtils.getKeyValue("reportQueryTables",QryKey);
		buildQuery = buildQuery + " $FROM$ " + queryTables;

		//Query Conditions from Front End
		String queryConditions	= request.getParameter("queryconditions");
		if(queryConditions == null || queryConditions.length()==0)
			queryConditions	= (String)request.getAttribute("queryconditions");

		if(queryConditions!=null && queryConditions.length()!=0)
		{
			buildQuery = buildQuery + " WHERE " + queryConditions;		
		}
 		Query = buildQuery;
	}	 
	 	
	session.setAttribute("Query",Query);
	session.setAttribute("querymode",querymode);
	session.setAttribute("JRPTCODE",JRPTCODE);
	session.setAttribute("MODE",mode);
	 
	subQueryData = GenUtils.getKeyValue("reportQueryFields",QryKey+"_SUB");
	subQueryData = subQueryData.replaceAll("@@CONDITION@@", dateCheck);
 	 
	//Extra Paramaters Should be given in ParamName1#ParamValue1##ParamName2#ParamValue2
	session.setAttribute("xtrParam",param);
	session.setAttribute("SUBQUERY", subQueryData);
	session.setAttribute("QryKey", QryKey);
	session.setAttribute("reportName", reportName);
	 
%> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Amur</title>
	
 
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet" />   
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/css/jquery-ui-1.8.21.custom.min.css" />
<link href="${pageContext.request.contextPath}/css/agency-app.min.css" rel="stylesheet" />
<script type="text/javascript"> 
window.setTimeout(refreshGrid, 2000);

function refreshGrid() { 
	$('#form1')[0].action="${pageContext.request.contextPath}/<%=appName %>/controlallreports.action";
	$('#form1').submit();
}
</script>
</head>
<body>
<form name="form1" id="form1" method="post">

		<div id="content" class="span10">
			<div>
				<ul class="breadcrumb">
					<li><a href="#">Home</a> <span class="divider">&gt;&gt;</span>
					</li>
					<li><a href="#">Reports Processing</a>
					</li>
 				</ul>
			</div>   
				<div class="row-fluid sortable">

					<div class="box span12">

						<div class="box-header well" data-original-title>
							 <i class="icon-edit"></i>List Of Reports Processing
							 
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i
									class="icon-cog"></i></a> <a href="#"
									class="btn btn-minimize btn-round"><i
									class="icon-chevron-up"></i></a> 
							</div>
						</div>
						<div class="box-content"> 
							<table width="98%" border="0" cellpadding="5" cellspacing="1"
								class="table" border=1>
								<tr>
									<td width="20%">&nbsp;</td>
									<td width="10%">&nbsp;</td>
									<td width="60%"> 
										<img src="${pageContext.request.contextPath}/images/ajax-loaders/ajax-loader-5.gif" /> 
									</td> 
								</tr>
								<tr>
									<td >&nbsp;</td>
									<td colspan="2">  
										<h4>Please Wait.. Do not refresh or press back until the process is done. </h4> 
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
