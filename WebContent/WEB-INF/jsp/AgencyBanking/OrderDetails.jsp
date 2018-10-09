
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


var userLinkData ='${USER_LINKS}';
var jsonLinks = jQuery.parseJSON(userLinkData);
var linkIndex = new Array();
var linkName = new Array();
var linkStatus = new Array();

$(document).ready(function () { 
    var newOrderData ='${responseJSON.NEW_ORDERS}';
    var failedOrderData ='${responseJSON.FAILED_ORDERS}';
    var assignedOrderData ='${responseJSON.ASSIGNED_ORDERS}';
    var enrouteOrderData ='${responseJSON.ENROUTE_ORDERS}';    
    var deliveredOrderData ='${responseJSON.DELIVERED_ORDERS}';
      
    var newOrderJson = JSON.parse(newOrderData);
    var failedOrderJson = JSON.parse(failedOrderData);
    var assignedOrderJson = JSON.parse(assignedOrderData);
    var enrouteOrderJson = JSON.parse(enrouteOrderData);
    var deliveredOrderJson = JSON.parse(deliveredOrderData);
        
});  




///Failed Orders Table***********************************************************
$(document).ready(function(){
	var failedOrderData ='${responseJSON.FAILED_ORDERS}';
	var failedOrderJson = JSON.parse(failedOrderData);
	
	var val = 1;
    var rowindex = 0;
    var colindex = 1;
    var addclass = "";
    
    $.each(failedOrderJson, function(i, v) {
        if(val % 2 == 0 ) {
            addclass = "even";
            val++;
        }
        else {
            addclass = "odd";
            val++;
        }  
        
        /*if(v.STATUS != "Success"){
        	$(".rider-form").css("display", "none");
        }*/
        
        var rowCount = $('#failedOrderTbody > tr').length;
               console.log(v.ORDER_ITEMS);
        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+ 
        	"<td><a href='#' id='SEARCH_NO' value='orderId@"+v.ORDER_ID+"' aria-controls='DataTables_Table_0'>"+v.ORDER_ID+"</span></td>"+
        	"<td style='display:none'>"+v.ORDER_ID+" </td>"+
         	"<td>"+v.PAYMENT_STATUS+"</td>"+
         	"<td>"+v.TXN_AMT+"</span> </td>"+ 
         	"<td>"+v.ORDER_ITEMS+"</span></td>"+
         	"<td>"+v.TXN_DATE+"</span> </td>"+ 
         	"<td>"+v.SHIPPING_ADDRESS+"</td></tr>";
                  
         $('#failedOrderTbody').append(appendTxt);  
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
            if(ariaControlsval=="Order_Table" && contVal=="" ) {
                $("#stores").hide();
                $("#terminals").hide();
            } 
            else if(ariaControlsval=="DataTables_Table_Store" && contVal=="" ) {
                $("#terminals").hide();
            }               
        }); 
})





///New Orders Table***********************************************************
$(document).ready(function(){
	var newOrderData ='${responseJSON.NEW_ORDERS}';
	var newOrderJson = JSON.parse(newOrderData);
		
	var val = 1;
    var rowindex = 0;
    var colindex = 1;
    var addclass = "";
    
    $.each(newOrderJson, function(i, v) {
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
        "<td><a href='#' id='SEARCH_N' value='orderId@"+v.ORDER_ID+"' aria-controls='DataTables_Table_0'>"+v.ORDER_ID+"</span></td>"+
        "<td style='display:none'>"+v.ORDER_ID+" </td>"+
         "<td>"+v.PAYMENT_STATUS+"</td>"+
         "<td>"+v.TXN_AMT+"</span> </td>"+ 
         "<td>"+v.ORDER_ITEMS+"</span></td>"+
         "<td>"+v.TXN_DATE+"</span> </td>"+ 
         "<td>"+v.SHIPPING_ADDRESS+"</td></tr>";
                  
         $('#newOrderTbody').append(appendTxt);  
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
            if(ariaControlsval=="Order_Table" && contVal=="" ) {
                $("#stores").hide();
                $("#terminals").hide();
            } 
            else if(ariaControlsval=="DataTables_Table_Store" && contVal=="" ) {
                $("#terminals").hide();
            }               
        }); 
})





