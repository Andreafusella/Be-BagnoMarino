package com.bagno.marino.service;

import com.bagno.marino.exception.general.BadRequestException;
import com.bagno.marino.model.category.Category;
import com.bagno.marino.model.category.CategoryCreateDto;
import com.bagno.marino.repository.CategoryRepository;
import com.bagno.marino.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemService itemService;

    private void validateCreateDto(CategoryCreateDto dto) {
        if (categoryRepository.existsByName(dto.getName())) throw new BadRequestException("Category name already exists");
        if (dto.getName() == null || dto.getName().length() > 30) throw new BadRequestException("Category name cannot be longer than 30 characters");
    }

    private void validateDeleteDto(Long id) {
        if (!categoryRepository.existsById(id)) throw new BadRequestException("Category does not exist");
    }

    public void save(CategoryCreateDto dto) {
        validateCreateDto(dto);

        Category category = new Category();
        category.setName(dto.getName());
        category.setIcon(dto.getIcon());

        categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        validateDeleteDto(id);
        itemService.deleteAllByCategory(id);

        categoryRepository.deleteById(id);
    }

    public List<String> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        List<String> categoriesName = new ArrayList<>();

        for (Category c : categories) {
            categoriesName.add(c.getName());
        }

        return categoriesName;
    }
}
