package com.cooperate.controller;

import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Rent;
import com.cooperate.service.ContributionService;
import com.cooperate.service.GaragService;
import com.cooperate.service.JournalHistoryService;
import com.cooperate.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class RentController {

    @Autowired
    private RentService rentService;

    @Autowired
    private GaragService garagService;

    @Autowired
    private ContributionService contributionService;

    @Autowired
    private JournalHistoryService historyService;

    //Проверка на существование такого начисления
    @RequestMapping(value = "checkYearRent", method = RequestMethod.GET)
    public String checkYearRent(@RequestParam("year") Integer year, ModelMap map,
                                HttpServletResponse response) {
        if (rentService.checkRent(year)) {
            map.addAttribute("year", year);
            map.addAttribute("rent", new Rent());
            return "modalNewRent";
        } else {
            map.put("message", "Период текущего года существует");
            response.setStatus(409);
            return "error";
        }
    }

    @RequestMapping(value = "saveRent", method = RequestMethod.POST)
    public String saveRent(Rent rent, ModelMap map) {
        rentService.saveOrUpdate(rent);
        rentService.createNewPeriod(rent);
        historyService.event("Создан новый период-" + rent.getYearRent());
        map.put("message", "Сумма оплаты за " + rent.getYearRent() + " год введена!");
        return "success";
    }

    //Страница с вводом старого начисления
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "oldRentPage", method = RequestMethod.GET)
    public String oldRentPage(ModelMap map) {
        map.addAttribute("rent", new Rent());
        return "oldRent";
    }

    //Сохранения старого начисления
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "saveOldRent", method = RequestMethod.POST)
    public String saveOldRent(Rent rent, ModelMap map, HttpServletResponse response) {
        if (rentService.checkRent(rent.getYearRent())) {
            for (Garag g : garagService.getGarags()) {
                List<Contribution> contributions = g.getContributions();
                Contribution contribution = new Contribution();
                contribution.setYear(rent.getYearRent());
                if (g.getPerson() != null) {
                    if (!g.getPerson().getBenefits().equals("")) {
                        contribution.setBenefitsOn(true);
                    }
                }
                contributions.add(contribution);
                g.setContributions(contributions);
                garagService.saveOrUpdate(g);
            }
            rentService.saveOrUpdate(rent);
            historyService.event("Создан старый период-" + rent.getYearRent());
            map.put("message", "Начисление за " + rent.getYearRent() + " сохранено!");
            return "success";
        } else {
            map.put("message", "Начисление данного года уже существует !");
            response.setStatus(409);
            return "error";
        }
    }
}
