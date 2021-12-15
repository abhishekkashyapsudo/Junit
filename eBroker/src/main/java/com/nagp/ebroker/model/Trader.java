package com.nagp.ebroker.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import com.nagp.ebroker.exception.EquityNotAvailableException;
import com.nagp.ebroker.exception.InsufficientEquityAvailableException;
import com.nagp.ebroker.exception.InsufficientFundsException;
import com.nagp.ebroker.exception.InvalidQuantityException;

@Entity
@Table(name = "TBL_TRADER")
public class Trader {

	@Id
	@GeneratedValue
	private int id;

	@Column
	private String userName;
	@Column
	private double funds;

	
	public Trader(int id, String userName, double funds) {
		super();
		this.id = id;
		this.userName = userName;
		this.funds = funds;
	}
	
	public Trader() {
		
	}

	@ElementCollection(targetClass = Integer.class)
	@MapKeyColumn(name = "user_name")
	@Column(name = "quantity")
	private Map<Integer, Integer> equities;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getFunds() {
		return funds;
	}

	public void setFunds(double funds) {
		this.funds = funds;
	}

	public Map<Integer, Integer> getEquities() {
		return equities;
	}

	public void setEquities(Map<Integer, Integer> equities) {
		this.equities = equities;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean hasEquity(int id) {
		return equities.containsKey(id);
	}

	public void purchaseEquity(Equity equity, int quantity)
			throws InsufficientFundsException, InvalidQuantityException {

		
		int equityId = equity.getId();

		double purchaseAmount = equity.getPrice() * quantity;
		validatePurchasingEquity(quantity, purchaseAmount);

		if (hasEquity(equityId)) {
			equities.put(equityId, equities.get(equityId) + quantity);
		} else {
			equities.put(equityId, quantity);
		}
		funds -= purchaseAmount;

	}

	private void validatePurchasingEquity(int quantity, double purchaseAmount)
			throws InvalidQuantityException, InsufficientFundsException {
		if (quantity <= 0) {
			throw new InvalidQuantityException();
		}
		if (!hasEnoughFunds(purchaseAmount)) {
			throw new InsufficientFundsException();
		}
	}

	private boolean hasEnoughFunds(double amount) {
		return amount <= funds;
	}

	
	public void sellEquity(Equity equity, int quantity)
			throws EquityNotAvailableException, InvalidQuantityException, InsufficientEquityAvailableException {
		int equityId = equity.getId();

		validateSellingEquity(quantity, equityId);

		if (equities.get(equityId) == quantity) {
			equities.remove(equityId);
		}
		else {
			equities.put(equityId, equities.get(equityId) - quantity);
		}
		double sellingAmount = equity.getPrice() * quantity;
		funds = funds + sellingAmount ;
	}

	private void validateSellingEquity(int quantity, int id)
			throws InvalidQuantityException, EquityNotAvailableException, InsufficientEquityAvailableException {
		if (quantity <= 0) {
			throw new InvalidQuantityException();
		}
		if (!hasEquity(id)) {
			throw new EquityNotAvailableException();
		}

		if (equities.get(id) < quantity) {
			throw new InsufficientEquityAvailableException();
		}
	}


	public void addFund(double amount) {
		if(amount <= 0) {
			throw new RuntimeException("Amount should be greater than 0");
		}
		funds += amount;
	}

	@Override
	public String toString() {
		return "Trader [id=" + id + ", userName=" + userName + ", funds=" + funds + "<br/>";
	}

}
