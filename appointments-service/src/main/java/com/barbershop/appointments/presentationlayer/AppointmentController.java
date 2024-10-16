package com.barbershop.appointments.presentationlayer;

import com.barbershop.appointments.businesslayer.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/barbers/{barberId}/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping()
    public ResponseEntity<List<AppointmentResponseModel>> getAllAppointmentsForBarber(@PathVariable String barberId) {
        return ResponseEntity.ok().body(appointmentService.getAllAppointmentsForBarber(barberId));
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseModel> getBarberAppointmentByAppointmentId(@PathVariable String barberId,
                                                                                        @PathVariable String appointmentId) {
        return ResponseEntity.ok().body(appointmentService.getBarberAppointmentByAppointmentId(barberId, appointmentId));
    }

    @PostMapping()
    public ResponseEntity<AppointmentResponseModel> addAppointmentToBarber(@RequestBody AppointmentRequestModel
                                                                                       appointmentRequestModel,
                                                                           @PathVariable String barberId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.addAppointmentToBarber(appointmentRequestModel,
                barberId));
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseModel> updateAppointment(@RequestBody AppointmentRequestModel
                                                                                  appointmentRequestModel,
                                                            @PathVariable String barberId, @PathVariable String appointmentId) {
        return ResponseEntity.ok().body(appointmentService.updateAppointmentToBarber(appointmentRequestModel, barberId,
                appointmentId));
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable String barberId, @PathVariable String appointmentId) {
        appointmentService.deleteAppointmentToBarber(barberId, appointmentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
