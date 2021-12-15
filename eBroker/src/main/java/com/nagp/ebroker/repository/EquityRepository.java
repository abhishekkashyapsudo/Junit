package com.nagp.ebroker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagp.ebroker.model.Equity;

@Repository
public interface EquityRepository extends JpaRepository<Equity, Integer>{

}
