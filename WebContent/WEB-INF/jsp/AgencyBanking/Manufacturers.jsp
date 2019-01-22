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
			
		
	var storeData ='${responseJSON.MANUFACTURER_JSON}';
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
		
		//rowindex = ++rowindex;
		colindex = ++ colindex; 
		
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
		"<td >"+colindex+"</td>"+
		"<td><id='SEARCH_NO' value='prdid@"+v.manufacturerId+"' aria-controls='DataTables_Table_0'>"+v.manufacturerName+"</span></td>"+ 
		/* "<td>"+v.prdName+"</span> </td>"+  "<td style='display:none'>"+v.prdId+" </td>"+ */
		"<td style='display:none'>"+v.manufacturerId+" </td>"+
		"<td>"+v.manufacturerContact+"</td>"+ 
		"<td>"+v.manufacturerSecondContact+"</td>"+  
		"<td><a id='modify-manufacturer' href='#' index='"+rowindex+"' data-toggle='tooltip' data-placement='bottom' title='Edit Manufacturer' ><img class='action-button' alt='Edit' src='${pageContext.request.contextPath}/images/edit-button.png'></a> &nbsp;&nbsp;<a id='view-manufacturer' index='"+rowindex+"'  href='#' title='View Manufacturer' data-toggle='tooltip' ><img class='action-button' alt='Edit' src='${pageContext.request.contextPath}/images/view-button.png'></a> </td></tr>";
			
			
			$("#merchantTBody").append(appendTxt);	
			rowindex = ++rowindex;
			//colindex = ++colindex;
	});

	
		//console.log(userGroupData);
		$.each(jsonLinks, function(index, v) {
			linkIndex[index] = index;
			linkName[index] = v.name;
			linkStatus[index] = v.status;
		}); 
		
		$("input[type=search]").on('keyup',function() {
			
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
		
		 
		
}); 

$(document).on('click','a',function(event) {
	var v_id=$(this).attr('id');
	 console.log("value of v_id["+v_id+"]");
	if(v_id != 'SEARCH_NO') {
		var disabled_status= $(this).attr('disabled'); 
		var queryString = 'entity=${loginEntity}'; 
		var v_action = "NO";
		
		var groupId = "";  
		var userId = "";   
		console.log("disabled_status["+disabled_status+"]"); 
		/* var index1 = $(this).attr('index'); */  
		var index1 = $(this).parent().closest('tr').attr('index');
		
		//var index1=$(this).parent().closest('tr').index();
		//alert(index1)
		
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		var searchTrRows = parentId+" tr"; 
		//alert("searchTrRows::"+searchTrRows);
		var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
		//alert("searchTrRows::"+searchTdRow);		 
				 
		if(disabled_status == undefined) {  
			if (v_id ==  "view-manufacturer" || v_id ==  "modify-manufacturer") { 
				 
				 // Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {  
					if(indexTd == 2) {
						groupId = $(this).text(); 
					}
				}); 

				if(v_id ==  "view-manufacturer") { 
					v_action="manufacturerInformation";  
					queryString += '&type=View'; 
				} else if(v_id ==  "modify-manufacturer") { 
					v_action="manufacturerInformation";  
					queryString += '&type=Modify'; 
				} 
				
				queryString += '&manfid='+groupId;  
			}  
		} else {
		
			
		}
		console.log("queryString["+queryString+"] v_action["+v_action+"]");
		if(v_action != "NO") {
			 postData(v_action+".action",queryString);
		
			<%-- $("#form1")[0].action="<%=ctxstr%>/<%=appName %>/"+v_action+".action"+queryString; --%>
			//$("#form1").submit();
		}
	} else {
		// The below code is for quick searching.
		var txt_sr = $(this).text();
		var parentId =$(this).parent().closest('table').attr('id'); 
		//console.log("The val ["+txt_sr+"]");
		//console.log("The Attr Id ["+parentId+"]");
		
		$('div input[type=search]').each(function(){
			if($(this).attr("aria-controls") == parentId) {
				//console.log("Val ["+$(this).val()+"]"); 
				//$(this).val('');
				$(this).val(txt_sr);
				$(this).trigger("keyup");
			} /*else {
				if(parentId !='DataTables_Table_0') {
					$(this).val('');
				} 
					
			}*/
		});
	}
}); 

$(document).ready(function() {
	$('#DataTables_Table_0').DataTable({
		"filter": true
	});
} );


</script> 
		
</head>

<body>
	<form name="form1" id="form1" method="post" action="">	
	<!-- topbar ends --> 
	
	<div class="page-header">
        <div>
            <label>Manufacturers</label>            
        </div>  
    </div>
    
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item active">Manufacturer Information</li>
        <li class="function-buttons">
            <div id="add_button">
                <a href="createManufacturer.action" class="btn activate" id="manufacturer-create" title='New Manufacturer Creation' data-toggle='popover'  data-content='Creating a new Manufacturer'>Add New Manufacturer</a>
            </div>
        </li>
    </ol>       
	<div class="content-panel"> 
		<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable" id="DataTables_Table_0" >
			<thead>
				<tr>
					<th>S No</th>
					<th>Manufacturer Name</th>
					<th style='display:none'></th>
					<th>Contact</th>
					<th>Secondary Contact</th>
					<th>Actions</th>
				</tr>
			</thead> 
			<tbody id="merchantTBody">
					
			</tbody>
		</table>
	</div>
			
</form>
<form name="form2" id="form2" method="post">

</form>	

</body>
</html>
