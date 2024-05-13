package com.drg.usersapi.dto;

import com.drg.usersapi.entities.User;
import com.drg.usersapi.utils.ConverterUtil;
import com.drg.usersapi.validation.FullUpdate;
import com.drg.usersapi.validation.PartialUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDTO {
	@JsonProperty("id")
	private Long id;

	@NotBlank(groups = { FullUpdate.class })
	@Email(groups = { FullUpdate.class, PartialUpdate.class })
	@JsonProperty("email")
	@Schema(name = "email", example = "example@mail.com", required = true)
	private String email;

	@NotBlank(groups = { FullUpdate.class })
	@JsonProperty("first_name")
	@Schema(name = "first_name", example = "John", required = true)
	private String firstName;

	@NotBlank(groups = { FullUpdate.class })
	@JsonProperty("last_name")
	@Schema(name = "last_name", example = "Smith", required = true)
	private String lastName;

	@NotBlank(groups = { FullUpdate.class })
	@JsonProperty("birth_date")
	@Schema(name = "birth_date", example = "2000-01-11", required = true)
	private String birthDate;

	@JsonProperty("address")
	@Schema(name = "address", example = "Main St. Mankato Mississippi 96522")
	private String address;

	@JsonProperty("phone_number")
	@Schema(name = "phone_number", example = "212-456-7890")
	private String phoneNumber;

	public UserDTO(User user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.birthDate = ConverterUtil.stringFromLocalDate(user.getBirthDate());
		this.email = user.getEmail();
		this.phoneNumber = user.getPhoneNumber();
		this.address = user.getAddress();
	}

	public UserDTO(Long id, String email, String firstName, String lastName, String birthDate, String address, String phoneNumber) {
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
}
