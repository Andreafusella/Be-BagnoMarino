package com.bagno.marino.service;

import com.bagno.marino.exception.general.BadRequestException;
import com.bagno.marino.exception.general.EntityNotFoundException;
import com.bagno.marino.model.admin.Admin;
import com.bagno.marino.model.util.LoginRequestDto;
import com.bagno.marino.model.util.LoginResponseDto;
import com.bagno.marino.repository.AdminRepository;
import com.bagno.marino.service.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtService jwtService;

    private void validateLogin(LoginRequestDto dto) {
        //controllare se l'email è valida
    }

    public LoginResponseDto login(LoginRequestDto data) {

        validateLogin(data);
        Admin admin = adminRepository.findByEmail(data.getEmail()).orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (!admin.getPassword().equals(data.getPassword())) throw new BadRequestException("Credential incorrect");

        return new LoginResponseDto(jwtService.generateToken(admin.getId(), admin.getEmail(), admin.getRole()));
    }
}
