<%@ page import="java.util.*"%><!DOCTYPE html>

<html>
<head>
<script type="text/javascript">
 var path = '${pageContext.request.contextPath}';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="http://cdn.datatables.net/1.10.3/js/jquery.dataTables.min.js"></script>
<script src="http://datatables.net/release-datatables/extensions/ColVis/js/dataTables.colVis.js"></script> -->

<script src="${pageContext.request.contextPath}/pagenationjs/jquery-1.12.2.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/dataTables.colVis.js"></script>


<title>Person Form</title>

<script type="text/javascript">


var groupId='<%= session.getAttribute("userGroup") %>';

var table;

jQuery(document).ready(function() {
	
	if(groupId=="CICMAKERS" || groupId=="CICCHECKER" || groupId=="EXTCCMAKER" ){
		$("#content").hide();
		$("#hide-dash").show();
	}else
	{
		//$("#hidedash").hide();
		$("#content").show();
		$("#hide-dash").hide();
	}	
	
	 table = $('#personTable').dataTable({
	 
	 
    "bPaginate": true,
    "iDisplayStart":0,
    "iDisplayLength":100,
    "bProcessing" : true,
    "bServerSide" : true,
    "sAjaxSource" : path+"/com/ceva/pagination/HomeLiveTransactionsServlet.java?searchVal="+$("#searchVal").val()
 });
 
 
 
 	
 });
 
$(function() {
	$( "#sortable" ).sortable();
	$( "#sortable" ).disableSelection();
 
	$( "#sortable1" ).sortable();
	$( "#sortable1" ).disableSelection();
}); 
</script>


</head>
<body>


