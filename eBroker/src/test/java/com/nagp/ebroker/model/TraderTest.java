package com.nagp.ebroker.model;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagp.ebroker.exception.EquityNotAvailableException;
import com.nagp.ebroker.exception.InsufficientEquityAvailableException;
import com.nagp.ebroker.exception.InsufficientFundsException;
import com.nagp.ebroker.exception.InvalidQuantityException;

class TraderTest {

	private Trader trader;
	private Equity equity;
	
	@BeforeEach
	public void setup() {
		equity = new Equity();
		equity.setId(1);
		equity.setName("Test");
		equity.setPrice(10);
		trader = new Trader();
		trader.setId(1001);
		trader.setUserName("Test");
		trader.setFunds(1100.50);
		Map<Integer, Integer> equities = new HashMap<>();
		equities.put(1, 10);
		equities.put(2, 20);
		equities.put(3, 30);
		equities.put(4, 40);
		equities.put(5, 50);
		trader.setEquities(equities);
		trader.setEquities(equities);
	}
	
	@Test
	void testGetUserName() {
		assertEquals("Test", trader.getUserName());
	}

	@Test
	void testSetUserName() {
		assertEquals("Test", trader.getUserName());
		trader.setUserName("Test Username");
		assertEquals("Test Username", trader.getUserName());
	}

	@Test
	void testGetFunds() {
		assertEquals(1100.50, trader.getFunds());
	}

	@Test
	void testSetFunds() {
		assertEquals(1100.50, trader.getFunds());
		trader.setFunds(1110.50);
		assertEquals(1110.50, trader.getFunds());
	}

	@Test
	void testGetEquities() {
		Map<Integer, Integer> equities = new HashMap<>();
		equities.put(1, 10);
		equities.put(2, 20);
		equities.put(3, 30);
		equities.put(4, 40);
		equities.put(5, 50);
		assertArrayEquals(equities.entrySet().toArray(), trader.getEquities().entrySet().toArray());
	}

	@Test
	void testSetEquities() {
		Map<Integer, Integer> equities = new HashMap<>();
		equities.put(13, 10);
		equities.put(2, 20);
		equities.put(3, 305);
		equities.put(44, 40);
		equities.put(54, 50);
		trader.setEquities(equities);
		assertArrayEquals(equities.entrySet().toArray(), trader.getEquities().entrySet().toArray());

	}

	@Test
	void testGetId() {
		assertEquals(1001, trader.getId());
	}

	@Test
	void testSetId() {
		assertEquals(1001, trader.getId());
		trader.setId(1021);
		assertEquals(1021, trader.getId());
	}

	@Test
	void testHasEquity() {
		assertTrue(trader.hasEquity(2));
		assertFalse(trader.hasEquity(22));
	}

	@Test
	@DisplayName("Test validations for purchaseEquity works fine.")
	void testPurchaseEquityValidations() {
		
		InvalidQuantityException exception = assertThrows(InvalidQuantityException.class, () -> {
			trader.purchaseEquity(equity, 0);
		});
		assertEquals("Quantity should be greater than 0", exception.getMessage());
		
		InsufficientFundsException insuException  = assertThrows(InsufficientFundsException.class, () -> {
			trader.purchaseEquity(equity, 110000);
		});
		assertEquals("Insufficient Funds to buy this equity", insuException.getMessage());	
	}
	
	@Test
	void testPurchaseEquity() throws InsufficientFundsException, InvalidQuantityException {
		
		trader.purchaseEquity(equity, 5);	
		assertEquals(15, trader.getEquities().get(1));
		assertEquals(1050.5, trader.getFunds());
		trader.purchaseEquity(equity, 80);	
		assertEquals(95, trader.getEquities().get(1));
		assertEquals(250.5, trader.getFunds());
		equity.setId(210);
		trader.purchaseEquity(equity, 5);	
		assertEquals(5, trader.getEquities().get(210));
		assertEquals(200.5, trader.getFunds());
		
	}
	
	@Test
	@DisplayName("Test validations for sellEquity works fine.")
	void testSellEquityValidations() {
		
		InvalidQuantityException exception = assertThrows(InvalidQuantityException.class, () -> {
			trader.sellEquity(equity, 0);
		});
		assertEquals("Quantity should be greater than 0", exception.getMessage());
		
		InsufficientEquityAvailableException insuException  = assertThrows(InsufficientEquityAvailableException.class, () -> {
			trader.sellEquity(equity, 11);
		});
		assertEquals("Insufficient Equity available with the trader.", insuException.getMessage());
		
		equity.setId(333);
		EquityNotAvailableException notAvailableException  = assertThrows(EquityNotAvailableException.class, () -> {
			trader.sellEquity(equity, 5);
		});
		assertEquals("Equity not available with the trader.", notAvailableException.getMessage());
	
	}
	
	@Test
	void testSellEquity() throws EquityNotAvailableException, InvalidQuantityException, InsufficientEquityAvailableException {
		
		trader.sellEquity(equity, 5);	
		assertEquals(5, trader.getEquities().get(1));
		assertEquals(1150.5, trader.getFunds());
		trader.sellEquity(equity, 5);	
		assertFalse(trader.getEquities().containsKey(1));
		assertEquals(1200.5, trader.getFunds());
		
	}

	@Test
	void testAddFund() {
		trader.addFund(200000);
		assertEquals(201100.50, trader.getFunds());
		
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			trader.addFund(0);
		});
		assertEquals("Amount should be greater than 0", thrown.getMessage());
	}

	@Test
	void testToString() {
		assertEquals("Trader [id=1001, userName=Test, funds=1100.5<br/>", trader.toString());
	}

}
