
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
	var bankacctfinalData="${responseJSON.BANK_MULTI_DATA}";
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
			var hudumaService=eachfieldArr[0];
			var hudumaDesc=eachfieldArr[1];
			
				var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
				"<td  >"+rowindex+"</td>"+
				"<td><input type='hidden' name='frequencies' value='"+hudumaService+"' />"+hudumaService+"</td>"+	
				"<td><input type='hidden' name='dateTime' value='"+hudumaDesc+"' />"+hudumaDesc+" </td>"+ 
				"</tr>";
				
				$("#tbody_data1").append(appendTxt);	  
			rowindex = ++rowindex;
			colindex = ++ colindex; 
		}
		
});

  
function createSubService(){
	
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/inserhudumaServiceAct.action';
	$("#form1").submit();
	return true;
	
}


</script>
 
</head>

<body>
	<form name="form1" id="form1" method="post" action="">
	
		
			<div id="content" class="span10">  
		 
			    <div>
						<ul class="breadcrumb">
						  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
						  <li> <a href="#">Fee Management</a> <span class="divider"> &gt;&gt; </span></li>
						  <li><a href="#">Register Huduma Service Confirmation</a></li>
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
								<i class="icon-edit"></i>Register Huduma Service Confirmation
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div> 
							<div id="bankAccountInformation" class="box-content">
								<fieldset>
									<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
										<tr class="even">
											<td ><strong><label for="Bin">Huduma Service Code<font color="red">*</font></label></strong></td>
											<td> ${responseJSON.hudumaServiceCode}
												<input name="hudumaServiceCode" type="hidden" id="hudumaServiceCode" class="field"  maxlength="6"  value='${responseJSON.hudumaServiceCode}'>
												</td>
											<td ><strong><label for="processingCode Desc">Huduma Service Description<font color="red">*</font></label></strong></td>
											<td> ${responseJSON.hudumaServiceDesc}
												<input name="hudumaServiceDesc" type="hidden"  id="hudumaServiceDesc" class="field" value='${responseJSON.hudumaServiceDesc}'  maxlength="50" >
											</td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Bin">Virtual Card<font color="red">*</font></label></strong></td>
											<td> ${responseJSON.virtualCard}
												<input name="virtualCard" type="hidden" id="virtualCard" class="field"  maxlength="20"  value='${responseJSON.virtualCard}'></td>
											<td ></td>
											<td></td>
										</tr>
										
									</table>
								</fieldset>
								
								<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
										id="bankAcctData" >
										  <thead>
											<tr >
												<th>Sno</th>
												<th>Huduma Processing Code</th>
												<th>Huduma Processing Description</th>
											</tr>
										  </thead>    
										 <tbody  id="tbody_data1">
										 </tbody>
								</table>
							</div>
							</div>
							</div>
				<input type="hidden" name="bankMultiData" id="bankMultiData" value="${responseJSON.BANK_MULTI_DATA}"></input>
			 
				 <div class="form-actions"> 
					<a  class="btn btn-danger" href="#" onClick="createSubService()">Confirm</a>
				</div> 
			</div>
		 
</form>
</body>
</html>
