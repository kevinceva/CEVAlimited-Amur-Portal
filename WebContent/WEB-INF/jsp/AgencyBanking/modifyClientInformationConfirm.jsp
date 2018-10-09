
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
<%@taglib uri="/struts-tags" prefix="s"%>
 
<SCRIPT type="text/javascript">  

var toDisp = '${type}';

$(document).ready(function(){ 
	 
	/* var userStatus = '${responseJSON.CV0013}';
	var text = "";
	
	if( userStatus == 'Active')
		text = "<a href='#' class='label label-success' id='activ-deactiv-user'  >"+userStatus+"</a>";
	else if( userStatus == 'De-Active')
		text = "<a href='#'  class='label label-warning' id='activ-deactiv-user'  >"+userStatus+"</a>";
	else if( userStatus == 'InActive')
		text = "<a href='#'  class='label label-info' id='activ-deactiv-user'  >"+userStatus+"</a>";
	
	$('#spn-user-status').append(text);
   */
   
	 var sttd = "${status}";
	 	if( sttd == 'Active') {
			sttd = "<a href='#' class='label label-success' >Active</a>";
		    } else {
		    	sttd = "<a href='#' class='label label-warning' >"+sttd+"</a>";
		    }
	
	$("#statusdisp").append(sttd);
	
	 var bsttd = "${bstatus}";
	 
		if( bsttd == 'BS') {
			bsttd = "<a href='#' class='label label-success' >Active</a>";
		}
	 	if( bsttd == 'BV') {
			bsttd = "<a href='#' class='label label-warning' >Beneficiary ID Verified</a>";
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
		
	var vbid="${bid}";
	
	if (vbid.length == 0)
	{
		$("#ben-grid").hide();
	}
	else
	{
		$("#ben-grid").show();
	}	
	
   
	$('#btn-submit').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/clientModifyAck.action";
		$("#form1").submit();					
	});  
	
	$('#btn-cancel').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/clientinfo.action?pid=141";
		$("#form1").submit();					
	});
});
//--> 
</SCRIPT> 
 <style type="text/css">
.messages {
  font-weight: bold;
  color: green;
  padding: 2px 8px;
  margin-top: 2px;
}

.errors{
  font-weight: bold;
  color: red;
  padding: 2px 8px;
  margin-top: 2px;
}
label.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
.errmsg {
color: red;
}
</style>
	 
</head>

