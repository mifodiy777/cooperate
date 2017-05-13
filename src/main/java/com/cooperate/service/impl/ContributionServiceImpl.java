package com.cooperate.service.impl;

import com.cooperate.dao.ContributionDAO;
import com.cooperate.dao.RentDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Rent;
import com.cooperate.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service
public class ContributionServiceImpl implements ContributionService {

    @Autowired
    private ContributionDAO contributionDAO;

    @Autowired
    private RentDAO rentDAO;


    @Transactional
    public void saveOrUpdate(Contribution contribution) {
        contributionDAO.save(contribution);
    }


    public Contribution getContributionByGaragAndYear(Integer garagId, Integer year) {
        return contributionDAO.getContributionByGaragAndYear(garagId, year);
    }

    //Вычисление пени

    @Transactional
    public void updateFines() {
        Calendar calendar = Calendar.getInstance();
        //Получаем список долгов с включенным режимом пени
        for (Contribution c : contributionDAO.findByFinesOn(true)) {
            //Находим сумму долга
            Float sumContribute = c.getSumFixed();
            //ВЫчисляем кол-во дней с последнего обновления.
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
                    e.printStackTrace();
                }
            } else {
                c.setFinesOn(false);
            }
            contributionDAO.save(c);
        }
    }

    //Метод вычисленеия дней с последнего обновления пеней
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


    public void onFines() {
        Calendar cal = Calendar.getInstance();
        //Включение пеней для должников со следующего года
        if (cal.get(Calendar.MONTH) == 0) {
            for (Contribution c : contributionDAO.findByFinesOnAndYear(false, cal.get(Calendar.YEAR) - 1)) {
                if (c.getSumFixed() != 0) {
                    c.setFinesOn(true);
                    c.setFinesLastUpdate(cal);
                    contributionDAO.save(c);
                }
            }
        }
        //Включение пеней для должников не уплативших до 1 июля
        if (cal.get(Calendar.MONTH) == 6) {
            Rent rent = rentDAO.findByYearRent(cal.get(Calendar.YEAR));
            for (Contribution c : contributionDAO.findByFinesOnAndYear(false, cal.get(Calendar.YEAR))) {
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
                    c.setFinesLastUpdate(cal);
                    contributionDAO.save(c);
                }

            }
        }
    }
}
