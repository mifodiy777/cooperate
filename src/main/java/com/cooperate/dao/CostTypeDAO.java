package com.cooperate.dao;

import com.cooperate.entity.CostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Класс по работе с базой данных через объект CostType
 */
@Repository
public interface CostTypeDAO extends JpaRepository<CostType, Integer> {

}
