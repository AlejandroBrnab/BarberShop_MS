package com.barbershop.apigateway.presentationlayer.clients;

import com.barbershop.apigateway.domainserviceclient.clients.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponseModel extends RepresentationModel<ClientResponseModel> {

    private String clientId;
    private Address address;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
}
