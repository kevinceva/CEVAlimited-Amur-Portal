
<!DOCTYPE html>
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
  
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
input#userId{text-transform:uppercase};
</style>
<s:set value="responseJSON" var="respData"/>
<!-- add row validation --> 
<script type="text/javascript">  
function randomString() {
   var charSet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    var randomString = '';
    for (var i = 0; i < 8; i++) {
    	var randomPoz = Math.floor(Math.random() * charSet.length);
    	randomString += charSet.substring(randomPoz,randomPoz+1);
    }
    return randomString;
}

function encryptPassword(){
	var encryptPass;
	var password=randomString();
 	$("#password").val(password);
	encryptPass = b64_sha256(password);
	$("#encryptPassword").val(encryptPass);
}


var finalData="";
var listDate = "expiryDate".split(",");
var datepickerOptions = {
			showTime: false,
			showHour: false,
			showMinute: false,
  		dateFormat:'dd/mm/yy',
  		alwaysSetTime: false,
		changeMonth: true,
		changeYear: true
};


$(function() {
	$(listDate).each(function(i,val){
 		$('#'+val).datepicker(datepickerOptions);
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
	//$('#enableUserId').removeAttr('style'); 
});	
</script>

<script type="text/javascript">

var val = 1; 
var rowindex = 0;
var colindex = 0;
var userstatus = "";
var v_message = "";
var listid = "adminType,officeLocation1".split(",");
var headerList = "userId,employeeNo,firstName,lastName,officeLocation1,email".split(",");
var tabArry ; 
var modTabArry ;  
var modHeaderBodyArry ;  

function loadToolTip(){
	$('[rel="tooltip"],[data-rel="tooltip"]').tooltip({"placement":"bottom",delay: { show: 400, hide: 200 }});
}

function clearVals(){ 
	$('#employeeNo').val('');
	$('#firstName').val('');
	$('#lastName').val('');
	$('#userId').val('');
	$('#email').val('');
	$('#telephoneRes').val('');
	$('#telephoneOff').val('');
	$('#mobile').val('');
	$('#fax').val('');
	$('#expiryDate').val('');
	
	var officeTxn = $('select#officeLocation1 option:selected').text();
	
	var offVal = $('#officeLocation').val();
	if(offVal.length == 0) 
		offVal+=officeTxn;
	else
		offVal+=","+officeTxn;
	
	$('#officeLocation').val(offVal); 
	
	$(listid).each(function(index,val){ 
		$('#'+val).find('option').each(function( i, opt ) { 
			if( opt.value === '' ) {
				$(opt).attr('selected', 'selected');
				$('#'+val).trigger("liszt:updated");
			}
		}); 
	});

	rowCount = $('#tbody_data > tr').length; 
	if(rowCount > 0 )  $("#error_dlno").text(''); 
}



/** Form2 Add,Modify Starts*/
var index1 = "";
var searchTdRow = "";
var searchTrRows = "";
var selTextList = "adminType,officeLocation1";
 
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

var addDataVals = function (clickType) {
	 // add custom behaviour
	 var appendTxt = "";
	 var tabArrText = "";
	 var data1 = "";
	try {
		
		encryptPassword(); 
		
		data1 = commonData('#user-details','ADD');
		rowindex = $("#multi-row-data > span") .length ;
		$("#multi-row-data").append("<span id='hidden_span_"+rowindex+"' index="+rowindex+" ind-id='"+tabArry[0]+"' hid-names='"+data1.split("@@")[1]+"' ></span>");
		$('#multi-row-data > span#hidden_span_'+rowindex).append(data1.split("@@")[0]);

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
 				"<td><a class='btn btn-min btn-info' href='#' id='editDat' index='"+rowindex+"' title='Edit' data-rel='tooltip'> <i class='icon-edit icon-white'></i></a> &nbsp;<a class='btn btn-min btn-warning' href='#' id='row-cancel' index='"+rowindex+"' title='Reset' data-rel='tooltip'> <i class='icon icon-undo icon-white'></i></a>&nbsp; <a class='btn btn-min btn-danger' href='#' id='delete' index='"+rowindex+"' title='Delete' data-rel='tooltip'> <i class='icon-trash icon-white'></i></a></td></tr>";
		rowindex = ++rowindex;

	} catch(e) {
		console.log(e);
	}
	return appendTxt;
};

var modDataVals = function (clickType) {
	 // add custom behaviour
		var tabArrText = "";
		var appendTxt = "";

		var data1 = "";
	try {
		modHeaderBodyArry = new Array();

		data1 = commonData('#user-details','MODIFY');

		modHeaderBodyArry[0]=data1.split("@@")[1];
		modHeaderBodyArry[1]=data1.split("@@")[0];
	} catch(e)  {
		console.log(e);
	}
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
$(function() {   

	var userictadminrules = {
		rules : {
			userId : { required : true,  regex: /^[A-Z0-9]+$/ },
			employeeNo : { required : true, regex: /^[0-9]+$/  } ,
			firstName : { required : true,regex: /^[a-zA-Z]+$/} ,
			lastName : { required : true,regex: /^[a-zA-Z]+$/ } ,
			adminType : { required : true } ,
			officeLocation1 : { required : true } ,
			expiryDate : { required : true, date : true } ,
			email : { required : true , email : true}
		},		
		messages : {
			userId : { 
						required : "Please enter User Id.",
						regex : "User Id, can not contain any special characters."
					  }, 
			employeeNo : { 
							required : "Please enter Employee No.",
							regex : "Employee No, contain digits only."
						},
			firstName : { 
							required : "Please enter First Name.",
							regex : "First Name, can not allow digits or special characters."
						},
			lastName : { 
							required : "Please enter Last Name.",
							regex : "Last Name, can not allow digits or special characters."
						},
			adminType : { 
							required : "Please choose User Designation."
						},
			officeLocation1 : { 
							required : "Please choose Office Location."
						},
			expiryDate : { 
							required : "Please select Expiry Date.",
							date  : "Expiry date, should be always greater than system date."
						},
			email : { 
							required : "Please enter email address.",
							email : "Please enter a valid email address."
						}
		} 
	};

	$.validator.addMethod("date", function (value, element) {

		var expDate = value.split('/'); 
		var myDate = new Date(expDate[2], expDate[1] - 1, expDate[0]); 
		var today = new Date(); 
		
		if ( value.match(/^\d\d?\/\d\d?\/\d\d\d\d$/) && myDate > today ) {
			return true;
		} else {
			return false;
		}
	});


	 $('#userId').live('keyup',function(){
		 var id = $(this).attr('id');
		 var v_val = $(this).val(); 
		 $("#"+id).val(v_val.toUpperCase());
	});	
	
	
	// Update Edited-Row Starts Here.
	$('#modCap').live('click', function () {
		$("#error_dlno").text('');	 
		$("#form1").validate(userictadminrules); 
		if($("#form1").valid()) {
			var queryString = "entity=${loginEntity}&method=searchUser&userId="+ $('#userId').val().toUpperCase()+"&employeeNo="+$('#employeeNo').val();	
			$.getJSON("postJson.action", queryString,function(data) { 
				userstatus = data.status; 
				v_message = data.message; 
				if(userstatus == "FOUND") { 
					if(v_message != "SUCCESS") {
						$('#error_dlno').text(v_message);
					}  
				} else {

 					var spanValues = modDataVals("MODIFY");
					 
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
						}if(index == 5){
							$(this).text(modTabArry[4]);
						}if(index == 6){
							$(this).text(modTabArry[5]);
						}
					});  
					$("#multi-row-data span").each(function(index,value) { 
					  
					 if($(this).attr("index") == index1 ) { //&& $(this).attr("ind-id") == modTabArry[0]
						 $(this).attr("hid-names",modHeaderBodyArry[0]);
						 $(this).text(modHeaderBodyArry[1]);  
						 return;
					 }
					}); 
			 
					clearVals();   
					alignSerialNo("#tbody_data > tr");
			
					//Hide Add Button and Display Update Button
					$('#modCap').hide();
					$('#addCap').show();
					
					} 
				});  
			}
				
		});
	
 
	// The below event is to delete the entire row on selecting the delete button 
	$("#delete").live('click',function() { 
		var delId = $(this).attr('index');
		$(this).parent().parent().remove();
		//if($('#tbody_data > tr').length == 0) { rowindex = 0; colindex=0; }
		
		var spanId = "";
		$('#multi-row-data > span').each(function(index){  
			spanId =  $(this).attr('index'); 
			if(spanId == delId) {
				$(this).remove();
			}
		}); 
		
		clearVals();
		// Aligning the serial number
		alignSerialNo("#tbody_data > tr");
		
		//Show Add Button and Hide Update Button
		$('#modCap').hide();
		$('#addCap').show();
		$('.tooltip').remove();
	}); 
	
	
	
	// The below event is to Edit row on selecting the delete button 
	$('#editDat').live('click',function() { 
		$("form").validate().cancelSubmit = true; 
		 
		index1 = $(this).attr('index');  
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		searchTrRows = parentId+" tr"; 
		searchTdRow = '#'+searchTrRows+"#tr-"+index1 +' > td';
		
		var idSearch = "";
		var hidnames = "";
		var text_val = ""; 
	  
		var spanData = $("#multi-row-data > span#hidden_span_"+index1);
		hidnames =  $("#multi-row-data > span#hidden_span_"+index1).attr('hid-names');
		text_val =  $("#multi-row-data > span#hidden_span_"+index1).text();
		
		var hidarr=hidnames.split(",");
		var textarr=text_val.split(",");  
	 
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
		$('#modCap').show();
		$('#addCap').hide(); 
	}); 
	
	
	$('#btn-cancel').live('click', function () { 
		$("form").validate().cancelSubmit = true; 
		var url="${pageContext.request.contextPath}/<%=appName %>/userGrpCreation.action?pid=7"; 
		$("#form1")[0].action=url;
		$("#form1").submit();  
	});
	
	$('#btn-submit').live('click', function () { 
		//$("form").validate().cancelSubmit = true;
		var rowCount = $('#tbody_data > tr').length; 
		 $("#error_dlno").text('');
		if(rowCount > 0) {
			$("#form1").validate().cancelSubmit = true;
		 
			$('#multi-row-data > span').each(function(index) {  
 				if(index == 0)  finalData = $(this).text();
				else finalData += "#"+ $(this).text();
			}); 
		 
			$('#multiData').val(finalData);
			
			var officeLoc = "";
			$('#officeLocation').val('');
			
			$('#tbody_data > tr').each(function(index){   
				var id = $(this).attr("id");
				$('#tbody_data > tr#'+id +' > td').each(function(index1){  
					if(index1 == 5 ) { 
						if(index == 0 ) officeLoc=$(this).text().trim();
						else officeLoc+=","+$(this).text().trim();
					}
				});
			});
			
			$('#officeLocation').val(officeLoc);
			
 			var url="${pageContext.request.contextPath}/<%=appName %>/ictAdmincreatePageConfirm.action"; 
			$("#form1")[0].action=url;
			$("#form1").submit(); 
		} else { 
			$("#error_dlno").text("Please add atleast one user.");
		}
	});
	
	$("#telephoneRes,#telephoneOff,#mobile,#fax,#employeeNo").keypress(function (e) {
	 //if the letter is not digit then display error and don't type anything
	 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
		//display error message
		$("#"+$(this).attr('id')+"_err").html("Digits Only").show().fadeOut(4000);
			   return false;
		}
   }); 
   
	$.validator.addMethod("regex", function(value, element, regexpr) {          
		 return regexpr.test(value);
	   }, "");    
	
	// Add Row Starts Here.
	$('#addCap').live('click', function () {
		$("#error_dlno").text('');	 
		$("#form1").validate(userictadminrules);
		 		
		if($("#form1").valid()) { 
			var textMess = "#tbody_data > tr"; 
			var queryString = "entity=${loginEntity}&method=searchUser&userId="+ $('#userId').val().toUpperCase()+"&employeeNo="+$('#employeeNo').val();	
			$.getJSON("postJson.action", queryString,function(data) { 
				userstatus = data.status; 
				v_message = data.message;
				 
				if(userstatus == "FOUND") { 
					if(v_message != "SUCCESS") {
						$('#error_dlno').text(v_message);
					}  
				} else { 					 
					var appendTxt = addDataVals("ADD");
					$("#mytable").append(appendTxt);  
					clearVals();
				}  
				alignSerialNo(textMess); 
				loadToolTip();
			});   
			 
		} else {
			return false;				
		}  
	});   
	
	// Clear Form Records Row Starts Here.
	$('#row-cancel').live('click', function () {
		$("#error_dlno").text(''); 
		 clearVals(); 
		 
		//Show Add Button and Hide Update Button
		$('#modCap').hide();
		$('#addCap').show();
	}); 
	
});  	

