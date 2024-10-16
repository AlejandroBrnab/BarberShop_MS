package com.barbershop.clients.businesslayer;

import com.barbershop.clients.presentationlayer.ClientRequestModel;
import com.barbershop.clients.presentationlayer.ClientResponseModel;

import java.util.List;

public interface ClientService {

    public List<ClientResponseModel> getAllClients();

    public ClientResponseModel getClientById(String clientId);

    public ClientResponseModel addClient(ClientRequestModel clientRequestModel);

    public ClientResponseModel updateClient(ClientRequestModel clientRequestModel, String clientId);

    public void deleteClient(String clientId);
}
