package com.pratik.insta.response;

import lombok.Data;

@Data
public class MessageResponse {

	private String message;

	public MessageResponse() {
		// TODO Auto-generated constructor stub
	}

	public MessageResponse(String message) {
		super();
		this.message = message;
	}
}
