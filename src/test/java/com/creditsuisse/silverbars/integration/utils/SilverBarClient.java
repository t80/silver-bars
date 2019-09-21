package com.creditsuisse.silverbars.integration.utils;

import com.creditsuisse.silverbars.infrastructure.api.in.SilverBarOrderDto;
import com.creditsuisse.silverbars.infrastructure.api.out.OrderResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

public class SilverBarClient {

    private final String port;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpHeaders headers = new HttpHeaders();

    private String lastStatusCode;

    public SilverBarClient(String port) {
        this.port = port;
        this.headers.setContentType(APPLICATION_JSON_UTF8);
    }

    public OrderResponseDto registerOrder(SilverBarOrderDto silverBarOrderDto) throws IOException {

        HttpEntity<SilverBarOrderDto> requestEntity =
                new HttpEntity<>(silverBarOrderDto, headers);

        try {

            ResponseEntity<OrderResponseDto> responseEntity = restTemplate.postForEntity(
                    "http://localhost:" + port + "/silverbars/orders",
                    requestEntity,
                    OrderResponseDto.class);

            lastStatusCode = responseEntity.getStatusCode().toString();

            return responseEntity.getBody();

        } catch (RestClientResponseException ex) {

            return handleException(ex);
        }
    }

    public String lastHttpStatusCode() {
        return lastStatusCode;
    }

    private OrderResponseDto handleException(RestClientResponseException ex) throws IOException {

        lastStatusCode = String.valueOf(ex.getRawStatusCode());

        return objectMapper.readValue(
                ex.getResponseBodyAsString(), OrderResponseDto.class);
    }
}
