package com.santhosh.urlshortener.service;

import com.santhosh.urlshortener.entity.UrlMapping;
import com.santhosh.urlshortener.exception.MaxRetriesExceededException;
import com.santhosh.urlshortener.exception.ShortCodeNotFoundException;
import com.santhosh.urlshortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class UrlService {
    private static final String ALPHABET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int CODE_LENGTH=7;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final int MAX_RETRIES = 5;

    private final UrlMappingRepository urlMappingRepository;

    UrlService(UrlMappingRepository urlMappingRepository){
        this.urlMappingRepository=urlMappingRepository;
    }

    @Transactional
    public String create(String url){
        for(int i = 0; i< MAX_RETRIES; i++) {
            String shortCode = generateRandomCode();
            if (!urlMappingRepository.existsByShortCode(shortCode)) {
                UrlMapping urlMapping= new UrlMapping(shortCode,url);
                urlMappingRepository.save(urlMapping);
                return shortCode;
            }
        }
        throw new MaxRetriesExceededException("Failed to generate a unique short code after " + MAX_RETRIES + " attempts."
        );
    }
    private String generateRandomCode(){
        StringBuilder sc= new StringBuilder();
        for(int i=0;i<CODE_LENGTH;i++){
            int index= SECURE_RANDOM.nextInt(ALPHABET.length());
            sc.append(ALPHABET.charAt(index));
        }
        return sc.toString();
    }

    @Transactional(readOnly = true)
    public String resolve(String shortCode){
        return urlMappingRepository.findByShortCode(shortCode)
                .map(UrlMapping::getOriginalUrl)
                .orElseThrow(()-> new ShortCodeNotFoundException(shortCode));
    }

}
