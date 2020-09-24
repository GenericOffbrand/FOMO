package com.fomofeeder.site.dao;

import com.fomofeeder.site.model.PricePoint;
import com.fomofeeder.site.model.Stock;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class StockDaoDB implements StockDao
{
    private final String dbURL = "jdbc:mysql://localhost:3306/fomo_stock";
    private final String dbUser = "root";
    private final String dbPass = "password1";

    private Connection dbConnection;
    private Statement dbStatement;
    private PreparedStatement dbPreparedStatement;
    private ResultSet dbResultSet = null;

    public StockDaoDB ()
    {
        try
        {
            dbConnection = DriverManager.getConnection(dbURL, dbUser, dbPass);
            dbStatement = dbConnection.createStatement();
        }
        catch (Exception e)
        {
            System.out.println("Bro, connection ain't working.");
            e.printStackTrace();
        }

    }

    @Override
    public void addStock(Stock stock)
    {


    }

    @Override
    public Stock getStock(String tickerSymbol)
    {
        try{

            ArrayList<PricePoint> priceHistory = new ArrayList<>();

            dbResultSet = dbStatement.executeQuery("SELECT * FROM stocks " +
                    "WHERE ticker = '" + tickerSymbol + "'");

            dbResultSet.next();

            int stockID = dbResultSet.getInt("id");
            //Empty ArrayList for Stock can still be altered since reference is still stored in variable priceHistory
            Stock requestedStock = new Stock(dbResultSet.getString("company_name"),
                    dbResultSet.getString("ticker"), dbResultSet.getDouble("display_priority"),
                    priceHistory);


            //begin to populate all price data related to requested stock
            dbResultSet = dbStatement.executeQuery("SELECT * FROM prices " +
                    "WHERE stocks_id = " + stockID);

            while(dbResultSet.next())
            {
                priceHistory.add(new PricePoint(dbResultSet.getDouble("price"),
                        dbResultSet.getTimestamp("time").getTime()));
            }


            return requestedStock;
        }
        catch (Exception e)
        {

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Stock getStock(int id)
    {
        return null;
    }

    @Override
    public Stock refreshPrices(Stock stock)
    {
        return null;
    }

    @Override
    public void updatePrices(Stock stock)
    {

    }

    @Override
    public void deleteStock(Stock stock)
    {

    }
}
