
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
$(document).ready(function () {
	var tds=new Array();
	var merchantData ='${responseJSON.CreditDashboard}';
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
		
		//rowindex = ++rowindex;
		colindex = ++ colindex; 
		
		index=colindex-1;
		
			
		//alert(tds);
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
		"<td >"+colindex+"</td>"+
		"<td><a href='#'  id='MerchantView' index='"+rowindex+"' >"+v.storeId+"</a></span></td>"+	
		"<td> <a href='#' id='StoreView' index='"+rowindex+"' >"+v.ticketId+" </a></span> </td>"+ 
		"<td> <a href='#' id='TerminalView' index='"+rowindex+"' >"+v.referenceNo+"</a></span> </td>"+ 
		"<td>"+v.makerId+"</span></td>"+
		"<td>"+v.makerDate+"</span></td></tr>";
			
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
		 }   if(indexTd == 2) {
		 }   if(indexTd == 3) {
			terminalID=$(this).text();
		 }   if(indexTd == 4) {
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
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Branch Ticket Viewer</a>  </li> 
				</ul>
			</div> 
								  
			<div class="row-fluid sortable"><!--/span--> 
				<div class="box span12">
					  <div class="box-header well" data-original-title>Branch Ticket Information
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round" data-toggle="tooltip"><i class="icon-chevron-up"></i></a>
							<a href="#" class="btn btn-close btn-round" data-toggle="tooltip"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content" id="primaryDetails">
						<fieldset>
							<table width="950" class="table table-striped table-bordered bootstrap-datatable " 
							id="DataTables_Table_0"  >
								<thead>
									<tr >
										<th >S No</th>
										<th>Store Id</th>
										<th>Ticket No</th>
										<th>Reference No</th>
										<th>Created By</th>
										<th>Created Date</th>
									</tr>
								</thead> 
								<tbody  id="merchantTBody">
								</tbody>
							</table>
						</fieldset>
					</div>
                </div> 
			</div>
		<div class="form-actions">
			<input type="button" id="non-printable" class="btn btn-success" onclick="JavaScript:window.print();" value="print" />
		</div>  
	</div> 
</form>
 </body>
</html>
