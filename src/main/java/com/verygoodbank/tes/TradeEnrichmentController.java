package com.verygoodbank.tes;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
@RequestMapping("/api/v1")
public class TradeEnrichmentController
{
    private final TradeEnrichmentService tradeEnrichmentService;
    
    TradeEnrichmentController(TradeEnrichmentService tradeEnrichmentService)
    {
        this.tradeEnrichmentService = tradeEnrichmentService;
    }
    
    @PostMapping(value = "/enrich", consumes = "multipart/form-data", produces = "text/csv")
    void enrich(@RequestParam("file") MultipartFile file, HttpServletResponse response)
    {
        try
        {
            try(InputStream fileInputStream = file.getInputStream(); OutputStream outputStream = response.getOutputStream())
            {
                tradeEnrichmentService.processStreams(fileInputStream, outputStream);
                response.flushBuffer();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}