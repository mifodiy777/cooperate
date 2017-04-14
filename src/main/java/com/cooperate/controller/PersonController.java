package com.cooperate.controller;

import com.cooperate.Utils;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Person;
import com.cooperate.gson.PersonAdapter;
import com.cooperate.gson.PersonPageAdapter;
import com.cooperate.service.GaragService;
import com.cooperate.service.PersonService;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private GaragService garagService;

    private final Logger logger = Logger.getLogger(PersonController.class);

    @RequestMapping(value = "persons", method = RequestMethod.GET)
    public ModelAndView getPersonsPage() {
        return new ModelAndView("persons");
    }

    @RequestMapping(value = "allPerson", method = RequestMethod.GET)
    public ResponseEntity<String> getPersons() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Person.class, new PersonPageAdapter());
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return Utils.convertListToJson(gson, personService.getPersons());
    }

    @RequestMapping(value = "membersPage", method = RequestMethod.GET)
    public ModelAndView getPavilionsPage() {
        return new ModelAndView("members");
    }

    @RequestMapping(value = "members", method = RequestMethod.GET)
    public ResponseEntity<String> getMembers() {
        GsonBuilder gson = new GsonBuilder();
        gson.excludeFieldsWithoutExposeAnnotation();
        gson.registerTypeAdapter(Person.class, new PersonAdapter());
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return Utils.convertListToJson(gson, personService.getMembers());
    }

    @RequestMapping(value = "person", method = RequestMethod.GET)
    public String addPersonForm(ModelMap map) {
        map.addAttribute("type", "Режим добавления владельца");
        map.addAttribute("person", new Person());
        return "person";
    }


    @RequestMapping(value = "savePerson", method = RequestMethod.POST)
    public String savePerson(Person person, ModelMap map) {
        personService.saveOrUpdate(person);
        logger.info("Владелец сохранен!(" + person.getFIO() + ")");
        map.put("message", "Владелец сохранен!");
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "success";
    }

    @RequestMapping(value = "person/{id}", method = RequestMethod.GET)
    public String editPersonForm(@PathVariable("id") Integer id, ModelMap map) {
        map.addAttribute("type", "Режим редактирования владельца");
        map.addAttribute("person", personService.getPerson(id));
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "person";
    }


    //Удаление назначения к гаражу из режима редактирования владельца
    @RequestMapping(value = "deleteGaragInPerson", method = RequestMethod.POST)
    public String deletePerson(@RequestParam("idGarag") Integer idGarag,
                               ModelMap map, HttpServletResponse response) {
        try {
            Garag garag = garagService.getGarag(idGarag);
            garag.setPerson(null);
            garagService.save(garag);
            logger.info("Удален гараж у владельца(" + garag.getName() + ")");
            map.put("message", "Назначение удаленно!");
            return "success";
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно удалить, так как гараж используется!");
            response.setStatus(409);
            return "error";
        }
    }


    @RequestMapping(value = "deletePerson/{id}", method = RequestMethod.POST)
    public String deleteGarag(@PathVariable("id") Integer id, ModelMap map, HttpServletResponse response) {
        try {
            for (Garag garag : personService.getPerson(id).getGaragList()) {
                garag.setPerson(null);
                garagService.save(garag);
            }
            Person person = personService.getPerson(id);
            logger.info("Владелец удаленн(" + person.getFIO() + ")");
            personService.delete(id);
            map.put("message", "Владелец удален!");
            return "success";
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно удалить, так как владелец используется!");
            response.setStatus(409);
            return "error";
        }
    }
}
