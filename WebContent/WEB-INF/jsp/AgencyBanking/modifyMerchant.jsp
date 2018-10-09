
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
    $('#merchantName').val('');
    $('#merchantCategory').val('');
    $('#merchantSubCategory').val('');
    $('#categoryOption').val('');
    $('#merchantBank').val('');
    $('#accountNumber').val('');
    
}

var validationRules = {
        rules : {
            merchantName : { required : true,  regex: /^[a-zA-Z0-9]+$/ },
            merchantCategory : { required : true },
            merchantSubCategory : { required : true },
            categoryOption : { required : true },
            merchantBank : { required : true,  regex: /^[a-zA-Z]+$/ },
            accountNumber : { required : true,  regex: /^[0-9]+$/ }        
        },
        messages : {
            merchantName : { 
                        required : "Please enter the Merchant Name",
                        regex : "Please use letters and numbers only."
                      },
            merchantCategory : { 
                        required : "Please select a Category."
                      },
            merchantSubCategory : { 
                        required : "Please select a Sub Category."
                      },
            categoryOption : { 
                        required : "Please select a Sub Category."
                      },
            merchantBank : { 
                        required : "Please enter the Merchant Bank.",
                        regex : "Please use letters only."
                      },
            accountNumber : { 
                        required : "Please enter Bank Account Number.",
                        regex : "Please use numbers only."
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
        
        var merchantName = $('#merchantName').val();
        var merchantCategory = $('#merchantCategory').val();
        var merchantSubCategory = $('#merchantSubCategory').val();      
        var categoryOption = $('#categoryOption').val();
        var merchantBank = $('#merchantBank').val();
        var accountNumber = $('#accountNumber').val();     
       
        $("#error_dlno").text('');     
        $("#form1").validate(validationRules); 
        if($("#form1").valid()) {
            
            $("#confirm_merchantName").val(merchantName);
            $("#confirm_merchantCategory").val(merchantCategory);
            $("#confirm_merchantSubCategory").val(merchantSubCategory);
            $("#confirm_categoryOption").val(categoryOption);
            $("#confirm_merchantBank").val(merchantBank);
            $("#confirm_accountNumber").val(accountNumber);
            
            $('#my_modal').modal('show');              
        }
        
    });   
    
    $('#btn-submit').on('click',function(e) {       
        $("#confirm_form")[0].action="<%=request.getContextPath()%>/<%=appName %>/confirmMerchant.action";
        $("#confirm_form").submit();
        clearVals();        
    });
     
    $('#btn-cancel').on('click',function() {  
        $("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/merchantDetails.action";
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
            <img class="header-icon" alt="Dashboard Icon" src="${pageContext.request.contextPath}/images/icon-add.png">
            <label>Create Merchant</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item">
          <a href="merchantDetails.action">Merchant Information</a>
        </li>
        <li class="breadcrumb-item active">
          <a href="createMerchant.action?pid=<%=session.getAttribute("session_refno").toString() %>">Create Merchant</a> 
        </li>
    </ol>
    
    <!-- Confirmation modal starts here -->
    
    <div class="modal fade" id="my_modal">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            
            <label class="modal-title">Confirm Merchant</label>
          </div>
          <div class="modal-body">
            <form id="confirm_form" name="confirm_form" method="post">
               <table class="form-table">              
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="confirm_merchantName">Merchant Name</label>
                              <input type="text" name="confirm_merchantName" class="form-control" id="confirm_merchantName" placeholder="Enter Merchant Name" ondrop="return false;" onpaste="return false;" readonly></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="confirm_merchantCategory">Merchant Category</label>
                              <input type="text" name="confirm_merchantCategory" class="form-control" id="confirm_merchantCategory" ondrop="return false;" onpaste="return false;" readonly></input>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td>                        
                           <div class="form-group">
                              <label for="confirm_merchantSubCategory">Merchant Sub Category</label>
                              <input type="text" name="confirm_merchantSubCategory" class="form-control" id="confirm_merchantSubCategory" ondrop="return false;" onpaste="return false;" readonly></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="confirm_categoryOption">Display Sub Category on Channels?</label>
                              <input type="text" name="confirm_categoryOption" class="form-control" id="confirm_categoryOption" ondrop="return false;" onpaste="return false;" readonly></input>
                           </div>
                       </td>
                   </tr>                   
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="confirm_merchantBank">Merchant Bank</label>
                              <input type="text" name="confirm_merchantBank" class="form-control" id="confirm_merchantBank" ondrop="return false;" onpaste="return false;" readonly></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="confirm_accountNumber">Bank Account Number</label>
                              <input type="text" name="confirm_accountNumber" class="form-control" id="confirm_accountNumber" ondrop="return false;" onpaste="return false;" readonly></input>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="confirm_location">Select Location</label>
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
    
    
    <form name="form1" id="form1" method="post">        
        <div class="content-panel" id="user-details">       
           <fieldset>
               <table class="form-table">              
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="merchantName">Merchant Name</label>
                              <input type="text" name="merchantName" class="form-control" id="merchantName" placeholder="Enter Merchant Name" ondrop="return false;" onpaste="return false;"></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="merchantCategory">Merchant Category</label>
                              <select class="form-control" id="merchantCategory" name="merchantCategory" required >
                                <option value="" disabled selected>Select your option</option>
                                <option value="Restaurant">Restaurant</option>
                                <option value="Spa">Spa & Salon</option>
                                <option value="Club">Night Life</option>
                              </select>
                            </div>
                       </td>
                   </tr>
                   <tr>
                       <td>                        
                           <div class="form-group">
                              <label for="merchantSubCategory">Merchant Sub Category</label>
                              <select class="form-control" id="merchantSubCategory" name="merchantSubCategory" required >
                                <option value="" disabled selected>Select your option</option>
                                <option value="Continental">Continental</option>
                                <option value="Swahili">Swahili</option>
                                <option value="Italian">Italian</option>
                                <option value="Japanese">Japanese</option>
                              </select>
                            </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="categoryOption">Display Sub Category on Channels?</label>
                              <select class="form-control" id="categoryOption" name="categoryOption" required >
                                <option value="" disabled selected>Select your option</option>
                                <option value="Y">Yes</option>
                                <option value="N">No</option>                                
                              </select>
                            </div>
                       </td>
                   </tr>                   
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="merchantBank">Merchant Bank</label>
                              <input type="text" name="merchantBank" class="form-control" id="merchantBank" placeholder="Enter Bank Name" ondrop="return false;" onpaste="return false;"></input>
                           </div>
                       </td>
                       <td>
                           <div class="form-group">
                              <label for="accountNumber">Bank Account Number</label>
                              <input type="text" name="accountNumber" class="form-control" id="accountNumber" placeholder="Enter Account Number" ondrop="return false;" onpaste="return false;"></input>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div class="form-group">
                              <label for="airtime_amount">Select Location</label>
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
    
    
</body>
</html>

