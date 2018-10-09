<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
 <%@taglib uri="/struts-tags" prefix="s"%> 
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<%String appName = session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>

  
<script type="text/javascript" > 
  
var val = 1;
var rowindex = 0;
var colindex = 0;
var bankAcctFinalData="";
var binstatus = "";
var v_message = "";

$(document).ready(function() {
		
		var mydata ='${responseJSON.BANK_LIST}';
    	var json = jQuery.parseJSON(mydata);
    	$.each(json, function(i, v) {
    	    var options = $('<option/>', {value: v.key , text:v.val}).attr('data-id',i);  
    	    $('#bankCode').append(options);
    	});
		
		/*$('#bankCode,#bankName').live('keyup',function(){
			 var id = $(this).attr('id');
			 var v_val = $(this).val(); 
			 $("#"+id).val(v_val.toUpperCase());
		});*/
		
		
		
	// The below event is to delete the entire row on selecting the delete button 
		$('#delete').live('click',function() {  
			var delId = $(this).attr('index');
			$(this).parent().parent().remove();
			if($('#bankAcctData > tbody  > tr').length == 0) { rowindex = 0; colindex=0; }
			
			//console.log(delId );
			var spanId = "";
			$('#multi-row-data > span').each(function(index){  
				spanId =  $(this).attr('index');
				if(spanId == delId) {
					$(this).remove();
				}
			}); 
			//console.log("text1 hidden is ==>"+  $("#multi-row-data").html() );  
		}); 
		
		
		// The below event is to Edit row on selecting the edit button 
		$('#editDat').live('click',function() { 
			$("form").validate().cancelSubmit = true; 
			var v_id=$(this).attr('id');
			
			var index1 = $(this).attr('index');  
 			var parentId =$(this).parent().closest('tbody').attr('id'); 
			var searchTrRows = parentId+" tr"; 
			var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
			
			$(searchTdRow).each(function(index) {  
				 
				//console.log("Index ==> "+ index + " value ==> "+ $(this).text() );
				 
			}); 
			
		}); 
		
		$('#btn-back').live('click', function () { 
			$("form").validate().cancelSubmit = true; 
			var url="${pageContext.request.contextPath}/<%=appName %>/serviceMgmtAct.action"; 
			$("#form1")[0].action=url;
			$("#form1").submit(); 
			 
		});
			
		$('#btn-submit').live('click', function () {  
			var rowCount = $('#tbody_data > tr').length; 
			 $("#error_dlno").text('');
			if(rowCount > 0) {
				$("#form1").validate().cancelSubmit = true;
				var specChar = "";
				var prevCount = "";
				$('#multi-row-data > span').each(function(index){  
					//console.log("index ["+$(this).attr('index')+"] name ["+$(this).attr('name')+"]  value["+$(this).val()+"] ");
				 
					if(index == 0)  finalData = $(this).text();
					else finalData += "#"+ $(this).text();
					 
				}); 
				//console.log("finalData ["+finalData+"]");
				$('#bankMultiData').val(finalData);
 				//var url="${pageContext.request.contextPath}/<%=appName %>/insertIctAdminDetails.action"; 
 				var url="${pageContext.request.contextPath}/<%=appName %>/registerBinSubmitAct.action"; 
				$("#form1")[0].action=url;
				$("#form1").submit(); 
			} else { 
				$("#error_dlno").text("Please add atleast one record.");
			}
		});
		
		$("#bin,#bankIndex").keypress(function (e) {
		 //if the letter is not digit then display error and don't type anything
		 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
			//display error message
			$("#"+$(this).attr('id')+"_err").html("Digits Only").show().fadeOut("slow");
				   return false;
			}
	   }); 


	$('#addCap2').on('click', function () {
		$("#error_dlno").text('');	
			
		 var registerBinRules = {
		   rules : {
				bankCode : { required : true},
				bankName : { required : true },
				bin : { required : true , digits : true }, 
				binDescription : { required : true },
				bankIndex : { required : true , digits : true}
		   },  
		   messages : {
			bankCode : { 
			   required : "Please enter bank code."
				},
			bankName : { 
			   required : "Please enter bank name."
				},
			bin : { 
			   required : "Please enter six digit bin."
				},
			binDescription : { 
			   required : "Please enter bin description."
				},
			bankIndex : { 
			   required : "Please enter bank index."
				}
		   } 
		 };
		 
		$("#form1").validate(registerBinRules);
		if($("#form1").valid()) {
			var rowCount = $('#bankAcctData > tbody > tr').length; 
			var data= $( "#bankCode option:selected" ).val();
			var bankCode=data.split("-")[0];
			var queryString = "entity=${loginEntity}&method=searchBin&bin="+$('#bin').val()+"&bankIndex="+$('#bankIndex').val()+"&bankCode="+bankCode;
			
			$.getJSON("postJson.action", queryString,function(data) {   
					binstatus = data.status; 
					v_message = data.message; 
					//console.log('binstatus ===> '+binstatus);
					//console.log('v_message ===> '+v_message);
					if(binstatus == 'FOUND') {
						if(v_message == 'BIN_BANK_DUP') {
							$('#error_dlno').text('Bin with Bank Code Exists.');
						} else if(v_message == 'BIN_DUP') {
							$('#error_dlno').text('Bin Already Exists.');
						} else if(v_message == 'BANK_CODE_DUP') {
							$('#error_dlno').text('Bank Code Exists.');
						}
					} else {  
					
						var bankcode = $( "#bankCode option:selected" ).val() == undefined ? ' ' : $( "#bankCode option:selected" ).val();
						var bankName = $('#bankName').val() == undefined ? ' ' : $('#bankName').val();
						var bin = $('#bin').val() == undefined ? ' ' : $('#bin').val();
						var binDescription = $('#binDescription').val() == undefined ? ' ' : $('#binDescription').val();
						var bankIndex = $('#bankIndex').val() == undefined ? ' ' : $('#bankIndex').val();
						//alert(bankcode);
						//var eachrow=bin+","+binDescription; 
						bankcode=bankcode.split("-")[0];
						var  hiddenInput ="";
						
						$('#bank-details').find('input,select').each(function(index){ 
							var nameInput = $(this).attr('name');
							var valToSpn = ($(this).attr('value') =='' ? ' ' :$(this).attr('value')); 
							
							if(nameInput != undefined) {
							  if(index == 0) hiddenInput += valToSpn;
							  else hiddenInput += ","+valToSpn  ; 
							}
						}); 
						
						 $("#multi-row-data").append("<span id='hidden_span_"+rowindex+"' index="+rowindex+" ></span>");
 						 $('#hidden_span_'+rowindex).append(hiddenInput);
						var addclass = "";
						if(val % 2 == 0 ) {
							addclass = "even";
							val++;
						}
						else {
							addclass = "odd";
							val++;
						}  
						var rowCount = $('#tbody_data > tr').length;  
						colindex = ++ colindex;   
						var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
									"<td>"+colindex+"</td>"+
									"<td>"+bankcode+"</td>"+	
									"<td>"+bankName+"</td>"+	
									"<td>"+bin+"</td>"+	
									"<td>"+binDescription+" </td>"+ 
									"<td>"+bankIndex+" </td>"+ 
									"<td class='center'> <a class='btn btn-min btn-info' href='#' id='editDat' index='"+rowindex+"'> <i class='icon-edit icon-white'></i></a>  <a class='btn btn-min btn-danger' href='#' id='delete' index='"+rowindex+"'> <i class='icon-trash icon-white'></i> </a></td></tr>";
						rowindex = ++rowindex;						
						$("#tbody_data").append(appendTxt);	 
						$('#bankCode').val('');
						$('#bankName').val('');
						$('#bin').val('');
						$('#binDescription').val('');
						$('#bankIndex').val('');
						//bankAcctFinalData=bankAcctFinalData+"#"+eachrow; 
						
						$('#bankCode').find('option').each(function( i, opt ) { 
							if( opt.value == "" ) {
								$(opt).attr('selected', 'selected');
								$('#bankCode').trigger("liszt:updated");
							}
						}); 
					}
			});   
		}  else {
			return false;				
		}  
	});   
	
	
	var config = {
		      '.chosen-select'           : {},
		      '.chosen-select-deselect'  : {allow_single_deselect:true},
		      '.chosen-select-no-single' : {disable_search_threshold:10},
		      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
		      '.chosen-select-width'     : {width:"95%"}
		    }
		    for (var selector in config) {
		      $(selector).chosen(config[selector]);
		    }
});    

