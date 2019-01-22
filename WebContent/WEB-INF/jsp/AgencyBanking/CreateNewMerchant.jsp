<html lang="en">
<head>
<meta charset="utf-8" />
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Brian Kiptoo">
<%@taglib uri="/struts-tags" prefix="s"%>  
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<%String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 
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
<s:set value="responseJSON" var="respData"/>
<script type="text/javascript" >


$(document).ready(function() { 
  	
  	$("#userData1").hide();
	$("#userData2").hide(); 
	$('#bankAgenctNo').val((Math.random() * 100000).toString().substring(0,7).replace(".",""));
	
	var val = 1;
	var rowindex = 1;
	var colindex = 0; 
  $.validator.addMethod("regex", function(value, element, regexpr) {          
		 return regexpr.test(value);
   }, "");  
}); 
 

function getGenerateMerchantScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/generateMerchantAct.action';
	$("#form1").submit();
	return true;
} 	
</script>
<script type="text/javascript">

var merchantCreateRules= {
	rules : {
		merchantName : { required : true, minlength :4 },
		merchantID : {required : true, 	minlength :15, 	maxlength :15},
		storeId : {required : true},
		storeName : {required : true},
		location : {required : true},
		kraPin : {required : true, 	minlength :4,	regex: /^([A-Z]{1}[0-9]{5,}[A-Z]{0,})+$/ },
 		managerName : {required : true},
		addressLine1 : {required : true},
		addressLine2 : {required : true},
		city		: {required : true},
		poBoxNumber : {required : true},
		//telephoneNumber1 : {required : true},
		mobileNumber : {required : true , number : true},
		//faxNumber : {required : true , number : true},
		prmContactPerson : {required : true},
		prmContactNumber : {required : true , number : true},
		email : {required : true, email : true},
		merchantType : {required : true},
		memberType : {required : true} 
	},
	messages : {
		merchantName : { required : "Please Enter Merchant Name."},
		merchantID : { required : "Merchant Id ."},
		storeId : { required : "Please Enter Merchant Name."},
		storeName : { required : "Please Enter Store Name."},
		location : { required : "Please Select Location."},
		kraPin : { required : "Please Enter KRA Pin." , regex : "Please Enter valid KRA Pin eg (C122121111X  or C122121111)" },
		managerName : { required : "Please Enter Manager Name."},
		addressLine1 : { required : "Please Enter Address1."},
		addressLine2 : { required : "Please Enter Address2."},
		city		: { required : "Please Enter City."},
		poBoxNumber : { required : "Please Enter P.O. Box Number."},
		//telephoneNumber1 : { required : "Please Enter Merchant Name."},
		mobileNumber : {required : "Please Enter Mobile Number."},
		//faxNumber : { required : "Please Enter Merchant Name."},
		prmContactPerson : { required : "Please Enter Primary Contact Person."},
		prmContactNumber : { required : "Please Enter Primary Contact Number."},
		email : { required : "Please Enter Email."},
		merchantType : { required : "Please Select Merchant Type."},
		memberType : { required : "Please Select Member Type."} 
	}
};

var form4Rules = {
     rules : { 
		documentId : {  required: true   } 
    },  
    messages : {
			documentId : {   required: "Please Select DocumentId." } 
    } ,
	errorPlacement: function(error, element) {
	    if ( element.is( ':radio' ) || element.is( ':checkbox' ) )
			error.appendTo( element.parent() );
	    else if ( element.is( ':password' ) )
			error.hide();
	    else
			error.insertAfter( element );
	}
};

var form2Rules = {
     rules : { 
		accountNumber : {  required: true   },
		acctDescription : {  required: true },
		bankBranch : { required: true  },
		bankName : {   required:   true },
		transferCode : {   required:   true }
    },  
    messages : {
			accountNumber : {   required: "Please Input Account Number."    },
			acctDescription : {   required: "Please Select Account Description." },
			bankBranch : {  required: "Please Input Bank Name."    },
			bankName : {   required: "Please Input Bank Branch."   } ,
			transferCode : {   required: "Please Input Transfer Code(Swift Code)."   } 
    } ,
	errorPlacement: function(error, element) {
	    if ( element.is( ':radio' ) || element.is( ':checkbox' ) )
			error.appendTo( element.parent() );
	    else if ( element.is( ':password' ) )
			error.hide();
	    else
			error.insertAfter( element );
	}
};



var val = 1; 
var rowindex = 0;
var colindex = 0;
var userstatus = "";
var v_message = ""; 
var tabArry ; 
var modTabArry ;  
var modHeaderBodyArry ;  
var listid = "acctDescription".split(",");
function clearVals(){ 
	$('#accountNumber').val('');
	$('#bankName').val('');
	$('#bankBranch').val('');
	$('#transferCode').val('');
	
	$(listid).each(function(index,val){ 
		$('#'+val).find('option').each(function(i, opt ) { 
			if( opt.value == '' ) {
				$(opt).attr('selected', 'selected');
				$('#'+val).trigger("liszt:updated"); //bankBranch
			}
		}); 
	});

	rowCount = $('#tbody_data1 > tr').length; 
	if(rowCount > 0 )  $("#error_dlno").text('');
}

/** Form2 Add,Modify Starts*/
var index1 = "";
var searchTdRow = "";
var searchTrRows = "";
var selTextList = "acctDescription"; 
var headerList = "accountNumber,acctDescription,bankBranch,bankName,transferCode".split(","); 

function commonData(id,type){
	var hiddenInput ="";
	var hiddenNames = "";
	var tabarrindex = 0;
	tabArry = new Array();
	modTabArry = new Array(); 
	try {
		//console.log(id);
		$(id).find('input[type=text],input[type=hidden],select').each(function(indxf){ 
			var nameInput = "";
			var valToSpn =  "";
			try{
				  nameInput = $(this).attr('name') ;
				  valToSpn = ($(this).attr('value') =='' ? ' ' :$(this).attr('value')); 
			} catch(e1) {
				//console.log('The Exception Stack is :: '+ e1);
			} 
			//console.log("Type ["+$(this).attr('type')+"]  Name ["+$(this).attr('name')+"] Values["+$(this).val()+"]");
			
			if(nameInput != undefined) {
			  if(indxf == 0)  {
				hiddenInput += valToSpn;
				hiddenNames += nameInput;
			  } else {
				hiddenInput += ","+valToSpn; 
				hiddenNames += ","+nameInput; 
			  }
			  
			   if(jQuery.inArray(nameInput, headerList) != -1){ 
				  if(selTextList.indexOf(nameInput) != -1) {
					tabArry[tabarrindex] = ($('select#'+nameInput+' option:selected').text().trim() == "Select" ? " " : $('select#'+nameInput+' option:selected').text().trim()); 
					modTabArry[tabarrindex] = ($('select#'+nameInput+' option:selected').text().trim() == "Select" ? " " : $('select#'+nameInput+' option:selected').text().trim()); 
				  } else {
					tabArry[tabarrindex] = valToSpn; 
					modTabArry[tabarrindex] = valToSpn; 
				  } 
				  tabarrindex++;
			  }
			} 
			 
		}); 
	} catch (e) {
		console.log(e);
	}  
	return hiddenInput+"@@"+hiddenNames;
}

var addBankInfo = function (clickType) {
	 // add custom behaviour
	 var appendTxt = "";
	 var tabArrText = ""; 
	 var data1 = "";
	try { 
		data1 = commonData('#bankAccountInformation','ADD');
		 
		rowindex = $("#multi-row-bank > span") .length ;
		$("#multi-row-bank").append("<span id='hidden_span_"+rowindex+"' index="+rowindex+" ind-id='"+tabArry[0]+"' hid-names='"+data1.split("@@")[1]+"' ></span>");
		$('#multi-row-bank > span#hidden_span_'+rowindex).append(data1.split("@@")[0]);
	 
		var addclass = ""; 
		
		if(val % 2 == 0 ){
			addclass = "even";
			val++;
		} else {
			addclass = "odd";
			val++;
		}
		
		colindex = ++ colindex;
		
		$(tabArry).each(function(index){ 
			tabArrText+="<td>"+tabArry[index].trim()+"</td> ";
		});
		
		appendTxt = "<tr class="+addclass+" align='center' id='tr-"+rowindex+"' index='"+rowindex+"'>"+
				"<td class='col_"+colindex+"'>"+colindex+"</td>"+ tabArrText+ 
				//"<td><a class='btn btn-min btn-info' href='#' id='editDat' index='"+rowindex+"'> <i class='icon-edit icon-white'></i></a> &nbsp;<a class='btn btn-min btn-warning' href='#' id='row-cancel-bank' index='"+rowindex+"'> <i class='icon icon-undo icon-white'></i></a>&nbsp; <a class='btn btn-min btn-danger' href='#' id='delete-bank' index='"+rowindex+"'> <i class='icon-trash icon-white'></i></a></td></tr>";
				"<td><a class='btn btn-min btn-info' href='#' id='editDatBank' index='"+rowindex+"' title='Edit' data-toggle='tooltip'> <i class='icon-edit icon-white'></i></a> &nbsp;<a class='btn btn-min btn-warning' href='#' id='row-cancel-bank' index='"+rowindex+"' title='Reset' data-toggle='tooltip'> <i class='icon icon-undo icon-white'></i></a>&nbsp; <a class='btn btn-min btn-danger' href='#' id='delete-bank' index='"+rowindex+"' title='Delete' data-toggle='tooltip'> <i class='icon-trash icon-white'></i></a></td></tr>";
 		
	} catch(e) {
		console.log(e);
	}   
	return appendTxt; 
};
	
