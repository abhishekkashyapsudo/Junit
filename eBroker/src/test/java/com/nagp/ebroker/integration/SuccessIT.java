package com.nagp.ebroker.integration;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import org.hamcrest.Matchers;
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

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class SuccessIT {

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

		trader = new Trader();
		trader.setId(0);
		trader.setUserName("Test");
		trader.setFunds(1100.50);
		Map<Integer, Integer> map = new HashMap<>();
		map.put(2, 10);
		map.put(3, 20);
		map.put(4, 30);
		map.put(5, 40);
		map.put(6, 50);
		trader.setEquities(map);

		trader = traderRepository.save(trader);
		equities = equityRepository.saveAll(equities);

	}

	@Test
	public void testSell() throws Exception {
		assertEquals(1, trader.getId());
		assertEquals(1100.50, trader.getFunds());
		assertEquals("Test", trader.getUserName());
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
				mockMvc.perform(get("/equity/sell").param("id", "" + equities.get(0).getId()).param("quantity", "5"))
						.andExpect(status().isOk())
						.andExpect(content().string("Updated Trader details: \n"
								+ "Trader [id=1, userName=Test, funds=1150.5<br/>Available Equities: <br/>Equity [id=2, name=Test 1, price=10.0]     ===> 5<br/>Equity [id=3, name=Test 2, price=20.0]     ===> 20<br/>Equity [id=4, name=Test 3, price=30.0]     ===> 30<br/>Equity [id=5, name=Test 4, price=40.0]     ===> 40<br/>Equity [id=6, name=Test 5, price=50.0]     ===> 50<br/><br/>"
								+ ""));
				mockMvc.perform(get("/equity/sell").param("id", "" + equities.get(0).getId()).param("quantity", "5"))
						.andExpect(status().isOk()).andExpect(content().string("Updated Trader details: \n"
								+ "Trader [id=1, userName=Test, funds=1200.5<br/>Available Equities: <br/>Equity [id=3, name=Test 2, price=20.0]     ===> 20<br/>Equity [id=4, name=Test 3, price=30.0]     ===> 30<br/>Equity [id=5, name=Test 4, price=40.0]     ===> 40<br/>Equity [id=6, name=Test 5, price=50.0]     ===> 50<br/><br/>"));

			}
		}

	}

	@Test
	public void testBuy() throws Exception {
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
				assertEquals("Test 1", equities.get(0).getName());
				mockMvc.perform(get("/equity/buy").param("id", "" + equities.get(0).getId()).param("quantity", "5"))
						.andExpect(status().isOk())
						.andExpect(content().string("Updated Trader details: \n"
								+ "Trader [id=1, userName=Test, funds=1050.5<br/>Available Equities: <br/>Equity [id=2, name=Test 1, price=10.0]     ===> 15<br/>Equity [id=3, name=Test 2, price=20.0]     ===> 20<br/>Equity [id=4, name=Test 3, price=30.0]     ===> 30<br/>Equity [id=5, name=Test 4, price=40.0]     ===> 40<br/>Equity [id=6, name=Test 5, price=50.0]     ===> 50<br/><br/>"
								+ ""));
				
				Equity equity = new Equity(0, "Test new", 100);
				equity = equityRepository.save(equity);
				mockMvc.perform(get("/equity/buy").param("id", "" + equity.getId()).param("quantity", "5"))
				.andExpect(status().isOk())
				.andExpect(content().string("Updated Trader details: \n"
						+ "Trader [id=1, userName=Test, funds=550.5<br/>Available Equities: <br/>Equity [id=2, name=Test 1, price=10.0]     ===> 15<br/>Equity [id=3, name=Test 2, price=20.0]     ===> 20<br/>Equity [id=4, name=Test 3, price=30.0]     ===> 30<br/>Equity [id=5, name=Test 4, price=40.0]     ===> 40<br/>Equity [id=6, name=Test 5, price=50.0]     ===> 50<br/>Equity [id=7, name=Test new, price=100.0]     ===> 5<br/><br/>"
						+ ""));

				
			}
		}

	}

	@Test
	public void testAddFunds() throws Exception {
		assertEquals("Test 1", equities.get(0).getName());
		mockMvc.perform(get("/equity/addFunds").param("amount", "1000")).andExpect(status().isOk())
				.andExpect(content().string("Updated Trader details: \n"
						+ "Trader [id=1, userName=Test, funds=2100.5<br/>Available Equities: <br/>Equity [id=2, name=Test 1, price=10.0]     ===> 10<br/>Equity [id=3, name=Test 2, price=20.0]     ===> 20<br/>Equity [id=4, name=Test 3, price=30.0]     ===> 30<br/>Equity [id=5, name=Test 4, price=40.0]     ===> 40<br/>Equity [id=6, name=Test 5, price=50.0]     ===> 50<br/><br/>"
						+ ""));

	}
	
	@Test
	public void testEquities() throws Exception {
		mockMvc.perform(get("/equity/available"))
		.andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(5)))
        .andExpect(jsonPath("$[0].id", Matchers.is(2)))
        .andExpect(jsonPath("$[0].name", Matchers.is("Test 1")))
        .andExpect(jsonPath("$[0].price", Matchers.is(10.0)))
        .andExpect(jsonPath("$[1].id", Matchers.is(3)))
        .andExpect(jsonPath("$[1].name", Matchers.is("Test 2")))
        .andExpect(jsonPath("$[1].price",  Matchers.is(20.0)))
		.andExpect(jsonPath("$[2].id", Matchers.is(4)))
        .andExpect(jsonPath("$[2].name", Matchers.is("Test 3")))
        .andExpect(jsonPath("$[2].price",  Matchers.is(30.0)))
		.andExpect(jsonPath("$[3].id", Matchers.is(5)))
        .andExpect(jsonPath("$[3].name", Matchers.is("Test 4")))
        .andExpect(jsonPath("$[3].price",  Matchers.is(40.0)))
		.andExpect(jsonPath("$[4].id", Matchers.is(6)))
        .andExpect(jsonPath("$[4].name", Matchers.is("Test 5")))
        .andExpect(jsonPath("$[4].price",  Matchers.is(50.0)));

	}
	
	@Test
	public void testTrader() throws Exception {
		mockMvc.perform(get("/equity/trader"))
		.andExpect(status().isOk())
		.andExpect(content().string( "Trader [id=1, userName=Test, funds=1100.5<br/>Available Equities: <br/>Equity [id=2, name=Test 1, price=10.0]     ===> 10<br/>Equity [id=3, name=Test 2, price=20.0]     ===> 20<br/>Equity [id=4, name=Test 3, price=30.0]     ===> 30<br/>Equity [id=5, name=Test 4, price=40.0]     ===> 40<br/>Equity [id=6, name=Test 5, price=50.0]     ===> 50<br/><br/>"
				+ ""));
		
		trader.setEquities(new HashMap<>());
		trader = traderRepository.save(trader);
		mockMvc.perform(get("/equity/trader"))
		.andExpect(status().isOk())
		.andExpect(content().string("Trader [id=1, userName=Test, funds=1100.5<br/>Available Equities: <br/>No Equity available."));

	}

}
