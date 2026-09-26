package com.temple.Archana.exception;

import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorResponse {

	private int Apistatuscode;
	private String ErrorMessage;
	private Map<String, String> errors ;
	

	public ErrorResponse(int Apistatuscode, String ErrorMessage ) {
		
		this.Apistatuscode=Apistatuscode;
		this.ErrorMessage=ErrorMessage;
	
	}
	
	public ErrorResponse(int Apistatuscode, String ErrorMessage, Map<String, String> errors ) {
		this.Apistatuscode=Apistatuscode;
		this.ErrorMessage=ErrorMessage;
		this.errors=errors;
	}
	
	public int getApistatuscode() {
		return Apistatuscode;
	}

	public void setApistatuscode(int apistatuscode) {
		Apistatuscode = apistatuscode;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}
	public Map<String, String> getError() {
		return errors;
	}

	public void setError(Map<String, String> errors) {
		this.errors = errors;
	}
	
}
