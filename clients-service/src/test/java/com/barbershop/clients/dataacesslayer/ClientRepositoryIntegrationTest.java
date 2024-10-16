package com.barbershop.clients.dataacesslayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRepositoryIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUpDb(){
        clientRepository.deleteAll();
    }

    @Test
    public void whenClientExists_ReturnClientByClientId(){
        //arrange (set up test data)
        Client client1 = new Client(new Address("1234", "Saint-Constant", "QC", "Canada",
                "JS4 8N9"), "Alice", "Johnson", "alice@example.com",
                "748-107-9665");

        clientRepository.save(client1);
        //act
        Client client =
                clientRepository.findClientByClientIdentifier_ClientId(client1.getClientIdentifier().getClientId());

        //assert
        assertNotNull(client);
        assertEquals(client.getClientIdentifier(), client1.getClientIdentifier());
        assertEquals(client.getFirstName(), client1.getFirstName());
        assertEquals(client.getLastName(), client1.getLastName());
    }

    @Test
    public void whenClientDoesNotExist_ReturnNull(){
        Client savedClient= clientRepository.findClientByClientIdentifier_ClientId("1234");

        assertNull(savedClient);
    }
}