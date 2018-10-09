
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
	
var vbid = "${responseJSON.BEN_ID}";

if (vbid.length == 0)
{
	$('#ben-grid').hide();
}	
else
{  
	$('#ben-grid').show();	
	
}

var sttd = "${responseJSON.STATUS}";

	if( sttd == 'Active') {
	sttd = "<a href='#' class='label label-success' >"+sttd+"</a>";
    }else {
    	sttd = "<a href='#' class='label label-warning' >"+sttd+"</a>";
    }

$("#statusdisp").append(sttd);


	var bsttd = "${responseJSON.BSTATUS}";
	
	if( bsttd == 'BV') {
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
			
		$('#btn-submit').live('click',function() {  
			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/clientinfo.action?pid=141";
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
				  <li> <a href="clientinfo.action?pid=141">Client Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#"> ${type} Client </a></li>
				</ul>
			</div>  
		 	
		 	<div class="row-fluid sortable">
           
	<div class="box span12">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Client Details 
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
							<td width="25%" >${responseJSON.CLIENT_ID}  <input type="hidden" name="cid"  id="cid" value="${responseJSON.CLIENT_ID}" /> </td> 
							<td width="25%" ><strong><label for="Client Name"> Client Name</label></strong></td>
							<td width="25%" >${responseJSON.CLIENT_NAME} <input type="hidden" name="cname"  id="cname" value="${responseJSON.CLIENT_NAME}" /></td>
						</tr>
						<tr class="odd">
 							<td><strong><label for="Gender"> Gender</label></strong></td>
 							<td width="25%" >${responseJSON.GENDER} <input type="hidden" name="gender"  id="gender" value="${responseJSON.GENDER}" /></td>
 							<td><strong><label for="Date Of Birth"> Date Of Birth</label></strong></td>
							<td width="25%" >${responseJSON.DOB} <input type="hidden" name="dob"  id="dob" value="${responseJSON.DOB}" /></td>
						</tr> 
						<tr class="odd">
 							<td><strong><label for="ID Type"> ID Type</label></strong></td>
 							<td width="25%" >${responseJSON.ID_TYPE}  <input type="hidden" name="idtype"  id="idtype" value="${responseJSON.ID_TYPE}" /> </td>
 					        <td><strong><label for="ID Number"> ID Number</label></strong></td>
 					        <td width="25%" >${responseJSON.ID_NUMBER}  <input type="hidden" name="idno"  id="idno" value="${responseJSON.ID_NUMBER}" /> </td>
				       </tr> 
						<tr class="odd">
							<td><strong><label for="Mobile"> Mobile</label></strong></td>
							<td width="25%" >${responseJSON.MOB}  <input type="hidden" name="mob"  id="mob" value="${responseJSON.MOB}" /> </td> 
							<td><strong><label for="Address"> Address</label></strong></td>
							<td width="25%" >${responseJSON.ADDR}  <input type="hidden" name="addr"  id="addr" value="${responseJSON.ADDR}" /> </td>
						 </tr>
						<tr class="odd">
							<td><strong><label for="Postal Code">Postal Code</label></strong></td>
							<td width="25%" >${responseJSON.PCODE}  <input type="hidden" name="pcode"  id="pcode" value="${responseJSON.PCODE}" /> </td>
							<td><strong><label for="E-Mail"> E-Mail</label></strong></td>
							<td width="25%" >${responseJSON.EMAIL}  <input type="hidden" name="email"  id="email" value="${responseJSON.EMAIL}" /> </td>
						</tr>  
						<tr class="odd">
							<td><strong><label for="Created Date">Created Date</label></strong></td>
							<td width="25%" >${responseJSON.MKRDT}  <input type="hidden" name="cdate"  id="cdate" value="${responseJSON.MKRDT}" /> </td> 
							<td><strong><label for="Status">Status</label></strong></td>
							<td width="25%" > <span id="statusdisp"></span> </td>
							<%-- <td><strong><label for="Status">Status<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.STATUS}  <input type="hidden" name="status"  id="status" value="${responseJSON.STATUS}" /> </td> --%>
						</tr> 

				</table>
			</fieldset> 
			
              
		</div>
		</div>
		</div> 
		
		 	<div class="row-fluid sortable">
           
	<div  id="ben-grid"  class="box span13" >
                            
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
						<tr class="odd" > 
							<td width="25%" ><strong><label for="Beneficiary ID"> Beneficiary Id</label></strong></td>
							<td width="25%" >${responseJSON.BEN_ID}  <input type="hidden" name="bid"  id="bid" value="${responseJSON.BEN_ID}" /> </td> 
							<td width="25%" ><strong><label for="Beneficiary Name"> Beneficiary Name</label></strong></td>
							<td width="25%" >${responseJSON.BEN_NAME} <input type="hidden" name="bname"  id="bname" value="${responseJSON.BEN_NAME}" /></td>
						</tr>
						<tr class="odd">
 							<td><strong><label for="Gender"> Gender<font color="red">*</font></label></strong></td>
 							<td width="25%" >${responseJSON.BGENDER} <input type="hidden" name="bgender"  id="bgender" value="${responseJSON.BGENDER}" /></td>
 							<td><strong><label for="Date Of Birth"> Date Of Birth<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.BDOB} <input type="hidden" name="bdob"  id="bdob" value="${responseJSON.BDOB}" /></td>
						</tr> 
						<tr class="odd">
 					        <td><strong><label for="ID Number"> ID Number</label></strong></td>
 					        <td width="25%" >${responseJSON.BID_NUMBER}  <input type="hidden" name="bidno"  id="bidno" value="${responseJSON.BID_NUMBER}" /> </td>
 					        <td><strong><label for="Mobile"> Mobile</label></strong></td>
							<td width="25%" >${responseJSON.BMOB}  <input type="hidden" name="bmob"  id="bmob" value="${responseJSON.BMOB}" /> </td> 
				       </tr> 
					<%-- 	<tr class="odd">
							<td><strong><label for="Validation Count">Validation Count<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.VAL_CNT}  <input type="hidden" name="vcnt"  id="vcnt" value="${responseJSON.VAL_CNT}" /> </td>
							<td><strong><label for="Photo"> Photo<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.PHOTO}  <input type="hidden" name="photo"  id="photo" value="${responseJSON.PHOTO}" /> </td>
						</tr>   --%>
						<tr class="odd">
							<td><strong><label for="Created Date">Created Date<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.BMKRDT}  <input type="hidden" name="bcdate"  id="bcdate" value="${responseJSON.BMKRDT}" /> </td> 
							<td><strong><label for="Status">Status<font color="red">*</font></label></strong></td>
							<td width="25%" > <span id="bstatusdisp"></span> </td>
							<%-- <td><strong><label for="Status">Status<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.STATUS}  <input type="hidden" name="status"  id="status" value="${responseJSON.STATUS}" /> </td> --%>
						</tr> 

				</table>
			</fieldset> 
			
              
		</div>
		</div>
		</div> 		
		<div class="form-actions">
		   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Next"   ></input>
		  </div> 
              						 
	</div><!--/#content.span10-->
		  
 </form>
</body>
</html>

