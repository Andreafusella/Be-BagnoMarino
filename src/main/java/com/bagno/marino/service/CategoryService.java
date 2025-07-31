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

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ModelMapper modelMapper;

    private void validateCreateDto(CategoryCreateDto dto) {
        if (categoryRepository.existsByName(dto.getName())) throw new BadRequestException("Category name already exists");
        if (dto.getName() == null || dto.getName().length() > 30) throw new BadRequestException("Category name cannot be longer than 30 characters");
        if (dto.getSubCategoryId() != -1) {
            if (!categoryRepository.existsById(dto.getSubCategoryId())) throw new BadRequestException("Category does not exist");
        }
        if (dto.getOrderIndex() != null && dto.getOrderIndex() < 0) throw new IllegalArgumentException("Order index must be greater than 0");
    }

    private void validateDeleteDto(Long id) {

    }

    private void validateUpdateDto(CategoryUpdateDto dto) {
        if (categoryRepository.existsByName(dto.getName())) throw new BadRequestException("Category name already exists");
        if (dto.getName() == null || dto.getName().length() > 30) throw new BadRequestException("Category name cannot be longer than 30 characters");
        if (dto.getSubCategoryId() != -1) {
            if (!categoryRepository.existsById(dto.getSubCategoryId())) throw new BadRequestException("Category does not exist");
        }
        if (dto.getOrderIndex() != null && dto.getOrderIndex() < 0) throw new IllegalArgumentException("Order index must be greater than 0");
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

        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));

        itemService.deleteAllByCategory(id);

        List<Category> categoriesToUpdate = categoryRepository.findByParentAndOrderIndexGreaterThanOrderByOrderIndexAsc(category.getParent(), category.getOrderIndex());

        for (Category c : categoriesToUpdate) {
            c.setOrderIndex(c.getOrderIndex() - 1);
        }

        categoryRepository.saveAll(categoriesToUpdate);

        categoryRepository.deleteById(id);
    }

    public void update(CategoryUpdateDto dto) {
        validateUpdateDto(dto);

        Category category = categoryRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Not found category"));

        Category oldParent = category.getParent();
        Category newParent = categoryRepository.findById(dto.getSubCategoryId()).orElse(null); // può essere null

        boolean parentChanged = !Objects.equals(oldParent, newParent);
        boolean indexChanged = !category.getOrderIndex().equals(dto.getOrderIndex());

        if (parentChanged && oldParent != null) {
            List<Category> oldSiblings = categoryRepository.findAllByParentIdOrderByOrderIndexAsc(oldParent.getId());
            for (Category sibling : oldSiblings) {
                if (sibling.getOrderIndex() > category.getOrderIndex()) {
                    sibling.setOrderIndex(sibling.getOrderIndex() - 1);
                }
            }
            categoryRepository.saveAll(oldSiblings);
        }

        if (parentChanged || indexChanged) {
            List<Category> newSiblings = newParent == null
                    ? categoryRepository.findAllByParentIdIsNullOrderByOrderIndexAsc()
                    : categoryRepository.findAllByParentIdOrderByOrderIndexAsc(newParent.getId());

            int newIndex = dto.getOrderIndex();

            if (newIndex > newSiblings.size()) {
                newIndex = newSiblings.size();
            }

            for (Category sibling : newSiblings) {
                if (parentChanged || sibling.getId() != category.getId()) {
                    if (sibling.getOrderIndex() >= newIndex) {
                        sibling.setOrderIndex(sibling.getOrderIndex() + 1);
                    }
                }
            }

            categoryRepository.saveAll(newSiblings);
            category.setOrderIndex(newIndex);
        }

        if (!category.getName().equals(dto.getName())) {
            category.setName(dto.getName());
        }

        if (!category.getIcon().equals(dto.getIcon())) {
            category.setIcon(dto.getIcon());
        }

        if (parentChanged) {
            category.setParent(newParent);
        }

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
}
