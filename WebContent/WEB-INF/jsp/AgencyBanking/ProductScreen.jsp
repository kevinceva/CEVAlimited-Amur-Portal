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


$(document).ready(function(){
	
		$.getJSON("fetchProducts.action", function(data){
			
			var json = data.responseJSON.DEMAND_PRODUCTS;
			//var json = jQuery.parseJSON(orderData);
		    //var json = JSON.parse(orderData);
		    
		    console.log("json[  "+json+"  ]");
		    var val = 1;
		    var rowindex = 0;
		    var colindex = 1;
		    var addclass = "";
		    var disctype="";
		    
		    
		    $.each(json, function(i, v) {
		        if(val % 2 == 0 ) {
		            addclass = "even";
		            val++;
		        }
		        else {
		            addclass = "odd";
		            val++;
		        }  
		        
		        if (v.discountType == 'F')
					disctype='Flat';
				else
					disctype='Percentage';
		        
		        var rowCount = $('#storeTBody > tr').length;
		       
		        
		        
		        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
		        "<td >"+colindex+"</td>"+  
		        "<td><id='SEARCH_NO' value='prdid@"+v.PRD_ID+"' aria-controls='DataTables_Table_0'>"+v.PRD_NAME+"</span> </td>"+ 
		        "<td style='display:none'>"+v.PRD_ID+" </td>"+
		        "<td>"+v.PRD_DESC+"</td>"+   
		        "<td>"+v.PRD_PRICE+"</span> </td>"+
		        "<td><a id='modify-product' href='#' index='"+rowindex+"' data-toggle='tooltip' data-placement='bottom' title='Edit Product' ><img class='action-button' alt='Edit' src='${pageContext.request.contextPath}/images/edit-button.png'></a> &nbsp;&nbsp;<a id='view-product' index='"+rowindex+"'  href='#' title='View Product' data-rel='tooltip' ><img class='action-button' alt='Edit' src='${pageContext.request.contextPath}/images/view-button.png'></a> </td></tr>";
				   
		            $("#demandTBody").append(appendTxt);  
		            rowindex = ++rowindex;
		            colindex = ++colindex;
		    });
		    
		    $('#DataTables_Table_0').DataTable();

		    
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
	
});

$(document).ready(function(){
	
	$.getJSON("fetchSupplyProducts.action", function(data){
		
		var json = data.responseJSON.SUPPLY_PRODUCTS;
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
	        
	        if (v.discountType == 'F')
				disctype='Flat';
			else
				disctype='Percentage';
	        
	        var rowCount = $('#storeTBody > tr').length;
	       
	        
	        
	        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
	        "<td >"+colindex+"</td>"+  
	        "<td><id='SEARCH_NO' value='prdid@"+v.PRD_ID+"' aria-controls='DataTables_Table_0'>"+v.PRD_NAME+"</span> </td>"+ 
	        "<td style='display:none'>"+v.PRD_ID+" </td>"+ 
	        "<td>"+v.PRD_PRICE+"</span> </td>"+
	        "<td><a id='modify-product' href='#' index='"+rowindex+"' data-toggle='tooltip' data-placement='bottom' title='Edit Product' ><img class='action-button' alt='Edit' src='${pageContext.request.contextPath}/images/edit-button.png'></a> &nbsp;&nbsp;<a id='view-product' index='"+rowindex+"'  href='#' title='View Product' data-rel='tooltip' ><img class='action-button' alt='Edit' src='${pageContext.request.contextPath}/images/view-button.png'></a> </td></tr>";
			   
	            $("#supplyTBody").append(appendTxt);  
	            rowindex = ++rowindex;
	            colindex = ++colindex;
	    });
	    
	    $('#DataTables_Table_1').DataTable();

	    
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

});

/*
$(document).ready(function () { 
    var orderData ='${responseJSON.PRD_JSON}';
    //var json = jQuery.parseJSON(orderData);
    var json = JSON.parse(orderData);
    
    console.log("json[  "+json+"  ]");
    var val = 1;
    var rowindex = 0;
    var colindex = 1;
    var addclass = "";
    var disctype="";
    
    
    $.each(json, function(i, v) {
        if(val % 2 == 0 ) {
            addclass = "even";
            val++;
        }
        else {
            addclass = "odd";
            val++;
        }  
        
        if (v.discountType == 'F')
			disctype='Flat';
		else
			disctype='Percentage';
        
        var rowCount = $('#storeTBody > tr').length;
       
        
        
        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
        "<td >"+colindex+"</td>"+  
        "<td><id='SEARCH_NO' value='prdid@"+v.prdId+"' aria-controls='DataTables_Table_0'>"+v.prdName.replace(/[+]/g, " ")+"</span> </td>"+ 
        "<td style='display:none'>"+v.prdId+" </td>"+
        "<td>"+v.prdDesc.replace(/[+]/g, " ")+"</td>"+   
        "<td>"+v.price.toFixed(2)+"</span> </td>"+ 
        "<td><a id='modify-product' href='#' index='"+rowindex+"' data-toggle='tooltip' data-placement='bottom' title='Edit Product' ><img class='action-button' alt='Edit' src='${pageContext.request.contextPath}/images/edit-button.png'></a> &nbsp;&nbsp;<a id='view-product' index='"+rowindex+"'  href='#' title='View Product' data-rel='tooltip' ><img class='action-button' alt='Edit' src='${pageContext.request.contextPath}/images/view-button.png'></a> </td></tr>";
		   
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
});  */

$(document).on('click','a',function(event) {
    var v_id=$(this).attr('id');
     console.log("value of v_id["+v_id+"]");
    if(v_id != 'SEARCH_NO') {
        var disabled_status= $(this).attr('disabled'); 
        var queryString = 'entity=${loginEntity}'; 
        var v_action = "NO";
        
        var prodId = "";  
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
            } else if (v_id ==  "view-product" || v_id ==  "modify-product") { 
                 
                 // Anchor Tag ID Should Be Equal To TR OF Index
                $(searchTdRow).each(function(indexTd) {  
                    if(indexTd == 2) {
                    	prodId = $(this).text(); 
                    }
                }); 

                if(v_id ==  "view-product") { 
					v_action="productInformation";  
					queryString += '&type=View'; 
				} else if(v_id ==  "modify-product") { 
					v_action="productInformation";  
					queryString += '&type=Modify'; 
				} 
				
				queryString += '&prdid='+prodId;  
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
	//$('#DataTables_Table_0, #DataTables_Table_1').DataTable({
		//"filter": true
	//});
} );


</script>  

</head>

<body>
<form name="form1" id="form1" method="post">

    <div class="page-header">
        <div>
            <label>All Products</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item active">Products</li>
        <!-- <li class="function-buttons">
	        <div class="dropdown">
			  	<button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			    Dropdown button
			  	</button>
			  	<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
				    <a class="dropdown-item" href="#">Action</a>
				    <a class="dropdown-item" href="#">Another action</a>
				    <a class="dropdown-item" href="#">Something else here</a>
			  	</div>
			</div> -->
        <li class="function-buttons">
            <div id="add_button">
                <a href="createProduct.action?pid=<%=session.getAttribute("session_refno").toString() %>" class="btn btn-success" id="product-creation" title='New Product Creation' data-rel='popover'  data-content='Creating a new product'>Create Product</a>
            </div>
        </li>
    </ol>
    
    <div class="content-panel">
    	<div class="container">
    	
    	<ul class="nav nav-tabs" id="prdTabs">
		    <li class="active"><a class="active show" data-toggle="tab" href="#home">Demand Products</a></li>
		    <li><a data-toggle="tab" href="#menu1">Supply Products</a></li>		    
	  	</ul>
	
	  	<div class="tab-content">
	  	<br/>
	    	<div id="home" class="tab-pane active">
		      	<table width='100%' class="table stripe datatable" id="DataTables_Table_0" >
	            <thead>
	                <tr style="border-bottom: 0;">
	                    <th>S No</th>
						<th>Product Name</th>
						<th style='display:none'></th>
						<th>Description</th>
						<th>Price</th>
						<th>Actions</th>
	                </tr>
	            </thead> 
	            <tbody id="demandTBody">
	            
	            </tbody>
	            </table> 
		    </div>
		    
		    <div id="menu1" class="tab-pane fade">
		      	<table width='100%' class="table stripe datatable" id="DataTables_Table_1" >
	            <thead>
	                <tr style="border-bottom: 0;">
	                    <th>S No</th>
						<th>Product Name</th>
						<th style='display:none'></th>
						<th>Price</th>
						<th>Actions</th>
	                </tr>
	            </thead> 
	            <tbody id="supplyTBody">
	            
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