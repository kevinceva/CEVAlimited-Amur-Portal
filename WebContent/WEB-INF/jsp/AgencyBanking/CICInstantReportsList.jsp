<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@page import="java.util.Map"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@page import="net.sf.jasperreports.engine.JasperReport"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.ceva.reports.fileserver.beans.ReportDescriptor"%>
<%@page
	import="com.ceva.crypto.utils.CryptoTool,com.ceva.util.PasswordGenerate,com.ceva.util.InitCrypto"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="com.ceva.reports.fileserver.services.ReportServices"%>
<%@page import="java.net.URL"%>

 
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>

<%	 	
	
	System.out.println(" Checking the Session Id :"+session.isNew());
		//if(session.isNew()==true){
    if (session==null){   
			 
              request.getRequestDispatcher(
				"/jsp/logout.jsp")
				.forward(request, response);
				return;	
				
	}else{%>

<%!
	private SimpleDateFormat inputFormat		= new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat outputFormat		= new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat displayFormat		= new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	private SimpleDateFormat formatInFileName	= new SimpleDateFormat("yyyyMMddHHmm");
	private String str_reportPath				= null;
	private String str_reportFromDate			= null;
	private String currentUri					= null;
	private String filePath						= null;
	private String myPath						= null;
	private String k							= null;
	private JasperPrint jasperPrintObj			= null;
	private String uri							= null;
 	
	ReportServices reportServices=new ReportServices();
	Map<String,String> reportFormatsMap= null; 
	ReportDescriptor[] contentResourceList=new ReportDescriptor[0];
	SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
  %>
  
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Amur</title>
 
</head>
<body>
	<div id="content" class="span10">
		
			<div>
				<ul class="breadcrumb">
					<li><a href="#">Home</a> <span class="divider">&gt;&gt;</span>
					</li>
					<li><a href="#">Reports List</a>
					</li>
					 
				</ul>
			</div>
			<%	
	session = request.getSession(true);
	InitCrypto.initializeCrypto(request);
	str_reportFromDate	= (String)session.getAttribute("reqFromDate");
	str_reportPath		= (String)session.getAttribute("reportPath");
	jasperPrintObj		= (JasperPrint)session.getAttribute("jasperPrintObj");
	session.setAttribute("jasperPrintObj",jasperPrintObj); 
 	try
	{ 
  		if(contentResourceList!=null) {
			contentResourceList= reportServices.listPrepaidReports(str_reportPath);
 			String uri=contentResourceList[0].getUriString();
 			//currentUri=uri.substring(0,uri.lastIndexOf("/")-1);
 			currentUri=uri.substring(0,uri.lastIndexOf("/"));
  			reportFormatsMap = new LinkedHashMap<String,String>(); 
         
         for (ReportDescriptor reportDesc : contentResourceList ) {
 				String name=(reportDesc.getFileame()==null?"":reportDesc.getFileame());            
     			String key=name.substring(0,name.lastIndexOf('.'));
             	String format=name.substring(name.lastIndexOf('.')+1);
     			reportFormatsMap.put(key,format+"~"+(String)(reportFormatsMap.get(key)==null?"":reportFormatsMap.get(key)));
          }
          
		}
%> 

<form name="form1" id="form1" method="post" action="${pageContext.request.contextPath}/<%=appName %>/userGrpCreation.action?pid=7">

	<div class="row-fluid sortable">
           
	<div class="box span12">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>List Of Reports 
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
				
			</div>
		</div>
<div class="box-content">
			
		<table width="98%" border="0" cellpadding="5" cellspacing="1"
		 class="table table-striped table-bordered bootstrap-datatable">
		<tr>
		<td width="60%"><strong>Report Name </strong></td>
		<td width="29%"><strong>Report Date</strong></td>	
<%	
	Iterator<String> iter1 = reportFormatsMap.keySet().iterator();
	List<String> fExtns =null;
	while(iter1.hasNext())
	{
		String k1=(String) iter1.next();
		String v1=(String) reportFormatsMap.get(k1);
		String fileNamepath1=currentUri+File.separator+k1;
		 

		String v1Array[] = v1.split("~"); 
		fExtns =  Arrays.asList(v1Array); 
		Collections.sort(fExtns); 
		  
		for(String fExtnsStr:fExtns)
		{
			if(fExtnsStr.equalsIgnoreCase("PDF"))
			{%>
				<td width="3%" align="center"><strong>PDF</strong></td>		
			<%}
			else if(fExtnsStr.equalsIgnoreCase("XLS"))
			{%>
				<td width="3%" align="center"><strong>XLS</strong></td>					
			<%}
			else if(fExtnsStr.equalsIgnoreCase("CSV"))
			{%>
				<td width="3%" align="center"><strong>CSV</strong></td>
			<%}
			else if(fExtnsStr.equalsIgnoreCase("HTML"))
			{%>
				<td width="3%" align="center"><strong>HTML</strong></td>
			<%} 
		}%>
	</tr> 
	<tr>

<%
	}
	
	Iterator<String> iter = reportFormatsMap.keySet().iterator();
	int index=0;
	long dispstart=System.currentTimeMillis();
	String param=null;
	while(iter.hasNext())
	{
		String key=(String)iter.next();
		String value=(String)reportFormatsMap.get(key);
		String fileNamepath=currentUri+File.separator+key;
	 
		String vArray[] = value.split("~");
 		fExtns =  Arrays.asList(vArray);
		Collections.sort(fExtns);

		System.out.println("key is :::: " + key);
		String rName= session.getAttribute("reportName").toString();  //key.substring(0,key.lastIndexOf('-'));
		String rDate=key.substring(key.lastIndexOf('-')+1);
		System.out.println("rName is :::: " + rName);
		String formats[]=value.split("~");
		if(index%2==1) 
			out.println("<tr class='even'>");
		else
			out.println("<tr class='odd'>");	
%>
		<td><%=rName%></td>
		<td><%=str_reportFromDate%></td>
<%
 		for(String fExtnsStr:fExtns)
		{    
			if(fExtnsStr.equalsIgnoreCase("PDF"))
			{%>
				<%param=PasswordGenerate.encodeURL(CryptoTool.ByteArrayToHexString(CryptoTool.encrypt(currentUri+File.separator+key+".pdf").getBytes()));%>
				<td align="center"><a href="../ReportExporterServlet?param=<%=param%>" onmousedown="window.status='  '; return true;"  onMouseOut="window.status='  ';  return true;"><%= (value.indexOf("pdf")>=0?"<img src=\"../images/pdf.gif\" border=\"0\" />":"") %></a></td>
			<%}
			else if(fExtnsStr.equalsIgnoreCase("XLS"))
			{%>
				<%param=PasswordGenerate.encodeURL(CryptoTool.ByteArrayToHexString(CryptoTool.encrypt(currentUri+File.separator+key+".xls").getBytes()));%>
				<td align="center"><a href="../ReportExporterServlet?param=<%=param%>" onmousedown="window.status='  '; return true;"  onMouseOut="window.status='  ';  return true;"><%= (value.indexOf("xls")>=0?"<img src=\"../images/xls.gif\" border=\"0\" />":"") %></a></td>
			<%}
			else if(fExtnsStr.equalsIgnoreCase("CSV"))
			{%>
				<%param=PasswordGenerate.encodeURL(CryptoTool.ByteArrayToHexString(CryptoTool.encrypt(currentUri+File.separator+key+".csv").getBytes()));%>
				<td align="center"><a href="../ReportExporterServlet?param=<%=param%>" onmousedown="window.status='  '; return true;" onMouseOut="window.status='  ';  return true;"><%= (value.indexOf("csv")>=0?"<img src=\"../images/csv.gif\" border=\"0\" />":"") %></a></td>
			<%}
			else if(fExtnsStr.equalsIgnoreCase("HTML"))
			{%>
				<td align="center"><a href="../HtmlReportExporterServlet" target="_blank" onmousedown="window.status='  '; return true;"  onMouseOut="window.status='  ';  return true;"><%= (value.indexOf("html")>=0?"<img src=\"../images/html.gif\" border=\"0\" />":"") %></a></td>
			<%}			 
		}%>
		</tr>
		</table>
	<%
			index++;
		}
			long dispend=System.currentTimeMillis();
			dispend=dispend-dispstart;
			
		}
		catch(Exception e)
		{
			throw new ServletException(e);
		}	
		 
	%>
				<table width="950" border="0" cellpadding="0"  cellspacing="0" class="head1">
					<tr>
					  <td>&nbsp;</td>
					</tr>
				  </table>			 
			   
									
				  <div class="form-actions">
					<button type="submit" class="btn btn-primary">Next</button>
					<button class="btn">Cancel</button>
					<input name="tag" type="hidden" id="tag" value="newprod" />
				  </div>
			  </div>
                    </div>
                   </div> 

		 <script type="text/javascript">  
			$(document).ready(function () { 
				$('#save').live('click', function () {  
					$("#form1")[0].action="${pageContext.request.contextPath}/AgencyBanking/userGrpCreation.action?pid=7";
					$('#form1').submit(); 
				});	 
			}); 
		</script>

			</form>
		</div> 
</body> 
</html><%}%>
