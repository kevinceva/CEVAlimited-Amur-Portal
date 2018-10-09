
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
	
	/* var catjson = '${catalogRespJSON.CATEGORY_JSON}';
	
	
	
	var inCatid = '${responseJSON.catid}';
	
	
	var arr = jQuery.parseJSON(catjson)
	for (var i = 0; i < arr.length; i++) 
	{
		if (arr[i].categoryId == inCatid)
		{
			$('#catname').val(arr[i].categoryName);
			$('#catname_spn').text(arr[i].categoryName);
			$('#catdesc').val(arr[i].categoryDescription);
			$('#catdesc_spn').text(arr[i].categoryDescription);
			$('#catmkrdt').val(arr[i].createdDate);
			$('#catmkrdt_spn').text(arr[i].createdDate);
			
			breakLoop = true;
		}
	} */ 
	
	
	$('#btn-cancel').on('click',function() { 
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/category.action";
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
				  <li> <a href="clientinfo.action?pid=141">Category Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#"> ${type} SubCategory </a></li>
				</ul>
			</div>  
		 	
		 	<div class="row-fluid sortable">
           
	<div class="box span12">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>SubCategory Details 
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
							 <td width="25%" ><strong><label for="Category Name">Sub Category Name </label></strong></td>
							<td width="25%" >${responseJSON.subcatname}<input type="hidden" class="field" required=true name="subcatname"  id="subcatname" value="${responseJSON.subcatname}" /></td>
							<td width="25%" ><strong></strong></td>
							<td width="25%" ></td>							 
						</tr>
						<tr class="odd">
 							<td  width="25%"><strong><label for="Category Desc"> Sub Category Description </label></strong></td>
 							<td width="25%" >${responseJSON.subcatdesc}<input type="hidden" class="field" required=true name="subcatdesc"  id="subcatdesc"  value="${responseJSON.subcatdesc}" /></td>
 							<td><strong></strong></td>
							<td width="25%" ></td> 
						</tr> 
				</table>
				<input type="hidden" name="type"  id="type" value="Create"/>
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

