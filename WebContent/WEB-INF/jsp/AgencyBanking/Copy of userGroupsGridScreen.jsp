<!DOCTYPE html>
<html lang="en">
<head>
<%@taglib uri="/struts-tags" prefix="s"%> 
<meta charset="utf-8">
<title> </title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
 
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
	var userDetails = "";
	var userRights = "";
	var usersList = new Array();
 	var userTableRights=new Array(); 
  
	var userLinkData ='${USER_LINKS}';
	var jsonLinks = jQuery.parseJSON(userLinkData);
	var linkIndex = new Array();
	var linkName = new Array();
	var linkStatus = new Array();
	
	function postData(actionName,paramString) {
		$('#form2').attr("action", actionName)
				.attr("method", "post");
		
		var paramArray = paramString.split("&");
		 
		$(paramArray).each(function(indexTd,val) {
			var input = $("<input />").attr("type", "hidden").attr("name", val.split("=")[0]).val(val.split("=")[1].trim());
			$('#form2').append($(input));	 
		}); 
		$('#form2').submit();	
	}
	
    $(document).ready(function () { 
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
		 
			var merchantIdSearchKey = $("#DataTables_Table_0_filter >label >input").attr('value').toUpperCase(); 
			// The below code is to check the main textbox 
 			if(merchantIdSearchKey.length == 0 ) {
				
				for(var i=0;i<usersList.length;i++){
					reqUser=usersList[i];
					$("#"+reqUser).hide();
				} 
				for(var i=0;i<userTableRights.length;i++){
					reqUser=userTableRights[i];
					$("#"+reqUser).hide();
				} 
			} else {
				// Checking the text other than master text box is equal to zero or not.
				 if(storeSearchKey.length == 0) { 
					for(var i=0;i<userTableRights.length;i++){
						reqUser=userTableRights[i];
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
						 
							var usersTableSrch=(storeSearchKey+"_USERS").toUpperCase();
							var rowFetFromTab = tds[2].toUpperCase()+"_USERS";
							 
							for(var i=0;i<usersList.length;i++) {
								reqUser = usersList[i].toUpperCase(); 
 								 
								if( reqUser == usersTableSrch 
									|| rowFetFromTab == reqUser) {
									$("#"+usersList[i]).show();
								} else {
									 
								}
							} 
							
							var userRightsSrc = (storeSearchKey.replace(".","_")+"_RIGHTS").toUpperCase();
							rowFetFromTab = tds[3].replace(".","_").toUpperCase()+"_RIGHTS";
						  
							for(var i=0;i<=userTableRights.length;i++){
								if(userTableRights[i] != undefined) { reqUser=userTableRights[i].toUpperCase(); }
								 
								if( reqUser == rowFetFromTab) {
									$("#"+userTableRights[i]).show();
								} else $("#"+userTableRights[i]).hide();
							} 
								
						} else {
							
							//if(idToHide.indexOf("_USERS") != -1 ){ 
								for(var i=0;i<userTableRights.length;i++){
									reqUser=userTableRights[i];
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
		var queryString = 'entity=${loginEntity}'; 
		var v_action = "NO";
		
		var groupId = "";  
		var userId = "";   
		 
		var index1 = $(this).attr('index');  
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		var searchTrRows = parentId+" tr"; 
		var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
				 
		if(disabled_status == undefined) {  
			if( v_id == "group-creation") { 
				v_action = "userGrpCreationNew";  
			} else if (v_id ==  "create-user" 
					|| v_id ==  "view-group" 
					|| v_id ==  "modify-group"
					|| v_id ==  "user-authorize"				) { 
				 
				 // Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {  
					if(indexTd == 1) {
						groupId = $(this).text(); 
					}
				}); 

				if(v_id ==  "create-user") {  
					v_action = "getICTAdminCreatePage";
					queryString += '&type=create'; 
				} else if(v_id ==  "view-group") { 
					v_action="viewUserGroup";  
					queryString += '&type=View'; 
				} else if(v_id ==  "modify-group") { 
					v_action="viewUserGroup";  
					queryString += '&type=Modify'; 
				} else if(v_id ==  "user-authorize") { 
					v_action="getUnAuthUsersAct";  
				} 
				
				queryString += '&groupID='+groupId;  
			}  else if (v_id ==  "view-user" 
						|| v_id ==  "modify-user" 
						|| v_id ==  "activ-deactiv-user" 
						|| v_id ==  "password-reset"
						|| v_id == "assign-dashboard-user"  
						|| v_id == "assign-report-user") {  
				
				// Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {   
					if(indexTd == 1) {
						groupId = $(this).text(); 
					}
					if(indexTd == 2) {
						userId = $(this).text(); 
					}
				}); 
			 
				queryString += '&groupID='+groupId+'&userId='+userId;
				
				if(v_id ==  "view-user") { 
					queryString+='&type=View'; 
				} else if(v_id ==  "modify-user") { 
					queryString+='&type=Modify'; 
				} else if(v_id ==  "activ-deactiv-user") { 
					queryString+='&type=ActiveDeactive';  
				} else if(v_id ==  "password-reset") { 
					queryString+='&type=PasswordReset';   
				} else if(v_id ==  "assign-dashboard-user") {  
					queryString += '&roleGroupId='+groupId;  
				}  else if(v_id ==  "assign-report-user") {  
					//queryString += '&groupId='+groupId;  
				}   
				
				if(v_id ==  "assign-dashboard-user") {
					v_action="assignDashBoardLinks";
				} else if(v_id == "assign-report-user"){
					v_action="userassignedreports";
				}else {
					v_action="userInformation";
				}
			 
			} else if (v_id ==  "modify-user-rights" 
						|| v_id ==  "view-user-rights" ) { 
				   
				 // Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {  
					 if (indexTd == 1) {
						userId=$(this).text();
					 }  
				}); 

				queryString += '&userId='+userId;  
				
				if(v_id ==  "view-user-rights") { queryString += '&type=View'; }
				else {queryString += '&type=Modify'; }
				
				v_action="modifyUserAccessRights"; 
			
			} else if (v_id ==  "dashboard") { 
				queryString+="&pid=7";  
				v_action="dashboardUsers";
			}  
		} else {
		
			// No Rights To Access The Link 
		}
		//console.log(queryString);
		if(v_action != "NO") {
			 postData(v_action+".action",queryString);
		
			//$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/"+v_action+".action"+queryString;
			//$("#form1").submit();
		}
	} else {
	
		// The below code is for quick searching.
		var txt_sr = $(this).attr('value');
		var parentId =$(this).parent().closest('table').attr('id'); 
		//console.log("The val ["+txt_sr+"] " ); 
		
		$('div input[type=text]').each(function(){
			//console.log("["+$(this).attr("aria-controls") +"] == ["+ parentId+"]");
			if($(this).attr("aria-controls") == parentId) {
				//console.log("Val ["+$(this).text()+"]"); 
				//$(this).val('');
				$(this).val(txt_sr);
				$(this).trigger("keyup");
			} /*else {
				if(parentId !='DataTables_Table_0') {
					$(this).val('');
				} 
					
			}*/
		});
	}
}); 

