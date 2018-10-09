 <!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>CEVA </title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
	
<style type="text/css">
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
label.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
.errmsg {
color: red;
}
 
</style>    
<script type="text/javascript" >

$(document).ready(function() {
	
	var myData =jQuery.parseJSON('${resJson.aaData}');
	
	var myCompleteData = new Array();
	var myDataList = new Array();
	
	
	
	function getData(){
		
		var returnData = new Array();
		$.each(myData, function(i, obj) {
			returnData = new Array();
			returnData[0]=obj.TEXT1;
		    returnData[1]=obj.TEXT2;
		    returnData[2]=obj.TEXT3;
		    myCompleteData.push(returnData);
		});
		
		return myCompleteData;
	}
	
	var iTotalRecordCnt = '${resJson.iTotalRecords}';
	var iTotalDisplayRecordsCnt = '${resJson.iTotalDisplayRecords}';
	
	
	console.log(iTotalRecordCnt +" ]count in other ["+iTotalDisplayRecordsCnt);
	$("#DataTables_Table_0").dataTable({
		
		
			"processing": true,
        	"serverSide": true,
        	"filter" : true,
        	"sPaginationType": "full_numbers",
            "sAjaxSource": "<%=ctxstr%>/<%=appName%>/pagenationExAct.action",
            "sEcho": 3,
            "iTotalRecords": iTotalRecordCnt,
            "iDisplayStart": 0,
            "iTotalDisplayRecords": 20,
            "iDisplayLength": 20,
            "aaData": getData(),
            "deferLoading": 57,
            "aoColumns": [
                          { "aaData": "Text1" },
                          { "aaData": "Text2" },
                          { "aaData": "Text3" }
                      ] 
      
    });
	
	$(".prev disabled").remove();
	
});
</script> 
		
</head>

<body>
	<form name="form1" id="form1" method="post" action="">	
	 
	<div id="form1-content" class="span10">   
		 					  
           <div class="row-fluid sortable"><!--/span-->
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>Sample Information
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content"> 
					<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable"  id="DataTables_Table_0" >
						<thead>
							<tr>
								<th>Text1</th>
								<th>Text2</th>
							    <th>Text3</th>  
							</tr>
						</thead> 
						 <tbody id="tbody">
						</tbody>  
					</table>
				</div>
			</div>
		</div> 
		 	 
		</div> 
	</div> 
	 
	<div  id="users"> 
	</div> 
   
</form>
 
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
 