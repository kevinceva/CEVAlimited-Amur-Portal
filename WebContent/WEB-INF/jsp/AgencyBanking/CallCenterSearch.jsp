<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<% String checkTeller= session.getAttribute("userLevel").toString() == null ? "NO_VALUE" : session.getAttribute("userLevel").toString(); %>
<% String userGroup= session.getAttribute("userGroup").toString() == null ? "NO_VALUE" : session.getAttribute("userGroup").toString(); %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Call Center</title> 
  

<style type="text/css">
select#region,
select#headOffice,
select#Location,
select#userid,
select#bank,
select#reportname,
select#custprof,
select#txnType {
	width : 290px;
 }
input#fromDate,
input#toDate,
input#userid1 {
	width : 280px;
 }
</style>

<script type="text/javascript"> 


 
</script>
</head> 
<body> 
<form name="form1" id="form1" method="post" > 
	<div id="content" class="span10">
	
	<div>
		<ul class="breadcrumb">
			<li>
				<a href="home.action">Home</a> <span class="divider">&gt;&gt;</span>
			</li>
			<li><a href="#">Call Center</a>
			</li>
			 
		</ul>
	</div> 
			<div class="box-content" id="top-layer-anchor">
				<span>
				<!-- 	<a href="#" class="btn btn-info" id="sms-details" title='SMS' data-rel='popover'  data-content='SMS Details.'>
					<i class="icon icon-web icon-white"></i>&nbsp;SMS Details</a> &nbsp; -->
					 <input type="button" class="btn btn-info" type="text"  name="sms-details" id="sms-details" value="SMS"   ></input>							
				</span>
							 
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
			 <i class="icon-edit"></i>Call Center Search
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div> 		
		<div class="box-content"> 
			<fieldset>
				<table width="100%" border="0" cellpadding="5" cellspacing="1" 
					id="table table-striped table-bordered bootstrap-datatable">			
						<tr class="even">
<%--  							<td width="25%"><strong><label for="Gender"> Mobile No / Policy No / ID Number <font color="red">*</font></label></strong></td>
 							<td width="1%" ><input type="text" style="width:50px; font-weight:bold;" name="ctryCode"  id="ctryCode" value="+254" readonly/></td> --%>
 							<td></td>
 							<td></td>
 							<td width="15%" ><input type="text" name="srchval"  id="srchval" maxlength="9"/></td>
							<td width="25%" ><input type="button" class="btn btn-primary" type="text"  name="search" id="search" value="Search" width="100" ></input></td>
							<td width="25%"></td>
						</tr> 
				 </table>  
				</fieldset>
	 
		</div>
		</div>
</div> 
 	               
 <script type="text/javascript">  
 

		 
		$('#search').on('click', function () {
						var data="254"+$("#srchval").val();
						$("#srchval").val(data);
						/* var value=$("#srchval").val();
						$("#srchva12").val(value); */
						$("#form1")[0].action="${pageContext.request.contextPath}/<%=appName %>/callcentersearchmob.action";
						$('#form1').submit(); 
					});
		
		$('#sms-details').on('click',function() {
			/* var srhval = '${responseJSON.srchval}';
			alert("value of srchval llll:"+srhval); */
			//$("#srchval").val()=srchal;	
			//alert("srchval::::::::::"+$("#srchval").val());
			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcentersmsdetails.action?";
			$("#form1").submit();					
		});		

</script>
 
<input type="hidden" name="search" id="search" value="" /> 
<!-- <input type="hidden" name="srchva12" id="srchva12" value="" />  -->
		
	</div><!--/#content.span10-->
</form>	 
</body> 
</html>