
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 <%@taglib uri="/struts-tags" prefix="s"%> 
<style type="text/css">
.errors {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
input#abbreviation{text-transform:uppercase};
</style>
<SCRIPT type="text/javascript"> 
 
$(document).ready(function(){   
	
	$('#btn-Cancel').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/viewBillerDetails.action?pid=91";
		$("#form1").submit();					
	});  
	 
});
  
</SCRIPT>  
</head> 
<body>
	<form name="form1" id="form1" method="post" autocomplete="off">
	  <div id="content" class="span10"> 
			 
		    <div> 
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				    <li> <a href="viewBillerDetails.action?pid=91">Pay Biller Details</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li><a href="#">View Biller</a></li>
				</ul>
			</div>   
		 	
	<div class="row-fluid sortable"> 
		<div class="box span12">  
			<div class="box-header well" data-original-title>
				 <i class="icon-edit"></i>Basic Details  
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					
				</div>
			</div>
						
			<div class="box-content">
				<fieldset>
					 
				<table width="98%"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " > 
						<tr>
							<td width="20%" ><label for="Abbreviation"><strong>Abbreviation</strong></label></td>
							<td width="30%" ><s:property value='billerBean.abbreviation' /><input type="hidden" name="abbreviation"  id="abbreviation" value="<s:property value='billerBean.abbreviation' />"  /></td> 
							<td width="20%" ><label for="Code"><strong>Code</strong></label></td>
							<td width="30%" ><s:property value='billerBean.billerCode' /><input type="hidden" name="billerCode"  id="billerCode"  value="<s:property value='billerBean.billerCode' />"  readonly="readonly"  /></td>							
						</tr>
					  	<tr> 
							<td><label for="Name"><strong>Name</strong></label></td>
							<td> <s:property value='billerBean.name' /> <input type="hidden" name="name"  id="name"   value="<s:property value='billerBean.name' />"  width="400px"  />   </td>						 
							<td><label for="Agency Type"><strong>Agency Type</strong></label></td>
							<td>  <s:property value='billerBean.agencyType' /> </td> 
						</tr>
						<tr > 
							<td><label for="Account Type"><strong>Account Type</strong></label></td>
							<td>  <s:property value='billerBean.accountType' /> </td>
							<td><label for="Account No"><strong>Account No</strong></label></td>
							<td>  <s:property value='billerBean.accountNo' /> </td>
						</tr> 
						<tr > 
							<td><label for="Biller Type"><strong>Biller Type</strong></label></td>
							<td colspan=3>  <s:property value='billerBean.billerType' /> </td>
						</tr>						
				</table>
			</fieldset>  
		</div>
	</div>
	</div> 
	
	<div class="row-fluid sortable"> 
		<div class="box span12">  
			<div class="box-header well" data-original-title>
				 <i class="icon-edit"></i>Contact Details  
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					
				</div>
			</div>
						
			<div class="box-content">
				<fieldset>
					 
				<table width="98%"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " > 
						<tr> 
							<td width="20%" ><label for="Address"><strong>Address</strong></label></td>
							<td width="30%"><s:property value='billerBean.address' /> <input type="hidden" name="address"  id="address"   value="<s:property value='billerBean.address' />"    />   </td>  
							<td width="20%" >&nbsp;</td>
							<td width="30%" >&nbsp;</td>
						</tr>
					  	<tr> 
							<td><label for="Telephone"><strong>Telephone</strong></label></td>
							<td colspan=3><s:property value='billerBean.telephone' /><input type="hidden" name="telephone"  id="telephone"   value="<s:property value='billerBean.telephone' />"    />   </td>  
							 
						</tr>
						<tr> 
							<td><label for="Contact Person"><strong>Contact Person</strong></label></td>
							<td colspan=3> 
								<s:property value='billerBean.contactPerson' /> <input type="hidden" name="contactPerson"  id="contactPerson"   value="<s:property value='billerBean.contactPerson' />"    />   
							</td>
						</tr>
						<tr>
							<td><label for="Email"><strong>Email</strong></label></td>
							<td colspan=3> 
								<s:property value='billerBean.email' /> <input type="hidden" name="email"  id="email"   value="<s:property value='billerBean.email' />"    />   
							</td>
						</tr> 
				</table>
			</fieldset>  
		</div>
	</div>
	</div> 
	
	<div class="row-fluid sortable"> 
		<div class="box span12">  
			<div class="box-header well" data-original-title>
				 <i class="icon-edit"></i>Commissions
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>					
				</div>
			</div>
						
			<div class="box-content">
				<fieldset>					 
				<table width="98%"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
						<tr > 							 
							<td width="20%" ><label for="Commission Type"><strong>Commission Type</strong></label></td>
							<td width="30%">  <s:property value='billerBean.commissionType' /> </td>
						 	<td width="20%" >&nbsp;</td>
						 	<td width="30%" >&nbsp;</td>
						</tr> 
						<tr > 
							<s:set value="billerBean.commissionType" var="commType"/> 
							
							<s:if test="#commType == 'Amount' " > 
								<td ><label for="Amount"><strong>Amount</strong></label></td>
							</s:if>
							<s:else>
								<td ><label for="Rate"><strong>Rate</strong></label></td>
							</s:else>
							<td colspan="3"> <s:property value='billerBean.amount' /> <input type="hidden" name="amount"  id="amount"   value="<s:property value='billerBean.amount' />"/>   </td>  
						</tr>
					  
				</table>
			</fieldset>  
		</div>
	</div>
	</div> 
   	<div class="form-actions" >
         <input type="button" class="btn " name="btn-Cancel" id="btn-Cancel" value="Next" width="100" ></input>
       </div>  
               						 
	</div> 
 </form> 
</body>
</html> 