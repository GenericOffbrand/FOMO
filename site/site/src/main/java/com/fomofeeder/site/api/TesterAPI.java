package com.fomofeeder.site.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fomofeeder.site.dao.StockDaoDB;
import com.fomofeeder.site.dao.StockRepo;
import com.fomofeeder.site.model.PricePoint;
import com.fomofeeder.site.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
    //After these tests, every method has attachment @ResponseBody
    //In the actual program the class would have attachment @RestController instead
    //so that each method doesn't require @ResponseBody


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
    "price": 140.42,
    "time": 55
    }
     */

    @Autowired
    StockRepo stockRepo;

    @GetMapping
    @ResponseBody
    //@JsonIgnore
    //@JsonIgnoreProperties("displayPriority")
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
        System.out.println(testStock.getPriceHistory().get(0).getPrice());
        System.out.println(testStock.getPriceHistory().get(0).getTime());

    }

//    @RequestMapping("database")
//    @GetMapping()
//    @ResponseBody
//    public List<Stock> showDataBase()
//    {
//
//
//
//    }

    private StockDaoDB testDAO = new StockDaoDB();
//    @RequestMapping("database")
    @GetMapping(path = "{ticker}")
    @ResponseBody
    public Stock showStock(@PathVariable("ticker") String tickerSymbol)
    {

        return(testDAO.getStock(tickerSymbol));
    }

    @RequestMapping("submit")
    @PostMapping
    @ResponseBody
    public void addStock (@RequestBody Stock stock)
    {
        System.out.println(testDAO.addStock(stock));
    }

    @RequestMapping("delete")
    @DeleteMapping
    @ResponseBody
    public void deleteStock (@RequestBody Stock stock)
    {
        System.out.println(testDAO.deleteStock(stock));
    }
}
