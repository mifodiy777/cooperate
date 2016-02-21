package com.cooperate.controller;

import com.cooperate.entity.Garag;
import com.cooperate.service.ContributionService;
import com.cooperate.service.FileParseService;
import com.cooperate.service.GaragService;
import com.cooperate.service.JournalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class FileParseController {

    @Autowired
    private FileParseService fileParseService;

    @Autowired
    private JournalHistoryService historyService;

    @Autowired
    private ContributionService contributionService;

    @Autowired
    private GaragService garagService;

    //Страница конвертации
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "fileUploadPage", method = RequestMethod.GET)
    public ModelAndView getFileUploadPage() {
        return new ModelAndView("fileUploadPage");
    }


    //Конвертация
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "benefits", defaultValue = "false") Boolean benefits,
                                   ModelMap map, HttpServletResponse response) {
        if (!file.isEmpty()) {
            if (benefits) {
                fileParseService.parsePerson(file);
            } else {
                fileParseService.parseGarag(file);
            }
            historyService.event("Ковертирование выполненно!");
            map.put("message", "Конвертация выполненна!");
            return "success";
        } else {
            map.put("message", "Файл не найден");
            response.setStatus(409);
            return "error";
        }
    }
}
