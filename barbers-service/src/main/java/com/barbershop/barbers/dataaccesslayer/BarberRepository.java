package com.barbershop.barbers.dataaccesslayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberRepository extends JpaRepository<Barber, Integer> {

    Barber findBarberByBarberIdentifier_Sin(String sin);
}
