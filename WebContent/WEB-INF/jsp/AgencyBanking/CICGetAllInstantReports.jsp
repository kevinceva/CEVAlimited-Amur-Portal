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
	/* System.out.println("[Query	] :"+Query	);
	System.out.println("[JRPTCODE	] :"+JRPTCODE	);
	System.out.println("[str	] :"+str	);
	System.out.println("[param	] :"+param+"[dateCheck ]:"+dateCheck+"[reportName :]"+reportName); */
	if(Query == null || Query.length()==0)
			Query	= (String)request.getAttribute("Query");
	//System.out.println("[Query	] :"+Query	);
	/* Query from Property Files and Condition form FrontEnd or html page */
	if(Query==null || Query.length()==0)
	{
		 QryKey	= request.getParameter("QryKey");
		 /* System.out.println("[QryKey	] :"+QryKey	); */
		
		if(QryKey == null || QryKey.length()==0)
				Query	= (String)request.getAttribute("QryKey");
				/* System.out.println("[22 QryKey	] :"+QryKey	); */

		//Query Fields Names from reportQueryFields.properties
		String queryFields = GenUtils.getKeyValue("reportQueryFields",QryKey);
		/* System.out.println("[queryFields	] :"+queryFields	); */
		String buildQuery = "SELECT " + queryFields;
		/* System.out.println("[buildQuery	] :"+buildQuery	); */

		//Extra Query Fields Names from Front End
		extraFields		= request.getParameter("extrafields");
		/* System.out.println("[extraFields	] :"+extraFields	); */
		if(extraFields == null || extraFields.length()==0)
			extraFields	= (String)request.getAttribute("extrafields");
			/* System.out.println("[22 extraFields	] :"+extraFields	); */

		if(extraFields!=null
			&& extraFields.length()!=0)
		{
			buildQuery = buildQuery + ", " + extraFields;
			/* System.out.println("[22 buildQuery	] :"+buildQuery	); */
		}

		//Query Tables Names from reportQueryTables.properties
		String queryTables = GenUtils.getKeyValue("reportQueryTables",QryKey);
		/* System.out.println("[queryTables	] :"+queryTables	); */
		buildQuery = buildQuery + " $FROM$ " + queryTables;
		/* System.out.println("[buildQuery22	] :"+buildQuery	); */

		//Query Conditions from Front End
		String queryConditions	= request.getParameter("queryconditions");
		/* System.out.println("[queryConditions 22	] :"+queryConditions	); */
		if(queryConditions == null || queryConditions.length()==0)
			queryConditions	= (String)request.getAttribute("queryconditions");
			/* System.out.println("[queryConditions 222	] :"+queryConditions	); */

		if(queryConditions!=null && queryConditions.length()!=0)
		{
			buildQuery = buildQuery + " WHERE " + queryConditions;		
		}
		/* System.out.println("[buildQuery 222	] :"+buildQuery	); */
 		Query = buildQuery;
	}	 
	 	
	session.setAttribute("Query",Query);
	session.setAttribute("querymode",querymode);
	session.setAttribute("JRPTCODE",JRPTCODE);
	session.setAttribute("MODE",mode);
	/* System.out.println("[222 Query	] :"+Query +"[querymode :]"+querymode	);
	System.out.println("[222 JRPTCODE	] :"+JRPTCODE +"[mode :]"+mode	); */
	
	 
	subQueryData = GenUtils.getKeyValue("reportQueryFields",QryKey+"_SUB");
	/* System.out.println("[222 subQueryData	] :"+subQueryData ); */
	subQueryData = subQueryData.replaceAll("@@CONDITION@@", dateCheck);
	/* System.out.println("[2222 subQueryData	] :"+subQueryData); */
 	 
	//Extra Paramaters Should be given in ParamName1#ParamValue1##ParamName2#ParamValue2
	session.setAttribute("xtrParam",param);
	session.setAttribute("SUBQUERY", subQueryData);
	session.setAttribute("QryKey", QryKey);
	session.setAttribute("reportName", reportName);
	/* System.out.println("[222 xtrParam	] :"+param +"[SUBQUERY :]"+subQueryData	);
	System.out.println("[222 QryKey	] :"+QryKey +"[reportName :]"+reportName	); */
	 
%> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Amur</title>
	
 
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet" />   
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/css/jquery-ui-1.8.21.custom.min.css" />
<link href="${pageContext.request.contextPath}/css/agency-app.min.css" rel="stylesheet" />
<script type="text/javascript"> 
window.setTimeout(refreshGrid, 2000);

function refreshGrid() { 
	$('#form1')[0].action="${pageContext.request.contextPath}/<%=appName %>/ciccontrolallreports.action";
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
