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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RentServiceImpl implements RentService {

    @Autowired
    private RentDAO rentDAO;

    @Autowired
    private GaragService garagService;

    @Autowired
    private PaymentService paymentService;

    @Override
    @Transactional
    public void saveOrUpdate(Rent rent) {
        rentDAO.save(rent);
    }

    @Override
    //Существует ли период начислений
    public Boolean checkRent(Integer year) {
        return rentDAO.countByYearRent(year) == 0;
    }

    @Override
    public List<Rent> getRents() {
        return rentDAO.findAll();
    }

    @Override
    @Transactional
    //Создание нового периода
    public void createNewPeriod(Rent rent) {
        for (Garag garag : garagService.getGarags()) {
            if (garag.getPerson() != null) {
                //Создаем новый период для каждого гаража
                Contribution contribution = new Contribution();
                contribution.setYear(rent.getYearRent());
                if (garag.getPerson().getMemberBoard()  != null && garag.getPerson().getMemberBoard()  ) {
                    contribution.setMemberBoardOn(true);
                } else {
                    contribution.setContribute(rent.getContributeMax());
                }
                if (garag.getPerson().getBenefits().equals("")) {
                    contribution.setContLand(rent.getContLandMax());
                } else {
                    contribution.setContLand(rent.getContLandMax() / 2);
                    contribution.setBenefitsOn(true);
                }
                contribution.setContTarget(rent.getContTargetMax());
                if (garag.getContributions() != null) {
                    garag.getContributions().add(contribution);
                } else {
                    List<Contribution> list = new ArrayList<Contribution>();
                    list.add(contribution);
                    garag.setContributions(list);
                }
                garagService.saveOrUpdate(garag);
                for(Payment payment : paymentService.getPaymentOnGarag(garag)){
                    paymentService.pay(payment,true,"default");
                }
            }
        }
    }

    @Override
    //Период начисления определенного года
    public Rent findByYear(Integer year) {
        return rentDAO.findByYearRent(year);
    }

    @Override
    public List<Rent> findAll() {
        return rentDAO.findAll(sortByYearAsc());
    }

    private Sort sortByYearAsc() {
        return new Sort(Sort.Direction.DESC, "yearRent");
    }
}
