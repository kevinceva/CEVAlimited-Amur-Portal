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
var storeList = new Array(); // merchantid_STORES

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
						 
							var usersTableSrch=(storeSearchKey+"_STORES").toUpperCase();
							var rowFetFromTab = tds[2].toUpperCase()+"_STORES";
							//console.log(rowFetFromTab+"<==>"+ usersTableSrch);
 							
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
							
							var userRightsSrc = (storeSearchKey.replace(".","_")+"_TERMINALS").toUpperCase();
							rowFetFromTab = tds[2].replace(".","_").toUpperCase()+"_TERMINALS";
						 	 
							for(var i=0;i<=terminalTables.length;i++){
								//console.log("[" +rowFetFromTab+"<==>"+ reqUser +"]");
								try {
									if(terminalTables[i] != undefined) { reqUser=terminalTables[i].toUpperCase(); }
									 
									if( reqUser == rowFetFromTab) {
										$("#"+terminalTables[i]).show();
									} else $("#"+terminalTables[i]).hide();
								} catch(e) {
									console.log(e);
								} 
							} 
								
						} else {
							
							//if(idToHide.indexOf("_STORES") != -1 ){ 
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
		var storeId ="";
		var merchantId ="";
		var terminalId ="";
		var merchantName  ="";
		var storeName  ="";
		 
		/* var index1 = $(this).attr('index'); */  
		var index1 = $(this).parent().closest('tr').attr('index');
		
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		var searchTrRows = parentId+" tr"; 
		var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
				 
		if(disabled_status == undefined) {  
			if (v_id ==  "store-create" 
					|| v_id ==  "merchant-modify"
					|| v_id ==  "merchant-view" 
					|| v_id == "merchant-terminate" 
					|| v_id ==  "store-authorize" ) {
					
					//console.log($(searchTdRow))
					 // Anchor Tag ID Should Be Equal To TR OF Index
						$(searchTdRow).each(function(indexTd) {  
							if (indexTd == 1) {
								// Frequency
								//console.log($(this).text());
								merchantId=$(this).text().trim();
							 }   if(indexTd == 2) {
								// Time or Date
								merchantName=$(this).text().trim();
							 }   if(indexTd == 3) {
								// email ids
							 }   if(indexTd == 4) {
							 }  
						}); 

						queryString += 'merchantID='+merchantId; 

						if(v_id ==  "store-create") {  
							v_action = "getStoreCreateScreenAct";
							queryString += '&merchantName='+merchantName;
						} else if(v_id ==  "merchant-modify") { 
							v_action="merchantModifyScreenAct";  
						} else if(v_id ==  "merchant-view") {  
							v_action = "merchantViewAct";
						} else if(v_id ==  "merchant-terminate") { 
							v_action="merchantTerminateScreenAct";  
						} else if(v_id ==  "store-authorize") { 
							queryString += '&status=STORE'; 
							v_action="merchantAuthorization";  
						}  

			}  else if (v_id ==  "terminal-create" 
					|| v_id ==  "store-view"
					|| v_id ==  "store-terminate" 
					|| v_id == "store-modify" 
					|| v_id ==  "terminal-authorize") {
					
					 // Anchor Tag ID Should Be Equal To TR OF Index
						$(searchTdRow).each(function(indexTd) {  
							 if (indexTd == 1) {
								// Frequency
								storeId=$(this).text().trim();
								//alert(storeId);
							 } else if(indexTd == 2) {
								// Time or Date
								storeName=$(this).text().trim();
								//alert(storeName);
							 } else if(indexTd == 3) {
								merchantId=$(this).text().trim();
							 } else if(indexTd == 4) {
								merchantName=$(this).text().trim();
							 } 
						}); 

						queryString += 'merchantID='+merchantId+'&merchantName='+merchantName+'&storeId='+storeId+'&storeName='+storeName;

						if(v_id ==  "terminal-create") {  
							v_action = "createTerminalAct";
						} else if(v_id ==  "store-view") { 
							v_action="viewStoreAct";  
						} else if(v_id ==  "store-terminate") {  
							v_action = "terminateStoreScreenAct";
						} else if(v_id ==  "store-modify") { 
							v_action="modifyStoreScreenAct";  
						} else if(v_id ==  "terminal-authorize") {
							queryString += '&status=TERM'; 
							v_action="merchantAuthorization";  
						}  

			}  else if (v_id ==  "terminal-view" 
					|| v_id ==  "terminal-terminate"
					|| v_id ==  "terminal-modify" 
					|| v_id == "terminal-assign-services"
					|| v_id ==  "terminal-assign-user" 
					|| v_id == "terminal-services-view") {
					
					 // Anchor Tag ID Should Be Equal To TR OF Index
					$(searchTdRow).each(function(indexTd) {  
						 if (indexTd == 1) {
							// Terminal
							terminalId=$(this).text().trim();
						 } else if(indexTd == 2) {
							 // Serial No
						 } else if(indexTd == 3) {
							// Time or Date
							storeId=$(this).text().trim();
						 } else if(indexTd == 4) {
							merchantId=$(this).text().trim();
						 } else if(indexTd == 5) {
							merchantName=$(this).text().trim();
						 } 
					}); 

					queryString += 'terminalID='+terminalId+'&storeId='+storeId+'&merchantID='+merchantId;

					if(v_id ==  "terminal-create") {  
						v_action = "viewTerminalScreenAct";
					} else if(v_id ==  "terminal-terminate") { 
						v_action="terminateTerminalScreenAct";  
					} else if(v_id ==  "terminal-modify") {  
						v_action = "modifyTerminalScreenAct";
					} else if(v_id ==  "terminal-assign-services") { 
						v_action="assignUserServiceScreenAct";  
					} else if(v_id ==  "terminal-assign-user") {  
						v_action = "assignUserTerminalScreenAct";
					} else if(v_id ==  "terminal-services-view") { 
						v_action="viewAssignedServicesAct";  
					}   else if(v_id ==  "terminal-view") { 
						v_action="viewTerminalScreenAct";  
					} 
					
			} else if(v_id ==  "merchant-add") { 
				v_action="getMerchantCreateScreenAct";  
			} else if(v_id ==  "merchant-dashboard") { 
				v_action="getDashboardAct";  
			} else if(v_id ==  "merchant-authorization"  ) {
				queryString +='status=MERC';
				v_action="merchantAuthorization";  
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
					<li><a href="#"> Merchant Management</a></li>
				</ul>
			</div>
			
			<div class="box-content" id="top-layer-anchor">
				 <div>
					<a href="#" class="btn btn-success" id="merchant-add"   title='Add New Merchant' data-rel='popover'  data-content='Creating a new merchant.'><i class='icon icon-plus icon-white'></i>&nbsp;Add New Merchant</a> &nbsp; 
 					<a href="#" class="btn btn-primary" id="merchant-dashboard"   title='Dashboard' data-rel='popover'  data-content='Viewing the Merchants,Stores & Terminals.'><i class='icon icon-users icon-white'></i>&nbsp;Merchant Dashboard</a> &nbsp;
 				 </div>	
			</div>
								  
           <div class="row-fluid sortable"><!--/span-->
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>Merchant Information
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content"> 
					<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable"  
						id="DataTables_Table_0" >
						<thead>
							<tr>
								<th>S No</th>
								<th>Merchant ID</th>
								<th class="hidden-phone">Merchant Name </th>
								<th>Status</th>
								<th class="hidden-phone">Date Created</th>
								<th>Actions</th>
							</tr>
						</thead> 
						<tbody id="merchantTBody">
							<s:iterator value="responseJSON['MERCHANT_LIST']" var="userGroups" status="userStatus"> 
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
 													storeDetails += '<s:property value="value" />_STORES';
 												} else {
 													storeDetails += ',<s:property value="value" />_STORES';
 												} 
 											</script>
										</s:if>
										 <s:elseif test="#status.index == 1" >
											 <td class='hidden-phone'><s:property value="value"  /></td>
										 </s:elseif> 
										  <s:elseif test="#status.index == 2" >
												<s:set var="merchstatus" value="value" />
												
												<s:if test="#merchstatus == 'Active'" > 
													<s:set value="%{'false'}" var="labelstatus" scope="page"/>
													 <s:set value="%{'label-success'}" var="merchlabel" />
													 <s:set value="%{'btn btn-danger'}" var="statusclass" /> 
													 <s:set value="%{'Deactivate'}" var="text1" /> 
													 <s:set value="%{'icon-unlocked'}" var="text" /> 
												</s:if>
												<s:elseif test="#merchstatus == 'Inactive'" >
													<s:set value="%{'false'}" var="labelstatus" scope="page"/>
													 <s:set value="%{'label-warning'}" var="merchlabel" />
													 <s:set value="%{'btn btn-success'}" var="statusclass" /> 
													 <s:set value="%{'Activate'}" var="text1" /> 
													 <s:set value="%{'icon-locked'}" var="text" />
												 </s:elseif>
											 	<s:elseif test="#merchstatus == 'Un-Authorize'" >
												 	<s:set value="%{'true'}" var="labelstatus" scope="page"/>
													 <s:set value="%{'label-primary'}" var="merchlabel" />
													 <s:set value="%{' '}" var="statusclass" /> 
													 <s:set value="%{' '}" var="text1" /> 
													 <s:set value="%{' '}" var="text" />
												 </s:elseif> 
												 
											<td><a href='#' class='label <s:property value="#merchlabel" />' index="<s:property value='#userInDetStatus1.index' />" ><s:property value="value" /></a></td>												
 										 </s:elseif> 
 										 <s:elseif test="#status.index == 3" >
											 <td class='hidden-phone'><s:property value="value" /></td>
										 </s:elseif>
									</s:iterator> 
									<td><a id='store-create' class='btn btn-success' href='#' index="<s:property value='#userInDetStatus1.index' />" title='Create Store' data-rel='tooltip'><i class='icon icon-plus icon-white'></i></a>&nbsp;
										<a id='merchant-modify' class='btn btn-warning' href='#' index="<s:property value='#userInDetStatus1.index' />" title='Merchant Modify' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;
										<a id='merchant-view' class='btn btn-info' href='#' index="<s:property value='#userInDetStatus1.index' />" title='View' data-rel='tooltip'><i class='icon icon-note icon-white'></i></a>&nbsp;
										 
										<s:if test="#attr.labelstatus != 'true'" > 
											<a id='merchant-terminate' class='btn <s:property value="#statusclass" />' href='#' index="<s:property value='#userInDetStatus1.index' />" title='<s:property value="#text1" />' data-rel='tooltip' ><i class='icon <s:property value="#text" /> icon-white'></i></a>&nbsp; 
										</s:if>
										
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
		<s:set value="responseJSON" var="respData"/> 
		<s:set value="%{'_STORES'}" var="searchKey"/> 
		
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
					<div class="box-header well" data-original-title>Store Information
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content" id='UserTable<s:property value="#userInDetStatus.index+1" />' > 
						<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;' width='100%'   
							class='table table-striped table-bordered bootstrap-datatable datatable' 
							id='DataTables_Table_S_<s:property value="#userInDetStatus.index+1" />'>
							<thead>
								<tr>
									<th>S No</th>
									<th>Store ID</th>
									<th class='hidden-phone'>Store Name </th>
									<th class='center hidden-phone'>Merchant ID</th>
									<th>Status </th>
									<th class='hidden-phone'>Date Created</th>
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
											<td><a href='#' id='SEARCH_NO' value='<s:property value="#userInDetails1['storeId']" />' ><s:property value="#userInDetails1['storeId']" /></a></td>  
												<script type="text/javascript"> 
 													terminalDetails += '<s:property value="#userInDetails1['storeId']" />_TERMINALS'+","; 
	 											</script>
											<td class='hidden-phone'><s:property value="#userInDetails1['storeName']" /></td> 
											<td class='center hidden-phone'><s:property value="#userInDetails1['merchantID']" /></td> 
													<s:if test="#userInDetails1['status'] == 'Active'" > 
														 <s:set value="%{'label-success'}" var="merchStatus" />
														 <s:set value="%{'btn btn-danger'}" var="statusclass"/> 
														 <s:set value="%{'Activate'}" var="text1"/> 
														 <s:set value="%{'icon-unlocked'}" var="text" /> 
													</s:if>
													<s:elseif test="#userInDetails1['status'] == 'Inactive'" >
														  <s:set value="%{'label-warning'}" var="merchStatus" />
														 <s:set value="%{'btn btn-success'}" var="statusclass"/> 
														 <s:set value="%{'Activate'}" var="text1"/> 
														 <s:set value="%{'icon-locked'}" var="text"/>
													 </s:elseif>
													 <s:elseif test="#userInDetails1['status'] == 'Un-Authorize'" >
														  <s:set value="%{'label-primary'}" var="merchStatus" />
														 <s:set value="%{' '}" var="statusclass"/> 
														 <s:set value="%{' '}" var="text1"/> 
														 <s:set value="%{' '}" var="text"/>
													 </s:elseif> 
											<td><a href='#' class='label <s:property value="#merchStatus" />' index="<s:property value='#userInDetStatus1.index' />" ><s:property value="#userInDetails1['status']" /></a></td>									  
											<td class='hidden-phone'><s:property value="#userInDetails1['makerDate']" /></td> 
											<td>
												<a class='btn btn-success' href='#' id='terminal-create'  index="<s:property value='#userInDetStatus1.index' />" title='Create Terminal' data-rel='tooltip' ><i class='icon icon-plus icon-white'></i></a>&nbsp;
												<a class='btn btn-warning' href='#' id='store-modify' index="<s:property value='#userInDetStatus1.index' />" title='Modify' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;
												<a class='btn btn-info' href='#' id='store-view'  index="<s:property value='#userInDetStatus1.index' />" title='View' data-rel='tooltip'><i class='icon icon-page icon-white'></i></a>&nbsp;																								
												
												<s:if test="#userInDetails1['status'] != 'Un-Authorize'" >
												 	<a class='btn <s:property value="#statusclass" />' href='#' id='store-terminate' index="<s:property value='#userInDetStatus1.index' />" title='<s:property value="#text1" />' data-rel='tooltip'> <i class='icon <s:property value="#text" /> icon-white'></i> </a>&nbsp;
												 </s:if> 
												
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
		<script type="text/javascript"> 
			  $(document).ready(function () {
				  terminalDetails = terminalDetails.split(",");
					$.each(terminalDetails, function(indexLink, val) {
						if(val != "")
						terminalTables[indexLink]=val;	 
					});  
			  }); 
		</script>
		<s:set value="terminalJSON" var="respData"/> 
		<s:set value="%{'_TERMINALS'}" var="searchKey"/> 
		
		<s:bean name="com.ceva.base.common.bean.JsonDataToObject" var="jsonToList">
		  	<s:param name="jsonData" value="#respData"/> 
			<s:param name="searchData" value="#searchKey"/>  
		</s:bean>
		
		<!-- Below Is For Setting Terminals...... -->
		
		<div id="terminals">
			<s:set value="#jsonToList.data" var="userDetails" />  
		 	<s:iterator value="#userDetails" var="userInDetails" status="userInDetStatus"> 
             <div class="row-fluid sortable" id='<s:property  value="key" />'  style="display:none">
				<div class="box span12">
					<div class="box-header well" data-original-title>Terminal Information
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content" id='UserTable<s:property value="#userInDetStatus.index+1" />' > 
						<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;' width='100%'   
							class='table table-striped table-bordered bootstrap-datatable datatable' 
							id='DataTables_Table_T_<s:property value="#userInDetStatus.index+1" />'>
							<thead>
								<tr>
									<th>S No</th>
									<th>Terminal ID</th>
									<th>Serial No</th>
									<th class='hidden-phone'>Store ID </th>
									<th class='center hidden-phone'>Merchant ID</th>
									<th>Status</th>
									<th class='hidden-phone'>Date Created</th>
									<th>Actions</th>
								</tr>
							</thead> 
							<tbody id="TerminalTable<s:property  value="#userInDetStatus.index+1" />"> 
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
											<td><s:property value="#userInDetails1['terminalID']" /></td>  
											<td><s:property value="#userInDetails1['serialNo']" /></td> 
											<td class='hidden-phone'><s:property value="#userInDetails1['storeId']" /></td> 
											<td class='center hidden-phone'><s:property value="#userInDetails1['merchantID']" /></td> 
												<s:if test="#userInDetails1['status'] == 'Active'" > 
													 <s:set value="%{'label-success'}" var="merchStatus" />
													 <s:set value="%{'btn btn-danger'}" var="statusclass"/> 
													 <s:set value="%{'Activate'}" var="text1"/> 
													 <s:set value="%{'icon-unlocked'}" var="text" /> 
												</s:if>
												<s:elseif test="#userInDetails1['status'] == 'Deactive'" >
													  <s:set value="%{'label-warning'}" var="merchStatus" />
													 <s:set value="%{'btn btn-success'}" var="statusclass"/> 
													 <s:set value="%{'Activate'}" var="text1"/> 
													 <s:set value="%{'icon-locked'}" var="text"/>
												 </s:elseif>
												 <s:elseif test="#userInDetails1['status'] == 'Un-Authorize'" >
													  <s:set value="%{'label-primary'}" var="merchStatus" />
													 <s:set value="%{' '}" var="statusclass"/> 
													 <s:set value="%{' '}" var="text1"/> 
													 <s:set value="%{' '}" var="text"/>
												 </s:elseif> 
											<td><a href='#' class='label <s:property value="#merchStatus" />' index="<s:property value='#userInDetStatus1.index' />" ><s:property value="#userInDetails1['status']" /></a></td>									  
											<td class='hidden-phone'><s:property value="#userInDetails1['makerDate']" /></td> 
											<td>
												<p>
													<a class='btn btn-success' href='#' id='terminal-assign-services' index="<s:property value='#userInDetStatus1.index' />" title='Assign Services' data-rel='tooltip'><i class='icon icon-plus icon-white'></i></a>&nbsp;
													<a class='btn btn-info' href='#' id='terminal-services-view' index="<s:property value='#userInDetStatus1.index' />" title='View Services' data-rel='tooltip'><i class='icon icon-page icon-white'></i></a>&nbsp;
													<a class='btn btn-success' href='#' id='terminal-assign-user' index="<s:property value='#userInDetStatus1.index' />" title='Assign Users' data-rel='tooltip'><i class='icon icon-star-on icon-white'></i></a>
												</p>
												<p>	<a class='btn btn-warning' href='#' id='terminal-modify' index="<s:property value='#userInDetStatus1.index' />" title='Modify' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;
													<a class='btn btn-info' href='#' 	id='terminal-view' 	index="<s:property value='#userInDetStatus1.index' />" title='View' data-rel='tooltip'><i class='icon icon-note icon-white'></i></a>&nbsp;
													<s:if test="#userInDetails1['status'] != 'Un-Authorize'" >
														<a class='btn <s:property value="#statusclass" />' href='#' id='terminal-terminate' index="<s:property value='#userInDetStatus1.index' />" title='<s:property value="#text1" />' data-rel='tooltip'><i class='icon <s:property value="#text" /> icon-white'></i></a>
													</s:if> 
												</p> 
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
	 
	<div  id="users"> 
	</div> 
</div>  
</form>
<form name="form2" id="form2" method="post">

</form>	
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
