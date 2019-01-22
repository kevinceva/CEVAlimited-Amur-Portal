<%-- Sample Data
test param is:<%= request.getParameter("merchantId")%> --%>
<html>
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.css">

	<head>
		<script type="text/javascript" src="/CevaBase/js/jquery-1.12.3.min.js"></script>
		<script type="text/javascript" src="/CevaBase/js/jquery.validate.min.js"></script>
		<link href="${pageContext.request.contextPath}/css/agency-app.min.css"	rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/chosen.min.css"  	rel="stylesheet" type="text/css" media="screen"  />  

		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-dropdown.min.js"></script>  
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.validate.min.js"></script> 
		
		<script type="text/javascript">
		$(function() {
			
			var merchantId= '<%= request.getParameter("merchantId")%>';
			//alert(merchantId);
			
			var queryString = "merchantId="+merchantId;	
			$.getJSON("AgencyBanking/storeJsonAct.action", queryString,function(data) { 
				//alert(data.responseJSON.STORE_LIST);
				
				var storeData =data.responseJSON.STORE_LIST;
				//alert(storeData);
				 var json = storeData;
				//alert(json);
				var val = 1;
				var rowindex = 0;
				var colindex = 0;
				var addclass = "";
				$.each(json, function(i, v) {
					if(val % 2 == 0 ) {
						addclass = "even";
						val++;
					}
					else {
						addclass = "odd";
						val++;
					}  
					var rowCount = $('#storeTBody > tr').length;
					
					//rowindex = ++rowindex;
					colindex = ++ colindex; 
					
					var appendTxt = "<tr class="+addclass+" index='"+rowindex+"' id='"+rowindex+"' > "+
					"<td >"+colindex+"</td>"+
					"<td><a href='#' id='STORE_INFO' value='"+v.storeId+"'>"+v.storeId+"</span></a></td>"+	
					"<td>"+v.storeName+"</span> </td>"+ 
					"<td>"+v.merchantID+"</span> </td>"+ 
					"<td>"+v.status+"</span> </td>"+ 
					"<td>"+v.makerDate+"</span></td>"+
					"<td><a class='btn btn-success' href='#' id='terminal-create'  index='"+rowindex+"' title='Create Terminal' data-toggle='tooltip' ><i class='icon icon-plus icon-white'></i></a>&nbsp; <a class='btn btn-warning' href='#' id='store-modify' index='"+rowindex+"' title='Modify' data-toggle='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp; <a class='btn btn-info' href='#' id='store-view'  index='"+rowindex+"' title='View' data-toggle='tooltip'><i class='icon icon-page icon-white'></i></a>&nbsp;																								 </td>"+
					"</tr>";
						
						$("#SBody").append(appendTxt);	
						rowindex = ++rowindex;
				});		
	 
				/* var t = $('#DataTables_Table_S01').DataTable();
			    var counter = 1;
			 	alert(t);
			        t.row.add( [
			            counter +'.1',
			            counter +'.2',
			            counter +'.3',
			            counter +'.4',
			            counter +'.5',
			            counter +'.6',
			            counter +'.7'
			        ] ).draw( false );
			 
			        counter++; */
			 
			});
		});
				 

		
			
		</script>
	 <style>
         #tabs-1{font-size: 10px;}
         .ui-widget-header {
            background:#070A63;
            border: 1px solid #b9cd6d;
            color: #FFFFFF;
            font-weight: bold;
         }
         #tabs-2{font-size: 10px;}
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
	
				<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;' width='100%'   
							class='table table-striped table-bordered bootstrap-datatable datatable' 
							id='DataTables_Table_S01'>
							<thead>
								<tr>
									<th>S No</th>
									<th>Store ID</th>
									<th class='hidden-phone'>Store Name </th>
									<th class='center hidden-phone'>Merchant ID</th>
									<th>Status </th>
									<th class='hidden-phone'>Date Created</th>
									<th>Actions</th>
								</tr>
							</thead> 
							<tbody id="SBody"> 
							</tbody>
				</table>
	
	</body>
	
	<script type="text/javascript" src='/CevaBase/js/jquery.dataTables11.min.js'></script>
<script type="text/javascript" src='/CevaBase/js/datatable-add-scr.min.js'></script> 
	
</html>