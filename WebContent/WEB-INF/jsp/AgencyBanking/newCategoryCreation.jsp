
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
<%@taglib uri="/struts-tags" prefix="s"%>   
 
<!-- Below is ZTree Structure --> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/treenode/css/demo.min.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/treenode/css/zTreeStyle/zTreeStyle.min.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/css/treenode/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/css/treenode/jquery.ztree.excheck-3.5.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/css/treenode/jquery.ztree.exhide-3.5.min.js"></script>

<style type="text/css">
label.error, .errors {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
} 

input#groupID{text-transform:uppercase};
</style>

<SCRIPT type="text/javascript">
var checBoxCnt = 0;


//<!--
var setting = {
	check: {
		enable: true
	},
	data: {
		key: {
			title: "title"
		},
		simpleData: {
			enable: true
		}
	},
	callback: {
		onCheck: onCheck
	}
}; 
//var zNodes =  ${responseJSON.user_rights};


$.validator.addMethod("regex", function(value, element, regexpr) {          
	 return regexpr.test(value);
  }, "");  

 
var intermediaryRules = {
	rules : {
		catName : { required : true, regex : /^[A-Z0-9]+$/,  minlength: 5},
		catDesc : { required : true } 
	},		
	messages : {
		catName : { 
					required : "Please enter Category Name",
					regex : "Category Name, should not contain any special characters.",
 					minlength : "Please enter atleast 5 characters for Category Name."
				  }, 
				  catDesc : { 
						required : "Please enter Category Description."
					}
	}
};


$(document).ready(function() {

	$('#btn-submit').live('click',function() { 
		
		$("#form1").validate(intermediaryRules);

		if($("#form1").valid()) { 			
				var url="${pageContext.request.contextPath}/<%=appName %>/confirmCategoryDetails.action";
				$("#form1")[0].action=url;
				$("#form1").submit(); 	
			
		} else {
			return false;
		}	 
	});
	
	
	$('#btn-cancel').live('click',function() {  
		$("#form1")[0].action="${pageContext.request.contextPath}/<%=appName %>/categoryManagement.action?pid=43";
		$("#form1").submit();	
	});  
});
//--> 
</SCRIPT>  
</head> 
<body>
<form name="form1" id="form1" method="post"> 
	<div id="content" class="span10">  
            <!-- content starts -->
			<div> 
				<ul class="breadcrumb">
				  <li><a href="#">New Category Creation</a></li>
				</ul>
			</div>  
		<div class="row-fluid sortable"> 
			<div class="box span12"> 
				<div class="box-header well" data-original-title>
						<i class="icon-edit"></i>Category Details
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					</div>
				</div>
						
				<div class="box-content">
					<fieldset> 
						<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable "> 
							 <tr>
								<td width="15%"><strong><label for="Group Id">Category Name<font color="red">*</font></label></strong></td>
								<td width="35%"><input name="catName" id="catName" class="field"  required="true"  type="text"  maxlength="20" /></td>
								<td width="25%"><strong><label for="Category Description">Category Description<font color="red">*</font></label></strong></td>
								<td width="25%"><input name="catDesc" type="text" class="field"  required="true" id="catDesc"   /></td>	
							</tr>
						</table>  
					</fieldset>   
				</div>
			</div>
		</div>
					
		
	   <div class="form-actions">
			<input type="button" name="btn-submit" class="btn btn-success" id="btn-submit" value="Submit" />
			<input type="button" name="btn-cancel" class="btn btn-primary" id="btn-cancel" value="Cancel" /> 
			<input type="hidden" name="catName"  id="catName" value="" />
			<input type="hidden" name="catDesc"  id="catDesc" value="" />
	  </div>
	</div> 
</form> 
</body>
</html>

