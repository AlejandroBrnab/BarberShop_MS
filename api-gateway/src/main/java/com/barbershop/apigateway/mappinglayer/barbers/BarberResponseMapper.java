package com.barbershop.apigateway.mappinglayer.barbers;

import com.barbershop.apigateway.presentationlayer.barbers.BarberController;
import com.barbershop.apigateway.presentationlayer.barbers.BarberResponseModel;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface BarberResponseMapper {

    BarberResponseModel responseModelToResponseModel(BarberResponseModel barberResponseModel);

    List<BarberResponseModel> responseModelListToResponseModelList(List<BarberResponseModel> barberResponseModelList);

    @AfterMapping
    default void addLinks(@MappingTarget BarberResponseModel barberResponseModel){
        //customer link
        Link selfLink = linkTo(methodOn(BarberController.class).getBarberBySin(barberResponseModel.getSin())).withSelfRel();
        barberResponseModel.add(selfLink);

        Link allLinks = linkTo(methodOn(BarberController.class).getAllBarbers()).withRel("all barbers");
        barberResponseModel.add(allLinks);
    }
}
