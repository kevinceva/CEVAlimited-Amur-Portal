
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
	 
<link rel="stylesheet" type="text/css" media="screen" href='${pageContext.request.contextPath}/css/jquery.cleditor.min.css' />  
 
 
   
<script type="text/javascript" >
$(document).ready(function() {
    $('.cleditor').cleditor();
	var mydata ='${responseJSON.InboxSubjectList}';
 	var json = jQuery.parseJSON(mydata);
 	$.each(json, function(i, v) {
		// create option with value as index - makes it easier to access on change
		var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
		// append the options to job selectbox
		$('#mailSubject').append(options);
	});
	
		
	var storeData ='${responseJSON.TERMINAL_USERS}';
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
		var rowCount = $('#storeTBody > tr').length;
		
		//rowindex = ++rowindex;
		colindex = ++ colindex; 
		var terminalID=v.terminalID;
		//alert(terminalID);
		var userId=v.userid;
		if(v.terminalID=='' || v.terminalID==undefined){
			terminalID="";
		}
		if(v.userid=='' || v.userid==undefined){
			userId="";
		}
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
				"<td >"+colindex+"</td>"+
				"<td class=''>"+terminalID+"</span></td>"+	
				//"<td class=''>"+userId+"</span> </td>"+ 
		"</tr>";
			
		$("#storeTBody").append(appendTxt);	
		rowindex = ++rowindex;
	});
			
			
});

var list = "validFrom,validThru,terminalDate".split(",");
var datepickerOptions = {
			showTime: false,
			showHour: false,
			showMinute: false,
  		dateFormat:'dd-mm-yy',
  		alwaysSetTime: false,
		changeMonth: true,
		changeYear: true
};


$(function() {
	$(list).each(function(i,val){
		$('#'+val).datepicker(datepickerOptions);
	});
});
 


function createInbox(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/inboxSubmitAct.action';
	$("#form1").submit();
	return true;
}	
</script>
 
</head>

<body>
<form name="form1" id="form1" method="post"> 
	<div id="content" class="span10"> 
	
		<div>
			<ul class="breadcrumb">
			  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
			  <li><a href="#">Credit Requisition Inbox</a></li>
			</ul>
		</div>
				
		<div class="row-fluid sortable"><!--/span--> 
			<div class="box span12"> 
				<div class="box-header well" data-original-title>Inbox Information
				  <div class="box-icon"> 
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
				  </div>
				</div> 
				<div class="box-content" id="terminalDetails"> 
					<fieldset> 
						<table width="950" border="0" cellpadding="5" cellspacing="1"  
							class="table table-striped table-bordered bootstrap-datatable "> 
							<tr class="even">
								<td ><strong><label for="To E-Mail">To<font color="red">*</font></label></strong></td>
								<td><input name="emailId" type="text" id="emailId" class="field" value="${responseJSON.emailId}" style="width:450px" ></td>
							</tr>
							<tr class="odd">
								<td ><strong><label for="To E-Mail">CC</label></strong></td>
								<td><input name="ccEmailId" type="text" id="ccEmailId" class="field" value="${responseJSON.ccEmailId}" style="width:450px" ></td>
							</tr>
						
							<tr class="even">
								<td><strong><label for="Subject">Subject<font color="red">*</font></label></strong></td>
								<td> 
									<select id="mailSubject" name="mailSubject" style="width:450px" required=true >
										<option value="">Select</option>
									</select>
								</td>
										
							</tr>
							<tr class="odd">
								<td ><strong><label for="Message">Message<font color="red">*</font></label></strong></td>
								<td >
									<textarea name="emailMessage" id="emailMessage" class="cleditor" ></textarea>
									
								</td>	
							</tr>
						</table>
					</fieldset>
					</div>
            </div>
		</div>			 
			
		<div class="row-fluid sortable"><!--/span--> 
			<div class="box span12">
                  <div class="box-header well" data-original-title>Terminal Information
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div>   
            <div class="box-content">
				<fieldset>
					<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable" 
						id="DataTables_Table_0"  >
						<thead>
							<tr >
								<th>S No</th>
								<th>Terminal Id</th>
								<th>User Id </th>
							</tr>
						</thead> 
						<tbody  id="storeTBody">
						</tbody>
					</table>
				</fieldset>
            </div>
          </div> 
		</div>  
	<div class="form-actions"> 
		<a class="btn btn-danger" href="#" onClick="createInbox()">Submit</a>
	</div> 
</div> 
 </form>
<script src='${pageContext.request.contextPath}/js/jquery.cleditor.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script> 
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script>  
</body>
</html>
