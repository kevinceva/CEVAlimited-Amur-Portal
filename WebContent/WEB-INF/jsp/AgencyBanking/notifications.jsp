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

<script type="text/javascript">
	$(document).ready(function(){
		
		/*var validationRules = {
				rules : {
					deliveryChannel : {required : true},
					sendToGroup : {required : true},
					sendToRider : {required : true},
					sendToCustomer : {required : true},
					notificationType : {required : true},
					notificationSubject : {required : true},
					messageTitle : {required : true, regex : /^[a-zA-Z0-9'.,-]{4,}(?: [a-zA-Z0-9'.,-]+){0,}+$/},
					messageSubTitle : {required : true, regex : /^[a-zA-Z0-9'.,-]{4,}(?: [a-zA-Z0-9'.,-]+){0,}+$/},
					notificationMessage : {required : true, regex : /^[a-zA-Z0-9'.,-]{4,}(?: [a-zA-Z0-9'.,-]+){0,}+$/}
				}, 
				messages : {
					deliveryChannel : { 
		                required : "Please enter the Delivery Channel",
		                regex : "Please use letters and numbers only."
		              },
					sendToGroup : {
						required : "Please select a Group."
					},
					notificationType : {
						required : "Please select Notification Type."
					},
					notificationSubject : {
						required : "Please select Notification Subject."
					},
					messageTitle : {
						required : "Please enter Message Title.",
						regex : "Please use letters and numbers only."
					},
					messageSubTitle : {
						required : "Please enter Sub Title."
						regex : "Please use letters and numbers only."
					},
					notificationMessage : {
						required : "Please enter a message.",
						regex : "Please use letters and numbers only."
					}              
		              
				}
		}*/
		
		var customerJSON = '${responseJSON.CUSTOMERS}';
		var customerData = JSON.parse(customerJSON);
		var ridersJSON = '${responseJSON.Riders.RIDERS}';
		var riderData = JSON.parse(ridersJSON);
		
		var queryString = null;
		
		//For riders
		for (var m = 0; m < riderData.length; m++)
		{
			var riderName = riderData[m].RIDER_NAME;
			var riderops = $('<option/>', {value: riderData[m].MOBILE_NUMBER, text: riderName}).attr('data-id',m);   		
			$('#sendToRider').append(riderops);
		}
		
		//For Customers
		for (var n = 0; n < customerData.length; n++)
		{
			var riderName = customerData[n].CUSTOMER_NAME;
			var riderops = $('<option/>', {value: customerData[n].MOBILE_NUMBER, text: riderName}).attr('data-id',n);   		
			$('#sendToCustomer').append(riderops);
		}
		
		$('#sendToGroup').change(function(){		    
		    var reciepientGroup = $(this).val();
		    if(reciepientGroup == "customers"){
		    	
		    	$('#selectCustomerContainer').show();
		    	$('#selectRiderContainer').hide();
		    		    	
		    } else if(reciepientGroup == "riders"){
		    	$('#selectRiderContainer').show();
		    	$('#selectCustomerContainer').hide();

		    }
		});
		
		$('#deliveryChannel').change(function(){
			var deliveryGroup = $(this).val();
			
			if(deliveryGroup == "EMAIL"){
				$('#messageTitle').show();
				$('#messageSubTitle').hide();
			} 
			else if(deliveryGroup == "PUSH"){
				$('#messageTitle').show();
				$('#messageSubTitle').show();
			}
			else if(deliveryGroup == "SMS"){
				$('#messageTitle').hide();
				$('#messageSubTitle').hide();
			}
			else {
				$('#messageTitle').hide();
				$('#messageSubTitle').hide();
			}
				
		});
		
		$('#btn-clear').click(function(){
			document.getElementById('send-notification-form').reset();
			$('#selectCustomerContainer').hide();
	    	$('#selectRiderContainer').hide();
	    	$('#messageTitle').hide();
			$('#messageSubTitle').hide();
		});
		
		$('#btn-submit').click(function(){
			//$('#send-notification-form').validate(validationRules);
			//if($('#send-notification-form').valid()){
				
				var deliveryChannel = $('#deliveryChannel').val();
				var sendToGroup = $('#sendToGroup').val();
				var sendToRider = $('#sendToRider').val();
				var sendToCustomer = $('#sendToCustomer').val();
				var notificationType = $('#notificationType').val();
				var notificationSubject = $('#notificationSubject').val();
				var messageTitle = $('#message_Title').val();
				var messageSubTitle = $('#message_SubTitle').val();
				var notificationMessage = $('#notificationMessage').val();
				
				var queryString = "deliveryChannel="+deliveryChannel;
				queryString += "&sendToGroup="+sendToGroup;
				queryString += "&sendToRider="+sendToRider;
				queryString += "&sendToCustomer="+sendToCustomer;
				queryString += "&notificationType="+notificationType;
				queryString += "&notificationSubject="+notificationSubject;
				queryString += "&messageTitle="+messageTitle;
				queryString += "&messageSubTitle="+messageSubTitle;
				queryString += "&notificationMessage="+notificationMessage;
								
				$.getJSON( "sendNotifications.action", queryString, function(data){
					console.log("Query submitted successfully.");
				});
			//};
		});
	});
