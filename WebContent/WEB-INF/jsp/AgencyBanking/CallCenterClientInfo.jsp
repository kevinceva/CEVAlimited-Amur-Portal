
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
	
	var size = '${responseJSON.CLN_LIST.cid}';
	console.log("valueof size:"+size+"---"+'${responseJSON.CLN_LIST.cid}');
	
	if (size == 0 || size == null )
	{		
		document.getElementById("nodata-grid").style.display="block";
		
		
		
	}
	else
	{	
		
		document.getElementById("data-grid").style.display="block";
		document.getElementById("action-grid").style.display="block";
		document.getElementById("txn-grid").style.display="block";
		
	 	var sttd = '${responseJSON.CLN_LIST.status}';
	 	var champsttd = '${responseJSON.CLN_LIST.champStatus}';
	 	console.log("champStatus:"+champsttd);
	 	if( sttd == 'Active') {
			sttd = "<a href='#' class='label label-success' >Active</a>";
		    } else {
		    	sttd = "<a href='#' class='label label-warning' >"+sttd+"</a>";
		    }
	 	
	 	if( champsttd == '1') {
	 		champsttd = "<a href='#' class='label label-success' >Yes</a>";
		    } else {
		    	champsttd = "<a href='#' class='label label-warning' >No</a>";
		    }	 	
		
		
		$("#statusdisp").append(sttd);
		$("#champstatusdisp").append(champsttd);
		
		 var bsttd = '${responseJSON.CLN_LIST.bstatus}';
			if( bsttd == 'BS') {
				bsttd = "<a href='#' class='label label-success' >Active</a>";
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
			else if( bsttd == 'BV') {
				bsttd = "<a href='#' class='label label-warning' >Beneficiary Verified</a>";
				}			
			else {
				bsttd = "<a href='#' class='label label-warning' >Beneficiary Verification In Progress</a>";
			}
			
			
			$("#bstatusdisp").append(sttd);	
			

			
			var purData ='${responseJSON.PUR_LIST}';
			console.log("purData:"+purData);
			var json = jQuery.parseJSON(purData);
			var val = 1;
			var rowindex = 1;
			var colindex = 0;
			var addclass = "";
			var psttd="";
			
			$.each(json, function(i, v) {
				if(val % 2 == 0 ) {
					addclass = "even";
					val++;
				}
				else {
					addclass = "odd";
					val++;
				}  
				var rowCount = $('#storeTBody > tr').length;
				
				colindex = ++ colindex; 
				
				psttd=v.STATUS;
				
				if( psttd.trim() == 'SUCCESS') {
					psttd = "<a href='#' class='label label-success' index='"+rowindex+"'>"+psttd+"</a>";
				    } 
				else {
					psttd = "<a href='#' class='label label-warning' index='"+rowindex+"'>"+psttd+"</a>";
				    }
				
				 
				var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
				"<td >"+colindex+"</td>"+
				/* "<td><a href='#' id='SEARCH_NO' value='CLIENTID@"+v.PURCHASE_ID+"' aria-controls='DataTables_Table_0'>"+v.PURCHASE_ID+"</span></td>"+ */
				"<td>"+v.TXN+"</span></td>"+
				"<td>"+v.PURCHASE_ID+"</span></td>"+
				"<td>"+v.PRIM_AMT+"</span></td>"+
				"<td>"+v.SUM_ASUR+"</span></td>"+
				"<td>"+v.MKRDT+"</span></td>"+
				"<td>"+psttd+"</span></td>"+
				"<td>"+v.RID+"</span></td>"+
				"<td>"+v.AID+"</span></td>";
					$("#tbody_data").append(appendTxt);	
					rowindex = ++rowindex;
					
			});				
			
  
		
	$('#btn-bendet').on('click',function() {
		/* var srhval = '${responseJSON.srchval}';
		alert("value of srchval llll:"+srhval); */
		//$("#srchval").val()=srchal;	
		//alert("srchval::::::::::"+$("#srchval").val());
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcenterbendet.action?";
		$("#form1").submit();					
	});

	$('#btn-purdet').on('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcenterstmt.action?";
		$("#form1").submit();					
	});
	
	$('#btn-compdet').on('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcenterraisecase.action?";
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
				  <li> <a href="callcenter.action?pid=145">Call Center</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#"> Client Information </a></li>
				</ul>
			</div>  
		 	
		 	<div class="row-fluid sortable">
           
	<div id="data-grid"  class="box span12" style="display:none;">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Client Details 
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
							<td width="25%" ><strong><label for="Client ID"> Client Id</label></strong></td>
							<td width="25%" >${responseJSON.CLN_LIST.cid} <input type="hidden" name="cid"  id="cid" value="${responseJSON.CLN_LIST.cid}" /> </td> 
							<td width="25%" ><strong><label for="Client Name"> Client Name</label></strong></td>
							<td width="25%" >${responseJSON.CLN_LIST.cname} <input type="hidden" name="cname"  id="cname" value="${responseJSON.CLN_LIST.cname}" /></td>
						</tr>
						<tr class="odd">
 							<td><strong><label for="Gender"> Gender</label></strong></td>
 							<td width="25%" >${responseJSON.CLN_LIST.gender} <input type="hidden" name="gender"  id="gender" value="${responseJSON.CLN_LIST.gender}" /></td>
 							<td><strong><label for="Date Of Birth"> Date Of Birth</label></strong></td>
							<td width="25%" >${responseJSON.CLN_LIST.dob} <input type="hidden" name="dob"  id="dob" value="${responseJSON.CLN_LIST.dob}" /></td>
						</tr> 
						<tr class="odd">
 							<td><strong><label for="ID Type"> ID Type</label></strong></td>
 							<td width="25%" >${responseJSON.CLN_LIST.idtype}  <input type="hidden" name="idtype"  id="idtype" value="${responseJSON.CLN_LIST.idtype}" /> </td>
 					        <td><strong><label for="ID Number"> ID Number</label></strong></td>
 					        <td width="25%" >${responseJSON.CLN_LIST.idno}  <input type="hidden" name="idno"  id="idno" value="${responseJSON.CLN_LIST.idno}" /> </td>
				       </tr> 
<%-- 					<tr class="odd">
							<td><strong><label for="Mobile"> Mobile</label></strong></td>
							<td width="25%" >${responseJSON.mob}  <input type="hidden" name="mob"  id="mob" value="${responseJSON.mob}" /> </td> 
							<td><strong><label for="Address"> Address</label></strong></td>
							<td width="25%" >${responseJSON.addr}  <input type="hidden" name="addr"  id="addr" value="${responseJSON.addr}" /> </td>
						 </tr>
						<tr class="odd">
							<td><strong><label for="Postal Code">Postal Code<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.pcode}  <input type="hidden" name="pcode"  id="pcode" value="${responseJSON.pcode}" /> </td>
							<td><strong><label for="E-Mail"> E-Mail<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.email}  <input type="hidden" name="email"  id="email" value="${responseJSON.email}" /> </td>
						</tr>   
						<tr class="odd">
							<td><strong><label for="Created Date">Created Date<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.cdate}  <input type="hidden" name="cdate"  id="cdate" value="${responseJSON.cdate}" />  
							<input type="hidden" name="srchval"  id="srchval" value="${responseJSON.srchval}" /></td> 
							<td><strong><label for="Status">Status<font color="red">*</font></label></strong></td>
							<td width="25%" > <span id="statusdisp"></span> </td>
							<%-- <td><strong><label for="Status">Status<font color="red">*</font></label></strong></td>
							<td width="25%" >${responseJSON.STATUS}  <input type="hidden" name="status"  id="status" value="${responseJSON.STATUS}" /> </td> 
						</tr> --%>
 						<tr class="odd">
							<td><strong><label for="Mobile"> Mobile</label></strong></td>
							<td width="25%" >${responseJSON.CLN_LIST.mob}  <input type="hidden" name="mob"  id="mob" value="${responseJSON.CLN_LIST.mob}" /> 
							<input type="hidden" name="srchval"  id="srchval" value="${responseJSON.CLN_LIST.srchval}" /></td> 
							<td><strong><label for="Created Date">Created Date</label></strong></td>
							<td width="25%" >${responseJSON.CLN_LIST.cdate}  <input type="hidden" name="cdate"  id="cdate" value="${responseJSON.CLN_LIST.cdate}" /> </td> 
						</tr> 
						<tr class="odd">
							<td><strong><label for="Status">Status</label></strong></td>
							<td width="25%" > <span id="statusdisp"></span> </td>
							<td><strong><label for="IPRS Desc">IPRS Status</label></strong></td>
							<td width="25%" >${responseJSON.CLN_LIST.iprs}  <input type="hidden" name="iprs"  id="iprs" value="${responseJSON.CLN_LIST.iprs}" /> </td> 
						</tr> 
 						<tr class="odd">
							<td><strong><label for="Premuim"> Cumulative Premium</label></strong></td>
							<td width="25%" >${responseJSON.CLN_LIST.cprim}  <input type="hidden" name="cprim"  id="cprim" value="${responseJSON.CLN_LIST.cprim}" /> </td> 
							<td><strong><label for="SumAssured">Cumulative Sum Assured</label></strong></td>
							<td width="25%" >${responseJSON.CLN_LIST.casrd}  <input type="hidden" name="casrd"  id="casrd" value="${responseJSON.CLN_LIST.casrd}" /> </td> 
						</tr>
						<tr class="odd">
							<td><strong><label for="ChampStatus"> Championship </label></strong></td>
							<td width="25%" > <span id="champstatusdisp"></span> </td> 
							<td></td>
							<td width="25%" > </td> 
						</tr>  						
									
				</table>
			</fieldset> 
			
              
		</div>
		</div>
		</div> 
			
	<div  class="row-fluid sortable" ><!--/span--> 
			<div  id="txn-grid" class="box span12" style="display:none;"> 
				<div class="box-header well" data-original-title>
						<i class="icon-edit"></i>Purchase Details
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					</div>
				</div> 
	 

				<div id="terminalLimitDetails">
					<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
							id="DataTables_Table_Purchase"  >
						  <thead>
								<tr >
									<th>S No</th>
									<th>Txn Type</th>
									<th>Purchase ID</th>
									<th>Txn Amount</th>
									<th>Sum Assured</th>
									<th class="center hidden-phone">Creation Date</th>
									<th>Status</th>
									<th>Mpesa Reference No</th>
									<th>Txn Reference No</th>
								</tr>
						  </thead>    
						 <tbody  id="tbody_data">
						 </tbody>
					</table> 
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
							<!-- <td width="50%" ><input type="button" class="btn btn-primary" type="text"  name="btn-purdet" id="btn-purdet" value="Statements"   ></input></td> -->
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
<!-- 		<div class="form-actions">
		   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Next"   ></input>
		  </div>  -->
              						 
	</div><!--/#content.span10-->
		  
 </form>
</body>
</html>

