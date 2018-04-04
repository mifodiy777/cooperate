package com.cooperate.controller;

import com.cooperate.Utils;
import com.cooperate.editor.CalendarCustomEditor;
import com.cooperate.entity.Cost;
import com.cooperate.gson.CostAdapter;
import com.cooperate.service.CostService;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

/**
 * Контроллер по работе с расходами
 */
@Controller
public class CostController {

    private static final Logger LOGGER = Logger.getLogger(CostController.class);

    @Autowired
    private CostService service;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Calendar.class, new CalendarCustomEditor());
    }


    /**
     * Страница с формой добавления расходов
     *
     * @return страница cost.jsp
     */
    @RequestMapping(value = "cost", method = RequestMethod.GET)
    public String addGaragForm(ModelMap map) {
        map.addAttribute("cost", new Cost());
        return "cost";
    }

    /**
     * Страница расходами
     *
     * @return страница costs.jsp
     */
    @RequestMapping(value = "costsPage", method = RequestMethod.GET)
    public String getCostsPage() {
        return "costs";
    }

    /**
     * @return страница historyGarag.jsp
     */
    @RequestMapping(value = "getCosts", method = RequestMethod.POST)
    public ResponseEntity<String> getCosts() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Cost.class, new CostAdapter());
        return Utils.convertListToJson(gson, service.getAll());
    }

    /**
     * Сохранение расхода
     *
     * @param cost расход
     * @return сообщение
     */
    @RequestMapping(value = "saveCost", method = RequestMethod.POST)
    public String saveCost(Cost cost, ModelMap map, HttpServletResponse response) {
        try {
            service.saveCost(cost);
        } catch (Exception e) {
            map.put("message", "Ошибка по работе с БД!");
            response.setStatus(409);
            return "error";
        }
        LOGGER.info("Запись о расходе: " + cost.getType().getName() + " произведена");
        map.put("message", "Запсь о расходе произведена");
        return "success";
    }
}