</script>
<script type="text/javascript">
    /*$(function() {
        // initialize sol
        $('#sendToCustomer').searchableOptionList();
        $('#sendToRider').searchableOptionList();
        $('#deliveryChannel').searchableOptionList();
    });*/
    
</script>
<script>
    $('#sendToCustomer').bootstrapDualListbox();
   
  </script>
</head>
<body>
	<div class="page-header">
		<div>
			<label>Campaign Management</label>
		</div>
	</div>
	<ol class="breadcrumb">
		<li class="breadcrumb-item">
			<a href="home.action">Dashboard</a>
		</li>
		<li class="breadcrumb-item">
			<a href="notificationAction.action">Campaign Management</a>
		</li>		
	</ol>
	
	<div class="container cont-contaner">
		<div class="row">
			<div class="col-sm-8 panel">
				<div class="panel-head">
					<label><i class="fa fa-bell"></i> Send Campaign Message</label>
				</div>
				<div class="panel-content">
					<form method="post" class="amur_forms" name="send-notification-form" id="send-notification-form">
						<div class="form-left">		
							<div class="form-group">
								<label for="deliveryChannel">Select Delivery Channel<span>*</span>: </label>
								<select class="form-control" id="deliveryChannel" name="deliveryChannel">
									<option value="EMAIL">Email</option>
									<option value="PUSH">Push Notification</option>
									<option value="SMS">SMS</option>
								</select>
							</div>
													    			    				
							<div class="form-group">
								<label for="sendToGroup">Select Recipient Group<span>*</span>: </label>
								<select class="form-control" id="sendToGroup" name="sendToGroup">
									<option value="" disabled selected>Select your option</option>
									<option value="customers">Customers</option>
									<option value="riders">Riders</option>
								</select>
							</div>	
		    			
			    			<div id="selectRiderContainer" style="display: none">		    			    				
								<div class="form-group">
									<label for="sendToRider">Select Rider<span>*</span>: </label>
									<select class="form-control" id="sendToRider" name="sendToRider">
										<option value="" disabled selected>Select Rider</option>
										<option value="ALL">All Riders</option>										
									</select>
								</div>												
			    			</div>    	
			    			
			    			<div id="selectCustomerContainer" style="display: none">		    			    				
								<div class="form-group">
									<label for="sendToCustomer">Select Customer<span>*</span>: </label>
									<select class="form-control" id="sendToCustomer" name="sendToCustomer">																			
									</select>
								</div>												
			    			</div> 
						</div>
						
						<div class="form-right">
						
							<div class="form-group">
								<label for="notificationType">Select Notification Type<span>*</span>: </label>
								<select class="form-control" id="notificationType" name="notificationType">
									<option value="" disabled selected>Select your option</option>
									<option value="promotion">Promotion</option>
									<option value="discount">Discount</option>
									<option value="other">Others</option>
								</select>
							</div>		
							
							<div class="form-group">
								<label for="notificationType">Select Notification Subject<span>*</span>: </label>
								<select class="form-control" id="notificationSubject" name="notificationSubject">
									<option value="" disabled selected>Select your option</option>
									<option value="FreshNDaily">Fresh n Daily</option>
									<option value="Airtime">Airtime</option>
									<option value="Travel">Travel</option>
									<option value="Doctor">Doctor</option>
									<option value="Merchant">Merchant</option>
								</select>
							</div>	
								
							<div id="messageTitle" style="display: none">
								<div class="form-group">
									<label for="message-title">Title: </label>
									<input class="form-control" type="text" id="message_Title" name="message_Title" placeholder="Enter Notification Title"/>
								</div>
							</div>			
							
							<div id="messageSubTitle" style="display: none">
								<div class="form-group">
									<label for="message-title">Sub Title: </label>
									<input class="form-control" type="text" id="message_SubTitle" name="message_SubTitle" placeholder="Enter Notification Subtitle"/>
								</div>
							</div>							
								
							<div class="form-group">
								<label for="sendMessage">Enter your message: </label>
								<textarea name="notificationMessage" id="notificationMessage"  placeholder="Write message here..."></textarea>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="col-sm-4 panel">
				<div class="panel-head">
					<label><i class="fa fa-history"></i> Campaigns History</label>
				</div>
				<div class="panel-content">
				
				</div>
			</div>
		</div>
		
	</div>
	
	<div class="content-panel">
    	<div class="container">
    		<div class="row">
	    		<div class="col-sm-4 col-xs-6">
	         		<input class="btn activate" id="btn-submit" type="button" value="Send Message"> 
	         		<input class="btn deactivate" id="btn-clear" type="button" value="Clear Form"> 
	   			</div>
	    	</div>
    	</div>
    </div>
			
	
</body>