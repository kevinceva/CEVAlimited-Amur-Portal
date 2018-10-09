
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

<script type="text/javascript"> 

function clearVals(){ 
    $('#riderFname').val('');
    $('#riderLname').val('');
    $('#riderMobile').val('');
    $('#riderId').val('');
    $('#riderEmail').val('');
    
}

var validationRules = {
        rules : {
        	riderFname : { required : true,  regex: /^[a-zA-Z]+$/ },
        	riderLname : { required : true,  regex: /^[a-zA-Z]+$/ },
        	riderId : { required : true,  regex: /^[0-9]+$/ },
        	riderMobile : { required : true,  regex: /^[a-zA-Z0-9]+$/ },
        	riderEmail : { required : true }
            
        },
        messages : {
        	riderFname : { 
                        required : "Please enter First Name",
                        regex : "Please use letters only."
                      },
                  
           	riderLname : { 
                        required : "Please enter Last Name",
                        regex : "Please use letters only."
                      }, 
            
           	riderId : { 
                        required : "Please enter ID Number.",
                        regex : "Please use numbers only."
                      },  
                      
           	riderMobile : { 
                        required : "Please enter Mobile Number",
                        regex : "Please use numbers only."
                      },
                      
            riderEmail : { 
                        required : "Please enter Email."
                      }
        } 

};

$(document).ready(function(){
	var submitPath = "<%=request.getContextPath()%>/<%=appName %>/modifyRider.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName %>/riderDetails.action";
    
    $.validator.addMethod("regex", function(value, element, regexpr) {          
        return regexpr.test(value);
      }, ""); 
       
    $('#btn-verify').on('click',function(e) { 
        
        var first_name = $('#firstName').val();
        var last_name = $('#lastName').val();
        var id_number = $('#idNumber').val();      
        var mobile_number = $('#mobileNumber').val();
        var email = $('#email').val();
                
        $("#error_dlno").text('');     
        $("#riders_form").validate(validationRules); 
        if($("#riders_form").valid()) {
            
            $("#riderFname").val(first_name);
            $("#riderLname").val(last_name);
            $("#riderId").val(id_number);
            $("#riderMobile").val(mobile_number);
            $("#riderEmail").val(email);
                        
            $('#my_modal').appendTo("body").modal('show');              
        }
        
    });   
         
    $('#btn-cancel').on('click',function() { 
		$("#no_name")[0].action=backPath;
		$("#no_name").submit();	 
	});
    
    $('#btn-submit').on('click',function() {      
        	$('#riders_confirm_form').submit(function(){
    		    $.post(submitPath, $(this).serialize(), function(json) {
    		      
    		      if(json.responseJSON.remarks == "SUCCESS"){
    		    	  swal({
    		    		  title: "Success",
    		    		  text: "Rider information changed successfully.",
    		    		  icon: "success",
    		    		  button: "Continue",
    		    		}).then(function(result){
    		    			  window.location = backPath;
    		            });
    		      }else {
    		    	  swal({
    		    		  title: "Sorry!",
    		    		  text: "Rider information cannot be changed at this moment.",
    		    		  icon: "error",
    		    		  button: "Continue",
    		    		}).then(function(result){
    		    			  window.location = backPath;
    		            });
    		      }
    		      
    		    }, 'json');
    		    return false;
    		  });
        	$("#riders_confirm_form").submit();
        
    }); 		
    
    $('#btn-clear').on('click',function(){
        clearVals();
    });

    
});
</script>       
</head>

