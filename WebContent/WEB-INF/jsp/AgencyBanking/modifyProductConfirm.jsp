
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
	
	var disctypejson='${responseJSON.disctype_json}';
	
/* 	var inCatName = '${responseJSON.PRODUCT_INFO.categoryId}';
	var inSubCatName = '${responseJSON.PRODUCT_INFO.subCategoryId}';
	var inManfName = '${responseJSON.PRODUCT_INFO.manufacturerId}'; */
	
	var inCatName='${catname}';
	var inSubCatName ='${subcatname}';
	var inManfName ='${manfname}';
	 var inPrdId='${prdid}';	
	var inDisctype='${disctype}';
	
	console.log("2  2 2 inDisctype["+inDisctype+"] inCatName["+inCatName+"] inSubCatName["+inSubCatName+"] inManfName["+inManfName+"] inPrdId["+inPrdId+"]");
	
	/* if (disctype == "F")
		$('#disctype_span').text('Flat');
	else
		$('#disctype_span').text('Percentage'); */
	
		$('#prdid').val(inPrdId);
	
	var arr = jQuery.parseJSON(catjson)
	for (var i = 0; i < arr.length; i++) 
	{
		if (arr[i].categoryId == inCatName)
		{
			$('#catname').val(arr[i].categoryName);
			$('#catname_spn').text(arr[i].categoryName);
			$('#catid').val(arr[i].categoryId);
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
				$('#subcatid').val(subarr[j].subCategoryId);
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
			$('#manfid').val(manfarr[i].manufacturerId);
			breakLoop = true;
		}
	}
	
	var disctypearr = jQuery.parseJSON(disctypejson)
	for (var t = 0; t < disctypearr.length; t++) 
	{
		if (disctypearr[t].disctype == inDisctype)
		{
			$('#disctype').val(disctypearr[t].typedesc);
			$('#disctype_spn').text(disctypearr[t].typedesc);
		}
		
	}
		
});

$(document).ready(function(){
			
	
	var submitPath = "<%=request.getContextPath()%>/<%=appName %>/productModifyAck.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName %>/products.action";
		
	$('#btn-submit').on('click',function() {
		
		$('#product-modifyconfirm-form').submit(function(){
		    $.post(submitPath, $(this).serialize(), function(json) {
		      
		      if(json.responseJSON.remarks == "SUCCESS"){
		    	  swal({
		    		  title: "Success",
		    		  text: "Product Changed successfully.",
		    		  icon: "success"
		    		}).then(function(result){
		    			  window.location = backPath;
		            });
		      }else {
		    	  swal({
		    		  title: "Sorry!",
		    		  text: "Product details cannot be changed at this moment.",
		    		  icon: "error"
		    		}).then(function(result){
		    			  window.location = backPath;
		            });
		      }
		      
		    }, 'json');
		    return false;
		  });	
		$("#product-modifyconfirm-form").submit();
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
            <label>Confirm Product Changes</label>        
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
		    	<form method="post" id="product-modifyconfirm-form" name="product-form" class="amur-forms">
			       		<div class="col-sm-4 col-xs-6">
							<div class="form-group">
						       	<label class="col-form-label" for="new_product_name">Product Name</label>				                    	
						       	<input class="form-control" id="new_product_name" name="prdname" type="text" value="${prdname}">
						    </div>
							<div class="form-group">
							    <label class="col-form-label" for="manfname">Manufacturer Name</label>
							    <span id="manfname_spn" class="form-control"></span><input type="hidden" class="field" required=true name="manfname"  id="manfname"  />
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="new_pcode">Price</label>
							    <input class="form-control" id="new_pcode" name="pcode" type="text" value="${pcode}">
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="new_quantity">Quantity</label>
							    <input class="form-control" id="new_quantity" name="quantity" type="text" value="${quantity}">
							</div>
					       	<div class="form-group">
							    <label class="col-form-label" for="new_wholesaleprice">Whole Sale Price</label>
							    <input class="form-control" id="new_wholesaleprice" name="wholesaleprice" type="text" value="${wholesaleprice}">
							</div>							
					    </div>
					                
					    <div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="new_catname">Category Name</label>
					           	<span id="catname_spn" class="form-control"></span><input type="hidden" class="field" required=true name="catname"  id="catname"  />			      	
							</div>
					       	<div class="form-group">
					           	<label class="col-form-label" for="new_prddesc">Product Description</label>
					           	<input class="form-control" id="new_prddesc" name="prddesc" type="text" value="${prddesc}">
					       	</div>
					       	<div class="form-group">
					           	<label class="col-form-label" for="new_disctypee">Discount Type</label>
					           	<span id="disctype_spn" class="form-control"></span><input type="hidden" class="field" required=true name="disctype"  id="disctype"/> 				      	
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="new_reordrlvl">Restocking Level</label>
							    <input class="form-control" id="new_reordrlvl" name="reordrlvl" type="text" value="${reordrlvl}">
							</div>
					   	</div>
					                
					   	<div class="col-sm-4 col-xs-6">
					       	<div class="form-group">
					           	<label class="col-form-label" for="new_subcatname">Sub Category Name</label>					          	
								<span id="subcatname_spn" class="form-control"></span><input type="hidden" class="field" required=true name="subcatname"  id="subcatname"/>
					      	</div>
					       	<div class="form-group">
					           	<label class="col-form-label" for="new_modelno">Model/Brand</label>
					          	<input class="form-control" id="new_modelno" name="modelno" type="text" value="${modelno}">
					       	</div>
					       	<div class="form-group">
							    <label class="col-form-label" for="new_discount">Discount</label>
							    <input class="form-control" id="new_discount" name="discount" type="text" value="${discount}">
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="new_provider">Service Provider / Retailer</label>
							    <input class="form-control" id="new_provider" name="provider" type="text"  value="${provider}">
							</div>
					    </div>
					    <input type="hidden" name="type"  id="type" value="Modify"/>
						<input type="hidden" name="prdid"  id="prdid" value="$('#prdid')}">
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
	         		<a href="#" class="btn activate" id="btn-submit" role="button">Confirm Changes</a>
	         		<a href="#" class="btn deactivate" id="btn-cancel" role="button">Go Back</a>
	   			</div>
	    	</div>
    	</div>
    </div>	
</body>

</html>

