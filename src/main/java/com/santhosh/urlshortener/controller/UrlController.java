package com.santhosh.urlshortener.controller;

import com.santhosh.urlshortener.dto.UrlRequest;
import com.santhosh.urlshortener.dto.UrlResponse;
import com.santhosh.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlController {

    private final UrlService urlService;
    private final String baseUrl;

    UrlController(UrlService urlService,
                  @Value("${app.base-url}")String baseUrl){
        this.urlService=urlService;
        this.baseUrl=baseUrl;
    }


    @PostMapping("/api/urls")
    public ResponseEntity<UrlResponse> createShortCode(@Valid @RequestBody UrlRequest request){
        String shortCode=urlService.create(request.getUrl());
        UrlResponse urlResponse= new UrlResponse(baseUrl+shortCode,request.getUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(urlResponse);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode){
        String originalUrl=urlService.resolve(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }



}
