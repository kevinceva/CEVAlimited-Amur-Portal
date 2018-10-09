
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
 <%@taglib uri="/struts-tags" prefix="s"%> 
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 
<SCRIPT type="text/javascript"> 
var toDisp = '${type}';

$(document).ready(function(){
	<%-- var merchantData ='${responseJSON.refno}';
	console.log("merchantData ---"+merchantData);
	if (merchantData == 0 || merchantData == null )
	{		
		document.getElementById("mkr-grid").style.display="block";
		
		console.log("iffffffffffffffffffffffffffffffffffffff");
		
	}
	else
	{	
		document.getElementById("data-grid").style.display="block";
		document.getElementById("act-grid").style.display="block";
		console.log("merchantData ---"+merchantData);
		console.log("status:"+'${responseJSON.status}');
		 var refno ='${responseJSON.refno}';
		 var status ='${responseJSON.status}';
		 console.log("value of refno:"+'${responseJSON.compid}');
		 /*var json = jQuery.parseJSON(caseData);
	
		console.log("json ====="+json+"-"+'${responseJSON.MERCHANT_LIST}'); */
	
		$('#btn-submit').live('click',function() {  
			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcentercompactcnfm.action?";
			$("#form1").submit();					
		});	
	} --%>	
	
	/* var msgtxt='${responseJSON.rtxt}';
	msgtxt=msgtxt.replace("@Q","'");
	$("#rtxt").val(msgtxt);
	$("#rtxtdisp").text(msgtxt); */
	
	var retstr ='${responseJSON.retstr}';
	console.log("retstr ---"+retstr);
	
	if (retstr=="Success")
	{		
		document.getElementById("data-grid").style.display="block";
		document.getElementById("act-grid").style.display="block";
	}
	else
	{
		document.getElementById("nodata-grid").style.display="block";
	}
	$('#btn-submit').live('click',function() { 
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcenterraisesmscnfm.action?";
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
			 
		    <%-- <div> 
				<ul class="breadcrumb">
				 <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="callcentersmsdetails.action">Call Center SMS</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#"> SMS Information </a></li>
				</ul>
			</div>   --%>
		 	
		 	
			<%-- <table height="3">
			 <tr>
			    <td colspan="3">
			    	<div class="messages" id="messages"><s:actionmessage /></div>
			    	<div class="errors" id="errors"><s:actionerror /></div>
			    </td>
		    </tr>
		 </table> --%>
		 		 	
		 	<div class="row-fluid sortable">
           
	<div id="data-grid"  class="box span12" style="display:none;">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>SMS Details 
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>
						
		<div  class="box-content" >
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
						<tr class="odd" > 
							<td width="25%" ><strong><label for="Requested Date"> Requested Date</label></strong></td>
							<td width="25%" >${rdate} <input type="hidden" name="rdate"  id="rdate" value="${rdate}" /> </td> 
							<td width="25%" ><strong><label for="Request From"> Request From</label></strong></td>
							<td width="25%" >${rfrom} <input type="hidden" name="rfrom"  id="rfrom" value="${rfrom}" /></td>
						</tr>
						<tr class="odd">
 							<td><strong><label for="Request To"> Request To</label></strong></td>
 							<td width="25%" >${rto}  <input type="hidden" name="rto"  id="rto" value="${rto}" /> </td>
 							<td><strong><label for="Message"> Message</label></strong></td>
							<td width="25%" ><span id="rtxtdisp">${rtxt}</span> <input type="hidden" name="rtxt"  id="rtxt" value="${rtxt}" /></td>
						</tr> 
						<tr class="odd">
							<td width="25%"><strong><label for="Formatted Message">Reply / Edited Message</label></strong></td>
 							<td width="15%" ><input type="text"  name="ftxt"  id="ftxt" ></textarea> </td>
 							<td width="25%"><strong><label for="Remarks">Remarks</label></strong></td>
 							<td width="15%" ><textarea  name="remarks"  id="remarks" ></textarea> </td>	
				       </tr> 
				       <tr class="odd">
							<td width="25%"><strong><label for="Map To">Map To</label></strong></td>
 							<td width="15%" ><select name="mapto"  id="mapto" >
 										<option value="IDNO">ID Number</option>
 										<option value="BENMOB">Beneficiary Mobile Number</option>
 										</select> </td>
 							<td width="25%"></td>
 							<td width="15%" ></td>	
				       </tr>  
							<input type="hidden" name="acn"  id="acn" value="${acn}" /> 		
				</table>
			</fieldset> 
			
              
		</div>
		</div>
		</div>
		
		<div class="row-fluid sortable">
		<div id="mkr-grid"  class="box span12" style="display:none;">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Complaint Details 
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>
						
		<div  class="box-content" >
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
					 	<tr class="odd" > 
							<td width="25%" ><strong><font color="red"><b> Maker Cannot Be The Checker </b></font> </strong></td>
							
						</tr>
				</table>
			</fieldset> 
		</div>
		</div>		
		</div> 
			
		 	<div class="row-fluid sortable">
           
	<div id="action-grid" class="box span13" style="display:none;">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Call Center Options
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>
						
		<div  class="form-actions" >
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
						<tr class="odd" > 
							<!-- <td width="33%" ><input type="button" class="btn btn-primary" type="text"  name="btn-bendet" id="btn-bendet" value="Beneficiary Details"   ></input> </td> --> 
							<td width="50%" ><input type="button" class="btn btn-primary" type="text"  name="btn-purdet" id="btn-purdet" value="Statements"   ></input></td>
							<td width="50%" ><input type="button" class="btn btn-primary" type="text"  name="btn-compdet" id="btn-compdet" value="Raise Complaint"   ></input></td>
						</tr>

				</table>
			</fieldset> 
			
              
		</div>
		</div>

	<div id="nodata-grid" class="box span12" style="display:none;">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>SMS Details 
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>
						
		<div class="box-content" >
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >	
									<tr>
										
										<td><strong><font color="red">"${retstr}"</font></strong></td>
									
									</tr>
		
				</table>
			</fieldset> 
			
              
		</div>
		</div>									
		
		</div> 	
		<div id="act-grid" class="form-actions" style="display:none;">
		   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Confirm"   ></input>
		  </div>  
              						 
	</div><!--/#content.span10-->
		  
 </form>
</body>
</html>

