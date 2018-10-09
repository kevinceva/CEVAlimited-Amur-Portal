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
 <!-- jQuery -->  
  
  <!-- application script for Charisma demo -->   
<link rel="stylesheet" type="text/css" href="<%=ctxstr%>/css/radial-menu.min.css" /> 

<script type="text/javascript" >
var mydata = '${responseJSON.SWITCH_BANK_DATA}';
var json = jQuery.parseJSON(mydata);
window.setTimeout( refreshGrid, 10000);
function refreshGrid() { 
    var formInput='method=getSwitchStatus';
	json = "";
 	$.getJSON('<%=ctxstr%>/<%=appName %>/postJson.action', 
				formInput,
				function(data) {
					json =  data.responseJSON.SWITCH_BANK_DATA; 
					fillJqGrid(json);
	});
	
	window.setTimeout(refreshGrid, 10000);

}
 
$(document).ready(function() {
	fillJqGrid(json);
});
function fillJqGrid(){
	$("#wheel2").html("");
	$.each(json, function(i, v) { 
		if(v.status=='A')
			$("#wheel2").append('<li class="item"><a href="#home"><font color="yellow">'+v.bankname+'</font></a></li>');
		else
			$("#wheel2").append('<li class="item"><a href="#home"><font color="red">'+v.bankname+'</font></a></li>');
	}); 
}

function getServiceScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/serviceMgmtAct.action';
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
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
			  <li> <a href="#">Switch UI</a> </li>
			</ul>
		</div>
	 
		<table height="3">
		 <tr>
			<td colspan="3">
				<div class="messages" id="messages"><s:actionmessage /></div>
				<div class="errors" id="errors"><s:actionerror /></div>
			</td>
		</tr>
	 </table> 
			 
		<div class="row-fluid sortable"> 
			<div class="box span12"> 
					<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Switch Status
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

						</div>
					</div>
				<div class="box-content">
					<fieldset> 
						<table width="950" border="0" cellpadding="5" cellspacing="1" 
							class="table table-striped table-bordered bootstrap-datatable " id="user-details">  
						<tr class="odd"> <td colspan=4>
							<a href="#wheel2" class="wheel-button ne">
							 <span>+</span>
							</a>
							<ul id="wheel2" class="wheel">
							   <li class="item"><a href="#home">A</a></li>
							  <li class="item"><a href="#home">B</a></li>
							  <li class="item"><a href="#home">C</a></li>
							  <li class="item"><a href="#home">D</a></li>
							</ul>
							</td> </tr> 
						</table>
					</fieldset> 
				</div>
			</div>
		</div> 					<!-- content ends -->
	</div><!--/#content.span10-->
<script type="text/javascript" src="<%=ctxstr%>/js/jquery.wheelmenu.min.js"></script>
 		 
<script type="text/javascript">
	$(".wheel-button").wheelmenu({
	  trigger: "hover", // Can be "click" or "hover". Default: "click"
	  animation: "fly", // Entrance animation. Can be "fade" or "fly". Default: "fade"
	  animationSpeed: "fast", // Entrance animation speed. Can be "instant", "fast", "medium", or "slow". Default: "medium"
	  angle: "all", // Angle which the menu will appear. Can be "all", "N", "NE", "E", "SE", "S", "SW", "W", "NW", or even array [0, 360]. Default: "all" or [0, 360]
	});
</script> 
 </form> 
 </body>
</html>
