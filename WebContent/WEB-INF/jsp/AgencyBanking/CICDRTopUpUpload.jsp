
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 <%@taglib uri="/struts-tags" prefix="s"%> 
 
<script src="${pageContext.request.contextPath}/js/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui-1.8.21.custom.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.layout.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/i18n/grid.locale-en.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/ui.multiselect.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.jqGrid.src.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.tablednd.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.contextmenu.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui-timepicker-addon.js"></script>
<script src="${pageContext.request.contextPath}/js/builddynamictable.js" type="text/javascript"></script>
 <script type="text/javascript">
			$.jgrid.no_legacy_api = true;
			$.jgrid.useJSON = true;
</script>
<style type="text/css">
.errors {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
</style>
<SCRIPT type="text/javascript">  
$(document).ready(function(){  
	 
	$('#btn-submit').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/billerRegistration.action?pid=91";
		$("#form1").submit();					
	}); 
	
	$('#btn-Cancel').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/home.action";
		$("#form1").submit();					
	}); 
	
});

function setupErrorsGrid(jsonErrors)
{
 var i=0;
 //var gridData = '{"sample":[{"uploadErrors":"Please Enter Member Name","excelRowNo":"1"},{"uploadErrors":"Please Enter Member Name","excelRowNo":"2"},{"uploadErrors":"Please Enter Member Name","excelRowNo":"3"},{"uploadErrors":"Please Enter Member Name","excelRowNo":"4"},{"uploadErrors":"Please Enter Member Name","excelRowNo":"5"}]}';
//alert(jsonErrors);
	document.getElementById("errorsDIV").style.display="block";
 var gridData =jsonErrors;
 
 var jsonArr = jQuery.parseJSON(jsonErrors);
 var jsArr = jsonArr.FINALJSON;
// console.log(jsonArr);
// console.log(jsArr.length);
 var htmlString="";
 $('#errorsBody').empty();

 for(i=0;i<jsArr.length;i++)
 {
  htmlString = htmlString + "<tr class='values' id='b"+(i+1)+"'>";
 // console.log(jsArr[i].excelRowNo+"   "+jsArr[i].uploadErrors);
 // htmlString = htmlString + "<td>" + "" + "</td>";
 // htmlString = htmlString + "<td>" + "" + "</td>";
 // htmlString = htmlString + "<td>" + "" + "</td>";
 // htmlString = htmlString + "<td>" + "" + "</td>";
  htmlString = htmlString + "<td>" + jsArr[i].excelRowNo + "</td>";
  htmlString = htmlString + "<td>" + jsArr[i].uploadErrors + "</td>";
 // htmlString = htmlString + "<td>" + "" + "</td>";

  htmlString = htmlString + "</tr>";

  $('#errorsBody').append(htmlString);

  htmlString="";
 }
}

var fileuploadRules = {
		rules : {
		uploadFile : { required : true },
		},
		messages : {
		uploadFile : { 
					required : "Please select the file."
				  }, 
		
			}
		};
		

