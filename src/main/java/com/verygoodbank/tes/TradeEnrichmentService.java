package com.verygoodbank.tes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Service
public class TradeEnrichmentService
{
    private static final Logger logger = LoggerFactory.getLogger(TradeEnrichmentService.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final byte[] BINARY_OUTPUT_HEADER = "date,product_name,currency,price\n".getBytes(StandardCharsets.UTF_8);
    private static final byte[] LINE_SEPARATOR_AS_BINARY = "\n".getBytes(StandardCharsets.UTF_8);
    private static final String FIELD_SEPARATOR = ",";
    private static final byte[] FIELD_SEPARATOR_AS_BINARY = FIELD_SEPARATOR.getBytes(StandardCharsets.UTF_8);
    private static final byte[] MISSING_PRODUCT_NAME = "Missing Product Name".getBytes(StandardCharsets.UTF_8);
    
    private final Map<String, String> products;
    
    TradeEnrichmentService(Map<String, String> products)
    {
        this.products = products;
    }
    
    public void processStreams(InputStream fileInputStream, OutputStream outputStream)
    {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream)))
        {
            outputStream.write(BINARY_OUTPUT_HEADER);
            
            bufferedReader.readLine();
            
            while(bufferedReader.ready())
            {
                String line = bufferedReader.readLine();
                String[] split = line.split(FIELD_SEPARATOR);
                
                if(!isValidDate(split[0]))
                {
                    logger.error("Date format is invalid for: {}", split[0]);
                    continue;
                }
                
                outputStream.write(split[0].getBytes(StandardCharsets.UTF_8));
                outputStream.write(FIELD_SEPARATOR_AS_BINARY);
                
                if(products.containsKey(split[1]))
                {
                    String value = products.get(split[1]);
                    outputStream.write(value.getBytes(StandardCharsets.UTF_8));
                }
                else
                {
                    outputStream.write(MISSING_PRODUCT_NAME);
                }
                
                outputStream.write(FIELD_SEPARATOR_AS_BINARY);
                
                outputStream.write(split[2].getBytes(StandardCharsets.UTF_8));
                outputStream.write(FIELD_SEPARATOR_AS_BINARY);
                
                outputStream.write(split[3].getBytes(StandardCharsets.UTF_8));
                
                outputStream.write(LINE_SEPARATOR_AS_BINARY);
            }
        }
        catch(IOException e)
        {
            logger.error("Error occurred while processing", e);
        }
    }
    
    private boolean isValidDate(String date)
    {
        if(date == null || date.length() != 8)
        {
            return false;
        }
        
        try
        {
            LocalDate.parse(date, DATE_TIME_FORMATTER);
            
            return true;
        }
        catch(DateTimeParseException e)
        {
            return false;
        }
    }
}
