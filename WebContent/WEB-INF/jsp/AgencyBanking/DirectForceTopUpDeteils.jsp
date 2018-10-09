
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
	var merchantData ='${responseJSON.rid}';
	console.log("merchantData ---"+merchantData);
	var arid = '${responseJSON.arid}';
	console.log(" value of arid:"+arid);
	if (merchantData == 0 || merchantData == null )
	{		
		document.getElementById("mkr-grid").style.display="block";		
	}
	else
	{	
		document.getElementById("data-grid").style.display="block";
		document.getElementById("act-grid").style.display="block";
/* 		console.log("merchantData ---"+merchantData);
		console.log("status:"+'${responseJSON.status}');
		 var refno ='${responseJSON.refno}';
		 var status ='${responseJSON.status}';
		 console.log("value of refno:"+'${responseJSON.compid}'); */
		 /*var json = jQuery.parseJSON(caseData);
	
		console.log("json ====="+json+"-"+'${responseJSON.MERCHANT_LIST}'); */
	
		
	}
	
	$('#btn-submit').live('click',function() { 
		
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/initiateDirectforcetopup.action?";
		$("#form1").submit();					
	});	
});

$(document).ready(function() {
    $('input:radio').change(function() {
      // alert('ole');
      var data=$("input[name='serradio']:checked").val();
      alert(data)
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
				  <li> <a href="forcetopup.action">Force TopUp</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="forcetopup.action"> Force TopUp Information </a></li>
				</ul>
			</div>  
		 	
		 	
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
			 <i class="icon-edit"></i>Force TopUp Details 
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
							<td width="25%" ><strong><label for="Policy Id"> Policy No</label></strong></td>
							<td width="25%" >${responseJSON.mob} <input type="hidden" name="mob"  id="mob" value="${responseJSON.mob}" /> </td> 
							<td width="25%" ><strong><label for="TopUp Amount"> TopUp Amount</label></strong></td>
							<td width="25%" >${responseJSON.amt} <input type="hidden" name="amt"  id="amt" value="${responseJSON.amt}" /></td>
						</tr>
						<tr class="odd">
 							<td><strong><label for="Airtime Reference No"> Airtime Reference No</label></strong></td>
							<td width="25%" >${responseJSON.cid} <input type="hidden" name="cid"  id="cid" value="${responseJSON.cid}" /></td>						
 							<td><strong><label for="Mpesa Reference No">Mpesa Reference No</label></strong></td>
 							<td width="25%" >${responseJSON.rid} <input type="hidden" name="rid"  id="rid" value="${responseJSON.rid}" /></td>
						</tr> 
						<tr class="odd">
 							<td><strong><label for="Transaction Date"> Transaction Date</label></strong></td>
 							<td width="25%" >${responseJSON.cdate}  <input type="hidden" name="cdate"  id="cdate" value="${responseJSON.cdate}" /> </td>
							<td width="25%"><strong></strong></td>
 							<td width="15%" ></textarea> </td>	
				       </tr> 								
				</table>
			</fieldset> 
			<input type="hidden" name="status"  id="status" value="${responseJSON.status}" />
			<input type="hidden" name="arid"  id="arid" value="${responseJSON.arid}" />
			<input type="hidden" name="refno"  id="refno" value="${responseJSON.refno}" />
              
		</div>
		
		<div  class="box-content" >
		<fieldset> 
			<table width="950"  border="0" cellpadding="5" cellspacing="1" 	class="table table-striped table-bordered bootstrap-datatable " >
					<tr class="odd" > 
							<td  width="25%" class="even"><input type="radio" name="serradio" checked id="serradio1" value="oldmob"/>&nbsp;&nbsp;Same Mobile Number&nbsp;&nbsp;</td>	
							<td width="25%" class="even" > <input type="radio" name="serradio" id="serradio2" value="newmob"/>&nbsp;&nbsp;New Mobile Number&nbsp; &nbsp; </td> 
							<td width="50%" ><input type="text"  name="newmob"  id="newmob" ></td>
						</tr>
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
							<td width="25%" ><strong><font color="red"><b> Raise a request at call center to proceed further </b></font> </strong></td>
							
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
			 <i class="icon-edit"></i>Client Details 
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
										
										<td><strong></strong>No Records Found</td>
									
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

