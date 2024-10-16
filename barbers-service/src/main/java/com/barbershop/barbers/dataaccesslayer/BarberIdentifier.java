package com.barbershop.barbers.dataaccesslayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class BarberIdentifier {

    private String sin;

    public BarberIdentifier(){
        this.sin = UUID.randomUUID().toString();
    }

    public BarberIdentifier(String sin) {
        this.sin = sin;
    }
}