<form>
		<div id="hide-dash" class="span10" style="display:none;">
			<div>
				 <ul class="breadcrumb">Welcome to Amur...</ul>
				 <img alt="" src="../images/dash.png"  style="width: 75%; margin-top: 5px;">
			</div>
		 </div>
		
			
		
		<div id="content" class="span10" style="display:none;">  
			
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Dashboard</a></li>
				</ul>
			</div>
 		<div id="sortable1"></div>
 		
 		<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title> Statistics
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content" > 
					<fieldset> 
				<table style="width:100%;"  border="0" cellpadding="5" cellspacing="1" 	class="table table-striped table-bordered bootstrap-datatable " >
						<tr class="odd" > 
							<td width="25%" ><strong><label for="dayCount"><font color="brown">Daily Airtime Count</font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("dayCount").toString()%> </strong></font> <input type="hidden" name="dayCount"  id="dayCount"  value="<%=session.getAttribute("dayCount").toString()%>" /> </td> 
							<td width="25%" ><strong><label for="daySale"><font color="brown" >Daily Airtime Sale</font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("daySale").toString()%></strong></font> <input type="hidden" name="daySale"  id="daySale" value="<%=session.getAttribute("daySale").toString()%>" /></td>
						</tr>
						<tr class="odd" > 
							<td width="25%" ><strong><label for="weekCount"><font color="brown">Weekly Airtime Count</font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("weekCount").toString()%> </strong></font> <input type="hidden" name="weekCount"  id="weekCount"  value="<%=session.getAttribute("weekCount").toString()%>" /> </td> 
							<td width="25%" ><strong><label for="weekSale"><font color="brown" >Weekly Airtime Sale </font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("weekSale").toString()%></strong></font> <input type="hidden" name="weekSale"  id="weekSale" value="<%=session.getAttribute("weekSale").toString()%>" /></td>
						</tr>	
						<tr class="odd" > 
							<td width="25%" ><strong><label for="ActivePolices"><font color="brown">Total Active Policies</font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("policyCount").toString()%> </strong></font> <input type="hidden" name="policyCount"  id="policyCount"  value="<%=session.getAttribute("policyCount").toString()%>" /> </td> 
							<td width="25%" ><strong><label for="AirtimeSold"><font color="brown" >Total Airtime Sold</font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("totalSale").toString()%></strong></font> <input type="hidden" name="totalSale"  id="totalSale" value="<%=session.getAttribute("totalSale").toString()%>" /></td>
						</tr>
						<tr class="odd" > 
							<td width="25%" ><strong><label for="NonIdReg"><font color="brown">Pending Client ID Registrations </font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("ClnCount").toString()%> </strong></font> <input type="hidden" name="ClnCount"  id="ClnCount"  value="<%=session.getAttribute("ClnCount").toString()%>" /> </td> 
							<td width="25%" ><strong><label for="NonBenIdReg"><font color="brown" >Pending Beneficiary Registrations</font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("BenCount").toString()%></strong></font> <input type="hidden" name="BenCount"  id="BenCount" value="<%=session.getAttribute("BenCount").toString()%>" /></td>
						</tr>
						<tr class="odd" > 
							<td width="25%" ><strong><label for="ActvAgent"><font color="brown">Active Champions </font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("AgntCnt").toString()%> </strong></font> <input type="hidden" name="AgntCnt"  id="AgntCnt"  value="<%=session.getAttribute("AgntCnt").toString()%>" /> </td> 
							<td width="25%" ><strong><label for="ActThruAgnt"><font color="brown" >Activations Through Champions</font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("CustCnt").toString()%></strong></font> <input type="hidden" name="CustCnt"  id="CustCnt" value="<%=session.getAttribute("CustCnt").toString()%>" /></td>
						</tr>
						<tr class="odd" > 
							<td width="25%" ><strong><label for="ClnFailCnt"><font color="brown">Client Verifications Failed </font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("ClnFailCnt").toString()%> </strong></font> <input type="hidden" name="ClnFailCnt"  id="ClnFailCnt"  value="<%=session.getAttribute("ClnFailCnt").toString()%>" /> </td> 
							<td width="25%" ><strong><label for="DailyClnActvCnt"><font color="brown">Daily Activations </font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("DailyClnActvCnt").toString()%> </strong></font> <input type="hidden" name="DailyClnActvCnt"  id="DailyClnActvCnt"  value="<%=session.getAttribute("DailyClnActvCnt").toString()%>" /> </td>
						</tr> 
						<tr class="odd" > 
							<td width="25%" ><strong><label for="TotalClnCnt"><font color="brown">Total Policies </font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("TotalClnCnt").toString()%> </strong></font> <input type="hidden" name="TotalClnCnt"  id="TotalClnCnt"  value="<%=session.getAttribute("TotalClnCnt").toString()%>" /> </td> 
							<td width="25%" ><strong><label for="IDWithMulPol"><font color="brown">Multiple Policy Holders  </font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("MulPolCnt").toString()%> </strong></font> <input type="hidden" name="MulPolCnt"  id="MulPolCnt"  value="<%=session.getAttribute("MulPolCnt").toString()%>" /> </td> 
						</tr>
						<tr class="odd" > 
							<td width="25%" ><strong><label for="TotalCashSale"><font color="brown">Total Cash Sale</font></label></strong></td>
							<td width="25%" ><font color="blue"><strong><%=session.getAttribute("totalCashSale").toString()%> </strong></font> <input type="hidden" name="totalCashSale"  id="totalCashSale"  value="<%=session.getAttribute("totalCashSale").toString()%>" /> </td> 
							<td width="25%" ></td>
							<td width="25%" ></td> 							
						</tr>																																								
					</table>
			</fieldset> 
				</div>
			</div>
	</div> 
 
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>Live Transactions
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content" style="overflow-x: scroll;"> 
					<table  class="table table-striped table-bordered bootstrap-datatable datatable"  style="width:100%;"	id="personTable" >
						<thead>
							<tr>
								<th  style="font-size: 0.8em; font-family:Tahoma; " >S No</th>
								<th  style="font-size: 0.8em; font-family:Tahoma; ">Txn Date</th>
								<th  style="font-size: 0.8em; font-family:Tahoma; ">Txn Type</th>
								<th  style="font-size: 0.8em; font-family:Tahoma; ">Policy No</th>
								<th  style="font-size: 0.8em; font-family:Tahoma; ">Amount</th>
								<th  style="font-size: 0.8em; font-family:Tahoma; ">Status</th>	
								<th  style="font-size: 0.8em; font-family:Tahoma; ">Channel</th>		
								<th  style="font-size: 0.8em; font-family:Tahoma; ">Txn Reference No</th>
								<th  style="font-size: 0.8em; font-family:Tahoma; ">Mpesa Reference No</th>
							</tr>
						</thead> 
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		

	</div> 
	
 
	<input type="hidden" id="searchVal" name="searchVal" value="" />
</form>
</body>
 
</html>