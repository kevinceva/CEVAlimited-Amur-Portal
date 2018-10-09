
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
 	 
<style type="text/css">
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
    
	var mydata ='${selectedUserText}';
	var dataarray = mydata.split("#");
	for(var i=0;i<dataarray.length;i++){
		var o = new Option(dataarray[i], dataarray[i]);
		$(o).html(dataarray[i]);
		$("#userSelectedList").append(o);
	}
	$.each(mydata.split("#"), function(i,e){
		$("#userSelectedList option[value='" + e + "']").prop("selected", true);
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
 
function getPreviousScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/assignServiceterminalBackAct.action';
	$("#form1").submit();
	return true;
}


function assignServiceToTerminal(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/assignServiceToTerminalAct.action';
	$("#form1").submit();
	return true;
}

 
</script>
 
</head>

<body>
	<form name="form1" id="form1" method="post" action="">	
			<div id="content" class="span10">  
			    <div>
					<ul class="breadcrumb">
					  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Assign Service to User</a></li>
					</ul>
				</div>
				<div class="row-fluid sortable"><!--/span-->
					<div class="box span12"> 
						<div class="box-header well" data-original-title>
									<i class="icon-edit"></i>Terminal Information
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div>  
						<div id="terminalDetails" class="box-content"> 
							<fieldset>
								<table width="950" border="0" cellpadding="5" cellspacing="1"  
										class="table table-striped table-bordered bootstrap-datatable ">
									<tr class="even">
										<td width="20%"><strong><label for="Merchant ID">Merchant ID</label></strong></td>
										<td width="30%"> ${merchantID}
											<input name="merchantID" type="hidden" id="merchantID" class="field" value=" ${merchantID}">
										</td>
										<td width="20%"><strong><label for="Store ID">Store ID</label></strong></td>
										<td width="30%"> ${storeId}
											<input name="storeId"  type="hidden" id="storeId" class="field"  value=" ${storeId}" readonly  > 
										</td>
									</tr>
									<tr class="odd">
										<td><strong><label for="Terminal ID">Terminal ID</label></strong></td>
										<td colspan=3> ${terminalID}
											<input name="terminalID" type="hidden"  id="terminalID" class="field" value="${terminalID}"  maxlength="8">
										</td>
										 
									</tr> 						
								</table>
							 <fieldset>
						</div> 
						
						<div class="box-content" id="terminalUserDetails">
							 <fieldset>
								 <table width="950" border="0" cellpadding="5" cellspacing="1"  
									class="table table-striped table-bordered bootstrap-datatable ">
								
										<tr class="even">
											<td width="20%"><strong><label for="Services">Services</label></strong></td>
											<td colspan=3>
												<select multiple id="userSelectedList" name="userSelectedList" class="chosen-select" style="width: 220px;" disabled>
												</select> 
											</td>								
										</tr>
								</table>
							</fieldset>
						</div>	
							<input type="hidden" name="selectedUserText" id="selectedUserText" value="${selectedUserText}"/>
							<input type="hidden" name="selectedServices" id="selectedServices" value='${selectedServices}' ></input>
					</div> 
				</div> 
				<div align="left">
					<a  class="btn btn-danger" href="#" onClick="getPreviousScreen()">Back</a> &nbsp;&nbsp;
					<a  class="btn btn-success" href="#" onClick="assignServiceToTerminal()">Save</a>
				</div>	 
						
		</div>
 </form>	
</body>
</html>
