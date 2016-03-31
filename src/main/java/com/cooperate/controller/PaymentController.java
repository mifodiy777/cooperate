package com.cooperate.controller;

import com.cooperate.Utils;
import com.cooperate.editor.CalendarCustomEditor;
import com.cooperate.entity.Payment;
import com.cooperate.gson.PaymentAdapter;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Calendar.class, new CalendarCustomEditor());
    }

    @RequestMapping(value = "paymentsPage", method = RequestMethod.GET)
    public String getPaymentsPage(@RequestParam(required = false, value = "year") Integer year, ModelMap map) {
        map.addAttribute("setYear", (year == null) ? Calendar.getInstance().get(Calendar.YEAR) : year);
        map.addAttribute("years", paymentService.findYears());
        map.addAttribute("rents", rentService.getRents());
        return "payments";
    }

    @RequestMapping(value = "payments", method = RequestMethod.GET)
    public ResponseEntity<String> getPayments(@RequestParam("setYear") Integer year) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.registerTypeAdapter(Payment.class, new PaymentAdapter());
        return Utils.convertListToJson(gsonBuilder, paymentService.findByYear(year));
    }

    //Модальное окно платежа
    @RequestMapping(value = "payModal", method = RequestMethod.GET)
    public String payModal(@RequestParam("idGarag") Integer id, ModelMap map) {
        map.addAttribute("garag", garagService.getGarag(id));
        map.addAttribute("payment", new Payment());
        return "modalPay";
    }

    //Сохранение платежа
    @RequestMapping(value = "savePayment", method = RequestMethod.POST)
    @ResponseBody
    public Integer savePayment(Payment payment) {
        payment = paymentService.pay(payment, false);
        historyService.event("Оплата по гаражу:" + payment.getGarag().getName() + " произведена");
        return payment.getId();
    }

    //Печать выбранного чека
    @RequestMapping(value = "printOrder/{id}", method = RequestMethod.GET)
    public String printOrder(@PathVariable("id") Integer id, ModelMap map) {
        map.addAttribute("pay", paymentService.getPayment(id));
        return "order";
    }

    //Удаление платежа
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "deletePayment/{id}", method = RequestMethod.POST)
    public String deletePayment(@PathVariable("id") Integer id, ModelMap map, HttpServletResponse response) {
        try {
            String garag = paymentService.getPayment(id).getGarag().getName();
            paymentService.delete(id);
            historyService.event("Платеж к гаражу " + garag + " удален!");
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно удалить платеж");
            response.setStatus(409);
            return "error";
        }
        map.put("message", "Платеж удален!");
        return "success";
    }

}
