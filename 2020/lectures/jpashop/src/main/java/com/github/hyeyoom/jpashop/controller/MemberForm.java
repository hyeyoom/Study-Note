package com.github.hyeyoom.jpashop.controller;

import com.github.hyeyoom.jpashop.domain.Address;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;

    public Address toAddress() {
        return new Address(city, street, zipcode);
    }
}
