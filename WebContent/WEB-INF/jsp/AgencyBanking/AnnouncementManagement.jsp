<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>MicroInsurance</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
  
    
 <link rel="stylesheet" type="text/css" media="screen" href='${pageContext.request.contextPath}/css/jquery.cleditor.min.css' />  

   
<script type="text/javascript" >

var userLinkData ='${USER_LINKS}';
var jsonLinks = jQuery.parseJSON(userLinkData);
var linkIndex = new Array();
var linkName = new Array();
//var linkStatus = new Array();

 var announceRules = {
		   rules : {
				messageText : { required : true} 
		   },  
		   messages : {
			messageText : { 
			   required : "Please enter Message."
				} 
		   } 
		 };
$(document).ready(function () { 

	$('.cleditor').cleditor();
	
	var selectedRadVal="";
	$("input[type=radio]").on("click",function(){
		var radId = "";
		radId =$(this).attr("id"); 
		selectedRadVal=$(this).attr("value");
		$('input[type=radio]').each(function(){
			if($(this).attr("id") != radId) {
				$("#"+$(this).attr("id")+"_select").prop('disabled', 'disabled');
				$("#"+$(this).attr("id")+"_select option").each(function(){
					if($(this).attr('value') == "") {
						$(this).attr('selected',true);
					}					
				});
			} else {
				$("#"+radId+"_select").attr('disabled', false);
			}
		});
		 
	}); 
	
	 
	
	$('#btn-submit').on('click', function () { 
		if(selectedRadVal=='group_val'){
			var groupid=$("#s_group_select").val();
			$("#typeOfDataVal").val(groupid);
			$("#typeOfData").val("G");
		}
		else if(selectedRadVal=='merchant_val'){
			var merchantId=$("#s_merchant_select").val();
			$("#typeOfDataVal").val(merchantId);
			$("#typeOfData").val("M");
		}
		else if(selectedRadVal=='user_val'){
			var userid=$("#s_user_select").val();
			$("#typeOfDataVal").val(userid);
			$("#typeOfData").val("U");
		}else{
			var groupid=$("#s_group_select").val();
			$("#typeOfDataVal").val(groupid);
			$("#typeOfData").val("G");
		}
		
		$("#form2").validate(announceRules);
		 if($("#form2").valid()) { 
			var data=$("#typeOfDataVal").val();
			var sdata=data.split("~");
			$("#typeOfDataVal").val(sdata[0]);
			var url="${pageContext.request.contextPath}/<%=appName %>/announcementconfirm.action"; 
			$("#form2")[0].action=url;
			$("#form2").submit(); 
		} else { 
			return false;
		}
	}); 
	
	$("#createAnnounce").hide();
 	$.each(jsonLinks, function(index, v) {
		linkIndex[index] = index;
		linkName[index] = v.name;
	//	linkStatus[index] = v.status;
	});  
	
	var storeData ='${responseJSON.ANNOUNCE_LIST}';
 	var json = jQuery.parseJSON(storeData);
 
	var val = 1;
	var rowindex = 0;
	var colindex = 0;
	var addclass = "";
	$.each(json, function(i, v) {
		if(val % 2 == 0 ) {
			addclass = "even";
			val++;
		}
		else {
			addclass = "odd";
			val++;
		}  
		var rowCount = $('#announceTBody > tr').length;
		
		colindex = ++ colindex; 
		
		var status="";
			var status_class ="";
			var text = "";
			if(v.STATUS == 'Active') {
				status = "<a href='#' class='label label-success' index='"+rowindex+"'>"+v.STATUS+"</a>";
				status_class = "btn btn-danger";
				text = "Deactivate";
			} else if(v.STATUS == 'De-Active') {
				status = "<a href='#'  class='label label-warning'  index='"+rowindex+"'>"+v.STATUS+"</a>";
				status_class = "btn btn-success";
				text = "Activate";
			} else if(v.STATUS == 'In-active') {
				status = "<a href='#'  class='label label-info'  index='"+rowindex+"'>"+v.STATUS+"</a>";
			}
		
		var appendTxt = "<tr class="+addclass+" id='"+rowindex+"' index='"+rowindex+"'> "+
				"<td >"+colindex+"</td>"+
				"<td >"+v.TXN_REF_NO+"</span></td>"+	
				"<td class='hidden-phone'>"+v.TYPE_OF_SETTING+"</span> </td>"+ 
 				"<td>"+v.MESSAGE+"</span></td>"+
				"<td class='hidden-phone'>"+status+"</span></td>"+
				"<td class='center hidden-phone'>"+v.makerId+"</span></td>"+
				"<td class='hidden-phone'>"+v.makerDate+"</span></td>"+
				"<td><p><a id='announcement-modify' class='btn btn-success' href='#' index='"+rowindex+"'>Modify</a> &nbsp;<a id='announcement-status-change' class='btn btn-danger' href='#' index='"+rowindex+"'>De-Active</a></p></tr>";
			
			$("#announceTBody").append(appendTxt);	
			rowindex = ++rowindex;
	});
		
		
	// Search For Top Layer
	$('#top-layer-anchor').find('a').each(function(index) {
		var anchor = $(this);   
		var flagToDo = false;
		 
		$.each(linkIndex, function(indexLink, v) {	 
				if(linkName[indexLink] == anchor.attr('id'))  {
					flagToDo = true;
				} 
		});
		
		if(!flagToDo) {
			anchor.attr("disabled","disabled");
		} else {
			anchor.removeAttr("disabled");
		} 
	});
	
	//Search For The Links That Are Assigned To TABLE Level
	 $('table > tbody').find('a').each(function(index) {
		var anchor = $(this);   
		var flagToDo = false;
		 
		$.each(linkIndex, function(indexLink, v) {	 
		//console.log(linkName[indexLink] +" === "+ anchor.attr('id') +" >" + (linkName[indexLink] == anchor.attr('id')));
			if(linkName[indexLink] == anchor.attr('id'))  {
				flagToDo = true;
			} 
		}); 
		if(!flagToDo) {
			anchor.attr("disabled","disabled");
		} else {
			anchor.removeAttr("disabled");
		} 
		 
	}); 
	
	
	// for announcement create
	var mydata ='${responseJSON.USER_GROUPS}';

	var json = jQuery.parseJSON(mydata);
	
	$.each(json, function(i, v) {
		var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
		$('#s_group_select').append(options);
	});
	
	mydata ='${responseJSON.MERCHANT_GROUPS}';
	
	json = jQuery.parseJSON(mydata);
	
	$.each(json, function(i, v) {
		var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);  
		$('#s_merchant_select').append(options);
	});
	
	mydata ='${responseJSON.USER_INFO}';
	
	json = jQuery.parseJSON(mydata);
	
	$.each(json, function(i, v) {
		var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);  
		$('#s_user_select').append(options);
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
	
}); 
	
