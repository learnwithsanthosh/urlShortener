package com.santhosh.urlshortener.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UrlRequest {
    @NotNull
    @Size(max = 2048)
    @Pattern(regexp = "^https?://.+",message = "URL must start with http:// or https://")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
