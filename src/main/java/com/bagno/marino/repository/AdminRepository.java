package com.bagno.marino.repository;

import com.bagno.marino.model.admin.Admin;

import java.util.Optional;

public interface AdminRepository extends BaseRepository<Admin, Integer>{
    Optional<Admin> findByEmail(String email);
}
