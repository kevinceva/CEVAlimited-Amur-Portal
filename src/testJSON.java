import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class testJSON {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String jsonString = "\"PRODUCTS\": [\r\n" + 
				" {\r\n" + 
				" \"prodID\": \"ORDID95098448426694\",\r\n" + 
				" \"price\": 3000,\r\n" + 
				" \"quantity\": \"2\",\r\n" + 
				" \"discount\": 0\r\n" + 
				" },\r\n" + 
				" {\r\n" + 
				" \"prodID\": \"ORDID95098448426694\",\r\n" + 
				" \"price\": 200,\r\n" + 
				" \"quantity\": \"1\",\r\n" + 
				" \"discount\": 0\r\n" + 
				" },\r\n" + 
				" {\r\n" + 
				" \"prodID\": \"ORDID95098448426694\",\r\n" + 
				" \"price\": 30000,\r\n" + 
				" \"quantity\": \"1\",\r\n" + 
				" \"discount\": 0\r\n" + 
				" }\r\n" + 
				" ]\r\n" + 
				"";
		JSONObject requestJSON = new JSONObject().fromObject(jsonString);
		JSONArray myJson = requestJSON.getJSONArray("PRODUCTS");
		
		for (int i = 0; i < myJson.size(); ++i) {
		    JSONObject rec = myJson.getJSONObject(i);
		    String product_id = rec.getString("prodID");
		    String price = rec.getString("price");
		    // ...
		    System.out.println(product_id);
		}
		
		//System.out.println(product_id);
	}

}
