package com.barbershop.clients.mapperlayer;

import com.barbershop.clients.dataacesslayer.Client;
import com.barbershop.clients.presentationlayer.ClientResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientResponseMapper {

    @Mapping(expression = "java(client.getClientIdentifier().getClientId())", target = "clientId")
    ClientResponseModel entityToResponseModel(Client client);

    List<ClientResponseModel> entityListToResponseModel(List<Client> clientList);

   /* @AfterMapping
    default void addLinks(@MappingTarget ClientResponseModel model, Client client){
        //self link
        Link selfLink = linkTo(methodOn(ClientController.class).getClientById(model.getClientId())).withSelfRel();
        model.add(selfLink);

        //all clients
        Link clientsLink = linkTo(methodOn(ClientController.class).getAllClients()).withRel("All clients");
        model.add(clientsLink);
    }*/
}
