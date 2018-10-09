<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Random"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>


<script type="text/javascript">

	var createProfileRules = {
		   rules : {
			profileId : { required : true }
		   },  
		   messages : {
			profileId : { 
		       required : "Please enter ID Number."
		        }
		   } 
	 };
	
	$(function(){
		
		$("#submit").click(function(){
			$("#form1").validate(createProfileRules);
			if($("#form1").valid()){
				$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioDetForWdlAct.action";
				$("#form1").submit();
				return true;
			}else{
				return false;
			}
		});
		
		$("#back").click(function(){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioAct.action";
			$("#form1").submit();
		});
		
	});

</script>


</head>
<body >
	<form name="form1" id="form1" method="post" action="" autocomplete="off">
	<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="home.action">Bio Activities</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Bio Cash Withdrawal</a></li>
				</ul>
			</div>
			
			<div>
				<ul class="breadcrumb">
					<li><a href="#"  style="font-size: 15px;font-variant: inherit;">Bio Cash Withdrawal</a></li>
				</ul>
			</div>


<div class="row-fluid sortable">
			<div class="box span12">
			<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>Profile Details
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
				</div>
			</div>

			<div class="box-content" >
			 <fieldset>
			 <table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
				<tr  class="even">
					 <td width="20%"><label for="Address Line1"><strong>ID Number<font color="red">*</font></strong></label></td>
					 <td><input name="profileId" type="text"  id="profileId" class="field" maxlength='30' value=""></td>
				</tr>
				<tr class="odd">
					<td colspan="2">
						<input class="btn btn-danger" type="submit" class="btn btn-danger"  name="back" id="back" value="Back" />  &nbsp;&nbsp;
						<input class="btn btn-success" type="submit" name="submit" id="submit" value="Submit" />
					</td>
					
				</tr>
			</table>
			</fieldset>
			</div>
			</div>
			</div>
			
			
		</div>
	</form>
</body>
</html>
