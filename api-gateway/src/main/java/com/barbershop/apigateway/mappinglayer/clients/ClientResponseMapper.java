package com.barbershop.apigateway.mappinglayer.clients;

import com.barbershop.apigateway.presentationlayer.clients.ClientController;
import com.barbershop.apigateway.presentationlayer.clients.ClientResponseModel;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ClientResponseMapper {

    ClientResponseModel responseModelToResponseModel(ClientResponseModel clientResponseModel);

    List<ClientResponseModel> responseModelListToResponseModelList(List<ClientResponseModel> clientResponseModelList);

    @AfterMapping
    default void addLinks(@MappingTarget ClientResponseModel clientResponseModel){
        //customer link
        Link selfLink = linkTo(methodOn(ClientController.class).getClientById(clientResponseModel.getClientId())).withSelfRel();
        clientResponseModel.add(selfLink);

        Link allLinks = linkTo(methodOn(ClientController.class).getAllClients()).withRel("all clients");
        clientResponseModel.add(allLinks);
    }
}
