package com.fomofeeder.site.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

//By using Component code won't compile due to undefined bean of type "String"
//@Component
public class Stock
{
    private String name;
    private String tickerSymbol;
    private double displayPriority;
    //does there have to be a spring bean injection here to make this work?
    private ArrayList<PricePoint> priceHistory = new ArrayList<>();

    //Stock objects need to be able to be created using json strings.
    public Stock(@JsonProperty("name") String name,
                 @JsonProperty("tickerSymbol") String tickerSymbol)
    {
        this.name = name;
        this.tickerSymbol = tickerSymbol;
    }

    //An entire stock object would only be created through a database call
    //There's no need to add @JsonProperty in that case
    public Stock(String name, String tickerSymbol,
                 double displayPriority, ArrayList<PricePoint> priceHistory)
    {
        this.name = name;
        this.tickerSymbol = tickerSymbol;
        this.displayPriority = displayPriority;
        this.priceHistory = priceHistory;
    }

    public String getName()
    {
        return name;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public ArrayList<PricePoint> getPriceHistory()
    {
        return priceHistory;
    }

    public double getDisplayPriority()
    {
        return displayPriority;
    }

    public void setDisplayPriority(double displayPriority)
    {
        this.displayPriority = displayPriority;
    }
}
