package com.santhosh.urlshortener.service;

import com.santhosh.urlshortener.entity.UrlMapping;
import com.santhosh.urlshortener.exception.ShortCodeNotFoundException;
import com.santhosh.urlshortener.repository.UrlMappingRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    @Mock
    private UrlMappingRepository urlMappingRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UrlService urlService;


    @Test
    void create_shouldGenerateBase62CodeAndPersistUrlMapping(){
        when(urlMappingRepository.nextId()).thenReturn(3844L);
        String originalUrl = "https://example.com";
        String shortCode = urlService.create(originalUrl);

        assertEquals("100", shortCode);
        ArgumentCaptor<UrlMapping> captor =
                ArgumentCaptor.forClass(UrlMapping.class);

        verify(entityManager).persist(captor.capture());

        UrlMapping savedMapping = captor.getValue();

        assertEquals(3844L, savedMapping.getId());
        assertEquals("100", savedMapping.getShortCode());
        assertEquals(originalUrl, savedMapping.getOriginalUrl());
    }

    @Test
    void resolve_shouldReturnOriginalUrlForGivenShortCode(){
        String shortCode = "100";
        String originalUrl = "https://example.com";

        UrlMapping urlMapping = new UrlMapping(3844L,shortCode, originalUrl);

        when(urlMappingRepository.findByShortCode(shortCode))
                .thenReturn(java.util.Optional.of(urlMapping));

        String resolvedUrl = urlService.resolve(shortCode);

        assertEquals(originalUrl, resolvedUrl);
    }

    @Test
    void resolve_shouldThrowExceptionForNonExistentShortCode(){
        String shortCode = "nonexistent";

        when(urlMappingRepository.findByShortCode(shortCode))
                .thenReturn(java.util.Optional.empty());
        ShortCodeNotFoundException ex=assertThrows(ShortCodeNotFoundException.class, () -> urlService.resolve(shortCode));
        assertEquals("Short code not found: nonexistent",ex.getMessage());
    }
}
