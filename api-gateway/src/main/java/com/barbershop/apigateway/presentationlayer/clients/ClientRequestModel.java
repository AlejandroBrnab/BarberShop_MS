package com.barbershop.apigateway.presentationlayer.clients;

import com.barbershop.apigateway.domainserviceclient.clients.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
/*@NoArgsConstructor*/
@Builder
@AllArgsConstructor
public class ClientRequestModel {

    private Address address;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
}
