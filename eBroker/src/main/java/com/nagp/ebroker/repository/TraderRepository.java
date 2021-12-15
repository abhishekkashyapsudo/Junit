package com.nagp.ebroker.repository;

import org.springframework.stereotype.Repository;

import com.nagp.ebroker.model.Trader;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface TraderRepository extends JpaRepository<Trader, Integer>{

}
