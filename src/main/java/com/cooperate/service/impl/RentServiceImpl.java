package com.cooperate.service.impl;

import com.cooperate.dao.RentDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;
import com.cooperate.entity.Rent;
import com.cooperate.service.GaragService;
import com.cooperate.service.PaymentService;
import com.cooperate.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RentServiceImpl implements RentService {

    @Autowired
    RentDAO rentDAO;

    @Autowired
    GaragService garagService;

    @Autowired
    PaymentService paymentService;

    @Override
    @Transactional
    public void saveOrUpdate(Rent rent) {
        rentDAO.save(rent);
    }

    @Override
    @Transactional
    //Существует ли период начислений
    public Boolean checkRent(Integer year) {
        return rentDAO.countByYearRent(year) == 0;
    }

    @Override
    @Transactional
    public List<Rent> getRents() {
        return rentDAO.findAll();
    }

    @Override
    @Transactional
    public Rent getRent(Integer id) {
        return rentDAO.getOne(id);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        rentDAO.delete(id);
    }

    @Override
    @Transactional
    //Создание нового периода
    public void createNewPeriod(Rent rent) {
        //Период для обычных владельцев
        Contribution contribution = new Contribution();
        contribution.setYear(rent.getYearRent());
        contribution.setContribute(rent.getContributeMax());
        contribution.setContLand(rent.getContLandMax());
        contribution.setContTarget(rent.getContTargetMax());
        List<Contribution> contributionList = new ArrayList<Contribution>();
        contributionList.add(contribution);
        List<Contribution> contributionListBenefits = new ArrayList<Contribution>();
        //Период для льготников
        Contribution cBenefits = new Contribution();
        cBenefits.setYear(rent.getYearRent());
        cBenefits.setContribute(rent.getContributeMax());
        //Для льготников 50% скидка на аренду земли
        cBenefits.setContLand(rent.getContLandMax() / 2);
        cBenefits.setBenefitsOn(true);
        cBenefits.setContTarget(rent.getContTargetMax());
        contributionListBenefits.add(cBenefits);
        for (Garag g : garagService.getGarags()) {
            if (!g.getPerson().getBenefits().equals("")) {
                if (g.getContributions() == null) {
                    g.setContributions(contributionListBenefits);
                    garagService.saveOrUpdate(g);
                } else {
                    g.getContributions().add(cBenefits);
                    garagService.saveOrUpdate(g);
                }
                //Проверка остатков не проведенных в период
                Integer index = g.getContributions().size();
                Contribution c = g.getContributions().get(index - 2);
                if (c.getBalance() != 0f) {
                    Payment payment = paymentService.getPaymentOnGarag(g);
                    paymentService.pay(payment);
                }
            } else {
                if (g.getContributions() == null) {
                    g.setContributions(contributionList);
                    garagService.saveOrUpdate(g);
                } else {
                    g.getContributions().add(contribution);
                    garagService.saveOrUpdate(g);
                }
                //Проверка остатков не проведенных в период
                Integer index = g.getContributions().size();
                Contribution c = g.getContributions().get(index - 2);
                if (c.getBalance() != 0f) {
                    Payment payment = paymentService.getPaymentOnGarag(g);
                    paymentService.pay(payment);
                }
            }
        }
    }

    @Override
    @Transactional
    //Период начисления определенного года
    public Rent findByYear(Integer year) {
        return rentDAO.findByYearRent(year);
    }
}
