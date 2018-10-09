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
 			var json = jQuery.parseJSON(storeData);
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
 				colindex = ++ colindex; 
				
				var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
				"<td>"+colindex+"</td>"+
				"<td>"+v.storeId+" </td>"+	
				"<td>"+v.storeName+" </td>"+ 
				"<td>"+v.merchantID+" </td>"+ 
				"<td>"+v.makerDate+" </td>"+
				"<td><p><a id='store-limit-create' class='btn btn-success' href='#' index='"+rowindex+"' title='Create Cash in hand Limit' data-rel='tooltip'><i class='icon icon-plus icon-white'></i></a> &nbsp; <a  class='btn btn-warning' href='#' index='"+rowindex+"' id='store-limit-modify' title='Cash In Hand Modify' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a> &nbsp; <a  id='store-limit-view' class='btn btn-info' href='#' index='"+rowindex+"' title='Cash In Hand View' data-rel='tooltip'><i class='icon icon-book icon-white'></i></a> &nbsp;<a id='store-deposit-limit-create' class='btn btn-success' href='#' index='"+rowindex+"' title='Create Deposit Limit' data-rel='tooltip' data-rel='tooltip'><i class='icon icon-plus icon-white'></i></a> &nbsp; <a  class='btn btn-warning' href='#' index='"+rowindex+"' id='store-deposit-limit-modify' title='Deposit Limit Modify' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a> &nbsp; <a  id='store-deposit-limit-view' class='btn btn-info' href='#' index='"+rowindex+"' title='Deposit Limit View' data-rel='tooltip'><i class='icon icon-book icon-white'></i></a></p></td></tr>";
					
					$("#storeTBody").append(appendTxt);	
					rowindex = ++rowindex;
			});
			
			
			var terminalTables=new Array();
			var terminalData ='${terminalJSON}';
			var json = jQuery.parseJSON(terminalData);
			var i=0;
			for(key in json) {
							
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
						 +"<th>S No</th>"
						 +"<th>Terminal ID</th>"
						 +"<th>Store ID </th>"
						 +"<th>Merchant ID</th>"
						 +"<th>Status</th>"
						 +"<th>Date Created</th>"
						 +"<th>Actions</th>"
						 +"</tr></thead>"
						 +"<tbody  id='"+terminalTBody+"'>"
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
					var appendTxt = "<tr class="+addclass+" index='"+terminalrowindex+"' id='"+terminalrowindex+"'> "+
							"<td>"+terminalcolindex+"</td>"+
							"<td>"+v.terminalID+"</span></td>"+	
							"<td>"+v.storeId+"</span> </td>"+ 
							"<td>"+v.merchantID+"</span></td>"+
							"<td>"+v.status+"</span></td>"+
							"<td>"+v.makerDate+"</span></td>"+
							"<td> <a  class='btn btn-danger' href='#' id='terminal-limit-create' index='"+terminalrowindex+"' title='Create Terminal Limit' data-rel='tooltip'><i class='icon icon-plus icon-white'></i></a> &nbsp; <a  class='btn btn-warning' href='#' id='terminal-limit-modify' index='"+terminalrowindex+"' title='Modify Limit' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a> &nbsp; <a  class='btn btn-info' href='#' id='terminal-limit-view' index='"+terminalrowindex+"' title='View Limit' data-rel='tooltip'> <i class='icon icon-book icon-white'></i></a></td></tr>";
									
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
				var tabData=$(this).attr('aria-controls');
				var storeSearchKey=$(this).val(); 
				var noOfRows=$('#'+tabData+' tbody tr').length;
				var tds=new Array();
				var hideData = "";
				//storeSearchKey=$("#DataTables_Table_0_filter >label >input").attr('value').toUpperCase();
				 
				if(noOfRows==1){
					var i=1;
					$('#'+tabData+' tbody tr').each(function () {
						$('td',this).each(function (){
						 tds[i]=$(this).text();
						 i++;
						});
					});
					var TerminlaTable=tds[4].trim()+"_"+tds[2].trim()+"_"+"TERMINALS";
					for(var i=0;i<=terminalTables.length;i++){
						if(TerminlaTable==terminalTables[i]){
							$("#"+TerminlaTable).show();
						}
					}
							
				}else{
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
	
	if(v_id != 'SEARCH_NO') {
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
			  if (v_id ==  "store-limit-create" 
					|| v_id ==  "store-limit-modify"
					|| v_id == "store-limit-view"
					||	v_id ==  "store-deposit-limit-create" 
					|| v_id ==  "store-deposit-limit-modify"
					|| v_id == "store-deposit-limit-view") {
				
				 
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

				if(v_id ==  "store-limit-create") {  
					v_action = "getStoreLimitCreateScreenAct";
				} else if(v_id ==  "store-limit-modify") { 
					v_action="getStoreLimitModifyScreenAct";  
				} else if(v_id ==  "store-limit-view") { 
					v_action="getStoreLimitViewScreenAct";  
				}else if(v_id ==  "store-deposit-limit-create") {  
					v_action = "getStoreCashDepositLimitCreateAct";
				} else if(v_id ==  "store-deposit-limit-modify") { 
					v_action="getStoreCashDepositLimitModifyAct";  
				} else if(v_id ==  "store-deposit-limit-view") { 
					v_action="getStoreCashDepositLimitViewAct";  
				}
				
				queryString+='merchantID='+merchantId+'&storeId='+storeId
				
			}  else if (v_id ==  "terminal-limit-create" 
						|| v_id ==  "terminal-limit-modify" 
						|| v_id ==  "terminal-limit-view") { 
				 
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
				
				if(v_id ==  "terminal-limit-create") { 
					v_action="createTerminalLimitScreenAct";
				} else if(v_id ==  "terminal-limit-modify") { 
					v_action="modifyTerminalLimitScreenAct";
				} else if(v_id ==  "terminal-limit-view") { 
					v_action="viewTerminalLimitScreenAct"; 
				}   
			}  
		} else { 
			// No Rights To Access The Link 
		}
		
		if(v_action != "NO") {
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/"+v_action+".action"+queryString;
			$("#form1").submit();
		}
	} else {
		// The below code is for quick searching.
		var txt_sr = $(this).text().trim();
		var parentId =$(this).parent().closest('table').attr('id');  
		
		$('div input[type=text]').each(function(){
			if($(this).attr("aria-controls") == parentId)  {
				$(this).val(txt_sr);
				$(this).trigger("keyup");
			}  
		});
	
	}
}); 	
</script> 
</head> 
<body>
 <form name="form1" id="form1" method="post" action="">	 
	<div id="content" class="span10">   
		<div>
			 <ul class="breadcrumb">
				<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
				<li><a href="#">Float Management</a> <span class="divider"> &gt;&gt; </span></li>
				<li><a href="#">Limit Management</a></li>
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
					<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable"  
								id="DataTables_Table_0">
						<thead>
							<tr>
								<th>S No</th>
								<th>Store Id</th>
								<th>Store Name </th>
								<th>Merchant Id</th>
								<th>Date Created</th>
								<th>Actions</th>
							</tr>
						</thead> 
						<tbody id="storeTBody">
						</tbody>
					</table>
				</fieldset>
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
