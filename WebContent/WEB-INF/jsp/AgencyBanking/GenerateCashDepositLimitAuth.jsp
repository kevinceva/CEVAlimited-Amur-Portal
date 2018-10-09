<!DOCTYPE html>
<html lang="en">
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
				
				var user_status="";
				var status_class ="";
				var text = "";
				var text1 = "";
				
				if(v.storeLimitStatus == 'Approved') {
					user_status = "<a href='#' class='label label-success' index='"+rowindex+"'>"+v.storeLimitStatus+"</a>";
					status_class = "btn btn-danger";
					text = "<i class='icon icon-locked icon-white'></i>&nbsp;";
					text1 = "Reject";
				} else {
					user_status = "<a href='#'  class='label label-warning'  index='"+rowindex+"'>"+v.storeLimitStatus+"</a>";
					status_class = "btn btn-success";
					text = "<i class='icon icon-unlocked icon-white'></i>&nbsp;";
					text1 = "Approve";
				} 
				
				
				var appendTxt = "<tr class="+addclass+" id='"+rowindex+"' index='"+rowindex+"'> "+
						"<td >"+colindex+"</td>"+
						"<td>"+v.storeId+"</span></td>"+	
						"<td>"+v.merchantID+"</span> </td>"+ 
						"<td> kshs "+v.storeLimit+" /= </span> </td>"+ 
						"<td>"+v.storeLimitStatus+"</span></td>"+
						"<td>"+v.requestedBy+"</span></td>"+
						"<td>"+v.requestedDate+"</span></td>"+
						//"<td class='center '><a id='store-cd-limit-approve' class='btn btn-danger' href='#' index='"+rowindex+"'>Approve/Reject</a> &nbsp;<a id='store-cd-limit-approve-view' class='btn btn-danger' href='#' index='"+rowindex+"'>View Status</a></tr>";
						"<td class='center'><a id='store-cd-limit-approve' class='"+status_class+"' href='#' index='"+rowindex+"' title='"+text1+"' data-rel='tooltip'>"+text+"</a> &nbsp;<a id='store-cd-limit-approve-view' class='btn btn-info' href='#' index='"+rowindex+"' title='View Status' data-rel='tooltip'><i class='icon icon-page icon-white'></i></a></tr>";							
					$("#storeTBody").append(appendTxt);	
					rowindex = ++rowindex;
			});
			
			
				$("#DataTables_Table_0_filter >label >input").live('keyup',function(){
					
					 storeSearchKey=$("#DataTables_Table_0_filter >label >input").attr('value');
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
						var TerminlaTable=tds[3].trim()+"_"+tds[2].trim()+"_"+"TERMINALS";
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
	var merchantName  ="";
	 
	var index1 = $(this).attr('index');  
	var parentId =$(this).parent().closest('tbody').attr('id'); 
	var searchTrRows = parentId+" tr"; 
	var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
			 
 	if(disabled_status == undefined) {  
		  if (v_id ==  "store-cd-limit-approve" 
				|| v_id ==  "store-cd-limit-approve-view"  ) { 
 			 
			 // Anchor Tag ID Should Be Equal To TR OF Index
			$(searchTdRow).each(function(indexTd) {  
				 if (indexTd == 1) {
					storeId=$(this).text();
				 }   if(indexTd == 2) {
					merchantId=$(this).text();
				 }   if(indexTd == 3) {
				 }   if(indexTd == 4) {
				 }  
			});  
			
			queryString += 'merchantID='+merchantId+'&storeId='+storeId;  
			if(v_id ==  "store-cd-limit-approve") {  
				v_action = "getStoreCashDepositLimitApproveAct";
 			} else v_action = "getStoreCashDepositLimitApproveViewScreenAct";
			
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
 
			<!-- content starts -->
			  <div>
				 <ul class="breadcrumb">
					<li><a href="#">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Cash Deposit Limit</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Authorization</a></li>
				</ul>
			</div> 			  
    <div class="row-fluid sortable"><!--/span--> 
		<div class="box span12">
                  <div class="box-header well" data-original-title>Store Information
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				  </div>
                  
            <div class="box-content" > 
				<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable" 
					id="DataTables_Table_0"  >
					<thead>
						<tr >
							<th>S No</th>
							<th>Store Id</th>
							<th>Merchant Id </th>
							<th>Store Limit</th>
							<th>Limit Status</th>
							<th>Requested By</th>
							<th>Requested Date</th>
							<th>Actions</th>
						</tr>
					</thead> 
					<tbody  id="storeTBody">
					</tbody>
				</table>
             </div>
          </div>
        </div>  
		  <div  id="terminals"> 
		</div>   
	</div> 
 </form> 
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script>  
</body>
</html>
