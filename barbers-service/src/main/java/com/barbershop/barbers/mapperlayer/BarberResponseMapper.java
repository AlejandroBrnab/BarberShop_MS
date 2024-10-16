package com.barbershop.barbers.mapperlayer;

import com.barbershop.barbers.dataaccesslayer.Barber;
import com.barbershop.barbers.presentationlayer.BarberResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface BarberResponseMapper {

    //la primera matchea con lo que esta dentro del metodo,la segunda matchea con la entity,
    // la tercera con el identifier, y la cuarta con la response model?
    @Mapping(expression = "java(barber.getBarberIdentifier().getSin())", target = "sin")
    @Mapping(expression = "java(barber.getIsAvailable())", target = "isAvailable")
    BarberResponseModel entityToResponseModel(Barber barber);

    List<BarberResponseModel> entityListToResponseModelList(List<Barber> barberList);

}
