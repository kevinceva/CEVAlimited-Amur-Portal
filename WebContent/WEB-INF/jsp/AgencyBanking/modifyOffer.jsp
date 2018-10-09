
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 

<script type="text/javascript"> 

var userLinkData ='${USER_LINKS}';
var jsonLinks = jQuery.parseJSON(userLinkData);
var linkIndex = new Array();
var linkName = new Array();
var linkStatus = new Array();

var toDisp = '${type}';

function clearVals(){ 
    $('#mod_dealname').val('');
    $('#mod_operatorname').val('');
    $('#mod_price').val('');
    $('#mod_country').val('');
    $('#mod_category').val('');
    $('#mod_availablebookings').val('');
    $('#mod_travelitinerary').val('');
    $('#mod_importantinformation').val('');
    $('#mod_terms').val('');
    $('#mod_startdate').val('');
    $('#mod_enddate').val('');
};

var validationRules = {
        rules : {
            mod_dealname : { required : true,  regex: /^[a-zA-Z0-9]+$/ },
            mod_operatorname : { required : true,  regex: /^[a-zA-Z0-9]+$/ },
            mod_price : { required : true,  regex: /^[0-9]+$/ },
            mod_availablebookings : { required : true,  regex: /^[0-9]+$/ },
            mod_travelitinerary : { required : true,  regex: /^[a-zA-Z0-9]+$/ },
            mod_importantinformation : { required : true,  regex: /^[a-zA-Z0-9]+$/ },
            mod_terms : { required : true,  regex: /^[a-zA-Z0-9]+$/ }
        
        },
        messages : {
            mod_dealname : { 
                        required : "Please enter the Deal Name",
                        regex : "Please use letters and numbers only."
                      },
                  
                      mod_operatorname : { 
                        required : "Please enter the Operator's Name",
                        regex : "Please use letters and numbers only."
                      },          
                      mod_price : { 
                        required : "Please enter the Price in KES.",
                        regex : "Please use numbers only."
                      },                          
                      mod_availablebookings : { 
                        required : "Please enter number of Available Bookings.",
                        regex : "Please use numbers only."
                      },
                      mod_travelitinerary : { 
                        required : "Please enter Travel Itinerary.",
                        regex : "Please use numbers and letters only."
                      },
                      mod_importantinformation : { 
                        required : "Please enter Important Information.",
                        regex : "Please use numbers and letters only."
                      },
                      mod_terms : { 
                        required : "Please enter the Terms and Conditions.",
                        regex : "Please use numbers and letters only."
                      }
        } 

};


