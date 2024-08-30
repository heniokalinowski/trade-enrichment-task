package com.verygoodbank.tes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

class TradeEnrichmentApplicationTests
{
    @Test
    public void testEnrichTrades()
    {
        TradeEnrichmentConfiguration tradeEnrichmentConfiguration = new TradeEnrichmentConfiguration();
        Map<String, String> products = tradeEnrichmentConfiguration.products();

        TradeEnrichmentService tradeEnrichmentService = new TradeEnrichmentService(products);

        String inputData = """
            date,product_id,currency,price
            20160101,1,EUR,10.0
            20160101,2,EUR,20.1
            20160101,3,EUR,30.34
            20160101,11,EUR,35.34
            """;
        
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        
        tradeEnrichmentService.processStreams(inputStream, byteArrayOutputStream);
        
        String outputData = byteArrayOutputStream.toString();

        String expectedData = """
            date,product_name,currency,price
            20160101,Treasury Bills Domestic,EUR,10.0
            20160101,Corporate Bonds Domestic,EUR,20.1
            20160101,REPO Domestic,EUR,30.34
            20160101,Missing Product Name,EUR,35.34
            """;
        
        Assertions.assertEquals(expectedData, outputData);
    }
}
