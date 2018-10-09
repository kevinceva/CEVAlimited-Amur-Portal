
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

var validationRules = {
        rules : {
        	merchantName : { required : true,  regex: /^[a-zA-Z0-9.,-]{4,}(?: [a-zA-Z0-9.,-]+){0,}$/ },
        	merchantTillNo : { required : true,  regex: /^[0-9]+$/ }        
        },
        messages : {
        	merchantName : { 
                        required : "Please enter the Merchant Name",
                        regex : "Please use letters and numbers only."
                      },
          merchantTillNo : { 
                        required : "Please enter Mpesa Till Number.",
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
        var accountNumber = $('#merchantTillNo').val();   
        var merchantEmail = $('#merchantEmail').val();
       
        $("#error_dlno").text('');     
        $("#form1").validate(validationRules); 
        if($("#form1").valid()) {
            
            $("#confirm_merchantName").val(merchantName);
            $("#confirm_merchantTillNo").val(accountNumber);
            $("#confirm_merchantEmail").val(merchantEmail);
            
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
    
    $('#btn-clear').click(function(){
    	document.getElementById("form1").reset(); 
    });
    
});


</script>
    
  
        
</head>

<body>
<div class="content-body">    
    
    <div class="page-header">
        <div>
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
      <div class="modal-dialog amur-form-modal">
        <div class="modal-content">
          <div class="modal-header">            
            <label class="modal-title">Confirm Merchant</label>
          </div>
          <div class="modal-body">
          		<form id="confirm_form" name="confirm_form" class="amur-form" method="post">
	               	<div id="amur-form-container">
			   			<ul>
			   				<li>
			   					<div class="form-group">
			   						<div id="amur-form-left">
			   							<label>Merchant Name<span>*</span> : </label>
			   						</div>
			   						<div id="amur-form-right">
			   							<input type="text" id="confirm_merchantName" name="confirm_merchantName" readonly/>
			   						</div>
			   					</div>
			   				</li>
			   				<li>
			   					<div class="form-group">
			   						<div id="amur-form-left">
			   							<label>Merchant Email<span>*</span> : </label>
			   						</div>
			   						<div id="amur-form-right">
			   							<input type="text" id="confirm_merchantEmail" name="confirm_merchantEmail" readonly/>
			   						</div>
			   					</div>
			   				</li>
			   				<li>
			   					<div class="form-group">
			   						<div id="amur-form-left">
			   							<label>Mpesa Till Number<span>*</span> : </label>
			   						</div>
			   						<div id="amur-form-right">
			   							<input type="text" id="confirm_merchantTillNo" name="confirm_merchantTillNo" readonly/>
			   						</div>
			   					</div>
			   				</li>
			   			</ul>
			   		</div>
	            </form>
          </div>
          <div class="modal-footer">            
            <button type="button" id="btn-submit" class="btn btn-success">Save Merchant</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    
    
           
	<div class="content-panel" id="user-details">       
	   <form name="form1" id="form1" class="amur-form" method="post">
	   		<div id="amur-form-container">
	   			<ul>
	   				<li>
	   					<div class="form-group">
	   						<div id="amur-form-left">
	   							<label>Merchant Name<span>*</span> : </label>
	   						</div>
	   						<div id="amur-form-right">
	   							<input type="text" id="merchantName" name="merchantName" placeholder="Merchant Name"/>
	   						</div>
	   					</div>
	   				</li>
	   				<li>
	   					<div class="form-group">
	   						<div id="amur-form-left">
	   							<label>Merchant Email<span>*</span> : </label>
	   						</div>
	   						<div id="amur-form-right">
	   							<input type="email" id="merchantEmail" name="merchantEmail" placeholder="Merchant Email"/>
	   						</div>
	   					</div>
	   				</li>
	   				<li> 
	   					<div class="form-group">
	   						<div id="amur-form-left">
	   							<label>Mpesa Till Number<span>*</span> : </label>
	   						</div>
	   						<div id="amur-form-right">
	   							<input type="text" id="merchantTillNo" name="merchantTillNo" placeholder="Merchant Till Number"/>
	   						</div>
	   					</div>
	   				</li>
	   			</ul>
	   		</div>
	   </form>
	</div>        
        <div class="content-panel form-actions">
            <input type="button" class="btn btn-success" id="btn-verify" value="Submit"/>    
            <input type="button" class="btn btn-cancel" id="btn-clear" value="Clear"/>   
        </div>      
    </div>    
    
    
</body>
</html>

