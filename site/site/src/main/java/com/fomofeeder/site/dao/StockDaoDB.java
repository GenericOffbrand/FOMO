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

    //Todo: make it so that either all object data is added or none. Right now, stock table can be updated but
    //Todo: price history will not be added along with it.
    @Override
    public boolean addStock(Stock stock)
    {
        try
        {
            PreparedStatement prepStmt;

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


            prepStmt = dbConnection.prepareStatement("INSERT INTO prices(price, time, stocks_id) " +
                    "VALUES (?, ?, ?)");
            //Price, time, and the stock referenced are critical information for price points.
            for (int i = 0; i < stock.getPriceHistory().size(); i++)
            {
                prepStmt.setDouble(1, stock.getPriceHistory().get(i).getPrice());
                prepStmt.setTimestamp(2, new Timestamp(stock.getPriceHistory().get(i).getTime()));
                prepStmt.setInt(3, addedStockID);

                prepStmt.execute();
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
                    "WHERE stocks_id = " + stockID + " ORDER BY time DESC");

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
        return stock;
    }

    //Decided that this method would overwrite entries with the same timestamp because it's the most general option.
    //If more in depth comparisons were needed, then that would be the responsibility of a higher layer, not the Dao.
    @Override
    public boolean updatePrices(Stock stock)
    {
        try
        {
            PreparedStatement prepStmt;
            dbResultSet = dbStatement.executeQuery("SELECT * FROM stocks WHERE ticker = '" +
                    stock.getTickerSymbol() + "'");
            //We don't want to continue if there is no entry to update.
            if (!dbResultSet.next() ||
                !stock.getName().equalsIgnoreCase(dbResultSet.getString("company_name")))
                {return false;}

            int stockID = dbResultSet.getInt("id");

            //Create a sorted ArrayList to check for overlap
            dbResultSet = dbStatement.executeQuery("SELECT * FROM prices WHERE stocks_id = " +
                    stockID + " ORDER BY time DESC");
            ArrayList<PricePoint> dbPriceHistory = new ArrayList<>();
            ArrayList<PricePoint> objPriceHistory = stock.getPriceHistory();
            while(dbResultSet.next())
            {
                dbPriceHistory.add(new PricePoint(dbResultSet.getDouble("price"),
                        dbResultSet.getTimestamp("time").getTime()));
            }

            int i;
            int k = 0;
            prepStmt = dbConnection.prepareStatement("INSERT INTO prices(price, time, stocks_id) " +
                    "VALUES (?, ?, ?)");
            for (i = 0; i < objPriceHistory.size(); i++)
            {
                while(objPriceHistory.get(i).getTime() <= dbPriceHistory.get(k).getTime())
                {
                    //Deletes previous MySQL price entry if there are duplicate times
                    if (objPriceHistory.get(i).getTime() == dbPriceHistory.get(k).getTime())
                    {
                        prepStmt = dbConnection.prepareStatement("DELETE FROM prices WHERE stocks_id = ? AND " +
                                "time = ?");
//                        dbStatement.executeUpdate("DELETE FROM prices WHERE stocks_id = " +
//                                stockID + " AND time = " + new Timestamp(dbPriceHistory.get(k).getTime()));
                        prepStmt.setInt(1, stockID);
                        prepStmt.setTimestamp(2, new Timestamp(dbPriceHistory.get(k).getTime()));

                        prepStmt.executeUpdate();

                        prepStmt = dbConnection.prepareStatement("INSERT INTO prices(price, time, stocks_id) " +
                                "VALUES (?, ?, ?)");
                    }
                    k++;
                }

                //Adds object price entry to database only after checks for duplication
                prepStmt.setDouble(1, objPriceHistory.get(i).getPrice());
                prepStmt.setTimestamp(2, new Timestamp(objPriceHistory.get(i).getTime()));
                prepStmt.setInt(3, stockID);

                prepStmt.execute();
                k = 0;
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
    public boolean deleteStock(Stock stock)
    {
        try
        {
            dbResultSet = dbStatement.executeQuery("SELECT * FROM stocks WHERE ticker = '" +
                    stock.getTickerSymbol() + "'");
            //Checks existence, ticker, and name to make absolutely sure user of deleteStock is not deleting
            //on accident
            if (!(dbResultSet.next() &&
                stock.getTickerSymbol().equals(dbResultSet.getString("ticker")) &&
                stock.getName().equalsIgnoreCase(dbResultSet.getString("company_name"))))
                {return false;}

            //At this point we're certain of the stock we want to delete, but deletion from stocks table
            //must come after references in prices table
            int stockID = dbResultSet.getInt("id");

            //Price information needs to be deleted as well to make space if a future new stock gets the same
            //ID as the deleted stock
            dbStatement.executeUpdate("DELETE FROM prices WHERE stocks_id = " + stockID);

            //After all prices with the foreign key from stock are deleted, the stock entry in stocks can be
            //deleted as well.
            dbStatement.executeUpdate("DELETE FROM stocks WHERE id = " + stockID);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

}
