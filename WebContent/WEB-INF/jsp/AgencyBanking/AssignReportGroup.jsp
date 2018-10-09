
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 
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
input.button_add {
    background-image: url(<%=ctxstr%>/images/Left.png); /* 16px x 16px */
    background-color: transparent; /* make the button transparent */
    background-repeat: no-repeat;  /* make the background image appear only once */
    background-position: 0px 0px;  /* equivalent to 'top left' */
    border: none;           /* assuming we don't want any borders */
    cursor: pointer;        /* make the cursor like hovering over an <a> element */
    height: 30px;            /*make this the size of your image */
    padding-left: 30px;      /*make text start to the right of the image */
    vertical-align: middle; /* align the text vertically centered */
}
input.button_add2 {
    background-image: url(<%=ctxstr%>/images/Right.png); /* 16px x 16px */
    background-color: transparent; /* make the button transparent */
    background-repeat: no-repeat;  /* make the background image appear only once */
    background-position: 0px 0px;  /* equivalent to 'top left' */
    border: none;           /* assuming we don't want any borders */
    cursor: pointer;        /* make the cursor like hovering over an <a> element */
    height: 30px;           /* make this the size of your image */
    padding-left: 30px;     /* make text start to the right of the image */
    vertical-align: middle; /* align the text vertically centered */
}
</style>
<script type="text/javascript" >
$(document).ready(function() { 
	
	var mydata ='${responseJSON.REPORT_MAIN}';
	var json = jQuery.parseJSON(mydata);
	$.each(json, function(i, v) {
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
	    $('#userlist1').append(options);
	});
	
	var mydata1 ='${responseJSON.REPORT_USER_GROUP}';
	var json1 = jQuery.parseJSON(mydata1);
	$.each(json1, function(i, v) {
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
	    $('#userlist2').append(options);
	});
	
	$('#B1').click(function(e) {  
		var selectedOpts = $('#userlist1 option:selected');
		
		if (selectedOpts.length == 0) { 
			e.preventDefault();
		}

		$('#userlist2').append($(selectedOpts).clone());
		$(selectedOpts).remove();
		e.preventDefault();

		/* $("#userlist2").selectOptionSort({
			orderBy: "text",
			sort: "asc"
		}); */

    });

    $('#B2').click(function(e) {
        var selectedOpts = $('#userlist2 option:selected');
       
        if (selectedOpts.length == 0) {
            alert("Nothing to move.");
            e.preventDefault();
        }

        $('#userlist1').append($(selectedOpts).clone());
        $(selectedOpts).remove();
        e.preventDefault();
		/* 
		$("#userlist1").selectOptionSort({
			orderBy: "text",
			sort: "asc"
		}); */
    	
	});
});

 
function getGenerateMerchantScreen(){
	$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/userGrpCreation.action?pid=7";
	$("#form1").submit();
	return true;
}
 
function assignDashtoGroup(){
	getUsers();
	//alert(selectUsers);
	selectUsers=selectUsers.slice(1);
    	$('#selectUsers').val(selectUsers);
	$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/userassignedreportscnf.action";
	$("#form1").submit();
	return true;
}

var selectUsers="";
function getUsers(){
	for(var i=0;i<document.form1.userlist2.length;i++){
		var eachone=document.form1.userlist2[i].value;
		selectUsers=selectUsers+","+eachone;
	}
		
}

</script>

</head>

<body>

<form name="form1" id="form1" method="post" action="">
		<div id="content" class="span10">
			<div>
				<ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider">
							&gt;&gt; </span></li>
					<li><a href="#">User Management</a> <span class="divider">
							&gt;&gt; </span></li>
					<li><a href="#">User Reports</a></li>
				</ul>
			</div>

			<div class="row-fluid sortable">

				<div class="box span12">

					<div class="box-header well" data-original-title>
						<i class="icon-edit"></i>Select User Reports
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i
								class="icon-cog"></i></a> <a href="#"
								class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a>

						</div>
					</div>
					<div class="box-content">
						<fieldset> 
							<table width="950" border="0" cellpadding="5" cellspacing="1"
								class="table table-striped table-bordered bootstrap-datatable ">
								<tr class="even">
									<td width="20%"><label for="Group Id"><strong>Group
												Id</strong></label></td>
									<td width="30%">${responseJSON.GROUP_ID} <input
										name="groupId" type="hidden" id="groupId" class="field"
										value="${responseJSON.GROUP_ID}">

									</td>
									<td width="20%"><label for="Store ID"><strong>Group
												Name</strong></label></td>
									<td width="30%">${responseJSON.GROUP_NAME} <input
										name="grpname" type="hidden" id="storeId" class="grpname"
										value="${responseJSON.GROUP_NAME}" />
									</td>
								</tr>

								<tr class="odd">
									<td><label for="User Id"><strong>User Id</strong></label></td>
									<td>${responseJSON.USER_ID} <input name="userId"
										type="hidden" id="userId" class="field"
										value="${responseJSON.USER_ID}">
									</td>
									<td><label for="Employee Number"><strong>Employee
												Number</strong></label></td>
									<td>${responseJSON.EMP_NO} <input name="employeeNo"
										type="hidden" id="employeeNo" class="grpname"
										value="${responseJSON.EMP_NO}" />
									</td>
								</tr>

							</table>
						</fieldset>
						</div>


						<div id="userDetails">
							<table width="950" border="0" cellpadding="5" cellspacing="1"
								class="table table-striped table-bordered bootstrap-datatable ">
								<tr class="even">
									<td>
										<div align="right">
											<select multiple style="width: 300px; height: 150px;"
												size="10" name="userlist1" id="userlist1">
											</select>
										</div>
									</td>
									<td class="odd" width="30px;"><br></br> <input
										type="button" id="B1" class="button_add2" name="B1" /><br></br>
										<input type="button" id="B2" class="button_add" name="B2" />
									</td>
									<td><select multiple style="width: 300px; height: 150px;"
										size="10" name="userlist2" id="userlist2">
									</select></td>
								</tr>
							</table>
							</br>
						</div>
						<input type="hidden" name="selectUsers" id="selectUsers"></input>
					</div>
				</div>
			 
			<div align="center">
				<a class="btn btn-danger" href="#"
					onClick="getGenerateMerchantScreen()">Back</a> &nbsp;&nbsp; <a
					class="btn btn-danger" href="#" onClick="assignDashtoGroup()">Submit</a>
			</div>
		</div> 
	</form> 
</body>
</html>
