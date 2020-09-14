package com.fomofeeder.site.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PricePoint
{
    //after instantiation, price point object becomes read only
    private final double high;
    private final double low;
    private final double open;
    private final double close;
    private final double volume;
    private final double percentChange;
    private final long time;

    public PricePoint(@JsonProperty("high") double high,
                      @JsonProperty("low") double low,
                      @JsonProperty("open") double open,
                      @JsonProperty("close") double close,
                      @JsonProperty("volume") double volume,
                      @JsonProperty("percentChange") double percentChange,
                      @JsonProperty("time") long time)
    {
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.volume = volume;
        this.percentChange = percentChange;
        this.time = time;
    }


}
