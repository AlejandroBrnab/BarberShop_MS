package com.barbershop.apigateway.domainserviceclient.appointments;

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
