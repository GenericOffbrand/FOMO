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

    public Stock(@JsonProperty("name") String name,
                 @JsonProperty("tickerSymbol") String tickerSymbol)
    {
        this.name = name;
        this.tickerSymbol = tickerSymbol;
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
