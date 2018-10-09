
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
$(document).ready(function() { 
	
		
	var storeData ='${responseJSON.MERCHANT_LIST}';
	var json = jQuery.parseJSON(storeData);
	var val = 1;
	var rowindex = 1;
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
		var rowCount = $('#storeTBody > tr').length;
		
		colindex = ++ colindex; 
		
		sttd=v.STATUS;
		
		if( sttd.trim() == 'SUCCESS') {
			sttd = "<a href='#' class='label label-success' index='"+rowindex+"'>"+sttd+"</a>";
		    } 
		else {
		    	sttd = "<a href='#' class='label label-warning' index='"+rowindex+"'>"+sttd+"</a>";
		    }
		
		 
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
		"<td >"+colindex+"</td>"+
		"<td><a href='#' id='SEARCH_NO' value='CLIENTID@"+v.PURCHASE_ID+"' aria-controls='DataTables_Table_0'>"+v.PURCHASE_ID+"</span></td>"+
		"<td>"+v.PRIM_AMT+"</span></td>"+
		"<td>"+v.SUM_ASUR+"</span></td>"+
		"<td>"+v.MKRDT+"</span></td>"+
		"<td>"+sttd+"</span></td>";
			$("#tbody_data").append(appendTxt);	
			rowindex = ++rowindex;
			
	});	
	
	
	$('#btn-submit').live('click',function() { 
		var srhval = '${responseJSON.srchval}';
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcentersearch.action?";
		$("#form1").submit();					
	}); 
	
});
 
	
</script>
 
</head>

<body>
 <form name="form1" id="form1">
	<div id="content" class="span10">
            			<!-- content starts -->
		<div>
				<ul class="breadcrumb">
				  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="#">Call Center</a> <span class="divider"> &gt;&gt; </span></li>
				  <li><a href="#">Purchase Information</a></li>
				</ul>
		</div>		
		<div class="row-fluid sortable"><!--/span--> 
			<div class="box span12"> 
				<div class="box-header well" data-original-title>
						<i class="icon-edit"></i>Purchase Details
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					</div>
				</div> 
	 

				<div id="terminalLimitDetails">
					<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
							id="DataTables_Table_Purchase"  >
						  <thead>
								<tr >
									<th>S No</th>
									<th>Purchase ID</th>
									<th>Premium Amount</th>
									<th>Sum Assured</th>
									<th class="center hidden-phone">Creation Date</th>
									<th>Status</th>
								</tr>
						  </thead>    
						 <tbody  id="tbody_data">
						 </tbody>
					</table> 
				</div>
			</div>
		</div>

		<div class="form-actions">
			 <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Back"   ></input>
			<input type="hidden" name="srchval"  id="srchval" value="${responseJSON.srchval}" />
		</div> 
	</div>	
</form>
</body>
</html>
