package com.fomofeeder.site.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;

//By using Component code won't compile due to undefined bean of type "String"
//@Component
@Entity
@Table(name = "stocks")
public class Stock
{
    //Annotations above variable name are for the connection between Spring Boot and MySQL database
    //names are the column name
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name= "id")
    private int id;

    @Column(name = "company_name")
    private String name;

    @Column(name = "ticker")
    private String tickerSymbol;

    @Column(name = "display_priority")
    private double displayPriority;

    private ArrayList<PricePoint> priceHistory = new ArrayList<>();

    //Todo: remove this default constructor before shipping and solve any compiling issues caused
    //Todo: by the lack of a default constructor. Particularly, when calling StockRepo.findAll()
    public Stock() {}

    //Stock objects need to be able to be created using json strings for functions like
    //web price scraping
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
