package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class UserWithStatusResponse {
	private Integer userId;
	private String firstName;
	private String lastName;
	private String email;
	private String mobile;
	private String address;
	private String status;

	public UserWithStatusResponse(Integer userId, String firstName, String lastName, String email, String mobile,
			String address, String status) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobile = mobile;
		this.address = address;
		this.status = status;
	}
}