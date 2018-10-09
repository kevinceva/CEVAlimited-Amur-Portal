<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title> </title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
	
	 

<style type="text/css">
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
label.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
.errmsg {
color: red;
}
 
</style>    
<script type="text/javascript" >
function postData(actionName,paramString){
	$('#form2').attr("action", actionName)
			.attr("method", "post");
	
	var paramArray = paramString.split("&");
	var input = "" ;
	$(paramArray).each(function(indexTd,val) {
		if(val != "") {
			input = $("<input />").attr("type", "hidden").attr("name", val.split("=")[0]).val(val.split("=")[1].trim());
			$('form').append($(input));	 
		}
	});	

	$('form').submit();	
} 
var storeDetails	= "";
var terminalDetails	= "";

var userLinkData ='${USER_LINKS}';
var jsonLinks = jQuery.parseJSON(userLinkData);
var linkIndex = new Array();
var linkName = new Array();
var linkStatus = new Array();
var terminalTables=new Array(); //  store_TERMINALS;
var storeList = new Array(); // merchantid_ACCOUNTS

$(document).ready(function () { 
		
		//console.log(userGroupData);
		$.each(jsonLinks, function(index, v) {
			linkIndex[index] = index;
			linkName[index] = v.name;
			linkStatus[index] = v.status;
		}); 
		
		$("input[type=text]").live('keyup',function() {
			var tabData=$(this).attr('aria-controls');
			var storeSearchKey = $(this).val(); 
			var noOfRows=$('#'+tabData+' tbody tr').length;
			var tds = new Array();
 			var reqUser = "";
			//var idToHide = $(this).parents().eq(7).attr('id');
			
			//console.log("TabData ["+$(this).parents().eq(7).attr('id')+"]");
			 
			var merchantIdSearchKey = $("#DataTables_Table_0_filter >label >input").attr('value').toUpperCase(); 
			// The below code is to check the main textbox 
 			if(merchantIdSearchKey.length == 0 ) {
				
				for(var i=0;i<storeList.length;i++){
					reqUser=storeList[i];
					$("#"+reqUser).hide();
				} 
				for(var i=0;i<terminalTables.length;i++){
					reqUser=terminalTables[i];
					$("#"+reqUser).hide();
				} 
			} else {
				// Checking the text other than master text box is equal to zero or not.
				 if(storeSearchKey.length == 0) { 
					for(var i=0;i<terminalTables.length;i++){
						reqUser=terminalTables[i];
						$("#"+reqUser).hide();
					} 
				} else {   
						// Checking the Dependency of the master in the child Dashboard.
						if(noOfRows == 1 ) {
							var index=1;
							$('#'+tabData+' tbody tr').each(function () {
								$('td',this).each(function (){
								 tds[index]=$(this).text().trim();
								 index++;
								});
							});  
						 
							var usersTableSrch=(storeSearchKey+"_ACCOUNTS").toUpperCase();
							var rowFetFromTab = tds[2].toUpperCase()+"_ACCOUNTS";
 							
							for(var i=0;i<storeList.length;i++) { 
								try {
									reqUser = storeList[i].toUpperCase(); 
									//console.log("[" +reqUser+"<==>"+ usersTableSrch +"] [" +rowFetFromTab+"<==>"+ reqUser+"]");
									 
									if( reqUser == usersTableSrch 
										|| rowFetFromTab == reqUser) {
										$("#"+storeList[i]).show();
									} else {
										//TODO: Here to hide the current Table GRID.
										//$("#"+storeList[i]).hide();
									}
								} catch(e) {
									console.log(e);
								} 
								
							} 
							
								
						} else {
							
							//if(idToHide.indexOf("_ACCOUNTS") != -1 ){ 
								for(var i=0;i<terminalTables.length;i++){
									reqUser=terminalTables[i];
									$("#"+reqUser).hide();
								}  
							/*} else {
								
							}*/
						}
					}  
			 }
		});  
		
		// Search For Top Layer
		$('#top-layer-anchor').find('a').each(function(index) {
			var anchor = $(this);   
			var flagToDo = false;
			 
			$.each(linkIndex, function(indexLink, v) {	 
			//console.log(linkName[indexLink] +" === "+ anchor.attr('id') +" >" + (linkName[indexLink] == anchor.attr('id')));
				if(linkName[indexLink] == anchor.attr('id'))  {
					flagToDo = true;
				} 
			}); 
			if(!flagToDo) {
				anchor.attr("disabled","disabled");
			} else {
				anchor.removeAttr("disabled");
			} 
		});
		
		//Search For The Links That Are Assigned To TABLE Level
		 $('table > tbody').find('a').each(function(index) {
			var anchor = $(this);   
			var flagToDo = false;
			 
			$.each(linkIndex, function(indexLink, v) {	 
			//console.log(linkName[indexLink] +" === "+ anchor.attr('id') +" >" + (linkName[indexLink] == anchor.attr('id')));
				if(linkName[indexLink] == anchor.attr('id'))  {
					flagToDo = true;
				} 
			}); 
			if(!flagToDo) {
				anchor.attr("disabled","disabled");
			} else {
				anchor.removeAttr("disabled");
			} 
			 
		});  
}); 

