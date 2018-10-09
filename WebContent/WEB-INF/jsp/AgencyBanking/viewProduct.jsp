
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
	
	//fecthSubCatList('${responseJSON.PRODUCT_INFO.categoryId}');
	/* $('#subcatname').val('${responseJSON.PRODUCT_INFO.subCategoryId}');
	$('#subcatname').trigger("liszt:updated"); 
	
	
	$('#catname').val('${responseJSON.PRODUCT_INFO.categoryId}');
	$('#catname').trigger("liszt:updated");  */
	
	var catjson = '${catalogRespJSON.CATEGORY_JSON}';
	
	var manfjson = 	'${manfRespJSON.MANUFACTURER_JSON}';
	
	var inCatName = '${responseJSON.PRODUCT_INFO.categoryId}';
	var inSubCatName = '${responseJSON.PRODUCT_INFO.subCategoryId}';
	var inManfName = '${responseJSON.PRODUCT_INFO.manufacturerId}';
	
	
	var arr = jQuery.parseJSON(catjson)
	for (var i = 0; i < arr.length; i++) 
	{
		if (arr[i].categoryId == inCatName)
		{
			$('#catname').val(arr[i].categoryName);
			$('#catname_spn').text(arr[i].categoryName);
			breakLoop = true;
		}
		var subarr ="";
		subarr =arr[i].subCatSet;
		for (var j = 0; j < subarr.length; j++) 
		{
			if (subarr[j].subCategoryId == inSubCatName)
			{
				$('#subcatname').val(subarr[j].subCategoryName);
				$('#subcatname_spn').text(subarr[j].subCategoryName);
				breakLoop = true;
			}
		}
	} 
	
	var manfarr = jQuery.parseJSON(manfjson);
	for (var i = 0; i < manfarr.length; i++) 
	{
		if (manfarr[i].manufacturerId == inManfName)
		{
			$('#manfname').val(manfarr[i].manufacturerName);
			$('#manfname_spn').text(manfarr[i].manufacturerName);
			breakLoop = true;
		}
	}

	$('#btn-cancel').on('click',function() { 
		$("#product-confirm-form")[0].action="<%=request.getContextPath()%>/<%=appName %>/products.action";
		$("#product-confirm-form").submit();	 
	});
	
		
});
 
</SCRIPT> 
		
</head>

<body>

	<div class="page-header">
        <div>
            <label>Product Details</label>        
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
          <a href="#">Product Details</a>       
        </li>
    </ol>
		
	<div class="content-panel">
    	<div class="container">
	    	<div class="row">
		    	<form method="post" id="product-confirm-form" name="product-form" class="amur-forms">
			       		<div class="col-sm-4 col-xs-6">
							<div class="form-group">
						       	<label class="col-form-label" for="product_name">Product Name</label>				                    	
						       	<input class="form-control" id="product_name" name="prdname" type="text" value="${responseJSON.PRODUCT_INFO.prdName}" readonly>
						    </div>
							<div class="form-group">
							    <label class="col-form-label" for="manfname">Manufacturer Name</label>
							    <input type="text" class="form-control" required=true name="manfname"  id="manfname"  readonly/>
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="pcode">Price</label>
							    <input class="form-control" id="pcode" name="pcode" type="text" value="${responseJSON.PRODUCT_INFO.price}" readonly>
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="quantity">Quantity</label>
							    <input class="form-control" id="quantity" name="quantity" type="text" value="${responseJSON.PRODUCT_INFO.quantity}" readonly>
							</div>							
					    </div>
					                
					    <div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="catname">Category Name</label>
					           	<input type="text" class="form-control" required=true name="catname"  id="catname"  readonly>					      	
							</div>
					       	<div class="form-group">
					           	<label class="col-form-label" for="prddesc">Product Description</label>
					           	<input class="form-control" id="prddesc" name="prddesc" type="text" value="${responseJSON.PRODUCT_INFO.prdDesc}" readonly>
					       	</div>
					       	<div class="form-group">
							    <label class="col-form-label" for="wholesaleprice">Whole Sale Price</label>
							    <input class="form-control" id="wholesaleprice" name="wholesaleprice" type="text" value="${responseJSON.PRODUCT_INFO.wholeSaleAmt}" readonly>
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="reordrlvl">Restocking Level</label>
							    <input class="form-control" id="reordrlvl" name="reordrlvl" type="text" value="${responseJSON.PRODUCT_INFO.recorderLevel}" readonly>
							</div>
					   	</div>
					                
					   	<div class="col-sm-4 col-xs-6">
					       	<div class="form-group">
					           	<label class="col-form-label" for=""subcatname"">Sub Category Name</label>					          	
								<input type="text" class="form-control" required=true name="subcatname"  id="subcatname"  readonly/>
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
					    <input type="hidden" name="type"  id="type" value="modify"/>
						<input type="hidden" name="catid"  id="catid" value="$('#catid')}">
						<input type="hidden" name="subcatid"  id="subcatid" value="$('#subcatid')}">
						<input type="hidden" name="manfid"  id="manfid" value="$('#manfid')}">            
		       	</form>
		    </div>
	    </div>
    </div>
    
    <div class="content-panel">
    	<div class="container">
    		<div class="row">
	    		<div class="col-sm-4 col-xs-6">
	         		<a href="#" class="btn deactivate" id="btn-cancel" role="button">Back</a>
	   			</div>
	    	</div>
    	</div>
    </div>	
    
</body>


</html>