$(document).ready(function(){
    
    $.validator.addMethod("regex", function(value, element, regexpr) {          
        return regexpr.test(value);
      }, ""); 
    
    
    $('#mod_dealname').val('${responseJSON.TRAVEL_OFFER_INFO.dealName}');
    $('#mod_operatorname').val('${responseJSON.TRAVEL_OFFER_INFO.operatorName}');
    $('#mod_price').val('${responseJSON.TRAVEL_OFFER_INFO.amount}');
    $('#mod_country').val('${responseJSON.TRAVEL_OFFER_INFO.dealCountry}');
    $('#mod_category').val('${responseJSON.TRAVEL_OFFER_INFO.offerType}');
    $('#mod_availablebookings').val('${responseJSON.TRAVEL_OFFER_INFO.maxBookings}');
    $('#mod_travelitinerary').val('${responseJSON.TRAVEL_OFFER_INFO.travelItinerary}');
    $('#mod_importantinformation').val('${responseJSON.TRAVEL_OFFER_INFO.importantInfo}');
    $('#mod_terms').val('${responseJSON.TRAVEL_OFFER_INFO.terms}');
    $('#startdate').val('${responseJSON.TRAVEL_OFFER_INFO.startDate}');
    $('#enddate').val('${responseJSON.TRAVEL_OFFER_INFO.endDate}');
    $('#offerId').val('${responseJSON.TRAVEL_OFFER_INFO.travelOfferId}');
    
    $('#btn-submit').on('click',function() {
        $("#error_dlno").text(''); 
        $("#form1").validate(validationRules);
        if($("#form1").valid()) {       
            $("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/offerModifyConfirm.action";
            $("#form1").submit();   
        } else {
            return false;
        }
    }); 
     
    $('#btn-cancel').on('click',function() {  
        $("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/specialOffers.action";
        $("#form1").submit();                   
    }); 
    
    $('#btn-clear').on('click',function(){
        clearVals();
    });
    
});


</script>
    
  
        
</head>

<body>
<div class="content-body">    
    
    <div class="page-header">
        <div>
            <img class="header-icon" alt="Dashboard Icon" src="${pageContext.request.contextPath}/images/editbutton.png">
            <label>Edit Offer</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item">
          <a href="specialOffers.action">Travel Offers</a>
        </li>
        <li class="breadcrumb-item active">
          <a href="#">Edit Offer</a>        
        </li>
    </ol>
    
    
    <form name="form1" id="form1" method="post">        
        <div class="content-panel" id="user-details">       
           <fieldset>
               <table class="form-table">              
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Deal Name</label>
                              <input type="text" name="mod_dealname" class="form-control" id="mod_dealname" placeholder="Enter Deal Name" ondrop="return false;" onpaste="return false;"></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Operator Name</label>
                              <input type="text" name="mod_operatorname" class="form-control" id="mod_operatorname" placeholder="Enter Operator's Name" ondrop="return false;" onpaste="return false;"></input>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Start Date</label>
                              <input type="text" name="mod_startdate" class="form-control" id="startdate" ondrop="return false;" onpaste="return false;" placeholder="Enter Start Date"></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">End Date</label>
                              <input type="text" name="mod_enddate" class="form-control" id="enddate" ondrop="return false;" onpaste="return false;" placeholder="Enter End Date"></input>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="country_select">Choose Country</label>
                              <select class="form-control" id="mod_country" name="mod_country">
                                <option value="${responseJSON.TRAVEL_OFFER_INFO.dealCountry}" selected>${responseJSON.TRAVEL_OFFER_INFO.dealCountry}</option>
                                <option value="Kenya">Kenya</option>
                              </select>
                            </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Select Category</label>
                              <select class="form-control" id="mod_category" name="mod_category">
                                <option value="${responseJSON.TRAVEL_OFFER_INFO.offerType}" selected>${responseJSON.TRAVEL_OFFER_INFO.offerType}</option>
                                <option value="F">Flat</option>
                                <option value="P">Percentage</option>
                              </select>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Price(KES)</label>
                              <input type="text" name="mod_price" class="form-control" id="mod_price" placeholder="Enter Price in Ksh" ondrop="return false;" onpaste="return false;"></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Maximum Available Bookings</label>
                              <input type="text" name="mod_availablebookings" class="form-control" id="mod_availablebookings" placeholder="Enter Number of Available Bookings" ondrop="return false;" onpaste="return false;"></input>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Travel Itinerary</label>
                              <textarea rows=3 class="form-control" name="mod_travelitinerary" placeholder="Enter Travel Itinerary" id="mod_travelitinerary"></textarea>
                           </div>
                       </td>
                       <td>
                            <div class="form-group">
                              <label for="airtime_amount">Important Information</label>
                              <textarea rows=3 class="form-control" name="mod_importantinformation" placeholder="Enter Important Information" id="mod_importantinformation"></textarea>
                           </div>
                       </td>
                   </tr>
                   <tr>
                        <td>
                            <div class="form-group">
                              <label for="airtime_amount">Terms & Condition</label>
                              <textarea rows=3 class="form-control" name="mod_terms" id="mod_terms" placeholder="Enter Terms & Conditions"></textarea>
                           </div>                        
                        </td>
                        <td>
                        
                        </td>                        
                   </tr>                           
               </table> 
               <input type="hidden" name="offerId"  id="offerId" value="" />
               <input type="hidden" name="type"  id="type" value="Modify" />              
              </fieldset>    
        </div>
        
        <div class="content-panel form-actions">
            <input type="button" class="btn btn-success" id="btn-submit" name="modCap" value="Update"/>  
            <input type="button" class="btn btn-cancel" id="btn-cancel" name="btn-cancel" value="Cancel"/>    
            <input type="button" class="btn btn-cancel" id="btn-clear" name="btn-clear" value="Clear"/>   
        </div>
        
        </form>
        
        
    </div>
    
</body>
</html>

