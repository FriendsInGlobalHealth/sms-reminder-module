package org.openmrs.module.smsreminder.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SmsReminderHelper {

	public boolean isNodeInstalled(){
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec("node -v");
			p.waitFor();
			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			
			while ((line = b.readLine()) != null) {
				if (line.split("\\.").length == 3) {
					return true;
				}
			}
			
			b.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
