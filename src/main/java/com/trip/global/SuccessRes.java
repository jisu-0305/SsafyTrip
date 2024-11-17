package com.trip.global;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SuccessRes extends GeneralRes {
	
	private String message;
	
	public SuccessRes() {}
	
	public SuccessRes(String message) {
		this.message = message;
	}
}
