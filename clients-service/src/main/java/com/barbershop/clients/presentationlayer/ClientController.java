package com.barbershop.clients.presentationlayer;

import com.barbershop.clients.businesslayer.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @GetMapping()
    public ResponseEntity<List<ClientResponseModel>> getAllClients(){
        return ResponseEntity.ok().body(clientService.getAllClients());
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientResponseModel> getClientById(@PathVariable String clientId){
        return ResponseEntity.ok().body(clientService.getClientById(clientId));
    }

    @PostMapping()
    public ResponseEntity<ClientResponseModel> addClient(@RequestBody ClientRequestModel clientRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.addClient(clientRequestModel));
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientResponseModel> updateClient(@RequestBody ClientRequestModel clientRequestModel,
                                                            @PathVariable String clientId){
        return ResponseEntity.ok().body(clientService.updateClient(clientRequestModel, clientId));
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable String clientId){
        clientService.deleteClient(clientId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
