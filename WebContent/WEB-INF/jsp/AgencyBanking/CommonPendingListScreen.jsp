<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>MicroInsurance</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%
	String ctxstr = request.getContextPath();
	String appName = session.getAttribute(
			CevaCommonConstants.ACCESS_APPL_NAME).toString();
%>
 <%@taglib uri="/struts-tags" prefix="s"%>
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

.messages {
	font-weight: bold;
	color: green;
	padding: 2px 8px;
	margin-top: 2px;
}

.errors {
	font-weight: bold;
	color: red;
	padding: 2px 8px;
	margin-top: 2px;
}
</style>

<s:set value="responseJSON" var="respData"/>
<script type="text/javascript">
var parentId = "merchant-auth-data > tr";
var subParent = "";
var checkedCheckbox = false;

$(function() {
	var auth_code = "<s:property value='#respData.auth_code' />";
	var status = "<s:property value='#respData.status' />";
 
 
	if(auth_code == 'USERAUTH') {
		$('.merchant').text('User');
 	} else if(auth_code == 'MODUSERAUTH') {
		$('.merchant').text('Modified User ');
 	} else if(auth_code == 'MERCHAUTH') {
		$('.merchant').text('New Merchant');
 	} else if(auth_code == 'STOREAUTH') {
		$('.merchant').text('New Store');
 	}
 	else if(auth_code == 'TERMAUTH') {
		$('.merchant').text('New Terminal ');
 	}
 	else if(auth_code == 'FEEAUTH') {
		$('.merchant').text('New Fee');
 	}
 	else if(auth_code == 'SERVICEAUTH') {
		$('.merchant').text('Service ');
 	}
 	else if(auth_code == 'PRCSSAUTH') {
		$('.merchant').text('Transaction Code');
 	}else if(auth_code == 'BINAUTH') {
		$('.merchant').text('Bin Authrisation');
 	}
 	else if(auth_code == 'SERVICEAUTH') {
		$('.merchant').text('Service Authrisation');
 	}
 	else if(auth_code == 'SUBSEAUTH') {
		$('.merchant').text('Sub Service Authrisation');
 	}
 	else if(auth_code == 'USERSTATUSAUTH') {
		$('.merchant').text('User Status Authrisation');
 	}
 	else if(auth_code == 'TMMAUTH') {
		$('.merchant').text('Terminal Migration ');
 	}
 	

	var storeData = '${responseJSON.agentMultiData}';
 	var json = jQuery.parseJSON(storeData);
	var val = 1;
	var rowindex = 0;
	var colindex = 0;
	var addclass = "";
	var appendTxt = "";

	if(json.length == 0) {
		$('#primaryDetails').show();
		$('#merch-details').hide();
		$('#btn-submit').hide();
		$('#btn-back').show();
	} else {
		$('#primaryDetails').hide();
		$('#merch-details').show();
		$('#btn-submit').show();
		$('#btn-back').show();  
	}


	// add multiple select / deselect functionality
	$("#select-all").click(function () {
		$("#error_dlno").text("");
		$('.center-chk').attr('checked', this.checked);
	});

	// if all checkbox are selected, check the selectall checkbox
	// and viceversa
	$(".center-chk").click(function(){
		$("#error_dlno").text("");
		if($(".center-chk").length == $(".center-chk:checked").length) {
			$("#select-all").attr("checked", "checked");
		} else {
			$("#select-all").removeAttr("checked");
		}
	});

	$("#btn-back").click(function(event) {
		event.preventDefault();
		var url="${pageContext.request.contextPath}/<%=appName %>/AuthorizationAll.action?pid=118";
		$("#form1")[0].action=url;
		$("#form1").submit();
	});

	$("#btn-submit").click(function(event) {
		event.preventDefault();
		var searchIDs="";
	 
 		$("tbody#merchant-auth-data input:radio:checked").each(function(index) {
 			searchIDs=$(this).val(); 
 		   $('#REF_NO').val(searchIDs);
		});

        if(searchIDs.length == 0) {
			$("#error_dlno").text("Please check atleast one record.");
		} else {
			
			var url="${pageContext.request.contextPath}/<%=appName %>/commonAuthListCnf.action";
			$("#form1")[0].action=url;
			$("#form1").submit();

		}
	}); 

});
</script>
</head>
<body>
<form name="form1" id="form1" method="post" >
	<div id="content" class="span10">
			<!-- content starts -->
			<div>
				<ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="serviceMgmtAct.action?pid=10">Merchant Management</a> <span class="divider"> &gt;&gt; </span> </li>
					<li><a href="#"><span id="header-data" class="merchant"> </span> Authorization</a></li>
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
						<span id="header-data" class="merchant"> </span> Information
						<div class="box-icon">
							<a href="#" class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a> <a href="#"
								class="btn btn-close btn-round"><i class="icon-remove"></i></a>
						</div>
					</div>
					<div class="box-content" id="primaryDetails">
						<fieldset>
							<table width="950" border="0" cellpadding="5" cellspacing="1"
								class="table table-striped table-bordered bootstrap-datatable " >
								<tr class="even">
									<td colspan="4">A maker can not authorize records, please login as checker for authorizing.</td>
								</tr>
							</table>
						</fieldset>
					</div>
					<div class="box-content" id="merch-details">
						<fieldset>
							<table width="100%" class="table table-striped table-bordered bootstrap-datatable "
								id="DataTables_Table_0">
								<thead id="merchant-auth-header">
									<tr>
										<th> </th>
										<s:generator val="%{#respData.headerData}"
											var="bankDat" separator="##" >  
											<s:iterator status="itrStatus">  
													<th><s:property /></th> 
											</s:iterator>  
										</s:generator> 
									</tr> 
								</thead>
								<tbody id="merchant-auth-data">
									<s:iterator value="#respData.agentMultiData" var="authCommonList" status="authCommonStatus">  
										<s:if test="#authCommonStatus.even == true" > 
											<tr class="even" index="<s:property value='#authCommonStatus.index' />"  id="<s:property value='#authCommonStatus.index' />">
										 </s:if>
										 <s:elseif test="#authCommonStatus.odd == true">
		      								<tr class="odd" index="<s:property value='#authCommonStatus.index' />"  id="<s:property value='#authCommonStatus.index' />">
		   								 </s:elseif> 
		   								 
		   								 <td ><input class='center-chk' type='radio' value='<s:property value="#authCommonList['REF_NO']" />' /></td>
		   								 
		   								 <s:if test="#respData.auth_code == 'USERAUTH' " >		   								 	
 											<td><s:property value="#authCommonList['LOGIN_USER_ID']" /> </td>   
 											<td><s:property value="#authCommonList['USER_NAME']" /> </td>   
 											<td><s:property value="#authCommonList['MAKER_ID']" /> </td>   
 											<td><s:property value="#authCommonList['MAKER_DTTM']" /> </td>   
 											<td><s:property value="#authCommonList['LOCATION']" /> </td> 
 											<td><s:property value="#authCommonList['OFFICE_NAME']" /> </td>    
 											<td class='hidden-phone'><a href='#' class='label label-info' index='#authCommonStatus.index'><s:property value="#authCommonList['STATUS']" /></a> </td> 
		   								 </s:if> 
		   								 <s:elseif test="#respData.auth_code == 'SERVICEAUTH' " >		   								 	
 											<td><s:property value="#authCommonList['SERVICE_CODE']" /> </td>   
 											<td><s:property value="#authCommonList['SERVICE_NAME']" /> </td>   
 											<td><s:property value="#authCommonList['MAKER_ID']" /> </td>   
 											<td><s:property value="#authCommonList['MAKER_DATE']" /> </td>  
 											<td class='hidden-phone'><a href='#' class='label label-info' index='#authCommonStatus.index'><s:property value="#authCommonList['status']" /></a> </td> 
		   								 </s:elseif> 
		   								  <s:elseif test="#respData.auth_code == 'BINAUTH' " >		   								 	
 											<td><s:property value="#authCommonList['BIN_CODE']" /> </td>   
 											<td><s:property value="#authCommonList['BANK_NAME']" /> </td> 
 											<td><s:property value="#authCommonList['BIN']" /> </td>   
 											<td><s:property value="#authCommonList['BIN_DESC']" /> </td>     
 											<td><s:property value="#authCommonList['MAKER_ID']" /> </td>   
 											<td><s:property value="#authCommonList['MAKER_DATE']" /> </td>  
 											<td class='hidden-phone'><a href='#' class='label label-info' index='#authCommonStatus.index'><s:property value="#authCommonList['STATUS']" /></a> </td> 
		   								 </s:elseif>
		   								 <s:elseif test="#respData.auth_code == 'SUBSEAUTH' " >		   								 	
 											<td><s:property value="#authCommonList['SUB_SERVICE_CODE']" /> </td>   
 											<td><s:property value="#authCommonList['SUB_SERVICE_NAME']" /> </td>
											<td><s:property value="#authCommonList['SERVICE_CODE']" /> </td>   
 											<td><s:property value="#authCommonList['SERVICE_NAME']" /> </td>     
 											<td><s:property value="#authCommonList['MAKER_ID']" /> </td>   
 											<td><s:property value="#authCommonList['MAKER_DATE']" /> </td>  
 											<td class='hidden-phone'><a href='#' class='label label-info' index='#authCommonStatus.index'><s:property value="#authCommonList['STATUS']" /></a> </td> 
		   								 </s:elseif> 
		   								 
		   								 <s:elseif test="#respData.auth_code == 'FEEAUTH' " >		
	   								 		<td><s:property value="#authCommonList['FEE_CODE']" /> </td> 
	   								 		<td><s:property value="#authCommonList['SERVICE_CODE']" /> </td>         								 	
 											<td><s:property value="#authCommonList['SUB_SERVICE_CODE']" /> </td>   
 											<td><s:property value="#authCommonList['FEE_NAME']" /> </td> 
 											<td><s:property value="#authCommonList['MAKER_ID']" /> </td>   
 											<td><s:property value="#authCommonList['MAKER_DATE']" /> </td>  
 											<td class='hidden-phone'><a href='#' class='label label-info' index='#authCommonStatus.index'><s:property value="#authCommonList['STATUS']" /></a> </td> 
		   								 </s:elseif> 
		   								 <s:elseif test="#respData.auth_code == 'TERMAUTH' " >		   								 	
 											<td><s:property value="#authCommonList['TERMINAL_ID']" /> </td>   
 											<td><s:property value="#authCommonList['STORE_ID']" /> </td>
											<td><s:property value="#authCommonList['MERCHANT_ID']" /> </td>   
 											<td><s:property value="#authCommonList['SERIAL_NO']" /> </td>     
 											<td><s:property value="#authCommonList['MAKER_ID']" /> </td>   
 											<td><s:property value="#authCommonList['MAKER_DATE']" /> </td>  
 											<td class='hidden-phone'><a href='#' class='label label-info' index='#authCommonStatus.index'><s:property value="#authCommonList['STATUS']" /></a> </td> 
		   								 </s:elseif>
		   								  <s:elseif test="#respData.auth_code == 'STOREAUTH' " >		   								 	
 											<td><s:property value="#authCommonList['STORE_ID']" /> </td>   
 											<td><s:property value="#authCommonList['STORE_NAME']" /> </td>
											<td><s:property value="#authCommonList['MERCHANT_ID']" /> </td>   
 											<td><s:property value="#authCommonList['MERCHANT_NAME']" /> </td>     
 											<td><s:property value="#authCommonList['MAKER_ID']" /> </td>   
 											<td><s:property value="#authCommonList['MAKER_DATE']" /> </td>  
 											<td class='hidden-phone'><a href='#' class='label label-info' index='#authCommonStatus.index'><s:property value="#authCommonList['STATUS']" /></a> </td> 
		   								 </s:elseif> 	
		   								  <s:elseif test="#respData.auth_code == 'MERCHAUTH' " >		   								 	
 											<td><s:property value="#authCommonList['MER_ID']" /> </td>   
 											<td><s:property value="#authCommonList['MER_NAME']" /> </td>
											<td><s:property value="#authCommonList['MER_ADM']" /> </td>   
 											<td><s:property value="#authCommonList['makerId']" /> </td>     
 											<td><s:property value="#authCommonList['makerDate']" /> </td>   
  											<td class='hidden-phone'><a href='#' class='label label-info' index='#authCommonStatus.index'><s:property value="#authCommonList['status']" /></a> </td> 
		   								 </s:elseif> 
		   								   <s:elseif test="#respData.auth_code == 'MODUSERAUTH' " >		   								 	
 											<td><s:property value="#authCommonList['LOGIN_USER_ID']" /> </td>   
 											<td><s:property value="#authCommonList['USER_NAME']" /> </td> 
 											<td><s:property value="#authCommonList['MAKER_ID']" /> </td>   
 											<td><s:property value="#authCommonList['MAKER_DATE']" /> </td>
 											<td><s:property value="#authCommonList['LOCATION']" /> </td>   
 											<td><s:property value="#authCommonList['OFFICE_NAME']" /> </td>      
 											<td class='hidden-phone'><a href='#' class='label label-info' index='#authCommonStatus.index'><s:property value="#authCommonList['STATUS']" /></a> </td> 
		   								 </s:elseif>
		   								  <s:elseif test="#respData.auth_code == 'USERSTATUSAUTH' " >		   								 	
 											<td><s:property value="#authCommonList['User_id']" /> </td>   
 											<td><s:property value="#authCommonList['User_Name']" /> </td> 
 											<td><s:property value="#authCommonList['Location']" /> </td>  
 											<td><s:property value="#authCommonList['MAKER_ID']" /> </td>    
 											<td><s:property value="#authCommonList['MAKER_DATE']" /> </td> 
 											<td class='hidden-phone'><a href='#' class='label label-info' index='#authCommonStatus.index'><s:property value="#authCommonList['STATUS']" /></a> </td> 
		   								 </s:elseif>  		 											  
		 								</tr> 
									 </s:iterator>
								</tbody>
							</table>
						</fieldset>
					</div>
				</div>
			</div>
			<div class="form-actions"> 
				<input type="button" class="btn btn-info" name="btn-back"
						id="btn-back" value="Back"  /> 
			
				<input type="button" class="btn btn-success" name="btn-submit"
						id="btn-submit" value="Next"  /> 
			
				<span id ="error_dlno" class="errors"></span> 
  				<input name="STATUS" type="hidden" id="STATUS" value="<s:property value="#respData.status" />" />
  				<input name="AUTH_CODE" type="hidden" id="AUTH_CODE" value="<s:property value="#respData.auth_code" />"  />
				<input type="hidden" name="REF_NO" id="REF_NO" /> 
			</div>
 	</div>
</form>
</body>
</html>
