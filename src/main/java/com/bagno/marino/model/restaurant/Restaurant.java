package com.bagno.marino.model.restaurant;

import com.bagno.marino.model.admin.Admin;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private String address;
    private String phone;
    private String openingHours;

    @OneToOne
    private Admin admin;
}
