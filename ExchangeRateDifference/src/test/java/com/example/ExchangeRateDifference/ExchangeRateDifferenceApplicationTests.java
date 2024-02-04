package com.example.ExchangeRateDifference;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
// import java.util.Map;

// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.junit.MockitoJUnitRunner;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

import com.example.ExchangeRateDifference.Controller.ExchangeRateDifferenceController;
import com.example.ExchangeRateDifference.Service.ExchangeRateDifferenceService;

@SpringBootTest
class ExchangeRateDifferenceApplicationTests {

	@Mock
    private ExchangeRateDifferenceService exchangeRateService;

    @InjectMocks
    private ExchangeRateDifferenceController controller;

	@Test
	void contextLoads() {
	}

	
    @Test
    public void testGetUsdExchangeRatesByDate_Positive() {
        String currentDate = LocalDate.now().toString();
        String mockResponse = "{\"_id\": \"" + currentDate + "\", \"exchangeRate\": 1.23}"; // Mock response with current date

        // Mock service method behavior
        when(exchangeRateService.getUsdExchangeRateByDate(currentDate)).thenReturn(mockResponse);

        // Call the controller method
        String response = controller.getUsdExchangeRatesByDate(currentDate);

        // Assert the response contains the expected date
        assertEquals(mockResponse, response);
    }

    @Test
    public void testGetUsdExchangeRatesByDate_Negative() {
        String futureDate = LocalDate.now().plusDays(1).toString(); // Future date
        String expectedResponse = "Data is not there for "+ futureDate;
		String mockResponse = "Data is not there for "+ futureDate;;

        // Mock service method behavior
        when(exchangeRateService.getUsdExchangeRateByDate(futureDate)).thenReturn(mockResponse);

        // Call the controller method
        String response = controller.getUsdExchangeRatesByDate(futureDate);

        // Assert the response for future date
        assertEquals(expectedResponse,response);
    }

	@Test
    public void testCreateExchangeRate_Positive() {
        String mockData = "{\"id\":\"2025/02/05\",\"year\":2023,\"month\":2,\"day\":5,\"base_code\":\"USD\",\"FJD\":2.16227327,\"MXN\":18.91712629,\"SCR\":13.38323558,\"INR\":82.21741995}";
        String expectedResponse = "Exchange rate data inserted for 2025/02/05";
		String mockResponse = "Exchange rate data inserted for 2025/02/05";

        // Mock service method behavior
        when(exchangeRateService.getUsdExchangeRateByDate(mockData)).thenReturn(mockResponse);

        // Call the controller method
        String response = controller.getUsdExchangeRatesByDate(mockData);

        // Assert the response for future date
        assertEquals(expectedResponse,response);
    }

	@Test
    public void testCreateExchangeRate_Negative() {
		String mockData = "{\"id\":\"2023/02/05\",\"year\":2023,\"month\":2,\"day\":5,\"base_code\":\"USD\",\"FJD\":2.16227327,\"MXN\":18.91712629,\"SCR\":13.38323558,\"INR\":82.21741995}";
        String expectedResponse = "Record is already present";
		String mockResponse = "Record is already present";

        // Mock service method behavior
        when(exchangeRateService.createExchangeRate(mockData)).thenReturn(mockResponse);

        // Call the controller method
        String response = controller.createExchangeRate(mockData);

        // Assert the response for future date
        assertEquals(expectedResponse,response);
    }

	@Test
    public void testUpdateExchangeRate_Positive() {
		LocalDate Date = LocalDate.now();
        String mockData = "{\"exchangeRate\": 1.35,\"currency\": \"USD\",\"date\": \"2023-02-05\"}";
        String expectedResponse = "Exchange rate data inserted for 2025/02/05";
		String mockResponse = "Exchange rate data inserted for 2025/02/05";

        // Mock service method behavior
        when(exchangeRateService.updateExchangeRate(mockData,Date)).thenReturn(mockResponse);

        // Call the controller method
        String response = controller.updateExchangeRate(Date.toString(),mockData);

        // Assert the response for future date
        assertEquals(expectedResponse,response);
    }

	@Test
    public void testUpdateExchangeRate_Negative() {
		LocalDate Date = LocalDate.now();
		String mockData = "{\"exchangeRate\": 1.35,\"currency\": \"USD\",\"date\": \"2023-02-05\"}";
        String expectedResponse = "Record is already present";
		String mockResponse = "Record is already present";

        // Mock service method behavior
        when(exchangeRateService.updateExchangeRate(mockData,Date)).thenReturn(mockResponse);

        // Call the controller method
        String response = controller.updateExchangeRate(Date.toString(),mockData);

        // Assert the response for future date
        assertEquals(expectedResponse,response);
    }

	@Test
    public void testDeleteExchangeRate_Positive() {
		LocalDate Date = LocalDate.now();
        String expectedResponse = "Deleted Successfully";
		String mockResponse = "Deleted Successfully";

        // Mock service method behavior
        when(exchangeRateService.deleteExchangeRate(Date)).thenReturn(mockResponse);

        // Call the controller method
        String response = controller.deleteExchangeRate(Date.toString());

        // Assert the response for future date
        assertEquals(expectedResponse,response);
    }

	@Test
    public void testDeleteExchangeRate_Negative() {
		LocalDate FutureDate = LocalDate.now().plusDays(10);
        String expectedResponse = "Deleted Successfully";
		String mockResponse = "Deleted Successfully";

        // Mock service method behavior
        when(exchangeRateService.deleteExchangeRate(FutureDate)).thenReturn(mockResponse);

        // Call the controller method
        String response = controller.deleteExchangeRate(FutureDate.toString());

        // Assert the response for future date
        assertEquals(expectedResponse,response);
    }

	

}
