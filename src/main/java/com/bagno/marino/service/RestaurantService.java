package com.bagno.marino.service;

import com.bagno.marino.exception.general.EntityNotFoundException;
import com.bagno.marino.model.admin.Admin;
import com.bagno.marino.model.restaurant.*;
import com.bagno.marino.repository.AdminRepository;
import com.bagno.marino.repository.CategoryRepository;
import com.bagno.marino.repository.ItemRepository;
import com.bagno.marino.repository.RestaurantRepository;
import com.bagno.marino.service.util.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ModelMapper modelMapper;

//    private void validateCreateDto(RestaurantCreateDto dto) {
//        getAndCheckTokenAdmin();
//        if (dto.getName() == null || dto.getName().isBlank()) throw new IllegalArgumentException("Il nome del ristorante è obbligatorio.");
//        if (dto.getName().length() > 100) throw new IllegalArgumentException("Il nome del ristorante non può superare i 100 caratteri.");
//
//        if (dto.getDescription() == null || dto.getDescription().isBlank()) throw new IllegalArgumentException("La descrizione è obbligatoria.");
//        if (dto.getDescription().length() > 300) throw new IllegalArgumentException("La descrizione non può superare i 300 caratteri.");
//
//        if (dto.getAddress() == null || dto.getAddress().isBlank()) throw new IllegalArgumentException("L'indirizzo è obbligatorio.");
//        if (dto.getAddress().length() > 200) throw new IllegalArgumentException("L'indirizzo non può superare i 200 caratteri.");
//
//        if (dto.getPhone() == null || dto.getPhone().isBlank()) throw new IllegalArgumentException("Il numero di telefono è obbligatorio.");
//        if (!dto.getPhone().matches("^\\+?[0-9 ]{7,20}$")) throw new IllegalArgumentException("Il numero di telefono non è valido.");
//
//        if (dto.getOpeningHours() == null || dto.getOpeningHours().isBlank()) throw new IllegalArgumentException("L'orario di apertura è obbligatorio.");
//        if (dto.getOpeningHours().length() > 200) throw new IllegalArgumentException("L'orario di apertura non può superare i 200 caratteri.");
//    }

    private void validateUpdateDto(RestaurantUpdateDto dto) {
        getAndCheckTokenAdmin();

        if (dto.getName() != null) {
            if (dto.getName().isBlank()) throw new IllegalArgumentException("Il nome del ristorante non può essere vuoto.");
            if (dto.getName().length() > 100) throw new IllegalArgumentException("Il nome del ristorante non può superare i 100 caratteri.");
        }

        if (dto.getDescription() != null) {
            if (dto.getDescription().isBlank()) throw new IllegalArgumentException("La descrizione non può essere vuota.");
            if (dto.getDescription().length() > 300) throw new IllegalArgumentException("La descrizione non può superare i 300 caratteri.");
        }

        if (dto.getAddress() != null) {
            if (dto.getAddress().isBlank()) throw new IllegalArgumentException("L'indirizzo non può essere vuoto.");
            if (dto.getAddress().length() > 200) throw new IllegalArgumentException("L'indirizzo non può superare i 200 caratteri.");
        }

        if (dto.getPhone() != null) {
            if (dto.getPhone().isBlank()) throw new IllegalArgumentException("Il numero di telefono non può essere vuoto.");
            if (!dto.getPhone().matches("^\\+?[0-9 ]{7,20}$")) throw new IllegalArgumentException("Il numero di telefono non è valido.");
        }

        if (dto.getEmail() != null) {
            if (dto.getEmail().isBlank()) throw new IllegalArgumentException("L'email non può essere vuota");
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!dto.getEmail().matches(emailRegex)) throw new IllegalArgumentException("L'email non è valida");
        }
    }

//    public void save(RestaurantCreateDto data) {
//
//        validateCreateDto(data);
//
//        Integer adminId = jwtService.getCurrentAdminId();
//        Admin admin = adminRepository.findById(adminId).get();
//
//        Restaurant restaurant = new Restaurant();
//        restaurant.setName(data.getName());
//        restaurant.setDescription(data.getDescription());
//        restaurant.setAddress(data.getAddress());
//        restaurant.setPhone(data.getPhone());
//        restaurant.setOpeningHours(data.getOpeningHours());
//        restaurant.setAdmin(admin);
//
//        restaurantRepository.save(restaurant);
//
//    }

    public void update(RestaurantUpdateDto data) {

        validateUpdateDto(data);

        Integer adminId = getAndCheckTokenAdmin();

        Restaurant restaurant = restaurantRepository.findByAdmin_Id(adminId).orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        if (data.getName() != null) restaurant.setName(data.getName());
        if (data.getDescription() != null) restaurant.setDescription(data.getDescription());
        if (data.getAddress() != null) restaurant.setAddress(data.getAddress());
        if (data.getPhone() != null) restaurant.setPhone(data.getPhone());
        if (data.getOpeningTime() != null) restaurant.setOpeningTime(data.getOpeningTime());
        if (data.getClosingTime() != null) restaurant.setClosingTime(data.getClosingTime());
        if (data.getEmail() != null) restaurant.setEmail(data.getEmail());

        restaurantRepository.save(restaurant);
    }

    public RestaurantDto getInfo() {
        Integer adminId = getAndCheckTokenAdmin();

        Restaurant restaurant = restaurantRepository.findByAdmin_Id(adminId).orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        RestaurantDto restaurantDto = new RestaurantDto();
        modelMapper.map(restaurant, restaurantDto);
        return restaurantDto;
    }

    public RestaurantInfoItemCategoryDto getNumberItemAndCategory() {
        int itemAvailable = itemRepository.countByAvailableTrue();
        int itemNotAvailable = itemRepository.countByAvailableFalse();

        long category = categoryRepository.count();

        RestaurantInfoItemCategoryDto dto = new RestaurantInfoItemCategoryDto();
        dto.setItemAvailable(itemAvailable);
        dto.setItemNotAvailable(itemNotAvailable);
        dto.setCategory(category);

        return dto;
    }


    private Integer getAndCheckTokenAdmin() {
        Integer adminId = jwtService.getCurrentAdminId();
        if (adminId == null || !adminRepository.existsById(adminId)) throw new EntityNotFoundException("Error during request");

        return adminId;
    }
}
