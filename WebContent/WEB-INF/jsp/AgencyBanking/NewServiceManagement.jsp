<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title> </title>
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
function postData(actionName,paramString){
	$('#form2').attr("action", actionName)
			.attr("method", "post");
	
	var paramArray = paramString.split("&");
	var input = "" ;
	$(paramArray).each(function(indexTd,val) {
		if(val != "") {
			input = $("<input />").attr("type", "hidden").attr("name", val.split("=")[0]).val(val.split("=")[1].trim());
			$('form').append($(input));	 
		}
	});	

	$('form').submit();	
} 
var storeDetails	= "";
var terminalDetails	= "";

var userLinkData ='${USER_LINKS}';
var jsonLinks = jQuery.parseJSON(userLinkData);
var linkIndex = new Array();
var linkName = new Array();
var linkStatus = new Array();
var terminalTables=new Array(); //  store_TERMINALS;
var storeList = new Array(); // merchantid_STORES

$(document).ready(function () { 
	var storeData ='${responseJSON.SERVICE_LIST}';
	//alert(mydata);
	var json = jQuery.parseJSON(storeData);
	//alert(json);
	var val = 1;
	var rowindex = 0;
	var colindex = 1;
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
		//colindex = ++ colindex; 
		
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
		"<td >"+colindex+"</td>"+
		"<td><a href='#' id='SEARCH_NO' value='SERVICE@"+v.SERVICE_CODE+"' aria-controls='DataTables_Table_0'>"+v.SERVICE_CODE+"</span></td>"+	
		"<td>"+v.SERVICE_NAME+"</span> </td>"+ 
		"<td>"+v.MAKER_ID+"</span> </td>"+ 
		"<td>"+v.MAKER_DTTM+"</span></td>"+
		"<td><p><a id='sub-service-create' class='btn btn-success' href='#' index='"+colindex+"' title='Create Sub Service' data-rel='tooltip'><i class='icon icon-plus icon-white'></i></a>&nbsp;<a  id='service-view' class='btn btn-info' href='#' index='"+colindex+"' title='View' data-rel='tooltip'><i class='icon icon-note icon-white'></i></a></p></td></tr>";
			
			$("#merchantTBody").append(appendTxt);	
			rowindex = ++rowindex;
			colindex = ++colindex;
	});

	
		//console.log(userGroupData);
		$.each(jsonLinks, function(index, v) {
			linkIndex[index] = index;
			linkName[index] = v.name;
			linkStatus[index] = v.status;
		}); 
		
		$("input[type=search]").live('keyup',function() {
			
			var ariaControlsval=$(this).attr('aria-controls');
			var contVal=$(this).val();
			//alert(ariaControlsval+"------------"+contVal)
			if(ariaControlsval=="DataTables_Table_0" && contVal=="" ) {
				$("#stores").hide();
				$("#terminals").hide();
			} 
			else if(ariaControlsval=="DataTables_Table_Store" && contVal=="" ) {
				$("#terminals").hide();
			} 
			
				
		});  
		
		 $(document).on('click','a',function(event) {
		    	
	    	var v_id=$(this).attr('id');
	    	var typeOFReq = $(this).attr('value').split("@")[0];
	    	//alert(typeOFReq);
			if(typeOFReq == "SERVICE"){
				var serviceCode = $(this).attr('value').split("@")[1];
		    	//alert(serviceCode);
		    	
		    	var t = $('#DataTables_Table_Store').DataTable();
				t.clear().draw();
				
				var queryString = "serviceCode="+serviceCode;	
				$.getJSON("subServiceListJsonAct.action", queryString,function(data) { 
					
					var storeData =data.responseJSON.SUBSERVICE_LIST;
					var json = storeData;
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
						 
						var lastTd="<p> <a  class='btn btn-warning' href='#' id='fee-create' index='"+colindex+"' title='Create Fee' data-rel='tooltip'><i class='icon icon-plus icon-white'></i></a>&nbsp;<a  class='btn btn-info' href='#' id='sub-service-view' index='"+colindex+"' title='View' data-rel='tooltip'><i class='icon icon-note icon-white'></i></a></p> &nbsp;&nbsp;";
						var firstTd = "<a href='#' id='SEARCH_NO' value='SUBSERVICE@"+v.SUB_SERVICE_CODE+"' >"+v.SUB_SERVICE_CODE+"</a>";
						
					        var i= t.row.add( [
								rowindex,
								firstTd,
								v.SUB_SERVICE_NAME,
								v.SERVICE_CODE,
								v.SERVICE_NAME,
								v.MAKER_DTTM,
								lastTd
					        ] ).draw( false );
					        t.rows(i).nodes().to$().attr("id", rowindex);
					        t.rows(i).nodes().to$().attr("index", rowindex);
					 
					        rowindex = ++rowindex;
					        colindex = ++colindex;
					});		
		 		    
				 	$("#stores").show();
				});

			}
			else if(typeOFReq == "SUBSERVICE"){
				var subServiceCode = $(this).attr('value').split("@")[1];
		    	//alert(subServiceCode);
		    	
				var queryString = "subServiceCode="+subServiceCode;	
				$.getJSON("feeListJsonAct.action", queryString,function(data) { 
					
					var terminalTable = $('#DataTables_Table_Terminal').DataTable();
					terminalTable.clear().draw();
					var storeData =data.responseJSON.FEE_LIST;
					var json = storeData;
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
						 
						var lastTd="<p><a class='btn btn-warning' href='#' id='fee-view'  title='View' data-rel='tooltip'><i class='icon icon-note icon-white'></i></a> </p> &nbsp;";
						var firstTd = "<a href='#' id='SEARCH_NO' value='FEE-"+v.FEE_CODE+"' >"+v.FEE_CODE+"</a>";
							
							var i=terminalTable.row.add( [
								rowindex,
								firstTd,
								v.SUB_SERVICE_CODE,
								v.SERVICE_CODE,
								v.MAKER_ID,
								v.MAKER_DTTM,
								lastTd
					        ] ).draw( false );
					 
							terminalTable.rows(i).nodes().to$().attr("id", rowindex);
							terminalTable.rows(i).nodes().to$().attr("index", rowindex);
					        rowindex = ++rowindex;
					        colindex = ++colindex;
					});	
					
				 	$("#terminals").show();
				});

			}
			
			var txt_sr = $(this).attr('value').split("@")[1];
			//alert(txt_sr);
			var parentId =$(this).parent().closest('table').attr('id'); 
			
			var ariaControlsval=$(this).attr('aria-controls');
			//alert(ariaControlsval);
			//alert(txt_sr+"----"+parentId);
			$('div input[type=search]').each(function(){
				
				if(ariaControlsval == parentId) {
					$("#"+ariaControlsval).val(txt_sr);
					$("#"+ariaControlsval).trigger("keyup");
				} 
			});
			
				
	    }); 
		
		// Search For Top Layer
		$('#top-layer-anchor').find('a').each(function(index) {
			var anchor = $(this);   
			var flagToDo = false;
			 
			$.each(linkIndex, function(indexLink, v) {	 
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
	 //alert("v_id::"+v_id);
	if(v_id != 'SEARCH_NO') {
		var disabled_status= $(this).attr('disabled'); 
		var queryString = '?'; 
		var v_action = "NO";
		var serviceCode ="";
		var serviceName ="";
		var subServiceCode ="";
		var feeCode ="";
		//console.log(this); 
		  
		var index1 = $(this).parent().closest('tr').attr('index');
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		var searchTrRows = parentId+" tr"; 
		var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
		
		if(disabled_status == undefined) {  
			//alert('disabled_status..:'+disabled_status)
			if( v_id == 'sub-service-create'
				|| v_id == 'service-view' ) {
				
				 // Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {
					//console.log(indexTd);
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
						subServiceCode=$(this).text().trim();
						//alert(subServiceCode);
					 } else if(indexTd == 2) {
					 } else if(indexTd == 3) {
						 serviceCode=$(this).text().trim();
					 } else if(indexTd == 4) {
						feeCode=$(this).text().trim();
					 } 
				});
				
				queryString+='serviceCode='+serviceCode+'&subServiceCode='+subServiceCode+'&feeCode='+feeCode; 
				//alert(queryString);
				
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
			return false;
		} 
	} else {
		// The below code is for quick searching.
		//alert("hello");
		var txt_sr = $(this).text().trim();
		//alert(txt_sr);
		var parentId =$(this).parent().closest('table').attr('id');  
		//alert(parentId);
		$('div input[type=search]').each(function(){
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
		 
			  <div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#"> Fee Management</a></li>
				</ul>
			</div>
			
			<div class="box-content" id="top-layer-anchor">
				 <div>
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
			 	</div>	
			</div>
								  
           <div class="row-fluid sortable"><!--/span-->
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>Service Information
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content"> 
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
				</div>
			</div>
		</div> 
		
		<!-- Loading Stores -->
	 	<div  id="stores"  style="display:none">  
             <div class="row-fluid sortable" id='<s:property  value="key" />'>
				<div class="box span12">
					<div class="box-header well" data-original-title>Sub Service Information
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content"  > 
						<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;' width='100%'   
							class='table table-striped table-bordered bootstrap-datatable datatable' 
							id='DataTables_Table_Store'>
							<thead>
								<tr>
									<th>S No</th>
									<th>Sub Service Code</th>
									<th class='hidden-phone'>Sub Service Name </th>
									<th class='center hidden-phone'>Service Code</th>
									<th>Service Name </th>
									<th class='hidden-phone'>Date Created</th>
									<th>Actions</th>
								</tr>
							</thead> 
							<tbody id="storeTBody"> 
		                    </tbody>
						</table>
					</div>
				</div>
			</div>  
		</div>
		
		<!-- Below Is For Setting Terminals...... -->
		
		<div id="terminals"   style="display:none">
             <div class="row-fluid sortable">
				<div class="box span12">
					<div class="box-header well" data-original-title>Fee Information
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content"  > 
						<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;' width='100%'   
							class='table table-striped table-bordered bootstrap-datatable datatable' 
							id='DataTables_Table_Terminal'>
							<thead>
								<tr>
									<th>S No</th>
									<th>Fee Code</th>
									<th>Sub Service Code</th>
									<th class='hidden-phone'>Service Code </th>
									<th class='hidden-phone'>Created By </th>
									<th class='hidden-phone'>Created Date</th>
									<th>Actions</th>
								</tr>
							</thead> 
							<tbody id="TerminalTbody"> 
		                    </tbody>
						</table>
					</div>
				</div>
			</div>   
		</div> 
	</div> 
	 
	 
</div>  
</form>
<form name="form2" id="form2" method="post">

</form>	

<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables11.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
