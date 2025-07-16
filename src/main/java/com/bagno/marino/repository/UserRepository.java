package com.bagno.marino.repository;

import com.bagno.marino.model.user.User;
import org.springframework.stereotype.Repository;

/**
 * @author MarioArcomano
 */
@Repository
public interface UserRepository extends BaseRepository<User, Integer> {
}
