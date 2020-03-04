package com.sonim.bft.ftp;

import com.sonim.bft.adb.AdbCommunication;
import com.sonim.bft.automation.FlashingAutomation;
import com.sonim.bft.restapi.model.Status;

public class FTPLocal extends Thread{
	private String bineryName;
	private String buildPath;
	public FTPLocal(String bineryName, String buildPath) {
		this.bineryName = bineryName;
		this.buildPath = buildPath;
	}
	
	public void run() {
		try {
			System.out.println("Downloading Started");
			int i=0;
			while(i<100) {
				Status.MESSAGE = new StringBuffer("Download in progress. File Name:"+bineryName+" [Downloaded : "+(++i)+"% ]");
				Thread.sleep(2000);
			}
			System.out.println("Downloading completed");
			System.out.println("Starting decompres");
			i=0;
			while(i<100) {
				Status.MESSAGE = new StringBuffer("Decompres in progress. [Completed : "+(++i)+"% ]");
				Thread.sleep(500);
			}
			System.out.println("Finished decompress");
			AdbCommunication adb = new AdbCommunication();
			adb.rebootEDL(Status.DEVICE1_ID.toString());
 			Status.MESSAGE = new StringBuffer("Starting flashing");
 			System.out.println(Status.MESSAGE); 
			FlashingAutomation automation = new FlashingAutomation(bineryName.replaceAll(".tar.gz", ""),buildPath);
			automation.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
