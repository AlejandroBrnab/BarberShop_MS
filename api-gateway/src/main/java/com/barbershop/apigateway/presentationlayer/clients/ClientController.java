package com.barbershop.apigateway.presentationlayer.clients;

import com.barbershop.apigateway.businesslayer.clients.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientResponseModel>> getAllClients(){
        return ResponseEntity.ok().body(clientService.getAllClients());
    }

    @GetMapping(value = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientResponseModel> getClientById(@PathVariable String clientId){
        return ResponseEntity.ok().body(clientService.getClientByClientId(clientId));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<ClientResponseModel> addClient(@RequestBody ClientRequestModel clientRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(clientRequestModel));
    }

    @PutMapping(value = "{clientId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ClientResponseModel> updateClient(@RequestBody ClientRequestModel clientRequestModel,
                                                            @PathVariable String clientId){
        return ResponseEntity.ok().body(clientService.updateClient(clientRequestModel, clientId));
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void>deleteBarber(@PathVariable String clientId){
        clientService.deleteClient(clientId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
