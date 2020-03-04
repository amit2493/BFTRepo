package com.sonim.bft.restapi.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonim.bft.adb.AdbCommunication;
import com.sonim.bft.automation.FlashingAutomation;
import com.sonim.bft.automation.SetupAutomation;
import com.sonim.bft.compress.TAR;
import com.sonim.bft.ftp.FTPFunctions;
import com.sonim.bft.ftp.FTPLocal;
import com.sonim.bft.restapi.model.BFTModel;
import com.sonim.bft.restapi.model.DeviceModel;
import com.sonim.bft.restapi.model.ResponseModel;
import com.sonim.bft.restapi.model.Status;

@CrossOrigin( origins = "*" )
@RestController
public class BFTController {
	@Value("${app.bft.setup.wifiname}")
	private String wifiName;
	@Value("${app.bft.setup.wifipwd}")
	private String wifiPwd;
	@Value("${app.bft.setup.googleid}")
	private String gmailId;
	@Value("${app.bft.setup.googlepwd}")
	private String gmailPwd;
	@Value("${app.bft.setup.buildpath}")
	private String buildPath;
	@Value("${app.bft.setup.fpt_ip}")
	private String ftpIp;
	@Value("${app.bft.setup.ftp.username}")
	private String ftpUser;
	@Value("${app.bft.setup.ftp.password}")
	private String ftpPwd;

	
	@PostMapping(path = "/startBFT")
	public ResponseModel startBFT(@RequestBody BFTModel bftModel) {
		System.out.println("Received BFT Start request");
		ResponseModel resultModel = new ResponseModel();
		try {
			if (!Status.STATUS.toString().equals("Ready")) {
				resultModel.setStatus(Status.STATUS.toString());
				resultModel.setMessage("Some process is going on, Not able to handle request now");
				return resultModel;
			}
			AdbCommunication adb = new AdbCommunication();
			List<DeviceModel> list = new ArrayList<DeviceModel>();
			list = adb.getDeviceList();
			if (list == null || list.size() < 2) {
				resultModel.setStatus(Status.STATUS.toString());
				resultModel.setMessage("Devicee are not available \n" + Status.MESSAGE);
				return resultModel;
			}
			if (list.get(0).getDeviceId().equals(bftModel.getDeviceId())) {
				Status.DEVICE1_ID = new StringBuffer(list.get(0).getDeviceId());
				Status.DEVICE2_ID = new StringBuffer(list.get(1).getDeviceId());
			} else {
				Status.DEVICE1_ID = new StringBuffer(list.get(1).getDeviceId());
				Status.DEVICE2_ID = new StringBuffer(list.get(0).getDeviceId());
			}

			Status.STATUS = new StringBuffer("In process");
			Status.FTP_URL = new StringBuffer("");
			System.out.println("Build name: "+bftModel.getBineryName());
			File sourceFile = new File(this.buildPath + bftModel.getBineryName());
			if (!sourceFile.exists()) {
				FTPFunctions ftpFunctions = new FTPFunctions(this.ftpIp, 21, this.ftpUser, this.ftpPwd,
						bftModel.getFtpLocation(), bftModel.getBineryName(),this.buildPath);
				ftpFunctions.start();
				resultModel.setMessage("Downloading Started");
			} else {
				Status.MESSAGE = new StringBuffer("Build already downloaded");
				System.out.println("Build already downloaded");
//				FTPLocal local = new FTPLocal(bftModel.getBineryName(), this.buildPath);
//				local.start();
				
				
				adb.rebootEDL(Status.DEVICE1_ID.toString());
				FlashingAutomation automation = new FlashingAutomation(bftModel.getBineryName().replaceAll(".tar.gz", ""),this.buildPath);
				automation.start();
			}
			Status.MESSAGE = new StringBuffer("Flashing process started");
			System.out.println("Flashing process started");
			resultModel.setStatus(Status.MESSAGE.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Status.STATUS = new StringBuffer("Exception occured");
			e.printStackTrace();
		}
		return resultModel;
	}

	@RequestMapping(path = "/status")
	public ResponseModel statusBFT() {
		ResponseModel resultModel = new ResponseModel();
		System.out.println("status request received");
		try {
			if(Status.DEVICE1_ID.toString().equals("Nill")) {
				AdbCommunication adb = new AdbCommunication();
				List<DeviceModel> list = new ArrayList<DeviceModel>();
				list = adb.getDeviceList();
				Status.DEVICE1_ID = new StringBuffer(list.get(0).getDeviceId());
				Status.DEVICE2_ID = new StringBuffer(list.get(1).getDeviceId());
			}			resultModel.setStatus(Status.STATUS.toString());
			resultModel.setMessage(
					Status.MESSAGE.toString() + ", Connected Devices: " + Status.DEVICE1_ID + ", " + Status.DEVICE2_ID);
			resultModel.setFtplinks(Status.FTP_URL.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Status.STATUS = new StringBuffer("Exception occured");
			e.printStackTrace();
		}
		return resultModel;
	}

	@RequestMapping(path = "/update")
	public ResponseModel updateBFT(String message) {
		ResponseModel resultModel = new ResponseModel();
		try {
			if ("Testing completed".equals(message)) {
				Status.MESSAGE = new StringBuffer("Testing completed, Preparing output files");
				System.out.println(Status.MESSAGE);
				TAR.compress("TestOutput.tar", new File("SAFE_v1.0\\Output\\Loop1"));
				Status.MESSAGE = new StringBuffer("Files Ready to upload");
				System.out.println(Status.MESSAGE);
				FTPFunctions ftpFunctions = new FTPFunctions(this.ftpIp, 21, this.ftpUser, this.ftpPwd);
				ftpFunctions.upload("TestOutput.tar", "TestOutput.tar", "/Logs/Test/");
				ftpFunctions.upload("Flash_report.jpg", "Flash_report.jpg", "/Logs/Flash/");
				Status.MESSAGE = new StringBuffer("Output files uploaded successfully: ");
				Status.FTP_URL = new StringBuffer("'/Logs/Test/TestOutput.tar','/Logs/Flash/Flash_report.jpg'");

				// System.out.println(Status.MESSAGE);
				System.out.println(Status.MESSAGE);
			}
			Status.STATUS = new StringBuffer("Ready");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Status.STATUS = new StringBuffer("Exception occured");
			e.printStackTrace();
		}
		return resultModel;
	}

	@RequestMapping(path = "/deviceList")
	public ResponseModel getDeviceList() {
		ResponseModel resultModel = new ResponseModel();
		try {
			if (Status.STATUS.toString().equals("In process"))
				Status.MESSAGE = new StringBuffer("Not able to perform request now \n" + Status.MESSAGE);
			else {
				AdbCommunication adb = new AdbCommunication();
				List<?> list = new ArrayList<DeviceModel>();
				list = adb.getDeviceList();
				resultModel.setResponse(list);
				Status.MESSAGE = new StringBuffer("");
				resultModel.setMessage("Device list prepared");
			}
			resultModel.setStatus(Status.STATUS.toString());
			resultModel.setMessage(Status.MESSAGE.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Status.STATUS = new StringBuffer("Exception occured");
			e.printStackTrace();
		}
		return resultModel;
	}

	@RequestMapping(path = "/startSetup")
	public ResponseModel doTest() {
		ResponseModel resultModel = new ResponseModel();
		try {
			AdbCommunication adb = new AdbCommunication();
			// adb.checkDeciveReady(Status.DEVICE1_ID.toString());
			// adb.checkDeciveReady(Status.DEVICE2_ID.toString());
			System.out.println("Flashing comleted");
			System.out.println("Waiting for device");
			//adb.checkDeciveReady(Status.DEVICE1_ID.toString());
			Thread.sleep(1000*10);
			List<DeviceModel> deviceList = adb.getDeviceList();
			int i=1;
			while (deviceList == null || deviceList.size() < 2) {
				System.out.println("Device list preparing "+i+".......Found:"+deviceList.size());
				if(++i==20) {
					adb.reboot(Status.DEVICE1_ID.toString());
					Thread.sleep(50000);
				}
				if(i==30) {
					System.out.println("Device communication failed ID: "+Status.DEVICE1_ID.toString()+"Stoping process");
					Status.MESSAGE = new StringBuffer("Device communication failed ID: "+Status.DEVICE1_ID.toString()+"Stoping process");
					Status.STATUS = new StringBuffer("Ready");
					return resultModel;
				}
					
				Thread.sleep(10000);
				deviceList = adb.getDeviceList();
			}
			System.out.println("Device list prepared "+i+".......Found:"+deviceList.size());
			//Thread.sleep(1000*60*5);
			Status.MESSAGE = new StringBuffer("Setup wizard started");
			System.out.println("Setup wizard started");
			Status.DEVICE1 = new StringBuffer("Started:" + Status.DEVICE1_ID);
			SetupAutomation setupAutomation = new SetupAutomation(deviceList.get(0).getDevice(),
					Status.DEVICE1_ID.toString(), deviceList.get(0).getAndriodVersion(), "0.0.0.0", "4723", wifiName,
					wifiPwd, gmailId, gmailPwd);
			setupAutomation.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Status.STATUS = new StringBuffer("Exception occured");
			e.printStackTrace();
		}
		return resultModel;
	}

	@RequestMapping(path = "/startTest")
	public ResponseModel doTest1() {
		ResponseModel resultModel = new ResponseModel();
		try {
			Status.STATUS = new StringBuffer("Ready");
			Status.MESSAGE = new StringBuffer("");
			// TestAutomation automation = new TestAutomation();
			// automation.start();
			resultModel.setStatus(Status.STATUS.toString());
			resultModel.setMessage(Status.MESSAGE.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Status.STATUS = new StringBuffer("Exception occured");
			e.printStackTrace();
		}
		return resultModel;
	}
}

