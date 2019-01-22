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
    var riderData ='${responseJSON.PRD_JSON}';
    var json = jQuery.parseJSON(riderData);    
    var val = 1;
    var rowindex = 0;
    var colindex = 0;
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
        
        //rowindex = ++rowindex;
        colindex = ++ colindex; 
        
        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
        "<td >"+colindex+"</td>"+  
        "<td><id='SEARCH_NO' value='riderid@"+v.prdId+"' aria-controls='DataTables_Table_0'>"+v.prdName.replace(/[+]/g, " ")+"</span> </td>"+ 
        "<td style='display:none'>"+v.prdId+" </td>"+
        "<td>"+v.prdDesc.replace(/[+]/g, " ")+"</td>"+   
        "<td>"+v.price+"</span> </td>"+ 
        "<td>"+v.disctype_json+"</span></td>"+
        "<td>"+v.quantity+"</span> </td>"+ 
        "<td>"+v.createdDate+"</span> </td>"+
        "<td><a id='modify-product' class='btn btn-warning' href='#' index='"+rowindex+"'  title='Edit Product' data-toggle='tooltip' ><i class='icon icon-edit icon-white'></i></a> &nbsp;<a id='view-product' class='btn btn-success' index='"+rowindex+"'  href='#' title='View Product' data-toggle='tooltip' ><i class='icon icon-book icon-white'></i></a> </td></tr>";
            
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
            } else if (v_id ==  "view-rider" || v_id ==  "modify-rider") { 
                 
                 // Anchor Tag ID Should Be Equal To TR OF Index
                $(searchTdRow).each(function(indexTd) {  
                    if(indexTd == 2) {
                    	rideId = $(this).text(); 
                    }
                }); 

                if(v_id ==  "view-rider") { 
                    v_action="riderInformation";  
                    queryString += '&type=View'; 
                } else if(v_id ==  "modify-rider") { 
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
            <img class="header-icon" alt="Dashboard Icon" src="${pageContext.request.contextPath}/images/icon-plane.png">
            <label>Riders</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item active">Riders</li>
        <li class="function-buttons">
            <div id="add_button">
                <a href="createRiderPage.action?pid=<%=session.getAttribute("session_refno").toString() %>" class="btn btn-info" id="rider-creation" title='New Rider Creation' data-toggle='popover'  data-content='Creating a new rider'>Create Rider</a>
            </div>
        </li>
    </ol>
    
    <div class="">    
        <div class="content-panel" style="overflow-x: scroll;">            
            <table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable" id="DataTables_Table_0" >
            <thead>
                <tr>
                    <th>S No.</th>
                    <th>Product Name</th>
                    <th style='display:none'></th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Discount Type</th>
                    <th>Quantity</th>
                    <th>Date Created</th>
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