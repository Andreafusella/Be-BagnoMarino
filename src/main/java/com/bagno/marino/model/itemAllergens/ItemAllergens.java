package com.bagno.marino.model.itemAllergens;

import com.bagno.marino.model.allergens.Allergens;
import com.bagno.marino.model.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ItemAllergens {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Allergens allergens;

    @ManyToOne
    private Item items;
}
