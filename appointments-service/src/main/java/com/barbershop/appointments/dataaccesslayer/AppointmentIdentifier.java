package com.barbershop.appointments.dataaccesslayer;

import lombok.Getter;
import org.springframework.stereotype.Indexed;

import java.util.UUID;

@Getter
@Indexed
public class AppointmentIdentifier {

    private String appointmentId;

    public AppointmentIdentifier(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public AppointmentIdentifier(){
        this.appointmentId = UUID.randomUUID().toString();
    }
}
