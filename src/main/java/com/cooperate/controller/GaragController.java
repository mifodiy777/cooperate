package com.cooperate.controller;

import com.cooperate.entity.Garag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * Контороллер по работе с гаражами
 * Created by Кирилл on 25.07.2015.
 */
@Controller
public class GaragController {

    private static final Logger logger = LoggerFactory.getLogger(GaragController.class);

    /**
     * Получение страницы всех гаражей
     *
     * @param series ряд, по умолчанию 1.
     * @param model    Model map
     * @return garags.jsp
     */
    @GetMapping("/")
    public String getGaragsPage(@RequestParam(defaultValue = "1", value = "series") String series, Model model) {
        model.addAttribute("setSeries", series); //ряд, по умолчанию выбирается ряд "1"
        try {
            model.addAttribute("series", "1"); //список рядов для nav-tabs
            return "garags";
        } catch (DataAccessResourceFailureException e) {
            model.addAttribute("textError", "Ошибка базы данных, проверте подключение к БД");
            return "errorPage";
        }
    }

    /**
     * Получение списка гаражей
     *
     * @param garag  ID Гаража, по умолчанию этого переходп нет.
     *               Параметр появляется только после перехода в гаражи из списка владельцев
     * @param series Ряд
     * @return JSON список гаражей
     */
    @RequestMapping(value = "allGarag", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getGarag(@RequestParam(required = false, value = "garag") Integer garag,
                                   @RequestParam("setSeries") String series) {
        Garag g = new Garag();
        g.setNumber("1");
        g.setSeries("1");
        return ResponseEntity.ok(Collections.singleton(g));
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

}
