package com.bagno.marino.repository;

import com.bagno.marino.model.itemAllergens.ItemAllergens;

import java.util.List;

public interface ItemAllergensRepository extends BaseRepository<ItemAllergens, Long> {
    List<ItemAllergens> findAllByItems_Id(Long id);
}
