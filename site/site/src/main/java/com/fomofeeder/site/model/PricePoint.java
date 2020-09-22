package com.fomofeeder.site.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PricePoint
{
    //after instantiation, price point object becomes read only
    private final double price;
    private final long time;

    public PricePoint(@JsonProperty("high") double price,
                      @JsonProperty("time") long time)
    {
        this.price = price;
        this.time = time;
    }

    public double getPrice()
    {
        return price;
    }

    public long getTime()
    {
        return time;
    }
}