<body>    
    <div class="page-header">
        <div>
            <label>Edit Rider</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item">
          <a href="riderDetails.action?pid=<%=session.getAttribute("session_refno").toString() %>">Riders</a>
        </li>
        <li class="breadcrumb-item active">
          <a href="#">Edit Rider</a>        
        </li>
    </ol>
    
    <div class="content-panel">
    	<div class="container">
	    	<div class="row">
	    		<form method="post" id="riders_form" class="amur-forms" name="riders_form">
			       		<div class="col-sm-4 col-xs-6">
							<div class="form-group">
						       	<label class="col-form-label" for="riderFname">First Name</label>				                    	
						       	<input type="text" class="form-control" required=true name="firstName" id="firstName" placeholder="Enter First Name..." value="${responseJSON.RIDER_INFO.riderFirstName}"/>
						    </div>
						    <div class="form-group">
						       	<label class="col-form-label" for="riderMobile">Mobile Number</label>				                    	
						       	<input type="text" class="form-control" required=true name="mobileNumber" id="mobileNumber" placeholder="Enter Mobile Number..." value="${responseJSON.RIDER_INFO.mobileNumber}"/>
						    </div>							
					    </div>					                
					    <div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="riderLname">Last Name</label>
					           	<input type="text" class="form-control" required=true name="lastName" id="lastName" placeholder="Enter Last Name..." value="${responseJSON.RIDER_INFO.riderLastName}"/>					      	
							</div>
							<div class="form-group">
						       	<label class="col-form-label" for="riderEmail">Email</label>				                    	
						       	<input type="text" class="form-control" required=true name="email" id="email" placeholder="Enter Email Address..." value="${responseJSON.RIDER_INFO.email}"/>
						    </div>					       	
					   	</div>
					   	<div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="riderId">ID Number</label>
					           	<input type="text" class="form-control" required=true name="idNumber" id="idNumber" placeholder="Enter ID Number..." value="${responseJSON.RIDER_INFO.idNumber}"/>					      	
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
	    		<div class="col-sm-6 col-xs-6">
	        		<a href="#" class="btn activate" id="btn-verify" role="button">Save</a>
	         		<a href="#" class="btn deactivate" id="btn-clear" role="button">Clear</a>
	         		<a href="#" class="btn deactivate" id="btn-cancel" role="button">Go back</a>
	   			</div>
	    	</div>
    	</div>
    </div>
    
    <div class="modal fade" id="my_modal">
      	<div class="modal-dialog">
        <div class="modal-content">
          	<div class="modal-header">            
            	<label class="modal-title">Confirm Rider Information</label>
          	</div>
          	<div class="modal-body">
    			<div class="container">
		    		<div class="row">
			    		<form method="post" id="riders_confirm_form" class="amur-forms" name="riders_confirm_form">
				       		<div class="col-sm-4 col-xs-6">
								<div class="form-group">
							       	<label class="col-form-label" for="riderFname">First Name</label>				                    	
							       	<input type="text" class="form-control" required=true name="riderFname"  id="riderFname" value="" readonly/>
							    </div>
							    <div class="form-group">
							       	<label class="col-form-label" for="riderMobile">Mobile Number</label>				                    	
							       	<input type="text" class="form-control" required=true name="riderMobile"  id="riderMobile" value="" readonly/>
							    </div>							
						    </div>					                
						    <div class="col-sm-4 col-xs-6">
						        <div class="form-group">
						           	<label class="col-form-label" for="riderLname">Last Name</label>
						           	<input type="text" class="form-control" required=true name="riderLname"  id="riderLname" value="" readonly/>					      	
								</div>
								<div class="form-group">
							       	<label class="col-form-label" for="riderEmail">Email</label>				                    	
							       	<input type="text" class="form-control" required=true name="riderEmail"  id="riderEmail" value="" readonly/>
							    </div>					       	
						   	</div>
						   	<div class="col-sm-4 col-xs-6">
						        <div class="form-group">
						           	<label class="col-form-label" for="riderId">ID Number</label>
						           	<input type="text" class="form-control" required=true name="riderId"  id="riderId" value="" readonly/>					      	
								</div>					       	
						   	</div>
						   	<input type="hidden" name="riderid"  id="rider_id" value="${responseJSON.RIDER_INFO.riderId}"/>
               				<input type="hidden" name="type"  id="type" value="Modify" /> 
			       		</form>
			       	<form id="no_name" method="post"></form>
			    	</div>
	    		</div>           
          	</div>
          	<div class="modal-footer">            
	            <button type="button" id="btn-submit" class="btn activate">Save Rider</button>
	            <button type="button" class="btn deactivate" data-dismiss="modal">Cancel</button>
          	</div>
        </div><!-- /.modal-content -->
      	</div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    
</body>
</html>

