package com.automic.utils;

import java.security.Key;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.uc4.util.Base64;


public class CryptoUtils {

	// Key below not used.
	//static final String key = "Un1ver$e2015!@)#"; // The key for 'encrypting' and 'decrypting'.
	
	/*public static String encode(String s) {
        return Base64.encode(s.getBytes(),true);
    }


    public static String decode(String s) {
    	if(s.subSequence(0, 2).equals("--")){
    		
    	}
        return new String(Base64.decode(s), StandardCharsets.UTF_8);
    }
    */
	
	private final static String ENCODING = "ISO-8859-1";

	private static byte[] FIX_KEY = {0x70,0x65,0x45,0x71,(byte)0xE6,0x45,0x65,0x64};

	private static byte[] PREFIX1 = { '-', '-', '1', '0' };
	private static byte[] PREFIX2 = { (byte) 0xAD, (byte) 0xAD, '1', '0' };
	
	/**
	 * Creates a new <code>ConnectStringPassword</code> object.
	 * 
	 * @param pw Password which can be encrypted
	 */
	
	public static String decode(String pw) {
		return decrypt(pw, FIX_KEY);
	}
	public static String encode(String pw) {
		return "--10"+encrypt(pw, FIX_KEY);
	}
	
	private static String decrypt(String value, byte[] key) {
		if (value.length() < 4) return value;

		boolean hasPrefix = startsWith(value, PREFIX1) || startsWith(value, PREFIX2);
		
		if (!hasPrefix) return value;
		try {	
			Key secretKey = new SecretKeySpec(key,"DES");			
			byte[] input = hexStringToByteArray(value.substring(4));

			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding"); 
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] result = cipher.doFinal(input);

			//find 0 byte (anything after that is ignored!! because it is padding)
			int len = 0;
			for (int i = 0; i < result.length; i++) {
				if (result[i] == 0) {
					len = i;
					break;
				}
			}
			if (len == 0) len = result.length;

			byte[] text = new byte[len];

			System.arraycopy(result, 0, text, 0, len);
			return new String(text, ENCODING);			

		} catch(Exception e) {
			throw new RuntimeException(e);
		}	

	}
	private static String encrypt(String value, byte[] key) {

		try {	
			Key secretKey = new SecretKeySpec(key,"DES");	
			
			byte[] input =value.getBytes();

			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding"); 
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			// We need to pad the byte array. It needs to have a length of a multiple of 8 (for encryption purposes)
			int targetSize = input.length;
			while(targetSize % 8 != 0){
				targetSize ++;
			}

			// Defining a new array with a size as a multiple of 8
			byte[] newInput = new byte[targetSize];
			System.arraycopy(input, 0, newInput, 0, input.length);
			
			// Padding the array with a 0 byte and random bytes following it.
			boolean zeroByteSet = false;
			for(int i=input.length;i<targetSize;i++){
				
				if(zeroByteSet){
					byte[] nbyte = new byte[1];
					 Random randomno = new Random();
					 randomno.nextBytes(nbyte);
					newInput[i] = nbyte[0];
				}
				if(!zeroByteSet){newInput[i] = 0;zeroByteSet=true;}
			}

			byte[] result = cipher.doFinal(newInput);

			return bytesToHex(result);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}	

	}
	
	/**
	 * Creates a byte array from a Hex String
	 * 
	 * @param text Input string, may contain spaces
	 * @return byte array
	 */
	static byte[] hexStringToByteArray(String text) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (Character.isLetterOrDigit(c)) builder.append(c);
		}			
		String data = builder.toString();		
		if (data.length() % 2 != 0) return null;
		byte[] ret = new byte[data.length() / 2];
		int c = 0;
		for (int i = 0; i < data.length(); i+=2) {
			String hex = data.substring(i, i+2);
			ret[c] = (byte) Integer.parseInt(hex, 16);
			c++;
		}		
		return ret;
	}

	private static boolean startsWith(String value, byte[] firstBytes) {
		for (int i = 0; i < firstBytes.length; i++) {
			if ((firstBytes[i] & 0xFF) != value.charAt(i)) {
				return false;
			}
		}
		return true;
	}
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
    
}