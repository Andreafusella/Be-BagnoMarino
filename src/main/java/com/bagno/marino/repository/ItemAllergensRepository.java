package com.bagno.marino.repository;

import com.bagno.marino.model.itemAllergens.ItemAllergens;

import java.util.List;

public interface ItemAllergensRepository extends BaseRepository<ItemAllergens, Integer> {
    List<ItemAllergens> findAllByItems_Id(Integer id);
}