var modBankAccVals = function (clickType) {
	 // add custom behaviour
		var tabArrText = "";
		var appendTxt = ""; 
		 
		var data1 = ""; 
	try {  
		modHeaderBodyArry = new Array();
		
		data1 = commonData('#bankAccountInformation','MODIFY'); 
		
		modHeaderBodyArry[0]=data1.split("@@")[1];
		modHeaderBodyArry[1]=data1.split("@@")[0];
	} catch(e)  {
		console.log(e);
	}  
	//console.log(data1.split("@@")[1]+"##"+data1.split("@@")[0]);
	return ""; 
};

/** Form2 Add,Modify Ends*/

function alignSerialNo(serialId) {  
	if($(serialId).length > 0) {
		$(serialId +' > td:first-child').each(function(index){  
			$(this).text(index+1);
		}); 
	}
}

</script>
<script type="text/javascript"> 
function recordCheck(idtocheck,userIdInput){ 
	var check = false; 
	var userIdRecords = new Array();
	 
	try { 
		if($(idtocheck).length > 0) {
			// To Check The Record Exists In The Add-Row
			$(idtocheck+' > td:nth-child(2)').each(function(indx){ 
 				userIdRecords[indx]=$(this).text().trim(); 
			});
			
			if(jQuery.inArray(userIdInput.trim(), userIdRecords) == -1){ 
				check = true; 
			} 
			
		} else {
			check = true; 
		}
		
	} catch(e){
		console.log(e);
	}
	//console.log('Duplicate Record Check.. '+ check);
	return check;
}

function loadToolTip(){
	$('[rel="tooltip"],[data-toggle="tooltip"]').tooltip({"placement":"bottom",delay: { show: 400, hide: 200 }});
}

$(function(){

/** Form2 Add Row Validation Starts*/
	// Add Row Starts Here.
	$('#addCapBank').live('click', function(){
		$("#bankAcctMsg").text('');
 		$("#error_dlno").text('');	 
		$("#form2").validate(form2Rules); 
		 var textMess = "#tbody_data1 > tr";  
		  
		 //console.log("Inside bank Adding....");
	
		if($("#form2").valid()) {  
			// Is To Check Account Exist's or not 
			if(recordCheck(textMess,$('#accountNumber').val())) { 
				var appendTxt = addBankInfo("ADD");
				//console.log("appendTxt ===> "+ appendTxt);
				$("#tbody_data1").append(appendTxt);  
				clearVals();
				alignSerialNo(textMess); 
				$("#bankAcctMsg").text('');
				$('#error_dlno').text('');
				loadToolTip();
			} else { 
				$("#error_dlno").text('');
				$('#bankAcctMsg').text('Account Number Exists.');
			}  
			
		} else {
			// This is to throw error nessage.
			$('#error_dlno').text('');
			//$('#error_dlno').text('The User With "'+$('#userId option:selected').val()+'" Exists.');
			return false;
		} 
	 
	});   
	
	// Update Edited-Row Starts Here.
	$('#modCapBank').live('click', function () {
		$("#error_dlno").text('');	 
		$("#form2").validate(form2Rules); 
		if($("#form2").valid()) {

 			var spanValues = modBankAccVals("MODIFY");
			
 			searchTdRow = '#'+searchTrRows+"#tr-"+index1 +' > td';
 			$(searchTdRow).each(function(index,value) {
 				if(index == 1){
					$(this).text(modTabArry[0]);
				}if(index == 2){
					$(this).text(modTabArry[1]);
				}if(index == 3){
					$(this).text(modTabArry[2]);
				}if(index == 4){
					$(this).text(modTabArry[3]);
				} 
			});  
			 
			$("#multi-row-bank span").each(function(index,value) { 
 				 if($(this).attr("index") == index1 ) {  
					 $(this).attr("hid-names",modHeaderBodyArry[0]);
					 $(this).text(modHeaderBodyArry[1]); 
					 return;
				 }
			}); 
		 
			clearVals();   
			alignSerialNo("#tbody_data1 > tr");
			
			//Hide Update Button and Display Add Button
			$('#modCapBank').hide();
			$('#addCapBank').show();
			
		} else {
			return false;				
		} 
		
	}); 
	
	// The below event is to Edit row on selecting the delete button 
	$('#editDatBank').live('click',function() { 
		$("#form2").validate().cancelSubmit = true;
		
		index1 = $(this).attr('index');  
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		searchTrRows = parentId+" tr"; 
		searchTdRow = '#'+searchTrRows+"#tr-"+index1 +' > td';
		
		var idSearch = "";
		var hidnames = "";
		var text_val = ""; 
 	 
		var spanData = $("#multi-row-bank > span#hidden_span_"+index1);
		hidnames =  $("#multi-row-bank > span#hidden_span_"+index1).attr('hid-names');
		text_val =  $("#multi-row-bank > span#hidden_span_"+index1).text();  
		
		var hidarr=hidnames.split(",");
		var textarr=text_val.split(",");  
		 
		//console.log(hidarr);
		//console.log(textarr);
		//$('#acc-check').is(":checked");
		 
		if(textarr[3] != " ") {
			$("#acc-check").prop("checked", true); 
			$("#bankBranch").val("");
			$("#bankBranch").trigger("liszt:updated"); //bankBranch
		} else {
			$('#acc-check').prop("checked", false);
		}
		
		/* if( $(".checkclass").is(':checked') ) {
			// Non-IBL Account
			$('#bankinfospn').hide();
			$('#bankinfospn1').show();
		} else {
			//IBL Account
			$('#bankinfospn').show();
			$('#bankinfospn1').hide();
		} */
		
		$(hidarr).each(function(index,value) { 
			 if(jQuery.inArray( value, listid ) != -1){
				$('#'+value).find('option').each(function( i, opt ) { 
					if( opt.value == textarr[index] ) {
						$(opt).attr('selected', 'selected');
						$('#'+value).trigger("liszt:updated");
					}
				}); 
			} else {	
				$("#"+value).val(textarr[index] == undefined ? " " : textarr[index] .trim()); 
			}
		}); 
		
		//Hide Add Button and Display Update Button
		$('#modCapBank').show();
		$('#addCapBank').hide();
		
	});
	
	// The below event is to delete the entire row on selecting the delete button 
	$("#delete-bank").live('click',function() { 
		var delId = $(this).attr('index');
		$(this).parent().parent().remove();
 		
		var spanId = "";
		$('#multi-row-bank > span').each(function(index){  
			spanId =  $(this).attr('index'); 
			if(spanId == delId) {
				$(this).remove();
			}
		}); 
		
		clearVals();
		// Aligning the serial number
		alignSerialNo("#tbody_data1 > tr");
		
		//Show Add Button and Hide Update Button
		$('#modCapBank').hide();
		$('#addCapBank').show();
	}); 
	
	// Clear Form Records Row Starts Here.
	$('#row-cancel-bank').live('click', function () {
		$("#error_dlno").text(''); 
		 clearVals(); 
		 
		//Show Add Button and Hide Update Button
		$('#modCapBank').hide();
		$('#addCapBank').show();
	});  

});

/** Form2 Add Row Validation Ends*/
  
