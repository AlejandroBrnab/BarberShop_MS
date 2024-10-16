package com.barbershop.appointments.dataaccesslayer;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    Appointment findAppointmentByBarberModel_SinAndAppointmentIdentifier_AppointmentId(String sin, String appointmentId);

    List<Appointment> findAppointmentsByBarberModel_Sin(String sin);
}
