package com.cooperate.service;

import com.cooperate.dao.ContributionDAO;
import com.cooperate.dao.CustomDAO;
import com.cooperate.dao.RentDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Rent;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Сервис для работы с периодами долгов(Contribution).
 */
@Service
public class ContributionService {

    @Autowired
    private ContributionDAO contributionDAO;

    @Autowired
    private CustomDAO customDAO;

    @Autowired
    private RentDAO rentDAO;

    private final static Logger LOGGER = Logger.getLogger(ContributionService.class);

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
        //Получаем список долгов с включенным режимом пени
        for (Contribution c : contributionDAO.findByFinesOn(true)) {
            Float sumContribute = c.getSumFixed(); //Находим сумму долга
            //Вычисляем кол-во дней с последнего обновления.
            //Первая дата должна устанавливаться при включении режима пеней
            DateTime now = DateTime.now();
            if (sumContribute != 0) {
                try {
                    // получаем кол-во дней с последнего обновления
                    long days = Days.daysBetween(new DateTime(c.getFinesLastUpdate().getTimeInMillis()), now).getDays();
                    //Получаем пени за один день для текущего долга
                    double unit = sumContribute * 0.001;
                    // Получаем точную сумму пеней
                    Double finesDouble = unit * days;
                    //Округляем пени до 50
                    long fines = (int) (finesDouble / 50) * 50;
                    if (fines != 0) {
                        //Вычисляем разницу дней для правильной установки даты последнего обновления пеней в связи с округлением
                        //Получаем разницу в сумме пеней
                        long difference = (finesDouble.intValue() % 50);
                        //Если есть остаток от округления до 50, то определяем кол-во дней для достижения этой суммы
                        int dayDifference = (difference > 0) ? (int) Math.ceil(difference / unit) : 0;
                        //Устанавливаем полученную дату после всех вычислений
                        c.setFinesLastUpdate(now.minusDays(dayDifference).toGregorianCalendar());
                        //Суммируем существующие пени с вычисленными
                        int newFines = (int) (c.getFines() + fines);

                        if (newFines < sumContribute) {
                            c.setFines(newFines);
                        } else if (newFines == sumContribute) {
                            c.setFines(newFines);
                            c.setFinesOn(false);
                        } else {
                            //Если новые пени больши суммы долгов определяем сумму начислений этого года и отключаем пени
                            c.setFinesOn(false);
                            if (c.getFines() == 0) {
                                c.setFines(sumContribute.intValue());
                            }
                        }
                    }
                } catch (ArithmeticException e) {
                    LOGGER.error("Арифметическая ошибка для contribution_id=" + c.getId());
                }
            } else {
                c.setFinesOn(false); // если сумма долга равна 0, то режим начисления пеней выключается.
            }
            contributionDAO.save(c); //сохраняем долговой период
        }
    }

    /**
     * Метод включения режима начисления пеней
     *
     * @param now Текущая дата
     */
    public void onFines(Calendar now) {
        Rent rent = rentDAO.findByYearRent(now.get(Calendar.YEAR));
        //Дата для 1 января текущего года
        Calendar newYear = new GregorianCalendar(now.get(Calendar.YEAR), 0, 1);
        //Дата для 1 июля текущего года
        Calendar july = new GregorianCalendar(now.get(Calendar.YEAR), 6, 1);

        //Включение пеней для должников со следующего года
        for (Contribution c : customDAO.findContributionsByFines(now.get(Calendar.YEAR) - 1)) {
            c.setFinesOn(true);
            c.setFinesLastUpdate(newYear);
            contributionDAO.save(c);
        }
        //Включение пеней для должников не уплативших до 1 июля
        if (now.get(Calendar.MONTH) >= Calendar.JULY) {
            for (Contribution c : customDAO.findContributionsByFines(now.get(Calendar.YEAR))) {
                if (getRentMax(rent, c).equals(c.getSumFixed().intValue())) {
                    c.setFinesOn(true);
                    c.setFinesLastUpdate(july);
                    contributionDAO.save(c);
                }
            }
        }
    }


    private Integer getRentMax(Rent rent, Contribution c) {
        Integer rentMax = 0;
        if (c.getContributeMax() != 0) {
            rentMax += Math.round(c.getContributeMax());
        } else {
            if (!c.isMemberBoardOn()) {
                rentMax += Math.round(rent.getContributeMax());
            }
        }
        if (c.isBenefitsOn()) {
            rentMax += Math.round(rent.getContLandMax()) / 2;
        } else {
            rentMax += Math.round(rent.getContLandMax());
        }
        rentMax += Math.round(rent.getContTargetMax());
        return rentMax;
    }
}
