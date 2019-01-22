
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

var validationRules = {
        rules : {
        	merchantName : { required : true,  regex: /^[a-zA-Z0-9.,-]{4,}(?: [a-zA-Z0-9.,-]+){0,}$/ },
        	merchantTillNo : { required : true,  regex: /^[0-9]+$/ },
        	merchantAddress : { required : true, regex: /^[a-zA-Z0-9.,-=]{0,}(?: [a-zA-Z0-9.,-=]+){0,}$/ },
        	/*merchantCategory : { required : true, regex: /^[a-zA-Z0-9.,-=]{0,}(?: [a-zA-Z0-9.,-=]+){0,}$/ },
        	merchantSubCategory : { required : true, reger: /^[a-zA-Z0-9.,-=]{0,}(?: [a-zA-Z0-9.,-=]+){0,}$/ },*/
        	merchantEmail : { required : true },
        	merchantMobile : { required : true, regex: /^[0-9]+$/ }
        },
        messages : {
        	merchantName : { required : "Please enter the Merchant Name", regex : "Please use letters and numbers only." },
          	merchantTillNo : { required : "Please enter Mpesa Till Number.", regex : "Please use numbers only." },
          	merchantAddress : { required : "Please enter the Merchant Address", regex : "Please use letters and numbers only." },
          	/*merchantCategory : { required : "Select Merchant Category", regex : "Please use letters and numbers only." },
          	merchantSubCategory : { required : "Select Merchant Sub Category", regex : "Please use letters and numbers only." },*/
          	merchantEmail : { required : "Please enter Merchant Mobile Number" },
          	merchantMobile : { required : "Please enter Merchant Mobile Number", regex : "Please use numbers only." }
        } 

};

</script>
<script type="text/javascript">

function fetchMerchantSubCat(catname){
	var merchantCat = '${responseJSON.MERCHANTS_CATEGORIES}';
	console.log("Merchant sub category ::"+merchantCat)
	var merchantCategory=$('#merchantCategory').val();
	var arr = jQuery.parseJSON(merchantCat)
	var subarr ="";
	for (var i = 0; i < arr.length; i++) 
	{
		if (merchantCategory==arr[i].CATEGORY_ID){
			subarr =arr[i].SUBCATEGORIES;
			$("#merchantSubCategory option").remove();
			$('#merchantSubCategory').append('<option value="" disabled selected>Select Merchant Sub Category</option>');
			for (var j = 0; j < subarr.length; j++) 
			{				
				var options1 = $('<option/>', {value: subarr[j].SUBCATEGORY_ID, text: subarr[j].SUBCATEGORY_NAME.replace(/[+]/g, " ")}).attr('data-id',j);
				$('#merchantSubCategory, #merchantSubCategoryConfirm').append(options1);
				//$('#merchantSubCategoryConfirm').append(options1);
			}
		}
	} 
}

