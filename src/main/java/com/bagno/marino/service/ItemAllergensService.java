package com.bagno.marino.service;

import com.bagno.marino.model.allergens.Allergens;
import com.bagno.marino.model.allergens.AllergensDto;
import com.bagno.marino.model.item.Item;
import com.bagno.marino.model.itemAllergens.ItemAllergens;
import com.bagno.marino.model.itemAllergens.ItemAllergensCreateDto;
import com.bagno.marino.repository.AllergensRepository;
import com.bagno.marino.repository.ItemAllergensRepository;
import com.bagno.marino.repository.ItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemAllergensService {

    @Autowired
    private ItemAllergensRepository itemAllergensRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AllergensRepository allergensRepository;

    @Autowired
    private ModelMapper modelMapper;

    private void validateCreateDto(ItemAllergensCreateDto dto) {}

    public void create(ItemAllergensCreateDto dto) {
        validateCreateDto(dto);

        Item item = itemRepository.findById(dto.getItemId()).get();
        Allergens allergens = allergensRepository.findById(dto.getAllergenId()).get();

        ItemAllergens itemAllergens = new ItemAllergens();
        itemAllergens.setAllergens(allergens);
        itemAllergens.setItems(item);

        itemAllergensRepository.save(itemAllergens);
    }

    public List<AllergensDto> getAllergensByItem(Item item) {
        List<ItemAllergens> itemAllergens = itemAllergensRepository.findAllByItems_Id(item.getId());

        List<AllergensDto> list = new ArrayList<>();
        for (ItemAllergens ia : itemAllergens) {
            Allergens a = ia.getAllergens();
            if (a != null) {
                AllergensDto dto = new AllergensDto();
                modelMapper.map(a, dto);
                list.add(dto);
            }
        }
        return list;
    }

    public void deleteAllItemAllergensByItem(Item item) {
        List<ItemAllergens> itemAllergens = itemAllergensRepository.findAllByItems_Id(item.getId());
        itemAllergensRepository.deleteAll(itemAllergens);
    }

}