///Assigned Orders Table***********************************************************
$(document).ready(function(){
	var assignedOrderData ='${responseJSON.ASSIGNED_ORDERS}';
	var assignedOrderJson = JSON.parse(assignedOrderData);
		
	var val = 1;
    var rowindex = 0;
    var colindex = 1;
    var addclass = "";
    
    $.each(assignedOrderJson, function(i, v) {
        if(val % 2 == 0 ) {
            addclass = "even";
            val++;
        }
        else {
            addclass = "odd";
            val++;
        }  
        var rowCount = $('#assignedOrderTbody > tr').length;
               
        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
        "<td><a href='#' id='SEARCH_NO' value='orderId@"+v.ORDER_ID+"' aria-controls='DataTables_Table_0'>"+v.ORDER_ID+"</span></td>"+
        "<td style='display:none'>"+v.ORDER_ID+" </td>"+
         "<td>"+v.TXN_AMT+"</td>"+
         "<td>"+v.ITEMS_COUNT+"</span> </td>"+ 
         "<td>"+v.RIDER_NAME+"</span></td>"+
         "<td>"+v.TXN_DATE+"</span> </td>"+ 
         "<td>"+v.ASSIGNED_DATE+"</td></tr>";
                  
         $('#assignedOrderTbody').append(appendTxt);  
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
            if(ariaControlsval=="Order_Table" && contVal=="" ) {
                $("#stores").hide();
                $("#terminals").hide();
            } 
            else if(ariaControlsval=="DataTables_Table_Store" && contVal=="" ) {
                $("#terminals").hide();
            }               
        }); 
});



///Enroute Orders Table***********************************************************
$(document).ready(function(){
	var enrouteOrderData ='${responseJSON.ENROUTE_ORDERS}'; 
	var enrouteOrderJson = JSON.parse(enrouteOrderData);
		
	var val = 1;
    var rowindex = 0;
    var colindex = 1;
    var addclass = "";
    
    $.each(enrouteOrderJson, function(i, v) {
        if(val % 2 == 0 ) {
            addclass = "even";
            val++;
        }
        else {
            addclass = "odd";
            val++;
        }  
        var rowCount = $('#enrouteOrderTbody > tr').length;
               
        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
        "<td><a href='#' id='SEARCH_NO' value='orderId@"+v.ORDER_ID+"' aria-controls='DataTables_Table_0'>"+v.ORDER_ID+"</span></td>"+
        "<td style='display:none'>"+v.ORDER_ID+" </td>"+
         "<td>"+v.TXN_AMT+"</td>"+
         "<td>"+v.ITEMS_COUNT+"</span> </td>"+ 
         "<td>"+v.RIDER_NAME+"</span></td>"+
         "<td>"+v.TXN_DATE+"</span> </td>"+ 
         "<td>"+v.DELIVERY_COMMENCE_DATE+"</td></tr>";
                  
         $('#enrouteOrderTbody').append(appendTxt);  
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
            if(ariaControlsval=="Order_Table" && contVal=="" ) {
                $("#stores").hide();
                $("#terminals").hide();
            } 
            else if(ariaControlsval=="DataTables_Table_Store" && contVal=="" ) {
                $("#terminals").hide();
            }               
        }); 
})




