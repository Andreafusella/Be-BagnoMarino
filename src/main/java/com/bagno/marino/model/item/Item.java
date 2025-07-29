package com.bagno.marino.model.item;

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

    private String name;
    private String description;
    private Double price;
    private Integer orderIndex;
    private Boolean available;
    private Boolean special;
    private Boolean frozen;


    @ManyToOne
    private Category category;
}
