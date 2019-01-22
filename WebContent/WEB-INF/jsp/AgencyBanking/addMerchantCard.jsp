<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>BackOffice</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%> 
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>


 
<script type="text/javascript" > 
$(document).ready(function(){
	$('#merchantCategory, #merchantName').select2();

	$.validator.addMethod("regex", function (value, element, regexpr) {
		return regexpr.test(value);
	}, "");
	
});

function redirectAct()
{
	$("#form2")[0].action='<%=request.getContextPath()%>/<%=appName %>/ParameterCreation.action';
	$("#form2").submit();
	return true;
} 

var finaljson; 
var datavalidation = {

	rules: {
		merchantCategory: {
			required: true
		},
		merchantName: {
			required: true
		},
		customerNumber: {
			required: true,
			regex: /^[0-9]+$/
		},
		amountPaid: {
			required: true,
			regex: /^[0-9]+$/
		},
		receiptNumber: {
			required: true,
			regex: /^[0-9]+$/
		},
		datePaid: {
			required: true
		}

	},
	messages: {

		merchantCategory: {
			required: "Please Select Merchant Category."
		},
		merchantName: {
			required: "Please Select Merchant Name."
		},
		customerNumber: {
			required: "Please Enter Customer Phone Number.",
			regex : "Please use numbers only."
		},
		amountPaid: {
			required: "Please Enter Amount Paid.",
			regex : "Please use numbers only."
		},
		receiptNumber: {
			required: "Please Enter the Receipt Number.",
			regex : "Please use numbers only."
		},
		datePaid: {
			required: "Please Select Date Paid."
		}

	}
};
 
var carddata="merchantCategory|text#merchantName|text#customerNumber|text#amountPaid|text#receiptNumber|text#datePaid|text#"; //branchaccno|text#
var carddatajsonObj = [];

function addrow() {

	$("#form1").validate(datavalidation);
	console.log("Addrow >>>> ");

	if ($("#form1").valid()) {

		var myval = buildSingleRequestjson(carddata);
		var finalobj = jQuery.parseJSON(myval);

		var status = validateData(finalobj, carddatajsonObj, 1);

		carddatajsonObj.push(finalobj);
		$("#errormsg").empty();
		makeempty();

		var totval = JSON.stringify(carddatajsonObj);
		console.log("Final Json Object [" + totval + "]");

		buildfeetable(carddatajsonObj);
		$("#submitdata").fadeIn( "slow" );
	}
	else {
		console.log("Data not valid");
	}
}


function validateData(myval, myfinalobj, val) {

	var status = false;
	var merchantCategory = myval.merchantCategory;
	var merchantName = myval.merchantName;
	var customerNumber = myval.customerNumber;
	var amountPaid = myval.amountPaid;
	var receiptNumber = myval.receiptNumber;
	var datePaid = myval.datePaid;

	$.each(myfinalobj, function (index, jsonObject) {
		var tabmerchantCategory = jsonObject.merchantCategory;
		var tabmerchantName = jsonObject.merchantName;
		var tabcustomerNumber = jsonObject.customerNumber;
		var tabamountPaid = jsonObject.amountPaid;
		var tabreceiptNumber = jsonObject.receiptNumber;
		var tabdatePaid = jsonObject.datePaid;


		if ((merchantCategory == tabmerchantCategory)) {
			status = true;
		}
	});
	return status;
}



function makeempty()
{
	document.getElementById("form1").reset();
	$('#merchantCategory, #merchantName').select2("val", "");
} 


