
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
	//bankacctfinalData=bankacctfinalData.slice(1);
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
			var processingCode=eachfieldArr[0];
			var processingDesc=eachfieldArr[1];
			
				var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
				"<td >"+rowindex+"</td>"+
				"<td><input type='hidden' name='frequencies' value='"+processingCode+"' />"+processingCode+"</td>"+	
				"<td><input type='hidden' name='dateTime' value='"+processingDesc+"' />"+processingDesc+" </td>"+ 
				"</tr>";
				
				$("#tbody_data1").append(appendTxt);	  
			rowindex = ++rowindex;
			colindex = ++ colindex; 
		}
		
});

 

 var registerBinRules = {
   rules : {
    bankCode : { required : true },
	bankName : { required : true }
   },  
   messages : {
    bankCode : { 
       required : "Please enter Bank Code."
        },
	bankName : { 
       required : "Please enter Bank Name."
        }
   } 
  };
  

function createSubService(){
	
	$("#form1").validate(registerBinRules);
	if($("#form1").valid()){	
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/inserProcessingCodeAct.action';
		$("#form1").submit();
		return true;
	}else{
			return false;
	}

}


</script>
 

</head>

<body>
	<form name="form1" id="form1" method="post" action="">
	 
		<div id="content" class="span10">
            			<!-- content starts -->
			    <div>
						<ul class="breadcrumb">
						  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
						  <li> <a href="#">Fee Management</a> <span class="divider"> &gt;&gt; </span></li>
						  <li><a href="#">Register Transaction Processing Code</a></li>
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
								<i class="icon-edit"></i>Register Transaction Processing Code
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div>

							<div id="primaryDetails" class="box-content"> 
								<fieldset>
									<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
											id="bankAcctData" >
									  <thead>
											<tr>
												<th>Sno</th>
												<th>Transaction Processing Code</th>
												<th>Transaction Processing Description</th>
											</tr>
									  </thead>    
									 <tbody  id="tbody_data1">
									 </tbody>
									</table>
								</fieldset>
							</div>
						</div>
					</div>
		<input type="hidden" name="bankMultiData" id="bankMultiData" value="${responseJSON.BANK_MULTI_DATA}"></input>   

		<div align="center"> 
			<a  class="btn btn-danger" href="#" onClick="createSubService()">Confirm</a>
		</div> 
	</div>  
</form>
</body>
</html>
