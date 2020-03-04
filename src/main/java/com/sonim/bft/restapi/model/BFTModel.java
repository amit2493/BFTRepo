package com.sonim.bft.restapi.model;

public class BFTModel {
	private String ftpLocation;
	private String bineryName;
	private String deviceId;
	private String checkSum;
	private String token;
	
	
	public BFTModel() {
	}

	public BFTModel(String ftpLocation, String bineryName, String deviceId, String checkSum, String token) {
		this.setFtpLocation(ftpLocation);
		this.bineryName = bineryName;
		this.setDeviceId(deviceId);
		this.checkSum = checkSum;
		this.token = token;
	}

	public String getBineryName() {
		return bineryName;
	}

	public void setBineryName(String bineryName) {
		this.bineryName = bineryName;
	}

	public String getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getFtpLocation() {
		return ftpLocation;
	}

	public void setFtpLocation(String ftpLocation) {
		this.ftpLocation = ftpLocation;
	} 
	
	
}
