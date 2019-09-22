package com.creditsuisse.silverbars.integration.utils;

import com.creditsuisse.silverbars.infrastructure.api.in.SilverBarOrderDto;
import com.creditsuisse.silverbars.infrastructure.api.out.OrderResponseDto;
import com.creditsuisse.silverbars.infrastructure.api.out.OrderSummaryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

public class SilverBarClient {

    private final String port;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String lastStatusCode;

    public SilverBarClient(String port) {
        this.port = port;
    }

    public OrderResponseDto registerOrder(SilverBarOrderDto silverBarOrderDto) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);

        try {

            ResponseEntity<OrderResponseDto> responseEntity = restTemplate.postForEntity(
                    "http://localhost:" + port + "/silverbars/orders",
                    new HttpEntity<>(silverBarOrderDto, headers),
                    OrderResponseDto.class);

            lastStatusCode = responseEntity.getStatusCode().toString();

            return responseEntity.getBody();

        } catch (RestClientResponseException ex) {

            return handleException(ex, OrderResponseDto.class);
        }
    }

    public void deleteOrder(String orderId) {

        try {

            ResponseEntity<Void> responseEntity =
                    restTemplate.exchange(
                            "http://localhost:" + port + "/silverbars/orders/" + orderId,
                            DELETE,
                            new HttpEntity<>(new HttpHeaders()),
                            new ParameterizedTypeReference<Void>() {
                            });

            lastStatusCode = responseEntity.getStatusCode().toString();

        } catch (RestClientResponseException ex) {

            lastStatusCode = String.valueOf(ex.getRawStatusCode());
        }
    }

    public OrderSummaryDto getLiveOrderSummary() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(asList(APPLICATION_JSON_UTF8));

        try {

            ResponseEntity<OrderSummaryDto> responseEntity =
                    restTemplate.exchange(
                            "http://localhost:" + port + "/silverbars/orders/summary",
                            GET,
                            new HttpEntity<Void>(headers),
                            new ParameterizedTypeReference<OrderSummaryDto>() {
                            });

            lastStatusCode = responseEntity.getStatusCode().toString();

            return responseEntity.getBody();

        } catch (RestClientResponseException ex) {

            return handleException(ex, OrderSummaryDto.class);
        }
    }

    public String lastHttpStatusCode() {
        String httpStatusCode = lastStatusCode;
        lastStatusCode = null;

        return httpStatusCode;
    }

    private <T> T handleException(RestClientResponseException ex, Class<T> clazz) throws IOException {

        lastStatusCode = String.valueOf(ex.getRawStatusCode());

        return objectMapper.readValue(
                ex.getResponseBodyAsString(), clazz);
    }
}
