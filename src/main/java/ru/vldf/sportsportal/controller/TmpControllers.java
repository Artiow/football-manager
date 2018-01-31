package ru.vldf.sportsportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vldf.sportsportal.service.UserService;

@Controller
public class TmpControllers {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/matches"})
    public String matchesPage(ModelMap map) {
        userService.setAuthUserIn(map, "name");

        return "matches";
    }

    @GetMapping(value = {"/playgrounds"})
    public String playgroundsPage(ModelMap map) {
        userService.setAuthUserIn(map, "name");

        return "playgrounds";
    }
}