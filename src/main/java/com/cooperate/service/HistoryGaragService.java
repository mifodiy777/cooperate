package com.cooperate.service;

import com.cooperate.entity.Garag;
import com.cooperate.entity.HistoryGarag;

public interface HistoryGaragService {

    public HistoryGarag getHistoryGarag(Integer id);

    public HistoryGarag saveReason(String reason, String fio, Garag garag);

    public void delete(Integer id);


}
