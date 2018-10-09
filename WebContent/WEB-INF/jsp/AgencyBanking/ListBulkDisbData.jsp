
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Banking</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<%@taglib uri="/struts-tags" prefix="s"%> 	
 <s:set value="bulkBean.responseJSON" var="respData"/>
<script type="text/javascript" > 
var usersList = new Array();
var groupsList = new Array(); 
var userLinkData ='${USER_LINKS}';
var jsonLinks = jQuery.parseJSON(userLinkData);
var linkIndex = new Array();
var linkName = new Array();
var linkStatus = new Array();


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

$(document).ready(function () {
	
	//console.log(userGroupData);
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
	var v_id=$(this).attr('id'); 
	var queryString = "";
	if(v_id == 'ref_check') {
		var relData = $(this).attr('rel'); 
		console.log(relData);
		queryString += 'refNo='+relData;    
		 v_action = "viewIndividualBulkUploadData";   
		 postData(v_action+".action",queryString);  
	}
	
	
}); 
</script>  
</head>

<body>
<form name="form1" id="form1" method="post" >	
	<div id="content" class="span10"> 
	  <div>
		 <ul class="breadcrumb">
			<li><a href="home.action">Home </a> <span class="divider"> &gt;&gt; </span></li> 
 			<li><a href="#">View Bulk Disbursment Records </a> </li>
		</ul>
	</div>  
			
		<table height="3">
			<tr>
				<td colspan="3">
					<div class="messages" id="messages">
						<s:actionmessage />
					</div>
					<div class="errors" id="errors">
						<s:actionerror />
					</div>
				</td>
			</tr>
	  </table> 	
      					 
    <div class="row-fluid sortable"><!--/span--> 
		<div class="box span12" id="groupInfo">
                  <div class="box-header well" data-original-title>View Bulk Disbursment Records 
					<div class="box-icon"> <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					 <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> </div>
				</div>
           		<div class="box-content">
 					<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable" 
						id="DataTables_Table_0"  >
								<thead>
									<tr> 
										<th>Ref No</th>
										<th>Status</th>
										<th>Uploaded File Name</th>
 										<th>Success/Failure Processed</th>
										<th>Uploaded By</th>										
										<th>Uploaded Date</th>
  									</tr>
								</thead> 
								<tbody  id="orgTBody">
   									<s:iterator value="#respData.bulk_data" var="bulkData" status="bulkDataStatus" >
 										<s:if test="#userDetStatus.even == true" > 
											<s:set value="%{'even'}" var="flag"/> 
 										 </s:if>
										 <s:elseif test="#userStatus.odd == true">
		      								<s:set value="%{'odd'}" var="flag"/> 	
		   								 </s:elseif> 
		   								 	<tr class="<s:property value='#flag' />" index="<s:property value='#bulkDataStatus.index' />"  id="<s:property value='#bulkDataStatus.index' />">
 											<td class='hidden-phone'><a id="ref_check" rel="<s:property value="#bulkData['refNo']" />"><s:property value="#bulkData['refNo']" /></a></td>
											<td class='hidden-phone'><s:property value="#bulkData['status']" /> </td>
 											<td class='hidden-phone'><s:property value="#bulkData['fileName']" /> </td>
 											<td class='hidden-phone'><s:property value="#bulkData['successRec']" />/<s:property value="#bulkData['failureRec']" /> </td>
  											<td class='hidden-phone'><s:property value="#bulkData['createdBy']" /></td>
											<td class='hidden-phone'><s:property value="#bulkData['createdDate']" />  </td>
											 
			                      		</tr>
			                      	</s:iterator> 	 
								</tbody>
							</table>
                    </div>
          </div>
		</div>   
		
</div><!--/#content.span10--> 
</form> 
<form name="form2" id="form2" method="post">

</form>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
