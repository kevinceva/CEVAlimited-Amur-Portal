
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
	
	 
<script> 

function TerminateConfirm(){
	$("#form1")[0].action='<%=ctxstr%>/<%=appName %>/storeTermnateConfirmAct.action';
	$("#form1").submit();
	return true;
}
function getGenerateScreen(){
	$("#form1")[0].action='<%=ctxstr%>/<%=appName %>/generateMerchantAct.action';
	$("#form1").submit();
	return true;
}
		
$(document).ready(function () {
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
		var merchnatId=(v.merchantID == undefined) ? "" : v.merchantID;
		var storeId=(v.storeId == undefined) ? "" : v.storeId;
		var terminalID=(v.terminalID == undefined) ? "" : v.terminalID;
		var status=(v.status == undefined) ? "" : v.status; 
		var location=(v.store_name == undefined) ? "" : v.store_name; 
		
		var serialno=(v.serialno == undefined) ? "" : v.serialno;
		var authterminalid=(v.authterminalid == undefined) ? "" : v.authterminalid;
		
		index=colindex-1;
		
		var user_status="";
		 
		if(status == 'Active') {
			user_status = "<a href='#' class='label label-success' index='"+rowindex+"'> "+status+" </a>"; 
		} else  {
			user_status = "<a href='#'  class='label label-warning'  index='"+rowindex+"'>"+status+" </a>"; 
		} 
 
		var tabData="DataTables_Table_0"; 
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
					"<td >"+colindex+"</td>"+
					"<td><a href='#'  id='MerchantView' index='"+rowindex+"' >"+merchnatId+"</a></span></td>"+	
					"<td> <a href='#' id='StoreView' index='"+rowindex+"' >"+storeId+" </a></span> </td>"+ 
					"<td class='hidden-phone'>"+location+"</span></td>"+
					"<td > <a href='#' id='TerminalView' index='"+rowindex+"' >"+terminalID+"</a></span> </td>"+ 
					"<td class='center hidden-phone'>"+user_status+"</span></td>"+
					"<td >"+serialno+"</span></td>"+
					"<td class='hidden-phone'>"+authterminalid+"</span></td>"+
				"</tr>";
			
		$("#merchantTBody").append(appendTxt);	
		rowindex = ++rowindex;
	});
	
});


$(document).on('click','#MerchantView',function(event) {  
	index = $(this).attr('index');
 	var searchRow = "DataTables_Table_0 tbody tr:eq("+(index)+") td"; 
 	$('#'+searchRow).each(function (indexTd) { 
		 if (indexTd == 1) {
 			merchantId=$(this).text();
		 }   if(indexTd == 2) {
 			merchantName=$(this).text();
		 }   if(indexTd == 3) {
 		 }   if(indexTd == 4) {
		 }  
	}); 
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/merchantViewDashboardAct.action?merchantID='+merchantId;
	$("#form1").submit();
	return true;
});

$(document).on('click','#StoreView',function(event) {  
	index = $(this).attr('index');
 	var searchRow = "DataTables_Table_0 tbody tr:eq("+(index)+") td"; 
 	$('#'+searchRow).each(function (indexTd) {
 		 
		 if (indexTd == 1) {
 		 }   if(indexTd == 2) {
 			storeId=$(this).text();
		 }   if(indexTd == 3) {
 		 }   if(indexTd == 4) {
		 }  
	}); 
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/viewStoreDashboardAct.action?storeId='+storeId;
	$("#form1").submit();
	return true;
});

$(document).on('click','#TerminalView',function(event) {  
	index = $(this).attr('index');
 	var searchRow = "DataTables_Table_0 tbody tr:eq("+(index)+") td"; 
 	$('#'+searchRow).each(function (indexTd) {
 		 
		 if (indexTd == 1) {
 		 }  if(indexTd == 2) {
 		 }  if(indexTd == 3) {
 		 }  if(indexTd == 4) {
			terminalID=$(this).text();
		 }  
	}); 
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/viewTerminalDashboardAct.action?terminalID='+terminalID;
	$("#form1").submit();
	return true;
});
	
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
<form name="form1" id="form1" method="post" action=""> 
	<div id="content" class="span10">  
			<div>
					<ul class="breadcrumb">
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="generateMerchantAct.action?pid=9">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Merchant Dashboard </a></li>
					</ul>
			</div>
		   
	<div class="row-fluid sortable"><!--/span--> 
		<div class="box span12" id="groupInfo">
		  <div class="box-header well" data-original-title>Merchant Information
			  <div class="box-icon"> 
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
				<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
			  </div>
		  </div> 
			<div class="box-content"> 
				<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable" 
						id="DataTables_Table_0" >
					<thead>
						<tr >
							<th  style="width: 25px;">S No</th>
							<th>Merchant ID</th>
							<th>Store Id </th>
							<th class="hidden-phone">Store Location </th>
							<th>Terminal ID</th>
							<th class="center hidden-phone">Terminal Status</th>
							<th >Serial No</th>
							<th class="hidden-phone">Terminal, Authorized By</th>
						</tr>
					</thead> 
					<tbody  id="merchantTBody">
					</tbody>
				</table>
			  </div>
		</div> 
   </div> 
	<div class="form-action">
		<input type="button" id="non-printable" class="btn btn-success" onclick="JavaScript:window.print();" value="print" />
	</div> 
</div>	 
</form>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script> 
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
