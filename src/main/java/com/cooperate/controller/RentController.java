package com.cooperate.controller;

import com.cooperate.entity.Rent;
import com.cooperate.service.RentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class RentController {

    @Autowired
    private RentService rentService;

    private final Logger logger = Logger.getLogger(RentController.class);

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
        logger.info("Создан новый период-" + rent.getYearRent());
        map.put("message", "Сумма оплаты за " + rent.getYearRent() + " год введена!");
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "success";
    }

}
