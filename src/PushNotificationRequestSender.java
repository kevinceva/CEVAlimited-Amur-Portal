
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import net.sf.json.JSONObject;

public class PushNotificationRequestSender {
	
	public static void sendPushNotification(JSONObject json){
		
		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
        String webServiceURL = "https://amurnotifications.azurewebsites.net/api/HttpTriggerCSharp1?code=i0sWW38yhpCloGNhMaaaC1pV1ZOe6TKLle3w6q2r9pT6TcZKjF9qIg==";
        System.out.println("Distance URL  :::: " + webServiceURL);
        String json1;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(webServiceURL);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");
			
			/*JSONObject json = new JSONObject();
			json.put("DeviceType", "Android");
			json.put("UserTag", "9030009911");
			json.put("Payload", "to send to the device");*/
			
			 HttpEntity entity = new ByteArrayEntity(json.toString().getBytes("UTF-8"));
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			String line = "";
			StringBuilder resp = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				resp.append(line);

			}
			rd.close();
			System.out.println("response String in HTTP Class:" + resp.toString());
			
		        
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		JSONObject json = new JSONObject();
		json.put("DeviceType", "ANDROID");
		json.put("UserTag", "254729455784");
		json.put("Payload", "mESSAAGE FROM TONY");
		sendPushNotification(json);
	}

}



