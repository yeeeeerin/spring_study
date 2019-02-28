package com.example.jpatransactionandlock.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Address {

    private String city;

    private String street;

    private String zipCode;

}
