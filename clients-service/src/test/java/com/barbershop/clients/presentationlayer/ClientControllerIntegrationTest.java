package com.barbershop.clients.presentationlayer;

import com.barbershop.clients.dataacesslayer.Address;
import com.barbershop.clients.dataacesslayer.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ClientControllerIntegrationTest {
    private final String BASE_URI_CLIENTS = "/api/v1/clients";
    private final String FOUND_CLIENT_ID = "81-712-9162";

    private final String FOUND_CLIENT_FIRST_NAME = "Alice";

    private final String FOUND_CLIENT_LAST_NAME = "Johnson";

    private final String NOT_FOUND_CLIENT_ID = "81-712-916";

    private final String INVALID_CLIENT_ID = "81-9962";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenGetClients_thenReturnAllClients() {
        //arrange
        long sizeDB = clientRepository.count();
        //act
        webTestClient.get()
                .uri(BASE_URI_CLIENTS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ClientResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.size() == sizeDB);
                    assertEquals(list.get(0).getClientId(), FOUND_CLIENT_ID);
                });
    }

    @Test
    public void whenGetClientDoesNotExist_thenReturnNotFound(){
        //act and assert
        webTestClient.get().uri(BASE_URI_CLIENTS + "/" + NOT_FOUND_CLIENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown client ID: " + NOT_FOUND_CLIENT_ID);
    }

    @Test
    public void whenValidClient_thenCreateClient() {
        //arrange
        long sizeDB = clientRepository.count();

        ClientRequestModel clientRequestModel = new ClientRequestModel(new Address("1234", "Saint-Constant", "QC", "Canada",
                "JS4 8N9"), "Alicia", "Maravilla", "alice@example.com",
                "748-107-9665");

        webTestClient.post()
                .uri(BASE_URI_CLIENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(clientRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ClientResponseModel.class)
                .value((clientResponseModel) -> {
                    assertNotNull(clientResponseModel);
                    assertEquals(clientRequestModel.getFirstName(), clientResponseModel.getFirstName());
                    assertEquals(clientRequestModel.getLastName(), clientResponseModel.getLastName());
                    assertEquals(clientRequestModel.getPhoneNumber(), clientResponseModel.getPhoneNumber());
                });
        long sizeDbAfter = clientRepository.count();
        assertEquals(sizeDB + 1, sizeDbAfter);
    }

    @Test
    public void whenClientDoesNotExistOnUpdate_thenReturnNotFound() {
        // Arrange
        ClientRequestModel clientRequestModel = new ClientRequestModel(new Address("1234", "Saint-Constant", "QC", "Canada",
                "JS4 8N9"), "Alicia", "Maravilla", "alicia@example.com",
                "748-107-9665");

        // Perform a PUT or PATCH request to update a client that does not exist
        webTestClient.put()
                .uri(BASE_URI_CLIENTS + "/" + NOT_FOUND_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(clientRequestModel) // Set the request body with the updated client data
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown client ID: " + NOT_FOUND_CLIENT_ID);
    }

    @Test
    public void whenClientExist_thenDeleteClient() {
        // Arrange: Ensure that the client exists in the database and retrieve its ID
        String existingClientId = FOUND_CLIENT_ID;

        // Act: Delete the client
        webTestClient.delete()
                .uri(BASE_URI_CLIENTS + "/" + FOUND_CLIENT_ID)
                .exchange()
                .expectStatus().isNoContent();

        // Assert: Verify the client is deleted
        webTestClient.get()
                .uri(BASE_URI_CLIENTS + "/" + FOUND_CLIENT_ID)
                .exchange().
                expectStatus().isNotFound();
    }

    @Test
    public void whenValidClient_thenUpdateClient() {
        // Arrange: Assuming "FOUND_CLIENT_ID" contains the ID of an existing client
        String clientIdToUpdate = FOUND_CLIENT_ID;

        // Create the client update request model
        ClientRequestModel clientRequestModel = new ClientRequestModel(new Address("789 Oak St", "Somewhere", "Quebec", "Canada",
                "M1N2O3"), "Juan", "Perez", "alice@example.com",
                "748-107-9665");

        // Perform the PUT request to update the client
        webTestClient.put()
                .uri(BASE_URI_CLIENTS + "/" + clientIdToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(clientRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ClientResponseModel.class)
                .value((clientResponseModel) -> {
                    assertNotNull(clientResponseModel);
                    assertEquals(clientRequestModel.getFirstName(), clientResponseModel.getFirstName());
                    assertEquals(clientRequestModel.getLastName(), clientResponseModel.getLastName());
                    assertEquals(clientRequestModel.getPhoneNumber(), clientResponseModel.getPhoneNumber());
                });
    }

    @Test
    public void whenClientExists_thenReturnClient() {
        // Arrange: Assuming there is a client with the specified ID in the database
        String existingClientId = FOUND_CLIENT_ID;

        // Act: Perform a GET request to retrieve the client by ID
        webTestClient.get()
                .uri(BASE_URI_CLIENTS + "/" + FOUND_CLIENT_ID) // Adjust the URI to match your endpoint for retrieving clients by ID
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ClientResponseModel.class)
                .value((client) -> {
                    // Assert: Verify that the returned client is not null and has the correct properties
                    assertNotNull(client);
                    assertEquals("Juan", client.getFirstName());
                    assertEquals("Perez", client.getLastName());
                    assertEquals("748-107-9665", client.getPhoneNumber());
                });
    }

    @Test
    public void whenClientsDontExistOnDelete_thenReturnNotFound() {
        // Arrange: Assuming there are no clients in the database
        String nonExistingClientId = INVALID_CLIENT_ID;

        // Act: Perform a DELETE request to delete a client that doesn't exist
        webTestClient.delete()
                .uri(BASE_URI_CLIENTS + "/" + nonExistingClientId) // Adjust the URI to match your endpoint for deleting clients by ID
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown client ID: " + nonExistingClientId);
    }

    @Test
    public void whenPhoneNumberNotValidOnAdd_thenReturnUnprocessableEntity(){
        // Arrange
        String clientId = FOUND_CLIENT_ID; // Replace with an existing league ID
        ClientRequestModel clientRequestModel = new ClientRequestModel(new Address("789 Oak St", "Somewhere", "Quebec", "Canada",
                "M1N2O3"), "Juan", "Perez", "alice@example.com",
                "748-107-9665999");

        // Act & Assert
        webTestClient.post()
                .uri(BASE_URI_CLIENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(clientRequestModel)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())// Adjust this to 422, as Spring WebFlux may return 400 for custom exceptions by default
                .expectBody()
                .jsonPath("$.status").isEqualTo("UNPROCESSABLE_ENTITY") // Adjust the message according to your implementation
                .jsonPath("$.message").isEqualTo("Phone number exceeds maximum length " + clientRequestModel.getPhoneNumber());
    }
}