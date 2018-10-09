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
	var v_id=$(this).attr('id') ;
	//alert($(this).attr("id"));
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
				if(v_id ==  "add-biller-type") {
					v_action="addBillerType";
				} else if(v_id == "add-biller-id"){
					v_action="addBillerId";
				} 
			 
		} else {
		
			// No Rights To Access The Link 
		}
		//console.log(queryString);
		if(v_action != "NO") {
			 postData(v_action+".action",queryString); 
		 
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
				<li><a href="#">Mpesa A/C Management</a></li>
			</ul>
		 </div>
		<div class="box-content" id="top-layer-anchor">
			<span>
				<a href="#" class="btn btn-info" id="add-biller-type" title='Add Biller Type' data-rel='popover'  data-content='Creating a new biller type.'>
				<i class="icon icon-web icon-white"></i>&nbsp;Add Biller Type</a> &nbsp;							
			</span>
			<span>
				<a href="#" class="btn btn-warning" id="add-biller-id" title='Add Biller Id' data-rel='popover'  data-content='Creating biller id.'>
				<i class="icon icon-briefcase icon-white"></i>&nbsp;Add Biller Id</a> &nbsp; 
			</span>   
			<span>
				<a href="#" class="btn btn-warning" id="validation" title='Validation' data-rel='popover' data-content="Create Validation's For Biller Type / Biller Id.">
				<i class="icon icon-briefcase icon-white"></i>&nbsp;Validation</a> &nbsp; 
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
								<th>Biller Type</th>
								<th>Biller Id</th>
 								<th class="hidden-phone">Description</th>
 								<th>Actions</th>
							</tr>
						</thead> 
						<tbody id="userGroupTBody">
							 <s:iterator value="payBillBean.responseJSON['MerchantDashboard']" var="billerData" status="billerDataStatus"> 
		                        <!--  Starting iterator for USER DETAILS  --> 
								<s:if test="#userDetStatus.even == true" > 
									<s:set value="%{'even'}" var="flag"/> 
									 </s:if>
								 <s:elseif test="#userStatus.odd == true">
	     								<s:set value="%{'odd'}" var="flag"/> 	
	  								 </s:elseif> 
	  								 	<tr class="<s:property value='#flag' />" index="<s:property value='#billerDataStatus.index' />"  id="<s:property value='#billerDataStatus.index' />">
	  								 	<td class='hidden-phone'><s:property value='#billerDataStatus.index+1' /></td>
										<td class='hidden-phone'><s:property value="#billerData['billerType']" /></td>
									<td class='hidden-phone'><s:property value="#billerData['billerId']" /> </td>
									<td class='hidden-phone'><s:property value="#billerData['description']" /> </td>
									<td class='hidden-phone'><s:property value="#billerData['createdBy']" /></td>
									<td class='hidden-phone'><s:property value="#billerData['createdDate']" />  </td>
									<td> 
										 <p>
											 <a class='btn btn-warning' href='#' id='biller-edit' index="<s:property value='#billerDataStatus.index' />" title='Edit Biller' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;
											 <a class='btn btn-info' href='#' id='biller-view' index="<s:property value='#billerDataStatus.index' />" title='View Biller' data-rel='tooltip'><i class='icon icon-page icon-white'></i></a>&nbsp;
											 <a class='btn btn-success' href='#' id='biller-upload-data' index="<s:property value='#billerDataStatus.index' />" title='Upload Biller Data' data-rel='tooltip'><i class='icon icon-star-on icon-white'></i></a>
										</p>
									</td> 
	                      		</tr>
	                      	</s:iterator> 	 
						</tbody>
					</table>
				</div>
			</div>
		</div>   
		
	</div>
</form>
<form name="form2" id="form2" method="post">
</form>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>