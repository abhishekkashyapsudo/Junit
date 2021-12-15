package com.nagp.ebroker.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import org.springframework.test.web.servlet.MockMvc;

import com.nagp.ebroker.model.Equity;
import com.nagp.ebroker.model.Trader;
import com.nagp.ebroker.repository.EquityRepository;
import com.nagp.ebroker.repository.TraderRepository;
import com.nagp.ebroker.util.DateHelper;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ValidationsIT {

	@Autowired
	private MockMvc mockMvc;

	private List<Equity> equities;
	private Trader trader;

	@Autowired
	private EquityRepository equityRepository;

	@Autowired
	private TraderRepository traderRepository;

	@BeforeEach
	public void setup() {

		equities = new ArrayList<>();
		equities.add(new Equity(0, "Test 1", 10));
		equities.add(new Equity(0, "Test 2", 20));
		equities.add(new Equity(0, "Test 3", 30));
		equities.add(new Equity(0, "Test 4", 40));
		equities.add(new Equity(0, "Test 5", 50));

		trader = new Trader(0, "Test", 1100.50);
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 10);
		map.put(2, 20);
		map.put(3, 30);
		map.put(4, 40);
		map.put(5, 50);
		trader.setEquities(map);
	}

	@Test
	void testSellAndPurchaseEquityDateAndTimeValidations() throws Exception {
		String[] dateStrings = { "2021-12-11 11:59", "2021-12-12 11:59", "2021-12-13 18:59", "2021-12-13 08:00" };
		String[] operations = { "buy", "sell" };
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		for (String operation : operations) {
			for (String dateStr : dateStrings) {
				LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
				Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
				LocalDate date = LocalDate.now(clock);
				LocalTime time = LocalTime.now(clock);

				try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {

					try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
						mockedDate.when(LocalDate::now).thenReturn(date);
						mockedTime.when(LocalTime::now).thenReturn(time);
						mockMvc.perform(get("/equity/" + operation).param("id", "" + equities.get(0).getId())
								.param("quantity", "10")).andExpect(status().isOk())
								.andExpect(content().string("Trade can only be carried in Monday-Friday 9-3"));

					}
				}
			}
		}

	}

	@Test
	void testSellAndPurchaseEquityTraderValidation() throws Exception {
		trader = traderRepository.save(trader);
		String dateStr = "2021-12-13 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String[] operations = { "buy", "sell" };

		for (String operation : operations) {
			LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
			Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
			LocalDate date = LocalDate.now(clock);
			LocalTime time = LocalTime.now(clock);

			try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {

				try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
					mockedDate.when(LocalDate::now).thenReturn(date);
					mockedTime.when(LocalTime::now).thenReturn(time);
					mockMvc.perform(get("/equity/" + operation).param("id", "877").param("quantity", "10"))
							.andExpect(status().isOk())
							.andExpect(content().string("No such equity found in the database"));

				}
			}
		}

	}

	@Test
	void testSellAndPurchaseEquityAmountValidation() throws Exception {
		trader = traderRepository.save(trader);
		equities = equityRepository.saveAll(equities);
		String dateStr = "2021-12-13 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String[] operations = { "buy", "sell" };

		for (String operation : operations) {
			LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
			Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
			LocalDate date = LocalDate.now(clock);
			LocalTime time = LocalTime.now(clock);

			try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {

				try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
					mockedDate.when(LocalDate::now).thenReturn(date);
					mockedTime.when(LocalTime::now).thenReturn(time);
					mockMvc.perform(get("/equity/" + operation).param("id", "" + equities.get(0).getId())
							.param("quantity", "-10")).andExpect(status().isOk())
							.andExpect(content().string("Quantity should be greater than 0"));

				}
			}
		}

	}

	@Test
	void testSellQuantityValidation() throws Exception {
		trader = traderRepository.save(trader);
		Equity equity = new Equity();
		equity.setId(0);
		equity.setName("Test Name");
		equity.setPrice(99.9);
		equities.add(equity);
		equities = equityRepository.saveAll(equities);
		String dateStr = "2021-12-13 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String[] operations = { "sell" };

		for (String operation : operations) {
			LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
			Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
			LocalDate date = LocalDate.now(clock);
			LocalTime time = LocalTime.now(clock);

			try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {

				try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
					mockedDate.when(LocalDate::now).thenReturn(date);
					mockedTime.when(LocalTime::now).thenReturn(time);
					mockMvc.perform(get("/equity/" + operation).param("id", "" + equities.get(1).getId())
							.param("quantity", "1000")).andExpect(status().isOk())
							.andExpect(content().string("Insufficient Equity available with the trader."));
					
					mockMvc.perform(get("/equity/" + operation).param("id", ""+equity.getId())
							.param("quantity", "1000")).andExpect(status().isOk())
							.andExpect(content().string("Equity not available with the trader."));

				}
			}
		}

	}

	@Test
	void testBuyInsufficientFundsValidation() throws Exception {
		trader = traderRepository.save(trader);
		equities = equityRepository.saveAll(equities);
		String dateStr = "2021-12-13 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String[] operations = { "buy" };

		for (String operation : operations) {
			LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
			Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
			LocalDate date = LocalDate.now(clock);
			LocalTime time = LocalTime.now(clock);

			try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {

				try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
					mockedDate.when(LocalDate::now).thenReturn(date);
					mockedTime.when(LocalTime::now).thenReturn(time);
					mockMvc.perform(get("/equity/" + operation).param("id", "" + equities.get(0).getId())
							.param("quantity", "1000")).andExpect(status().isOk())
							.andExpect(content().string("Insufficient Funds to buy this equity"));

				}
			}
		}

	}

	@Test
	void testInvalidTraderPurchaseSellEquityValidation() throws Exception {
		equities = equityRepository.saveAll(equities);
		String dateStr = "2021-12-13 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String[] operations = { "buy", "sell" };

		for (String operation : operations) {
			LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
			Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
			LocalDate date = LocalDate.now(clock);
			LocalTime time = LocalTime.now(clock);

			try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {

				try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
					mockedDate.when(LocalDate::now).thenReturn(date);
					mockedTime.when(LocalTime::now).thenReturn(time);
					mockMvc.perform(get("/equity/" + operation).param("id", "" + equities.get(0).getId())
							.param("quantity", "10")).andExpect(status().isOk())
							.andExpect(content().string("Trader not initialized in the database"));

				}
			}
		}

	}

	@Test
	void testAddFundsTraderValidation() throws Exception {

		String dateStr = "2021-12-13 11:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
		Clock clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
		LocalDate date = LocalDate.now(clock);
		LocalTime time = LocalTime.now(clock);

		try (MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {

			try (MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
				mockedDate.when(LocalDate::now).thenReturn(date);
				mockedTime.when(LocalTime::now).thenReturn(time);

				// trader validation
				mockMvc.perform(get("/equity/addFunds").param("amount", "10")).andExpect(status().isOk())
						.andExpect(content().string("Trader not initialized in the database"));

				trader = traderRepository.save(trader);

				// equity validation
				mockMvc.perform(get("/equity/addFunds").param("amount", "-10")).andExpect(status().isOk())
						.andExpect(content().string("Amount should be greater than 0"));
				// equity validation
				mockMvc.perform(get("/equity/addFunds").param("amount", "0")).andExpect(status().isOk())
						.andExpect(content().string("Amount should be greater than 0"));


			}
		}

	}

	@Test
	void testTraderDetailsValidation() throws Exception {
		// trader validation
		mockMvc.perform(get("/equity/trader").param("amount", "10")).andExpect(status().isOk())
				.andExpect(content().string("Trader not initialized in the database"));

		trader = traderRepository.save(trader);

	}
	
	

}
