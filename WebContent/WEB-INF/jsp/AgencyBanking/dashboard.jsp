<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%String ctxstr = request.getContextPath();%>
<%String appName = session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString();%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/my_style.css">

<script type="text/javascript">
$(document).ready(function(){
	fetchOrders();
});

var validationRules = {
		rules : {
			riderid : {required : true}
		}, 
		messages : {
			required : "Please choose a rider."
		}
}


//Select text from riders dropdown
function getSelectedText(elementId) {
    var elt = document.getElementById(elementId);
    return elt.options[elt.selectedIndex].text;
}

$(document).on('change','#rider',function(){	
	var mytext = document.getElementById('rider').options[document.getElementById('rider').selectedIndex].text;
	document.getElementById("rider_name").innerHTML = mytext;
});

$(document).ready(function(){	
	var submitPath = "<%=request.getContextPath()%>/<%=appName%>/assignOrder.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName%>/dashboard.action";
	
	$('#assign-order').on('click',function() {
		
		$('#assign-rider-form').validate(validationRules);
		if($('#assign-rider-form').valid()){
			var rider_number = document.getElementById('rider').value;
			var order_number = document.getElementById('assign_order_Id').value;
			swal({
	            title: "Assign rider this order?",
	            text: "Press Ok to continue.", 
	            icon: "warning",
	            buttons: true,
	            dangerMode: true,
	        })
	        .then((willDelete) => {
	          if (willDelete) {
	        	  $('#assign-rider-form').submit(function(){
					    $.post(submitPath, $(this).serialize(), function(json) {		      
					      if(json.responseJSON.remarks == "SUCCESS"){
					    	  swal({
					    		  title: "Success",
					    		  text: "Order Assigned successfully.",
					    		  icon: "success",
					    		  button: "Continue",
					    		}).then(function(result){				    					    			
					    			window.location.href = backPath;
					            });
					      }else if(json.responseJSON.remarks == "FAILURE") {
					    	  swal({
					    		  title: "Sorry!",
					    		  text: "Order Assignment failed. Please try again later.",
					    		  icon: "error",
					    		  button: "Continue",
					    		}).then(function(result){
					    			window.location.href = backPath;
					            });
					      }
					      
					    }, 'json');
					    return false;
					  });
					 $("#assign-rider-form").submit();
	          } else {
	            swal("Request Cancelled.");
	          }
	        });
		} 	
	}); 
});	

$(document).ready(function(){	
	var submitPath = "<%=request.getContextPath()%>/<%=appName%>/cancelOrder.action";
	var backPath = "<%=request.getContextPath()%>/<%=appName%>/dashboard.action";
	
	$('#cancel-order').on('click',function() {
		
		//$('#assign-rider-form').validate(validationRules);
		//if($('#assign-rider-form').valid()){
			//var rider_number = document.getElementById('rider').value;
			var order_number = document.getElementById('assign_order_Id').value;
			//alert(order_number);
			swal({
	            title: "Do you want to cancel this order?",
	            text: "Press Ok to continue.", 
	            icon: "warning",
	            buttons: true,
	            dangerMode: true,
	        })
	        .then((willDelete) => {
	          if (willDelete) {
	        	  $('#assign-rider-form').submit(function(){
					    $.post(submitPath, $(this).serialize(), function(json) {		      
					      if(json.responseJSON.remarks == "SUCCESS"){
					    	  swal({
					    		  title: "Success",
					    		  text: "Order has been cancelled.",
					    		  icon: "success",
					    		  button: "Continue",
					    		}).then(function(result){				    					    			
					    			window.location.href = backPath;
					            });
					      }else if(json.responseJSON.remarks == "FAILURE") {
					    	  swal({
					    		  title: "Sorry!",
					    		  text: "Order cannot be cancelled. Please try again later.",
					    		  icon: "error",
					    		  button: "Continue",
					    		}).then(function(result){
					    			window.location.href = backPath;
					            });
					      }
					      
					    }, 'json');
					    return false;
					  });
					 $("#assign-rider-form").submit();
	          } else {
	            swal("Request Cancelled.");
	          }
	        });
		//} 	
	}); 
});	


