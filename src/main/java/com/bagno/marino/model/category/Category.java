package com.bagno.marino.model.category;

import com.bagno.marino.model.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String icon;

    private String name;

    private Integer orderIndex;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Item> items;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> subcategories = new ArrayList<>();
}
