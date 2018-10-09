
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

$(document).ready(function () { 
	$(document).on('click','a',function(event) {
		
		var id=$(this).attr("id");
		//alert(id);
		if(id=="bio-registration"){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioReg.action";
			$("#form1").submit();
		}else if(id=="bio-deposit"){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioDeposit.action";
			$("#form1").submit();
		}else if(id=="bio-cashwdl"){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioWdl.action";
			$("#form1").submit();
		}else if(id=="bio-profile-balance"){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioProfileBal.action";
			$("#form1").submit();
		}else if(id=="bio-profile-transaction"){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioprofileTrans.action";
			$("#form1").submit();
		}else if(id=="bio-bulkdisbursment"){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioprofilebulkDis.action";
			$("#form1").submit();
		}else if(id=="bio-send-money"){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioSendMoney.action";
			$("#form1").submit();
		}else if(id=="bio-request-money"){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioRequestMoney.action";
			$("#form1").submit();
		}else if(id=="bio-request-money-approve"){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioRequestMoneyApprove.action";
			$("#form1").submit();
		}
		
	});
});

</script>

</head>
<body >
	<form name="form1" id="form1" method="post" autocomplete="off">
	
		
		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Bio Activities</a></li>
				</ul>
			</div>
 
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>Bio Activities
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content" id="top-layer-anchor">
				 <div style="text-align: center; text-decoration: none;">
					<p>
						<a style="width:200px;height: 40px !important;padding-top: 30px !important;" href="#" class="btn btn-success" id="bio-registration"   title='Bio Registration' data-rel='popover'  data-content='Bio Registration.'><i class='icon icon-users icon-white'></i>&nbsp;Bio Registration</a> &nbsp; 
	 					<a style="width:200px;height: 40px !important;padding-top: 30px !important;" href="#" class="btn btn-primary" id="bio-deposit"   title='Bio Deposit' data-rel='popover'  data-content='Bio Deposit'><i class='icon icon-plus icon-white'></i>&nbsp;Bio Deposit</a> &nbsp;
	 					<a style="width:200px;height: 40px !important;padding-top: 30px !important;" href="#" class="btn btn-link" id="bio-cashwdl"   title='Bio Cash Withdrawal' data-rel='popover'  data-content='Bio Cash Withdrawal'><i class='icon icon-minus icon-white'></i>&nbsp;Bio Cash Withdrawal</a> &nbsp;
 					</p>
 					<p>
						<a style="width:200px;height: 40px !important;padding-top: 30px !important;" href="#" class="btn btn-info" id="bio-send-money"   title='Bio Send Money' data-rel='popover'  data-content='Bio Send Money'><i class='icon icon-users icon-white'></i>&nbsp;Send Money</a> &nbsp; 
	 					<a style="width:200px;height: 40px !important;padding-top: 30px !important;" href="#" class="btn btn-warning" id="bio-request-money"   title='Bio Request Money' data-rel='popover'  data-content='Bio Request Money'><i class='icon icon-plus icon-white'></i>&nbsp;Requuest Money</a> &nbsp;
	 					<a style="width:200px;height: 40px !important;padding-top: 30px !important;" href="#" class="btn btn-info" id="bio-request-money-approve"   title='Bio Request Money Approval' data-rel='popover'  data-content='Bio Request Money Approval'><i class='icon icon-minus icon-white'></i>&nbsp;Request Money Approval</a> &nbsp;
 					</p>
 					<p>
	 					<a style="width:200px;height: 40px !important;padding-top: 30px !important;" href="#" class="btn btn-link" id="bio-profile-balance"   title='Bio Profile Balance Enquiry' data-rel='popover'  data-content='Bio Profile Balance Enquiry'><i class='icon icon-users icon-white'></i>&nbsp;Bio Profile Balance</a> &nbsp;
	 					<a style="width:200px;height: 40px !important;padding-top: 30px !important;" href="#" class="btn btn-primary" id="bio-profile-transaction"   title='Bio Profile Transaction History' data-rel='popover'  data-content='Bio Profile Transaction History'><i class='icon icon-users icon-white'></i>&nbsp;Bio Profile Transaction History</a> &nbsp;
	 					<a style="width:200px;height: 40px !important;padding-top: 30px !important;" href="#" class="btn btn-success" id="bio-bulkdisbursment"   title='Bio Bulk Disbursment' data-rel='popover'  data-content='Bio Bulk Disbursment'><i class='icon icon-minus icon-white'></i>&nbsp;Bulk Disbursment</a> &nbsp;
 					</p>
 				 </div>	
			</div>
			</div>
		</div> 
	</div>	
		
 </form>
</body>
</html>
