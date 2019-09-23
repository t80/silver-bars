package com.creditsuisse.silverbars.integration.utils;

import com.creditsuisse.silverbars.domain.SilverBarOrder;
import com.creditsuisse.silverbars.infrastructure.api.out.OrderResponseDto;
import com.creditsuisse.silverbars.infrastructure.api.out.OrderSummaryDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

public class SilverBarClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl;

    private String lastStatusCode;

    public SilverBarClient(String port, String host, String rootPath) {
        this.baseUrl = host + ":" + port + rootPath;
    }

    public OrderResponseDto registerOrder(SilverBarOrder silverBarOrderDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);

        try {

            ResponseEntity<OrderResponseDto> responseEntity = restTemplate.postForEntity(
                    baseUrl + "/orders",
                    new HttpEntity<>(silverBarOrderDto, headers),
                    OrderResponseDto.class);

            lastStatusCode = responseEntity.getStatusCode().toString();

            return responseEntity.getBody();

        } catch (RestClientResponseException ex) {

            lastStatusCode = String.valueOf(ex.getRawStatusCode());

            return null;
        }
    }

    public void deleteOrder(String orderId) {

        try {

            ResponseEntity<Void> responseEntity =
                    restTemplate.exchange(
                            baseUrl + "/orders/" + orderId,
                            DELETE,
                            new HttpEntity<>(new HttpHeaders()),
                            new ParameterizedTypeReference<Void>() {
                            });

            lastStatusCode = responseEntity.getStatusCode().toString();

        } catch (RestClientResponseException ex) {

            lastStatusCode = String.valueOf(ex.getRawStatusCode());
        }
    }

    public OrderSummaryDto getLiveOrderSummary() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(asList(APPLICATION_JSON_UTF8));

        try {

            ResponseEntity<OrderSummaryDto> responseEntity =
                    restTemplate.exchange(
                            baseUrl + "/orders/summary",
                            GET,
                            new HttpEntity<Void>(headers),
                            new ParameterizedTypeReference<OrderSummaryDto>() {
                            });

            lastStatusCode = responseEntity.getStatusCode().toString();

            return responseEntity.getBody();

        } catch (RestClientResponseException ex) {

            lastStatusCode = String.valueOf(ex.getRawStatusCode());

            return null;
        }
    }

    public String lastHttpStatusCode() {
        String httpStatusCode = lastStatusCode;
        lastStatusCode = null;

        return httpStatusCode;
    }
}
