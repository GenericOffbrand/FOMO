package com.fomofeeder.site.api;

import com.fomofeeder.site.model.PricePoint;
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


    //Test using this:
    /*
    POST: localhost:8080/test/
    {
    "name": "Apple Computers",
    "tickerSymbol": "APPL"
    }

    POST: localhost:8080/test/stockdata
    {
    "high": 332.00,
    "low" : 320,
    "open": 340.95,
    "close": 429.62,
    "volume": 70440000,
    "percentChange": -19.79,
    "time": 55
    }
     */
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

    @RequestMapping("stockdata")
    @PostMapping
    @ResponseBody
    public void addStockData (@RequestBody PricePoint pricePoint)
    {
        System.out.println("You're adding a price now");
        testStock.getPriceHistory().add(pricePoint);
        System.out.println(testStock.getPriceHistory().get(0).getClose());
        System.out.println(testStock.getPriceHistory().get(0).getHigh());

    }
}
