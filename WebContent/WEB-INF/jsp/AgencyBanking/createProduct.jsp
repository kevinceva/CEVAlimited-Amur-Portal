
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
<SCRIPT type="text/javascript">
//var toDisp = '${type}';

/*function getOptionFor(optionText,optionValue)
{
	return "<option value='"+optionValue+"'>"+optionText+"</option>";
}*/

//Validation Rules for demand form.................................................................................
var demandValidationRules = {
        rules : {
        	
        	catname : { required : true },
        	subcatname : { required : true },
        	manfname : { required : true },
        	productDemandName : { required : true, regex : /^[a-zA-Z0-9.,-=]{0,}(?: [a-zA-Z0-9.,-=]+){0,}$/ },
        	amurDemandName : { required : true, regex : /^[a-zA-Z0-9.,-=]{0,}(?: [a-zA-Z0-9.,-=]+){0,}$/ },
        	sellPriceNoVat : { required : true, regex : /^[0-9]+$/ },
        	vatRate : { required : true },
        	sellPriceVat : { required : true, regex : /^[0-9]+$/ }
        	        	
        
        }, 
        messages : {
        	catname : { 
                required : "Please select the Category Name."
              },
          
            subcatname : { 
                required : "Please select the Sub Category Name."
              }, 
              
            manfname : {
                required : "Please select Supplier's Name."
               },
               
           	productDemandName : {
                required : "Please enter the Supplier's Product Name.",
                regex : "Please use letters and Numbers only."
               },
                
            amurDemandName : { 
                required : "Please enter the Actual Product Name.",
                regex : "Please use numbers only."
              },
              
            sellPriceNoVat : { 
                required : "Please enter the Selling Price without VAT.",
                regex : "Please use numbers only."
              },
              
            vatRate : { 
                required : "Please select the VAT Rate.",
                regex : "Please use numbers only."
              },
            sellPriceVat : { 
                required : "Please enter the Selling Price with VAT.",
                regex : "Please use numbers only."
              }
		}
};



//Validation Rules for supply form.................................................................................
var supplyValidationRules = {
rules : {
        	
        	catname2 : { required : true },
        	subcatname2 : { required : true },
        	manfname2 : { required : true },
        	productSupplierName : { required : true, regex : /^[a-zA-Z0-9.,-=]{0,}(?: [a-zA-Z0-9.,-=]+){0,}$/ },
        	productQuantity : { required : true, regex : /^[0-9]+$/ },
        	reorderLvl : { required : true, regex : /^[0-9]+$/ },
        	vatRate : { required : true },
        	buyPriceNoVat : { required : true, regex : /^[0-9]+$/ },
        	buyPriceVat : { required : true, regex : /^[0-9]+$/ }
        
        }, 
        messages : {
        	catname2 : { 
                required : "Please select the Category Name."
              },
          
            subcatname2 : { 
                required : "Please select the Sub Category Name."
              }, 
              
            manfname2 : {
                required : "Please select Supplier's Name."
               },
               
           	productSupplierName : {
                required : "Please enter the Supplier's Product Name.",
                regex : "Please use letters and Numbers only."
               },
                              
           	productQuantity : { 
                required : "Please enter Amount of the product.",
                regex : "Please use numbers only."
              },
              
            reorderLvl : { 
                required : "Please enter the Reorder Level of the product.",
                regex : "Please use numbers only."
              },
              
            vatRate : { 
                required : "Please select the VAT Rate."
              },
              
            buyPriceNoVat : { 
                  required : "Please enter the Buying price without VAT.",
                  regex : "Please use numbers only."
              },
              
            buyPriceVat : {
            	required : "Please enter the Buying Price with VAT.",
            	regex : "Please use numbers only."
            }
		}
}


	function readURL(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        
	        reader.onload = function (e) {
	            $('#productImageView').attr('src', e.target.result);
	        }
	        
	        reader.readAsDataURL(input.files[0]);
	    }
	}
	
$(function(){
	$("#productImage").change(function(){
	    readURL(this);
	});
});


//Button Functions...........................................................................................

//Saving demand products
$(function(){
	$.validator.addMethod("regex", function(value, element, regexpr) {          
        return regexpr.test(value);
      }, "");
	
	
	$('#saveSupplyDemand').click(function(){
		$("#productDemandForm").validate(demandValidationRules); 
		if($("#productDemandForm").valid()) {
			alert("Demand product form is valid");
			$("#productDemandForm")[0].action="<%=request.getContextPath()%>/<%=appName %>/productCreateConfirm.action";
			$("#productDemandForm").submit();
		}
	});
	
	$('#clearSupplyDemand').click(function(){
		document.getElementById("productDemandForm").reset();
		$('#productImageView').attr('src', '');
	});
	
	$('#saveSupplyProduct').click(function(){
		$("#productSupplyForm").validate(supplyValidationRules); 
		if($("#productSupplyForm").valid()) {
			alert("Supply product form is valid");
			$("#productSupplyForm")[0].action="<%=request.getContextPath()%>/<%=appName %>/createSupplyProducts.action";
			$("#productSupplyForm").submit();
		}
	});
});


