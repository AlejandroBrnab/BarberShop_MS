package com.barbershop.barbers.businesslayer;

import com.barbershop.barbers.presentationlayer.BarberRequestModel;
import com.barbershop.barbers.presentationlayer.BarberResponseModel;

import java.util.List;

public interface BarberService {

    public List<BarberResponseModel> getAllBarbers();

    public BarberResponseModel getBarberBySin(String sin);

    public BarberResponseModel addBarber(BarberRequestModel barberRequestModel);

    public BarberResponseModel updateBarber(BarberRequestModel barberRequestModel, String sin);

    public void deleteBarber(String sin);

//    public void updateAvailabilityToBarber(BarberRequestModel barberRequestModel, String sin);
}