function fetchOrders(){
	
	$.getJSON("dashboardData.action", function(data){
		
		var json = data.responseJSON.NEW_ORDERS;
		//var json = jQuery.parseJSON(orderData);
	    //var json = JSON.parse(orderData);
	    
	    console.log("json[  "+json+"  ]");
	    var val = 1;
	    var rowindex = 0;
	    var colindex = 1;
	    var addclass = "";
	    
	    
	    $.each(json, function(i, v) {
	        if(val % 2 == 0 ) {
	            addclass = "even";
	            val++;
	        }
	        else {
	            addclass = "odd";
	            val++;
	        }  
	        	        
	        var rowCount = $('#newOrderTbody > tr').length;
	           
	        
	        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
	        "<td >"+colindex+"</td>"+
	        "<td><a href='#' id='SEARCH_NO' value='prdid@"+v.ORDER_ID+"' aria-controls='DataTables_Table_0'>"+v.ORDER_ID+"</a> </td>"+ 
	        "<td style='display:none'>"+v.ORDER_ID+" </td>"+ 
	        "<td>"+v.TXN_AMT+"</span> </td>"+
	        "<td>"+v.TXN_DATE+"</span> </td>"+
	        "<td>"+v.SHIPPING_ADDRESS+"</span> </td></tr>";
			   
	            $("#newOrderTbody").append(appendTxt);  
	            rowindex = ++rowindex;
	            colindex = ++colindex;
	    });
	    
	    $('#newOrderTable').DataTable({
	    	"pageLength": 5
	    });

	});

};


$(function(){		
		$('#print-order').on('click',function(){
			$('#assign-rider-form').validate(validationRules);
			if($('#assign-rider-form').valid()){
				
				swal("Please wait...", "Receipt download will start shortly.");
				
				var queryString = "orderId="+$('#assign_order_Id').val();
				queryString += "&riderId="+$('#rider').val();
				var submitPath = "<%=request.getContextPath()%>/<%=appName%>/printOrderReceipt.action";
				$.post(submitPath, queryString, function(data){
					console.log("Receipt printed");
					var reportName = null;
					var response = data.responseJSON.reportName;
					if(response != null){
						console.log(response);
						window.location.href = "../ReceiptExporterServlet?reportName="+response;
					}else{
						console.log("no response");
					}
				});
			}
			
		})
})

function isLoaded()
{
  var pdfFrame = window.frames["pdf"];
  pdfFrame.focus();
 // pdfFrame.print();
}

