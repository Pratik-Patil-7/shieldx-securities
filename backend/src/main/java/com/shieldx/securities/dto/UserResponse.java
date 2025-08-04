package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class UserResponse {
	
	private int userId;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String mobile;
	private String address;

	public UserResponse(int userId, String firstName, String lastName, String username, String email, String mobile,
			String address) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.mobile = mobile;
		this.address = address;
	}

}