//For product demand..........................................................................................

function fecthSubCatList(catname){

	var catejson = '${catalogRespJSON.CATEGORY_JSON}';
	//var catname=$('#catname').val();
	var arr = jQuery.parseJSON(catejson)
	var subarr ="";
	for (var i = 0; i < arr.length; i++) 
	{
		if (catname==arr[i].categoryId){
			subarr =arr[i].subCatSet;
			$("#subcatname option").remove();
			for (var j = 0; j < subarr.length; j++) 
			{
				var options1 = $('<option/>', {value: subarr[j].subCategoryId, text: subarr[j].subCategoryName.replace(/[+]/g, " ")}).attr('data-id',j);
				$('#subcatname').append(options1);
			}
		}
	} 
}

$(document).ready(function(){
	
	fecthSubCatList('${responseJSON.PRODUCT_INFO.categoryId}');
	$('#subcatname').val('${responseJSON.PRODUCT_INFO.subCategoryId}');
	$('#subcatname').trigger("liszt:updated");
		
	$('#catname').val('${responseJSON.PRODUCT_INFO.categoryId}');
	$('#catname').trigger("liszt:updated"); 
	
	var catjson = '${catalogRespJSON.CATEGORY_JSON}';
	//console.log("catjson ["+catjson+"]");
	
	var manfjson = 	'${manfRespJSON.MANUFACTURER_JSON}';
	//console.log("manfjson ["+manfjson+"]");
	
	var disctypejson='${responseJSON.disctype_json}';
	
	//console.log("disctypejson["+disctypejson+"]");

	
	var arr = jQuery.parseJSON(catjson)
	for (var i = 0; i < arr.length; i++) 
	{
	   	//console.log('index: ' + i + ', id: ' + arr[i].categoryId );    
		var options = $('<option/>', {value: arr[i].categoryId, text: arr[i].categoryName.replace(/[+]/g, " ")}).attr('data-id',i);
		$('#catname').append(options);
	} 
	

	$('#catname').change(function(){
		fecthSubCatList($('#catname').val());
	
	});
	
	
	$('#catname').val('${responseJSON.PRODUCT_INFO.categoryId}');
	$('#catname').trigger("liszt:updated"); 


	var manfarr = jQuery.parseJSON(manfjson)
	for (var m = 0; m < manfarr.length; m++) 
	{
		var manfops = $('<option/>', {value: manfarr[m].manufacturerId, text: manfarr[m].manufacturerName.replace(/[+]/g, " ")}).attr('data-id',m);   		
		$('#manfname').append(manfops);
	}
	
	$('#manfname').val('${responseJSON.PRODUCT_INFO.manufacturerId}');
	$('#manfname').trigger("liszt:updated"); 
			
});


//For product supplier..........................................................................................

function fecthSubCatList2(catname){

	var catejson = '${catalogRespJSON.CATEGORY_JSON}';
	//var catname=$('#catname').val();
	var arr = jQuery.parseJSON(catejson)
	var subarr ="";
	for (var i = 0; i < arr.length; i++) 
	{
		if (catname==arr[i].categoryId){
			subarr =arr[i].subCatSet;
			$("#subcatname2 option").remove();
			for (var j = 0; j < subarr.length; j++) 
			{
				var options1 = $('<option/>', {value: subarr[j].subCategoryId, text: subarr[j].subCategoryName.replace(/[+]/g, " ")}).attr('data-id',j);
				$('#subcatname2').append(options1);
			}
		}
	} 
}

