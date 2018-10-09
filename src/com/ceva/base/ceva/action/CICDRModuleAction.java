package com.ceva.base.ceva.action;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.struts2.ServletActionContext;

import util.StringUtil;

import com.ceva.base.common.dao.CICAgencyDAO;
import com.ceva.base.common.dao.CICDRModuleDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.ceva.base.common.bean.TwoWaySMSBean;
import com.ceva.util.Validation;

public class CICDRModuleAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(CICDRModuleAction.class);
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject finalerrrespjson=new JSONObject();
	
	private HttpSession session = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	protected HttpServletRequest request;
	
	public String merchantId;
	public String storeId;
	public String srchval;
	
	private String cid = null;
	private String cname = null;
	private String gender = null;
	private String dob = null;
	private String idtype = null;
	private String idno = null;
	private String mob = null;
	private String addr = null;
	private String pcode = null;
	private String email = null;
	private String cdate = null;
	private String status = null;
	private String refmob = null;
	private String comp = null;
	
	
	private String bid = null;
	private String bname = null;
	private String bgender = null;
	private String bdob = null;
	private String bidno = null;
	private String bmob = null;
	private String bcdate = null;
	private String bstatus = null;
	private String photo = null;
	private String vcnt = null;
	private String bsign = null;
	private String cases = null;
	private String remarks = null;
	
	private String fromDate = null;
	private String toDate = null;
	
	private String refno = null;
	private String rsddt = null;
	private String rsdby = null;
	private String compid = null;
	private String rid = null;
	private String newmob = null;
	private String amt = null;
	
	private String rfrom = null;
	private String rdate = null;
	private String rtxt = null;
	private String rto = null;
	
	private String bonus = null;
	private String famt = null;
	
	private String tdate= null;
	
	private File uploadFile;
	private String uploadFileFileName;	
	private String fileName;
	
