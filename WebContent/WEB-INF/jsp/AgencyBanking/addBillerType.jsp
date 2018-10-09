
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
			billerTypeName : { required : true },
			description : { required : true }  
		},		
		messages : {
			billerTypeName : { 
				required : "Please Enter Biller Type Name." 
			  }, 
			  description : { 
					required : "Please Enter Description." 
				} 
		},
		errorElement: 'label'
	};

$(document).ready(function(){   
	
 
	
	$("#form1").validate(billerrules); 
	$('#btn-submit').live('click',function() {  
		if($("#form1").valid()) {
 			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/addBillerTypeConfirm.action";
			$("#form1").submit();	
			return true;
		} else {
			return false;
		}
						
	}); 
	
	$('#btn-Cancel').live('click',function() {  
		$("#form1").validate().cancelSubmit = true;
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/mpesaAccManagement.action?pid=95";
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
				   <li><a href="#">Add Biller Type</a></li>
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
							<td width="20%"><label for="Biller Type Name"><strong>Biller Type Name<font color="red">*</font></strong></label></td>
							<td width="30%" colspan=3><input type="text" name="billerTypeName"  id="billerTypeName" required=true  value="<s:property value='payBillBean.billerTypeName' />"   />   </td>		 
						</tr> 
						<tr > 
							<td><label for="Description"><strong>Description<font color="red">*</font></strong></label></td>
							<td><textarea name="description"  id="description"   required=true style="height: 69px; width: 453px;" ><s:property value='payBillBean.description' /></textarea> </td> 
							 
						</tr> 
						<tr >  
							<td><label for="BFUB Account"><strong>BFUB Account </strong></label></td>
							<td><input type="text" name="bfubaccount"  id="bfubaccount" value="<s:property value='payBillBean.bfubaccount' />"   />  </td>							
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
  
</body>
</html> 