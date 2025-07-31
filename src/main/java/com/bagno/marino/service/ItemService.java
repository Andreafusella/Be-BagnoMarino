package com.bagno.marino.service;

import com.bagno.marino.exception.general.BadRequestException;
import com.bagno.marino.exception.general.EntityNotFoundException;
import com.bagno.marino.exception.general.IllegalArgumentException;
import com.bagno.marino.model.allergens.Allergens;
import com.bagno.marino.model.allergens.AllergensDto;
import com.bagno.marino.model.category.Category;
import com.bagno.marino.model.category.CategoryWithItemsDto;
import com.bagno.marino.model.item.*;
import com.bagno.marino.model.itemAllergens.ItemAllergens;
import com.bagno.marino.model.itemAllergens.ItemAllergensCreateDto;
import com.bagno.marino.repository.AllergensRepository;
import com.bagno.marino.repository.CategoryRepository;
import com.bagno.marino.repository.ItemAllergensRepository;
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
    private ItemAllergensRepository itemAllergensRepository;

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

    }

    private void validateUpdate(ItemUpdateDto dto) {
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

    @Transactional
    public ItemDto save(ItemCreateDto dto) {
        validateCreateDto(dto);

        Category category = categoryRepository.findById(dto.getCategory()).get();

        Integer orderIndex = dto.getOrderIndex();

        if (orderIndex == null) {
            Integer maxOrder = itemRepository.findMaxOrderIndexByCategory(category).orElse(0);
            orderIndex = maxOrder + 1;
        } else {

            //se mette 100 e gli item sono 5 deve impostare 6 e non 100
            updateIndexItem(category, orderIndex);
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

    public ItemDto update(ItemUpdateDto dto) {
        validateUpdate(dto);

        Item item = itemRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Not found item"));

        ItemDto itemDto = new ItemDto();

        if (!dto.getName().equals(item.getName())) item.setName(dto.getName());
        if (!dto.getDescription().equals(item.getDescription())) item.setDescription(dto.getDescription());
        if (!dto.getPrice().equals(item.getPrice())) item.setPrice(dto.getPrice());
        if (!dto.getAvailable().equals(item.getAvailable())) item.setAvailable(dto.getAvailable());
        if (!dto.getSpecial().equals(item.getSpecial())) item.setSpecial(dto.getSpecial());
        if (!dto.getFrozen().equals(item.getFrozen())) item.setFrozen(dto.getFrozen());
        if (!dto.getOrderIndex().equals(item.getOrderIndex())) {
            int oldIndex = item.getOrderIndex();
            int newIndex = dto.getOrderIndex();
            Category category = item.getCategory();

            if (newIndex > oldIndex) {
                List<Item> itemsToShiftUp = itemRepository.findByCategoryAndOrderIndexBetweenOrderByOrderIndexAsc(
                        category, oldIndex + 1, newIndex
                );
                for (Item i : itemsToShiftUp) {
                    i.setOrderIndex(i.getOrderIndex() - 1);
                }
                itemRepository.saveAll(itemsToShiftUp);
            } else {
                List<Item> itemsToShiftDown = itemRepository.findByCategoryAndOrderIndexBetweenOrderByOrderIndexAsc(
                        category, newIndex, oldIndex - 1
                );
                for (Item i : itemsToShiftDown) {
                    i.setOrderIndex(i.getOrderIndex() + 1);
                }
                itemRepository.saveAll(itemsToShiftDown);
            }

            item.setOrderIndex(newIndex);
        }

        List<ItemAllergens> currentAllergens = itemAllergensRepository.findAllByItems_Id(item.getId());

        List<Long> currentIds = new ArrayList<>();
        for (ItemAllergens ia : currentAllergens) {
            currentIds.add(ia.getAllergens().getId());
        }

        List<Long> newIds = dto.getAllergensIds();

        for (ItemAllergens ia : currentAllergens) {
            if (!newIds.contains(ia.getAllergens().getId())) {
                itemAllergensRepository.delete(ia);
            }
        }

        for (Long id : newIds) {
            if (!currentIds.contains(id)) {
                Allergens allergen = allergensRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Allergen not found with id: " + id));

                ItemAllergens newItemAllergen = new ItemAllergens();
                newItemAllergen.setItems(item);
                newItemAllergen.setAllergens(allergen);
                itemAllergensRepository.save(newItemAllergen);
            }
        }

        if (!item.getCategory().getId().equals(dto.getCategory())) {
            Category oldCategory = item.getCategory();
            Category newCategory = categoryRepository.findById(dto.getCategory()).orElseThrow(() -> new EntityNotFoundException("New category not found"));

            List<Item> oldItemsToUpdate = itemRepository.findByCategoryAndOrderIndexGreaterThanOrderByOrderIndexAsc(
                    oldCategory, item.getOrderIndex()
            );
            for (Item i : oldItemsToUpdate) {
                i.setOrderIndex(i.getOrderIndex() - 1);
            }
            itemRepository.saveAll(oldItemsToUpdate);

            Integer newIndex = dto.getOrderIndex();
            if (newIndex == null) {
                Integer maxOrder = itemRepository.findMaxOrderIndexByCategory(newCategory).orElse(0);
                newIndex = maxOrder + 1;
            } else {
                List<Item> newItemsToShift = itemRepository.findByCategoryAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(
                        newCategory, newIndex
                );
                for (Item i : newItemsToShift) {
                    i.setOrderIndex(i.getOrderIndex() + 1);
                }
                itemRepository.saveAll(newItemsToShift);
            }

            item.setCategory(newCategory);
            item.setOrderIndex(newIndex);

            modelMapper.map(item, itemDto);
        }
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

        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item not found"));

        List<Item> itemsToUpdate = itemRepository.findByCategoryAndOrderIndexGreaterThanOrderByOrderIndexAsc(
                item.getCategory(), item.getOrderIndex()
        );

        for (Item i : itemsToUpdate) {
            i.setOrderIndex(i.getOrderIndex() - 1);
        }

        itemRepository.saveAll(itemsToUpdate);

        itemRepository.deleteById(id);
    }


    @Transactional
    public void deleteAllByCategory(Long categoryId) {
        List<Item> items = itemRepository.findAllByCategory_Id(categoryId);
        itemRepository.deleteAll(items);
    }

    public List<ItemWithCategoryDto> getAll() {
        List<Category> categories = categoryRepository.findAllByOrderByOrderIndexAsc();

        List<ItemWithCategoryDto> response = new ArrayList<>();
        for (Category c : categories) {
            ItemWithCategoryDto dto = new ItemWithCategoryDto();
            List<Item> items = itemRepository.findAllByCategory_IdOrderByOrderIndexAsc(c.getId());
            List<ItemDto> itemDtos = new ArrayList<>();

            for (Item i : items) {
                ItemDto itemDto = new ItemDto();
                modelMapper.map(i, itemDto);
                itemDto.setAllergenes(itemAllergensService.getAllergensByItem(i));

                itemDtos.add(itemDto);
            }

            dto.setCategory(c.getName());
            dto.setCategoryId(c.getId());
            dto.setCategoryIcon(c.getIcon());
            dto.setItems(itemDtos);

            response.add(dto);
        }
        return response;
    }

    public void changeAvailable(ItemChangeAvailableDto dto) {

        Item item = itemRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Not found item"));

        item.setAvailable(dto.getAvailable());
        itemRepository.save(item);
    }

    private void updateIndexItem(Category category, Integer orderIndex) {
        List<Item> itemsToShift = itemRepository.findByCategoryAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(category, orderIndex);

        for (Item i : itemsToShift) {
            i.setOrderIndex(i.getOrderIndex() + 1);
        }

        itemRepository.saveAll(itemsToShift);
    }

}
