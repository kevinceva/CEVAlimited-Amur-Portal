
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
	
	var catid='${responseJSON.catid}';
	console.log("catid["+catid+"]");
	$('#catid').val(catid);
	
	
	$('#btn-submit').on('click',function() {			
		$("#subcategory-form")[0].action="<%=request.getContextPath()%>/<%=appName %>/subcatCreateConfirm.action";
		$("#subcategory-form").submit();
	}); 
	
	$('#btn-cancel').on('click',function() { 
		$("#subcategory-form")[0].action="<%=request.getContextPath()%>/<%=appName %>/category.action";
		$("#subcategory-form").submit();	 
	});
	
		
});
	//--> 
</SCRIPT>	
</head>

<body>

	<div class="page-header">
        <div>
            <label>Create Subcategory</label>        
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
          <a href="#">Create Sub Category</a>       
        </li>
    </ol>
    
    <div class="content-panel">
    	<div class="container">
	    	<div class="row">
		    	<form method="post" id="subcategory-form" class="amur-forms" name="product-form">
			       		<div class="col-sm-4 col-xs-6">
							<div class="form-group">
						       	<label class="col-form-label" for="product_name">Sub Category Name</label>				                    	
						       	<input type="text" class="form-control" required=true name="subcatname"  id="subcatname" placeholder="Enter Subcategory Name..." />
						    </div>
							
					    </div>
					                
					    <div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="catname">Sub Category Description</label>
					           	<input type="text" class="form-control" required=true name="subcatdesc" id="subcatdesc"  placeholder="Enter Subcategory Description..." />					      	
							</div>
					       	
					   	</div>
					    <input type="hidden" name="type"  id="type" value="Create"/>
						<input type="hidden" name="catid"  id="catid" value="${responseJSON.catid}"/>          
		       	</form>
		    </div>
	    </div>
    </div>
    <div class="content-panel">
    	<div class="container">
    		<div class="row">
	    		<div class="col-sm-4 col-xs-6">
	        		<a href="#" class="btn activate" id="btn-submit" role="button">Submit</a>
	         		<a href="#" class="btn deactivate" id="btn-cancel" role="button">Cancel</a>
	   			</div>
	    	</div>
    	</div>
    </div>

</body>



</html>

