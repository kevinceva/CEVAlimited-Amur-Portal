
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

function getInfo(){
 	var data=$("#cases option:selected" ).text();
  	
 	var caseid=$("#cases option:selected" ).val();
 	
 	
 	console.log("caseid == :"+caseid);
 	if (caseid == "111"  || data == "111")
 	{	
  		var formInput='mob='+${responseJSON.CLDET.MOB}+'&srchval='+${responseJSON.srchval};
  		//var json="";
  		//var appendTxt="";
 	   $.getJSON('getClientFailedTxns.action', formInput,function(data) {
 		 
 		  var fjson = data.responseJSON.CLNT_FLD_TXNS;
 		 
 		  if (fjson == 0 || fjson == null)
 		{
 			 $("#hide-err").show();	
 			$("#fld-txn-grid").hide();
 			$("#action-grid").hide();	
 		}else	
	 	{
 			$("#hide-err").hide();
 			$("#fld-txn-grid").show();
 			$("#action-grid").show();
 			var val = 1;
	 			var rowindex = 0;
	 			var colindex = 0;
	 			var addclass = "";
	 	    	$.each(fjson, function(i, v) {
	 	    		if(val % 2 == 0 ) {
	 	   			addclass = "even";
	 	   			val++;
	 	   		}
	 	   		else {
	 	   			addclass = "odd";
	 	   			val++;
	 	   		}  
	 	   		var rowCount = $('#tBody > tr').length;
	 	   		
	 	   		colindex = ++ colindex;    				
		   			
	 	   		index=colindex-1;
	 	   		//alert(index);
	 	   			var tabData="DataTables_Table_0";
	 	   			
	 	   		//alert(tds);
	 	   		 var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
	 	   		 "<td><input type='radio' name='rrid' id='" +v.rid+ "'/></td>"+ 
	 	   		"<td >"+colindex+"</td>"+
	 	   		"<td >"+v.cdate+"</td>"+
	 	   		"<td >"+v.pid+"</td>"+
	 	   		"<td >"+v.amt+"</td>"+
	 	   	"<td >"+v.status+"</td>"+
	 	   "<td >"+v.aid+"</td>"+
	 	   		"<td>"+v.rid+"</span></td></tr>";
	 	   		$("#tBody").empty();	
	 	   		$("#tBody").append(appendTxt);	
	 	   		rowindex = ++rowindex;
	 			});
 	   		}
 		});
 	   
 	  
 	}
 	else
 	{
 		$("#fld-txn-grid").hide();
			$("#action-grid").show();
			$("#hide-err").hide();
 	}	
 }

$(document).ready(function(){
	
	var caseData ='${responseJSON.caseJson}';
	var clData = '${responseJSON.CLDET.CID}';
	//alert("  case data:"||caseData);
	/* var json = jQuery.parseJSON(caseData);
	alert("  json data:"||json); */
	
	var json = jQuery.parseJSON(caseData);
	 var options = "";
	  
	 $.each(json, function(i, v) {
	       options = $('<option/>', {value: i, text: v }).attr('data-id',i);
	     $('#cases').append(options);
	 });
	 
	 $.validator.addMethod("valueNotEquals", function(value, element, arg){
		   return arg != value;
		  }, "Value must not equal arg.");

		  // configure your validation
		  
	 var validationRules = {
				rules : { 
					cases : { valueNotEquals : "0" } 
				},		
				messages : { 
					cases : { 
						valueNotEquals : "Please Select a Complaint."
								}
				} 
			}; 
	 
	
		
	$('#btn-submit').on('click',function() {
		 var rid = $('input[name=rrid]:checked', '#form1').attr("id"); 
		
		$("#form1").validate(validationRules);
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/callcenterinsertcase.action?rid="+rid;
		$("#form1").submit();					
	});

	$('#btn-back').on('click',function() { 
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
           
	<div class="box span12">
                            
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
							<td width="25%" ><strong><label for="Client ID"> Client Id</label></strong></td>
							<td width="25%" >${responseJSON.CLDET.CID} <input type="hidden" name="cid"  id="cid" value="${responseJSON.CLDET.CID}" /> </td> 
							<td width="25%" ><strong><label for="Client Name"> Client Name</label></strong></td>
							<td width="25%" >${responseJSON.CLDET.CNAME} <input type="hidden" name="cname"  id="cname" value="${responseJSON.CLDET.CNAME}" />
							<input type="hidden" name="srchval"  id="srchval" value="${responseJSON.srchval}" /></td>
						</tr>
						<tr class="odd">
							<td><strong><label for="Mobile"> Mobile</label></strong></td>
							<td width="25%" >${responseJSON.CLDET.MOB}  <input type="hidden" name="mob"  id="mob" value="${responseJSON.CLDET.MOB}" /> </td> 
							<td><strong><label for="E-Mail"> E-Mail</label></strong></td>
							<td width="25%" >${responseJSON.CLDET.EMAIL}  <input type="hidden" name="email"  id="email" value="${responseJSON.CLDET.EMAIL}" /> </td>
						 </tr>
						<tr class="odd">
							<td><strong><label for="Case">Complaints<font color="red">*</font></label></strong></td>
							<td width="25%" ><select name="cases" id="cases"  onChange="getInfo()"><option value="0">Select</option></select></td> 
							<td width="25%"><strong><label for="Remarks">Remarks</label></strong></td>
 							<td width="15%" ><textarea  name="remarks"  id="remarks" ></textarea> </td>	
						</tr> 
				</table>
			</fieldset> 
			
              
		</div>
		</div>
		</div>
		 
		<div class="row-fluid sortable">
		<div id="hide-err" class="span12" style="display:none;">
		<div>
			 <ul class="breadcrumb" style="font-weight: bold">No Failed Transactions Found</ul>
		</div>
		 </div>
		<div id="fld-txn-grid" class="box span12" style="display:none;" >
        
			  
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Failed Transactions
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>				  
                  
            <div class="box-content">
				<fieldset>
					<table width="950%" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable datatable" 
						id="DataTables_Table_0" >
						<thead>
							<tr >
								<th width="7%">Select</th>
								<th width="7%">S No</th>								
								<th>Transaction Date</th>
								<th>Policy Number</th>
								<th>Amount</th>
								<th>Status</th>
								<th>Airtime Reference No</th>
								<th>Mpesa Reference No</th>
							</tr>
						</thead> 
						<tbody  id="tBody">
						</tbody>
					</table>
				</fieldset> 
		</div>
		</div>
		
		</div>
		
		<div id="action-grid" class="form-actions" style="display:none;">
		   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Submit"   ></input>
		   <input type="button" class="btn btn-primary" type="text"  name="btn-back" id="btn-back" value="Back"   ></input>
		</div> 
              						 
	</div><!--/#content.span10-->
		  
 </form>
</body>
</html>

