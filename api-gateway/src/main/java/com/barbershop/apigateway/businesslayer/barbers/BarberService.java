package com.barbershop.apigateway.businesslayer.barbers;

import com.barbershop.apigateway.presentationlayer.barbers.BarberRequestModel;
import com.barbershop.apigateway.presentationlayer.barbers.BarberResponseModel;

import java.util.List;

public interface BarberService {

    List<BarberResponseModel> getAllBarbers();

    BarberResponseModel getBarberBySin(String barberId);

    BarberResponseModel createBarber(BarberRequestModel barber);

    BarberResponseModel updateBarber(BarberRequestModel barber, String barberId);

    void deleteBarber(String barberId);
}
