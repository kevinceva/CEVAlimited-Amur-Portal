
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
   
 
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/treenode/css/demo.min.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/treenode/css/zTreeStyle/zTreeStyle.min.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/css/treenode/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/css/treenode/jquery.ztree.excheck-3.5.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/css/treenode/jquery.ztree.exhide-3.5.min.js"></script>
<style type="text/css">
label.error, .errors {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
} 
</style>
<SCRIPT type="text/javascript">
var checBoxCnt = 0;	
	function checkChecked(classCheck){
			
		if(classCheck == "button chk checkbox_true_full" 
			|| classCheck == "button chk checkbox_true_part" 
			 ||classCheck == "button chk checkbox_true_disable")	return true;
		
		return false;
	}
	
	function getCheckVal(obj){ 
		var getText = "#"+$(obj).attr('id') +" > a > span:eq(1)";
		getText = $(getText).text(); 
		return getText;
	}	 
	
	function constructString(parentId) { 
		var getText = "";  
		var checkedStringConstruction="";	
		$(parentId).each(function(mainindex,mainele){   // Top Parent Iteration
		 
			var status = false;
			var pid = $(this).attr('class').replace("level","");
			var id = $(this).attr('id').replace("treeDemo_","");
			//console.log(" class is :: "+ $(this).attr('class') );  
			 
			var parentId = $(this).attr('id');
			//console.log(" parentId is :: "+ parentId);
			
			var span1Class = "#"+parentId+" > span#"+parentId+"_switch";
			var span2Class = "#"+parentId+" > span#"+parentId+"_check";
			 
			//console.log(" span1Class is :: "+ span1Class  ); 
			//console.log(" span2Class is :: "+ span2Class  ); 
			//console.log(" Tag name is :: "+ $(this).get(0).tagName +" Status is ["+status +"] " + $(span2Class).attr('class')); 
			
			getText = getCheckVal(this);
			
			//checkedStringConstruction+='{ id:'+id+', pId:'+pid+',  name:"'+getText+'" , checked:'+checkChecked($(span2Class).attr('class'))+' , open:true, title:"" },';
			checkedStringConstruction+='{ "id":"'+id+'", "pId":"'+pid+'",  "name":"'+getText+'" , "checked":"'+checkChecked($(span2Class).attr('class'))+'" , "open":"true", "title":"", "chkDisabled":"true"},';
		 
		 
			var getUlList = "#"+parentId +' > ul > li'; 
			//console.log("|constructStringSub|  getUlList == "+ getUlList + " getUlList.length == "+ $(getUlList).length);
			if($(getUlList).length > 0) {
				checkedStringConstruction+=constructStringSub(getUlList,parentId); 
			} else return;
			 
			 
		});  
	
		return checkedStringConstruction;
	} 
	function constructStringSub(parentSubId,parentId) {
	
		var getText = "";  
		var str="";	
		$(parentSubId).each(function(mainindex,mainele) {   // Sub Child Iteration
		 
			var status = false;
			//var pid = $(this).attr('class').replace("level","");
			var pid = $("#"+parentId).attr('id').replace("treeDemo_","");
			var id = $(this).attr('id').replace("treeDemo_","");
			//console.log(" class is :: "+ $(this).attr('class') ); 
			
			var parentSubId = $(this).attr('id');
			//console.log(" parentSubId is :: "+ parentSubId);
			
			var span1Class = "#"+parentSubId+" > span#"+parentSubId+"_switch";
			var span2Class = "#"+parentSubId+" > span#"+parentSubId+"_check";
			 
			//console.log(" span1Class is :: "+ span1Class  ); 
			//console.log(" span2Class is :: "+ span2Class  ); 
			//console.log(" Tag name is :: "+ $(this).get(0).tagName +" Status is ["+status +"] " + $(span2Class).attr('class')); 
			
			getText = getCheckVal(this); 
			//str+='{ id:'+id+', pId:'+pid+',  name:"'+getText+'" , checked:'+checkChecked($(span2Class).attr('class'))+' , open:true, title:"" },';
			str+='{ "id":"'+id+'", "pId":"'+pid+'",  "name":"'+getText+'" , "checked":"'+checkChecked($(span2Class).attr('class'))+'" , "open":"true", "title":"","chkDisabled":"true"},';
	 
			var getUlList = "#"+parentSubId +' > ul > li';
			 
			//console.log("|constructStringSub|  getUlList == "+ getUlList + " getUlList.length == "+ $(getUlList).length);
			if($(getUlList).length > 0) {
				str+=constructStringSubSub(getUlList,parentSubId); 
			} else return;
		 
		}); 
		 
		return str;
	}
	
	function constructStringSubSub(parentSubSId,parentId) {
	
		var getText = "";  
		var str="";	
		$(parentSubSId).each(function(mainindex,mainele) {   // Sub Child Iteration
		 
			var status = false; 
			var pid = $("#"+parentId).attr('id').replace("treeDemo_","");
			var id = $(this).attr('id').replace("treeDemo_","");
			//console.log(" pid is :: "+ pid +" id is ::: "+ id); 
			
			var parentSubSId = $(this).attr('id');
			//console.log(" parentSubSId is :: "+ parentSubSId);
			
			var span1Class = "#"+parentSubSId+" > span#"+parentSubSId+"_switch";
			var span2Class = "#"+parentSubSId+" > span#"+parentSubSId+"_check";
			 
			//console.log(" span1Class is :: "+ span1Class  ); 
			//console.log(" span2Class is :: "+ span2Class  ); 
			//console.log(" Tag name is :: "+ $(this).get(0).tagName +" Status is ["+status +"] " + $(span2Class).attr('class')); 
			
			getText = getCheckVal(this); 
			//str+='{ id:'+id+', pId:'+pid+',  name:"'+getText+'" , checked:'+checkChecked($(span2Class).attr('class'))+' , open:true, title:"" },';
			str+='{ "id":"'+id+'", "pId":"'+pid+'",  "name":"'+getText+'" , "checked":"'+checkChecked($(span2Class).attr('class'))+'" , "open":"true", "title":"", "chkDisabled":"true"},';
	 
			var getUlList = "#"+parentSubSId +' > ul > li';
			 
			//console.log("|constructStringSub|  getUlList == "+ getUlList + " getUlList.length == "+ $(getUlList).length);
			if($(getUlList).length > 0) {
				str+=constructStringSubSub(getUlList); 
			} else return;
		 
		});  
		 
		return str;
	}
	
		//<!--
		var setting = {
			check: {
				enable: true
			},
			data: {
				key: {
					title: "title"
				},
				simpleData: {
					enable: true
				}
			},
			callback: {
				onCheck: onCheck
			}
		}; 
		var totalData ='${responseJSON.json_val}';
		var json = jQuery.parseJSON(totalData);
		 
		var zNodes = "";
		for(key in json)
		{  
			if(key =="user_details") { 
				zNodes =json[key];
				break;
			}
		}  
	 
		function onCheck(e, treeId, treeNode) { 
			count();
		}

		function setTitle(node) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = node ? [node]:zTree.transformToArray(zTree.getNodes());
			for (var i=0, l=nodes.length; i<l; i++) {
				var n = nodes[i];
				n.title = "[" + n.id + "] isFirstNode = " + n.isFirstNode + ", isLastNode = " + n.isLastNode;
				//zTree.updateNode(n);
			}
		}
		function count() {
			function isForceHidden(node) {
				if (!node.parentTId) return false;
				var p = node.getParentNode();
				return !!p.isHidden ? true : isForceHidden(p);
			}
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			checkCount = zTree.getCheckedNodes(true).length,
			nocheckCount = zTree.getCheckedNodes(false).length,
			hiddenNodes = zTree.getNodesByParam("isHidden", true),
			hiddenCount = hiddenNodes.length;

			for (var i=0, j=hiddenNodes.length; i<j; i++) {
				var n = hiddenNodes[i];
				if (isForceHidden(n)) {
					hiddenCount -= 1;
				} else if (n.isParent) {
					hiddenCount += zTree.transformToArray(n.children).length;
				}
			}

			$("#isHiddenCount").text(hiddenNodes.length);
			$("#hiddenCount").text(hiddenCount);
			$("#checkCount").text(checkCount);
			$("#nocheckCount").text(nocheckCount);
			checBoxCnt = checkCount;
		}
		function showNodes() {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getNodesByParam("isHidden", true);
			zTree.showNodes(nodes);
			setTitle();
			count();
		}
		function hideNodes() {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getSelectedNodes();
			if (nodes.length == 0) {
				alert("Please select one node at least.");
				return;
			}
			zTree.hideNodes(nodes);
			setTitle();
			count();
		}
		
		$(document).ready(function() {
			$('#prevJsonString').val(totalData);
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			$("#hideNodesBtn").bind("click", {type:"rename"}, hideNodes);
			$("#showNodesBtn").bind("click", {type:"icon"}, showNodes);
			setTitle();
			count();
			
			var methodToCheck = '${type}';
			
			if(methodToCheck == 'View') {
				$('#btn-modify').hide();
				$('#btn-view').show();
			} else {
				$('#btn-modify').show();
				$('#btn-view').hide();
			}
			
			$('#btn-submit').live('click',function() {  
				//console.log('inside submit..');
				//console.log('checBoxCnt['+checBoxCnt+']');
				if(checBoxCnt > 0) {
					var parentId = '#treeDemo > li'; 
					var getBuildStr = constructString(parentId); 
					getBuildStr = getBuildStr.substr(0,getBuildStr.lastIndexOf(",")); 
					
					var checkedParentStringConstruction = '{ "user_details" : [' + getBuildStr+ ']}'; 
					
					$('#jsonVal').val(checkedParentStringConstruction);
					$('#keyVal').val("user_details");
					$('#treeDemo').attr('disabled','selected');
					
					$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/confirmUserAssignDetails.action";
					$("#form1").submit(); 
				} else {
					$('#spn_error').text('Please assign rights to the user.');
 				} 
			});
			
			$('#btn-next,#btn-cancel').live('click',function() {  
 				$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/userGrpCreation.action?pid=7";
				$("#form1").submit();				
			});
			 
		});
		//--> 
	</SCRIPT>
