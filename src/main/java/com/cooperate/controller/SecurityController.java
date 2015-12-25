package com.cooperate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 * User: velievvm
 * Date: 16.07.15
 * Time: 19:49
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class SecurityController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Неправильный логин или пароль!");
        }
        if (logout != null) {
            model.addAttribute("msg", "Вы вышли");
        }
        return "login";
    }
}
