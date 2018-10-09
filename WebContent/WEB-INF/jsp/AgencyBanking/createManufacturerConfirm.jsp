
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

$(document).ready(function(){
			
	
	var submitPath = "<%=request.getContextPath()%>/<%=appName %>/manufacturerCreateAck.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName %>/manufacturers.action";
		
	$('#btn-submit').on('click',function() {
		
		$('#manufacturer_form').submit(function(){
		    $.post(submitPath, $(this).serialize(), function(json) {
		      
		      if(json.responseJSON.remarks == "SUCCESS"){
		    	  swal({
		    		  title: "Success",
		    		  text: "Manufacturer Added successfully.",
		    		  icon: "success",
		    		  button: "Continue",
		    		}).then(function(result){
		    			  window.location = backPath;
		            });
		      }else {
		    	  swal({
		    		  title: "Sorry!",
		    		  text: "Manufacturer cannot be added at this moment.",
		    		  icon: "error",
		    		  button: "Continue",
		    		}).then(function(result){
		    			  window.location = backPath;
		            });
		      }
		      
		    }, 'json');
		    return false;
		  });	
		$("#manufacturer_form").submit();
	});
	
	$('#btn-cancel').on('click',function() { 
		$("#no_name")[0].action=backPath;
		$("#no_name").submit();	 
	});

		
});
	//--> 
</SCRIPT>
    
  
		
</head>

<body>
	<div class="page-header">
        <div>
            <label>Confirm Manufacturer Creation</label>        
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item">
          <a href="manufacturers.action">All Manufacturers</a>
        </li>
        <li class="breadcrumb-item active">
          <a href="#">Confirm Manufacturer Creation</a>       
        </li>
    </ol>
    
    <div class="content-panel">
    	<div class="container">
	    	<div class="row">
		    	<form method="post" id="manufacturer_form" class="amur-forms" name="manufacturer_form">
			       		<div class="col-sm-4 col-xs-6">
							<div class="form-group">
						       	<label class="col-form-label" for="manfName">Manufacturer Name</label>				                    	
						       	<input type="text" class="form-control" required=true name="manfName"  id="manfName" value="${responseJSON.manfName}" readonly />
						    </div>							
					    </div>					                
					    <div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="manfCont">Manufacturer Contact</label>
					           	<input type="text" class="form-control" required=true name="manfCont"  id="manfCont" value="${responseJSON.manfCont}" readonly />					      	
							</div>					       	
					   	</div>
					   	<div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="manfSecCon">Manufacturer Second Contact</label>
					           	<input type="text" class="form-control" required=true name="manfSecCon"  id="manfSecCon" value="${responseJSON.manfSecCon}" readonly />					      	
							</div>					       	
					   	</div>
					    <input type="hidden" name="type"  id="type" value="Create"/>        
		       	</form>
		       	<form id="no_name" method="post"></form>
		    </div>
	    </div>
    </div>
    <div class="content-panel">
    	<div class="container">
    		<div class="row">
	    		<div class="col-sm-4 col-xs-6">
	        		<a href="#" class="btn activate" id="btn-submit" role="button">Save</a>
	         		<a href="#" class="btn deactivate" id="btn-cancel" role="button">Cancel</a>
	   			</div>
	    	</div>
    	</div>
    </div>
	
</body>
</html>

