package com.barbershop.barbers.presentationlayer;

import com.barbershop.barbers.dataaccesslayer.Address;
import com.barbershop.barbers.dataaccesslayer.BarberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BarberControllerIntegrationTest {

    private final String BASE_URI_BARBERS = "/api/v1/barbers";

    private final String FOUND_BARBER_ID = "185-48-4455";

    private final String FOUND_BARBER_FIRST_NAME = "Alice";

    private final String FOUND_BARBER_LAST_NAME = "Johnson";

    private final String NOT_FOUND_BARBER_ID = "185-4455";

    private final String INVALID_BARBER_ID = "185-48-445599";

    @Autowired
    private BarberRepository barberRepository;

    @Autowired
    private WebTestClient webTestBarber;

    @Test
    public void whenGetBarbers_thenReturnAllBarbers() {
        //arrange
        long sizeDB = barberRepository.count();
        //act
        webTestBarber.get()
                .uri(BASE_URI_BARBERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BarberResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.size() == sizeDB);
                    assertEquals(list.get(0).getSin(), FOUND_BARBER_ID);
                });
    }

    @Test
    public void whenGetBarberDoesNotExist_thenReturnNotFound(){
        //act and assert
        webTestBarber.get().uri(BASE_URI_BARBERS + "/" + NOT_FOUND_BARBER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown barber SIN: " + NOT_FOUND_BARBER_ID);
    }

    @Test
    public void whenValidBarber_thenCreateBarber() {
        //arrange
        long sizeDB = barberRepository.count();

        BarberRequestModel barberRequestModel = new BarberRequestModel(new Address("1234", "Saint-Constant", "QC",
                "Canada", "JS4 8N9"), new ArrayList<>(), "Juan", "Doe", "john@example.com",
                new BigDecimal("50000.00"), "768-456-9749", LocalDate.of(1990, 1,1), true);

        webTestBarber.post()
                .uri(BASE_URI_BARBERS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(barberRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BarberResponseModel.class)
                .value((barberResponseModel) -> {
                    assertNotNull(barberResponseModel);
                    assertEquals(barberRequestModel.getFirstName(), barberResponseModel.getFirstName());
                    assertEquals(barberRequestModel.getLastName(), barberResponseModel.getLastName());
                    assertEquals(barberRequestModel.getPhoneNumber(), barberResponseModel.getPhoneNumber());
                });
        long sizeDbAfter = barberRepository.count();
        assertEquals(sizeDB + 1, sizeDbAfter);
    }

    @Test
    public void whenBarberDoesNotExistOnUpdate_thenReturnNotFound() {
        // Arrange
        BarberRequestModel barberRequestModel = new BarberRequestModel(new Address("1234", "Saint-Constant", "QC",
                "Canada", "JS4 8N9"), new ArrayList<>(), "John", "Does", "john@example.com",
                new BigDecimal("50000.00"), "768-456-9749", LocalDate.of(1990, 1,1), true);

        // Perform a PUT or PATCH request to update a barber that does not exist
        webTestBarber.put()
                .uri(BASE_URI_BARBERS + "/" + NOT_FOUND_BARBER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(barberRequestModel) // Set the request body with the updated barber data
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown barber SIN: " + NOT_FOUND_BARBER_ID);
    }

    @Test
    public void whenBarberExist_thenDeleteBarber() {
        // Arrange: Ensure that the barber exists in the database and retrieve its ID
        String existingBarberId = FOUND_BARBER_ID;

        // Act: Delete the barber
        webTestBarber.delete()
                .uri(BASE_URI_BARBERS + "/" + FOUND_BARBER_ID)
                .exchange()
                .expectStatus().isNoContent();

        // Assert: Verify the barber is deleted
        webTestBarber.get()
                .uri(BASE_URI_BARBERS + "/" + FOUND_BARBER_ID)
                .exchange().
                expectStatus().isNotFound();
    }

    @Test
    public void whenValidBarber_thenUpdateBarber() {
        // Arrange: Assuming "FOUND_BARBER_ID" contains the ID of an existing barber
        String barberIdToUpdate = FOUND_BARBER_ID;

        // Create the barber update request model
        BarberRequestModel barberRequestModel = new BarberRequestModel(new Address("1234", "Saint-Constant", "QC",
                "Canada", "JS4 8N9"), new ArrayList<>(), "Juan", "Doe", "john@example.com",
                new BigDecimal("50000.00"), "768-456-9749", LocalDate.of(1990, 1,1), true);

        // Perform the PUT request to update the barber
        webTestBarber.put()
                .uri(BASE_URI_BARBERS + "/" + barberIdToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(barberRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BarberResponseModel.class)
                .value((barberResponseModel) -> {
                    assertNotNull(barberResponseModel);
                    assertEquals(barberRequestModel.getFirstName(), barberResponseModel.getFirstName());
                    assertEquals(barberRequestModel.getLastName(), barberResponseModel.getLastName());
                    assertEquals(barberRequestModel.getPhoneNumber(), barberResponseModel.getPhoneNumber());
                });
    }

    @Test
    public void whenBarberExists_thenReturnBarber() {
        // Arrange: Assuming there is a barber with the specified ID in the database
        String existingBarberId = FOUND_BARBER_ID;

        // Act: Perform a GET request to retrieve the barber by ID
        webTestBarber.get()
                .uri(BASE_URI_BARBERS + "/" + FOUND_BARBER_ID) // Adjust the URI to match your endpoint for retrieving barbers by ID
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BarberResponseModel.class)
                .value((barber) -> {
                    // Assert: Verify that the returned barber is not null and has the correct properties
                    assertNotNull(barber);
                    assertEquals("Juan", barber.getFirstName());
                    assertEquals("Doe", barber.getLastName());
                    assertEquals("768-456-9749", barber.getPhoneNumber());
                });
    }

    @Test
    public void whenBarbersDontExistOnDelete_thenReturnNotFound() {
        // Arrange: Assuming there are no barbers in the database
        String nonExistingBarberId = INVALID_BARBER_ID;

        // Act: Perform a DELETE request to delete a barber that doesn't exist
        webTestBarber.delete()
                .uri(BASE_URI_BARBERS + "/" + nonExistingBarberId) // Adjust the URI to match your endpoint for deleting barbers by ID
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown barber SIN: " + nonExistingBarberId);
    }

    @Test
    public void whenPhoneNumberNotValidOnAdd_thenReturnUnprocessableEntity(){
        // Arrange
        String barberId = FOUND_BARBER_ID;
        BarberRequestModel barberRequestModel = new BarberRequestModel(new Address("1234", "Saint-Constant", "QC",
                "Canada", "JS4 8N9"), new ArrayList<>(), "Juan", "Doe", "john@example.com",
                new BigDecimal("50000.00"), "768-456-9749999", LocalDate.of(1990, 1,1), true);

        // Act & Assert
        webTestBarber.post()
                .uri(BASE_URI_BARBERS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(barberRequestModel)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())// Adjust this to 422, as Spring WebFlux may return 400 for custom exceptions by default
                .expectBody()
                .jsonPath("$.status").isEqualTo("UNPROCESSABLE_ENTITY") // Adjust the message according to your implementation
                .jsonPath("$.message").isEqualTo("Phone number exceeds maximum length " + barberRequestModel.getPhoneNumber());
    }

//    @Test
//    public void whenUpdateBarberAvailability_thenUpdateAvailability() {
//        // Arrange: Assuming "FOUND_BARBER_ID" contains the ID of an existing barber
//        String barberIdToUpdate = FOUND_BARBER_ID;
//
//        // Create the barber availability update request model
//        BarberRequestModel barberRequestModel = new BarberRequestModel(new Address("1234", "Saint-Constant", "QC",
//                "Canada", "JS4 8N9"), new ArrayList<>(), "Juan", "Doe", "john@example.com",
//                new BigDecimal("50000.00"), "768-456-9749999", LocalDate.of(1990, 1,1), true);
//        barberRequestModel.setIsAvailable(false); // Set new availability status to false
//
//        // Perform the PUT request to update the barber availability
//        webTestBarber.put()
//                .uri(BASE_URI_BARBERS + "/" + barberIdToUpdate)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .bodyValue(barberRequestModel)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                .expectBody(BarberResponseModel.class)
//                .value((barberResponseModel) -> {
//                    // Assert: Verify that the returned barber response model is not null and has the correct availability status
//                    assertNotNull(barberResponseModel);
//                    assertFalse(barberResponseModel.getIsAvailable()); // Ensure availability is updated to false
//                });
//    }
//
//    @Test
//    public void whenUpdateBarberAvailabilityWithSameStatus_thenNoUpdate() {
//        // Arrange: Assuming "FOUND_BARBER_ID" contains the ID of an existing barber
//        String barberIdToUpdate = FOUND_BARBER_ID;
//
//        // Fetch the current availability status of the barber
//        BarberResponseModel currentBarber = webTestBarber.get()
//                .uri(BASE_URI_BARBERS + "/" + barberIdToUpdate)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(BarberResponseModel.class)
//                .returnResult().getResponseBody();
//
//        // Create the barber availability update request model with the same availability status
//        BarberRequestModel barberRequestModel = new BarberRequestModel(new Address("1234", "Saint-Constant", "QC",
//                "Canada", "JS4 8N9"), new ArrayList<>(), "Juan", "Doe", "john@example.com",
//                new BigDecimal("50000.00"), "768-456-9749999", LocalDate.of(1990, 1,1), true);
//        barberRequestModel.setIsAvailable(currentBarber.getIsAvailable()); // Set new availability status to the current status
//
//        // Perform the PUT request to update the barber availability
//        webTestBarber.put()
//                .uri(BASE_URI_BARBERS + "/" + barberIdToUpdate)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .bodyValue(barberRequestModel)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                .expectBody(BarberResponseModel.class)
//                .value((barberResponseModel) -> {
//                    // Assert: Verify that the returned barber response model is not null and has the same availability status
//                    assertNotNull(barberResponseModel);
//                    assertEquals(currentBarber.getIsAvailable(), barberResponseModel.getIsAvailable()); // Ensure availability is not updated
//                });
//    }
}