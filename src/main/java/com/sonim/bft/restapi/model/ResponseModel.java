package com.sonim.bft.restapi.model;

import java.util.List;

public class ResponseModel {
	private String status;
	private String message;
	private String ftplinks;
	private List<?> response;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFtplinks() {
		return ftplinks;
	}
	public void setFtplinks(String ftplinks) {
		this.ftplinks = ftplinks;
	}
	public List<?> getResponse() {
		return response;
	}
	public void setResponse(List<?> response) {
		this.response = response;
	}
}
