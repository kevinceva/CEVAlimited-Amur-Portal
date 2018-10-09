<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<link href="../css/chosen.min.css" 	rel="stylesheet" type="text/css" media="screen"  /> 
<link href="../css/agency-app.min.css"	rel="stylesheet" type="text/css" />
<script type="text/javascript"	src="../js/jquery-1.7.2.min.js"></script>
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@page import="com.ceva.base.common.utils.DBConnector"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Random"%>
<%@ page import="java.sql.*" %>
<script type="text/javascript">
 var path = '${pageContext.request.contextPath}';
</script>
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

.page {
	background: #004c89;
	width: 100%;
	height: 98%;
}

.header {
	width: 100%;
	height: 84px;
	background: linear-gradient(#CB0808 0%, #9D0606 100%);
	background: -moz-linear-gradient(#CB0808 0%, #9D0606 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #CB0808),
		color-stop(100%, #9D0606));
	background: -webkit-linear-gradient(#CB0808 0%, #9D0606 100%);
	border-bottom: 3px solid #052e56 !important
}
.explorer{
margin-top: 32px;
}
.link{
height: 40px !important;padding-top: 30px !important;
}
.odd{
background-color: rgb(232, 232, 232);
}
</style>


<script type="text/javascript">

function cashDeposit(){
	alert("HELLO");
	<%  
	    Connection connection = null;
	    
	    connection = DBConnector.getConnection();
	
	    PreparedStatement pstmt =null;
	
	    String profileId= request.getParameter("profileId");
	    String amount = request.getParameter("amount");
	    String modeOfDeposit = request.getParameter("modeOfDeposit");
	    String ref =""+new Random().nextInt(999999);
	    String status="";
	    String message="";
	    
	    String sql = "INSERT INTO W_REF_TXN (WITHDRAWALREF_ID,TXN_REF_NO,AMOUNT,STATUS,DATE_REF_GENERATED,TXNTYPE,PROFILE_ID,TXNDATE,PAYMENT_MODE,CHANNEL)"+ 
	    "VALUES (?,?,?,?,sysdate,?,?,sysdate,?,?)";
	    
	    pstmt = connection.prepareStatement(sql);
	    
	    
	    if(modeOfDeposit.equals("WEB")){
	    	status="S";
	    }else if(modeOfDeposit.equals("POS")){
	    	status="P";
	    }
	    
	    pstmt.setString(1, ref);
	    pstmt.setString(2, ref);
	    pstmt.setString(3, amount);
	    pstmt.setString(4, status);
	    pstmt.setString(5,"PCDP" );
	    pstmt.setString(6, profileId);
	    pstmt.setString(7, modeOfDeposit);
	    pstmt.setString(8, "BIO");
	    
	    int i=pstmt.executeUpdate();
	    
	    if(i>0){
	    	if(modeOfDeposit.equals("WEB")){
		    	
		    	 if(i>0){
		 	    	PreparedStatement pstmt1 = connection.prepareStatement("update W_PROFILEBALANCES set BALANCE=BALANCE+?,DATELASTUPDATED=sysdate where PROFILE_ID=?");
		 		    pstmt1.setString(1, amount);
		 		    pstmt1.setString(2, profileId);
		 		    int j=pstmt1.executeUpdate();
		 		    if(j>0){
		 		    	message="Cash Deposit Transaction successfully completed with Reference number:"+ref;
		 		    }
		 	    }
		    }else if(modeOfDeposit.equals("POS")){
		    	message="Your Deposit reference number is:"+ref+".Please complete Transaction in POS with this Reference number.";
		    }
	    }
	    
	%>
}

function loadData(){
	
	<%	
	System.out.println("session:::"+session);

		request.setAttribute("bio_mid", "BPWU"+new Random().nextInt(9999));
	%>
}

</script>

</head>
<body onload="loadData()" style="background: #004c89;">
	<form name="form1" id="form1" method="get" action="BioActivities.jsp" autocomplete="off">
	<div class="page">	
		<div class="header">
			<img alt="" src="../images/logo.png"  style="width: 25%; margin-top: 5px;">
		</div>
		<div class="explorer" style=" background-color: #FFFFFF !important; margin-left: 196px; border-radius: 2px;">
		<div id="content" class="span10" style=" background-color:#FFFFFF !important;">

			<div>
				<ul class="breadcrumb">
					<li><a href="#"  style="font-size: 15px;font-variant: inherit;">Bio Cash Deposit</a></li>
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
			<span id="ajaxGetUserServletResponse"></span>
			<div class="box-content" id=" ">
			 <fieldset>
			 <table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
				<tr  class="even">
					 <td width="20%"><%=message %></td>
				</tr>
				<tr  class="odd">
					 <td width="20%"><input class="btn btn-success" type="submit" name="submit" id="submit" value="Next" /></td>
				</tr>
				
			</table>
			</fieldset>
			</div>
			</div>
			</div>
			
		</div>
		</div>
		</div>
	</form>
</body>
</html>
