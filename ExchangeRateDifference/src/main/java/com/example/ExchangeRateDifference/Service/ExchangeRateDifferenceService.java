package com.example.ExchangeRateDifference.Service;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.json.JSONObject;
import java.lang.String;

@Service
public class ExchangeRateDifferenceService {

    public String getUsdExchangeRateByDate(String date) {
        LocalDate Date = LocalDate.parse(date);
        System.out.println(date);
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("exchange_rates");
            String collectionName = "exchange_data";
            if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
                database.createCollection(collectionName);
            }
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Document existingDocument = collection.find(Filters.eq("_id", formatDate(Date))).first();
            System.out.println(existingDocument);
            // return ExchangeRate;
            if (existingDocument != null) {
                System.out.println("Exchange rate data already exists for " + date);
                return existingDocument.toJson();
            } else {
                System.out.println("Exchange rate data stored in MongoDB for " + date);
                return "Data is not there for " + date;
            }
        }
    }

    public String createExchangeRate(String exchangeRateData) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")){
            MongoDatabase database = mongoClient.getDatabase("exchange_rates");
            String collectionName = "exchange_data";
            if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
                database.createCollection(collectionName);
            }
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Document document = Document.parse(exchangeRateData);
            String date = document.getString("id");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate Date = LocalDate.parse(date,formatter);
            document.put("_id", formatDate(Date)); // Set document ID to the date
            document.put("timestamp", LocalDate.now().toString()); // Record timestamp
            
            // Check if the record already exists
            Document existingRecord = collection.find(new Document("_id", formatDate(Date))).first();
            if (existingRecord != null) {
                System.out.println("Exchange rate data already exists for " + date);
                return "Record is already present";
            } else {
                collection.insertOne(document);
                System.out.println("Exchange rate data stored in MongoDB for " + date);
                return "Exchange rate data inserted for " + date;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred during insertion";
        }
    }    

    public String updateExchangeRate(String exchangeRateData, LocalDate date) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")){
            MongoDatabase database = mongoClient.getDatabase("exchange_rates");
            String collectionName = "exchange_data";
            if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
                database.createCollection(collectionName);
            }
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Document document = Document.parse(exchangeRateData);
            document.put("_id", formatDate(date)); // Set document ID to the date
            document.put("timestamp", LocalDate.now().toString()); // Update timestamp
            UpdateResult updateResult = collection.replaceOne(new Document("_id", formatDate(date)), document);
            System.out.println("Exchange rate data updated in MongoDB for " + date);
            if (updateResult.getModifiedCount() > 0) {
                System.out.println("Exchange rate data updated in MongoDB for " + date);
                return document.toJson(); // Return the updated document as JSON
            } else {
                System.out.println("Exchange rate data not found for " + date);
                return "Record not found"; // Return a message indicating record not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred during update"; // Return an error message
        }
    }

    public String deleteExchangeRate(LocalDate date) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")){
            MongoDatabase database = mongoClient.getDatabase("exchange_rates");
            String collectionName = "exchange_data";
            if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
                database.createCollection(collectionName);
            }
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Document deletedRecord = collection.findOneAndDelete(new Document("_id", formatDate(date)));
            System.out.println("Exchange rate data deleted from MongoDB for " + date);
            if (deletedRecord != null) {
                String deletedId = deletedRecord.getString("_id");
                System.out.println("Exchange rate data deleted from MongoDB for " + deletedId);
                return deletedId; // Return the ID of the deleted record
            } else {
                System.out.println("Exchange rate data not found for " + date);
                return "Record not found"; // Return a message indicating record not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred during deletion";
        }
        
    }

    public void retrieveAndStoreUsdExchangeRates(LocalDate date) {

        String apiKey = "https://v6.exchangerate-api.com/v6/eaa8b4782ed09be0e5dd71f3/latest/USD";
        LocalDate endDate = LocalDate.now(); // Today's date
        LocalDate startDate = endDate.minusYears(1); // One year ago

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("exchange_rates");

            // Create the collection if it doesn't exist
            String collectionName = "exchange_data";
            if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
                database.createCollection(collectionName);
            }

            MongoCollection<Document> collection = database.getCollection(collectionName);

            while (!startDate.isAfter(endDate)) {
                Document existingDocument = collection.find(Filters.eq("_id", formatDate(startDate))).first();

                if (existingDocument == null) {
                    fetchDataAndStoreInMongoDB(apiKey, startDate, collection);
                } else {
                    System.out.println("Data already exists for " + startDate);
                }

                // Move to the next date
                startDate = startDate.plusDays(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchDataAndStoreInMongoDB(String apiKey, LocalDate date, MongoCollection<Document> collection) {
        try {
            URL url = new URL("https://v6.exchangerate-api.com/v6/" + "eaa8b4782ed09be0e5dd71f3" + "/history/USD/" + formatDate(date));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Store response in MongoDB
                storeResponseInMongoDB(response.toString(), date, collection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeResponseInMongoDB(String response, LocalDate date, MongoCollection<Document> collection) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            Document document = new Document();
            document.put("_id", formatDate(date));
            document.put("year", date.getYear());
            document.put("month", date.getMonthValue());
            document.put("day", date.getDayOfMonth());
            document.put("base_code", jsonResponse.getString("base_code"));
            JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");
            for (String currency : conversionRates.keySet()) {
                document.put(currency, conversionRates.getDouble(currency));
            }
            collection.insertOne(document);
            System.out.println("Response stored in MongoDB successfully for " + date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
    
}
