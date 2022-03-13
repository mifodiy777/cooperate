package com.cooperate.service;

import com.cooperate.dao.RentDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;
import com.cooperate.entity.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RentService {

    @Autowired
    private RentDAO rentDAO;

    @Autowired
    private GaragService garagService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * Сохранение периода начислений
     *
     * @param rent период начислений
     */
    @Transactional
    public void saveOrUpdate(Rent rent) {
        rentDAO.save(rent);
    }

    //Существует ли период начислений

    /**
     * Проверка существования периода определенного года
     *
     * @param year год
     * @return true если не существует
     */
    public Boolean checkRent(Integer year) {
        return rentDAO.findByYearRent(year) == null;
    }

    /**
     * Получение всех периодов начисления
     *
     * @return список периодов
     */
    public List<Rent> getRents() {
        return rentDAO.findAll();
    }

    /**
     * Создание нового периода
     *
     * @param rent период
     */
    @Transactional
    public void createNewPeriod(Rent rent) {
        int countIsElectricMeter = Integer.parseInt(dictionaryService.findByName("electricMeter"));
        for (Garag garag : garagService.getGarags()) {
            if (garag.getPerson() != null) {
                //Создаем новый период для каждого гаража
                Contribution contribution = new Contribution();
                contribution.setYear(rent.getYearRent());
                if (garag.getPerson().getMemberBoard() != null && garag.getPerson().getMemberBoard()) {
                    contribution.setMemberBoardOn(true);
                } else if (garag.getElectricMeter()) {
                    contribution.setContribute(rent.getContributeMax() - countIsElectricMeter);
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
                garagService.save(garag);
                for (Payment payment : paymentService.getPaymentOnGarag(garag)) {
                    paymentService.pay(payment, true, "default");
                }
            }
        }
    }

    /**
     * Получение периода определенного года
     *
     * @param year год
     * @return период
     */
    public Rent findByYear(Integer year) {
        return rentDAO.findByYearRent(year);
    }

    /**
     * Получение всех периодов в отсортированном порядке
     *
     * @return список периодов
     */
    public List<Rent> findAll() {
        return rentDAO.findAll(sortByYearAsc());
    }

    private Sort sortByYearAsc() {
        return new Sort(Sort.Direction.DESC, "yearRent");
    }
}
