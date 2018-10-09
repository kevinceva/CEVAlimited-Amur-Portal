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
	
	$("#refNO").hide();
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
						"<td>"+user_status+"</span></td>"+
						"<td>"+v.requestedBy+"</span></td>"+
						"<td>"+v.requestedDate+"</span></td>"+
						"<td>"+v.AUTH_TYPE+"</span></td>"+
						"<td style='display:none'>"+v.REF_NO+"</span></td>"+
						"<td><a id='treasury-store-limit-approve' class='"+status_class+"' href='#' index='"+rowindex+"' title='"+text1+"' data-rel='tooltip'>"+text+"</a> &nbsp;<a id='treasury-store-limit-approve-view' class='btn btn-info' href='#' index='"+rowindex+"' title='View Status' data-rel='tooltip'><i class='icon icon-page icon-white'></i></a></tr>";							
			$("#storeTBody").append(appendTxt);	
			rowindex = ++rowindex;
	});

	  
	
 	var terminalTables=new Array();
	var terminalData ='${terminalJSON}';
	var json = jQuery.parseJSON(terminalData);
	var i=0;
	for(key in json)
	{
					
		var terminalData=json[key];
		var terminalval = 1;
		var terminalrowindex = 0;
		var terminalcolindex = 1;
		var addclass = "";
		var merchant;
		var store;
		var Mterminal=key;
			var MTablle="TerminalTable"+i;
			var terminalTxt="<div class='row-fluid sortable' id='"+Mterminal+"' >"
			+"<div class='box span12'>"
			+"<div class='box-header well' data-original-title> Terminal Information"
			+"<div class='box-icon'><a href='#' class='btn btn-minimize btn-round'><i class='icon-chevron-up'></i></a> <a href='#' class='btn btn-close btn-round'><i class='icon-remove'></i></a>"
			+"</div>"
			+"</div>"
 			+"<div class='box-content' id='"+MTablle+"'></div>"
 			+"</div>"
			+"</div>";
			$("#terminals").append(terminalTxt);
			var dataTable="DataTables_Table__"+i;
			var terminalTBody="terminalTBody"+i;
			var tableTxt="<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;  ' width='100%' class='table table-striped table-bordered bootstrap-datatable datatable' id='"+dataTable+"'>"
				+"<thead><tr role='row'>"
				+"<th  >S No</th>"
				+"<th  >Terminal ID</th>"
				+"<th  >Station ID </th>"
				+"<th  >Organization ID</th>"
				+"<th>Terminal Limit</th>"
				+"<th>Limit Status</th>"
				+"<th>Requested By</th>"
				+"<th>Requested Date</th>"
				+"<th>Actions</th>"
				+"</tr></thead>"
				+"<tbody id='"+terminalTBody+"'>"
				+"</tbody></table>";
			$("#"+MTablle).append(tableTxt);								
			
		 
		$.each(terminalData, function(i, v) {
			if(terminalval % 2 == 0 ) {
				addclass = "even";
				terminalval++;
			}
			else {
				addclass = "odd";
				terminalval++;
			}  
			
			var user_status="";
			var status_class ="";
			var text = "";
			var text1 = "";
			
			if(v.terminalLimitStatus == 'Approved') {
				user_status = "<a href='#' class='label label-success' index='"+rowindex+"'>"+v.terminalLimitStatus+"</a>";
				status_class = "btn btn-danger";
				text = "<i class='icon icon-locked icon-white'></i>&nbsp;";
				text1 = "Reject";
			} else  {
				user_status = "<a href='#'  class='label label-warning'  index='"+rowindex+"'>"+v.terminalLimitStatus+"</a>";
				status_class = "btn btn-success";
				text = "<i class='icon icon-unlocked icon-white'></i>&nbsp;";
				text1 = "Approve";
			} 
				
			var appendTxt = "<tr class="+addclass+" index='"+terminalrowindex+"' id='"+terminalrowindex+"' > "+
						"<td  >"+terminalcolindex+"</td>"+
						"<td >"+v.terminalID+"</span></td>"+	
						"<td >"+v.storeId+"</span> </td>"+ 
						"<td>"+v.merchantID+"</span></td>"+
						"<td> kshs "+v.terminalLimit+" /= </span></td>"+
						"<td>"+user_status+"</span></td>"+
						"<td>"+v.terminalRequestedBy+"</span></td>"+
						"<td>"+v.terminalRequestedDate+"</span></td>"+
						"<td ><a id='terminal-limit-approve' class='"+status_class+"' href='#' index='"+terminalrowindex+"' title='"+text1+"' data-rel='tooltip'>"+text+"</a> &nbsp;<a id='terminal-limit-approve-view' class='btn btn-info' href='#' index='"+terminalrowindex+"' title='View Status' data-rel='tooltip'><i class='icon icon-page icon-white'></i></a></tr>";

						$("#"+terminalTBody).append(appendTxt);	
						terminalrowindex = ++terminalrowindex;
						terminalcolindex = ++ terminalcolindex; 
						merchant=v.merchantID;
						store=v.storeId;
			
		});
		$("#"+Mterminal).hide();
		i++;
		terminalTables[i]=merchant+"_"+store+"_TERMINALS";
	}
	
		
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
		var authType = "";
		var referenceno ="";
		var cardNumber = "";
		var posRRN="";
	var index1 = $(this).attr('index');  
	var parentId =$(this).parent().closest('tbody').attr('id'); 
	var searchTrRows = parentId+" tr"; 
	var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
			 
 	if(disabled_status == undefined) {  
		  if (v_id ==  "treasury-store-limit-approve" 
				|| v_id ==  "treasury-store-limit-approve-view"  ) { 
 			 
			 // Anchor Tag ID Should Be Equal To TR OF Index
			$(searchTdRow).each(function(indexTd) {  
				
				 if (indexTd == 1) {
					storeId=$(this).text();
				 }
				 if(indexTd == 2) {
					merchantId=$(this).text();
				 }
				 if(indexTd == 3) {
				 }
				 if(indexTd == 4) {
				 }
				 if(indexTd == 7) {
					authType=$(this).text();
					if(authType=="Cheque Approval"){
						cardNumber=storeId;
					}
				 } 
				 if(indexTd == 8) {
					referenceno=$(this).text();
				 }  
			});  
			 
			if(authType!="Cheque Approval")
				queryString += 'merchantID='+merchantId+'&storeId='+storeId+'&storeCreditRefNo='+referenceno;
			else
				queryString += 'cardNumber='+cardNumber+'&referenceno='+referenceno; 
				
			if(v_id ==  "treasury-store-limit-approve") {  
				if(authType=="Cash in Hand Limit") 
					v_action = "getStoreLimitApproveAct";
				else if(authType=="Terminal Limit") 
					v_action = "getStoreCashDepositLimitApproveAct";
				else if(authType=="Cash Credit") 
					v_action = "getStoreCreditApproveAct";
				else if(authType=="Cheque Approval") 
					v_action = "chequeApprovalAct";
 			} else if(v_id ==  "treasury-store-limit-approve-view") {  
				if(authType=="Cash in Hand Limit") 
					v_action = "getStoreLimitApproveViewScreenAct";
				else if(authType=="Terminal Limit") 
					v_action = "getStoreCashDepositLimitApproveViewScreenAct";
				else if(authType=="Cash Credit") 
					v_action = "getStoreCreditApproveViewScreenAct";
 			}
			
			
		}  else if (v_id ==  "terminal-limit-approve" 
					|| v_id ==  "terminal-limit-approve-view"  ) { 
 			 
			// Anchor Tag ID Should Be Equal To TR OF Index
					$(searchTdRow).each(function(indexTd) {  
						 if (indexTd == 1) {
							// Frequency
							terminalId=$(this).text();
							//alert(terminalId);
						 } else if(indexTd == 2) {
							// Time or Date
							storeId=$(this).text();
							//alert(storeId);
						 } else if(indexTd == 3) {
							merchantId=$(this).text();
							//alert(merchantId);
						 } else if(indexTd == 4) {
							merchantName=$(this).text();
						 } 
					}); 
				 
					queryString += 'terminalID='+terminalId+'&storeId='+storeId+'&merchantID='+merchantId;
					
					if(v_id ==  "terminal-limit-approve") {  
						v_action = "getTerminalLimitApproveAct";
		 			} else v_action = "getTerminalLimitApproveViewAct";
				} 
				else if (v_id ==  "merchant-settlement-approve"  ) { 
				 
			// Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {  
				
				 if(indexTd == 4) {
					posRRN=$(this).text();
				 } 
			}); 
		 
			queryString += 'posRRN='+posRRN;
			
			if(v_id ==  "merchant-settlement-approve") {  
				v_action = "merchantSettlementApproveAct";
			} 
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
	<!-- topbar ends -->
	
		 
			<div id="content" class="span10">  
			  <div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Treasury Authorization</a> </li>
				</ul>
			</div> 
								  
        <div class="row-fluid sortable"><!--/span--> 
			<div class="box span12">
                  <div class="box-header well" data-original-title>Treasury Authorization Requests
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round" data-rel="tooltip"><i class="icon-chevron-up"></i></a>
						<a href="#" class="btn btn-close btn-round" data-rel="tooltip"><i class="icon-remove"></i></a> 
					</div>
				</div>
                      
					<div class="box-content">
						<fieldset>
						<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable" 
								id="DataTables_Table_0">
							<thead>
								<tr >
									<th>S No</th>
									<th>Station Id</th>
									<th>Organization Id </th>
									<th>Amount </th>
									<th>Status</th>
									<th>Requested By</th>
									<th>Requested Date</th>
									<th>Authorization Type</th>
									<th id="refNO">Reference No</th>
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
	  <div  id="terminals"> 
		</div>   
		
		<!--  <div class="row-fluid sortable"> 
			<div class="box span12">
                  <div class="box-header well" data-original-title>Merchant Settlement Authorization Requests
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round" data-rel="tooltip"><i class="icon-chevron-up"></i></a>
						<a href="#" class="btn btn-close btn-round" data-rel="tooltip"><i class="icon-remove"></i></a> 
					</div>
				</div>
                      
					<div class="box-content">
						<fieldset>
						<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable" 
								id="DataTables_Table_1">
							<thead>
								<tr >
									<th>S No</th>
									<th>Store ID</th>
									<th>Store Name </th>
									<th>Store Account Number</th>
									<th>POS RRN</th>
									<th>TXN Date</th>
									<th>Terminal No.</th>
									<th>Amount</th>
									<th>Maker Id</th>
									<th>Actions</th>
								</tr>
							</thead> 
							<tbody  id="settleTBody">
							</tbody> 
						</table>
						</fieldset>
                    </div>
			</div>
         </div>    -->						 
</div>  		 
</form>  
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.js'></script>  
</body>
</html>
