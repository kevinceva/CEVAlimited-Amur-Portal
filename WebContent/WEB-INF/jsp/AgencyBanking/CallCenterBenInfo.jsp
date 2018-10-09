
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
	
	var size = '${responseJSON.bid}';
	if (size == 0)
	{	
		document.getElementById("nodata-grid").style.display="block";
	}
	else
	{	
		document.getElementById("data-grid").style.display="block";
		document.getElementById("action-grid").style.display="block";
		
	 var bsttd = '${responseJSON.bstatus}';
	 
		if( bsttd == 'BS') {
			bsttd = "<a href='#' class='label label-success' >Active</a>";
		}
		else if( bsttd == 'BV') {
			bsttd = "<a href='#' class='label label-success' >Beneficiary ID Verified</a>";
		}
		else if( bsttd == 'BC') {
			bsttd = "<a href='#' class='label label-warning' >Beneficiary ID Received</a>";
		}
		else if( bsttd == 'BR') {
			bsttd = "<a href='#' class='label label-warning' >Beneficiary Mobile No Submitted</a>";
		}
		else if( bsttd == 'BF') {
			bsttd = "<a href='#' class='label label-warning' >Beneficiary Verification Failed</a>";
			}
		else {
			bsttd = "<a href='#' class='label label-warning' >Beneficiary Verification In Progress</a>";
		}
		
		
		$("#bstatusdisp").append(bsttd);
		var srhval = '${responseJSON.srchval}';
  
		
	$('#btn-submit').live('click',function() { 
		var srhval = '${responseJSON.srchval}';
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcentersearch.action?";
		$("#form1").submit();					
	}); 
	
	}
		
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
				  <li> <a href="clientinfo.action?pid=141">Call Center</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#"> Client Information </a></li>
				</ul>
			</div>  
		 	
		 	<div class="row-fluid sortable">
           
	<div id="data-grid" class="box span12" style="display:none;">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Beneficiary Details 
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
							<td width="25%" ><strong><label for="Beneficiary ID"> Beneficiary Id</label></strong></td>
							<td width="25%" >${responseJSON.bid}  <input type="hidden" name="bid"  id="bid" value="${responseJSON.bid}" /> </td> 
							<td width="25%" ><strong><label for="Beneficiary Name"> Beneficiary Name</label></strong></td>
							<td width="25%" >${responseJSON.bname} <input type="hidden" name="bname"  id="bname" value="${responseJSON.bname}" /></td>
						</tr>
						<tr class="odd">
 							<td><strong><label for="Gender"> Gender<font color="red">*</font></label></strong></td>
 							<td width="25%" >${responseJSON.bgender} <input type="hidden" name="bgender"  id="bgender" value="${responseJSON.bgender}" /></td>
 							<td><strong><label for="Date Of Birth"> Date Of Birth<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.bdob} <input type="hidden" name="bdob"  id="bdob" value="${responseJSON.bdob}" /></td>
						</tr> 
						<tr class="odd">
 					        <td><strong><label for="ID Number"> ID Number</label></strong></td>
 					        <td width="25%" >${responseJSON.bidno}  <input type="hidden" name="bidno"  id="bidno" value="${responseJSON.bidno}" /> </td>
 					        <td><strong><label for="Mobile"> Mobile</label></strong></td>
							<td width="25%" >${responseJSON.bmob}  <input type="hidden" name="bmob"  id="bmob" value="${responseJSON.bmob}" /> </td> 
				       </tr> 
						<tr class="odd">
							<td><strong><label for="Created Date">Created Date<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.bcdate}  <input type="hidden" name="bcdate"  id="bcdate" value="${responseJSON.bcdate}" />
							<input type="hidden" name="srchval"  id="srchval" value="${responseJSON.srchval}" /> </td> 
							<td><strong><label for="Status">Status<font color="red">*</font></label></strong></td>
							<td width="25%" > <span id="bstatusdisp"></span> </td>
						</tr> 				       

				</table>
			</fieldset> 
			
              
		</div>
		</div>
		</div> 
		
	
		<div id="action-grid" class="form-actions" style="display:none;">
		   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Next"   ></input>
		  </div>  
		  
		  	<div id="nodata-grid" class="box span12" style="display:none;">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Beneficiary Details 
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
              						 
	</div><!--/#content.span10-->
		  
 </form>
</body>
</html>

