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
    public boolean addStock(Stock stock)
    {
        try
        {
            //Every stock has a unique ticker. There must be a check that no stock in DB matches object's ticker
            //before the object is added into database.
            dbResultSet = dbStatement.executeQuery("SELECT * FROM stocks WHERE ticker = '" +
                    stock.getTickerSymbol() + "'");
            //We don't want to continue with adding if there's already a stock with the same ticker in the DB
            if (dbResultSet.next())
                return false;


            dbStatement.execute("INSERT INTO stocks(ticker, company_name, display_priority) " +
                    "VALUES ('" + stock.getTickerSymbol() + "', '" + stock.getName() + "', " +
                    stock.getDisplayPriority() + ")");

            //MySQL database has auto-increment for the ID in the 'stocks' table. Select the new stock
            //and find the ID that was assigned to it.
            int addedStockID;

            dbResultSet = dbStatement.executeQuery("SELECT * FROM stocks " +
                    "WHERE ticker = '" + stock.getTickerSymbol() + "'");
            dbResultSet.next(); addedStockID = dbResultSet.getInt("id");


            //Price, time, and the stock referenced are critical information for price points.
            for (int i = 0; i < stock.getPriceHistory().size(); i++)
            {
                dbStatement.execute("INSERT INTO prices(price, time, stocks_id) " +
                        "VALUES (" + stock.getPriceHistory().get(i).getPrice() + ", " +
                        stock.getPriceHistory().get(i).getTime() + ", " + addedStockID +
                        ")");
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public Stock getStock(String tickerSymbol)
    {
        try{

            dbResultSet = dbStatement.executeQuery("SELECT * FROM stocks " +
                    "WHERE ticker = '" + tickerSymbol + "'");
            dbResultSet.next();

            return createStockPOJO(dbResultSet);
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
        try{

            dbResultSet = dbStatement.executeQuery("SELECT * FROM stocks " +
                    "WHERE id = " + id);
            dbResultSet.next();

            return createStockPOJO(dbResultSet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    //Given a result set that references a certain stock, return an object from the database
    private Stock createStockPOJO(ResultSet rs)
    {
        try
        {
            ArrayList<PricePoint> priceHistory = new ArrayList<>();

            int stockID = rs.getInt("id");
            //Empty ArrayList for Stock can still be altered since reference is still stored in variable priceHistory
            Stock requestedStock = new Stock(rs.getString("company_name"),
                    rs.getString("ticker"), rs.getDouble("display_priority"),
                    priceHistory);


            //begin to populate all price data related to requested stock
            rs = dbStatement.executeQuery("SELECT * FROM prices " +
                    "WHERE stocks_id = " + stockID);

            while(rs.next())
            {
                priceHistory.add(new PricePoint(rs.getDouble("price"),
                        rs.getTimestamp("time").getTime()));
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
    public Stock refreshPrices(Stock stock)
    {
        return null;
    }

    @Override
    public void updatePrices(Stock stock)
    {

    }

    @Override
    public boolean deleteStock(Stock stock)
    {
        return false;
    }
}
