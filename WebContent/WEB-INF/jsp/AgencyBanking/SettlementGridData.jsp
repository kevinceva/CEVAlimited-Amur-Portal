
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
 
<script type="text/javascript" > 
var usersList = new Array();
var groupsList = new Array(); 
function postData(actionName,paramString){
		$('form').attr("action", actionName)
				.attr("method", "post");
		
		var paramArray = paramString.split("&");
		
		$(paramArray).each(function(indexTd,val) { 
			var input = $("<input />").attr("type", "hidden").attr("name", val.split("=")[0]).val(val.split("=")[1]);
			$('form').append($(input));	 
		});	

		$('form').submit();	
}

$(document).ready(function () {

	$(document).on('click','a',function(event) {  
 		var queryString = "";
		var v_id=$(this).attr('id');
		var index1 = $(this).attr('index');  
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		var searchTrRows = parentId+" tr"; 
		var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
		
		var groupId = "";  
		var userId = "";  
		var action = "";
		
		$(searchTdRow).each(function(indexTd) {  
			if(indexTd == 1) {
				groupId = $(this).text(); 
			}
			if(indexTd == 2) {
				userId = $(this).text(); 
			} 
		}); 
			
		queryString += 'groupID='+groupId+'&userId='+userId;
		
		if(v_id == 'group-view') {
			queryString+='&type=GroupView';
		} else {
			queryString+='&type=View';
		}
		
		if(v_id == 'group-view' 
			|| v_id == 'row-view' ) {
			postData("userDashInformation.action",queryString);
		}
		
		 
	}); 
	
	
	$('#btn-submit').live('click', function () { 
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/settlementStatus.action?pid=116';
		$("#form1").submit();
		return true; 
	});
	 
});  
</script> 
 
 
		
</head>

<body>
<form name="form1" id="form1" method="post" >	
	<div id="content" class="span10"> 
	  <div>
		 <ul class="breadcrumb">
			<li><a href="#">Home </a> <span class="divider"> &gt;&gt; </span></li>
			<li><a href="settlementStatus.action?pid=116">Settlement Data</a> <span class="divider"> &gt;&gt; </span> </li>
			<li><a href="#">Settlement DashBoard</a> </li>
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
                  <div class="box-header well" data-original-title>Settlement DashBoard 
					<div class="box-icon"> <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					 <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> </div>
				</div>
           		<div class="box-content">
 					<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable" 
						id="DataTables_Table_0"  >
								<thead>
									<tr>
										<th>S No</th>
										<th>Txn Ref No</th>
										<th>Bank Code</th>
										<th>Bank Name </th>
										<th>File Name</th>
										<th>File Status</th> 
										<th>File Description</th>
										<th>Settlement Date</th>
 									</tr>
								</thead> 
								<tbody  id="userGroupTBody">
									<s:iterator value="responseJSON['settle_data']" var="settlementData" status="settlementDataStatus">
			                      		<!--  Starting iterator for USER DETAILS  --> 
										<s:if test="#userDetStatus.even == true" > 
											<s:set value="%{'even'}" var="flag"/> 
 										 </s:if>
										 <s:elseif test="#userStatus.odd == true">
		      								<s:set value="%{'odd'}" var="flag"/> 	
		   								 </s:elseif> 
		   								 	<tr class="<s:property value='#flag' />" index="<s:property value='#settlementDataStatus.index' />"  id="<s:property value='#settlementDataStatus.index' />">
											<td><s:property value="#settlementDataStatus.index+1" /></td>
											<td class='hidden-phone'><s:property value="#settlementData['txnrefno']" /></td>
											<td class='hidden-phone'><s:property value="#settlementData['bank_id']" /></td>
											<td class='hidden-phone'><s:property value="#settlementData['bank_name']" />  
 											<td><s:url  action="downloadSettlementFile" namespace="/AgencyBanking" id="fileDownload">
 												</s:url>
 												
 												<s:a href="%{fileDownload}?fileId=%{#settlementData['file_name']}&txnRefNo=%{#settlementData['txnrefno']}&fileStatus=%{#settlementData['file_status']}"> 
 													 <s:property value="#settlementData['file_name']" />  </s:a> </td>
	 											<s:if test="#settlementData['file_status'] == 'SUCCESS'" > 
 													<s:set value="%{'label-success'}" var="userstatus"/>
													<s:set value="%{'SUCCESS'}" var="statustext"/>
	  											</s:if>
												<s:else>
 													  <s:set value="%{'label-warning'}" var="userstatus"/>
													  <s:set value="%{'FAILED'}" var="statustext"/>
	  											</s:else> 
											<td>
												<a href='#' class='label <s:property value="#userstatus" />' 
											 	     index="<s:property value='#settlementDataStatus.index' />" >
											 		 <s:property value="#statustext" />
											 	</a>
											 </td> 
											<td class='hidden-phone'><s:property value="#settlementData['file_desc']" /></td>
											<td class='center hidden-phone'><s:property value="#settlementData['file_date']" /></td> 
			                      		</tr>
			                      	</s:iterator> 	 
								</tbody>
							</table>
                    </div>
          </div>
		</div> 
		
		<div class="form-actions"> 
			<input type="button" class="btn btn-success" type="text"  name="btn-submit" id="btn-submit" value="Back" width="100" /> 
		</div>   
	 
</div><!--/#content.span10--> 
</form> 
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
