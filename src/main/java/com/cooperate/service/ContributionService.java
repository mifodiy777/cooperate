package com.cooperate.service;

import com.cooperate.controller.ContributionController;
import com.cooperate.dao.ContributionDAO;
import com.cooperate.dao.RentDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Rent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 * Сервис для работы с периодами долгов(Contribution).
 */
@Service
public class ContributionService {

    @Autowired
    private ContributionDAO contributionDAO;

    @Autowired
    private RentDAO rentDAO;

    private final Logger logger = Logger.getLogger(ContributionService.class);

    /**
     * Метод сохранения долгов определенного года
     *
     * @param contribution период долга
     */
    @Transactional
    public void saveOrUpdate(Contribution contribution) {
        contributionDAO.saveAndFlush(contribution);
    }

    /**
     * Получение определенного долгового периода для определенного гаража.
     *
     * @param garagId Id гаража
     * @param year    год долгового периода
     * @return долговой период
     */
    public Contribution getContributionByGaragAndYear(Integer garagId, Integer year) {
        Contribution contribution = contributionDAO.getContributionByGaragAndYear(garagId, year);
        if (contribution == null) {
            contribution = new Contribution();
            contribution.setYear(year);
        }
        return contribution;
    }

    /**
     * Метод обновления пеней(fines) для всех гаражей с включенным режимом начисления пеней(finesOn)
     */
    @Transactional
    public void updateFines() {
        Calendar calendar = Calendar.getInstance();// текущая дата
        //Получаем список долгов с включенным режимом пени
        for (Contribution c : contributionDAO.findByFinesOn(true)) {
            Float sumContribute = c.getSumFixed(); //Находим сумму долга
            //Вычисляем кол-во дней с последнего обновления.
            //Первая дата должна устанавливаться при включении режима пеней
            if (sumContribute != 0) {
                try {
                    long days = getDays(calendar, c.getFinesLastUpdate());
                    Double finesDouble = (sumContribute * 0.001) * days;
                    int fines = (finesDouble.intValue() / 50);
                    fines *= 50;
                    //Вычисляем сумму пени
                    if (fines != 0) {
                        int difference = (finesDouble.intValue() % 50);
                        float unit = (float) (sumContribute * 0.001);
                        difference = (difference > 0) ? Math.round(difference / unit) : 0;
                        difference = calendar.get(Calendar.DAY_OF_YEAR) - difference;
                        if (difference >= 0 && calendar.get(Calendar.DAY_OF_YEAR) > difference) {
                            calendar.set(Calendar.DAY_OF_YEAR, difference);
                        }
                        c.setFinesLastUpdate(calendar);
                        int newFines = c.getFines() + fines;
                        if (newFines < sumContribute) {
                            c.setFines(newFines);
                        } else if (newFines == sumContribute) {
                            c.setFines(newFines);
                            c.setFinesOn(false);
                        } else {
                            //Если новые пени больши суммы долго определяем сумму начислений этого года и отключаем пени
                            c.setFinesOn(false);
                            if (c.getFines() == 0) {
                                c.setFines(sumContribute.intValue());
                            }
                        }

                    }
                } catch (ArithmeticException e) {
                    logger.error("Арифметическая ошибка для contribution_id=" + c.getId());
                }
            } else {
                c.setFinesOn(false); // если сумма долга равна 0, то режим начисления пеней выключается.
            }
            contributionDAO.save(c); //сохраняем долговой период
        }
    }

    /**
     * Метод вычисленеия количества дней с даты последнего обновления пеней
     */
    private long getDays(Calendar calendar, Calendar lastUpdate) {
        long today = calendar.getTimeInMillis();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, lastUpdate.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, lastUpdate.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, lastUpdate.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long days = cal.getTimeInMillis();
        return (today - days) / (24 * 60 * 60 * 1000);
    }

    /**
     * Метод включения режима начисления пеней
     *
     * @param now Текущая дата
     */
    public void onFines(Calendar now) {
        //Включение пеней для должников со следующего года
        if (now.get(Calendar.MONTH) == 0) {
            for (Contribution c : contributionDAO.findByFinesOnAndYear(false, now.get(Calendar.YEAR) - 1)) {
                if (c.getSumFixed() != 0) {
                    c.setFinesOn(true);
                    c.setFinesLastUpdate(now);
                    contributionDAO.save(c);
                }
            }
        }
        //Включение пеней для должников не уплативших до 1 июля
        if (now.get(Calendar.MONTH) == Calendar.JULY) {
            Rent rent = rentDAO.findByYearRent(now.get(Calendar.YEAR));
            for (Contribution c : contributionDAO.findByFinesOnAndYear(false, now.get(Calendar.YEAR))) {
                Integer rentMax = 0;
                if (!c.isMemberBoardOn()) {
                    rentMax += Math.round(rent.getContributeMax());
                }
                if (c.isBenefitsOn()) {
                    rentMax += Math.round(rent.getContLandMax()) / 2;
                } else {
                    rentMax += Math.round(rent.getContLandMax());
                }
                rentMax += Math.round(rent.getContTargetMax());
                if (rentMax.equals(c.getSumFixed().intValue())) {
                    c.setFinesOn(true);
                    c.setFinesLastUpdate(now);
                    contributionDAO.save(c);
                }

            }
        }
    }
}
