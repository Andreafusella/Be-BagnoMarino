package com.bagno.marino.service;

import com.bagno.marino.model.category.Category;
import com.bagno.marino.model.category.CategoryCreateDto;
import com.bagno.marino.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private void validateCreateDto(CategoryCreateDto dto) {
        if (categoryRepository.existsByName(dto.getName())) throw new IllegalArgumentException("Category name already exists");
        if (dto.getName() == null || dto.getName().length() > 30) throw new IllegalArgumentException("Category name cannot be longer than 30 characters");
    }

    public void save(CategoryCreateDto dto) {
        validateCreateDto(dto);

        Category category = new Category();
        category.setName(dto.getName());

        categoryRepository.save(category);
    }
}
