<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>MicroInsurance</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%
	String ctxstr = request.getContextPath(); 
	String appName = session.getAttribute(
			CevaCommonConstants.ACCESS_APPL_NAME).toString();
%>
   
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

.messages {
	font-weight: bold;
	color: green;
	padding: 2px 8px;
	margin-top: 2px;
}

.errors {
	font-weight: bold;
	color: red;
	padding: 2px 8px;
	margin-top: 2px;
}
</style>
<script type="text/javascript">
	var parentId = "announceTBody > tr";
	var subParent = "";
	var checkedCheckbox = false;
	
$(function() { 
	var storeData = '${responseJSON.USER_LIST}';
	var storeUserList = "${keyVal}";
	var json = jQuery.parseJSON(storeData);
	var val = 1;
	var rowindex = 0;
	var colindex = 0;
	var addclass = "";
	$ .each( json, function(i, v) {
		if (val % 2 == 0) {
			addclass = "even";
			val++;
		} else {
			addclass = "odd";
			val++;
		}
		
		var rowCount = $('#announceTBody > tr').length;

		colindex = ++colindex;

		var status = "";
		var checkedStatus = " ";
	 
		if (v.USER_STATUS == 'New User') {
			status = "<a href='#' class='label label-info' index='"+rowindex+"'>"
					+ v.USER_STATUS + "</a>"; 
		}
		
		if(storeUserList.indexOf(v.USER_ID) != -1 ) {
			checkedStatus = "checked";
		} 
		
		//console.log("checkedStatus ==> "+checkedStatus);
		var appendTxt = "<tr class="+addclass+" id='"+rowindex+"' index='"+rowindex+"'> "
				+ "<td ><input class='center-chk' type='checkbox' value='" + v.USER_ID + "' "+checkedStatus+" /></td>"
				+ "<td>" + v.USER_ID + "</span></td>"
				+ "<td>" + v.USER_NAME + "</span> </td>"
				+ "<td> " + v.ROLE_GRP_ID + "  </span> </td>"
				+ "<td>"+ v.EMAIL+ "</span></td>"
				+ "<td class='center hidden-phone'>"+ v.EMPID + "</span></td>"
				+ "<td class='hidden-phone'>" + status + "</span></td></tr>";

		$("#announceTBody").append(appendTxt);
		rowindex = ++rowindex;  
	}); 
	
	 
	
	if(storeUserList.split(",").length == rowindex 
			&& rowindex != 0)  {
		$("#select-all").trigger("click");	
	}
	
	// add multiple select / deselect functionality
	$("#select-all").click(function () {
		$("#error_dlno").text("");
		$('.center-chk').attr('checked', this.checked);
	});
 
	// if all checkbox are selected, check the selectall checkbox
	// and viceversa
	$(".center-chk").click(function(){
		$("#error_dlno").text("");
		if($(".center-chk").length == $(".center-chk:checked").length) {
			$("#select-all").attr("checked", "checked"); 
		} else {
			$("#select-all").removeAttr("checked");
		} 
	});
 
	$("#btn-submit").click(function(event) {
		event.preventDefault();
		var searchIDs = [] ;
		var data = "";
		var mulData = "";
		var innerMulData = "";
		//$("tbody#announceTBody input:checkbox:checked").map(function() {
		$("tbody#announceTBody input:checkbox:checked").each(function(index) {
			searchIDs.push($(this).val());
			if(index == 0) {
				data += $(this).val();
			} else {
				data += ","+$(this).val();
			} 
		});
		
		
		$("#"+parentId).each(function(indexTr){ 
			subParent = parentId + "#"+$(this).attr("id")+" td";				
			//console.log(subParent);  
			innerMulData = "";
			$("#"+subParent).each(function(indexTd){ 
				//$('#announceTBody > tr#0 td > .center-chk').is(':checked')
				if(indexTd == 0) { 
					checkedCheckbox = $("#"+subParent+" > .center-chk").is(":checked");
				}
				//console.log(checkedCheckbox); 
				if(checkedCheckbox) {
					if(indexTd != 0) { 
						if(indexTd == 1) {
							innerMulData += $(this).text().trim();				
						} else {
							innerMulData += ","+$(this).text().trim();
						}
					} 
				} 
			}); 
			if(innerMulData.indexOf(",") !=-1) {
				mulData += innerMulData +"#";
			}
		}); 
		
		if(searchIDs.length == 0) {
			$("#error_dlno").text("Please check atleast one record."); 
		} else {
			$("#keyVal").val(data);
			$("#multiData").val(mulData.slice(0,-1));
			$("#error_dlno").text("");
			var url="${pageContext.request.contextPath}/<%=appName %>/confirmUnAuthUsersAct.action"; 
			$("#form1")[0].action=url;
			$("#form1").submit(); 
		
		}
	}); 
});
</script> 
</head> 
<body>
<form name="form1" id="form1" method="post" >
	<div id="content" class="span10"> 
			<!-- content starts -->
			<div>
				<ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="userGrpCreation.action?pid=7">User Management</a> <span class="divider"> &gt;&gt; </span> </li>
					<li><a href="#">User Authorization</a></li>
				</ul>
			</div>  
			<div class="row-fluid sortable">
				<div class="box span12">
					<div class="box-header well" data-original-title>
						User Information
						<div class="box-icon">
							<a href="#" class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a> <a href="#"
								class="btn btn-close btn-round"><i class="icon-remove"></i></a>
						</div>
					</div>
					<div class="box-content">
						<fieldset>
							<table width="100%" class="table table-striped table-bordered bootstrap-datatable "
								id="DataTables_Table_0">
								<thead>
									<tr>
										<th ><input type="checkbox" name="select-all" id="select-all" /></th>
										<th >User Id</th>
										<th >User Name</th>
										<th >Group ID</th>
										<th class="center hidden-phone">Email Id</th>
										<th class="hidden-phone">Employee Id</th>
										<th >Status</th>
									</tr>
								</thead>
								<tbody id="announceTBody">
								</tbody>
							</table>
						</fieldset>
					</div> 
				</div>
			</div>
			<div class="form-actions">
				<input type="button" class="btn btn-success" name="btn-submit"
						id="btn-submit" value="Authorize"  />
				<span id ="error_dlno" class="errors"></span>
				
				<input name="groupID" type="hidden" id="groupID" value="${groupID}" />
 				<input name="keyVal" type="hidden" id="keyVal" value="${keyVal}" />
				<input name="multiData" type="hidden" id="multiData" value="${multiData}" />
			</div>
			<!--/#content.span10-->
	</div>  
</form>
</body>
</html>
