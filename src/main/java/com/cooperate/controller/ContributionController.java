package com.cooperate.controller;

import com.cooperate.editor.CalendarCustomEditor;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;
import com.cooperate.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.List;

@Controller
public class ContributionController {

    @Autowired
    private GaragService garagService;

    @Autowired
    private ContributionService contributionService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private JournalHistoryService journalService;

    @Autowired
    private RentService rentService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Calendar.class, new CalendarCustomEditor());
    }

    //Форма добавления старых периодов
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "editContribute", method = RequestMethod.GET)
    public String editContributeForm(@RequestParam("idGarag") Integer id,
                                     @RequestParam("year") Integer year, ModelMap map) {
        Contribution contribution = contributionService.getContributionByGaragAndYear(id, year);
        if (contribution == null) {
            contribution = new Contribution();
            contribution.setYear(year);
        }
        map.addAttribute("contribution", contribution);
        map.addAttribute("garag", garagService.getGarag(id));
        map.addAttribute("max", rentService.findByYear(year));
        return "modalEditContribute";
    }

    //Добавление старых периодов
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "saveContribute", method = RequestMethod.POST)
    public String saveContribute(Contribution contribute, @RequestParam("idGarag") Integer id,
                                 ModelMap map, HttpServletResponse response) {
        Garag garag = garagService.getGarag(id);
        try {
            if (contribute.getId() != null) {
                contributionService.saveOrUpdate(contribute);
            } else {
                garag.getContributions().add(contribute);
                garagService.saveOrUpdate(garag);
            }
            journalService.event("Долг для гаража " + garag.getName() + " за " +
                    contribute.getYear() + " год назначен");
            map.put("message", "Долг за " + contribute.getYear() + " год введен успешно");
            return "success";
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно сохранить долг");
            response.setStatus(409);
            return "error";
        }
    }

    //Сохранения дополнительных взносов
    @RequestMapping(value = "saveAddingCount", method = RequestMethod.GET)
    public String saveAddingCount(@RequestParam("id") Integer id,
                                  @RequestParam("count") Float count, ModelMap map) {
        Garag garag = garagService.getGarag(id);
        Payment payment = new Payment();
        payment.setNumber(paymentService.getMaxNumber());
        payment.setYear(Calendar.getInstance().get(Calendar.YEAR));
        payment.setFio(garag.getPerson().getFIO());
        payment.setAdditionallyPay(count);
        payment.setDatePayment(Calendar.getInstance());
        payment.setGarag(garag);
        payment.setDebtPastPay(garagService.sumContribution(garag));
        paymentService.saveOrUpdate(payment);
        journalService.event("Дополнительный взнос  для гаража " + garag.getName() + " уплачен");
        map.put("message", "Дополнительный взнос  для гаража " + garag.getName() + " уплачен");
        return "success";
    }

}
