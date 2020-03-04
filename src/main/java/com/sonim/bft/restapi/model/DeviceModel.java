package com.sonim.bft.restapi.model;

public class DeviceModel {
	private String deviceId;
	private String device;
	private String product;
	private String model;
	private String andriodVersion;
	
	public DeviceModel(String deviceId, String product, String model, String device,String andriodVersion) {
		this.deviceId = deviceId;
		this.device = device;
		this.product = product;
		this.model = model;
		this.andriodVersion = andriodVersion;
	}

	public DeviceModel(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getAndriodVersion() {
		return andriodVersion;
	}
	public void setAndriodVersion(String andriodVersion) {
		this.andriodVersion = andriodVersion;
	}
}
