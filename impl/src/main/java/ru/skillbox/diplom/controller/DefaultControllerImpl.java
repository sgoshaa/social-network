package ru.skillbox.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.diplom.resources.DefaultController;

@Controller
public class DefaultControllerImpl implements DefaultController {

    @Override
    @RequestMapping("/")
    public String getMainPage() {
        return "index";
    }

    @Override
    @RequestMapping("/**/{path:[^\\\\.]*}")
    public String goForward() {
        return "forward:/";
    }
}
