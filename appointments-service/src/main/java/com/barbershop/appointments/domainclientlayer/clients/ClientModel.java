package com.barbershop.appointments.domainclientlayer.clients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class ClientModel {

    String clientId;
    String firstName;
    String lastName;
}
