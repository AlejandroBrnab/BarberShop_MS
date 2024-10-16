package com.barbershop.barbers.presentationlayer;

import com.barbershop.barbers.businesslayer.BarberService;
import com.barbershop.barbers.dataaccesslayer.Address;
import com.barbershop.barbers.utils.exceptions.InvalidInputException;
import com.barbershop.barbers.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = BarberController.class)
class BarberControllerUnitTest {
    private final String FOUND_BARBER_ID = "81-712-9162";

    private final String NOT_FOUND_BARBER_ID = "81-712-916";

    private final String INVALID_BARBER_ID = "81-9962";

    @Autowired
    BarberController barberController;

    @MockBean
    private BarberService barberService;

    @Test
    public void whenNoBarberExists_thenReturnEmptyList(){
        //arrange
        when(barberService.getAllBarbers()).thenReturn(Collections.emptyList());

        //act
        ResponseEntity<List<BarberResponseModel>> responseEntity= barberController.getAllBarbers();

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isEmpty());
        verify(barberService, times(1)).getAllBarbers();
    }

    @Test
    public void whenBarberExists_thenReturnBarber(){
        //arrange

        BarberRequestModel barberRequestModel= BarberRequestModel.builder().build();
        BarberResponseModel barberResponseModel= BarberResponseModel.builder().build();

        when(barberService.addBarber(barberRequestModel)).thenReturn(barberResponseModel);

        //act
        ResponseEntity<BarberResponseModel> responseEntity= barberController.addBarber(barberRequestModel);

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(barberResponseModel, responseEntity.getBody());
        verify(barberService, times(1)).addBarber(barberRequestModel);
    }

    @Test
    public void whenBarberExists_thenDeleteBarber() throws NotFoundException {
        // Arrange
        doNothing().when(barberService).deleteBarber(FOUND_BARBER_ID);

        // Act
        ResponseEntity<Void> responseEntity = barberController.deleteBarber(FOUND_BARBER_ID);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(barberService, times(1)).deleteBarber(FOUND_BARBER_ID);
    }

    @Test
    public void WhenBarberDoesNotExistOnDelete_thenReturnNotFoundException() throws NotFoundException {
        // Arrange
        String nonExistentBarberId = INVALID_BARBER_ID;
        doThrow(NotFoundException.class).when(barberService).deleteBarber(nonExistentBarberId);

        // Act and Assert
        try {
            barberController.deleteBarber(nonExistentBarberId);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(barberService, times(1)).deleteBarber(nonExistentBarberId);
        }
    }


    @Test
    public void whenBarberNotFoundOnGet_thenReturnNotFoundException() {
        // Arrange
        when(barberService.getBarberBySin(NOT_FOUND_BARBER_ID)).thenThrow(NotFoundException.class);

        // Act and Assert
        try {
            barberController.getBarberBySin(NOT_FOUND_BARBER_ID);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(barberService, times(1)).getBarberBySin(NOT_FOUND_BARBER_ID);
        }
    }

    @Test
    public void whenBarberNotExistOnUpdate_thenReturnNotFoundException() throws NotFoundException {
        // Arrange
        BarberRequestModel updatedBarber = new BarberRequestModel(new Address("1234", "Saint-Constant", "QC",
                "Canada", "JS4 8N9"), new ArrayList<>(), "Juan", "Doe", "john@example.com",
                new BigDecimal("50000.00"), "768-456-9749999", LocalDate.of(1990, 1,1), true);
        when(barberService.updateBarber(updatedBarber, NOT_FOUND_BARBER_ID)).thenThrow(NotFoundException.class);

        // Act and Assert
        try {
            barberController.updateBarber(updatedBarber, NOT_FOUND_BARBER_ID);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(barberService, times(1)).updateBarber(updatedBarber, NOT_FOUND_BARBER_ID);
        }
    }


    @Test
    public void whenBarberExist_thenReturnUpdateBarber() throws NotFoundException {
        // Arrange
        String existingBarberId = FOUND_BARBER_ID;
        BarberRequestModel updatedBarber = new BarberRequestModel(new Address("1234", "Saint-Constant", "QC",
                "Canada", "JS4 8N9"), new ArrayList<>(), "Juan", "Doe", "john@example.com",
                new BigDecimal("50000.00"), "768-456-9749999", LocalDate.of(1990, 1,1), true);
        BarberResponseModel updatedResponse = BarberResponseModel.builder().sin(FOUND_BARBER_ID).build();

        when(barberService.updateBarber(updatedBarber, FOUND_BARBER_ID)).thenReturn(updatedResponse);

        // Act
        ResponseEntity<BarberResponseModel> responseEntity = barberController.updateBarber(updatedBarber, FOUND_BARBER_ID);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(FOUND_BARBER_ID, responseEntity.getBody().getSin());
        verify(barberService, times(1)).updateBarber(updatedBarber, FOUND_BARBER_ID);
    }


    @Test
    public void whenCustomerNotFoundOnGet_ThenThrowNotFoundException() {
        //arrange
        when(barberService.getBarberBySin(NOT_FOUND_BARBER_ID)).thenThrow(new NotFoundException(
                "Unknown barber SIN: " + NOT_FOUND_BARBER_ID));

        //act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            barberController.getBarberBySin(NOT_FOUND_BARBER_ID);
        });

        //assert
        assertEquals("Unknown barber SIN: " + NOT_FOUND_BARBER_ID, exception.getMessage());
        verify(barberService, times(1)).getBarberBySin(NOT_FOUND_BARBER_ID);
    }

    @Test
    public void whenCustomerNotCorrectOnPost_ThenThrowNotValidInputException() {
        //arrange
        when(barberService.getBarberBySin(INVALID_BARBER_ID)).thenThrow(new InvalidInputException(
                "Not valid barber SIN: " + INVALID_BARBER_ID));

        //act
        InvalidInputException exception = assertThrowsExactly(InvalidInputException.class, () -> {
            barberController.getBarberBySin(INVALID_BARBER_ID);
        });

        //assert
        assertEquals("Not valid barber SIN: " + INVALID_BARBER_ID, exception.getMessage());
        verify(barberService, times(1)).getBarberBySin(INVALID_BARBER_ID);
    }
}