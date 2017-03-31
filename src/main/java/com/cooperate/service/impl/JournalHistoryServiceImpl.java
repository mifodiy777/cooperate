package com.cooperate.service.impl;

import com.cooperate.dao.JournalHistoryDAO;
import com.cooperate.entity.JournalHistory;
import com.cooperate.service.JournalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class JournalHistoryServiceImpl implements JournalHistoryService {

    @Autowired
    private JournalHistoryDAO journalHistoryDAO;

    @Override
    public List<JournalHistory> getJournalHistorys() {
        return journalHistoryDAO.findAll();
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        journalHistoryDAO.delete(id);
    }

    //Запись события
    @Override
    @Transactional
    public void event(String event) {
        JournalHistory jh = new JournalHistory();
        jh.setDateEvent(Calendar.getInstance());
        jh.setEvented(event);
        journalHistoryDAO.save(jh);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
        journalHistoryDAO.delete(journalHistoryDAO.getEventMoreTwoYear(cal));
    }
}
