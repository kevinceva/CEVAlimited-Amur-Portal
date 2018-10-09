
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String ctxstr = request.getContextPath();
%>
<%
	String appName = session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString();
%>

<script type="text/javascript">		
		var custStatus = "";
		var customerInfo ='${responseJSON.CUSTOMER_INFO}';		
		var infoJson = JSON.parse(customerInfo);
		
		var customer_id = infoJson[0].CUSTOMER_ID;
		var customer_name = infoJson[0].CUSTOMER_NAME;
		var customer_mobile = infoJson[0].MOBILE_NUMBER;
		var customer_email = infoJson[0].EMAIL;
		var date_created = infoJson[0].DATE_CREATED;
		var customer_status = infoJson[0].STATUS;
		
		
		
		$(document).ready(function(){
			if(customer_status == '1'){
				custStatus='Active';
				$("#activate-customer").css("display", "none");
			}else{
				custStatus='Inactive';
				$("#deactivate-customer").css("display", "none");
			}
			
	    	document.getElementById("customer_name").value = customer_name.replace(/_/g, "'");
	    	document.getElementById("customer_id").value = customer_id;
	    	document.getElementById("custId").value = customer_id;
	    	document.getElementById("customer_email").value = customer_email;
	    	document.getElementById("customer_phone").value = customer_mobile;
	    	document.getElementById("date_created").value = date_created;
	    	document.getElementById("status").value = custStatus;
	    });
</script>

<script type="text/javascript">	
		
		$(document).ready(function(){
			var customerWallet ='${responseJSON.CUSTOMER_WALLET}';
			var walletJSON = JSON.parse(customerWallet);
		    
		    console.log("Wallet JSON"+walletJSON);
		    var val = 1;
		    var rowindex = 0;
		    var colindex = 1;
		    var addclass = "";
		    var accountType ="";
		    
		    
		    
		    $.each(walletJSON, function(i, v) {
		        if(val % 2 == 0 ) {
		            addclass = "even";
		            val++;
		        }
		        else {
		            addclass = "odd";
		            val++;
		        }  
		        
		        if(v.ACCOUNT_TYPE == 'LIFEINS'){
		        	accountType = "Life Insuarance";
				}else if(v.ACCOUNT_TYPE == 'WALLET'){
					accountType = "Amur Wallet";
				}else{
					accountType = "Health Insuarance";
				}
		        
		        
		        var rowCount = $('#storeTBody > tr').length;
		        
		        //rowindex = ++rowindex;
		        //colindex = ++ colindex; 
		        
		        
		        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
		        "<td id='tableRow'>"+accountType+"</td>"+  
		        "<td >"+v.ACCOUNT_NUMBER+"</td>"+  
		        "<td >"+v.PREMIUM+"</td>"+ 
		        "<td >"+v.SUMASSURED+"</td>";
		            
		            $("#walletTBody").append(appendTxt);  
		            rowindex = ++rowindex;
		            colindex = ++colindex;
		    });
		    
		    $('#walletTable').DataTable();
		    
	    });
</script>

