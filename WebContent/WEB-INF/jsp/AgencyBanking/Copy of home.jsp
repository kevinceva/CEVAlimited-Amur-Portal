 
<%@page
	import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 
<meta charset="utf-8">
<title>CEVA</title> 
<link rel="shortcut icon" href="<%=ctxstr%>/images/<%=appName %>/favicon.ico" />


<style type="text/css">
body {
	padding-bottom: 40px;
}

.sidebar-nav {
	padding: 3px 0;
}

#nav {
	float: left;
	min-width: 200px;
	/*color:#ffffff;*/
	/* border-top: 1px solid #999;
    border-right: 1px solid #999;
    border-left: 1px solid #999;*/
	list-style: none;
}

#nav li a {
	/*display: block;*/
	/* padding: 10px 15px;*/
	/*background: #AC4A02;*/
	/* border-top: 1px solid #eee;
    border-bottom: 1px solid #999;*/
	text-decoration: none;
	/*color: #ffffff;*/
	list-style: none;
}

#nav li a:hover,#nav li a.active {
	/*background: #FC8023;*/
	/* color: #fff;*/
	
}

#nav li ul {
	display: none;
	list-style: none;
}

#nav li ul li a {
	/*background: #D35B03;*/
	list-style: none;
}
.amtclass {
	font-weight : bold;
} 

 
.td-bold{
	font-size: 0.8em; font-family:Tahoma;
	font-weight : bold;
}
</style>
<s:set value="responseJSON1" var="respData"/>

 <s:if test="#respData.size == 0" >
 	<s:set value="commonBean.responseJSON1" var="respData"/>
 </s:if>
 
<script type="text/javascript">

var halfPageDashboard ='${responseJSON1.halfpagedata}'; 
if(halfPageDashboard == '') {
	halfPageDashboard ='${commonBean.responseJSON1.halfpagedata}';
}
var json = jQuery.parseJSON(halfPageDashboard);

var dashBoardFun = '${responseJSON.dashboarddata}';
if(dashBoardFun == '') {
	dashBoardFun ='${commonBean.responseJSON1.halfpagedata}';
}
var json1 = jQuery.parseJSON(dashBoardFun);

window.setTimeout( refreshGrid, 125000);
var oTable;
function refreshGrid() { 
    var formInput='';
	json = "";
 	$.getJSON('<%=ctxstr%>/<%=appName %>/livedata.action', 
				formInput,
		function(data) {
			json =  data.responseJSON1.halfpagedata; 
			oTable = $('#DataTables_Table_0').dataTable();
   			var oSettings = oTable.fnSettings();
   			var length = oSettings._iDisplayLength;
   			var pageCurr = parseInt($('.active a').text(), 0); 
   	    	oTable.fnDestroy();
   	    	
			fillJqGrid(json);
			$('#DataTables_Table_0').dataTable({
 				"processing": true,
			    "serverSide": true,
			    "iDisplayLength": length,
			    /* "iPage": pageCurr,  
			   	"fnDrawCallback": function () {
			          alert( 'Now on page'+ this.fnPagingInfo().iPage );
			      }, */
		        "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span12'i><'span12 center'p>>",
				"sPaginationType": "bootstrap",
				/* "ajax": "livedata.action",
				"sServerMethod": "POST", */
				"bDestroy": true,
				"oLanguage": {
					"sLengthMenu":  "_MENU_ records per page"
				} 
		    }); 
 			oTable.fnPageChange(pageCurr,false);
 			
			fillFloatGrid(data.responseJSON1.FLOAT_DATA,data.responseJSON1.FLOAT_MSG);
			announceData(data.responseJSON1.USER_ANNOUNCEMENT ,data.responseJSON1.GROUP_ANNOUNCEMENT);
			
	});
	
	window.setTimeout(refreshGrid, 25000);
}
 
$(document).ready(function () { 
	fillJqGrid(json);
	dashBoardItems('');
	
	var userAnn = '${responseJSON1.USER_ANNOUNCEMENT}';
	var groupAnn = '${responseJSON1.GROUP_ANNOUNCEMENT}';
	
	if(userAnn == '' && groupAnn == '') {
		userAnn = '${commonBean.responseJSON1.USER_ANNOUNCEMENT}';
		groupAnn = '${commonBean.responseJSON1.GROUP_ANNOUNCEMENT}';
	}
	
	announceData(userAnn,groupAnn); 
 
	$( "#sortable" ).sortable();
	$( "#sortable" ).disableSelection();
 
	$( "#sortable1" ).sortable();
	$( "#sortable1" ).disableSelection(); 
	
});

