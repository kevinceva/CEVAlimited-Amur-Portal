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
	$.getJSON("fetchMerchants.action",function(data){
		var merchantData = data.responseJSON.MERCHANTS;
	    //var json = jQuery.parseJSON(merchantData);
	    var json = merchantData;
	    console.log("json["+json+"]");
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
	        "<td><id='SEARCH_NO' value='prdid@"+v.MERCHANT_ID+"' aria-controls='DataTables_Table_0'>"+v.MERCHANT_NAME+"</span> </td>"+ 
	        "<td>"+v.MERCHANT_EMAIL+"</td>"+ 
	        "<td>"+v.MERCHANT_TILL_NO+"</td>"+   
	        "<td>"+v.DATE_CREATED+"</span> </td>"+ 
	        "<td><a id='modify-offer' class='btn btn-warning' href='#' index='"+rowindex+"' title='Edit Offer' data-rel='tooltip' ><i class='icon icon-edit icon-white'></i></a> &nbsp;<a id='view-offer' class='btn btn-success' index='"+rowindex+"'  href='#' title='View Offer' data-rel='tooltip' ><i class='icon icon-book icon-white'></i></a> </td></tr>";
	            
	            $("#merchantTBody").append(appendTxt);  
	            rowindex = ++rowindex;
	            colindex = ++colindex;
	    });

	    $('#merchantsTable').DataTable();
	});
});

$(document).on('click','a',function(event) {
    var v_id=$(this).attr('id');
     console.log("value of v_id["+v_id+"]");
    if(v_id != 'SEARCH_NO') {
        var disabled_status= $(this).attr('disabled'); 
        var queryString = 'entity=${loginEntity}'; 
        var v_action = "NO";
        
        var groupId = "";  
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
            if( v_id == "merchant-creation") { 
                v_action = "createMerchant";  
            } else if (v_id ==  "view-merchant" || v_id ==  "modify-merchant") { 
                 
                 // Anchor Tag ID Should Be Equal To TR OF Index
                $(searchTdRow).each(function(indexTd) {  
                    if(indexTd == 2) {
                        groupId = $(this).text(); 
                    }
                }); 

                if(v_id ==  "view-merchant") { 
                    v_action="merchantInformation";  
                    queryString += '&type=View'; 
                } else if(v_id ==  "modify-merchant") { 
                    v_action="merchantInformation";  
                    queryString += '&type=Modify'; 
                } 
                
                queryString += '&prdid='+groupId;  
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

</script>   
</head>

<body>
<form name="form1" id="form1" method="post">

	<div class="page-header">
        <div>
            <label>Mechants Information</label>            
        </div>  
    </div>
    
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item active">Merchant Information</li>
        <li class="function-buttons">
            <div id="add_button">
            	<a href="#" id="merchant-creation" role="button" title='New Merchant Creation' class="btn btn-success">Create Merchant</a>           
            </div>
        </li>
    </ol>
       
   <div class="content-panel">            
       <table class="table table-striped table-bordered bootstrap-datatable datatable" id="merchantsTable" >
            <thead> 
                <tr>
                    <th>S No.</th>
                    <th>Merchant Name</th>
                    <th>Email</th>
                    <th>MPESA Till Number</th>
                    <th>Date Created</th>
                    <th>Actions</th>
                </tr>
            </thead> 
            <tbody id="merchantTBody">
            
            </tbody>
		</table>                
	</div>        

</form>

<form name="form2" id="form2" method="post">

</form> 
</body>
 
</html>