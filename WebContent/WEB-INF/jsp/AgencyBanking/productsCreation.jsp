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
        	prdSupplierId : { required : true },
        	productDemandDesc : { required : true, regex : /^[a-zA-Z0-9.,-=]{0,}(?: [a-zA-Z0-9.,-=]+){0,}$/ },
        	demandSKU : { required : true, regex : /^[a-zA-Z0-9.,-=]{0,}(?: [a-zA-Z0-9.,-=]+){0,}$/ },
        	amurDemandName : { required : true, regex : /^[a-zA-Z0-9.,-=]{0,}(?: [a-zA-Z0-9.,-=]+){0,}$/ },
        	sellPriceNoVat : { required : true, regex : /^[0-9.]+$/ },
        	markupRate : { required : true, regex : /^[0-9.]+$/ },
        	vatRate : { required : true },
        	sellPriceVat : { required : true, regex : /^[0-9.]+$/ }
        	        	
        
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
               
            prdSupplierId : {
                required : "Please select Supplier's Product Name."
            },
            
            productDemandDesc : { 
               	required : "Please enter a brief Product Desciption",
                regex : "Please use numbers and letters only."
            },         
            
            demandSKU : { 
                required : "Please enter a brief Product Desciption",
                regex : "Please use numbers and letters only."
           	}, 
           	
            amurDemandName : { 
                required : "Please enter the Actual Product Name.",
                regex : "Please use numbers only."
            },
              
            sellPriceNoVat : { 
                required : "Please enter the Selling Price without VAT.",
                regex : "Please use numbers only."
              },
              
            markupRate : {
            	required : "Please enter the Markup Rate.",
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
        	vatRate2 : { required : true },
        	buyPriceNoVat : { required : true, regex : /^[0-9.]+$/ },
        	buyPriceVat : { required : true, regex : /^[0-9.]+$/ }
        
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
              
            vatRate2 : { 
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


	/*function readURL(input) {
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
*/

//Button Functions...........................................................................................



$(document).ready(function(){	
	var submitPath = "<%=request.getContextPath()%>/<%=appName%>/productCreateConfirm.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName%>/createProduct.action";
	
	$('#saveSupplyDemand').click(function(){
		$("#productDemandForm").ajaxSubmit({url: 'productCreateConfirm.action', type: 'post'},function(json){
			if(json.responseJSON.remarks == "SUCCESS"){
				alert("data submitted");
			} else {
				alert("No data submitted");
			}
		}
			
		);
		//$("#productDemandForm").submit();
		
		
	});
	
	/*$('#saveSupplyDemand').on('click',function(e) {
		var offerImage = $('#offerImage').val();
		//$('#productDemandForm').validate(validationRules);
		//if($('#productDemandForm').valid()){
			swal({
	            title: "Do you want to save this product?",
	            //text: "Do you want to save this product?", 
	            icon: "warning",
	            buttons: true,
	            dangerMode: true,
	        })
	        .then((willDelete) => {
	          if (willDelete) {
	        	  $('#productDemandForm').submit(function(e){
					    $.post(submitPath, $(this).serialize(), function(json) {		      
					      if(json.responseJSON.remarks == "SUCCESS"){
					    	  swal({
					    		  title: "Success",
					    		  text: "Product saved successfully.",
					    		  icon: "success",
					    		  button: "Continue",
					    		}).then(function(result){				    					    			
					    			window.location.href = backPath;
					            });
					      }else if(json.responseJSON.remarks == "FAILURE") {
					    	  swal({
					    		  title: "Sorry!",
					    		  text: "Product Info has not been saved. Please try again later.",
					    		  icon: "error",
					    		  button: "Continue",
					    		}).then(function(result){
					    			window.location.href = backPath;
					            });
					      }
					      
					    }, 'json');
					    return false;
					  });
					 $("#productDemandForm").submit();
	          } else {
	            swal("Request Cancelled.");
	          }
	        });
		//} 	
	});*/
});	



//Saving demand products
$(function(){
	$.validator.addMethod("regex", function(value, element, regexpr) {          
        return regexpr.test(value);
      }, "");
		
	$('#clearSupplyDemand').click(function(){
		document.getElementById("productDemandForm").reset();
		$('#productImageView').attr('src', '');
	});
	
	$(document).ready(function(e){	
		var submitPath = "<%=request.getContextPath()%>/<%=appName%>/createSupplyProducts.action";
		var backPath = "<%=request.getContextPath()%>/<%=appName%>/createProduct.action";
		
		$('#saveSupplyProduct').on('click',function() {
			
			//$('#productSupplyForm').validate(validationRules);
			//if($('#productSupplyForm').valid()){
				swal({
		            title: "Do you want to save this product?",
		            //text: "Press Ok to continue.", 
		            icon: "warning",
		            buttons: true,
		            dangerMode: true,
		        })
		        .then((willDelete) => {
		          if (willDelete) {
		        	  $('#productSupplyForm').submit(function(){
						    $.post(submitPath, $(this).serialize(), function(json) {		      
						      if(json.responseJSON.remarks == "SUCCESS"){
						    	  swal({
						    		  title: "Success",
						    		  text: "Product saved successfully.",
						    		  icon: "success",
						    		  button: "Continue",
						    		}).then(function(result){				    					    			
						    			window.location.href = backPath;
						            });
						      }else if(json.responseJSON.remarks == "FAILURE") {
						    	  swal({
						    		  title: "Sorry!",
						    		  text: "Product Info has not been saved. Please try again later.",
						    		  icon: "error",
						    		  button: "Continue",
						    		}).then(function(result){
						    			window.location.href = backPath;
						            });
						      }
						      
						    }, 'json');
						    return false;
						  });
						 $("#productSupplyForm").submit();
		          } else {
		            swal("Request Cancelled.");
		          }
		        });
			//} 	
		}); 
	});	
	
	
	$('#goBack').click(function(){
		$("#productSupplyForm")[0].action="<%=request.getContextPath()%>/<%=appName %>/products.action";
		$("#productSupplyForm").submit();
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
			
			var defaultOption = $('<option value="">Select Subcategory...</option>');
			$('#subcatname').append(defaultOption);
			
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
			
			var defaultOption = $('<option value="">Select Subcategory...</option>');
			$('#subcatname2').append(defaultOption);
			
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

$(document).ready(function(){
	var productJson = '${responseJSON.CAT_PRODUCTS}';
	console.log("Product JSON :: "+productJson);
	
});

function fetchProductList(subcatname){

	var prdjson = '${responseJSON.CAT_PRODUCTS}';
	var subcatname = $('#subcatname').val();
	var arr = jQuery.parseJSON(prdjson)
	var subarr ="";
	for (var i = 0; i < arr.length; i++) 
	{
		if (subcatname == arr[i].SUB_CATEGORY_ID){
			subarr = arr[i].SUB_CAT_PRODUCTS;
			$("#prdSupplierId option").remove();
			
			var defaultOption = $('<option value="">Select Supplier Product Name...</option>');
			$('#prdSupplierId').append(defaultOption);
			
			for (var j = 0; j < subarr.length; j++) 
			{				
				var options1 = $('<option/>', {value: subarr[j].PRD_ID, text: subarr[j].PRD_NAME.replace(/[+]/g, " ")}).attr('data-id',j);				
				$('#prdSupplierId').append(options1);
			}
		}
	} 
}

function fetchProductPrice(prdId){
	var prdJson = '${responseJSON.CAT_PRODUCTS}';
	var referralRate = '${responseJSON.REFERRAL_RATE}';
	var healthPremium = '${responseJSON.HEALTHPREMIUM}';
	var prdId = $('#prdSupplierId').val();
	var arr = jQuery.parseJSON(prdJson);
	var prdPrice = "";
	var subarr = "";
	
	for (var i = 0; i < arr.length; i++){
		var subarr = arr[i].SUB_CAT_PRODUCTS;
		for (var j = 0; j < subarr.length; j++) {
			if(prdId == subarr[j].PRD_ID){
				prdPrice = subarr[j].PRD_PRICE;
				
				
			}
		}
	}
	
	var referral = referralRate * prdPrice;
	var healthPr = healthPremium * prdPrice;
		
	$('#referralEarnings').val(referral);
	$('#healthPremiumAmt').val(healthPr);
	$('#sellPriceNoVat').val(prdPrice);
	$('#prdPrice').val(prdPrice);
}



function calculateMarkup(mrkRate){
	var prdPrice = $('#prdPrice').val();
	var markupType = $('#markupType').val();
	var markupRate = $('#markupRate').val();
	var sellPriceWithoutVat;
	var markupAmount;
	
	if(markupType == 'percentile'){
		var markupAmount = (markupRate / 100) * prdPrice;
		markupAmount = markupAmount.toFixed(2);
		$('#markupAmountNoVat').val(markupAmount);		
	} else {
		markupAmount = markupRate;
		$('#markupAmountNoVat').val(markupRate);
	}
	
	sellPriceWithoutVat = parseFloat(markupAmount) + parseFloat(prdPrice);	
	$('#sellPriceWithoutVat').val(sellPriceWithoutVat);	
};

function calculateReferralNoVat(referralRate){
	var prdPrice = $('#sellPriceVat').val();
	var referralRate = $('#referralRate').val();
	var referralAmtNoVat;
	
	referralAmtNoVat = parseFloat(referralRate / 100) * parseFloat(prdPrice);
	referralAmtNoVat = referralAmtNoVat.toFixed(2);
	$('#referralAmtNoVat').val(referralAmtNoVat);
}


function calculateHealthPremiumNoVat(healthPremiumRate){
	var prdPrice = $('#sellPriceVat').val();
	var healthPremiumRate = $('#healthPremiumRate').val();
	var healthPremiumNoVat;	
	healthPremiumNoVat = parseFloat(healthPremiumRate / 100) * parseFloat(prdPrice);	
	healthPremiumNoVat = healthPremiumNoVat.toFixed(2);
	$('#healthPremiumNoVat').val(healthPremiumNoVat);	
}

function calculateVatOnPrdPrice(priceVatType){
	var vatType = $('#priceVatType').val();
	var prdPriceNoVat = $('#sellPriceWithoutVat').val();
	var prdPriceVat;
	
	if(vatType == 'sixteenPercent'){
		prdPriceVat = parseFloat(prdPriceNoVat * 0.16) + parseFloat(prdPriceNoVat);
	} else {
		prdPriceVat = parseFloat(prdPriceNoVat * 0) + parseFloat(prdPriceNoVat);
	}
	
	$('#sellPriceVat').val(prdPriceVat);
}

function calculateSellPrice(healthPremiumNoVat) {
	var prdPrice = $('#prdPrice').val();
	var markupAmt = $('#markupAmountNoVat').val();
	var referralAmt = $('#referralAmtNoVat').val();
	var healthPremium = $('#healthPremiumNoVat').val();
	var prdPriceNoVat;
	
	prdPriceNoVat = prdPrice + markupAmt + referralAmt + healthPremium;
	
	$('#sellPriceWithoutVat').val(prdPriceNoVat);
}

function calculateSupplyVAT(buyPriceNoVat){
	var vatType = $('#vatRate2').val();
	var buyPriceNoVat = $('#buyPriceNoVat').val();
	var vatRate;
	var vatAmt;
	var buyingPriceVAT;
	if(vatType == 'sixteenPercent'){
		vatRate = 0.16;
	} else {
		vatRate = 0;
	}
	
	vatAmt = parseFloat(vatRate * buyPriceNoVat) + parseFloat(buyPriceNoVat);
	
	vatAmt = vatAmt.toFixed(2);
	$('#buyPriceVat').val(parseFloat(vatAmt));
}

$(document).ready(function() {
    $('.searchSelect').select2();
});

</SCRIPT>
    
  
        
</head>

<body>
<div class="content-body">    
    
    <div class="page-header">
        <div>
            <label>Add Product</label>            
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
          <a href="createProduct.action?pid=<%=session.getAttribute("session_refno").toString() %>">Add Products</a> 
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
							<form name="productDemandForm" id="productDemandForm" enctype="multipart/form-data" class="amur-form" method="post">
									
									<div class="form-group">
										<label>Category Name<span>*</span> :</label>
										<select id="catname" name="catname" onChange="fecthSubCatList()" class="form-control searchSelect" >
											<option value="">Select Category...</option>
										</select>	
									</div>  
									
									<div class="form-group">
										<label>Sub Category Name<span>*</span> :</label>
										<select id="subcatname" name="subcatname" data-placeholder="Select Sub Category..." onChange="fetchProductList()" class="form-control searchSelect">
											<option value="">Select Sub Category...</option>
										</select> 
									</div>
									
									<div class="form-group">
										<label>Supplier Name<span>*</span> :</label>
										<select id="manfname" name="manfname" data-placeholder="Select Supplier Name..." class="form-control searchSelect">
											<option value="">Select Supplier Name...</option>
										</select>
									</div>
									
									<div class="form-group">
										<label>Supplier Product Name<span>*</span> :</label>
										<select id="prdSupplierId" name="prdSupplierId" data-placeholder="Select Supplier Product Name..." onChange="fetchProductPrice()" class="form-control searchSelect">
											<option value="">Select Supplier Product Name...</option>
										</select>
									</div>
																		
									<div class="form-group">
										<label>Amur Product Name<span>*</span> :</label>
										<input type="text" name="amurDemandName" id="amurDemandName" class="form-control" placeholder="Enter Amur Product Name..."/>
									</div>
									
									<div class="form-group">
										<label>Product Description<span>*</span> :</label>
										<input type="text" name="productDemandDesc" id="productDemandDesc" class="form-control" placeholder="Enter Product Description..."/>
									</div>
									
									<div class="form-group">
										<label>SKU Size<span>*</span> :</label>
										<input type="text" name="skuSize" id="skuSize" class="form-control" placeholder="Enter Stock Keeping Unit (Size)..."/>
									</div>
									
									<div class="form-group">
										<label>SKU Unit<span>*</span> :</label>
										<input type="text" name="skuUnit" id="skuUnit" class="form-control" placeholder="Enter Stock Keeping Unit (Unit)..."/>
									</div>
									
									<div class="form-group">
										<label>Supply Buying Price (Excluding VAT)<span>*</span> :</label>
										<input type="text" name="sellPriceNoVat" id="sellPriceNoVat" readonly class="form-control" onChange="calculatePremium()" placeholder="Enter Selling Price..."/>
									</div>
									
									<div class="form-group">
										<label>Markup Type<span>*</span> :</label>
										<select id="markupType" name="markupType" data-placeholder="Select Markup Type..." class="form-control searchSelect">
											<option value="">Select Markup Type...</option>
											<option value="percentile">Percent</option>
											<option value="flat">Flat</option>
										</select>
									</div>
									
									<div class="form-group">
										<label>Markup<span>*</span> :</label>
										<input type="text" name="markupRate" id="markupRate" onChange="calculateMarkup()" class="form-control" placeholder="Enter Markup Rate..."/>
									</div>
									
									<div class="form-group">
										<label>Markup Amount on Buying Price<span>*</span> :</label>
										<input type="text" name="markupAmountNoVat" id="markupAmountNoVat" class="form-control" placeholder="Enter Markup Rate..."/>
									</div>
									
									<div class="form-group">
										<label>Selling Price (Without VAT)<span>*</span> :</label>
										<input type="text" name="sellPriceWithoutVat" id="sellPriceWithoutVat" class="form-control" placeholder="Enter Selling Price (without VAT)..."/>
									</div>
									
									<div class="form-group">
										<label>VAT Type<span>*</span> :</label>
										<select id="priceVatType" name="priceVatType" data-placeholder="Select VAT Type..." onchange="calculateVatOnPrdPrice()" class="form-control searchSelect">
											<option value="">Select VAT Type...</option>
											<option value="sixteenPercent">16%</option>
											<option value="exempt">Exempt</option>
											<option value="zeroRated">Zero Rated</option>
										</select>
									</div>
	
									<div class="form-group">
										<label>Selling Price (With VAT)<span>*</span> :</label>
										<input type="text" name="sellPriceVat" id="sellPriceVat" class="form-control" placeholder="Enter Selling Price (with VAT)..."/>
									</div>
									
									<div class="form-group">
										<label>Referral Earnings Rate<span>*</span> :</label>
										<input type="text" name="referralRate" id="referralRate" onChange="calculateReferralNoVat()" class="form-control"/>
									</div>
									
									<div class="form-group">
										<label>Referral Earnings on Buying Price<span>*</span> :</label>
										<input type="text" name="referralAmtNoVat" id="referralAmtNoVat" class="form-control" readonly/>
									</div>
																	
									<div class="form-group">
										<label>Health Premium Rate<span>*</span> :</label>
										<input type="text" name="healthPremiumRate" id="healthPremiumRate" onChange="calculateHealthPremiumNoVat()" class="form-control"/>
									</div>
									
									<div class="form-group">
										<label>Health Premium (Without VAT)<span>*</span> :</label>
										<input type="text" name="healthPremiumNoVat" id="healthPremiumNoVat" onChange="calculateSellPriceNoVat()" class="form-control" readonly/>
									</div>
																		
									
									
									<div class="form-group">
										<label>Attach Image<span>*</span> :</label>
											<!-- <input type='file' name="productImage" id="productImage" accept="image/*" onchange="readURL(this);"/> -->
											<input type="file" name="offerImage" id="offerImage" accept="image/*">
											<!-- <img id="blah" id="prodImage" src="http://placehold.it/180" alt="product image"/> -->
									</div>
									
									<input type="hidden" id="prdPrice" name="prdPrice"/>
									<input type="hidden" id="totalVatAmt" name="totalVatAmt"/>
							</form>
							<div class="button-container">
								<input type="button" class="btn btn-success" value="Save" id="saveSupplyDemand"/>
								<input type="button" class="btn btn-info" value="Clear" id="clearSupplyDemand"/>
								<input type="button" class="btn btn-danger" value="Back" id="goBack"/>
							</div>
						</div>
						
						
						
						<div id="product_supply" class="tabcontent">
							<form name="productSupplyForm" id="productSupplyForm" class="amur-form" method="post">
								<fieldset>
									<div class="form-group">
										<label>Category Name<span>*</span> :</label>
										<select id="catname2" name="catname2" onChange="fecthSubCatList2()" class="form-control searchSelect" >
											<option value="">Select Category...</option>
										</select>	
									</div>  
									
									<div class="form-group">
										<label>Sub Category Name<span>*</span> :</label>
										<select id="subcatname2" name="subcatname2" data-placeholder="Select Sub Category..."  class="form-control searchSelect">
											<option value="">Select Sub Category...</option>
										</select> 
									</div>
									
									<div class="form-group">
										<label>Supplier Name<span>*</span> :</label>
										<select id="manfname2" name="manfname2" data-placeholder="Select Manufacturer..." class="form-control searchSelect">
											<option value="">Select Supplier Name...</option>
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
										<select id="vatRate2" name="vatRate2" onChange="calculateSupplyVAT()" data-placeholder="Select VAT Rate..." class="form-control searchSelect">
											<option value="">Select VAT Rate...</option>
											<option value="sixteenPercent">16 Percent</option>
											<option value="exempt">Exempt</option>
											<option value="zeroRated">Zero Rated</option>
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
						</div>
						 
						</div></div>
					</div>    	
	    	
		    </div>
	    </div>
        
    <script type="text/javascript">
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
    
</body>
</html>