$(document).ready(function(){
	
	$('#merchantCategory, #merchantSubCategory').select2();
	//Populate merchant category and sub category
	var merchantCat = '${responseJSON.MERCHANTS_CATEGORIES}';
	
	var arr = jQuery.parseJSON(merchantCat)
	for (var i = 0; i < arr.length; i++) 
	{
	   	console.log('index: ' + i + ', id: ' + arr[i].CATEGORY_ID );    
		var options = $('<option/>', {value: arr[i].CATEGORY_ID, text: arr[i].CATEGORY_NAME.replace(/[+]/g, " ")}).attr('data-id',i);
		$('#merchantCategory, #merchantCategoryConfirm').append(options);
		//$('#merchantCategoryConfirm').append(options);
	} 	
	
	//Image upload preveiwer
	function readURL(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        
	        reader.onload = function (e) {
	            $('#merchantImageView').attr('src', e.target.result);
	            $('#merchantImageViewConfirm').attr('src', e.target.result);
	        }	        
	        reader.readAsDataURL(input.files[0]);
	    }
	}
	
	$(function(){
		$("#merchantImage").change(function(){
		    readURL(this);
		});
	});
	
    
    $.validator.addMethod("regex", function(value, element, regexpr) {          
        return regexpr.test(value);
      }, ""); 
       
    $('#btn-verify').on('click',function(e) { 
        
        var merchantName = $('#merchantName').val();
        var merchantEmail = $('#merchantEmail').val();
        var merchantTillNo = $('#merchantTillNo').val();   
        var merchantMobile = $('#merchantEmail').val();
        var merchantAddress = $('#merchantAddress').val();
        var merchantCategory = $('#merchantCategory').val();
        var merchantSubCategory = $('#merchantSubCategory').val();
       
        $("#error_dlno").text('');     
        $("#form1").validate(validationRules); 
        if($("#form1").valid()) {
            
            $("#merchantNameConfirm").val(merchantName);
            $("#merchantEmailConfirm").val(merchantEmail);
            $("#merchantTillNoConfirm").val(merchantTillNo);
            $("#merchantMobileConfirm").val(merchantMobile);
            $("#merchantAddressConfirm").val(merchantAddress);
            $('#merchantCategoryConfirm').val(merchantCategory);
        	//$('#merchantCategoryConfirm').trigger("liszt:updated");
        	$('#merchantSubCategoryConfirm').val(merchantSubCategory);
        	//$('#merchantSubCategoryConfirm').trigger("liszt:updated");
            
            $('#my_modal').modal('show');              
        }
        
    });   
    
    
    $('#btn-back').on('click',function() {  
        $("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/merchantDetails.action";
        $("#form1").submit();                   
    }); 
    
    $('#btn-clear').click(function(){
    	document.getElementById("form1").reset();
    	$('#merchantCategory, #merchantSubCategory').select2("val", "");
    });
    
    var submitPath = "<%=request.getContextPath()%>/<%=appName%>/confirmMerchant.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName%>/merchantDetails.action";
	
	$('#btn-submit').on('click',function() {	
		
			swal({
	            title: "Do you want to create this merchant?",
	            text: "Press Ok to continue.", 
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
					    		  	text: "Merchant created successfully.",
					    		  	icon: "success",
					    		  	button: "Continue",
					    		}).then(function(result){				    					    			
					    			window.location.href = backPath;
					            });
		      		        },
		      		      	error: function (response) {
		      		      	swal({
					    		  title: "Sorry!",
					    		  text: "Merchant creation failed. Please try again later.",
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
		})	
    
	});


	



</script>
    
  
        
</head>



<body>
	<div class="content-body">
		<div class="page-header">
			<div>
				<label>Create Merchant</label>            
			</div>
		</div>
		<ol class="breadcrumb">
			<li class="breadcrumb-item">
				<a href="home.action">Dashboard</a>
			</li>
			<li class="breadcrumb-item">
				<a href="merchantDetails.action">Merchant Information</a>
			</li>
			<li class="breadcrumb-item active">
				<a href="createMerchant.action?pid=<%=session.getAttribute("session_refno").toString() %>">Create Merchant</a> 
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
								<table class="table table-bordered" style="width:100%">
									<tr class="form-group">
										<td>
											<label>Merchant Name<span>*</span> : </label>
										</td>
										<td>
											<input type="text" id="merchantNameConfirm" name="merchantNameConfirm" placeholder="Merchant Name" readonly/>	
										</td>
									</tr>
									<tr class="form-group">
										<td>
											<label>Merchant Email<span>*</span> : </label>
										</td>
										<td>
											<input type="email" id="merchantEmailConfirm" name="merchantEmailConfirm" placeholder="Merchant Email" readonly/>
										</td>
									</tr>
									<tr class="form-group">
										<td>
											<label>Merchant Address<span>*</span> : </label>
										</td>
										<td>
											<input type="text" id="merchantAddressConfirm" name="merchantAddressConfirm" placeholder="Merchant Address" readonly/>
										</td>
									</tr>
									<tr class="form-group">
										<td>
											<label>Merchant Mobile Number<span>*</span> : </label>
										</td>
										<td>
											<input type="text" id="merchantMobileConfirm" name="merchantMobileConfirm" placeholder="Merchant Mobile Number" readonly/>
										</td>
									</tr>
									<tr class="form-group">
										<td>
											<label>Merchant Category<span>*</span> : </label>
										</td>
										<td>
											<select class="form-control" id="merchantCategoryConfirm" name="merchantCategoryConfirm" onChange="fetchMerchantSubCat()"disabled="true">
												<option value="" disabled selected>Select Merchant Category</option>
											</select>
										</td>
									</tr>
									<tr class="form-group">
										<td>
											<label>Merchant Sub Category<span>*</span> : </label>
										</td>
										<td>
											<select class="form-control" id="merchantSubCategoryConfirm" name="merchantSubCategoryConfirm"disabled="true">
												<option value="" disabled selected>Select Merchant Sub Category</option>
											</select>
										</td>
									</tr>
									<tr class="form-group">
										<td>
											<label>Mpesa Till Number<span>*</span> : </label>
										</td>
										<td>
											<input type="text" id="merchantTillNoConfirm" name="merchantTillNoConfirm" placeholder="Merchant Till Number" readonly/>
										</td>
									</tr>
									<tr class="form-group">
										<td>
											<label>Attach Merchant Image<span>*</span> : </label>
										</td>
										<td>
											<img id="merchantImageViewConfirm" src="#" alt="No merchant image selected" />
										</td>
									</tr>
								</table>
							
							</div>
						</form>
					</div>
					<div class="modal-footer">            
						<button type="button" id="btn-submit" class="btn btn-success">Save Merchant</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->
		
		
		<div class="content-panel" id="user-details">
			<form name="form1" id="form1" class="amur-form" enctype="multipart/form-data"  method="post">
				<div id="amur-form-container">
					<table class="table table-bordered" style="width:100%">
						<tr class="form-group">
							<td>
								<label>Merchant Name<span>*</span> : </label>
							</td>
							<td>
								<input type="text" id="merchantName" name="merchantName" placeholder="Merchant Name"/>	
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Merchant Email<span>*</span> : </label>
							</td>
							<td>
								<input type="email" id="merchantEmail" name="merchantEmail" placeholder="Merchant Email"/>
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Merchant Address<span>*</span> : </label>
							</td>
							<td>
								<input type="text" id="merchantAddress" name="merchantAddress" placeholder="Merchant Address"/>
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Merchant Mobile Number<span>*</span> : </label>
							</td>
							<td>
								<input type="text" id="merchantMobile" name="merchantMobile" placeholder="Merchant Mobile Number"/>
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Merchant Category<span>*</span> : </label>
							</td>
							<td>
								<select class="form-control" id="merchantCategory" name="merchantCategory" onChange="fetchMerchantSubCat()">
									<option value="" disabled selected>Select Merchant Category</option>
								</select>
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Merchant Sub Category<span>*</span> : </label>
							</td>
							<td>
								<select class="form-control" id="merchantSubCategory" name="merchantSubCategory">
									<option value="" disabled selected>Select Merchant Sub Category</option>
								</select>
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Mpesa Till Number<span>*</span> : </label>
							</td>
							<td>
								<input type="text" id="merchantTillNo" name="merchantTillNo" placeholder="Merchant Till Number"/>
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Attach Merchant Image<span>*</span> : </label>
							</td>
							<td>
								<input type="file" name="merchantImage" class="form-control" id="merchantImage" accept="image/*">
							</td>
						</tr>
						<tr class="form-group">
							<td>
							</td>
							<td>
								<img id="merchantImageView" src="#" alt="No merchant image selected" />
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		<div class="content-panel form-actions">
			<input type="button" class="btn btn-success" id="btn-verify" value="Submit"/>    
			<input type="button" class="btn btn-cancel" id="btn-clear" value="Clear"/>  
			<input type="button" class="btn btn-danger" id="btn-back" value="Back"/>  
		</div>
	</div>
</body>


</html>

