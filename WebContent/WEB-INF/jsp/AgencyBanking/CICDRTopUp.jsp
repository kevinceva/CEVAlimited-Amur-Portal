
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

var listDate = "tdate".split(",");
var datepickerOptions = {
			showTime: false,
			showHour: false,
			showMinute: false,
  		dateFormat:'dd/mm/yy',
  		alwaysSetTime: false,
		changeMonth: true,
		changeYear: true
};

/* $(function() {
	$(listDate).each(function(i,val){
 		$('#'+val).datepicker(datepickerOptions);
	});  
	 
 
	var config = {
      '.chosen-select'           : {},
      '.chosen-select-deselect'  : {allow_single_deselect:true},
      '.chosen-select-no-single' : {disable_search_threshold:10},
      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
      '.chosen-select-width'     : {width:"95%"}
    }
    for (var selector in config) {
      $(selector).chosen(config[selector]);
    }
	//$('#enableUserId').removeAttr('style'); 
});	 */

$(document).ready(function(){
	document.getElementById("act-grid").style.display="block";
	
	$(listDate).each(function(i,val){
 		$('#'+val).datepicker(datepickerOptions);
	});  
	 
 
/* 	var config = {
      '.chosen-select'           : {},
      '.chosen-select-deselect'  : {allow_single_deselect:true},
      '.chosen-select-no-single' : {disable_search_threshold:10},
      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
      '.chosen-select-width'     : {width:"95%"}
    }
    for (var selector in config) {
      $(selector).chosen(config[selector]);
    }	
 */

	$('#btn-submit').on('click',function() { 
		var amt=document.getElementById("amt").value;
		var mob=document.getElementById("mob").value;
		var refno=document.getElementById("refno").value;
		var famt=document.getElementById("famt").value;
		var tdate=document.getElementById("tdate").value;
		
		if (mob == null || mob == "")
		{
			alert("Please enter mobile number.");
			return false;
		}
		
		if (amt == null || amt == ""  )
		{
			alert("Please enter amount.");
			return false;
		}
		
		if (famt == null || famt == "" )
		{
			alert("Please check the final amount.");
			return false;
		}
		
		if (refno == null || refno == "")
		{
			alert("Please enter mpesa reference number.");
			return false;
		}
		
		if (tdate == null || tdate == "")
		{
			alert("Please enter transaction date.");
			return false;
		}		
		
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/initiateDRTopUp.action?";
		$("#form1").submit();					
	});	
	
	$('#agents').on('click',function() {
		/* var srhval = '${responseJSON.srchval}';
		alert("value of srchval llll:"+srhval); */
		//$("#srchval").val()=srchal;	
		//alert("srchval::::::::::"+$("#srchval").val());
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/selfregAgents.action?";
		$("#form1").submit();					
	});	
			
});

function fetchName()
{
	var formInput = $('#form1').serialize();
	console.log("value of formInput :"+formInput);
	var agnurl ='<%=request.getContextPath()%>/<%=appName %>/fetchName.action';
    var bio_resval="";
    //alert("biourl:="+biourl);
    $.ajax
    ({
    type: "POST",
    url: agnurl,
    data:formInput,
    cache: false,
    success: function(html)
    {
    	
     var clnreg = html.responseJSON.remarks;
     var exst_cnt = html.responseJSON.exst_cnt;
     console.log("value of clnreg:"+clnreg+"--"+exst_cnt);
     if ( clnreg == "Y")
    {	 
     $("#cname").val(html.responseJSON.cname);
	     if ( exst_cnt == 0)
	     {	 
		     document.getElementById("act-grid").style.display="block";
		     document.getElementById("nodata-grid").style.display="none";   
	    }else
	    {
	    	document.getElementById("nodata-grid").style.display="none"; 
	    	document.getElementById("exst-grid").style.display="block";
	    }	
	 }
     else
    {
    	 document.getElementById("exst-grid").style.display="none";
    	 document.getElementById("nodata-grid").style.display="block";
    }
    }, 
     error: function (jqXHR, textStatus, errorThrown)
      {
      alert("Error moving to next");
      }
    });
}

