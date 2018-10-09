
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
function getGenerateMerchantScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/generateMerchantAct.action';
	$("#form1").submit();
	return true;
}
 
</script>
	 
		
</head>

<body>
	<form name="form1" id="form1" method="post" action="">
	<!-- topbar ends --> 
	<div id="content" class="span10">  
		<div>
			<ul class="breadcrumb">
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
			  <li> <a href="#">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
			  <li><a href="#">Terminate Store</a></li>
			</ul>
		</div>
		<div class="row-fluid sortable"><!--/span--> 
			<div class="box span12">
				<div align="center">
					${storeId}  Store Terminated Successfully.
				</div>
			 
					
			</div> 
		</div>
		<div align="center">
			<a  class="btn btn-danger" href="#" onClick="getGenerateMerchantScreen()">Next</a> &nbsp;&nbsp;
		</div> 
	</div> 
</form>
</body>
</html>
