
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="TEAM">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>

  
<script type="text/javascript" > 

function TerminateConfirm(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/storeTermnateConfirmAct.action';
	$("#form1").submit();
	return true;
}
function getGenerateScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/generateMerchantAct.action';
	$("#form1").submit();
	return true;
}
		
$(function () {
	var tds=new Array();
	var merchantData ='${responseJSON.MerchantDashboard}';
	var json = jQuery.parseJSON(merchantData);
	var val = 1;
	var rowindex = 0;
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
		var rowCount = $('#merchantTBody > tr').length;
		
 		colindex = ++ colindex; 
		var processingCode=(v.processingCode == undefined) ? "" : v.processingCode;
		var processingName=(v.processingName == undefined) ? "" : v.processingName;
		var makerId=(v.makerId == undefined) ? "" : v.makerId;
		var makerDate=(v.makerDate == undefined) ? "" : v.makerDate;
		index=colindex-1;
		
 		var tabData="DataTables_Table_0"; 
			
 		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
		"<td >"+colindex+"</td>"+
		"<td>"+v.hudumaServiceCode+"</span></td>"+	
		"<td>"+v.virtualCard+"</span> </td>"+ 
		"<td>"+v.hudumaSubService+"</span></td>"+	
		"<td>"+v.hudumaSubServiceName+"</span> </td>"+ 
		"<td>"+v.makerId+"</span></td>"+
		"<td>"+v.makerDate+"</span></td></tr>";
			
		$("#merchantTBody").append(appendTxt);	
		rowindex = ++rowindex;
	});
	
	 
	
});

function serviceMgmt(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/serviceMgmtAct.action';
	$("#form1").submit();
	return true;
}	 
</script>
<style type="text/css" media="print">
@media print
{
	#non-printable { display: none; }
	#printable {
	display: block;
	width: 100%;
	height: 100%;
	}
}
</style> 
</head> 
<body>
	<form name="form1" id="form1" method="post">	
	<!-- topbar ends -->
	
		
			<div id="content" class="span10">  
		 
			<div>
				<ul class="breadcrumb">
				  <li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li><a href="serviceMgmtAct.action?pid=10">Fee Management</a> <span class="divider"> &gt;&gt; </span></li>
				  <li><a href="#">View Huduma Service</a></li>
				</ul>
			</div>
				
		<div class="row-fluid sortable"><!--/span--> 
			<div class="box span12"> 
				<div class="box-header well" data-original-title>Huduma Service Information
				  <div class="box-icon"> 
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
				  </div>
				</div> 
				<div class="box-content"> 
					<table width="100%"  id="DataTables_Table_0" 
						class="table table-striped table-bordered bootstrap-datatable datatable">
						<thead>
							<tr >
								<th>S No</th>
								<th>Huduma Code</th>
								<th>virtual Card</th>
								<th>Huduma Service Code</th>
								<th>Huduma Service Name</th>
								<th >Created By</th>
								<th >Created Date </th>
							</tr>
						</thead> 
						<tbody  id="merchantTBody">
						</tbody>
					</table>
				</div>
            </div>
      </div>
		<div class="form-actions"> 
			<input type="button" name="btn-submit" class="btn btn-success" id="btn-submit" value="Next" onclick="serviceMgmt()"/> 
		</div>  
	</div>  
		 
</form>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script> 
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
