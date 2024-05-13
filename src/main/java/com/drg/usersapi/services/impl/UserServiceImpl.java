package com.drg.usersapi.services.impl;

import com.drg.usersapi.dto.UserDTO;
import com.drg.usersapi.entities.User;
import com.drg.usersapi.exceptions.BirthDateRestrictionException;
import com.drg.usersapi.exceptions.InvalidDateRangeException;
import com.drg.usersapi.exceptions.UserNotFoundException;
import com.drg.usersapi.services.UserService;
import com.drg.usersapi.utils.ConverterUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	@Value("${user.age.min}")
	private int minUserAge;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Override
	public UserDTO insert(UserDTO userDTO) {
		LOGGER.debug("Inserting user: {}", userDTO);
		try {
			User user = new User(userDTO);
			checkUser(user);
			//create user in the repository
			return new UserDTO(user);
		} catch (Exception e) {
			LOGGER.error("Error inserting user: {}", e.getMessage());
			throw e;
		}
	}

	@Override
	public void delete(Long id) {
		LOGGER.debug("Deleting user with id: {}", id);
		try {
			User user = getUserById(id);
			// Delete user from the repository
		} catch (Exception e) {
			LOGGER.error("Error deleting user with id {}: {}", id, e.getMessage());
			throw e;
		}
	}

	@Override
	public UserDTO updateUser(Long id, UserDTO userDTO) {
		LOGGER.debug("Updating user: {}", userDTO);
		try {
			User user = getUserById(userDTO.getId());
			user.setEmail(userDTO.getEmail());
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setBirthDate(ConverterUtil.localDateFromString(userDTO.getBirthDate()));
			user.setAddress(userDTO.getAddress());
			user.setPhoneNumber(userDTO.getPhoneNumber());
			checkUser(user);
			// Save the updated user to the repository
			return new UserDTO(user);
		} catch (Exception e) {
			LOGGER.error("Error updating user: {}", e.getMessage());
			throw e;
		}
	}

	@Override
	public UserDTO partialUpdateUser(Long id, UserDTO partialUser) {
		LOGGER.debug("Partial updating user with id {}: {}", id, partialUser);
		try {
			User user = readById(id);
			updateFieldsFromDTO(user, partialUser);
			checkUser(user);
			// Save the updated user to the repository
			return new UserDTO(user);
		} catch (Exception e) {
			LOGGER.error("Error partial updating user with id {}: {}", id, e.getMessage());
			throw e;
		}
	}

	private void checkUser(User user) {
		LocalDate birthDate = user.getBirthDate();
		if (birthDate != null) {
			if (!birthDate.isBefore(LocalDate.now())) {
				throw new BirthDateRestrictionException("Birth date value must be earlier than current date.");
			}

			if (!isUserOldEnough(birthDate)) {
				throw new BirthDateRestrictionException("User must be at least " + minUserAge + " years old.");
			}
		}
	}

	private boolean isUserOldEnough(LocalDate birthDate) {
		LocalDate currentDate = LocalDate.now();
		return Period.between(birthDate, currentDate)
				.getYears() >= minUserAge;
	}

	private User getUserById(Long id) {
		User user = readById(id);
		if (user == null) {
			throw new UserNotFoundException("User with id " + id + " not found");
		}
		return user;
	}

	public User readById(Long id) {
		// find user by id
		return new User();
	}

	@Override
	public List<UserDTO> getUsersByBirthDateRange(String from, String to) {
		LOGGER.debug("Fetching users by birth date range: from {} to {}", from, to);
		try {
			LocalDate fromDate = ConverterUtil.localDateFromString(from);
			LocalDate toDate = ConverterUtil.localDateFromString(to);

			if (toDate.isBefore(fromDate)) {
				throw new InvalidDateRangeException("'From' date must be before 'To' date.");
			}

			// Fetch users from the repository
			return Collections.emptyList();
		} catch (Exception e) {
			LOGGER.error("Error fetching users by birth date range: {}", e.getMessage());
			throw e;
		}
	}

	private void updateFieldsFromDTO(User user, UserDTO partialUser) {
		if (!StringUtils.isEmpty(partialUser.getEmail())) {
			user.setEmail(partialUser.getEmail());
		}
		if (!StringUtils.isEmpty(partialUser.getFirstName())) {
			user.setFirstName(partialUser.getFirstName());
		}
		if (!StringUtils.isEmpty(partialUser.getLastName())) {
			user.setLastName(partialUser.getLastName());
		}
		if (!StringUtils.isEmpty(partialUser.getBirthDate())) {
			user.setBirthDate(ConverterUtil.localDateFromString(partialUser.getBirthDate()));
		}
		if (!StringUtils.isEmpty(partialUser.getAddress())) {
			user.setAddress(partialUser.getAddress());
		}
		if (!StringUtils.isEmpty(partialUser.getPhoneNumber())) {
			user.setPhoneNumber(partialUser.getPhoneNumber());
		}
	}

}
