package com.barbershop.apigateway.presentationlayer.barbers;

import com.barbershop.apigateway.domainserviceclient.barbers.Address;
import com.barbershop.apigateway.domainserviceclient.barbers.Specialty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BarberResponseModel extends RepresentationModel<BarberResponseModel> {

    private String sin;
    private Address address;
    private List<Specialty> specialties;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private BigDecimal salary;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Boolean isAvailable;
}
