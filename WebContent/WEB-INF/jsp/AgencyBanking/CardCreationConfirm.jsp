<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>IMPERIAL</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%> 
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 
<script type="text/javascript"> 

function redirectAct() {
	$("#form1")[0].action = '<%=request.getContextPath()%>/<%=appName %>/addMerchantCard.action';
	$("#form1").submit();
	return true;

}

$(function () {

	var jsoarray = '${responseJSON.FINAL_JSON}';
	console.log("jsoarray >>> [" + jsoarray + "]");
	var finaldata = jQuery.parseJSON(jsoarray);
	buildbranchtable(finaldata);

});


function buildbranchtable(jsonArray) {

	$("#tbody_data").empty();
	var i = 0;
	var htmlString = "";

	$.each(jsonArray, function (index, jsonObject) {
		++i;

		htmlString = htmlString + "<tr class='values' id=" + i + ">";
		htmlString = htmlString + "<td id=sno name=Transaction >" + i + "</td>";
		htmlString = htmlString + "<td id='merchantCategory' name=merchantCategory >" + jsonObject.merchantCategory + "</td>";
		htmlString = htmlString + "<td id='merchantName' name=merchantName >" + jsonObject.merchantName + "</td>";
		htmlString = htmlString + "<td id='customerNumber' name=customerNumber >" + jsonObject.customerNumber + "</td>";
		htmlString = htmlString + "<td id='amountPaid' name=amountPaid >" + jsonObject.amountPaid + "</td>";
		htmlString = htmlString + "<td id='receiptNumber' name=receiptNumber >" + jsonObject.receiptNumber + "</td>";
		htmlString = htmlString + "<td id='datePaid' name=datePaid >" + jsonObject.datePaid + "</td>";
		htmlString = htmlString + "</tr>";
	});

	console.log("Final HtmlString [" + htmlString + "]");
	$("#tbody_data").append(htmlString);
}


function confirmAct() {
	var submitPath = "<%=request.getContextPath()%>/<%=appName%>/cardCreationAck.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName%>/addMerchantCard.action";

	swal({
			title: "Confirm merchant cards?",
			text: "Press Ok to continue.",
			icon: "warning",
			buttons: true,
			dangerMode: true,
		})
		.then((willDelete) => {
			if (willDelete) {
				$('#form1').submit(function () {
					$.post(submitPath, $(this).serialize(), function (json) {
						if (json.responseJSON.remarks == "SUCCESS") {
							swal({
								title: "Success",
								text: "Merchants' Cards successfully processed.",
								icon: "success",
								button: "Continue",
							}).then(function (result) {
								window.location.href = backPath;
							});
						}
						else if (json.responseJSON.remarks == "FAILURE") {
							swal({
								title: "Sorry!",
								text: "Merchant card processing failed. Please try again later.",
								icon: "error",
								button: "Continue",
							}).then(function (result) {
								window.location.href = backPath;
							});
						}

					}, 'json');
					return false;
				});
				$("#form1").submit();
			}
			else {
				swal("Request Cancelled.");
			}
		});

	return true;

}
</script>
</head>

<body>
	<div class="content-body">
		<div class="page-header">
			<div>
				<label>Merchant Payment Cards Confirmation</label>            
			</div>
		</div>
		<ol class="breadcrumb">
			<li class="breadcrumb-item">
				<a href="home.action?pid=<%=session.getAttribute("session_refno").toString() %>">Dashboard</a>
			</li>
			<li class="breadcrumb-item">
				<a href="addMerchantCard.action?pid=<%=session.getAttribute("session_refno").toString() %>">Merchant Payment Cards</a>
			</li>
			<li class="breadcrumb-item active">
				<a href="#">Merchant Payment Cards Confirmation</a> 
			</li>
		</ol>
		<form name="form1" id="form1" method="post" action="">
			<div class="content-panel" id="user-details">				
				<div class="table-container">
					<div class="panel-head">
						<label></i>Confirm New Payment Cards</label>
					</div>
					<div class="table-content">
						<table class="table table-striped table-bordered bootstrap-datatable" id="acqdetails" >
							<thead>
								<tr>
									<th>Sno</th>
									<th>Merchant Category</th>
									<th>Merchant Name</th>
									<th>Mobile Number</th>
									<th>Amount</th>
									<th>Receipt Number</th>
									<th>Date</th>
								</tr>
							</thead>
							<tbody id="tbody_data">
								<tr>
									<td colspan="8">
										This table is empty.
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="content-panel form-actions" id="submitdata">
				<input type="hidden" id="finaljsonarray" name="finaljsonarray" value='${responseJSON.FINAL_JSON}'>
				<input type="button" id="non-printable" class="btn btn-secondary" onclick="redirectAct();" value="Go Back" />
				<input type="button" id="non-printable" class="btn btn-success" onclick="confirmAct();" value="Confirm Cards" />
			</div>			
		</form>
	</div>
</body>
</html>
