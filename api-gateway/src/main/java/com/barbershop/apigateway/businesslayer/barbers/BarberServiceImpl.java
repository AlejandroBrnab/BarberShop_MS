package com.barbershop.apigateway.businesslayer.barbers;

import com.barbershop.apigateway.domainserviceclient.barbers.BarberServiceClient;
import com.barbershop.apigateway.mappinglayer.barbers.BarberResponseMapper;
import com.barbershop.apigateway.presentationlayer.barbers.BarberRequestModel;
import com.barbershop.apigateway.presentationlayer.barbers.BarberResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BarberServiceImpl implements BarberService {

    private final BarberServiceClient barberServiceClient;
    private final BarberResponseMapper barberResponseMapper;

    public BarberServiceImpl(BarberServiceClient barberServiceClient, BarberResponseMapper barberResponseMapper) {
        this.barberServiceClient = barberServiceClient;
        this.barberResponseMapper = barberResponseMapper;
    }

    @Override
    public List<BarberResponseModel> getAllBarbers() {
        return barberResponseMapper.responseModelListToResponseModelList(barberServiceClient.getAllBarbers());
    }

    @Override
    public BarberResponseModel getBarberBySin(String barberId) {
        return barberResponseMapper.responseModelToResponseModel(barberServiceClient.getBarberByBarberSin(barberId));
    }

    @Override
    public BarberResponseModel createBarber(BarberRequestModel barber) {
        return barberResponseMapper.responseModelToResponseModel(barberServiceClient.addBarber(barber));
    }

    @Override
    public BarberResponseModel updateBarber(BarberRequestModel barber, String barberId) {
        return barberResponseMapper.responseModelToResponseModel(barberServiceClient.updateBarber(barber, barberId));
    }

    @Override
    public void deleteBarber(String barberId) {
        // Delegate the deletion operation to the BarberServiceClient
        barberServiceClient.deleteBarber(barberId);
    }
}
