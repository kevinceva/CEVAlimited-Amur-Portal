
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
 		
$(document).ready(function () {
	var tds=new Array();
	var merchantData ='${responseJSON.CLN_LIST}';
	console.log("merchantData ---"+merchantData);
	var json = jQuery.parseJSON(merchantData);
	console.log("json ---"+json);
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
		var rowCount = $('#tBody > tr').length;
		
		//rowindex = ++rowindex;
		colindex = ++ colindex; 
		 var status=(v.status == undefined) ? "" : v.status;
		var cid=(v.cid == undefined) ? "" : v.cid;
		var idno=(v.idno == undefined) ? "" : v.idno;
		var cdate=(v.cdate == undefined) ? "" : v.cdate;
		var mob=(v.mob == undefined) ? "" : v.mob;
		var cname=(v.cname == undefined) ? "" : v.cname;
	
		index=colindex-1;
		console.log(" mobile "+mob);
			var tabData="DataTables_Table_0";
			
			//var gmob='<a href="#" id="'+mob+'"></a>';
			 mob="<td ><a href='#' id='"+mob+"' value='"+mob+"' aria-controls='DataTables_Table_0'>"+mob+"</span></td>";
			
			//rid='<a href="#" id="QA-APP">Queries Approved</a>';
		//alert(tds);
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
		"<td >"+colindex+"</td>"+
		"<td >"+cid+"</td>"+
		"<td >"+cname+"</td>"+
		"<td >"+idno+"</td>"+
		mob+
		"<td >"+cdate+"</td>"+
		"<td >"+status+"</td>"+"</tr>";

		console.log("appendTxt..."+appendTxt);
			
		$("#tBody").append(appendTxt);	
		rowindex = ++rowindex;
	});
	
	
	
	
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


$(document).on('click','a',function(event) {
	var v_id=$(this).attr('id') ;
	var queryString = 'srchval='+v_id;
	
	 if (v_id != undefined  )
	{
		postData("callcentersearch.action",queryString);
	} 
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
					  <li><a href="callcenter.action">Call Center</a></li>
					</ul>
				</div>
				
		<div class="row-fluid sortable"><!--/span-->
        
			<div class="box span12">
        
                  <div class="box-header well" data-original-title>Client Information
					  <div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					  </div>
				  </div>
                  
            <div class="box-content">
				<fieldset>
					<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable" 
						id="DataTables_Table_0" >
						<thead>
							<tr >
								<th width="7%">S No</th>
								<th>Client ID</th>
								<th>Client Name</th>
								<th>ID Number</th>
								<th>Mobile Number</th>
								<th>Created Date</th>
								<th>Status</th>
							</tr>
						</thead> 
						<tbody  id="tBody">
						</tbody>
						
					</table>
				</fieldset> 
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
