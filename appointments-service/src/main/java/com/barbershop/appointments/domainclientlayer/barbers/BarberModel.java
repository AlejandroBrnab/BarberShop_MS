package com.barbershop.appointments.domainclientlayer.barbers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class BarberModel {

    String sin;
    String firstName;
    String lastName;
    Boolean isAvailable;
}