$(document).on('click','#SEARCH_NO',function(event){
	$('.riderAssign').css({
        'display': 'none'
    });
	$('#rider-form').css({
        'display': 'block'
    });
	var v_id = $(this).attr('id');
	var orderId = $(this).attr('value').split("@")[1];
		
	var queryString = "ordid="+orderId;
	
	var orderList = null;
	var customerData = null;
	var riderjson = null;
	
	$('#rider').empty().append('<option value="" disabled selected>Select rider</option>');
	$.getJSON("orderInformation.action", queryString,function(data){
		
		var orderList = data.responseJSON.ORDER_LIST;
		var customerData = data.responseJSON.CUSTOMER_INFO;
		var riderjson = data.responseJSON.RIDER_LIST;
				
		var customerName = customerData[0].CUSTOMER_NAME;
		var txnDate = customerData[0].TXN_DATE;
		var channel = customerData[0].CHANNEL;
		var paymentMode = customerData[0].PAYMENT_MODE;
		var customerName = customerData[0].CUSTOMER_NAME;
		var customerEmail = customerData[0].EMAIL_ID;
		var customerMobile = customerData[0].MOBILE_NUMBER;
		var shippingAddress = customerData[0].SHIPPING_ADDRESS;
		var orderValue = customerData[0].TXN_AMT;
		
		var shippingAddress = shippingAddress.split("#");
	
		for (var k = 0; k < shippingAddress.length; k++) {
			var address = shippingAddress[0];
			var remarks = shippingAddress[1];			
		}
		
		if(remarks == null)
		{
			remarks = "No remarks.";
		} else if (remarks == "null") {
			remarks = "No remarks."
		}
		
		document.getElementById("orderValue").innerHTML = orderValue;
		document.getElementById("orderId").innerHTML = orderId;
		document.getElementById("orderDate").innerHTML = txnDate;
		document.getElementById("orderChannel").innerHTML = channel;
		document.getElementById("customerName").innerHTML = customerName;
		document.getElementById("customerEmail").innerHTML = customerEmail;
		document.getElementById("customerMobile").innerHTML = customerMobile;
		document.getElementById("location").innerHTML = address;
		document.getElementById("orderMode").innerHTML = paymentMode;
		document.getElementById("remarks").innerHTML = remarks;
		
		$('#assign_order_Id').val(orderId);
				
		
		console.log("Customer Name "+customerName);
		console.log("Customer Name "+txnDate);
		console.log("Customer Name "+channel);
		console.log("Customer Name "+paymentMode);
		console.log("Customer Name "+customerName);
		
	    var val = 1;
	    var rowindex = 0;
	    var colindex = 0;
	    var addclass = "";
	    
	    
	    var html = "<table border='1|1' cellpadding='10'>";
	    for (var i = 0; i < orderList.length; i++) {
	        html+="<tr>";
	        html+="<td>"+orderList[i].PRD_NAME+"</td>";
	        html+="<td>"+orderList[i].QUANTITY+"</td>";
	        html+="<td style='text-align: right;'>"+orderList[i].UNIT_PRICE+".00</td>";
	        html+="<td style='text-align: right;'>"+orderList[i].TOTAL_PRICE+".00</td>";       
	        html+="</tr>";
	    }
	    html+="</table>";
	    $("#storeTBody").html(html);
	    	    
	    //loading rider dropdown
		for (var m = 0; m < riderjson.length; m++)
		{
			var riderName = riderjson[m].RIDER_NAME;
			var riderops = $('<option/>', {value: riderjson[m].RIDER_ID, text: riderName}).attr('data-id',m);   		
			$('#rider').append(riderops);
		}
	    
	    $('#my_modal').appendTo("body").modal('show');
		
	});	
});
	
