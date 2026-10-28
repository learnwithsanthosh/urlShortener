package com.santhosh.urlshortener.repository;

import com.santhosh.urlshortener.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);

    @Query(value = """
    SELECT nextval(
        pg_get_serial_sequence('url_mapping', 'id')::regclass
    )
    """, nativeQuery = true)
    Long nextId();
}
