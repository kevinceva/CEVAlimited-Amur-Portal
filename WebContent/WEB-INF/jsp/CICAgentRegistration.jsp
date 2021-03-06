
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
 <%@taglib uri="/struts-tags" prefix="s"%> 
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 
<SCRIPT type="text/javascript"> 
var toDisp = '${type}';

$(document).ready(function(){


	$('#btn-submit').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/cicagentregsave.action?";
		$("#form1").submit();					
	});	
			
});

function fetchName()
{
	var formInput = $('#form1').serialize();
	console.log("value of formInput :"+formInput);
	var agnurl ='<%=request.getContextPath()%>/<%=appName %>/fetchName.action';
    var bio_resval="";
    //alert("biourl:="+biourl);
    $.ajax
    ({
    type: "POST",
    url: agnurl,
    data:formInput,
    cache: false,
    success: function(html)
    {
    	
     var clnreg = html.responseJSON.remarks;
     var exst_cnt = html.responseJSON.exst_cnt;
     console.log("value of clnreg:"+clnreg+"--"+exst_cnt);
     if ( clnreg == "Y")
    {	 
     $("#cname").val(html.responseJSON.cname);
	     if ( exst_cnt == 0)
	     {	 
		     document.getElementById("act-grid").style.display="block";
		     document.getElementById("nodata-grid").style.display="none";   
	    }else
	    {
	    	document.getElementById("nodata-grid").style.display="none"; 
	    	document.getElementById("exst-grid").style.display="block";
	    }	
	 }
     else
    {
    	 document.getElementById("exst-grid").style.display="none";
    	 document.getElementById("nodata-grid").style.display="block";
    }
    }, 
     error: function (jqXHR, textStatus, errorThrown)
      {
      alert("Error moving to next");
      }
    });
}
	//--> 
</SCRIPT>
    
  
		
</head>

<body>
	<form name="form1" id="form1" method="post">
	<!-- topbar ends -->
	 <div id="content" class="span10"> 
			 
		    <div> 
				<ul class="breadcrumb">
				 <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="queries.action">CIC Agent Registration</a> <span class="divider"> &gt;&gt; </span></li>
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
		 	<div class="row-fluid sortable">
           
	<div id="data-grid"  class="box span12" >
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>Agent Details 
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>
						
		<div  class="box-content" >
			<fieldset> 
				<span > <font color="green" size="4"> ${responseJSON.result}</font> </span>
				<table width="950"  border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
						<tr class="odd" > 
							<td width="25%" ><strong><label for="Agent Mobile Number"> Agent Mobile No</label></strong></td>
							<td width="25%" ><input type="text" name="mob"  id="mob"  onchange="fetchName()"/> </td> 
							<td width="25%" ><strong><label for="Agent Name"> Agent Name</label></strong></td>
							<td width="25%" >${responseJSON.CLN_LIST.cname} <input type="text" name="cname"  id="cname" value="${responseJSON.cname}" readonly/></td>
						</tr>
 						<tr class="odd" > 
							<td width="25%" ><strong><label for="Email"> Email Id</label></strong></td>
							<td width="25%" ><input type="text" name="email"  id="email"  /> </td> 
							<td width="25%" ><strong></strong></td>
							<td width="25%" ></td>
						</tr>	 					
							
				</table>
			</fieldset> 
			
              
		</div>
		</div>
		</div>

			
	<div class="row-fluid sortable">
           
	<div id="nodata-grid" class="box span12" style="display:none;">
						
		<div class="box-content" >
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >	
									<tr>
										
										<td><strong><font color="red"><b> Client Not Registered / InActive</b></font></strong> </td>
									
									</tr>
		
				</table>
			</fieldset> 
			
              
		</div>
		</div>
		</div>
		
		<div class="row-fluid sortable">
		<div id="exst-grid" class="box span12" style="display:none;">
						
		<div class="box-content" >
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >	
									<tr>
										
										<td><strong><font color="red"><b> Client Already Registered </b></font></strong> </td>
									
									</tr>
		
				</table>
			</fieldset> 
			
              
		</div>
		</div>											
		
		</div> 	
		<div id="act-grid" class="form-actions" style="display:none;">
		   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Save"   ></input>
		  </div>  
              						 
	</div><!--/#content.span10-->
		  
 </form>
</body>
</html>

