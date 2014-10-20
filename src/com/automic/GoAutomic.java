package com.automic;
import java.io.IOException;
import com.automic.tests.Tests;


public class GoAutomic {

	/**   
	 * this class only triggers tests
	 */

		public static void main(String argv[]) throws IOException {
			
			String AEHostnameOrIP = "192.168.11.135";
			//String AEHostnameOrIP = "172.16.148.35";
			int AECPPrimaryPort = 2217;
			int AEClientNumber = 5; // 330;
			String AEUserLogin = "ARA"; //"BSP";
			String AEUserDepartment = "ARA"; //"AUTOMIC";
			String AEUserPassword = "ARA"; //"oneAutomation";
			char AEMessageLanguage = 'E';
			
			AECredentials myClient = new AECredentials(AEHostnameOrIP,AECPPrimaryPort,AEClientNumber,AEUserDepartment,AEUserLogin,AEUserPassword,AEMessageLanguage);
			Tests runTests = new Tests(myClient);
	
		}
}