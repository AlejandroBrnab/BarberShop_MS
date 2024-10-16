package com.barbershop.appointments.presentationlayer;

import com.barbershop.appointments.dataaccesslayer.Appointment;
import com.barbershop.appointments.dataaccesslayer.AppointmentRepository;
import com.barbershop.appointments.domainclientlayer.barbers.BarberModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class AppointmentControllerIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AppointmentRepository appointmentRepository;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    private final String BARBER_BASE_URl = "http://localhost:7001/api/v1/barbers";

    private final String APPOINTMENT_BASE_URL = "api/v1/barbers";

    @BeforeEach
    public void init(){
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void whenGetAppointmentById_thenReturnAppointment() throws URISyntaxException, JsonProcessingException {
        //arrange
        var barberModel = BarberModel.builder().sin("185-48-4455").firstName("John").lastName("Doe").isAvailable(true).build();

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(BARBER_BASE_URl + "/185-48-4455")))
                .andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(barberModel)));

        //find the sale ID
        List<Appointment> appointments = appointmentRepository.findAppointmentsByBarberModel_Sin(barberModel.getSin());

        Appointment appointment = appointments.stream().filter(s -> s.getReviewModel().getReviewId().equals("98-8359696")).
                findFirst().get();

        assertNotNull(appointment);

        String url = APPOINTMENT_BASE_URL + "/" + barberModel.getSin() + "/appointments/" +
                appointment.getAppointmentIdentifier().getAppointmentId();

        //act and assert
        webClient.get().uri(url).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().
                expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody(AppointmentResponseModel.class).
                value((response) ->{
                    assertNotNull(response);
                    assertEquals(appointment.getAppointmentIdentifier().getAppointmentId(), response.getAppointmentId());
                    assertEquals(appointment.getReviewModel().getReviewId(),  response.getReviewId());
                });
    }

}