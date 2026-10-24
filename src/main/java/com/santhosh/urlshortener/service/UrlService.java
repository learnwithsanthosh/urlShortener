package com.santhosh.urlshortener.service;

import com.santhosh.urlshortener.exceptionHandler.ShortCodeNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlService {
    private static final String ALPHABET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int CODE_LENGTH=7;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final Map<String, String> values = new ConcurrentHashMap<>();
    private final int MAX_RETRIES = 5;

    public String create(String url){
        for(int i=0;i<MAX_RETRIES;i++) {
            String shortCode = generateRandomCode();
            String existingUrl = values.putIfAbsent(shortCode, url);
            if (existingUrl == null) {
                return shortCode;
            }
        }
        throw new IllegalStateException(
                "Unable to generate a unique short code after "+ MAX_RETRIES+" attempts"
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

    public String resolve(String shortCode){
        if(values.containsKey(shortCode)){
            return values.get(shortCode);
        }
        else{
            throw new ShortCodeNotFoundException(shortCode);
        }
    }

}
