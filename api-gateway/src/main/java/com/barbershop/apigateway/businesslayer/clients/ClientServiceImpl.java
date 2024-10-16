package com.barbershop.apigateway.businesslayer.clients;

import com.barbershop.apigateway.domainserviceclient.clients.ClientServiceClient;
import com.barbershop.apigateway.mappinglayer.clients.ClientResponseMapper;
import com.barbershop.apigateway.presentationlayer.clients.ClientRequestModel;
import com.barbershop.apigateway.presentationlayer.clients.ClientResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientServiceClient clientServiceClient;
    private final ClientResponseMapper clientResponseMapper;

    public ClientServiceImpl(ClientServiceClient clientServiceClient, ClientResponseMapper clientResponseMapper) {
        this.clientServiceClient = clientServiceClient;
        this.clientResponseMapper = clientResponseMapper;
    }

    @Override
    public List<ClientResponseModel> getAllClients() {
        return clientResponseMapper.responseModelListToResponseModelList(clientServiceClient.getAllClients());
    }

    @Override
    public ClientResponseModel getClientByClientId(String clientId) {
        return clientResponseMapper.responseModelToResponseModel(clientServiceClient.getClientByClientId(clientId));
    }

    @Override
    public ClientResponseModel createClient(ClientRequestModel client) {
        return clientResponseMapper.responseModelToResponseModel(clientServiceClient.addClient(client));
    }

    @Override
    public ClientResponseModel updateClient(ClientRequestModel client, String clientId) {
        return clientResponseMapper.responseModelToResponseModel(clientServiceClient.updateClient(client, clientId));
    }

    @Override
    public void deleteClient(String clientId) {
        // Delegate the deletion operation to the BarberServiceClient
        clientServiceClient.deleteClient(clientId);
    }
}
