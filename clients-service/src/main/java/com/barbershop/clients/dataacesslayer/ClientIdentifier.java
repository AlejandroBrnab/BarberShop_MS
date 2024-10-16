package com.barbershop.clients.dataacesslayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class ClientIdentifier {

    private String clientId;

    public ClientIdentifier(String clientId) {
        this.clientId = clientId;
    }

    public ClientIdentifier() {
        this.clientId = UUID.randomUUID().toString();
    }
}
