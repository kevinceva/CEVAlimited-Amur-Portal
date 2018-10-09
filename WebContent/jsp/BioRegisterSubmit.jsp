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

	var list = "dob".split(",");
	var datepickerOptions = {
				showTime: false,
				showHour: false,
				showMinute: false,
	  		dateFormat:'dd/mm/yy',
	  		alwaysSetTime: false,
			changeMonth: true,
			changeYear: true
	};
	
	var bioStatusRes;

	$(function(){
		
		$(list).each(function(i,val){
			$('#'+val).datepicker(datepickerOptions);
		});
		
		$("#submit").click(function(){
			if(bioStatusRes == "Y"){
				$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioRegConfirm.action";
				$("#form1").submit();
			}else{
				alert("Bio Registration Failed.Please try again for complete Registration.");
				$('#chk1').attr('checked', false);
				return false;
				
			}
			
		});
		
		$("#back").click(function(){
			$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioAct.action";
			$("#form1").submit();
		});
		
		$('#chk1').click(function() {
	        if ($(this).is(':checked')) {
	        	validateBio();
	        }
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
		      alert("Bio Registratio Failed.Please try again for complete Registration.");
		      $('#chk1').attr('checked', false);
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
	<form name="form1" id="form1" method="post" action="" autocomplete="off">
		
		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="home.action">Bio Activities</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Bio Registration</a></li>
				</ul>
			</div>
			
			<div>
					<ul class="breadcrumb">
						<li><a href="#" style="font-size: 15px;font-variant: inherit;">Bio Registration</a></li>
					</ul>
				</div>
	
	
				<div class="row-fluid sortable" >
						<div class="box span12" >
						<div class="box-header well" data-original-title>
								<i class="icon-edit"></i>Profile Details
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
							</div>
						</div>
			
						<div class="box-content">
						 <fieldset>
						 <table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
							<tr  class="even">
								 <td width="20%"><label for="Address Line1"><strong>Profile Id<font color="red">*</font></strong></label></td>
								 <td>${profileId }<input name="profileId" type="hidden"  id="profileId" class="field" maxlength='30' value="${profileId }" readonly></td>
							</tr>
							
							<tr class="odd">
								 <td width="20%"><label for="Profile Name"><strong>First Name<font color="red">*</font></strong></label></td>
								 <td>${firstName }<input name="firstName" id="firstName" class="field" type="hidden"  maxlength='30' value="${firstName }" /></td>
							</tr>
							<tr class="even">
								 <td width="20%"><label for="Profile Name"><strong>Last Name<font color="red">*</font></strong></label></td>
								 <td>${lastName }<input name="lastName" id="lastName" class="field" type="hidden"  maxlength='30' value="${lastName }" /></td>
							</tr>
							<tr  class="even">
								 <td width="20%"><label for="Address Line1"><strong>Date Of Birth<font color="red">*</font></strong></label></td>
								 <td>${dob }<input name="dob" type="hidden"  id="dob" class="field" maxlength='30' value="${dob }"></td>
							</tr>
							<tr  class="odd">
								 <td width="20%"><label for="Address Line1"><strong>National Id<font color="red">*</font></strong></label></td>
								 <td>${nationalId }<input name="nationalId" type="hidden"  id="nationalId" class="field" maxlength='30' value="${nationalId }"></td>
							</tr>
							<tr  class="even">
								 <td width="20%"><label for="Address Line1"><strong>Mobile No<font color="red">*</font></strong></label></td>
								 <td>${mobileNo }<input name="mobileNo" type="hidden"  id="mobileNo" class="field" maxlength='30' value="${mobileNo }"></td>
							</tr>
							<tr  class="even">
								 <td width="20%"><label for="Address Line1"><strong>Email<font color="red">*</font></strong></label></td>
								 <td>${emailId }<input name="emailId" type="hidden"  id="emailId" class="field" maxlength='30' value="${emailId }"></td>
							</tr>
							<tr class="odd">
								<td colspan="2">
									<%
											ResourceBundle resource = ResourceBundle.getBundle("BioConfiguration");
											String sPath=resource.getString("ipport");
											System.out.println(sPath);
									
									%>
									<jsp:plugin type="applet" code="FM220SDKGUIRegisterMultipleIdsDemo.class" codebase="/CevaBase/jsp/test1" archive="FM220SDK.jar" width = "500" height = "400">
									<jsp:params>
											    <jsp:param name="msg" value="<%= sPath %>" />
											    <jsp:param name="memberId" value='<%= session.getAttribute("BIO_MID")  %>' />
											    <jsp:param name="makerId" value='<%=(String)session.getAttribute(CevaCommonConstants.MAKER_ID) %>' />
											    <jsp:param name="JSESSIONID" value="<%= request.getSession(false).getId() %>" />
									</jsp:params>
										<jsp:fallback>
											<p>Unable to load applet</p>
										</jsp:fallback>
									</jsp:plugin>
									
								</td>
							</tr>
							<tr  class="odd" >
								 <td colspan="2">
								 	<input type="checkbox" name="chk1" id="chk1" value="N">
								 	<label for="Address Line1"><strong>Please select check box for proceed.<font color="red">*</font></strong></label>
								 	
								 </td>
							</tr>
							<tr class="even">
								<td colspan="2">
									<input class="btn btn-danger" type="submit" name="back" id="back" value="Back" /> &nbsp;&nbsp;&nbsp;
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
