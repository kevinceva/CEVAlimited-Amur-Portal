
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
	var inDisctype='${disctype}';
	
	console.log("2  2 2 inDisctype["+inDisctype+"] inCatName["+inCatName+"] inSubCatName["+inSubCatName+"] inManfName["+inManfName+"] ");
	
	/* if (disctype == "F")
		$('#disctype_span').text('Flat');
	else
		$('#disctype_span').text('Percentage'); */
	
	
	var arr = jQuery.parseJSON(catjson)
	for (var i = 0; i < arr.length; i++) 
	{
		if (arr[i].categoryId == inCatName)
		{
			$('#catname').val(arr[i].categoryName.replace(/[+]/g, " "));
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
				$('#subcatname').val(subarr[j].subCategoryName.replace(/[+]/g, " "));
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
			$('#manfname').val(manfarr[i].manufacturerName.replace(/[+]/g, " "));
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
	
	//var inCatid = '${responseJSON.catid}';
	//console.log("inCatid["+inCatid+"]");
	//$('#catid').val(inCatid);
	
		
	var submitPath = "<%=request.getContextPath()%>/<%=appName %>/productCreateAck.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName %>/products.action";
	
	$('#btn-submit').on('click',function() {
		
		$('#product-confirm-form').submit(function(){
		    $.post(submitPath, $(this).serialize(), function(json) {
		      
		      if(json.responseJSON.remarks == "SUCCESS"){
		    	  swal({
		    		  title: "Success",
		    		  text: "Product created successfully.",
		    		  icon: "success",
		    		  button: "Continue",
		    		}).then(function(result){
		    			  window.location = backPath;
		            });
		      }else {
		    	  swal({
		    		  title: "Sorry!",
		    		  text: "Your product cannot be created at this moment.",
		    		  icon: "error",
		    		  button: "Continue",
		    		}).then(function(result){
		    			  window.location = backPath;
		            });
		      }
		      
		    }, 'json');
		    return false;
		  });	
		$("#product-confirm-form").submit();
	});
	
	$('#btn-cancel').on('click',function() { 
		$("#no_name")[0].action="<%=request.getContextPath()%>/<%=appName %>/products.action";
		$("#no_name").submit();	 
	});
	
		
});

</SCRIPT>
    
  
		
</head>

<body>
	<div class="page-header">
        <div>
            <label>Confirm Product</label>        
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
          <a href="#">Product Creation Confirmation</a>       
        </li>
    </ol>
		
	<div class="content-panel">
    	<div class="container">
	    	<div class="row">
		    	<form method="post" id="product-confirm-form" name="product-form" class="amur-forms">
			       		<div class="col-sm-4 col-xs-6">
							<div class="form-group">
						       	<label class="col-form-label" for="product_name">Product Name</label>				                    	
						       	<input class="form-control" id="product_name" name="prdname" type="text" value="${prdname}" readonly>
						    </div>
							<div class="form-group">
							    <label class="col-form-label" for="manfname">Manufacturer Name</label>
							    <input type="text" class="form-control" required=true name="manfname"  id="manfname"  readonly/>
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="pcode">Price</label>
							    <input class="form-control" id="pcode" name="pcode" type="text" value="${pcode}" readonly>
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="quantity">Quantity</label>
							    <input class="form-control" id="quantity" name="quantity" type="text" value="${quantity}" readonly>
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="wholesaleprice">Whole Sale Price</label>
							    <input class="form-control" id="wholesaleprice" name="wholesaleprice" type="text" value="${wholesaleprice}" readonly>
							</div>
					    </div>
					                
					    <div class="col-sm-4 col-xs-6">
					        <div class="form-group">
					           	<label class="col-form-label" for="catname">Category Name</label>
					           	<input type="text" class="form-control" required=true name="catname"  id="catname"  readonly>					      	
							</div>
					       	<div class="form-group">
					           	<label class="col-form-label" for="prddesc">Product Description</label>
					           	<input class="form-control" id="prddesc" name="prddesc" type="text" value="${prddesc}" readonly>
					       	</div>
					       	<div class="form-group">
							    <label class="col-form-label" for="disctype">Discount Type</label>
							    <input type="text" class="form-control" required=true name="disctype"  id="disctype"  readonly/>
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="reordrlvl">Restocking Level</label>
							    <input class="form-control" id="reordrlvl" name="reordrlvl" type="text" value="${reordrlvl}" readonly>
							</div>
					   	</div>
					                
					   	<div class="col-sm-4 col-xs-6">
					       	<div class="form-group">
					           	<label class="col-form-label" for=""subcatname"">Sub Category Name</label>					          	
								<input type="text" class="form-control" required=true name="subcatname"  id="subcatname"  readonly/>
					      	</div>
					       	<div class="form-group">
					           	<label class="col-form-label" for="modelno">Model/Brand</label>
					          	<input class="form-control" id="modelno" name="modelno" type="text" value="${modelno}" readonly>
					       	</div>
					       	<div class="form-group">
							    <label class="col-form-label" for="discount">Discount</label>
							    <input class="form-control" id="discount" name="discount" type="text" value="${discount}" readonly>
							</div>
							<div class="form-group">
							    <label class="col-form-label" for="provider">Service Provider / Retailer</label>
							    <input class="form-control" id="provider" name="provider" type="text"  value="${provider}" readonly>
							</div>
					    </div>
					    <input type="hidden" name="type"  id="type" value="modify"/>
						<input type="hidden" name="catid"  id="catid" value="$('#catid')}">
						<input type="hidden" name="subcatid"  id="subcatid" value="$('#subcatid')}">
						<input type="hidden" name="manfid"  id="manfid" value="$('#manfid')}">            
		       	</form>
		       	<form id="no_name" method="post"></form>
		    </div>
	    </div>
    </div>
    
    <div class="content-panel">
    	<div class="container">
    		<div class="row">
	    		<div class="col-sm-4 col-xs-6">
	        		<a href="#" class="btn activate" id="btn-submit" role="button">Submit</a>
	         		<a href="#" class="btn deactivate" id="btn-cancel" role="button">Cancel</a>
	   			</div>
	    	</div>
    	</div>
    </div>		
		
</body>


</html>