$(document).on('click','a',function(event) {
	var v_id=$(this).attr('id');
	var disabled_status= $(this).attr('disabled'); 
	var queryString = '?'; 
	var v_action = "NO";
	var storeId ="";
	var merchantId ="";
	var terminalId ="";
	var merchantName  ="";
	 
	var index1 = $(this).attr('index');  
	var parentId =$(this).parent().closest('tbody').attr('id'); 
	var searchTrRows = parentId+" tr"; 
	var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
			 
 	if(disabled_status == undefined) {  
		  if (v_id ==  "announcement-modify" 
				|| v_id ==  "announcement-view" 
				|| v_id == "announcement-status-change") { 
 			 
			 // Anchor Tag ID Should Be Equal To TR OF Index
			$(searchTdRow).each(function(indexTd) {  
				 if (indexTd == 1) {
					refno=$(this).text();
				 }
			});  
			 
			queryString += 'referenceNo='+refno;  
			if(v_id ==  "announcement-modify") {  
				v_action = "announcementModifyAct";
 			}else if(v_id == "announcement-status-change"){
				v_action = "announcementStatusChangeAct";
			}
			else v_action = "announcementViewAct";
			
		} else if(v_id ==  "announcement-create") { 
			//v_action="announceaction";  
			$("#createAnnounce").show();
		} 
			
	} else {
	
		// No Rights To Access The Link 
	}
	
	if(v_action != "NO") {
		$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/"+v_action+".action"+queryString;
		$("#form1").submit();
	}
});  
</script>
<style type="text/css">
label.error {
  font-weight: bold;
  color: red;
  padding: 2px 8px;
  margin-top: 2px;
}
.errmsg {
color: red;
}
.errors {
color: red;
}
.messages {
  font-weight: bold;
  color: green;
  padding: 2px 8px;
  margin-top: 2px;
}

