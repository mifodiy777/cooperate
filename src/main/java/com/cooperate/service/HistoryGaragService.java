package com.cooperate.service;

import com.cooperate.dao.HistoryGaragDAO;
import com.cooperate.entity.Garag;
import com.cooperate.entity.HistoryGarag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service
public class HistoryGaragService {

    @Autowired
    private HistoryGaragDAO historyGaragDAO;

    /**
     * Получение определенной записи о сменах владельца гаража
     * @param id ID Записи
     * @return запись истории
     */
    public HistoryGarag getHistoryGarag(Integer id) {
        return historyGaragDAO.getOne(id);
    }

    /**
     * Сохранение записи о причине смены владельца
     * @param reason Причина
     * @param fio ФИО
     * @param garag Гараж
     * @return запись истории о смене владельца
     */
    @Transactional
    public HistoryGarag saveReason(String reason, String fio, Garag garag) {
        return historyGaragDAO.save(new HistoryGarag(Calendar.getInstance(), fio, reason, garag));
    }

    /**
     * Удаление записи истории
     * @param id Запись
     */
    @Transactional
    public void delete(Integer id) {
        historyGaragDAO.delete(id);
    }
}
