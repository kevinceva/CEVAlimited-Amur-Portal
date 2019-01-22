
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

console.log(toDisp);

function getOptionFor(optionText,optionValue)
{
	return "<option value='"+optionValue+"'>"+optionText+"</option>";
}

//Fetch subcategory list according to category selected
function fecthSubCatList(catname){
	var catejson = '${catalogRespJSON.CATEGORIES}';	
	var arr = jQuery.parseJSON(catejson)
	var subarr ="";
	for (var i = 0; i < arr.length; i++) 
	{
	 //console.log("catval"+arr[i].catname);
	 //console.log("arrobjcatval"+arr[i].categoryId);
		if (catname==arr[i].categoryId){
		subarr =arr[i].subCatSet;
		//console.log("INTO LOOPP"+catname);
		$("#subcatname option").remove();
		for (var j = 0; j < subarr.length; j++) 
		{
			var options1 = $('<option/>', {value: subarr[j].subCategoryId, text: subarr[j].subCategoryName}).attr('data-id',j);
			//console.log('indexsubbbb: ' + j + ', id: ' + subarr[j].subCategoryId );  
			$('#subcatname').append(options1);
		}
		}
	} 
}

//Load supplier product incase subcategory changes
function fetchProductList(subcatname){

	var prdjson = '${supplyProductJSON.CAT_PRODUCTS}';
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



$(document).ready(function(){
		
	//Populate product category
	fecthSubCatList('${productJSON.PRODUCT_INFO.catName}');
	$('#subcatname').val('${productJSON.PRODUCT_INFO.subCatName}');
	$('#subcatname').trigger("liszt:updated"); 
		
	$('#catname').val('${productJSON.PRODUCT_INFO.catName}');
	$('#catname').trigger("liszt:updated"); 
	
	var catjson = '${catalogRespJSON.CATEGORIES}';
	//console.log("catjson ["+catjson+"]");
	
	var manfjson = 	'${manfRespJSON.SUPPLIER_LIST}';
	//console.log("manfjson ["+manfjson+"]");
	
	
	//Populate Category Dropdown
	var arr = jQuery.parseJSON(catjson);
	//var arr = catjson;
	for (var i = 0; i < arr.length; i++) 
	{
	   	//console.log('index: ' + i + ', id: ' + arr[i].categoryId );    
		var options = $('<option/>', {value: arr[i].categoryId, text: arr[i].categoryName}).attr('data-id',i);
		$('#catname').append(options);
	} 
	
	//Fetch subcategory when category changes
	$('#catname').change(function(){
		fecthSubCatList($('#catname').val());	
	});
	
	//Populate product category field
	$('#catname').val('${productJSON.PRODUCT_INFO.catName}');
	$('#catname').trigger("liszt:updated");

	//Populate manufacturer dropdown
	var manfarr = jQuery.parseJSON(manfjson)
	for (var m = 0; m < manfarr.length; m++) 
	{
		var manfops = $('<option/>', {value: manfarr[m].manufacturerId, text: manfarr[m].manufacturerName}).attr('data-id',m);   		
		$('#manfname').append(manfops);
	}
	
	//Populate product manufacturer field
	$('#manfname').val('${productJSON.PRODUCT_INFO.manfname}');
	$('#manfname').trigger("liszt:updated"); 
	
	if($('#subcatname').val()==""){
		fecthSubCatList('${productJSON.PRODUCT_INFO.catName}');
	} else {
		
	}
	
	//Catalog Products loading	
	var prdjson = '${supplyProductJSON.CAT_PRODUCTS}';
	var prdarr = JSON.parse(prdjson);
	var subarr = "";
	
	for (var n = 0; n < prdarr.length; n++) 
	{	
		//console.log(prdarr[n].SUB_CAT_PRODUCTS);
		subarr = prdarr[n].SUB_CAT_PRODUCTS;
		for (var j = 0; j < subarr.length; j++){
			//console.log("Catalog product name :: "+subarr[j].PRD_NAME);
			var options = $('<option/>', {value: subarr[j].PRD_ID, text: subarr[j].PRD_NAME}).attr('data-id',i);
			$('#prdSupplierId').append(options);
		}
	   	
	}
	
	//Preload supplier product name for specific product
	$('#prdSupplierId').val('${productJSON.PRODUCT_INFO.prdSupplyId}');
	$('#prdSupplierId').trigger("liszt:updated");
		
	
	
	
	
	//Populate Products Information details
	$('#amurDemandName').val('${productJSON.PRODUCT_INFO.prdName}');
	$('#productDemandDesc').val('${productJSON.PRODUCT_INFO.prdDesc}');
	$('#skuSize').val('${productJSON.PRODUCT_INFO.skuSize}');
	$('#skuUnit').val('${productJSON.PRODUCT_INFO.skuUnit}');
	
	
	
	
	//Populate product markup inforamtion
	$('#markupType').val('${productJSON.PRODUCT_INFO.markupType}');
	$('#markupType').trigger("liszt:updated");	
	$('#markupAmountNoVat').val('${productJSON.PRODUCT_INFO.markupValue}');
	
	
	
	//Convert markupRate to real numbers
	var markupRate = '${productJSON.PRODUCT_INFO.markupRate}';
	
	if($('#markupType').val() == "percentile"){
		markupRate = markupRate * 100;
	} else {
		markupRate = markupRate;
	}
	$('#markupRate').val(markupRate);
	
	
	
	//Populate referral information
	$('#referralRate').val('${productJSON.PRODUCT_INFO.referralRate}');
	$('#referralAmtNoVat').val('${productJSON.PRODUCT_INFO.referralAmt}');
	
	
	
	//Convert Referral Rate to real numbers
	var referralRate = '${productJSON.PRODUCT_INFO.referralRate}';
	referralRate = referralRate * 100;
	console.log("Referral rate :: "+referralRate);
	$('#referralRate').val(referralRate);
	
		
	//Populate health premium information
	$('#healthPremiumRate').val('${productJSON.PRODUCT_INFO.healthPremRate}');
	//$('#healthPremiumNoVat').val('${productJSON.PRODUCT_INFO.healthPremAmt}');
	
	
	
	
	//Convert Health Rate to real numbers
	var healthPremiumRate = '${productJSON.PRODUCT_INFO.healthPremRate}';	
	healthPremiumRate = healthPremiumRate * 100;
	$('#healthPremiumRate').val(healthPremiumRate);
	calculateHealthPremiumNoVat();
	
	//Populate supplier buying price without VAT
	function fetchProductPrice(prdId){
		var prdJson = '${supplyProductJSON.CAT_PRODUCTS}';
		var prdId = $('#prdSupplierId').val();
		var arr = jQuery.parseJSON(prdJson);
		var prdPrice = "";
		var subarr = "";
		
		for (var i = 0; i < arr.length; i++){
			var subarr = arr[i].SUB_CAT_PRODUCTS;
			for (var j = 0; j < subarr.length; j++) {
				if(prdId == subarr[j].PRD_ID){
					prdPrice = subarr[j].PRD_PRICE;
					console.log("Prd Price :: "+prdPrice);
					
				}
			}
		}
		
		$('#sellPriceNoVat').val(prdPrice);
		$('#prdPrice').val(prdPrice);
	}
	
	
	if($('#prdSupplierId').val()){
		fetchProductPrice();
		
	}else{
		fetchProductPrice();
		
	}
	
	
	//Setting up VAT Type
	$('#priceVatType').val('${productJSON.PRODUCT_INFO.vatType}');
	$('#prdImageName').val('${productJSON.PRODUCT_INFO.prdImageName}');
	
	$('#prdImgPrev').attr('src','${productJSON.PRODUCT_INFO.prdImage}');
	
	
});	


//Submitting form data




$(document).ready(function(){	
	var submitPath = "<%=request.getContextPath()%>/<%=appName%>/productModifyConfirm.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName%>/products.action";
	
	$('#saveSupplyDemand').on('click',function() {
		
		
			swal({
	            title: "Save product changes?",
	            text: "Press Ok to save.", 
	            icon: "warning",
	            buttons: true,
	            dangerMode: true,
	        })
	        .then((willDelete) => {
	          if (willDelete) {
	        	  $("#product-modify-form").submit(function(e) {
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
					    		  	text: "Product edited successfully.",
					    		  	icon: "success",
					    		  	button: "Continue",
					    		}).then(function(result){				    					    			
					    			window.location.href = backPath;
					            });
		      		        },
		      		      	error: function (response) {
		      		      	swal({
					    		  title: "Sorry!",
					    		  text: "Product edit failed. Please try again later.",
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
		      		$("#product-modify-form").submit();
		          } else {
		            swal("Request Cancelled.");
		          }
	        });
		//} 	
	}); 
});














$(function(){
	function readURL(input) {

		  if (input.files && input.files[0]) {
		    var reader = new FileReader();

		    reader.onload = function(e) {
		      $('#prdImgPrev').attr('src', e.target.result);
		    }

		    reader.readAsDataURL(input.files[0]);
		  }
		}

		$("#prdImg").change(function() {
		  readURL(this);
		});
});


$(function(){
	$('#goBack').on('click',function() { 
		$("#product-modify-form")[0].action="<%=request.getContextPath()%>/<%=appName %>/products.action";
		$("#product-modify-form").submit();	 
	});
});



//Calculating markup, sellingPriceWithVat on default
$(function(){
	if($('#markupRate').val()){
		calculateMarkup();		
	}else{
		
	};
	
	if($('#priceVatType').val()){
		calculateVatOnPrdPrice();		
	}else{
		
	}
	
	if($('#sellPriceVat').val()){	
		calculateHealthPremiumNoVat();
		calculateReferralNoVat();
	}else{
		//alert("Selling price has no value");
	}
});



//Round off to 2 decimals
function roundOff(number){
	(Math.round( number * 100 )/100 ).toString(); 
	var newNumber = number.toFixed(2);
	//console.log(newNumber);
	return newNumber;
}


//Calculate Markup
function calculateMarkup(mrkRate){
	var prdPrice = $('#prdPrice').val();
	var markupType = $('#markupType').val();
	var markupRate = $('#markupRate').val();
	var sellPriceWithoutVat;
	var markupAmount;
		
	if(markupType == 'percentile'){
		var markupAmount = (markupRate / 100) * parseFloat(prdPrice);
		markupAmount = markupAmount.toFixed(2);
		$('#markupAmountNoVat').val(roundOff(parseFloat(markupAmount)));		
	} else {
		markupAmount = markupRate;
		$('#markupAmountNoVat').val(roundOff(parseFloat(markupAmount)));
	}
	
	sellPriceWithoutVat = parseFloat(markupAmount) + parseFloat(prdPrice);
	$('#sellPriceWithoutVat').val(roundOff(sellPriceWithoutVat));	
};

//Calculate referral amount without VAT
function calculateReferralNoVat(referralRate){
	var prdPrice = $('#sellPriceVat').val();
	var referralRate = $('#referralRate').val();
	var referralAmtNoVat;
	
	referralAmtNoVat = parseFloat(referralRate / 100) * parseFloat(prdPrice);
	referralAmtNoVat = referralAmtNoVat.toFixed(2);
	$('#referralAmtNoVat').val(referralAmtNoVat);
}

//Calculate health premium amount without VAT
function calculateHealthPremiumNoVat(healthPremiumRate){
	$('#healthPremiumNoVat').val("");		
	
	var prdPrice = $('#sellPriceVat').val();
	var healthPremiumRate = $('#healthPremiumRate').val();
	//console.log("Health premium Rate :: "+healthPremiumRate);
	//console.log("Product Price :: "+prdPrice);
	var healthPremiumNoVat;	
	healthPremiumNoVat = parseFloat(healthPremiumRate / 100) * parseFloat(prdPrice);
	healthPremiumNoVat = parseFloat(healthPremiumNoVat);
	//console.log("Health premium :: "+roundOff(healthPremiumNoVat));
	$('#healthPremiumNoVat').val(healthPremiumNoVat);
}


//Calculate VAT on Product Price
function calculateVatOnPrdPrice(priceVatType){
	var vatType = $('#priceVatType').val();
	var prdPriceNoVat = $('#sellPriceWithoutVat').val();
	var prdPriceVat;
	
	if(vatType == 'sixteenPercent'){
		prdPriceVat = parseFloat(prdPriceNoVat * 0.16) + parseFloat(prdPriceNoVat);
	} else {
		prdPriceVat = parseFloat(prdPriceNoVat * 0) + parseFloat(prdPriceNoVat);
	}
	
	$('#sellPriceVat').val(Math.round(roundOff(prdPriceVat)));
}





</SCRIPT>
	
</head>

<body>
	<div class="page-header">
        <div>
            <label>Edit Product</label>        
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
          <a href="#">Change Product Information</a>       
        </li>
    </ol>
		
	<div class="content-panel">
    	<div class="container">
	    	<div class="row">    		
		       	
		       	<form name="product-modify-form" id="product-modify-form" enctype="multipart/form-data" class="amur-form" method="post">
									
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
										<label>Referral Earnings Rate (%)<span>*</span> :</label>
										<input type="text" name="referralRate" id="referralRate" onChange="calculateReferralNoVat()" class="form-control"/>
									</div>
									
									<div class="form-group">
										<label>Referral Earnings on Buying Price<span>*</span> :</label>
										<input type="text" name="referralAmtNoVat" id="referralAmtNoVat" class="form-control" readonly/>
									</div>
																	
									<div class="form-group">
										<label>Health Premium Rate (%)<span>*</span> :</label>
										<input type="text" name="healthPremiumRate" id="healthPremiumRate" onChange="calculateHealthPremiumNoVat()" class="form-control"/>
									</div>
									
									<div class="form-group">
										<label>Health Premium (Without VAT)<span>*</span> :</label>
										<input type="text" name="healthPremiumNoVat" id="healthPremiumNoVat" onChange="calculateSellPriceNoVat()" class="form-control" readonly/>
									</div>
																		
									
									
									<div class="form-group">
										<label>Attach Image<span>*</span> :</label>
										<!-- <input type='file' name="productImage" id="productImage" accept="image/*" onchange="readURL(this);"/> -->
										<input type="file" name="prdImg" id="prdImg" accept="image/*">
										<img id="prdImgPrev" src="http://placehold.it/180" alt="product image"/>
									</div>
									
									<input type="hidden" id="prdPrice" name="prdPrice"/>
									<input type="hidden" id="totalVatAmt" name="totalVatAmt"/>
									<input type="hidden" id="prdImageName" name="prdImageName"/>
									
									<input type="hidden" name="type"  id="type" value="Modify"/>
									<input type="hidden" name="prdid"  id="prdid" value="${productJSON.PRODUCT_INFO.prdid}"> 
							</form>
							
		       	
		       	
		    </div>
	    </div>
    </div>
    
	<div class="content-panel">
    	<div class="container">
    		<div class="row">
	    		<div class="button-container">
								<input type="button" class="btn btn-success" value="Save" id="saveSupplyDemand"/>
								<input type="button" class="btn btn-info" value="Clear" id="clearSupplyDemand"/>
								<input type="button" class="btn btn-danger" value="Back" id="goBack"/>
							</div>
	    	</div>
    	</div>
    </div>
    	
</body>
</html>

