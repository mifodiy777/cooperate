package com.cooperate.dao;

import com.cooperate.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentDAO  extends JpaRepository<Rent, Integer> {

    //Кол-во начилсений определенного года
    Integer countByYearRent(Integer year);

    //Нахождение определенного начисления по году
    Rent findByYearRent(Integer year);
}