$(document).on('click','a',function(event) {
	var v_id=$(this).attr('id');
	
	if(v_id != 'SEARCH_NO') {
		var disabled_status= $(this).attr('disabled'); 
		var queryString = ''; 
		var v_action = "NO";
		var accountNumner  ="";
		var billerCode = "";
		/* var index1 = $(this).attr('index'); */  
		var index1 = $(this).parent().closest('tr').attr('index');
		
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		var searchTrRows = parentId+" tr"; 
		var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
				 
		if(disabled_status == undefined) {  
			if (v_id ==  "biller-account-create" 
					|| v_id ==  "biller-modify"
					|| v_id ==  "biller-view" ) {
					
					//console.log($(searchTdRow))
					 // Anchor Tag ID Should Be Equal To TR OF Index
						$(searchTdRow).each(function(indexTd) {  
							if (indexTd == 1) {
								// Frequency
								//console.log($(this).text());
								billerCode=$(this).text().trim();
							 }   if(indexTd == 2) {
							 }   if(indexTd == 3) {
								// email ids
							 }   if(indexTd == 4) {
							 }  
						}); 

						queryString += '&billerCode='+billerCode;
						
						if(v_id ==  "biller-account-create") {  
							v_action = "createBillerAccountAct";
						} else if(v_id ==  "biller-modify") { 
							v_action="modifyBillerAct";  
						} else if(v_id ==  "biller-view") {  
							v_action = "billerViewAct";
						} 

			}  else if (v_id ==  "biller-account-modify" 
					|| v_id ==  "biller-account-view" ) {
					
					 // Anchor Tag ID Should Be Equal To TR OF Index
						$(searchTdRow).each(function(indexTd) {  
							 if (indexTd == 1) {
								// Frequency
								billerCode=$(this).text().trim();
								//alert(storeId);
							 } else if(indexTd == 2) {
								// Time or Date
								accountNumner=$(this).text().trim();
								//alert(storeName);
							 } else if(indexTd == 3) {
							 } else if(indexTd == 4) {
							 } 
						}); 

						queryString += 'billerCode='+billerCode+'&accountNumber='+accountNumner;
						if(v_id ==  "biller-account-view") { 
							v_action="billerAccountViewAct";  
						} else if(v_id == "biller-account-modify"){
							v_action="billerAccountModifyAct";  
						}

			} else if(v_id ==  "biller-add") { 
				v_action="addNewBillerAct";  
			} else if(v_id ==  "biller-transaction-history") { 
				v_action="billerTransactionHistoryAct";  
			}  
			
		} else { 
			// No Rights To Access The Link 
		}
		
		if(v_action != "NO") {
			postData(v_action+".action",queryString);
			//$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/"+v_action+".action"+queryString;
			//$("#form1").submit();
		}
	} else {
		// The below code is for quick searching.
		var txt_sr = $(this).attr('value');
		var parentId =$(this).parent().closest('table').attr('id');  
		
		$('div input[type=text]').each(function(){
			if($(this).attr("aria-controls") == parentId) { 
				$(this).val(txt_sr);
				$(this).trigger("keyup");
			}  
		});
	}
}); 
</script> 
		