function createMerchant(){
	
	$("#form1").validate(merchantCreateRules);
	
	var finalData = ""; 
	var finalData1 = ""; 
	var finalData2 = ""; 
	$('#multi-row-bank > span').each(function(index) {  
			//console.log("index ["+$(this).attr('index')+"] name ["+$(this).attr('name')+"]  value["+$(this).val()+"] "); 
		if(index == 0)  finalData = $(this).text();
		else finalData += "#"+ $(this).text();
	}); 
	$('#bankAcctMultiData').val(finalData);
	
 	$('#multi-row-agent > span').each(function(index) {  
				//console.log("index ["+$(this).attr('index')+"] name ["+$(this).attr('name')+"]  value["+$(this).val()+"] "); 
		if(index == 0)  finalData1 = $(this).text();
		else finalData1 += "#"+ $(this).text();
	});	
	$('#agentMultiData').val(finalData1);
	
 	$('#multi-row-doc > span').each(function(index) {  
				//console.log("index ["+$(this).attr('index')+"] name ["+$(this).attr('name')+"]  value["+$(this).val()+"] "); 
		if(index == 0)  finalData2 = $(this).text();
		else finalData2 += "#"+ $(this).text();
	});
	
	$('#documentMultiData').val(finalData2);	 
 
	if($("#form1").valid()){ 
		 if($('#bankAcctMultiData').val()==""){
			$("#error_dlno").text("Bank account information required.");
			return false;
		} else {
			$("#bankAcctMsg").text('');
			var data=$( "#location option:selected" ).text();
			$("#locationVal").val(data);
			data=$( "#merchantType option:selected" ).text();
			$("#merchantTypeVal").val(data);
			$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/getMerchantCreateConfirmScreenAct.action';
			$("#form1").submit();
			return true;
		}
	} else {
		return false;
	}
}


function getLocation(){
	var data=$( "#location option:selected" ).text();
	$("#locationVal").val(data);
}
function getMerhchantType(){
	var data=$( "#merchantType option:selected" ).text();
	$("#merchantTypeVal").val(data);
}
	
	
function getMerchantID(){
 	var formInput='merchantName='+$('#merchantName').val();
    $.getJSON('generateMerchantIdAct.action', formInput,function(data) {
		var merchantId = data.responseJSON.merchantID;
		var storeId=data.responseJSON.storeId;
		$('#merchantID').val(merchantId);
		$('#storeId').val(storeId);
    }); 
   return false;
}
	 
</script>
 
<script type="text/javascript">
function clearValsAgent(){ 
	$('#bankAgenctNo').val('');
	$('#MPeasAgenctNo').val('');
	$('#airtelMoneyAgenetNo').val('');
	$('#orangeMoneyAgentNo').val(''); 	
	$('#mpesaTillNumber').val('');  
}
var index2 = "";
var selTextListAgent = ""; 
var headerListAgent = "bankAgenctNo,MPeasAgenctNo,airtelMoneyAgenetNo,orangeMoneyAgentNo,mpesaTillNumber".split(","); 
var listidagent = "".split(",");

function commonDataAgent(id,type){
	var hiddenInput ="";
	var hiddenNames = "";
	var tabarrindex = 0;
	tabArry = new Array();
	modTabArry = new Array();
	
	try {
		//console.log("commonDataAgent  :::" + id);
		$(id).find('input[type=text],input[type=hidden],select').each(function(indxf){ 
			var nameInput = "";
			var valToSpn =  "";
			try{
				  nameInput = $(this).attr('name') ;
				  valToSpn = ($(this).attr('value') =='' ? ' ' :$(this).attr('value')); 
			} catch(e1) {
				//console.log('The Exception Stack is :: '+ e1);
			}  
			
			if(nameInput != undefined) {
			  if(indxf == 0)  {
				hiddenInput += valToSpn;
				hiddenNames += nameInput;
			  } else {
				hiddenInput += ","+valToSpn; 
				hiddenNames += ","+nameInput; 
			  }
			   
			   if(jQuery.inArray(nameInput, headerListAgent) != -1){ 
				  if(selTextListAgent.indexOf(nameInput) != -1) {
					tabArry[tabarrindex] = ($('select#'+nameInput+' option:selected').text().trim() == "Select" ? " " : $('select#'+nameInput+' option:selected').text().trim()); 
					modTabArry[tabarrindex] = ($('select#'+nameInput+' option:selected').text().trim() == "Select" ? " " : $('select#'+nameInput+' option:selected').text().trim()); 
				  } else {
					tabArry[tabarrindex] = valToSpn; 
					modTabArry[tabarrindex] = valToSpn; 
				  } 
				  tabarrindex++;
			  }
			} 
			 
		}); 
	} catch (e) {
		console.log(e);
	}  
	return hiddenInput+"@@"+hiddenNames;
}
/** Form3 Add Row Validation Agent Starts */
var addAgentInfo = function (clickType) {
	 // add custom behaviour
	 var appendTxt = "";
	 var tabArrText = ""; 
	 var data1 = "";
	try { 
		//console.log("Before commonDataAgent  :::"); 
		data1 = commonDataAgent('#agentBasedInfo','ADD'); 
		rowindex = $("#multi-row-agent > span") .length ;
		 
		$("#multi-row-agent").append("<span id='hidden_span_"+rowindex+"' index="+rowindex+" ind-id='"+tabArry[0]+"' hid-names='"+data1.split("@@")[1]+"' ></span>");
		$('#multi-row-agent > span#hidden_span_'+rowindex).append(data1.split("@@")[0]);
	 
		var addclass = ""; 
		
		if(val % 2 == 0 ){
			addclass = "even";
			val++;
		} else {
			addclass = "odd";
			val++;
		}
		
		colindex = ++ colindex;
		
		$(tabArry).each(function(index){ 
			tabArrText+="<td>"+tabArry[index].trim()+"</td> ";
		});
		
		appendTxt = "<tr class="+addclass+" align='center' id='tr-"+rowindex+"' index='"+rowindex+"'>"+
				"<td class='col_"+colindex+"'>"+colindex+"</td>"+ tabArrText+ 
 				"<td><a class='btn btn-min btn-info' href='#' id='editDatAgent' index='"+rowindex+"' title='Edit' data-toggle='tooltip'> <i class='icon-edit icon-white'></i></a> &nbsp;<a class='btn btn-min btn-warning' href='#' id='row-cancel-agent' index='"+rowindex+"' title='Reset' data-toggle='tooltip'> <i class='icon icon-undo icon-white'></i></a>&nbsp; <a class='btn btn-min btn-danger' href='#' id='delete-agent' index='"+rowindex+"' title='Delete' data-toggle='tooltip'> <i class='icon-trash icon-white'></i></a></td></tr>";
		 
		
	} catch(e) {
		console.log(e);
	}  
 	
	return appendTxt; 
};
	
var modAgentAccVals = function (clickType) {
	 // add custom behaviour
	var tabArrText = "";
	var appendTxt = ""; 
	 
	var data1 = ""; 
	try {  
		modHeaderBodyArry = new Array();
		
		data1 = commonDataAgent('#agentBasedInfo','MODIFY'); 
		
		modHeaderBodyArry[0]=data1.split("@@")[1];
		modHeaderBodyArry[1]=data1.split("@@")[0];
	} catch(e)  {
		console.log(e);
	}  
	//console.log(data1.split("@@")[1]+"##"+data1.split("@@")[0]);
	return ""; 
};

function recordAgentCheck(idtocheck,userIdInput) {
	var check = false;
	var userIdRecords = new Array();
	try { 
		if($(idtocheck).length > 0) {
			// To Check The Record Exists In The Add-Row
			$(idtocheck+' > td:nth-child(2)').each(function(indx){ 
 				userIdRecords[indx]=$(this).text().trim(); 
			});
			
			if(jQuery.inArray(userIdInput.trim(), userIdRecords) == -1){ 
				check = true;
			}
			
		} else {
			check = true; 
		}		
	} catch(e) {
		console.log(e);
	}
	//console.log('Duplicate Record Check.. '+ check);
	return check;
}