$(document).ready(function(){
	
	fecthSubCatList2('${responseJSON.PRODUCT_INFO.categoryId}');
	$('#subcatname2').val('${responseJSON.PRODUCT_INFO.subCategoryId}');
	$('#subcatname2').trigger("liszt:updated");
		
	$('#catname2').val('${responseJSON.PRODUCT_INFO.categoryId}');
	$('#catname2').trigger("liszt:updated"); 
	
	var catjson = '${catalogRespJSON.CATEGORY_JSON}';
	//console.log("catjson ["+catjson+"]");
	
	var manfjson = 	'${manfRespJSON.MANUFACTURER_JSON}';
	//console.log("manfjson ["+manfjson+"]");
	
	var disctypejson='${responseJSON.disctype_json}';
	
	//console.log("disctypejson["+disctypejson+"]");

	
	var arr = jQuery.parseJSON(catjson)
	for (var i = 0; i < arr.length; i++) 
	{
	   	//console.log('index: ' + i + ', id: ' + arr[i].categoryId );    
		var options = $('<option/>', {value: arr[i].categoryId, text: arr[i].categoryName.replace(/[+]/g, " ")}).attr('data-id',i);
		$('#catname2').append(options);
	} 
	

	$('#catname2').change(function(){
		console.log("Category ID :: "+$('#catname2').val());
		fecthSubCatList2($('#catname2').val());
	
	});
	
	
	$('#catname2').val('${responseJSON.PRODUCT_INFO.categoryId}');
	$('#catname2').trigger("liszt:updated"); 


	var manfarr = jQuery.parseJSON(manfjson)
	for (var m = 0; m < manfarr.length; m++) 
	{
		var manfops = $('<option/>', {value: manfarr[m].manufacturerId, text: manfarr[m].manufacturerName.replace(/[+]/g, " ")}).attr('data-id',m);   		
		$('#manfname2').append(manfops);
	}
	
	$('#manfname2').val('${responseJSON.PRODUCT_INFO.manufacturerId}');
	$('#manfname2').trigger("liszt:updated"); 
			
});

</SCRIPT>
    
  
		
</head>

