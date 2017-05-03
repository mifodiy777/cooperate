package com.cooperate.service;

import com.cooperate.dao.PaymentDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDAO paymentDAO;

    @Autowired
    private ContributionService contributionService;

    @Autowired
    private GaragService garagService;

    /**
     * Получение списка периодов для которых существуют платежи
     * @return список периодов
     */
    public List<Integer> findYears() {
        return paymentDAO.findYears();
    }

    /**
     * Сохранение платежа
     * @param payment Платеж
     * @return сохраненный платеж
     */
    @Transactional
    public Payment saveOrUpdate(Payment payment) {
        return paymentDAO.save(payment);
    }

    /**
     * Получение списка платежей определенного года
     * @param year год
     * @return Список платежей
     */
    public List<Payment> findByYear(Integer year) {
        return paymentDAO.findByYear(year);
    }

    /**
     * Получение платежа по идентификатору
     * @param id ID платежа
     * @return платеж
     */
    public Payment getPayment(Integer id) {
        return paymentDAO.getOne(id);
    }

    /**
     * Удаление платежа
     * @param id ID платежа
     */
    @Transactional
    public void delete(Integer id) {
        paymentDAO.delete(id);
    }

    /**
     * Возвращает платежи для определенного гаража с остатками денег
     * @param garag Гараж
     * @return список платежей
     */
    public List<Payment> getPaymentOnGarag(Garag garag) {
        return paymentDAO.getPaymentOnGarag(garag.getId());
    }

    /**
     * Метод проведения платежа, или при раскидывания остаточных средств при создании нового долгового периода
     * @param payment Платеж
     * @param isCreateNewPeriod true - вызов метода при создании нового периода.
     * @param type Тип платежа
     * @return Платеж
     */
    @Transactional
    public Payment pay(Payment payment, Boolean isCreateNewPeriod, String type) {
        //Получаем гараж
        Garag garag = garagService.getGarag(payment.getGarag().getId());
        Calendar now = Calendar.getInstance();
        if (!isCreateNewPeriod) {
            //Назначили время
            if (payment.getDatePayment() == null) {
                payment.setDatePayment(now);
                payment.setYear(now.get(Calendar.YEAR));
            } else {
                payment.setYear(payment.getDatePayment().get(Calendar.YEAR));
            }
            //Назначили номер
            payment.setNumber(getMaxNumber());
            payment.setGarag(garag);
            payment.setFio(garag.getPerson().getFIO());
        }
        if (type.equals("adding")) {
            payment.setAdditionallyPay(payment.getPay());
            payment.setPay(0f);
            payment.setDebtPastPay(garagService.sumContribution(payment.getGarag()));
            return paymentDAO.save(payment);
        }
        int size = garag.getContributions().size();
        int i = 1;
        float oldContribute = garag.getOldContribute();
        if (oldContribute != 0) {
            if (payment.getPay() <= oldContribute) {
                payment.setOldContributePay(payment.getPay());
                garag.setOldContribute(oldContribute - payment.getPay());
                payment.setPay(0);
            } else {
                payment.setOldContributePay(oldContribute);
                payment.setPay(payment.getPay() - oldContribute);
                garag.setOldContribute(0f);
            }
        }
        for (Contribution c : garag.getContributions()) {
            if (payment.getPay() == 0) {
                break;
            }
            if (c.getSumFixed() + c.getFines() == 0) {
                continue;
            }
            if (payment.getPay() >= c.getSumFixed()) {
                Float reminder = c.getSumFixed();
                payment.setContributePay(payment.getContributePay() + c.getContribute());
                c.setContribute(0f);
                payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                c.setContLand(0f);
                payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                c.setContTarget(0f);
                c.setFinesOn(false);
                payment.setPay(payment.getPay() - reminder);
                //Если взнос уплачен проверяем оплату по пеням.
                if (payment.getPay() != 0) {
                    if (c.getFines() >= 0 && payment.getPay() >= c.getFines()) {
                        payment.setFinesPay(payment.getFinesPay() + c.getFines());
                        payment.setPay(payment.getPay() - c.getFines());
                        c.setFines(0);
                    } else {
                        payment.setFinesPay(payment.getFinesPay() + Math.round(payment.getPay()));
                        c.setFines(c.getFines() - Math.round(payment.getPay()));
                        payment.setPay(0);
                    }
                }
            } else {
                if (c.getContribute() > payment.getPay()) {
                    payment.setContributePay(payment.getContributePay() + payment.getPay());
                    c.setContribute(c.getContribute() - payment.getPay());
                    payment.setPay(0);
                } else {
                    payment.setPay(payment.getPay() - c.getContribute());
                    payment.setContributePay(payment.getContributePay() + c.getContribute());
                    c.setContribute(0f);
                }
                if (c.getContLand() > payment.getPay()) {
                    payment.setContLandPay(payment.getContLandPay() + payment.getPay());
                    c.setContLand(c.getContLand() - payment.getPay());
                    payment.setPay(0);
                } else {
                    payment.setPay(payment.getPay() - c.getContLand());
                    payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                    c.setContLand(0f);
                }
                if (c.getContTarget() > payment.getPay()) {
                    payment.setContTargetPay(payment.getContTargetPay() + payment.getPay());
                    c.setContTarget(c.getContTarget() - payment.getPay());
                    payment.setPay(0);
                }
                //Для текущего года при частичной оплате в любом месяце пени выключаются
                if (c.getYear().equals(now.get(Calendar.YEAR))) {
                    c.setFinesOn(false);
                }
            }
            contributionService.saveOrUpdate(c);
            i++;
        }
        payment.setDebtPastPay(garagService.sumContribution(payment.getGarag()));
        return paymentDAO.save(payment);
    }

    /**
     * Вычисление номера чека - Max
     * @return свободный максимальный номер чека
     */
    private Integer getMaxNumber() {
        Integer number = paymentDAO.getMaxValueNumber();
        number = (number == null) ? 1 : paymentDAO.getMaxValueNumber() + 1;
        return number;
    }
}

