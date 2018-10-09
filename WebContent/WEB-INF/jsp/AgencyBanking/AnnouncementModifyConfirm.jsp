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
<%String appName = session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 
   
<script type="text/javascript" > 

$(document).ready(function() {  
	$('#btn-submit').live('click', function () {  
		var url="${pageContext.request.contextPath}/<%=appName %>/announcementModifyConfirm.action"; 
		$("#form1")[0].action=url;
		$("#form1").submit(); 
		 
	});  
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
</head> 
<body>
<form name="form1" id="form1" method="post" action="">	
 
		<div id="content" class="span10">  
			<div>
				<ul class="breadcrumb">
				  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="#">Announcement</a>  </li>
				</ul>
			</div>
			<div class="row-fluid sortable"><!--/span-->
				<div class="box span12"> 
					<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Information
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

						</div>
					</div>  
					<div class="box-content"  id="userDetails">
						 <fieldset>
							 <table width="950" border="0" cellpadding="5" cellspacing="1"  
									class="table table-striped table-bordered bootstrap-datatable">
								<tr class="even">
									<td> <strong><label for="Reference No">Reference No</label></strong></td> 
									<td>
										${referenceNo}
									</td>
								</tr> 
								<tr class="odd" id="tr_group">
									<td width="15%"><strong><label for="Select Group"><span id="selectedLable"></span></label></strong></td>
									<td width="50%"> <span id="selectedMUG" ></span> </td>
								</tr> 
								<tr class="even">
									<td> <strong><label for="Select User">Message</label></strong></td> 
									<td>
										${messageText}
									</td>
								</tr> 
								<input type="hidden" name="typeOfDataVal" id="typeOfDataVal" value="${typeOfDataVal}"/>
								<input type="hidden" name="typeOfData" id="typeOfData" value="${typeOfData}" />
								<input type="hidden" name="messageText" id="messageText" value="${messageText}" />
								<input type="hidden" name="referenceNo" id="referenceNo" value="${referenceNo}" />
							</table>
						</fieldset> 
					</div>
				</div>
			</div> 
			<div class="form-actions">
				<input type="button" name="btn-submit" id="btn-submit" class="btn btn-success" value="Modify" />
			</div>	 
		</div> 
</form>
</body>
</html>
