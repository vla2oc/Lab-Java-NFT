package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class GreetingController {

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("");
        return "about";
    }
    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("");
        return "support";
    }

}
