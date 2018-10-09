<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
	<%@taglib uri="/struts-tags" prefix="s"%>  
	<%String ctxstr = request.getContextPath(); %>
	<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
	
<script type="text/javascript">

	//Showing Customer General Information
	var customerData = '${responseJSON.CUSTOMER_INFO}';
	var customerJSON = JSON.parse(customerData);
	
	var orderId = customerJSON[0].ORDER_ID;
	var txnDate = customerJSON[0].TXN_DATE;
	var channel = customerJSON[0].CHANNEL;
	var paymentMode = customerJSON[0].PAYMENT_MODE;
	var customerName = customerJSON[0].CUSTOMER_NAME;
	var customerEmail = customerJSON[0].EMAIL_ID;
	var customerMobile = customerJSON[0].MOBILE_NUMBER;
	var shippingAddress = customerJSON[0].SHIPPING_ADDRESS;
	
	$(document).ready(function(){
		document.getElementById("orderId").innerHTML = orderId;
		document.getElementById("orderDate").innerHTML = txnDate;
		document.getElementById("orderChannel").innerHTML = channel;
		document.getElementById("customerName").innerHTML = customerName;
		document.getElementById("customerName2").innerHTML = customerName;
		document.getElementById("customerEmail").innerHTML = customerEmail;
		document.getElementById("customerMobile").innerHTML = customerMobile;
		document.getElementById("customerMobile2").innerHTML = customerMobile;
		document.getElementById("location").innerHTML = shippingAddress;
		document.getElementById("orderMode").innerHTML = paymentMode;
	});
	
	//Showing Order List Table contents
	var orderData = '${responseJSON.ORDER_LIST}';
	var orderJSON = JSON.parse(orderData);
	
	$(document).ready(function(){
		
		var val = 1;
        var rowindex = 0;
        var colindex = 0;
        var addclass = "";
		
		$.each(orderJSON, function(i, v) {
            if(val % 2 == 0 ) {
                addclass = "even";
                val++;
            }
            else {
                addclass = "odd";
                val++;
            }  
            var rowCount = $('#storeTBody > tr').length;
            
            colindex = ++ colindex; 
            
            
            var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
            "<td >"+colindex+"</td>"+  
            "<td>"+v.PRD_NAME+"</td>"+
            "<td>"+v.UNIT_PRICE+"</td>"+   
            "<td>"+v.TOTAL_PRICE+"</span> </td>"+ 
            "<td>"+v.QUANTITY+"</span></td>";
           
                $("#merchantTBody").append(appendTxt);  
                rowindex = ++rowindex;
                colindex = ++colindex;
        });
	});

