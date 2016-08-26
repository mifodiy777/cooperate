package com.cooperate.dao;

import com.cooperate.entity.HistoryGarag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryGaragDAO extends JpaRepository<HistoryGarag, Integer> {   

}