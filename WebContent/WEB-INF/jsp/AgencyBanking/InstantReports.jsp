
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<% String checkTeller= session.getAttribute("userLevel").toString() == null ? "NO_VALUE" : session.getAttribute("userLevel").toString(); %>
<% String userGroup= session.getAttribute("userGroup").toString() == null ? "NO_VALUE" : session.getAttribute("userGroup").toString(); 

 
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Create Entity</title> 
  

<style type="text/css">
select#region,
select#headOffice,
select#Location,
select#userid,
select#bank,
select#reportname,
select#custprof,
select#txnType {
	width : 290px;
 }
input#fromDate,
input#toDate,
input#userid1 {
	width : 280px;
 }
</style>

<script type="text/javascript"> 

var list = "fromDate,toDate".split(",");
var datepickerOptions = {
			showTime: false,
			showHour: false,
			showMinute: false,
  		dateFormat:'dd/mm/yy',
  		alwaysSetTime: false,
		changeMonth: true,
		changeYear: true
};	
	
 

function setStringCond(str) {
	var strArr = str.split("\,");
	 
	var changedString = "";
	for(var i=0;i<strArr.length;i++) {
			if(i == strArr.length ) changedString+=  "'"+strArr.split('-')[0]+"'";		
			else changedString+= "'"+strArr.split('-')[0] +"',";
	}	

	return	changedString;					
}

var userdata = ""; 
var arrayData = {
				'region' : '${responseJSON.region}',
				'headOffice' :  '${responseJSON.headoffice}',
				'Location' :'${responseJSON.location}',
				'userid' : '${responseJSON.userid}', 
				'bank' : '${responseJSON.bank}',
				'reportname' : '${responseJSON.reportname}',
				'custprof' : '${responseJSON.custprof}'	 
		 };
$(function(){ 
	
		$(list).each(function(i,val){
			$('#'+val).datepicker(datepickerOptions);
		});
			
			
 		$.each(arrayData, function(selectKey, arrvalue ){ 
			var json = $.parseJSON(arrvalue); 
			var options = ''; 
			if(selectKey != 'userid') { 
				options =$('<option/>', {value: 'Not Required', text: 'Not Required'});  
				$('#'+selectKey).append(options); 
			} 
			 
			 $.each(json, function(i, v) {
				if(selectKey == 'userid') {
					//console.log("index ==> "+ i);
					 if(i == 0)  userdata+=  "'"+v.key +"'";	 	
						else userdata+= ",'"+v.key  +"'" ; 
				}
				 options = $('<option/>', {value: v.key, text: v.val});    
				 $('#'+selectKey).append(options);
			 }); 
			 
			  if(selectKey == 'bank') { 
				// To Iterate the Select box ,and setting the in-putted as selected 
				$('#'+selectKey).find('option').each(function( i, opt ) { 
				    if( opt.value === 'KCB' ) 
				        $(opt).attr('selected', 'selected');
				});
			 }
		 
		}); 
 		
 		 
		
	 
 		$("select#region, select#headOffice, select#Location").live("change",function(){ 
			var idAttr = $(this).attr('id');  
			 
			$('select#region, select#headOffice, select#Location').removeAttr('style');
			 
			$('td.regionTd').children('span').empty();			
			$('td.headOfficeTd').children('span').empty();
			$('td.LocationTd').children('span').empty();
			 
			 
			$('#region-spn').text(' ');
			$('#headOffice-spn').text(' ');
			$('#Location-spn').text(' ');
			
			var queryString = ""; 
			
			var build  = ""; 
			var flag = false; 
			 
			if(idAttr == 'region') {
				build  = $("#"+idAttr).val() == null ? 'NO' :  $("#"+idAttr).val().join('\',\''); 
				queryString = "region='"+build + "'&method=searchData&selectedSelBox="+idAttr; 
				$('select#headOffice, select#Location').removeAttr('style'); 
				$('td.headOfficeTd   span').remove();
				$('td.LocationTd   span').remove();	
				
			} else if(idAttr == 'headOffice') { 
				build  = $("#"+idAttr).val() == null ? 'NO' :  $("#"+idAttr).val().join('\',\'');
				queryString = "hoffice='"+build + "'&method=searchData&selectedSelBox="+idAttr;
				$('select#region, select#Location').removeAttr('style');
				$('td.regionTd   span').remove();				 
				$('td.LocationTd   span').remove();	
			} else if(idAttr == 'Location') {  
				build  = $("#"+idAttr).val() == null ? 'NO' :  $("#"+idAttr).val().join('\',\'');
				queryString = "location='"+build + "'&method=searchData&selectedSelBox="+idAttr;
				$('select#region, select#headOffice').removeAttr('style');
				 $('td.regionTd   span').remove();
				$('td.headOfficeTd   span').remove(); 
			}  
		 
			if(build.indexOf("Not Required") != -1 ) {
				 $('#'+idAttr).css({"border-color": "red", 
									 "border-weight":"1px", 
									 "border-style":"solid"});
												 
				 //$('td.'+idAttr+'Td ').append("<span class='"+idAttr+"-spn'>&nbsp; Please unselect 'Not Required' option from the combox.<span >"); 
				 $('#'+idAttr+'-spn').text("&nbsp; Please unselect \'Not Required\' option from the combox.");
				 
			}	else {
				flag = true;
			}
			 
			 
			if(idAttr == "region") {  
				$("select#Location").find('option').removeAttr("selected"); 
				 $("select#headOffice").find('option').removeAttr("selected"); 
			} else if(idAttr == "headOffice") { 
				  $("select#region").find('option').removeAttr("selected"); 
				  $("select#Location").find('option').removeAttr("selected"); 
			} else if(idAttr == "Location") { 
				  $("select#region").find('option').removeAttr("selected"); 
				  $("select#headOffice").find('option').removeAttr("selected"); 
			}   
			
			 
			if(flag && build != 'NO') { 
				$.getJSON("postJson.action", queryString,function(data) {  
						 
					 var selectToFill = data.fillSelectBox;
					 $('#'+selectToFill).empty(); 
					 
					 var options = $('<option/>', {value: 'Not Required', text: 'Not Required'}); 
					 $('#'+selectToFill).append(options);
					 
					 $.each(data.details,function(key, value){
						 
						options = $('<option/>', {value: key, text: value});    
						$('#'+selectToFill).append(options);
					}); 

				});
			}
			 
		}); 
}); 
 
