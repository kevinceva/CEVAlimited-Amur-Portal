
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
<SCRIPT type="text/javascript"> 
var toDisp = '${type}';

$(document).ready(function(){
			
	var remarks = '${responseJSON.remarks}';
	console.log("remarks["+remarks+"]");

	$("#respmsg").text(remarks);
	
	$('#btn-submit').on('click',function() { 
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/manufacturers.action";
		$("#form1").submit();	 
	});

		
});
	//--> 
</SCRIPT>
    
  
		
</head>

<body>
	<form name="form1" id="form1" method="post">
	<!-- topbar ends -->
	 <div id="content" class="span10"> 
			 
		    <div> 
				<ul class="breadcrumb">
				 <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="clientinfo.action?pid=141">Manufacturer Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#"> ${type} Manufacturer </a></li>
				</ul>
			</div>  
		 	
		 	<div class="row-fluid sortable">
		 	
		 	          <div class="box span12">
                            
		<div class="box-header well" data-original-title>
		<span id="respmsg"></span>
		</div>
			
		</div>
           
	<div class="box span12">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Manufacturer Details
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>
						
		<div class="box-content">
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
						<tr class="odd" > 
							<td  align="center"><strong><label for="Manufacturer Name"> Manufacturer Name</label></strong></td>
							<td  align="left">${responseJSON.manfName}<input type="hidden"  name="manfName"  id="manfName" value="${responseJSON.manfName}" /> </td>
						</tr>
						<tr> 
							<td align="center" ><strong><label for="Manufacturer Contact"> Manufacturer Contact</label></strong></td>
							<td align="center" >${responseJSON.manfCont} <input type="hidden"  name="manfCont"  id=""manfCont"" value="${responseJSON.manfCont}" /></td>
						</tr>
						<tr class="odd">
 							<td align="center"><strong><label for="Manufacturer Second Contact"> Manufacturer Second Contact</label></strong></td>
 							<td align="center" >${responseJSON.manfSecCon}<input type="hidden"  name="manfSecCon"  id="manfSecCon" value="${responseJSON.manfSecCon}" /></td>
						</tr> 
				</table>
			
			</fieldset> 
			
              
		</div>
		</div>
		</div> 
		
		   
	<div class="form-actions">
	   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Next" width="100" ></input>
	  </div> 
		 
              						 
	</div><!--/#content.span10-->
		  
 </form>
</body>
</html>

