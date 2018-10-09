
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
	<%@taglib uri="/struts-tags" prefix="s"%>  
	<%String ctxstr = request.getContextPath(); %>
	<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
	
	
	
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
		var riderData ='${responseJSON2.RIDER_ORDER_JSON}';
	    var json = jQuery.parseJSON(riderData);    
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
	        var rowCount = $('#storeTBody > tr').length;
	               
	        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
	        "<td >"+colindex+"</td>"+  
	        "<td><a href='#' id='SEARCH_NO' value='orderId@"+v.orderId+"' aria-controls='DataTables_Table_0'>"+v.orderId+"</span></td>"+
	        "<td style='display:none'>"+v.orderId+" </td>"+
	        "<td>"+v.txnAmt+"</td>"+   
	        "<td>"+v.customerName+"</span> </td>"+ 
	        "<td>"+v.mobileNumber+"</span></td>"+
	        "<td>"+v.shippingAddress+"</span> </td>"+ 
	        "<td>"+v.status+"</span> </td>";
	            
	            $("#Order_Table").append(appendTxt);  
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
	
	$(document).ready(function(){
		var riderStatus ="";
		
		if ('${responseJSON.RIDER_INFO.status}' == 'A'){
			riderStatus='Active';
	    	$("#btn-activate").css("display", "none");
		}else{
			riderStatus='Inactive';
			$("#btn-deactivate").css("display", "none");
		}	
	    
	});
	
	
	$(document).ready(function () {
		var riderData ='${responseJSON3.NEW_ORDERS}';
	    var json = jQuery.parseJSON(riderData);    
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
	        var rowCount = $('#storeTBody > tr').length;
	               
	        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
	        "<td >"+colindex+"</td>"+  
	        "<td><a href='#' id='SEARCH_NO' value='orderId@"+v.ORDER_ID+"' aria-controls='DataTables_Table_0'>"+v.ORDER_ID+"</span></td>"+
	        "<td style='display:none'>"+v.ORDER_ID+" </td>"+
	        "<td>"+v.TXN_AMT+"</td>"+
	        "<td>"+v.CUSTOMER_NAME+"</td>"+ 
	        "<td>"+v.MOBILE_NUMBER+"</td>"+
	        "<td>"+v.SHIPPING_ADDRESS+"</td>"+
	        "<td><input type='checkbox' name='order_number' class='table_checkbox' value="+v.ORDER_ID+"/></td>";
	            
	            $("#Order_Table2").append(appendTxt);  
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

	$(document).on('click','a',function(event) {
	    var v_id=$(this).attr('id');
	     console.log("value of v_id["+v_id+"]");
	    if(v_id != 'SEARCH_NO') {
	        var disabled_status= $(this).attr('disabled'); 
	        var queryString = 'entity=${loginEntity}'; 
	        var v_action = "NO";
	        
	        var rideId = "";  
	        var userId = "";   
	        console.log("disabled_status["+disabled_status+"]"); 
	        /* var index1 = $(this).attr('index'); */  
	        var index1 = $(this).parent().closest('tr').attr('index');
	        
	        //var index1=$(this).parent().closest('tr').index();
	        //alert(index1)
	        
	        var parentId =$(this).parent().closest('tbody').attr('id'); 
	        var searchTrRows = parentId+" tr"; 
	        //alert("searchTrRows::"+searchTrRows);
	        var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
	        //alert("searchTrRows::"+searchTdRow);       
	                 
	        if(disabled_status == undefined) {  
	            if( v_id == "product-create") { 
	                v_action = "createProduct";  
	            } else if (v_id ==  "view-orders" || v_id ==  "assign-order") { 
	                 
	                 // Anchor Tag ID Should Be Equal To TR OF Index
	                $(searchTdRow).each(function(indexTd) {  
	                    if(indexTd == 2) {
	                    	rideId = $(this).text(); 
	                    }
	                }); 

	                if(v_id ==  "view-orders") { 
	                    v_action="ridersOrderDetails";  
	                    queryString += '&type=View'; 
	                } else if(v_id ==  "assign-order") { 
	                    v_action="riderInformation";  
	                    queryString += '&type=Modify'; 
	                } 
	                
	                queryString += '&riderid='+rideId;  
	                
	            }
	        } else {
	        
	            // No Rights To Access The Link 
	        }
	        console.log("queryString["+queryString+"]");
	        if(v_action != "NO") {
	             postData(v_action+".action",queryString);
	        
	            //$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/"+v_action+".action"+queryString;
	            //$("#form1").submit();
	        }
	    } else {
	        // The below code is for quick searching.
	        var txt_sr = $(this).text();
	        var parentId =$(this).parent().closest('table').attr('id'); 
	        //console.log("The val ["+txt_sr+"]");
	        //console.log("The Attr Id ["+parentId+"]");
	        
	        $('div input[type=search]').each(function(){
	            if($(this).attr("aria-controls") == parentId) {
	                //console.log("Val ["+$(this).val()+"]"); 
	                //$(this).val('');
	                $(this).val(txt_sr);
	                $(this).trigger("keyup");
	            } /*else {
	                if(parentId !='DataTables_Table_0') {
	                    $(this).val('');
	                } 
	                    
	            }*/
	        });
	    }
	}); 

	$(document).on('click','a',function(event){
		
		var v_id = $(this).attr('id');
		var orderId = $(this).attr('value').split("@")[1];
		
		var table = $('#order_list').DataTable();
		table.clear().draw();
		
		var queryString = "orderId="+orderId;
		$.getJSON("fetchOrderList.action", queryString,function(data){
			
			var orderList = data.responseJSON.PRODUCT_LIST;
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
		$('#Order_Table').DataTable({
			"filter": true
		});
	});
	
	$(document).ready(function() {
		$('#Order_Table2').DataTable({
			"filter": true
		});
	});
		
	$(document).ready(function(){ 
		var riderNumber = '${responseJSON.RIDER_INFO.riderId}';
		var queryString = "";
		$('#rider_id').val(riderNumber);
		
        $('#btn-back').on('click',function() {  
            $("#button-form")[0].action="<%=request.getContextPath()%>/<%=appName %>/riderDetails.action?pid=<%=session.getAttribute("session_refno").toString() %>";
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
        
        $('#btn-clear-check').on('click',function(){
        	$('input:checkbox').removeAttr('checked');
        });
        
        
        
	});
	
	$(document).ready(function(){
		$('#btn-confirm-assign').on('click',function() {
            $("#confirm_assign_form")[0].action="<%=request.getContextPath()%>/<%=appName %>/assignOrder.action";
            $("#confirm_assign_form").submit();                   
        }); 
	});
	
	
	
	$(document).ready(function(){	
		var submitPath = "<%=request.getContextPath()%>/<%=appName %>/activeDeactivateRider.action";
		
		$('#btn-activate').on('click',function() { 
			swal({
	            title: "Activate Rider?",
	            text: "Press Ok to Activate Rider.", 
	            icon: "warning",
	            buttons: true,
	            dangerMode: true,
	        })
	        .then((willDelete) => {
	          if (willDelete) {
	        	  $('#rider_information').submit(function(){
					    $.post(submitPath, $(this).serialize(), function(json) {		      
					      if(json.responseJSON.remarks == "SUCCESS"){
					    	  swal({
					    		  title: "Success",
					    		  text: "Rider Activated successfully.",
					    		  icon: "success",
					    		  button: "Continue",
					    		})/*.then(function(result){
					    			  window.location = backPath;
					            });*/
					      }else {
					    	  swal({
					    		  title: "Sorry!",
					    		  text: "Rider Activation failed. Please try again later.",
					    		  icon: "error",
					    		  button: "Continue",
					    		})/*.then(function(result){
					    			  window.location = backPath;
					            });*/
					      }
					      
					    }, 'json');
					    return false;
					  });
					  $("#rider_information").submit();
	          } else {
	            swal("Request Cancelled.");
	          }
	        });  	
		}); 
		
		$('#btn-deactivate').on('click',function() { 
			swal({
	            title: "Deactivate Rider?",
	            text: "Press Ok to Deactivate Rider.", 
	            icon: "warning",
	            buttons: true,
	            dangerMode: true,
	        })
	        .then((willDelete) => {
	          if (willDelete) {
	        	  $('#rider_information').submit(function(){
					    $.post(submitPath, $(this).serialize(), function(json) {		      
					      if(json.responseJSON.remarks == "SUCCESS"){
					    	  swal({
					    		  title: "Success",
					    		  text: "Rider Deactivated successfully.",
					    		  icon: "success",
					    		  button: "Continue",
					    		})/*.then(function(result){
					    			  window.location = backPath;
					            });*/
					      }else {
					    	  swal({
					    		  title: "Sorry!",
					    		  text: "Rider Deactivation failed. Please try again later.",
					    		  icon: "error",
					    		  button: "Continue",
					    		})/*.then(function(result){
					    			  window.location = backPath;
					            });*/
					      }
					      
					    }, 'json');
					    return false;
					  });
					  $("#rider_information").submit();
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
            <label>Rider Information</label>        
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item">
          <a href="riderDetails.action?pid=<%=session.getAttribute("session_refno").toString() %>">Riders</a>
        </li>
        <li class="breadcrumb-item active">
          <a href="#">Rider's Information</a>       
        </li>
    </ol>
    
    <div class="content-panel">    
    	<div class="container-fluid">
    		<div class="row">
    			
    			<div class="cont">
		          	<div class="tab">
		            	<div id="tab-buttons">
		              		<button class="tablinks rider_general" onclick="openCity(event, 'rider_general')" id="defaultOpen"><label>Rider General</label></button>
		              		<button class="tablinks customer_orders" onclick="openCity(event, 'customer_orders')"><label>Orders History</label></button>
		              		<button class="tablinks assign_order" onclick="openCity(event, 'assign_orders')"><label>Assign Order</label></button>
		            	</div>
		          	</div>
		        </div>
		        
		        <div class="tab-body">
            	<div id="rider_general" class="tabcontent">
	              	<div class="">
		                <form id="rider_information" name="rider_information" method="post">
		                   	<fieldset> 
                			<table border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
                        		<tr class="even" > 
                            		<td width="25%" ><strong><label for="Product Name">First Name</label></strong></td>
                            		<td width="25%" >${responseJSON.RIDER_INFO.riderFirstName}  <input type="hidden" name="prdname"  id="prdname" value="${responseJSON.RIDER_INFO.riderFirstName}" /> </td> 
                            		<td width="25%" ><strong><label for="Category Name">Last Name</label></strong></td>
                           	 		<td width="25%" >${responseJSON.RIDER_INFO.riderLastName} <input type="hidden" name="catname"  id="catname" value="${responseJSON.RIDER_INFO.riderLastName}" /></td>
                        		</tr>
                        		<tr class="odd">
                            		<td><strong><label for="Sub Category Name">Mobile Number</label></strong></td>
                            		<td width="25%" >${responseJSON.RIDER_INFO.mobileNumber} <input type="hidden" name="subcatname"  id="subcatname" value="${responseJSON.RIDER_INFO.mobileNumber}" /></td>
                            		<td><strong><label for="Manufacturer Name">Email</label></strong></td>
                            		<td width="25%" >${responseJSON.RIDER_INFO.email} <input type="hidden" name="manfname"  id="manfname" value="${responseJSON.RIDER_INFO.email}" /></td>
                        		</tr> 
                        		<tr class="even" > 
                            		<td width="25%" ><strong><label for="Product Name">Rider ID Number</label></strong></td>
                            		<td width="25%" >${responseJSON.RIDER_INFO.idNumber}  <input type="hidden" name="prdname"  id="prdname" value="${responseJSON.RIDER_INFO.idNumber}" /> </td> 
                            		<td width="25%" ><strong><label for="Category Name">MPIN</label></strong></td>
                            		<td width="25%" >${responseJSON.RIDER_INFO.mpin} <input type="hidden" name="catname"  id="catname" value="${responseJSON.RIDER_INFO.mpin}" /></td>
                        		</tr>
                        		<tr class="odd">
                            		<td><strong><label for="Sub Category Name">Password</label></strong></td>
                            		<td width="25%" >${responseJSON.RIDER_INFO.password} <input type="hidden" name="subcatname"  id="subcatname" value="${responseJSON.RIDER_INFO.password}" /></td>
                            		<td><strong><label for="Manufacturer Name">Status</label></strong></td>
                            		<td width="25%" > <input type="hidden" name="manfname"  id="manfname" value="" /></td>
                        		</tr> 
                       	 			<tr class="even" > 
                            		<td width="25%" ><strong><label for="Product Name">Created By</label></strong></td>
                            		<td width="25%" >${responseJSON.RIDER_INFO.createdBy}<input type="hidden" name="prdname"  id="prdname" value="${responseJSON.RIDER_INFO.createdBy}" /> </td> 
                            		<td width="25%" ><strong><label for="Category Name">Approved By</label></strong></td>
                            		<td width="25%" >${responseJSON.RIDER_INFO.approvedBy} <input type="hidden" name="catname"  id="catname" value="${responseJSON.RIDER_INFO.approvedBy}" /></td>
                        		</tr>
                        		<tr class="odd">
                            		<td><strong><label for="Sub Category Name">Created Date</label></strong></td>
                            		<td width="25%" >${responseJSON.RIDER_INFO.createdDate} <input type="hidden" name="subcatname"  id="subcatname" value="${responseJSON.RIDER_INFO.createdDate}" /></td>
                            		<td><strong><label for="Manufacturer Name">Approved Date</label></strong></td>
                            		<td width="25%" >${responseJSON.RIDER_INFO.approvedDate} <input type="hidden" name="manfname"  id="manfname" value="${responseJSON.RIDER_INFO.approvedDate}" /></td>
                        		</tr>
                        		<input type="hidden" id="riderId" name="riderId" value="${responseJSON.RIDER_INFO.riderId}"/>
                 			</table>
            			</fieldset>		                  
		                  	<input type="button" class="btn activate" id="btn-activate" value="Activate">
            				<input type="button" class="btn deactivate" id="btn-deactivate" value="Deactivate">		      
		                </form>
	              	</div>
            	</div>

            	<div id="customer_orders" class="tabcontent">
              		<div class="car">
                		<div class="table-responsive">
	                		<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable" id="Order_Table" >
				      			<thead>
					        		<tr>
							   			<th>S No.</th>
							   			<th>Order ID</th>
							   			<th style='display:none'></th>
							   			<th>Amount </th>
							   			<th>Customer Name</th>
							   			<th>Mobile Number</th>
							   			<th>Shipping Address</th>
							  			<th>Status</th>
						   			</tr>
				       			</thead> 
				       			<tbody id="merchantTBody">
					   			</tbody>
	            			</table> 
             			</div>
             		</div>       
           		</div>
           		
           		<div id="assign_orders" class="tabcontent">
           			<div class="car">
                		<div class="table-responsive">
	                		<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable" id="Order_Table2" >
				      			<thead>
					        		<tr>
							   			<th>S No.</th>
							   			<th>Order ID</th>
							   			<th style='display:none'></th>
							   			<th>Amount </th>
							   			<th>Customer Name</th>
							   			<th>Mobile Number</th>
							   			<th>Shipping Address</th>
							   			<th>Assign</th>
						   			</tr>
				       			</thead> 
				       			<tbody id="merchantTBody">
					   			</tbody>
	            			</table>
	            			<form method="post" name="order-list-form" id="order-list-form">
	            				<input type="hidden" name="order_numbers" id="order_numbers" value=""/>
	            				<input type="hidden" name="rider_id" id="rider_id" value=""/>
	            			</form> 
	            			<a href="#" class="btn activate" id="btn-assign" role="button">Assign</a>
	            			<a href="#" class="btn deactivate" id="btn-clear-check" role="button">Clear</a>
             			</div>
             		</div>
           		</div>
           		           		
          	</div>

    		</div>
    	</div>
    </div>
    <div class="content-panel">
   		<div class="form-actions">
      		<form method="post" name="button-form" id="button-form">
       			<input type="button" class="btn btn-primary" type="text" name="btn-back" id="btn-back" value="Back"></input>
       		</form>           		
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
            		<table class='table table-striped table-bordered bootstrap-datatable datatable' id='order_list'>
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
        	</div><!-- /.modal-content -->
      	</div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    
    <div class="modal fade" id="my_modal2">
      	<div class="modal-dialog">
        	<div class="modal-content">
          		<div class="modal-header">            
            		<label class="modal-title">Orders List</label>
          		</div>
          		<div class="modal-body">
            		<table class='table table-striped table-bordered bootstrap-datatable datatable' id='order_journey'>
						<thead>
							<tr>
								<th>Order Number</th>
								<th>Distance</th>
								<th>Time of Arrival</th>
							</tr>
						</thead> 
						<tbody id="storeTBody"> 
		                </tbody>
					</table>
          		</div>
          		<form method="post" id="confirm_assign_form">
          			<input type="hidden" name="orderList" id="orderList" value=""/>
	            	<input type="hidden" name="riderid" id="riderid" value=""/>
          		</form>
          		<div class="modal-footer">
          			<button type="button" class="btn btn-confirm-assign" id="btn-confirm-assign">Confirm Assignment</button>
            		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          		</div>
        	</div><!-- /.modal-content -->
      	</div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</body>


</html>