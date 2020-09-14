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



    //methods for testing
    //will not be present in final product.

    //redirects back and forth until browser stops process. Logs all redirects into console
    @RequestMapping("/ping.html")
    public String PingController()
    {
        System.out.println("PING");
        return "redirect:/pong.html";
    }

    @RequestMapping("/pong.html")
    public String PongController()
    {
        System.out.println("PONG");
        return "redirect:/ping.html";
    }
}
