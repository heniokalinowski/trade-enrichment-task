package com.verygoodbank.tes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Configuration
class TradeEnrichmentConfiguration
{
    @Bean
    Map<String, String> products()
    {
        try(
            InputStream inputStream = TradeEnrichmentConfiguration.class.getResourceAsStream("product.csv");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        )
        {
            bufferedReader.readLine();
            
            String line;
            Map<String, String> result = new HashMap<>();
            
            while((line = bufferedReader.readLine()) != null)
            {
                String[] values = line.split(",");
                String id = values[0];
                String name = values[1];
                
                result.put(id, name);
            }
            
            return result;
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
