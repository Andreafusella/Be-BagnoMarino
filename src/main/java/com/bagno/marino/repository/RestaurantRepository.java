package com.bagno.marino.repository;

import com.bagno.marino.model.restaurant.Restaurant;

import java.util.Optional;

public interface RestaurantRepository extends BaseRepository<Restaurant, Integer> {

    Optional<Restaurant> findByAdmin_Id(Integer adminId);
}
