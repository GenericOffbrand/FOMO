package com.fomofeeder.site.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class StockController
{
    @RequestMapping("/")
    //@ResponseBody
    public String StockController()
    {
        System.out.println("hi");
        return "redirect:/index.html";
    }




}
