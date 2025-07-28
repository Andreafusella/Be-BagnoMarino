package com.bagno.marino.service;

import com.bagno.marino.exception.general.BadRequestException;
import com.bagno.marino.exception.general.EntityNotFoundException;
import com.bagno.marino.model.admin.Admin;
import com.bagno.marino.model.util.LoginRequestDto;
import com.bagno.marino.model.util.LoginResponseDto;
import com.bagno.marino.repository.AdminRepository;
import com.bagno.marino.service.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtService jwtService;

    private void validateLogin(LoginRequestDto dto) {

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) throw new BadRequestException("Email non può essere vuota");
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) throw new BadRequestException("Password non può essere vuota");

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (!dto.getEmail().matches(emailRegex)) throw new BadRequestException("Email non valida");
    }

    public LoginResponseDto login(LoginRequestDto data) {
        validateLogin(data);

        Admin admin = adminRepository.findByEmail(data.getEmail())
                .orElse(null);

        if (admin == null || !BCrypt.checkpw(data.getPassword(), admin.getPassword())) {
            throw new BadRequestException("Credenziali non valide");
        }

        return new LoginResponseDto(jwtService.generateToken(admin.getId(), admin.getEmail(), admin.getRole()));
    }
}
