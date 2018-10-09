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

<script src="${pageContext.request.contextPath}/pagenationjs/jquery-1.12.2.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/dataTables.colVis.js"></script>

<script type="text/javascript">

	var bioStatusRes;
	
	$(function(){
		
		$("#submit").click(function(){
			var amt=$("#amount").val();
			var requestFromIdNumber=$("#requestFromIdNumber").val();
			
			if(amt=="" || amt=="0" || amt=="0.0"){
				alert("Please enter amount.");
				return false;
			}else if(requestFromIdNumber==""){
				alert("Please enter Request From ID Number.");
				return false;
			}else{
				//if(bioStatusRes == "Y"){
					$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioRequestMoneyConfirm.action";
					$("#form1").submit();
					return true;
				/* }else{
					alert("Bio Verification Failed.Please try again for complete Transaction.");
					return false;
					$("#amount").val("");
					$("#modeOfDeposit").val("");
				} */
			}
			
			
		});
		
		$("#back").click(function(){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioAct.action";
			$('#form1').submit();
		});
	
		
				
		
	});
	
	
	 function validateBio(){
			var biourl ='<%=request.getContextPath()%>'+'/AgencyBanking/ValidateBiometric';
		    var bio_resval="";
		    //alert("biourl:="+biourl);
		    $.ajax
		    ({
		    type: "POST",
		    url: biourl,
		    cache: false,
		    success: function(html)
		    {
		    	//alert(html);
		     var bioval =jQuery.parseJSON(html);
		     bio_resval=bioval.biores;
		     //alert(bio_resval);
		     if (bio_resval == "Y")
		     {
		    	 //alert("inside true");
		    	 bioStatusRes="Y";
		     }
		     else
		     {
		    	 bioStatusRes="N";
		      alert("Bio Verification Failed.Please try again for complete Transaction.");
		      $("#amount").val("");
			  $("#modeOfDeposit").val("");
		     }
		    }, 
		     error: function (jqXHR, textStatus, errorThrown)
		      {
		      alert("Error moving to next");
		      }
		    });
		} 

	

</script>

</head>
<body >
	<form name="form1" id="form1" action="" method="post" >
	<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="home.action">Bio Activities</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Bio Request Money</a></li>
				</ul>
			</div>
			
			<div>
				<ul class="breadcrumb">
					<li><a href="#"  style="font-size: 15px;font-variant: inherit;">Bio Request Money</a></li>
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
						 	<tr >
						 		<td width="50%">
						 						
						 			<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
										<tr  class="even">
											 <td width="25%"><label for="Address Line1"><strong>Profile Id</strong></label></td>
											 <td> ${responseJSON.PROFILE_ID}<input name="profileId" type="hidden"  id="profileId" class="field" maxlength='30' value="${responseJSON.PROFILE_ID}"></td>
										</tr>
										<tr>	
											 <td width="20%"><label for="Address Line1"><strong>Profile Name</strong></label></td>
											 <td>${responseJSON.NAME}<input name="customerName" type="hidden"  id="customerName" class="field" maxlength='30' value="${responseJSON.NAME}"></td>
										</tr>
										
										<tr  class="odd">
											 <td width="20%"><label for="Address Line1"><strong>Date of Birth</strong></label></td>
											 <td>${responseJSON.DOB}<input name="dateOfBirth" type="hidden"  id="dateOfBirth" class="field" maxlength='30' value="${responseJSON.DOB}"></td>
										</tr>
										<tr>
											 <td width="20%"><label for="Address Line1"><strong>Mobile No</strong></label></td>
											 <td>${responseJSON.MSISDN}<input name="mobileNo" type="hidden"  id="mobileNo" class="field" maxlength='30' value="${responseJSON.MSISDN}"></td>
										</tr>
										<tr  class="even">
											 <td width="20%"><label for="Address Line1"><strong>Email Id</strong></label></td>
											 <td>${responseJSON.EMAILADDRESS}<input name="emailId" type="hidden"  id="emailId" class="field" maxlength='30' value="${responseJSON.EMAILADDRESS}"></td>
										</tr>
										<tr>	
											 <td width="20%"><label for="Address Line1"><strong>National Id</strong></label></td>
											 <td>${responseJSON.IDNUMBER}<input name="nationalId" type="hidden"  id="nationalId" class="field" maxlength='30' value="${responseJSON.IDNUMBER}"></td>
										</tr>
										<tr  class="even">
											 <td width="20%"><label for="Address Line1"><strong>Balance</strong></label></td>
											 <td >${responseJSON.BALANCE} /-kshs<input name="balance" type="hidden"  id="balance" class="field" maxlength='30' value="${responseJSON.BALANCE}"></td>
										</tr>
										<tr  class="odd" >
											 <td width="20%"><label for="Address Line1"><strong>Request From ID Number<font color="red">*</font></strong></label></td>
											 <td colspan="3"><input name="requestFromIdNumber" type="text"  id="requestFromIdNumber" class="field"  value=""></td>
										</tr>
										<tr  class="odd" >
											 <td width="20%"><label for="Address Line1"><strong>Amount<font color="red">*</font></strong></label></td>
											 <td colspan="3"><input name="amount" type="text"  id="amount" class="field" maxlength='30' value="" onchange="validateBio()"></td>
										</tr>
										
						 			</table>
						 		</td>
						 		<td>
						 			<%
										ResourceBundle resource = ResourceBundle.getBundle("BioConfiguration");
										String sPath=resource.getString("ipport");
										System.out.println(sPath);
						
									%>
									<jsp:plugin type="applet" code="FM220Deposit.class" codebase="/CevaBase/jsp/test1" archive="FM220SDK.jar" width = "500" height = "400">
									<jsp:params>
											    <jsp:param name="msg" value="<%= sPath %>" />
											    <jsp:param name="memberId" value='<%= session.getAttribute("BIO_MID") %>' />
											    <jsp:param name="makerId" value='<%= session.getAttribute("BIO_MID") %>' />
											    <jsp:param name="JSESSIONID" value="<%= request.getSession(false).getId() %>" />
									</jsp:params>
										<jsp:fallback>
											<p>Unable to load applet</p>
										</jsp:fallback>
									</jsp:plugin>
									<br/><br/><br/>
									
									<input  class="btn btn-danger" type="submit"  name="back" id="back" value="Back" />  &nbsp;&nbsp;
									<input class="btn btn-success" type="submit" name="submit" id="submit" value="Confirm" />
									
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
