
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


function fecthSubCatList(catname){

	var catejson = '${catalogRespJSON.CATEGORY_JSON}';
	//var catname=$('#catname').val();
	var arr = jQuery.parseJSON(catejson)
	var subarr ="";
	for (var i = 0; i < arr.length; i++) 
	{
	 console.log("catval"+catname);
	 console.log("arrobjcatval"+arr[i].categoryId);
		if (catname==arr[i].categoryId){
		subarr =arr[i].subCatSet;
		console.log("INTO LOOPP"+catname);
		$("#subcatname option").remove();
		for (var j = 0; j < subarr.length; j++) 
		{
			var options1 = $('<option/>', {value: subarr[j].subCategoryId, text: subarr[j].subCategoryName}).attr('data-id',j);
			console.log('indexsubbbb: ' + j + ', id: ' + subarr[j].subCategoryId );  
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
	console.log("catjson ["+catjson+"]");
	
	var manfjson = 	'${manfRespJSON.MANUFACTURER_JSON}';
	console.log("manfjson ["+manfjson+"]");
	
	var disctypejson='${responseJSON.disctype_json}';
	
	console.log("disctypejson["+disctypejson+"]");
	
	var prdid = '${responseJSON.PRODUCT_INFO.prdId}';
	$('#prdid').val(prdid);
	
	console.log("prdid["+prdid+"]");
	
	if (disctype == "F")
		$('#disctype').val('Flat');
	else
		$('#disctype').val('Percentage');
	
	
	
	var arr = jQuery.parseJSON(catjson)
	for (var i = 0; i < arr.length; i++) 
	{
	   	console.log('index: ' + i + ', id: ' + arr[i].categoryId );    
		var options = $('<option/>', {value: arr[i].categoryId, text: arr[i].categoryName}).attr('data-id',i);
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
		var manfops = $('<option/>', {value: manfarr[m].manufacturerId, text: manfarr[m].manufacturerName}).attr('data-id',m);   		
		$('#manfname').append(manfops);
	}
	
	$('#manfname').val('${responseJSON.PRODUCT_INFO.manufacturerId}');
	$('#manfname').trigger("liszt:updated"); 
	
	var disctypearr = jQuery.parseJSON(disctypejson)
	for (var t = 0; t < disctypearr.length; t++) 
	{
		var typeops = $('<option/>', {value: disctypearr[t].disctype, text: disctypearr[t].typedesc}).attr('data-id',t);   		
		$('#disctype').append(typeops);
	}
	
	$('#disctype').val('${responseJSON.PRODUCT_INFO.discountType}');
	$('#disctype').trigger("liszt:updated"); 

	
	/*$('#btn-submit').on('click',function(){
		var product_name = $('#new_product_name').val();
        var category_name = $('.new_catname').val();
        var subcategory_name = $('#subcatname').val();  
        var manufacturer_name = $('#manfname').val();        
        var retail_price = $('#new_pcode').val();
        var quantity = $('#new_quantity').val();  
        var wholesale_price = $('#new_wholesaleprice').val();
        var product_description = $('#new_prddesc').val();        
        var discount_type = $('#disctype').val();  
        var restocking_level = $('#new_reordrlvl').val();
        var model = $('#new_modelno').val();
        var discount = $('#new_discount').val();        
        var service_provider = $('#new_provider').val();
        
        $("#new_product_name").val(product_name);
        //$("#catname").val(category_name);
        //$("#subcatname").val(subcategory_name);
        //$("#manfname").val(manufacturer_name);
        $("#pcode").val(retail_price);
        $("#quantity").val(quantity);
        $("#wholesaleprice").val(wholesale_price);
        $("#prddesc").val(product_description);
        //$("#disctype").val(discount_type);
        $("#reordrlvl").val(restocking_level);
        $("#modelno").val(model);
        $("#discount").val(discount);
        $("#provider").val(service_provider);
        
        $('#my_modal').appendTo("body").modal('show');  
	});
	*/
	
	$('#btn-cancel').on('click',function() { 
		$("#product-modify-form")[0].action="<%=request.getContextPath()%>/<%=appName %>/products.action";
		$("#product-modify-form").submit();	 
	});
	
	$('#btn-submit').on('click',function() { 
		$("#product-modify-form")[0].action="<%=request.getContextPath()%>/<%=appName %>/productModifyConfirm.action";
		$("#product-modify-form").submit();	 
	});
	
	
	
		
});
	//--> 
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
		    	<form method="post" id="product-modify-form" name="product-form" class="amur-forms">
			       		<div class="col-sm-4 col-xs-6">
							<div class="form-group">
						       	<label class="col-form-label" for="new_product_name">Product Name</label>				                    	
						       	<input class="form-control" id="new_product_name" name="prdname" type="text" value="${responseJSON.PRODUCT_INFO.prdName}">
						    </div>
							<div class="form-group">
							    <label class="col-form-label" for="manfname">Manufacturer Name</label>
							    <select id="manfname" name="manfname" required='true' data-placeholder="Choose Manufacturer..." class="form-control">
									<option value="">Choose Manufacturer...</option>
								</select>
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="new_pcode">Price</label>
							    <input class="form-control" id="new_pcode" name="pcode" type="text" value="${responseJSON.PRODUCT_INFO.price}">
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="new_quantity">Quantity</label>
							    <input class="form-control" id="new_quantity" name="quantity" type="text" value="${responseJSON.PRODUCT_INFO.quantity}">
							</div>
					       	<div class="form-group">
							    <label class="col-form-label" for="new_wholesaleprice">Whole Sale Price</label>
							    <input class="form-control" id="new_wholesaleprice" name="wholesaleprice" type="text" value="${responseJSON.PRODUCT_INFO.wholeSaleAmt}">
							</div>							
					    </div>
					                
					    <div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="new_catname">Category Name</label>
					           	<select id="catname" name="catname" onChange="fecthSubCatList()"  required='true' class="form-control">
									<option value="">Choose Category...</option>
								</select> 				      	
							</div>
					       	<div class="form-group">
					           	<label class="col-form-label" for="new_prddesc">Product Description</label>
					           	<input class="form-control" id="new_prddesc" name="prddesc" type="text" value="${responseJSON.PRODUCT_INFO.prdDesc}">
					       	</div>
					       	<div class="form-group">
					           	<label class="col-form-label" for="new_disctypee">Discount Type</label>
					           	<select id="disctype" name="disctype" required='true' data-placeholder="Choose Discount Type" class="form-control">
										<option value="">Choose Discount Type...</option>
								</select>				      	
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="new_reordrlvl">Restocking Level</label>
							    <input class="form-control" id="new_reordrlvl" name="reordrlvl" type="text" value="${responseJSON.PRODUCT_INFO.recorderLevel}">
							</div>
					   	</div>
					                
					   	<div class="col-sm-4 col-xs-6">
					       	<div class="form-group">
					           	<label class="col-form-label" for="new_subcatname">Sub Category Name</label>					          	
								<select id="subcatname" name="subcatname" required='true' data-placeholder="Choose Sub Category..." class="form-control new_subcatname">
									<option value="">Choose Sub Category...</option>
								</select>
					      	</div>
					       	<div class="form-group">
					           	<label class="col-form-label" for="new_modelno">Model/Brand</label>
					          	<input class="form-control" id="new_modelno" name="modelno" type="text" value="${responseJSON.PRODUCT_INFO.modelBrandNo}">
					       	</div>
					       	<div class="form-group">
							    <label class="col-form-label" for="new_discount">Discount</label>
							    <input class="form-control" id="new_discount" name="discount" type="text" value="${responseJSON.PRODUCT_INFO.discount}">
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="new_provider">Service Provider / Retailer</label>
							    <input class="form-control" id="new_provider" name="provider" type="text"  value="${responseJSON.PRODUCT_INFO.serviceProvider}">
							</div>
					    </div>
					    <input type="hidden" name="type"  id="type" value="Modify"/>
						<input type="hidden" name="prdid"  id="prdid" value="${responseJSON.PRODUCT_INFO.prdId}">          
		       	</form>
		    </div>
	    </div>
    </div>
    
	<div class="content-panel">
    	<div class="container">
    		<div class="row">
	    		<div class="col-sm-4 col-xs-6">
	         		<a href="#" class="btn activate" id="btn-submit" role="button">Save</a>
	         		<a href="#" class="btn deactivate" id="btn-cancel" role="button">Back</a>
	   			</div>
	    	</div>
    	</div>
    </div>
    
    <!--div class="modal fade" id="my_modal">
      	<div class="modal-dialog">
        	<div class="modal-content">
	          	<div class="modal-header">            
	            	<label class="modal-title">Confirm Offer</label>
	          	</div>
	          	<div class="modal-body">
		            <form method="post" id="product-modify-form" name="product-form" class="amur-forms">
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="col-form-label" for="product_name">Product Name</label>				                    	
								<input class="form-control" id="product_name" name="prdname" type="text" value="${responseJSON.PRODUCT_INFO.prdName}" readonly>
							</div>
							<div class="form-group">
								<label class="col-form-label" for="manfname">Manufacturer Name</label>
								<select id="manfname" name="manfname" required='true' data-placeholder="Choose Manufacturer..." class="form-control">
									<option value="">Choose Manufacturer...</option>
								</select>
							</div>
							<div class="form-group">
								<label class="col-form-label" for="pcode">Price</label>
								<input class="form-control" id="pcode" name="pcode" type="text" value="${responseJSON.PRODUCT_INFO.price}" readonly>
							</div>
							<div class="form-group">
								<label class="col-form-label" for="quantity">Quantity</label>
								<input class="form-control" id="quantity" name="quantity" type="text" value="${responseJSON.PRODUCT_INFO.quantity}" readonly>
							</div>
							<div class="form-group">
								<label class="col-form-label" for="wholesaleprice">Whole Sale Price</label>
								<input class="form-control" id="wholesaleprice" name="wholesaleprice" type="text" value="${responseJSON.PRODUCT_INFO.wholeSaleAmt}" readonly>
							</div>							
						</div>
								                
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="col-form-label" for="catname">Category Name</label>
								<select id="catname" name="catname" onChange="fecthSubCatList()"  required='true' class="form-control">
									<option value="">Choose Category...</option>
								</select> 				      	
							</div>
							<div class="form-group">
								<label class="col-form-label" for="prddesc">Product Description</label>
								<input class="form-control" id="prddesc" name="prddesc" type="text" value="${responseJSON.PRODUCT_INFO.prdDesc}" readonly>
							</div>
							<div class="form-group">
								<label class="col-form-label" for="catname">Discount Type</label>
								<select id="disctype" name="disctype" required='true' data-placeholder="Choose Discount Type" class="form-control" readonly>
									<option value="">Choose Discount Type...</option>
								</select>				      	
							</div>
							<div class="form-group">
								<label class="col-form-label" for="reordrlvl">Restocking Level</label>
								<input class="form-control" id="reordrlvl" name="reordrlvl" type="text" value="${responseJSON.PRODUCT_INFO.recorderLevel}" readonly>
							</div>
						</div>
								                
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="col-form-label" for=""subcatname"">Sub Category Name</label>					          	
								<select id="subcatname" name="subcatname" required='true' data-placeholder="Choose Sub Category..." class="form-control" readonly>
									<option value="">Choose Sub Category...</option>
								</select>
							</div>
							<div class="form-group">
								<label class="col-form-label" for="modelno">Model/Brand</label>
								<input class="form-control" id="modelno" name="modelno" type="text" value="${responseJSON.PRODUCT_INFO.modelBrandNo}" readonly>
							</div>
							<div class="form-group">
								<label class="col-form-label" for="discount">Discount</label>
								<input class="form-control" id="discount" name="discount" type="text" value="${responseJSON.PRODUCT_INFO.discount}" readonly>
							</div>
							<div class="form-group">
								<label class="col-form-label" for="provider">Service Provider / Retailer</label>
								<input class="form-control" id="provider" name="provider" type="text"  value="${responseJSON.PRODUCT_INFO.serviceProvider}" readonly>
							</div>
						</div>
						<input type="hidden" name="type"  id="type" value="Modify"/>
						<input type="hidden" name="prdid"  id="prdid" value="${responseJSON.PRODUCT_INFO.prdId}">          
				    </form>
	          	</div>
	          	<div class="modal-footer">            
	            	<a href="#" class="btn activate" id="btn-confirm-product" role="button">Save Changes</a>
	            	<a href="#" class="btn deactivate" id="btn-clear" data-dismiss="modal" role="button">Cancel</a>
	          	</div>
        	</div><!-- /.modal-content ->
      	</div><!-- /.modal-dialog ->
    </div--><!-- /.modal -->	
</body>
</html>

