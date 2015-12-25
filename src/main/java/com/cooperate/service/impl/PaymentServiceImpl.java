package com.cooperate.service.impl;

import com.cooperate.dao.PaymentDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;
import com.cooperate.service.ContributionService;
import com.cooperate.service.GaragService;
import com.cooperate.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentDAO paymentDAO;

    @Autowired
    ContributionService contributionService;

    @Autowired
    GaragService garagService;

    @Override
    @Transactional
    public Payment saveOrUpdate(Payment rent) {
        return paymentDAO.save(rent);
    }

    @Override
    @Transactional
    public List<Payment> getPayments() {
        return paymentDAO.findAll();
    }

    @Override
    @Transactional
    public Payment getPayment(Integer id) {
        return paymentDAO.getOne(id);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        paymentDAO.delete(id);
    }

    //Возвращает платеж для определенного гаража с остатками денег
    @Override
    public Payment getPaymentOnGarag(Garag garag) {
        return paymentDAO.getPaymentOnGarag(garag.getId());
    }

    @Override
    public Integer getMaxNumber() {
        return paymentDAO.getMaxValueNumber();
    }

    //Метод платежа
    @Override
    @Transactional
    public void pay(Payment payment) {
        Garag garag = garagService.getGarag(payment.getGarag().getId());
        Calendar now = Calendar.getInstance();
        String fio = garag.getPerson().getLastName() + " " + garag.getPerson().getName() +
                " " + garag.getPerson().getFatherName();
        payment.setFio(fio);
        //Находим длинну коллекции
        int size = garag.getContributions().size();
        //Инициализируем счетчик
        int i = 0;
        //Достаем все начисления за все года отсортированные по году ASC
        for (Contribution c : garag.getContributions()) {
            i++;
            // Есть ли долг по членскому взносу
            if (c.getContribute() != 0f) {
                //Если он меньше платежа идем дальше
                if (c.getContribute() < payment.getPay()) {
                    //Вносим значения суммы за членский взнос в платеж(чек)
                    payment.setContributePay(payment.getContributePay() + c.getContribute());
                    // Вычитаем потраченные деньги из платежа
                    payment.setPay(payment.getPay() - c.getContribute());
                    //Обнуляем долг членского взноса за текущий год
                    c.setContribute(0f);
                    //Проверяем  долг за землю меньше оставшихся средств платежа
                    if (c.getContLand() < payment.getPay()) {
                        //Вносим значения суммы за аренду в платеж(чек)
                        payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                        //Вычитаем
                        payment.setPay(payment.getPay() - c.getContLand());
                        //Обнуляем
                        c.setContLand(0f);
                        if (c.getContTarget() < payment.getPay()) {
                            payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                            payment.setPay(payment.getPay() - c.getContTarget());
                            c.setContTarget(0f);
                            c.setFinesOn(false);
                            if (c.getFines() < payment.getPay()) {
                                payment.setFinesPay(payment.getFinesPay() + c.getFines());
                                payment.setPay(payment.getPay() - c.getFines());
                                c.setFines(0);
                                if (size == i) {
                                    c.setBalance(payment.getPay());
                                    paymentDAO.save(payment);
                                    contributionService.saveOrUpdate(c);
                                }
                            }
                            if (c.getFines() == payment.getPay()) {
                                payment.setFinesPay(payment.getFinesPay() + c.getFines());
                                payment.setPay(payment.getPay() - c.getFines());
                                c.setFines(0);
                                paymentDAO.save(payment);
                                contributionService.saveOrUpdate(c);
                                break;
                            }
                            if (c.getFines() > payment.getPay()) {
                                payment.setFinesPay(payment.getFinesPay() + (int) payment.getPay());
                                c.setFines(c.getFines() - (int) payment.getPay());
                                payment.setPay(0f);
                                paymentDAO.save(payment);
                                contributionService.saveOrUpdate(c);
                                break;
                            }
                        }
                        if (c.getContTarget() == payment.getPay()) {
                            payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                            c.setContTarget(c.getContTarget() - payment.getPay());
                            payment.setPay(0f);
                            c.setFinesOn(false);
                            paymentDAO.save(payment);
                            contributionService.saveOrUpdate(c);
                            break;
                        }
                        if (c.getContTarget() > payment.getPay()) {
                            payment.setContTargetPay(payment.getContTargetPay() + payment.getPay());
                            c.setContTarget(c.getContTarget() - payment.getPay());
                            payment.setPay(0f);
                            paymentDAO.save(payment);
                            contributionService.saveOrUpdate(c);
                            break;
                        }
                    }
                    if (c.getContLand() == payment.getPay()) {
                        payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                        c.setContLand(c.getContLand() - payment.getPay());
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }
                    if (c.getContLand() > payment.getPay()) {
                        payment.setContLandPay(payment.getContLandPay() + payment.getPay());
                        c.setContLand(c.getContLand() - payment.getPay());
                        payment.setPay(0f);
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }

                }
                if (c.getContribute() == payment.getPay()) {
                    payment.setContributePay(payment.getContributePay() + c.getContribute());
                    c.setContribute(c.getContribute() - payment.getPay());
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
                if (c.getContribute() > payment.getPay()) {
                    payment.setContributePay(payment.getContributePay() + payment.getPay());
                    c.setContribute(c.getContribute() - payment.getPay());
                    payment.setPay(0f);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
            }
            if (c.getContLand() != 0) {
                if (c.getContLand() < payment.getPay()) {
                    //Вносим значения суммы за аренду в платеж(чек)
                    payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                    //Вычитаем
                    payment.setPay(payment.getPay() - c.getContLand());
                    //Обнуляем
                    c.setContLand(0f);
                    if (c.getContTarget() < payment.getPay()) {
                        payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                        payment.setPay(payment.getPay() - c.getContTarget());
                        c.setContTarget(0f);
                        c.setFinesOn(false);
                        if (c.getFines() < payment.getPay()) {
                            payment.setFinesPay(payment.getFinesPay() + c.getFines());
                            payment.setPay(payment.getPay() - c.getFines());
                            c.setFines(0);
                            if (size == i) {
                                c.setBalance(payment.getPay());
                                paymentDAO.save(payment);
                                contributionService.saveOrUpdate(c);
                            }

                        }
                        if (c.getFines() == payment.getPay()) {
                            payment.setFinesPay(payment.getFinesPay() + c.getFines());
                            payment.setPay(payment.getPay() - c.getFines());
                            c.setFines(0);
                            paymentDAO.save(payment);
                            contributionService.saveOrUpdate(c);
                            break;
                        }
                        if (c.getFines() > payment.getPay()) {
                            payment.setFinesPay(payment.getFinesPay() + (int) payment.getPay());
                            c.setFines(c.getFines() - (int) payment.getPay());
                            payment.setPay(0f);
                            paymentDAO.save(payment);
                            contributionService.saveOrUpdate(c);
                            break;
                        }
                    }
                    if (c.getContTarget() == payment.getPay()) {
                        payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                        c.setContTarget(c.getContTarget() - payment.getPay());
                        payment.setPay(0f);
                        c.setFinesOn(false);
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }
                    if (c.getContTarget() > payment.getPay()) {
                        payment.setContTargetPay(payment.getContTargetPay() + payment.getPay());
                        c.setContTarget(c.getContTarget() - payment.getPay());
                        payment.setPay(0f);
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }
                }
                if (c.getContLand() == payment.getPay()) {
                    payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                    c.setContLand(c.getContLand() - payment.getPay());
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
                if (c.getContLand() > payment.getPay()) {
                    payment.setContLandPay(payment.getContLandPay() + payment.getPay());
                    c.setContLand(c.getContLand() - payment.getPay());
                    payment.setPay(0f);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
            }
            if (c.getContTarget() != 0) {
                if (c.getContTarget() < payment.getPay()) {
                    payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                    payment.setPay(payment.getPay() - c.getContTarget());
                    c.setContTarget(0f);
                    c.setFinesOn(false);
                    if (c.getFines() < payment.getPay()) {
                        payment.setFinesPay(payment.getFinesPay() + c.getFines());
                        payment.setPay(payment.getPay() - c.getFines());
                        c.setFines(0);
                        if (size == i) {
                            c.setBalance(payment.getPay());
                            paymentDAO.save(payment);
                            contributionService.saveOrUpdate(c);
                        }

                    }
                    if (c.getFines() == payment.getPay()) {
                        payment.setFinesPay(payment.getFinesPay() + c.getFines());
                        payment.setPay(payment.getPay() - c.getFines());
                        c.setFines(0);
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }
                    if (c.getFines() > payment.getPay()) {
                        payment.setFinesPay(payment.getFinesPay() + (int) payment.getPay());
                        c.setFines(c.getFines() - (int) payment.getPay());
                        payment.setPay(0f);
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }
                }
                if (c.getContTarget() == payment.getPay()) {
                    payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                    c.setContTarget(c.getContTarget() - payment.getPay());
                    payment.setPay(0f);
                    c.setFinesOn(false);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
                if (c.getContTarget() > payment.getPay()) {
                    payment.setContTargetPay(payment.getContTargetPay() + payment.getPay());
                    c.setContTarget(c.getContTarget() - payment.getPay());
                    payment.setPay(0f);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }

            }
            if (c.getFines() != 0) {
                if (c.getFines() < payment.getPay()) {
                    payment.setFinesPay(payment.getFinesPay() + c.getFines());
                    payment.setPay(payment.getPay() - c.getFines());
                    c.setFines(0);
                    if (size == i) {
                        c.setBalance(payment.getPay());
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                    }

                }
                if (c.getFines() == payment.getPay()) {
                    payment.setFinesPay(payment.getFinesPay() + c.getFines());
                    payment.setPay(payment.getPay() - c.getFines());
                    c.setFines(0);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
                if (c.getFines() > payment.getPay()) {                   
                    payment.setFinesPay(payment.getFinesPay() + (int) payment.getPay());
                    c.setFines(c.getFines() - (int) payment.getPay());
                    payment.setPay(0f);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
            }
            if (size == i) {
                c.setBalance(payment.getPay());
                paymentDAO.save(payment);
                contributionService.saveOrUpdate(c);
            }


        }

    }
}

