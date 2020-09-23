package com.fomofeeder.site.dao;

import com.fomofeeder.site.model.Stock;

public interface StockDao
{
    //Any data access object (file or database) will need to be able to
    //do any of these methods
    public void addStock(Stock stock);
    public Stock getStock(String tickerSymbol);
    public Stock getStock(int id);
    public Stock refreshPrices (Stock stock);
    public void updatePrices (Stock stock);
    public void deleteStock (Stock stock);
}