///Delivered Orders Table***********************************************************
$(document).ready(function(){
	var deliveredOrderData ='${responseJSON.DELIVERED_ORDERS}';
	var deliveredOrderJson = JSON.parse(deliveredOrderData);
	
	var val = 1;
    var rowindex = 0;
    var colindex = 1;
    var addclass = "";
    
    $.each(deliveredOrderJson, function(i, v) {
        if(val % 2 == 0 ) {
            addclass = "even";
            val++;
        }
        else {
            addclass = "odd";
            val++;
        }  
        var rowCount = $('#deliveredOrderTbody > tr').length;
               
        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
        "<td><a href='#' id='SEARCH_NO' value='orderId@"+v.ORDER_ID+"' aria-controls='DataTables_Table_0'>"+v.ORDER_ID+"</span></td>"+
        "<td style='display:none'>"+v.ORDER_ID+" </td>"+
         "<td>"+v.TXN_AMT+"</td>"+
         "<td>"+v.ITEMS_COUNT+"</span> </td>"+ 
         "<td>"+v.RIDER_NAME+"</span></td>"+
         "<td>"+v.TXN_DATE+"</span> </td>"+ 
         "<td>"+v.DELIVERY_COMMENCE_DATE+"</span> </td>"+
         "<td>"+v.DELIVERED_DATE+"</td></tr>";
                  
         $('#deliveredOrderTbody').append(appendTxt);  
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
            if(ariaControlsval=="Order_Table" && contVal=="" ) {
                $("#stores").hide();
                $("#terminals").hide();
            } 
            else if(ariaControlsval=="DataTables_Table_Store" && contVal=="" ) {
                $("#terminals").hide();
            }               
        }); 
})


//for new orders
$(document).on('click','#SEARCH_N',function(event){
	$("#storeTBody").html("");
	document.getElementById('storeTBody').innerHTML = "";
	
	
	var v_id = $(this).attr('id');
	var orderId = $(this).attr('value').split("@")[1];
	
	var orderList = null;
	var customerData = null;
	var riderjson = null;
	
	var queryString = "ordid="+orderId;	
		
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
		var orderAmount = customerData[0].TXN_AMT;
		
		var shippingAddress = shippingAddress.split("#");
	
		for (var k = 0; k < shippingAddress.length; k++) {
			var address = shippingAddress[0];
			var remarks = shippingAddress[1];
			
			
		}
		
		if(remarks == "null")
		{
			remarks = "No remarks.";
		} else {
			
		}
		
		document.getElementById("orderId").innerHTML = orderId;
		document.getElementById("orderDate").innerHTML = txnDate;
		document.getElementById("orderChannel").innerHTML = channel;
		document.getElementById("customerName").innerHTML = customerName;
		document.getElementById("customerEmail").innerHTML = customerEmail;
		document.getElementById("customerMobile").innerHTML = customerMobile;
		document.getElementById("location").innerHTML = address;
		document.getElementById("orderMode").innerHTML = paymentMode;
		document.getElementById("remarks").innerHTML = remarks;
		document.getElementById("orderValue").innerHTML = orderAmount;
		
		$('#assign_order_Id').val(orderId);
				
	    var val = 1;
	    var rowindex = 0;
	    var colindex = 0;
	    var addclass = "";
	    
	    html = "";
	    for (var i = 0; i < orderList.length; i++) {
	        html+="<tr>";
	        html+="<td>"+orderList[i].PRD_NAME+"</td>";
	        html+="<td>"+orderList[i].UNIT_PRICE+"</td>";
	        html+="<td>"+orderList[i].TOTAL_PRICE+"</td>";
	        html+="<td>"+orderList[i].QUANTITY+"</td>";
	        
	        html+="</tr>";
	    }
	    html+="";
	    //$("#storeTBody").append(html);
	    document.getElementById("storeTBody").innerHTML = html;
	    
	    
	  //loading rider dropdown
		for (var m = 0; m < riderjson.length; m++)
		{
			var riderName = riderjson[m].RIDER_NAME;
			var riderops = $('<option/>', {value: riderjson[m].RIDER_ID, text: riderName}).attr('data-id',m);   		
			$('#rider').append(riderops);
		}
	    
	    $("#print-order").css("display", "block");
	    $("#assign-order").css("display", "block");
	    $("#rider-form").css("display", "block");
	    //$('#my_modal').appendTo("body").modal('show');
	    $('#my_modal').modal('show');
	    
	});	
});


