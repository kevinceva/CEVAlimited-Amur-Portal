<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>CEVA</title>
<%@taglib uri="/struts-tags" prefix="s"%>  
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>    
<s:set value="responseJSON" var="respData"/>
<script type="text/javascript" > 

$(document).on('click','a',function(event) {  
	var anchorText = $(this).text();
	if(anchorText != 0 ) {
		postData($(this).attr('action'),$(this).attr('param'));
	} else {
		//console.log("The entry is :::: " + anchorText);
	}  
});

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

</script>
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
<link rel="shortcut icon" href="images/favicon.ico">		
</head> 
<body>  
		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Authorization</a></li>
				</ul>
			</div> 
		<form name="form1" id="form1" method="post" action="">							  
              <div class="row-fluid sortable"><!--/span--> 
				<div class="box span12">
					  <div class="box-header well" data-original-title>Authorization List
						  <div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					  </div>  
						<div class="box-content"> 
							<table width='100%' class="table table-striped table-bordered bootstrap-datatable "  
 								id="DataTables_Table_0" >
								<thead>
									<tr>
										<th>S No</th> 
										<th>Authorization Name</th>
										<th class="center hidden-phone">Today List</th>
									    <th>Authorized</th>
										<th class="hidden-phone">Rejected</th>
										<th class="hidden-phone">Pending</th>
								  	</tr>
								</thead> 
								<tbody  id="authorizeTbody">
									<s:iterator value="responseJSON['AUTH_PENDING_LIST']" var="authPendingList" status="authPendingStatus">  
										<s:if test="#authPendingStatus.even == true" > 
											<tr class="even" index="<s:property value='#authPendingStatus.index' />"  id="<s:property value='#authPendingStatus.index' />">
										 </s:if>
										 <s:elseif test="#authPendingStatus.odd == true">
		      								<tr class="odd" index="<s:property value='#authPendingStatus.index' />"  id="<s:property value='#authPendingStatus.index' />">
		   								 </s:elseif> 
											<td><s:property value="#authPendingStatus.index+1" /></td> 
											<td><s:property value="#authPendingList['HEADING_NAME']" /> </td>   
											<td><a href='#' class='label label-success' index="<s:property value='#authPendingStatus.index' />" data-toggle='tooltip' title="Today's List is : <s:property value="#authPendingList['NEW']" />"  action="<s:property value="#authPendingList['ACTION_URL']" />"  param="AUTH_CODE=<s:property value="#authPendingList['AUTH_CODE']" />&STATUS=P"><s:property value="#authPendingList['NEW']" /></a></td>
										  	<td><a href='#' class='label label-success' index="<s:property value='#authPendingStatus.index' />" data-toggle='tooltip' title="Authorized List is : <s:property value="#authPendingList['CLOSED']" />"  action="<s:property value="#authPendingList['ACTION_URL']" />"  param="AUTH_CODE=<s:property value="#authPendingList['AUTH_CODE']" />&STATUS=C"><s:property value="#authPendingList['CLOSED']" /></a></td>
										  	<td><a href='#' class='label label-important' index="<s:property value='#authPendingStatus.index' />" data-toggle='tooltip' title="Rejected List is : <s:property value="#authPendingList['REJECTED']" />" action="<s:property value="#authPendingList['ACTION_URL']" />"  param="AUTH_CODE=<s:property value="#authPendingList['AUTH_CODE']" />&STATUS=R"><s:property value="#authPendingList['REJECTED']" /></a></td>
										  	<td><a href='#' class='label label-success' index="<s:property value='#authPendingStatus.index' />" data-toggle='tooltip' title="Pending List is : <s:property value="#authPendingList['PENDING']" />" action="<s:property value="#authPendingList['ACTION_URL']" />"  param="AUTH_CODE=<s:property value="#authPendingList['AUTH_CODE']" />&STATUS=P"><s:property value="#authPendingList['PENDING']" /></a></td>											  
		 								</tr> 
									 </s:iterator>
								</tbody>
							</table>
						</div> 
					</div> 
 			</div> 
		</form> 
	</div>
<form name="form2" id="form2" method="post">
</form>
 </body>
</html>
