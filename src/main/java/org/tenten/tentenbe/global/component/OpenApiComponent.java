package org.tenten.tentenbe.global.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenApiComponent {
    @Value("${open-api.url}")
    private String apiUrl;
    @Value("${open-api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;



}
