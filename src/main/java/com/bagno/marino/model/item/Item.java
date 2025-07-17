package com.bagno.marino.model.item;

import com.bagno.marino.model.base.BaseEntity;
import com.bagno.marino.model.category.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Double price;

    @ManyToOne
    private Category category;
}
