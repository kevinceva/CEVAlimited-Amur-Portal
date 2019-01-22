
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Tony Kithaka">


<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 

<script type="text/javascript"> 

var validationRules = {
        rules : {
        	offerTitle : { required : true,  regex: /^[a-zA-Z0-9.,-]{4,}(?: [a-zA-Z0-9.,-]+){0,}$/ },
        	offerSubtitle : { required : true,  regex: /^[a-zA-Z0-9.,-]{4,}(?: [a-zA-Z0-9.,-]+){0,}$/ },
        	offerDiscountType : { required : true },
        	discountAmount : { required : true,  regex: /^[0-9]+$/ },
        	offerMessage : { required : true, regex: /^[a-zA-Z0-9.,-]{0,}(?: [a-zA-Z0-9.,-]+){0,}$/ }/*,
        	offerImage : { required : true }*/
        },
        messages : {
        	offerTitle : { 
                        required : "Please enter offer title.",
                        regex : "Please use letters and numbers only."
                      },
            offerSubTitle : { 
                        required : "Please enter offer subtitle.",
                        regex : "Please use letters and numbers only."
                      },     
            offerDiscountType : { 
                         regex : "Please enter a discount type for your offer."
                        },
            discountAmount : { 
                        required : "Please enter discount amount.",
                        regex : "Please use numbers only."
                        }, 
          	offerMessage : { 
                        required : "Please enter offer message.",
                        regex : "Please use letters and numbers only."
                      }/*,
            offerImage : { 
                        required : "Please add an image."
                        }*/
        } 

};


</script>
<script type="text/javascript">
var merchantData

