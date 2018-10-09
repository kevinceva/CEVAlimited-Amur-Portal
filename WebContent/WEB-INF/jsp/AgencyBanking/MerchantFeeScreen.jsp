
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>IMPERIAL</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
 <%@taglib uri="/struts-tags" prefix="s"%>  
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<s:set value="responseJSON" var="respData"/>
 <script type="text/javascript" >
$(document).ready(function() { 
	
	var val = 1;
	var rowindex = 1;
	var colindex = 0;
	var bankfinalData="<s:property value="#respData.SLAB" />";
	var bankfinalDatarows = "";
	var eachfieldArr = "";
	var appendTxt = "";
	
	if(bankfinalData != "") {
		var bankfinalDatarows = bankfinalData.split("#");
		
		if(val % 2 == 0 ) {
			addclass = "even"; 
		} else {
			addclass = "odd"; 
		}   
		val++; 
		for(var i=0;i<bankfinalDatarows.length;i++){
			  eachrow=bankfinalDatarows[i];
			  eachfieldArr=eachrow.split("@"); 
			  var freq = eachfieldArr[0];
			  
			  if(freq == "200") {
				freq = "Per Txn";
			  } else if(freq == "205"){
				freq = "Daily";
			  } else if(freq == "210"){
				freq = "Weekly";
			  } else if(freq == "215"){
				freq = "Monthly";
			  } else if(freq == "220"){
				freq = "Yearly";
			  } else if(freq == "225"){
				freq = "LifeTime";
			  }
			  
			  appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
 				"<td>"+rowindex+"</td>"+
				"<td>"+freq+"</td>"+	
 				"<td>"+eachfieldArr[1]+"</td>"+	
				"<td>"+(eachfieldArr[2] == 'A' ? 'Amount' : 'Count' )+"</td>"+	
				"<td>"+(eachfieldArr[3] )+"</td>"+	
				"<td>"+eachfieldArr[4]+" </td>"+ 
				"<td>"+(eachfieldArr[5] == 'P' ? 'Percentile' : 'Flat')+" </td>"+  
				"<td>"+eachfieldArr[6]+" </td>"+  
				"<td>"+eachfieldArr[7]+" </td>"+  
				"<td>"+eachfieldArr[8]+" </td>"+  
				"<td>"+eachfieldArr[9]+" </td>"+
				"</tr>";
				$("#tbody_data1").append(appendTxt);	  
			rowindex = ++rowindex;
			colindex = ++ colindex; 
		} 
	}
	
	val = 1;
	rowindex = 1;
	colindex = 0;
	bankfinalData="<s:property value="#respData.ACQDET" />"; 
	bankfinalDatarows = "";
	eachfieldArr = "";
	appendTxt = "";
	if(bankfinalData != "") {
		var bankfinalDatarows = bankfinalData.split("#");
		
		if(val % 2 == 0 ) {
			addclass = "even"; 
		} else {
			addclass = "odd"; 
		}  
		
		val++; 
	 
		for(var i=0;i<bankfinalDatarows.length;i++){
			eachrow=bankfinalDatarows[i];
			eachfieldArr=eachrow.split(","); 
			appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
 				"<td>"+rowindex+"</td>"+
				"<td>"+eachfieldArr[0]+"</td>"+	
				"<td>"+eachfieldArr[1]+"</td>"+
				"</tr>";
				$("#tbody_data3").append(appendTxt);	  
			rowindex = ++rowindex;
			colindex = ++ colindex; 
		} 
	} else {
		$('#acq-bin-chk').hide();
	}
	
	if('${responseJSON.serviceTrans}' == 'ONUS') {
		$("#acq-bin-chk").hide();
	}
	
}); 

function getServiceScreen(){
	var data=$("#mrcode").val();
	alert(data);
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/feemerchantScreen.action';
	$("#form1").submit();
	return true;
}
 
function getServiceScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/merchantFeeAssign.action';
	$("#form1").submit();
	return true;
}
</script>
</head>
<body>
<form name="form1" id="form1" method="post" action="">
	<div id="content" class="span10">  
		<div>
			<ul class="breadcrumb">
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt;  </span> </li>
			  <li> <a href="serviceMgmtAct.action?pid=10">Fee Management</a> <span class="divider"> &gt;&gt;  </span></li>
			  <li> <a href="#">Fee View</a> </li>
 			</ul>
		</div>
		<div class="row-fluid sortable"><!--/span--> 
			<div class="box span12"> 
				 
				<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>Fee Details
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					</div>
				</div> 
					<div id="primaryDetails" class="box-content">
						<fieldset>
							<table width="950" border="0" cellpadding="5" cellspacing="1" 
								class="table table-striped table-bordered bootstrap-datatable " >
									<tr class="even">
										<td width="20%"><label for="Service ID"><strong>Service Code</strong></label></td>
										<td width="30%"> <s:property value="#respData.serviceCode" />  </td>
										<td width="20%"> <label for="Service Name"><strong>Service Name</strong></label> </td>
										<td width="30%"><s:property value="#respData.serviceName" />  </td>
									</tr>
									<tr class="odd">
										<td > <label for="Sub Service Code"><strong>Sub Service Code</strong></label> </td>
										<td > <s:property value="#respData.subServiceCode" /> 	</td>
										<td> <label for="Sub Service Name"><strong>Sub Service Name</strong></label> </td>
										<td> <s:property value="#respData.subServiceName" /> 	</td>
									</tr>
									<tr class="even">
										<td > <label for="Fee Code"><strong>Fee Code</strong></label> </td>
										<td> <s:property value="#respData.feeCode" /> 	</td>
										<td> <label for="Service"><strong>ON-US/OFF-US Flag </strong></label> 	</td>
										<td><s:property value="#respData.ONUS_OFFUS_FLAG" />  </td>  
 									</tr>
								 
							</table>
						</fieldset>
				</div> 
			<div id="acq-bin-chk" class="box-content">
 				<fieldset>  
					<table id="mytable" width="950" border="0" cellpadding="5" cellspacing="1" 
						class="table table-striped table-bordered bootstrap-datatable ">
						<thead>
							<tr>
								<th>Sno</th>
 								<th>Bin</th>
								<th>Acquirer Id</th>
							</tr>
						</thead>    
						<tbody id="tbody_data3"> 
						</tbody>
					</table> 
				<fieldset>  
			</div> 
			<div id="secondaryDetails" class="box-content">
				<fieldset> 
					<table id="mytable" width="950" border="0" cellpadding="5" cellspacing="1" 
						class="table table-striped table-bordered bootstrap-datatable ">
						<thead>
							<tr>
								<th>Sno</th>
								<th>Frequency</th>
								<!-- <th>Channels</th> -->
								<th>Currency</th>
								<th>Condition</th>
								<th>From Amount/Count</th>
								<th>To Amount/Count</th> 
								<th>F/P</th> 
								<th>Bank Account</th> 
								<th>Bank Amount</th> 
								<th>Agent Account</th> 
								<th>Agent Amount</th> 
							</tr>
						</thead>    
						<tbody id="tbody_data1"> 
						</tbody>
						<input type="hidden" name="mrcode" id="mrcode" value="${mrcode}" />
					</table> 
				</fieldset> 
			</div>
		</div>
	</div>  
	  
	<div class="form-actions"> 
		<a  class="btn btn-danger" href="#" onClick="getServiceScreen()">Next</a>
	</div> 
</div>
</form>
</body>
</html>
