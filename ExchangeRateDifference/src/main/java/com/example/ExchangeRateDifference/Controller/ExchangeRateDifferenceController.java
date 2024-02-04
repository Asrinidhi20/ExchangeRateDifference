package com.example.ExchangeRateDifference.Controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.ExchangeRateDifference.Service.ExchangeRateDifferenceService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/exchangeRate")
public class ExchangeRateDifferenceController {
    
    @Autowired
    private ExchangeRateDifferenceService exchangeRateService;

    @GetMapping("/exchangeRateByDate/{date}")
    public String getUsdExchangeRatesByDate(@PathVariable String date) {
        return exchangeRateService.getUsdExchangeRateByDate(date);
    }

    @PostMapping("/create")
    public String createExchangeRate(@RequestBody String exchangeRate) {
        return exchangeRateService.createExchangeRate(exchangeRate);
    }

    @PutMapping("/update/{id}")
    public String updateExchangeRate(@PathVariable String id, @RequestBody String updatedExchangeRate) {
        LocalDate date = LocalDate.parse(id);
        return exchangeRateService.updateExchangeRate(updatedExchangeRate,date);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteExchangeRate(@PathVariable String id) {
        LocalDate date = LocalDate.parse(id);
        exchangeRateService.deleteExchangeRate(date);
        return "Deleted Successfully";
    }

    @GetMapping("/diff")
    public ResponseEntity<Map<String, Double>> getExchangeRateDifference(@RequestParam String code) {
        Map<String, Double> result = new HashMap<>();
       
        String currency = code; // Currency to analyze
        String baseCurrency = "USD"; // Base currency

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("exchange_rates");
            String collectionName = "exchange_data";

            // Check if the collection exists, create it if not
            if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
                database.createCollection(collectionName);
            }

            MongoCollection<Document> collection = database.getCollection(collectionName);

            // Calculate percentage change for different time intervals
            String[] intervals = {"Last one day", "Last one week", "Last one Month", "Last 3 Month", "Last 6 Month", "Last 9 Month", "Last 12 Month"};
            for (String interval : intervals) {
                LocalDate endDate = LocalDate.now();
                LocalDate startDate;
                switch (interval) {
                    case "Last one day":
                        startDate = endDate.minusDays(1);
                        break;
                    case "Last one week":
                        startDate = endDate.minusWeeks(1);
                        break;
                    case "Last one Month":
                        startDate = endDate.minusMonths(1);
                        break;
                    case "Last 3 Month":
                        startDate = endDate.minusMonths(3);
                        break;
                    case "Last 6 Month":
                        startDate = endDate.minusMonths(6);
                        break;
                    case "Last 9 Month":
                        startDate = endDate.minusMonths(9);
                        break;
                    case "Last 12 Month":
                        startDate = endDate.minusYears(1);
                        break;
                    default:
                        startDate = endDate;
                        break;
                }

                // Calculate percentage change for the given currency
                double percentageChange = calculatePercentageChange(collection, currency, baseCurrency, startDate, endDate);
                result.put(interval, percentageChange);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private static double calculatePercentageChange(MongoCollection<Document> collection, String currency, String baseCurrency, LocalDate startDate, LocalDate endDate) {
        // Retrieve exchange rates for the given currency and base currency for the specified time period
        List<Double> exchangeRates = new ArrayList<>();
        List<String> ids = Arrays.asList(formatDate(startDate), formatDate(endDate));
        collection.find(Filters.in("_id", ids)).forEach(document -> {
            if (document.containsKey(currency)) {
                exchangeRates.add(document.getDouble(currency));
            }
        });

        // Calculate percentage change
        System.out.println(exchangeRates);
        System.out.println(ids);
        if (exchangeRates.size() < 2) {
            // Not enough data to calculate percentage change
            return 0.0;
        } else {
            double initialRate = exchangeRates.get(0);
            double finalRate = exchangeRates.get(exchangeRates.size() - 1);
            System.out.println(initialRate);
            System.out.println(finalRate);
            double percentageChange = ((finalRate - initialRate) / initialRate) * 100;
            return Double.parseDouble(String.format("%.2f", percentageChange));

        }
    }

    private static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
        
}


    
