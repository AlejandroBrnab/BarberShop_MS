package com.barbershop.appointments.domainclientlayer.reviews;

import com.barbershop.appointments.domainclientlayer.clients.ClientModel;
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
public class ReviewServiceClient {

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    private final String REVIEWS_SERVICE_BASE_URL;

    public ReviewServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                               @Value("${app.reviews-service.host}") String reviewsServiceHost,
                               @Value("${app.reviews-service.port}") String reviewsServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        REVIEWS_SERVICE_BASE_URL = "http://" + reviewsServiceHost + ":" + reviewsServicePort + "/api/v1/reviews";
    }

    public ReviewModel getReviewByReviewId(String reviewId) {
        try {
            String url = REVIEWS_SERVICE_BASE_URL + "/" + reviewId;

            ReviewModel reviewModel = restTemplate.getForObject(url, ReviewModel.class);

            return reviewModel;
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
