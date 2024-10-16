package com.barbershop.clients.presentationlayer;

import com.barbershop.clients.businesslayer.ClientService;
import com.barbershop.clients.dataacesslayer.Address;
import com.barbershop.clients.utils.exceptions.InvalidInputException;
import com.barbershop.clients.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ClientController.class)
class ClientControllerUnitTest {

    private final String FOUND_CLIENT_ID = "81-712-9162";

    private final String NOT_FOUND_CLIENT_ID = "81-712-916";

    private final String INVALID_CLIENT_ID = "81-9962";

    @Autowired
    ClientController clientController;

    @MockBean
    private ClientService clientService;

    @Test
    public void whenNoClientExists_thenReturnEmptyList(){
        //arrange
        when(clientService.getAllClients()).thenReturn(Collections.emptyList());

        //act
        ResponseEntity<List<ClientResponseModel>> responseEntity= clientController.getAllClients();

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isEmpty());
        verify(clientService, times(1)).getAllClients();
    }

    @Test
    public void whenClientExists_thenReturnClient(){
        //arrange

        ClientRequestModel clientRequestModel= ClientRequestModel.builder().build();
        ClientResponseModel clientResponseModel= ClientResponseModel.builder().build();

        when(clientService.addClient(clientRequestModel)).thenReturn(clientResponseModel);

        //act
        ResponseEntity<ClientResponseModel> responseEntity= clientController.addClient(clientRequestModel);

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(clientResponseModel, responseEntity.getBody());
        verify(clientService, times(1)).addClient(clientRequestModel);
    }

    @Test
    public void whenClientExists_thenDeleteClient() throws NotFoundException {
        // Arrange
        doNothing().when(clientService).deleteClient(FOUND_CLIENT_ID);

        // Act
        ResponseEntity<Void> responseEntity = clientController.deleteClient(FOUND_CLIENT_ID);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(clientService, times(1)).deleteClient(FOUND_CLIENT_ID);
    }

    @Test
    public void WhenClientDoesNotExistOnDelete_thenReturnNotFoundException() throws NotFoundException {
        // Arrange
        String nonExistentClientId = INVALID_CLIENT_ID;
        doThrow(NotFoundException.class).when(clientService).deleteClient(nonExistentClientId);

        // Act and Assert
        try {
            clientController.deleteClient(nonExistentClientId);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(clientService, times(1)).deleteClient(nonExistentClientId);
        }
    }


    @Test
    public void whenClientNotFoundOnGet_thenReturnNotFoundException() {
        // Arrange
        when(clientService.getClientById(NOT_FOUND_CLIENT_ID)).thenThrow(NotFoundException.class);

        // Act and Assert
        try {
            clientController.getClientById(NOT_FOUND_CLIENT_ID);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(clientService, times(1)).getClientById(NOT_FOUND_CLIENT_ID);
        }
    }

    @Test
    public void whenClientNotExistOnUpdate_thenReturnNotFoundException() throws NotFoundException {
        // Arrange
        ClientRequestModel updatedClient = new ClientRequestModel(new Address("789 Oak St", "Somewhere", "Quebec", "Canada",
                "M1N2O3"), "Juan", "Perez", "alice@example.com",
                "748-107-9665");
        when(clientService.updateClient(updatedClient, NOT_FOUND_CLIENT_ID)).thenThrow(NotFoundException.class);

        // Act and Assert
        try {
            clientController.updateClient(updatedClient, NOT_FOUND_CLIENT_ID);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(clientService, times(1)).updateClient(updatedClient, NOT_FOUND_CLIENT_ID);
        }
    }


    @Test
    public void whenClientExist_thenReturnUpdateClient() throws NotFoundException {
        // Arrange
        String existingClientId = FOUND_CLIENT_ID;
        ClientRequestModel updatedClient = new ClientRequestModel(new Address("789 Oak St", "Somewhere", "Quebec", "Canada",
                "M1N2O3"), "Juan", "Perez", "alice@example.com",
                "748-107-9665");
        ClientResponseModel updatedResponse = ClientResponseModel.builder().clientId(FOUND_CLIENT_ID).build();

        when(clientService.updateClient(updatedClient, FOUND_CLIENT_ID)).thenReturn(updatedResponse);

        // Act
        ResponseEntity<ClientResponseModel> responseEntity = clientController.updateClient(updatedClient, FOUND_CLIENT_ID);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(FOUND_CLIENT_ID, responseEntity.getBody().getClientId());
        verify(clientService, times(1)).updateClient(updatedClient, FOUND_CLIENT_ID);
    }


    @Test
    public void whenClientNotFoundOnGet_ThenThrowNotFoundException() {
        //arrange
        when(clientService.getClientById(NOT_FOUND_CLIENT_ID)).thenThrow(new NotFoundException(
                "Unknown client ID: " + NOT_FOUND_CLIENT_ID));

        //act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            clientController.getClientById(NOT_FOUND_CLIENT_ID);
        });

        //assert
        assertEquals("Unknown client ID: " + NOT_FOUND_CLIENT_ID, exception.getMessage());
        verify(clientService, times(1)).getClientById(NOT_FOUND_CLIENT_ID);
    }

    @Test
    public void whenClientNotCorrectOnPost_ThenThrowNotValidInputException() {
        //arrange
        when(clientService.getClientById(INVALID_CLIENT_ID)).thenThrow(new InvalidInputException(
                "Not valid client ID: " + INVALID_CLIENT_ID));

        //act
        InvalidInputException exception = assertThrowsExactly(InvalidInputException.class, () -> {
            clientController.getClientById(INVALID_CLIENT_ID);
        });

        //assert
        assertEquals("Not valid client ID: " + INVALID_CLIENT_ID, exception.getMessage());
        verify(clientService, times(1)).getClientById(INVALID_CLIENT_ID);
    }
}