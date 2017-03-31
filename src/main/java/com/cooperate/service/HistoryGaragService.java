package com.cooperate.service;

import com.cooperate.entity.Garag;
import com.cooperate.entity.HistoryGarag;

public interface HistoryGaragService {

    HistoryGarag getHistoryGarag(Integer id);

    HistoryGarag saveReason(String reason, String fio, Garag garag);

    void delete(Integer id);


}
