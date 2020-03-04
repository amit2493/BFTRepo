package com.sonim.bft.automation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sonim.bft.restapi.model.Status;

public class FlashingAutomation  extends Thread{
	private String buildName;
	private String buildPath;
	private String deviceModel;
	private String buildVersion;
	private String operator;
	
	public FlashingAutomation(String buildName,String buildPath) {
		this.buildName = buildName;
		this.buildPath = buildPath;
		setBuildInfo();
	}
	
	public void run() {
		try { 
			
			String flashingTool = deviceModel.equals("XP8")?"Autoflash_XP8.exe":"Autoflash_XP3.exe";
			String[] command = {"cmd.exe", "/C", "Start", flashingTool, buildPath+buildName };
			Runtime rn = Runtime.getRuntime();
			Process pr = rn.exec(command);
			pr.waitFor();
			//Process process = new ProcessBuilder("D:\\test\\"+flashingTool,buildName).start();
			Status.MESSAGE =  new StringBuffer("Flashing process started");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setBuildInfo() {
		Pattern pattern = Pattern.compile("\\d{1,2}[A-Z]{1}\\.\\d{1,2}\\.\\d{1,2}\\-\\d{2}\\-\\d{1,2}\\.\\d{1,2}\\.\\d{1,2}\\-\\d{1,2}\\.\\d{1,2}\\.\\d{1,2}");
		Matcher matcher = pattern.matcher(buildName);
		if(matcher.find()) {
			String build=matcher.group();
			this.deviceModel = "XP"+build.charAt(0);
			this.buildVersion = build.substring(build.length()-6);
			this.operator = build.substring(build.length()-9,build.length()-7);
		}
	}

}
