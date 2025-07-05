package com.shieldx.securities.dto;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddVIPpersonDTO {

	@NotBlank(message = "Name is Required")
	private String name;
	@NotBlank(message = "Email is Required")
	@Email
    private String email;
	@NotBlank(message = "Mobile Number is Required")
    private String mobile;
	@NotBlank(message = "Gender is required")
    private String gender;
	@CreatedDate
    private LocalDate dateOfBirth;
    @NotBlank(message = "Address is Required")
    private String address;
    @NotBlank(message = "Profession is Required")
    private String profession;
    private String reasonForSecurity;
}
