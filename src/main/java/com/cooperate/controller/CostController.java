package com.cooperate.controller;

import com.cooperate.Utils;
import com.cooperate.editor.CalendarCustomEditor;
import com.cooperate.entity.Cost;
import com.cooperate.entity.CostType;
import com.cooperate.gson.CostAdapter;
import com.cooperate.service.CostService;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.List;

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
     * Отображение формы редактирования типа расхода
     *
     * @param id  id типа расхода
     * @param map ModelMap
     * @return costType.jsp
     */
    @RequestMapping(value = "costType/{id}", method = RequestMethod.GET)
    public String editGaragForm(@PathVariable("id") Integer id, ModelMap map) {
        map.addAttribute("costType", service.getType(id));
        return "costType";
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
     * Страница с типами расходов
     *
     * @return страница costs.jsp
     */
    @RequestMapping(value = "costTypesPage", method = RequestMethod.GET)
    public String getCostTypesPage() {
        return "costTypes";
    }

    /**
     * @return список всех расходов
     */
    @RequestMapping(value = "getCosts", method = RequestMethod.GET)
    public ResponseEntity<String> getCosts() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Cost.class, new CostAdapter());
        return Utils.convertListToJson(gson, service.getAll());
    }

    /**
     * @return список всех расходов
     */
    @RequestMapping(value = "getCostTypes", method = RequestMethod.GET)
    public ResponseEntity<String> getCostType() {
        GsonBuilder gson = new GsonBuilder();
        return Utils.convertListToJson(gson, service.getTypes());
    }

    /**
     * @return список типов расходов
     */
    @RequestMapping(value = "getTypes", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<List<CostType>> getTypes() {
        return new ResponseEntity<List<CostType>>(service.getTypes(), HttpStatus.OK);
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
            LOGGER.info("Запись о расходе: " + cost.getType().getName() + " произведена");
            map.put("message", "Запись о расходе произведена");
            return "success";
        } catch (Exception e) {
            map.put("message", "Ошибка по работе с БД! Попробуйте снова выбрать тип.");
            response.setStatus(409);
            return "error";
        }
    }

    /**
     * Изменение типа расхода
     *
     * @param type тип расхода
     * @return сообщение
     */
    @RequestMapping(value = "saveType", method = RequestMethod.POST)
    public String saveCostType(CostType type, ModelMap map, HttpServletResponse response) {
        try {
            if (!service.existType(type)) {
                service.saveType(type);
                LOGGER.info("Запись о типе расхода: " + type.getName() + " произведена");
                map.put("message", "Запись о типе расхода изменена");
                return "success";
            } else {
                map.put("message", "Название типа расхода должно быть уникальным.");
                response.setStatus(409);
                return "error";
            }
        } catch (Exception e) {
            map.put("message", "Ошибка по работе с БД!");
            response.setStatus(409);
            return "error";
        }
    }

    /**
     * Удаление расхода
     *
     * @param id       ID расхода
     * @param map      ModelMap
     * @param response ответ
     * @return сообщение о результате удаления расхода из базы
     */
    @RequestMapping(value = "deleteCost/{id}", method = RequestMethod.POST)
    public String deleteCost(@PathVariable("id") Integer id, ModelMap map, HttpServletResponse response) {
        try {
            service.delete(id);
            LOGGER.info("Расход удален!");
            map.put("message", "Расход удален!");
            return "success";
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно удалить расход");
            response.setStatus(409);
            return "error";
        }
    }

    /**
     * Удаление типа расхода
     *
     * @param id       ID типа расхода
     * @param map      ModelMap
     * @param response ответ
     * @return сообщение о результате удаления расхода из базы
     */
    @RequestMapping(value = "deleteCostType/{id}", method = RequestMethod.POST)
    public String deleteCostType(@PathVariable("id") Integer id, ModelMap map, HttpServletResponse response) {
        try {
            service.deleteType(id);
            LOGGER.info("Тип расхода удален!");
            map.put("message", "Тип расхода удален!");
            return "success";
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно удалить тип расхода");
            response.setStatus(409);
            return "error";
        }
    }
}
