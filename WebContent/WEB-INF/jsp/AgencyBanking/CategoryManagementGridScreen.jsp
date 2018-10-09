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
			
		console.log("category grid screen");
				
	var catjson ='${responseJSON.CATEGORY_JSON}';

	var subarr = "";
 	var catarr = jQuery.parseJSON(catjson);
 	console.log("catarr["+catarr+"]");	
		
		var val = 1;
		var rowindex = 0;
		var colindex = 1;
		var addclass = "";
		
	
	 	$.each(catarr, function(i, v) {
			if(val % 2 == 0 ) {
				addclass = "even";
				val++;
			}
			else {
				addclass = "odd";
				val++;
			}  
			var rowCount = $('#storeTBody > tr').length;
						
			 
			
			var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
			"<td >"+colindex+"</td>"+
			"<td><a href='#' id='SEARCH_NO' value='catid@"+v.categoryId+"' aria-controls='DataTables_Table_0'>"+v.categoryName+"</span></td>"+	
			"<td style='display:none'>"+v.categoryId+"</span> </td>"+ 
			"<td>"+v.categoryDescription+"</span> </td>"+
			"<td>"+v.createdDate+"</span> </td>"+ 
			"<td><a id='create-subcategory' href='#' index='"+rowindex+"' title='Create Sub-Category' data-rel='tooltip' ><img class='action-button' alt='Create' src='${pageContext.request.contextPath}/images/add-button.png'></a>&nbsp;<a id='modify-category' href='#' index='"+rowindex+"'  title='Modify Category' data-rel='tooltip' ><img class='action-button' alt='Edit' src='${pageContext.request.contextPath}/images/edit-button.png'></a>&nbsp;<a id='view-category' index='"+rowindex+"'  href='#' title='View Category' data-rel='tooltip' ><img class='action-button' alt='View' src='${pageContext.request.contextPath}/images/view-button.png'></a>&nbsp; </td></tr>";
				
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
		
		 $(document).on('click','a',function(event) {
		    	
	    	var v_id=$(this).attr('id');
	    	var typeOFReq = $(this).attr('value').split("@")[0];
	    	
	    	console.log("typeOFReq["+typeOFReq+"]");
	    	
			if(typeOFReq == "catid"){
				var catid = $(this).attr('value').split("@")[1];
		    	//alert("cat ID ["+catid+"]");
		    	
		    	var t = $('#DataTables_Table_Store').DataTable();
				t.clear().draw();
				
				var queryString = "catid="+catid;	
				var subarr="";
				
				console.log("catid["+catid+"]");
				console.log("catarr["+catarr+"]");
				for (var i = 0; i < catarr.length; i++) 
				{
					if (catid==catarr[i].categoryId)
					{
						subarr =catarr[i].subCatSet;
						console.log("subarr["+subarr+"]");
					//$.getJSON("subCatJsonAct.action", queryString,function(data) { 
						
						/* var storeData =data.responseJSON.USER_LIST;
						var json = storeData; */
						var val = 1;
						var rowindex = 1;
						var colindex = 0;
						var addclass = "";
						$.each(subarr, function(i, v) {
							if(val % 2 == 0 ) {
								addclass = "even";
								val++;
							}
							else {
								addclass = "odd";
								val++;
							}  
							 
							var user_status="";
							var status_class ="";
							var text = "";
							var text1 = "";
							
							
							var lastTd="<a id='modify-subcategory' href='#' index='"+rowindex+"'  title='Modify SubCategory' data-rel='tooltip' ><img class='action-button' alt='Edit' src='${pageContext.request.contextPath}/images/edit-button.png'></a>&nbsp;<a id='view-subcategory' index='"+rowindex+"'  href='#' title='View SubCategory' data-rel='tooltip' ><img class='action-button' alt='View' src='${pageContext.request.contextPath}/images/view-button.png'></a>&nbsp; </td></tr>";
							//var lastTd="<p><a class='btn btn-warning' href='#' id='modify-subcategory' index='"+rowindex+"'  title='Modify SubCategory' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;<a  class='btn btn-success' href='#' id='view-subcategory' index='"+rowindex+"' title='View SubCategory' data-rel='tooltip'> <i class='icon icon-book icon-white'></i></a>&nbsp;</p> &nbsp;&nbsp;";
							/* var subtd="<span style='display:none'>"+v.subCategoryId+"</span>"; */
							var firstTd = "<span  id='SEARCH_NO' value='subcatid@"+v.subCategoryId+"' >"+v.subCategoryName+"</span>";
							
						        var i= t.row.add( [
									rowindex,
									firstTd,
									v.subCategoryId,
									v.subCategoryDescription,
									v.createdDate,
									lastTd
						        ] ).draw( false );
						        
							         t.rows(i).nodes().to$().attr("id", rowindex);
							        t.rows(i).nodes().to$().attr("index", rowindex); 
						 
						        rowindex = ++rowindex;
						        colindex = ++colindex;
						});		
			 		    
					 	$("#stores").show();
					 	breakLoop = true;
					}
				}
				//});

			}
			else if(typeOFReq == "subcatid"){
				var subcatid = $(this).attr('value').split("@")[1];
		    	//alert(merchantId);
		    	
		    	console.log("subcatid["+subcatid+"]");
				
				
				var queryString = "subcatid="+subcatid;	
				$.getJSON("fetchUserRightsJsonAct.action", queryString,function(data) { 
					
					var terminalTable = $('#DataTables_Table_Terminal').DataTable();
					terminalTable.clear().draw();
					var storeData =data.responseJSON.RIGHTS_LIST;
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
						
						var selDisp = "<select name='rightsDisp' multiple style='width: 350px;height: 450px;'> ";
						var splitDat = v.name.split(",");
							$(splitDat).each(function(i,val){
								selDisp+="<option> "+val +" </option>";
							}); 
						selDisp+="</select>";
						
						 
						var lastTd="<p><a class='btn btn-warning' href='#' id='modify-user-rights' index='' title='Modify Rights' data-rel='tooltip'> <i class='icon icon-edit icon-white'></i></a>&nbsp;<a  class='btn btn-info' href='#' id='view-user-rights' index='' title='View User Rights' data-rel='tooltip'><i class='icon icon-book icon-white'></i></a>&nbsp;";
						//var firstTd = "<a href='#' id='SEARCH_NO' value='TERMINAL-"+v.user_id+"' >"+v.user_id+"</a>";
							
							var i=terminalTable.row.add( [
								rowindex,
								v.user_id,
								selDisp,
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
				//console.log("["+$(this).attr("aria-controls") +"] == ["+ parentId+"]");
				
				if(ariaControlsval == parentId) {
					//console.log("Val ["+$(this).text()+"]"); 
					//alert($(this).attr("aria-controls"));
					//$(this).val('');
					$("#"+ariaControlsval).val(txt_sr);
					$("#"+ariaControlsval).trigger("keyup");
				} /*else {
					if(parentId !='DataTables_Table_0') {
						$(this).val('');
					} 
						
				}*/
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
	
	if(v_id != 'SEARCH_NO') {
		var disabled_status= $(this).attr('disabled'); 
		var queryString = 'entity=${loginEntity}'; 
		var v_action = "NO";
		
		var catid = "";  
		var userId = "";   
		var subcatid = "";  
		
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
			if( v_id == "category-creation") { 
				v_action = "createCategory";  
			} else if (v_id ==  "create-subcategory" 
					|| v_id ==  "view-category" 
					|| v_id ==  "modify-category"				) { 
				 
				 // Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {  
					if(indexTd == 2) {
						catid = $(this).text(); 
					}
				}); 

				if(v_id ==  "create-subcategory") {  
					v_action = "createSubCategory";
					queryString += '&type=create'; 
				} else if(v_id ==  "view-category") { 
					v_action="categoryInformation";  
					queryString += '&type=View'; 
				} else if(v_id ==  "modify-category") { 
					v_action="categoryInformation";  
					queryString += '&type=Modify'; 
				} 
				
				queryString += '&catid='+catid;  
			}  else if (v_id ==  "view-subcategory" 
						|| v_id ==  "modify-subcategory" ) {  
				
				// Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {   
					if(indexTd == 2) {
						subcatid = $(this).text(); 
					}
					
				}); 
			 
				queryString += '&subcatid='+subcatid;
				if(v_id ==  "view-subcategory") { 
					queryString+='&type=View'; 
				} else if(v_id ==  "modify-subcategory") { 
					queryString+='&type=Modify'; 
				}    
				
				if(v_id ==  "view-subcategory") {
					v_action="subcatInformation";
				} else if(v_id == "modify-subcategory"){
					v_action="subcatInformation";
				}else {
					v_action="userInformation";
				}
			 
			} else if (v_id ==  "modify-user-rights" 
						|| v_id ==  "view-user-rights" ) { 
				   
				 // Anchor Tag ID Should Be Equal To TR OF Index
				$(searchTdRow).each(function(indexTd) {  
					 if (indexTd == 1) {
						userId=$(this).text();
					 }  
				}); 

				queryString += '&userId='+userId;  
				
				if(v_id ==  "view-user-rights") { queryString += '&type=View'; }
				else {queryString += '&type=Modify'; }
				
				v_action="modifyUserAccessRights"; 
			
			} else if (v_id ==  "dashboard") { 
				queryString+="&pid=7";  
				v_action="dashboardUsers";
			}  else if (v_id == 'export-users') {
				v_action="usersexport";
			}
		} else {
		
			// No Rights To Access The Link 
		}
		//console.log(queryString);
		if(v_action != "NO") {
			 postData(v_action+".action",queryString);
		
			//$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/"+v_action+".action"+queryString;
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
            <label>Category Management</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item active">Category Information</li>
        <li class="function-buttons">
            <div id="add_button">
                <a href="#" class="btn activate" id="category-creation" title='New Category Creation' data-rel='popover'  data-content='Creating a new category'>Create Category</a>
            </div>
        </li>
    </ol>
    <div class="content-panel">			   
	    <div class="container"> 
			<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable" id="DataTables_Table_0" >
				<thead>
					<tr>
						<th>S No</th>
						<th>Category Name</th>
						<th style='display:none'>Category id</th>
						<th>Description </th>
						<th>Creation Date</th>
						<th>Actions</th>
					</tr>
				</thead> 
				<tbody id="merchantTBody">
						
				</tbody>
			</table>
		</div>
	</div>	
	
		
	
		<!-- Loading Sub Categories -->
	<div  id="stores" style="display:none">  
   	<div id='<s:property  value="key" />'>
	<div class="box-content content-panel panel-head">
    	<label>Sub Category List</label>
    </div> 
	<div class="content-panel"> 
		<div class="container">
			<table class='table table-striped table-bordered bootstrap-datatable datatable' id='DataTables_Table_Store'>
				<thead>
					<tr>
						<th>S No</th>
						<th>SubCategory Name</th>
						<th>SubCategory id</th>
						<th>Description</th>									
						<th>Creation Date</th>
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

 
</form>
<form name="form2" id="form2" method="post">

</form>	


</body>
</html>
