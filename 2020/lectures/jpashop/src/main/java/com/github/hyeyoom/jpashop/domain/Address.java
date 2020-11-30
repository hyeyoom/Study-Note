package com.github.hyeyoom.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // jpa 스펙 상 엔티티나 임베디드 타입(VO)은 기본 생성자를 public이나 protected로 두어야 함
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
