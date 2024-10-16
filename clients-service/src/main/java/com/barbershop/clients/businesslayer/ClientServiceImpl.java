package com.barbershop.clients.businesslayer;

import com.barbershop.clients.dataacesslayer.Client;
import com.barbershop.clients.dataacesslayer.ClientIdentifier;
import com.barbershop.clients.dataacesslayer.ClientRepository;
import com.barbershop.clients.mapperlayer.ClientRequestMapper;
import com.barbershop.clients.mapperlayer.ClientResponseMapper;
import com.barbershop.clients.presentationlayer.ClientRequestModel;
import com.barbershop.clients.presentationlayer.ClientResponseModel;
import com.barbershop.clients.utils.exceptions.InvalidPhoneNumberException;
import com.barbershop.clients.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientResponseMapper clientResponseMapper;
    private final ClientRequestMapper clientRequestMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientResponseMapper clientResponseMapper,
                             ClientRequestMapper clientRequestMapper) {
        this.clientRepository = clientRepository;
        this.clientResponseMapper = clientResponseMapper;
        this.clientRequestMapper = clientRequestMapper;
    }

    @Override
    public List<ClientResponseModel> getAllClients() {
        List<Client> clientList = clientRepository.findAll();
        return clientResponseMapper.entityListToResponseModel(clientList);
    }

    @Override
    public ClientResponseModel getClientById(String clientId) {
        Client client = clientRepository.findClientByClientIdentifier_ClientId(clientId);
        if (client == null) {
            throw new NotFoundException("Unknown client ID: " + clientId);
        }
        return clientResponseMapper.entityToResponseModel(client);
    }

    @Override
    public ClientResponseModel addClient(ClientRequestModel clientRequestModel) {
        Client client = clientRequestMapper.requestModelToEntity(clientRequestModel, new ClientIdentifier());
        if (clientRequestModel.getPhoneNumber().length() > 12){
            throw new InvalidPhoneNumberException("Phone number exceeds maximum length " + clientRequestModel.getPhoneNumber());
        }
        return clientResponseMapper.entityToResponseModel(clientRepository.save(client));
    }

    @Override
    public ClientResponseModel updateClient(ClientRequestModel clientRequestModel, String clientId) {
        Client foundClient = clientRepository.findClientByClientIdentifier_ClientId(clientId);
        if (foundClient == null) {
            throw new NotFoundException("Unknown client ID: " + clientId);
        }
        if (foundClient.getPhoneNumber().length() > 12){
            throw new InvalidPhoneNumberException("Phone number exceeds maximum length " + clientRequestModel.getPhoneNumber());
        }
        Client client = clientRequestMapper.requestModelToEntity(clientRequestModel,
                foundClient.getClientIdentifier());
        client.setId(foundClient.getId());
        return clientResponseMapper.entityToResponseModel(clientRepository.save(client));
    }

    @Override
    public void deleteClient(String clientId) {
        Client foundClient = clientRepository.findClientByClientIdentifier_ClientId(clientId);
        if (foundClient == null) {
            throw new NotFoundException("Unknown client ID: " + clientId);
        }
        clientRepository.delete(foundClient);
    }
}