/** Form3 Add,Modify Ends*/
$(function(){ 
	// Add Row Starts Here.
	$('#addCapAgent').live('click', function(){
		$("#bankAcctMsg").text('');
 		$("#error_dlno").text('');	 
		//$("#form3").validate(form3Rules); 
		 var textMess = "#tbody_data3 > tr";  
		  
		 //console.log("Inside bank agent....");
	
		//if($("#form3").valid()) { 
			// Is To Check Account Exist's or not 
			if(recordCheck(textMess,$('#bankAgenctNo').val())) { 
				var appendTxt = addAgentInfo("ADD");
				//console.log("appendTxt ===> "+ appendTxt);
				$("#tbody_data3").append(appendTxt);  
				clearValsAgent();
				alignSerialNo(textMess); 
				$("#agentAcctMsg").text('');
				$('#error_dlno').text('');
				loadToolTip();
				$('#bankAgenctNo').val((Math.random() * 100000).toString().substring(0,7).replace(".","")); 
			} else { 
				$("#error_dlno").text('');
				$('#agentAcctMsg').text('Account Number Exists.');
			}
		/*} else {
			// This is to throw error nessage.
			$('#error_dlno').text('');
			//$('#error_dlno').text('The User With "'+$('#userId option:selected').val()+'" Exists.');
			return false;
		} */
	 
	});   
	
	// Update Edited-Row Starts Here.
	$('#modCapAgent').live('click', function () {
		$("#error_dlno").text('');	 
		//$("#form3").validate(form3Rules); 
		//if($("#form3").valid()) {

 			var spanValues = modAgentAccVals("MODIFY");
			
 			searchTdRow = '#'+searchTrRows+"#tr-"+index2 +' > td';
 			$(searchTdRow).each(function(index,value) {
 				if(index == 1){
					$(this).text(modTabArry[0]);
				}if(index == 2){
					$(this).text(modTabArry[1]);
				}if(index == 3){
					$(this).text(modTabArry[2]);
				}if(index == 4){
					$(this).text(modTabArry[3]);
				}if(index == 5){
					$(this).text(modTabArry[3]);
				} 
			});  
			 
			$("#multi-row-agent span").each(function(index,value) { 
 				 if($(this).attr("index") == index2 ) {  
					 $(this).attr("hid-names",modHeaderBodyArry[0]);
					 $(this).text(modHeaderBodyArry[1]); 
					 return;
				 }
			}); 
		 
			clearValsAgent();   
			alignSerialNo("#tbody_data3 > tr");
			
			//Hide Update Button and Display Add Button
			$('#modCapAgent').hide();
			$('#addCapAgent').show(); 
			$('#bankAgenctNo').val((Math.random() * 100000).toString().substring(0,7).replace(".",""));
 		/*} else {
			return false;				
		} */ 
	}); 
	
	// The below event is to Edit row on selecting the delete button 
	$('#editDatAgent').live('click',function() { 
		$("#form3").validate().cancelSubmit = true;
		
		index2 = $(this).attr('index');  
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		searchTrRows = parentId+" tr"; 
		searchTdRow = '#'+searchTrRows+"#tr-"+index2 +' > td';
		
		var idSearch = "";
		var hidnames = "";
		var text_val = ""; 
 	 
		var spanData = $("#multi-row-agent > span#hidden_span_"+index2);
		hidnames =  $("#multi-row-agent > span#hidden_span_"+index2).attr('hid-names');
		text_val =  $("#multi-row-agent > span#hidden_span_"+index2).text(); 
		
		var hidarr=hidnames.split(",");
		var textarr=text_val.split(",");   
		
		//console.log(hidarr);
		//console.log(textarr); 
		
		$(hidarr).each(function(index,value) { 
			 if(jQuery.inArray( value, listidagent ) != -1){
				$('#'+value).find('option').each(function( i, opt ) { 
					if( opt.value == textarr[index] ) {
						$(opt).attr('selected', 'selected');
						$('#'+value).trigger("liszt:updated");
					}
				}); 
			} else {	
				$("#"+value).val(textarr[index] == undefined ? " " : textarr[index].trim()); 
			}
		}); 
		 
		//Hide Add Button and Display Update Button
		$('#modCapAgent').show();
		$('#addCapAgent').hide();
		
	});
	
	// The below event is to delete the entire row on selecting the delete button 
	$("#delete-agent").live('click',function() { 
		var delId = $(this).attr('index');
		$(this).parent().parent().remove();
 		
		var spanId = "";
		$('#multi-row-agent > span').each(function(index){  
			spanId =  $(this).attr('index'); 
			if(spanId == delId) {
				$(this).remove();
			}
		}); 
		
		clearValsAgent();
		
		//if($('#tbody_data3 > tr').length == 0) { 
			// Aligning the serial number
			alignSerialNo("#tbody_data3 > tr");	
			if($('#bankAgenctNo').val() == "") {
				$('#bankAgenctNo').val((Math.random() * 100000).toString().substring(0,7).replace(".",""));
			}
		//}  
		
		//Show Add Button and Hide Update Button
		$('#modCapAgent').hide();
		$('#addCapAgent').show();
	}); 
	
	// Clear Form Records Row Starts Here.
	$('#row-cancel-agent').live('click', function () {
		$("#error_dlno").text(''); 
		 clearValsAgent(); 
		 
		//Show Add Button and Hide Update Button
		$('#modCapAgent').hide();
		$('#addCapAgent').show();
	});  

});

/** Form3 Add Row Validation Agent Ends*/
 </script>

<script type="text/javascript">
function clearValsDocs(){ 
	$('#documentDescription').val('');
	$('#gracePeriod').val('');
 
	$(listiddoc).each(function(index,val){ 
		$('#'+val).find('option').each(function(i, opt ) { 
			if( opt.value == '' ) {
				$(opt).attr('selected', 'selected');
				$('#'+val).trigger("liszt:updated"); //bankBranch
			}
		}); 
	});

}
var index3 = "";
var selTextListDocs = "documentId,mandatory"; 
var headerListDocs = "documentId,documentDescription,gracePeriod,mandatory".split(","); 
var listiddoc = "documentId,mandatory".split(",");
function commonDataDocs(id,type){
	var hiddenInput ="";
	var hiddenNames = "";
	var tabarrindex = 0;
	tabArry = new Array();
	modTabArry = new Array();
	
	try {
		//console.log(id);
		$(id).find('input[type=text],input[type=hidden],select').each(function(indxf){ 
			var nameInput = "";
			var valToSpn =  "";
			try{
				  nameInput = $(this).attr('name') ;
				  valToSpn = ($(this).attr('value') =='' ? ' ' :$(this).attr('value')); 
			} catch(e1) {
				//console.log('The Exception Stack is :: '+ e1);
			} 
			//console.log("Type ["+$(this).attr('type')+"]  Name ["+$(this).attr('name')+"] Values["+$(this).val()+"]");
			
			if(nameInput != undefined) {
			  if(indxf == 0)  {
				hiddenInput += valToSpn;
				hiddenNames += nameInput;
			  } else {
				hiddenInput += ","+valToSpn; 
				hiddenNames += ","+nameInput; 
			  }
			  
			  //console.log(nameInput+"===" +headerListDocs);
 			  //console.log(jQuery.inArray(nameInput, headerListDocs));
			   if(jQuery.inArray(nameInput, headerListDocs) != -1){ 
				  if(selTextListDocs.indexOf(nameInput) != -1) {
					tabArry[tabarrindex] = ($('select#'+nameInput+' option:selected').text().trim() == "Select" ? " " : $('select#'+nameInput+' option:selected').text().trim()); 
					modTabArry[tabarrindex] = ($('select#'+nameInput+' option:selected').text().trim() == "Select" ? " " : $('select#'+nameInput+' option:selected').text().trim()); 
				  } else {
					tabArry[tabarrindex] = valToSpn; 
					modTabArry[tabarrindex] = valToSpn; 
				  } 
				  tabarrindex++;
			  }
			} 
			 
		}); 
	} catch (e) {
		console.log(e);
	}  
	return hiddenInput+"@@"+hiddenNames;
}
/** Form3 Add Row Validation Docs Starts */
var addDocsInfo = function (clickType) {
	 // add custom behaviour
	 var appendTxt = "";
	 var tabArrText = ""; 
	 var data1 = "";
	try { 
		data1 = commonDataDocs('#documentInformation','ADD'); 
	 
		rowindex = $("#multi-row-doc > span") .length ;
		
		$("#multi-row-doc").append("<span id='hidden_span_"+rowindex+"' index="+rowindex+" ind-id='"+tabArry[0]+"' hid-names='"+data1.split("@@")[1]+"' ></span>");
		$('#multi-row-doc > span#hidden_span_'+rowindex).append(data1.split("@@")[0]);
	 
		var addclass = ""; 
		
		if(val % 2 == 0 ){
			addclass = "even";
			val++;
		} else {
			addclass = "odd";
			val++;
		}
		
		colindex = ++ colindex;
		
		$(tabArry).each(function(index){ 
			tabArrText+="<td>"+tabArry[index].trim()+"</td> ";
		});
		
		appendTxt = "<tr class="+addclass+" align='center' id='tr-"+rowindex+"' index='"+rowindex+"'>"+
				"<td class='col_"+colindex+"'>"+colindex+"</td>"+ tabArrText+ 
 				"<td><a class='btn btn-min btn-info' href='#' id='editDatDocs' index='"+rowindex+"' title='Edit' data-toggle='tooltip'> <i class='icon-edit icon-white'></i></a> &nbsp;<a class='btn btn-min btn-warning' href='#' id='row-cancel-docs' index='"+rowindex+"' title='Reset' data-toggle='tooltip'> <i class='icon icon-undo icon-white'></i></a>&nbsp; <a class='btn btn-min btn-danger' href='#' id='delete-docs' index='"+rowindex+"' title='Delete' data-toggle='tooltip'> <i class='icon-trash icon-white'></i></a></td></tr>";
 		
	} catch(e) {
		console.log(e);
	}  
 	
	return appendTxt; 
};
	