<script type="text/javascript">
		
		
		//Action buttons submit
		function postData(actionName,paramString){
		    $('#form2').attr("action", actionName)
		            .attr("method", "post");
		    
		    var paramArray = paramString.split("&");
		    var input = "" ;
		    $(paramArray).each(function(indexTd,val) {
		        if(val != "") {
		            input = $("<input />").attr("type", "hidden").attr("name", val.split("=")[0]).val(val.split("=")[1].trim());
		            $('form').append($(input));  
		        }
		    });

		    $('form').submit(); 
		} 


		$(document).ready(function () { 
			var customerOrder ='${responseJSON.CUSTOMER_ORDERS}';
			var orderJson = JSON.parse(customerOrder);
		    
		    //console.log("json["+json+"]");
		    var val = 1;
		    var rowindex = 0;
		    var colindex = 1;
		    var addclass = "";
		    
		    
		    
		    $.each(orderJson, function(i, v) {
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
		        //colindex = ++ colindex; 
		        
		        
		        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
		        "<td >"+colindex+"</td>"+  
		        "<td><a href='#' id='SEARCH_NO' value='orderId@"+v.ORDER_ID+"' aria-controls='DataTables_Table_0'>"+v.ORDER_ID+"</span></td>"+
		        //"<td><id='SEARCH_NO' value='orderId@"+v.ORDER_ID+"' aria-controls='DataTables_Table_0'>"+v.ORDER_ID+"</span> </td>"+ 
		        "<td style='display:none'>"+v.ORDER_ID+" </td>"+
		        "<td>"+v.TXN_AMOUNT+"</td>"+   
		        "<td>"+v.TXN_DATE+"</span> </td>"+ 
		        "<td>"+v.ORDER_STATUS+"</span></td>";
		        //"<td><a id='view-order' class='btn btn-success' index='"+rowindex+"'  href='#' title='View Order Contents' data-rel='tooltip' ><i class='icon icon-book icon-white'></i></a> </td></tr>";
		            
		            $("#merchantTBody").append(appendTxt);  
		            rowindex = ++rowindex;
		            colindex = ++colindex;
		    });

		    
		        //console.log(userGroupData);
		        
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
		});  
		
		$(document).on('click','a',function(event){
			
			var v_id = $(this).attr('id');
			var orderId = $(this).attr('value').split("@")[1];
			
			var table = $('#order_list').DataTable();
			table.clear().draw();
			
			var queryString = "orderId="+orderId;
			$.getJSON("fetchOrderList.action", queryString,function(data){
				
				var orderList = data.responseJSON.PRODUCT_LIST;
				//var jsonList = JSON.parse(orderList);
				
				console.log("Product List :: "+orderList);
			    var val = 1;
			    var rowindex = 0;
			    var colindex = 0;
			    var addclass = "";
			    
			    $.each(orderList, function(i, v) {
			    	if(val % 2 == 0 ) {
						addclass = "even";
						val++;
					}
					else {
						addclass = "odd";
						val++;
					}  
			    	
			    	var i= table.row.add( [
						v.PRODUCT_NAME,
						v.PRODUCT_PRICE,
						v.PRODUCT_QUANTITY,
						v.PRODUCT_DATE
			        ] ).draw( false );
			    	table.rows(i).nodes().to$().attr("id", rowindex);
			    	table.rows(i).nodes().to$().attr("index", rowindex);
			 
			        rowindex = ++rowindex;
			        colindex = ++colindex;			    	
			    	
			    });
			    
			    $('#my_modal').appendTo("body").modal('show');
				
			});
			
		});

		$(document).ready(function() {
			$('#DataTables_Table_0').DataTable({
				"filter": true,
				columnDefs: [
			        { "width": "80px", "targets": [1] }]
			});
		});
		
		$(document).ready(function(){	
			var submitPath = "<%=request.getContextPath()%>/<%=appName%>/activateDeactivateCustomer.action";
			
			var current_status = document.getElementById("status").value;
			
			$('#activate-customer').on('click',function() {
				var activate_status = "";
				swal({
		            title: "Activate Customer?",
		            text: "Press Ok to Activate Customer.", 
		            icon: "warning",
		            buttons: true,
		            dangerMode: true,
		        })
		        .then((willDelete) => {
		          if (willDelete) {
		        	  $('#customer-form').submit(function(){
						    $.post(submitPath, $(this).serialize(), function(json) {		      
						      if(json.responseJSON.remarks == "SUCCESS"){
						    	  swal({
						    		  title: "Success",
						    		  text: "Customer Activated successfully.",
						    		  icon: "success",
						    		  button: "Continue",
						    		}).then(function(result){
						    			  						    			
						    			if(current_status == 'Inactive'){
						    				activate_status='Active';
						    			}else{
						    				activate_status = 'Inactive';
						    			}
						    			document.getElementById("status").value = "";
						    			document.getElementById("status").value = activate_status;
						    			$("#activate-customer").css("display", "none");
						    			$("#deactivate-customer").css("display", "inline-block");
						            });
						      }else {
						    	  swal({
						    		  title: "Sorry!",
						    		  text: "Customer Activation failed. Please try again later.",
						    		  icon: "error",
						    		  button: "Continue",
						    		}).then(function(result){
						    			  //window.location = backPath;
						            });
						      }
						      
						    }, 'json');
						    return false;
						  });
						  $("#customer-form").submit();
		          } else {
		            swal("Request Cancelled.");
		          }
		        });  	
			}); 
			
			$('#deactivate-customer').on('click',function() { 
				var deactivate_status = "";
				swal({
		            title: "Deactivate Customer?",
		            text: "Press Ok to Deactivate Customer.", 
		            icon: "warning",
		            buttons: true,
		            dangerMode: true,
		        })
		        .then((willDelete) => {
		          if (willDelete) {
		        	  $('#customer-form').submit(function(){
						    $.post(submitPath, $(this).serialize(), function(json) {		      
						      if(json.responseJSON.remarks == "SUCCESS"){
						    	  swal({
						    		  title: "Success",
						    		  text: "Customer Deactivated successfully.",
						    		  icon: "success",
						    		  button: "Continue",
						    		}).then(function(result){
						    			
						    			if(current_status == 'Active'){
						    				deactivate_status='Inactive';
						    			}else{
						    				deactivate_status='Active';
						    			}
						    			document.getElementById("status").value = "";
						    			document.getElementById("status").value = deactivate_status;
						    			$("#activate-customer").css("display", "inline-block");
						    			$("#deactivate-customer").css("display", "none");
						    			
						            });
						      }else {
						    	  swal({
						    		  title: "Sorry!",
						    		  text: "Customer Deactivation failed. Please try again later.",
						    		  icon: "error",
						    		  button: "Continue",
						    		})/*.then(function(result){
						    			  window.location = backPath;
						            });*/
						      }
						      
						    }, 'json');
						    return false;
						  });
						  $("#customer-form").submit();
		          } else {
		            swal("Request Cancelled.");
		          }
		        });  	
			}); 
		});
		
	</script>


