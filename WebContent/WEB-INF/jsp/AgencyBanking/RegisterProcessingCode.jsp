
<!DOCTYPE html>
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
function getServiceScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/serviceMgmtAct.action';
	$("#form1").submit();
	return true;
}


  

function createSubService(){
	bankAcctFinalData=bankAcctFinalData.slice(1);
	if(bankAcctFinalData==""){
		$("#errors").text("Please add atleast one Processing Code");
		return false;
	}else{
		$("#errors").text("");
		$("#bankMultiData").val(bankAcctFinalData);
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/registerProcessingCodeSubmitAct.action';
		$("#form1").submit();
		return true;
	}
	
}


</script>

<style type="text/css">
label.error {
  font-weight: bold;
  color: red;
  padding: 2px 8px;
  margin-top: 2px;
}
.errmsg {
color: red;
}
.errors {
color: red;
}
</style> 
 

</head>

<body>
	<form name="form1" id="form1" method="post" action="">
	 

			<div id="content" class="span10">
            			<!-- content starts -->
			    <div>
						<ul class="breadcrumb">
						  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
						  <li> <a href="#">Fee Management</a> <span class="divider"> &gt;&gt; </span></li>
						  <li><a href="#"> Register Transaction Processing Code</a></li>
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
								<i class="icon-edit"></i> Register Transaction Processing Code
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
						</div>

					<div id="primaryDetails"  class="box-content">
						<fieldset>
							<table width="950" border="0" cellpadding="5" cellspacing="1" 
								class="table table-striped table-bordered bootstrap-datatable " >
								
								<tr class="even">
									<td ><strong><label for="Bin">Transaction Processing Code<font color="red">*</font></label></strong></td>
									<td><input name="processingCode" type="text" id="processingCode" class="field"  maxlength="6"  value='${responseJSON.processingCode}'></td>
									<td ><strong><label for="processingCode Desc">Transaction Processing Description<font color="red">*</font></label></strong></td>
									<td><input name="processingDescription" type="text"  id="processingDescription" class="field" value='${responseJSON.processingDescription}'  maxlength="50" ></td>
								</tr>
								<tr class="even">
										<td colspan="4" align="center"><input type="button" class="btn btn-success" name="addCap2" id="addCap2" value="Add" ></input></td>
								</tr>
							</table>
						</fieldset>
						
		<script >
				var val = 1;
				var rowindex = 1;
				var colindex = 0;
						var bankAcctFinalData="";
						 $('#addCap2').on('click', function () {
							var processingCode = $('#processingCode').val() == undefined ? ' ' : $('#processingCode').val();
							var processingDescription = $('#processingDescription').val() == undefined ? ' ' : $('#processingDescription').val();
							
							var eachrow=processingCode+","+processingDescription;
													
						var addclass = "";
						if(processingCode == '') 
						{
							alert('please enter Transaction Processing Code');			
						}
						else if(processingDescription == '') 
						{
							alert('please enter Transaction Processing Description');			
						}
						
						else
						{ 
								if(val % 2 == 0 ) {
									addclass = "even";
									val++;
								}
								else {
									addclass = "odd";
									val++;
								}  
								var rowCount = $('#tbody_data1 > tr').length;
								
								
								  
								var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
									"<td>"+rowindex+"</td>"+
									"<td><input type='hidden' name='frequencies' value='"+processingCode+"' />"+processingCode+"</td>"+	
									"<td><input type='hidden' name='dateTime' value='"+processingDescription+"' />"+processingDescription+" </td>"+ 
									"<td> <a class='btn btn-min btn-info' href='#' id='editDat'> <i class='icon-edit icon-white'></i></a>  <a class='btn btn-min btn-danger' href='#' id='deleteemail'> <i class='icon-trash icon-white'></i> </a></td></tr>";
										
								$("#tbody_data1").append(appendTxt);	 
								$('#processingCode').val('');
								$('#processingDescription').val('');
								bankAcctFinalData=bankAcctFinalData+"#"+eachrow;
								
								rowindex = ++rowindex;
								colindex = ++ colindex; 
						}
				});   



		</script>
								
			<input name="subServiceText" type="hidden" id="subServiceText" class="field"  />	 
			<input type="hidden" name="bankMultiData" id="bankMultiData" value="" />
			<fieldset>
				<table width="100%" class="table table-striped table-bordered bootstrap-datatable "
					id="bankAcctData">
					  <thead>
							<tr >
								<th>Sno</th>
								<th>processingCode</th>
								<th>processing Description</th>
								<th>Action</th>
							</tr>
					  </thead>    
					 <tbody  id="tbody_data1">
					 </tbody>
				</table>
			</fieldset>		
										
			</div>
		</div><!--/#content.span10-->
	</div><!--/fluid-row-->  
	<div align="center">
		<a  class="btn btn-danger" href="#" onClick="getServiceScreen()">Back</a> &nbsp;&nbsp;
		<a  class="btn btn-danger" href="#" onClick="createSubService()">Submit</a>
	</div>  
</div> 
</form>
</body>
</html>