function fileUpload(form, action_url, div_id) {
	//alert("hai babu r u there?");
	
		//$("#form1").validate(fileuploadRules);

		if($("#form1").valid()) {  
			
			
		
	document.getElementById("errorsDIV").style.display="none";
	
    var iframe = document.createElement("iframe");
    iframe.setAttribute("id", "upload_iframe");
    iframe.setAttribute("name", "upload_iframe");
    iframe.setAttribute("width", "0");
    iframe.setAttribute("height", "0");
    iframe.setAttribute("border", "0");
    iframe.setAttribute("style", "width: 0; height: 0; border: none;"); 
    form.parentNode.appendChild(iframe);
    window.frames['upload_iframe'].name = "upload_iframe";

    iframeId = document.getElementById("upload_iframe");

    var eventHandler = function ()
    {

            if (iframeId.detachEvent)
             iframeId.detachEvent("onload", eventHandler);
            else
             {
              iframeId.removeEventListener("load", eventHandler, false);
              //lightbox_close();
             // alert("UPLOADED SUCCESS FULLY..");
             }
			 
			//Sravan End Of Code

            if (iframeId.contentDocument)
            {
                content = iframeId.contentDocument.body.innerHTML;
            }
            else if (iframeId.contentWindow)
            {
                content = iframeId.contentWindow.document.body.innerHTML;
            }
            else if (iframeId.document)
            {
                content = iframeId.document.body.innerHTML;
            }

            document.getElementById(div_id).innerHTML = content;
            
        	var finaldatastatus = $('#'+div_id).text();
        	//alert(status);
        	
            
            
            var jsonStatus = jQuery.parseJSON(finaldatastatus);
            var errorStatus = jsonStatus.STATUS;
            
            console.log("errorStatus "+errorStatus);
           // console.log("jsonStatus "+jsonStatus);
         	// var i;
            
            
          // alert(status != 'null');
            
            //if(finaldatastatus != 'null' && finaldatastatus != '{}' && finaldatastatus != 'undefind' && finaldatastatus != '')
            if(errorStatus=='E')
            {
				//alert(status=='null');
				$("#successDIV").hide();
				$("#successDIV").html("<center><strong><font color='red'>UPLOADE FAILED</font></strong></center><br>");
				$("#MemberBeitPremimum").show();
				
            }else	
			if(errorStatus=='F')
            {
				//alert(status=='null');
				setupErrorsGrid($('#'+div_id).text());
				$("#successDIV").hide();
				
				
            }
            else
            {
				var noOfRowsUpdated=jsonStatus.NOOfRows;
            	//alert('success');
            	$("#errorsDIV").hide();
            	$("#successDIV").show();
            	$("#MemberBeitPremimum").show();
            	
				  // console.log('bbbbbbbbb ['+jsonStatus.ENDPOLDATA.FINALJSON+']');
           
			
				
				$("#successDIV").html("<center><strong>"+noOfRowsUpdated+" Records UPLOADED SUCCESS FULLY</strong></center><br>");
				
			/* 	// loadSelGrid(endpoldata,"endosementDataBody");
				
				var str='MCATEGORY,SUMTYPE,BENEFIT,SUBBENEFIT,MPLUSRULE,MEMBCOUNT,premiumamount,Nooffamilies,FAMILYPREMIUM,SUMASSURED,FLOATCOVERAGE';
				buildtablebody(endpoldata,str,"endosementDataBody");
				
				//fillpoilcyPremiumDetails(premiumDetails,"PolicyNopremiumDet");
			
			//For Filling TX details
			
			
				var taxDet = jsonStatus.BUSASUMDATA;
				fillPremiumDet(taxDet); */

				//console.log("taxDet "+taxDet);
				//console.log("Total Sum Assured "+taxDet.TotalSumAssured);
				
			 
            }
            
        }

    if (iframeId.addEventListener) iframeId.addEventListener("load", eventHandler, true);
    if (iframeId.attachEvent) iframeId.attachEvent("onload", eventHandler);

    form.setAttribute("target", "upload_iframe");
    form.setAttribute("action", action_url);
    form.setAttribute("method", "post");
    form.setAttribute("enctype", "multipart/form-data");
    form.setAttribute("encoding", "multipart/form-data");

    form.submit();

    document.getElementById(div_id).innerHTML = "Uploading...";
		
    return true;
		}else
			{
			console.log("form1 not valid");
			alert("form1 not valid");
			return false;
			}
		
}
function getOptionFor(thisValue,optionValue)
{
		return "<option value='"+optionValue+"'>"+thisValue+"</option>";
}
</SCRIPT>  
</head> 
<body>
	<form name="form1" id="form1" method="post">
	  <div id="content" class="span10"> 
			 
		    <div> 
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				   <li><a href="agentupload.action">DR TopUp File Upload </a>  </li>
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
				 <i class="icon-edit"></i>DR TopUp File Upload
				
			</div>
						
			<div class="box-content">
				<fieldset> 
									<table width="950" border="0" cellpadding="5" cellspacing="1"
						class="table">

						

						<tr>
							<td width="15%" class="odd"><label for="Card Inventory File">DR TopUp
									Excel File <font color="red">*</font>
							</label></td>
							<td width="25%" class="odd" align="left"><input type="file"
								name="uploadFile" id="uploadFile" /></td>

							<td class="odd" align="left" width="30%" ><input
								type="button" class="btn btn-success" name="cardsFile"
								id="cardsFile" value="Upload DR TopUp Excel File"
								onClick="fileUpload(this.form,'<%=request.getContextPath()%>/<%=appName %>'+'/drtopupuploadfile.action','testid')" />
								<div id="testid" style="display: none;"></div></td>

							<!-- <td class="odd"  align="left" ><input type="button"  class="btn btn-success" 
								 name="Initiate" id="SAMPLEXLSFILEDOWNLOAD" value="SAMPLEXLSFILEDOWNLOAD" width="100" onClick="downloadxls()"></input></td> -->

						</tr>

					</table>
							<div id="errorsDIV" style="display:none;">
     <div id="container" >

     <table  id="table"  style="width:100%; height:300px;text-align: center" border=1>
        <!-- <caption</caption> -->

      <thead>
       <tr>
        <!-- <th class="sorting_asc" tabindex="0" aria-controls="DataTables_Table_0" rowspan="1" colspan="1"  style="width: 10px;">S No</th> -->
        <!-- <th >Member</th>
        <th >Member Type</th>
        <th >Mobile</th>
        <th >Province</th> -->
        <th >Excel Row No</th>
        <th >Upload Failure Remarks</th>
       <!--  <th >Member ID</th> -->
       </tr>
      </thead>

      <tbody id="errorsBody" style="overflow:scroll;width:100%;height:70px;">
      </tbody>

     </table>
     </div>
    </div>
    	<div id="successDIV" style="display:none;">
		</div>
			</fieldset>  
		</div>
		</div>
		</div> 
         	<!-- <div class="form-actions" >
         		<input type="button" class="btn btn-primary" type="hidden"  name="btn-submit" id="btn-submit" value="Next" width="100" ></input>
          	 	&nbsp; <input type="button" class="btn " type="text"  name="btn-Cancel" id="btn-Cancel" value="Home" width="100" ></input>
            </div>   -->
               						 
	</div> 
 </form>
</body>
</html> 