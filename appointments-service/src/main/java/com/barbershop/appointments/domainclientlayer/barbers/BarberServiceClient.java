package com.barbershop.appointments.domainclientlayer.barbers;

import com.barbershop.appointments.utils.HttpErrorInfo;
import com.barbershop.appointments.utils.exceptions.InvalidInputException;
import com.barbershop.appointments.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
@Slf4j
public class BarberServiceClient {

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    private final String BARBERS_SERVICE_BASE_URL;

    public BarberServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                               @Value("${app.barbers-service.host}") String barbersServiceHost,
                               @Value("${app.barbers-service.port}") String barbersServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        BARBERS_SERVICE_BASE_URL = "http://" + barbersServiceHost + ":" + barbersServicePort + "/api/v1/barbers";
    }

    public BarberModel getBarberByBarberId(String barberId) {
        try {
            String url = BARBERS_SERVICE_BASE_URL + "/" + barberId;

            BarberModel barberModel = restTemplate.getForObject(url, BarberModel.class);

            return barberModel;
        }catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void updateBarberAvailability(String barberId, Boolean isAvailable) {
        try {
            String url = BARBERS_SERVICE_BASE_URL + "/" + barberId + "/newAvailability";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Boolean> requestBody = new HashMap<>();
            requestBody.put("isAvailable", isAvailable);

            HttpEntity<Map<String, Boolean>> requestEntity = new HttpEntity<>(requestBody, headers);

            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);

        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        //include all possible responses from the client
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidInputException(getErrorMessage(ex));
        }
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch (IOException ioex) {
            return ioex.getMessage();
        }
    }
}
