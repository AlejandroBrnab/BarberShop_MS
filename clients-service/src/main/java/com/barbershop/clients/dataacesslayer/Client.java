package com.barbershop.clients.dataacesslayer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private ClientIdentifier clientIdentifier;

    @Embedded
    private Address address;

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;

    public Client(@NotNull Address address, @NotNull String firstName, @NotNull String lastName,
                  @NotNull String emailAddress, @NotNull String phoneNumber) {
        this.clientIdentifier = new ClientIdentifier();
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }
}
