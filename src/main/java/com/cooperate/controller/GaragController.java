package com.cooperate.controller;

import com.cooperate.Utils;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Person;
import com.cooperate.exception.ExistGaragException;
import com.cooperate.gson.PersonAdapter;
import com.cooperate.service.*;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Контороллер по работе с гаражами
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
    private RentService rentService;

    @Autowired
    private HistoryGaragService historyGaragService;

    private static final Logger logger = Logger.getLogger(GaragController.class);

    /**
     * Получение страницы всех гаражей
     *
     * @param series ряд, по умолчанию 1.
     * @param map    Model map
     * @return garags.jsp
     */
    @RequestMapping(value = "garagPage", method = RequestMethod.GET)
    public String getGaragsPage(@RequestParam(defaultValue = "1", value = "series") String series, ModelMap map) {
        map.addAttribute("setSeries", series); //ряд
        map.addAttribute("series", garagService.getSeries()); //список рядов для nav-tabs
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "garags";
    }

    /**
     * Переход со страницы Владельцев к определенному гаражу
     *
     * @param id     ID гаража
     * @param series ряд гаража
     * @param map    ModelMap
     * @return garags.jsp
     */
    @RequestMapping(value = "linkGarag", method = RequestMethod.GET)
    public String linkGarag(@RequestParam("id") Integer id, @RequestParam("series") String series,
                            ModelMap map) {
        map.addAttribute("setSeries", series);// ряд
        map.addAttribute("series", garagService.getSeries()); //список рядов для nav-tabs
        map.addAttribute("garagId", id); // id выбранного гаража
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "garags";
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
    public ResponseEntity<String> getGarag(@RequestParam(required = false, value = "garag") Integer garag,
                                           @RequestParam("setSeries") String series) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.registerTypeAdapter(Person.class, new PersonAdapter());
        if (garag != null) {
            List<Garag> garags = new ArrayList<Garag>();
            garags.add(garagService.getGarag(garag));
            return Utils.convertListToJson(gsonBuilder, garags);
        }
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return Utils.convertListToJson(gsonBuilder, garagService.findBySeries(series));
    }

    /**
     * Отображение формы добавления гаража
     *
     * @param map ModelMap
     * @return garag.jsp
     */
    @RequestMapping(value = "garag", method = RequestMethod.GET)
    public String addGaragForm(ModelMap map) {
        map.addAttribute("type", "Режим добавления гаража");
        map.addAttribute("isOldGarag", false); //создание гаража
        map.addAttribute("rents", rentService.getRents()); // список периодов системы
        map.addAttribute("garag", new Garag());
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "garag";
    }

    /**
     * Отображение формы редактирования гаража
     *
     * @param id  id гаража
     * @param map ModelMap
     * @return garag.jsp
     */
    @RequestMapping(value = "garag/{id}", method = RequestMethod.GET)
    public String editGaragForm(@PathVariable("id") Integer id, ModelMap map) {
        map.addAttribute("type", "Режим редактирования гаража");
        map.addAttribute("isOldGarag", true); //редактирование гаража
        map.addAttribute("rents", rentService.findAll());
        map.addAttribute("garag", garagService.getGarag(id));
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "garag";
    }

    /**
     * Отображение формы замены владельца гаража
     *
     * @param id  id Гаража
     * @param map ModelMap
     * @return changePerson.jsp
     */
    @RequestMapping(value = "changePerson/{id}", method = RequestMethod.GET)
    public String changePerson(@PathVariable("id") Integer id, ModelMap map) {
        Garag garag = garagService.getGarag(id);
        map.addAttribute("garag", garag);
        map.addAttribute("person", garag.getPerson());
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "changePerson";
    }

    /**
     * Метод для замены владельца у гаража/гаражей
     *
     * @param garagId      id гаража
     * @param oldPersonId  id прошлого владельца
     * @param person       Владелец
     * @param searchPerson Выполнялся ли поиск и замена владельца
     * @param deletePerson Удалять ли предыдущего владельца
     * @param oneGarag     Замена только ли у текущего гаража
     * @param map          ModelMap
     * @return сообщение об успешном выполнении замены владельца
     */
    @RequestMapping(value = "change", method = RequestMethod.POST)
    public String changePerson(Person person,
                               @RequestParam("garag") Integer garagId,
                               @RequestParam("searchPerson") Boolean searchPerson,
                               @RequestParam("deletePerson") Boolean deletePerson,
                               @RequestParam("oldPerson") Integer oldPersonId,
                               @RequestParam("countGarag") Boolean oneGarag,
                               @RequestParam("reason") String reason, ModelMap map) {
        //todo переделать, перенести в сервис
        //Дергаем текущий гараж
        Garag garag = garagService.getGarag(garagId);
        // Если поиск не производился(т.е. новый владелец не из базы и удалять его не надо)
        if (!searchPerson && !deletePerson) {
            //Очищаем id владельца
            person.setId(null);
            //Очищаем адрес
            person.getAddress().setId(null);
        }
        //Если гараж у данного владельца не один, владельца необходимо удалить, поиск 
        if (!oneGarag && deletePerson && !searchPerson) {
            for (Garag g : garag.getPerson().getGaragList()) {
                historyGaragService.saveReason(reason, garag.getPerson().getFIO(), g);
            }
            personService.saveOrUpdate(person);
            logger.info("Владелец заменен!(" + person.getFIO() + ")");
            map.put("message", "Владелец заменен!");
            return "success";
        }
        if (oneGarag) {
            historyGaragService.saveReason(reason, garag.getPerson().getFIO(), garag);
            garag.setPerson(person);
            garagService.save(garag);
        } else {
            Person oldPerson = personService.getPerson(oldPersonId);
            person = personService.saveOrUpdate(person);
            for (Garag g : oldPerson.getGaragList()) {
                historyGaragService.saveReason(reason, garag.getPerson().getFIO(), g);
                g.setPerson(person);
                garagService.save(g);
            }
        }
        if (searchPerson && deletePerson) {
            personService.delete(oldPersonId);
        }
        logger.info("Владелец заменен!(" + person.getFIO() + ")");
        map.put("message", "Владелец заменен!");
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "success";
    }


    //Информационно модальное окно для гаража

    /**
     * Информационное модальное окно для гаража - платежи, долги.
     *
     * @param id  ID Гаража
     * @param map ModelMap
     * @return garagInf.jsp
     */
    @RequestMapping(value = "garagInf", method = RequestMethod.GET)
    public String payModal(@RequestParam("idGarag") Integer id, ModelMap map) {
        Garag garag = garagService.getGarag(id);
        map.addAttribute("contributionAll", garagService.sumContribution(garag));
        map.addAttribute("garag", garag);
        //todo помоему это лишнее
        map.addAttribute("fio", garag.getPerson().getFIO());
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "garagInf";
    }

    /**
     * Печатная форма информации по гаражу - платежи, долги.
     *
     * @param id  ID Гаража
     * @param map ModelMap
     * @return infPrint.jsp
     */
    @RequestMapping(value = "infPrint/{id}", method = RequestMethod.GET)
    public String infGarag(@PathVariable("id") Integer id, ModelMap map) {
        Garag garag = garagService.getGarag(id);
        map.addAttribute("contributionAll", garagService.sumContribution(garag));
        map.addAttribute("garag", garag);
        map.addAttribute("fio", garag.getPerson().getFIO());
        map.addAttribute("now", Calendar.getInstance().getTime());
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "infPrint";
    }

    //Сохранения гаража

    @RequestMapping(value = "saveGarag", method = RequestMethod.POST)
    public String saveGarag(Garag garag, ModelMap map, HttpServletResponse response) {
        try {
            garagService.saveOrUpdate(garag);
            logger.info("Гараж " + garag.getName() + " сохранен!");
            contributionService.updateFines();
            map.put("message", "Гараж сохранен!");
            return "success";
        } catch (ExistGaragException e) {
            logger.error("Невозможно сохранить гараж, он уже существует");
            map.put("message", "Невозможно создать гараж, так как он уже существует!");
            response.setStatus(409);
            return "error";
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getMessage());
            map.put("message", "Ошибка по работе с БД!");
            response.setStatus(409);
            return "error";
        }
    }

    //Поиск имеющихся владельцев

    @RequestMapping(value = "searchPerson", method = RequestMethod.POST)
    public String searchPerson(@RequestParam("pattern") String pattern, ModelMap map) {
        List<Person> persons = personService.findByfio(pattern);
        map.addAttribute("persons", persons);
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return "personRes";
    }

    //Вывод владельца после поиска и внесение в форму данных

    @RequestMapping(value = "getPerson", method = RequestMethod.GET)
    public ResponseEntity<String> getPerson(@RequestParam("personId") Integer id) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        //todo Добавить вариант ошибки подключения к БД, или ошибка запроса
        return Utils.createJsonResponse(gsonBuilder, personService.getPerson(id));
    }

    //Удаление гаража

    @RequestMapping(value = "deleteGarag/{id}", method = RequestMethod.POST)
    public String deleteGarag(@PathVariable("id") Integer id, ModelMap map, HttpServletResponse response) {
        try {
            Garag garag = garagService.getGarag(id);
            garagService.delete(id);
            logger.info("Гараж " + garag.getName() + " удален!");
            map.put("message", "Гараж удален!");
            return "success";
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Невозможно удалить, так как гараж используется!");
            response.setStatus(409);
            return "error";
        }
    }

    //Пересччет долгов по пеням и включение пеней

    @RequestMapping(value = "updateFines", method = RequestMethod.POST)
    public String updateFines(ModelMap map, HttpServletResponse response) {
        try {
            contributionService.updateFines();
            contributionService.onFines(Calendar.getInstance());
            Cookie day_sync = new Cookie("day_sync", "checked");
            response.addCookie(day_sync);
            map.put("message", "Данные успешно обнавленны!");
            return "success";
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Данные не обновились! Обратитесь к администратору ");
            response.setStatus(409);
            return "error";
        }
    }
}
