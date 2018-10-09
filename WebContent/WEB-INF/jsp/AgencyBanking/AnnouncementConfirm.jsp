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
$(function(){ 
	$('#btn-submit').live('click', function () {  
		var url="${pageContext.request.contextPath}/<%=appName %>/announcementconfirm.action"; 
		$("#form1")[0].action=url;
		$("#form1").submit();  
	});  
});    
 
</script> 
</head> 
<body>
<form name="form1" id="form1" method="post" action=""> 
	<div id="content" class="span10">  
		<div>
			<ul class="breadcrumb">
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
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
							<tr class="even" id="tr_group">
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
						</table>
					</fieldset> 
				</div>
			</div>
		</div> 
		<div class="form-action">
			<input type="button" name="btn-submit" id="btn-submit" class="btn btn-success" value="Confirm" />
		</div>	 
	</div>	 
</form>
</body>
</html>