</head>

<body>
	<form name="form1" id="form1" method="post" action="">	
	<!-- topbar ends --> 
	<div id="form1-content" class="span10">   
		 
			  <div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#"> Pay Bill</a></li>
				</ul>
			</div>
			
			<div class="box-content" id="top-layer-anchor">
				 <div>
					<a href="#" class="btn btn-success" id="biller-add"   title='Add New Biller' data-rel='popover'  data-content='Creating a new merchant.'><i class='icon icon-plus icon-white'></i>&nbsp;Add New Biller</a> &nbsp; 
					<a href="#" class="btn btn-success" id="biller-transaction-history"   title='Transaction History' data-rel='popover'  data-content='View Pay bill Transactions.'><i class='icon icon-minus icon-white'></i>&nbsp;Transaction History</a> &nbsp;
 				 </div>	
			</div>
								  
           <div class="row-fluid sortable"><!--/span-->
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>Biller Information
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content"> 
					<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable"  	id="DataTables_Table_0" >
						<thead>
							<tr>
								<th>S No</th>
								<th>Biller ID</th>
								<th class="hidden-phone">Biller Name </th>
								<th class="hidden-phone">Abbreviation </th>
								<th class="hidden-phone">Commission Type </th>
								<th class="hidden-phone">Amount/Rate</th>
								<th>Created By</th>
								<th class="hidden-phone">Date Created</th>
								<th>Actions</th>
							</tr>
						</thead> 
						<tbody id="merchantTBody">
							<s:iterator value="responseJSON1['BILLER_LIST']" var="userGroups" status="userStatus"> 
								<s:if test="#userStatus.even == true" > 
									<tr class="even" index="<s:property value='#userStatus.index' />"  id="<s:property value='#userStatus.index' />">
								 </s:if>
								 <s:elseif test="#userStatus.odd == true">
      								<tr class="odd" index="<s:property value='#userStatus.index' />"  id="<s:property value='#userStatus.index' />">
   								 </s:elseif> 
									<td><s:property value="#userStatus.index+1" /></td>
									<!-- Iterating TD'S -->
									<s:iterator value="#userGroups" status="status" > 
										<s:if test="#status.index == 0" >  
											<td> <a href='#' id='SEARCH_NO' value='<s:property value="value" />'><s:property value="value" /></a></td> 
											<script type="text/javascript">
  												if(<s:property value='#userStatus.index' /> == 0){
 													storeDetails += '<s:property value="value" />_ACCOUNTS';
 												} else {
 													storeDetails += ',<s:property value="value" />_ACCOUNTS';
 												} 
 											</script>
										</s:if>
										 <s:elseif test="#status.index == 1" >
											 <td class='hidden-phone'><s:property value="value"  /></td>
										 </s:elseif> 
										  <s:elseif test="#status.index == 2" >
												<s:set var="merchstatus" value="value" />
												 
											<td><s:property value="value" /></td>												
 										 </s:elseif> 
 										 <s:elseif test="#status.index == 3" >
											 <td class='hidden-phone'><s:property value="value" /></td>
										 </s:elseif>
										 <s:elseif test="#status.index == 4" >
											 <td class='hidden-phone'><s:property value="value" /></td>
										 </s:elseif>
										 <s:elseif test="#status.index == 5" >
											 <td class='hidden-phone'><s:property value="value" /></td>
										 </s:elseif>
										 <s:elseif test="#status.index == 6" >
											 <td class='hidden-phone'><s:property value="value" /></td>
										 </s:elseif>
									</s:iterator> 
									<td><a id='biller-account-create' class='btn btn-success' href='#' index="<s:property value='#userInDetStatus1.index' />" title='Create Biller Account' data-rel='tooltip'><i class='icon icon-plus icon-white'></i></a>&nbsp;
										<a id='biller-modify' class='btn btn-warning' href='#' index="<s:property value='#userInDetStatus1.index' />" title='Biller Modify' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;
										<a id='biller-view' class='btn btn-info' href='#' index="<s:property value='#userInDetStatus1.index' />" title='Biller View' data-rel='tooltip'><i class='icon icon-note icon-white'></i></a>&nbsp;
										
									</td>
 								</tr> 
							</s:iterator>
						</tbody>
					</table>
				</div>
			</div>
		</div> 
		<script type="text/javascript"> 
			  $(document).ready(function () {
				  storeDetails = storeDetails.split(",");
					$.each(storeDetails, function(indexLink, val) {	
						 storeList[indexLink]=val;	 
					}); 					
			  }); 
		</script>
		<s:set value="responseJSON1" var="respData"/> 
		<s:set value="%{'_ACCOUNTS'}" var="searchKey"/> 
		
		<s:bean name="com.ceva.base.common.bean.JsonDataToObject" var="jsonToList">
		  	<s:param name="jsonData" value="#respData"/>  
 		    <s:param name="searchData" value="#searchKey"/>  
		</s:bean>
		<!-- Loading Stores -->
	 	<div  id="stores">  
	 		<s:set value="#jsonToList.data" var="userDetails" />  
		 	<s:iterator value="#userDetails" var="userInDetails" status="userInDetStatus"> 
             <div class="row-fluid sortable" id='<s:property  value="key" />' style="display:none">
				<div class="box span12">
					<div class="box-header well" data-original-title>Biller Account Information
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content" id='UserTable<s:property value="#userInDetStatus.index+1" />' > 
						<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;' width='100%'   	class='table table-striped table-bordered bootstrap-datatable datatable' 	id='DataTables_Table_S_<s:property value="#userInDetStatus.index+1" />'>
							<thead>
								<tr>
									<th>S No</th>
									<th>Biller ID</th>
									<th class='hidden-phone'>Account Number</th>
									<th class='center hidden-phone'>Account Name</th>
									<th>Created By </th>
									<th class='hidden-phone'>Created Date</th>
									<th>Actions</th>
								</tr>
							</thead> 
							<tbody id="terminalTBody<s:property  value="#userInDetStatus.index+1" />"> 
			                      	<s:set value="value" var="arrayData"/> 
			                      	<s:iterator value="#arrayData" var="userInDetails1" status="userInDetStatus1">
			                      		<!--  Starting iterator for Store DETAILS  --> 
									 
										<s:if test="#userDetStatus.even == true" > 
											<s:set value="%{'even'}" var="flag"/> 
 										 </s:if>
										 <s:elseif test="#userStatus.odd == true">
		      								<s:set value="%{'odd'}" var="flag"/> 	
		   								 </s:elseif> 
		   								 	<tr class="<s:property value='#flag' />" index="<s:property value='#userInDetStatus1.index' />"  id="<s:property value='#userInDetStatus1.index' />">
											<td><s:property value="#userInDetStatus1.index+1" /></td> 
											<td><s:property value="#userInDetails1['BILLER_ID']" /></td>  
											<td class='hidden-phone'><s:property value="#userInDetails1['ACCOUNT_NUMBER']" /></td> 
											<td class='center hidden-phone'><s:property value="#userInDetails1['ACCOUNT_NAME']" /></td> 
											<td class='center hidden-phone'><s:property value="#userInDetails1['MAKER_ID']" /></td> 
											<td class='center hidden-phone'><s:property value="#userInDetails1['MAKER_DATE']" /></td> 
											<td>
												<a id='biller-account-modify' class='btn btn-warning' href='#' index="<s:property value='#userInDetStatus1.index' />" title='Biller Account Modify' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;
												<a class='btn btn-info' href='#' id='biller-account-view'  index="<s:property value='#userInDetStatus1.index' />" title='View' data-rel='tooltip'><i class='icon icon-page icon-white'></i></a>&nbsp;																								
												
											</td> 
									</tr>
								</s:iterator> 
		                    </tbody>
						</table>
					</div>
				</div>
			</div>  
		 	</s:iterator> 
		</div>
	
	</div>  
</form>
<form name="form2" id="form2" method="post">

</form>	
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
