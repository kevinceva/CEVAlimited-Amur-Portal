<!DOCTYPE html>
<html lang="en">
<%@taglib uri="/struts-tags" prefix="s"%> 
<head>
<meta charset="utf-8">
<title>MicroInsurance</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
	 
<script type="text/javascript" >

	    function getStores(){
	    	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/getExistingStoresAct.action';
	    	$("#form1").submit();
	    	return true;
	    }
	
	    
	    var userLinkData ='${USER_LINKS}';
		var jsonLinks = jQuery.parseJSON(userLinkData);
		var linkIndex = new Array();
		var linkName = new Array();
		var linkStatus = new Array();
		
	    $(document).ready(function () { 
			
			//console.log(userGroupData);
			$.each(jsonLinks, function(index, v) {
				linkIndex[index] = index;
				linkName[index] = v.name;
				linkStatus[index] = v.status;
			});  
			
			var storeData ='${responseJSON.STORE_LIST}';
			//alert(mydata);
			var json = jQuery.parseJSON(storeData);
			//alert(json);
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
				var rowCount = $('#storeTBody > tr').length;
				
				//rowindex = ++rowindex;
				colindex = ++ colindex; 
				
				var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
				"<td >"+colindex+"</td>"+
				"<td>"+v.storeId+"</span></td>"+	
				"<td>"+v.storeName+"</span> </td>"+ 
				"<td>"+v.merchantID+"</span> </td>"+ 
				"<td>"+v.CURRENT_AMOUNT+"</span> </td>"+ 
				"<td>"+v.CURRENT_LIMIT+"</span></td>"+
				"<td>"+v.RECOVERY_AMOUNT+"</span></td>"+
				"<td ><a id='recovery-create' class='btn btn-danger' href='#' index='"+rowindex+"'>Generate Recovery</a> &nbsp; </td></tr>";
					
					$("#storeTBody").append(appendTxt);	
					rowindex = ++rowindex;
			});
			
			
				
				$("#DataTables_Table_0_filter >label >input").live('keyup',function(){
					
					 storeSearchKey=$("#DataTables_Table_0_filter >label >input").attr('value').toUpperCase();
					//alert(storeSearchKey);
					var tabData=$(this).attr('aria-controls');
					//alert(tabData);
					var storeSearchKey=$(this).val();
					//alert(storeSearchKey);
					//alert($('#'+tabData+' tbody tr').length);
					var noOfRows=$('#'+tabData+' tbody tr').length;
					//alert(noOfRows);
					var tds=new Array();
					if(noOfRows==1){
						var i=1;
						$('#'+tabData+' tbody tr').each(function () {
							$('td',this).each(function (){
							 //alert($(this).text());
							 tds[i]=$(this).text();
							 i++;
							});
						});
						//alert(tds[2]+"--"+tds[4]);
						var TerminlaTable=tds[4].trim()+"_"+tds[2].trim()+"_"+"TERMINALS";
						//alert(TerminlaTable);
						for(var i=0;i<=terminalTables.length;i++){
							if(TerminlaTable==terminalTables[i]){
								//alert(terminalTables[i]);
								$("#"+TerminlaTable).show();
							}
						}
								
					}else{
						//alert("inside");
						for(var i=0;i<=terminalTables.length;i++){
								$("#"+terminalTables[i]).hide();
						}
					}

	    	});
			
			// Search For Top Layer
			$('#top-layer-anchor').find('a').each(function(index) {
				var anchor = $(this);   
				var flagToDo = false;
				 
				$.each(linkIndex, function(indexLink, v) {	 
				//console.log(linkName[indexLink] +" === "+ anchor.attr('id') +" >" + (linkName[indexLink] == anchor.attr('id')));
					if(linkName[indexLink] == anchor.attr('id'))  {
						flagToDo = true;
						} 
				}); 
				if(!flagToDo) {
					anchor.attr("disabled","disabled");
				} else {
					anchor.removeAttr("disabled");
				} 
			});
			
			//Search For The Links That Are Assigned To TABLE Level
			 $('table > tbody').find('a').each(function(index) {
				var anchor = $(this);   
				var flagToDo = false;
				 
				$.each(linkIndex, function(indexLink, v) {	 
				//console.log(linkName[indexLink] +" === "+ anchor.attr('id') +" >" + (linkName[indexLink] == anchor.attr('id')));
					if(linkName[indexLink] == anchor.attr('id'))  {
						flagToDo = true;
					} 
				}); 
				if(!flagToDo) {
					anchor.attr("disabled","disabled");
				} else {
					anchor.removeAttr("disabled");
				} 
				 
			}); 
			
	    }); 
		
$(document).on('click','a',function(event) {
	 var v_id=$(this).attr('id');
	 var disabled_status= $(this).attr('disabled'); 
	 var queryString = '?'; 
	 var v_action = "NO";
	 var storeId ="";
	 var merchantId ="";
	 var terminalId ="";
	 var merchantName ="";
	 
	 
	var index1 = $(this).attr('index');  
	var parentId =$(this).parent().closest('tbody').attr('id'); 
	var searchTrRows = parentId+" tr"; 
	var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
			 
 	if(disabled_status == undefined) {  
		  if (v_id ==  "recovery-create"  ) {
			
 			 
			 // Anchor Tag ID Should Be Equal To TR OF Index
			$(searchTdRow).each(function(indexTd) {  
				 if (indexTd == 1) {
					// Frequency
					storeId=$(this).text();
					//alert(storeId);
				 }   if(indexTd == 2) {
				 }   if(indexTd == 3) {
					merchantId=$(this).text();
 				 }   if(indexTd == 4) {
				 }  
			}); 

			if(v_id ==  "recovery-create") {  
				v_action = "getStoreCashDepositLimitCreateAct";
 			} 
			
			queryString+='merchantID='+merchantId+'&storeId='+storeId
			
		}  
	} else {
	
		// No Rights To Access The Link 
	}
	
	if(v_action != "NO") {
		$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/"+v_action+".action"+queryString;
		$("#form1").submit();
	}
}); 	
	</script>

	 
</head>

<body>
 <form name="form1" id="form1" method="post" action="">	 
	<div id="content" class="span10">  
		 
			  <div>
					 <ul class="breadcrumb">
						<li><a href="#">Home</a> <span class="divider"> &gt;&gt; </span></li>
						<li><a href="#">Recovery Management</a></li>
					</ul>
			</div>
			
		<table height="3">
			 <tr>
				<td colspan="3">
					<div class="messages" id="messages"> <font color="red"> <s:actionmessage /> </font> </div>
					<div class="errors" id="errors"> <font color="red"> <s:actionerror /> </font></div>
				</td>
			</tr>
		 </table>	 
								  
     <div class="row-fluid sortable"><!--/span--> 
		<div class="box span12"> 
				 
			<div class="box-header well" data-original-title>Store Information
				<div class="box-icon"> 
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
				</div>
			</div> 
			<div class="box-content">
				<fieldset>
					<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable" 
							id="DataTables_Table_0"   >
						<thead>
							<tr >
								<th>S No</th>
								<th>Store Id</th>
								<th>Store Name </th>
								<th>Merchant Id </th>
								<th>Current Amount</th>
								<th>Current Limit</th>'
								<th>Recovery Amount</th>
								<th>Actions</th>
							</tr>
						</thead> 
						<tbody  id="storeTBody">
						</tbody>
					</table>
				</fieldset>
            </div>
        </div>
    </div> 
</div> 
</form>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 	
</body>
</html>
