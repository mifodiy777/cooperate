package com.cooperate.service;

import com.cooperate.dao.HistoryGaragDAO;
import com.cooperate.entity.Garag;
import com.cooperate.entity.HistoryGarag;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

/**
 * Тестирование сервиса - история гаража
 * Created by Кирилл on 22.03.2017.
 */
public class HistoryGaragServiceTest {

    @InjectMocks
    private HistoryGaragService historyGaragService = new HistoryGaragService();

    @Mock
    private HistoryGaragDAO historyGaragDAO;

    @BeforeClass
    public void setUp() {
        initMocks(this);
    }

    @BeforeMethod
    public void init() {
        reset(historyGaragDAO);
    }

    /**
     * Получение определенной истории по идентификатору
     * @throws Exception
     */
    @Test
    public void testGetHistoryGarag() throws Exception {
        historyGaragService.getHistoryGarag(100);
        verify(historyGaragDAO).getOne(100);
    }

    /**
     * Сохранении истории
     * @throws Exception
     */
    @Test
    public void testSaveReason() throws Exception {
        HistoryGarag historyGarag = new HistoryGarag();
        given(historyGaragDAO.save(any(HistoryGarag.class))).willReturn(historyGarag);
        assertEquals(historyGaragService.saveReason("reason", "fio", new Garag()), historyGarag);
    }

    /**
     * Удаление истории
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        historyGaragService.delete(100);
        verify(historyGaragDAO).delete(100);
    }

}