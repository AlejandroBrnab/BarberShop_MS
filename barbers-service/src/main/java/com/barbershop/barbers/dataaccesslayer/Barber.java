package com.barbershop.barbers.dataaccesslayer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "barbers")
@Data
@NoArgsConstructor
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private BarberIdentifier barberIdentifier;

    @Embedded
    private Address address;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "barber_specialties", joinColumns = @JoinColumn(name = "barber_id"))
    private List<Specialty> specialties;

    private String firstName;
    private String lastName;
    private String emailAddress;
    private BigDecimal salary;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Boolean isAvailable;


    public Barber(@NotNull Address address, @NotNull List<Specialty> specialties, @NotNull String firstName,
                  @NotNull String lastName, @NotNull String emailAddress, @NotNull BigDecimal salary,
                  @NotNull String phoneNumber, @NotNull LocalDate dateOfBirth, @NotNull Boolean isAvailable) {
        this.barberIdentifier = new BarberIdentifier();
        this.address = address;
        this.specialties = specialties;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.salary = salary;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.isAvailable = isAvailable;
    }
}
