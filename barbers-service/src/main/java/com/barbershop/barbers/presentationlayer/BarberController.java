package com.barbershop.barbers.presentationlayer;

import com.barbershop.barbers.businesslayer.BarberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/barbers")
public class BarberController {
    private final BarberService barberService;

    public BarberController(BarberService barberService) {
        this.barberService = barberService;
    }

    @GetMapping()
    public ResponseEntity<List<BarberResponseModel>> getAllBarbers() {
        /*log.debug("Received request to get all barbers"); // Log the request*/
        return ResponseEntity.ok().body(barberService.getAllBarbers());
    }

    @GetMapping("/{barberId}")
    public ResponseEntity<BarberResponseModel> getBarberBySin(@PathVariable String barberId) {
        /*log.debug("Received request to get barber by ID: {}", barberId); // Log the request and ID*/
        return ResponseEntity.ok().body(barberService.getBarberBySin(barberId));
    }

    @PostMapping()
    public ResponseEntity<BarberResponseModel> addBarber(@RequestBody BarberRequestModel BarberRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(barberService.addBarber(BarberRequestModel));
    }

    @PutMapping("/{barberId}")
    public ResponseEntity<BarberResponseModel> updateBarber(@RequestBody BarberRequestModel BarberRequestModel,
                                                            @PathVariable String barberId) {
        return ResponseEntity.ok().body(barberService.updateBarber(BarberRequestModel, barberId));
    }

//    @PutMapping("/{barberId}/newAvailability")
//    public ResponseEntity<Void> updateBarberAvailability(@RequestBody BarberRequestModel barberRequestModel, @PathVariable String barberId) {
//        barberService.updateAvailabilityToBarber(barberRequestModel, barberId);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    @DeleteMapping("/{barberId}")
    public ResponseEntity<Void> deleteBarber(@PathVariable String barberId) {
        barberService.deleteBarber(barberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
