package com.DesignMyNight.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {

    @GetMapping("/checkout.html")
    public String showCheckoutPage() {
        return "checkout";
    }
}