</script>
</head> 
<body> 
<form name="form1" id="form1" method="post" > 
	<div id="content" class="span10">
	<div>
		<ul class="breadcrumb">
			<li>
				<a href="home.action">Home</a> <span class="divider">&gt;&gt;</span>
			</li>
			<li><a href="#">Reports</a>
			</li>
			 
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
			 <i class="icon-edit"></i>Select a report to view 
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div> 		
		<div class="box-content"> 
			<fieldset>
				<table width="100%" border="0" cellpadding="5" cellspacing="1" 
					id="table table-striped table-bordered bootstrap-datatable">
						<%  
							if(checkTeller.equalsIgnoreCase("HEAD OFFICE") 
								|| checkTeller.equalsIgnoreCase("REGION")   
								|| checkTeller.equalsIgnoreCase("BRANCH MANAGER")  ) { 
						 
								if(!checkTeller.equalsIgnoreCase("BRANCH MANAGER")) {	
							%>
									<tr id="tr-region">
										<td  width="8%">&nbsp;</td>
										<td  width="11%"><label for="Region"><strong>Region</strong></label></td>
										<td  width="81%" class="regionTd">
										<select name="region" id="region" multiple  class="chosen" width="450px">
										</select><span class='region-spn'></span></td>
									</tr>
									<tr  id="tr-head-office">
										<td  >&nbsp;</td>
										<td ><strong><label for="Head Office"><strong>Head Office </strong></label></td>
										<td class="headOfficeTd"><strong><select name="headOffice" id="headOffice"  multiple class="chosen" width="450px">
										</select><span class='headOffice-spn'></span></strong></td>
									</tr>
							<%  } %>
								<tr id="tr-branch">
									<td  >&nbsp;</td>
									<td  ><label for="Branch"><strong>Branch</strong> </label></td>
									<td  class="LocationTd"><select name="Location" id="Location"  multiple class="chosen" width="450px">
									</select><span class='Location-spn'></span></td>
								</tr>
						<%   
						}
						
						if(checkTeller.equalsIgnoreCase("USER")) {	
						%>
							<tr id="tr-user-id">
								<td  >&nbsp;</td>
								<td ><label for="User Id"><strong>User Id</strong></label></td>
								<td class="useridTd"><strong> <input name="name" type="text"  id="userid1" name="userid1" value="<%=(String)session.getAttribute(CevaCommonConstants.MAKER_ID) %>"  disabled="disabled" /></strong></td>
							</tr> 
							<tr id="tr-trns-type" >
								<td>&nbsp;</td>
								<td><label for="Txn Type"><strong>Transaction Type</strong></label></td>
								<td  class="bankTd">
									<select name="txnType" id="txnType"  multiple  class="chosen" width="450px">
										<!-- <option value="Not Required">ALL</option> -->
										<option value="WDL">WDL-Cash Withdrawal</option>
										<option value="CDP">CDP-Cash Deposit</option> 
									</select>
								</td>
							</tr>
						<% } else {  %>
							
							<tr id="tr-user-id">
								<td  >&nbsp;</td>
								<td ><label for="User Id"><strong>User Id</strong></label></td>
								<td class="useridTd"><strong><select name="userid" id="userid"  multiple class="chosen" width="450px">
								</select></strong></td>
							</tr>
							<%
							if(checkTeller.equalsIgnoreCase("Branch Office") 
								|| checkTeller.equalsIgnoreCase("Branches")) {	
							%>
								<tr id="tr-trns-type" >
									<td>&nbsp;</td>
									<td><label for="Txn Type"><strong>Transaction Type</strong></label></td>
									<td  class="bankTd"><select name="txnType" id="txnType"  multiple  class="chosen" width="450px">
										<!-- <option value="ALL">ALL</option> -->
										<option value="WDL">WDL-Cash Withdrawal</option>
										<option value="CDP">CDP-Cash Deposit</option>
									</select></td>
								</tr>
								<% 
								} else {
								%> 
									<tr id="tr-trns-type" >
										<td>&nbsp;</td>
										<td><label for="Txn Type"><strong>Transaction Type</strong></label></td>
										<td  class="bankTd"><select name="txnType" id="txnType"  multiple  class="chosen" width="450px">
											<!-- <option value="ALL">ALL</option> -->
											<option value="WDL">WDL-Cash Withdrawal</option>
											<option value="CDP">CDP-Cash Deposit</option>
											<option value="HUDUMA">HUDUMA-HUDUMA</option>
											<option value="BEQ">BEQ-Balance Inquiry</option>
											<option value="MST">MST-Mini Statement</option>
											<option value="FTR">FTR-FUND TRANSFER</option>											
											<option value="PWDL">PWDL-PWALLET CASH WITHDRAWL</option>
											<option value="PCDP">PCDP-PWALLET CASH DEPOSIT</option>
											<option value="PDIS">PDIS-PWALLET CASH DISPENSER</option>
											<option value="PFT">PFT-PWALLET FUND TRANSFER</option>
											<option value="PBE">PBE-PWALLET BALANCE INQUIRY</option>
										</select></td>
									</tr>
									<tr  id="tr-custprof"  >
										<td  >&nbsp;</td>
										<td  > <label for="Customer Profile"><strong>Customer Profile</strong></label></td>
										<td  class="custprofTd"><select name="custprof" id="custprof" class="chosen" width="450px">
										</select></td>
									</tr>
								<% 
									}
								%> 
							<% 
						}
						%> 
						
						<tr  id="tr-bank">
							<td  >&nbsp;</td>
							<td  > <label for="Bank"><strong>Bank</strong></label></td>
							<td  class="bankTd"><select name="bank" id="bank" class="chosen" width="450px">
							</select></td>
						</tr>
						<tr  id="tr-reportname">
							<td  >&nbsp;</td>
							<td  > <label for="Report Name"><strong>Report Name<font color="red">*</font></strong></label></td>
							<td class="reportnameTd"><select name="reportname" id="reportname" class="selectTarget" width="450px">
							</select></td>
						</tr>
						<tr id="tr-fromdate" >
							<td >&nbsp;</td>
							<td > <label for="From Date"><strong>From Date<font color="red">*</font></strong></label></strong></td>
							<td class="fromDateTd"><strong><input name="name" type="text"  id="fromDate" name="fromDate"  /></strong></td>
						</tr>
						<tr  id="tr-todate" >
							<td >&nbsp;</td>
							<td > <label for="To Date"><strong>To Date<font color="red">*</font></strong></label></strong></td>
							<td class="toDateTd"><strong> <input name="name" type="text"  id="toDate" name="toDate"  /></strong></td>
						</tr> 
				 </table>  
				</fieldset>
			<div class="form-actions"> 
				  <input type="button" class="btn btn-primary" type="text"  name="save" id="save" value="Generate Report" width="100" ></input>
				  <input type="button" class="btn" type="text"  name="save" id="save" value="Cancel" width="100" ></input>
			 </div>  
		</div>
		</div>
