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
 
.messages {
  font-weight: bold;
  color: green;
  padding: 2px 8px;
  margin-top: 2px;
}

.errors{
  font-weight: bold;
  color: red;
  padding: 2px 8px;
  margin-top: 2px;
}
</style>
<script type="text/javascript" >  
 
var val = 1; 
var rowindex = 0;
var colindex = 0;
var flagAcc = true;
var userictadminrules = {
	rules : {
		service : { required : true },
		accountname : { required : true } ,
		openbalance : { required : true, digits: true } ,
		//closebalance : { required : true,digits: true } ,
		accounttype : { required : true }
	},		
	messages : {
		service : { 
					required : "Please Select Services."
				  }, 
		accountname : { 
						required : "Please Select Account Name."
					},
		openbalance : { 
						required : "Please Input Open Balance."
					},
		/*closebalance : { 
						required : "Please Input Close Balance."
					},*/
		accounttype : { 
						required : "Please Choose Office Location."
					} 
	} 
};
$(document).ready(function() {
 
 
 	var mydata ='${responseJSON.LOCATION_LIST}';
 	var json = jQuery.parseJSON(mydata);
 	$.each(json, function(i, v) {
	    // create option with value as index - makes it easier to access on change
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);
	    // append the options to job selectbox
	    $('#service').append(options);
	}); 
	
	$('#accounttype').live('change',function() {
		var accttype = $('#accounttype').val();
		var service = $('#service').val();
		
		if(accttype != '' && service != '')  {
		   var formInput='accounttype='+accttype +'&service='+service+'&method=checkTransactionType'; 
		   //$.getJSON('checkTransactionType.action', formInput,function(data) { 
		   $.getJSON('postJson.action', formInput,function(data) { 
				var resultCnt=data.responseJSON.RESULT_COUNT;
				if(resultCnt==1) { 
					$("#error_dlno").text("Service Already Exists with "+$('#accounttype').val());
					flagAcc = false;
				} else {
					$("#error_dlno").text("");
					flagAcc = true;
				}
		   });  
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
	
	// The below event is to delete the entire row on selecting the delete button 
	$('#delete').live('click',function() {  
		var delId = $(this).attr('index');
		$(this).parent().parent().remove();
		if($('#myTable > tbody  > tr').length == 0) { rowindex = 0; colindex=0; } 
		var spanId = "";
		$('#multi-row-data > span').each(function(index){  
			spanId =  $(this).attr('index');
			if(spanId == delId) {
				$(this).remove();
			}
		});  
	}); 
   
   $('#save').live('click', function () { 
			
		var rowCount = $('#tbody_data > tr').length; 
		 $("#error_dlno").text('');
		 
		if(rowCount > 0 && flagAcc) {
			$("#form1").validate().cancelSubmit = true;
			var specChar = "";
			var prevCount = "";
			$('#multi-row-data > span').each(function(index){   
				if(index == 0)  finalData = $(this).text();
				else finalData += "#"+ $(this).text(); 
			}); 
		 
			$('#bankMultiData').val(finalData);
			 
			var url="${pageContext.request.contextPath}/<%=appName %>/BankDetialsConfirm.action"; 
			$("#form1")[0].action=url;
			$("#form1").submit(); 
		} else { 
			$("#error_dlno").text("Please add atleast one service.");
		}
	});
   
	// Add Row Script.
	var finalData="";
	 $('#addCap').on('click', function () { 
		$("#error_dlno").text('');	
		
	 
		$("#form1").validate(userictadminrules);
			
		if($("#form1").valid() && flagAcc) {  
	 
			var service = $('#service').val() == undefined ? ' ' : $('#service').val();
			var accountname = $('#accountname').val() == undefined ? ' ' : $('#accountname').val();
			var openbalance = $('#openbalance').val() == undefined ? ' ' : $('#openbalance').val();
			var closebalance = $('#closebalance').val() == undefined ? ' ' : $('#closebalance').val();
			var accounttype = $('#accounttype').val() == undefined ? ' ' : $('#accounttype').val();  
			//var eachrow= service+","+accountname+","+openbalance+","+closebalance+","+accounttype;
			
			var  hiddenInput ="";
			$('#user-details').find('input,select').each(function(index){ 
				var valToSpn = ($(this).attr('value') =='' ? ' ' :$(this).attr('value')); 
				//console.log("Name ["+$(this).attr('name')+"] Values["+$(this).val()+"]");
				if($(this).attr('name') != undefined) {
				  if(index == 0) hiddenInput += valToSpn;
				  else hiddenInput += ","+valToSpn; 
				}
			}); 
			
			 $("#multi-row-data").append("<span id='hidden_span_"+rowindex+"' index="+rowindex+" ></span>");
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
			var appendTxt = "<tr class="+addclass+" align='center' id='"+rowindex+"' index='"+rowindex+"'>"+
					"<td class='col_"+colindex+"'> &nbsp;"+colindex+"   </td> "+ 
					"<td> "+service+" </td> "+
					"<td> "+accountname+" </td> "+
					"<td>"+openbalance+" </td> "+
					//" <td>"+closebalance+" </td> "+
					" <td>"+accounttype+" </td> "+
					" <td><input type='button' class='btn btn-info' name='delete' id='delete' value='Delete' index='"+rowindex+"' /></td></tr>";
			rowindex = ++rowindex;
			$("#mytable").append(appendTxt); 
			$('#openbalance').val('');
			$('#bankAccount').val('');
			//$('#closebalance').val('');
		 
			var listid = "accountname,service,accounttype".split(",");
			$(listid).each(function(index,val){ 
				$('#'+val).find('option').each(function( i, opt ) { 
					if( opt.value === '' ) {
						$(opt).attr('selected', 'selected');
						$('#'+val).trigger("liszt:updated");
					}
				}); 
			});  
			
			var rowCount = $('#myTable > tr').length; 
			if(rowCount > 0 )  $("#error_dlno").text('');
		} 
		else
		{
			return false;
		}  
	}); 
}); 

</script>  
</head> 
<body>
	<form name="form1" id="form1" method="post" action="">
	<!-- topbar ends -->
	
		
			<div id="content" class="span10">  
             <!-- content starts -->
			<div>
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
 				  <li><a href="#">Bank Account Creation</a></li>
				</ul>
			</div>
		 <div class="row-fluid sortable"> 
			<div class="box span12">									
				<div class="box-header well" data-original-title>
					 <i class="icon-edit"></i>Bank Account Creation
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					</div>
				</div> 		
				<div class="box-content">
					<fieldset> 
						<table width="950"  border="0" cellpadding="5" cellspacing="1" id="user-details"
							class="table table-striped table-bordered bootstrap-datatable " >

							<tr class="even">
								<td  width="25%" ><strong><label for="service"> Services<font color="red">*</font></label></strong></td>
								<td   width="25%">
									<select  name="service" id="service"  class="chosen-select" required="required"
										style="width:250px">
										<option value="">select</option>
									</select>
								</td>  
								<td width="25%"><strong><label for="accountname">Account Name<font color="red">*</font></label></strong></td>
								<td width="25%"><!-- <input name="accountname" type="text" class="field" id="accountname"  onblur="upperCase()" /> -->
									<select  name="accountname" id="accountname" class="chosen-select-no-results"  required="required">
										<option value="">select</option>
										<option value="FLOAT ACCOUNT">FLOAT ACCOUNT</option>
										<option value="COMMISSION ACCOUNT">COMMISSION ACCOUNT</option>
										<option value="COLLECTIONS ACCOUNT">COLLECTIONS ACCOUNT</option>
									 </select></td>
							</tr> 
							<tr class="odd">  
								<td><strong><label for="openbalance">Opening Balance<font color="red">*</font></label></strong></td>
								<td><input name="openbalance"  type="text" id="openbalance" required="required"  class="field"/> <span id="openbalance_err" class="errmsg"></span></td>
								<td><strong><label for="accounttype">Account Type<font color="red">*</font></label></strong></td>
								<td >
									<select  name="accounttype" id="accounttype" class="chosen-select-no-results" required="required">
										<option value="">select</option>
										<option value="T">T-Float</option>
										<option value="F">F-Commision</option>
										<option value="C">C-Collections</option>
									 </select>
								</td>
							</tr>
							 <tr class="odd">  
								<td><strong><label for="Bank Account">Bank Account<font color="red">*</font></label></strong></td>
								<td><input name="bankAccount"  type="text" id="bankAccount" required="required"  class="field"/> <span id="openbalance_err" class="errmsg"></span></td>
								<td>&nbsp;</td>
								<td>&nbsp; <input name="closebalance" type="hidden" id="closebalance" class="field"  value="0" />  </td>
							</tr> 
							 
						</table>
					</fieldset> 
				</div>
					 
				<div class="box-content">
					<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
								id="mytable"  >
					  <thead>
							<tr >
								<th >Sno</th>
								<th >Services</th>
								<th >Account Name</th>
								<th >Open Balance</th>
								<!-- <th width="25%"  style="width: 111px;">Close Balance</th> -->
								<th >Account Type</th>
								<th  >Actions</th> 
							</tr>
					  </thead>    
					  <tbody  id="tbody_data"> 
					  </tbody>
					</table> 
				</div>   
				 
				<span id="multi-row-data" class="multi-row-data" style="display:none"> </span>
				<input type="hidden" name="bankMultiData" id="bankMultiData" />
			</div>
		</div>	 
		
	<div class="form-actions">
		<input type="button" class="btn btn-success" name="addCap" id="addCap"  value="Add" />
		&nbsp;<input type="button" class="btn btn-primary" name="save" id="save" value="save" width="100"  ></input> 
		&nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Cancel" width="100" ></input>
			<span id ="error_dlno" class="errors"></span>
	</div> 
 </div>
 </form> 
</body> 
</html>