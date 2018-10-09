
<!DOCTYPE html>
<%@page import="java.util.Random"%>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">



<style type="text/css">
.errors {
	font-weight: bold;
	color: red;
	padding: 2px 8px;
	margin-top: 2px;
}

input#abbreviation {
	text-transform: uppercase
}
;
</style>

<script type="text/javascript">
	function loadData(){
		
		<%	
		System.out.println("session:::"+session);

			request.setAttribute("bio_mid", "BPWU"+new Random().nextInt(9999));
		%>
	}
	
	function bioRegister(){
		$("#form1")[0].action='<%=request.getContextPath()%>/AgencyBanking/bioReg.action';  
		$("#form1").submit();
		return true;
	}

</script>

</head>
<body onload="loadData()">
	<form name="form1" id="form1" method="post" autocomplete="off">
		<div id="content" class="span10">

			<div>
				<ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider">
							&gt;&gt; </span></li>
					<li><a href="#">Bio Activities</a></li>
				</ul>
			</div>

			<table height="3">
				<tr>
					<td colspan="3">
						<div class="messages" id="messages">
							<s:actionmessage />
						</div>
						<div class="errors" id="errors">
							<s:actionerror />
						</div>
					</td>
				</tr>
			</table>

			<div class="box-content" id="top-layer-anchor">
				 <div>
					<a href="#" class="btn btn-success" id="bio-registration" onClick="bioRegister()"  title='Bio Registration' data-rel='popover'  data-content='Bio Registration.'><i class='icon icon-users icon-white'></i>&nbsp;Bio Registration</a> &nbsp; 
 					<a href="#" class="btn btn-primary" id="bio-deposit"   title='Bio Deposit' data-rel='popover'  data-content='Bio Deposit'><i class='icon icon-plus icon-white'></i>&nbsp;Bio Deposit</a> &nbsp;
 					<a href="#" class="btn btn-warning" id="bio-cashwdl"   title='Bio Cash Withdrawal' data-rel='popover'  data-content='Bio Cash Withdrawal'><i class='icon icon-minus icon-white'></i>&nbsp;Bio Cash Withdrawal</a> &nbsp;
 				 </div>	
			</div>

		</div>
	</form>
</body>
</html>
