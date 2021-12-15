package com.nagp.ebroker.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class DateHelperTest {

	@Test
	@DisplayName("Test IsValidDate returns false if date is not weekday ")
	void testIsValidDateFalse() {
		String[] dates = { "2021-12-12T10:12:30.00Z", "2021-12-11T10:12:30.00Z" };
		for (String str : dates) {
			Clock clock = Clock.fixed(Instant.parse(str), ZoneId.of("Asia/Kolkata"));

			LocalDate date = LocalDate.now(clock);
			try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class)) {
				when(LocalDate.now()).thenReturn(date).thenReturn(date);
				assertFalse(DateHelper.isValidDate());
			}
		}
	}

	@Test
	@DisplayName("Test IsValidDate returns true if date is weekday ")
	void testIsValidDateTrue() {
		String[] dateStrings = { "2021-12-13 08:59", "2021-12-13 15:01" };
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		for (String dateStr : dateStrings) {
			LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
			Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
			LocalDate date = LocalDate.now(clock);
			LocalTime time = LocalTime.now(clock);

			try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class)) {

				try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
					mockedDate.when(LocalDate::now).thenReturn(date);
					mockedTime.when(LocalTime::now).thenReturn(time);
					assertFalse(DateHelper.isValidDate());
				}
			}
		}
	}

	@Test
	@DisplayName("Test IsValidDate returns true if time is between 10-3 ")
	void testIsValidTimeFalse() {
		String dateStr = "2021-12-13 10:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
		Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
		LocalDate date = LocalDate.now(clock);
		LocalTime time = LocalTime.now(clock);

		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class)) {

			try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
				mockedDate.when(LocalDate::now).thenReturn(date);
				mockedTime.when(LocalTime::now).thenReturn(time);
				assertTrue(DateHelper.isValidDate());
			}
		}
	}

	@Test
	public void testPrivateInstance() throws InstantiationException {
		InstantiationException thrown = assertThrows(InstantiationException.class, () -> {
			DateHelper.class.newInstance();
		});
		assertEquals("Instances of this type are forbidden.", thrown.getMessage());
	}

}
