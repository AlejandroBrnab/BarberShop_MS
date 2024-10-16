package com.barbershop.apigateway.domainserviceclient.clients;

import com.barbershop.apigateway.presentationlayer.clients.ClientRequestModel;
import com.barbershop.apigateway.presentationlayer.clients.ClientResponseModel;
import com.barbershop.apigateway.utils.HttpErrorInfo;
import com.barbershop.apigateway.utils.exceptions.InvalidInputException;
import com.barbershop.apigateway.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    public List<ClientResponseModel> getAllClients() {
        try {
            String url = CLIENTS_SERVICE_BASE_URL;

            ClientResponseModel[] clientResponseModel = restTemplate.getForObject(url, ClientResponseModel[].class);

            return Arrays.asList(clientResponseModel);
        }catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public ClientResponseModel getClientByClientId(String clientId) {
        try {
            String url = CLIENTS_SERVICE_BASE_URL + "/" + clientId;

            ClientResponseModel clientResponseModel = restTemplate.getForObject(url, ClientResponseModel.class);

            return clientResponseModel;
        }catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public ClientResponseModel updateClient(ClientRequestModel clientRequestModel, String clientId) {
        try {
            String url = CLIENTS_SERVICE_BASE_URL + "/" + clientId;

            // Set the headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the request entity with the league data in the body and headers
            HttpEntity<ClientRequestModel> requestEntity = new HttpEntity<>(clientRequestModel, headers);

            // Send the PUT request to update the league
            restTemplate.put(url, requestEntity);

            // Assuming the leagues service returns the updated league data, you can fetch it
            ClientResponseModel updatedClient = restTemplate.getForObject(url, ClientResponseModel.class);

            return updatedClient;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public ClientResponseModel addClient(ClientRequestModel clientRequestModel) {
        try {
            String url = CLIENTS_SERVICE_BASE_URL;

            // Set the headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the request entity with the league data in the body and headers
            HttpEntity<ClientRequestModel> requestEntity = new HttpEntity<>(clientRequestModel, headers);

            // Send the POST request to add the league
            ClientResponseModel clientModel = restTemplate.postForObject(url, requestEntity, ClientResponseModel.class);

            return clientModel;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteClient(String clientId) {
        try {
            String url = CLIENTS_SERVICE_BASE_URL + "/" + clientId;
            restTemplate.delete(url);
        } catch (HttpClientErrorException ex) {
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
