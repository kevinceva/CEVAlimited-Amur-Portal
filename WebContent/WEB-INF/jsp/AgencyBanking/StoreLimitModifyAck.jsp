
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 
<script type="text/javascript" >

function getGenerateLimitScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/limitManagementAct.action';
	$("#form1").submit();
	return true;
}
	
</script>
	 
</head>

<body>
	<form name="form1" id="form1" method="post" action="">
	
		 
			<div id="content" class="span10">  
			  <div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Float Management</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Limit Management</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Store Limit Modify Acknowledge</a></li>
				</ul>
			</div> 
								  
			<div class="row-fluid sortable"><!--/span--> 
				<div class="box span12">
					  <div class="box-header well" data-original-title>Store Limit Details
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round" data-rel="tooltip"><i class="icon-chevron-up"></i></a>
							<a href="#" class="btn btn-close btn-round" data-rel="tooltip"><i class="icon-remove"></i></a> 
						</div>
					</div>
                      
					<div class="box-content" id="primaryDetails"> 
						<div align="center">
							Limit Amount of kshs ${responseJSON.storeLimit} /= successfully assigned to Store ${responseJSON.storeId} of Merchant ${responseJSON.merchantID} .
						</div> 
					</div> 
				</div>
			</div>
			<div class="form-actions">
				<a  class="btn btn-danger" href="#" onClick="getGenerateLimitScreen()">Next</a> &nbsp;&nbsp;
			</div>  
	</div>
		 
</form>
</body>
</html>
