package com.barbershop.apigateway.presentationlayer.barbers;

import com.barbershop.apigateway.businesslayer.barbers.BarberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BarberResponseModel>> getAllBarbers(){
        /*log.debug("Received request to get all barbers"); // Log the request*/
        return ResponseEntity.ok().body(barberService.getAllBarbers());
    }

    @GetMapping(value = "/{barberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BarberResponseModel> getBarberBySin(@PathVariable String barberId){
        /*log.debug("Received request to get barber by ID: {}", barberId); // Log the request and ID*/
        return ResponseEntity.ok().body(barberService.getBarberBySin(barberId));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<BarberResponseModel> addBarber(@RequestBody BarberRequestModel barberRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(barberService.createBarber(barberRequestModel));
    }

    @PutMapping(value = "{barberId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<BarberResponseModel> updateBarber(@RequestBody BarberRequestModel barberRequestModel,
                                                                    @PathVariable String barberId){
        return ResponseEntity.ok().body(barberService.updateBarber(barberRequestModel, barberId));
    }

    @DeleteMapping("/{barberId}")
    public ResponseEntity<Void>deleteBarber(@PathVariable String barberId){
        barberService.deleteBarber(barberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