</script>
 
</head>
<body> 
<form name="form1" id="form1" method="post"> 
	 
<div id="content" class="span10"> 
			<!-- content starts -->
	<div> 
		<ul class="breadcrumb">
		  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
		  <li> <a href="userGrpCreation.action?pid=7">User Management</a> <span class="divider"> &gt;&gt; </span></li> 
		  <li> <a href="#">User Creation</a></li> 
		</ul>
	</div>  

			
  <div class="row-fluid sortable"> 
	<div class="box span12">
					
			<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>User Details
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

				</div>
			</div>  
		<div class="box-content" id="user-details">
			<fieldset> 
				 <table width="950" border="0" cellpadding="5" cellspacing="1" 
							class="table table-striped table-bordered bootstrap-datatable " id="user-details">  
						<tr class="odd">
 							<td width="20%"><label for="User Id"><strong>User Id<font color="red">*</font></strong></label></td>
							<td width="30%"><input name="userId" id="userId" class="field" type="text" required=true   maxlength="20"/>	 </td>						 
 							<td width="20%"><label for="Employee No"><strong>Employee No<font color="red">*</font></strong></label></td>
	                    	<td width="30%"><input name="employeeNo" id="employeeNo" type="text"  class="field" required=true   maxlength="10" /> <span id="employeeNo_err" class="errmsg"></span> </td>
						</tr>
						<tr class="odd">
 							<td><label for="First Name"><strong>First Name<font color="red">*</font></strong></label></td>
							<td><input name="firstName" id="firstName"  type="text" class="field" required=true  maxlength="50" /></td>
 							<td><label for="Last Name"><strong>Last Name<font color="red">*</font></strong></label></td>
							<td><input name="lastName" type="text" class="field" id="lastName"  required=true maxlength="50" /></td>
						</tr>
						<tr class="odd">
 							<td><label for="Telephone Res"><strong>Telephone(Res)</strong></label></td>
							<td><input name="telephoneRes" type="text" class="field" id="telephoneRes"   maxlength="9" size="30" /><span id="telephoneRes_err" class="errmsg"></span></td>
 					        <td><label for="Telephone Off"><strong>Telephone(Off)</strong></label></td>
					        <td><input name="telephoneOff" type="text" class="field"  id="telephoneOff"  maxlength="9" size="30" /><span id="telephoneOff_err" class="errmsg"></span></td>
				       </tr>

				     <tr class="odd">
 						    <td><label for="Mobile"><strong>Mobile</strong></label></td>
							<td><input name="mobile" type="text" class="field" id="mobile" maxlength="9" size="30" /><span id="mobile_err" class="errmsg"></span></td>
 					        <td><label for="Fax"><strong>Fax</strong></label></td>
					        <td><input name="fax" type="text" class="field" id="fax"  maxlength="9" size="30" /><span id="fax_err" class="errmsg"></span></td>
				     </tr>

				    <tr class="odd">
 					  <td><label for="User Designation"><strong>User Designation<font color="red">*</font></strong></label></td>
					  <td>
						<select id="adminType" name="adminType" data-placeholder="Choose User Designation..." 
										class="chosen-select" style="width: 220px;" required=true  >
							<option value="">Select</option>
							<option value="Region">Region</option>
							<option value="Head Office">Head Office</option>
							<!-- <option value="Branch Office">Branch </option> -->
							<option value="Branch Office">Supervisor</option>
							<option value="Branch Manager">Branch Manager</option>
							<option value="User">User</option>
						</select>
					 </td>
 					 <td><label for="Office Location"><strong>Office Location<font color="red">*</font></strong></label></td>
					  <td> 
						<s:select cssClass="chosen-select" 
							headerKey="" 
							headerValue="Select"
							list="#respData.LOCATION_LIST" 
							name="officeLocation1" 
 							id="officeLocation1" 
							requiredLabel="true" 
							theme="simple"
							data-placeholder="Choose office location..." 
 							 /> 
					   </td>
				    </tr>
				<tr class="odd">
 					<td><label for="Expiry Date"><strong>Expiry Date<font color="red">*</font></strong></label></td>
					<td>
					 	<input name="expiryDate" type="text" class="field" id="expiryDate" required=true  maxlength="20" size="20" />
					</td>
 					<td><label for="E-Mail"><strong>E-Mail<font color="red">*</font></strong></label></td>
					<td>
					 	<input name="email" type="text" class="field" id="email" required=true  maxlength="50" />
						<input name="password" type="hidden" class="field" id="password"   />
						<input name="encryptPassword" type="hidden" class="field" id="encryptPassword"  />
					</td>
				</tr> 
			</table>
		</fieldset> 
		</div>	
		<div class="box-content"> 
			<table class="table table-striped table-bordered bootstrap-datatable " 
						id="mytable" style="width: 100%;">
			  <thead>
					<tr>
						<th>Sno</th>
						<th>User Id</th>
						<th>Employee No</th>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Office Location</th>
						<th>E-Mail</th> 
						<th>Actions</th> 
					</tr>
			  </thead>    
			  <tbody id="tbody_data">
			   
			  	<s:bean name="com.ceva.base.common.bean.JsonDataToObject" var="jsonToList">
					<s:param name="jsonData" value="%{multiData}"/>  
					<s:param name="searchData" value="%{'#'}"/>  
				</s:bean> 
				<s:iterator value="#jsonToList.data" var="mulData"  status="mulDataStatus" >
							<script type="text/javascript">
								var hiddenNames1 = "";
								$(function(){
									$('#user-details').find('input[type=text],input[type=hidden],select').each(function(index){ 
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
									$("#multi-row-data").append("<span id='hidden_span_<s:property value="#mulDataStatus.index" />' index='<s:property value="#mulDataStatus.index" />' ind-id='"+data1[0]+"' hid-names='"+hiddenNames1+"' ><s:property value="#mulData" /></span>");
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
								<s:iterator status="itrStatus"   > 
									<s:if test="#itrStatus.index == 0 || #itrStatus.index == 1 
											|| #itrStatus.index == 2 || #itrStatus.index == 3
											|| #itrStatus.index == 9 || #itrStatus.index == 11" > 
											<td><s:property  /></td> 
										</s:if>
								</s:iterator>  
							</s:generator>
						<td><a class='btn btn-min btn-info' href='#' id='editDat' index='<s:property value="#mulDataStatus.index" />' title='Edit' data-rel='tooltip'> <i class='icon-edit icon-white'></i></a> &nbsp;
							<a class='btn btn-min btn-warning' href='#' id='row-cancel' index='<s:property value="#mulDataStatus.index" />' title='Reset' data-rel='tooltip'> <i class='icon icon-undo icon-white'></i></a>&nbsp; 
							<a class='btn btn-min btn-danger' href='#' id='delete' index='<s:property value="#mulDataStatus.index" />' title='Delete' data-rel='tooltip'> <i class='icon-trash icon-white'></i></a>
						</td>
					</tr>
				</s:iterator>
			  </tbody>
			</table> 
		</div>  
	</div>	
	</div> 
	<span id="multi-row-data" class="multi-row-data" style="display:none"> </span>
	
	<div class="form-actions">
		   <input type="button" class="btn btn-success" name="modCap" id="modCap" value="Update" width="100" style="display:none" ></input> 
		   <input type="button" class="btn btn-success" name="addCap" id="addCap" value="Add" width="100"  ></input> 
			&nbsp;<input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Submit" width="100" ></input>
			&nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Cancel" width="100" ></input>
			<span id ="error_dlno" class="errors"></span>
			
 			<input name="groupID" type="hidden" id="groupID" value="<s:property value="groupID" />" />
			<input name="entity" type="hidden" id="entity" value="<s:property value="entity" />" />
			<input type="hidden" name="multiData" id="multiData" value="<s:property value="multiData" />" />
			<input type="hidden" name="officeLocation" id="officeLocation" value="<s:property value="officeLocation" />" />
	</div>  
<script language="Javascript" src="<%=ctxstr%>/js/authenticate.min.js"></script>
<script language="javascript" src="<%=ctxstr%>/js/sha256.min.js" ></script> 

<script type="text/javascript">  
$(function() {
	$(listDate).each(function(i,val){
 		$('#'+val).datepicker(datepickerOptions);
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
	
	var loc = $('#officeLocation').val().split(","); 
	
	$('#tbody_data > tr ').each(function(index){   
		var id = $(this).attr("id");
		$('#tbody_data > tr#'+id +' > td').each(function(index1){  
 			if(index1 == 5 ) {
				$(this).text(loc[index]);
			}
		});
	});
	 
});
</script>
</div> 
</form> 
</body>  
</html>