//for the rest of the orders tables
$(document).on('click','#SEARCH_NO',function(event){
	
	var v_id = $(this).attr('id');
	var orderId = $(this).attr('value').split("@")[1];
	
	var orderList = null;
	var customerData = null;
	var riderjson = null;
	
	var queryString = "ordid="+orderId;
	var orderList = "";
		
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
		var orderAmount = customerData[0].TXN_AMT;
		
		var shippingAddress = shippingAddress.split("#");
		
	
		for (var k = 0; k < shippingAddress.length; k++) {
			var address = shippingAddress[0];
			var remarks = shippingAddress[1];
			
			console.log(address);
		}
		
		if(remarks == null)
		{
			remarks = "No remarks.";
		} else {
			console.log(remarks);
		}
		
		document.getElementById("orderId").innerHTML = orderId;
		document.getElementById("orderDate").innerHTML = txnDate;
		document.getElementById("orderChannel").innerHTML = channel;
		document.getElementById("customerName").innerHTML = customerName;
		document.getElementById("customerEmail").innerHTML = customerEmail;
		document.getElementById("customerMobile").innerHTML = customerMobile;
		document.getElementById("location").innerHTML = address;
		document.getElementById("orderMode").innerHTML = paymentMode;
		document.getElementById("remarks").innerHTML = remarks;
		document.getElementById("orderValue").innerHTML = orderAmount;
		
		$('#assign_order_Id').val(orderId);
				
	    var val = 1;
	    var rowindex = 0;
	    var colindex = 0;
	    var addclass = "";
	    
	    html = "";
	    for (var i = 0; i < orderList.length; i++) {
	        html+="<tr>";
	        html+="<td>"+orderList[i].PRD_NAME+"</td>";
	        html+="<td>"+orderList[i].QUANTITY+"</td>";
	        html+="<td>"+orderList[i].UNIT_PRICE+"</td>";
	        html+="<td>"+orderList[i].TOTAL_PRICE+"</td>";
	        
	        
	        html+="</tr>";
	    }
	    html+="";
	    //$("#storeTBody").html(html);
	    document.getElementById("storeTBody").innerHTML = html;
	    
	    //orderList = null;
	    
	    /*$.each(orderList, function(i, v) {
	    	if(val % 2 == 0 ) {
				addclass = "even";
				val++;
			}
			else {
				addclass = "odd";
				val++;
			}  
	    	
	    	var i= table.row.add( [
				v.PRD_NAME,
				v.UNIT_PRICE,
				v.TOTAL_PRICE,
				v.QUANTITY
	        ] ).draw( false );
	    	table.rows(i).nodes().to$().attr("id", rowindex);
	    	table.rows(i).nodes().to$().attr("index", rowindex);
	 
	        rowindex = ++rowindex;
	        colindex = ++colindex;			    	
	    	
	    });*/
	    
	  //loading rider dropdown
		for (var m = 0; m < riderjson.length; m++)
		{
			var riderName = riderjson[m].RIDER_NAME;
			var riderops = $('<option/>', {value: riderjson[m].RIDER_ID, text: riderName}).attr('data-id',m);   		
			$('#rider').append(riderops);
		}
	    
	    $("#print-order").css("display", "block");
	    $("#assign-order").css("display", "none");
		$("#rider-form").css("display", "none");
		$("#cancel-order").css("display", "none");
	    $('#my_modal').modal('show');
	    
	});	
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
	var backPath = "<%=request.getContextPath()%>/<%=appName%>/orderDetails.action";
	
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
	var backPath = "<%=request.getContextPath()%>/<%=appName%>/orderDetails.action";
	
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

$(function(){
		function printData(){
			$('#printable').printThis({
			    importCSS: false,
			    loadCSS: "${pageContext.request.contextPath}/css/my_style.css"
			});			
		}
		
		$('#print-order').on('click',function(){
			$('#assign-rider-form').validate(validationRules);
			if($('#assign-rider-form').valid()){
				$('#assign-rider-form').addClass('notPrint');
				$('.riderAssign').css({
			        'display': 'block'
			    });
				$('#rider-form').css({
			        'display': 'none'
			    });
				printData();
			}			
		})
})


