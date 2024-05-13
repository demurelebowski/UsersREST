package com.drg.usersapi.entities;

import com.drg.usersapi.dto.UserDTO;
import com.drg.usersapi.utils.ConverterUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
	private Long id;

	private String email;

	private String firstName;

	private String lastName;

	private LocalDate birthDate;

	private String address;

	private String phoneNumber;

	public User(UserDTO userDTO) {
		this.id = userDTO.getId();
		this.firstName = userDTO.getFirstName();
		this.lastName = userDTO.getLastName();
		this.birthDate = ConverterUtil.localDateFromString(userDTO.getBirthDate());
		this.email = userDTO.getEmail();
		this.phoneNumber = userDTO.getPhoneNumber();
		this.address = userDTO.getAddress();
	}
}
