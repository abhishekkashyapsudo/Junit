package com.nagp.ebroker.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.nagp.ebroker.model.Equity;
import com.nagp.ebroker.model.Trader;
import com.nagp.ebroker.service.EquityService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EquityController.class)
class EquityControllerTest {

	private static Trader trader;
	@MockBean
	private EquityService service;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@BeforeAll
	public static void setupSpec() {
		trader = new Trader(1001, "test", 10000);

		Map<Integer, Integer> equities = new HashMap<>();
		equities.put(1, 10);
		equities.put(2, 20);
		equities.put(3, 30);
		equities.put(4, 40);
		equities.put(5, 50);
		trader.setEquities(equities);
	}
	@Test
	void testBuyEquity() throws Exception {
		String result = trader.toString();
		Mockito.when(service.buy(1, 10)).thenReturn(result);
		
		mockMvc.perform(get("/equity/buy")
		.param("id","1")
		.param("quantity", "10"))
		.andExpect(status().isOk())
        .andExpect(content().string("Trader [id=1001, userName=test, funds=10000.0<br/>"));

	}

	@Test
	void testSellEquity() throws Exception {
		String result = trader.toString();
		Mockito.when(service.sell(1, 10)).thenReturn(result);
		
		mockMvc.perform(get("/equity/sell")
		.param("id","1")
		.param("quantity", "10"))
		.andExpect(status().isOk())
        .andExpect(content().string("Trader [id=1001, userName=test, funds=10000.0<br/>"));
	}

	@Test
	void testAddFunds() throws Exception {
		String result = trader.toString();
		Mockito.when(service.addFunds(1000)).thenReturn(result);
		
		mockMvc.perform(get("/equity/addFunds")
		.param("amount","1000"))
		.andExpect(status().isOk())
        .andExpect(content().string("Trader [id=1001, userName=test, funds=10000.0<br/>"));
	}

	@Test
	void testAvailable() throws Exception {
		Equity equity1 = new Equity(1, "Test1", 1000);
		Equity equity2 = new Equity(2, "Test2", 2000);
		List<Equity> equities = new ArrayList<>();
		equities.add(equity1);
		equities.add(equity2);
		
		Mockito.when(service.allEquities()).thenReturn(equities);
		
		mockMvc.perform(get("/equity/available"))
		.andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(2)))
        .andExpect(jsonPath("$[0].id", Matchers.is(1)))
        .andExpect(jsonPath("$[0].name", Matchers.is("Test1")))
        .andExpect(jsonPath("$[0].price", Matchers.is(1000.0)))
        .andExpect(jsonPath("$[1].id", Matchers.is(2)))
        .andExpect(jsonPath("$[1].name", Matchers.is("Test2")))
        .andExpect(jsonPath("$[1].price",  Matchers.is(2000.0)));
	
	}

	@Test
	void testTrader() throws Exception {
		String result = trader.toString();
		Mockito.when(service.traderDetails()).thenReturn(result);
		
		mockMvc.perform(get("/equity/trader"))
		.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string("Trader [id=1001, userName=test, funds=10000.0<br/>"));
	}

}
