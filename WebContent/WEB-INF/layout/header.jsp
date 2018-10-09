<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%
	String appName = session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString();

	String userLevel = "";
	String location = "";
	String logintime = "";

	try {
		userLevel = session.getAttribute("userLevel").toString() == null
				? " "
				: session.getAttribute("userLevel").toString();
		location = session.getAttribute("location").toString() == null
				? " "
				: session.getAttribute("location").toString();
		logintime = session.getAttribute("loginTime").toString() == null
				? " "
				: session.getAttribute("loginTime").toString();
	} catch (Exception e) {
		userLevel = " ";
		location = " ";
		logintime = " ";
	}
%>


<meta charset="utf-8" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/sha256.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/rightclick_script.js"></script>
<script type="text/javascript">


$(function () { 
	 var v=<%=session.getAttribute("pid").toString()%>;
	 
	 $("#TAB-"+v).addClass("active");
	 
});
	
$(function(){
	 
	 $("form").submit(function(){
		 //alert($(this).attr('id'));
		 var totval="";
		 var totkey=""; 
		  
		
		 $('#'+$(this).attr('id')+' input:not(:disabled),#'+$(this).attr('id')+' select:not(:disabled),#'+$(this).attr('id')+' textarea:not(:disabled)').each(
				    function(index){  
				        var input = $(this);
				        if(typeof(input.attr('name'))  === "undefined"){
				        	
				        } else{
				        
				       // if(input.attr('type')!="button" && input.attr('type')!="radio" && input.attr('type')!="checkbox"  && input.attr('type')!="file" && input.attr('name')!="adminType"){
				        	if(input.attr('type')!="button" && input.attr('type')!="radio" && input.attr('type')!="checkbox"  && input.attr('type')!="file"){
				        	//alert('Type: ' + input.attr('type') + '-----Name: ' + input.attr('name') + '-----Value: ' + input.val());
				        	totkey=totkey+""+input.attr('name')+",";
				        	totval=totval+""+input.val()+",";
				        	}
				        }
				      
				    }
				);
		 <%-- var salt = "<%=session.getAttribute("SALT").toString() %>";  --%>
		//alert(salt);
		
		var input1 = $("<input />").attr("type", "hidden").attr("name", "keyvalue").val(b64_sha256(totval+"<%=session.getAttribute("SALT").toString()%>"));
		var input2 = $("<input />").attr("type", "hidden").attr("name", "keys").val(totkey);
			
			$('#'+$(this).attr('id')).append(input1);
			$('#'+$(this).attr('id')).append(input2);
		});
});

</script>

<script type="text/javascript">    
    
function unique(list) {
    var result = [];
    $.each(list, function(i, e) {
        if ($.inArray(e, result) == -1) result.push(e);
    });
    return result;
}

function checkExists(sel) {
    var status = false;
    if ($(sel).length) status = true;
    return status;
} 

</script>
<div class="header-body">
	<div class="main-logo">
		<a href="home.action"><img
			src="${pageContext.request.contextPath}/images/logo.png"
			alt="Amur Logo" /></a>
	</div>
	<div class="account-button">		
		<div class="dropdown">
			<div class="dropdown">
				<button class="btn btn-logout dropdown-toggle" type="button" data-toggle="dropdown">
					<%=(String) session.getAttribute(CevaCommonConstants.MAKER_ID)%> <span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li>
			           <a href="${pageContext.request.contextPath}/logout.action" target="_parent">Logout</a>
			       </li>
				</ul>
			</div>
		</div>
	</div>
</div>