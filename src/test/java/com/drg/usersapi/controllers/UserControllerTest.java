package com.drg.usersapi.controllers;

import com.drg.usersapi.dto.UserDTO;
import com.drg.usersapi.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserServiceImpl service;
	private UserDTO user;
	private static final String USERS_ENDPOINT = "/users";

	@Before
	public void setup() {
		user = new UserDTO(1L, "example@mail.com", "John", "Tray", "1999-01-02", "Main St. Mankato Mississippi 96522", "212-456-7890");
	}

	@Test
	public void givenValidUserData_whenCreateUser_thenReturnCreatedUser() throws Exception {
		UserDTO createdUserDTO = user.toBuilder()
				.id(2L)
				.build();

		given(service.insert(any(UserDTO.class))).willReturn(createdUserDTO);

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.first_name", is(user.getFirstName())))
				.andExpect(jsonPath("$.last_name", is(user.getLastName())))
				.andExpect(jsonPath("$.email", is(user.getEmail())))
				.andExpect(jsonPath("$.birth_date", is(user.getBirthDate())));
	}

	@Test
	public void givenInvalidUserData_whenCreateUser_thenReturnBadRequest() throws Exception {
		user = new UserDTO();

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenIncompleteUserData_whenCreateUser_thenReturnBadRequest() throws Exception {
		user.setLastName(null);

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenNonValidEmail_whenCreateUser_thenReturnBadRequest() throws Exception {
		user.setEmail("wrong");

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenNonValidFirstName_whenCreateUser_thenReturnBadRequest() throws Exception {
		user.setFirstName("");

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenNonValidLastName_whenCreateUser_thenReturnBadRequest() throws Exception {
		user.setLastName("");

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenNonValidBirthday_whenCreateUser_thenReturnBadRequest() throws Exception {
		user.setBirthDate("");

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenInvalidBirthday_whenCreateUser_thenReturnBadRequest() throws Exception {
		user.setBirthDate(null);

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenNullAddress_whenCreateUser_thenReturnBadRequest() throws Exception {
		user.setAddress(null);

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isCreated());
	}

	@Test
	public void givenEmptyAddress_whenCreateUser_thenReturnBadRequest() throws Exception {
		user.setAddress("");

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isCreated());
	}

	@Test
	public void givenNullPhone_whenCreateUser_thenReturnBadRequest() throws Exception {
		user.setPhoneNumber(null);

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isCreated());
	}

	@Test
	public void givenEmptyPhone_whenCreateUser_thenReturnBadRequest() throws Exception {
		user.setPhoneNumber("");

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isCreated());
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void givenInvalidJsonPayload_whenCreateUser_thenReturnBadRequest() throws Exception {
		String invalidJsonPayload = "{\"email\": \"example@mail.com\" \"first_name\": \"John\"}";

		mvc.perform(post(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
						.content(invalidJsonPayload))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenUsers_whenGetUsersByBirthDateRange_thenReturnJsonArray() throws Exception {
		List<UserDTO> userList = Arrays.asList(user);

		given(service.getUsersByBirthDateRange("2022-01-01", "2022-01-03")).willReturn(userList);

		mvc.perform(get(USERS_ENDPOINT + "?from=2022-01-01&to=2022-01-03").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].first_name", is(user.getFirstName())));
	}

	@Test
	public void givenUsersInvalidDate_whenGetUsersByBirthDateRange_thenReturnBadRequest() throws Exception {
		List<UserDTO> userList = Arrays.asList(user);

		given(service.getUsersByBirthDateRange("2022-01-01", "2022-01-03")).willReturn(userList);

		mvc.perform(get(USERS_ENDPOINT).param("from", "2022-01-06")
						.param("to", "2022-01-03")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}
