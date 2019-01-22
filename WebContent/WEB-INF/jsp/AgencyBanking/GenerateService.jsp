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
		
var userLinkData ='${USER_LINKS}';
var jsonLinks = jQuery.parseJSON(userLinkData);
var linkIndex = new Array();
var linkName = new Array();
var linkStatus = new Array();

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


$(document).ready(function () { 
	
	$.each(jsonLinks, function(index, v) {
		linkIndex[index] = index;
		linkName[index] = v.name;
		linkStatus[index] = v.status;
	});  
	
	var serviceSerachKey;  
			
	$("input[type=text]").live('keyup',function() {
 		var tabData=$(this).attr('aria-controls');
		var storeSearchKey=$(this).val();
		var hideData = "";
		
		var noOfRows=$('#'+tabData+' tbody tr').length;
		var tds=new Array();
		serviceSerachKey=$("#DataTables_Table_0_filter >label >input").attr('value').toUpperCase(); 
		if(serviceSerachKey.length == 0 ) { 
			for(var i=0;i<feeTables.length;i++){
				hideData=feeTables[i];
				$("#"+hideData).hide();
			} 
			for(var i=0;i<subServiceList.length;i++){
				hideData=subServiceList[i];
				$("#"+hideData).hide();
			} 
		 } else {
			if(storeSearchKey.length == 0) { 
				for(var i=0;i<feeTables.length;i++) {
					hideData=feeTables[i];
					$("#"+hideData).hide();
				}
				/*for(var i=0;i<storeList.length;i++){
					hideData=storeList[i];
					$("#"+hideData).hide();
				}*/		
			} else { 
				if(noOfRows==1){
					var i=1;
					$('#'+tabData+' tbody tr').each(function () {
						$('td',this).each(function (){
						 tds[i]=$(this).text();
						 i++;
						});
					});
					
					storeSearchKey=serviceSerachKey+"_SUBSERVICE";
					for(var i=0;i<subServiceList.length;i++){
						if(subServiceList[i] == storeSearchKey){
							reqStore=subServiceList[i];
 							$("#"+reqStore).show();
						} 
					}
					
					var FeeTable=tds[4]+"_FEE"; 
					$("#"+FeeTable).show();
					for(var i=0;i<=feeTables.length;i++){
						if(FeeTable == feeTables[i]){
							$("#"+FeeTable).show();
						}  
					} 	
				} else{
					//alert("inside"); 
				} 
			}
		}
	});
	
	
	
	var subServiceList = new Array();
	
	var merchantData ='${responseJSON.ServiceInfo}';
 	var json = jQuery.parseJSON(merchantData);
 	var val = 1;
	var rowindex = 0;
	var colindex = 0;
	var addclass = "";
	$.each(json, function(i, v) {
		if(val % 2 == 0 ) {
			addclass = "even";
			val++;
		} else {
			addclass = "odd";
			val++;
		}  
		var rowCount = $('#merchantTBody > tr').length;
		
 		colindex = ++ colindex; 
		
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"'> "+
			"<td >"+colindex+"</td>"+
			"<td><a href='#' id='SEARCH_NO'>"+v.serviceCode+"</a></td>"+	
			"<td>"+v.serviceName+"</td>"+ 
			"<td class='center hidden-phone'>"+v.makerId+" </td>"+ 
			"<td class='hidden-phone'>"+v.makerDate+" </td>"+
			"<td><p><a id='sub-service-create' class='btn btn-success' href='#' index='"+rowindex+"' title='Create Sub Service' data-toggle='tooltip'><i class='icon icon-plus icon-white'></i></a>&nbsp;<a  id='service-view' class='btn btn-info' href='#' index='"+rowindex+"' title='View' data-toggle='tooltip'><i class='icon icon-note icon-white'></i></a></p></td></tr>";
			
			$("#merchantTBody").append(appendTxt);	
			rowindex = ++rowindex;
 			subServiceList[i]=v.serviceCode+"_SUBSERVICE";
 	});

	var totalData ='${subServiceJSON}';
	var json = jQuery.parseJSON(totalData);
	var i=1;
	
	for(key in json) { 
		if(key!="ServiceInfo") {
 			var storeData=json[key];
			var storeval = 1;
			var storerowindex = 0;
			var storecolindex = 1;
			var addclass = "";
			
			var Mstore=key;
			var MTablle="SubServiceTable"+i;
			var storeTxt="<div class='row-fluid sortable' id='"+Mstore+"' >"
					+"<div class='box span12'>"
					+"<div class='box-header well' data-original-title> Sub Service Information"
					+"<div class='box-icon'><a href='#' class='btn btn-minimize btn-round'><i class='icon-chevron-up'></i></a> <a href='#' class='btn btn-close btn-round'><i class='icon-remove'></i></a>"
					+"</div>"
					+"</div>"
					+"<div class='box-content' id='"+MTablle+"'></div>"
					+"</div>"
					+"</div>";
			$("#subservices").append(storeTxt);
			var dataTable="DataTables_Table_"+i;
			var StoreTBody="SubServiceTBody"+i;
			var tableTxt="<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;  ' width='100%' class='table table-striped table-bordered bootstrap-datatable datatable' id='"+dataTable+"'  >"
					+"<thead><tr>"
					+"<th >S No</th>"
					+"<th class='hidden-phone'>Service Code</th>"
					+"<th class='center hidden-phone'>Service Name </th>"
					+"<th >Sub Service Code</th>"
					+"<th >Sub Service Name </th>"
					+"<th class='hidden-phone'>Date Created</th>"
					+"<th >Actions</th>"
					+"</tr></thead>"
					+"<tbody  id='"+StoreTBody+"'>"
					+"</tbody></table>";
				
			$("#"+MTablle).append(tableTxt); 
				
			$.each(storeData, function(i, v) {
				if(storeval % 2 == 0 ) {
					addclass = "even";
					storeval++;
				}
				else {
					addclass = "odd";
					storeval++;
				}  
				var appendTxt = "<tr class="+addclass+" index='"+storerowindex+"' id='"+storerowindex+"' > "+
							"<td>"+storecolindex+"</td>"+
							"<td class='hidden-phone'>"+v.serviceCode+"</td>"+	
							"<td class='center hidden-phone'>"+v.serviceName+"</td>"+ 
							"<td ><a href='#' id='SEARCH_NO'>"+v.subServiceCode+"</a></td>"+
							"<td>"+v.subServiceName+"</td>"+
							"<td class='hidden-phone'>"+v.makerDate+"</td>"+
							"<td><p> <a  class='btn btn-warning' href='#' id='fee-create' index='"+storerowindex+"' title='Create Fee' data-toggle='tooltip'><i class='icon icon-plus icon-white'></i></a>&nbsp;<a  class='btn btn-info' href='#' id='sub-service-view' index='"+storerowindex+"' title='View' data-toggle='tooltip'><i class='icon icon-note icon-white'></i></a></p></td></tr>";
				$("#"+StoreTBody).append(appendTxt);	
				storerowindex = ++storerowindex;
				storecolindex = ++ storecolindex;  
			});
			$("#"+Mstore).hide();
		}
		i++;
	}
		
		
	for(var i=0;i<subServiceList.length;i++){
		reqStore=subServiceList[i];
		$("#"+reqStore).hide();
	} 			
		 
	var feeTables=new Array();
	var terminalData ='${feeJSON}';
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
		feeTables[i]=key;
			var MTablle="TerminalTable"+i;
			var terminalTxt="<div class='row-fluid sortable' id='"+Mterminal+"' >"
			+"<div class='box span12'>"
			+"<div class='box-header well' data-original-title> Fee Information"
			+"<div class='box-icon'><a href='#' class='btn btn-minimize btn-round'><i class='icon-chevron-up'></i></a> <a href='#' class='btn btn-close btn-round'><i class='icon-remove'></i></a>"
			+"</div>"
			+"</div>"
			+"<div class='box-content' id='"+MTablle+"'></div>"
			+"</div>"
			+"</div>";
			$("#fees").append(terminalTxt);
			var dataTable="DataTables_Table__"+i;
			var terminalTBody="terminalTBody"+i;
			var tableTxt="<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;  ' width='100%' class='table table-striped table-bordered bootstrap-datatable  dataTable' id='"+dataTable+"' >"
				+"<thead><tr>"
				+"<th>S No</th>"
				+"<th>Fee Code</th>"
				+"<th class='hidden-phone'>Sub Service Code </th>"
				+"<th class='hidden-phone'>Service Code</th>"
				+"<th class='center hidden-phone'>Created By</th>"
				+"<th class='hidden-phone'>Created Date </th>"
				+"<th>Actions</th>"
				+"</tr></thead>"
				+"<tbody   id='"+terminalTBody+"'>"
				+"</tbody></table>";
			$("#"+MTablle).append(tableTxt);								
			
			//alert(terminalData);
			$.each(terminalData, function(i, v) {
				if(terminalval % 2 == 0 ) {
					addclass = "even";
					terminalval++;
				} else {
					addclass = "odd";
					terminalval++;
				}  
				var appendTxt = "<tr class="+addclass+" index='"+terminalrowindex+"' id='"+terminalrowindex+"'> "+
							"<td>"+terminalcolindex+"</td>"+
							"<td>"+v.feeCode+"</span></td>"+	
							"<td class='hidden-phone'>"+v.subServiceCode+"</span> </td>"+ 
							"<td class='hidden-phone'>"+v.serviceCode+"</span></td>"+
							"<td class='center hidden-phone'>"+v.makerId+"</span></td>"+
							"<td class='hidden-phone'>"+v.makerDate+"</span></td>"+
							"<td><a class='btn btn-warning' href='#' id='fee-view' index='"+terminalrowindex+"' title='View' data-toggle='tooltip'><i class='icon icon-note icon-white'></i></a></td></tr>";
								
							$("#"+terminalTBody).append(appendTxt);	
							terminalrowindex = ++terminalrowindex;
							terminalcolindex = ++ terminalcolindex; 
				
			});
			$("#"+Mterminal).hide();
		i++;
	}
		 
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
		var serviceCode ="";
		var serviceName ="";
		var subServiceCode ="";
		var feeCode ="";
		 
		var index1 = $(this).attr('index');  
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		var searchTrRows = parentId+" tr"; 
		var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
		
		if(disabled_status == undefined) {  
			if( v_id == 'sub-service-create'
				|| v_id == 'service-view' ) {
			
				 // Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) { 
					 if (indexTd == 1) {
						// Frequency
						serviceCode=$(this).text().trim();
					 }   if(indexTd == 2) {
						serviceName=$(this).text().trim();
					 }   if(indexTd == 3) {
					 }   if(indexTd == 4) {
					 }   
				}); 
				queryString+='serviceCode='+serviceCode;
				
				if(v_id == 'sub-service-create') {
					queryString+='&serviceName='+serviceName;
					v_action="subServiceCreateScreenAct";
				} else {
					v_action="serviceViewAct";
				} 
				
			}  else if( v_id == 'fee-create' 
						|| v_id == 'sub-service-view') {
						
				// Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {  
					if (indexTd == 1) {
						serviceCode=$(this).text().trim();
					 } else if(indexTd == 2) {
					 } else if(indexTd == 3) {
						subServiceCode=$(this).text().trim();
					 } else if(indexTd == 4) {
						feeCode=$(this).text().trim();
					 } 
				});
			
				queryString+='serviceCode='+serviceCode+'&subServiceCode='+subServiceCode+'&feeCode='+feeCode; 
				
				if( v_id == 'sub-service-view') 
					v_action="viewSubServiceAct";
				else v_action="createFeeAct";
				
			} else if( v_id == 'fee-view') {
				// Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {  
					if (indexTd == 1) {
						// Frequency
						feeCode=$(this).text().trim();
						//alert(terminalId);
					 } else if(indexTd == 2) {
					 } else if(indexTd == 3) {
					 } else if(indexTd == 4) {
					 } 
				}); 
			
				queryString+='feeCode='+feeCode;
				v_action="viewFeeDetailsAct";
			} else if( v_id == 'fee-dashboard') {  
				v_action="getFeeDashboardAct";
			} else if( v_id == 'add-new-service') {  
				v_action="getServiceScreenAct";
			} else if( v_id == 'register-bin') {  
				v_action="registerBinAct";
			} else if( v_id == 'register-process-code') {  
				v_action="registerProcessingCodeAct";
			} else if( v_id == 'register-huduma-service') {  
				v_action="registerHudumaServiceAct";
			} else if( v_id == 'view-register-bin') {  
				v_action="getBinViewDetAct";
			}else if( v_id == 'view-register-process-code') {  
				v_action="getProcessingCodeViewDetAct";
			}else if( v_id == 'view-register-huduma-service') {  
				v_action="getHudumaServiceViewDetAct";
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
		
	
function getBinView(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/getBinViewDetAct.action';
	$("#form1").submit();
	return true;
}

function getProcessingCodeView(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/getProcessingCodeViewDetAct.action';
	$("#form1").submit();
	return true;
}

function getHudumaCodeView(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/getHudumaServiceViewDetAct.action';
	$("#form1").submit();
	return true;
}
	
</script>

	 
</head>

<body>
 <form name="form1" id="form1" method="post" action="">	
	<!-- topbar ends -->
	 <div id="form1-content" class="span10">   
			<!-- content starts -->
			<div>
				<ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="serviceMgmtAct.action?pid=10">Fee Management</a></li>
				</ul>
			</div>  
			<span class="box-content" id="top-layer-anchor" > 
				<p> 
					<a href="#" id="add-new-service" class="btn btn-success ajax-link"  ><i class='icon icon-compose icon-white'></i>&nbsp;Add New Service</a>  
					<a href="#" id="register-bin" class="btn btn-info ajax-link"  ><i class='icon icon-plus icon-white'></i>&nbsp;Register Bin</a>  
					<a href="#" id="register-process-code"  class="btn btn-warning ajax-link"  ><i class='icon icon-star-on icon-white'></i>&nbsp;Register Processing Code</a>  
					<a href="#" id="register-huduma-service" class="btn btn-primary ajax-link"  ><i class='icon icon-calendar icon-white'></i>&nbsp;Register Huduma Service</a> 					
				</p> 
				<p> 
					<a href="#" id="fee-dashboard"  class="btn btn-warning ajax-link"  ><i class='icon icon-users icon-white'></i>&nbsp;Dashboard</a> 
					<a href="#" id="view-register-bin" class="btn btn-info ajax-link"  ><i class='icon icon-page icon-white'></i>&nbsp;View Register Bin</a>  
					<a href="#" id="view-register-process-code"  class="btn btn-warning ajax-link"   ><i class='icon icon-star-on icon-white'></i>&nbsp;View  Processing Code</a>  
					<a href="#" id="view-register-huduma-service" class="btn btn-primary ajax-link"  ><i class='icon icon-page icon-white'></i>&nbsp;View Huduma Service</a>
 				</p>
			</span>
								  
        <div class="row-fluid sortable"><!--/span--> 
			<div class="box span12">
				<div class="box-header well" data-original-title>Service Information
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content">
					<fieldset>
						<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable"  
							id="DataTables_Table_0" >
							<thead>
								<tr>
									<th>S No</th>
									<th>Service Code</th>
									<th>Service Name </th>
									<th class="center hidden-phone">Created By</th>
									<th class="hidden-phone">Created Date </th>
									<th>Actions</th>
								</tr>
							</thead> 
							<tbody id="merchantTBody">
							</tbody>
						</table>
					</fieldset>
				</div>
			</div>
        </div> 
		<div  id="subservices"> 
		</div>
		<div  id="fees"> 
		</div> 
		  			 
</div> 
</form>	
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
