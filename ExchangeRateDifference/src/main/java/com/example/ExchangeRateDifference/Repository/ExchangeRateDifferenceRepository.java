package com.example.ExchangeRateDifference.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.ExchangeRateDifference.Model.ExchangeRate;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateDifferenceRepository extends MongoRepository<ExchangeRate, String>{
    List<ExchangeRate> findByDate(LocalDate date);

    
} 

