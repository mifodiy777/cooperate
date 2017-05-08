package com.cooperate.dao;

import com.cooperate.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PaymentDAO extends JpaRepository<Payment, Integer> {

    /**
     * Метод возвращает платежи с остаточными средствами для определенного гаража
     * @param id Идентификатор гаража
     * @return список платежей
     */
    @Query("select p from Payment p inner join p.garag g where p.pay <> 0 and g.id = :id ")
    List<Payment> getPaymentOnGarag(@Param("id") Integer id);

    /**
     * Последний номер платежа
     * @return номер
     */
    @Query("select max(p.number) from Payment p")
    Integer getMaxValueNumber();

    /**
     * Список годов для которых в базе существуют платежи
     * @return
     */
    @Query("select distinct p.year from Payment p")
    List<Integer> findYears();

    /**
     * Список платежей определенного года
     * @param year год
     * @return список платежей
     */
    List<Payment> findByYear(Integer year);

    /**
     * Список платежей в определенный промежуток времени
     * @param start начало периода
     * @param end конец периода
     * @return список платежей
     */
    @Query("select distinct p from Payment p where p.datePayment BETWEEN :start and :end")
    List<Payment> findByDateBetween(@Param("start") Calendar start, @Param("end") Calendar end);

}
