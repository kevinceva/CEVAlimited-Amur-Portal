
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<% String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<% String checkTeller= session.getAttribute("userLevel").toString() == null ? "NO_VALUE" : session.getAttribute("userLevel").toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Create Entity</title>


<style type="text/css">
select#region,
select#headOffice,
select#Location,
select#userid,
select#bank,
select#reportname,
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

function getToday()
{
	var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1;
    var yyyy = today.getFullYear();

    if(dd<10){
        dd='0'+dd;
    }
    if(mm<10){
        mm='0'+mm;
    }

    return dd+"/"+mm+"/"+yyyy;
}


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
				'reportname' : '${responseJSON.reportname}'
		 };
$(function(){

		$(list).each(function(i,val){
			$('#'+val).datepicker(datepickerOptions);
		});

		 console.log(getToday());
		$('#fromDate').val(getToday());
		$('#toDate').val(getToday());

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
			<li><a href="#">Mobile Reports</a>
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
			 <i class="icon-edit"></i> Reports
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
			</div>
		</div>
		<div class="box-content" >
			<fieldset >
				<table width="100%" border="0"  cellpadding="5" cellspacing="5" id="table table-striped table-bordered bootstrap-datatable" > 
				
				<tr id="tr-region">				
					<td  width="8%">&nbsp;</td>
					<td  width="11%"><label for="Region"><strong>Select Report</strong></label></td>
					<td  width="81%" class="regionTd">
					<select name="reportname" id="reportname" class="chosen" width="450px"></select><span class='region-spn'></span></td> 
					 
				</tr> 

				<tr  id="tr-accid">
					<td >&nbsp;</td>
					<td><label for="Report Name"><strong>Account Number</strong></label></td>
					<td class="reportnameTd1"><input name="accid" id="accid"  type="text" style="width: 280px;" class="field"   /></td>
				</tr>
				<tr>
					<td >&nbsp;</td>
					<td><label for="Report Name"><strong>Biller ID</strong></label></td>
					<td class="reportnameTd2"><input name="billerid" id="billerid"  type="text"  style="width: 280px;" class="field"   /></td>
				</tr>
				<tr id="tr-fromdate" >
					<td >&nbsp;</td>
					<td> <label for="From Date"><strong>From Date<font color="red">*</font></strong></label></strong></td>
					<td class="fromDateTd"><strong><input name="name" type="text"  id="fromDate" name="fromDate"  ></strong></td>
				</tr>
				<tr>
					<td >&nbsp;</td>
					<td> <label for="To Date"><strong>To Date<font color="red">*</font></strong></label></strong></td>
					<td class="toDateTd"><strong> <input name="name" type="text"  id="toDate" name="toDate"  ></strong></td>
				</tr>  
		 </table>
	</fieldset> 

			<div class="form-actions" align="center">
				  <input type="button" class="btn btn-primary" type="text"  name="save" id="save" value="Generate Report" width="100" ></input>
				  <input type="button" class="btn" type="text"  name="save" id="save" value="Cancel" width="100" ></input>
			 </div>
		</div>
		</div> 


 <script type="text/javascript"><!--

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
			//alert("00"+v_reportName);

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
				 $('td.toDateTd').append("<span > &nbsp; ToDate should always be greater than From Date</span >");

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

					 var v_reportName_1 = "";
					 var flag = false;
					 var extraParam = "";


					 //var queryBuild = "MMT.BANK_TERM_ID=AU.TERMID  AND MMT.BANK_MID=SUBSTR(AU.MERCHANT_ID,0,8)  AND A.MERC_ID=B.MERC_ID AND MMT.STORE_ID=B.STORE_ID AND A.STORE_ID=B.STORE_ID AND PBM.OFFICE_CODE=A.BANK_SID AND AA.REGION_CODE=PBM.REGION_CODE and TRUNC(AU.TXNREQDTTIME)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY')";
					 var dateCheck = " (TRUNC(T.TXNDATE)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY')) ";
					 var queryBuild =  " ";
					 var subQueryBuild = "";
					 // extraParam = "FROM_DATE@@"+fromDate+"##TO_DATE@@"+toDate+"##REPORT_TITLE@@";
					//alert("11"+v_reportName);
					 var repArr = v_reportName.split("-");

					 v_reportName = repArr[0];
					 v_reportName_1 = repArr[1];
					 $('#reportName').val(v_reportName_1);

					/* 
					16		Terminals set up report.
					17		Merchant settlement report-Summary
					18		New Registered Merchant/Agent Daily/Monthly/Yearly
					19      Service Tax Transaction Log
					 
						if(v_reportName == '01' ){
							//   01		Customer Transaction Log
						      var str1=ClientActivityReport();
							  queryBuild =str1+" and "+dateCheck+" ";
							  subQueryBuild=str1+" and "+dateCheck+" ";

								$('#QryKey').val('CUSTOMER_TRANS_LOG_REP');
								$('#JRPTCODE').val('CUSTOMER_TRANS_LOG');
								extraParam = "FROM_DATE@@"+fromDate+"##TO_DATE@@"+toDate+"##REPORT_TITLE@@MPESA Transaction Log";
							    flag=true;

						}*/
						if(v_reportName == '13' ){
							//   01		ALL REPORTS 
						      var str1=ClientActivityReport();
							    queryBuild = " T.RRN = P.RRNO   and  R.BILLER_ID=P.BILLER_ID  ";
							  queryBuild +=str1+" and "+dateCheck+" ";
							  subQueryBuild=str1+" and "+dateCheck+" ";
 

								$('#QryKey').val('PAY_BILL_RPT');
								$('#JRPTCODE').val('PAY_BILL_RPT');
								extraParam = "FROM_DATE@@"+fromDate+"##TO_DATE@@"+toDate+"##REPORT_TITLE@@PAYBILL REPORT";
							    flag=true;

						} else{
							alert('Report is in process.');
						}
				     if(flag) {
							$('#queryconditions').val(queryBuild);
							$('#dateCheck').val(dateCheck);
							$('#eparam').val(extraParam);
							$('#subQueryConditions').val(subQueryBuild);

							$("#form1")[0].action="${pageContext.request.contextPath}/<%=appName %>/reportsall.action";
							$('#form1').submit();
						}
				}
			}
		}); // End of OnClick Event
	}); // End Of Ready Function

	function ClientActivityReport(){
		 var v_region  = $("#region").text()  == null ? 'NO' :  setSelValues("region") ;
		 var v_accid =  $("#accid").val();
		 var v_bid =  $("#billerid").val();
		 
		 v_region  = (v_region == '' || v_region.indexOf("Not Required") != -1 ) ? 'NO' :  v_region ;
		queryBuild =  " ";

		 if(v_accid !='') { queryBuild +=" and P.ACCOUNT_NO ='"+v_accid+"'"; }
		 if(v_bid !='') { queryBuild +=" and R.BILLER_ID ='"+v_bid+"'"; }
	

		return queryBuild;
	}

</script>

		<input type="hidden" name="querymode" id="querymode" value="page" />
		<input type="hidden" name="JRPTCODE" id="JRPTCODE" value='' />
		<input type="hidden" name="mode" id="mode" value="pdf,xls,csv" />
		<!--<input type="hidden" name="mode" id="mode" value="pdf,html,xls,csv" />-->
		<input type="hidden" name="extrafields" id="extrafields" value="" />
		<input type="hidden" name="queryconditions"  id="queryconditions" value="" />
		<input type="hidden" name="QryKey" id="QryKey" value="" />
		<input type="hidden" name="eparam" id="eparam" value="" />
		<input type="hidden" name="dateCheck" id="dateCheck" value="" />
		<input type="hidden" name="subQueryConditions" id="subQueryConditions" value=" " />
		<input type="hidden" name="reportName" id="reportName" value="" />

	</div><!--/#content.span10-->
</form>
</body>
</html>