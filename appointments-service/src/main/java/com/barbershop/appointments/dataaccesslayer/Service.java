package com.barbershop.appointments.dataaccesslayer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class Service {

    private String type;

    public Service(String type) {
        this.type = type;
    }
}
