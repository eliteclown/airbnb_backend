package com.karthik.projects.airBnbApp.services;


import com.karthik.projects.airBnbApp.entities.Booking;

public interface CheckoutService {
    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);
}