$(document).ready(function(){
	var riderNumber = '${responseJSON.RIDER_INFO.riderId}';
	var queryString = "";
	$('#rider_id').val(riderNumber);
	
    $('#btn-back').on('click',function() {  
        $("#button-form")[0].action="<%=request.getContextPath()%>/<%=appName%>/riderDetails.action?pid=<%=session.getAttribute("session_refno").toString()%>";
        $("#button-form").submit();                   
    }); 
    
    $('#btn-assign').on('click',function(){
    	var selectedRec="";
    	$("input[name='order_number']:checked").each(function(){
    		selectedRec += $(this).val()+",";
    	});
    	$('#order_numbers').val(selectedRec);
    	
    	
    	var rider_number = document.getElementById('rider_id').value;
		 	var order_number = document.getElementById('order_numbers').value;
		 	
		 	$('#riderid').val(rider_number);
 		$('#orderList').val(selectedRec);
 		
 		console.log("Your order number :: "+selectedRec);
		 	
			queryString += '&orderList='+selectedRec;
			
			var table = $('#order_journey').DataTable();
			table.clear().draw();
			$.getJSON("fetchETA.action", queryString,function(data){
				var orderList = data.fullJourneyObject.JOURNEY;
				console.log(orderList);
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
						v.ORDER_NUMBER,
						v.DISTANCE,
						v.TIME
			        ] ).draw( false );
			    	table.rows(i).nodes().to$().attr("id", rowindex);
			    	table.rows(i).nodes().to$().attr("index", rowindex);
			 
			        rowindex = ++rowindex;
			        colindex = ++colindex;			    	
			    	
			    });
			    
			    $('#my_modal2').appendTo("body").modal('show');
			 	selectedRec = null;
			 	console.log("Selected records ::"+selectedRec);
			});
		 	
    });
});

$(document).ready(function(){
	$('#btn-confirm-assign').on('click',function() { 
        $("#confirm_assign_form")[0].action="<%=request.getContextPath()%>/<%=appName%>/assignOrder.action";
		$("#confirm_assign_form").submit();
	});
});

	$(document).ready(function() {
		$('#newOrderTable').DataTable();
		$('#assignedOrderTable').DataTable();
		$('#enrouteOrderTable').DataTable();
		$('#deliveredOrderTable').DataTable();
		$('#failedOrderTable').DataTable();
	});
</script>


