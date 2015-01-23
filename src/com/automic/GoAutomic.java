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
			int AEClientNumber = 340; //5; // 330;
			String AEUserLogin = "JSM"; //"ARA"; //"BSP";
			String AEUserDepartment = "AUTOMIC"; //"ARA"; //"AUTOMIC";
			String AEUserPassword = "automic"; //"oneAutomation";
			char AEMessageLanguage = 'E';
			
			AECredentials myClientTarget = new AECredentials(AEHostnameOrIP,AECPPrimaryPort,AEClientNumber,AEUserDepartment,AEUserLogin,AEUserPassword,AEMessageLanguage);
			
			AECredentials myClientSource = new AECredentials(AEHostnameOrIP,AECPPrimaryPort,330,AEUserDepartment,"BSP","oneAutomation",AEMessageLanguage);
			
			try {
				Tests runTests = new Tests(myClientSource,myClientTarget);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
}