package com.cooperate.dao;

import com.cooperate.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface PaymentDAO extends JpaRepository<Payment, Integer> {

    //Метод возвращает не нулевой платеж для определенного гаража
    @Query("select p from Payment p inner join p.garag g where p.pay <> 0 and g.id = :id ")
    Payment getPaymentOnGarag(@Param("id") Integer id);

    @Query("select max(p.number) from Payment p")
    Integer getMaxValueNumber();

    @Query("select distinct p.year from Payment p")
    List<Integer> findYears();

    List<Payment> findByYear(Integer year);

}
