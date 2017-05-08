package com.cooperate.dao;

import com.cooperate.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface RentDAO  extends JpaRepository<Rent, Integer> {

    /**
     * Нахождение определенного начисления по году
     * @param year год
     * @return начисление определенного года
     */
    Rent findByYearRent(Integer year);

}
