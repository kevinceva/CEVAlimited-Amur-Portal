package com.ceva.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class CevaParamUtils { 

	public Hashtable<String,String> ProcessCTDS(String srcData) {
		String FieldName = "";
		String FieldVal = "";

		Hashtable<String,String> hsProcess = null;

		int len = 0;
		try {			
			hsProcess = new Hashtable<String,String>();

			while (true) {
				if (srcData.equals(""))
					break;
				FieldName = srcData.substring(0, 6);
				len = Integer.parseInt(srcData.substring(6, 9));
				FieldVal = srcData.substring(9, 9 + len);
				hsProcess.put(FieldName, FieldVal);
				srcData = srcData.substring(9 + len);
			}
			return hsProcess;
		} catch (Exception e) {
			System.out.println("ERROR : [ProcessCTDS] in DBUtils ["
					+ e.getMessage() + "]");
			return null;
		}
	}

	/*
	 * Function : SetField Parameters : CTDS String and FieldToUpdate Return Val
	 * : New CTDS String
	 */
	public String SetField(String CTDSStr, String DataFldNo2Upt) {
		int start = 0 ;
		int len = 0;

		String FieldNo = "";
		String FieldNoUpts = "";
		String dummyStr = "";

		if (DataFldNo2Upt.equals("")){	return CTDSStr; }
		dummyStr = DataFldNo2Upt;
		try {
			while (true) {
				start = 0;
				len = 0;
				DataFldNo2Upt = dummyStr
						.substring(0, 6 + 3 + Integer.parseInt(dummyStr
								.substring(start + 6, start + 6 + 3)));
				if (DataFldNo2Upt.length() != 6 + 3 + Integer
						.parseInt(DataFldNo2Upt.substring(start + 6,
								start + 6 + 3)))
					return CTDSStr;
				FieldNoUpts = DataFldNo2Upt.substring(start, start + 6);
				while (true) {
					if (start == CTDSStr.length())
						break;
					FieldNo = CTDSStr.substring(start, start + 6);
					if (FieldNo.equals(""))
						break;
					len = Integer.parseInt(CTDSStr.substring(start + 6,
							start + 6 + 3));
					if (FieldNo.equals(FieldNoUpts)) {
						CTDSStr = CTDSStr.substring(0, start) + DataFldNo2Upt
								+ CTDSStr.substring(start + 6 + 3 + len);
						break;
					}
					start += 6 + 3 + len;					
				}
				dummyStr = dummyStr.substring(6 + 3 + Integer.parseInt(dummyStr
						.substring(6, 8)));
				if (dummyStr.equals(""))
					break;
			}
			return CTDSStr;
		} catch (Exception e) {
			System.out.println("ERROR : [SetField] in DBUtils ["
					+ e.getMessage() + "]");
			return null;
		}
	}

	/*
	 * Function : DelField Parameters : CTDS String and DataFieldNo Return Val :
	 * New CTDS String
	 */
	public String DelField(String CTDSStr, String DataFldNo) {
		int start = 0; 
		int len = 0;
		
		String FieldNo = "";

		try {
			while (true) {
				if (start == CTDSStr.length())
					return CTDSStr;

				FieldNo = CTDSStr.substring(start, start + 6);
				if (FieldNo.equals(""))
					return null;
				len = Integer.parseInt(CTDSStr.substring(start + 6,
						start + 6 + 3));
				if (FieldNo.equals(DataFldNo)) {
					return CTDSStr.substring(0, start)
							+ CTDSStr.substring(start + 6 + 3 + len);
				}
				start += 6 + 3 + len;
			}
		} catch (Exception e) {
			System.out.println("ERROR : [DelField] in DBUtils ["
					+ e.getMessage() + "]");
			return null;
		}
	}

	public String AddField(String CTDSStr, String DataFldToAdd) {
		String FieldNo = "";
		String Length = "";
		String Value = "";
		String valueLen = "";
		
		try {		

			FieldNo = DataFldToAdd.substring(0, 6);
			if (FieldNo.equals(""))
				return null;
			Value = DataFldToAdd.substring(6);
			valueLen = String.valueOf(Value.length());		
			
			if (valueLen.length() >= 0)
				Length = "000";
			if (valueLen.length() >= 1)
				Length = "00" + valueLen;
			if (valueLen.length() >= 2)
				Length = "0" + valueLen;
			if (valueLen.length() >= 3)
				Length = valueLen;

			return CTDSStr + FieldNo + Length + Value;
		} catch (Exception e) {
			System.out.println("ERROR : [AddField] in DBUtils ["
					+ e.getMessage() + "]");
			return null;
		}
	}

	/* Prepares CTDS String from Hashtable<String,String> */
	public Vector<String> PrepareCTDS(Hashtable<String,String> hs) {
		String DataFldNo = "";				
		String Length = ""; 
		String Value = ""; 
		String valueLen = "";
		String SingleRowData = ""; 
		String KeyStr = ""; 

		Enumeration<String> enum_var = null;
		Hashtable<String,String> temphs = null;		
		Vector<String> MultipleRowData = null;
		Vector<String> RetVec = null;

		int MultipleRowCount = 1;

		try {

			MultipleRowData = new Vector<String>();
			RetVec = new Vector<String>();
			enum_var = hs.keys();

			/* Prepare CTDS String for Multiple and Single Rows Individually */

			while (enum_var.hasMoreElements()) {

				KeyStr =  enum_var.nextElement();
				if (KeyStr.startsWith("ROW_")) {
					temphs = ProcessDelim( hs.get("ROW_"
							+ String.valueOf(MultipleRowCount)));
					MultipleRowData.addElement(makeCTDS(temphs));
					MultipleRowCount++;
				} else {
					DataFldNo = KeyStr;
					Value =  hs.get(DataFldNo);
					valueLen = String.valueOf(Value.length()); 
					
					if (valueLen.length() >= 1)
						Length = "00" + valueLen;
					if (valueLen.length() >= 2)
						Length = "0" + valueLen;
					if (valueLen.length() >= 3)
						Length = valueLen;
					SingleRowData += DataFldNo + Length + Value;
				}
			}

			/* Concatenate CTDS String for Multiple and Single Rows */
			if (MultipleRowData.size() > 0)
				for (int i = 0; i < MultipleRowData.size(); i++)
					RetVec.addElement(SingleRowData
							+  MultipleRowData.elementAt(i));
			else
				RetVec.addElement(SingleRowData);

			return RetVec;
		} catch (Exception e) {
			System.out.println("ERROR : [PrepareCTDS] in DBUtils ["
					+ e.getMessage() + "]");
			return null;
		}
	}

	public String SingleCTDS(Hashtable<String,String> hs) {
		String DataFldNo = "";
		String Length = "";
		String Value = "";
		String SingleRowData = "";
		String KeyStr = "";
		String valueLen = "";
		
		Enumeration<String> enum_var = null;
		
		try {
			enum_var = hs.keys();

			while (enum_var.hasMoreElements()) {
				KeyStr =  enum_var.nextElement();
				if (KeyStr.startsWith("ROW_"))
					continue;
				else {
					DataFldNo = KeyStr;
					Value =  hs.get(DataFldNo);
					valueLen = String.valueOf(Value.length());
					
					if (valueLen.length() >= 1)
						Length = "00" + valueLen;
					if (valueLen.length() >= 2)
						Length = "0" + valueLen;
					if (valueLen.length() >= 3)
						Length = valueLen;
					SingleRowData += DataFldNo + Length + Value;
				}
			}
			System.out.println("SINGLE CTDS RES [" + SingleRowData + "]");
			
			return SingleRowData;
		} catch (Exception e) {
			System.out.println("ERROR : [SingleCTDS] in DBUtils ["
					+ e.getMessage() + "]");
			return null;
		}
	}

	public String makeCTDS(Hashtable<String,String> hs) {
		String CTDSStr = "";
		String DataFldNo = "";
		String Length = "";
		String Value = "";
		String valueLen = "";
		Enumeration<String> enum_var = null;
		

		try {
			enum_var = hs.keys();
			while (enum_var.hasMoreElements()) {
				DataFldNo =  enum_var.nextElement();
				Value =  hs.get(DataFldNo);
				valueLen = String.valueOf(Value.length());
				
				if (valueLen.length() >= 1)
					Length = "00" + valueLen;
				if (valueLen.length() >= 2)
					Length = "0" + valueLen;
				if (valueLen.length() >= 3)
					Length = valueLen;
				CTDSStr += DataFldNo + Length + Value;
				Length = "";
			}

			return CTDSStr;
		}

		catch (Exception e) {
			System.out.println("ERROR : [makeCTDS] in DBUtils ["
					+ e.getMessage() + "]");
			return null;
		}
	}

	/* Convert Delim(|=) to Hashtable<String,String> */
	public Hashtable<String,String> ProcessDelim(String srcData) {
		String data = "";	
		
		Hashtable<String,String> hs = null;
		int pos = 0;
		int start = 0;
		
		try {
			 hs = new Hashtable<String,String>();
			 
			while (true) {
				if (srcData.equals(""))
					break;
				start = srcData.indexOf('|');
				if (start == -1)
					break;
				data = srcData.substring(0, start);
				pos = data.indexOf("~~");
				hs.put(data.substring(0, pos),
						data.substring(pos + 2, data.length()));
				srcData = srcData.substring(start + 1, srcData.length());
			}
			return hs;
		} catch (Exception e) {
			System.out.println("ERROR : [ProcessDelim] in DBUtils ["
					+ e.getMessage() + "]");
			return null;
		}
	}

}
