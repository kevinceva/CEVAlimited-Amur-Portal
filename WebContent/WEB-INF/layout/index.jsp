<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%
        String ctxstr = request.getContextPath();
%>
<title>Amur | LOGIN</title>

<script type="text/javascript">
function redirectLogin() {
        document.form1.action = "<%=ctxstr%>/cevaappl.action";
        document.form1.submit();
}
</script>
</head>
<body onload="redirectLogin()">
        <form class="form-horizontal" name="form1" id="form1" action=""
                method="post">
                <div class="container-fluid">
                        <div class="row-fluid">
                                <div class="span12 center login-header">
                                        <h2>Amur</h2>
                                </div>
                                <!--/span-->
                        </div>
                        <!--/row-->

                        <div class="row-fluid">
                                <div class="well span5 center login-box">
                                        <div class="alert alert-info"></div>

                                        <div class="input-prepend" title="Username">
                                                <span class="add-on"><i class="icon-user"></i></span>&nbsp;&nbsp;&nbsp;
                                                <img src="<%=ctxstr%>/images/ajax-loaders/ajax-loader-4.gif" />
                                                &nbsp;&nbsp;&nbsp;Redirecting to login page... Please Wait..
                                        </div>
                                </div>
                                <!--/span-->
                        </div>
                        <!--/row-->
                </div>
                <!--/fluid-row-->
        </form>
</body>
</html>