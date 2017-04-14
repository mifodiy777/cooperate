package com.cooperate.controller;

import com.cooperate.entity.Garag;
import com.cooperate.service.GaragService;
import com.cooperate.service.HistoryGaragService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class HistoryGaragController {

    @Autowired
    private HistoryGaragService historyGaragService;

    @Autowired
    private GaragService garagService;

    private final Logger logger = Logger.getLogger(HistoryGaragController.class);

    //Информационно модальное окно для история изменений владельцев гаража

    @RequestMapping(value = "getHistoryGarag/{id}", method = RequestMethod.GET)
    public String historyModalGarag(@PathVariable("id") Integer id, ModelMap map) {
        Garag garag = garagService.getGarag(id);
        map.addAttribute("garag", garag);
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "historyGarag";
    }

    //Удаление гаража
    @RequestMapping(value = "deleteReason", method = RequestMethod.POST)
    public String deleteReason(@RequestParam("idReason") Integer idReason,
                               ModelMap map, HttpServletResponse response) {
        try {
            historyGaragService.delete(idReason);
            map.put("message", "Запись о смене владельца удалена!");
            return "success";
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно удалить!");
            response.setStatus(409);
            return "error";
        }
    }
}
