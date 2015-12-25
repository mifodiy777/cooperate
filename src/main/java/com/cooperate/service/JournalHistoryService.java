package com.cooperate.service;

import com.cooperate.entity.JournalHistory;

import java.util.List;

public interface JournalHistoryService {

    List<JournalHistory> getJournalHistorys();

    JournalHistory getJournalHistory(Integer id);

    void delete(Integer id);

    void event(String event);
}
