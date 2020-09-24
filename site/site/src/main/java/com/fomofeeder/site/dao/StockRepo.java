package com.fomofeeder.site.dao;

import com.fomofeeder.site.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepo extends JpaRepository<Stock, Integer>
{
}
