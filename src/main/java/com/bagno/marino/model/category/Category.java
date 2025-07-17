package com.bagno.marino.model.category;

import com.bagno.marino.model.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Item> items;
}
