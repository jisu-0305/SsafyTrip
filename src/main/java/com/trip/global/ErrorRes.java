package com.trip.global;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorRes extends GeneralRes {
	private String message;
	
	public ErrorRes() {}
	
	public ErrorRes(String message) {
		this.message = message;
	}
	
	
}
