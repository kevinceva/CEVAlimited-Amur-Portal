import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;


public class Test {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
	
		//pid = 717e6b0ad127ebb30538d0caca98bb2f41
		
				if("9155fb272dc17e3b7d0839e4304aab1641".toString().indexOf("9155fb272dc17e3b7d0839e4304aab16")==0){

		//if("cfaa462b1e17480d17025a2b5ee7a2b748".toString().indexOf("17a6dfee7e8a3b2e6548292ff9981904")==0){
			System.out.println("true");
			
		}else {
			System.out.println("false");
		}
		
		
	}

}
