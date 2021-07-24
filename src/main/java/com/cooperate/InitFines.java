package com.cooperate;

import com.cooperate.service.ContributionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Calendar;

/**
 * Класс обновления пеней при запуске приложения
 */
public class InitFines {

    private final Logger logger = Logger.getLogger(InitFines.class);

    @Autowired
    private ContributionService service;

    /**
     * При инициализации контекста запускаетс метод обновления пеней
     */
    public void initFines(){
        try {
            service.onFines(Calendar.getInstance());
            service.updateFines();
            logger.info("Обновление данных произведено");
        } catch (DataIntegrityViolationException e) {
            logger.error(e);
        }
    }
}
