<!DOCTYPE html>
<html lang="en">
<head>

<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<script type="text/javascript" >


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
	var riderData ='${responseJSON.RIDER_ORDER_JSON}';
    
    console.log("Rider order :: "+riderData);
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
                alert("Rider Id is :: "+rideId);
                
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
		"filter": true
	});
} );

</script>   
</head>

<body>
<form name="form1" id="form1" method="post">

<div class="">
    <div class="page-header">
        <div>
            <label>Riders' Orders Details</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item">
          <a href="riderOrder.action">Riders' Orders</a>
        </li>
        <li class="breadcrumb-item active">
          <a href="#">Rider Order Details</a>
        </li>  
    </ol>
    
    <div class="">    
        <div class="content-panel">            
            <table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable" id="DataTables_Table_0" >
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
 </div>         

<input type="hidden" id="searchVal" name="searchVal" value="" />
</form>

<form name="form2" id="form2" method="post">

</form> 

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
</body>
 
</html>