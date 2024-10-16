package com.barbershop.barbers.businesslayer;

import com.barbershop.barbers.dataaccesslayer.Barber;
import com.barbershop.barbers.dataaccesslayer.BarberIdentifier;
import com.barbershop.barbers.dataaccesslayer.BarberRepository;
import com.barbershop.barbers.mapperlayer.BarberRequestMapper;
import com.barbershop.barbers.mapperlayer.BarberResponseMapper;
import com.barbershop.barbers.presentationlayer.BarberRequestModel;
import com.barbershop.barbers.presentationlayer.BarberResponseModel;
import com.barbershop.barbers.utils.exceptions.InvalidPhoneNumberException;
import com.barbershop.barbers.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BarberServiceImpl implements BarberService{

    private final BarberRepository barberRepository;
    private final BarberResponseMapper barberResponseMapper;
    private final BarberRequestMapper barberRequestMapper;

    public BarberServiceImpl(BarberRepository barberRepository, BarberResponseMapper barberResponseMapper,
                             BarberRequestMapper barberRequestMapper) {
        this.barberRepository = barberRepository;
        this.barberResponseMapper = barberResponseMapper;
        this.barberRequestMapper = barberRequestMapper;
    }

    @Override
    public List<BarberResponseModel> getAllBarbers() {
        List<Barber> barberList = barberRepository.findAll();
        return barberResponseMapper.entityListToResponseModelList(barberList);
    }

    @Override
    public BarberResponseModel getBarberBySin(String sin) {
        Barber barber = barberRepository.findBarberByBarberIdentifier_Sin(sin);

        if (barber == null) {
            throw new NotFoundException("Unknown barber SIN: " + sin);
        }
        return barberResponseMapper.entityToResponseModel(barber);
    }

    @Override
    public BarberResponseModel addBarber(BarberRequestModel barberRequestModel) {
        Barber barber = barberRequestMapper.requestModelToEntity(barberRequestModel, new BarberIdentifier());
        if (barberRequestModel.getPhoneNumber().length() > 12){
            throw new InvalidPhoneNumberException("Phone number exceeds maximum length " + barberRequestModel.getPhoneNumber());
        }
        return barberResponseMapper.entityToResponseModel(barberRepository.save(barber));
    }

    @Override
    public BarberResponseModel updateBarber(BarberRequestModel barberRequestModel, String sin) {
        Barber foundBarber = barberRepository.findBarberByBarberIdentifier_Sin(sin);
        if (foundBarber == null) {
            throw new NotFoundException("Unknown barber SIN: " + sin);
        }
        if (foundBarber.getPhoneNumber().length() > 12){
            throw new InvalidPhoneNumberException("Phone number exceeds maximum length");
        }
        Barber barber = barberRequestMapper.requestModelToEntity(barberRequestModel,
                foundBarber.getBarberIdentifier());
        barber.setId(foundBarber.getId());
        return barberResponseMapper.entityToResponseModel(barberRepository.save(barber));
    }

    @Override
    public void deleteBarber(String sin) {
        Barber foundBarber = barberRepository.findBarberByBarberIdentifier_Sin(sin);
        if (foundBarber == null) {
            throw new NotFoundException("Unknown barber SIN: " + sin);
        }
        barberRepository.delete(foundBarber);
    }

//    @Override
//    public void updateAvailabilityToBarber(BarberRequestModel barberRequestModel, String sin) {
//        Barber barber = barberRepository.findBarberByBarberIdentifier_Sin(sin);
//        if (barber == null) {
//            throw new NotFoundException("Invalid barberId: " + sin);
//        }
//
//        // Ensure the invariant by updating availability only if it has changed
//        if (!barber.getIsAvailable().equals(barberRequestModel.getIsAvailable())) {
//            barber.setIsAvailable(barberRequestModel.getIsAvailable());
//            barberRepository.save(barber);
//        }
//    }
}
