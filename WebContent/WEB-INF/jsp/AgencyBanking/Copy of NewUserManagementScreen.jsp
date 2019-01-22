<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.css">
<title>Insert title here</title>
	 <script type="text/javascript">
	 $(function() {
		 	
		      //var tabTitle = $( "#tab_title" ),
		      var tabTitle = "Store Management",
		      tabContent = $( "#tab_content" ),
		      tabTemplate = "<li class='ui-state-default ui-corner-top'><a href='#href'>#label</a> <span class='ui-icon ui-icon-close' role='presentation'>Remove Tab</span></li>",
		      tabCounter = 2;
		 
		    var tabs = $( "#tabs" ).tabs();
		 
		    // modal dialog init: custom buttons and a "close" callback resetting the form inside
		    var dialog = $( "#dialog" ).dialog({
		      autoOpen: false,
		      modal: true,
		      buttons: {
		        Add: function() {
		          addTab();
		          $( this ).dialog( "close" );
		        },
		        Cancel: function() {
		          $( this ).dialog( "close" );
		        }
		      },
		      close: function() {
		        form[ 0 ].reset();
		      }
		    });
		 
		    // addTab form: calls addTab function on submit and closes the dialog
		    var form = dialog.find( "form" ).submit(function( event ) {
		      addTab();
		      dialog.dialog( "close" );
		      event.preventDefault();
		    });
		 
		    // actual addTab function: adds new tab using the input from the form above
		    function addTab(merchantId) {	
		    	console.log('adding');
		      var label = "Store Management" || "Tab " + tabCounter,
		        id = "tabs-" + tabCounter,
		        li = $( tabTemplate.replace( '#href', "#" + id ).replace( '#label', label ) ),
		        tabContentHtml = tabContent.val() || "Tab " + tabCounter + " content.";
		      //console.log('li.:'+li);
		      //console.log('tabContentHtml.:'+tabContentHtml);
		      tabs.find( ".ui-tabs-nav" ).append( li );
		      //tabs.append( "<div id='" + id + "' class='ui-tabs-panel ui-widget-content ui-corner-bottom'><p>" + tabContentHtml + "</p></div>" );
		      var reqUrl="<%=request.getContextPath()%>/stores?merchantId="+merchantId;
		      //console.log(reqUrl);
		      tabs.append( "<div id='" + id + "' class='ui-tabs-panel ui-widget-content ui-corner-bottom'><p>" + '<iframe src='+reqUrl+' width="100%" height="100%" allowtransparency="true" frameBorder="0" id="iframe-1">Your browser does not support IFRAME</iframe>' + "</p></div>" );
		      tabs.tabs( "refresh" );
		      tabCounter++;
		      $("tabs").tabs("enable", tabCounter);
		    }
		   
		    /* function addTabNew(tabData) {	
		    	alert('adding'+tabData);
		      var label = "Store Management" || "Tab " + tabCounter,
		        id = "tabs-" + tabCounter,
		        //li = $( tabTemplate.replace( /#\{href\}/g, "#" + id ).replace( /#\{label\}/g, label ) ),
		        li = $( tabTemplate.replace( '#href', "#" + id ).replace( '#label', label ) ),
		        tabContentHtml = tabData;
		      console.log('li.:'+li);
		      console.log('tabContentHtml.:'+tabContentHtml);
		      tabs.find( ".ui-tabs-nav" ).append( li );
		      tabs.append( "<div id='" + id + "' class='ui-tabs-panel ui-widget-content ui-corner-bottom'><p>" + tabContentHtml + "</p></div>" );
		      tabs.tabs( "refresh" );
		      tabCounter++;
		      tabData="";
		    } */
		 
		    
		    // addTab button: just opens the dialog
		    $( "#add_tab" )
		      .button()
		      .click(function() {
		        dialog.dialog( "open" );
		      });
		 
		    // close icon: removing the tab on click
		    tabs.delegate( "span.ui-icon-close", "click", function() {
		      var panelId = $( this ).closest( "li" ).remove().attr( "aria-controls" );
		      $( "#" + panelId ).remove();
		      tabs.tabs( "refresh" );
		    });
		 
		    tabs.bind( "keyup", function( event ) {
		      if ( event.altKey && event.keyCode === $.ui.keyCode.BACKSPACE ) {
		        var panelId = tabs.find( ".ui-tabs-active" ).remove().attr( "aria-controls" );
		        $( "#" + panelId ).remove();
		        tabs.tabs( "refresh" );
		      }
		    });
		    
		    $(document).on('click','a',function(event) {
		    	
		    	
		    	 var v_id=$(this).attr('id');
		    	var merchantId = $(this).attr('value');
				if(v_id == "MERCHANT_INFO"){
					addTab(merchantId);
				}
					
		    }); 
		    
		  });
      </script>
		
      <style>
         #tabs-1{font-size: 11px;}
         .ui-widget-header {
            background:#070A63;
            border: 1px solid #b9cd6d;
            color: #FFFFFF;
            font-weight: bold;
         }
         #tabs-2{font-size: 11px;}
         .ui-widget-header {
            background:#070A63;
            border: 1px solid #b9cd6d;
            color: #FFFFFF;
            font-weight: bold;
         }
      </style>
      <style>
		  #dialog label, #dialog input { display:block; }
		  #dialog label { margin-top: 0.5em; }
		  #dialog input, #dialog textarea { width: 95%; }
		  #tabs { margin-top: 1em;background:#FFFFFF; }
		  #tabs li .ui-icon-close { float: left; margin: 0.4em 0.2em 0 0; cursor: pointer; }
		  #add_tab { cursor: pointer; }
	 </style>
      
