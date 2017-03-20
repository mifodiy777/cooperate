package com.cooperate.service.impl;

import com.cooperate.dao.HistoryGaragDAO;
import com.cooperate.entity.Garag;
import com.cooperate.entity.HistoryGarag;
import com.cooperate.service.HistoryGaragService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service
public class HistoryGaragServiceImpl implements HistoryGaragService {

    @Autowired
    private HistoryGaragDAO historyGaragDAO;


    public HistoryGarag getHistoryGarag(Integer id) {
        return historyGaragDAO.getOne(id);
    }


    @Transactional
    public HistoryGarag saveReason(String reason, String fio, Garag garag) {
        return historyGaragDAO.save(new HistoryGarag(Calendar.getInstance(), fio, reason, garag));
    }


    @Transactional
    public void delete(Integer id) {
        historyGaragDAO.delete(id);
    }
}
