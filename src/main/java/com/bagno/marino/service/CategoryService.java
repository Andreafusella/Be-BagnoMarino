package com.bagno.marino.service;

import com.bagno.marino.exception.general.BadRequestException;
import com.bagno.marino.exception.general.EntityNotFoundException;
import com.bagno.marino.exception.general.IllegalArgumentException;
import com.bagno.marino.model.category.*;
import com.bagno.marino.model.item.Item;
import com.bagno.marino.model.item.ItemDto;
import com.bagno.marino.repository.CategoryRepository;
import com.bagno.marino.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ItemAllergensService itemAllergensService;

    private void validateCreateDto(CategoryCreateDto dto) {
        if (categoryRepository.existsByName(dto.getName())) throw new BadRequestException("Il nome della categoria già esiste");
        if (dto.getName() == null || dto.getName().length() > 100) throw new BadRequestException("Il nome della categoria non può essere lungo più di 100 caratteri");
        if (dto.getSubCategoryId() != -1) {
            if (!categoryRepository.existsById(dto.getSubCategoryId())) throw new BadRequestException("La categoria non esiste");
        }
        if (dto.getOrderIndex() != null && dto.getOrderIndex() < 0) throw new IllegalArgumentException("L'indice dell'ordine deve essere uguale o maggiore di 0");
    }

    private void validateDeleteDto(Long id) {

    }

    private void validateUpdateDto(CategoryUpdateDto dto) {
        Category category = categoryRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Categoria non trovata"));

        String normalizedName = category.getName().toLowerCase();

        Optional<Category> existingCategory = categoryRepository.findByNameIgnoreCase(normalizedName);
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(dto.getId())) {
            throw new BadRequestException("Esiste già una categorua con lo stesso nome");
        }
        if (dto.getName() == null || dto.getName().length() > 100) throw new BadRequestException("Il nome della categoria non può essere lungo più di 100 caratteri");
        if (dto.getSubCategoryId() != -1) {
            if (!categoryRepository.existsById(dto.getSubCategoryId())) throw new BadRequestException("La categoria non esiste");
        }
        if (dto.getOrderIndex() != null && dto.getOrderIndex() < 0) throw new IllegalArgumentException("L'indice dell'ordine deve essere uguale o maggiore di 0");
    }

    public void save(CategoryCreateDto dto) {
        validateCreateDto(dto);

        List<Category> categoriesToShift = categoryRepository.findByOrderIndexGreaterThanEqualOrderByOrderIndexAsc(dto.getOrderIndex());

        Integer orderIndex = dto.getOrderIndex();

        if (orderIndex == null) {
            Integer maxOrder = categoryRepository.findMaxOrderIndex().orElse(0);
            orderIndex = maxOrder + 1;
        } else {
            for (Category c : categoriesToShift) {
                c.setOrderIndex(c.getOrderIndex() + 1);
            }

            categoryRepository.saveAll(categoriesToShift);
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setIcon(dto.getIcon());
        category.setOrderIndex(orderIndex);

        if (dto.getSubCategoryId() != -1) {
            category.setParent(categoryRepository.findById(dto.getSubCategoryId()).orElse(null));
        }

        categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        validateDeleteDto(id);

        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria non trovata"));

        List<Item> items = itemRepository.findAllByCategory_Id(id);

        for (Item item : items) {
            itemAllergensService.deleteAllItemAllergensByItem(item);
        }

        itemService.deleteAllByCategory(id);

        List<Category> categoriesToUpdate = categoryRepository.findByParentAndOrderIndexGreaterThanOrderByOrderIndexAsc(category.getParent(), category.getOrderIndex());

        for (Category c : categoriesToUpdate) {
            c.setOrderIndex(c.getOrderIndex() - 1);
        }

        categoryRepository.saveAll(categoriesToUpdate);

        categoryRepository.deleteById(id);
    }

    @Transactional
    public void update(CategoryUpdateDto dto) {
        validateUpdateDto(dto);

        Category category = categoryRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria non trovata"));

        Category oldParent = category.getParent();
        Category newParent = (dto.getSubCategoryId() != -1)
                ? categoryRepository.findById(dto.getSubCategoryId()).orElse(null)
                : null;

        Integer oldIndex = category.getOrderIndex();
        Integer newIndex = dto.getOrderIndex() != null ? dto.getOrderIndex() : oldIndex;

        boolean parentChanged = !Objects.equals(oldParent, newParent);
        boolean indexChanged = !oldIndex.equals(newIndex);

        if (parentChanged) {
            List<Category> oldSiblings = oldParent == null
                    ? categoryRepository.findByParentIsNullAndOrderIndexGreaterThanOrderByOrderIndexAsc(oldIndex)
                    : categoryRepository.findByParentIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(oldParent.getId(), oldIndex);

            for (Category sibling : oldSiblings) {
                sibling.setOrderIndex(sibling.getOrderIndex() - 1);
            }
            categoryRepository.saveAll(oldSiblings);

            List<Category> newSiblings = newParent == null
                    ? categoryRepository.findByParentIsNullAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(newIndex)
                    : categoryRepository.findByParentIdAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(newParent.getId(), newIndex);

            for (Category sibling : newSiblings) {
                sibling.setOrderIndex(sibling.getOrderIndex() + 1);
            }
            categoryRepository.saveAll(newSiblings);

            category.setParent(newParent);
            category.setOrderIndex(newIndex);
            categoryRepository.save(category);

        } else if (indexChanged) {

            List<Category> siblings = newParent == null
                    ? categoryRepository.findByParentIsNullOrderByOrderIndexAsc()
                    : categoryRepository.findByParentIdOrderByOrderIndexAsc(newParent.getId());

            if (newIndex > oldIndex) {
                for (Category sibling : siblings) {
                    if (!sibling.getId().equals(category.getId())
                            && sibling.getOrderIndex() > oldIndex
                            && sibling.getOrderIndex() <= newIndex) {
                        sibling.setOrderIndex(sibling.getOrderIndex() - 1);
                    }
                }
            } else {
                for (Category sibling : siblings) {
                    if (!sibling.getId().equals(category.getId())
                            && sibling.getOrderIndex() >= newIndex
                            && sibling.getOrderIndex() < oldIndex) {
                        sibling.setOrderIndex(sibling.getOrderIndex() + 1);
                    }
                }
            }

            categoryRepository.saveAll(siblings);

            category.setOrderIndex(newIndex);
            categoryRepository.save(category);
        }

        category.setName(dto.getName());
        category.setIcon(dto.getIcon());

        categoryRepository.save(category);
    }



    public List<CategoryDto> getAllCategoryNotSubCategory() {
        List<Category> categories = categoryRepository.findByParentIsNullOrderByOrderIndexAsc();

        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            modelMapper.map(category, categoryDto);
            categoryDtos.add(categoryDto);
        }

        return categoryDtos;
    }

    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            modelMapper.map(category, categoryDto);
            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }

    public void updatePosition(List<CategoryUpdatePositionDto> dto) {
        for (CategoryUpdatePositionDto c : dto) {
            Category category = categoryRepository.findById(c.getId()).orElseThrow(() -> new EntityNotFoundException("Categoria non trovata"));

            category.setOrderIndex(c.getOrderIndex());
            categoryRepository.save(category);
        }
    }

    public CategoryUpdateDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria non trovata"));

        CategoryUpdateDto categoryUpdateDto = new CategoryUpdateDto();

        modelMapper.map(category, categoryUpdateDto);
        if (category.getParent() != null) {
            categoryUpdateDto.setSubCategoryId(category.getParent().getId());
        }

        return categoryUpdateDto;
    }
}
