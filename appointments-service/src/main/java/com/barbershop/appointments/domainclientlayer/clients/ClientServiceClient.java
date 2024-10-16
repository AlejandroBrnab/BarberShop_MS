package com.barbershop.appointments.domainclientlayer.clients;

import com.barbershop.appointments.utils.HttpErrorInfo;
import com.barbershop.appointments.utils.exceptions.InvalidInputException;
import com.barbershop.appointments.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
@Slf4j
public class ClientServiceClient {

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    private final String CLIENTS_SERVICE_BASE_URL;

    public ClientServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                               @Value("${app.clients-service.host}") String clientsServiceHost,
                               @Value("${app.clients-service.port}") String clientsServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        CLIENTS_SERVICE_BASE_URL = "http://" + clientsServiceHost + ":" + clientsServicePort + "/api/v1/clients";
    }

    public ClientModel getClientByClientId(String clientId) {
        try {
            String url = CLIENTS_SERVICE_BASE_URL + "/" + clientId;

            ClientModel clientModel = restTemplate.getForObject(url, ClientModel.class);

            return clientModel;
        }catch (HttpClientErrorException ex) {
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
