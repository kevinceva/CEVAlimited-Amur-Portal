import java.net.*; 
import java.io.*; 

class sendSMS 
{ 

public static void main(String args[]) 
{ 
boolean debug=true; 

String phone	=""; 
String message	=""; 
String ppgHost	="http://localhost:8800/"; 
String username ="";
String password ="";

try
{ 
phone=URLEncoder.encode("254726605242", "UTF-8"); 

if(debug) 
System.out.println("phone------>"+phone); 
message=URLEncoder.encode("TestMessageFromPOSTA", "UTF-8"); 

if(debug) 
System.out.println("message---->"+message); 
} 
catch (UnsupportedEncodingException e) 
{ 
System.out.println("Encoding not supported"); 
} 
String clientId="18";
String client_token="47b2acca70010f64bd5dcadd444cb45f04400057_180632001470907688";
String url_str="http://196.202.202.194/api/index.php/v1/send"+"?MSISDN="+phone+"&message="+message+"&client_id="+clientId+"&client_token="+client_token; 
// String url_str=ppgHost+"?user="+username+"&password="+password+"&PhoneNumber="+phone+"&Text="+message; 

if(debug) 
System.out.println("url string->"+url_str); 


try
{	
URL url2=new URL(url_str); 

HttpURLConnection connection = (HttpURLConnection) url2.openConnection(); 
connection.setDoOutput(false); 
connection.setDoInput(true); 

if(debug) 
System.out.println("Opened Con->"+connection); 

String res=connection.getResponseMessage(); 

if(debug) 
System.out.println("Get Resp Message->"+res); 

int code = connection.getResponseCode () ; 

if ( code == HttpURLConnection.HTTP_OK ) 
{ 
connection.disconnect() ; 
}
}
catch(IOException e)
{
System.out.println("unable to create new url"+e.getMessage());
}



} // main
} // class
