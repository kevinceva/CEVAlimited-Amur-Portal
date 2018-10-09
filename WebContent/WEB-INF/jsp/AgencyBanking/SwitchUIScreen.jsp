

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta charset="utf-8">
<title>Banking</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString();%> 

<script type="text/javascript" >
	var usersList = new Array();
	var groupsList = new Array();
	var userTableRights=new Array();

    function switchusercreation() {
     	$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/switchBankCreation.action";
    	$("#form1").submit();
    	return true;
    }


    $(document).ready(function () { 
		$("#DataTables_Table_0_filter >label >input").live('keyup',function(){

			var merchantIdSearchKey=$("#DataTables_Table_0_filter >label >input").attr('value').toUpperCase();
			var reqUser;
			 if(merchantIdSearchKey.length==0) {

				for(var i=0;i<usersList.length;i++){
					reqUser=usersList[i];
					$("#"+reqUser).hide();
				}
				for(var i=0;i<userTableRights.length;i++){
					reqUser=userTableRights[i];
					$("#"+reqUser).hide();
				}
			 } else {

				for(var i=0;i<usersList.length;i++) {
					reqUser=usersList[i];
					$("#"+reqUser).hide();
				}

				for(var i=0;i<usersList.length;i++) {
					if(usersList[i].indexOf(merchantIdSearchKey) != -1 ) {
						reqUser=usersList[i];
							$("#"+reqUser).show();
					}
				}

			 }
    	});



		var bankData ='${responseJSON.BANK_DATA}';
		//alert(bankData);

		var json = jQuery.parseJSON(bankData);

		var val = 1;
		var rowindex = 0;
		var colindex = 0;
		var addclass = "";

		$.each(json, function(index, v) {

			if(val % 2 == 0 ) {
				addclass = "even";
				val++;
			}
			else {
				addclass = "odd";
				val++;
			}
			var rowCount = $('#userGroupTBody > tr').length;

				colindex = ++ colindex;

			var appendTxt = "<tr class="+addclass+" id='"+rowindex+"' index='"+rowindex+"'> "+
			"<td>"+colindex+"</td>"+
			"<td>"+v.bankname+"</span></td>"+
			"<td>"+v.bankcode+"</span> </td>"+
			"<td>"+v.acquirerId+"</span></td>"+
			"<td>"+v.status+"</span></td>"+

			"<td class='center '><a  id='bank-modify-group' class='btn btn-success'  href='#' index='"+rowindex+"' >Modify</a> &nbsp; <a  id='switch-view-group' class='btn btn-warning' href='#' index='"+rowindex+"'  class='btn btn-warning' >View</a> &nbsp; </td></tr>";

			$("#userGroupTBody").append(appendTxt);
			rowindex = ++rowindex; 
		});




		$(document).on('click','#bank-modify-group',function(event) {
			 index = $(this).attr('index');

			 //alert("hellow"+index);

			var searchRow = "DataTables_Table_0 tbody tr:eq("+(index)+") td";
				//alert(searchRow);
				$('#'+searchRow).each(function (indexTd) {
					 //alert("index posting in loop is ==> "+indexTd);

					 if (indexTd == 1) {
						// Frequency
						bankname=$(this).text();
						//alert(bankname);
					 }   if(indexTd == 2) {
						// Time or Date
						bankcode=$(this).text();
						//alert(bankcode);
					 }   if(indexTd == 3) {
						// email ids
						 acquirerId=$(this).text();
						//alert(acquirerId);
						//alert(acquirerId);
					 }

				});

				$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/modifyaction.action?bankname="+bankname+"&bankcode="+bankcode+"&acquirerId="+acquirerId+"&status="+status;
				$("#form1").submit();
				return true;

		});


		$(document).on('click','#switch-view-group',function(event) {
			 index = $(this).attr('index');

			 //alert("hellow"+index);

			var searchRow = "DataTables_Table_0 tbody tr:eq("+(index)+") td";
				//alert(searchRow);
				$('#'+searchRow).each(function (indexTd) {
					 //alert("index posting in loop is ==> "+indexTd);

					 if (indexTd == 1) {
						// Frequency
						bankname=$(this).text();
						//alert(bankname);
					 }   if(indexTd == 2) {
						// Time or Date
						bankcode=$(this).text();
						//alert(bankcode);
					 }   if(indexTd == 3) {
						// email ids
						 acquirerId=$(this).text();
							//alert(acquirerId);
						// alert(acquirerId);
					 } if(indexTd == 4)
						 {
						 status=$(this).text();
							//alert(status);
						 }

				});

				$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/viewaction.action?bankname="+bankname+"&bankcode="+bankcode+"&acquirerId="+acquirerId+"&status="+status;
				$("#form1").submit();
				return true;

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
});

</script>
 
</head>

<body>
<form name="form1" id="form1" method="post" >
 
	<div id="content" class="span10">
		<!-- content starts -->
		  <div>
				 <ul class="breadcrumb">
					<li><a href="#">Home </a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">SwitchUIScreen</a></li>
				</ul>
		</div>

		<div class="box-content" id="top-layer-anchor">
			 <span>
				<a href="#" class="btn btn-primary ajax-link" id="group-creation" onclick="switchusercreation()">Genarate Bank Creation</a> &nbsp;
			 </span>
			<span>
				<a href="#" class="btn btn-primary ajax-link" id="dashboard">Dashboard</a> &nbsp;
			 </span>
				<!-- <span>
				<a href="#" class="btn btn-primary ajax-link" id="exportExcel">Export Excel</a> &nbsp;
			 </span> -->

		</div>

	<div class="row-fluid sortable"><!--/span--> 
		<div class="box span12" id="groupInfo">
                  <div class="box-header well" data-original-title>Bank Information
					<div class="box-icon"> <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> </div>
				  </div>
           	 
		 <div class="box-content">
			<fieldset>
 					<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable"
						id="DataTables_Table_0">
						<thead>
							<tr >
								<th >S No</th>
								<th>Bank ID</th>
								<th>Bank Name </th>
								<th>Acqire ID</th>
								<th>Status</th>
								<th>Actions</th>
							</tr>
						</thead> 
						<tbody  id="userGroupTBody">
						</tbody>
					</table>
				</fieldset>
			</div>
		  </div>
	</div>  
</div> 
</form>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