</script>	
	

	<script type="text/javascript">
	
		
		/*var orderData ='${responseJSON.ORDER_DETAILS}';
	    var json = JSON.parse(orderData);
	    
	    var userLinkData ='${USER_LINKS}';
	    var jsonLinks = jQuery.parseJSON(userLinkData);
	    var linkIndex = new Array();
	    var linkName = new Array();
	    var linkStatus = new Array();
	    
	    $(document).ready(function(){
			if(orderData != null){
				var orderId = json[0].ORD_ID;
			    var date = json[0].TXN_DATE;
			    var channel = json[0].CHANNEL;
			    var customername = json[0].CUSTOMERNAME;
			    var customeremail = json[0].CUSTOMEREMAIL;
			    var customermobile = json[0].CUSTOMERMOBILENO;
			    var orderMode = json[0].PAYMENTMODE;
			    var address = json[0].SHIPPINGADDRESS;
			    
			    document.getElementById("orderId").innerHTML = orderId;
		    	document.getElementById("orderDate").innerHTML = date;
		    	document.getElementById("orderChannel").innerHTML = channel;
		    	document.getElementById("customerName").innerHTML = customername;
		    	document.getElementById("customerName2").innerHTML = customername;
		    	document.getElementById("customerEmail").innerHTML = customeremail;
		    	document.getElementById("customerMobile").innerHTML = customermobile;
		    	document.getElementById("customerMobile2").innerHTML = customermobile;
		    	document.getElementById("location").innerHTML = address;
		    	document.getElementById("orderMode").innerHTML = orderMode;
		    	$('#order_Id').val(orderId);	    	
		    	
		    	var product_list = json[0].PRODUCTS;    	
		    	
		    	var val = 1;
		        var rowindex = 0;
		        var colindex = 0;
		        var addclass = "";
		    	
		    	$.each(product_list, function(i, v) {
		            if(val % 2 == 0 ) {
		                addclass = "even";
		                val++;
		            }
		            else {
		                addclass = "odd";
		                val++;
		            }  
		            var rowCount = $('#storeTBody > tr').length;
		            
		            //rowindex = ++rowindex;
		            colindex = ++ colindex; 
		            
		            
		            var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
		            "<td >"+colindex+"</td>"+  
		            "<td><id='SEARCH_NO' value='ordid@"+v.PRODUCT_NAME+"' aria-controls='DataTables_Table_0'>"+v.PRODUCT_NAME.replace(/[+]/g, " ")+"</span> </td>"+ 
		            "<td style='display:none'>"+v.PRODUCT_PRICE+" </td>"+
		            "<td>"+v.PRODUCT_PRICE+"</td>"+   
		            "<td>"+v.PRODUCT_QUANTITY+"</span> </td>"+ 
		            "<td>"+v.PRODUCT_DATE+"</span></td>";
		           
		                $("#merchantTBody").append(appendTxt);  
		                rowindex = ++rowindex;
		                colindex = ++colindex;
		        });
		    	
		    	//console.log(userGroupData);
		        $.each(jsonLinks, function(index, v) {
		            linkIndex[index] = index;
		            linkName[index] = v.name;
		            linkStatus[index] = v.status;
		        }); 
		        
		        $("input[type=search]").on('keyup',function() {
		            
		            var ariaControlsval=$(this).attr('aria-controls');
		            var contVal=$(this).val();
		            //alert(ariaControlsval+"------------"+contVal)
		            if(ariaControlsval=="DataTables_Table_0" && contVal=="" ) {
		                $("#stores").hide();
		                $("#terminals").hide();
		            } 
		            else if(ariaControlsval=="DataTables_Table_Store" && contVal=="" ) {
		                $("#terminals").hide();
		            }               
		        });  
		    	
		    }*/
			
			$('#assign-order').on('click',function() {
	    		 var rider_number = document.getElementById('rider').value;
	    		 var order_number = document.getElementById('order_Id').value;
	    		 alert("Rider Number :: "+rider_number);
	    		 alert("Order Number :: "+order_number);
	    		 $("#rider_order_form")[0].action="<%=request.getContextPath()%>/<%=appName %>/assignOrder.action";
	    	     $("#rider_order_form").submit();  
	    		  
	    	 });
		});
	    
	    $(document).ready(function() {
	    	$('#DataTables_Table_0').DataTable({
	    		"filter": true
	    	});
	    } );
	    
	    $(document).ready(function() {
	    	var riderjson = '${riderRespJSON.RIDER_JSON}';
	    	
	    	var riderArr = jQuery.parseJSON(riderjson)
	    	for (var m = 0; m < riderArr.length; m++)
	    	{
	    		var riderName = riderArr[m].riderFirstName +" "+ riderArr[m].riderLastName;
	    		var riderops = $('<option/>', {value: riderArr[m].riderId, text: riderName}).attr('data-id',m);   		
	    		$('#rider').append(riderops);
	    	}
	    }); 
	   
	        
	</script>
