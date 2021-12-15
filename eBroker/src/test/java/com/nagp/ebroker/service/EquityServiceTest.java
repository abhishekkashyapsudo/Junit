package com.nagp.ebroker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nagp.ebroker.dao.EquityDao;
import com.nagp.ebroker.model.Equity;
import com.nagp.ebroker.model.Trader;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EquityService.class)
class EquityServiceTest {

	@Autowired
	private EquityService service;

	@MockBean
	private EquityDao dao;

	private static Trader trader;
	private static List<Equity> equities;

	@BeforeAll
	public static void setupSpec() {
		equities = new ArrayList<>();
		equities.add(new Equity(1, "Test 1", 10));
		equities.add(new Equity(2, "Test 2", 20));
		equities.add(new Equity(3, "Test 3", 30));
		equities.add(new Equity(4, "Test 4", 40));
		equities.add(new Equity(5, "Test 5", 50));

		trader = new Trader(1001, "Test", 1000.50);

		Map<Integer, Integer> equities = new HashMap<>();
		equities.put(1, 10);
		equities.put(2, 20);
		equities.put(3, 30);
		equities.put(4, 40);
		equities.put(5, 50);
		trader.setEquities(equities);
	}

	@BeforeEach
	public void setup() {
		trader.setFunds(1000.50);
		if (trader.getEquities().isEmpty()) {
			Map<Integer, Integer> equities = new HashMap<>();
			equities.put(1, 10);
			equities.put(2, 20);
			equities.put(3, 30);
			equities.put(4, 40);
			equities.put(5, 50);
			trader.setEquities(equities);
		}
	}

