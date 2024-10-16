package com.barbershop.clients.presentationlayer;

import com.barbershop.clients.dataacesslayer.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponseModel /*extends RepresentationModel<ClientResponseModel>*/ {

    private String clientId;
    private Address address;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
}