$(document).ready(function(){
	
	var merchantInfo ='${responseJSON.MERCHANTS}';
    var merchantData = jQuery.parseJSON(merchantInfo);    
	
    console.log("Merchants :: "+merchantInfo);
    
	$('#merchantID').empty().append('<option value="" disabled selected>Select Merchant</option>');
	for (var m = 0; m < merchantData.length; m++)
	{
		var merchantName = merchantData[m].MERCHANT_NAME;
		var merchants = $('<option/>', {value: merchantData[m].MERCHANT_ID, text: merchantName}).attr('data-id',m);   		
		$('#merchantID').append(merchants);
	}
    
    $.validator.addMethod("regex", function(value, element, regexpr) {          
        return regexpr.test(value);
      }, ""); 
       
      
    
    $('#btn-submit').on('click',function(e) {
        $("#confirm_form")[0].action="<%=request.getContextPath()%>/<%=appName %>/confirmOffer.action";
        $("#confirm_form").submit();
        clearVals();
    });
     
    $('#btn-cancel').on('click',function() {  
        $("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/merchantDetails.action";
        $("#form1").submit();                   
    }); 
    
    $('#btn-clear').click(function(){
    	document.getElementById("form1").reset(); 
    });
    
});

$(document).ready(function(){	
	var submitPath = "<%=request.getContextPath()%>/<%=appName%>/confirmOffer.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName%>/lifestyleOffers.action";
	
	$('#btn-verify').on('click',function() {
		
		
			swal({
	            title: "Save offer details?",
	            text: "Press Ok to save.", 
	            icon: "warning",
	            buttons: true,
	            dangerMode: true,
	        })
	        .then((willDelete) => {
	          if (willDelete) {
	        	  $("#form1").submit(function(e) {
		      		    e.preventDefault();    
		      		    var formData = new FormData(this);
		      			//console.log("Form data :: "+formData);
		      		    $.ajax({
		      		        url: submitPath,
		      		        type: 'POST',
		      		        data: formData,
		      		        success: function (response) {
		      		            //alert(response.responseJSON.remarks);
		      		          	swal({
					    		  	title: "Success",
					    		  	text: "Offer saved successfully.",
					    		  	icon: "success",
					    		  	button: "Continue",
					    		}).then(function(result){				    					    			
					    			window.location.href = backPath;
					            });
		      		        },
		      		      	error: function (response) {
		      		      	swal({
					    		  title: "Sorry!",
					    		  text: "Offer saving failed. Please try again later.",
					    		  icon: "error",
					    		  button: "Continue",
					    		}).then(function(result){
					    			window.location.href = backPath;
					            });
		      	        	},
		      		        
		      		        cache: false,
		      		        contentType: false,
		      		        processData: false
		      		    });
		      		});
		      		$("#form1").submit();
		          } else {
		            swal("Request Cancelled.");
		          }
	        });
		//} 	
	}); 
});


</script>
    
  
        
</head>

<body>
<div class="content-body">    
    
    <div class="page-header">
        <div>
            <label>Create Merchant Offer</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item">
          <a href="lifestyleOffers.action">Merchant Offers Information</a>
        </li>
        <li class="breadcrumb-item active">
          <a href="createOffer.action?pid=<%=session.getAttribute("session_refno").toString() %>">Create Offer</a> 
        </li>
    </ol>
    
    <!-- Confirmation modal starts here -->
    
    <div class="modal fade" id="my_modal">
      <div class="modal-dialog amur-form-modal">
        <div class="modal-content">
          <div class="modal-header">            
            <label class="modal-title">Confirm Merchant</label>
          </div>
          <div class="modal-body">
          		<form id="confirm_form" name="confirm_form" class="amur-form" method="post">
	               	<div id="amur-form-container">
			   			<ul>
			   				<li class="form-group">
				   				<div id="amur-form-left">
				   					<label>Discount Type<span>*</span> : </label>
				   				</div>
				   				<div id="amur-form-right">
				   					<select name="confirm_discountType" readonly>
				   						<option value="F">Flat Rate</option>
  										<option value="P">Percentage Rate</option>
				   					</select>
				   				</div>
				   			</li>
			   				<li class="form-group">
				   				<div id="amur-form-left">
				   					<label>Offer Title<span>*</span> : </label>
				   				</div>
				   				<div id="amur-form-right">
				   					<input type="text" id="confirm_offerTitle" name="confirm_offerTitle" readonly/>
				   				</div>
				   			</li>
				   			<li class="form-group">
				   				<div id="amur-form-left">
				   					<label>Offer Subtitle<span>*</span> : </label>
				   				</div>
				   				<div id="amur-form-right">
				   					<input type="text" id="confirm_offerSubtitle" name="confirm_offerSubtitle" readonly/>
				   				</div>
				   			</li>
				   			<li class="form-group">
				   				<div id="amur-form-left">
				   					<label>Attach Image<span>*</span> : </label>
				   				</div>
				   				<div id="amur-form-right">
				   					<input type="text" name="confirm_offerImage" id="confirm_offerImage" readonly/>
				   				</div>
				   			</li>
				   			<li class="form-group">
				   				<div id="amur-form-left">
				   					<label>Discount Type<span>*</span> : </label>
				   				</div>
				   				<div id="amur-form-right">
				   					<select name="confirm_discountType" readonly>
				   						<option value="F">Flat Rate</option>
  										<option value="P">Percentage Rate</option>
				   					</select>
				   				</div>
				   			</li>
				   			<li class="form-group">
				   				<div id="amur-form-left">
				   					<label>Discount Amount<span>*</span> : </label>
				   				</div>
				   				<div id="amur-form-right">
				   					<input type="text" name="confirm_discountAmount" id="confirm_discountAmount" readonly>
				   				</div>
				   			</li>
				   			<li class="form-group">
				   				<div id="amur-form-left">
				   					<label>Discount Amount<span>*</span> : </label>
				   				</div>
				   				<div id="amur-form-right">
				   					<textarea name="confirm_offerMessage" id="confirm_offerMessage" readonly></textarea>
				   				</div>
				   			</li>
			   			</ul>
			   		</div>
	            </form>
          </div>
          <div class="modal-footer">            
            <button type="button" id="btn-submit" class="btn btn-success">Broadcast Offer</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    
    
           
	<div class="content-panel" id="user-details">       
	   <form name="form1" id="form1" class="amur-form" enctype="multipart/form-data" method="post">
	   		<div id="amur-form-container">
	   			<ul>
	   					<li class="form-group">
			   				<div id="amur-form-left">
			   					<label>Merchant Name<span>*</span> : </label>
			   				</div>
			   				<div id="amur-form-right">
			   					<select name="merchantID" id="merchantID">
			   						
			   					</select>
			   				</div>
			   			</li>
			   			<li class="form-group">
			   				<div id="amur-form-left">
			   					<label>Offer Title<span>*</span> : </label>
			   				</div>
			   				<div id="amur-form-right">
			   					<input type="text" id="offerTitle" name="offerTitle" placeholder="Offer title"/>
			   				</div>
			   			</li>
			   			<li class="form-group">
			   				<div id="amur-form-left">
			   					<label>Offer Subtitle<span>*</span> : </label>
			   				</div>
			   				<div id="amur-form-right">
			   					<input type="text" id="offerSubtitle" name="offerSubtitle" placeholder="Offer Subtitle"/>
			   				</div>
			   			</li>
			   			<li class="form-group">
			   				<div id="amur-form-left">
			   					<label>Attach Image<span>*</span> : </label>
			   				</div>
			   				<div id="amur-form-right">
			   					<input type="file" name="offerImage" id="offerImage" accept="image/*">
			   				</div>
			   			</li>
			   			<li class="form-group">
			   				<div id="amur-form-left">
			   					<label>Discount Type<span>*</span> : </label>
			   				</div>
			   				<div id="amur-form-right">
			   					<select name="offerDiscountType">
			   						<option value="" disabled selected>Discount Type</option>
			   						<option value="F">Flat Rate</option>
  									<option value="P">Percentage Rate</option>
			   					</select>
			   				</div>
			   			</li>
			   			<li class="form-group">
			   				<div id="amur-form-left">
			   					<label>Discount Amount<span>*</span> : </label>
			   				</div>
			   				<div id="amur-form-right">
			   					<input type="text" name="discountAmount" id="discountAmount" placeholder="Discount Amount">
			   				</div>
			   			</li>
			   			<li class="form-group">
			   				<div id="amur-form-left">
			   					<label>Offer Message<span>*</span> : </label>
			   				</div>
			   				<div id="amur-form-right">
			   					<textarea name="offerMessage" id="offerMessage" placeholder="Offer Message"></textarea>
			   				</div>
			   			</li>
			   						   			
	   			</ul>
	   		</div>
	   </form>
	</div>        
        <div class="content-panel form-actions">
            <input type="button" class="btn btn-success" id="btn-verify" value="Submit"/>    
            <input type="button" class="btn btn-cancel" id="btn-clear" value="Clear"/>   
        </div>      
    </div>    
    
    
</body>
</html>

