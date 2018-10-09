
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 
<script type="text/javascript">  
  
$(function() { 
	
	var val = 1;
	var rowindex = 1; 
	var bankfinalData="<s:property value="multiData" />";
	var bankfinalDatarows=bankfinalData.split("#");
	if(val % 2 == 0 ) {
		addclass = "even";
		val++;
	}
	else {
		addclass = "odd";
		val++;
	} 
	
	var offArr = '${officeLocation}'.split(",");
	
	for(var i=0;i<bankfinalDatarows.length;i++) {
		var eachrow=bankfinalDatarows[i];
		var eachfieldArr=eachrow.split(",");
		var service=eachfieldArr[0];
		var accountname=eachfieldArr[2];
		var openbalance=eachfieldArr[3];
		var closebalance = eachfieldArr[1];
		var accounttype = offArr[i]; 
		var accounttype1 = eachfieldArr[11]; 

		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
			 "<td>"+rowindex+"</td>"+ 
			 "<td>"+service+"</td>"+
			 "<td>"+accountname+"</td>"+
			 "<td>"+openbalance+" </td>"+
			 "<td>"+closebalance+"</td>"+
			 "<td>"+accounttype+"</td>"+
			 "<td>"+accounttype1+"</td>"+
		 "</tr>";
		$("#tbody_data").append(appendTxt);  
		rowindex++;
	} 
		
	$('#btn-cancel').live('click', function () {  
		var url="${pageContext.request.contextPath}/<%=appName %>/getICTAdminCreatePage.action"; 
		$("#form1")[0].action=url;
		$("#form1").submit();  
	});
		
	$('#btn-submit').live('click', function () {  
		var url="${pageContext.request.contextPath}/<%=appName %>/insertIctAdminDetails.action"; 
		$("#form1")[0].action=url;
		$("#form1").submit(); 
		 
	});    
}); 
	

</script>

</head>
<body> 
<form name="form1" id="form1" method="post"> 
	 
	<div id="content" class="span10"> 
		<div> 
			<ul class="breadcrumb">
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
			  <li> <a href="userGrpCreation.action?pid=7">User Management</a> <span class="divider"> &gt;&gt; </span></li> 
			  <li> <a href="getICTAdminCreatePage.action">User Creation</a>  </li> 
			   <li> <a href="#">User Creation Confirmation</a>  </li> 
			</ul>
		</div>  

			
  <div class="row-fluid sortable"> 
	<div class="box span12"> 
			<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>User Details Confirm
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
				</div>
			</div>  
			<div class="box-content">	  
				<fieldset>		 
					<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
								id="mytable">
					  <thead>
							<tr>
								<th>Sno</th>
								<th>User Id</th>
								<th>First Name</th>
								<th>Last Name</th>
								<th>Employee No</th>
								<th>Office Location</th>
								<th>E-Mail</th>							 
							</tr>
					  </thead>    
					  <tbody id="tbody_data"> 
					  	<s:generator val="%{#multiData}" var="bankDat" separator="#" >  
							<s:iterator status="itrStatus">  
								<td><s:property /></td> 
							</s:iterator>  
						</s:generator>
					  </tbody>
					</table> 
				</fieldset>
			</div> 
		 

			</div>
			</div>
		<div class="form-actions">
			  <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Confirm" width="100" ></input>
				&nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Back" width="100" ></input> 
				
				<input name="groupID" type="hidden" id="groupID" value="<s:property value="groupID" />" />
				<input name="entity" type="hidden" id="entity" value="<s:property value="entity" />" />
				<input type="hidden" name="multiData" id="multiData" value="<s:property value="multiData" />" />
				<input type="hidden" name="officeLocation" id="officeLocation" value="<s:property value="officeLocation" />" />
		</div> 
	</div> 
 	 
</form> 
</body>  
</html>