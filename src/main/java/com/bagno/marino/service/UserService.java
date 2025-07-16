package com.bagno.marino.service;

import com.bagno.marino.model.user.*;
import com.r2u.memonti.model.user.*;
import com.bagno.marino.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @author MarioArcomano
 */
@Service
public class UserService extends AbstractService<User, UserDto, UserCreateDto, UserUpdateDto, UserPageDto, Integer> {

    public enum Fields {
        name(String.class),
        lastname(String.class),
        username(String.class),
        email(String.class),
        countryCallingCode(String.class),
        phone(String.class),
        nationality(String.class),
        gender(String.class);

        private final Class valueClass;

        Fields(Class valueClass) {
            this.valueClass = valueClass;
        }

        public Class getValueClass() {
            return valueClass;
        }
    }

    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    protected void validateCreateDto(UserCreateDto userCreateDto) {

    }

    @Override
    protected void validateUpdateDto(UserUpdateDto userUpdateDto, User existing) {

    }

    @Override
    protected void validateDelete(Integer id) {

    }
}
