package com.wmp.docparser.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping(path = "/")
    public String hello() {
        return "index";
    }
}
