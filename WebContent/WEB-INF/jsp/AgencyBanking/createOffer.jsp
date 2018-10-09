
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

function clearVals(){ 
    $('#dealname').val('');
    $('#operatorname').val('');
    $('#price').val('');
    $('#country').val('');
    $('#category').val('');
    $('#availablebookings').val('');
    $('#travelitinerary').val('');
    $('#importantinformation').val('');
    $('#terms').val('');
    $('#startdate').val('');
    $('#enddate').val('');
}

var validationRules = {
        rules : {
            dealname : { required : true,  regex: /^[a-zA-Z0-9]+$/ },
            operatorname : { required : true,  regex: /^[a-zA-Z0-9]+$/ },
            price : { required : true,  regex: /^[0-9]+$/ },
            startdate : { required : true },
            enddate : { required : true },
            //startdate : { required : true,  regex: /^(?:(?:31(\/|-|\.)(?:0?[13578]|1[02]))\1|(?:(?:29|30)(\/|-|\.)(?:0?[1,3-9]|1[0-2])\2))(?:(?:1[6-9]|[2-9]\d)?\d{2})$|^(?:29(\/|-|\.)0?2\3(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\d|2[0-8])(\/|-|\.)(?:(?:0?[1-9])|(?:1[0-2]))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$/},
            //enddate : { required : true,  regex: /^(?:(?:31(\/|-|\.)(?:0?[13578]|1[02]))\1|(?:(?:29|30)(\/|-|\.)(?:0?[1,3-9]|1[0-2])\2))(?:(?:1[6-9]|[2-9]\d)?\d{2})$|^(?:29(\/|-|\.)0?2\3(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\d|2[0-8])(\/|-|\.)(?:(?:0?[1-9])|(?:1[0-2]))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$/},
            availablebookings : { required : true,  regex: /^[0-9]+$/ },
            travelitinerary : { required : true,  regex: /^[a-zA-Z0-9]+$/ },
            importantinformation : { required : true,  regex: /^[a-zA-Z0-9]+$/ },
            terms : { required : true,  regex: /^[a-zA-Z0-9]+$/ }
        
        },
        messages : {
            dealname : { 
                        required : "Please enter the Deal Name",
                        regex : "Please use letters and numbers only."
                      },
                  
            operatorname : { 
                        required : "Please enter the Operator's Name",
                        regex : "Please use letters and numbers only."
                      }, 
            startdate : {
                        required : "Please enter the Start Date."
                       },
            enddate : {
                        required : "Please enter the End Date."
                        },
            price : { 
                        required : "Please enter the Price in KES.",
                        regex : "Please use numbers only."
                      },                          
            availablebookings : { 
                        required : "Please enter number of Available Bookings.",
                        regex : "Please use numbers only."
                      },
            travelitinerary : { 
                        required : "Please enter Travel Itinerary.",
                        regex : "Please use numbers and letters only."
                      },
            importantinformation : { 
                        required : "Please enter Important Information.",
                        regex : "Please use numbers and letters only."
                      },
            terms : { 
                        required : "Please enter the Terms and Conditions.",
                        regex : "Please use numbers and letters only."
                      }
        } 

};


</script>
<script type="text/javascript">


