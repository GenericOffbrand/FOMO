package com.fomofeeder.site.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "prices")
public class PricePoint
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    //after instantiation, price point object becomes read only
    @Column(name = "price")
    private final double price;
    @Column(name = "time")
    private final long time;

    @ManyToOne()
    @JoinColumn(name = "stocks_id")
    private Stock parentStock;

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
