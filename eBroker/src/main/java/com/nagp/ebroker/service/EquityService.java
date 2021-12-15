package com.nagp.ebroker.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagp.ebroker.dao.EquityDao;
import com.nagp.ebroker.exception.EquityNotFoundException;
import com.nagp.ebroker.exception.TraderNotInitialisedException;
import com.nagp.ebroker.model.Equity;
import com.nagp.ebroker.model.Trader;
import com.nagp.ebroker.util.DateHelper;

@Service
public class EquityService {

	private static final String LINE_BREAK = "<br/>";
	private static final String UPDATED_TRADER_DETAILS = "Updated Trader details: \n";
	public static final int TRADER_ID = 1;
	@Autowired
	private EquityDao dao;

	public String buy(int id, int quantity) {
		if (!DateHelper.isValidDate()) {
			return "Trade can only be carried in Monday-Friday 9-3";
		} else {
			try {
				Trader trader = getTrader(TRADER_ID);
				Equity equity = getEquity(id);
				trader.purchaseEquity(equity, quantity);
				dao.updateTrader(trader);
				return UPDATED_TRADER_DETAILS + trader.toString() + equities(trader.getEquities());
			} catch (Exception e) {
				return e.getMessage();
			}
		}

	}

	private Equity getEquity(int id) throws EquityNotFoundException {
		Optional<Equity> equity = dao.getEquity(id);
		if (equity.isPresent()) {
			return equity.get();
		}
		throw new EquityNotFoundException();
	}

	private Trader getTrader(int traderId) throws TraderNotInitialisedException {
		Optional<Trader> trader = dao.traderDetails(traderId);
		if (trader.isPresent()) {
			return trader.get();
		}
		throw new TraderNotInitialisedException();
	}

	public String sell(int id, int quantity) {
		if (!DateHelper.isValidDate()) {
			return "Trade can only be carried in Monday-Friday 9-3";
		} else {
			try {
				Trader trader = getTrader(TRADER_ID);
				Equity equity = getEquity(id);
				trader.sellEquity(equity, quantity);
				dao.updateTrader(trader);
				return UPDATED_TRADER_DETAILS + trader.toString() + equities(trader.getEquities());
			} catch (Exception e) {
				return e.getMessage();
			}
		}
	}

	public String addFunds(double amount) {
		try {
			Trader trader = getTrader(TRADER_ID);
			trader.addFund(amount);
			dao.updateTrader(trader);
			return UPDATED_TRADER_DETAILS + trader.toString() + equities(trader.getEquities());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public List<Equity> allEquities() {
		return dao.findAll();
	}

	public String traderDetails() {
		try {
			Trader trader = getTrader(TRADER_ID);
			return trader.toString() + equities(trader.getEquities());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	private String equities(Map<Integer, Integer> equities) {
		StringBuilder sb = new StringBuilder();
		sb.append("Available Equities: ").append(LINE_BREAK);
		if (equities.isEmpty()) {
			sb.append("No Equity available.");
			return sb.toString();
		}
		List<Equity> eqts = dao.getEquities(equities.keySet());
		eqts.forEach(e -> sb.append(e).append("     ===> ").append(equities.get(e.getId())).append(LINE_BREAK));
		sb.append(LINE_BREAK);
		return sb.toString();
	}
}
