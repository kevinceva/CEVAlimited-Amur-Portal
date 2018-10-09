
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
	
	var caseData ='${responseJSON.caseJson}';
	var clData = '${responseJSON.CLDET.CID}';
	var cases = '${responseJSON.cases}';
	//alert("  case data:"||caseData);
	/* var json = jQuery.parseJSON(caseData);
	alert("  json data:"||json); */
	
	if (cases=="117")
	{
		document.getElementById("nonref-grid").style.display="block";
	}	
	else
	{
		document.getElementById("ref-grid").style.display="block";
	}	
	
	var json = jQuery.parseJSON(caseData);
	 var options = "";
	  
	 $.each(json, function(i, v) {
	       options = $('<option/>', {value: i, text: v }).attr('data-id',i);
	     $('#cases').append(options);
	 });
		console.log("in the call action outer");
		
		$('#btn-submit').live('click',function() { 
			var srhval = '${responseJSON.srchval}';
			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcentersearch.action?";
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
				  <li> <a href="callcenter.action?pid=145">Call Center</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#"> Complaint Information </a></li>
				</ul>
			</div>  
		 	
		 	<div class="row-fluid sortable">
           
	<div id="ref-grid" class="box span12" style="display:none;">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Complaint Details 
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
							<td width="25%" ><strong><label for="Client ID"> Complaint Has Been Raised with Reference Number :   <font color="red"><b>  ${responseJSON.REFNO}</b></font> </label></strong></td>
							
						</tr>
						
				</table>
			</fieldset> 
			
              
		</div>
		</div>
			<div id="nonref-grid" class="box span12" style="display:none;">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Complaint Details 
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
							<td width="25%" ><strong><label for="Client ID"> Your Current Sum Assured Is :   <font color="red"><b>  ${responseJSON.CSA.CSA}</b></font> Reached Max Sum Assured 200,000 </label></strong></td>
							
						</tr>
						
				</table>
			</fieldset> 
			
              
		</div>
		</div>
		</div> 

<!-- 		<div class="form-actions">
		   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Next"   ></input>
		  </div>   -->
              						 
	</div><!--/#content.span10-->
		  
 </form>
</body>
</html>