	@Test
	@DisplayName("Test exception is thrown if date/time is not weekday")
	void testBuyFail() {
		String dateStr = "2021-12-11 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
		Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
		LocalDate date = LocalDate.now(clock);
		LocalTime time = LocalTime.now(clock);

		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class)) {

			try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
				mockedDate.when(LocalDate::now).thenReturn(date);
				mockedTime.when(LocalTime::now).thenReturn(time);
				assertEquals("Trade can only be carried in Monday-Friday 9-3", service.buy(1, 10));

			}
		}

	}

	@Test
	@DisplayName("Test exception is thrown if trader is not present in DB")
	void testBuyFail1() {
		Mockito.when(dao.getEquity(1)).thenReturn(Optional.of(equities.get(0)));
		String dateStr = "2021-12-13 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
		Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
		LocalDate date = LocalDate.now(clock);
		LocalTime time = LocalTime.now(clock);

		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class)) {

			try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
				mockedDate.when(LocalDate::now).thenReturn(date);
				mockedTime.when(LocalTime::now).thenReturn(time);
				assertEquals("Trader not initialized in the database", service.buy(1, 10));
			}
		}

	}

	@Test
	@DisplayName("Test exception is thrown if equity is not present in DB")
	void testBuyFail2() {
		Mockito.when(dao.traderDetails(1)).thenReturn(Optional.of(trader));
		String dateStr = "2021-12-13 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
		Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
		LocalDate date = LocalDate.now(clock);
		LocalTime time = LocalTime.now(clock);

		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class)) {

			try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
				mockedDate.when(LocalDate::now).thenReturn(date);
				mockedTime.when(LocalTime::now).thenReturn(time);
				assertEquals("No such equity found in the database", service.buy(1, 10));
			}
		}

	}

	@Test
	void testBuy() {
		List<Equity> resultEquities = new ArrayList<>();
		resultEquities.add(new Equity(1, "Test 1", 10));
		resultEquities.add(new Equity(2, "Test 2", 20));
		resultEquities.add(new Equity(3, "Test 3", 30));
		resultEquities.add(new Equity(4, "Test 4", 40));
		resultEquities.add(new Equity(5, "Test 5", 50));
		Mockito.when(dao.traderDetails(1)).thenReturn(Optional.of(trader));
		Mockito.when(dao.getEquity(1)).thenReturn(Optional.of(equities.get(0)));
		Mockito.when(dao.getEquities(trader.getEquities().keySet())).thenReturn(resultEquities);
		String dateStr = "2021-12-13 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
		Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
		LocalDate date = LocalDate.now(clock);
		LocalTime time = LocalTime.now(clock);

		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class)) {

			try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
				mockedDate.when(LocalDate::now).thenReturn(date);
				mockedTime.when(LocalTime::now).thenReturn(time);
				assertEquals("Updated Trader details: \n"
						+ "Trader [id=1001, userName=Test, funds=900.5<br/>Available Equities:"
						+ " <br/>Equity [id=1, name=Test 1, price=10.0]     ===> 20<br/>Equity "
						+ "[id=2, name=Test 2, price=20.0]     ===> 20<br/>Equity "
						+ "[id=3, name=Test 3, price=30.0]     ===> 30<br/>Equity [id=4, name=Test 4, price=40.0]"
						+ "     ===> 40<br/>Equity [id=5, name=Test 5, price=50.0]     ===> 50<br/><br/>",
						service.buy(1, 10));

			}
		}

	}

	@Test
	@DisplayName("Test exception is thrown if date/time is not weekday while selling")
	void testSellFail() {
		String dateStr = "2021-12-11 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
		Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
		LocalDate date = LocalDate.now(clock);
		LocalTime time = LocalTime.now(clock);

		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class)) {

			try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
				mockedDate.when(LocalDate::now).thenReturn(date);
				mockedTime.when(LocalTime::now).thenReturn(time);
				assertEquals("Trade can only be carried in Monday-Friday 9-3", service.sell(1, 10));

			}
		}

	}

	@Test
	@DisplayName("Test exception is thrown if equity is not present in DB while selling")
	void testSellFail1() {
		Mockito.when(dao.traderDetails(1)).thenReturn(Optional.of(trader));
		String dateStr = "2021-12-13 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
		Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
		LocalDate date = LocalDate.now(clock);
		LocalTime time = LocalTime.now(clock);

		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class)) {

			try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
				mockedDate.when(LocalDate::now).thenReturn(date);
				mockedTime.when(LocalTime::now).thenReturn(time);
				assertEquals("No such equity found in the database", service.sell(1, 10));
			}
		}

	}

	@Test
	void testSell() {
		Mockito.when(dao.traderDetails(1)).thenReturn(Optional.of(trader));
		Mockito.when(dao.getEquity(1)).thenReturn(Optional.of(equities.get(0)));

		String dateStr = "2021-12-13 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
		Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
		LocalDate date = LocalDate.now(clock);
		LocalTime time = LocalTime.now(clock);

		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class)) {

			try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
				mockedDate.when(LocalDate::now).thenReturn(date);
				mockedTime.when(LocalTime::now).thenReturn(time);
				assertEquals(
						"Updated Trader details: \n"
								+ "Trader [id=1001, userName=Test, funds=1100.5<br/>Available Equities: <br/><br/>",
						service.sell(1, 10));

			}
		}
	}

	@Test
	void testAddFundsFail() {
		assertEquals("Trader not initialized in the database", service.addFunds(200));
	}

	@Test
	void testAddFunds() {
		trader.setEquities(new HashMap<>());
		Mockito.when(dao.traderDetails(1)).thenReturn(Optional.of(trader));

		assertEquals("Updated Trader details: \n"
				+ "Trader [id=1001, userName=Test, funds=1200.5<br/>Available Equities: <br/>No Equity available.",
				service.addFunds(200));
	}

	@Test
	void testAllEquities() {
		Mockito.when(dao.findAll()).thenReturn(equities);
		assertArrayEquals(equities.toArray(), service.allEquities().toArray());
	}

	@Test
	void testTraderDetailsFail() {
		assertEquals("Trader not initialized in the database", service.traderDetails());
	}

	@Test
	void testTraderDetails() {
		Mockito.when(dao.traderDetails(1)).thenReturn(Optional.of(trader));

		assertEquals("Trader [id=1001, userName=Test, funds=1000.5<br/>Available Equities: <br/><br/>",
				service.traderDetails());
	}

}