/*	public String getMerchantDashBoard(){
		return "success";
	}*/
	
	public String CommonScreen() {
		logger.debug("Inside CommonScreen...");
		result = "success";
		return result;
	}	
	
	public String drTopUpConf(){
		
		logger.debug("inside [CICDRModuleAction][forceTopUpConf].. ");
		CICDRModuleDAO cicdrmodDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();			

			requestJSON.put("mob", getMob());
			requestJSON.put("amt", getAmt());
			requestJSON.put("bonus", getBonus());
			requestJSON.put("famt", getBonus());
			requestJSON.put("remarks", getRemarks());
			requestJSON.put("txndt", getTdate());
			requestJSON.put("refno", getRefno());
			requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));	
		
			
			System.out.println("requestJSON:"+requestJSON.toString());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO.toString() + "]");
			cicdrmodDAO = new CICDRModuleDAO();
			responseDTO = cicdrmodDAO.drForceTopUp(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
	
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				System.out.println("in hte action else");
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [CICDRModuleAction][forceTopUpConf] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
	
			errors = null;
			cicdrmodDAO = null;
		}
	
		return result;
	
	}	
	
	public String execute()
	{
		{		

			
			logger.debug("##############################  DR TopUp Upload Sart ##################");

			boolean finalStatus=true;
			int exlrownum =0;
			String userId="";
			String fileName="";
			String result="";
			
			JSONArray finalData=new JSONArray();
			JSONObject reqJSON=new JSONObject();
			CICAgencyDAO cicagencyDAO = null;

			try{

				HttpSession session=ServletActionContext.getRequest().getSession(); 
				userId= (String) session.getAttribute(CevaCommonConstants.MAKER_ID);

				String	pathx =  session.getServletContext().getRealPath("/Cards");	
				
				logger.debug("pathx value["+pathx+"]");

				File fileToCreate = new File(pathx, uploadFileFileName);
				FileUtils.copyFile(uploadFile, fileToCreate);
				fileName = uploadFileFileName; 

				String entitycode = (String) session.getAttribute("entitycode");

				logger.info("uploadFileFileName ["+uploadFileFileName+"] Path ["+pathx+"] entitycode ["+entitycode+"]");
				
				//Variable Declaration Started
			
				JSONArray errorresarayJSON=new JSONArray();

				String mobNo="";
				String amount="";
				String bonus="";
				String refno="";

				CICDRModuleDAO finalobj = new CICDRModuleDAO();
				
				
				logger.info("filename ["+fileName+"] ");
			
			

				if (!fileName.isEmpty()) {

					File empdepndexcel = new File(pathx + "/" + fileName);
					FileInputStream inputStream1 = new FileInputStream(empdepndexcel);
					POIFSFileSystem fileSystem1 = new POIFSFileSystem(inputStream1);
					HSSFWorkbook wb1 = new HSSFWorkbook(fileSystem1);
					List<String> refnos = new ArrayList<String>();

					HSSFSheet hospsheet = wb1.getSheetAt(0);
					java.util.Iterator rowIterator1 = hospsheet.rowIterator();

					while (rowIterator1.hasNext()) {

						HSSFRow row = (HSSFRow) rowIterator1.next();
						

						logger.debug("Row Number ["+ row.getRowNum()+"]");

						if(row.getRowNum()==0)
						{
							logger.error("Error File");

						}else{ 
							boolean finalstatus=isBlankRow(row, 35);

							logger.info("Blank Status ["+finalstatus+"]");
							
							if(finalstatus)
							{
								break;

							}else{
								
								exlrownum = row.getRowNum()+1;								
								
								mobNo=getCellData(row, 0,"STR");
								amount=getCellData(row, 1,"STR");
								bonus=getCellData(row, 2,"STR");
								refno=getCellData(row, 3,"STR");
								
								if(!refnos.contains(refno)){
									refnos.add(refno);
								}
								else{
									finalStatus=false;
									logger.info("duplicate refno no found..:"+refno);
									errorresarayJSON = getJsonObject("Duplicate M-Pesa Reference Number Found :"+refno, exlrownum, errorresarayJSON);
								}							
								

							logger.info("Row Data [mobNo=" + mobNo + ", amount=" + amount + ", bonus="+bonus+", refno="+refno+"]");
							
							if(StringUtil.isNullOrEmpty(mobNo)){						
									finalStatus=false;
									remarks="Please Enter the mobile number";
									errorresarayJSON= getJsonObject(remarks, exlrownum, errorresarayJSON);							
							}
							
							if(StringUtil.isNullOrEmpty(amount)){						
									finalStatus=false;
									remarks="Please Enter the amount";
									errorresarayJSON= getJsonObject(remarks, exlrownum, errorresarayJSON);							
							}
							
							if(StringUtil.isNullOrEmpty(refno)){						
								finalStatus=false;
								remarks="Please Enter the M-Pesa Reference Number";
								errorresarayJSON= getJsonObject(remarks, exlrownum, errorresarayJSON);							
							}							

							reqJSON.put("mobNo", mobNo);
							reqJSON.put("amount", amount);
							reqJSON.put("bonus", bonus);
							reqJSON.put("refno", refno);
							
							
							
							Boolean valid = finalobj.refnovalid(refno);
							if(!valid)
							{
								
								finalStatus=false;
								remarks="Request Already Raised For This Reference Number.";
								errorresarayJSON= getJsonObject(remarks, exlrownum, errorresarayJSON);
								
							}
							

							finalData.add(reqJSON);
						}

						}
					}
					
					//Functional Validations Start
					logger.debug("Functional Validation Start");

					logger.debug("Final Validation Status ["+finalStatus+"]");

					if(finalStatus)
					{

						//EndorsementDAO finalobj = new EndorsementDAO();
						logger.debug("There is no Validation Errors........."+finalData.toString());
						
						String mapStatus =finalobj.initiateDRTopUp(finalData,userId,fileName); //Need To do Chnage
						logger.debug("After Saved All Excel topup Data into [DR Topup] Table mapStatus["+mapStatus+"]");
						if (null != mapStatus && "SUCCESS".equalsIgnoreCase(mapStatus))
						{	
							finalerrrespjson.put("STATUS","S");
							finalerrrespjson.put("NOOfRows",exlrownum-1);
							//finalerrrespjson.put("PremiumDetails",premiumDet);
							logger.info("DR TopUp Upload Action Final Response Data ["+finalerrrespjson+"]");

							result = ActionSupport.SUCCESS;
							return result;
						}						
						else
						{
							JSONObject errorresJSON=new JSONObject();
							errorresJSON.put("uploadErrors", "DR TopUp Upload FAILED DUE TO INTERNAL ERROR");
							//errorresJSON.put("excelRowNo", errorData[0]);
							errorresarayJSON.add(errorresJSON);
							finalerrrespjson.put("FINALJSON", errorresarayJSON);
							finalerrrespjson.put("STATUS","F");
							logger.debug(finalerrrespjson);
							result = ActionSupport.SUCCESS;
						}

					}else
					{
						logger.debug("Validation Error Happen ["+errorresarayJSON+"]");
						finalerrrespjson.put("FINALJSON", errorresarayJSON);
						finalerrrespjson.put("STATUS","F");
						logger.debug(finalerrrespjson);
						result = ActionSupport.SUCCESS;
					}

					//finalobj.membarendsmentProcess(finalData);

					logger.debug("############################## DR TopUp  File Upload End ##################");

				}
				

			}catch(Exception e)
			{	

				finalerrrespjson.put("STATUS","E");
				e.printStackTrace();
				addActionError(e.getMessage());
				//Need To write Code Here for failure
				return ActionSupport.SUCCESS;


			}
			finally
			{
				finalStatus=true;

			}
			return result;
		}
	}



	public  String getCellData(HSSFRow row , int celno,String type)
	{

		String str = "";
		try
		{

			if (row.getCell((short) celno) == null) {
				str = "";
			} else {       

				if(row.getCell((short) celno).getCellType()==Cell.CELL_TYPE_NUMERIC){
					//long m1 = (long)row.getCell((short) celno).getNumericCellValue();
					
					 HSSFCell cell =  row.getCell((short) celno);
					
					if(type.equals("DATE"))
					{
						 
						str=(String)getDateValue(cell);
						
					}else
					{
						long m1 = (long)cell.getNumericCellValue();
						str=Long.toString(m1);
					}
				}
				else if(row.getCell((short) celno).getCellType()==Cell.CELL_TYPE_STRING){
					//str = row.getCell((short) celno).getStringCellValue();

					str = row.getCell((short) celno).getStringCellValue();

				}  else	if(row.getCell((short) celno).getCellType()==Cell.CELL_TYPE_STRING){
						str = row.getCell((short) celno).getBooleanCellValue()+"";

					}else
						{
							str="";
						}

			}

		}catch(Exception e)
		{
			logger.error("Exception Raised ["+e.getMessage()+"]");
			e.printStackTrace();
		}

		return str;

	}	

	 public static Object getDateValue(HSSFCell cell) 
	  {
	      String finalDate="";
	      double numericValue = cell.getNumericCellValue();
	      java.util.Date date = HSSFDateUtil.getJavaDate(numericValue);
	      // Add the timezone offset again because it was subtracted automatically by Apache-POI (we need UTC)
	      long tzOffset = TimeZone.getDefault().getOffset(date.getTime());
	      date = new Date(date.getTime() + tzOffset);
	      finalDate=Validation.convertDateToString(date, "dd/MM/YYYY");
	      
	      return finalDate;
	    
	  }
	 
		public static boolean isBlankRow(HSSFRow row,int collength)
		{

			boolean status = false;
			int finalcount=0;

			try
			{
				if(row!=null){


					for(int celno=0;celno<collength;celno++)
					{

						
						if(row.getCell((short) celno)==null)
						{
							++finalcount;
						}else
						if(row.getCell((short) celno).getCellType()==Cell.CELL_TYPE_BLANK){
							++finalcount;
						}else
						{
							
						}
					}

					//logger.info("finalcount ["+finalcount+"] collength ["+collength+"]");
					
					
					if(finalcount==collength)
						status=true;

				}else
				{
					 status=true;
				}
				
			}catch(Exception e)
			{
				e.printStackTrace();
				status=true;
			}

			return status;

		}	
		
		public static JSONArray getJsonObject(String remarks,int exlrownum , JSONArray errorresarayJSON)
		{

			JSONObject errorresJSON=new JSONObject();
			errorresJSON.put("uploadErrors", remarks);
			errorresJSON.put("excelRowNo", ""+exlrownum);

			errorresarayJSON.add(errorresJSON);

			return errorresarayJSON;

		}
		
		public static JSONArray buildErrorJsonObj(ArrayList<String> errorList,JSONArray errorresarayJSON)
		{
			for (String string : errorList) {

				String []errorData = string.split("-");

				if(errorData.length==2)
				{
					System.out.println("Error Data : "+errorData[0]+"   "+errorData[1]);
					JSONObject errorresJSON=new JSONObject();
					errorresJSON.put("uploadErrors", errorData[1]);
					errorresJSON.put("excelRowNo", errorData[0]);
					errorresarayJSON.add(errorresJSON);
				}
			}

			return errorresarayJSON;
		}	
		
	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}


	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getSrchval() {
		return srchval;
	}

	public void setSrchval(String srchval) {
		this.srchval = srchval;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getMob() {
		return mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public String getBgender() {
		return bgender;
	}

	public void setBgender(String bgender) {
		this.bgender = bgender;
	}

	public String getBdob() {
		return bdob;
	}

	public void setBdob(String bdob) {
		this.bdob = bdob;
	}

	public String getBidno() {
		return bidno;
	}

	public void setBidno(String bidno) {
		this.bidno = bidno;
	}

	public String getBmob() {
		return bmob;
	}

	public void setBmob(String bmob) {
		this.bmob = bmob;
	}

	public String getBcdate() {
		return bcdate;
	}

	public void setBcdate(String bcdate) {
		this.bcdate = bcdate;
	}

	public String getBstatus() {
		return bstatus;
	}

	public void setBstatus(String bstatus) {
		this.bstatus = bstatus;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getVcnt() {
		return vcnt;
	}

	public void setVcnt(String vcnt) {
		this.vcnt = vcnt;
	}

	public String getBsign() {
		return bsign;
	}

	public void setBsign(String bsign) {
		this.bsign = bsign;
	}

	public String getCases() {
		return cases;
	}

	public void setCases(String cases) {
		this.cases = cases;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getRefno() {
		return refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

	public String getRsddt() {
		return rsddt;
	}

	public void setRsddt(String rsddt) {
		this.rsddt = rsddt;
	}

	public String getRsdby() {
		return rsdby;
	}

	public void setRsdby(String rsdby) {
		this.rsdby = rsdby;
	}

	public String getCompid() {
		return compid;
	}

	public void setCompid(String compid) {
		this.compid = compid;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getNewmob() {
		return newmob;
	}

	public void setNewmob(String newmob) {
		this.newmob = newmob;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getRfrom() {
		return rfrom;
	}
	public void setRfrom(String rfrom) {
		this.rfrom = rfrom;
	}
	public String getRdate() {
		return rdate;
	}
	public void setRdate(String rdate) {
		this.rdate = rdate;
	}
	public String getRtxt() {
		return rtxt;
	}
	public void setRtxt(String rtxt) {
		this.rtxt = rtxt;
	}
	public String getRto() {
		return rto;
	}
	public void setRto(String rto) {
		this.rto = rto;
	}
	public String getRefmob() {
		return refmob;
	}
	public void setRefmob(String refmob) {
		this.refmob = refmob;
	}
	public String getComp() {
		return comp;
	}
	public void setComp(String comp) {
		this.comp = comp;
	}
	public JSONObject getFinalerrrespjson() {
		return finalerrrespjson;
	}
	public void setFinalerrrespjson(JSONObject finalerrrespjson) {
		this.finalerrrespjson = finalerrrespjson;
	}
	public File getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	public String getUploadFileFileName() {
		return uploadFileFileName;
	}
	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	public String getFamt() {
		return famt;
	}

	public void setFamt(String famt) {
		this.famt = famt;
	}

	public String getTdate() {
		return tdate;
	}

	public void setTdate(String tdate) {
		this.tdate = tdate;
	}

	
}
