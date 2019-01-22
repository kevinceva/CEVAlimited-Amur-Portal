<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<% String checkTeller= session.getAttribute("userLevel").toString() == null ? "NO_VALUE" : session.getAttribute("userLevel").toString(); %>
<% String userGroup= session.getAttribute("userGroup").toString() == null ? "NO_VALUE" : session.getAttribute("userGroup").toString(); %>

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
				'reportname' : '${responseJSON.reportname}'
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
		 /* 		 
			  if(selectKey == 'cid') { 
				// To Iterate the Select box ,and setting the in-putted as selected 
				$('#'+selectKey).find('option').each(function( i, opt ) { 
				    if( opt.value === 'KCB' ) 
				        $(opt).attr('selected', 'selected');
				});
			 } */
		 
		});
 		
 		 
		
	 
/* 	$("select#region, select#headOffice, select#Location").live("change",function(){ 
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
			 
		});  */ 
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
		 
		$('#save').click(function () { 
				 
			$('#toDate').removeAttr('style'); 
			$('td.toDateTd > span').text('');
			
			$('#fromDate').removeAttr('style');
			$('td.fromDateTd > span').text('');
			
			$('#reportname').removeAttr('style');
			$('td.reportnameTd > span').text('');
			 
			var v_reportName = $("#reportname option:selected").text();
			 
			var fromDate = $("#fromDate").val();
			var toDate = $("#toDate").val();
			var mkrid="<%=(String)session.getAttribute(CevaCommonConstants.MAKER_ID) %>";
		   
			var fromDate1 = new Date(splitDate(fromDate));
			var toDate1 = new Date(splitDate(toDate));
			var sysDate = new Date();
			
			 //alert("value of params:"+fromDate1+"-"+toDate1+"-"+v_reportName+"mkr-------"+mkrid); 
			
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
					 var v_bank = $("#bank option:selected").val();
					 var v_cid  = $("#cid").val()  == null ? '-NO-' :  $("#cid").val() ; 
					 var v_mob  = $("#mob").val()  == null ? '-NO-' :  $("#mob").val() ; 
					 var v_pno  = $("#pno").val()  == null ? '-NO-' :  $("#pno").val() ; 
			 
					 var v_reportName_1 = "";
					 var flag = false;
					 var extraParam = "";
			
					 v_cid  = (v_cid == '' || v_cid.indexOf("Not Required") != -1 ) ? '-NO-' :  v_cid ;
					 v_pno  = (v_pno == '' || v_pno.indexOf("Not Required") != -1 ) ? '-NO-' :  v_pno ;
					 v_mob  = (v_mob == '' || v_mob.indexOf("Not Required") != -1 ) ? '-NO-' :  v_mob ;
					 
/* 					 alert("value of inputs :"+v_cid+"-"+v_pno+"-"+v_mob);
 */					 
					 //var queryBuild = "MMT.BANK_TERM_ID=AU.TERMID  AND MMT.BANK_MID=SUBSTR(AU.MERCHANT_ID,0,8)  AND A.MERC_ID=B.MERC_ID AND MMT.STORE_ID=B.STORE_ID AND A.STORE_ID=B.STORE_ID AND PBM.OFFICE_CODE=A.BANK_SID AND AA.REGION_CODE=PBM.REGION_CODE and TRUNC(AU.TXNREQDTTIME)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY')";
					 var ciddateCheck = " (TRUNC(cc.DATE_CREATED)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY')) ";
					 var pnoDateCheck = " (TRUNC(CP.DATE_STARTED)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY'))  ";
					 var purDateCheck = " (TRUNC(CPP.DATE_CREATED)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY'))  ";
					 var biddateCheck = " (TRUNC(cb.DATE_CREATED)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY')) ";
					 var gendateCheck = " (TRUNC(DATE_CREATED)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY')) ";
					 var inactiveCond = " and cc.client_id=cp.client_id and cc.client_id=cb.client_id and 	CP.STATUS<>'PS' ";
					 var activeCond = " and cc.client_id=cp.client_id and 	CP.STATUS='PS' ";
					 var benfcond = " and cc.client_id=cb.client_id ";
					 var purchasecond = " and Cc.Client_Id=Cp.Client_Id and Cp.Policy_Id=Cpp.Policy_Id ";
					 
					// var pwalletCustDateCheck = " (TRUNC(WRT.TXNDATE)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY'))  ";
					 var queryBuild =  " ";
					 var queryBuild1 =  " ";
					 extraParam = "FROM_DATE@@"+fromDate+"##TO_DATE@@"+toDate+"##MKR_ID@@"+mkrid+"##REPORT_TITLE@@";;
					 //var repArr = v_reportName.split("-");
					 
					 console.log("v_reportName ["+v_reportName+"]");
					 v_reportName_1 = v_reportName;
					 $('#reportName').val(v_reportName_1);
					 
					 extraParam+=v_reportName_1.toUpperCase();
					  
					if( v_reportName == '2' // client report
							|| v_reportName == '1' // active policies report
							|| v_reportName == '3' // inactive policies Report
							|| v_reportName == '4'  //purchase report
							|| v_reportName == '5'  //beneficiary report
							|| v_reportName == '6'  //airtime report
							) {
							
						if(v_reportName == '2' ) {
							$('#QryKey').val('CLIENT_REPORT'); 
							$('#JRPTCODE').val('CLIENT_REPORT');
							queryBuild=ciddateCheck+" "+benfcond; 
							if(v_cid != '-NO-') {
								queryBuild+=" AND cc.CLIENT_ID="+v_cid;
								 extraParam+="##CID@@"+v_cid;
							  }
							if(v_mob != '-NO-') {
								queryBuild+=" AND cc.mobile_number="+v_mob;
								extraParam+="##MOB@@"+v_mob;
							  }	
							queryBuild+="  ORDER BY cc.DATE_CREATED DESC ";
						} else if(v_reportName == '1') {
							$('#QryKey').val('ACTIVE_POLICY_REPORT'); 
							$('#JRPTCODE').val('ACTIVE_POLICY_REPORT');
							queryBuild=pnoDateCheck+" "+activeCond;
							if(v_cid != '-NO-') {
								queryBuild+=" AND CC.CLIENT_ID="+v_cid;
								 extraParam+="##CID@@"+v_cid;
							  }
							if(v_mob != '-NO-') {
								queryBuild+=" AND CC.mobile_number="+v_mob;
								extraParam+="##MOB@@"+v_mob;
							  }
							queryBuild+="  ORDER BY CP.DATE_STARTED DESC ";
							
						} else if(v_reportName == '3') {
							$('#QryKey').val('INACTIVE_POLICY_REPORT');
							$('#JRPTCODE').val('INACTIVE_POLICY_REPORT');
							queryBuild=pnoDateCheck+" "+inactiveCond;
							if(v_cid != '-NO-') {
								queryBuild+=" AND CC.CLIENT_ID="+v_cid;
								 extraParam+="##CID@@"+v_cid;
							  }
							if(v_mob != '-NO-') {
								queryBuild+=" AND CC.mobile_number="+v_mob;
								extraParam+="##MOB@@"+v_mob;
							  }
							queryBuild+="  ORDER BY CP.DATE_STARTED DESC ";
						}else if(v_reportName == '4') {
							$('#QryKey').val('PURCHASE_REPORT');
							$('#JRPTCODE').val('PURCHASE_REPORT');
							queryBuild=purDateCheck+" "+purchasecond;
							if(v_cid != '-NO-') {
								queryBuild+=" AND CC.CLIENT_ID="+v_cid;
								 extraParam+="##CID@@"+v_cid;
							  }
							if(v_mob != '-NO-') {
								queryBuild+=" AND CC.mobile_number="+v_mob;
								extraParam+="##MOB@@"+v_mob;
							  }
							queryBuild+="  ORDER BY CPP.DATE_CREATED DESC ";
						}else if(v_reportName == '5') {
							$('#QryKey').val('BENEFICIARY_REPORT'); 
							$('#JRPTCODE').val('BENEFICIARY_REPORT');
							queryBuild=biddateCheck+" "+benfcond ; 
							if(v_cid != '-NO-') {
								queryBuild+=" AND cb.CLIENT_ID="+v_cid;
								 extraParam+="##CID@@"+v_cid;
							  }
							if(v_mob != '-NO-') {
								queryBuild+=" AND cb.mobile_number="+v_mob;
								extraParam+="##MOB@@"+v_mob;
							  }	
							queryBuild+="  ORDER BY CB.DATE_CREATED DESC "; 
						}else if(v_reportName == '6') {
							$('#QryKey').val('AIRTIME_REPORT'); 
							$('#JRPTCODE').val('AIRTIME_REPORT');
							queryBuild=gendateCheck; 
							if(v_mob != '-NO-') {
								queryBuild+=" AND policy_id="+v_mob;
								extraParam+="##MOB@@"+v_mob;
							  }	
							queryBuild+="  ORDER BY DATE_CREATED DESC "; 
						}
						
						
						
						flag = true;
						
					}else if (v_reportName == 'Orders Net Sales')
					{
						$('#QryKey').val('ORDERS_NET_SALES'); 
						$('#JRPTCODE').val('ORDERS_NET_SALES');	
						queryBuild+=" OM.ORDER_STATUS=OS.ID and (TRUNC(OM.TXN_DATE)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY'))  order by OM.TXN_DATE ";
						flag = true;
					}else if (v_reportName == 'Riders Orders Summary')
					{
						$('#QryKey').val('RIDERS_ORDERS_SUMMARY'); 
						$('#JRPTCODE').val('RIDERS_ORDERS_SUMMARY');	
						queryBuild+=" ma.rid=mo.rid and (TRUNC(mo.td)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY'))  group by ma.ln order by ma.ln ";
						flag = true;
					}else if (v_reportName == 'Riders Order Details')
					{
						$('#QryKey').val('RIDERS_ORDERS_DETAILS'); 
						$('#JRPTCODE').val('RIDERS_ORDERS_DETAILS');	
						queryBuild+=" RO.ORDER_ID=OM.ORDER_ID and (TRUNC(OM.TXN_DATE)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY'))  order by OM.TXN_DATE ";
						flag = true;
					}else if (v_reportName == 'Customer Wallet And Earnings')
					{
						$('#QryKey').val('CUSTOMER_WALLET_EARNINGS'); 
						$('#JRPTCODE').val('CUSTOMER_WALLET_EARNINGS');	
						queryBuild+=" CM.CIN=AE.CIN and (TRUNC(CM.CREATED_DATE)   between  to_date('"+fromDate+"','DD/MM/YYYY') AND  to_date('"+toDate+"','DD/MM/YYYY'))  ORDER BY (CM.FNAME||' '||CM.LNAME) ";
						flag = true;
					}	
					else { 
						alert(v_reportName_1 + ' is in progress.');
					} 
					if(flag) {
						
						$('#queryconditions').val(queryBuild);
						$('#dateCheck').val(ciddateCheck);
						$('#eparam').val(extraParam);
					/* 	alert("values of 1 :"+$('#dateCheck').val());
						alert("values of 2 :"+$('#eparam').val()); */
						alert("Generate report pressed.");
						$("#form1")[0].action="${pageContext.request.contextPath}/<%=appName %>/cicreportsall.action";
						$('#form1').submit(); 
					}
				}  
			}
		}); // End of OnClick Event 				 
	}); // End Of Ready Function  
</script>
	 
<input type="hidden" name="querymode" id="querymode" value="page" />
<input type="hidden" name="JRPTCODE" id="JRPTCODE" value='' />
<input type="hidden" name="mode" id="mode" value="pdf,html,xls" />
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