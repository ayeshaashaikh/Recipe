package com.ayesha.socialsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {


        @GetMapping("/home")
        public String home() {
            return "home"; // The name of your Thymeleaf template (home.html)
        }
    }


