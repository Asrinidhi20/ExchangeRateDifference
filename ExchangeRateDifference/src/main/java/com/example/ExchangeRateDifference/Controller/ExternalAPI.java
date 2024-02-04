package com.example.ExchangeRateDifference.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/exchangeRate")
public class ExternalAPI {

    @GetMapping("/latest-usd")
    public String GetDifference(){
        String uri = "https://v6.exchangerate-api.com/v6/eaa8b4782ed09be0e5dd71f3/latest/USD";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }

    
}
