package com.cooperate.controller;


import com.cooperate.service.JournalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OtherController {

    @Autowired
    private JournalHistoryService journalService;

    //Страница истории
    @RequestMapping(value = "historyPage", method = RequestMethod.GET)
    public String historyPage(ModelMap map) {
        map.addAttribute("history", journalService.getJournalHistorys());
        return "history";
    }

    //Страница истории
    @RequestMapping(value = "helpPage", method = RequestMethod.GET)
    public String helpPage() {      
        return "help";
    }


}
