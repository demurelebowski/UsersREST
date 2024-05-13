package com.drg.usersapi.services;

import com.drg.usersapi.dto.UserDTO;

import java.util.List;

public interface UserService {
	UserDTO insert(UserDTO dto);

	void delete(Long id);

	UserDTO updateUser(Long id, UserDTO user);

	List<UserDTO> getUsersByBirthDateRange(String from, String to);

	UserDTO partialUpdateUser(Long id, UserDTO user);
}
