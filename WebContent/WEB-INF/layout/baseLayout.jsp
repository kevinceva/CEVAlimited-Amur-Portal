<html>
<head>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@page import="com.ceva.base.common.utils.StringUtil"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<link
	href="${pageContext.request.contextPath}/vendor/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" />
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.5.0/css/bootstrap-datepicker.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/vendor/datatables/datatables.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/vendor/font-awesome/css/font-awesome.min.css">
<!-- <link
	href="https://fonts.googleapis.com/css?family=Saira+Condensed:400,700"
	rel="stylesheet"> -->
<link
	href="https://fonts.googleapis.com/css?family=Raleway:300,400,500,600"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/sb-admin.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap-duallistbox.css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/css/select2.min.css" rel="stylesheet" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/my_style.css">


<script
	src="https://cdnjs.cloudflare.com/ajax/libs/modernizr/2.8.3/modernizr.min.js"
	type="text/javascript"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/jquery.validation/1.15.0/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/vendor/datatables/datatables.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/printThis.js"></script>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/jquery-ui-1.11.2.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/jquery-ui-1.10.2.css">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap3-dialog/1.34.7/css/bootstrap-dialog.min.css" />
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap3-dialog/1.34.7/js/bootstrap-dialog.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.5.0/js/bootstrap-datepicker.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/js/select2.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.min.js"></script>

<title><tiles:insertAttribute name="title" ignore="true" /></title>


<script type="text/javascript">
	/*function preventBack() {
		window.history.forward();
	}
	setTimeout("preventBack()", 0);
	window.onunload = function() {
		null;
	};*/
</script>

<script type="text/javascript">
	$(document).ready(function() {
		var bodyEl = $('body');

		$('.menu-button').on('click', function(e) {
			bodyEl.toggleClass('active-nav');
			e.preventDefault();
		});
	});
</script>
<style type="text/css">
.messages {
	font-weight: bold;
	color: green;
	padding: 2px 8px;
	margin-top: 2px;
}

.errors {
	font-weight: bold;
	color: red;
	padding: 2px 8px;
	margin-top: 2px;
}

label.error {
	font-weight: bold;
	color: red;
	padding: 2px 8px;
	margin-top: 2px;
}

.errmsg {
	color: red;
}
</style>

</head>


<%
	String userid = (String) session.getAttribute(CevaCommonConstants.MAKER_ID);
	if (StringUtil.isNullOrEmpty(userid)) {
%>
<%
	response.sendRedirect("logout.action");
%>
<%
	} else {
%>

<body>
	<div class="main-body">
		<div class="container-fluid">
			<div class="row">
				<div class="side-bar">
					<div class="side-bar-top">
						<tiles:insertAttribute name="header" />
					</div>
					<div class="side-bar-bottom">
						<tiles:insertAttribute name="menu" />
					</div>
				</div>
				<div class="body-container">
					<div class="body-content">
						<tiles:insertAttribute name="body" />
					</div>
					<div class="footer-container">
						<tiles:insertAttribute name="footer" />
					</div>
				</div>
			</div>
		</div>
	</div>
</body>


<%
	}
%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.min.js">
	
</script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
<script type="text/javascript"
	src='${pageContext.request.contextPath}/js/fnFilterAll.min.js'></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.cookie.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.chosen.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/bootstrap-tooltip.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/bootstrap-popover.min.js"></script>
<!--script type="text/javascript"
	src="${pageContext.request.contextPath}/js/agency-default.min.js"></script-->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/modernizr.custom.51611.min.js"></script>
<!--script type="text/javascript"
	src="${pageContext.request.contextPath}/js/css3-mediaqueries.min.js"></script-->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/datepicker.js"></script>
<!-- script
	src="https://cloud.tinymce.com/stable/tinymce.min.js?apiKey=f8lsuhgnieoav8gllcus27lh5lafs4hhpopktw7aglahf0eb"></script-->
<!--script type="text/javascript"
	src="${pageContext.request.contextPath}/js/richtext.js"></script-->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/sweetalert.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.bootstrap-duallistbox.js"></script>
<script type="text/javascript">
	$('.startDate').datepicker({
		format : 'dd-mm-yyyy'
	});

	$('.endDate').datepicker({
		format : 'dd-mm-yyyy'
	});
</script>
</html>

