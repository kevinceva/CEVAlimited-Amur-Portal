
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
</style>
<SCRIPT type="text/javascript">  
$(document).ready(function(){ 
	

	
	$('#btn-Cancel').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/newPayBillAct.action";
		$("#form1").submit();					
	}); 
	
});
</SCRIPT>  
<s:set value="responseJSON" var="respData"/>
</head> 
<body>
	<form name="form1" id="form1" method="post">
	  <div id="content" class="span10"> 
			 
		    <div>
				<ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider">&gt;&gt; </span></li>
					<li><a href="#">Biller Management</a> <span class="divider">&gt;&gt; </span></li>
					<li><a href="#">View Bill</a></li>
				</ul>
			</div>  
			
			 <table height="3">
			 <tr>
			    <td colspan="3">
			    	<div class="messages" id="messages"><s:actionmessage /></div>
			    	<div class="errors" id="errors"><s:actionerror /></div>
			    </td>
		    </tr>
		 </table>
		 	
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
							<td width="30%" > ${responseJSON1.ABBREVATION} <input type="hidden" name="abbreviation"  id="abbreviation" value="${responseJSON1.ABBREVATION}"  /></td> 
						</tr>
						<tr>	
							<td width="20%" ><label for="Code"><strong>Code</strong></label></td>
							<td width="30%" > ${responseJSON1.BILLER_ID} <input type="hidden" name="billerCode"  id="billerCode"  value="${responseJSON1.BILLER_ID}"   /></td>							
						</tr>
					  	<tr> 
							<td><label for="Name"><strong>Name</strong></label></td>
							<td >  ${responseJSON1.NAME} <input type="hidden" name="name"  id="name"   value="${responseJSON1.NAME}"  width="400px"  />   </td>
						</tr>
						<tr>	
							<td><label for="Agency Type"><strong>Agency Type</strong></label></td>
							<td> 
								 ${responseJSON1.AGENCY_TYPE} <input type="hidden" name="agencyType"  id="agencyType"   value="${responseJSON1.AGENCY_TYPE}"    />
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
							<td width="30%"> ${responseJSON1.ADDRESS} <input type="hidden" name="address"  id="address"   value="${responseJSON1.ADDRESS}"    />   </td>  
							
						</tr>
					  	<tr> 
							<td><label for="Telephone"><strong>Telephone</strong></label></td>
							<td colspan=3>${responseJSON1.TELEPHONE}<input type="hidden" name="telephone"  id="telephone"   value="${responseJSON1.TELEPHONE}"    />   </td>  
							 
						</tr>
						<tr> 
							<td><label for="Contact Person"><strong>Contact Person</strong></label></td>
							<td colspan=3> 
								${responseJSON1.CONTACT_PERSON} <input type="hidden" name="contactPerson"  id="contactPerson"   value="${responseJSON1.CONTACT_PERSON}"    />   
							</td>
						</tr>
						<tr>
							<td><label for="Email"><strong>Email</strong></label></td>
							<td colspan=3> 
								${responseJSON1.EMAIL} <input type="hidden" name="email"  id="email"   value="${responseJSON1.EMAIL}"    />   
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
							<td width="30%"> ${responseJSON1.COMM_TYPE}   <input type="hidden" name="commissionType"  id="commissionType"   value="${responseJSON1.COMM_TYPE}"/>    </td>
						
						</tr> 
						<tr > 							 
							<td width="20%" ><label for="Commission Type"><strong>Amount/Rate</strong></label></td>
							<td width="30%"> ${responseJSON1.AMOUNT}   <input type="hidden" name="commissionType"  id="commissionType"   value="${responseJSON1.AMOUNT}"/>    </td>
						
						</tr> 
						
				</table>
			</fieldset>  
		</div>
	</div>
	</div> 
     	<div class="form-actions" >
           &nbsp;<input type="button" class="btn btn-primary" name="btn-Cancel" id="btn-Cancel" value="Next" width="100" ></input>
          </div>  
               						 
	</div> 
 </form>
</body>
</html> 