</script>
</head>
<body>
<body>
	<form id="form1" method="post" name="form1"></form>
	<div class="">
		<div class="page-header">
			<div>
				<label>Dashboard</label>
			</div>
		</div>
		<ol class="breadcrumb">
			<li class="breadcrumb-item">
				<a href="home.action">Dashboard</a>
			</li>	
			
		</ol>
				
		<div class="container-fluid">
			<!-- <div class="row"> -->
				<div class="content-panel desktop-panel">
					<div class="panel-head">
						<label><i class="fa fa-tags"></i> New Orders</label>
					</div>
					<table width='100%' class="table stripe datatable" id="newOrderTable">
						<thead>
							<tr style="border-bottom: 0;">
								<th>#</th>
								<th>Order ID</th>
								<th style='display: none'></th>
								<th>Amount (Ksh)</th>
								<th>Date Requested</th>
								<th>Shipping Address</th>
							</tr>
						</thead>
						<tbody id="newOrderTbody">
						</tbody>
					</table>
				</div>
			<!-- </div> -->			
		</div>
		<div class="col-sm-6 col-xs-6">
		
		</div>		
	</div>
	
	
	
	<div class="modal fade" id="my_modal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div id="printable">
					<div class="modal-container">
					<div class="modal-header">
						<label>Amur Order Information</label>
						<img src="${pageContext.request.contextPath}/images/logo.png" alt="Amur Logo"/>
					</div>
					
					<div class="modal-body" id="modal-body">						
						<table id="order-info-table">
							
							<tbody>
								<tr>
									<td>
										<div class="modal-section">
											<div class="section-header">
												<label>Order Details</label>
											</div>
											<div class="section-body">
												<table class="panel-table">
													<tbody>
														<tr>
															<td><label><strong>Order ID : </strong></label></td>
															<td class="modal-data"><span id="orderId"></span></td>
														</tr>
														<tr>
															<td><label><strong>Transaction Date/Time : </strong></label></td>
															<td class="modal-data"><span id="orderDate"></span></td>
														</tr>
														<tr>
															<td><label><strong>Channel : </strong></label></td>
															<td class="modal-data"><span id="orderChannel"></span></td>
														</tr>
														<tr>
															<td><label><strong>Payment Mode : </strong></label></td>
															<td class="modal-data"><span id="orderMode"></span></td>
														</tr>
														<!-- tr>
															<td><label><strong>Payment Ref. Number : </strong></label></td>
															<td class="modal-data"><span id="refnumber"></span></td>
														</tr-->	
														<tr class="riderAssign">
										                	<td><label><strong>Rider Name : </strong></label></td>
												            <td class=""><span id="rider_name"></span></td>
											            </tr>								
													</tbody>
												</table>									
											</div>
										</div>
										<div class="modal-section">
											<div class="section-header">
												<label>Customer Information</label>
											</div>
											<div class="section-body">
												<table class="panel-table">
													<tbody>
														<tr>
										                	<td><label><strong>Name : </strong></label></td>
												           	<td class="modal-data"><span id="customerName"></span></td>
											            </tr>
											            <tr>
												            <td><label><strong>Email :</strong></label></td>
												            <td class="modal-data"><span id="customerEmail"></span></td>
											            </tr>
											            <tr>
												            <td><label><strong>Phone Number :</strong></label></td>
												            <td class="modal-data"><span id="customerMobile"></span></td>
											            </tr>							                
											            <tr>
												            <td><label><strong>Specific Location :</strong></label></td>
												            <td class="modal-data"><span id="location"></span></td>
											            </tr>
											            <tr>
												            <td><label><strong>Remarks :</strong></label></td>
												            <td class="modal-data"><span id="remarks"></span></td>
											            </tr>											
													</tbody>
												</table>									
											</div>
										</div>
										
										<div class="modal-section" id="rider-form">
											<div class="section-header">
												<label>Select Delivery Rider</label>
											</div>
											<div class="section-body">
												<form id="assign-rider-form" name="assign-rider-form" method="post">
													<div class="form-group">
														<label for="rider">Choose Rider</label>
														<select id="rider" name="riderid" required>
															 <option value="" disabled selected>Select rider</option>
														</select>
													</div>
													<input type="hidden" value="" id="assign_order_Id" name="assign_order_Id"/>
												</form>									
											</div>
										</div>		
									</td>
									
									
									<td valign="top">
										<div class="modal-part modal-right">
											<div class="modal-section">
												<div class="section-header">
													<label>Basket List</label>
												</div>
												<div class="section-body">
													<table class='amur_table' id='order_list'>
													<thead>
														<tr>
															<th>Product Name</th>
															<th>Quantity</th>
															<th>Unit Price</th>
															<th>Total Price</th>											
														</tr>
													</thead>
													<tbody id="storeTBody">
													</tbody>
													
												</table>
												<div class="orderValue">
													<label>Total : <span id="orderValue"></span></label>
												</div>									
												</div>
											</div>		
										</div>	
									</td>
								</tr>								
							</tbody>
						</table>					
						
					</div>
				</div>
				
				</div>
				

				<div class="modal-footer">
					<button type="button" class="btn btn-info" id="print-order">Print Order</button>
					<button type="button" class="btn btn-danger" id="cancel-order">Cancel Order</button>
					<button type="button" class="btn btn-success" id="assign-order">Assign Rider</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				</div>
				<script type="text/javascript">
				
				</script>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</body>
</html>