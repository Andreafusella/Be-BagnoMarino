package com.bagno.marino.service;

import com.bagno.marino.model.allergens.Allergens;
import com.bagno.marino.model.item.Item;
import com.bagno.marino.model.itemAllergens.ItemAllergens;
import com.bagno.marino.model.itemAllergens.ItemAllergensCreateDto;
import com.bagno.marino.repository.AllergensRepository;
import com.bagno.marino.repository.ItemAllergensRepository;
import com.bagno.marino.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemAllergensService {

    @Autowired
    private ItemAllergensRepository itemAllergensRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AllergensRepository allergensRepository;

    private void validateCreateDto(ItemAllergensCreateDto dto) {
        // per ora non serve
    }

    public void create(ItemAllergensCreateDto dto) {
        validateCreateDto(dto);

        Item item = itemRepository.findById(dto.getItemId()).get();
        Allergens allergens = allergensRepository.findById(dto.getAllergenId()).get();

        ItemAllergens itemAllergens = new ItemAllergens();
        itemAllergens.setAllergens(allergens);
        itemAllergens.setItems(item);

        itemAllergensRepository.save(itemAllergens);
    }
}