</div> 
 	               
 <script type="text/javascript">  
 
	function splitDate(date) {
		var arr = date.split('/'); 
		var validDate = arr[1] + "/" + arr[0] + "/" + arr[2];  
		return validDate;
	}

	function setSelValues(id){ 
		var strPrep = "";
		var maxLen = $("#"+id +" > option:selected").length;
		var index = 0;
		
		$( "#"+id +" > option:selected").each(function() { 
			index++;
			 
			if( id == 'Location') { 
				if(index == 1)  strPrep+=  "'"+this.value.split("\-")[0] +"'";	 	
				else strPrep+= ",'"+this.value.split("\-")[0]  +"'" ; 
			 } else {
				if(index == 1)  strPrep+=  "'"+this.value +"'";	 	
				else strPrep+= ",'"+this.value  +"'" ; 
			 }
			 
				if(index == maxLen)  {index == 0;} 
			
		});  
		
		return strPrep;
	}

	$(function(){ 
		 
		$('#toDate').keypress(function() { 
			$('#toDate').removeAttr('style');
			$('td.toDateTd > span').text('');
		});
		
		$('#fromDate').keypress(function() {
		 
		   $('#fromDate').removeAttr('style');
		   $('td.fromDateTd > span').text('');
		});
		
		$('#toDate').blur(function() { 
			$('#toDate').removeAttr('style');
			$('td.toDateTd > span').text(''); 
		});
		
		$('#fromDate').blur(function() {  
		   $('#fromDate').removeAttr('style');
		   $('td.fromDateTd > span').text(''); 
		});
		
		 $('#reportname').blur(function() {  
		   $('#reportname').removeAttr('style');
		   $('td.reportnameTd > span').text(''); 
		});

		$(".selectTarget").change(function() {
			if( $('#reportname option:selected').text() != 'Not Required') {
				$('#reportname').removeAttr('style');
				$('td.reportnameTd > span').text(''); 
		   }
		}); 
		 
		$('#save').live('click', function () { 
				 
			$('#toDate').removeAttr('style'); 
			$('td.toDateTd > span').text('');
			
			$('#fromDate').removeAttr('style');
			$('td.fromDateTd > span').text('');
			
			$('#reportname').removeAttr('style');
			$('td.reportnameTd > span').text('');
			 
			var v_reportName = $("#reportname option:selected").text();
			 
			var fromDate = $("#fromDate").val();
			var toDate = $("#toDate").val();
		   
			var fromDate1 = new Date(splitDate(fromDate));
			var toDate1 = new Date(splitDate(toDate));
			var sysDate = new Date();
			
			if(fromDate == "" )
			{
				 $('#fromDate').css({"border-color": "red", 
									 "border-weight":"1px", 
									 "border-style":"solid"});
				 $('td.fromDateTd').append("<span > &nbsp; Please input from date.</span>");
			}
			 if(toDate == "")
			{
				 $('#toDate').css({"border-color": "red", 
								 "border-weight":"1px", 
								 "border-style":"solid"});
				 $('td.toDateTd').append("<span > &nbsp; Please input to date.</span >");
			}
			 
			if(v_reportName == 'Not Required' || v_reportName == '')
			{
				 $('#reportname').css({"border-color": "red", 
								 "border-weight":"1px", 
								 "border-style":"solid"});
								 
				 $('td.reportnameTd').append("<span >&nbsp; Please select a report to generate.<span >"); 
			} 
			
			if(fromDate1 > toDate1 ) {
				$('#toDate').css({"border-color": "red", 
								 "border-weight":"1px", 
								 "border-style":"solid"});
				 $('td.toDateTd').append("<span > &nbsp; To Date should always be greater than From Date.</span >");
				 
			} else  { 
				if( fromDate1 > sysDate ) {
					$('#fromDate').css({"border-color": "red", 
									 "border-weight":"1px", 
									 "border-style":"solid"});
					 $('td.fromDateTd').append("<span > &nbsp; From Date should always be less than sysdate.</span >");
					 
				} 
				 
				if( toDate1 > sysDate) {
					$('#toDate').css({"border-color": "red", 
									 "border-weight":"1px", 
									 "border-style":"solid"});
					 $('td.toDateTd').append("<span > &nbsp; To Date should always be less than sysdate.</span >");
					 
				} else if(fromDate1 <= toDate1 
						&& fromDate!="" 
						&& toDate!="" 
						&& v_reportName != 'Not Required') {   
					 var v_region  = $("#region").text()  == null ? 'NO' :  setSelValues("region") ; 
					 var v_headOffice  = $("#headOffice").text() == null ? 'NO' :  setSelValues("headOffice") ; 
					 var v_Location = $("#Location").text() == null ? 'NO' :  setSelValues("Location") ; 
					 var v_userid =  $("#userid").val() == null  ? 'NO' :  setSelValues("userid") ;  
					 var v_bank = $("#bank option:selected").val();
					 var v_txnType =  $("#txnType").text()  == null ? '-NO-' :  setSelValues("txnType") ; 
					 var v_custprof =  $("#custprof").val() == null  ? '-NO-' :  setSelValues("custprof") ; 
					 
					 var v_reportName_1 = "";
					 var flag = false;
					 var extraParam = "";
						
					 v_region  = (v_region == '' || v_region.indexOf("Not Required") != -1 ) ? 'NO' :  v_region ; 
					 v_headOffice  = (v_headOffice == '' || v_headOffice.indexOf("Not Required") != -1 ) ? 'NO' :  v_headOffice ; 
					 v_Location = (v_Location == '' || v_Location.indexOf("Not Required") != -1 ) ? 'NO' :  v_Location ; 
					 v_userid =  (v_userid == '' || v_userid.indexOf("Not Required") != -1 ) ? 'NO' :  v_userid ; 
					 v_txnType = (v_txnType == '' || v_txnType.indexOf("Not Required") != -1 ) ? '-NO-' :  v_txnType ; 
					 v_custprof = (v_custprof == '' || v_custprof.indexOf("Not Required") != -1 ) ? '-NO-' :  v_custprof ; 
					 
					 //var queryBuild = "MMT.BANK_TERM_ID=AU.TERMID  AND MMT.BANK_MID=SUBSTR(AU.MERCHANT_ID,0,8)  AND A.MERC_ID=B.MERC_ID AND MMT.STORE_ID=B.STORE_ID AND A.STORE_ID=B.STORE_ID AND PBM.OFFICE_CODE=A.BANK_SID AND AA.REGION_CODE=PBM.REGION_CODE and TRUNC(AU.TXNREQDTTIME)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY')";
					 var dateCheck = " (TRUNC(LT.TXNDATE)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY')) ";
					 var pwalletStatDateCheck = " (TRUNC(WRT.TXNDATE)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY'))  ";
					// var pwalletCustDateCheck = " (TRUNC(WRT.TXNDATE)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY'))  ";
					 var queryBuild =  " ";
					 var queryBuild1 =  " ";
					 extraParam = "FROM_DATE@@"+fromDate+"##TO_DATE@@"+toDate+"##REPORT_TITLE@@";
					 var repArr = v_reportName.split("-");
					 
					 v_reportName = repArr[0];
					 v_reportName_1 = repArr[1];
					 $('#reportName').val(v_reportName_1);
					  
					if(v_reportName == '05'  			// client activity report
							|| v_reportName == '04' //
							
							|| v_reportName == '11' //
							|| v_reportName == '01' //
							|| v_reportName == '09' // supervisor summary report
							|| v_reportName == '12' // settlement report
							|| v_reportName == '14' // settlement report
							|| v_reportName == '15' // settlement report
							|| v_reportName == '16' // P-wallet Organization Balance Report
							|| v_reportName == '17' // P-wallet Customer Balance Report
							|| v_reportName == '18' // P-wallet Customer Balance Report
							|| v_reportName == '03'
							|| v_reportName == '13' //Pay Bill Report
							|| v_reportName == '20' //Branch Wise Transaction Rre
							) {
							
						if(v_reportName.indexOf('03') == -1 && v_reportName.indexOf('13') == -1 && v_reportName.indexOf('20') == -1) {
							queryBuild =  " LT.TERMINALNUMBER=TM.TERMINAL_ID AND MM.MERCHANT_ID=LT.MERCHANTID AND MM.MERCHANT_ID=SM.MERCHANT_ID and SM.STORE_ID=TM.STORE_ID AND LT.APPROVEDBY=ULC.LOGIN_USER_ID AND ULC.COMMON_ID=UINFO.COMMON_ID AND PBM.OFFICE_CODE=SM.LOCATION AND "+dateCheck;
							if(v_reportName == '05' 
								|| v_reportName == '11' 
								|| v_reportName == '12' 
								|| v_reportName == '04') {
								dateCheck += " AND PBM.OFFICE_CODE=UINFO.LOCATION AND LT.APPROVEDBY=ULC.LOGIN_USER_ID AND 	ULC.COMMON_ID=UINFO.COMMON_ID ";
							}
						} else if(v_reportName.indexOf('13') != -1){
							queryBuild =  "LT.TERMINALNUMBER=TM.TERMINAL_ID  and SM.STORE_ID=TM.STORE_ID AND LT.APPROVEDBY=ULC.LOGIN_USER_ID AND ULC.COMMON_ID=UINFO.COMMON_ID AND PBM.OFFICE_CODE=SM.LOCATION  and P.BILLER_ID=R.BILLER_ID and P.RRNO=LT.RRN AND LT.TXNCODE='50' AND R.BILLER_ID=BAM.BILLER_ID   AND"+dateCheck;
							
						}else {
							//dateCheck+=" ";
							queryBuild = dateCheck;
						}
						var revCond = " AND LT.RESPONSECODE='00' and LT.ISREVERSED='N' ";
						
						if(v_reportName == '05') {
							$('#QryKey').val('ALL_TRN_REP'); 
							$('#JRPTCODE').val('DAILY_ALL_POS_CLIENT_TRAN_RPT');  
						} else if(v_reportName == '11') {
							$('#QryKey').val('ALL_TRN_EXP_REP'); 
							$('#JRPTCODE').val('EXCEPTION_REPORT');
							queryBuild+=" AND NVL(LT.RESPONSECODE,'NO') <> '00' ";
							dateCheck+=" AND NVL(LT.RESPONSECODE,'NO') <> '00'  ";
						} else if(v_reportName == '04') {
							$('#QryKey').val('ALL_POS_TRN_REP');
							$('#JRPTCODE').val('DAILY_ALL_POS_TRAN_RPT');
							queryBuild+=revCond;
							dateCheck+=revCond;
						} else if(v_reportName == '12') {  
							//$('#QryKey').val('BRN_POS_TRN_REP');
							//$('#JRPTCODE').val('DAILY_BRN_POS_TRAN_RPT');
							$('#QryKey').val('SETTLE_REP');
							$('#JRPTCODE').val('SETTLEMENT_REPORT');
							queryBuild+=revCond;
							dateCheck+=revCond;
						} else if(v_reportName == '01') {
							$('#QryKey').val('BRNCH_TRN_REP');
							$('#JRPTCODE').val('DAILY_POS_BRANCH_RPT');
							queryBuild+=revCond;
							dateCheck+=revCond ;
						}else if(v_reportName == '03') {
							$('#QryKey').val('COMM_TRN_REP');
							$('#JRPTCODE').val('DAILY_POS_COMM_RPT');
							//queryBuild+=" AND BNK.BIN = LT.ACQUIRERBIN AND LT.RRN=FIN.TXN_REF_NO "+revCond+" AND FIN.TXN_TYPE LIKE 'F%' AND FIN.FINANCIAL_CODE not like 'BULK%' ";
							queryBuild+=" AND BNK.BIN = LT.ACQUIRERBIN  ";
							dateCheck+=" AND "+queryBuild;
						} else if(v_reportName == '09') { // For Branch summary report. 09
							$('#QryKey').val('SUP_TRN_REP');
							$('#JRPTCODE').val('SUPERVISOR_SUMMARY_RPT');
							queryBuild+=revCond;
							dateCheck+=revCond;
						}  else if(v_reportName == '14') { // Organization Statement Report
							$('#QryKey').val('PWAL_ORG_STAT_RPT');
							$('#JRPTCODE').val('PWAL_ORG_STAT_RPT'); 
							 queryBuild1=pwalletStatDateCheck +"  AND PBM.OFFICE_CODE=UINFO.LOCATION AND WRT.MAKER_ID=ULC.LOGIN_USER_ID AND ULC.COMMON_ID=UINFO.COMMON_ID AND WRT.STATUS='S' AND WRT.ORG_ID IS NOT NULL AND WRT.PROFILE_ID  IS NULL  "; 
							 
							 if(v_txnType != '-NO-') {
								 queryBuild1+="AND WRT.TXNTYPE in ("+v_txnType+") ";
							 }
						}  else if(v_reportName == '15') { // Customer Profile Statement Report
							$('#QryKey').val('PWAL_PROF_STAT_RPT');
							$('#JRPTCODE').val('PWAL_PROF_STAT_RPT'); 
							  queryBuild1=pwalletStatDateCheck+"  AND PBM.OFFICE_CODE=UINFO.LOCATION AND WRT.MAKER_ID=ULC.LOGIN_USER_ID AND ULC.COMMON_ID=UINFO.COMMON_ID AND WRT.STATUS='S' AND WP.PROFILEID = WRT.PROFILE_ID AND WRT.ORG_ID IS NULL   "; 
							  
							  if(v_txnType != '-NO-') {
								 queryBuild1+=" AND WRT.TXNTYPE in ("+v_txnType+") ";
							  } 
							  if(v_custprof != '-NO-') {
								 queryBuild1+="  AND WP.PROFILEID in ("+v_custprof+") ";
							   }
						}  else if(v_reportName == '16') { // Organization Balance report
							$('#QryKey').val('PWAL_ORG_RPT');
							$('#JRPTCODE').val('PWAL_ORG_RPT');
							queryBuild1 = " WO.ORGANIZATIONID=WOP.ORGANIZATIONID AND WP.PROFILEID  = WOP.PROFILEID AND WO.ORGANIZATIONID  = WOB.ORGANIZATION_ID  ";
						}  else if(v_reportName == '17') { // Customer Balance report
							$('#QryKey').val('PWAL_PROF_BAL_RPT');
							$('#JRPTCODE').val('PWAL_PROF_BAL_RPT');
							queryBuild1 = " WP.PROFILEID=WPB.PROFILE_ID  ";
							
							if(v_custprof != '-NO-') {
								 queryBuild1+="  AND WP.PROFILEID in ("+v_custprof+") ";
							}
						}  else if(v_reportName == '18') { // Customer activity report
							$('#QryKey').val('PWAL_ACTIVITY_STAT_RPT');
							$('#JRPTCODE').val('PWAL_ACTIVITY_STAT_RPT'); 
							  queryBuild1=pwalletStatDateCheck+"  AND PBM.OFFICE_CODE=UINFO.LOCATION AND WRT.MAKER_ID=ULC.LOGIN_USER_ID AND ULC.COMMON_ID=UINFO.COMMON_ID AND WP.PROFILEID = WRT.PROFILE_ID  "; 
							  
							  if(v_txnType != '-NO-') {
								 queryBuild1+=" AND WRT.TXNTYPE in ("+v_txnType+") ";
							  } 
							  if(v_custprof != '-NO-') {
								 queryBuild1+="  AND WP.PROFILEID in ("+v_custprof+") ";
							   }
						}if(v_reportName == '13') {
							$('#QryKey').val('PAY_BILL_DET'); 
							$('#JRPTCODE').val('PAY_BILL_DET');  
						} if(v_reportName == '20') {
							$('#QryKey').val('BRANCH_WISE_TRANS_SUMMERY'); 
							$('#JRPTCODE').val('BRANCH_WISE_TXN_SUMMARY');  
						} 
						
						extraParam+=v_reportName_1.toUpperCase();
							
						<%
							if(checkTeller.equalsIgnoreCase("USER")) {	
						%>
							var qry = " AND upper(lt.APPROVEDBY) ='<%= (String)session.getAttribute(CevaCommonConstants.MAKER_ID) %>' ";
							queryBuild +=qry;
							dateCheck +=qry;
						<% } else if(checkTeller.equalsIgnoreCase("Branch Office") 
								|| checkTeller.equalsIgnoreCase("Branches")) { 
						%> 
								if(v_userid == 'NO') {  
									queryBuild +=" AND LT.APPROVEDBY in ("+userdata+") ";
									dateCheck +=" AND LT.APPROVEDBY in ("+userdata+") ";
									
								} else { 
									queryBuild +=" AND LT.APPROVEDBY in ("+v_userid+") ";
									dateCheck +=" AND LT.APPROVEDBY in ("+v_userid+") ";
								} 
						<% } else {  %> 
								if(v_reportName != '03'
									|| v_reportName != '01') {
									if(v_userid == 'NO') {  
										if(v_Location == 'NO') { 
											if(v_region != 'NO'  
													&& v_headOffice == 'NO' ) {  
												queryBuild += "AND PBM.REGION_CODE in ( "+v_region +" )";  
												if(v_reportName == '05' 
													|| v_reportName == '04') {
													dateCheck += "AND PBM.REGION_CODE in ( "+v_region +" )";  
												}
											 } else if((v_region != 'NO' 
													|| v_headOffice != 'NO')  ) {
													 
												queryBuild += "AND PBM.HPO_DEPARTMENT_CODE in ("+v_headOffice+")";  
												
												if(v_reportName == '05' 
													|| v_reportName == '04') {
													dateCheck += "AND PBM.HPO_DEPARTMENT_CODE in ("+v_headOffice+")";  
												}
											 }  
										} else	{
											queryBuild += " AND PBM.OFFICE_CODE in ("+v_Location+") and PBM.HPO_FLAG is null "; 
											if(v_reportName == '05' 
													|| v_reportName == '04') {
												dateCheck += " AND PBM.OFFICE_CODE in ("+v_Location+") and PBM.HPO_FLAG is null ";
											}
										}
									} else { 
										queryBuild +=" AND LT.APPROVEDBY in ("+v_userid+") "; 
										dateCheck +=" AND LT.APPROVEDBY in ("+v_userid+") "; 
									} 
								}
						<%  } %> 
						
					 
							if(v_txnType.indexOf("-NO-") == -1 
									&& v_txnType.indexOf("Not Required") == -1 ) { 
								queryBuild += " AND LT.TXNTYPE in ("+v_txnType+") ";
							} 
						 
							if(v_reportName != '01') {
								if(v_bank.indexOf("-NO-") == -1  
									&& v_bank.indexOf("Not Required") == -1) {
									
									if(v_reportName.indexOf('03') == -1 ) {
										queryBuild += " AND LT.BANKID IN ('"+v_bank+"') ";
										dateCheck += " AND LT.BANKID IN ('"+v_bank+"') ";
									} else if(v_reportName.indexOf('03') != -1 ) {
										//queryBuild += " AND LT.BANKID IN ('"+v_bank+"') AND FIN.FINANCIAL_CODE in (select FINANCIAL_CODE from FINANCIAL_MASTER where TRANS_TYPE='F' and BANK_CODE IN ('"+v_bank+"') ) ";
										queryBuild += " AND LT.BANKID IN ('"+v_bank+"') ";
										//dateCheck += " AND LT.BANKID IN ('"+v_bank+"') AND FIN.FINANCIAL_CODE in (select FINANCIAL_CODE from FINANCIAL_MASTER where TRANS_TYPE='F' and BANK_CODE IN ('"+v_bank+"') ) ";
										dateCheck += " AND LT.BANKID IN ('"+v_bank+"') ";
									}
								}
							}
							
							if(v_reportName != '01') {
								if(v_txnType.indexOf("-NO-") == -1) { 
									dateCheck += " AND LT.TXNTYPE in ("+v_txnType+") ";
								}
							} else {
								queryBuild += " AND LT.TXNTYPE in ('CDP','WDL') ";
								dateCheck += " AND LT.TXNTYPE in ('CDP','WDL') ";
							}	  
						<%
							if(checkTeller.equalsIgnoreCase("USER") 
								|| checkTeller.equalsIgnoreCase("Branch Office") 
								|| checkTeller.equalsIgnoreCase("Branches")) {	
						%>
							queryBuild += " AND LT.TXNTYPE in ('CDP','WDL') ";
							dateCheck += " AND LT.TXNTYPE in ('CDP','WDL') ";
							$("#mode").val("pdf");
						<% } else if (userGroup.equalsIgnoreCase("TREASURY")) { %> 
							$("#mode").val("pdf,html,xls");
						<% }  else { %> 
							$("#mode").val("pdf,html");
						<% } %> 
						
						//The below logic is added to view the Supervisor Transactions Reports
						<%
							if(checkTeller.equalsIgnoreCase("Branch Office")) {	
						%>
							queryBuild += " OR LT.APPROVEDBY in ('<%=(String)session.getAttribute(CevaCommonConstants.MAKER_ID) %>') ";
							dateCheck += " OR LT.APPROVEDBY in ('<%=(String)session.getAttribute(CevaCommonConstants.MAKER_ID) %>') ";
 						<% } %> 
						
						queryBuild += " and LT.BANKID NOT IN ('MPESAKEN','DEPOSITS') ";
						dateCheck += " and LT.BANKID NOT IN ('MPESAKEN','DEPOSITS') ";
						
						if(v_reportName.indexOf('01') == -1 
							&& v_reportName.indexOf('03') == -1
							&& v_reportName.indexOf('09') == -1  
							&& v_reportName.indexOf('12') == -1
							&& v_reportName.indexOf('13') == -1
							&& v_reportName.indexOf('20') == -1) {
								queryBuild +=" ORDER BY LT.TXNDATE ) ORDER BY SNO,TXN_DATE_TIME "; 
						} else if(v_reportName.indexOf('03') != -1 ) { 
							//queryBuild += " ORDER BY BNK.BANK_CODE,LT.TXNDATE ";
							queryBuild += "  ORDER BY BNK.BANK_NAME,LT.TXNDATE ) ORDER BY SNO ";
						}  else if(v_reportName.indexOf('01') != -1 ) { 
							queryBuild += " ) GROUP BY TXN_DESC,BRANCH,HONAME,REGION ORDER BY BRANCH ";
						}  else if(v_reportName.indexOf('09') != -1 ) { 
							queryBuild += " ) GROUP BY USER_ID,TXN_DESC,BRANCH,HONAME,REGION ORDER BY USER_ID ";
						}  else if(v_reportName.indexOf('12') != -1 ) { 
							queryBuild += " AND LT.TXNTYPE not in ('BEQ') ";
							queryBuild +=" ORDER BY LT.TXNDATE ) ORDER BY BANK,TXN_DATE_TIME "; 
						}  else if(v_reportName.indexOf('13') != -1 ) { 
							queryBuild +="  ORDER BY LT.TXNDATE desc"; 
						} 
						
						
						flag = true;
						
					} else { 
						alert(v_reportName_1 + ' is in progress.');
					} 
					if(flag) {
						
							if(v_reportName.indexOf('14') !=-1 
								|| v_reportName.indexOf('15') !=-1 
								|| v_reportName.indexOf('16') !=-1  
								|| v_reportName.indexOf('17') !=-1  
								|| v_reportName.indexOf('18') !=-1 ) { 
								
								if(v_reportName.indexOf('14') !=-1 
										|| v_reportName.indexOf('15') !=-1  
										|| v_reportName.indexOf('18') !=-1 
										|| v_reportName.indexOf('13') !=-1 ) { 
									queryBuild1+=" ORDER BY WRT.TXNDATE DESC ";	
								}
								
								$('#queryconditions').val(queryBuild1); 
								
								//console.log(queryBuild1);
						} else {
							$('#queryconditions').val(queryBuild); 
						}
						
						$('#dateCheck').val(dateCheck);
						$('#eparam').val(extraParam);								
						
						$("#form1")[0].action="${pageContext.request.contextPath}/<%=appName %>/reportsall.action";
						$('#form1').submit(); 
					}
				}  
			}
		}); // End of OnClick Event 				 
	}); // End Of Ready Function  
</script>
	 
<input type="hidden" name="querymode" id="querymode" value="page" />
<input type="hidden" name="JRPTCODE" id="JRPTCODE" value='' />
<input type="hidden" name="mode" id="mode" value="pdf,html" />
<input type="hidden" name="extrafields" id="extrafields" value="" />
<input type="hidden" name="queryconditions"  id="queryconditions" value="" />
<input type="hidden" name="QryKey" id="QryKey" value="" />
<input type="hidden" name="eparam" id="eparam" value="" />  
<input type="hidden" name="dateCheck" id="dateCheck" value="" />  
<input type="hidden" name="reportName" id="reportName" value="" />  
		
	</div><!--/#content.span10-->
</form>	 
</body> 
</html>