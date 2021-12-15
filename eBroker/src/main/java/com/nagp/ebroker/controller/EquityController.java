package com.nagp.ebroker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagp.ebroker.model.Equity;
import com.nagp.ebroker.service.EquityService;

@RestController
@RequestMapping("/equity")
public class EquityController {
	
	@Autowired
	private EquityService equityService;

	@GetMapping("/buy")
	public String buyEquity(@RequestParam("id") int id, @RequestParam("quantity") int quantity) {
		return equityService.buy(id, quantity);
		
	}
	
	@GetMapping("/sell")
	public String sellEquity(@RequestParam("id") int id, @RequestParam("quantity") int quantity) {
		return equityService.sell(id, quantity);
		
	}
	
	@GetMapping("/addFunds")
	public String addFunds(@RequestParam("amount") double amount) {
		return equityService.addFunds(amount);
		
	}
	
	@GetMapping("/available")
	public List<Equity> available() {
		return equityService.allEquities();
	}
	
	@GetMapping("/trader")
	public String trader() {
		return equityService.traderDetails();
	}
}
