
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
 var createFeeRules = {
    rules : {
     flatPercent : { required : true },
 	partnerAccount : { required : true },
 	faltPercentAmount : { required : true }
    },  
    messages : {
     flatPercent : { 
        required : "Please seelct Flat/Percent"
         },
 	partnerAccount : { 
        required : "Please select Partner Account"
         },
 	faltPercentAmount : {
 		required : "Please enter Flat/Percent Amount"
 	}
    } 
  };

 function createSubService(){
 	$("#form1").validate().cancelSubmit = true;
 	accountFinalData=accountFinalData.slice(1);
 	if(accountFinalData=="") {
 		alert("Please add alteast one record");
 		return false;
 	}else{
 		$("#accountMultiData").val(accountFinalData);
 		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/feeCreateSubAct.action';
 		$("#form1").submit();
 		return true;
 	}
 			
 }

 function getInfo(){
 	var data=$("#service option:selected" ).text();
  	$("#hudumaServiceName").val(data);
 	var service=$("#service option:selected" ).val();
  	var formInput='hudumaService='+service+'&method=checkHudumaSubService';
 	   $.getJSON('postJson.action', formInput,function(data) {
 	     	var json = data.responseJSON.SERVICE_LIST;
 	     	//document.form1.hudumaSubService.options.length=1;
			$('#hudumaSubService').find('option:not(:first)').remove();
 	    	$.each(json, function(i, v) {
 				var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);  
 				$('#hudumaSubService').append(options);
 			});
 		});
 }	

 function getSubService(){
 	var data=$("#hudumaSubService option:selected" ).text();
 		$("#hudumaSubServiceName").val(data);
 } 

var val = 1;
var rowindex = 1;
var colindex = 0;
var accountFinalData="";
 	
