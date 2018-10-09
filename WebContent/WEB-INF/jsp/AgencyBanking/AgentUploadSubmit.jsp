
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
<style type="text/css">
.errors {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
</style>
<SCRIPT type="text/javascript">  
$.jgrid.no_legacy_api = true;
$.jgrid.useJSON = true;


function processfile(){
	
		 $("#form1")[0].action='<%=request.getContextPath()%>'+'/cardsfileprocess.action';
			$("#form1").submit();	 
	
		}
		
$(document).ready(function() {		
	
	console.log("value of json:"+'${finalerrrespjson}');
	console.log("value of json ddd:"+'${finalerrrespjson.FINALJSON}');
	console.log("value of finale fname:"+'${finalerrrespjson.FILENAME}');
	console.log("value of finale json 111111:"+'<%= request.getAttribute("FINALJSON")%>');
	

var filename = '${finalerrrespjson.FILENAME}';
var status = '${finalerrrespjson.STATUS}';
var userid = '<%= (String) request.getAttribute("userid")%>';

$('#fname').val(filename);
$('#upid').val(userid);
$('#filename').val(filename);

});

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
    	
       // console.log(status);
        
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
			$("#successDIV").show();
			$("#successDIV").html("<center><strong><font color='red'>UPLOADE FAILED</font></strong></center><br>");
			$("#MemberBeitPremimum").hide();
			
        }else	
		if(errorStatus=='F')
        {
			//alert(status=='null');
			setupErrorsGrid($('#'+div_id).text());
			$("#successDIV").hide();
			$("#MemberBeitPremimum").hide();
			
        }
        else
        {
			var noOfRowsUpdated=jsonStatus.NOOfRows;
        	//alert('success');
        	$("#errorsDIV").hide();
        	$("#successDIV").show();
        	$("#MemberBeitPremimum").show();
        	
			  // console.log('bbbbbbbbb ['+jsonStatus.ENDPOLDATA.FINALJSON+']');
       
			var endpoldata = jsonStatus.ENDPOLDATA.FINALJSON;
			
			// var premiumDetails = jsonStatus.PremiumDetails.FINALARRAY;
				
			//console.log('premiumDetails '+premiumDetails);
				/* for(i=0;i<endpoldata.length;i++)
					console.log(i+":   "+endpoldata[i]); */
					
					
				
			var policyStatus = 	jsonStatus.POLICYSTATUS;
					
			//console.log('hai '+endpoldata);
			console.log(policyStatus);
			$("#PolicyUnderwriStatus").html(policyStatus.policyUWStatus);
			$("#PolicyEndType").html(policyStatus.policyEndosmenttype);
			$("#PolicyEndStatus").html(policyStatus.policyEndosmentstatus);
			
			
			$("#successDIV").html("<center><strong>"+noOfRowsUpdated+" Records UPLOADED SUCCESS FULLY</strong></center><br>");
			
			// loadSelGrid(endpoldata,"endosementDataBody");
			
			var str='MCATEGORY,SUMTYPE,BENEFIT,SUBBENEFIT,MPLUSRULE,MEMBCOUNT,premiumamount,Nooffamilies,FAMILYPREMIUM,SUMASSURED,FLOATCOVERAGE';
			buildtablebody(endpoldata,str,"endosementDataBody");
			
			//fillpoilcyPremiumDetails(premiumDetails,"PolicyNopremiumDet");
		
		//For Filling TX details
		
		
			var taxDet = jsonStatus.BUSASUMDATA;
			fillPremiumDet(taxDet);

			//console.log("taxDet "+taxDet);
			//console.log("Total Sum Assured "+taxDet.TotalSumAssured);
			
		 
        }
        
    }

function setupErrorsGrid(jsonErrors)
{
 var i=0;
 //var gridData = '{"sample":[{"uploadErrors":"Please Enter Member Name","excelRowNo":"1"},{"uploadErrors":"Please Enter Member Name","excelRowNo":"2"},{"uploadErrors":"Please Enter Member Name","excelRowNo":"3"},{"uploadErrors":"Please Enter Member Name","excelRowNo":"4"},{"uploadErrors":"Please Enter Member Name","excelRowNo":"5"}]}';
//alert(jsonErrors);
	
 var gridData =jsonErrors;
 // console.log('gridData '+gridData);
 
 var jsonArr = jQuery.parseJSON(jsonErrors);
 var jsArr = jsonArr.FINALJSON;
// console.log(jsonArr);
// console.log(jsArr.length);
 var htmlString="";
 $('#errorsBody').empty();
 
// console.log("jsArr.length ["+jsArr.length+"]");

 if(jsArr==undefined)
{
	 $("#successDIV").show();
	 $("#successDIV").html("<center><strong><font color='red'>UPLOADE FAILED</font></strong></center><br>");
}
 else
	 {
	 $("#successDIV").empty();
	 document.getElementById("errorsDIV").style.display="block";
	 
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
};

</script>
</head>
<body>
	<br></br>
	<div class="container-fluid">
		<div class="row-fluid">
			<div>
				<ul class="breadcrumb">
					<li><a href="#">Card Inventory Upload</a> <span
						class="divider"></span></li>
				</ul>
			</div>

			<center>
			<table height="5">
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
			</center>

			<div id="UserRoles" class="divtag">
				<form name="form1" id="form1" enctype="multipart/form-data"
					method="post">

			<table width="950" border="0" cellpadding="5" cellspacing="1"
				class="table">

				<tr>

				
					<td width="25%" class="odd"><label for="FileName ">File Name </label></td>
					<td width="25%" class="odd" align="left">				
					  <input type="text" name="fname" id="fname" readonly  /> 
					   <input type="hidden" name="filename" id="filename" readonly  /> 
					</td>	
										<td width="25%" class="odd"></td>
					<td width="25%" class="odd" align="left"></td>			
				</tr>
				
<!-- 				
				<tr>
					<td width="25%" class="even"><label for="TotalRec">Total Records </label></td>
					<td width="25%" class="even" align="left"> 				
					  <input type="text" name="totrec" id="totrec"  readonly /> 	
					</td>
					
					<td width="25%" class="even"><label for="UploadedBy">Uploaded By </label></td>
					<td width="25%" class="even" align="left"> 				
					  <input type="text" name="upid" id="upid"  readonly /> 	
					</td>				
			
				</tr> -->

			</table>
			
<div id="errorsDIV" style="display:none;">
     <div id="container" style="width:100%; height:300px;">

     <table class="zebra" id="table" style="width:100%;">
        <!-- <caption</caption> -->

      <thead>
       <tr>
        <th class="sorting_asc" tabindex="0" aria-controls="DataTables_Table_0" rowspan="1" colspan="1"  style="width: 10px;">S No</th> 
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

		<table>
			<tr>
				<td>
					<div id="fourthnext">
						<input type="button" class="btn btn-success" name="Submit"
							id="Submit" value="Process" width="100" onClick="processfile()"></input>
					</div>


				</td>
			</tr>
		</table>


		</form>
</div>
	</div>
	</div>

</body>


</html>