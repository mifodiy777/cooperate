package com.cooperate.dao;

import com.cooperate.entity.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Класс по работе с базой данных через объект Cost
 */
@Repository
public interface CostDAO extends JpaRepository<Cost, Integer> {

}
