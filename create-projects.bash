#!/usr/bin/env bash
 
spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=barbers-service \
--package-name=com.barbershop.barbers \
--groupId=com.barbershop.employees \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
barbers-service
 
spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=clients-service \
--package-name=com.barbershop.clients \
--groupId=com.barbershop.clients \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
clients-service
 
spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=reviews-service \
--package-name=com.barbershop.reviews \
--groupId=com.barbershop.reviews \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
reviews-service

spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=appointments-service \
--package-name=com.barbershop.appointments \
--groupId=com.barbershop.appointments \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
appointments-service
 
spring init \
--boot-version=3.2.3 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=api-gateway \
--package-name=com.barbershop.apigateway \
--groupId=com.barbershop.apigateway \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
api-gateway