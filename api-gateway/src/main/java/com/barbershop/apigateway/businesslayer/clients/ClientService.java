package com.barbershop.apigateway.businesslayer.clients;

import com.barbershop.apigateway.presentationlayer.clients.ClientRequestModel;
import com.barbershop.apigateway.presentationlayer.clients.ClientResponseModel;

import java.util.List;

public interface ClientService {

    List<ClientResponseModel> getAllClients();

    ClientResponseModel getClientByClientId(String clientId);

    ClientResponseModel createClient(ClientRequestModel client);

    ClientResponseModel updateClient(ClientRequestModel client, String clientId);

    void deleteClient(String clientId);
}
