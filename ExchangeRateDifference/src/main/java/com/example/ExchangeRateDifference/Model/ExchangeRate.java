package com.example.ExchangeRateDifference.Model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "exchange_data")
@EntityScan
public class ExchangeRate {
    @Id
    private String _id;
    private LocalDate date;
    private LocalDateTime year;
    private LocalDateTime month;
    private String base_code;
    private Map<String, Double> conversion_rates;
  
}
