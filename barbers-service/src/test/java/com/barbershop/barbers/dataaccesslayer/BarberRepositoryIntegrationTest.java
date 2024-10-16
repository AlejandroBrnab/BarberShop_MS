package com.barbershop.barbers.dataaccesslayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BarberRepositoryIntegrationTest {

    @Autowired
    private BarberRepository barberRepository;

    @BeforeEach
    public void setUpDb(){
        barberRepository.deleteAll();
    }

    @Test
    public void whenCBarberExists_ReturnBarberByBarberId(){
        //arrange (set up test data)
        Barber barber1 = new Barber(new Address("1234", "Saint-Constant", "QC", "Canada",
                "JS4 8N9"), new ArrayList<>(),"John", "Doe", "john@example.com",
                new BigDecimal("50000.00"), "768-456-9749", LocalDate.of(1990,1,1), true);

        barberRepository.save(barber1);
        //act
        Barber barber =
                barberRepository.findBarberByBarberIdentifier_Sin(barber1.getBarberIdentifier().getSin());

        //assert
        assertNotNull(barber);
        assertEquals(barber.getBarberIdentifier(), barber1.getBarberIdentifier());
        assertEquals(barber.getFirstName(), barber1.getFirstName());
        assertEquals(barber.getLastName(), barber1.getLastName());
    }

    @Test
    public void whenBarberDoesNotExist_ReturnNull(){
        Barber savedBarber= barberRepository.findBarberByBarberIdentifier_Sin("1234");

        assertNull(savedBarber);
    }
}