package com.karthik.projects.airBnbApp.Strategy;

import com.karthik.projects.airBnbApp.entities.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService {

    public BigDecimal calculateDynamicPricing(Inventory inventory){
        PricingStrategy pricingStrategy = new BasePricingStrategy();

        //apply the additional Strategies

        pricingStrategy =  new SurgePricingStrategy(pricingStrategy);
        pricingStrategy =  new OccupancyPricingStrategy(pricingStrategy);
        pricingStrategy =  new UrgencyPricingStrategy(pricingStrategy);
        pricingStrategy =  new HolidayPricingStrategy(pricingStrategy);

        return pricingStrategy.calculatePrice(inventory);
    }
}
