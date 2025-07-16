package com.karthik.projects.airBnbApp.Strategy;

import com.karthik.projects.airBnbApp.entities.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SurgePricingStrategy implements PricingStrategy {


    @Qualifier("surgePricingStrategy")
    private final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return pricingStrategy.calculatePrice(inventory).multiply(inventory.getSurgeFactor());
    }
}
