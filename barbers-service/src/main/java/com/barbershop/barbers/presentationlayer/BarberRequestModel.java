package com.barbershop.barbers.presentationlayer;

import com.barbershop.barbers.dataaccesslayer.Address;
import com.barbershop.barbers.dataaccesslayer.Specialty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
/*@NoArgsConstructor*/
@Builder
@AllArgsConstructor
public class BarberRequestModel {

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
