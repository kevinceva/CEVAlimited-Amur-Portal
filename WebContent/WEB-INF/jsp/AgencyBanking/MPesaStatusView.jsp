
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
		
		//rowindex = ++rowindex;
		colindex = ++ colindex; 
		var storeCreditRefNo=(v.storeCreditRefNo == undefined) ? "" : v.storeCreditRefNo;
		var customerKey=(v.customerKey == undefined) ? "" : v.customerKey;
		var billerId=(v.billerId == undefined) ? "" : v.billerId;
		var amount=(v.amount == undefined) ? "" : v.amount;
		var mobileNo=(v.mobile == undefined) ? "" : v.mobile;
		var makerid=(v.makerId == undefined) ? "" : v.makerId;
		var makerDate=(v.makerDate == undefined) ? "" : v.makerDate;
		var status=(v.status == undefined) ? "" : v.status;
		if(status!="Awaiting Confirmation")
			status="<a href='#'  id='MPesaStatus' index='"+rowindex+"' >"+status+"</a>";
			
		index=colindex-1;
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
		"<td >"+colindex+"</td>"+
		"<td>"+storeCreditRefNo+"</span></td>"+	
		"<td>"+customerKey+" </span> </td>"+ 
		"<td>"+billerId+"</span> </td>"+ 
		"<td>"+amount+"</span></td>"+
		"<td>"+mobileNo+"</span></td>"+
		"<td>"+makerid+"</span></td>"+
		"<td>"+makerDate+"</span></td>"+
		"<td>"+status+"</span></td></tr>";
			
			$("#merchantTBody").append(appendTxt);	
			rowindex = ++rowindex;
	});
	
});
		
		 
$(document).on('click','#MPesaStatus',function(event) {
	var OpenWindow = window.open("","OpenWindow","width=500,height=600,resizable=no");
	index = $(this).attr('index');
	var searchRow = "DataTables_Table_0 tbody tr:eq("+(index)+") td"; 
	$('#'+searchRow).each(function (indexTd) {
		 if (indexTd == 1) {
			referenceNo=$(this).text();
		 }   if(indexTd == 2) {
			customerId=$(this).text();
		 }   if(indexTd == 3) {
			billerName=$(this).text();
		 }   if(indexTd == 4) {
			payAmount=$(this).text();
		 }   if(indexTd == 5) {
			mobileNo=$(this).text();
		 } if(indexTd == 6) {
			createdBy=$(this).text();
		 } if(indexTd == 7) {
			createdDate=$(this).text();
		 } if(indexTd == 8) {
			status=$(this).text();
		 }
	}); 
		var data="<html><head><link href='/CevaBase/css/AgencyBanking/bootstrap-united.min.css' rel='stylesheet'/>"+
				"<style type='text/css' media='print'> @media print"+
				"{"+
				"#non-printable { display: none; }"+
				"#printable {"+
				"display: block;"+
				"width: 100%;"+
				"height: 100%;"+
				"}"+
				"}"+
				"</style></head>"+
				"<p id='printable'><div><table><tr><td><img alt='Logo' align='left' src='/CevaBase/images/AgencyBanking/logo.png'></td></tr></table></div><br/>"+
				"<div><center><table class='table'>"+
				 "<tr class='even'><td>Reference No</td><td>"+referenceNo+"</td></tr>"+
				 "<tr class='odd'><td>Customer Id</td><td>"+customerId+"</td></tr>"+
				 "<tr class='even'><td>Biller Name</td><td>"+billerName+"</td></tr>"+
				 "<tr class='odd'><td>Payment Amount</td><td>"+payAmount+"</td></tr>"+
				 "<tr class='odd'><td>Mobile No</td><td>"+mobileNo+"</td></tr>"+
				 "<tr class='odd'><td>Created By</td><td>"+createdBy+"</td></tr>"+
				 "<tr class='odd'><td>Subscription Date</td><td>"+createdDate+"</td></tr>"+
				 "<tr class='odd'><td>Status</td><td>"+status+"</td></tr>"+
				 "</table></center></div></p>"+
				 "<div align='center'><input type='button' id='non-printable' class='btn btn-success' onclick='JavaScript:window.print();' value='print' /></div></html>";
		OpenWindow.document.write(data);
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
            			<!-- content starts -->
			    <div>
						<ul class="breadcrumb">
						  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
						  <li> <a href="#">Biller</a> <span class="divider"> &gt;&gt; </span></li>
						  <li><a href="#">MPesa Status </a></li>
						</ul>
				</div>
			<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
					  <div class="box-header well" data-original-title>MPesa Information
						  <div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						  </div>
					  </div>
                  
					<div class="box-content"> 
						<fieldset>
							<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
									id="DataTables_Table_0" >
								<thead>
									<tr >
										<th>S No</th>
										<th>Reference No</th>
										<th>Customer Id </th>
										<th>Biller Name</th>
										<th>Payment Amount</th>
										<th>Mobile No</th>
										<th>Created By</th>
										<th>Subscription Date</th>
										<th>Status</th>
										
									</tr>
								</thead> 
								<tbody  id="merchantTBody">
								</tbody>
							</table>
						</fieldset>
					  </div>
				</div>
			</div>
		</div> 
</form>
</body>
</html>
