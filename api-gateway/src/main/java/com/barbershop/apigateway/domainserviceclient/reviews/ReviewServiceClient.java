package com.barbershop.apigateway.domainserviceclient.reviews;

import com.barbershop.apigateway.presentationlayer.reviews.ReviewRequestModel;
import com.barbershop.apigateway.presentationlayer.reviews.ReviewResponseModel;
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

    public List<ReviewResponseModel> getAllReviews() {
        try {
            String url = REVIEWS_SERVICE_BASE_URL;

            ReviewResponseModel[] reviewResponseModel = restTemplate.getForObject(url, ReviewResponseModel[].class);

            return Arrays.asList(reviewResponseModel);
        }catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public ReviewResponseModel getReviewByReviewId(String reviewId) {
        try {
            String url = REVIEWS_SERVICE_BASE_URL + "/" + reviewId;

           ReviewResponseModel reviewResponseModel = restTemplate.getForObject(url, ReviewResponseModel.class);

            return reviewResponseModel;
        }catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public ReviewResponseModel updateReview(ReviewRequestModel reviewRequestModel, String reviewId) {
        try {
            String url = REVIEWS_SERVICE_BASE_URL + "/" + reviewId;

            // Set the headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the request entity with the league data in the body and headers
            HttpEntity<ReviewRequestModel> requestEntity = new HttpEntity<>(reviewRequestModel, headers);

            // Send the PUT request to update the league
            restTemplate.put(url, requestEntity);

            // Assuming the leagues service returns the updated league data, you can fetch it
            ReviewResponseModel updatedReview = restTemplate.getForObject(url, ReviewResponseModel.class);

            return updatedReview;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public ReviewResponseModel addReview(ReviewRequestModel reviewRequestModel) {
        try {
            String url = REVIEWS_SERVICE_BASE_URL;

            // Set the headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the request entity with the league data in the body and headers
            HttpEntity<ReviewRequestModel> requestEntity = new HttpEntity<>(reviewRequestModel, headers);

            // Send the POST request to add the league
            ReviewResponseModel reviewModel = restTemplate.postForObject(url, requestEntity, ReviewResponseModel.class);

            return reviewModel;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteReview(String reviewId) {
        try {
            String url = REVIEWS_SERVICE_BASE_URL + "/" + reviewId;
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