//user-tbody
function undefinedcheck(data){
	return data == undefined  ? " " :  data;
}

function dashBoardItems(data) { 
	var val = 0;
	var valMul = 1;
	 
	var appendTxtmain ="";
	var appendTxt ="";
	$("#sortable1").html('');
	if( json1 != null ) {
		$.each(json1, function(i, v) {   
			if(v.val != undefined && v.key != undefined) { 
			
		 		var query_val=( v.val == undefined) ? "" : v.val;
				var query_key=(v.key == undefined) ? "" : v.key; 
				 
				if(val == 0) { 
					appendTxt +="<div class='sortable row-fluid' id='sortable'>";
				} 
				if( (4*valMul) == val ) { 
					appendTxt+="</div><div class='sortable row-fluid' id='sortable'>";
					valMul++;
				}
				appendTxt += "<a data-toggle='tooltip' title='Ksh 0' class='well span3 top-block' href='#'><span class='icon32 icon-color icon-briefcase'></span> <div>"+query_key+"</div> <div></div> <span class='notification'>"+query_val+"</span></a>";
				val++;
			}
	 	}); 
		
		appendTxtmain=appendTxt+"</div>"; 
		$("#sortable1").append(appendTxtmain);	
	} 

}

function announceData(data1,data2) { 

	$("#userannoumcement").html("");
	$("#groupannoumcement").html("");
	
	var userannouncement = data1;  
	if(userannouncement == "NOVAL"){
		$("#userannoumcement").text("");
	}else{
		data1="<h3>"+userannouncement+"</h3>";
		$("#userannoumcement").text("");
		$("#userannoumcement").append(data1);
	}
	
	var groupannouncement = data2;  
	if(groupannouncement == "NOVAL"){
		$("#groupannoumcement").text("");
	} else {
		data2="<h3>"+groupannouncement+"</h3>";
		$("#groupannoumcement").text("");
		$("#groupannoumcement").append(data2);
	} 
	
	textObject.flash($('.flash-colour'), 'colour', 900);
}

function fillJqGrid(data1) {  
	var val = 1;
	var rowindex = 0;
 	var addclass = "";
	//$("#user-tbody").html('');
	$("#user-tbody").empty();
	$.each(json, function(index, v) {
		
		if(val % 2 == 0 ) {
			addclass = "even";
			val++;
		} else {
			addclass = "odd";
			val++;
		} 
 		
 		var user_status = "";
		var text = undefinedcheck(v.status);
		 
		if(text.trim() == 'APPROVED') {
			user_status = "<a href='#' class='label label-success' index='"+rowindex+"'>"+text+"</a>"; 		 
		} else if(text.trim() == 'REVERSAL') {
			user_status = "<a href='#' class='label label-info' index='"+rowindex+"'>REVERSAL</a>"; 		
  		} else if(text.trim() == 'FAILED') {
			user_status = "<a href='#' class='label label-warning' index='"+rowindex+"'>DECLINED</a>"; 		
  		}   
		
		var appendTxt = "<tr class="+addclass+" id='"+rowindex+"' index='"+rowindex+"'> "+
			"<td>"+(++rowindex)+"</td>"+
			"<td>"+undefinedcheck(v.SERVICE)+" </td>"+	
			"<td>"+undefinedcheck(v.TXN_DESC)+"  </td>"+ 
			"<td class='hidden-phone'>"+undefinedcheck(v.CARD_NO)+" </td>"+
			"<td class='hidden-phone'>"+undefinedcheck(v.channel)+" </td>"+
			"<td>"+undefinedcheck(v.rrn)+" </td>"+
			"<td>"+undefinedcheck(v.posrrn)+" </td>"+
			"<td class='hidden-phone'>"+undefinedcheck(v.TXN_DATE_TIME)+" </td>"+
			"<td class='hidden-phone'>"+undefinedcheck(v.TXNAMT)+" </td>"+
			"<td class='hidden-phone'>"+undefinedcheck(v.MAKERID)+" </td>"+
			"<td class='center hidden-phone'>"+undefinedcheck(v.Branch)+" </td>"+
			"<td>"+ user_status+"</td> </tr>"; 
		$("#user-tbody").append(appendTxt);	
		rowindex = ++rowindex;  
	});
	
	//$('#DataTables_Table_0').DataTable().ajax.reload();
}   
 
