package com.drg.usersapi.controllers;

import com.drg.usersapi.dto.UserDTO;
import com.drg.usersapi.exceptions.BirthDateRestrictionException;
import com.drg.usersapi.exceptions.InvalidDateFormatException;
import com.drg.usersapi.exceptions.InvalidDateRangeException;
import com.drg.usersapi.services.UserService;
import com.drg.usersapi.validation.FullUpdate;
import com.drg.usersapi.validation.PartialUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping
	@Operation(summary = "Create a new user.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "User created."),
			@ApiResponse(responseCode = "400", description = "Problem with request.") })
	public ResponseEntity<?> insert(@RequestBody @Validated(FullUpdate.class) UserDTO user) {
		UserDTO createdUser = userService.insert(user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(createdUser);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update user.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User updated."),
			@ApiResponse(responseCode = "400", description = "Problem with request.") })
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Validated(FullUpdate.class) UserDTO user) {
		UserDTO updatedUser = userService.updateUser(id, user);
		return ResponseEntity.ok(updatedUser);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update user partially.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User updated."),
			@ApiResponse(responseCode = "400", description = "Problem with request.") })
	public ResponseEntity<?> updatePartialUser(@PathVariable Long id, @RequestBody @Validated(PartialUpdate.class) UserDTO partialUser) {
		UserDTO updatedUser = userService.partialUpdateUser(id, partialUser);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete user.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User deleted."),
			@ApiResponse(responseCode = "400", description = "Problem with request.") })
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.ok()
				.build();
	}

	@GetMapping
	@Operation(summary = "Search users by birth date range.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Users found."),
			@ApiResponse(responseCode = "400", description = "Problem with request.") })
	public ResponseEntity<?> searchUsersByBirthDateRange(@RequestParam @Parameter(name = "from", description = "from date", example = "2000-01-01") String from,
			@Parameter(name = "to", description = "to date", example = "2000-05-09") String to) {
		List<UserDTO> users = userService.getUsersByBirthDateRange(from, to);
		return ResponseEntity.ok(users);
	}

	@ExceptionHandler(BirthDateRestrictionException.class)
	public ResponseEntity<?> handleAgeRestrictionException(BirthDateRestrictionException e) {
		return ResponseEntity.badRequest()
				.body(e.getMessage());
	}

	@ExceptionHandler(InvalidDateFormatException.class)
	public ResponseEntity<?> handleInvalidDateFormatException(InvalidDateFormatException e) {
		return ResponseEntity.badRequest()
				.body(e.getMessage());
	}

	@ExceptionHandler(InvalidDateRangeException.class)
	public ResponseEntity<?> handleInvalidDateRangeException(InvalidDateRangeException e) {
		return ResponseEntity.badRequest()
				.body(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult()
				.getAllErrors()
				.forEach(error -> {
					String fieldName = ((FieldError) error).getField();
					String errorMessage = error.getDefaultMessage();
					errors.put(fieldName, errorMessage);
				});
		return ResponseEntity.badRequest()
				.body(errors);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleInvalidDateRangeException(HttpMessageNotReadableException e) {
		return ResponseEntity.badRequest()
				.body(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleInternalServerError(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Internal server error occurred.");
	}
}