</script>

 
</head>

<body>
<form name="form1" id="form1" method="post" >	
	<!-- topbar ends --> 
	<div id="form1-content" class="span10">   
			 
		<!-- content starts -->
		  <div>
			 <ul class="breadcrumb">
				<li><a href="home.action">Home </a> <span class="divider"> &gt;&gt; </span></li>
				<li><a href="#">User Management</a></li>
			</ul>
		 </div>
		<div class="box-content" id="top-layer-anchor">
			<span>
				<a href="#" class="btn btn-info" id="group-creation" title='New Group Creation' data-rel='popover'  data-content='Creating a new group.'>
				<i class="icon icon-web icon-white"></i>&nbsp;New Group Creation</a> &nbsp;							
			</span>
			<span>
				<a href="#" class="btn btn-warning" id="dashboard" title='View Users' data-rel='popover'  data-content='Viewing the registered users.'>
				<i class="icon icon-briefcase icon-white"></i>&nbsp;View Users</a> &nbsp; 
			</span> 			 
		</div> 
						  
		<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>Group Information
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
								<th>Group ID</th>
								<th>Group Name </th>
 								<th class="hidden-phone">Authorized ID Creator</th>
								<th class="center hidden-phone">Creation Date</th>
								<th>Actions</th>
							</tr>
						</thead> 
						<tbody id="userGroupTBody">
							<s:iterator value="responseJSON['USER_GROUPS']" var="userGroups" status="userStatus"> 
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
 													userDetails += '<s:property value="value" />_USERS';
 												} else {
 													userDetails += ',<s:property value="value" />_USERS';
 												} 
 											</script>
										</s:if>
										 <s:elseif test="#status.index == 2" >
											 <td class='hidden-phone'><s:property value="value" /></td>
										 </s:elseif>
										  <s:elseif test="#status.index == 3" >
											 <td class='center hidden-phone'><s:property value="value" /></td>
										 </s:elseif>
										  <s:else>
												<td><s:property value="value" /></td>
										 </s:else> 
									</s:iterator>  
									<td class="center" >
										<a id="create-user" class="btn btn-info"  href="#" index="<s:property value='#userStatus.index' />" title="Create User" data-rel="tooltip" ><i class="icon icon-plus icon-white"></i></a>&nbsp;
										<a id="modify-group"  class="btn btn-warning" href="#" index="<s:property value='#userStatus.index' />"  title="Modify Group" data-rel="tooltip" ><i class="icon icon-edit icon-white"></i></a> &nbsp;&nbsp;
										<a id="view-group" class="btn btn-success" index="<s:property value='#userStatus.index' />"  href="#" title="View Group" data-rel="tooltip" ><i class="icon icon-book icon-white"></i></a>&nbsp;
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
				  userDetails = userDetails.split(",");
					$.each(userDetails, function(indexLink, val) {	
						 usersList[indexLink]=val.replace(".","_");	 
					}); 
			  }); 
		</script>
		<s:set value="responseJSON" var="respData"/> 
		<s:set value="%{'_USERS'}" var="searchKey"/> 
		
		<s:bean name="com.ceva.base.common.bean.JsonDataToObject" var="jsonToList">
		  	<s:param name="jsonData" value="#respData"/> 
 		   <!--  <s:param name="searchData" value="%{'_RIGHTS','_USERS'}" />  userTableRights --> 
 		     <s:param name="searchData" value="#searchKey"/>  
		</s:bean>
	 
	 	<div  id="users-grp">  
	 		<s:set value="#jsonToList.data" var="userDetails" />  
		 	<s:iterator value="#userDetails" var="userInDetails" status="userInDetStatus"> 
             <div class="row-fluid sortable" id='<s:property  value="key" />' style="display:none" >
				<div class="box span12">
					<div class="box-header well" data-original-title>User Information
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content" id='UserTable<s:property value="#userInDetStatus.index+1" />' > 
						<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;' width='100%'   
							class='table table-striped table-bordered bootstrap-datatable datatable' id='DataTables_Table_<s:property value="#userInDetStatus.index+1" />'>
							<thead>
								<tr>
									<th>S No</th>
								<th class='hidden-phone'>Group ID</th>
									<th>User ID</th>
									<th>First Name</th>
									<th class='hidden-phone'>Last Name</th>
									<th class='center hidden-phone'>Email</th>
 									<th>Status</th>
									<th >Actions</th>
								</tr>
							</thead> 
							<tbody id="usersTBody<s:property  value="#userInDetStatus.index+1" />"> 
			                      	<s:set value="value" var="arrayData"/> 
			                      	<s:iterator value="#arrayData" var="userInDetails1" status="userInDetStatus1">
			                      		<!--  Starting iterator for USER DETAILS  --> 
										<s:if test="#userDetStatus.even == true" > 
											<s:set value="%{'even'}" var="flag"/> 
 										 </s:if>
										 <s:elseif test="#userStatus.odd == true">
		      								<s:set value="%{'odd'}" var="flag"/> 	
		   								 </s:elseif> 
		   								 	<tr class="<s:property value='#flag' />" index="<s:property value='#userInDetStatus1.index' />"  id="<s:property value='#userInDetStatus1.index' />">
											<td><s:property value="#userInDetStatus1.index+1" /></td>
											<td class='hidden-phone'><s:property value="#userInDetails1['roleGroupId']" /></td>
											<td><a href='#' id='SEARCH_NO' value='<s:property value="#userInDetails1['userId']" />' ><s:property value="#userInDetails1['userId']" /></a></td>  
												<script type="text/javascript"> 
 													userRights += '<s:property value="#userInDetails1['userId']" />_RIGHTS'+","; 
	 											</script>
											<td><s:property value="#userInDetails1['firstName']" /></td> 
											<td class='hidden-phone'><s:property value="#userInDetails1['lastName']" /></td>
											<td class='center hidden-phone'><s:property value="#userInDetails1['email']" /></td> 
										  	<s:if test="#userInDetails1['user_status'] == 'Active'" > 
												 <s:set value="%{'label-success'}" var="userstatus"/>
												 <s:set value="%{'btn btn-success'}" var="statusclass"/> 
												 <s:set value="%{'De-Active'}" var="text1"/> 
												 <s:set value="%{'icon-unlocked'}" var="text" /> 
											</s:if>
											<s:elseif test="#userInDetails1['user_status'] == 'De-Active'" >
												  <s:set value="%{'label-warning'}" var="userstatus"/>
												 <s:set value="%{'btn btn-success'}" var="statusclass"/> 
												 <s:set value="%{'Activate'}" var="text1"/> 
												 <s:set value="%{'icon-locked'}" var="text"/>
											 </s:elseif>
											 <s:elseif test="#userInDetails1['user_status'] == 'Un-Authorize'" >
												  <s:set value="%{'label-primary'}" var="userstatus"/>
												 <s:set value="%{' '}" var="statusclass"/> 
												 <s:set value="%{' '}" var="text1"/> 
												 <s:set value="%{' '}" var="text"/>
											 </s:elseif>
											 <s:elseif test="#userInDetails1['user_status'] == 'InActive'" >
												   <s:set value="%{'label-info'}" var="userstatus"/>
												 <s:set value="%{' '}" var="statusclass"/> 
												 <s:set value="%{' '}" var="text1"/> 
												 <s:set value="%{' '}" var="text"/>
											 </s:elseif>
											 <td ><a href='#' class='label <s:property value="#userstatus" />' index="<s:property value='#userInDetStatus1.index' />" ><s:property value="#userInDetails1['user_status']" /></a></td>									  
			 	  				
							 	  				<td> 
													<p style="display:inline-flex;"> 
														<a class='<s:property value="#statusclass" />' href='#' id='activ-deactiv-user' index="<s:property value='#userInDetStatus1.index' />" title='<s:property value="#text1" />'  data-rel='tooltip' > <i class='icon <s:property value="#text" /> icon-white'></i> </a>&nbsp;
														<a class='btn btn-success' href='#' id='view-user' index="<s:property value='#userInDetStatus1.index' />" title='View User' data-rel='tooltip'> <i class='icon icon-book icon-white'></i></a>&nbsp;
														<a class='btn btn-warning' href='#' id='modify-user' index="<s:property value='#userInDetStatus1.index' />"  title='Modify User' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;
														<a  class='btn btn-warning' id='assign-report-user'  href='#' index="<s:property value='#userInDetStatus1.index' />" title='Assign Reports' data-rel='tooltip'><i class='icon icon-pdf icon-white'></i></a>&nbsp;
														<a  class='btn btn-info' id='assign-dashboard-user'  href='#' index="<s:property value='#userInDetStatus1.index' />" title='Assign Dashboards' data-rel='tooltip' ><i class='icon icon-plus icon-white'></i></a>&nbsp;
														<a  class='btn btn-primary' href='#' id='password-reset' index="<s:property value='#userInDetStatus1.index' />" title='Password Reset' data-rel='tooltip'><i class='icon icon-redo icon-white'></i></a>
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
		<script type="text/javascript"> 
			  $(document).ready(function () {
				  userRights = userRights.split(",");
					$.each(userRights, function(indexLink, val) {
						if(val != "")
						userTableRights[indexLink]=val.replace(".","_");
	 
					}); 
			  }); 
		</script>
		<s:set value="responseJSON" var="respData"/> 
		<s:set value="%{'_RIGHTS'}" var="searchKey"/> 
		
		<s:bean name="com.ceva.base.common.bean.JsonDataToObject" var="jsonToList">
		  	<s:param name="jsonData" value="#respData"/> 
			<s:param name="searchData" value="#searchKey"/>  
		</s:bean>
		
		<!-- Below Is For Setting Rights...... -->
		
		<div id="rights-grp">
			<s:set value="#jsonToList.data" var="userDetails" />  
		 	<s:iterator value="#userDetails" var="userInDetails" status="userInDetStatus"> 
             <div class="row-fluid sortable" id='<s:property  value="key" />'  style="display:none">
				<div class="box span12">
					<div class="box-header well" data-original-title>User Rights Information
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content" id='UserTable<s:property value="#userInDetStatus.index+1" />' > 
						<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;' width='100%'   
							class='table table-striped table-bordered bootstrap-datatable datatable' 
							id='DataTables_Table_R_<s:property value="#userInDetStatus.index+1" />'>
							<thead>
								<tr>
									<th>S No</th>
									<th>User ID</th>
									<th class='hidden-phone'>Assigned Rights</th>						 
									<th >Actions</th>
								</tr>
							</thead> 
							<tbody id="rightsTBody<s:property  value="#userInDetStatus.index+1" />"> 
			                      	<s:set value="value" var="arrayData"/> 
			                      	<s:iterator value="#arrayData" var="userInDetails1" status="userInDetStatus1">
			                      	
			                      		<!--  Starting iterator for USER DETAILS  --> 
										<s:if test="#userDetStatus.even == true" > 
											<s:set value="%{'even'}" var="flag"/> 
 										 </s:if>
										 <s:elseif test="#userStatus.odd == true">
		      								<s:set value="%{'odd'}" var="flag"/> 	
		   								 </s:elseif> 
		   								 	<tr class="<s:property value='#flag' />" index="<s:property value='#userInDetStatus1.index' />"  id="<s:property value='#userInDetStatus1.index' />">
											<td><s:property value="#userInDetStatus1.index+1" /></td>
											<td><s:property value="#userInDetails1['user_id']" /></td> 
											<s:set value="#userInDetails1['name']" var="selectData"/> 
											<td class='hidden-phone'>
												<select name='rightsDisp' id='rightsDisp' multiple style='width: 550px;height: 450px;'>
													<s:generator val="%{#selectData}"  separator=","> 
														<s:iterator>
														  <option><s:property /></option>
														</s:iterator>
													</s:generator> 
												</select> 
											</td>
											<!-- Actions To Be Displayed..... --> 
											<td ><span>
												<a class='btn btn-warning' href='#' id='modify-user-rights' index='<s:property value="#userInDetStatus1.index" />' title='Modify Rights' data-rel='tooltip'> 
													<i class='icon icon-edit icon-white'></i></a>&nbsp;
												<a  class='btn btn-info' href='#' id='view-user-rights' index='<s:property value="#userInDetStatus1.index" />' title='View User Rights' data-rel='tooltip'>
													<i class='icon icon-book icon-white'></i></a>
												</span>
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