</head>
 <body>
 <form name="form1" id="form1" method="post" >  
	<div id="content" class="span10">
		<div>
			<ul class="breadcrumb">
				<li>
					<a href="home.action"><b>Home</b></a> <span class="divider">&gt;&gt;</span>
				</li>
				<li>
					<a href="#"><b> User Management</b></a> <span class="divider">&gt;&gt;</span>
				</li>
				<li><a href="#"><b>Modify User Rights</a>  
				</li> 
				 
			</ul>
		</div> 

	<table height="2">
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
		<div class="box span12"> 
			<div class="box-header well" data-original-title>
				 <i class="icon-edit"></i>User Details 
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					
				</div>
			</div>
						
		<div class="box-content">
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
					 <tr > 
							<td width="20%" ><strong><label for="User Id"><strong>User Id</label></strong></td>
							<td width="30%" >${userId} </td>  
							<td width="20%" ><strong><label for="Employee No"><strong>Employee No</label></strong></td>
							<td width="30%" >${responseJSON.emp_id}</td>
						</tr>
						<tr > 
							<td><strong><label for="First Name"><strong>First Name</label></strong></td>
							<td>${responseJSON.first_name} </td> 
							<td><strong><label for="Last Name"><strong>Last Name</label></strong></td>
							<td> ${responseJSON.last_name}</td>
						</tr>
						<tr > 
							<td><strong><label for="Telephone Res"><strong>Telephone(Res)</label></strong></td>
							<td>${responseJSON.res_no}</td> 
							<td><strong><label for="Telephone Off"><strong>Telephone(Off)</label></strong></td>
							<td>${responseJSON.off_no}</td>
						</tr>

						 <tr > 
								<td><strong><label for="Mobile"><strong>Mobile</label></strong></td>
								<td>${responseJSON.mobile_no}</td> 
								<td><strong><label for="Fax"><strong>Fax</label></strong></td>
								<td>${responseJSON.fax}</td>
						 </tr>

						<tr > 
							  <td><strong><label for="User Level"><strong>User Level</label></strong></td>
							  <td>
								${responseJSON.user_level}
							 </td> 
							 <td><strong><label for="Office Location"><strong>Office Location</label></strong></td>
							  <td>
								${responseJSON.location}
							   </td>
						</tr>
						<tr >
					 
							<td><strong><label for="Expiry Date"><strong>Expiry Date</label></strong></td>
							<td>
								${responseJSON.expiry_date}
							</td> 
							<td><strong><label for="E-Mail"><strong>E-Mail</label></strong></td>
							<td>
								${responseJSON.email}
							</td>
						</tr> 
				</table>
			</fieldset> 
		</div>
		</div>
		</div> 

	<div class="row-fluid sortable"> 
		<div class="box span12">
								
			<div class="box-header well" data-original-title>
				 <i class="icon-edit"></i>User Rights 
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					
				</div>
			</div>
						
		<div class="box-content">
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
					 <tr class="odd">
						<td width="20%">&nbsp;</td>
					 
						<td width="80%"><div class="zTreeDemoBackground left">
							<ul id="treeDemo" class="ztree"></ul> 
						</div> 
						</td>
					</tr>
				</table>
			</fieldset> 
			
		</div>
		</div>
	</div>
	
	<div class="form-actions" id="btn-modify">
	   <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Submit" width="100" ></input>
	   &nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Cancel" width="100" ></input>
		<span class="errors" id="spn_error"></span>			   
	</div>
	<div class="form-actions" id="btn-view">
	   <input type="button" class="btn btn-primary" type="text"  name="btn-next" id="btn-next" value="Next" width="100" ></input> 			   
	</div>
</div><!--/#content.span10-->
		 
<input type="hidden" name="jsonVal"  id="jsonVal" value="" />
<input type="hidden" name="keyVal"  id="keyVal" value="" />
<input type="hidden" name="methodToCheck"  id="methodToCheck" value="${type}" /> 
<input type="hidden" name="prevJsonString"  id="prevJsonString" value="" /> 
<input type="hidden" name="userId"  id="userId" value="${userId}" /> 
<input type="hidden" value="${responseJSON.GROUP_ID}" id="groupID" name="groupID" /> 
<input type="hidden" value="${responseJSON.emp_id}" id="empNo" name="empNo" /> 

 </form>		
</body> 
</html>