<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>

<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>


<script type="text/javascript">

$(function(){
	
	var storeData ='${responseJSON.TXN_HISTORY}';
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
		
		//rowindex = ++rowindex;
		colindex = ++ colindex; 
		
		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
		"<td >"+colindex+"</td>"+
		"<td>"+v.TXN_REF_NO+"</span></td>"+	
		"<td>"+v.TXNDATE+"</span> </td>"+ 
		"<td>"+v.TXNTYPE+"</span> </td>"+ 
		"<td>"+v.AMOUNT+"/-kshs</span></td>"+
		"<td>"+v.DRCR+ "</span></td><tr>";
			
			$("#tbody").append(appendTxt);	
			rowindex = ++rowindex;
	});
	
	$("#back").click(function(){
		$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioAct.action";
		$("#form1").submit();
	});
	
});
</script>
</head>
<body >
	<form name="form1" id="form1" method="get" action="" autocomplete="off">
		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="home.action">Bio Activities</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Bio Transaction History</a></li>
				</ul>
			</div>
			
			<div>
				<ul class="breadcrumb">
					<li><a href="#"  style="font-size: 15px;font-variant: inherit;">Bio Transaction History</a></li>
				</ul>
			</div>



<div class="row-fluid sortable">
			<div class="box span12">
			<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>Profile Transaction History for:: ${responseJSON.PROFILE_ID}
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<span id="ajaxGetUserServletResponse"></span>
			<div class="box-content" id=" ">
			 <fieldset>
			 <table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
				
				<thead>
							<tr>
								<th >S No</td>
								 <th >REF No</td>
								 <th >Transaction Date</th>
								 <th >Transaction Description</th>
								 <th >Amount</th>
								 <th >Transaction type</th>
							</tr>
						</thead> 
						<tbody  id="tbody" >
								
						</tbody>
			</table>
			<input class="btn btn-danger" type="submit" name="back" id="back" value="Next" />
			</fieldset>
			</div>
			</div>
			</div>
			
		</div>
			</form>
</body>
</html>
