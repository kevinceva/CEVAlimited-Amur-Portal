
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
	
	
	var inCatid = '${responseJSON.catid}';
	console.log("inCatid["+inCatid+"]");
	$('#catid').val(inCatid);
		
	$('#btn-cancel').on('click',function() { 
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/category.action";
		$("#form1").submit();	 
	});
	
		
});

$(document).ready(function(){
			
	
	var submitPath = "<%=request.getContextPath()%>/<%=appName %>/subcatModifyAck.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName %>/category.action";
		
	$('#btn-submit').on('click',function() {
		
		$('#subcategory_form').submit(function(){
		    $.post(submitPath, $(this).serialize(), function(json) {
		      
		      if(json.responseJSON.remarks == "SUCCESS"){
		    	  swal({
		    		  title: "Success",
		    		  text: "Category changed successfully.",
		    		  icon: "success",
		    		  button: "Continue",
		    		}).then(function(result){
		    			  window.location = backPath;
		            });
		      }else {
		    	  swal({
		    		  title: "Sorry!",
		    		  text: "Category cannot be changed at this moment.",
		    		  icon: "error",
		    		  button: "Continue",
		    		}).then(function(result){
		    			  window.location = backPath;
		            });
		      }
		      
		    }, 'json');
		    return false;
		  });	
		$("#subcategory_form").submit();
	});
	
	$('#btn-cancel').on('click',function() { 
		$("#no_name")[0].action=backPath;
		$("#no_name").submit();	 
	});

		
});
</SCRIPT>
</head>

<body>
	<div class="page-header">
        <div>
            <label>Confirm Subcategory Creation</label>        
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
          <a href="#">Confirm Sub Category</a>       
        </li>
    </ol>
    
    <div class="content-panel">
    	<div class="container">
	    	<div class="row">
		    	<form method="post" id="subcategory_form" class="amur-forms" name="subcategory_form">
			       		<div class="col-sm-4 col-xs-6">
							<div class="form-group">
						       	<label class="col-form-label" for="product_name">Sub Category Name</label>				                    	
						       	<input type="text" class="form-control" required=true name="subcatname"  id="subcatname" value="${responseJSON.subcatname}" readonly />
						    </div>
							
					    </div>
					                
					    <div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="catname">Sub Category Description</label>
					           	<input type="text" class="form-control" required=true name="subcatdesc"  id="subcatdesc" value="${responseJSON.subcatdesc}" readonly />					      	
							</div>
					       	
					   	</div>
					    <input type="hidden" name="type"  id="type" value="Modify"/>
						<input type="hidden" name="subcatid"  id="subcatid" value="${responseJSON.subcatid}"/>
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
	        		<a href="#" class="btn activate" id="btn-submit" role="button">Submit</a>
	         		<a href="#" class="btn deactivate" id="btn-cancel" role="button">Cancel</a>
	   			</div>
	    	</div>
    	</div>
    </div>
</body>
</html>

