package com.bagno.marino.service;

import com.bagno.marino.exception.general.BadRequestException;
import com.bagno.marino.exception.general.IllegalArgumentException;
import com.bagno.marino.model.category.Category;
import com.bagno.marino.model.category.CategoryWithItemsDto;
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
        String normalizedTitle = dto.getName().toLowerCase();
        if (itemRepository.existsByNormalizedTitle(normalizedTitle)) throw new BadRequestException("Title already exists");

        if (!categoryRepository.existsById(dto.getCategory())) throw new BadRequestException("Category does not exist");
        if (dto.getDescription() != null && dto.getDescription().length() > 40) throw new IllegalArgumentException("Description length must be less than 40 characters");
        if (dto.getPrice() == null || dto.getPrice() < 0) throw new IllegalArgumentException("Price must be greater than 0");
        for (Long i : dto.getAllergensIds()) {
            if (!allergensRepository.existsById(i)) throw new BadRequestException("Allergen does not exist with id: " + i);
        }
        if (dto.getOrderIndex() != null && dto.getOrderIndex() < 0) throw new IllegalArgumentException("Order index must be greater than 0");
    }

    private void validateDelete(Long id) {
        if (!itemRepository.existsById(id)) throw new BadRequestException("Not found any item");
    }

    @Transactional
    public ItemDto save(ItemCreateDto dto) {
        validateCreateDto(dto);

        Category category = categoryRepository.findById(dto.getCategory()).get();

        Integer orderIndex = dto.getOrderIndex();

        //impostare anche se è congelato o no
        if (orderIndex == null) {
            Integer maxOrder = itemRepository.findMaxOrderIndexByCategory(category).orElse(0);
            orderIndex = maxOrder + 1;
        } else {
            List<Item> itemsToShift = itemRepository.findByCategoryAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(category, orderIndex);

            for (Item i : itemsToShift) {
                i.setOrderIndex(i.getOrderIndex() + 1);
            }

            itemRepository.saveAll(itemsToShift);
        }

        Item item = new Item();
        modelMapper.map(dto, item);
        item.setCategory(category);
        item.setOrderIndex(orderIndex);


        Item itemSaved = itemRepository.save(item);

        for (Long allergenId : dto.getAllergensIds()) {
            ItemAllergensCreateDto allergenDto = new ItemAllergensCreateDto();
            allergenDto.setItemId(itemSaved.getId());
            allergenDto.setAllergenId(allergenId);
            itemAllergensService.create(allergenDto);
        }

        ItemDto itemDto = new ItemDto();
        modelMapper.map(itemSaved, itemDto);
        return itemDto;
    }

    public CategoryWithItemsDto getCategoryWithSubcategories(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));

        CategoryWithItemsDto dto = new CategoryWithItemsDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setIcon(category.getIcon());

        List<Item> items = itemRepository.findAllByCategory_IdAndAvailableTrueOrderByOrderIndexAsc(categoryId);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = new ItemDto();
            modelMapper.map(item, itemDto);
            itemDto.setCategoryName(item.getCategory().getName());
            itemDto.setAllergenes(itemAllergensService.getAllergensByItem(item));
            itemDtos.add(itemDto);
        }
        dto.setItems(itemDtos);

        List<Category> subcategories = categoryRepository.findAllByParentIdOrderByOrderIndexAsc(categoryId);
        List<CategoryWithItemsDto> subDtos = new ArrayList<>();
        for (Category sub : subcategories) {
            CategoryWithItemsDto subDto = getCategoryWithSubcategories(sub.getId());
            subDtos.add(subDto);
        }
        dto.setSubcategories(subDtos);

        return dto;
    }

    @Transactional
    public void delete(Long id) {
        validateDelete(id);

        itemRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllByCategory(Long categoryId) {
        List<Item> items = itemRepository.findAllByCategory_Id(categoryId);
        itemRepository.deleteAll(items);
    }

    private void updateIndexItems() {

    }
}
