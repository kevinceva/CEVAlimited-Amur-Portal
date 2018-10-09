
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
	
	var disctypearr = jQuery.parseJSON(disctypejson)
	for (var t = 0; t < disctypearr.length; t++) 
	{
		if (disctypearr[t].disctype == inDisctype)
		{
			$('#disctype').val(disctypearr[t].typedesc);
			$('#disctype_spn').text(disctypearr[t].typedesc);
		}
		
	}
	
	var remarks = '${responseJSON.remarks}';
	console.log("remarks["+remarks+"]");

	$("#respmsg").text(remarks);
	

	$('#btn-submit').on('click',function() { 
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/productModifyAck.action";
		$("#form1").submit();	 
	});	
	
	$('#btn-cancel').on('click',function() { 
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/products.action";
		$("#form1").submit();	 
	});
	
		
});
	//--> 
</SCRIPT>
    
  
		
</head>

<body>
	<form name="form1" id="form1" method="post">
	<!-- topbar ends -->
	 <div id="content" class="span10"> 
			 
		    <div> 
				<ul class="breadcrumb">
				 <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="clientinfo.action?pid=141">Product Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#"> ${type} Client </a></li>
				</ul>
			</div>  
		 	
		 	<div class="row-fluid sortable">
		 	
		 	          <div class="box span12">
                            
		<div class="box-header well" data-original-title>
		<span id="respmsg"></span>
		</div>
			
		</div>
           
	<div class="box span12">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Product Details 
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>
						
		<div class="box-content">
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
						<tr class="odd" > 
							<td width="25%" ><strong><label for="Product Name"> Product Name</label></strong></td>
							<td width="25%" >${prdname} <input type="hidden" class="field" required=true  maxlength="50" name="prdname"  id="prdname" value="${prdname}" /> </td>
							 <td width="25%" ><strong><label for="Category Name"> Category Name</label></strong></td>
							<td width="25%" >${catname}<input type="hidden" class="field" required=true name="catname"  id="catname" value="${catname}" /></td> 
						</tr>
						<tr class="odd">
 							<td  width="25%"><strong><label for="Sub Category Name"> Sub Category Name</label></strong></td>
 							<td width="25%" >${subcatname}<input type="hidden" class="field" required=true name="subcatname"  id="subcatname"  value="${subcatname}"/></td>
 							<td><strong><label for="Manufacturer Name"> Manufacturer Name</label></strong></td>
							<td width="25%" >${manfname}<input type="hidden" class="field" required=true name="manfname"  id="manfname" value="${manfname}"  /></td> 
						</tr> 
						<tr class="odd">
 							<td><strong><label for="Description"> Description</label></strong></td>
 							<td width="25%" > ${prddesc}<input type="hidden" class="field" required=true name="prddesc"  id="prddesc" value="${prddesc}" /> </td>
 					        <td><strong><label for="Picture"> Picture</label></strong></td>
 					        <td width="25%" > ${pic1} <input type="hidden" class="field" required=true name="pic1"  id="pic1" value="${pic1}" /> </td>
				       </tr> 
						<tr class="odd">
							<td><strong><label for="Model Brand"> Model/Brand</label></strong></td>
							<td width="25%" >${modelno}<input type="hidden" class="field" required=true name="modelno"  id="modelno" value="${modelno}" /> </td> 
							<td><strong><label for="Price">Price</label></strong></td>
							<td width="25%" >${pcode}<input type="hidden" class="field" required=true name="pcode"  id="pcode" value="${pcode}" /> </td>
						 </tr>
						<tr class="odd">
							<td><strong><label for="DiscType">Discount Type</label></strong></td>
							<td width="25%" ><span id="disctype_spn"></span><input type="hidden" class="field" required=true name="disctype"  id="disctype" " /> </td> 
							<td><strong><label for="Discount"> Discount</label></strong></td>
							<td width="25%" >${discount}<input type="hidden" class="field" required=true name="discount"  id="discount" value="${discount}" /> </td> 
						</tr>  
						<tr class="odd">
							<td><strong><label for="Quantity">Quantity</label></strong></td>
							<td width="25%" >${quantity}<input type="hidden" class="field" required=true name="quantity"  id="quantity" value="${quantity}" /> </td> 
							<td><strong><label for="Reorder">Re-Order Level</label></strong></td>
							<td width="25%" > ${reordrlvl}<input type="hidden" class="field" required=true name="reordrlvl"  id="reordrlvl" value="${reordrlvl}" /> </td>
						</tr> 
						<tr>
							<td><strong><label for="Provider">Service Provider / Retailer</label></strong></td>
							<td width="25%" > ${provider} <input type="hidden" class="field" required=true name="provider"  id="provider" value="${provider}" /> </td>
							<td><strong></strong></td>
							<td></td>
						</tr> 						
				</table>
				<input type="hidden" name="type"  id="type" value="modify"/>
				<input type="hidden" name="prdid"  id="prdid" value="$('#prdid')}">
			</fieldset> 
			
              
		</div>
		</div>
		</div> 
		
		
		<div class="form-actions" >
		   <input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Next"  ></input>
		</div>  

              						 
	</div><!--/#content.span10-->
		  
 </form>
</body>

<!-- <SCRIPT type="text/javascript"> 



$(document).ready(function(){
	
	var pagetype='${responseJSON.type}';
	alert(pagetype);
	if (pagetype=='View'){
		
		$('#prdname').attr('disabled','disabled');
		$('#catname').attr('disabled','disabled');
		$('#subcatname').attr('disabled','disabled');
		$('#manfname').attr('disabled','disabled');
		$('#prddesc').attr('disabled','disabled');
		$('#pic1').attr('disabled','disabled');
		$('#pcode').attr('disabled','disabled');
		$('#discount').attr('disabled','disabled');
		$('#quantity').attr('disabled','disabled');
		$('#status').attr('disabled','disabled');
		$('#provider').attr('disabled','disabled');
		$('#modelno').attr('disabled','disabled');
		$('#type').val('View');
		$('#btn-submit').hide();
		
	}else
	{

		$('#type').val('Modify');
		$('#btn-submit').show();
	}	
	
		
		
	
	
});
</script> -->

</html>

