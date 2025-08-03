package com.bagno.marino.controller;

import com.bagno.marino.model.category.CategoryCreateDto;
import com.bagno.marino.model.category.CategoryUpdateDto;
import com.bagno.marino.model.category.CategoryUpdatePositionDto;
import com.bagno.marino.model.category.CategoryWithItemsDto;
import com.bagno.marino.model.item.ItemUpdatePositionDto;
import com.bagno.marino.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody CategoryCreateDto data) {
        categoryService.save(data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("categoryId") Long categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.ok().build();
    }

    @PutMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody CategoryUpdateDto dto) {
        categoryService.update(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<?> getAllCategoryNotSubCategory() {
        return ResponseEntity.ok(categoryService.getAllCategoryNotSubCategory());
    }

    @PutMapping("/position")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePosition(@RequestBody List<CategoryUpdatePositionDto> dto) {
        categoryService.updatePosition(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long categoryId) {
        return ResponseEntity.ok().build();
    }

}
