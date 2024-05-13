package com.drg.usersapi.utils;

import com.drg.usersapi.exceptions.InvalidDateFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ConverterUtil {
	static final private String PATTERN = "yyyy-MM-dd";
	static final private DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

	public static LocalDate localDateFromString(String str) {
		if (Objects.isNull(str)) {
			return null;
		}
		try {
			return LocalDate.parse(str, DATE_TIME_FORMATTER);
		} catch (Exception e) {
			throw new InvalidDateFormatException("Invalid date format. Use: " + PATTERN);
		}
	}

	public static String stringFromLocalDate(LocalDate localDate) {
		if (Objects.isNull(localDate)) {
			return null;
		}
		return localDate.format(DATE_TIME_FORMATTER);
	}
}