function calcFinalAmt()
{

	var amt = document.getElementById("amt").value;
	console.log("value of amount:"+amt);
	var bonus = document.getElementById("bonus").value;
	console.log("value of bonus:"+bonus);
	var famt = parseInt(amt) + parseInt(amt * (bonus/(100)));
	console.log("fmat :="+famt);
	$("#famt").val(famt);
	$("#bonus").val(bonus);
		
}


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
				  <li> <a href="drtopup.action">CIC DR TopUp </a> <span class="divider"> &gt;&gt; </span></li>
				</ul>
			</div>  
			
<%-- 			<div class="box-content" id="top-layer-anchor">
				<span>
				<!-- 	<a href="#" class="btn btn-info" id="sms-details" title='SMS' data-rel='popover'  data-content='SMS Details.'>
					<i class="icon icon-web icon-white"></i>&nbsp;SMS Details</a> &nbsp; -->
					 <input type="button" class="btn btn-info" type="text"  name="agents" id="agents" value="Champions"   ></input>							
				</span>
							 
			</div>  --%>
		 	
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
           
	<div id="data-grid"  class="box span12" >
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>TopUp Details 
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>
						
		<div  class="box-content" >
			<fieldset> 
				<span > <font color="green" size="4"> ${responseJSON.result}</font> </span>
				<table width="950"  border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
						<tr class="odd" > 
							<td width="25%" ><strong><label for="Mobile Number"> Mobile No</label></strong></td>
							<td width="25%" ><input type="text" name="mob"  id="mob"  /> </td> 
							<td width="25%" ><strong><label for="Amount"> Amount</label></strong></td>
							<td width="25%" ><input type="text" name="amt"  id="amt" /></td>
						</tr>
						 <tr class="odd" > 
							<td width="25%" ><strong><label for="Bonus Percent"> Bonus Percentage</label></strong></td>
							<td width="25%" ><input type="text" name="bonus"  id="bonus"  value="0"  onchange="calcFinalAmt()" /> </td> 
							<td width="25%" ><strong><label for="Final Amount"> Final Amount </label></strong></td>
							<td width="25%" ><input type="text" name="famt"  id="famt"  onfocus="calcFinalAmt()" readonly/> </td> 
						</tr>
 						<tr class="odd" >
 							<td width="25%" ><strong><label for="MpesaRefNo"> M-pesa Reference No</label></strong></td>
							<td width="25%" ><input type="text" name="refno"  id="refno"  /> </td> 
							<td width="25%" ><strong><label for="Transaction Date"> Transaction Date</label></strong></td>
							<td class="fromDateTd" width="25%" ><input type="text" name="tdate"  id="tdate"  /> </td> 
						</tr>
						<tr class="odd" >
							<td width="25%" ><strong><label for="Remarks"> Remarks</label></strong></td>
							<td width="25%" ><input type="text" name="remarks"  id="remarks"  /> </td>
 							<td width="25%" ></td>
							<td width="25%" ></td> 							 
						</tr>							 					
							
				</table>
			</fieldset> 
			
              
		</div>
		</div>
		</div>

			
	<div class="row-fluid sortable">
           
	<div id="nodata-grid" class="box span12" style="display:none;">
						
		<div class="box-content" >
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >	
									<tr>
										
										<td><strong><font color="red"><b> Champion Not Registered / InActive</b></font></strong> </td>
									
									</tr>
		
				</table>
			</fieldset> 
			
              
		</div>
		</div>
		</div>
		
		<div class="row-fluid sortable">
		<div id="exst-grid" class="box span12" style="display:none;">
						
		<div class="box-content" >
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >	
									<tr>
										
										<td><strong><font color="red"><b> Champion Already Registered </b></font></strong> </td>
									
									</tr>
		
				</table>
			</fieldset> 
			
              
		</div>
		</div>											
		
		</div> 	
		<div id="act-grid" class="form-actions" style="display:none;">
		   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Force TopUp"   ></input>
		  </div>  
              						 
	</div><!--/#content.span10-->
		  
 </form>
</body>
</html>