function getBankName(){
	var data=$( "#bankCode option:selected").text();
	var sdata=data.split('-');
	$( "#bankName" ).val(sdata[1]);
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
			<div>
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="#">Fee Management</a> <span class="divider"> &gt;&gt; </span></li>
				  <li><a href="#">Register Bin</a></li>
				</ul>
			</div>
			<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
						<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Register Bin
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

							</div>
						</div>

						<div id="primaryDetails" class="box-content">
							<fieldset>   
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable " id="bank-details">
									<tr class="odd">
										<td width="20%"><strong><label for="Bank Code">Bank Code<font color="red">*</font></label></strong></td>
										<td width="30%">
										<select id="bankCode" name="bankCode" class="chosen-select"
											    required="required" onChange="getBankName()">
														<option value="">Select</option>
										</select>
										<span id="bankCode_err" class="errmsg"></span>
										</td>
										<!--<input name="bankCode" type="text" id="bankCode" class="field"  maxlength="15"  value='${responseJSON.bankCode}' required='true'>  -->
										<td width="20%"><strong><label for="Bank Name">Bank Name<font color="red">*</font></label></strong></td>
										<td width="30%"><input name="bankName" type="text"  id="bankName" class="field" value='${responseJSON.bankName}'  maxlength="50" required='true' readonly></td>
									</tr>
									<tr class="even">
										<td ><strong><label for="Bin">Bin<font color="red">*</font></label></strong></td>
										<td><input name="bin" type="text" id="bin" class="field"  maxlength="6"  value='${responseJSON.bin}' required='true'> <span id="bin_err" class="errmsg"></span></td>
										<td ><strong><label for="Bin Desc">Bin Description<font color="red">*</font></label></strong></td>
										<td><input name="binDescription" type="text"  id="binDescription" class="field" value='${responseJSON.binDescription}' required='true' maxlength="50" ></td>
									</tr>
									<tr class="even">
										<td ><strong><label for="Bank Index">Bank Index<font color="red">*</font></label></strong></td>
										<td><input name="bankIndex" type="text" id="bankIndex" class="field"  maxlength="20" required='true' value='${responseJSON.bankIndex}'><span id="bankIndex_err" class="errmsg"></span></td>
										<td >&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									 
								</table>
							 </fieldset> 
							</div>
							
								<input type="hidden" name="bankMultiData" id="bankMultiData" value=""></input>
							<table width="100%" class="table table-striped table-bordered bootstrap-datatable "
								id="bankAcctData"  >
									  <thead>
											<tr >
												<th>Sno</th>
												<th>Bank Code</th>
												<th>Bank Name</th>
												<th>Bin</th>
												<th>Bin Description</th>
												<th>Bank Index</th>
												<th>Action</th>
											</tr>
									  </thead>    
									 <tbody  id="tbody_data">
									 </tbody>
							</table> 
							

							<input name="subServiceText" type="hidden" id="subServiceText" class="field"  />
							 
						</div>
					</div>
						 
				 
			<span id="multi-row-data" class="multi-row-data" style="display:none"> </span>
			<div class="form-actions"> 
				<input type="button" class="btn btn-success" name="addCap2" id="addCap2" value="Add" />&nbsp;
				<input type="button" class="btn btn-info" name="btn-submit" id="btn-submit" value="Submit" />&nbsp;
				<input type="button" class="btn" name="btn-back" id="btn-back" value="Back" />
				<!-- <a  class="btn btn-danger" href="#" onClick="createSubService()">Submit</a>
				<a  class="btn btn-danger" href="#" onClick="getServiceScreen()">Back</a> &nbsp;&nbsp; -->
				&nbsp;<span id ="error_dlno" class="errors"></span>
			</div> 
	</div> 
</form>
</body>
</html>
