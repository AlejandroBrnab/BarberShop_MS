package com.barbershop.barbers.mapperlayer;

import com.barbershop.barbers.dataaccesslayer.Barber;
import com.barbershop.barbers.dataaccesslayer.BarberIdentifier;
import com.barbershop.barbers.presentationlayer.BarberRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BarberRequestMapper {

    @Mapping(target = "id", ignore = true)
    Barber requestModelToEntity(BarberRequestModel barberRequestModel, BarberIdentifier barberIdentifier);
}