</head>
<body>
	<div class="">
		<div class="page-header">
			<div>
				<label>Customer Details</label>
			</div>
		</div>
		<ol class="breadcrumb">
			<li class="breadcrumb-item"><a href="home.action">Dashboard</a>
			</li>
			<li class="breadcrumb-item"><a href="customerDetails.action">All
					Customers</a></li>
			<li class="breadcrumb-item active"><a href="#">Customer
					Details</a></li>
		</ol>

		<div class="content-panel">
			<div class="cont">
				<div class="tab">
					<div id="tab-buttons">
						<button class="tablinks customer_general"
							onclick="openCity(event, 'customer_general')" id="defaultOpen">
							<label>Customer General</label>
						</button>
						<button class="tablinks customer_orders"
							onclick="openCity(event, 'customer_orders')">
							<label>Orders & Transactions</label>
						</button>
						<!-- button class="tablinks bookings" onclick="openCity(event, 'bookings')"><label>Bookings & Reservations</label></button-->
						<button class="tablinks cash_wallet"
							onclick="openCity(event, 'cash_wallet')">
							<label>Wallet & Earnings</label>
						</button>
						<!--button class="tablinks chat" onclick="openCity(event, 'chat')">
							<label>Chat</label>
						</button-->
					</div>
				</div>
			</div>

			<div class="tab-body">
				<div id="customer_general" class="tabcontent">
					<div class="container">
						<div class="row">
							<form method="post" id="customer-form" name="customer-form">
								<div class="col-sm-4 col-xs-6">
									<div class="form-group">
										<label class="col-form-label" for="customer_name">Full
											Name</label> <input class="form-control" id="customer_name"
											type="text" readonly>
									</div>
									<div class="form-group">
										<label class="col-form-label" for="customer_id">Customer
											ID</label> <input class="form-control" id="customer_id" type="text"
											readonly>
									</div>
								</div>

								<div class="col-sm-4 col-xs-6">
									<div class="form-group">
										<label class="col-form-label" for="customer_email">Email</label>
										<input class="form-control" id="customer_email" type="text"
											readonly>
									</div>
									<div class="form-group">
										<label class="col-form-label" for="customer_phone">Phone
											Number</label> <input class="form-control" id="customer_phone"
											type="text" readonly>
									</div>
								</div>

								<div class="col-sm-4 col-xs-6">
									<div class="form-group">
										<label class="col-form-label" for="status">Status</label> <input
											class="form-control" id="status" type="text" readonly>
									</div>
									<div class="form-group">
										<label class="col-form-label" for="date_created">Date
											Created</label> <input class="form-control" id="date_created"
											type="text" readonly>
									</div>
								</div>
								<input type="hidden" id="custId" name="custId" />
							</form>
						</div>
						<div class="row">
							<div class="col-sm-4 col-xs-6">
								<input type="button" class="btn activate" id="activate-customer"
									value="Activate" /> <input type="button"
									class="btn deactivate" id="deactivate-customer"
									" value="Deactivate" />
							</div>
						</div>
					</div>
				</div>

				<div id="customer_orders" class="tabcontent">
					<div class="car">
						<div class="table-responsive">
							<table width='100%'
								class="table table-striped table-bordered bootstrap-datatable datatable"
								id="DataTables_Table_0">
								<thead>
									<tr>
										<th>S No.</th>
										<th>Order ID</th>
										<th style='display: none'></th>
										<th>Amount</th>
										<th>Date</th>
										<th>Status</th>
										<!-- th>Actions</th -->
									</tr>
								</thead>
								<tbody id="merchantTBody">

								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div id="bookings" class="tabcontent">
					<div class="car">
						<div class="table-responsive">
							<table class="table table-bordered" id="dataTable2" width="100%"
								cellspacing="0">
								<thead>
									<tr>
										<th>Ticket Number</th>
										<th>Category</th>
										<th>Status</th>
										<th>Date Created</th>
										<th>View Action</th>
									</tr>
								</thead>
								<tfoot>
									<tr>
										<th>Ticket Number</th>
										<th>Category</th>
										<th>Status</th>
										<th>Date Created</th>
										<th>View Action</th>
									</tr>
								</tfoot>
								<tbody>
									<tr>
										<td>65825</td>
										<td>Air Ticket</td>
										<td>Paid</td>
										<td>24/09/2017</td>
										<td><a href="view_customer.html" id="edit_button"><img
												src="images/edit.png"></a></td>
									</tr>
									<tr>
										<td>65825</td>
										<td>Hotel Room</td>
										<td>Reserved</td>
										<td>24/09/2017</td>
										<td><a href="view_customer.html" id="edit_button"><img
												src="images/edit.png"></a></td>
									</tr>

								</tbody>
							</table>
						</div>
					</div>
				</div>


				<div id="cash_wallet" class="tabcontent">
					<div class="">
						<div class="table-responsive">
							<table class="table table-bordered" id="walletTable" width="100%"
								cellspacing="0">
								<thead>
									<tr>
										<th>Account Type</th>
										<th>Account Number</th>
										<th>Premium (Ksh.)</th>
										<th>Sum Assured (Ksh.)</th>
									</tr>
								</thead>
								<tbody id="walletTBody">
									
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div id="chat" class="tabcontent">
					<div class="">chat</div>
				</div>
			</div>


		</div>

		<script>
	
	
      function openCity(evt, cityName) {
          var i, tabcontent, tablinks;
          tabcontent = document.getElementsByClassName("tabcontent");
          for (i = 0; i < tabcontent.length; i++) {
              tabcontent[i].style.display = "none";
          }
          tablinks = document.getElementsByClassName("tablinks");
          for (i = 0; i < tablinks.length; i++) {
              tablinks[i].className = tablinks[i].className.replace(" active", "");
          }
          document.getElementById(cityName).style.display = "block";
          evt.currentTarget.className += " active";
      }

      // Get the element with id="defaultOpen" and click on it
      document.getElementById("defaultOpen").click();
     </script>

	</div>

	<div class="modal fade" id="my_modal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<label class="modal-title">Orders List</label>
				</div>
				<div class="modal-body">
					<table
						class='table table-striped table-bordered bootstrap-datatable datatable'
						id='order_list'>
						<thead>
							<tr>
								<th>Product Name</th>
								<th>Price</th>
								<th>Quantity</th>
								<th>Date Added</th>
							</tr>
						</thead>
						<tbody id="storeTBody">
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
</body>


</html>