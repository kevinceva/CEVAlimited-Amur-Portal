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

	function postData(actionName,paramString){
	    
	    $.getJSON(actionName, paramString,function(data){
	    	console.log("Response JSON :: "+data.responseJSON.Resp_Message);
	    	alert(data.responseJSON.Resp_Message);
	    	
	    });
	} 
	
$(document).ready(function () {	
	
	$.getJSON("fetchMerchantsOffers.action",function(data){
		
	    var offerData =data.responseJSON.MERCHANT_OFFERS;
	    //var json = jQuery.parseJSON(offerData);    
	    var json = offerData;   
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
	       
	        var decodedURL = decodeURIComponent(v.OFFER_IMAGE);
	    	var imageURL = decodedURL.replace(new RegExp(/\\/g),'/');
	    
	       //console.log("Image url :: "+imageURL);
	        
	        var rowCount = $('#storeTBody > tr').length;
	        
	        //rowindex = ++rowindex;
	        colindex = ++ colindex;
	                
	        var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
	        "<td >"+colindex+"</td>"+
	        //"<td ><img src="+imageURL+" alt='Offer Image' id='offer_image'/></td>"+  
	        "<td><id='SEARCH_NO' value='offerID@"+v.OFFER_ID+"' aria-controls='DataTables_Table_0'>"+v.OFFER_ID+"</span> </td>"+ 
	        "<td style='display:none'>"+v.OFFER_ID+" </td>"+
	        "<td>"+v.OFFER_TITLE+"</td>"+
	        "<td>"+v.OFFER_SUBTITLE+"</td>"+ 
	        "<td>"+v.OFFER_TYPE+"</td>"+
	        "<td>"+v.AMOUNT+"</td>"+ 
	        "<td>"+v.DATE_CREATED+"</td>"+
	        //"<td>"+v.OFFER_MESSAGE+"</td>"+
	        "<td><a id='view-offer' class='btn btn-success' href='#' index='"+rowindex+"' title='View Offer' data-toggle='tooltip'>View</a> &nbsp;<a id='send-alert' class='btn btn-primary' index='"+rowindex+"'  href='#' title='Send Alert' data-toggle='tooltip'>Send</a> </td></tr>";
	            
	            $("#offerTBody").append(appendTxt);  
	            rowindex = ++rowindex;
	            colindex = ++colindex;
	    });
	    $('#offerTable').DataTable();
	});  
});
	
	
	$(document).on('click','a',function(event) {
	    var v_id=$(this).attr('id');
	     console.log("value of v_id["+v_id+"]");
	    if(v_id != 'SEARCH_NO') {
	        var disabled_status= $(this).attr('disabled'); 
	        var queryString = 'entity=${loginEntity}'; 
	        var v_action = "NO";
	        
	        var offerId = "";  
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
	            if( v_id == "offer-creati") { 
	                v_action = "createOff";  
	            } else if (v_id ==  "view-offer" || v_id ==  "send-alert") { 
	                 
	                 // Anchor Tag ID Should Be Equal To TR OF Index
	                $(searchTdRow).each(function(indexTd) {  
	                    if(indexTd == 2) {
	                    	offerId = $(this).text(); 
	                    }
	                }); 

	                if(v_id ==  "view-offer") { 
	                    v_action="merchantInformation";  
	                    queryString += '&type=View'; 
	                } else if(v_id ==  "send-alert") { 
	                    v_action="sendOfferAlert";  
	                    queryString += '&type=send'; 
	                } 
	                
	                queryString += '&offerID='+offerId;  
	            }
	        } else {
	        
	            // No Rights To Access The Link 
	        }
	        console.log("queryString["+queryString+"]");
	        if(v_action != "NO") {
	             postData(v_action+".action",queryString);
	        
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

<div class="">
    <div class="page-header">
        <div>
            <label>Merchant Offers</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item active">Merchant Offers Information</li>
        <li class="function-buttons">
            <div id="add_button">    
            	<a href="createOffer.action" role="button" class="btn btn-success" id="offer-creation">Create Offer</a>           
            </div>
        </li>
    </ol>
    
    <div class="">    
        <div class="content-panel">            
            <table class="table table-striped table-bordered bootstrap-datatable datatable" id="offerTable" >
            <thead>
                <tr>
                    <th>S No.</th>
                    <!-- <th>Image</th> -->
                    <th>Offer ID</th>
                    <th style='display:none'></th>
                    <th>Title</th>
                    <th>Subtitle</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Date Created</th>
                    <!-- <th>Message</th> -->
                    <th>Actions</th>
                </tr>
            </thead> 
            <tbody id="offerTBody">
            
            </tbody>
            </table>                
        </div>
      </div>
        
</div>

</form>
<form name="form2" id="form2" method="post"></form>

</body>
</html>