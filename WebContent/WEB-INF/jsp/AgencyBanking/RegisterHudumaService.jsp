
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="TEAM">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 
<script type="text/javascript" > 
var val = 1;
var rowindex = 1;
var colindex = 0;
var bankAcctFinalData="";
var specialKeys = new Array();

function getServiceScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/serviceMgmtAct.action';
	$("#form1").submit();
	return true;
}

function createSubService(){
	
	var rowCount = $('#tbody_data1 > tr').length; 
	$("#error_dlno").text('');
	//bankAcctFinalData=bankAcctFinalData.slice(1);
	if(rowCount == 0) {
		$("#errors").text("Please add atleast one Processing Code");
		return false;
	}else{
		var finalData = "";
		$("#form1").validate().cancelSubmit = true;
		$('#multi-row-data > span').each(function(index) {  
				//console.log("index ["+$(this).attr('index')+"] name ["+$(this).attr('name')+"]  value["+$(this).val()+"] "); 
				if(index == 0)  finalData = $(this).text();
				else finalData += "#"+ $(this).text();
		}); 
	 
		$('#bankMultiData').val(finalData); 
 		 
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/registerHudumaServiceSubmitAct.action';
		$("#form1").submit();
		return true;
	} 
}

function alignSerialNo(serialId) {  
	if($(serialId).length > 0) {
		$(serialId +' > td:first-child').each(function(index){  
			$(this).text(index+1);
		}); 
	}
}

  
var hudumaRules= {
	rules : {
		hudumaProcessingCode : {required : true},
		hudumaProcessingDesc : {required : true},
		hudumaServiceCode : {required : true},
		hudumaServiceDesc : {required : true},
		virtualCard : {required : true}
	},
	messages : {
		hudumaProcessingCode : { required :"Please enter Huduma Processing Code."},
		hudumaProcessingDesc : {required :"Please enter Huduma Processing Description."},
		hudumaServiceCode : {required :"Please enter Huduma Service Code."},
		hudumaServiceDesc : {required :"Please enter Huduma Service Description."},
		virtualCard : {required :"Please enter Virtual Card."}
	}
};

var headerList = "hudumaProcessingCode,hudumaProcessingDesc".split(",");
var addDataVals = function (clickType) {
	 // add custom behaviour
	try { 
		var  hiddenInput ="";
		var hiddenNames = "";
		var tabArrText = "";
		var appendTxt = "";
		var tabarrindex = 0;
		tabArry = new Array();  
		
		$('#user-details').find('input[type=text],select').each(function(index) { 
			var nameInput = $(this).attr('name');
			var valToSpn = ($(this).attr('value') =='' ? ' ' :$(this).attr('value')); 
		 
			if(nameInput != undefined) {
			  if(index == 0)  {
				hiddenInput += valToSpn;
				hiddenNames += nameInput;
			  }
			  else {
				hiddenInput += ","+valToSpn  ; 
				hiddenNames += ","+nameInput  ; 
			  }
			  
			   if(jQuery.inArray(nameInput, headerList) != -1){  
					tabArry[tabarrindex] = valToSpn;   
					tabarrindex++;
			  }
			} 
		});  
		 
		 $("#multi-row-data").append("<span id='hidden_span_"+rowindex+"' index="+rowindex+" ind-id='"+tabArry[0]+"' hid-names='"+hiddenNames+"' ></span>");
		 $('#hidden_span_'+rowindex).append(hiddenInput);
	 
		var addclass = ""; 
		
		if(val % 2 == 0 ){
			addclass = "even";
			val++;
		}
		else {
			addclass = "odd";
			val++;
		} 
		colindex = ++ colindex;
		
		$(tabArry).each(function(index){ 
			tabArrText+="<td>"+tabArry[index]+" </td> ";
		});
		
		appendTxt = "<tr class="+addclass+" align='center' id='tr-"+rowindex+"' index='"+rowindex+"'>"+
				"<td class='col_"+colindex+"'> &nbsp;"+colindex+"   </td> "+ tabArrText+ 
				"<td><a class='btn btn-min btn-info' href='#' id='editDat' index='"+rowindex+"'> <i class='icon-edit icon-white'></i></a> &nbsp;<a class='btn btn-min btn-warning' href='#' id='row-cancel' index='"+rowindex+"'> <i class='icon icon-undo icon-white'></i></a>&nbsp; <a class='btn btn-min btn-danger' href='#' id='delete' index='"+rowindex+"'> <i class='icon-trash icon-white'></i></a></td></tr>";
		rowindex = ++rowindex;	 
		
		tabarrindex = 0;
	}
	catch(e) 
	{
		console.log(e);
	} 
	return appendTxt; 
}

 

