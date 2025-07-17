package com.bagno.marino.controller;

import com.bagno.marino.model.category.CategoryDto;
import com.bagno.marino.model.item.ItemCreateDto;
import com.bagno.marino.model.item.ItemDto;
import com.bagno.marino.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/item", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody ItemCreateDto data) {
        ItemDto response = itemService.save(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<?> getAll() {
        List<CategoryDto> response = itemService.getAll();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> delete(@PathVariable Long itemId) {
        itemService.delete(itemId);
        return ResponseEntity.ok().build();
    }

//    @GetMapping()
//    public ResponseEntity<?> getById(@RequestParam Long id) {}
}
