package com.karthik.projects.airBnbApp.Strategy;

import com.karthik.projects.airBnbApp.entities.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface  PricingStrategy {
    BigDecimal calculatePrice(Inventory inventory);
}
