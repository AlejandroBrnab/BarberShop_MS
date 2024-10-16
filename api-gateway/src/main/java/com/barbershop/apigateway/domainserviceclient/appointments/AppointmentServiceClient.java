package com.barbershop.apigateway.domainserviceclient.appointments;

import com.barbershop.apigateway.presentationlayer.appointments.AppointmentRequestModel;
import com.barbershop.apigateway.presentationlayer.appointments.AppointmentResponseModel;
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
public class AppointmentServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String APPOINTMENTS_SERVICE_BASE_URL;

    private AppointmentServiceClient(RestTemplate restTemplate, ObjectMapper objectMapper,
                                   @Value("${app.appointments-service.host}") String appointmentsServiceHost,
                                   @Value("${app.appointments-service.port}") String appointmentsServicePort){
        this.restTemplate= restTemplate;
        this.mapper= objectMapper;

        APPOINTMENTS_SERVICE_BASE_URL= "http://" + appointmentsServiceHost + ":" +  appointmentsServicePort + "/api/v1/barbers/{barberId}/appointments";

    }

    public List<AppointmentResponseModel> getAllAppointments(String barberId){
        try {
            String url = APPOINTMENTS_SERVICE_BASE_URL.replace("{barberId}", barberId);
            AppointmentResponseModel[] appointmentResponseModels = restTemplate.getForObject(url, AppointmentResponseModel[].class);
            return Arrays.asList(appointmentResponseModels);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public AppointmentResponseModel getAppointmentByBarberIdAndAppointmentId(String barberId, String appointmentId){
        try {
            String url= APPOINTMENTS_SERVICE_BASE_URL.replace("{barberId}", barberId)  + "/" + appointmentId;
            AppointmentResponseModel appointmentModel=  restTemplate.getForObject(url,AppointmentResponseModel.class);
            return appointmentModel;
        }catch (HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }

    }

    public AppointmentResponseModel addAppointment(AppointmentRequestModel appointmentRequestModel, String barberId) {
        try {
            String url = APPOINTMENTS_SERVICE_BASE_URL.replace("{barberId}", barberId);

            // Set the headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the request entity with the league data in the body and headers
            HttpEntity<AppointmentRequestModel> requestEntity = new HttpEntity<>(appointmentRequestModel, headers);

            // Send the POST request to add the league
            AppointmentResponseModel appointmentModel = restTemplate.postForObject(url, requestEntity, AppointmentResponseModel.class);

            return appointmentModel;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public AppointmentResponseModel updateAppointment(String appointmentId, String barberId, AppointmentRequestModel appointmentRequestModel) {
        try {
            String url = APPOINTMENTS_SERVICE_BASE_URL.replace("{barberId}", barberId) +  "/" +  appointmentId;

            // Set the headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the request entity with the league data in the body and headers
            HttpEntity<AppointmentRequestModel> requestEntity = new HttpEntity<>(appointmentRequestModel, headers);

            // Send the PUT request to update the league
            restTemplate.put(url, requestEntity);

            // Assuming the leagues service returns the updated league data, you can fetch it
            AppointmentResponseModel updatedAppointment = restTemplate.getForObject(url, AppointmentResponseModel.class);

            return updatedAppointment;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteAppointment(String barberId, String appointmentId) {
        try {
            String url =APPOINTMENTS_SERVICE_BASE_URL.replace("{barberId}", barberId) + "/" + appointmentId;
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
