package com.fomofeeder.site.service;

import com.fomofeeder.site.model.Stock;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class StockScraper
{
    private final String alvaAPIKey = "VAWM0WWOIP6EO0ZM";

    //Timezone for Alpha Vantage API is US/Eastern
    private final String alvaTimeZone = "EST";
    //Hardcoded value for when price starts in the "4. close" line of Alpha Vantage API
    private final int alvaCloseIndex = 13;

    public Stock minuteStock(String ticker, String name)
    {
//        try
//        {
//            URL alvaCall = new URL("https://www.alphavantage.co/query?" +
//                    "function=TIME_SERIES_INTRADAY&" +
//                    "symbol=" + ticker + "&" +
//                    "interval=1min&" +
//                    "outputsize=full&" +
//                    "apikey=" + alvaAPIKey);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        RestTemplate restTemplate = new RestTemplate();

        //Create stock object
        Stock resultStock = new Stock(name, ticker);

        //Call upon the api to retrieve online data about stock

        try
        {
            String sServiceReturnJson = "";

//            URL alvaCall = new URL("https://www.alphavantage.co/query?" +
//                    "function=TIME_SERIES_INTRADAY&" +
//                    "symbol=" + ticker + "&" +
//                    "interval=1min&" +
//                    "outputsize=full&" +
//                    "apikey=" + alvaAPIKey);
            URL alvaCall = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&outputsize=full&apikey=demo");

            BufferedReader br = new BufferedReader(new InputStreamReader(alvaCall.openStream()));
            String strTemp;

            double pointPrice;
            long pointTime;
            String tempTime;

            //Todo: implement code to prime buffered reader to desired point in output JSON

            while (null != (strTemp = br.readLine())) {
                if (strTemp.contains("\": {"))
                {
                    tempTime = strTemp.substring(strTemp.indexOf("\""), strTemp.indexOf(":") - 1);
                    tempTime += " " + alvaTimeZone;

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss z");
                    pointTime = ZonedDateTime.parse(tempTime, dtf).toInstant().toEpochMilli();
                }
                if (strTemp.contains("\"4. c"))
                {
                    pointPrice = Double.parseDouble(strTemp.substring(alvaCloseIndex, strTemp.length() - 2));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //Parse json

        //Add to ArrayList<PricePoint> in stock object


        return null;
    }
}

//Alpha Vantage api key: VAWM0WWOIP6EO0ZM
/*
    Alpha Vantage allows for 5 API calls per minute, and 500 per day. They have a variety of call types from interday
    stock data to historical daily stock data.

    Interday, 1 minute granularity, seems to have a 14 day range (no data for weekends, test showed 10 days of data)

    Demo links for Alpha Vantage api:
    https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo
    https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&outputsize=full&apikey=demo
 */