function buildfeetable(jsonArray) {
	$("#tbody_data").empty();
	var i = 0;
	var htmlString = "";

	$.each(jsonArray, function (index, jsonObject) {
		var data = JSON.stringify(jsonObject);
		console.log("Data [" + data + "]");

		++i;
		htmlString = htmlString + "<tr class='values' id=" + i + ">";
		htmlString = htmlString + "<td id=sno name=Transaction >" + i + "</td>";
		htmlString = htmlString + "<td id='merchantCategory' name=merchantCategory >" + jsonObject.merchantCategory + "</td>";
		htmlString = htmlString + "<td id='merchantName' name=merchantName >" + jsonObject.merchantName + "</td>";
		htmlString = htmlString + "<td id='customerNumber' name=customerNumber >" + jsonObject.customerNumber + "</td>";
		htmlString = htmlString + "<td id='amountPaid' name=amountPaid >" + jsonObject.amountPaid + "</td>";
		htmlString = htmlString + "<td id='receiptNumber' name=receiptNumber >" + jsonObject.receiptNumber + "</td>";
		htmlString = htmlString + "<td id='datePaid' name=datePaid >" + jsonObject.datePaid + "</td>";

		htmlString = htmlString + "<td><a class='btn btn-warning' href='#' id='modify'   title='Modify' data-rel='tooltip'  onclick='upatedata(" + data + ")' >" +
			"<i class='icon icon-edit icon-white'></i></a> " +
			"<a id='remove' class='btn btn-info'  href='#'   title='Remove' data-rel='tooltip' ><i class='icon-remove'  onclick='deletedata(" + data + ")' ></i></a> </td>";

		htmlString = htmlString + "</tr>";

	});
	console.log("Final HtmlString [" + htmlString + "]");

	if (htmlString == '')
	$("#submitdata").hide();
	$("#tbody_data").append(htmlString);

}

