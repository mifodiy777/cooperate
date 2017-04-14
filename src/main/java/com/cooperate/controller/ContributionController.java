package com.cooperate.controller;

import com.cooperate.editor.CalendarCustomEditor;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;
import com.cooperate.service.ContributionService;
import com.cooperate.service.GaragService;
import com.cooperate.service.RentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

@Controller
public class ContributionController {

    @Autowired
    private GaragService garagService;

    @Autowired
    private ContributionService contributionService;

    @Autowired
    private RentService rentService;

    private final Logger logger = Logger.getLogger(ContributionController.class);

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Calendar.class, new CalendarCustomEditor());
    }

    /**
     * Форма добавления старых периодов
     * @param id ID Гаража
     * @param year год периода
     * @param map ModelMap
     * @return страница modalEditContribute.jsp
     */
    @RequestMapping(value = "editContribute", method = RequestMethod.GET)
    public String editContributeForm(@RequestParam("idGarag") Integer id,
                                     @RequestParam("year") Integer year, ModelMap map) {
        map.addAttribute("contribution", contributionService.getContributionByGaragAndYear(id, year));
        map.addAttribute("garag", garagService.getGarag(id));
        map.addAttribute("max", rentService.findByYear(year));
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "modalEditContribute";
    }

    /**
     * Добавление старых периодов
     * @param contribute Период
     * @param id ID Гаража
     * @param map ModelMap
     * @param response Response
     * @return
     */
    @RequestMapping(value = "saveContribute", method = RequestMethod.POST)
    public String saveContribute(Contribution contribute,
                                 @RequestParam("idGarag") Integer id,
                                 ModelMap map, HttpServletResponse response) {
        Garag garag = garagService.getGarag(id);
        try {
            if (contribute.getId() != null) {
                contributionService.saveOrUpdate(contribute);
            } else {
                garag.getContributions().add(contribute);
                garagService.save(garag);
            }
            logger.info("Долг для гаража " + garag.getName() + " за " +
                    contribute.getYear() + " год назначен");
            map.put("message", "Долг за " + contribute.getYear() + " год введен успешно!");
            return "success";
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно сохранить долг");
            response.setStatus(409);
            return "error";
        }
    }

}
