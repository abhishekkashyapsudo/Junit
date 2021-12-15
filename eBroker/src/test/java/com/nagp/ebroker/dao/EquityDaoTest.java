package com.nagp.ebroker.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.nagp.ebroker.EBrokerApplication;
import com.nagp.ebroker.model.Equity;
import com.nagp.ebroker.model.Trader;
import com.nagp.ebroker.repository.EquityRepository;
import com.nagp.ebroker.repository.TraderRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EBrokerApplication.class,
		EquityDao.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)

class EquityDaoTest {

	@Autowired
	private EquityRepository equityRepository;
	
	@Autowired
	private TraderRepository traderRepository;
	
	@Autowired
	private EquityDao dao;
	
	private List<Equity> equities;
	private Trader trader;
	
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
		map.put(21, 10);
		map.put(22, 20);
		map.put(23, 30);
		map.put(24, 40);
		map.put(25, 50);
		trader.setEquities(map);
		
		equityRepository.saveAll(equities);
		trader = traderRepository.save(trader);
		
	}
	
	@Test
	void testFindAll() {
		assertArrayEquals(equities.toArray(), dao.findAll().toArray());
		
	}

	@Test
	void testTraderDetails() {
		assertEquals(trader, dao.traderDetails(trader.getId()).get());
	}

	@Test
	void testGetEquity() {
		equities.forEach(e -> 
			assertEquals(e, dao.getEquity(e.getId()).get()));
	}

	@Test
	void testUpdateTrader() {
		assertEquals(trader, dao.traderDetails(trader.getId()).get());
		trader.setFunds(10000);
		dao.updateTrader(trader);
		assertEquals(trader, dao.traderDetails(trader.getId()).get());
	}

	@Test
	void testGetEquities() {
		Set<Integer> set = new HashSet<>();
		set.add(equities.get(0).getId());
		set.add(equities.get(2).getId());
		set.add(equities.get(4).getId());
		
		List<Equity> equities = dao.getEquities(set);
		assertEquals(3, equities.size());
		equities.forEach(e -> set.contains(e.getId()) );
			
	}
	
	

}