$(document).ready(function() { 

	// The below event is to delete the entire row on selecting the delete button 
	$("#delete").live('click',function() { 
		var delId = $(this).attr('index');
		$(this).parent().parent().remove();
	 
		var spanId = "";
		$('#multi-row-data > span').each(function(index){  
			spanId =  $(this).attr('index'); 
			if(spanId == delId) {
				$(this).remove();
			}
		}); 
		
		//clearVals();
		// Aligning the serial number
		alignSerialNo("#tbody_data > tr");
		
		//Show Add Button and Hide Update Button
		//$('#modCap').hide();
		//$('#addCap').show();
	}); 

	$('#addCap2').live('click', function () {
		$("#form1").validate(hudumaRules);
		if($("#form1").valid()) { 
			 var appendTxt = addDataVals("ADD");
			// console.log("appendText. "+ appendTxt);
			$("#tbody_data1").append(appendTxt); 
			
			$('#hudumaProcessingCode').val('');
			$('#hudumaProcessingDesc').val('');
			//$('#hudumaServiceCode').val('');
			//$('#hudumaServiceDesc').val('');
			//$('#virtualCard').val('');  
			
		}else{
			return false;
		}
				
	});   
	
	 
	
	specialKeys.push(8); //Backspace
 
	$("#virtualCard").bind("keypress", function (e) {
		var keyCode = e.which ? e.which : e.keyCode
		var ret = ((keyCode >= 48 && keyCode <= 57) || specialKeys.indexOf(keyCode) != -1);
	   // $(".error").css("display", ret ? "none" : "inline");
		return ret;
	});
	$("#virtualCard").bind("paste", function (e) {
		return false;
	});
	$("#virtualCard").bind("drop", function (e) {
		return false;
	});
        
});			 
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
			 
			    <div>
					<ul class="breadcrumb">
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#">Fee Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Register Huduma Service</a></li>
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
								<i class="icon-edit"></i>Register Huduma Service
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div>
						
							<div id="primaryDetails" class="box-content">
								<fieldset> 
									<table width="950" border="0" cellpadding="5" cellspacing="1" 
										class="table table-striped table-bordered bootstrap-datatable " id="service-details">
										
										<tr class="even">
											<td ><strong><label for="Bin">Huduma Service Code<font color="red">*</font></label></strong></td>
											<td ><input name="hudumaServiceCode" type="text" id="hudumaServiceCode" class="field"  maxlength="6"  value='${responseJSON.hudumaServiceCode}'></td>
											<td ><strong><label for="processingCode Desc">Huduma Service Description<font color="red">*</font></label></strong></td>
											<td><input name="hudumaServiceDesc" type="text"  id="hudumaServiceDesc" class="field" value='${responseJSON.hudumaServiceDesc}'  maxlength="50" ></td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Bin">Virtual Card<font color="red">*</font></label></strong></td>
											<td><input name="virtualCard" type="text" id="virtualCard" class="field"  maxlength="20"  value='${responseJSON.virtualCard}'></td>
											<td ></td>
											<td></td>
										</tr> 
									</table> 
								</fieldset>
							</div>
							 <div  class="box-content"> 
								<fieldset> 
									<table width="950" border="0" cellpadding="5" cellspacing="1" 
										class="table table-striped table-bordered bootstrap-datatable " id="user-details"> 
										 
										<tr class="even">
											<td ><strong><label for="Bin">Huduma Processing Code<font color="red">*</font></label></strong></td>
											<td ><input name="hudumaProcessingCode" type="text" id="hudumaProcessingCode" class="field"  maxlength="6"  value='${responseJSON.hudumaProcessingCode}'></td>
											<td ><strong><label for="processingCode Desc">Huduma Processing Description<font color="red">*</font></label></strong></td>
											<td><input name="hudumaProcessingDesc" type="text"  id="hudumaProcessingDesc" class="field" value='${responseJSON.hudumaProcessingDesc}'  maxlength="50" ></td>
										</tr>
										<tr class="even">
												<td colspan="4" align="center"><input type="button" class="btn btn-success" name="addCap2" id="addCap2" value="Add" ></input></td>
										</tr>
									</table> 
								</fieldset>
							
									<input type="hidden" name="bankMultiData" id="bankMultiData" value=""></input>
									<table width="100%" class="table table-striped table-bordered bootstrap-datatable "
										id="bankAcctData"  >
										<thead>
											<tr >
												<th>Sno</th>
												<th>Huduma Processing Code</th>
												<th>Huduma Processing Description</th>
												<th>Action</th>
											</tr>
										</thead>    
										<tbody  id="tbody_data1">
										</tbody>
									</table> 
							</div>
					</div>
				</div>
				<span id="multi-row-data" class="multi-row-data" style="display:none"> </span>
				<input name="subServiceText" type="hidden" id="subServiceText" class="field"  >
				 
		 <div class="form-actions">
			<a  class="btn btn-danger" href="#" onClick="getServiceScreen()">Back</a> &nbsp;&nbsp;
			<a  class="btn btn-danger" href="#" onClick="createSubService()">Submit</a>
			<span id ="error_dlno" class="errors"></span>
		</div>  
	</div> 
</form>
</body>
</html>
