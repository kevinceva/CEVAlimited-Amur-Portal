
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 
<SCRIPT type="text/javascript"> 
var toDisp = '${type}';

function getOptionFor(optionText,optionValue)
{
	return "<option value='"+optionValue+"'>"+optionText+"</option>";
}

$(document).ready(function(){
	
	//fecthSubCatList('${responseJSON.PRODUCT_INFO.categoryId}');
	/* $('#subcatname').val('${responseJSON.PRODUCT_INFO.subCategoryId}');
	$('#subcatname').trigger("liszt:updated"); 
	
	
	$('#catname').val('${responseJSON.PRODUCT_INFO.categoryId}');
	$('#catname').trigger("liszt:updated");  */
	
	 var catjson = '${catalogRespJSON.CATEGORY_JSON}';
	
	
	
	var inCatid = '${responseJSON.catid}';
	console.log("inCatid["+inCatid+"]");
	
	$('#catname').val(inCatid);
	
	var arr = jQuery.parseJSON(catjson)
	for (var i = 0; i < arr.length; i++) 
	{
		if (arr[i].categoryId == inCatid)
		{
			$('#catname').val(arr[i].categoryName);
			$('#catname_spn').text(arr[i].categoryName);
			$('#catdesc').val(arr[i].categoryDescription);
			$('#catdesc_spn').text(arr[i].categoryDescription);
			
			breakLoop = true;
		}
	}  

	$('#btn-submit').on('click',function() { 
			
			$("#category_form")[0].action="<%=request.getContextPath()%>/<%=appName %>/categoryModifyConfirm.action";
			$("#category_form").submit();	
		

	}); 
	
	$('#btn-cancel').on('click',function() { 
		$("#no_name")[0].action="<%=request.getContextPath()%>/<%=appName %>/category.action";
		$("#no_name").submit();	 
	});
	
		
});
	//--> 
</SCRIPT>
    
  
		
</head>

<body>
	<div class="page-header">
        <div>
            <label>Change Category Information</label>        
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item">
          <a href="category.action">All Categories</a>
        </li>
        <li class="breadcrumb-item active">
          <a href="#">Change Category Information</a>       
        </li>
    </ol>
    
    <div class="content-panel">
    	<div class="container">
	    	<div class="row">
		    	<form method="post" id="category_form" class="amur-forms" name="category_form">
			       		<div class="col-sm-4 col-xs-6">
							<div class="form-group">
						       	<label class="col-form-label" for="catname">Category Name</label>				                    	
						       	<input type="text" class="form-control" required=true name="catname" id="catname"/>
						    </div>
					    </div>					                
					    <div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="catdesc">Category Description</label>
					           	<input type="text" class="form-control" required=true name="catdesc" id="catdesc"/>					      	
							</div>					       	
					   	</div>	
					   				   	
					    <input type="hidden" name="type"  id="type" value="Modify"/>
						<input type="hidden" name="catid"  id="catid" value="${responseJSON.catid}"/>      
		       	</form>
		       	<form id="no_name" method="post"></form>
		    </div>
	    </div>
    </div>
    <div class="content-panel">
    	<div class="container">
    		<div class="row">
	    		<div class="col-sm-4 col-xs-6">
	    			<a href="#" class="btn activate" id="btn-submit" role="button">Confirm</a>
	         		<a href="#" class="btn deactivate" id="btn-cancel" role="button">Back</a>
	   			</div>
	    	</div>
    	</div>
    </div>

</body>

</html>

