
<!DOCTYPE html>

<%@taglib uri="/struts-tags" prefix="s"%> 

<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
		
	 
<script type="text/javascript" >
$(document).ready(function() {
	 
	var val = 1;
	var rowindex = 1;
	var colindex = 0;
	var bankacctfinalData="${responseJSON1.BANK_MULTI_DATA}";
	var bankacctfinalDatarows=bankacctfinalData.split("#");
	if(val % 2 == 0 ) {
	addclass = "even";
	val++;
	}
	else {
	addclass = "odd";
	val++;
	}  
	var rowCount = $('#tbody_data > tr').length;

	
		for(var i=0;i<bankacctfinalDatarows.length;i++){
			var eachrow=bankacctfinalDatarows[i];
			var eachfieldArr=eachrow.split(",");
			var billerId = eachfieldArr[0];
			var billerCode = eachfieldArr[1];
			var accountNumber = eachfieldArr[2];
			var accountName = eachfieldArr[3];
			var accountType = eachfieldArr[4];
			
			
			colindex = ++ colindex; 
			var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
			"<td>"+colindex+"</td>"+
			"<td>"+billerId+"</td>"+	
			"<td>"+billerCode+"</td>"+	
			"<td>"+accountNumber+"</td>"+	
			"<td>"+accountName+"</td>"+	
			"<td>"+accountType+" </td>"+ 
			"</tr>";
			
			$("#tbody_data1").append(appendTxt);	  
			rowindex = ++rowindex;
		}
		
}); 

function createSubService(){ 
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/billerAccountCreatConfirmAct.action';
	$("#form1").submit();
	 
}


</script>
 
</head>

<body>
	<form name="form1" id="form1" method="post" action="">
	 
			<div id="content" class="span10"> 
			 
			    <div>
					<ul class="breadcrumb">
						<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
						<li><a href="#"> Pay Bill</a></li><span class="divider"> &gt;&gt; </span>
						<li><a href="#"> Biller Account Creation</a></li>
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
					 
				<div class="row-fluid sortable"><!--/span-->

					<div class="box span12"> 
							<div class="box-header well" data-original-title>
								<i class="icon-edit"></i>Biller Account Creation
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
								</div>
							</div> 
							<div id="bankAccountInformation"  class="box-content"> 
								<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
									id="bankAcctData"  >
										  <thead>
												<tr >
													<th>Sno</th>
													<th>Biller Id</th>
													<th>Biller Code</th>
													<th>Account Number</th>
													<th>Account Name</th>
													<th>Account Type</th>
												</tr>
										  </thead>    
										 <tbody  id="tbody_data1">
										 </tbody>
								</table>
							</div>
							<input type="hidden" name="bankMultiData" id="bankMultiData" value="${responseJSON1.BANK_MULTI_DATA}" /> 
										
							</div>
						</div>
					 
					<div class="form-actions">  
						<a  class="btn btn-danger" href="#" onClick="createSubService()">Confirm</a>
					</div> 
			</div>
	 
</form>
</body>
</html>
