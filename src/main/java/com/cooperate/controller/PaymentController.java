package com.cooperate.controller;

import com.cooperate.Utils;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;
import com.cooperate.service.GaragService;
import com.cooperate.service.JournalHistoryService;
import com.cooperate.service.PaymentService;
import com.cooperate.service.RentService;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;


@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RentService rentService;

    @Autowired
    private GaragService garagService;

    @Autowired
    private JournalHistoryService historyService;

    @RequestMapping(value = "payments", method = RequestMethod.GET)
    public String getPaymentsPage(ModelMap map) {
        map.addAttribute("rents", rentService.getRents());
        return "payments";
    }

    @RequestMapping(value = "allPayment", method = RequestMethod.GET)
    public ResponseEntity<String> getPavilion() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.setDateFormat("dd.MM.yyyy");
        return Utils.convertListToJson(gsonBuilder, paymentService.getPayments());
    }

    //Модальное окно платежа
    @RequestMapping(value = "payModal", method = RequestMethod.GET)
    public String payModal(@RequestParam("idGarag") Integer id, ModelMap map) {
        Garag garag = garagService.getGarag(id);
        map.addAttribute("garag", garag);
        map.addAttribute("payment", new Payment());
        return "modalPay";
    }

    //Сохранение платежа
    @RequestMapping(value = "savePayment", method = RequestMethod.POST)
    public String savePayment(Payment payment, ModelMap map) {
        payment.setDatePayment(Calendar.getInstance());
        Integer number = paymentService.getMaxNumber();
        if (number == null) {
            payment.setNumber(1);
        } else {
            payment.setNumber(paymentService.getMaxNumber() + 1);
        }
        Payment paymentNew = paymentService.saveOrUpdate(payment);
        paymentService.pay(paymentNew);
        payment.setDebtPastPay(garagService.sumContribution(paymentNew.getGarag()));
        paymentService.saveOrUpdate(paymentNew);
        Garag garag = garagService.getGarag(paymentNew.getGarag().getId());
        historyService.event("Оплата по гаражу:" + garag.getSeries() + "-" +
                garag.getNumber() + " произведена");
        map.put("message", "Оплата произведена");
        return "success";
    }

    //Печать выбранного чека
    @RequestMapping(value = "printOrder/{id}", method = RequestMethod.GET)
    public String printOrder(@PathVariable("id") Integer id, ModelMap map) {
        Payment payment = paymentService.getPayment(id);
        map.addAttribute("pay", payment);
        return "order";
    }

    //Печать новосозданного чека
    @RequestMapping(value = "printNewOrder/{id}", method = RequestMethod.GET)
    public String printNewOrder(@PathVariable("id") Integer id, ModelMap map) {
        Garag garag = garagService.getGarag(id);
        map.addAttribute("pay", garag.getPayments().get(0));
        return "order";
    }

    //Удаление гаража
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "deletePayment/{id}", method = RequestMethod.POST)
    public String deletePayment(@PathVariable("id") Integer id, ModelMap map, HttpServletResponse response) {
        try {
            Payment payment = paymentService.getPayment(id);
            Garag garag = payment.getGarag();
            paymentService.delete(id);
            historyService.event("Платеж к гаражу " + garag.getSeries() + "-" + garag.getNumber() + " удален!");
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно удалить платеж");
            response.setStatus(409);
            return "error";
        }
        map.put("message", "Платеж удален!");
        return "success";
    }

}
