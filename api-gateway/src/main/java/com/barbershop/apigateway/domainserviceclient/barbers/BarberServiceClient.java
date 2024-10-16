package com.barbershop.apigateway.domainserviceclient.barbers;

import com.barbershop.apigateway.presentationlayer.barbers.BarberRequestModel;
import com.barbershop.apigateway.presentationlayer.barbers.BarberResponseModel;
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

    public List<BarberResponseModel> getAllBarbers() {
        try {
            String url = BARBERS_SERVICE_BASE_URL;
            /*log.debug("Sending GET request to: {}", url); // Log the URL being requested*/

            BarberResponseModel[] barberResponseModel = restTemplate.getForObject(url, BarberResponseModel[].class);

            log.debug("Received response for GET request to {}: {}", url, barberResponseModel); // Log the response received
            return Arrays.asList(barberResponseModel);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public BarberResponseModel getBarberByBarberSin(String barberId) {
        try {
            String url =BARBERS_SERVICE_BASE_URL + "/" + barberId;

            BarberResponseModel barberResponseModel = restTemplate.getForObject(url, BarberResponseModel.class);

            return barberResponseModel;
        }catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public BarberResponseModel updateBarber(BarberRequestModel barberRequestModel, String barberId) {
        try {
            String url = BARBERS_SERVICE_BASE_URL + "/" + barberId;

            // Set the headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the request entity with the league data in the body and headers
            HttpEntity<BarberRequestModel> requestEntity = new HttpEntity<>(barberRequestModel, headers);

            // Send the PUT request to update the league
            restTemplate.put(url, requestEntity);

            // Assuming the leagues service returns the updated league data, you can fetch it
            BarberResponseModel updatedBarber = restTemplate.getForObject(url, BarberResponseModel.class);

            return updatedBarber;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public BarberResponseModel addBarber(BarberRequestModel barberRequestModel) {
        try {
            String url = BARBERS_SERVICE_BASE_URL;

            // Set the headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the request entity with the league data in the body and headers
            HttpEntity<BarberRequestModel> requestEntity = new HttpEntity<>(barberRequestModel, headers);

            // Send the POST request to add the league
            BarberResponseModel barberModel = restTemplate.postForObject(url, requestEntity, BarberResponseModel.class);

            return barberModel;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteBarber(String barberId) {
        try {
            String url = BARBERS_SERVICE_BASE_URL + "/" + barberId;
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
