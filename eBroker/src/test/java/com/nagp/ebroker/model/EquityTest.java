package com.nagp.ebroker.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EquityTest {

	private Equity equity;
	@BeforeEach
	public void setup() {
		equity = new Equity();
		equity.setId(1001);
		equity.setName("Test");
		equity.setPrice(10);
	}
	@Test
	void testGetId() {
		assertEquals(1001, equity.getId());
	}

	@Test
	void testSetId() {
		assertEquals(1001, equity.getId());
		equity.setId(1002);
		assertEquals(1002, equity.getId());
	}

	@Test
	void testGetName() {
		assertEquals("Test", equity.getName());
	}

	@Test
	void testSetName() {
		assertEquals("Test", equity.getName());
		equity.setName("Test name");
		assertEquals("Test name", equity.getName());
	}

	@Test
	void testGetPrice() {
		assertEquals(10, equity.getPrice());
	}

	@Test
	void testSetPrice() {
		assertEquals(10, equity.getPrice());
		equity.setPrice(11);
		assertEquals(11, equity.getPrice());
	}

	@Test
	void testToString() {
		assertEquals("Equity [id=1001, name=Test, price=10.0]", equity.toString());
	}

}