$(document).ready(function(){
    
    $.validator.addMethod("regex", function(value, element, regexpr) {          
        return regexpr.test(value);
      }, ""); 
       
    $('#btn-verify').on('click',function(e) { 
        
        var deal_name = $('#dealname').val();
        var operator_name = $('#operatorname').val();
        var price = $('#price').val();      
        var country = $('#country').val();
        var category = $('#category').val();
        var available_bookings = $('#availablebookings').val();     
        var travel_itinerary = $('#travelitinerary').val();
        var important_info = $('#importantinformation').val();
        var terms = $('#terms').val();        
        var start_date = $('#startdate').val();
        var end_date = $('#enddate').val();
        
        
        var conf_terms = tinymce.get('terms').getContent({ format: 'text' });
        var conf_itinerary = tinymce.get('travelitinerary').getContent({ format: 'text' });
        var conf_info = tinymce.get('importantinformation').getContent({ format: 'text' });
        
        $("#error_dlno").text('');     
        $("#form1").validate(validationRules); 
        if($("#form1").valid()) {
            
            $("#confirm_dealname").val(deal_name);
            $("#confirm_operator").val(operator_name);
            $("#confirm_startdate").val(start_date);
            $("#confirm_enddate").val(end_date);
            $("#confirm_price").val(price);
            $("#confirm_country").val(country);
            $("#confirm_category").val(category);
            $("#confirm_bookings").val(available_bookings);
            $("#confirm_travelitinerary").val(conf_itinerary);
            $("#confirm_info").val(conf_info);
            $("#confirm_terms").val(conf_terms);
            
            $('#my_modal').appendTo("body").modal('show');              
        }
        
    });   
    
    $('#btn-submit').on('click',function(e) {       
        $("#confirm_form")[0].action="<%=request.getContextPath()%>/<%=appName %>/addOffer.action";
        $("#confirm_form").submit();
        clearVals();        
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
            <label>Create Offer</label>            
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
          <a href="createOfferPage.action?pid=<%=session.getAttribute("session_refno").toString() %>">Create Offer</a> 
        </li>
    </ol>
    
    <!-- Confirmation modal starts here -->
    
    
    
    
    <form name="form1" id="form1" method="post">  
      
        <div class="content-panel" id="user-details">       
           <fieldset>
               <table class="form-table">              
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Deal Name</label>
                              <input type="text" name="dealname" class="form-control" id="dealname" placeholder="Enter Deal Name" ondrop="return false;" onpaste="return false;"></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Operator Name</label>
                              <input type="text" name="operatorname" class="form-control" id="operatorname" placeholder="Enter Operator's Name" ondrop="return false;" onpaste="return false;"></input>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td>                        
                           <div class="form-group">
                              <label for="airtime_amount">Start Date</label>
                              <input type="text" name="startdate" class="form-control" id="startdate" ondrop="return false;" onpaste="return false;" placeholder="Enter Start Date"></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">End Date</label>
                              <input type="text" name="enddate" class="form-control" id="enddate" ondrop="return false;" onpaste="return false;" placeholder="Enter End Date"></input>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="country_select">Choose Country</label>
                              <select class="form-control" id="country" name="country" required >
                                <option value="" disabled selected>Select your option</option>
                                <option value="Kenya">Kenya</option>
                              </select>
                            </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Select Category</label>
                              <select class="form-control" id="category" name="category" required >
                                <option value="" disabled selected>Select your option</option>
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
                              <input type="text" name="price" class="form-control" id="price" placeholder="Enter Price in Ksh" ondrop="return false;" onpaste="return false;"><span id="select_currency"></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Maximum Available Bookings</label>
                              <input type="text" name="availablebookings" class="form-control" id="availablebookings" placeholder="Enter Number of Available Bookings" ondrop="return false;" onpaste="return false;"><span id="select_currency"></input>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Travel Itinerary</label>
                              <textarea rows=3 class="form-control tinyMCE formTextArea" name="travelitinerary" id="travelitinerary" placeholder="Enter Travel Itinerary"></textarea>
                           </div>
                       </td>
                       <td>
                            <div class="form-group">
                              <label for="airtime_amount">Important Information</label>
                              <textarea rows=3 class="form-control tinyMCE formTextArea" name="importantinformation" id="importantinformation" placeholder="Enter Important Information"></textarea>
                           </div>
                       </td>
                   </tr>
                   <tr>
                        <td>
                            <div class="form-group">
                              <label for="airtime_amount">Terms & Condition</label>
                              <textarea rows=3 class="form-control tinyMCE formTextArea" name="terms" id="terms"  placeholder="Enter Terms & Conditions"></textarea>
                           </div>                        
                        </td>
                        <td>
                        
                        </td>                        
                   </tr>                           
               </table> 
               <input type="hidden" name="type"  id="type" value="Create" />              
              </fieldset>    
        </div>
        
        <div class="content-panel form-actions">
            <input type="button" class="btn btn-success" id="btn-verify" name="addCap" value="Submit"/>  
            <input type="button" class="btn btn-cancel" id="btn-cancel" name="btn-cancel" value="Cancel"/>    
            <input type="button" class="btn btn-cancel" id="btn-clear" name="btn-clear" value="Clear"/>   
        </div>
        
        </form>
        
        
    </div>    
    
    <div class="modal fade" id="my_modal">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            
            <label class="modal-title">Confirm Offer</label>
          </div>
          <div class="modal-body">
            <form id="confirm_form" name="confirm_form" method="post">
               <table id="confirm_table">
                   <tr>
                       <td>
                           <div class="form-group">
                               <label for="airtime_amount" id="form_label">Deal Name</label>
                               <input type="text" name="confirm_dealname" class="form-control" id="confirm_dealname" value="" readonly></input>                          
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                               <label for="airtime_amount" id="form_label">Operator Name</label>
                               <input type="text" name="confirm_operator" class="form-control" id="confirm_operator" readonly></input>                      
                           </div>
                       </td>                 
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                               <label for="airtime_amount" id="form_label">Start Date</label>
                               <input type="text" name="confirm_startdate" class="form-control" id="confirm_startdate" readonly></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                               <label for="airtime_amount" id="form_label">End Date</label>
                               <input type="text" name="confirm_enddate" class="form-control" id="confirm_enddate" readonly></input>
                           </div>
                       </td>                      
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                               <label for="airtime_amount" id="form_label">Country</label>
                               <input type="text" name="confirm_country" class="form-control" id="confirm_country" readonly></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                               <label for="airtime_amount" id="form_label">Category</label>
                               <input type="text" name="confirm_category" class="form-control" id="confirm_category" readonly></input>
                           </div>
                       </td>                   
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                               <label for="airtime_amount" id="form_label">Price</label>
                               <input type="text" name="confirm_price" class="form-control" id="confirm_price" readonly></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                               <label for="airtime_amount" id="form_label">Max Bookings</label>
                               <input type="text" name="confirm_bookings" class="form-control" id="confirm_bookings" readonly></input>
                           </div>
                       </td> 
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount" id="form_label">Travel Itinerary</label>
                              <textarea rows=4 class="form-control" name="confirm_travelitinerary" id="confirm_travelitinerary" readonly></textarea>
                           </div>
                       </td>
                       <td>
                            <div class="form-group">
                              <label for="airtime_amount" id="form_label">Important Information</label>
                              <textarea rows=4 class="form-control" name="confirm_info" id="confirm_info" readonly></textarea>
                           </div>
                       </td>
                   </tr>
                   <tr>
                        <td>
                            <div class="form-group">
                              <label for="airtime_amount" id="form_label">Terms & Condition</label>
                              <textarea rows=4 class="form-control" name="confirm_terms" id="confirm_terms" readonly></textarea>
                           </div>                        
                        </td>
                        <td>
                        
                        </td>                        
                   </tr>         
               </table>
            </form>
          </div>
          <div class="modal-footer">            
            <button type="button" id="btn-submit" class="btn btn-success">Save Offer</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</body>
</html>

