package com.cooperate.service.impl;

import com.cooperate.dao.ContributionDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;
import com.cooperate.entity.Rent;
import com.cooperate.service.ContributionService;
import com.cooperate.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ContributionServiceImpl implements ContributionService {

    @Autowired
    ContributionDAO contributionDAO;

    @Autowired
    RentService rentService;

    @Override
    @Transactional
    public void saveOrUpdate(Contribution contribution) {
        contributionDAO.save(contribution);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        contributionDAO.delete(id);
    }

    //Метод начисления долгов новосозданного гаража
    @Override
    @Transactional
    public List<Contribution> getContributionOnGarag(Garag garag) {
        List<Contribution> contributionList = new ArrayList<Contribution>();
        for (Rent rent : rentService.getRents()) {
            if (rent.getYearRent() == Calendar.getInstance().get(Calendar.YEAR)) {
                Contribution contribution = new Contribution();
                contribution.setYear(rent.getYearRent());
                if (garag.getPerson() == null) {
                    contributionList.add(contribution);
                    return contributionList;
                }
                contribution.setContribute(rent.getContributeMax());
                if (!garag.getPerson().getBenefits().equals("")) {
                    contribution.setContLand(rent.getContLandMax() / 2);
                    //Назначение льготного периода
                    contribution.setBenefitsOn(true);
                } else {
                    contribution.setContLand(rent.getContLandMax());
                }
                contribution.setContTarget(rent.getContTargetMax());
                contributionList.add(contribution);
            }
        }
        return contributionList;
    }

    //Вычисление пени
    @Override
    @Transactional
    public void updateFines() {
        Calendar calendar = Calendar.getInstance();
        List<Rent> rentList = rentService.getRents();
        //Получаем список долгов с включенным режимом пени
        for (Contribution c : contributionDAO.findByFinesOn(true)) {
            //Находим сумму долга
            Float sum = c.getContribute() + c.getContLand() + c.getContTarget();
            //ВЫчисляем кол-во дней с последнего обновления.
            //Первая дата должна устанавливаться при включении режима пеней
            if (sum != 0) {
                long days = getDays(calendar, c.getFinesLastUpdate());
                Double finesDouble = (sum * 0.001) * days;
                int fines = (finesDouble.intValue() / 50);
                fines *= 50;
                //Вычисляем сумму пени
                if (fines != 0) {
                    c.setFinesLastUpdate(calendar);
                }
                int newFines = c.getFines() + fines;
                if (newFines <= sum) {
                    c.setFines(newFines);
                    c.setFinesSum(c.getFines());
                    contributionDAO.save(c);
                } else {
                    for (Rent rent : rentList) {
                        if (rent.getYearRent() == c.getYear()) {
                            float sumRent;
                            if (c.isBenefitsOn()) {
                                sumRent = rent.getContributeMax() + (rent.getContLandMax()/2) + rent.getContTargetMax();

                            } else {
                                sumRent = rent.getContributeMax() + rent.getContLandMax() + rent.getContTargetMax();

                            }
                            if (sum == sumRent && newFines > sum) {
                                c.setFinesOn(false);
                                c.setFines(sum.intValue());
                                c.setFinesSum(c.getFines());
                                contributionDAO.save(c);
                            }
                        }
                    }
                    if (c.isFinesOn()) {
                        c.setFinesOn(false);
                        if ((newFines - sum) == 50) {
                            c.setFines(sum.intValue());
                            c.setFinesSum(c.getFines());
                        }
                        contributionDAO.save(c);
                    }
                }
            }
        }
    }

    //Метод вычисленеия дней с последнего обновления пеней
    public long getDays(Calendar calendar, Calendar lastUpdate) {
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

    @Override
    public void onFines() {
        Calendar cal = Calendar.getInstance();
        //Включение пеней для должников со следующего года
        if (cal.get(Calendar.MONTH) == 0) {
            for (Contribution c : contributionDAO.findByFinesOn(false)) {
                if (c.getYear() < cal.get(Calendar.YEAR)) {
                    float sum = c.getContribute() + c.getContLand() + c.getContTarget();
                    if (sum != 0) {
                        c.setFinesOn(true);
                        c.setFinesLastUpdate(cal);
                        contributionDAO.save(c);
                    }
                }
            }
        }
        //Включение пеней для должников не уплативших до 1 июля
        if (cal.get(Calendar.MONTH) == 6) {
            List<Rent> rents = rentService.getRents();
            for (Contribution c : contributionDAO.findByFinesOnAndYear(false, cal.get(Calendar.YEAR))) {
                for (Rent rent : rents) {
                    if (rent.getYearRent() == c.getYear()) {
                        if (c.getContribute() == rent.getContributeMax()) {
                            c.setFinesOn(true);
                            c.setFinesLastUpdate(cal);
                            contributionDAO.save(c);
                        }
                    }
                }
            }
        }
    }
}
