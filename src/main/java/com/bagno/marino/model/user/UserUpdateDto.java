package com.bagno.marino.model.user;

import com.bagno.marino.model.base.BaseUpdateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto extends BaseUpdateDto {

    private String name;
    private String lastname;
    private String username;
    private String countryCallingCode;
    private String phone;
    private String nationality;
    private String gender;

    public UserUpdateDto() {
        super();
    }
}
