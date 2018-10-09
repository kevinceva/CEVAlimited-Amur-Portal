<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%String ctxstr = request.getContextPath();%>
<%String appName = session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString();%>

<script type="text/javascript">

var validationRules = {
		rules : {
			startdate : { required : true },
            enddate : { required : true },
            txnType : { required : true }
		},
		messages : {
			startdate : {
				required : "Please enter Start Date."
			},
			enddate : {
				required : "Please enter End Date."
			}
              
		}
}

$(document).ready(function(){
		
	var submitPath = "<%=request.getContextPath()%>/<%=appName%>/viewTransaction.action";
		
	$('#btn-cancel').on('click',function() {
		document.getElementById("txn-form").reset();
		var table = $('#txnTable').DataTable();
		table.clear().draw();	
	});	
		
	$('#btn-filter').on('click',function() {
		$('#txn-form').validate(validationRules);
		if($('#txn-form').valid()){
				
			var txnType = document.getElementById("txnType").value;
			var startDate = document.getElementById("startdate").value;
			var endDate = document.getElementById("enddate").value;
															
			var queryString = "txnType="+txnType;
				queryString += '&startdate='+startDate;
				queryString += '&enddate='+endDate;
				
			console.log(queryString);
			var table = $('#txnTable').DataTable();
			table.clear().draw();		
			
			$.getJSON(submitPath, queryString,function(json){
					
				txnData = json.responseJSON.TXNS; 
				var val = 1;
				var rowindex = 0;
				var colindex = 1;
				var addclass = "";
				
				
				$.each(txnData, function(i, v) {
			    	if(val % 2 == 0 ) {
						addclass = "even";
						val++;
					}
					else {
						addclass = "odd";
						val++;
					}  
			    	
			    	var i= table.row.add( [
						v.ORDER_ID,
						v.CUSTOMER_NAME,
						v.TXN_AMOUNT,
						v.PAYMENT_MODE,
						v.CHANNEL,
						v.TXN_DATE
			        ] ).draw( false );
			    	table.rows(i).nodes().to$().attr("id", rowindex);
			    	table.rows(i).nodes().to$().attr("index", rowindex);
			 
			        rowindex = ++rowindex;
			        colindex = ++colindex;			    	
			    	
			    });
								    				    
				});
				
			}			
		});	
		
	});
	$(document).ready(function(){
		$('#txnTable').DataTable();
	});
	
</script>

</head>
<body>
	<div class="page-header">
		<div>
			<label>Transactions</label>
		</div>
	</div>
	<ol class="breadcrumb">
		<li class="breadcrumb-item">
			<a href="home.action">Dashboard</a>
		</li>
		<li class="breadcrumb-item">
			<a href="transactionAction.action">Transactions</a>
		</li>		
	</ol>
	
	<div class="content-panel">
		<div class="panel-head">
			<label><i class="fa fa-exchange"></i>Filter Transactions</label>
		</div>
    	<div class="container">
	    	<div class="row">
		    	<form action="" method="post" class="amur_forms" name="txn-form" id="txn-form">
		    		<div class="col-sm-4 col-xs-6">		    		    		
		    			<div class="row">		    			    				
							<div class="form-group">
								<label for="txnType">Select transactions type : </label>
								<select class="form-control" id="txnType" name="txnType">
									<option value="" disabled selected>Select your option</option>
									<option value="4">Airtime</option>
									<option value="1">Me 2 Shop</option>
								</select>
							</div>												
		    			</div>	    			  		
		    		</div>
		    		
		    		<div class="col-sm-4 col-xs-6">	
		    			 <div class="row">		    			    				
							<div class="form-group">
								<label for="startDate">Enter Start Date : </label>
								<input type="text" name="startdate" class="form-control" id="startdate" ondrop="return false;" onpaste="return false;" placeholder="Enter Start Date"></input>
							</div>												
		    			</div>    
		    		</div>
		    		
		    		<div class="col-sm-4 col-xs-6">	
		    			 <div class="row">		    			    				
							<div class="form-group">
								<label for="endDate">Enter End Date : </label>
								<input type="text" name="enddate" class="form-control" id="enddate" ondrop="return false;" onpaste="return false;" placeholder="Enter End Date"></input>
							</div>												
		    			</div> 
		    		</div>
		    		
		    		<div class="col-sm-4 col-xs-6">
			    		<div class="row">
			    			<input type="button" id="btn-filter" class="btn activate" value="Show Transactions"/>
			    			<input type="button" id="btn-cancel" class="btn activate" value="Clear"/>
			   			</div>
			    	</div>
		    	</form>
		    	
	    	</div>
	    </div>
	</div>
	
	<div class="content-panel">
    	<div class="container">
    		<div class="panel-head">
				<label><i class="fa fa-tags"></i>Transactions</label>
			</div>
			<table class="table stripe datatable" id="txnTable">
				<thead>
					<tr>
						<!--th>#</th-->
						<th>Order ID</th>
						<th>Customer Name</th>
						<th>Amount</th>
						<th>Payment Mode</th>
						<th>Channel</th>
						<th>Date</th>
					</tr>
				</thead>
				<tbody id="txnTbody">
				</tbody>
			</table>
    	</div>
    </div>
			
	
</body>
