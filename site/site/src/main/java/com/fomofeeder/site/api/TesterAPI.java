package com.fomofeeder.site.api;

import com.fomofeeder.site.dao.StockDaoDB;
import com.fomofeeder.site.model.PricePoint;
import com.fomofeeder.site.model.Stock;
import com.fomofeeder.site.service.StockScraper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;


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
    @RequestMapping("database")
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

    @RequestMapping("MAZON")
    @GetMapping
    @ResponseBody
    public void addMazonTest ()
    {
        ArrayList<PricePoint> mazonPrices = new ArrayList<>();
        Stock mazon = new Stock("Amazon", "MAZON", 0, mazonPrices);

        mazonPrices.add(new PricePoint(4, 1578182400000L));
        mazonPrices.add(new PricePoint(5, 1578202400000L));

        System.out.println(testDAO.addStock(mazon));
    }

    @RequestMapping("timestamp")
    @GetMapping
    @ResponseBody
    public void timestampTest ()
    {
        System.out.println(new Timestamp(700000));
        System.out.println(new Timestamp(700000L));
        System.out.println(new Timestamp(1578182400000L));
    }

    @RequestMapping("updateTSLA")
    @GetMapping
    @ResponseBody
    public void updateTest()
    {
        //1578182400000L        2020-01-04 16:00:00
        //1578096000000L        2020-01-03 16:00:00
        //86400000L             24 hrs
        ArrayList<PricePoint> tslaPrices = new ArrayList<>();
        Stock tsla = new Stock("Tesla Inc", "TSLA", 0, tslaPrices);

        tslaPrices.add(new PricePoint(421,1578182400000L));
        tslaPrices.add(new PricePoint(520, 1578268800000L));

        System.out.println(testDAO.updatePrices(tsla));
    }

    @RequestMapping("scrapertest")
    @GetMapping
    @ResponseBody
    public void scraperTest()
    {
        StockScraper testScraper = new StockScraper();

        Stock testStock = testScraper.minuteStock("TSLA", "Tesla Inc");

        for(int i = 0; i < testStock.getPriceHistory().size(); i++)
        {
            System.out.println(testStock.getPriceHistory().get(i).getPrice() + "\n"
            + testStock.getPriceHistory().get(i).getTime());
        }
    }


    @RequestMapping("parseTest")
    @GetMapping
    @ResponseBody
    public void jsonParseTest()
    {
        StockScraper testScraper = new StockScraper();

        testScraper.minuteStock("TSLA", "Tesla Inc");
    }

}
