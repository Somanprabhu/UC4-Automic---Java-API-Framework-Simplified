package com.automic;
import java.io.IOException;

import org.xml.sax.SAXException;

import com.automic.tests.Tests;


public class GoAutomic {

	/**   
	 * this class only triggers tests
	 */

		public static void main(String argv[]) throws IOException {
			
			//String AEHostnameOrIP = "192.168.11.135";
			String AEHostnameOrIP = "172.16.148.35";
			int AECPPrimaryPort = 2217;
			int AEClientNumber = 330; //5; // 330;
			String AEUserLogin = "BSP"; //"ARA"; //"BSP";
			String AEUserDepartment = "AUTOMIC"; //"ARA"; //"AUTOMIC";
			String AEUserPassword = "oneAutomation"; //"oneAutomation";
			char AEMessageLanguage = 'E';
			
			AECredentials myClientTarget = new AECredentials(AEHostnameOrIP,AECPPrimaryPort,AEClientNumber,AEUserDepartment,AEUserLogin,AEUserPassword,AEMessageLanguage);
			
			AECredentials myClientSource = new AECredentials(AEHostnameOrIP,AECPPrimaryPort,1,AEUserDepartment,AEUserLogin,"automic",AEMessageLanguage);
			
			try {
				Tests runTests = new Tests(myClientSource,myClientTarget);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
}