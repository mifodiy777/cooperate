package com.cooperate.controller;

import com.cooperate.Utils;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Person;
import com.cooperate.gson.PersonAdapter;
import com.cooperate.service.*;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Кирилл on 25.07.2015.
 */

@Controller
public class GaragController {

    @Autowired
    private GaragService garagService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ContributionService contributionService;

    @Autowired
    private JournalHistoryService journalService;

    @Autowired
    private RentService rentService;

    @RequestMapping(value = "garagPage", method = RequestMethod.GET)
    public String getGaragsPage(@RequestParam(defaultValue = "1", value = "series") String series, ModelMap map) {
        map.addAttribute("setSeries", series);
        map.addAttribute("series", garagService.getSeries());
        return "garags";
    }

    @RequestMapping(value = "allGarag", method = RequestMethod.GET)
    public ResponseEntity<String> getGarag(@RequestParam("setSeries") String series) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.registerTypeAdapter(Person.class, new PersonAdapter());
        return Utils.convertListToJson(gsonBuilder, garagService.findBySeries(series));
    }

    @RequestMapping(value = "garag", method = RequestMethod.GET)
    public String addGaragForm(ModelMap map) {
        map.addAttribute("type", "Режим добавления гаража");
        map.addAttribute("editContribute", false);
        map.addAttribute("garag", new Garag());
        return "garag";
    }


    //Информационно модальное окно для гаража

    @RequestMapping(value = "infModal", method = RequestMethod.GET)
    public String payModal(@RequestParam("idGarag") Integer id, ModelMap map,
                           HttpServletResponse response) {
        Garag garag = garagService.getGarag(id);
        if (garag.getPerson() == null) {
            map.put("message", "Гаражу не назначен владелец!");
            response.setStatus(409);
            return "error";
        }
        String name = garag.getPerson().getLastName() + " " + garag.getPerson().getName() +
                " " + garag.getPerson().getFatherName();
        map.addAttribute("contributionAll", garagService.sumContribution(garag));
        map.addAttribute("garag", garag);
        map.addAttribute("name", name);
        return "modalInf";
    }

    //Печатная форма информации по гаражу

    @RequestMapping(value = "infGarag/{id}", method = RequestMethod.GET)
    public String infGarag(@PathVariable("id") Integer id, ModelMap map) {
        Garag garag = garagService.getGarag(id);
        String name = garag.getPerson().getLastName() + " " + garag.getPerson().getName() +
                " " + garag.getPerson().getFatherName();
        map.addAttribute("contributionAll", garagService.sumContribution(garag));
        map.addAttribute("garag", garag);
        map.addAttribute("name", name);
        map.addAttribute("name", name);
        map.addAttribute("now", Calendar.getInstance().getTime());
        return "garagInf";
    }

    //Сохранения гаража

    @RequestMapping(value = "saveGarag", method = RequestMethod.POST)
    public String saveGarag(Garag garag, ModelMap map, HttpServletResponse response) {
        if (garagService.existGarag(garag)) {
            //Если гараж новый и пустой
            if (garag.getId() == null && garag.getPerson() == null) {
                garagService.saveOrUpdate(garag);
                journalService.event("Новый гараж " + garag.getSeries() + "-" + garag.getNumber() + " сохранен!");
                map.put("message", "Гараж сохранен!");
                return "success";
            }
            //Если гараж новый и владелец новый
            if (garag.getId() == null && garag.getPerson().getId() == null) {
                garag.setContributions(contributionService.getContributionOnGarag(garag));
                garagService.saveOrUpdate(garag);
                journalService.event("Новый гараж " + garag.getSeries() + "-" + garag.getNumber() + " сохранен!");
                contributionService.updateFines();
                map.put("message", "Гараж сохранен!");
                return "success";
            }
            //Если гараж новый а владелец уже существует
            if (garag.getId() == null && garag.getPerson().getId() != 0) {
                garag.setContributions(contributionService.getContributionOnGarag(garag));
                garagService.saveOrUpdate(garag);
                journalService.event("Новый гараж " + garag.getSeries() + "-" + garag.getNumber() + " сохранен!");
                contributionService.updateFines();
                map.put("message", "Гараж сохранен!");
                return "success";
            }
            //Редактирование гаража
            if (garag.getId() != null) {
                Garag garagEdit = garagService.getGarag(garag.getId());
                garag.setContributions(garagEdit.getContributions());
                garag.setPayments(garagEdit.getPayments());
            }
            journalService.event("Гараж " + garag.getSeries() + "-" + garag.getNumber() + " изменен!");
            garagService.saveOrUpdate(garag);
            map.put("message", "Гараж сохранен!");
            return "success";
        } else {
            map.put("message", "Невозможно создать, так как гараж уже существует!");
            response.setStatus(409);
            return "error";
        }
    }

    //Редактирование гаража

    @RequestMapping(value = "garag/{id}", method = RequestMethod.GET)
    public String editGaragForm(@PathVariable("id") Integer id, ModelMap map) {
        map.addAttribute("type", "Режим редактирования гаража");
        map.addAttribute("editContribute", true);
        map.addAttribute("rents", rentService.getRents());
        map.addAttribute("now", Calendar.getInstance().get(Calendar.YEAR));
        map.addAttribute("garag", garagService.getGarag(id));
        return "garag";
    }

    //Поиск имеющихся владельцев

    @RequestMapping(value = "searchPerson", method = RequestMethod.POST)
    public String searchPerson(@RequestParam("pattern") String pattern, ModelMap map) {
        List<Person> persons = personService.findByfio(pattern);
        map.addAttribute("persons", persons);
        return "personRes";
    }
    //Вывод владельца после поиска и внесение в форму данных

    @RequestMapping(value = "getPerson", method = RequestMethod.GET)
    public ResponseEntity<String> getPerson(@RequestParam("personId") Integer id) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        return Utils.createJsonResponse(gsonBuilder, personService.getPerson(id));
    }

    //Удаление гаража

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "deleteGarag/{id}", method = RequestMethod.POST)
    public String deleteGarag(@PathVariable("id") Integer id, ModelMap map, HttpServletResponse response) {
        try {
            Garag garag = garagService.getGarag(id);
            garagService.delete(id);
            journalService.event("Гараж " + garag.getSeries() + "-" + garag.getNumber() + " удален!");
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно удалить, так как гараж используется!");
            response.setStatus(409);
            return "error";
        }
        map.put("message", "Гараж удален!");
        return "success";
    }

    //Удалить у гаража владельца(Сам владелец сохраняется)

    @RequestMapping(value = "assignDelete/{id}", method = RequestMethod.POST)
    public String assignDelete(@PathVariable("id") Integer id, ModelMap map, HttpServletResponse response) {
        try {
            Garag garag = garagService.getGarag(id);
            if (garag.getPerson() != null) {
                garag.setPerson(null);
                garagService.saveOrUpdate(garag);
                journalService.event("Назначение у гаража " + garag.getSeries() + "-" + garag.getNumber() + " удалено!");
                map.put("message", "Удалено назначение владельца!");
                return "success";
            } else {
                map.put("message", "У гаража нет владельца!");
                response.setStatus(409);
                return "error";
            }
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно удалить!");
            response.setStatus(409);
            return "error";
        }
    }

    //Пересччет долгов по пеням и включение пеней

    @RequestMapping(value = "updateFines", method = RequestMethod.POST)
    public String updateFines(ModelMap map, HttpServletResponse response) {
        try {
            contributionService.updateFines();
            contributionService.onFines();
            map.put("message", "Данные успешно обнавленны!");
            return "success";
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Данные не обновились! Обратитесь к администратору ");
            response.setStatus(409);
            return "error";
        }
    }
}