</head>
<body>
	
	
	<div id="content" class="span10"> 
			<div id="dialog" title="Tab data">
		  <form>
		    <fieldset class="ui-helper-reset">
		      <label for="tab_title">Title</label>
		      <input type="text" name="tab_title" id="tab_title" value="Tab Title" class="ui-widget-content ui-corner-all">
		      <label for="tab_content">Content</label>
		      <span name="tab_content" id="tab_content" class="ui-widget-content ui-corner-all">
						
			  </span>
		    </fieldset>
		  </form>
		</div>
		 
		<div id="tabs">
		  <ul>
		    <li><a href="#tabs-1">Merchant Management</a> <span class="ui-icon ui-icon-close" role="presentation">Remove Tab</span></li>
		  </ul>
		  <div id="tabs-1">
			    <div class="box-content" id="top-layer-anchor">
					 <div>
						<a href="#" class="btn btn-success" id="merchant-add"   title='Add New Merchant' data-toggle='popover'  data-content='Creating a new merchant.'><i class='icon icon-plus icon-white'></i>&nbsp;Add New Merchant</a> &nbsp; 
	 					<a href="#" class="btn btn-primary" id="merchant-dashboard"   title='Dashboard' data-toggle='popover'  data-content='Viewing the Merchants,Stores & Terminals.'><i class='icon icon-users icon-white'></i>&nbsp;Merchant Dashboard</a> &nbsp;
	 				 </div>	
				</div>
				
				
				<div class="row-fluid sortable">
						<div class="box span12" id="groupInfo">
							<div class="box-header well" data-original-title>Merchant Information
								<div class="box-icon"> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
									<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
								</div>
							</div> 
							<div class="box-content"> 
								<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable"  
									id="DataTables_Table_0" >
									<thead>
										<tr>
											<th>S No</th>
											<th>Merchant ID</th>
											<th class="hidden-phone">Merchant Name </th>
											<th>Status</th>
											<th class="hidden-phone">Date Created</th>
											<th>Actions</th>
										</tr>
									</thead> 
									<tbody id="merchantTBody">
										<s:iterator value="responseJSON['MERCHANT_LIST']" var="userGroups" status="userStatus"> 
											<s:if test="#userStatus.even == true" > 
												<tr class="even" index="<s:property value='#userStatus.index' />"  id="<s:property value='#userStatus.index' />">
											 </s:if>
											 <s:elseif test="#userStatus.odd == true">
			      								<tr class="odd" index="<s:property value='#userStatus.index' />"  id="<s:property value='#userStatus.index' />">
			   								 </s:elseif> 
												<td><s:property value="#userStatus.index+1" /></td>
												<!-- Iterating TD'S -->
												<s:iterator value="#userGroups" status="status" > 
													<s:if test="#status.index == 0" >  
														<td> <a href='#' id='MERCHANT_INFO' value='<s:property value="value" />'><s:property value="value" /></a></td> 
														
													</s:if>
													 <s:elseif test="#status.index == 1" >
														 <td class='hidden-phone'><s:property value="value"  /></td>
													 </s:elseif> 
													  <s:elseif test="#status.index == 2" >
															<s:set var="merchstatus" value="value" />
															
															<s:if test="#merchstatus == 'Active'" > 
																<s:set value="%{'false'}" var="labelstatus" scope="page"/>
																 <s:set value="%{'label-success'}" var="merchlabel" />
																 <s:set value="%{'btn btn-danger'}" var="statusclass" /> 
																 <s:set value="%{'Deactivate'}" var="text1" /> 
																 <s:set value="%{'icon-unlocked'}" var="text" /> 
															</s:if>
															<s:elseif test="#merchstatus == 'Inactive'" >
																<s:set value="%{'false'}" var="labelstatus" scope="page"/>
																 <s:set value="%{'label-warning'}" var="merchlabel" />
																 <s:set value="%{'btn btn-success'}" var="statusclass" /> 
																 <s:set value="%{'Activate'}" var="text1" /> 
																 <s:set value="%{'icon-locked'}" var="text" />
															 </s:elseif>
														 	<s:elseif test="#merchstatus == 'Un-Authorize'" >
															 	<s:set value="%{'true'}" var="labelstatus" scope="page"/>
																 <s:set value="%{'label-primary'}" var="merchlabel" />
																 <s:set value="%{' '}" var="statusclass" /> 
																 <s:set value="%{' '}" var="text1" /> 
																 <s:set value="%{' '}" var="text" />
															 </s:elseif> 
															 
														<td><a href='#' class='label <s:property value="#merchlabel" />' index="<s:property value='#userInDetStatus1.index' />" ><s:property value="value" /></a></td>												
			 										 </s:elseif> 
			 										 <s:elseif test="#status.index == 3" >
														 <td class='hidden-phone'><s:property value="value" /></td>
													 </s:elseif>
												</s:iterator> 
												<td><a id='store-create' class='btn btn-success' href='#' index="<s:property value='#userInDetStatus1.index' />" title='Create Store' data-toggle='tooltip'><i class='icon icon-plus icon-white'></i></a>&nbsp;
													<a id='merchant-modify' class='btn btn-warning' href='#' index="<s:property value='#userInDetStatus1.index' />" title='Merchant Modify' data-toggle='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;
													<a id='merchant-view' class='btn btn-info' href='#' index="<s:property value='#userInDetStatus1.index' />" title='View' data-toggle='tooltip'><i class='icon icon-note icon-white'></i></a>&nbsp;
													 
													<s:if test="#attr.labelstatus != 'true'" > 
														<a id='merchant-terminate' class='btn <s:property value="#statusclass" />' href='#' index="<s:property value='#userInDetStatus1.index' />" title='<s:property value="#text1" />' data-toggle='tooltip' ><i class='icon <s:property value="#text" /> icon-white'></i></a>&nbsp; 
													</s:if>
													
												</td>
			 								</tr> 
										</s:iterator>
									</tbody>
								</table>
							</div>
						</div>
		</div> 
			
		  </div>
		</div>
 
 
 </div>
<script type="text/javascript" src='/CevaBase/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='/CevaBase/js/datatable-add-scr.min.js'></script> 
 
</body>
</html>