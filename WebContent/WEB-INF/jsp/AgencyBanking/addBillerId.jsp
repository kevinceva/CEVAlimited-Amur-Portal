
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 <%@taglib uri="/struts-tags" prefix="s"%> 
 <link rel="stylesheet" type="text/css" media="screen" href='${pageContext.request.contextPath}/css/jquery.cleditor.min.css' /> 
<style type="text/css">
.errors {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
input#abbreviation{text-transform:uppercase};
</style>
<SCRIPT type="text/javascript"> 

var billerrules = {
		rules : {
			billerId : {required : true },
			billerTypeName : { required : true },
			description : { required : true } ,
			bfubaccount : {required : true }
		},		
		messages : {
			billerId : { 
				required : "Please Enter Biller Id." 
			  }, 
			billerTypeName : { 
				required : "Please Select Biller Type Name." 
			  }, 
			  description : { 
					required : "Please Enter Description." 
				} , 
				bfubaccount : { 
						required : "Please Enter BFUB Account." 
					}
		},
		errorElement: 'label'
	};

$(document).ready(function(){  
	
	$("#form1").validate(billerrules); 
	$('#btn-submit').live('click',function() {  
		if($("#form1").valid()) {
 			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/addBillerIdConfirm.action";
			$("#form1").submit();	
			return true;
		} else {
			return false;
		}
						
	}); 
	
	$('#btn-Cancel').live('click',function() {  
		$("#form1").validate().cancelSubmit = true;
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/addBillerId.action";
		$("#form1").submit();					
	}); 
	
	 
	
});

//For Closing Select Box Error Message_Start
$(document).on('change','select',function(event) {  
	 if($('#'+$(this).attr('id')).next('label').text().length > 0) {
		  $('#'+$(this).attr('id')).next('label').text(''); 
		  $('#'+$(this).attr('id')).next('label').remove();
	 }
 
});
//For Closing Select Box Error Message_End



</SCRIPT>  
</head> 
<body>
	<form name="form1" id="form1" method="post" autocomplete="off">
	  <div id="content" class="span10"> 
			 
		    <div> 
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li><a href="mpesaAccManagement.action?pid=95">Mpesa A/C Management</a><span class="divider"> &gt;&gt; </span> </li>
				   <li><a href="#">Add Biller Id</a></li>
				</ul>
			</div>  
			
			<table height="3">
			 <tr>
			    <td colspan="3">
			    	<div class="messages" id="messages"><s:actionmessage /></div>
			    	<div class="errors" id="errors"><s:actionerror /></div>
			    </td>
		    </tr>
		 </table>
		 	
	<div class="row-fluid sortable"> 
		<div class="box span12">  
			<div class="box-header well" data-original-title>
				 <i class="icon-edit"></i>Basic Details  
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					
				</div>
			</div>
						
			<div class="box-content">
				<fieldset>
					 
				<table width="98%"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " > 
						 <tr> 
							<td width="20%"><label for="Biller Type Name"><strong>Biller Id<font color="red">*</font></strong></label></td>
							<td width="30%" colspan=3><input type="text" name="billerId"  id="billerId" required=true  value="<s:property value='payBillBean.billerId' />"   />   </td>		 
						</tr> 
						<tr > 
							<td><label for="Description"><strong>Biller Type Name<font color="red">*</font></strong></label></td>
							<td> <s:select cssClass="chosen-select" 
									headerKey="" 
									headerValue="Select"
									list="responseJSON.billerTypeName" 
									name="billerTypeName" 
									value="payBillBean.billerTypeName" 
									id="billerTypeName" 
									requiredLabel="true" 
									theme="simple"
									data-placeholder="Choose Biller Type Name..." 
 									 />  &nbsp; <label id="billerType-id" class="errors" ></label>
 							</td> 
							 
						</tr> 
						<tr > 
							<td><label for="Description"><strong>Description<font color="red">*</font></strong></label></td>
							<td><textarea name="description"  id="description"  required=true style="height: 69px; width: 453px;"><s:property value='payBillBean.description' /></textarea> </td> 
							 
						</tr>  
						<tr >  
							<td><label for="BFUB Account"><strong>BFUB Account<font color="red">*</font></strong></label></td>
							<td><input type="text" name="bfubaccount"  id="bfubaccount" value="<s:property value='payBillBean.bfubaccount' />" required=true  />  </td>							
						</tr> 
				</table>
			</fieldset>  
		</div>
	</div>
	</div>   
   	<div class="form-actions" >
         <input type="button" class="btn btn-success" type="text"  name="btn-submit" id="btn-submit" value="Submit" width="100" ></input>&nbsp;
         &nbsp;<input type="button" class="btn btn-info" name="btn-Cancel" id="btn-Cancel" value="Back" width="100" ></input>
       </div>  
               						 
	</div> 
 </form>
   <script type="text/javascript">
$(function(){
	
	var config = {
      '.chosen-select'           : {},
      '.chosen-select-deselect'  : {allow_single_deselect:true},
      '.chosen-select-no-single' : {disable_search_threshold:10},
      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
      '.chosen-select-width'     : {width:"95%"}
    };
	
    for (var selector in config) {
      $(selector).chosen(config[selector]);
    } 
	
	 
});
</script>
</body>
</html> 