
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
		var processingCode=(v.processingCode == undefined) ? "" : v.processingCode;
		var processingName=(v.processingName == undefined) ? "" : v.processingName;
		var makerId=(v.makerId == undefined) ? "" : v.makerId;
		var makerDate=(v.makerDate == undefined) ? "" : v.makerDate;
		index=colindex-1;
		//alert(index);
		var tabData="DataTables_Table_0";
			
			
		//alert(tds);
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
			"<td >"+colindex+"</td>"+
			"<td><a href='#'  id='MerchantView' index='"+rowindex+"' >"+processingCode+"</a></span></td>"+	
			"<td> <a href='#' id='StoreView' index='"+rowindex+"' >"+processingName+" </a></span> </td>"+ 
			"<td>"+makerId+"</span></td>"+
			"<td>"+makerDate+"</span></td></tr>";
			
			$("#merchantTBody").append(appendTxt);	
			rowindex = ++rowindex;
	});
	
});
	
function serviceMgmtAct(){
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
<form name="form1" id="form1" method="post" action="">	
<!-- topbar ends -->

	
		<div id="content" class="span10">  
			<div>
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="serviceMgmtAct.action?pid=10">Fee Management</a> <span class="divider"> &gt;&gt; </span></li>
				  <li><a href="#">View Transaction Processing Code</a></li>
				</ul>
			</div>
			  
        	<div class="row-fluid sortable"><!--/span--> 
				<div class="box span12"> 
					<div class="box-header well" data-original-title>Transaction Processing Code Information
					  <div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					  </div>
					</div>
                  
				<div class="box-content"> 
					<fieldset>
						<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable" 
							id="DataTables_Table_0">
							<thead>
								<tr >
									<th>S No</th>
									<th>Transaction Processing Code</th>
									<th>Transaction Processing Name</th>
									<th>Created By</th>
									<th>Created Date </th>
								</tr>
							</thead>

							<tbody  id="merchantTBody">
							</tbody>
						</table>
					</fieldset>
              </div>
             </div>
            </div>
			<div class="form-actions" id="form6-sumbit"> 
				<input type="button" id="non-printable" class="btn btn-success" onclick="serviceMgmtAct();" value="Next" />
			</div>  
	</div> 
</form>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script> 
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