.errors{
  font-weight: bold;
  color: red;
  padding: 2px 8px;
  margin-top: 2px;
}
</style>  
<link rel="shortcut icon" href="images/favicon.ico">		
</head> 
<body>  
		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Announcement</a></li>
				</ul>
			</div>
			
      		<div class="box-content" id="top-layer-anchor">
				 <div>
					<a href="#" class="btn btn-success" id="announcement-create">
						<i class="icon-note icon-white"></i>Create Announcement</a> &nbsp; 
				 </div>	
			</div>	
		<form name="form2" id="form2" method="post" action="">			
			<div id="createAnnounce"> 
				<div class="row-fluid sortable"><!--/span--> 
						<div class="box span12">
								  <div class="box-header well" data-original-title>Select An Announcement
									<div class="box-icon"> 
										<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
										<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
									</div>
								  </div> 
								<div class="box-content"  id="userDetails">
									 <fieldset>
										 <table width="950" border="0" cellpadding="5" cellspacing="1"  
												class="table table-striped table-bordered bootstrap-datatable">
											<tr class="even" id="tr_group">
												<td width="15%"><strong><label for="Select Group">Select Group</label></strong></td>
												<td width="2%">  <input type="radio" name="announce" id="s_group" style="opacity: 10;" value="group_val" /> 
												</td>
												<td colspan=2>  
													<select id="s_group_select" name="s_group_select"   required='true' data-placeholder="Choose location..." 
														class="chosen-select" style="width: 220px;" >
															<option value="">Select</option>
													</select> 
												</td>
											</tr> 
											<tr class="even" id="tr_merchant">
												<td><strong><label for="Select Merchant">Select Merchant</label></strong></td>									 
												<td><input type="radio" name="announce" id="s_merchant" value="merchant_val"   style="opacity: 10;" /> </td>
												<td>
													<select id="s_merchant_select" name="s_merchant_select"   required='true' data-placeholder="Choose location..." 
															class="chosen-select" style="width: 220px;" disabled>
														<option value="">Select</option>
													</select>
												</td>
											</tr>
											<tr class="even" id="tr_user">
												<td> <strong><label for="Select User">Select User</label></strong></td>
												<td> <input type="radio" name="announce" id="s_user" value="user_val" style="opacity: 10;" /> </td>
												<td>
													<select id="s_user_select" name="s_user_select"   required='true' data-placeholder="Choose location..." 
															class="chosen-select" style="width: 220px;" disabled>
														<option value="">Select</option>
													</select>
												</td>
											</tr>
											<tr class="even">
												<td> <strong><label for="Select User">Message<font color="red">*</font></label></strong></td> 
												<td>&nbsp;</td>
												<td colspan=2>
													 <textarea class="cleditor" id="messageText" name="messageText" rows="3"></textarea>
												</td>
											</tr> 
											<input type="hidden" name="typeOfDataVal" id="typeOfDataVal" value=""/>
											<input type="hidden" name="typeOfData" id="typeOfData" value="" />
										</table>
									</fieldset> 
								</div>
								<div class="form-actions">
									&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="button" name="btn-submit" id="btn-submit" class="btn btn-success" value="Create" />
								</div>  
							</div> 
					</div> 
 				</div> 
		</form>
		<form name="form1" id="form1" method="post" action="">							  
              <div class="row-fluid sortable"><!--/span--> 
				<div class="box span12">
					  <div class="box-header well" data-original-title>Announcement Details
						  <div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					  </div>  
						<div class="box-content"> 
							<table width='100%' class="table table-striped table-bordered bootstrap-datatable "  
 								id="DataTables_Table_0" >
								<thead>
									<tr>
										<th>S No</th>
										<th>Reference No</th>
										<th class="hidden-phone">Level</th>
 										<th>Message</th>
										<th class="hidden-phone">Status</th>
										<th class="center hidden-phone">Generated By</th>
										<th class="hidden-phone">Generation Date</th>
										<th >Actions</th>
									</tr>
								</thead> 
								<tbody  id="announceTBody">
								</tbody>
							</table>
						</div> 
					</div> 
 			</div> 
		</form> 
	</div>
 <script src="${pageContext.request.contextPath}/js/jquery.cleditor.min.js"></script>
 </body>
</html>
