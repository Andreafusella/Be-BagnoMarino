package com.bagno.marino.controller;

import com.bagno.marino.model.category.CategoryWithItemsDto;
import com.bagno.marino.model.item.ItemChangeAvailableDto;
import com.bagno.marino.model.item.ItemCreateDto;
import com.bagno.marino.model.item.ItemDto;
import com.bagno.marino.model.item.ItemUpdateDto;
import com.bagno.marino.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/item", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody ItemCreateDto data) {
        ItemDto response = itemService.save(data);
        return ResponseEntity.ok(response);
    }

    @PutMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody ItemUpdateDto data) {
        itemService.update(data);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(itemService.getAll());
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long itemId) {
        itemService.delete(itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoryWithSubItems(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getCategoryWithSubcategories(id));
    }

    @PutMapping("/available")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeAvailable(@RequestBody ItemChangeAvailableDto dto) {
        itemService.changeAvailable(dto);
        return ResponseEntity.ok().build();
    }
}