var modDocsAccVals = function (clickType) {
	 // add custom behaviour
		var tabArrText = "";
		var appendTxt = ""; 
		 
		var data1 = ""; 
	try {  
		modHeaderBodyArry = new Array();
		
		data1 = commonDataDocs('#documentInformation','MODIFY'); 
		
		modHeaderBodyArry[0]=data1.split("@@")[1];
		modHeaderBodyArry[1]=data1.split("@@")[0];
	} catch(e)  {
		console.log(e);
	}  
	//console.log(data1.split("@@")[1]+"##"+data1.split("@@")[0]);
	return ""; 
}; 
/** Form3 Add,Modify Ends*/


/** Form4 Add,Modify Starts*/
$(function(){ 
	// Add Row Starts Here.
	$('#addCapDocs').live('click', function(){
		$("#bankAcctMsg").text('');
 		$("#error_dlno").text('');	 
		$("#form4").validate(form4Rules); 
		 var textMess = "#tbody_data2 > tr";
 	
		if($("#form4").valid()) { 
			// Is To Check Account Exist's or not 
			if(recordCheck(textMess,$('#documentId option:selected').text())) { 
				var appendTxt = addDocsInfo("ADD");
				//console.log("appendTxt ===> "+ appendTxt);
				$("#tbody_data2").append(appendTxt);  
				clearValsDocs();
				alignSerialNo(textMess); 
				$("#docsAcctMsg").text('');
				$('#error_dlno').text('');
				loadToolTip();
			} else { 
				$("#error_dlno").text('');
				$('#docsAcctMsg').text('Document Id Exists.');
			}
		} else {
			// This is to throw error nessage.
			$('#error_dlno').text('');
 			return false;
		} 
	 
	});   
	
	// Update Edited-Row Starts Here.
	$('#modCapDocs').live('click', function () {
		$("#error_dlno").text('');	 
		$("#form4").validate(form4Rules); 
		if($("#form4").valid()) {

 			var spanValues = modDocsAccVals("MODIFY");
			
 			searchTdRow = '#'+searchTrRows+"#tr-"+index3 +' > td';
 			$(searchTdRow).each(function(index,value) {
 				if(index == 1){
					$(this).text(modTabArry[0]);
				}if(index == 2){
					$(this).text(modTabArry[1]);
				}if(index == 3){
					$(this).text(modTabArry[2]);
				}if(index == 4){
					$(this).text(modTabArry[3]);
				} 
			});  
			 
			$("#multi-row-doc span").each(function(index,value) { 
 				 if($(this).attr("index") == index3 ) {  
					 $(this).attr("hid-names",modHeaderBodyArry[0]);
					 $(this).text(modHeaderBodyArry[1]); 
					 return;
				 }
			}); 
		 
			clearValsDocs();   
			alignSerialNo("#tbody_data2 > tr");
			
			//Hide Update Button and Display Add Button
			$('#modCapDocs').hide();
			$('#addCapDocs').show();
			
		} else {
			return false;				
		} 
		
	}); 
	
	// The below event is to Edit row on selecting the delete button 
	$('#editDatDocs').live('click',function() { 
		$("#form1").validate().cancelSubmit = true;
		$("#form2").validate().cancelSubmit = true;
		$("#form3").validate().cancelSubmit = true;
		
		index3 = $(this).attr('index');  
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		searchTrRows = parentId+" tr"; 
		searchTdRow = '#'+searchTrRows+"#tr-"+index3 +' > td';
		
		var idSearch = "";
		var hidnames = "";
		var text_val = "";  
		
		var spanData = $("#multi-row-doc > span#hidden_span_"+index3);
		hidnames =  $("#multi-row-doc > span#hidden_span_"+index3).attr('hid-names');
		text_val =  $("#multi-row-doc > span#hidden_span_"+index3).text(); 
		
		var hidarr=hidnames.split(",");
		var textarr=text_val.split(","); 
		
		$(hidarr).each(function(index,value) { 
			 if(jQuery.inArray( value, listiddoc ) != -1){
				$('#'+value).find('option').each(function( i, opt ) { 
					if( opt.value == textarr[index] ) {
						$(opt).attr('selected', 'selected');
						$('#'+value).trigger("liszt:updated");
					}
				}); 
			} else {	
				//$("#"+value).val(textarr[index].trim()); 
				$("#"+value).val(textarr[index] == undefined ? " " : textarr[index] .trim()); 
			}
		}); 
		 
		//Hide Add Button and Display Update Button
		$('#modCapDocs').show();
		$('#addCapDocs').hide();
		
	});
	
	// The below event is to delete the entire row on selecting the delete button 
	$("#delete-docs").live('click',function() { 
		var delId = $(this).attr('index');
		$(this).parent().parent().remove();
 		
		var spanId = "";
		$('#multi-row-doc > span').each(function(index){  
			spanId =  $(this).attr('index'); 
			if(spanId == delId) {
				$(this).remove();
			}
		}); 
		
		clearValsDocs();
		// Aligning the serial number
		alignSerialNo("#tbody_data2 > tr");
		
		//Show Add Button and Hide Update Button
		$('#modCapDocs').hide();
		$('#addCapDocs').show();
	}); 
	
	// Clear Form Records Row Starts Here.
	$('#row-cancel-docs').live('click', function () {
		$("#error_dlno").text(''); 
		 clearValsDocs(); 
		 
		//Show Add Button and Hide Update Button
		$('#modCapDocs').hide();
		$('#addCapDocs').show();
	});   
}); 
/** Form4 Add Row Validation Docs Ends*/
 </script>
 
<script type="text/javascript">
$(function(){ 
	 
	$("#telephoneNumber1,#telephoneNumber2,#mobileNumber,#faxNumber,#prmContactNumber,#postalCode").keypress(function (e) {
	 //if the letter is not digit then display error and don't type anything
	 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
		//display error message
		$("#"+$(this).attr('id')+"_err").html("Digits Only").show().fadeOut("slow");
			   return false;
		}
   }); 
   
	$('#kraPin').live('keyup',function(){
		 var id = $(this).attr('id');
		 var v_val = $(this).val(); 
		 $("#"+id).val(v_val.toUpperCase());
	});
	
	/* $('.checkclass').click(function(){ 
		//console.log("IS checked.... " + $(this).is(':checked'));
		if( $(this).is(':checked') ) {
			// Non-IBL Account
			$('#bankinfospn').hide();
			$('#bankinfospn1').show(); 
			$("#bankBranch").val("");
			$("#bankBranch").trigger("liszt:updated"); //bankBranch
		} else {
			//IBL Account
			$('#bankinfospn').show();
			$('#bankinfospn1').hide();
			$('#bankName').val('');
		}
	});  $('#bankinfospn1').hide();*/ 
	 
});	


</script> 
</head>

