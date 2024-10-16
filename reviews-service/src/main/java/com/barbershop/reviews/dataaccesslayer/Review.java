package com.barbershop.reviews.dataaccesslayer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "reviews")
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private ReviewIdentifier reviewIdentifier;

    /*@Embedded
    private BarberIdentifier barberIdentifier;

    @Embedded
    private ClientIdentifier clientIdentifier;

    @Embedded
    private AppointmentIdentifier appointmentIdentifier;*/

    private String description;
    private Integer score;

    public Review(@NotNull String description, @NotNull Integer score) {
        this.reviewIdentifier = new ReviewIdentifier();
        this.description = description;
        this.score = score;
    }
}
