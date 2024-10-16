package com.barbershop.barbers.dataaccesslayer;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Specialty {
    private String specialty;

    public Specialty(@NotNull String specialty) {
        this.specialty = specialty;
    }
}
