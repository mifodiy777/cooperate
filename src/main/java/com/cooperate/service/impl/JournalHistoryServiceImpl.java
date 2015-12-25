package com.cooperate.service.impl;

import com.cooperate.dao.JournalHistoryDAO;
import com.cooperate.entity.JournalHistory;
import com.cooperate.service.JournalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.util.calendar.LocalGregorianCalendar;

import java.util.Calendar;
import java.util.List;

@Service
public class JournalHistoryServiceImpl implements JournalHistoryService {

    @Autowired
    JournalHistoryDAO historyDAO;

    @Override
    @Transactional
    public List<JournalHistory> getJournalHistorys() {
        return historyDAO.findAll();
    }

    @Override
    @Transactional
    public JournalHistory getJournalHistory(Integer id) {
        return historyDAO.getOne(id);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        historyDAO.delete(id);
    }

    //Запись события
    @Override
    @Transactional
    public void event(String event) {
        JournalHistory jh = new JournalHistory();
        jh.setDateEvent(Calendar.getInstance());
        jh.setEvented(event);
        historyDAO.save(jh);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 2);
        historyDAO.delete(historyDAO.getEventMoreTwoYear(cal));
    }
}
