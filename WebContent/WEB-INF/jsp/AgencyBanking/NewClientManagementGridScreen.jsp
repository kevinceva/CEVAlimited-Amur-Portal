
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
			
	var storeData ='${responseJSON.GROUP_LIST}';
	//alert(storeData);
	var json = jQuery.parseJSON(storeData);
	//alert(json);
	var val = 1;
	var rowindex = 0;
	var colindex = 0;
	var addclass = "";
	var sttd="";
	$.each(json, function(i, v) {
		if(val % 2 == 0 ) {
			addclass = "even";
			val++;
		}
		else {
			addclass = "odd";
			val++;
		}  
		var rowCount = $('#storeTBody > tr').length;
		
		//rowindex = ++rowindex;
		colindex = ++ colindex; 
		
		//alert("value of v.STATUS:"+v.STATUS);
		sttd=v.STATUS;
		
		if( sttd.trim() == 'Active') {
			sttd = "<a href='#' class='label label-success' index='"+rowindex+"'>"+sttd+"</a>";
		    } /* 
		else if( sttd.trim() == 'CV') {
			sttd = "<a href='#' class='label label-info' index='"+rowindex+"'>"+sttd+"</a>"; 
		    }*/
		else {
		    	sttd = "<a href='#' class='label label-warning' index='"+rowindex+"'>"+sttd+"</a>";
		    }
		
/*
if(text.trim() == 'APPROVED') {
   user_status = "<a href='#' class='label label-success' index='"+rowindex+"'>"+text+"</a>";    
  } else if(text.trim() == 'REVERSAL') {
   user_status = "<a href='#' class='label label-info' index='"+rowindex+"'>REVERSAL</a>";   
    } else if(text.trim() == 'FAILED') {
   user_status = "<a href='#' class='label label-warning' index='"+rowindex+"'>DECLINED</a>";   
    }
*/
		 
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
		"<td >"+colindex+"</td>"+
		"<td><a href='#' id='SEARCH_NO' value='CLIENTID@"+v.CLIENT_ID+"' aria-controls='DataTables_Table_0'>"+v.CLIENT_ID+"</span></td>"+
		"<td>"+v.MOB+"</span></td>"+
		"<td>"+v.MKRDT+"</span></td>"+
		"<td>"+sttd+"</span></td>"+
		"<td><a id='modify-client'  class='btn btn-warning' href='#' index='"+rowindex+"'  title='Modify Client' data-toggle='tooltip' ><i class='icon icon-edit icon-white'></i></a> &nbsp;<a id='view-client' class='btn btn-success' index='"+rowindex+"'  href='#' title='View Client' data-toggle='tooltip' ><i class='icon icon-book icon-white'></i></a> </td></tr>";
		/* 
		"<td >"+colindex+"</td>"+
		"<td><a href='#' id='SEARCH_NO' value='CLIENTID@"+v.CLIENT_ID+"' aria-controls='DataTables_Table_0'>"+v.CLIENT_ID+"</span></td>"+	
		"<td>"+v.CLIENT_NAME+"</span> </td>"+ 
		"<td>"+v.GENDER+"</span> </td>"+ 
		"<td>"+v.DOB+"</span></td>"+
		"<td>"+v.ID_TYPE+"</span></td>"+
		"<td>"+v.ID_NUMBER+"</span></td>"+
		"<td>"+v.MOB+"</span></td>"+
		"<td>"+v.ADDR+"</span></td>"+
		"<td>"+v.PCODE+"</span></td>"+
		"<td>"+v.EMAIL+"</span></td>"+
		"<td>"+v.MKRDT+"</span></td>"+
		"<td>"+v.STATUS+"</span></td>"+
		"<td><a class='btn btn-info' id='create-user' href='#' index='"+rowindex+"' title='Create User' data-toggle='tooltip' ><i class='icon icon-plus icon-white'></i></a>&nbsp;<a id='modify-group'  class='btn btn-warning' href='#' index='"+rowindex+"'  title='Modify Group' data-toggle='tooltip' ><i class='icon icon-edit icon-white'></i></a> &nbsp;&nbsp;<a id='view-group' class='btn btn-success' index='"+rowindex+"'  href='#' title='View Group' data-toggle='tooltip' ><i class='icon icon-book icon-white'></i></a>&nbsp;&nbsp;<a id='user-authorize' class='btn btn-success' index='"+rowindex+"'  href='#' title='Authorize User' data-toggle='tooltip' ><i class='icon icon-unlocked icon-white'></i></a> </td></tr>"; */ 
			
			$("#merchantTBody").append(appendTxt);	
			rowindex = ++rowindex;
			/* colindex = ++colindex; */
	});

	
		//console.log(userGroupData);
		$.each(jsonLinks, function(index, v) {
			linkIndex[index] = index;
			linkName[index] = v.name;
			linkStatus[index] = v.status;
		}); 
		
		$("input[type=search]").live('keyup',function() {
			
			var ariaControlsval=$(this).attr('aria-controls');
			var contVal=$(this).val();
			//alert(ariaControlsval+"------------"+contVal)
			if(ariaControlsval=="DataTables_Table_0" && contVal=="" ) {
				$("#stores").hide();
				$("#terminals").hide();
			} 
			else if(ariaControlsval=="DataTables_Table_Store" && contVal=="" ) {
				$("#terminals").hide();
			} 
			
				
		});  
		
		 $(document).on('click','a',function(event) {
		    	
	    	var v_id=$(this).attr('id');
	    	var typeOFReq = $(this).attr('value').split("@")[0];
	    	
			if(typeOFReq == "CLIENTID"){
				var groupId = $(this).attr('value').split("@")[1];
		    	
		    	var t = $('#DataTables_Table_Store').DataTable();
				t.clear().draw();
				
				var queryString = "groupId="+groupId;	
				$.getJSON("clientPolicyJsonAct.action", queryString,function(data) { 
					
					var storeData =data.responseJSON.USER_LIST;
					var json = storeData;
					var val = 1;
					var rowindex = 1;
					var colindex = 0;
					var addclass = "";
					$.each(json, function(i, v) {
						if(val % 2 == 0 ) {
							addclass = "even";
							val++;
						}
						else {
							addclass = "odd";
							val++;
						}  
						 
						var user_status="";
						var sttd="";
						var status_class ="";
						var text = "";
						var text1 = "";
						
/* 						if(v.USER_STATUS == 'Active') {
							user_status = "<a href='#' class='label label-success' index='"+rowindex+"'>"+v.user_status+"</a>";
							status_class = "btn btn-danger";
							text = "<i class='icon icon-locked icon-white'></i>";
							text1 = "Deactivate";
						} else if(v.USER_STATUS == 'De-Active') {
							user_status = "<a href='#'  class='label label-warning'  index='"+rowindex+"'>"+v.user_status+"</a>";
							status_class = "btn btn-success";
							text = "<i class='icon icon-unlocked icon-white'></i>";
							text1 = "Activate";
						} else if(v.USER_STATUS == 'InActive') {
							user_status = "<a href='#'  class='label label-info'  index='"+rowindex+"'>"+v.user_status+"</a>";
						} 
						
						$("#pid").val(v.POLICY_ID);
						$("#pamt").val(v.PRIM_AMT);
						$("#samt").val(v.SUM_ASUR);
						$("#mdt").val(v.MKRDT);
						$("#sts").val(v.STATUS);*/
						
			/* 			sttd=v.STATUS;
						
						if( sttd.trim() == 'PS') {
							sttd = "<a href='#' class='label label-success' index='"+rowindex+"'>SUCCESS</a>";
						    } 
						else if( sttd.trim() == 'PV') {
							sttd = "<a href='#' class='label label-info' index='"+rowindex+"'>Policy Verified</a>";
						    }else {
						    	sttd = "<a href='#' class='label label-warning' index='"+rowindex+"'>Policy Created</a>";
						    } */						
						
						var lastTd="<p><a class='"+status_class+"' href='#' id='activ-deactiv-user' index='"+rowindex+"' title='"+text1+"' data-toggle='tooltip' > "+text+"</a>&nbsp;<a  class='btn btn-success' href='#' id='view-user' index='"+rowindex+"' title='View User' data-toggle='tooltip'> <i class='icon icon-book icon-white'></i></a>&nbsp;<a class='btn btn-warning' href='#' id='modify-user' index='"+rowindex+"'  title='Modify User' data-toggle='tooltip'><i class='icon icon-edit icon-white'></i></a></p><p><a  class='btn btn-warning' id='assign-report-user'  href='#' index='"+rowindex+"' title='Assign Reports' data-toggle='tooltip'><i class='icon icon-pdf icon-white'></i></a>&nbsp;<a  class='btn btn-primary' href='#' id='password-reset' index='"+rowindex+"' title='Password Reset' data-toggle='tooltip'><i class='icon icon-redo icon-white'></i></a>&nbsp;<a  class='btn btn-info' id='assign-dashboard-user'  href='#' index='"+rowindex+"' title='Assign Dashboards' data-toggle='tooltip' ><i class='icon icon-plus icon-white'></i></a></p> &nbsp;&nbsp;";
						var firstTd = "<a href='#' id='SEARCH_NO' value='EACHUSER@"+v.POLICY_ID+"' >"+v.POLICY_ID+"</a>";
						
					        var i= t.row.add( [
								rowindex,
								firstTd,
								v.PRIM_AMT,
								v.SUM_ASUR,
								v.SDT,
								v.EDT,/* as per ravi inst
								sttd, */
								lastTd
					        ] ).draw( false );
					        t.rows(i).nodes().to$().attr("id", rowindex);
					        t.rows(i).nodes().to$().attr("index", rowindex);
					 
					        rowindex = ++rowindex;
					        colindex = ++colindex; 
					});		
		 		    
				 	$("#stores").show();
				});

			}
			else if(typeOFReq == "EACHUSER"){
				var userId = $(this).attr('value').split("@")[1];
		    	//alert(merchantId);
		    	
		    	
				
				
				var queryString = "userId="+userId;	
				$.getJSON("fetchPolicyPurchaseJsonAct.action", queryString,function(data) { 
					
					var terminalTable = $('#DataTables_Table_Terminal').DataTable();
					terminalTable.clear().draw();
					var storeData =data.responseJSON.PURG_STMT;
					var json = storeData;
					var val = 1;
					var rowindex = 1;
					var colindex = 0;
					var addclass = "";
					
					 $.each(json, function(i, v) {
						if(val % 2 == 0 ) {
							addclass = "even";
							val++;
						}
						else {
							addclass = "odd";
							val++;
						}  
						var sttd="";
					/* 	var selDisp = "<select name='rightsDisp' multiple style='width: 350px;height: 450px;'> ";
						var splitDat = v.name.split(",");
							$(splitDat).each(function(i,val){
								selDisp+="<option> "+val +" </option>";
							}); 
						selDisp+="</select>"; */
						
						 
						var lastTd="<p><a class='btn btn-warning' href='#' id='modify-user-rights' index='' title='Modify Rights' data-toggle='tooltip'> <i class='icon icon-edit icon-white'></i></a>&nbsp;<a  class='btn btn-info' href='#' id='view-user-rights' index='' title='View User Rights' data-toggle='tooltip'><i class='icon icon-book icon-white'></i></a>&nbsp;";
						//var firstTd = "<a href='#' id='SEARCH_NO' value='TERMINAL-"+v.user_id+"' >"+v.user_id+"</a>";
							
/* 							var i=terminalTable.row.add( [
								rowindex,
								v.user_id,
								selDisp,
								lastTd
					        ] ).draw( false );
					 
							terminalTable.rows(i).nodes().to$().attr("id", rowindex);
							terminalTable.rows(i).nodes().to$().attr("index", rowindex);
					        rowindex = ++rowindex;
					        colindex = ++colindex; */
					        
							sttd=v.STATUS;
							
						 	if( sttd.trim() == 'SUCCESS') {
								sttd = "<a href='#' class='label label-success' index='"+rowindex+"'>SUCCESS</a>";
							    } 
							else {
							    	sttd = "<a href='#' class='label label-warning' index='"+rowindex+"'>"+sttd+"</a>";
							    } 					        
					        
						var firstTd = "<a href='#' id='SEARCH_NO' value='EACHPURG@"+v.PURCHASE_ID+"' >"+v.PURCHASE_ID+"</a>";
				        var i= terminalTable.row.add( [
							rowindex,
							v.PURCHASE_ID,
							v.PRIM_AMT,
							v.SUM_ASUR,
							v.MKRDT,
							sttd,
							lastTd
				        ] ).draw( false );
						terminalTable.rows(i).nodes().to$().attr("id", rowindex);
						terminalTable.rows(i).nodes().to$().attr("index", rowindex);
				 
				        rowindex = ++rowindex;
				        colindex = ++colindex; 
										        
					});	
					
				 	$("#terminals").show();
				});

			}
			
			var txt_sr = $(this).attr('value').split("@")[1];
			//alert(txt_sr);
			var parentId =$(this).parent().closest('table').attr('id'); 
			
			var ariaControlsval=$(this).attr('aria-controls');
			//alert(ariaControlsval);
			//alert(txt_sr+"----"+parentId);
			$('div input[type=search]').each(function(){
				//console.log("["+$(this).attr("aria-controls") +"] == ["+ parentId+"]");
				
				if(ariaControlsval == parentId) {
					//console.log("Val ["+$(this).text()+"]"); 
					//alert($(this).attr("aria-controls"));
					//$(this).val('');
					$("#"+ariaControlsval).val(txt_sr);
					$("#"+ariaControlsval).trigger("keyup");
				} /*else {
					if(parentId !='DataTables_Table_0') {
						$(this).val('');
					} 
						
				}*/
			});
			
				
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
		 
		/* var index1 = $(this).attr('index'); */  
		var index1 = $(this).parent().closest('tr').attr('index');
		
		//var index1=$(this).parent().closest('tr').index();
		//alert(index1)
		
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		var searchTrRows = parentId+" tr"; 
		//alert("searchTrRows::"+searchTrRows);
		var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
		//alert("searchTrRows::"+searchTdRow);		 
				 
		if(disabled_status == undefined) {  
			if( v_id == "group-creation") { 
				v_action = "userGrpCreationNew";  
			} else if (v_id ==  "create-user" 
					|| v_id ==  "view-client" 
					|| v_id ==  "modify-client"
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
				} else if(v_id ==  "view-client") { 
					//v_action="viewClientInfo";
					v_action="clientInformation";  
					queryString += '&type=View'; 
				} else if(v_id ==  "modify-client") { 
					//v_action="viewClientInfo";  
					v_action="clientInformation";
					queryString += '&type=Modify'; 
				} else if(v_id ==  "user-authorize") { 
					v_action="getUnAuthUsersAct";  
				} 
				
				queryString += '&groupId='+groupId;  
			}  else if (v_id ==  "view-user" 
						|| v_id ==  "modify-user" 
						|| v_id ==  "activ-deactiv-user" 
						|| v_id ==  "password-reset"
						|| v_id == "assign-dashboard-user"  
						|| v_id == "assign-report-user") {  
				
				// Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {   
					if(indexTd == 1) {
						userId = $(this).text(); 
					}
					if(indexTd == 2) {

						groupId = $(this).text(); 
					}
				}); 
			 
				queryString += '&groupID='+groupId+'&userId='+userId;
				//alert(queryString);
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
			}  else if (v_id == 'export-users') {
				v_action="usersexport";
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
		var txt_sr = $(this).text();
		var parentId =$(this).parent().closest('table').attr('id'); 
		//console.log("The val ["+txt_sr+"]");
		//console.log("The Attr Id ["+parentId+"]");
		
		$('div input[type=search]').each(function(){
			if($(this).attr("aria-controls") == parentId) {
				//console.log("Val ["+$(this).val()+"]"); 
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
	<form name="form1" id="form1" method="post" action="">	
	<!-- topbar ends --> 
	<div id="form1-content" class="span10">   
		 
			   <div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home </a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Client Information</a></li>
				</ul>
			 </div>
			
<%-- 			<div class="box-content" id="top-layer-anchor">
				<span>
					<a href="#" class="btn btn-info" id="group-creation" title='New Group Creation' data-toggle='popover'  data-content='Creating a new group.'>
					<i class="icon icon-web icon-white"></i>&nbsp;New Group Creation</a> &nbsp;							
				</span>
				<span>
					<a href="#" class="btn btn-warning" id="dashboard" title='View Users' data-toggle='popover'  data-content='Viewing the registered users.'>
					<i class="icon icon-briefcase icon-white"></i>&nbsp;View Users</a> &nbsp; 
				</span> 	
				<span>
					<a href="#" class="btn btn-success" id="export-users" title='Export Users' data-toggle='popover'  data-content='Exporting the registered users.'>
					<i class="icon icon-briefcase icon-white"></i>&nbsp;Export Users</a> &nbsp; 
				</span> 			 
			</div>  --%>
								  
           <div class="row-fluid sortable"><!--/span-->
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>Client Information
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
								<th>Client ID</th>
								<th>Mobile </th>
								<th class="center hidden-phone">Creation Date</th>
								<th>Status </th>
								<th>Actions</th> 
								
							<!-- 	<th>S No</th>
								<th>Client ID</th>
								<th>Client Name </th>
								<th>Gender </th>
								<th>Date Of Birth </th>
								<th>ID Type </th>
								<th>ID Number </th>
								<th>Mobile </th>
								<th>Address </th>
								<th>Postal Code </th>
								<th>Email </th>
								<th class="center hidden-phone">Creation Date</th>
								<th>Status </th>
								<th>Actions</th>  -->								
							</tr>
						</thead> 
						<tbody id="merchantTBody">
		
						</tbody>
					</table>
				</div>
			</div>
		</div> 
		
		<!-- Loading Stores -->
	 	<div  id="stores"  style="display:none">  
             <div class="row-fluid sortable" id='<s:property  value="key" />'>
				<div class="box span12">
					<div class="box-header well" data-original-title>Policy Information
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content"  > 
						<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;' width='100%'   
							class='table table-striped table-bordered bootstrap-datatable datatable' 
							id='DataTables_Table_Store'>
							<thead>
								<tr>
								<th>S No</th>
								<th>Policy ID </th>
								<th>Cummulative Premium</th>
								<th>Cummulative Sum Assured </th>
								<th class="center hidden-phone">Start Date</th>
								<th class="center hidden-phone">End Date</th>
								<!-- <th>Status</th> -->
									<%-- <td>Client Id</td><td><span id="cid"></span></td>
									<td>Policy ID</td><td><span id="pid"></span></td>
									<td>AIRTIME Premium Amount</td><td><span id="pamt"></span></td>
									<td>SUM Assured</td><td><span id="samt"></span></td>
									<td>Date Created</td><td><span id="mdt"></span></td>
									<td>Status</td><td><span id="sts"></span></td> --%>
								</tr>
							</thead> 
							<tbody id="storeTBody"> 
		                    </tbody>
						</table>
					</div>
				</div>
			</div>  
		</div>
		
		<!-- Below Is For Setting Terminals...... -->
		
		<div id="terminals"   style="display:none">
             <div class="row-fluid sortable">
				<div class="box span12">
					<div class="box-header well" data-original-title>Policy Purchase Information
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content"  > 
						<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;' width='100%'   
							class='table table-striped table-bordered bootstrap-datatable datatable' 
							id='DataTables_Table_Terminal'>
							<thead>
								<tr>
									<th>S No</th>
									<th>Purchase ID</th>
									<th>Premium Amount</th>
									<th>Sum Assured</th>
									<th class="center hidden-phone">Creation Date</th>
									<th>Status</th>
								</tr>
							</thead> 
							<tbody id="TerminalTbody"> 
		                    </tbody>
						</table>
					</div>
				</div>
			</div>   
		</div> 
	</div> 
	 
	
</div>  
</form>
<form name="form2" id="form2" method="post">

</form>	

<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables11.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
