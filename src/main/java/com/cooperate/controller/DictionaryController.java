package com.cooperate.controller;

import com.cooperate.Utils;
import com.cooperate.entity.Dictionary;
import com.cooperate.service.DictionaryService;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@Controller
public class DictionaryController {

    private static final Logger LOGGER = Logger.getLogger(DictionaryController.class);

    @Autowired
    private DictionaryService service;

    /**
     * Страница справочников
     *
     * @return страница dictionary.jsp
     */
    @RequestMapping(value = "dictionaryPage", method = RequestMethod.GET)
    public String getDictionaryPage() {
        return "dictionary";
    }

    /**
     * @return список всех справочных элементов
     */
    @RequestMapping(value = "getDictionary", method = RequestMethod.GET)
    public ResponseEntity<String> getDictionary() {
        GsonBuilder gson = new GsonBuilder();
        return Utils.convertListToJson(gson, service.getDictionary());
    }

    /**
     * Страница с формой добавления справочников
     *
     * @return страница dictionaryItem.jsp
     */
    @RequestMapping(value = "dictionary", method = RequestMethod.GET)
    public String addItemForm(ModelMap map) {
        map.addAttribute("dictionary", new Dictionary());
        return "dictionaryItem";
    }

    /**
     * Отображение формы редактирования типа расхода
     *
     * @param id  id типа расхода
     * @param map ModelMap
     * @return costType.jsp
     */
    @RequestMapping(value = "dictionary/{id}", method = RequestMethod.GET)
    public String editItemForm(@PathVariable("id") Integer id, ModelMap map) {
        map.addAttribute("dictionary", service.getItem(id));
        return "dictionaryItem";
    }

    /**
     * Изменение типа расхода
     *
     * @return сообщение
     */
    @RequestMapping(value = "saveDictionary", method = RequestMethod.POST)
    public String save(Dictionary dictionary, ModelMap map, HttpServletResponse response) {
        try {
            service.save(dictionary);
            LOGGER.info("Запись справочного элемента: " + dictionary.getName() + " произведена");
            map.put("message", "Запись справочного элемента произведена");
            return "success";
        } catch (Exception e) {
            map.put("message", "Ошибка по работе с БД!");
            response.setStatus(409);
            return "error";
        }
    }
}
