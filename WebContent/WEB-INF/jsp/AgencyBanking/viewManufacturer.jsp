
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
			
		$('#btn-submit').on('click',function() {  
			$("#no_name")[0].action="<%=request.getContextPath()%>/<%=appName %>/manufacturers.action";
			$("#no_name").submit();					
		}); 

		
});
	//--> 
</SCRIPT>
    
  
		
</head>

<body>
	<div class="page-header">
        <div>
            <label>Manufacturer Details</label>        
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
          <a href="#">Manufacturer Details</a>       
        </li>
    </ol>
    
    <div class="content-panel">
    	<div class="container">
	    	<div class="row">
		    	<form method="post" id="manufacturer_form" class="amur-forms" name="manufacturer_form">
			       		<div class="col-sm-4 col-xs-6">
							<div class="form-group">
						       	<label class="col-form-label" for="manfName">Manufacturer Name</label>				                    	
						       	<input type="text" class="form-control" required=true name="manfName"  id="manfName" value="${responseJSON.MANUFACTURER_INFO.manufacturerName}" readonly />
						    </div>							
					    </div>					                
					    <div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="manfCont">Manufacturer Contact</label>
					           	<input type="text" class="form-control" required=true name="manfCont"  id="manfCont" value="${responseJSON.MANUFACTURER_INFO.manufacturerContact}" readonly />					      	
							</div>					       	
					   	</div>
					   	<div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="manfSecCon">Manufacturer Second Contact</label>
					           	<input type="text" class="form-control" required=true name="manfSecCon"  id="manfSecCon" value="${responseJSON.MANUFACTURER_INFO.manufacturerSecondContact}" readonly />					      	
							</div>					       	
					   	</div>
					      
		       	</form>
		       	<form id="no_name" method="post"></form>
		    </div>
	    </div>
    </div>
    <div class="content-panel">
    	<div class="container">
    		<div class="row">
	    		<div class="col-sm-4 col-xs-6">
	        		<!-- a href="#" class="btn activate" id="btn-submit" role="button">Save</a-->
	         		<a href="#" class="btn deactivate" id="btn-submit" role="button">Back</a>
	   			</div>
	    	</div>
    	</div>
    </div>


	
</body>
</html>