function fillFloatGrid(data1,data2) {  
	var val = 1;
	var rowindex = 0;
 	var addclass = "";
	$("#float-tbody").html('');
	$.each(data1, function(key, v) { 
 		if(val % 2 == 0 ) {
			addclass = "even";
			val++;
		} else {
			addclass = "odd";
			val++;
		}  
		
		var appendTxt = "<tr class="+addclass+" id='"+rowindex+"' index='"+rowindex+"'> "; 
			if(data2 == "USER") {
				appendTxt+="<td>"+undefinedcheck(v.mer_id)+" </td>"+	
					"<td>"+undefinedcheck(v.str_id)+"  </td>"+ 
					"<td class='hidden-phone'>"+undefinedcheck(v.term_id)+" </td>"+
					"<td class='hidden-phone'>"+undefinedcheck(v.term_lmt)+" </td>"+
					"<td>"+undefinedcheck(v.term_curr_lmt)+" </td>"+
					"<td>"+undefinedcheck(v.channel)+" </td>"+ 
					"<td class='center hidden-phone'>"+undefinedcheck(v.serialno)+" </td>";
			} 	else {
				appendTxt+="<td>"+undefinedcheck(v.mer_id)+" </td>"+	
					"<td>"+undefinedcheck(v.str_id)+"  </td>"+ 
					"<td class='hidden-phone'>"+undefinedcheck(v.str_dpt_lmt)+" </td>"+
					"<td class='hidden-phone'>"+undefinedcheck(v.str_curr_amt)+" </td>"+
					"<td>"+undefinedcheck(v.curr_csh_dtp_lmt)+" </td>"+
					"<td>"+undefinedcheck(v.rec_amt)+" </td>"+
					"<td class='hidden-phone'>"+undefinedcheck(v.channel)+" </td>"+
					"<td class='hidden-phone'>"+undefinedcheck(v.tot_cdp_amt)+" </td>"+
					"<td class='hidden-phone'>"+undefinedcheck(v.tot_wdl_amt)+" </td>"+
					"<td class='center hidden-phone'>"+undefinedcheck(v.unall_amt)+" </td>";
			}		 
			appendTxt+="</tr>";  
			
		$("#float-tbody").append(appendTxt);	
		rowindex = ++rowindex;  
	});
}  

 	 
var textObject = {	
	delay : 300,
	effect : 'replace',
	classColour : 'blue',
	flash : function(obj, effect, delay) {
		if (obj.length > 0) {
			if (obj.length > 1) {
				jQuery.each(obj, function() {
					effect = effect || textObject.effect;
					delay = delay || textObject.delay;
					textObject.flashExe($(this), effect, delay);					
				});
			} else {
				effect = effect || textObject.effect;
				delay = delay || textObject.delay;
				textObject.flashExe(obj, effect, delay);
			}
		}
	},
	flashExe : function(obj, effect, delay) {
		var flash = setTimeout(function() {
			switch(effect) {
				case 'replace':
				obj.toggle();
				break;
				case 'colour':
				obj.toggleClass(textObject.classColour);
				break;
			}
			textObject.flashExe(obj, effect, delay);
		}, delay);
	}
}; 
</script>
<body> 
<form method="post" name="form1" id="form1"> 
	<div id="content" class="span10"> 	
		<div>
			<ul class="breadcrumb">
				<li><a href="home.action">Home</a> <span class="divider">&gt;&gt;</span> </li>
				<li><a href="#">Dashboard</a></li>
			</ul>
		</div> 
		<span class="container">
			<h6 class="flash-colour"><span id="groupannoumcement"></span></h6>
			<h6 class="flash-colour"><span id="userannoumcement"></span></h6> 
		</span> 
		<br/> 
		<div id="sortable1"></div>   
		
	<%-- 	<s:if test="#respData.FLOAT_MSG  == 'USER' || #respData.FLOAT_MSG  == 'BRANCH MANAGER'" >  --%>
			<div class="row-fluid sortable" id="float-data">  
				<div class="box span12" id="groupInfo">
				 <div class="box-header well" data-original-title>Float 
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content" id="user-data"> 
					<table class="table table-striped table-bordered bootstrap-datatable datatable" 
						id="DataTables_Table_1">
						<thead > 
							<tr>
								<s:if test="#respData.FLOAT_MSG  == 'USER'" > 
	 									<th>Merchant Id</th>
										<th>Store Id</th>
			 							<th class="hidden-phone">Terminal Id</th>
										<th>Terminal Limit</th>
										<th>Terminal Current Limit</th>
										<th class="hidden-phone">Channel</th>
										<th class="hidden-phone">Serial No</th>
								</s:if>
								<s:else > 
	 									<th>Merchant Id</th>
										<th>Store Id</th>
			 							<th class="hidden-phone">Store Deposit Limit</th>
										<th>Store Current Amount</th>
										<th>Current Cash Deposit Limit</th>
										<th>Recovery Amount</th>
										<th class="hidden-phone">Channel</th>
										<th class="hidden-phone">Total Cash Deposit Limit</th>
										<th class="hidden-phone">Total Withdrawal Limit</th>
										<th class="hidden-phone">Un-Allocated Amount</th>
	 							</s:else> 
							</tr>
						</thead> 
						
						<tbody id="float-tbody">
	 							<s:iterator value="#respData['FLOAT_DATA']" var="floatDetails" status="floatStatus"> 
	 								<s:if test="#floatStatus.even == true" > 
												<s:set value="%{'even'}" var="flag"/> 
		 										 </s:if>
												 <s:elseif test="#floatStatus.odd == true">
				      								<s:set value="%{'odd'}" var="flag"/> 	
				   								 </s:elseif> 
			   								 	<tr class="<s:property value='#flag' />" index="<s:property value='#floatStatus.index' />"  id="<s:property value='#floatStatus.index' />">
			   								 		<s:if test="#respData.FLOAT_MSG  == 'USER'" > 
			   								 			<td class='hidden-phone'><s:property value="#floatDetails['mer_id']" /></td>	 											 
														<td><s:property value="#floatDetails['str_id']" /></td> 
														<td class='hidden-phone'><s:property value="#floatDetails['term_id']" /></td>
														<td class='center hidden-phone'><s:property value="#floatDetails['term_lmt']" /></td>
														<td class='center hidden-phone'><s:property value="#floatDetails['term_curr_lmt']" /></td>
														<td class='center hidden-phone'><s:property value="#floatDetails['channel']" /></td>
														<td class='center hidden-phone'><s:property value="#floatDetails['serialno']" /></td>
			   								 		</s:if>
			   								 		<s:else > 
			   								 			<td class='hidden-phone'><s:property value="#floatDetails['mer_id']" /></td>	 											 
														<td><s:property value="#floatDetails['str_id']" /></td> 
														<td class='hidden-phone'><s:property value="#floatDetails['str_dpt_lmt']" /></td>
														<td class='center hidden-phone'><s:property value="#floatDetails['str_curr_amt']" /></td>
														
														<td class='hidden-phone'><s:property value="#floatDetails['curr_csh_dtp_lmt']" /></td>	 											 
														<td><s:property value="#floatDetails['rec_amt']" /></td> 
														<td class='hidden-phone'><s:property value="#floatDetails['channel']" /></td>
														<td class='center hidden-phone'><s:property value="#floatDetails['tot_cdp_amt']" /></td>
														
														<td class='center hidden-phone'><s:property value="#floatDetails['tot_wdl_amt']" /></td>
														<td class='center hidden-phone'><s:property value="#floatDetails['unall_amt']" /></td>
			   								 		</s:else>	 
			   								 	</tr>
								</s:iterator>
						</tbody>
					</table> 
				</div> 
			</div>  
		  </div>
	  <%-- </s:if> --%>
		<div class="row-fluid sortable">  
			<div class="box span12" id="groupInfo">
			 <div class="box-header well" data-original-title>Live Transactions
				<div class="box-icon"> 
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
				</div>
			</div>			 
			<div class="box-content"> 
				<table class="table table-striped table-bordered bootstrap-datatable datatable" 
					id="DataTables_Table_0">
					<thead>
						<tr>
							<th>S No</th>
							<th>Service</th>
							<th>Txn Description</th>
							<th class="hidden-phone">Card / Account</th>
							<th class="hidden-phone">Terminal Id</th>
							<th>RRN</th>
							<th>POS RRN</th>
							<th class="hidden-phone">Txn Date</th>
							<th class="hidden-phone">Amount</th>
							<th class="hidden-phone">Maker Id</th>
							<th class="center hidden-phone">Branch</th>
							<th class="">Status</th>
						</tr>
					</thead> 
					<tbody id="user-tbody">
					</tbody>
				</table> 
			</div>
		</div>  
	  </div>
	</div>  
</form>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script>
</body>
</html>
