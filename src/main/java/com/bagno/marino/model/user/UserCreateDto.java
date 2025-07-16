package com.bagno.marino.model.user;

import com.bagno.marino.model.base.BaseCreateDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author Mario Arcomano
 */
@Getter
@Setter
public class UserCreateDto extends BaseCreateDto {

    private String name;
    private String lastname;
    private String username;
    private String email;
    private String countryCallingCode;
    private String phone;
    private String nationality;
    private String gender;

    public UserCreateDto() {
        super();
    }
}
