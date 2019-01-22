
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

<script src="${pageContext.request.contextPath}/pagenationjs/jquery-1.12.2.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/dataTables.colVis.js"></script>
<script type="text/javascript">

$(document).ready(function () { 
	$(document).on('click','a',function(event) {
		
		var id=$(this).attr("id");
		//alert(id);
		if(id=="new-profile"){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioNewReg.action";
			$("#form1").submit();
		}else if(id=="exisitng-profile"){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioExtReg.action";
			$("#form1").submit();
		}
		
	});
});

</script>
<style type="text/css">
.btn-exist {
    background-color: #0e60f0;
    background-image: -moz-linear-gradient(top,#2a25e4,#1253f0);
    background-image: -ms-linear-gradient(top,#2a25e4,#1253f0);
    background-image: -webkit-gradient(linear,0 0,0 100%,from(#2a25e4),to(#1253f0));
    background-image: -webkit-linear-gradient(top,#2a25e4,#1253f0);
    background-image: -o-linear-gradient(top,#2a25e4,#1253f0);
    background-image: linear-gradient(top,#2a25e4,#1253f0);
    background-repeat: repeat-x;
    border-color: #51a351 #51a351 #387038;
    border-color: rgba(0,0,0,.1) rgba(0,0,0,.1) rgba(0,0,0,.25);
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
}

</style>
</head>
<body >
	<form name="form1" id="form1" method="post" autocomplete="off">
	
		
		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Bio Activities</a></li>
				</ul>
			</div>
 
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>Bio Registration
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content" id="top-layer-anchor">
				 <div style="text-align: center; text-decoration: none;">
 					<p>
	 					<a style="width:250px;height: 35px !important;padding-top: 30px !important;" href="#" class="btn btn-success" id="new-profile"   title='New Profile Registration' data-toggle='popover'  data-content='New Profile Registration'><i class='icon icon-users icon-white'></i>&nbsp;New Profile Registration</a> &nbsp;
	 					<a style="width:250px;height: 35px !important;padding-top: 30px !important;" href="#" class="btn btn-info" id="exisitng-profile"   title='Existing Profile Bio Registration' data-toggle='popover'  data-content='Existing Profile Bio Registration'><i class='icon icon-users icon-white'></i>&nbsp;Existing Profile Bio Registration</a> &nbsp;
 					</p>
 				 </div>	
			</div>
			</div>
		</div> 
	</div>	
		
			</form>
</body>
</html>
