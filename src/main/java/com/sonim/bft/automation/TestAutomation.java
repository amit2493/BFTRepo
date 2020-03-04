package com.sonim.bft.automation;

import com.sonim.bft.restapi.model.Status;

public class TestAutomation extends Thread {
	public void run() {
		try {

			String[] command = { "cmd.exe", "/C", "Start", "test.exe" };
			Runtime rn = Runtime.getRuntime();
			Process pr = rn.exec(command);
			pr.waitFor();
			// Process process = new
			// ProcessBuilder("D:\\test\\"+flashingTool,buildName).start();
			Status.MESSAGE = new StringBuffer("Test framework started");
			System.out.println("Test framework started");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