$(document).ready(function() {

	var feeCode='${responseJSON.subServiceCode}'+'-F000000'+'${responseJSON.feeCode}';
		$("#feeCode").val(feeCode);
		$("#TransData").hide();
		$("#service1").hide();
		$("#service2").hide();
		$("#service3").hide();
		$("#service4").hide();
		
		$("#Huduma").click(function(){
			if(form1.Huduma.checked == false) {				
				$("#serviceType").val("N");
				$("#service1").hide();
				$("#service2").hide();
				$("#service3").hide();
				$("#service4").hide();				
			} else {   
				$("#serviceType").val("Y");
				$("#service1").show();
				$("#service2").show();
				$("#service3").show();
				$("#service4").show();
				
			}
		});
		
		$("#Slab").click(function(){
			if(form1.Slab.checked == false) 
			{
				$("#TransData").hide();
				
			} else {   
				$("#TransData").show();
			}
		});
		
		
	
	var mydata ='${responseJSON.AccountList}';
     	var json = jQuery.parseJSON(mydata);
     	$.each(json, function(i, v) {
    	    // create option with value as index - makes it easier to access on change
    	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
    	    // append the options to job selectbox
    	    $('#partnerAccount').append(options);
    	});
		
	var data ='${responseJSON.ServicesList}';
     	  json = jQuery.parseJSON(data);
     	$.each(json, function(i, v) {
    	    // create option with value as index - makes it easier to access on change
    	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
    	    // append the options to job selectbox
    	    $('#service').append(options);
    	}); 
		
	 $('#addCap').live('click', function () {
		var partnerAccount = $('#partnerAccount').val() == undefined ? ' ' : $('#partnerAccount').val();
		var amount = $('#faltPercentAmount').val() == undefined ? ' ' : $('#faltPercentAmount').val();
		$("#form1").validate(createFeeRules);
		if($("#form1").valid()) { 	
			var eachrow=partnerAccount+","+amount;				
			var addclass = "";
			if(form1.Huduma.checked == true) {
				var hudumaService=$("#service option:selected" ).val();
				var hudumaSubService=$("#hudumaSubService option:selected" ).val();
				if(hudumaService==""){
					alert("Please select Huduma Service");
					return false;
				}
				else if(hudumaSubService==""){
					alert("Please select Huduma Sub Service");
					return false;
				}
			}
			if(form1.Slab.checked == true) {
				var slabFrom=$("#slabFrom" ).val();
				var slabTo=$("#slabTo" ).val();
				if(slabFrom==""){
					alert("Please enter Slab From");
					return false;
				}
				else if(slabTo==""){
					alert("Please enter Slab To");
					return false;
				}
			}
			if(partnerAccount==""){
				alert("Please enter Partner Account");
					return false;
			}
			if(amount==""){
				alert("Please enter Falt/Percent Amount");
					return false;
			}
			if(val % 2 == 0 ) {
						addclass = "even";
				val++;
			}
			else {
				addclass = "odd";
				val++;
			}  
			
			var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
								"<td  >"+rowindex+"</td>"+
								"<td>"+partnerAccount+"</td>"+	
								"<td>"+amount+" </td>"+ 
								"<td> <a class='btn btn-min btn-info' href='#' id='editDat'> <i class='icon-edit icon-white'></i></a>  <a class='btn btn-min btn-danger' href='#' id='deleteemail'> <i class='icon-trash icon-white'></i> </a></td></tr>";
									
								$("#tbody_data2").append(appendTxt);	 
								$('#partnerAccount').val('');
								$('#faltPercentAmount').val('');
								accountFinalData=accountFinalData+"#"+eachrow;
									
											
				
					colindex = ++ colindex; 
					rowindex = ++rowindex;
		}else{
			return false;
		}
	});  
	
	
	var feeDetailsExist = '${responseJSON.EXIST_FEE_DETAILS}';
	  json = $.parseJSON(feeDetailsExist);  
			 
	 $.each(json, function(i, v) {
		  
		var addclass = "";
					
		if(val % 2 == 0 ) {
			addclass = "even";
			val++;
		}
		else {
			addclass = "odd";
			val++;
		}  
		var rowCount = $('#tbody_data2 > tr').length;

		
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
						"<td> &nbsp;"+rowindex+"</td>"+
						"<td> "+v.FIN_CODE+"</td>"+	
						"<td> "+v.PER_FLT_AMT+" </td>"+ 
 						"<td><a class='btn btn-min btn-danger' href='#' id='deleteemail'> <i class='icon-trash icon-white'></i> </a></td></tr>";
 
		 $("#tbody_data2").append(appendTxt);
			rowindex = ++rowindex;
			colindex = ++ colindex; 
	 });  
});
</script>
</head>
<body>
<form name="form1" id="form1" method="post" action=""> 
	<div id="content" class="span10">  
		<div>
			<ul class="breadcrumb">
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
			  <li> <a href="#">Fee Management</a> <span class="divider"> &gt;&gt; </span></li>
			  <li><a href="#">Create Fee</a></li>
			</ul>
		</div>

	  <div class="row-fluid sortable"><!--/span--> 
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<i class="icon-edit"></i>Create Fee
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
				</div>
			</div> 
			<div id="primaryDetails" class="box-content">
				<fieldset> 
					 <table width="950" border="0" cellpadding="5" cellspacing="1" 
						class="table table-striped table-bordered bootstrap-datatable " >
							<tr class="odd">
								<td width="20%"><label for="Service ID"><strong>Service Code<font color="red">*</font></strong></label></td>
								<td width="30%"><input name="serviceCode" type="text" id="serviceCode" class="field" value="${responseJSON.serviceCode}" readonly ></td>
								<td width="20%"><label for="Service Name"><strong>Service Name<font color="red">*</font></strong></label></td>
								<td width="30%"><input name="serviceName" type="text"  id="serviceName" class="field"  value="${responseJSON.serviceName}" readonly></td>
							</tr>
							<tr class="odd">
								<td ><label for="Sub Service Code"><strong>Sub Service Code<font color="red">*</font></strong></label></td>
								<td ><input name="subServiceCode"  type="text" id="subServiceCode" class="field" value="${responseJSON.subServiceCode}"   readonly > </td>
								<td><label for="Sub Service Name"><strong>Sub Service Name<font color="red">*</font></strong></label></td>
								<td>
									<input name="subServiceName"  type="text" id="subServiceName" class="field" value="${responseJSON.subServiceName}"   readonly >
								</td>
							</tr>
							<tr class="odd">
								<td ><label for="Fee Code"><strong>Fee Code<font color="red">*</font></strong></label></td>
								<td><input name="feeCode" type="text" id="feeCode" class="field" value="${responseJSON.feeCode}" readonly ></td>
								
								<td ><label for="Fee Code"><strong>Flat/Percent<font color="red">*</font></strong></label></td>
								<td>
									<select id="flatPercent" name="flatPercent" >
										<option value="">Select</option>
										<option value="F" >Flat</option>
										<option value="P">Percent</option>
									</select>
								</td>  
							</tr>
							<tr class="odd">
								<td ><label for="Fee Code"><strong>Huduma</strong></label></td>
								<td> <input name="Huduma" type="checkbox" value="Huduma" id="Huduma"></td>
								
								<td ><label for="Fee Code"><strong>Slab</strong></label></td>
								<td>
									<input name="Slab" type="checkbox" value="Slab" id="Slab">
								</td>
								<input type="hidden" name="serviceType" id="serviceType" value="N">
							</tr> 
							<tr class="odd" id="TransData">
								<td ><label for="Slab From"><strong>Slab From<font color="red">*</font></strong></label></td>
								<td >
									<input name="slabFrom"  type="text" id="slabFrom" class="field" value="${responseJSON.slabFrom}" >
									<span id="slabFromErr"></span>
								</td>
								<td><label for="Slab To"><strong>Slab To<font color="red">*</font></strong></label></td>
								<td>
									<input name="slabTo"  type="text" id="slabTo" class="field" value="${responseJSON.slabTo}" >
									<span id="slabToErr"></span>
								</td>
							</tr>  
							<tr class="odd">
								<td id="service1">
									<label for="Service"><strong>Huduma Service<font color="red">*</font></strong></label>
								</td>
								<td id="service2">
									<select id="service" name="service" onChange="getInfo()">
										<option value=""><strong>Select</option>
									</select>
									<input name="hudumaServiceName"  type="hidden" id="hudumaServiceName" class="field" value="${responseJSON.hudumaServiceName}" >
								</td>
								<td id="service3">
									<label for="Service"><strong>Huduma Sub Service<font color="red">*</font></strong></label>
								</td>
								<td id="service4">
									<select id="hudumaSubService" name="hudumaSubService" onChange="getSubService()">
										<option value="">Select</option>
									</select>
									<input name="hudumaSubServiceName"  type="hidden" id="hudumaSubServiceName" class="field" value="${responseJSON.hudumaSubServiceName}">
								</td>
							</tr>
							<tr class="odd">
								<td >
									<label for="Service"><strong>Fee For<font color="red">*</font></strong></label>
								</td>
								<td colspan="3">
									<select id="feeFor" name="feeFor" >
										<option value="NO_DATA" selected><strong>Select</option>
										<option value="CUS"><strong>Customer</option>
										<option value="REC"><strong>Receipent</option>
										<option value="ONS"><strong>On share</option>
									</select>
								</td>
							</tr>
						</table> 
				</fieldset> 
			</div>
			
			<div class="box-content">
				<fieldset> 
					 <table width="950" border="0" cellpadding="5" cellspacing="1" 
						class="table table-striped table-bordered bootstrap-datatable " >
							<tr class="odd">
								<td ><label for="Partner Account"><strong>Partner Account<font color="red">*</font></strong></label></td>
								<td>
									<select id="partnerAccount" name="partnerAccount" >
										<option value="">Select</option>
									</select>
								</td>
								<td ><label for="Amount"><strong>Amount<font color="red">*</font></strong></label></td>
								<td>
									<input name="faltPercentAmount"  type="text" id="faltPercentAmount" class="field" value="${responseJSON.faltPercentAmount}" >
								</td>
							</tr>
							<tr class="odd">
								<td colspan="4"><input type="button" class="btn btn-success" name="addCap" id="addCap" value="Add" ></input></td>
							</tr>
					</table> 
				</fieldset> 
			</div>
			
			<div id="primaryDetails" class="box-content">
				<fieldset>  
					<table class="table table-striped table-bordered bootstrap-datatable " 
							id="accountData" style="width: 100%;">
						<thead>
							<tr>
								<th>Sno</th>
								<th>Partner Account</th>
								<th>Percent/Flat Amount</th>
								<th>Action</th>
							</tr>
						</thead>    
						<tbody  id="tbody_data2">
						</tbody>
					</table>
				</fieldset> 
			</div>  
			<input type="hidden" name="accountMultiData" id="accountMultiData" value="" /> 			
		</div><!--/#content.span10-->
	</div><!--/fluid-row--> 
	<div class="form-actions"> 
		<a class="btn btn-info" href="#" onClick="getServiceScreen()">Back</a> &nbsp;&nbsp;
		<a class="btn btn-success" href="#" onClick="createSubService()">Submit</a>
	</div> 
</div>  
</form>
</body>
</html>