function subitReq() {
	var totval = JSON.stringify(carddatajsonObj);
	var myEscapedJSONString = totval.replace(/\\n/g, " ")
		.replace(/\\'/g, " ")
		.replace(/\\"/g, ' ')
		.replace(/\\&/g, " ")
		.replace(/\\r/g, " ")
		.replace(/\\t/g, " ")
		.replace(/\\b/g, " ")
		.replace(/\\f/g, " ");

	//alert("After Final Json Object [" + totval + "]");
	//alert("myEscapedJSONString [" + myEscapedJSONString + "]");

	totval = myEscapedJSONString;
	$("#finaljsonarray").val(totval);

	$("#form2")[0].action = '<%=request.getContextPath()%>/<%=appName %>/cardCreationConfirm.action';
	$("#form2").submit();
	return true;
}

var dataupdateval;



function upatedata(myval) {
	$("#ADD").hide();
	$("#UPDATE").show();
	
	$('#merchantCategory').val(myval.merchantCategory);
	$('#merchantName').val(myval.merchantName);
	$('#customerNumber').val(myval.customerNumber);
	$('#amountPaid').val(myval.amountPaid);
	$('#receiptNumber').val(myval.receiptNumber);
	$('#datePaid').val(myval.datePaid);

	dataupdateval = myval;
}



function adddeletedata(val)
{
	console.log("Val ["+val+"]");
	fillsingledata(carddata,val);
	dataupdateval=val;
	viewdata(val);
}



function deletedata(val) {

	var finaljsonobj = [];
	swal({
			title: "Do you want to remove this?",
			text: "Press OK to continue.",
			icon: "warning",
			buttons: true,
			dangerMode: true,
		})
		.then((willDelete) => {
			if (willDelete) {

				console.log("Final Value [" + val + "]");
				dataupdateval = val;

				var merchantCategory = dataupdateval.merchantCategory;
				var merchantName = dataupdateval.merchantName;
				var customerNumber = dataupdateval.customerNumber;
				var amountPaid = dataupdateval.amountPaid;
				var receiptNumber = dataupdateval.receiptNumber;
				var datePaid = dataupdateval.datePaid;

				$.each(carddatajsonObj, function (index, jsonObject) {
					var tabmerchantCategory = jsonObject.merchantCategory;
					var tabmerchantName = jsonObject.merchantName;
					var tabcustomerNumber = jsonObject.customerNumber;
					var tabamountPaid = jsonObject.amountPaid;
					var tabreceiptNumber = jsonObject.receiptNumber;
					var tabdatePaid = jsonObject.datePaid;

					//console.log("tabadminType [" + tabadminType + "] tabadminName [" + tabadminName + "]");

					if ((tabmerchantCategory == merchantCategory) && (tabmerchantName == merchantName) && (tabcustomerNumber == customerNumber) && (tabamountPaid == amountPaid) && (tabreceiptNumber == receiptNumber) && (tabdatePaid == datePaid)) {

					}
					else {

						finaljsonobj.push(jsonObject);
					}
				});

				carddatajsonObj = [];
				carddatajsonObj = finaljsonobj;
				buildfeetable(carddatajsonObj);

				swal("Record has been removed.", {
					icon: "success",
				});

			}
			else {
				swal("Record not removed.");
			}
		});
	makeempty();
}





function updaterow()
{
	$("#form1").validate(datavalidation);
	if($("#form1").valid()) { 
	checkEqual('U');
	makeempty();
	
	$("#ADD").show();
	$("#UPDATE").hide();
	}
	
}




function checkEqual(val) {
	var finaljsonobj = [];

	var myval = buildSingleRequestjson(carddata);
	var newupdateddataobj = jQuery.parseJSON(myval);

	var adminType = dataupdateval.adminType;
	var adminName = dataupdateval.adminName;

	$.each(carddatajsonObj, function (index, jsonObject) {
		var tabadminType = jsonObject.adminType;
		var tabadminName = jsonObject.adminName;

		if ((adminType == tabadminType) && (adminName == tabadminName)) {
			if (val == 'U') {
				finaljsonobj.push(newupdateddataobj);
			}
		}
		else {
			finaljsonobj.push(jsonObject);
		}
	});

	carddatajsonObj = [];
	carddatajsonObj = finaljsonobj;
	buildfeetable(carddatajsonObj);
}



function viewdata(val) {
	console.log("Val [" + val + "]");
	var viewdata = "branchcode|text#branchname|text#branchaddress|text#branchaccno|text#";

	fillsingledata(viewdata, val);
	$("#Remove").hide();
}	
</script>
<s:set value="responseJSON" var="respData" />
</head>

<body>
	<div class="content-body">
		<div class="page-header">
			<div>
				<label>Merchant Payment Cards</label>            
			</div>
		</div>
		<ol class="breadcrumb">
			<li class="breadcrumb-item">
				<a href="home.action">Dashboard</a>
			</li>
			<li class="breadcrumb-item active">
				<a href="addMerchantCard.action?pid=<%=session.getAttribute("session_refno").toString() %>">Merchant Payment Cards</a> 
			</li>
		</ol>
		<div class="content-panel" id="user-details">
			<div class="panel-head">
				<label></i>Add New Payment Cards</label>
			</div>
			<form name="form1" id="form2" method="post" action="">
				 <div id="errormsg" class="errores"></div>		 
				<input type="hidden" id="finaljsonarray" name="finaljsonarray" >
			</form>
			<form name="form1" id="form1" class="amur-form" enctype="multipart/form-data"  method="post">
				<div id="amur-form-container">
					<table class="table table-bordered" style="width:100%">
						<tr class="form-group">
							<td>
								<label>Merchant Category<span>*</span> : </label>
							</td>
							<td>
								<select class="form-control" id="merchantCategory" name="merchantCategory" onChange="" class="field">
									<option value="" disabled selected>Select Merchant Category</option>
									<option value="testing">Testing</option>
								</select>
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Merchant Name<span>*</span> : </label>
							</td>
							<td>
								<select class="form-control" id="merchantName" name="merchantName" class="field">
									<option value="" disabled selected>Select Merchant Name</option>
									<option value="testing">Testing</option>
								</select>
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Customer Phone Number<span>*</span> : </label>
							</td>
							<td>
								<input type="text" id="customerNumber" name="customerNumber" placeholder="Customer Number" class="field"/>	
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Amount Paid (KES)<span>*</span> : </label>
							</td>
							<td>
								<input type="text" id="amountPaid" name="amountPaid" placeholder="Amount Paid (KES)" class="field"/>
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Receipt Number<span>*</span> : </label>
							</td>
							<td>
								<input type="text" id="receiptNumber" name="receiptNumber" placeholder="Receipt Number" class="field"/>
							</td>
						</tr>
						<tr class="form-group">
							<td>
								<label>Date<span>*</span> : </label>
							</td>
							<td>
								<input type="date" id="datePaid" name="datePaid" placeholder="Date"/>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		
		<div class="content-panel form-actions">
			<input type="button" id="ADD" class="btn btn-success" onclick="addrow();" value="Add Card Info" /> &nbsp;&nbsp;
			<input type="button" id="UPDATE" class="btn btn-success" onclick="updaterow();" value="Update Card Info" style="display:none"/> 
		</div>
		
		<div class="content-panel">
			<div class="table-container">
				<div class="panel-head">
					<label></i>New Payment Cards</label>
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
								<th>Action</th>
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
		
		<div class="content-panel form-actions" id="submitdata" style="display:none"> 
			<input type="button" id="non-printable" class="btn btn-success" onclick="subitReq();" value="Submit Card Data" />
		</div>
		
	</div>

</body>
</html>