<body>

	<div class="page-header">
        <div>
            <label>Add Products</label>        
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item">
          <a href="products.action">All Products</a>
        </li>
        <li class="breadcrumb-item active">
          <a href="createProduct.action">Create Product</a>       
        </li>
    </ol>
    
    <div class="content-panel">
    	<div class="container">
	    	<div class="row">
	    			<div class="cont">
						<div class="tab">
							<div id="tab-buttons">
								<button class="tablinks new_orders"
									onclick="openCity(event, 'product_demand')" id="defaultOpen">
									Products Demand
								</button>
								<button class="tablinks in_transit"
									onclick="openCity(event, 'product_supply')">
									Products Supply
								</button>								
							</div>
						</div>
					</div>
					
					<div class="tab-body">
					
					<!-- Product Demand form -->
					
						<div id="product_demand" class="tabcontent">
							<form name="productDemandForm" id="productDemandForm" class="amur-form" method="post">
								<fieldset>
									<div class="form-group">
										<label>Category Name<span>*</span> :</label>
										<select id="catname" name="catname" onChange="fecthSubCatList()" class="form-control" >
											<option value="">Select Category...</option>
										</select>	
									</div>  
									
									<div class="form-group">
										<label>Sub Category Name<span>*</span> :</label>
										<select id="subcatname" name="subcatname" data-placeholder="Select Sub Category..."  class="form-control">
											<option value="">Select Sub Category...</option>
										</select> 
									</div>
									
									<div class="form-group">
										<label>Supplier Name<span>*</span> :</label>
										<select id="manfname" name="manfname" data-placeholder="Select Manufacturer..." class="form-control">
											<option value="">Select Manufacturer...</option>
										</select>
									</div>
									
									<div class="form-group">
										<label>Supplier Product Name<span>*</span> :</label>
										<select id="prdSupplierId" name="prdSupplierId" data-placeholder="Select Supplier Product Name..." class="form-control">
											<option value="">Select Product Supplier Name...</option>
										</select>
									</div>
									
									<div class="form-group">
										<label>Supplier Product Name<span>*</span> :</label>
										<input type="text" name="productDemandName" id="productDemandName" class="form-control" placeholder="Enter Supplier Product Name..."/>
									</div>
									
									<div class="form-group">
										<label>Amur Product Name<span>*</span> :</label>
										<input type="text" name="amurDemandName" id="amurDemandName" class="form-control" placeholder="Enter Amur Product Name..."/>
									</div>
									
									<div class="form-group">
										<label>Product Description<span>*</span> :</label>
										<input type="text" name="productDemandDescription" id="productDemandDescription" class="form-control" placeholder="Enter Product Description..."/>
									</div>
									
									<div class="form-group">
										<label>Stock Keeping Unit<span>*</span> :</label>
										<input type="text" name="demandSKU" id="demandSKU" class="form-control" placeholder="Enter Stock Keeping Unit..."/>
									</div>
									
									<div class="form-group">
										<label>Selling Price (Without VAT)<span>*</span> :</label>
										<input type="text" name="sellPriceNoVat" id="sellPriceNoVat" class="form-control" placeholder="Enter Selling Price..."/>
									</div>
									
									<div class="form-group">
										<label>VAT Rate<span>*</span> :</label>
										<select id="vatRate" name="vatRate" required='true' data-placeholder="Select VAT Rate..." class="form-control">
											<option value="">Select Manufacturer...</option>
											<option value="sixteenPercent">16 Percent</option>
											<option value="exempt">Exempt</option>
											<option value="zeroRated">Zero Rated</option>
										</select>
									</div>
									
									<div class="form-group">
										<label>Selling Price (With VAT)<span>*</span> :</label>
										<input type="text" name="sellPriceVat" id="sellPriceVat" class="form-control" placeholder="Enter Selling Price (with VAT)..."/>
									</div>
									
									<div class="form-group">
										<label>Attach Image<span>*</span> :</label>
										<input type="file" name="productImage" class="form-control" id="productImage" accept="image/*">
										<img id="productImageView" src="#" alt="Product Image" />
									</div>
									
									<input type="hidden" name="type"  id="type" value="Create"/>
								</fieldset>
							</form>
							
							<div class="button-container">
								<input type="button" class="btn btn-success" value="Save" id="saveSupplyDemand"/>
								<input type="button" class="btn btn-info" value="Clear" id="clearSupplyDemand"/>
								<input type="button" class="btn btn-danger" value="Back" id="saveSupplyDemand"/>
							</div>
						</div>
						
						
						
						
						
						
						
						<!-- Product supply form -->
						<!-- <div id="product_supply" class="tabcontent">
							<form name="productSupplyForm" id="productSupplyForm" class="amur-form" method="post">
								<fieldset>
									<div class="form-group">
										<label>Category Name<span>*</span> :</label>
										<select id="catname2" name="catname2" onChange="fecthSubCatList2()" required='true' class="form-control" >
											<option value="">Select Category...</option>
										</select>	
									</div>  
									
									<div class="form-group">
										<label>Sub Category Name<span>*</span> :</label>
										<select id="subcatname2" name="subcatname2" required='true' data-placeholder="Select Sub Category..."  class="form-control">
											<option value="">Select Sub Category...</option>
										</select> 
									</div>
									
									<div class="form-group">
										<label>Supplier Name<span>*</span> :</label>
										<select id="manfname2" name="manfname2" required='true' data-placeholder="Select Manufacturer..." class="form-control">
											<option value="">Select Manufacturer...</option>
										</select>
									</div>
									
									<div class="form-group">
										<label>Supplier Product Name<span>*</span> :</label>
										<input type="text" name="productSupplierName" id="productSupplierName" class="form-control" placeholder="Enter Supplier Product Name..."/>
									</div>
									
									<div class="form-group">
										<label>Quantity<span>*</span> :</label>
										<input type="text" name="productQuantity" id="productQuantity" class="form-control" placeholder="Enter Product Quantity..."/>
									</div>
									
									<div class="form-group">
										<label>Reorder Level Quantity<span>*</span> :</label>
										<input type="text" name="reorderLvl" id="reorderLvl" class="form-control" placeholder="Enter Reorder Level Quantity..."/>
									</div>
									
									<div class="form-group">
										<label>Buying Price Per Unit (Without VAT)<span>*</span> :</label>
										<input type="text" name="buyPriceNoVat" id="buyPriceNoVat" class="form-control" placeholder="Enter Buying Price per Unit..."/>
									</div>
									
									<div class="form-group">
										<label>VAT Rate<span>*</span> :</label>
										<select id="vatRate" name="vatRate" required='true' data-placeholder="Select VAT Rate..." class="form-control">
											<option value="">Select Manufacturer...</option>
											<option value="16%">16 Percent</option>
											<option value="Exempt">Exempt</option>
											<option value="Zero rated">Zero Rated</option>
										</select>
									</div>
									
									<div class="form-group">
										<label>Buying Price Per Unit (With VAT)<span>*</span> :</label>
										<input type="text" name="buyPriceVat" id="buyPriceVat" class="form-control" placeholder="Enter Buying Price per Unit..."/>
									</div>
									
									<input type="hidden" name="type"  id="type" value="Create"/>
								</fieldset>								
							</form>
							
							<div class="button-container">
								<input type="button" class="btn btn-success" value="Save" id="saveSupplyProduct"/>
								<input type="button" class="btn btn-info" value="Clear" id="clearSupplyProduct"/>
								<input type="button" class="btn btn-danger" value="Back" id="back"/>
							</div>
						</div> -->
						
					</div>    	
	    	
		    </div>
	    </div>
    </div>
   
</body>

<script>
			function openCity(evt, cityName) {
				var i, tabcontent, tablinks;
				tabcontent = document.getElementsByClassName("tabcontent");
				for (i = 0; i < tabcontent.length; i++) {
					tabcontent[i].style.display = "none";
				}
				tablinks = document.getElementsByClassName("tablinks");
				for (i = 0; i < tablinks.length; i++) {
					tablinks[i].className = tablinks[i].className.replace(
							" active", "");
				}
				document.getElementById(cityName).style.display = "block";
				evt.currentTarget.className += " active";
			}

			// Get the element with id="defaultOpen" and click on it
			document.getElementById("defaultOpen").click();
		</script>


</html>

