import net.sf.json.JSONObject;


public class Sample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JSONObject jsno = new JSONObject();
		jsno.put("Hello", "I&M BANK LTD (2015)");
		System.out.println(jsno.getString("Hello"));
	}

}