</head>
<body>
	<div class="page-header">
        <div>
            <label>Order Details</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item">
          <a href="orderDetails.action">All Orders</a>
        </li>
        <li class="breadcrumb-item active">
          <a href="#">Order Details</a>       
        </li>
    </ol>
    
    <div class="">
    	<div class="box-content content-panel">    
    	<div class="container">
    		<div class="row">
    			<div class="col-sm-4 col-xs-6">
    				<div class="body-panel">
				        <div class="panel panel-default">
					        <div class="panel-heading">
					        	<h3 class="panel-title"><i class="fa fa-cog"></i> Order Details</h3>
					        </div>
					        <table class="table panel-table cell-border">
						        <tbody>
							        <tr>
							        	<td><label>Order ID</label></td>
							        	<td class=""><span id="orderId"></span></td>
							        </tr>
							        <tr>
							        	<td><label>Date</label></td>
							        	<td class=""><span id="orderDate"></span></td>
							        </tr>
							        <tr>
							        	<td><label>Channel</label></td>
							        	<td class=""><span id="orderChannel"></span></td>
							        </tr>	
							        <tr>
							        	<td><label>Payment Mode</label></td>
							        	<td class=""><span id="orderMode"></span></td>
							        </tr>	
							        <tr>
							        	<td><label>Payment Ref. Number</label></td>
							        	<td class=""><span id="refnumber"></span></td>
							        </tr>				                  
						        </tbody>
					        </table>
				        </div>
			        </div>
    				
    			</div>
    			
    			<div class="col-sm-4 col-xs-6">
	    			<div class="body-panel">
		            	<div class="panel panel-default">
			              	<div class="panel-heading">
			                	<h3 class="panel-title"><i class="fa fa-cog"></i> Customer Details</h3>
			              	</div>
			              	<table class="table panel-table">
				                <tbody>
					                <tr>
						                <td><label>Name</label></td>
						                <td class=""><span id="customerName"></span></td>
					                </tr>
					                <tr>
						                <td><label>Email</label></td>
						                <td class=""><span id="customerEmail"></span></td>
					                </tr>
					                <tr>
						                <td><label>Phone Number</label></td>
						                <td class=""><span id="customerMobile"></span></td>
					                </tr>				                  
				                </tbody>
			              	</table>
		            	</div>
	          		</div>
    			</div>
    			
    			<div class="col-sm-4 col-xs-6">
	    			<div class="body-panel">
		            	<div class="panel panel-default">
			              	<div class="panel-heading">
			                	<h3 class="panel-title"><i class="fa fa-cog"></i> Shipping Addresses</h3>
			              	</div>
			              	<div class="panel-body">
				                <table class="table panel-table">
					                <tbody>	
					                	<tr>
							                <td><label>Name</label></td>
							                <td class=""><span id="customerName2"></span></td>
						                </tr>					                
						                <tr>
							                <td><label>Phone Number</label></td>
							                <td class=""><span id="customerMobile2"></span></td>
						                </tr>
						                <tr>
							                <td><label>Specific Location</label></td>
							                <td class=""><span id="location"></span></td>
						                </tr>
						                <tr>
							                <td><label>Remarks</label></td>
							                <td class=""><span id="remarks"></span></td>
						                </tr>
					                </tbody>
				              	</table>
			              	</div>              
		            	</div>
	          		</div>
    			</div>
    		</div>
    	</div>
    </div>
    
    <div class="box-content content-panel panel-head">
    	<label>Order List</label>
    </div>
    
    <div class="content-panel">            
		<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable" id="DataTables_Table_0" >
            <thead>
                <tr>
                    <th>S No.</th>
                    <th>Product Name</th>
                    <th>Unit Price</th>
                    <th>Total Price</th>
                    <th>Quantity</th>
                </tr>
            </thead> 
            <tbody id="merchantTBody">
            
            </tbody>
		</table>                
	</div>
	
	<!--div class="box-content content-panel panel-head">
    	<label>Assign Rider</label>
    </div>
	
	<div class="content-panel">  
	
	</div>
	
    </div-->
    
</body>
</html>