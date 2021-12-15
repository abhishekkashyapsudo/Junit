package com.nagp.ebroker.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nagp.ebroker.model.Equity;
import com.nagp.ebroker.model.Trader;
import com.nagp.ebroker.repository.EquityRepository;
import com.nagp.ebroker.repository.TraderRepository;

@Repository
public class EquityDao {

	@Autowired
	private EquityRepository equityRepository;
	
	@Autowired
	private TraderRepository traderRepository;
	
	public List<Equity> findAll(){
		return equityRepository.findAll();
	}
	
	public Optional<Trader> traderDetails(int traderId){
		return traderRepository.findById(traderId);
		
	}

	public Optional<Equity> getEquity(int id) {
		return equityRepository.findById(id);
	}

	public void updateTrader(Trader trader) {
		traderRepository.save(trader);
	}

	public List<Equity> getEquities(Set<Integer> keySet) {
		return equityRepository.findAllById(keySet);
	}
}
