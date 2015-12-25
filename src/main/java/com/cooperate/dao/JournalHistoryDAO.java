package com.cooperate.dao;

import com.cooperate.entity.JournalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface JournalHistoryDAO extends JpaRepository<JournalHistory, Integer> {

    //Метод возвращает записи в журнале старше выбранной даты
    @Query("select h from JournalHistory h where h.dateEvent <= ?1 ")
    Iterable <JournalHistory> getEventMoreTwoYear(Calendar calendar);

}
