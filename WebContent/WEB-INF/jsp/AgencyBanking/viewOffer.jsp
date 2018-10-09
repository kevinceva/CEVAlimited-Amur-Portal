
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the Big Chief">
<meta name="author" content="Tony Kithaka">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 
<SCRIPT type="text/javascript"> 
var toDisp = '${type}';

$(document).ready(function(){
            
        $('#btn-submit').on('click',function() {  
            $("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/specialOffers.action";
            $("#form1").submit();                   
        }); 

        
});
    //--> 
</SCRIPT>
    
  
        
</head>

<body>

    <div class="page-header">
        <div>
            <img class="header-icon" alt="Dashboard Icon" src="${pageContext.request.contextPath}/images/editbutton.png">
            <label>Offer Information</label>            
        </div>  
    </div>
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item">
          <a href="specialOffers.action">Travel Offers</a>
        </li>
        <li class="breadcrumb-item active">
          <a href="#">Offer Details</a>       
        </li>
    </ol>
    
    <form name="form1" id="form1" method="post"> 
        
        <div class="box-content content-panel">
            <fieldset> 
                <table width="950"  border="0" cellpadding="5" cellspacing="1" 
                    class="table table-striped table-bordered bootstrap-datatable " >
                        <tr class="even" > 
                            <td width="25%" ><strong><label for="Product Name">Deal Name</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.dealName}  <input type="hidden" name="prdname"  id="prdname" value="${responseJSON.TRAVEL_OFFER_INFO.dealName}" /> </td> 
                            <td width="25%" ><strong><label for="Category Name">Operator Name</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.operatorName} <input type="hidden" name="catname"  id="catname" value="${responseJSON.TRAVEL_OFFER_INFO.operatorName}" /></td>
                        </tr>
                        <tr class="odd">
                            <td><strong><label for="Sub Category Name">Start Date</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.startDate} <input type="hidden" name="subcatname"  id="subcatname" value="${responseJSON.TRAVEL_OFFER_INFO.startDate}" /></td>
                            <td><strong><label for="Manufacturer Name">End Date</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.endDate} <input type="hidden" name="manfname"  id="manfname" value="${responseJSON.TRAVEL_OFFER_INFO.endDate}" /></td>
                        </tr> 
                        <tr class="even" > 
                            <td width="25%" ><strong><label for="Product Name">Country</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.dealCountry}  <input type="hidden" name="prdname"  id="prdname" value="${responseJSON.TRAVEL_OFFER_INFO.dealCountry}" /> </td> 
                            <td width="25%" ><strong><label for="Category Name">Offer Type</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.offerType} <input type="hidden" name="catname"  id="catname" value="${responseJSON.TRAVEL_OFFER_INFO.offerType}" /></td>
                        </tr>
                        <tr class="odd">
                            <td><strong><label for="Sub Category Name">Price</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.amount} <input type="hidden" name="subcatname"  id="subcatname" value="${responseJSON.TRAVEL_OFFER_INFO.amount}" /></td>
                            <td><strong><label for="Manufacturer Name">Available Bookings</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.maxBookings} <input type="hidden" name="manfname"  id="manfname" value="${responseJSON.TRAVEL_OFFER_INFO.maxBookings}" /></td>
                        </tr> 
                        <tr class="even" > 
                            <td width="25%" ><strong><label for="Product Name">Status</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.status}<input type="hidden" name="prdname"  id="prdname" value="${responseJSON.TRAVEL_OFFER_INFO.status}" /> </td> 
                            <td width="25%" ><strong><label for="Category Name">Created By</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.createdBy} <input type="hidden" name="catname"  id="catname" value="${responseJSON.TRAVEL_OFFER_INFO.createdBy}" /></td>
                        </tr>
                        <tr class="odd">
                            <td><strong><label for="Sub Category Name">Approved By</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.approvedBy} <input type="hidden" name="subcatname"  id="subcatname" value="${responseJSON.TRAVEL_OFFER_INFO.approvedBy}" /></td>
                            <td><strong><label for="Manufacturer Name">Approved Date</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.approvedDate} <input type="hidden" name="manfname"  id="manfname" value="${responseJSON.TRAVEL_OFFER_INFO.approvedDate}" /></td>
                        </tr>
                        <tr class="even">
                            <td><strong><label for="Sub Category Name">Travel Itinerary</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.travelItinerary} <input type="hidden" name="subcatname"  id="subcatname" value="${responseJSON.TRAVEL_OFFER_INFO.travelItinerary}" /></td>
                            <td><strong><label for="Manufacturer Name">Important Information</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.importantInfo} <input type="hidden" name="manfname"  id="manfname" value="${responseJSON.TRAVEL_OFFER_INFO.importantInfo}" /></td>
                        </tr> 
                        <tr class="even">
                            <td><strong><label for="Sub Category Name">Terms & Conditions</label></strong></td>
                            <td width="25%" >${responseJSON.TRAVEL_OFFER_INFO.terms} <input type="hidden" name="subcatname"  id="subcatname" value="${responseJSON.TRAVEL_OFFER_INFO.terms}" /></td>
                            <td></td>
                            <td width="25%" ></td>
                        </tr> 
                        

                </table>
            </fieldset>              
        
        </div> 
        
        
        <div class="form-actions">
           <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Next"   ></input>
          </div> 
                                     
    
          
 </form>
</body>
</html>