<body>
	<form name="form1" id="form1" method="post">
	<!-- topbar ends -->
	 <div id="content" class="span10"> 
		 
			
             			<!-- content starts -->
		    <div> 
				<ul class="breadcrumb">
				  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="clientinfo.action?pid=141">Client Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Modify Client Confirmation </a></li>
				</ul>
			</div>  
		 	<table height="3">
				<tr>
					<td colspan="3">
						<div class="messages" id="messages">
							<s:actionmessage />
						</div>
						<div class="errors" id="errors">
							<s:actionerror />
						</div>
					</td>
				</tr>
			</table> 
		 	<div class="row-fluid sortable">
           
	<div class="box span12">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Client Details Confirmation
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
							<td width="25%" ><strong><label for="Client ID"> Client Id</label></strong></td>
							<td width="25%" >${cid}  <input type="hidden" name="cid"  id="cid" value="${cid}" /> </td> 
							<td width="25%" ><strong><label for="Client Name"> Client Name</label></strong></td>
							<td width="25%" >${cname} <input type="hidden" name="cname"  id="cname" value="${cname}" /></td>
						</tr>
						<tr class="odd">
 							<td><strong><label for="Gender"> Gender<font color="red">*</font></label></strong></td>
 							<td width="25%" >${gender} <input type="hidden" name="gender"  id="gender" value="${gender}" /></td>
 							<td><strong><label for="Date Of Birth"> Date Of Birth<font color="red">*</font></label></strong></td>
							<td width="25%" >${dob} <input type="hidden" name="dob"  id="dob" value="${dob}" /></td>
						</tr> 
						<tr class="odd">
 							<td><strong><label for="ID Type"> ID Type</label></strong></td>
 							<td width="25%" >${idtype}  <input type="hidden" name="idtype"  id="idtype" value="${idtype}" /> </td>
 					        <td><strong><label for="ID Number"> ID Number</label></strong></td>
 					        <td width="25%" >${idno}  <input type="hidden" name="idno"  id="idno" value="${idno}" /> </td>
				       </tr> 
						<tr class="odd">
							<td><strong><label for="Mobile"> Mobile</label></strong></td>
							<td width="25%" >${mob}  <input type="hidden" name="mob"  id="mob" value="${mob}" /> </td> 
							<td><strong><label for="Address"> Address</label></strong></td>
							<td width="25%" >${addr}  <input type="hidden" name="addr"  id="addr" value="${addr}" /> </td>
						 </tr>
						<tr class="odd">
							<td><strong><label for="Postal Code">Postal Code<font color="red">*</font></label></strong></td>
							<td width="25%" >${pcode}  <input type="hidden" name="pcode"  id="pcode" value="${pcode}" /> </td>
							<td><strong><label for="E-Mail"> E-Mail<font color="red">*</font></label></strong></td>
							<td width="25%" >${email}  <input type="hidden" name="email"  id="email" value="${email}" /> </td>
						</tr>  
						<tr class="odd">
							<td><strong><label for="Created Date">Created Date<font color="red">*</font></label></strong></td>
							<td width="25%" >${cdate}  <input type="hidden" name="cdate"  id="cdate" value="${cdate}" /> </td> 
							<td><strong><label for="Status">Status<font color="red">*</font></label></strong></td>
							<td width="25%" > <span id="statusdisp"></span> </td>
						</tr> 
				</table>
				<input type="hidden" name="status"  id="status" value="${status}" /> 			
			</fieldset> 
		</div>
		</div>
		</div> 
		
	<div class="row-fluid sortable">
           
	<div id="ben-grid" class="box span12">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Beneficiary Details Confirmation
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
							<td width="25%" >${bid}  <input type="hidden" name="bid"  id="bid" value="${bid}" /> </td> 
							<td width="25%" ><strong><label for="Beneficiary Name"> Beneficiary Name</label></strong></td>
							<td width="25%" >${bname} <input type="hidden" name="bname"  id="bname" value="${bname}" /></td>
						</tr>
						<tr class="odd">
 							<td><strong><label for="BGender"> Gender<font color="red">*</font></label></strong></td>
 							<td width="25%" >${bgender} <input type="hidden" name="bgender"  id="bgender" value="${bgender}" /></td>
 							<td><strong><label for="Date Of Birth"> Date Of Birth<font color="red">*</font></label></strong></td>
							<td width="25%" >${bdob} <input type="hidden" name="bdob"  id="bdob" value="${bdob}" /></td>
						</tr> 
						<tr class="odd">
 					        <td><strong><label for="ID Number"> ID Number</label></strong></td>
 					        <td width="25%" >${bidno}  <input type="hidden" name="bidno"  id="bidno" value="${bidno}" /> </td>
 					        <td><strong><label for="Mobile"> Mobile</label></strong></td>
							<td width="25%" >${bmob}  <input type="hidden" name="bmob"  id="bmob" value="${bmob}" /> </td>  					        
				       </tr> 
<%-- 				   
							<tr class="odd">
							<td><strong><label for="Validation Count">Validation Count</label></strong></td>
							<td width="25%" >${vcnt}  <input type="hidden" name="vcnt"  id="vcnt" value="${vcnt}" /> </td>
							<td><strong><label for="Photo"> Photo<font color="red">*</font></label></strong></td>
							<td width="25%" >${photo}  <input type="hidden" name="photo"  id="photo" value="${photo}" /> </td>
						 </tr>  --%>
						<tr class="odd">
							<td><strong><label for="Created Date">Created Date<font color="red">*</font></label></strong></td>
							<td width="25%" >${bcdate}  <input type="hidden" name="bcdate"  id="bcdate" value="${bcdate}" /> </td> 
							<td><strong><label for="Status">Status<font color="red">*</font></label></strong></td>
							<td width="25%" > <span id="bstatusdisp"></span> </td>
						</tr> 
				</table>
				<input type="hidden" name="bstatus"  id="bstatus" value="${bstatus}" /> 			
			</fieldset> 
		</div>
		</div>
		</div>		
              						 
		<div class="form-actions">
		   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Confirm" width="100" ></input>
			&nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Cancel" width="100" ></input>
		 </div>
	</div><!--/#content.span10-->
		 
 </form>
</body>
</html>

