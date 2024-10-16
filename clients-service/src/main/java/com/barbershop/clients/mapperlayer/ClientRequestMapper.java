package com.barbershop.clients.mapperlayer;

import com.barbershop.clients.dataacesslayer.Client;
import com.barbershop.clients.dataacesslayer.ClientIdentifier;
import com.barbershop.clients.presentationlayer.ClientRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientRequestMapper {

    @Mapping(target = "id", ignore = true)
    Client requestModelToEntity(ClientRequestModel clientRequestModel, ClientIdentifier clientIdentifier);
}
