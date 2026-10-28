package com.santhosh.urlshortener.service;

import com.santhosh.urlshortener.entity.UrlMapping;
import com.santhosh.urlshortener.exception.ShortCodeNotFoundException;
import com.santhosh.urlshortener.repository.UrlMappingRepository;
import com.santhosh.urlshortener.util.Base62;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlService {
    private final UrlMappingRepository urlMappingRepository;
    private final EntityManager entityManager;

    public UrlService(UrlMappingRepository urlMappingRepository, EntityManager entityManager){
        this.urlMappingRepository=urlMappingRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public String create(String url){
        long id = urlMappingRepository.nextId();
        String shortCode = Base62.encode(id);

        UrlMapping urlMapping = new UrlMapping(id,shortCode, url);

        entityManager.persist(urlMapping);

        return shortCode;

    }
    @Transactional(readOnly = true)
    public String resolve(String shortCode){
        return urlMappingRepository.findByShortCode(shortCode)
                .map(UrlMapping::getOriginalUrl)
                .orElseThrow(()-> new ShortCodeNotFoundException(shortCode));
    }

}
