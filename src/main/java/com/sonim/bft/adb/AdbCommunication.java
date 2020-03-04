package com.sonim.bft.adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.sonim.bft.restapi.model.DeviceModel;
import com.sonim.bft.restapi.model.Status;

public class AdbCommunication {

	public List<DeviceModel> getDeviceList() throws Exception {
		String cmd = "adb devices -l";
		BufferedReader input =null;
		try {
			Process p;
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();

			String line;
			String version;
			String[] params;
			BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((line = err.readLine()) != null) {
				System.out.println(line);
			}
			err.close();
			List<DeviceModel> deviceList = new ArrayList<DeviceModel>();
			input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
				params = line.replaceAll("\\s{2,20}", " ").split(" ");
				if(line.indexOf("Offline")>0) 
					continue;
				if (line.indexOf("product") < 0)
					continue;
				// System.out.println(line);
				if (params.length < 5)
					continue;
				deviceList.add(new DeviceModel(params[0], params[2].substring(8), params[3].substring(6),
						params[4].substring(7), "8.1.0"));
//				p = Runtime.getRuntime().exec("adb -s " + params[0] + " shell getprop ro.build.version.release");
//				p.waitFor();
//				BufferedReader input1 = new BufferedReader(new InputStreamReader(p.getInputStream()));
//				version = (line = input1.readLine()) != null ? line : "";
//				deviceList.add(new DeviceModel(params[0], params[2].substring(8), params[3].substring(6),
//						params[4].substring(7), version));
			}
			return deviceList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			input.close();
		}
		return null;
	}

	public boolean rebootEDL(String deviceId) {

		try {
			Process p = Runtime.getRuntime().exec("adb -s " + deviceId + " reboot edl");
			p.waitFor();
			BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				if(line.equals("error: device offline")) {
					Status.MESSAGE = new StringBuffer("Not able to comminicate with device! Please reconnect and try again");
					System.out.println(line+"\nNot able to comminicate with device! Please reconnect and try again");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public String reboot(String deviceId) {

		try {
			Process p = Runtime.getRuntime().exec("adb -s " + deviceId + " reboot");
			p.waitFor();
		} catch (Exception e) {//error: device offline
			e.printStackTrace();
		}

		return null;
	}

	//adb -s 903290f3 wait-for-device logcat | findstr "Displayed com.google.android.setupwizard/.user.WelcomeActivity"
	public boolean checkDeciveReady(String deviceId) {
		try {
			String cmd="adb -s "+deviceId+" wait-for-device logcat | findstr \"Displayed com.google.android.setupwizard/.user.WelcomeActivity\"";
			System.out.println("Checking :"+deviceId +"  "+cmd);
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			System.out.println("Ready :"+deviceId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
