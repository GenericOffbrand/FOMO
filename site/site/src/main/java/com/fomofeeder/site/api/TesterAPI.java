package com.fomofeeder.site.api;

import com.fomofeeder.site.model.Stock;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


//to access tester through browser, use url localhost:8080/test/...
@Controller
@RequestMapping("test")
public class TesterAPI
{
    //methods for testing
    //will not be present in final product.

    //storage variables for testing
    private String testString;
    private Stock testStock;


    //redirects back and forth until browser stops process. Logs all redirects into console
    @RequestMapping("/ping.html")
    public String PingController()
    {
        System.out.println("PING");
        return "redirect:/test/pong.html";
    }

    @RequestMapping("/pong.html")
    public String PongController()
    {
        System.out.println("PONG");
        return "redirect:/test/ping.html";
    }
//
//    //test works as intended. allows REST POST to store string and GET to retrieve
//    @GetMapping
//    @ResponseBody
//    public String giveTestString()
//    {
//        return testString;
//    }
//
//    @PostMapping
//    @ResponseBody
//    public void setTestString(@RequestBody String testString)
//    {
//        this.testString = testString;
//    }

    @GetMapping
    @ResponseBody
    public Stock getTestStock()
    {
        return testStock;
    }

    @PostMapping
    @ResponseBody
    public void setTestStock(@RequestBody Stock testStock)
    {
        this.testStock = testStock;
        System.out.println(this.testStock.getName());
        System.out.println(this.testStock.getTickerSymbol());
    }
}