</head>
<body>
	<form id="form1" method="post" name="form1"></form>
	<div class="">
		<div class="page-header">
			<div>
				<label>All Orders Information</label>
			</div>
		</div>
		<ol class="breadcrumb">
			<li class="breadcrumb-item"><a href="home.action">Dashboard</a>
			</li>
			<li class="breadcrumb-item"><a
				href="orderDetails.action?pid=<%=session.getAttribute("session_refno").toString()%>">All
					Orders</a></li>
		</ol>

		<div class="content-panel">
			<div class="container-fluid">
				<div class="row">

					<div class="cont">
						<div class="tab">
							<div id="tab-buttons">
								<button class="tablinks new_orders"
									onclick="openCity(event, 'new_orders')" id="defaultOpen">
									<label>New Orders</label>
								</button>
								<button class="tablinks assigned_orders"
									onclick="openCity(event, 'assigned_orders')">
									<label>Assigned Orders</label>
								</button>
								<button class="tablinks in_transit"
									onclick="openCity(event, 'in_transit')">
									<label>Enroute Orders</label>
								</button>
								<button class="tablinks delivered_orders"
									onclick="openCity(event, 'delivered_orders')">
									<label>Delivered Orders</label>
								</button>
								<button class="tablinks failed_orders"
									onclick="openCity(event, 'failed_orders')">
									<label>Failed Orders</label>
								</button>
							</div>
						</div>
					</div>

					<div class="tab-body">
						<div id="new_orders" class="tabcontent">
							<div class="">

								<fieldset>
									<table width='100%' class="table stripe datatable"
										id="newOrderTable">
										<thead>
											<tr style="border-bottom: 0;">
												<th>Order ID</th>
												<th style='display: none'></th>
												<th>Payment Status</th>
												<th>Total Amount (Ksh)</th>
												<th>Number of Items</th>
												<th>Date Requested</th>
												<th>Shipping Address</th>												
											</tr>
										</thead>
										<tbody id="newOrderTbody">

										</tbody>
									</table>
								</fieldset>								
							</div>
						</div>

						<div id="assigned_orders" class="tabcontent">
							<div class="car">
								<div class="table-responsive">
									<table width='100%'
										class="table table-striped table-bordered bootstrap-datatable datatable"
										id="assignedOrderTable">
										<thead>
											<tr style="border-bottom: 0;">
												<th>Order ID</th>
												<th style='display: none'></th>
												<th>Order Value (Ksh)</th>
												<th>Number of Items</th>
												<th>Rider Assigned</th>
												<th>Order Date</th>
												<th>Assigned Date</th>
											</tr>
										</thead>
										<tbody id="assignedOrderTbody">

										</tbody>
									</table>
								</div>
							</div>
						</div>

						<div id="in_transit" class="tabcontent">
							<div class="car">
								<div class="table-responsive">
									<table width='100%'
										class="table table-striped table-bordered bootstrap-datatable datatable"
										id="enrouteOrderTable">
										<thead>
											<tr style="border-bottom: 0;">
												<th>Order ID</th>
												<th style='display: none'></th>
												<th>Order Value (Ksh)</th>
												<th>Number of Items</th>
												<th>Rider Assigned</th>
												<th>Order Date</th>
												<th>Delivery Start Date</th>
											</tr>
										</thead>
										<tbody id="enrouteOrderTbody">

										</tbody>
									</table>
								</div>
							</div>
						</div>

						<div id="delivered_orders" class="tabcontent">
							<div class="car">
								<div class="table-responsive">
									<table width='100%'
										class="table table-striped table-bordered bootstrap-datatable datatable"
										id="deliveredOrderTable">
										<thead>
											<tr style="border-bottom: 0;">
												<th>Order ID</th>
												<th style='display: none'></th>
												<th>Order Value (Ksh)</th>
												<th>Number of Items</th>
												<th>Rider Name</th>
												<th>Order Date</th>
												<th>Delivery Start Date</th>
												<th>Delivered Date</th>
											</tr>
										</thead>
										<tbody id="deliveredOrderTbody">

										</tbody>
									</table>
								</div>
							</div>
						</div>

						<div id="failed_orders" class="tabcontent">
							<div class="car">
								<div class="table-responsive">
									<table width='100%'
										class="table table-striped table-bordered bootstrap-datatable datatable"
										id="failedOrderTable">
										<thead>
											<tr>
												<th>Order ID</th>
												<th style='display: none'></th>
												<th>Payment Status</th>
												<th>Total Amount (Ksh)</th>
												<th>Number of Items</th>
												<th>Date Requested</th>
												<th>Shipping Address</th>
											</tr>
										</thead>
										<tbody id="failedOrderTbody">
										</tbody>
									</table>
								</div>
							</div>
						</div>


					</div>

				</div>
			</div>
		</div>
		<!--div class="content-panel">
			<div class="form-actions">
				<form method="post" name="button-form" id="button-form">
					<input type="button" class="btn btn-primary" type="text"
						name="btn-back" id="btn-back" value="Back"></input>
				</form>
			</div>
		</div-->

		<script>
			function openCity(evt, cityName) {
				var i, tabcontent, tablinks;
				tabcontent = document.getElementsByClassName("tabcontent");
				for (i = 0; i < tabcontent.length; i++) {
					tabcontent[i].style.display = "none";
				}
				tablinks = document.getElementsByClassName("tablinks");
				for (i = 0; i < tablinks.length; i++) {
					tablinks[i].className = tablinks[i].className.replace(
							" active", "");
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
	<!-- /.modal -->

</body>


</html>