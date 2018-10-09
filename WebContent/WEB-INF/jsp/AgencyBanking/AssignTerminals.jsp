<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Bank Details</title>
   
  <script type="text/javascript">

$(document).ready(function () {
		
    $('#save').on('click', function () {
    	finalData=finalData.slice(1);
    	$('#multiData').val(finalData);
        	//alert("inside true");
			$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/insertBankDetails.action';
			$("#form1").submit();
					return true;
    });
});

$(document).ready(function () {
	var mydata ='${responseJSON.ENTITY_LIST}';
	var json = jQuery.parseJSON(mydata);
	$.each(json, function(i, v) {
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
	    $('#entity').append(options);
	});
	
	var mydata ='${responseJSON.merchantList}';
	var json = jQuery.parseJSON(mydata);
	$.each(json, function(i, v) {
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
	    $('#merchantId').append(options);
	});
	
	var mydata ='${responseJSON.storeList}';
	var json = jQuery.parseJSON(mydata);
	$.each(json, function(i, v) {
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
	    $('#storeId').append(options);
	});
	
	var mydata ='${responseJSON.terminalList}';
	var json = jQuery.parseJSON(mydata);
	$.each(json, function(i, v) {
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
	    $('#terminalId').append(options);
	});
	
	var mydata ='${responseJSON.userList}';
	var json = jQuery.parseJSON(mydata);
	$.each(json, function(i, v) {
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
	    $('#userId').append(options);
	});
	
});

</script>

</head>
<body>
	 <form name="form1" id="form1">
	<div id="content" class="span10">  
		
			<div>
					<ul class="breadcrumb">
						<li>
							<a href="#">Bank Details</a> <span class="divider"></span>
						</li>					
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
	 
 <div class="row-fluid sortable"> 
			<div class="box span12">
									
				<div class="box-header well" data-original-title>
					 <i class="icon-edit"></i>Bank Details
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					</div>
				</div> 		
				<div class="box-content" id="createUpdateEntity">
					<fieldset>  
						 <table width="950" border="0" cellpadding="5" cellspacing="1" class="table">
								<tr class="even">
									<td ><strong><label for="Bank Code">Bank Entity<font color="red">*</font></label></strong></td>
									<td >
										<select id="entity"  class="field">
											<option value="">Select</option>
										</select>
									</td>
									<td ><strong><label for="Merchant ID">Merchant ID<font color="red">*</font></label></strong></td>
									<td>
										<select id="merchantId"  class="field">
											<option value="">Select</option>
										</select>
									 </td>
								</tr>
								
								<tr  class="odd">
									<td><strong><label for="Store ID">Store ID<font color="red">*</font></label></strong></td>
									<td>
										<select id="storeId"  class="field">
											<option value="">Select</option>
										</select>
									</td>
									<td><strong><label for="Terminal ID">Terminal ID<font color="red">*</font></label></strong></td>
									<td>
										<select id="terminalId"  class="field">
											<option value="">Select</option>
										</select>	
									</td>
								</tr>
								<tr class="even">
									<td><strong><label for="User ID">User ID<font color="red">*</font></label></strong></td>
									<td>
										<select id="userId"  class="field">
											<option value="">Select</option>
										</select>	
									</td>
									<td></td>
									<td></td>
								</tr>
								<tr  class="odd">
									<td colspan="4" align="center"><input type="button" class="btn btn-success" name="addCap" id="addCap" value=Add ></input></td>
								</tr>
							</table>
					</fieldset> 
				</div>
			
	<script type="text/javascript">
		var val = 1; 
		var finalData="";
		 $('#addCap').on('click', function () {
	 		//$(this).val('Delete');
			//$(this).attr('class','del');
			var entity = $('#entity').val() == undefined ? ' ' : $('#entity').val();
			var merchantId = $('#merchantId').val() == undefined ? ' ' : $('#merchantId').val();
			var storeId = $('#storeId').val() == undefined ? ' ' : $('#storeId').val();
			var terminalId = $('#terminalId').val() == undefined ? ' ' : $('#terminalId').val();
			var userId = $('#userId').val() == undefined ? ' ' : $('#userId').val();
			
			var eachrow=entity+","+merchantId+","+storeId+","+terminalId+","+userId;
			
			var addclass = "";
			 
			if(val % 2 == 0 ){
				addclass = "even";
				val++;
			}
			else {
				addclass = "odd";
				val++;
			} 
	  		
			if(entity == '') 
			{
				alert('please select Entity');
	 		}
			else if(merchantId == '') 
			{
				alert('please enter Merchant Id');			
			}
			else if(storeId == '') 
			{
				alert('please enter Store Id');			
			}
			else if(terminalId == '') 
			{
				alert('please enter Terminal Id');			
			}
			else if(userId == '') 
			{
				alert('please enter User Id');			
			}
			else
			{ 
				var rowCount = $('#myTable >tr').length;
				
	 			var appendTxt = "<tr class="+addclass+" align='center'><td><input type='hidden' name='entity' value='"+entity+"' />"+entity+" </td> "+
						" <td><input type='hidden' name='merchantId' value='"+merchantId+"'  />  "+merchantId+" </td> "+
						" <td><input type='hidden' name='storeId' value='"+storeId+"'  />  "+storeId+" </td> "+
						" <td><input type='hidden' name='terminalId' value='"+terminalId+"'  />  "+terminalId+" </td> "+
						" <td><input type='hidden' name='userId' value='"+userId+"'  />  "+userId+" </td> "+
						"<td><input type='button' class='btn btn-success' name='delete' id='delete' value='Delete' onClick='DelRow()' /></td></tr>";
				$("#mytable").append(appendTxt);
				$('#merchantId').val('');
				$('#storeId').val('');
				$('#terminalId').val('');
				$('#userId').val('');
				$('#entity').val('');
				finalData=finalData+"#"+eachrow;
			}
			
			
			
			
		});   
		 
	</script>
			
			<table id="mytable" width="950" border="0" cellpadding="5" cellspacing="1" class="table">
				<tr class="even" >
				<th><strong><label for="Bank Code">Bank Entity</label></strong></th> 
					<th><strong><label for="BIN">Merchant ID</label></strong></th>
					<th><strong><label for="BIN Discription">Store ID</label></strong></th>
					<th><strong><label for="BIN Type">Terminal ID</label></strong></th>	
					<th><strong><label for="Scheme">User ID</label></strong></th>	
		 			<th>&nbsp;</th>
			 	</tr> 
			</table>
		</div> 	 
	</div>
			<input type="hidden" name="multiData" id="multiData" value=""></input>
			<table width="950" border="0" cellpadding="5" cellspacing="1" class="table">
				<tr  class="even">
					<td  align="right"><input type="button" class="btn btn-success" name="save" id="save" value="Save"  ></input></td>
				</tr>	
			</table>
	</div>
	 </form> 
</body> 
</html>