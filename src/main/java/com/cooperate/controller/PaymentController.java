package com.cooperate.controller;

import com.cooperate.Utils;
import com.cooperate.editor.CalendarCustomEditor;
import com.cooperate.entity.Payment;
import com.cooperate.gson.PaymentAdapter;
import com.cooperate.service.GaragService;
import com.cooperate.service.PaymentService;
import com.cooperate.service.RentService;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
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

    private final Logger logger = Logger.getLogger(PaymentController.class);

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Calendar.class, new CalendarCustomEditor());
    }

    /**
     * Страница с чеками определенного года
     *
     * @param year Год
     * @param map  ModelMap
     * @return страница payments.jsp
     */
    @RequestMapping(value = "paymentsPage", method = RequestMethod.GET)
    public String getPaymentsPage(@RequestParam(required = false, value = "year") Integer year, ModelMap map) {
        try {
            map.addAttribute("setYear", (year == null) ? Calendar.getInstance().get(Calendar.YEAR) : year);
            map.addAttribute("years", paymentService.findYears());
            map.addAttribute("rents", rentService.getRents());
            return "payments";
        } catch (DataAccessResourceFailureException e) {
            map.addAttribute("textError", "Ошибка базы данных, проверте подключение к БД");
            return "errorPage";
        }
    }

    /**
     * Список всех платежей определенного года
     *
     * @param year Год
     * @return json список платежей
     */
    @RequestMapping(value = "payments", method = RequestMethod.GET)
    public ResponseEntity<String> getPayments(@RequestParam("setYear") Integer year) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.registerTypeAdapter(Payment.class, new PaymentAdapter());
        return Utils.convertListToJson(gsonBuilder, paymentService.findByYear(year));
    }

    /**
     * Модальное окно платежа
     *
     * @param id   ID Гаража
     * @param type Тип платежа (основной/дополнительный)
     * @param map  ModelMap
     * @return modalPay.jsp
     */
    @RequestMapping(value = "payModal", method = RequestMethod.GET)
    public String payModal(@RequestParam("idGarag") Integer id,
                           @RequestParam("type") String type,
                           ModelMap map) {
        map.addAttribute("type", type);
        map.addAttribute("payment", new Payment(garagService.getGarag(id)));
        return "modalPay";
    }

    /**
     * Сохранение платежа
     *
     * @param payment Платеж
     * @param type    Тип платежа
     * @return номер проведенного платежа
     */
    @RequestMapping(value = "savePayment", method = RequestMethod.POST)
    @ResponseBody
    public Integer savePayment(Payment payment, @RequestParam("type") String type) {
        payment = paymentService.pay(payment, false, type);
        logger.info("Оплата по гаражу:" + payment.getGarag().getName() + " произведена");
        return payment.getId();
    }

    /**
     * Печать выбранного чека
     *
     * @param id  ID чека
     * @param map ModelMap
     * @return страница с печатной формой чека order.jsp
     */
    @RequestMapping(value = "printOrder/{id}", method = RequestMethod.GET)
    public String printOrder(@PathVariable("id") Integer id, ModelMap map) {
        map.addAttribute("pay", paymentService.getPayment(id));
        return "order";
    }

    /**
     * Удаление платежа
     *
     * @param id       ID платежа
     * @param map      ModelMap
     * @param response ответ
     * @return сообщение о результате удаления платежа из базы
     */
    @RequestMapping(value = "deletePayment/{id}", method = RequestMethod.POST)
    public String deletePayment(@PathVariable("id") Integer id, ModelMap map, HttpServletResponse response) {
        try {
            String garag = paymentService.getPayment(id).getGarag().getName();
            paymentService.delete(id);
            logger.info("Платеж к гаражу " + garag + " удален!");
            map.put("message", "Платеж удален!");
            return "success";
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно удалить платеж");
            response.setStatus(409);
            return "error";
        }
    }
}
