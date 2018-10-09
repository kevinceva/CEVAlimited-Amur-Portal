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
    //var offerData ='${responseJSON.TRAVEL_OFFERS_JSON}';
    var orderData ='${responseJSON.NEW_ORDERS}';
    var json = jQuery.parseJSON(orderData);  
    console.log(json);
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
        
        /*if (v.status == 'A')
        	riderStatus='Approved';
		else
			riderStatus='Not Approved';*/
        
        var rowCount = $('#storeTBody > tr').length;
        
        //rowindex = ++rowindex;
        //colindex = ++ colindex; 
        
        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
        "<td >"+colindex+"</td>"+  
        "<td><id='SEARCH_NO' value='orderid@"+v.ORDER_ID+"' aria-controls='DataTables_Table_0'>"+v.ORDER_ID+"</span> </td>"+ 
        "<td style='display:none'>"+v.ORDER_ID+" </td>"+
        "<td>"+v.CUSTOMER_NAME+"</td>"+   
        "<td>"+v.MOBILE_NUMBER+"</td>"+ 
        "<td>"+v.SHIPPING_ADDRESS+"</td>"+
        "<td>"+v.TXN_DATE+"</td>"+ 
        "<td><a id='view-order' index='"+rowindex+"'  href='#' title='View Order' data-rel='tooltip' ><img class='action-button' alt='View' src='${pageContext.request.contextPath}/images/view-button.png'></a> </td></tr>";
            
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
        
        var orderId = "";  
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
            } else if (v_id ==  "view-order" || v_id ==  "modify-offer") { 
                 
                 // Anchor Tag ID Should Be Equal To TR OF Index
                $(searchTdRow).each(function(indexTd) {  
                    if(indexTd == 2) {
                    	orderId = $(this).text(); 
                    }
                }); 

                if(v_id ==  "view-order") { 
                    v_action="orderInformation";  
                    queryString += '&type=View'; 
                } else if(v_id ==  "modify-offer") { 
                    v_action="offerInformation";  
                    queryString += '&type=Modify'; 
                } 
                
                queryString += '&ordid='+orderId;  
            }
        } else {
        	alert("No id found");
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
            <label>Pending Orders</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item active">Pending Orders</li>        
    </ol>
    
    <div class="">    
        <div class="content-panel" style="overflow-x: scroll;">            
            <table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable" id="DataTables_Table_0" >
            <thead>
                <tr>
                    <th>S No.</th>
                    <th>Order ID</th>
                    <th style='display:none'></th>
                    <th>Customer Name</th>
                    <th>Mobile Number</th>
                    <th>Shipping Address</th>
                    <th>Transaction Date</th>
                    <th>Actions</th>
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
</body>
 
</html>