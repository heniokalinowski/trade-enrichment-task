package com.verygoodbank.tes;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public class PerformanceTest
{
    private static final int ROWS = 5_000_000;
    private static final int PRODUCTS = 1_000;
    private static final List<String> DATES = List.of("20160101", "20170101", "20180101", "20190101", "20200101", "20210101", "20220101", "20230101", "20240101");
    private static final List<String> CURRENCIES = List.of("EUR", "USD", "PLN");
    
    @Test
    public void performanceTest()
    {
        Random random = new Random();
        TradeEnrichmentConfiguration tradeEnrichmentConfiguration = new TradeEnrichmentConfiguration();
        TradeEnrichmentService tradeEnrichmentService = new TradeEnrichmentService(tradeEnrichmentConfiguration.products());
        
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append("date,product_id,currency,price\n");
        
        for(int i = 0; i < ROWS; i++)
        {
            String date = DATES.get(random.nextInt(DATES.size()));
            int productId = random.nextInt(PRODUCTS) + 1;
            String currency = CURRENCIES.get(random.nextInt(CURRENCIES.size()));
            double price = random.nextDouble();
            
            String line = String.format("%s,%d,%s,%.2f\n", date, productId, currency, price);
            
            stringBuilder.append(line);
        }
        
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(stringBuilder.toString().getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        
        Instant startTime = Instant.now();
        
        tradeEnrichmentService.processStreams(byteArrayInputStream, outputStream);
        
        Instant endTime = Instant.now();
        Duration timeElapsed = Duration.between(startTime, endTime);
        
        System.out.println("Time [ms]: " + timeElapsed.toMillis());
    }
}