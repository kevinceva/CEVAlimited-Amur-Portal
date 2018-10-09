
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%
	String ctxstr = request.getContextPath();
%>
<%
	String appName = session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString();
%>
<%@taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">

session_id = "<%=session.getAttribute("session_refno").toString()%>";
	//console.log("session id is :: " + session_id);

	function unique(list) {
		var result = [];
		$.each(list, function(i, e) {
			if ($.inArray(e, result) == -1)
				result.push(e);
		});
		return result;
	}

	function checkExists(sel) {
		var status = false;
		if ($(sel).length)
			status = true;
		return status;
	}
</script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#accordian .main-menu-item").click(function() {
			//slide up all the link lists
			$("#accordian ul ul").slideUp();
			//slide down the link list below the h3 clicked - only if its closed
			if (!$(this).next().is(":visible")) {
				$(this).next().slideDown();
			}
		})
	});
</script>
<div id="accordian">
	<ul>
		<s:set value="#session['MENU_DATA']" var="menuData" />
		<s:iterator value="#menuData['menudata']" var="innermenu"
			status="status">
			<s:if test="%{#innermenu['hasChild']==1 && #innermenu['isChild']==0}">
				<li id='TAB-<s:property value="#innermenu['id']" />'>
					<div class="main-menu-item">
						<img
							src="${pageContext.request.contextPath}/images/<s:property value="#innermenu['title']" />.png"
							alt="" id="menu-icon" /> <span class="hidden-tablet"><s:property
								value="#innermenu['menuName']" /> <s:property value="#" /></span>
					</div>
					<ul>
						<s:iterator value="#menuData['menudata']" var="childmenu"
							status="status2">
							<s:if test="%{#innermenu['id']==#childmenu['parentMenu']}">
								<li><a
									href="<%=ctxstr%>/<s:property value="#childmenu['applName']" />/<s:property value="#childmenu['menuaction']" />.action?pid=<%=session.getAttribute("session_refno").toString()%><s:property value="#childmenu['id']" />"><s:property
											value="#childmenu['menuName']" /></a></li>
							</s:if>
						</s:iterator>
					</ul>
				</li>
			</s:if>
			<s:elseif
				test="%{#innermenu['hasChild']==0 && #innermenu['isChild']==0}">

				<li id='TAB-<s:property value="#innermenu['id']" />'>

					<div class="main-menu-item">
						<a
							href='<%=ctxstr%>/<s:property value="#innermenu['applName']" />/<s:property value="#innermenu['menuaction']" />.action?pid=<%=session.getAttribute("session_refno").toString()%><s:property value="#innermenu['id']" />'
							class="ajax-link"> <img
							src="${pageContext.request.contextPath}/images/<s:property value="#innermenu['title']" />.png"
							alt="" id="menu-icon" /> <span class="hidden-tablet"><s:property
									value="#innermenu['menuName']" /></span>
						</a>
					</div>
				</li>
			</s:elseif>
		</s:iterator>

	</ul>
</div>

