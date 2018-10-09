
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 <%@taglib uri="/struts-tags" prefix="s"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
$(function() {

	var status = "${responseJSON.status}";
	var header = "${responseJSON.headerData}";

	  alert();

	if(status == 'serv') {
		$('.merchant').text('Service');
 	} else if(status == 'subserv') {
		$('.merchant').text('Sub Service');
 	} else if(status == 'proc') {
		$('.merchant').text('Processing Code');
 	}else if(status == 'BinAuth') {
		$('.merchant').text('Bin Authrisation');
 	}
 	else {
		$('.merchant').text('Fee ');
 	}

	var val = 1;
	var rowindex = 1;
	var bankfinalData="${responseJSON.bankMultiData}";
	var bankfinalDatarows=bankfinalData.split("#");


	var headerList = header.split("#");
	var headerData = "";

	if( header.indexOf("#") != -1 ) {
		$('#header_data').append("<tr>");

		$(headerList).each(function(index,val){
 			if(index == 0) {
				$('#header_data').append("<th>Sno</th>");
			}
			$('#header_data').append("<th>"+val+"</th>");
		});

		$('#header_data').append("</tr>");
 	}

	$(bankfinalDatarows).each(function(index,value) {
		if(val % 2 == 0 ) {
			addclass = "even";
			val++;
		} else {
			addclass = "odd";
			val++;
		}

 		var eachfieldArr = value.split(",");
		var status = "";
		$("#tbody_data").append("<tr class="+addclass+" index='"+rowindex+"'> ");
			$("#tbody_data").append("<td>"+rowindex+"</td>");

			$(eachfieldArr).each(function(index1,dataval){
				$("#tbody_data").append("<td>"+dataval+"</td>");
			});

			//$("#tbody_data").append("<td class='hidden-phone'><a href='#' class='label label-success' index='"+rowindex+"'> Active </a> </td>");
			$("#tbody_data").append("</tr>");

		rowindex++;
	});

	$('#btn-cancel').live('click', function () {
		var url="${pageContext.request.contextPath}/<%=appName %>/feeAuthorizationBack.action";
		$("#form1")[0].action=url;
		$("#form1").submit();
	});

	$('#btn-submit').live('click', function () {
		var url="${pageContext.request.contextPath}/<%=appName %>/feeAuthorizationAck.action";
		$("#form1")[0].action=url;
		$("#form1").submit();
	});
});
</script>

</head>
<body>
<form name="form1" id="form1" method="post">
	<div id="content" class="span10">
		<div>
			<ul class="breadcrumb">
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
			  <li> <a href="serviceMgmtAct.action?pid=10">Fee Management</a> <span class="divider"> &gt;&gt; </span></li>
			  <li> <a href="#"><span id="header-data" class="merchant"> </span> Authorization Confirmation</a>  </li>
			</ul>
		</div>
	<table height="3">
		 <tr>
			<td colspan="3">
				<div class="messages" id="messages"><s:actionmessage /></div>
				<div class="errors" id="errors"><s:actionerror /></div>
			</td>
		</tr>
	 </table>
	  <div class="row-fluid sortable">
		<div class="box span12">
				<div class="box-header well" data-original-title>
						<i class="icon-edit"></i><span id="header-data1" class="merchant"> </span> Authorization Confirmation
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					</div>
				</div>
				<div class="box-content">
					<fieldset>
						<table width="100%" class="table table-striped table-bordered bootstrap-datatable "
									id="mytable">
						  <thead id="header_data">
						  </thead>
						  <tbody id="tbody_data">
						  </tbody>
						</table>
					</fieldset>
				</div>
			</div>
		</div>
		<div class="form-actions">
			  <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Confirm" width="100" ></input>
				&nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Back" width="100" ></input>
				<input name="selectedUserText" type="hidden" id="selectedUserText" value="${responseJSON.selectedUserText}" />
				<input name="status" type="hidden" id="status" value="${responseJSON.status}" />
				<input name="bankMultiData" type="hidden" id="bankMultiData" value="${responseJSON.bankMultiData}" />
				<input name="agentMultiData" type="hidden" id="agentMultiData" value="<s:property value="responseJSON['agentMultiData']" />" />
				<input name="headerData" type="hidden" id="headerData" value="${responseJSON.headerData}" />
		</div>
	</div>
</form>
</body>
</html>