<body> 
		<div id="content" class="span10"> 
			    <div>
					<ul class="breadcrumb">
					  <li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li><a href="generateMerchantAct.action?pid=9">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Add New Merchant</a></li>
					</ul>
				</div>
		<form name="form1" id="form1" method="post" action="">  
				<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
					<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Merchant Primary Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						</div>
					</div>  
						
					<div class="box-content" id="primaryDetails"> 
						<fieldset> 
							<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable ">
								<tr class="even">
									<td width="20%"><label for="Merchant Name"><strong>Merchant Name<font color="red">*</font></strong></label></td>
									<td width="30%"><input name="merchantName" type="text"  id="merchantName" class="field"  
														value="<s:property value='#respData.merchantName' />" 
														maxlength="50" required='true' onChange="getMerchantID()" ></td>
									<td width="20%"><label for="Merchant ID"><strong>Merchant ID<font color="red">*</font></strong></label></td>
									<td width="30%"><input name="merchantID" type="text" id="merchantID" class="field" value="<s:property value='#respData.merchantID' />" maxlength="15" readonly ></td>
								</tr>
								<tr class="odd">
									<td><label for="Store Name"><strong>Store Name<font color="red">*</font></strong></label></td>
									<td><input name="storeName" type="text" class="field" id="storeName" value="<s:property value='#respData.storeName' />" required='true' /></td>
									<td ><label for="Store ID"><strong>Store ID<font color="red">*</font></strong></label></td>
									<td ><input name="storeId"  type="text" id="storeId" class="field"  value="<s:property value='#respData.storeId' />" readonly> </td>
											
								</tr>
								<tr class="even">
									<td ><label for="Location"><strong>Location<font color="red">*</font></strong></label></td>
									<td > 
										 <s:select cssClass="chosen-select" 
											headerKey="" 
											headerValue="Select"
											list="#respData.LOCATION_LIST" 
											name="location" 
											value="#respData.location" 
											id="location" requiredLabel="true" 
											theme="simple"
											data-placeholder="Choose Location..." 
											onchange="getLocation()"
											 />  
										<input type="hidden" name="locationVal" id="locationVal" value="<s:property value='#respData.location' />" />
									</td>
									<td ><label for="KRA PIN"><strong>KRA PIN<font color="red">*</font></strong></label></td>
									<td ><input name="kraPin" type="text" class="field" id="kraPin" value="<s:property value='#respData.kraPin' />" required='true' maxlength="11"/></td>	
								</tr>  
								<tr class="odd">
									<td ><label for="Merchant Type"><strong>Merchant Type<font color="red">*</font></strong></label></td>
									<td >
										<!-- <select id="merchantType" name="merchantType" data-placeholder="Choose Merchant Type..." 
											class="chosen-select" style="width: 220px;" required='true' onChange="getMerhchantType()">
											<option value="">Select</option>
										</select> -->
										 <s:select cssClass="chosen-select" 
											headerKey="" 
											headerValue="Select"
											list="#respData.MERCHANT_TYPE" 
											name="merchantType" 
											value="#respData.merchantType" 
											id="merchantType" requiredLabel="true" 
											theme="simple"
											data-placeholder="Choose Merchant Type..." 
											onchange="getMerhchantType()"
											 />  
										<input type="hidden" name="merchantTypeVal" id="merchantTypeVal" value="<s:property value="#respData.merchantType"/>" />
									</td>
									<td ><label for="Member Type"><strong>Member Type<font color="red">*</font></strong></label></td>
									<td >
										 <select id="memberType" name="memberType"  required='true' 
											data-placeholder="Choose member type..." 
											class="chosen-select" style="width: 220px;" >
											<option value="">Select</option>
											<option value="Merchant">Merchant</option>
											<option value="Agency">Agency</option>
										</select>  
									</td>	
								</tr>
							</table>
						</fieldset> 
						</div>  
					</div> 
				</div> 
				 
				<div class="row-fluid sortable"><!--/span--> 
						<div class="box span12">
								<div class="box-header well" data-original-title>
										<i class="icon-edit"></i>Communication Details
									<div class="box-icon">
										<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
										<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
									</div>
								</div>  
								 
								<div id="communicationDetails" class="box-content">
									<fieldset> 
										<table width="950" border="0" cellpadding="5" cellspacing="1" 
												class="table table-striped table-bordered bootstrap-datatable ">
											<tr class="even">
												<td width="20%"><label for="Manager Name"><strong>Manager Name<font color="red">*</font></strong></label></td>
												<td width="30%"><input name="managerName" type="text" id="managerName" class="field" value="<s:property value="#respData.managerName"/>" maxlength="50" required='true' ></td>
												<td width="20%"><label for="Email"><strong>Email<font color="red">*</font></strong></label></td>
												<td width="30%"><input name="email" type="text"  id="email" class="field"  value="<s:property value="#respData.email"/>" required='true' > </td>
											</tr>
											<tr class="odd">
												<td ><label for="Address Line 1"><strong>Address Line 1<font color="red">*</font></strong></label></td>
												<td ><input name="addressLine1" id="addressLine1" class="field" type="text"  maxlength="50" value="<s:property value="#respData.addressLine1"/>" required='true' ></td>
												<td ><label for="Address Line 2 "><strong>Address Line 2<font color="red">*</font> </strong></label></td>
												<td ><input name="addressLine2" type="text" class="field" id="addressLine2"   value="<s:property value="#respData.addressLine2"/>" required='true' /></td>
											</tr>
											<tr class="even">
												<td ><label for="Address Line 3 "><strong>Address Line 3</strong></label></td>
												<td ><input name="addressLine3" type="text" class="field" id="addressLine3" value="<s:property value="#respData.addressLine3"/>" /></td>
												<td><label for="City"><strong>City<font color="red">*</font></strong></label></td>
												<td ><input name="city" type="text" class="field" id="city"  value="<s:property value="#respData.city"/>" required='true' /></td>
											</tr>
											<%-- <tr class="odd">
												<td ><label for="Address Line 3 "><strong>Area<font color="red">*</font></strong></label></td>
												<td ><input name="area" type="text" class="field" id="area" value="<s:property value="#respData.area"/>" required='true' /></td> 
												<td ><label for="Postal Code"><strong>Postal Code<font color="red">*</font></strong></label></td>
												<td ><input name="postalCode" type="text" id="postalCode" class="field" value="<s:property value="#respData.postalCode"/>"  required='true'/> <span id="postalCode_err" class="errmsg"></span></td>
												
										   </tr> --%>
											<tr class="even">
												<td ><label for="P.O. Box Number "><strong>P.O. Box Number<font color="red">*</font></strong></label></td>
												<td ><input name="poBoxNumber" type="text" id="poBoxNumber" class="field" value="<s:property value="#respData.poBoxNumber"/>" required='true' /></td>
												<td ><label for="Telephone Number 1"><strong>Telephone Number 1</strong></label></td>
												<td><input name="telephoneNumber1" type="text" id="telephoneNumber1" class="field" value="<s:property value="#respData.telephoneNumber1"/>" /><span id="telephoneNumber1_err" class="errmsg"></span></td>											 
												
										   </tr>
										   <tr class="odd">
												<td ><label for="Telephone Number 2 "><strong>Telephone Number 2</strong></label>	</td>
												<td ><input name="telephoneNumber2" type="text" class="field" id="telephoneNumber2"  value="<s:property value="#respData.telephoneNumber2"/>" /><span id="telephoneNumber2_err" class="errmsg"></span></td>												
												<td ><label for="Mobile Number"><strong>Mobile Number<font color="red">*</font></strong></label></td>
												<td ><input name="mobileNumber" id="mobileNumber" class="field" type="text"  value="<s:property value="#respData.mobileNumber"/>" required='true' /><span id="mobileNumber_err" class="errmsg"></span></td>
												
										   </tr>
										<tr class="even">
											<td ><label for="Fax Number"><strong>Fax Number </strong></label></td>
											<td ><input name="faxNumber"  type="text" class="field" id="faxNumber" value="<s:property value="#respData.faxNumber"/>" /><span id="faxNumber_err" class="errmsg"></span></td>
											<td >&nbsp;</td >
											<td >&nbsp;</td >											 
											<%-- <td><label for="L/R Number"><strong>L/R Number<font color="red">*</font></strong></label></td>
											<td ><input name="lrNumber" type="text" class="field" id="lrNumber" value="<s:property value="#respData.lrNumber"/>" required='true' /></td> --%>
										</tr>
										<tr class="odd">  
											<td ><label for="Primary Contact Person"><strong>Primary Contact Person<font color="red">*</font></strong></label></td>
											<td ><input name="prmContactPerson" id="prmContactPerson" class="field" type="text" value="<s:property value="#respData.prmContactPerson"/>" required='true' ></td>
											<td><label for="Primary Contact Number"><strong>Primary Contact Number<font color="red">*</font></strong></label></td>
											<td ><input name="prmContactNumber" type="text" class="field" id="prmContactNumber" value="<s:property value="#respData.prmContactNumber"/>" required='true' /><span id="prmContactNumber_err" class="errmsg"></span></td>
										</tr>
										<%-- <tr class="even">  
											<td ><label for="Country"><strong>Country<font color="red">*</font></strong></label></td>
											<td >
											 <select id="country" name="country"   required='true' data-placeholder="Choose country..." 
												class="chosen-select" style="width: 220px;">
												<option value="">Select</option>
												<option value="Kenya">Kenya</option>
												<option value="Uganda">Uganda</option>
											</select> 
											</td>
											<td >&nbsp;</td >
											<td >&nbsp;</td >
										</tr> --%>
									</table>
								</fieldset> 
							</div> 
						</div> 
				</div>
	<input type="hidden" name="bankAcctMultiData" id="bankAcctMultiData" value="<s:property value="#respData.bankAcctMultiData"/>" />
	<input type="hidden" name="agentMultiData" id="agentMultiData" value="<s:property value="#respData.agentMultiData"/>" />
	<input type="hidden" name="documentMultiData" id="documentMultiData" value="<s:property value="#respData.documentMultiData"/>" />
	</form>
	<form name="form2" id="form2" method="post" action="">  
				<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12">
						<div class="box-header well" data-original-title>
								<i class="icon-edit"></i>Bank Account Information &nbsp;&nbsp; 
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							</div>
						</div>  
						<div id="bankAccountInformation" class="box-content" >
							<fieldset> 
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
										class="table table-striped table-bordered bootstrap-datatable "> 
									 <tr class="odd">
										<td width="20%"><label for="Account Number"><strong>Account Number<font color="red">*</font></strong></label></td>
										<td width="30%"><input name="accountNumber" id="accountNumber" class="field" type="text" maxlength="50" ></td>
										<td width="20%"><label for="Account Description"><strong>Account Description<font color="red">*</font></strong></label></td>
										<td width="30%"> <!-- <input name="acctDescription" id="acctDescription" class="field"  type="text" > -->
											<select id="acctDescription" name="acctDescription" required='true' 
												data-placeholder="Choose Account..." class="chosen-select" style="width: 220px;">
												<option value="">Select</option>
												<option value="savings">Savings</option>
												<option value="current">Current</option>
											</select>
										</td>	
									 </tr>
									  <tr class="even" id="bankinfospn"> 
										<td><label for="Bank Name"><strong>Bank Name<font color="red">*</font></strong></label></td>
										<td> 
											<input name="bankName" id="bankName" class="field" type="text" maxlength="50" value=" " />  
										</td>
										<td><label for="Bank Branch"><strong>Bank Branch<font color="red">*</font></strong></label></td>
										<td>  
											<input name="bankBranch" id="bankBranch" class="field" type="text"  value=" " />  
										</td>	
									 </tr> 
									 <tr class="odd" id="bankinfospn1">
										
										<td><label for="Transfer Code(Swift Code)"><strong>Transfer Code(Swift Code)<font color="red">*</font></strong></label></td>
										<td> 
											<input name="transferCode" id="transferCode" class="field" type="text" maxlength="50" value=" " />  
										</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>	
									 </tr> 
									<tr class="odd"> 
										<td colspan="4" align="center"><input type="button" class="btn btn-success" 
											name="addCapBank" id="addCapBank" value="Add Bank Information" /> &nbsp; 
											 <input type="button" class="btn btn-success" 
											name="modCapBank" id="modCapBank" value="Update  Bank Information" style="display:none"/> &nbsp;
											<span id="bankAcctMsg" class="errors"></span></td> 
									</tr>
									 
								</table> 
							</fieldset>  
							<span id="multi-row-bank" name="multi-row-bank" style="display:none"> </span>
						<table width="100%"
							class="table table-striped table-bordered bootstrap-datatable "
							id="bankAcctTable" style="width: 100%;">
							<thead>
								<tr>
									<th>Sno</th>
									<th>Account Number</th>
									<th>Account Description</th>
									<th>Bank Name</th>
									<th>Bank Branch</th>
									<th>Transfer Code(Swift Code)</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody id="tbody_data1">
							
							<s:bean name="com.ceva.base.common.bean.JsonDataToObject" var="jsonToList">
								<s:param name="jsonData" value="%{#respData.bankAcctMultiData}"/>  
								<s:param name="searchData" value="%{'#'}"/>  
							</s:bean> 
							<s:iterator value="#jsonToList.data" var="mulData"  status="mulDataStatus" >   
								 
										<script type="text/javascript">
											var hiddenNames1 = "";
											$(function(){
												$('#bankAccountInformation').find('input[type=text],input[type=hidden],select').each(function(index){ 
													var nameInput = $(this).attr('name'); 
													if(nameInput != undefined) {
													  if(index == 0)  {
														hiddenNames1 = nameInput;
													  } else {
														hiddenNames1 += ","+nameInput; 
													  }  
													} 
												}); 
												var data1 = "<s:property />";
												data1 = data1.split(",");
												
												$("#multi-row-bank").append("<span id='hidden_span_<s:property value="#mulDataStatus.index" />' index='<s:property value="#mulDataStatus.index" />' ind-id='"+data1[0]+"' hid-names='"+hiddenNames1+"' ><s:property value="#mulData" /></span>");
											});
											</script> 
									 	
									 	<s:if test="#mulDataStatus.even == true" > 
											<tr class="even" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
										</s:if>
										<s:elseif test="#mulDataStatus.odd == true">
											<tr class="odd" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
		   								</s:elseif> 
								
									<td><s:property value="#mulDataStatus.index+1" /></td>
										<s:generator val="%{#mulData}"
											var="bankDat" separator="," >  
											<s:iterator status="itrStatus"> 
												<s:if test="#itrStatus.index != 5" > 
													<td><s:property /></td>
												</s:if>
											</s:iterator>  
										</s:generator>
									<td><a class='btn btn-min btn-info' href='#' id='editDatBank' index='<s:property value="#mulDataStatus.index" />' title='Edit' data-toggle='tooltip'> <i class='icon-edit icon-white'></i></a> &nbsp;
										<a class='btn btn-min btn-warning' href='#' id='row-cancel-bank' index='<s:property value="#mulDataStatus.index" />' title='Reset' data-toggle='tooltip'> <i class='icon icon-undo icon-white'></i></a>&nbsp; 
										<a class='btn btn-min btn-danger' href='#' id='delete-bank' index='<s:property value="#mulDataStatus.index" />' title='Delete' data-toggle='tooltip'> <i class='icon-trash icon-white'></i></a>
									</td>
								</tr>
							</s:iterator> 
							</tbody>
						</table>
					</div> 
					</div> 
				</div>
				
	</form>
	<form name="form3" id="form3" method="post" action="">  
				<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12">
						<div class="box-header well" data-original-title>
								<i class="icon-edit"></i>Agent Based Information
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							</div>
						</div>  
						<div id="agentBasedInfo" class="box-content">
							<fieldset> 
								 <table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
										<tr class="even">
											<td width="20%"><label for="Document ID"><strong>Bank Agent Number</strong></label></td> 					
											<td width="30%"><input name="bankAgenctNo" id="bankAgenctNo" class="field" type="text" value="<s:property value="#responseJSON.AGEN_BILL_RAND" />" disabled /></td>
											<td width="20%"><label for="Document Description"><strong>MPesa Agent Number</strong></label></td>
											<td width="30%"><input name="MPeasAgenctNo" id="MPeasAgenctNo" class="field" type="text" /> </td>
										</tr>
										<tr  class="odd">
											<td><label for="Grace Period"><strong>Airtel Money Agent Number </strong></label></td>
											<td><input name="airtelMoneyAgenetNo" id="airtelMoneyAgenetNo" class="field"  type="text" /></td>
											<td><label for="mandatory"><strong>Orange Money Agent Number</strong></label></td>
											<td >
												<input name="orangeMoneyAgentNo" id="orangeMoneyAgentNo" class="field"  type="text" />
											</td>
										</tr>
										<tr class="even">
											<td><label for="Document ID"><strong>Visa Mid</strong></label></td> 					
											<td ><input name="mpesaTillNumber" id="mpesaTillNumber" class="field" type="text" /></td>
											<td ></td>
											<td ></td>
										</tr> 
										<tr class="odd"> 
											<td colspan="4" align="center"><input type="button" class="btn btn-success" 
												name="addCapAgent" id="addCapAgent" value="Add Agent Details" /> &nbsp; 
												 <input type="button" class="btn btn-success" 
												name="modCapAgent" id="modCapAgent" value="Update Agent Details" style="display:none"/> &nbsp;
												<span id="agentAcctMsg" class="errors"></span></td> 
										</tr>
								</table> 
							</fieldset> 
							<span id="multi-row-agent" name="multi-row-agent" style="display:none"> </span>	
							<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
									id="agentTable" style="width: 100%;">
								  <thead>
										<tr>
											<th>Sno</th>
											<th>Bank Agent Number</th>
											<th>MPesa Agent Number</th>
											<th>Airtel Money Agent Number</th>
											<th>Orange Money Agent Number</th>
											<th>Visa Mid</th>
											<th>Action</th>
										</tr>
								  </thead>    
								 <tbody id="tbody_data3">
									<s:bean name="com.ceva.base.common.bean.JsonDataToObject" var="jsonToList">
										<s:param name="jsonData" value="%{#respData.agentMultiData}"/>  
										<s:param name="searchData" value="%{'#'}"/>  
									</s:bean> 
									<s:iterator value="#jsonToList.data" var="mulData"  status="mulDataStatus" >  
										 
												<script type="text/javascript">
													var hiddenNames1 = "";
													$(function(){
														$('#agentBasedInfo').find('input[type=text],input[type=hidden],select').each(function(index){ 
															var nameInput = $(this).attr('name'); 
															if(nameInput != undefined) {
															  if(index == 0)  {
																hiddenNames1 = nameInput;
															  } else {
																hiddenNames1 += ","+nameInput; 
															  }  
															} 
														}); 
														var data1 = "<s:property />";
														data1 = data1.split(",");
												
														$("#multi-row-agent").append("<span id='hidden_span_<s:property value="#mulDataStatus.index" />' index='<s:property value="#mulDataStatus.index" />' ind-id='"+data1[0]+"' hid-names='"+hiddenNames1+"' ><s:property value="#mulData" /></span>");
													});
													</script> 
												<s:if test="#mulDataStatus.even == true" > 
													<tr class="even" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
												</s:if>
												<s:elseif test="#mulDataStatus.odd == true">
													<tr class="odd" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
												</s:elseif> 
										
											<td><s:property value="#mulDataStatus.index+1" /></td>
												<s:generator val="%{#mulData}"
													var="bankDat" separator="," >  
													<s:iterator status="itrStatus">  
														<td><s:property /></td> 
													</s:iterator>  
												</s:generator>
											<td><a class='btn btn-min btn-info' href='#' id='editDatAgent' index='<s:property value="#mulDataStatus.index" />' title='Edit' data-toggle='tooltip'> <i class='icon-edit icon-white'></i></a> &nbsp;
												<a class='btn btn-min btn-warning' href='#' id='row-cancel-agent' index='<s:property value="#mulDataStatus.index" />' title='Reset' data-toggle='tooltip'> <i class='icon icon-undo icon-white'></i></a>&nbsp; 
												<a class='btn btn-min btn-danger' href='#' id='delete-agent' index='<s:property value="#mulDataStatus.index" />' title='Delete' data-toggle='tooltip'> <i class='icon-trash icon-white'></i></a>
											</td>
										</tr>
									</s:iterator>
								</tbody>
							</table> 
						</div> 
					</div> 
				</div>
	</form>
	<form name="form4" id="form4" method="post" action="">  
				<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12">
						<div class="box-header well" data-original-title>
								<i class="icon-edit"></i>Document Information
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							</div>
						</div>  
						<div id="documentInformation" class="box-content">
							<fieldset> 
								<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
									<tr class="even">
										<td width="20%"><label for="Document ID"><strong>Document ID</strong></label></td> 					
										<td width="30%">
 											<select name="documentId" id="documentId" required='true' 
												data-placeholder="Choose DocumentId..." 
												class="chosen-select" style="width: 220px;" >
												<option value="">Select</option>
												<option value="BUSS">Business Permit</option>
												<option value="DID_PASS">Director Id/Passport</option>
												<option value="CER_INC">Certificate Of Incorporation</option>
												<option value="BUSS">Business Permit</option>
											</select>
											</td>
										<td width="20%"><label for="Document Description"><strong>Document Description</strong></label></td>
										<td width="30%"><input name="documentDescription" id="documentDescription" class="field" type="text"> </td>
									</tr>
									<tr  class="odd">
										<td><label for="Grace Period"><strong>Grace Period(in Days) </strong></label></td>
										<td><input name="gracePeriod" id="gracePeriod" class="field"  maxlength="50" type="text" ></td>
										<td><label for="mandatory"><strong>Mandatory</strong></label></td>
										<td>
											<select id="mandatory" name="mandatory"  required='true' data-placeholder="Choose member type..." class="chosen-select" style="width: 220px;" >
												<option value="">Select</option>
												<option value="Yes">Yes</option>
												<option value="No">No</option>
											</select>
										</td>
									</tr> 
									<tr class="odd"> 
										<td colspan="4" align="center"><input type="button" class="btn btn-success" 
											name="addCapDocs" id="addCapDocs" value="Add Document Information" /> &nbsp; 
											 <input type="button" class="btn btn-success" 
											name="modCapDocs" id="modCapDocs" value="Update Document Information" style="display:none"/> &nbsp;
											<span id="docsAcctMsg" class="errors"></span></td> 
									</tr>
							</table>
						</fieldset>  
						<span id="multi-row-doc" name="multi-row-doc" style="display:none"> </span> 
						<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
							id="documentTable" style="width: 100%;">
								  <thead>
										<tr>
											<th>Sno</th>
											<th>Document ID</th>
											<th>Document Description</th>
											<th>Grace Period</th>
											<th>Mandatory</th>
											<th>Action</th>
										</tr>
								  </thead>    
								 <tbody id="tbody_data2">
								 	<s:bean name="com.ceva.base.common.bean.JsonDataToObject" var="jsonToList">
										<s:param name="jsonData" value="%{#respData.documentMultiData}"/>  
										<s:param name="searchData" value="%{'#'}"/>  
									</s:bean> 
									<s:iterator value="#jsonToList.data" var="mulData"  status="mulDataStatus" >  
										 
												<script type="text/javascript">
													var hiddenNames1 = "";
													$(function(){
														$('#documentInformation').find('input[type=text],input[type=hidden],select').each(function(index){ 
															var nameInput = $(this).attr('name'); 
															if(nameInput != undefined) {
															  if(index == 0)  {
																hiddenNames1 = nameInput;
															  } else {
																hiddenNames1 += ","+nameInput; 
															  }  
															} 
														}); 
														var data1 = "<s:property />";
														data1 = data1.split(",");
														$("#multi-row-doc").append("<span id='hidden_span_<s:property value="#mulDataStatus.index" />' index='<s:property value="#mulDataStatus.index" />' ind-id='"+data1[0]+"' hid-names='"+hiddenNames1+"' ><s:property value="#mulData" /></span>");
													});
													</script> 
												
												<s:if test="#mulDataStatus.even == true" > 
													<tr class="even" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
												</s:if>
												<s:elseif test="#mulDataStatus.odd == true">
													<tr class="odd" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
												</s:elseif> 
										
											<td><s:property value="#mulDataStatus.index+1" /></td>
												<s:generator val="%{#mulData}"
													var="bankDat" separator="," >  
													<s:iterator status="itrStatus">  
															<td><s:property /></td> 
													</s:iterator>  
												</s:generator>
											<td><a class='btn btn-min btn-info' href='#' id='editDatDocs' index='<s:property value="#mulDataStatus.index" />' title='Edit' data-toggle='tooltip'> <i class='icon-edit icon-white'></i></a> &nbsp;
												<a class='btn btn-min btn-warning' href='#' id='row-cancel-docs' index='<s:property value="#mulDataStatus.index" />' title='Reset' data-toggle='tooltip'> <i class='icon icon-undo icon-white'></i></a>&nbsp; 
												<a class='btn btn-min btn-danger' href='#' id='delete-docs' index='<s:property value="#mulDataStatus.index" />' title='Delete' data-toggle='tooltip'> <i class='icon-trash icon-white'></i></a>
											</td>
										</tr>
									</s:iterator>
								 </tbody>
							</table>
									
						</div>
					</div>
				</div>  
	</form> 
		<div class="form-actions">
			<!-- <input type="button" class="btn btn-danger" name="form-back" id="form-back" value="Back" width="100" onClick="getGenerateMerchantScreen()" ></input> 
			<input type="button" class="btn btn-success" name="form-submit" id="form-submit" value="Submit" width="100"  onClick="createMerchant()"></input>  -->
			<a  class="btn btn-danger" href="#" onClick="getGenerateMerchantScreen()">Back</a> &nbsp;&nbsp;
			<a  class="btn btn-success" href="#" onClick="createMerchant()">Submit</a>
			<span id ="error_dlno" class="errors"></span>
		</div>  
	</div> 
<script type="text/javascript">
$(function(){
	
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
	
	$('#merchantType').val('<s:property value="#respData.merchantType" />');
	$('#merchantType').trigger("liszt:updated"); 
	
	$('#memberType').val('<s:property value="#respData.memberType" />');
	$('#memberType').trigger("liszt:updated");  
	
	//$('#country').val('<s:property value="#respData.country" />');
	//$('#country').trigger("liszt:updated"); 
	
});
</script>
</body>
</html>