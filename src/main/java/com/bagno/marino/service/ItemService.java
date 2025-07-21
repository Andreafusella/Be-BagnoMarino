package com.bagno.marino.service;

import com.bagno.marino.exception.general.BadRequestException;
import com.bagno.marino.exception.general.IllegalArgumentException;
import com.bagno.marino.model.category.Category;
import com.bagno.marino.model.category.CategoryDto;
import com.bagno.marino.model.item.Item;
import com.bagno.marino.model.item.ItemCreateDto;
import com.bagno.marino.model.item.ItemDto;
import com.bagno.marino.model.itemAllergens.ItemAllergensCreateDto;
import com.bagno.marino.repository.AllergensRepository;
import com.bagno.marino.repository.CategoryRepository;
import com.bagno.marino.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AllergensRepository allergensRepository;

    @Autowired
    private ItemAllergensService itemAllergensService;

    @Autowired
    private ModelMapper modelMapper;

    private void validateCreateDto(ItemCreateDto dto) {
        String normalizedTitle = dto.getTitle().trim().toLowerCase();
        if (itemRepository.existsByNormalizedTitle(normalizedTitle)) throw new BadRequestException("Title already exists");
        if (!categoryRepository.existsByName(dto.getCategory())) throw new BadRequestException("Category does not exist");
        if (dto.getDescription() != null && dto.getDescription().length() > 40) throw new IllegalArgumentException("Description length must be less than 100 characters");
        if (dto.getPrice() == null || dto.getPrice() < 0) throw new IllegalArgumentException("Price must be greater than 0");
        for (Integer i : dto.getAllergensIds()) {
            if (!allergensRepository.existsById(i)) throw new BadRequestException("Allergen does not exist with id: " + i);
        }
    }

    private void validateDelete(Integer id) {
        if (!itemRepository.existsById(id)) throw new BadRequestException("Not found any item");
    }

    @Transactional
    public ItemDto save(ItemCreateDto dto) {
        validateCreateDto(dto);

        Category category = categoryRepository.findByName(dto.getCategory());
        Item item = new Item();
        item.setTitle(dto.getTitle().trim());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setCategory(category);

        Item itemSave = itemRepository.save(item);

        for (Integer i : dto.getAllergensIds()) {
            ItemAllergensCreateDto itemAllergensCreateDto = new ItemAllergensCreateDto();
            itemAllergensCreateDto.setItemId(itemSave.getId());
            itemAllergensCreateDto.setAllergenId(i);
            itemAllergensService.create(itemAllergensCreateDto);
        }

        ItemDto itemDto = new ItemDto();
        modelMapper.map(itemSave, itemDto);

        return itemDto;
    }

    public List<CategoryDto> getAll() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryDto> list = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());

            List<ItemDto> listItemDto = new ArrayList<>();
            for (Item i : category.getItems()) {
                ItemDto itemDto = new ItemDto();
                modelMapper.map(i, itemDto);

                itemDto.setAllergenes(itemAllergensService.getAllergensByItem(i));
                listItemDto.add(itemDto);
            }
            categoryDto.setItems(listItemDto);
            list.add(categoryDto);
        }
        return list;
    }

    @Transactional
    public void delete(Integer id) {
        validateDelete(id);

        itemRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllByCategory(Long categoryId) {
        List<Item> items = itemRepository.findAllByCategory_Id(categoryId);
        itemRepository.deleteAll(items);
    }
}
